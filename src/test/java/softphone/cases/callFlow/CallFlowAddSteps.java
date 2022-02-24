package softphone.cases.callFlow;

/**
 * @author Abhishek
 *
 */

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.callFlows.CallFlowPage;

/**
 * @author Abhishek
 *
 */
public class CallFlowAddSteps extends SoftphoneBase{

	@Test(groups={"Regression"})
	public void call_flow_vm_abandoned_call()
	{
		System.out.println("Test case --call_flow_vm_abandoned_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 		= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String userAccout			= CONFIG.getProperty("qa_user_account");
		String abandonedConnectUser	= CONFIG.getProperty("qa_user_3_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, abandonedConnectUser);
		callFlowPage.addVoicemailStep(driver1, CallFlowPage.VMAudioType.Texttospeech, "This is a test voicemail", CONFIG.getProperty("qa_group_1_name"));
		callFlowPage.enableAddLastStepSetting(driver1);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_to_add_steps_number"));
	    seleniumBase.idleWait(10);
	    
	    //Hang up call before user picks it up
	    softPhoneCalling.hangupIfInActiveCall(driver2);	    
	    seleniumBase.idleWait(10);
	   
	    //verify data for first agent
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	    String callSubject = "Inbound Call: +12095368843, to Queue: Auto Group LT1 - Last Step: Voicemail, Auto Group LT1";
	    callScreenPage.openCallerDetailPage(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driver4).contains(abandonedConnectUser));
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //remove all call flow steps
	    callFlowPage.removeAllCallFlowSteps(driver1);
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
	public void call_flow_dial_user_abandoned_call()
	{
		System.out.println("Test case --call_flow_dial_user_abandoned_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String dialStepCallSub	= "Call Subject From Call Flow";
		String dialStepCallcom	= "Call Comment From Call FLow";
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user				= CONFIG.getProperty("qa_user_2_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user, "15");
		callFlowPage.addDialStepCallNotes(driver1, dialStepCallSub, dialStepCallcom);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);
	    
	    //Hang up call before user picks it up
	    softPhoneCalling.hangupIfInActiveCall(driver2);	    
	   
	    //verify data for first agent
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    callScreenPage.openCallerDetailPage(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + ", " + dialStepCallSub);
	    sfTaskDetailPage.verifyCallAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    assertTrue(sfTaskDetailPage.getSubject(driver4).contains(dialStepCallSub));
	    assertTrue(sfTaskDetailPage.getComments(driver4).contains(dialStepCallcom));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //remove all call flow steps
	    callFlowPage.removeAllCallFlowSteps(driver1);
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
	public void call_flow_dial_user_group_abandoned_call()
	{
		System.out.println("Test case --call_flow_dial_user_group_abandoned_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String userGroup		= CONFIG.getProperty("qa_group_1_name").trim();

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.CallQueue, userGroup, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		//Open the agents softphone and subscribe the queue
		softPhoneCallQueues.openCallQueuesSection(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, userGroup);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.isPickCallBtnInvisible(driver4);
	    
	    //Hang up call before user picks it up
	    softPhoneCalling.hangupIfInActiveCall(driver2);	    
	   
	    //verify data for first agent
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //remove all call flow steps
	    callFlowPage.removeAllCallFlowSteps(driver1);
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
	public void call_flow_multi_dial_user()
	{
		System.out.println("Test case --call_flow_dial_user_abandoned_call-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_name");
		String user2			= CONFIG.getProperty("qa_user_3_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user1, "25");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user2, "25");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    
	    // opening user's page
	    dashboard.openManageUsersPage(driver1);
	    usersPage.OpenUsersSettingsWithSalesForceId(driver1, CONFIG.getProperty("qa_user_2_name"), CONFIG.getProperty("qa_user_2_email"), CONFIG.getProperty("qa_user_2_salesforce_id"));
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    
	    //verify that call is under processing
	    assertTrue(userIntelligentDialerTab.isUserOnCall(driver1));
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    seleniumBase.idleWait(25);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    
	    //verify that call is removed from processing
	    usersPage.openOverViewTab(driver1);
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    seleniumBase.idleWait(1);
	    assertFalse(userIntelligentDialerTab.isUserOnCall(driver1));
		
	    //Verify that call is tranfered to second caller
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver3));
	    softPhoneCalling.declineCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //open call flow page
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_dial_no_answer_drop_vm_user()
	{
		System.out.println("Test case --call_flow_dial_no_answer_drop_vm_user-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_name");
		String user2			= CONFIG.getProperty("qa_user_3_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user1, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		assertEquals(callFlowPage.getTimeOutInputText(driver1), "15");
		callFlowPage.addVoicemailStep(driver1,  CallFlowPage.VMAudioType.Texttospeech, "This is a test voicemail", user2);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    dashboard.openManageUsersPage(driver1);
	    usersPage.OpenUsersSettingsWithSalesForceId(driver1, CONFIG.getProperty("qa_user_2_name"), CONFIG.getProperty("qa_user_2_email"), CONFIG.getProperty("qa_user_2_salesforce_id"));
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    //verify that call is under processing
	    usersPage.idleWait(2);
	    assertTrue(userIntelligentDialerTab.isUserOnCall(driver1));
	    softPhoneCalling.declineCall(driver4);
	    
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify that call is removed from pricessing
	    usersPage.openOverViewTab(driver1);
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    usersPage.idleWait(2);
	    assertFalse(userIntelligentDialerTab.isUserOnCall(driver1));
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
	    
	    //verify data for first agent
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
	    contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    sfTaskDetailPage.verifyCallStatus(driver3, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver3);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_dial_no_answer_drop_vm_user_group()
	{
		System.out.println("Test case --call_flow_dial_no_answer_drop_vm_user_group-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user				= CONFIG.getProperty("qa_user_3_name");
		String userGroup		= CONFIG.getProperty("qa_group_1_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user, "15");
		callFlowPage.addVoicemailStep(driver1,  CallFlowPage.VMAudioType.Texttospeech, "This is a test voicemail", userGroup);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    dashboard.openManageUsersPage(driver1);
	    usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("qa_user_2_email"));
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    //verify that call is under processing
	    usersPage.idleWait(2);
	    assertTrue(userIntelligentDialerTab.isUserOnCall(driver1));
	    softPhoneCalling.declineCall(driver3);
	    
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify that call is removed from pricessing
	    usersPage.openOverViewTab(driver1);
	    userIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    seleniumBase.idleWait(1);
	    assertFalse(userIntelligentDialerTab.isUserOnCall(driver1));
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
	    
	    //verify data for first agent
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver4), "Inbound");
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
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
	public void call_flow_loop_step()
	{
		System.out.println("Test case --call_flow_dial_no_answer_drop_vm_user-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String loopList			= CONFIG.getProperty("qa_user_2_number") + "|" + CONFIG.getProperty("qa_user_3_number");
		String loopListVariable	= "PhoneNumbers";

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//configuring loop step
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.adddLoopStep(driver1, loopList, loopListVariable);
		callFlowPage.dropForEachValueFunction(driver1, CallFlowPage.CallFlowFunctions.Dial);
		callFlowPage.configureDialStepIndividualUser(driver1, "{{" + loopListVariable + "}}", "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);

		// receiving call on call flow
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    seleniumBase.idleWait(20);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    
	    //Verify that call is tranfered to second caller
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver3));
	    softPhoneCalling.declineCall(driver3);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_branch_step()
	{
		System.out.println("Test case --call_flow_branch_step-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_number");
		String user2			= CONFIG.getProperty("qa_user_3_number");
		String BranchKey		= "{{call.fromAreaCode}}";

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//configuring loop step
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.adddBranchStep(driver1, BranchKey);
		callFlowPage.dropbranchValueFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, CONFIG.getProperty("prod_user_1_number").trim().substring(2, 5));;
		callFlowPage.configureDialStepIndividualUser(driver1, user1, "15");
		callFlowPage.dropbranchValueFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, CONFIG.getProperty("prod_user_2_number").trim().substring(2, 5));;
		callFlowPage.configureDialStepIndividualUser(driver1, user2, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);

		// receiving call on call flow
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.declineCall(driver4);
	    seleniumBase.idleWait(10);
	    
	    //Verify that call is tranfered to second caller
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver3));
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_menu_step()
	{
		System.out.println("Test case --call_flow_menu_step-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_number");
		String user2			= CONFIG.getProperty("qa_user_3_number");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//configuring loop step
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.adddMenuStep(driver1, CallFlowPage.VMAudioType.Texttospeech, CallFlowPage.VMAudioType.Texttospeech);
		callFlowPage.dropMenuFunction(driver1, "1",CallFlowPage.CallFlowFunctions.Dial);
		callFlowPage.configureDialStepIndividualUser(driver1, user1, "15");
		callFlowPage.dropMenuFunction(driver1, "2",CallFlowPage.CallFlowFunctions.Dial);
		callFlowPage.configureDialStepIndividualUser(driver1, user2, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.clickDialPadIcon(driver2);
	    softPhoneCalling.enterNumberInDialpad(driver2, "2");

		// verifying that call is available to for "2" menu digit
	    softPhoneCalling.isDeclineButtonVisible(driver3);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver3));
	    
		// verifying that call is not available to for "1" menu digit
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    seleniumBase.idleWait(5);
	    softPhoneCalling.declineCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify for invalid response
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.clickDialPadIcon(driver2);
	    softPhoneCalling.enterNumberInDialpad(driver2, "3");
	    
	    //verify that no call appears for any user
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver3));
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_prompt_step()
	{
		System.out.println("Test case --call_flow_prompt_step-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_number");
		String user2			= CONFIG.getProperty("qa_user_3_number");
		String spacing			= "10";
		String maxDigit			= "3";
		String validResponse	= "102";
		String textMessage		= "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String secondValidResponse	= "201";

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//configuring loop step
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.adddPromptStep(driver1, CallFlowPage.VMAudioType.Texttospeech, CallFlowPage.VMAudioType.Texttospeech, spacing, maxDigit);
		callFlowPage.dropPromptFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, validResponse);
		callFlowPage.configureDialStepIndividualUser(driver1, user1, "15");
		callFlowPage.dropPromptFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, secondValidResponse);
		callFlowPage.configureDialStepIndividualUser(driver1, user2, "15");
		callFlowPage.dropNoResponseFunction(driver1, CallFlowPage.CallFlowFunctions.SMS);
		callFlowPage.configureSMSStep(driver1, user1, textMessage);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.clickDialPadIcon(driver2);
	    softPhoneCalling.enterNumberEachDigit(driver2, secondValidResponse);

		// verifying that call is available to for "2" menu digit
	    softPhoneCalling.isDeclineButtonVisible(driver3);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver3));
	    
		// verifying that call is not available to for "1" menu digit
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.declineCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify for invalid response
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.isMuteButtonEnables(driver2);
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify that no call appears for any user
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver4, 1);
	    
	    //verify that message is received by agent
	    softPhoneActivityPage.verifyInboundMessage(driver4, textMessage);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_prompt_multi_function()
	{
		System.out.println("Test case --call_flow_prompt_multi_function-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_name");
		String user2			= CONFIG.getProperty("qa_user_3_number");
		String spacing			= "10";
		String maxDigit			= "3";
		String validResponse	= "102";
		String textMsgNoAnswer	= "No Answer Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String textMsgAnyResp	= "Any response Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String secondValidResponse	= "201";

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//configuring loop step
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.adddPromptStep(driver1, CallFlowPage.VMAudioType.Texttospeech, CallFlowPage.VMAudioType.Texttospeech, spacing, maxDigit);
		callFlowPage.dropPromptFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, validResponse);
		callFlowPage.selectGroupFromDialSection(driver1, CallFlowPage.DialCallRDNATOCat.User, user1);
		callFlowPage.dropNoAnswerFunction(driver1, CallFlowPage.CallFlowFunctions.SMS);
		callFlowPage.configureSMSStep(driver1, user2, textMsgNoAnswer);
		callFlowPage.dropPromptFunction(driver1, CallFlowPage.CallFlowFunctions.Dial, secondValidResponse);
		callFlowPage.configureDialStepIndividualUser(driver1, user2, "15");
		callFlowPage.dropAnyResponseFunction(driver1, CallFlowPage.CallFlowFunctions.SMS);
		callFlowPage.configureSMSStep(driver1, user1, textMsgAnyResp);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.clickDialPadIcon(driver2);
	    seleniumBase.idleWait(1);
	    softPhoneCalling.enterNumberEachDigit(driver2, validResponse);

		// verifying that call is available to for "2" menu digit
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.declineCall(driver4);
	    
		// verifying that call is not available to for other prompt response
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify that no call appears for any user
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    
	    //verify that message is received by agent
	    softPhoneActivityPage.verifyInboundMessage(driver3, textMsgNoAnswer);
	    
	    //verify for invalid response
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.isMuteButtonEnables(driver2);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.clickDialPadIcon(driver2);
	    seleniumBase.idleWait(1);
	    softPhoneCalling.enterNumberEachDigit(driver2, "777");
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify that no call appears for any user
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver4, 1);
	    
	    //verify that message is received by agent
	    softPhoneActivityPage.verifyInboundMessage(driver4, textMsgAnyResp);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_time_holiday_schedule()
	{
		System.out.println("Test case --call_flow_time_holiday_schedule-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_number");
		String holidayName 		= "AutomationHolidaySchedule";
		String holidayDesc		= "AutoHolidayDesc";
		String holidayEventName = "AutoHolidayEvent";
		Calendar c = Calendar.getInstance();
		String todaysDate 		= new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		
		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		//opening overview tab
		dashboard.clickAccountsLink(driver1);
		accountOverviewtab.openOverViewTab(driver1);
		accountOverviewtab.deleteRecords(driver1, holidayName);
		accountOverviewtab.createHolidaySchedule(driver1, holidayName, holidayDesc, holidayEventName, todaysDate, todaysDate);
		accountOverviewtab.saveAcccountSettings(driver1);
		
		//Configure holiday schedule step
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addTimeStep(driver1, holidayName);
		callFlowPage.dropHoldayScheduleFunction(driver1, CallFlowPage.CallFlowFunctions.Dial);
		callFlowPage.configureDialStepIndividualUser(driver1, user1, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    
		// verifying that call is available to for "2" menu digit
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.declineCall(driver4);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_time_out_holiday_schedule()
	{
		System.out.println("Test case --call_flow_time_out_holiday_schedule-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_number");
		String holidayName 		= "AutomationHolidaySchedule";
		String holidayDesc		= "AutoHolidayDesc";
		String holidayEventName = "AutoHolidayEvent";
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String tomorrowsDate 		= new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		
		//Opening manage call flow and searching call flow
	    loginSupport(driver1);
		//opening overview tab
		dashboard.clickAccountsLink(driver1);
		accountOverviewtab.openOverViewTab(driver1);
		accountOverviewtab.deleteRecords(driver1, holidayName);
		accountOverviewtab.createHolidaySchedule(driver1, holidayName, holidayDesc, holidayEventName, tomorrowsDate, tomorrowsDate);
		accountOverviewtab.saveAcccountSettings(driver1);
		
		//Configure holiday schedule step
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
	    callFlowPage.removeAllCallFlowSteps(driver1);
		//callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addTimeStep(driver1, holidayName);
		callFlowPage.dropHoldayScheduleFunction(driver1, CallFlowPage.CallFlowFunctions.Dial);
		callFlowPage.configureDialStepIndividualUser(driver1, user1, "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(10);
	    
		// verifying that call is available to for "2" menu digit
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_first_step_vm_user()
	{
		System.out.println("Test case --call_flow_first_step_vm_user-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_3_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addVoicemailStep(driver1,  CallFlowPage.VMAudioType.Texttospeech, "This is a test voicemail", user1);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verify data for first agent
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
	    contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    sfTaskDetailPage.verifyCallStatus(driver3, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver3);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_flow_first_step_vm_group()
	{
		System.out.println("Test case --call_flow_first_step_vm_group-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String userGroup		= CONFIG.getProperty("qa_group_1_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addVoicemailStep(driver1,  CallFlowPage.VMAudioType.Texttospeech, "This is a test voicemail", userGroup);
		callFlowPage.saveCallFlowSettings(driver1);
		
		// receiving call on call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	    contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver4), "Inbound");
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
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
	public void call_flow_dial_apex_agent_id()
	{
		System.out.println("Test case --call_flow_dial_apex_agent_id-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String userAccout		= CONFIG.getProperty("qa_user_account");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
		//add a step to dial apex agent
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.dragAndDropDialImage(driver1);
		callFlowPage.configureDialStepIndividualUser(driver1, "{{apex.agentID}", "15");
		callFlowPage.saveCallFlowSettings(driver1);
		
		//opening again the cal flow and saving it
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		callFlowPage.saveCallFlowSettings(driver1);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = {"Regression"})
	public void _afterMethod(ITestResult result) {
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			// remove all call flow steps
			String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
			String userAccout		= CONFIG.getProperty("qa_user_account");
			loginSupport(driver1);
			dashboard.navigateToManageCallFlow(driver1);
			callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
			callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
			callFlowPage.removeAllCallFlowSteps(driver1);
		}
	}
}