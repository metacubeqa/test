package softphone.cases.callFlow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CreateLeadMultiMatch extends SoftphoneBase{
	
	String newCreateLead = null;
	
	@BeforeClass(groups={"Regression", "Product Sanity", "ExludeForProd"})
	public void createMultiMatchContact(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	
		String existingContact	= null;
		String firstName	 	= CONFIG.getProperty("qa_user_2_name");
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);
		
		// Deleting contact if exist
		if (callScreenPage.isCallerUnkonwn(driver3)) {
			System.out.println("adding contact");
			callScreenPage.addCallerAsContact(driver3, CONFIG.getProperty("qa_user_2_name"), CONFIG.getProperty("contact_account_name"));
			existingContact = firstName + " Automation";
		} else {
			existingContact = callScreenPage.getCallerName(driver3);
		}
		
		// Calling from Agent's SoftPhone
		reloadSoftphone(driver2);
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.hangupActiveCall(driver2);
		seleniumBase.idleWait(5);

		// Updating contact to add to existing contact
		callScreenPage.clickOnUpdateDetailLink(driver3);
		callScreenPage.addCallerToExistingContact(driver3, existingContact);
		
		//search untill caller is multiple
		softPhoneContactsPage.searchUntilContacIsMultiple(driver3, CONFIG.getProperty("prod_user_1_number"));
	}
	
	@BeforeMethod(groups={"Regression", "Product Sanity", "ExludeForProd"})
	public void enableMultiMatchSettings(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	}
	
	@Test(groups={"Regression"})
	public void multi_contact_restrict_campaign_calls()
	{
		System.out.println("Test case --multi_contact_restrict_campaign_calls-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//enable create lead only for sfdc campaign calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//Open Salesforce campaign page and get number of leads count
		sfCampaign.openSalesforceCampaignPage(driver4);
		int leadCounts = Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//calling to agent directly so that no new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//verify that caller is multiple
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//verfy that no new lead has been created
		sfCampaign.openSalesforceCampaignPage(driver4);
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts);   
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//calling again to agent with campaign call flow so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;

		//verfy that new user has been created
		sfCampaign.openSalesforceCampaignPage(driver4);
		assertTrue(callerName.contains(sfCampaign.getFirstContactName(driver4).get(0)) && callerName.contains(sfCampaign.getFirstContactName(driver4).get(1)));
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts + 1);   
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void multi_contact_campaign_off_unanswered_on_decline()
	{
		System.out.println("Test case --multi_lead_campaign_off_unanswered_on_decline-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// enable create lead only for unanswered call
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		// calling again to agent so that no new lead gets created
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//get leads name
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//verfy that new user has been added
		callScreenPage.selectFirstContactFromMultiple(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		seleniumBase.idleWait(2);
		System.out.println("hanging up with caller");
		softPhoneCalling.declineCall(driver4);
		
		//hangup call from the caller
		seleniumBase.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(5);
		reloadSoftphone(driver4);

		// verify voicemail is present for the call received
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		seleniumBase.idleWait(2);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
		
		//calling again to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//verify that the caller is multiple yet and no new lead has been created
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//decline the call
		softPhoneCalling.declineCall(driver4);
		
		//hangup call from the caller and verify that new lead has been created with new name on call history page
		softPhoneCalling.hangupIfInActiveCall(driver2);
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("qa_user_2_name")));
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("prod_user_1_name")));
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		
		//Verify the leads name on call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("qa_user_2_name")));
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("prod_user_1_name")));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void multi_contact_campaign_on_unanswered_on()
	{
		System.out.println("Test case --multi_lead_campaign_on_unanswered_on-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//enable both lead creation on unanswered and through campaign calls only setting
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//Open Salesforce campaign page and get number of leads count
		sfCampaign.openSalesforceCampaignPage(driver4);
		int leadCounts = Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//calling to agent through campaign all flow
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		
		//verify that caller is multiple till now
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//connect the call and then hang it up
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify that no new lead is created
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), "Multiple");
		
		//verify that lead count is not increased in sfdc campaign
		sfCampaign.openSalesforceCampaignPage(driver4);
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts);   
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		
		//verify that no new lead is created yet
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//verify that call has been ignored
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);
		
		//hangup call from the caller and verify that new lead has been created
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		
		//verify lead name on call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("qa_user_2_name")));
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("prod_user_1_name")));
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains("Multiple"));
		
		//verify that lead count is increased on sfdc campaign
		sfCampaign.openSalesforceCampaignPage(driver4);
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts + 1);   
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_match_setting_off()
	{
		System.out.println("Test case --multi_match_setting_off-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");

		//disable lead creation on Multi Match contact and verify unanswered and SFDC campaign checkbox are disabled
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		assertTrue(accountIntelligentDialerTab.isCreateLeadSFDCCampaignDisabled(driver1));
		assertTrue(accountIntelligentDialerTab.isCreateLeadUnansweredCallDisabled(driver1));
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		
		//verify that caller is multiple
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Pikcup the call and then disconnect it
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify that new lead is created and verify it's detail
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_unanswered_disabled_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_unanswered_disabled-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");

		//enable create lead create for multi match contact and keep the checkboxes unchecked
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to agent on campaign call flow so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
	
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that new lead has been created and verify it's details
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void multi_contact_campaign_unanswered_disabled()
	{
		System.out.println("Test case --multi_contact_campaign_unanswered_disabled-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");

		//enable create lead create for multi match contact and keep the checkboxes unchecked
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to agent directly so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));

		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that new lead has been created and verify it's details
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_unanswered_disabled_queue_call()
	{
		System.out.println("Test case --multi_contact_campaign_unanswered_disabled_queue_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
			
	    //Subscribe to the queue
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

	    //enable create lead create for multi match contact and keep the checkboxes unchecked
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
	    accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the queue
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
		
		//pickup the call from the queue
		seleniumBase.idleWait(10);
		softPhoneCallQueues.pickCallFromQueue(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Verify that lead has been created
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_on_unanswered_off()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_off-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//enable create lead only for sfdc campaign calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to agent directly
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//verify that new lead is not created yet
		seleniumBase.idleWait(5);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void multi_contact_campaign_on_unanswered_off_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_off_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
			
		//enable create lead on multi match contact setting only and keep other boxes unchecked
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling again to through campaign call flow
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));

		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that new lead is created and verify it's details
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_on_unanswered_off_queue_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_off_queue_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
			
	    //Subscribe to the queue
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

		//enable create lead only for sfdc campaign calls
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the queue
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
		
		//pickup the call
		softPhoneCallQueues.pickCallFromQueue(driver4);
		seleniumBase.idleWait(5);
		
		//verify that no new lead is created in between the call
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority", "Product Sanity", "ExludeForProd"})
	public void multi_contact_campaign_on_unanswered_off_queue_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_off_queue_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
			
	    //subscribe to the queu
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

		//enable create lead only for sfdc campaign calls
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
	    accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to agent through campaign call flow so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_camp_call_flow_call_queue_smart_number"));
		
		//pickup the call
		softPhoneCallQueues.pickCallFromQueue(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Verify that new lead has been created and verify it's details
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_miss_call()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_miss_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
		
		// enable create lead only for unanswered call
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		// calling to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//verify that while the call is ringing no new lead is created
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Hangup the call call after some time
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);
		
		//hangup call from the agent
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver4);
	
		//Verify that new lead has been created
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;

		// verify the recent call entry is associated with the newly creted lead
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		seleniumBase.idleWait(2);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		
		//Verify that task created in sfdc is associated to newly created lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    sfTaskDetailPage.verifyCallAbandoned(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_answered_on_queue_call()
	{
		System.out.println("Test case --multi_contact_campaign_off_answered_on_queue_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//calling to agent through call flow associated with campaign
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    //Subscribe to the queue
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

		//enable lead creation on unanswered calls only setting
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
	    accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the queue
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
		
		//pickup the call
		softPhoneCallQueues.pickCallFromQueue(driver4);
		seleniumBase.idleWait(5);
		
		//verify that no new lead is created in between the call
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_pick_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_on_pick_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//enable lead creation on unanswered calls only setting
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		//calling to the queue
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//verify that in between the call the new lead is not created
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));	
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_select_caller()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_on_select_caller-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//enable lead creation on calls only setting
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		//calling directly to the agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//verify that no lead is created in between the call
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//Select first caller from the multiple matched dropdown
		callScreenPage.selectFirstContactFromMultiple(driver4);
		seleniumBase.idleWait(5);
		
		//Verify that no new is created  afetr selecting the caller from dropdown
		String callerName = callScreenPage.getCallerName(driver4);
		assertFalse(callScreenPage.isLeadImageVisible(driver4));
		
		//do not answer the call
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify that no new lead is created and recent call history is associated with the selected contact from the dropdown
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		
		//verify that no new lead is created
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_queue_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_queue_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("salesforce_connect_user");
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
			
	    //subscribe to a queue
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

		//enable create lead only for unanswered calls
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
	    accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the agent's queue which is associated with call flow with campaign
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_camp_call_flow_call_queue_smart_number"));
		
		//verify that call is appearing in the queue section for the user
		softPhoneCallQueues.isPickCallBtnVisible(driver4);
		seleniumBase.idleWait(5);
		
		//Do not accept the call and hangup
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCallQueues.isPickCallBtnInvisible(driver4);

		//verify that Lead has been created and verify its detail on call history page and caller detail page.
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), "Unknown Unknown");
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_user_busy()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_user_busy-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
		
		// enable create lead only for unanswered call
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//Set the user image as busy
		callScreenPage.setUserImageBusy(driver4);

		// calling to agent's number
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));

		//Do not accept the call and hangup the call
		System.out.println("hanging up with caller");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupActiveCall(driver2);
	
		//verify that new lead has been created and verify it's details
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");

		// verify that the recent call history is assigned to newly created lead
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		seleniumBase.idleWait(2);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		
		//Verify that task created in sfdc is associated to newly created lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
			
		//enable create lead only for unanswered calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the call flow number associated with campaign
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		
		//Verify that the new lead is not created and caller it multiple in between the call
		seleniumBase.idleWait(5);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_user_logout()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_user_logout-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");
		
		// enable create lead only for unanswered call
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//Logout the agent
		softPhoneSettingsPage.logoutSoftphone(driver4);

		// calling to the agent which has logged out
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));

		//Hangup the all after few seconds
		System.out.println("hanging up with caller");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupActiveCall(driver2);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver4, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));
	
		//verify that the new lead has been created and verify it's detail
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");

		// verify caller name on the call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		seleniumBase.idleWait(2);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		
		//Verify that task created in sfdc is associated to newly created lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user);
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), user);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_on_unanswered_on_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_on_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
			
		//enable create lead only for unanswered calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the call flow which is associated with the campaign
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		
		//Verify that no new lead is created until the call is connected
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(5);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verify that no new lead is created
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_on_unanswered_on_direct_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_on_direct_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
			
		//enable create lead for both unanswered and sfdc campaign calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the agent directly
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//verify that no new lead is created until the call is connected
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//pickup the call
		softPhoneCalling.declineCall(driver4);
		seleniumBase.idleWait(5);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//verify that no new lead is created
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_off_unanswered_on_busy_on_call()
	{
		System.out.println("Test case --multi_contact_campaign_off_unanswered_on_busy_on_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//enable create lead for both unanswered and sfdc campaign calls
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//agent is making so it is busy when the call comes
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);

		// calling to the agent which is busy on the call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));

		//Hangup the call after few seconds
		System.out.println("hanging up with caller");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupActiveCall(driver2);
		
		//disconnect the other call as well
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		seleniumBase.idleWait(5);
	
		//now verify that the new caller is created for the missed call and verify it's details
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(callerName));
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void multi_contact_campaign_on_unanswered_on_group_vm_camp_call()
	{
		System.out.println("Test case --multi_contact_campaign_on_unanswered_on_group_vm_camp_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
			
	    //subscribe to the queue  to which the vm will be dropped
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);

		//enable create lead for both sfdc campaign and unanswered calls
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
	    accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to the call flow which drops group vm and associated with the campaign
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_camp_call_flow_group_vm_smart_number"));
		
		//wait for few seconds so that vm is dropped
		seleniumBase.idleWait(20);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(5);

		//Verify that new lead should have been created with proper details
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
		newCreateLead = callScreenPage.getCallerName(driver4);
		assertEquals(newCreateLead, "Unknown Unknown");
		assertEquals(callScreenPage.getCallerCompany(driver4), "Unknown");
		
		//Verify that task created in sfdc is associated to newly created lead
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    reloadSoftphone(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driver4).equals(CONFIG.getProperty("salesforce_connect_user")));
	    assertEquals(sfTaskDetailPage.getCreatedByUser(driver4), CONFIG.getProperty("salesforce_connect_user"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups={"Regression", "Product Sanity", "ExludeForProd"}, dependsOnMethods = {"resetSetupDefault"})
	public void  _afterMethod(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.disableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		if(newCreateLead != null) {
			softPhoneContactsPage.searchUntilLeadPresent(driver3, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number")), newCreateLead);
			softPhoneContactsPage.deleteAllSFLeads(driver3, newCreateLead);
			newCreateLead = null;
		}
	}
	@AfterClass(groups={"Regression", "Product Sanity", "ExludeForProd"})
	public void afterClass(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	}
}