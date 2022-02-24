package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

/**
 * @author Abhishek
 *
 */
public class Voicemail extends SoftphoneBase{
	
	String oooCustomGreetingName = "AutomationOOOTestGreeting";
	
	@Test(groups = {"Sanity", "Regression"})
	public void verify_voicemail_drop_when_agent_busy() {
		System.out.println("Test case --verify_voicemail_drop_when_agent_busy-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		// decline call from agent's phone
		softPhoneCalling.declineAdditionalCall(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from called agent
		System.out.println("Call is removing from called agent");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		seleniumBase.idleWait(3);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount + 1);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount + 1);

	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void verify_voicemail_drop_when_agent_logout() {
		System.out.println("Test case --verify_voicemail_drop_when_agent_logout-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		//Logging out the agent
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		seleniumBase.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);

	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity", "MediumPriority"})
	public void verify_voicemail_drop_to_user_group() {
		System.out.println("Test case --verify_voicemail_drop_to_user_group-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_3_name"));

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.idleWait(2);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		System.out.println(missedCallCount);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		System.out.println(missedVoicemailCount);

		//Logging out the agent
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_3_number"));
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		seleniumBase.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount + 1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);

	    //verify data for vm dropped call
		softphoneCallHistoryPage.idleWait(2);
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority", "Product Sanity"})
	public void voicemail_user_group_not_subscribed() {
		System.out.println("Test case --voicemail_user_group_not_subscribed-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
	    //subscribe queues
	    softPhoneCallQueues.unSubscribeQueue(driver1, CONFIG.getProperty("qa_group_3_name"));  

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.idleWait(3);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_3_number"));
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		seleniumBase.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.idleWait(3);
		reloadSoftphone(driver1);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);
		softphoneCallHistoryPage.idleWait(3);
		softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
		softphoneCallHistoryPage.switchToAllCallsTab(driver1);
		softphoneCallHistoryPage.verifyGroupUnreadMissedCallRow(driver1, 0);
		
		//verify call entry background after recording play
	    softphoneCallHistoryPage.playVMByIndex(driver1, 1);
	    seleniumBase.idleWait(15);
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.verifyReadMissedCallRow(driver1, 0);

	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //play recording from all activity tab
	    softPhoneActivityPage.navigateToCallsTab(driver1);
	    softPhoneActivityPage.idleWait(2);
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    softPhoneActivityPage.playTaskRecording(driver1, callSubject);
	    seleniumBase.idleWait(1);
	    softPhoneActivityPage.getRecordingPlayerCurrentTime(driver1, callSubject);
	  		
	    //play recording from all calls tab
	    softPhoneActivityPage.navigateToCallsTab(driver1);
	    seleniumBase.idleWait(2);
	    softPhoneActivityPage.playTaskRecording(driver1, callSubject);
	    seleniumBase.idleWait(1);
	    softPhoneActivityPage.getRecordingPlayerCurrentTime(driver1, callSubject);
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	

	@Test(groups = { "Regression"})
	public void drop_voicemail_after_call_decline() {
		System.out.println("Test case --drop_voicemail_after_call_decline-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.isDeclineButtonVisible(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneCalling.declineCall(driver2);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		String callerName = callScreenPage.getCallerName(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    sfTaskDetailPage.clickAutomatedVMURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void create_voicemail() {
		System.out.println("Test case --create_voicemail-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String voiceMailName = "AutomationTestVoicemail";
		
		//navigate to voicemail Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToVoicemailDropTab(driver3);		

		//add a voicemail
		softPhoneSettingsPage.createVoiceMail(driver3, voiceMailName, 5);
		
		//Play voicemail
		softPhoneSettingsPage.playVoiceMail(driver3, voiceMailName);
		softPhoneSettingsPage.stopVoiceMailPlay(driver3, voiceMailName);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
		
		// pickup call
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);
		 
		//dropping Voice Mail
		callToolsPanel.dropVoiceMailByName(driver3, voiceMailName);
		
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected");
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver3).contains(voiceMailName));;
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    sfTaskDetailPage.clickAutomatedVMURL(driver3);
	    callRecordingPage.clickPlayButton(driver3, 0);
	    seleniumBase.switchToTab(driver3, 1);
	    
		// disconnect from called agent
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//navigate to voicemail Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToVoicemailDropTab(driver3);		
		
		//delete voicemail
		softPhoneSettingsPage.deleteVoiceMail(driver3, voiceMailName);
		
		  //refresh the recording page and verify that recording still present
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    driver3.navigate().refresh();
	    callRecordingPage.clickPlayButton(driver3, 0);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity", "Regression"})
	public void drop_voicemail_verify_sfdc_entries() {
		System.out.println("Test case --drop_voicemail_verify_sfdc_entries-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// Call is removing from called agent
		System.out.println("Call is removing from called agent");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
	    //verify data for outbound call
		String callerName = callScreenPage.getCallerName(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    
	    //verify recording data
	    sfTaskDetailPage.clickAutomatedVMURL(driver1);
	    callRecordingPage.downloadRecording(driver1, 0);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    callRecordingPage.clickPlayButton(driver1, 0);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity", "Regression"})
	public void order_voicemails_on_settings_page() {
		System.out.println("Test case --order_voicemails_on_settings_page-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String voiceMailName1 = "AutomationTestVoicemail1";
		String voiceMailName2 = "AutomationTestVoicemail2";
		
		//navigate to voicemail Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToVoicemailDropTab(driver3);
		
		//delete voicemails
		softPhoneSettingsPage.deleteVoiceMail(driver3, voiceMailName1);
		softPhoneSettingsPage.deleteVoiceMail(driver3, voiceMailName2);

		//add voicemails
		softPhoneSettingsPage.createVoiceMail(driver3, voiceMailName1, 1);
		softPhoneSettingsPage.createVoiceMail(driver3, voiceMailName2, 1);
		
		//Moving voicemail by 2 steps and verify it.
		softPhoneSettingsPage.moveUpVoiceMail(driver3, voiceMailName2, 2);
		
		//Moving down voicemail by 2 steps and verify it.
		softPhoneSettingsPage.moveDownVoiceMail(driver3, voiceMailName2, 2);
		
		//Moving voicemail to top and verify it.
		softPhoneSettingsPage.sendVMToTop(driver3, voiceMailName2);
		
		//Moving voicemail to bottom and verify it.
		softPhoneSettingsPage.sendVMToBottom(driver3, voiceMailName2);
		
		//Verify first up and last down button is disabled
		softPhoneSettingsPage.verifyFirstMoveUpButtonDisabled(driver3);
		softPhoneSettingsPage.verifyLastMoveDownButtonDisabled(driver3);
		
		//voiceMail List
		List<String> addedVoicemailsList = softPhoneSettingsPage.getVoiceMailList(driver3);
		
		//Making outbound call from softphone
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //get voicemail drop list
	    callToolsPanel.clickVoiceMailIcon(driver3);
	    List<String> voicemailsToDropList = callToolsPanel.getVoicemailDropLists(driver3);
	    
	    //Hangup call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verifying the lists have same order

	    assertTrue(addedVoicemailsList.equals(voicemailsToDropList));

	    //navigate to voicemail Tab
	  	softPhoneSettingsPage.clickSettingIcon(driver3);
	  	softPhoneSettingsPage.navigateToVoicemailDropTab(driver3);
		//delete voicemails
		softPhoneSettingsPage.deleteVoiceMail(driver3, voiceMailName1);
		softPhoneSettingsPage.deleteVoiceMail(driver3, voiceMailName2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "Product Sanity"})
	public void verify_voicemail_drop_when_agent_decline() {
		System.out.println("Test case --verify_voicemail_drop_when_agent_decline-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		softPhoneContactsPage.deleteAndAddContact(driver4, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		
		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		seleniumBase.idleWait(3);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver4);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
				
		// declining call
		System.out.println("declining call");
		softPhoneCalling.declineCall(driver4);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(3);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver4, missedVoicemailCount);

		//play vm and verify call counts
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
	    softphoneCallHistoryPage.playVMByIndex(driver4, 1);
	    
	    //verify VM count is decreased
	    softphoneCallHistoryPage.isMissedVMCountIncreased(driver4, missedVoicemailCount - 1);
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//play recording from all activity tab
		softPhoneActivityPage.navigateToOpenTasksTab(driver4);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject);
		
		//play recording from all calls tab
		softPhoneActivityPage.navigateToCallsTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);		
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority"})
	public void verify_voicemail_drop_when_call_missed() {
		System.out.println("Test case --verify_voicemail_drop_when_call_missed-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		// missing call
		System.out.println("missing call");
		seleniumBase.idleWait(25);
		String callerName = callScreenPage.getCallerName(driver1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(3);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);

		//play vm and verify call counts
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    softphoneCallHistoryPage.playVMByIndex(driver1, 1);
	    
	    //verify VM count is decreased
	    softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount - 1);
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    
	    //verify caller details in SFDC
	    callRecordingPage.openCallerSFDCLink(driver1);
	    assertEquals(contactDetailPage.getCallerName(driver1), callerName);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void verify_voicemail_drop_when_user_busy() {
		System.out.println("Test case --verify_voicemail_drop_when_user_busy-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		//making agent busy
		callScreenPage.setUserImageBusy(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		seleniumBase.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(3);
		reloadSoftphone(driver1);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);

		//play vm and verify call counts
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    softphoneCallHistoryPage.playVMByIndex(driver1, 1);
	    
	    //verify VM count is decreased
	    softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount - 1);
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void drop_voicemail_to_multiple_caller() {
		System.out.println("Test case --drop_voicemail_to_multiple_caller-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String contactFirstName = "Contact_Existing";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver1);

		// Deleting contact if exist
		if (!callScreenPage.isCallerMultiple(driver1) && callScreenPage.isCallerUnkonwn(driver1)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(driver1, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}

		if (!callScreenPage.isCallerMultiple(driver1)) {
			// Update caller to be contact
			callScreenPage.clickOnUpdateDetailLink(driver1);
			callScreenPage.addContactForExistingCaller(driver1, contactFirstName, CONFIG.getProperty("contact_account_name"));
			reloadSoftphone(driver1);

			// sync time from sfdc to ringdna
			seleniumBase.idleWait(30);
			
			softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("qa_user_2_number"));
		}

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// entering call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.enterCallNotes(driver1, callSubject, callSubject);
		assertTrue(callScreenPage.isCallerMultiple(driver1));
		softPhoneCalling.hangupIfInActiveCall(driver4);
		callScreenPage.selectFirstContactFromMultiple(driver1);
		
		assertEquals(callScreenPage.getCallerVoicemail(driver1), usedVoiceMail);

		softPhoneContactsPage.convertMultipleToSingle(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void create_and_verify_global_voicemail() {
		System.out.println("Test case --create_and_verify_global_voicemail-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String voiceMail = "Automation Global Voicemail";
		
		//navigating to support page 
		loginSupport(driver1);
				
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
				
		//disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.createVoiceMail(driver1, voiceMail, 10);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
				
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		 // pickup call
		 softPhoneCalling.pickupIncomingCall(driver2);
				
		//dropping Voice Mail
		callToolsPanel.dropVoiceMailByName(driver1, voiceMail);
		
		//enter call details
		String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
		String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
		
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callNotes);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(voiceMail));;
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// disconnect from called agent
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//navigating to support page 
		loginSupport(driver1);
						
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
						
		//disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteRecords(driver1, voiceMail);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
				
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//@Test(groups = {"Regression"})
	public void update_call_details_while_vm_dropped() {
		System.out.println("Test case --update_call_details_while_vm_dropped-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String voiceMail = "Automation Long VM (Don't Delete)";
				
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		 // pickup call
		 softPhoneCalling.pickupIncomingCall(driver2);
				
		//dropping Voice Mail
		callToolsPanel.dropVoiceMailByName(driver1, voiceMail);
		
		//enter call details
		String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		int rating = 4;
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
		String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
		callToolsPanel.giveCallRatings(driver1, rating);
		
	    //verify data for vm dropped call
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callNotes);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(voiceMail));;
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getDisposition(driver1).trim().isEmpty());
	    assertEquals(Integer.parseInt(sfTaskDetailPage.getRating(driver1)), 0);
	    assertTrue(sfTaskDetailPage.getComments(driver1).trim().isEmpty());  
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// disconnect from called agent
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify updated call history
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callNotes);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(voiceMail));;
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    assertEquals(Integer.parseInt(sfTaskDetailPage.getRating(driver1)), rating);
	    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void voice_mail_access_admin_user() {
		System.out.println("Test case --voice_mail_access_admin_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
	    WebDriver qaAutoAdminDriver = getDriver();
		SFLP.softphoneLogin(qaAutoAdminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qaAuto_admin_user_username"), CONFIG.getProperty("qaAuto_admin_user_password"));
	    loginSupport(qaAutoAdminDriver);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.declineCall(driver2);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		String callerName = callScreenPage.getCallerName(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    String automatedRecordingURl = sfTaskDetailPage.getAutomatedVMURL(driver1);
	    sfTaskDetailPage.clickAutomatedVMURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(adminDriver);		
		seleniumBase.switchToTab(adminDriver,seleniumBase.getTabCount(adminDriver));
		adminDriver.get(automatedRecordingURl);
		callRecordingPage.getNumberOfCallRecordings(adminDriver);
	    callRecordingPage.verifyCallerName(adminDriver, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(adminDriver);
	    callRecordingPage.verifyDurationNotZero(adminDriver);
		callRecordingPage.clickPlayButton(adminDriver, 0);
	    seleniumBase.closeTab(adminDriver);
	    seleniumBase.switchToTab(adminDriver, 1);
	    
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(qaAutoAdminDriver);		
		seleniumBase.switchToTab(qaAutoAdminDriver,seleniumBase.getTabCount(qaAutoAdminDriver));
		qaAutoAdminDriver.get(automatedRecordingURl);
  		callRecordingPage.verifyAccessDeniedMessageOtherOrg(qaAutoAdminDriver);
	    seleniumBase.closeTab(qaAutoAdminDriver);
	    seleniumBase.switchToTab(qaAutoAdminDriver, 1);

	    qaAutoAdminDriver.quit();
	    qaAutoAdminDriver = null;
	    adminDriver.quit();
	    adminDriver = null;
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("adminDriver", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void voice_mail_access_support_user() {
		System.out.println("Test case --voice_mail_access_support_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("webSupportDriver");
	    driverUsed.put("webSupportDriver", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.declineCall(driver2);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		String callerName = callScreenPage.getCallerName(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    String automatedRecordingURl = sfTaskDetailPage.getAutomatedVMURL(driver1);
	    sfTaskDetailPage.clickAutomatedVMURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(webSupportDriver);		
		seleniumBase.switchToTab(webSupportDriver,seleniumBase.getTabCount(webSupportDriver));
		webSupportDriver.get(automatedRecordingURl);
		callRecordingPage.getNumberOfCallRecordings(webSupportDriver);
	    callRecordingPage.verifyCallerName(webSupportDriver, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(webSupportDriver);
	    callRecordingPage.verifyDurationNotZero(webSupportDriver);
		callRecordingPage.clickPlayButton(webSupportDriver, 0);
	    seleniumBase.closeTab(webSupportDriver);
	    seleniumBase.switchToTab(webSupportDriver, 1);

	    webSupportDriver.quit();
	    webSupportDriver = null;
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case is pass");
	}
	
	//@Test(groups = { "MediumPriority"})
	public void voice_mail_deleted() {
		System.out.println("Test case --voice_mail_deleted-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.declineCall(driver2);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		String callerName = callScreenPage.getCallerName(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    sfTaskDetailPage.clickAutomatedVMURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    
	    //delete the vm
	    seleniumBase.switchToTab(driver1, 1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.deleteVMByIndex(driver1, 1);
	    assertFalse(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    
	    //refresh the recording page and verify that recording still present
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    driver1.navigate().refresh();
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    callRecordingPage.clickPlayButton(driver1, 0);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "Product Sanity"})
	public void create_custom_greeting() {
		System.out.println("Test case --create_custom_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String oldCustomGreetingName = "AutomationTestGreeting";
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);
		softPhoneSettingsPage.deleteAllCustomGreetings(driver3);

		//add a Custom Greeting
		softPhoneSettingsPage.createCustomGreeting(driver3, oldCustomGreetingName, 5);
		
		//Play custom Greeting
		softPhoneSettingsPage.playCustomGreeting(driver3, oldCustomGreetingName);
		
		//Update Custom Greeting
		String newCustomGreetingName = "UpdatedCustomGreeting";
		softPhoneSettingsPage.updateCustomGreeting(driver3, oldCustomGreetingName, newCustomGreetingName, 5);
		
		//find and Play updated custom Greeting
		softPhoneSettingsPage.playCustomGreeting(driver3, newCustomGreetingName);
		
		//verify that old custom greeting does not exist
		//assertEquals(softPhoneSettingsPage.getCustomGreetingIndex(driver3, oldCustomGreetingName), -1);
		
		//select Custom greeting
		softPhoneSettingsPage.selectCustomGreeting(driver3, newCustomGreetingName);
		assertTrue(softPhoneSettingsPage.isCustomGreetingSelected(driver3, newCustomGreetingName));
		
		//delete Custom Greeting
		softPhoneSettingsPage.deleteCustomGreeting(driver3, newCustomGreetingName);
		
		//verifying that after deleting the selected greeting, default greeting should get selected
		assertTrue(softPhoneSettingsPage.isCustomGreetingSelected(driver3, "Default Greeting"));
		
		//navigate to Custom Greeting Tab
		reloadSoftphone(driver3);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);	
		
		//Verify that Custom Greeting is deleted
		assertEquals(softPhoneSettingsPage.getCustomGreetingIndex(driver3, newCustomGreetingName), -1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void verify_default_greeting() {
		System.out.println("Test case --verify_default_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//String defaultGreetingName = "Default Greeting";
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);		
		
		//Verify that update and delete button are not present for default greeting
		softPhoneSettingsPage.verifyDefaultGreetingUpdateDeleteButton(driver3);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void verify_multiple_custom_greeting() {
		System.out.println("Test case --verify_multiple_custom_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String customGreetingName = "AutomationTestGreeting";
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);	
		
		softPhoneSettingsPage.deleteAllCustomGreetings(driver3);
		
		for (int i = 0; i < 5; i++) {
			// add a Custom Greeting
			softPhoneSettingsPage.createCustomGreeting(driver3, customGreetingName + i, 2);
		}

		for (int i = 0; i < 5; i++) {
			// Play custom Greeting
			softPhoneSettingsPage.playCustomGreeting(driver3, customGreetingName + i);
		}

		for (int i = 0; i < 5; i++) {
			// delete Custom Greeting
			softPhoneSettingsPage.deleteCustomGreeting(driver3, customGreetingName);
		}
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority"})
	public void enable_external_custom_greeting() {
		System.out.println("Test case --verify_multiple_custom_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);	
		
		//enable custom greeting number
		softPhoneSettingsPage.enableExternalGreeting(driver3,  CONFIG.getProperty("prod_user_1_number"));
		softPhoneSettingsPage.verifyExtenalNumberToolTip(driver3);
		
		//navigate to Custom Greeting Tab
		reloadSoftphone(driver3);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);	
	
		//disable external custom greeting number
		softPhoneSettingsPage.disableExternalNumber(driver3);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority"})
	public void add_multiple_external_custom_greeting() {
		System.out.println("Test case --add_multiple_external_custom_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);
		
		softPhoneSettingsPage.deleteAllExternalNumbers(driver3);
		
		//enable custom greeting number
		softPhoneSettingsPage.addExternalNumber(driver3,  CONFIG.getProperty("prod_user_1_number"));
		softPhoneSettingsPage.addExternalNumber(driver3,  CONFIG.getProperty("prod_user_2_number"));
		softPhoneSettingsPage.addExternalNumber(driver3,  CONFIG.getProperty("prod_user_3_number"));
		softPhoneSettingsPage.addExternalNumber(driver3,  CONFIG.getProperty("qa_user_2_number"));
		softPhoneSettingsPage.addExternalNumber(driver3,  CONFIG.getProperty("qa_user_3_number"));
		
		assertTrue(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_1_number")));
		assertTrue(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_2_number")));
		assertTrue(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_3_number")));
		assertTrue(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_2_number")));
		assertTrue(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_3_number")));
		
		softPhoneSettingsPage.deleteAllExternalNumbers(driver3);
		
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_1_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_2_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_3_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_2_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_3_number")));
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);	
		
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_1_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_2_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("prod_user_3_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_2_number")));
		assertFalse(softPhoneSettingsPage.isExternalGreetingNumberAdded(driver3,  CONFIG.getProperty("qa_user_3_number")));
	
		//disable external custom greeting number
		softPhoneSettingsPage.disableExternalNumber(driver3);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	
	@Test(groups = {"Regression", "MediumPriority"})
	public void create_ooo_custom_greeting() {
		System.out.println("Test case --create_ooo_custom_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver4);		
		softPhoneSettingsPage.deleteAllCustomGreetings(driver3);

		//add a Custom Greeting
		softPhoneSettingsPage.createOOOCustomGreeting(driver4, oooCustomGreetingName, 2);
		
		//select custom Greeting
		softPhoneSettingsPage.selectCustomGreeting(driver4, oooCustomGreetingName);
		
		//verify that OOO Greeting created and OOO icon is there
		softPhoneSettingsPage.verifyOOOGreetingCreated(driver4, oooCustomGreetingName);
		
		//verify that warning message is appearing
		softPhoneSettingsPage.verifyOOOGreetingMessagePresent(driver4);
		
		//reload softphone and verify warning message is present
		reloadSoftphone(driver4);
		softPhoneSettingsPage.verifyOOOGreetingMessagePresent(driver4);
		
		//Verify ooo custom greeting on admin application
		loginSupport(driver4);
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		assertEquals(userIntelligentDialerTab.getGreetingOOOLabelText(driver4, oooCustomGreetingName), "true");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Verify ooo custom greeting on support application
		loginSupport(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.searchUserWithSalesForceId(driver1, CONFIG.getProperty("qa_user_2_name"), CONFIG.getProperty("qa_user_2_email"), CONFIG.getProperty("qa_user_2_salesforce_id"));	
		usersPage.clickFirstUserAccountLink(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		assertEquals(userIntelligentDialerTab.getGreetingOOOLabelText(driver1, oooCustomGreetingName), "true");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//navigate to Custom Greeting Tab
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver4);	
		
		//delete Custom Greeting
		softPhoneSettingsPage.deleteCustomGreeting(driver4, oooCustomGreetingName);
		
		//verifying that after deleting the selected greeting, default greeting should get selected
		assertTrue(softPhoneSettingsPage.isCustomGreetingSelected(driver4, "Default Greeting"));
		
		//Verify that Custom Greeting is deleted
		assertEquals(softPhoneSettingsPage.getCustomGreetingIndex(driver4, oooCustomGreetingName), -1);
		
		//verify that warning message is not appearing
		softPhoneSettingsPage.warningMessageInvisble(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void convert_to_ooo_greeting() {
		System.out.println("Test case --convert_to_ooo_greeting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String oldCustomGreetingName = "AutomationTestGreeting";
		
		//navigate to Custom Greeting Tab and delete existing greetings
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver4);		
		softPhoneSettingsPage.deleteAllCustomGreetings(driver4);

		//add a Custom Greeting
		softPhoneSettingsPage.createCustomGreeting(driver4, oldCustomGreetingName, 5);
		softPhoneSettingsPage.playCustomGreeting(driver4, oldCustomGreetingName);
	
		//navigate to Custom Greeting Tab
		reloadSoftphone(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver4);	
		
		//Update custom greeting to OOO type
		oooCustomGreetingName = oldCustomGreetingName;
		softPhoneSettingsPage.clickUpdateCustomGreeting(driver4, oooCustomGreetingName);
		softPhoneSettingsPage.checkOOOGreetingCheckBox(driver4);
		softPhoneSettingsPage.clickSaveCustomGreetingButton(driver4);
		
		//verify that custom greeting is converted to OOO type
		softPhoneSettingsPage.verifyOOOGreetingCreated(driver4, oooCustomGreetingName);
		
		//select the OOO greeting
		softPhoneSettingsPage.selectCustomGreeting(driver4, oooCustomGreetingName);
		
		//verify that warning message is appearing
		softPhoneSettingsPage.verifyOOOGreetingMessagePresent(driver4);
		
		//Update custom greeting from OOO type to custom greeting
		oooCustomGreetingName = oldCustomGreetingName;
		softPhoneSettingsPage.clickUpdateCustomGreeting(driver4, oooCustomGreetingName);
		softPhoneSettingsPage.uncheckOOOGreetingCheckBox(driver4);
		softPhoneSettingsPage.clickSaveCustomGreetingButton(driver4);
		
		//verify that greeting has been changes to custom greeting
		softPhoneSettingsPage.verifyGreetingIsNotOOO(driver4, oooCustomGreetingName);
		
		//verify that warning message is not appearing
		softPhoneSettingsPage.warningMessageInvisble(driver4);
		
		//delete Custom Greeting
		softPhoneSettingsPage.selectCustomGreeting(driver4, "Default Greeting");
		softPhoneSettingsPage.deleteCustomGreeting(driver4, oooCustomGreetingName);
		
		//Verify that Custom Greeting is deleted
		assertEquals(softPhoneSettingsPage.getCustomGreetingIndex(driver4, oooCustomGreetingName), -1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		
		System.out.println("Test case is pass");
	}
	

	@Test( groups={"MediumPriority"})
	public void verify_supervisor_notes_user_vm() {

		System.out.println("--verify_supervisor_notes_create_and_update--");

		String supervisorNotes;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//taking incoming call to agent 1
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));

		//declining call at agent 
		System.out.println("decline call from agent");
		softPhoneCalling.declineCall(driver1);
		softPhoneCalling.idleWait(10);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		//hangup if there are call is active
		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		//open task and navigate to call recordings page on web app
		supervisorNotes = "This is SupervisorNotes: " + helperFunctions.GetCurrentDateTime();
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.clickRecordingURL(driver1);
		
		//Update supervisor notes 
		callRecordingPage.updateSuperVisorNotes(driver1, supervisorNotes);
		
		//adding the delay required for data sync
		softPhoneCalling.idleWait(30);
		
		// verify supervisor notes in sfdc tasks
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.navigate().refresh();
		String taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver1);
		assertEquals(taskSupervisorNotes.trim(), supervisorNotes.trim());
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_supervisor_notes_user_group_vm() {
		System.out.println("Test case --verify_supervisor_notes_user_group_vm-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
	    //subscribe queues
	    softPhoneCallQueues.unSubscribeQueue(driver1, CONFIG.getProperty("qa_group_3_name"));  
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_3_number"));
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		seleniumBase.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.idleWait(3);
		reloadSoftphone(driver1);
		
		//verify missed call and vm count before
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//open task and navigate to call recordings page on web app
		String supervisorNotes = "This is SupervisorNotes: " + helperFunctions.GetCurrentDateTime();
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.clickRecordingURL(driver1);
		
		//Update supervisor notes 
		callRecordingPage.updateSuperVisorNotes(driver1, supervisorNotes);
		
		//adding the delay required for data sync
		softPhoneCalling.idleWait(30);
		
		// verify supervisor notes in sfdc tasks
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.navigate().refresh();
		String taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver1);
		assertEquals(taskSupervisorNotes.trim(), supervisorNotes.trim());
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 @AfterMethod(groups = {"Regression", "Product Sanity"})
	  public void afterMethod(ITestResult result){
		if (result.getStatus() == 2) {
			if (result.getName().equals("create_ooo_custom_greeting") || result.getName().equals("convert_to_ooo_greeting")) {
				//delete Custom Greeting
				reloadSoftphone(driver4);
				softPhoneSettingsPage.clickSettingIcon(driver4);
				softPhoneSettingsPage.navigateToCustomGreetingTab(driver4);	
				softPhoneSettingsPage.selectCustomGreeting(driver4, "Default Greeting");
				softPhoneSettingsPage.deleteCustomGreeting(driver4, oooCustomGreetingName);
			}
		}
	}
}
