/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.cases.callFlow.CreateLeadMultiMatch;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallFlow extends SoftphoneBase{
	
	CreateLeadMultiMatch createLeadMultiMatch = new CreateLeadMultiMatch();

	@Test(groups={"Sanity", "Regression", "MediumPriority"})
	public void call_flow_add_new_lead()
	{
		System.out.println("Test case --call_flow_add_new_lead-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//Making outbound Call
		aa_AddCallersAsContactsAndLeads();
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
		softPhoneCalling.idleWait(2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver4), "Open");

		//Opening the caller detail page
		seleniumBase.idleWait(3);
		callScreenPage.openCallerDetailPage(driver4);
		assertEquals(contactDetailPage.getLeadStatus(driver4), "Open");
		assertTrue(contactDetailPage.getLeadDescription(driver4).contains("Created by RingDNA"));

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));

		//Verifying that related campaign is corrected and lead count for that campaign
		assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver4), CONFIG.getProperty("softphone_task_related_campaign"));
		sfTaskDetailPage.idleWait(2);
		sfTaskDetailPage.clickRelatedToCampaign(driver4);
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts + 1);
	
		//verify only single entry is there in RingDNA stats for today 
		if(System.getProperty("environment")==null){
			String todaysDate = HelperFunctions.GetCurrentDateTime("M/d/yyyy");
			sfCampaign.openRecentCampaignStatsEntry(driver4);
			String ringDNAStatsCreatedDate = sfCampaign.getRingDNAStatsCreatedDate(driver4);
			assertEquals(ringDNAStatsCreatedDate, todaysDate);
			
			//verify that multiple entries are not there in the Ringdna stats for today
			sfCampaign.openSalesforceCampaignPage(driver4);
			if(sfCampaign.openSecondRecentCampaignStatsEntry(driver4)){
				assertNotEquals(ringDNAStatsCreatedDate, sfCampaign.getRingDNAStatsCreatedDate(driver4));
			}
		}
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
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
	public void call_flow_add_new_lead_multiple_caller()
	{
		System.out.println("Test case --call_flow_add_new_lead_multiple_caller-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//calling the test methods for this case from different test class
		createLeadMultiMatch.createMultiMatchContact();
		createLeadMultiMatch.enableMultiMatchSettings();
		
		//enable create lead only for sfdc campaign calls
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//Open Salesforce campaign page and get number of leads count
		sfCampaign.openSalesforceCampaignPage(driver4);
		sfCampaign.openRecentCampaignStatsEntry(driver4);
		int leadCounts = Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		assertTrue(callScreenPage.isCallerMultiple(driver4));

		//verfy that no new lead has been created
		sfCampaign.openSalesforceCampaignPage(driver4);
		sfCampaign.openRecentCampaignStatsEntry(driver4);
		assertEquals(Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4)), leadCounts);   
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//get leads name
		String callerName = callScreenPage.getCallerName(driver4);

		//verfy that new user has been created
		sfCampaign.openSalesforceCampaignPage(driver4);
		assertTrue(callerName.contains(sfCampaign.getFirstContactName(driver4).get(0)) && callerName.contains(sfCampaign.getFirstContactName(driver4).get(1)));
		sfCampaign.openRecentCampaignStatsEntry(driver4);
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
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		//Hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), CONFIG.getProperty("qa_user_2_name"));
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);

		System.out.println("Test Case is Pass");
	}

	@Test(groups={"Sanity", "ExludeForProd", "Regression"})
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
	    
	    //verify that voicemail play icon appearing for first caller
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.verifyQueueCount(driver4, "1");
	    
	    //verify Queue Count
	    softPhoneCallQueues.openCallQueuesSection(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.verifyQueueCount(driver3, "1");
	    
	    //hangup active call from first caller
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver3);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
		//verify call data
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    assertFalse(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));	    
	    
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
	
	@Test(groups={"Regression"})
	public void unavailable_call_flow_account_level()
	{
		System.out.println("Test case --unavailable_call_flow_account_level-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.declineCall(driver1);
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		/******Verifying when agent ignores call*******/
		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		seleniumBase.idleWait(25);
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		/******Verifying when agent logout*******/
		//Logging out the agent
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
				
		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnvailableFlowSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "Product Sanity"})
	public void unavailable_call_flow_both_level()
	{
		System.out.println("Test case --unavailable_call_flow_both_level-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//select unavailable call flow from users page
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
		
		/******Verifying when agent ignores call*******/
	    //subscribe queue from users
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Decline call from the user
		seleniumBase.idleWait(25);
	    
	    //verify that voicemail play icon appearing for first caller
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.verifyQueueCount(driver4, "1");
	    
	    //hangup active call from first caller
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
		//verify call data
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    assertFalse(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
	    
		// open Support Page and remove unavailable call flow
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnvailableFlowSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//remove unavailable call flow from users page
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("qa_user_3_username"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver1);
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void unavailable_call_flow_account_level_listening_call()
	{
		System.out.println("Test case --unavailable_call_flow_account_level_listening_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);
		
		//calling to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		
		//pickup call from the call flow agent
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		//hanging up with caller 2
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver3);
				
		// open Support Page and remove unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnvailableFlowSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void unavailable_call_flow_user_level_second_call()
	{
		System.out.println("Test case --unavailable_call_flow_user_level_second_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_username"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		seleniumBase.idleWait(2);
		userIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		
		//Taking another incoming call to the agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//Verify that additional call is appearing
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Verifying that once the additional call is removed from agent it is redirected to call flow agent
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with call flow agent
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver3);
				
		// open Support Page and remove unavailable call flow setting
		loginSupport(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_username"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver1);
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_flow_add_new_lead_verify_stats()
	{
		System.out.println("Test case --call_flow_add_new_lead-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		//Making outbound Call
		aa_AddCallersAsContactsAndLeads();
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
		sfCampaign.openRecentCampaignStatsEntry(driver4);
		int leadCounts = Integer.parseInt(sfCampaign.getCampaignLeadsCount(driver4));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		//calling again to agent so that new lead gets created 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.idleWait(2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver4), "Open");

		//Opening the caller detail page
		seleniumBase.idleWait(3);
		callScreenPage.openCallerDetailPage(driver4);
		assertEquals(contactDetailPage.getLeadStatus(driver4), "Open");
		assertTrue(contactDetailPage.getLeadDescription(driver4).contains("Created by RingDNA"));

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));

		//Verifying that related campaign is corrected and lead count for that campaign
		assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver4), CONFIG.getProperty("softphone_task_related_campaign"));
		sfTaskDetailPage.idleWait(2);
		sfTaskDetailPage.clickRelatedToCampaign(driver4);
		sfCampaign.openRecentCampaignStatsEntry(driver4);
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
	
	@AfterMethod(groups={"MediumPriority", "Product Sanity"}, dependsOnMethods = {"resetSetupDefault"})
	public void  _afterMethod(ITestResult result){
		if(result.getName().equals("call_flow_add_new_lead_multiple_caller")){
			createLeadMultiMatch._afterMethod();
			aa_AddCallersAsContactsAndLeads();
		}
	}
}