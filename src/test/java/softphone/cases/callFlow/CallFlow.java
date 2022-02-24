package softphone.cases.callFlow;

/**
 * @author Abhishek
 *
 */

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

/**
 * @author Abhishek
 *
 */
public class CallFlow extends SoftphoneBase{

	@Test(groups={"Regression"})
	public void call_flow_contact_campaign()
	{
		System.out.println("Test case --call_flow_contact_campaign-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String campaign = CONFIG.getProperty("softphone_task_related_campaign").trim();
		
		//add caller as contact
		softPhoneContactsPage.deleteAndAddContact(driver4, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verify campaign on call screen
		assertEquals(campaign, callScreenPage.getCampaign(driver4));
		String callerName = callScreenPage.getCallerName(driver4);
		callScreenPage.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
		//Opening the caller detail page
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));

		//Verifying that related campaign is corrected and lead count for that campaign
		assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver4), campaign);
		sfTaskDetailPage.clickRelatedToCampaign(driver4);
		assertTrue(callerName.contains(sfCampaign.getFirstContactName(driver4).get(0)) && callerName.contains(sfCampaign.getFirstContactName(driver4).get(1)));
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
	public void call_flow_lead_campaign()
	{
		System.out.println("Test case --call_flow_lead_campaign-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String campaign 		= CONFIG.getProperty("softphone_task_related_campaign").trim();
		String callScriptsText	= "This is the automation campaign call script.";
		
		//add caller as contact
		softPhoneContactsPage.deleteAndAddLead(driver4, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		callScreenPage.getCallerName(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
		
		//verify call script icon
		callToolsPanel.verifyCallScriptsIcon(driver4);
		
		//verify that call script is present
		assertEquals(callToolsPanel.getCallScripts(driver4), callScriptsText);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verify campaign on call screen
		assertEquals(campaign, callScreenPage.getCampaign(driver4));
		callScreenPage.idleWait(10);

		//Opening the caller detail page
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));

		//Verifying that related campaign is corrected and lead count for that campaign
		assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver4), campaign);
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
	public void call_flow_add_new_lead_verify_details()
	{
		System.out.println("Test case --call_flow_add_new_lead_verify_details-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver4)) {
			callScreenPage.deleteCallerObject(driver4);
			reloadSoftphone(driver4);
		}		

		//Open Salesforce campaign page and get number of leads count
		sfCampaign.openSalesforceCampaignPage(driver4);
		int leadCounts = Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		String callerName = callScreenPage.getCallerName(driver4);

		//verfy that new user has been added
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

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_dial_queue(){
		System.out.println("Test case --call_flow_dial)_queue()-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		//subscribe queue
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, queueName);
		softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an call to call flow which has queue
		System.out.println("caller making call to call flow");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_call_flow_call_queue_smart_number"));
		
		//verify Queue Count
		softPhoneCallQueues.verifyQueueCount(driver1, "1");
		
		//pickup call from the queue
		softPhoneCallQueues.openCallQueuesSection(driver1);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with the called device
		System.out.println("hanging up with the device");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// wait that call has been removed from agent
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify sfdc data
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
		assertEquals(sfTaskDetailPage.getCallQueue(driver1), queueName + " - " + CONFIG.getProperty("qa_call_flow_call_queue")); 
		assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
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
	public void call_flow_dial_queue_with_forwarding(){
		System.out.println("Test case --call_flow_dial_queue_with_forwarding()-- started ");
		
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		//subscribe queue
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, queueName);
		softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an call to call flow which has queue
		System.out.println("caller making call to call flow");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_call_flow_call_queue_smart_number"));
		
		//verify Queue Count
		softPhoneCallQueues.verifyQueueCount(driver1, "1");
		
		//pickup call from the queue
		softPhoneCallQueues.openCallQueuesSection(driver1);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// pick call from forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hanging up with the second device
	    System.out.println("hanging up with the second device");
	    softPhoneCalling.hangupActiveCall(driver2);
		
		//hanging up with the called device
		System.out.println("hanging up with the device");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// wait that call has been removed from agent
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify sfdc data
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
		assertEquals(sfTaskDetailPage.getCallQueue(driver1), queueName + " - " + CONFIG.getProperty("qa_call_flow_call_queue")); 
		assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void a_call_flow_dial_to_extension_number() {

		System.out.println("Test case --call_flow_dial_to_extension_number-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		aa_AddCallersAsContactsAndLeads();

		//Dialing smart number of call flow which is set to dial to number with extension
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_call_flow_for_check_extension_number_number"));

		//Picking up call from extension number user
		softPhoneCalling.pickupIncomingCall(driver4);

		//Hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = "Inbound Call: +12013809068 - Last Step: Dial, +14157276628,1111";
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);

		System.out.println("Test Case is Pass");
	}

	@Test(groups={"Sanity", "ExludeForProd", "Regression", "Product Sanity"})
	public void a_call_flow_to_owner_id() {

		System.out.println("Test case --call_flow_to_owner_id-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();

		//open Support Page and enable send email through salesfore setting
		loginSupport(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("qa_user_3_username"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		seleniumBase.idleWait(2);
		userIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_owner_id"));
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //subscribe queue from users
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, queueName);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Decline call from the user
	    softPhoneCalling.declineCall(driver3);
	    
	    //verify Queue Count
	    softPhoneCallQueues.openCallQueuesSection(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.verifyQueueCount(driver3, "1");
	    
	    //verify that voicemail play icon appearing for first caller
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.verifyQueueCount(driver4, "1");
	    
	    //hangup active call from first caller
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("qa_user_1_number").trim() + " - Last Step: Voicemail";
	    callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);;
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver3);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);	    
	    
	    //open Support Page and enable send email through salesfore setting
	    loginSupport(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("qa_user_3_username"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver1);
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
}