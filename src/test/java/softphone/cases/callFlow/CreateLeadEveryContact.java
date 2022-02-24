package softphone.cases.callFlow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CreateLeadEveryContact extends SoftphoneBase{
	
	String newCreateLead = null;
	String callerName  = null;
	String callerTitle = null;
	String callerEmail = null;
	String companyName = null;
	
	@BeforeClass(groups={"Regression"})
	public void deleteAndAddContact(){
		//updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//Create a caller as contact and store its detail
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		String contactFirstName = CONFIG.getProperty("prod_user_1_name");
		softPhoneContactsPage.deleteAllContacts(driver3, contactNumber, contactFirstName);
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);
		softPhoneCalling.isMuteButtonEnables(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		callScreenPage.addCallerAsContact(driver3, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
		callScreenPage.closeErrorBar(driver3);
		callToolsPanel.isRelatedRecordsIconVisible(driver3);
		
		callScreenPage.openCallerDetailPage(driver3);
		callerName  = contactDetailPage.getCallerName(driver3);
		callerTitle = contactDetailPage.getCallerTitle(driver3);
		callerEmail = contactDetailPage.getCallerEmail(driver3);
		companyName = contactDetailPage.getCallerAccountName(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
	}
	
	@Test(groups={"Regression"})
	public void create_lead_incoming_call_sfdc_camp_on()
	{
		System.out.println("Test case --create_lead_incoming_call_sfdc_camp_on-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		deleteAndAddContact();
		
		String user = CONFIG.getProperty("qa_user_2_name");

		//Enable multiple lead creation for SFDC campaign and create lead on every incmoming call
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		//accountSalesforceTab.disablecreateLeadEveryContactSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to call flow so that new lead gets created
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_call_flow_number"));
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		callScreenPage.verifyCallerNameIsInvisible(driver4, callerName);
		String newLeadcallerName = callScreenPage.getCallerName(driver4);
		newCreateLead = newLeadcallerName;
		assertFalse(newCreateLead.contains(callerName));
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify newly created lead and no campaign associated with it
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		assertEquals(callScreenPage.getCampaign(driver4), "");
		
		//verify the lead entry in call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), newCreateLead);
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("prod_user_1_name")));
		
		//Verify that task created in sfdc is associated to newly created lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(newCreateLead));
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driver4).equals(user));
	    assertTrue(sfTaskDetailPage.getCreatedByUser(driver4).equals(user));
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
	public void create_lead_incoming_call()
	{
		System.out.println("Test case --create_lead_incoming_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		softPhoneContactsPage.deleteAndAddLead(driver3, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		
		//Enable create lead on every incmoming call
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		//accountSalesforceTab.disablecreateLeadEveryContactSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//calling to call flow number
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_call_flow_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		String callerName = callScreenPage.getCallerName(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify the newly created lead and no campaign associated with it
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		callerName = callScreenPage.getCallerName(driver4);
		newCreateLead = callerName;
		assertEquals(callScreenPage.getCampaign(driver4), "");
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		
		//verify the entry for recently created lead on call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
		assertFalse(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0).contains(CONFIG.getProperty("prod_user_1_name")));
		
		//verify that newly created task is assigned to lead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(callerName));
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
	
	//@Test(groups={"Regression"})
	public void create_lead_every_contact_incoming_call()
	{
		System.out.println("Test case --create_lead_every_contact_incoming_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String user = CONFIG.getProperty("qa_user_2_name");

		// Enable create lead for incoming call through contact only	
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.disableCreateLeadEveryInboundCallSetting(driver1);
		//accountSalesforceTab.enablecreateLeadEveryContactSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		//calling to call flow number
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		assertFalse(callScreenPage.isLeadImageVisible(driver4));
		softPhoneCalling.pickupIncomingCall(driver4);
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify the newly created lead and no campaign is associated with it.
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		newCreateLead = callerName;
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		
		//verify the data for lead is same as contact in sfdc
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    assertEquals(contactDetailPage.getCallerName(driver4), callerName);
	    assertEquals(contactDetailPage.getCallerTitle(driver4), callerTitle);
	    assertEquals(contactDetailPage.getCallerEmail(driver4), callerEmail);
	    assertEquals(contactDetailPage.getCallerCompanyName(driver4), companyName);
	    assertEquals(contactDetailPage.getLeadOwner(driver4), user);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getName(driver4).contains(callerName));
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driver4).equals(user));
	    assertTrue(sfTaskDetailPage.getCreatedByUser(driver4).equals(user));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
		//calling again to the call flow number to check no new leads gets created
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_call_flow_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone and verify that call came from multiple contact
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		assertTrue(callScreenPage.isCallerMultiple(driver4));
		
		//verify that only 2 contacts are there in multiple dropdown
		assertEquals(callScreenPage.getContactsCountInMultiDropDown(driver4), 2);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups={"Regression"})
	public void  _afterMethod(ITestResult result){
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
		//accountSalesforceTab.disablecreateLeadEveryContactSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		if(newCreateLead != null) {
			softPhoneContactsPage.searchUntilLeadPresent(driver3, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number")), newCreateLead);
			softPhoneContactsPage.deleteAllSFLeads(driver3, newCreateLead);
			newCreateLead = null;
		}
		
		if(result.getName().equals("create_lead_incoming_call")) {
			softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		}else if(result.getName().equals("create_lead_every_contact_incoming_call")) {
			softPhoneContactsPage.addContactIfNotExist(driver3, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));	
		}
	}
	
	@AfterClass(groups={"Regression"})
	public void afterClass(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	}
}