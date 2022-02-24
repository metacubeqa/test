package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftphoneCallHistoryPage.CallHistoryFiedls;
import support.source.accounts.AccountIntelligentDialerTab;
import utility.HelperFunctions;

public class GroupCallHistory extends SoftphoneBase{
	
	@BeforeClass(groups = {"Regression", "MediumPriority"})
	public void enableGroupCallHistory() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open Support Page and disable hide group call history setting
	  	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	driverUsed.put("driver1", false);
	}
	
	@Test(groups={"MediumPriority"})
	  public void group_call_entry_not_in_call_history()
	  {
	    System.out.println("Test case --verify_group_call_entry_not_in_call_history()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    aa_AddCallersAsContactsAndLeads();
	    
	    // making call to a random user
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number").trim());
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber 	= CONFIG.getProperty("qa_group_1_number").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    
		//accepting call from agent
	    String callPickTime		= HelperFunctions.GetCurrentDateTime("h:mm", true);
	    String callPickTime1	= HelperFunctions.GetCurrentDateTime("h:mm", false);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    String callerName 		= callScreenPage.getCallerName(driver1);
	    Date callStartTime		= HelperFunctions.parseStringToDateFormat(HelperFunctions.GetCurrentDateTime("h:mm:ss", false),"h:mm:ss");
	    
	    //disconnecting call from second agent
	    System.out.println("disconnecting call from  agent");
	    seleniumBase.idleWait(10);
	    Date callEndTime = HelperFunctions.parseStringToDateFormat(HelperFunctions.GetCurrentDateTime("h:mm:ss", false),"h:mm:ss");
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Calculate Call Duration
		SimpleDateFormat newDateTimeFormatter = new SimpleDateFormat("mm:ss");
		String callDuration = newDateTimeFormatter.format(Date.from(Instant.ofEpochMilli(callEndTime.getTime() - callStartTime.getTime())));
		int callDurationInTZ = Integer.parseInt(HelperFunctions.getDateTimeInTimeZone(callDuration, "UTC", "ss"));

		//verify call data and spinner wheel appears when navigating to group calls history
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.clickGroupsCalls(driver1);
		softphoneCallHistoryPage.waitUntilVisible(driver1, seleniumBase.spinnerWheel);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    String callerPhone = CONFIG.getProperty("prod_user_1_number").trim();
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	String callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//verify call duration on call history page
		int actualCallDuration = Integer.parseInt(softphoneCallHistoryPage.getCallDuration(driver1, 0).substring(3,4));
		assertTrue(actualCallDuration > (callDurationInTZ - 2) || actualCallDuration < (callDurationInTZ + 2));
		
		//verify that call doesn't comes on my call history page
		softphoneCallHistoryPage.clickMyCallsLink(driver1);
		assertFalse(callerPhone.contains(softphoneCallHistoryPage.getHistoryCallerPhone(driver1, 0)));
		assertFalse(callerName.contains(softphoneCallHistoryPage.getHistoryCallerName(driver1, 0)));
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void verify_group_call_entry_call_not_picked()
	  {
	    System.out.println("Test case --verify_group_call_entry_call_not_picked()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber 	= CONFIG.getProperty("qa_group_1_number").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, queueName);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    
		//accepting call from agent
	    String callPickTime		= HelperFunctions.GetCurrentDateTime("h:mm", true);
	    String callPickTime1	= HelperFunctions.GetCurrentDateTime("h:mm", false);
	    
	    //disconnecting call from second agent
	    System.out.println("disconnecting call from  agent");
	    seleniumBase.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//verify call data for caller 1
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
		seleniumBase.idleWait(3);
		
	    String callerPhone = CONFIG.getProperty("prod_user_1_number").trim();
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	String callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
	 	
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		assertEquals(softphoneCallHistoryPage.getCallDuration(driver1, 0), "00:00");
		
		//verify that call doesn't comes on my call history page
		softphoneCallHistoryPage.clickMyCallsLink(driver1);
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
		assertFalse(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//verify call data for caller 2
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.switchToMissedCallsTab(driver3);
		seleniumBase.idleWait(3);
		
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver3, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver3, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//verify call data for caller 3
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.switchToMissedCallsTab(driver4);
		seleniumBase.idleWait(3);
		
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver4, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver4, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//provide disposition to a call and verify it in group history page
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
		String Disposition = callToolsPanel.selectDisposition(driver1, 0);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
		assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), Disposition);

	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	public void verify_group_history_call_flow_dial(){
		System.out.println("Test case --group_history_call_flow_dial()-- started ");
		
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
		
		String queueName 		= CONFIG.getProperty("qa_group_1_name").trim();
	    String callFLowNumber 	= CONFIG.getProperty("qa_call_flow_call_queue_smart_number").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, queueName);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFLowNumber);
	    
		//accepting call from agent
	    String callPickTime		= HelperFunctions.GetCurrentDateTime("h:mm", true);
	    String callPickTime1	= HelperFunctions.GetCurrentDateTime("h:mm", false);
	    
	    //disconnecting call from second agent
	    System.out.println("disconnecting call from  agent");
	    seleniumBase.idleWait(25);
	    softPhoneCallQueues.queueCountInvisible(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		
	    String callerPhone = CONFIG.getProperty("prod_user_1_number").trim();
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);

		//verify call data for caller 1
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		
	 	String callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0); 	
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		assertEquals(softphoneCallHistoryPage.getCallDuration(driver1, 1), "00:00");
		
		softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
		seleniumBase.idleWait(3);
	 	callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0); 	
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		assertEquals(softphoneCallHistoryPage.getCallDuration(driver1, 1), "00:00");
		
		//verify that call doesn't comes on my call history page
		softphoneCallHistoryPage.clickMyCallsLink(driver1);
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
		assertFalse(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//verify call data for caller 2
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver3, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver3, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		softphoneCallHistoryPage.switchToMissedCallsTab(driver3);
		seleniumBase.idleWait(3);
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver3, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver3, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//verify call data for caller 3
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver4, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver4, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		softphoneCallHistoryPage.switchToMissedCallsTab(driver4);
		seleniumBase.idleWait(3);
		
		callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver4, 0);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver4, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_group_call(){
		System.out.println("Test case --verify_disposition_required_group_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int myCallsVmCounts = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
		int groupCallsVmCounts = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.openNewBlankTab(driver1);
		seleniumBase.switchToTab(driver1, 2);
		driver1.get(CONFIG.getProperty("qa_support_tool_site"));
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCallQueues.unSubscribeQueue(driver1, CONFIG.getProperty("qa_group_3_name"));
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_3_number"));
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.idleWait(15);
	 	softPhoneCalling.hangupIfInActiveCall(driver2);
	 	
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, myCallsVmCounts - 1);
		softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
		
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, groupCallsVmCounts);
		softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
		
		softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
		softphoneCallHistoryPage.playVMByIndex(driver1, 1);
		seleniumBase.idleWait(5);
		softphoneCallHistoryPage.playVMByIndex(driver1, 1);
		seleniumBase.idleWait(5);
		
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, groupCallsVmCounts - 1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.openNewBlankTab(driver1);
	 	seleniumBase.switchToTab(driver1, 2);
	 	driver1.get(CONFIG.getProperty("qa_support_tool_site"));
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups={"MediumPriority"})
	  public void verify_group_call_entry_without_navigation()
	  {
	    System.out.println("Test case --verify_group_call_entry_without_navigation()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber 	= CONFIG.getProperty("qa_group_1_number").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Switch to group call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    
		//accepting call from agent
	    String callPickTime		= HelperFunctions.GetCurrentDateTime("h:mm", true);
	    String callPickTime1	= HelperFunctions.GetCurrentDateTime("h:mm", false);
	    
	    //disconnecting call from second agent
	    System.out.println("disconnecting call from  agent");
	    seleniumBase.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(5);

		//verify call data for caller 1
	    String callerPhone = CONFIG.getProperty("prod_user_1_number").trim();
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	String callTime = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
	 	
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
		assertTrue(callTime.contains(callPickTime) || callTime.contains(callPickTime1));
		assertEquals(softphoneCallHistoryPage.getCallDuration(driver1, 0), "00:00");
		
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void verify_group_call_history_hide()
	  {
	    System.out.println("Test case --verify_group_call_history_hide()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    // receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number").trim());
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//Switch to group call history page
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		seleniumBase.idleWait(3);
		
		List<String> groupCallerPhoneNumberList = softphoneCallHistoryPage.getHistoryCallerPhoneList(driver1);
		List<String> groupCallerCallTimeList = softphoneCallHistoryPage.getHistoryCallerTimeList(driver1);
	    
	    // open Support Page and enable hide group call history setting
	    loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.enableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber 	= CONFIG.getProperty("qa_group_1_number").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    
	    //disconnecting call from second agent
	    System.out.println("disconnecting call from  agent");
	    seleniumBase.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Switch to group call history page
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		
		//verify that group call data is not appearing here
		for(int i = 0; i < groupCallerPhoneNumberList.size(); i++) {
			for(int j = 0; j < groupCallerPhoneNumberList.size(); j++) {
			assertFalse(softphoneCallHistoryPage.getHistoryCallerPhone(driver1, j).equals(groupCallerPhoneNumberList.get(i)) && softphoneCallHistoryPage.getHistoryCallerTime(driver1, j).equals(groupCallerCallTimeList.get(i)));
			}
		}

		//verify recent call data
	    String callerPhone = CONFIG.getProperty("prod_user_1_number").trim();
	    assertFalse(callerPhone.contains(softphoneCallHistoryPage.getHistoryCallerPhone(driver1, 0)));
	    
	  	//open Support Page and disable hide group call history setting
	  	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
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
	
	@AfterMethod(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void afterMethod(ITestResult result){
		if ((result.getStatus() == 2 || result.getStatus() == 3)) {
			if (result.getName() == "verify_group_call_history_hide") {
				enableGroupCallHistory();
			} else if(result.getName() == "verify_disposition_required_group_call"){
				// open Support Page and disable call disposition prompt setting
				seleniumBase.openNewBlankTab(driver1);
				seleniumBase.switchToTab(driver1, 2);
				driver1.get(CONFIG.getProperty("qa_support_tool_site"));
				dashboard.clickAccountsLink(driver1);
				System.out.println("Account editor is opened ");
				accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
				accountIntelligentDialerTab.disableCallToolsSetting(driver1);
				accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
				accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
				accountIntelligentDialerTab.saveAcccountSettings(driver1);
				seleniumBase.closeTab(driver1);
				seleniumBase.switchToTab(driver1, 1);
				reloadSoftphone(driver1);
			}
		}
	}
}
