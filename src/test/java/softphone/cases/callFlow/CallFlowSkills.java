package softphone.cases.callFlow;

/**
 * @author Abhishek
 *
 */

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.callFlows.CallFlowPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallFlowSkills extends SoftphoneBase{
	
	@Test(groups={"Regression"})
	public void call_flow_user_skills() {

		System.out.println("Test case --call_flow_user_skills-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();
		String driverCallReceived = null;
		WebDriver driverReceived  = null;

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	  //picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	softPhoneCalling.pickupIncomingCall(driver1);
	    	driverCallReceived = "driver1";
	    	driverReceived = driver1;
	    }else{
	    	softPhoneCalling.pickupIncomingCall(driver4);
	    	driverCallReceived = "driver4";
	    	driverReceived = driver4;
	    }
	    
	    //disconnect call
	    softPhoneCalling.hangupActiveCall(driverReceived);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driverReceived);
	    
	    //verify that call is received by driver1 which has highest skill
	    assertEquals(driverCallReceived, "driver1");
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups={"Regression"})
	  public void user_skills_agent_recevied_then_last()
	  {
	    System.out.println("Test case --user_skills_agent_recevied_then_last()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_2_call_flow_number").trim();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    String driverCallReceived = null;
	    
		// making first call to Call Flow with Skill
	    System.out.println("making first call to Call Flow with Skill");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	driverCallReceived = "driver1";
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	driverCallReceived = "driver3";
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		// making second call to Call Flow with Skill
	    System.out.println("making second call to Call Flow with Skill");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);	    
	    
	    //verifying that call is appearing to other device and declining the call
	    System.out.println("picking up with the first device");
	    if(!driverCallReceived.equals("driver1")){
	    	driverCallReceived = "driver1";
	    	driverCallReceived2 = driver1;
	    }else{
	    	driverCallReceived = "driver3";
	    	driverCallReceived2 = driver3;
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    //verifying that call is coming last to the agent who received the call first
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //verify data for first agent
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived1);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + " - Last Step: Dial, AutomationSkill2";
	    callScreenPage.openCallerDetailPage(driverCallReceived1);
	    contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived1);
	    sfTaskDetailPage.verifyCallStatus(driverCallReceived1, "Connected"); 
	    sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived1), "Completed");
	    assertEquals(sfTaskDetailPage.getSubject(driverCallReceived1), callSubject);
	    seleniumBase.closeTab(driverCallReceived1);
	    seleniumBase.switchToTab(driverCallReceived1, 1);
	  
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void user_skills_image_busy()
	  {
	    System.out.println("Test case --user_skills_image_busy()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_2_call_flow_number").trim();
	    
		// Setting user image busy
		System.out.println("Setting user image busy");
		callScreenPage.setUserImageBusy(driver1);
	    
		// making first call to Call Flow with Skill
	    System.out.println("making first call to Call Flow with Skill");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    
		//Call comes on second device
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.isCallBackButtonVisible(driver2);

	    // again make call to confirm that call is coming to other agent only
	    System.out.println("making first call to Call Flow with Skill");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    seleniumBase.idleWait(5);
	    
	    //verifying that call is coming to other agent
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //verify data for agent
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + " - Last Step: Dial, AutomationSkill2";
	    callScreenPage.openCallerDetailPage(driver3);
	    contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected"); 
	    sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver3);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getTaskStatus(driver3), "Completed");
	    assertEquals(sfTaskDetailPage.getSubject(driver3), callSubject);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
		// Setting user image available
		System.out.println("Setting user image busy");
		callScreenPage.setUserImageAvailable(driver1);
	  
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "Product Sanity"})
	public void different_user_skills_agent_ignores() {

		System.out.println("Test case --different_user_skills_first_ignores-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //ignoring call from the first device
	    System.out.println("ignoring call from the first device");
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);
	    
	    //verifying that call is coming to other agent
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + " - Last Step: Dial, "+ CONFIG.getProperty("qa_user_skill_1");
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups={"Regression"})
	public void different_user_skills_agent_busy() {
		System.out.println("Test case --different_user_skills_agent_busy-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();
		
		//Make user with highest skill busy on  another call
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver1);

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);

	    //verifying that call is coming to other agent directly
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + " - Last Step: Dial, AutomationSkill1";
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //disconnect call from first user
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups={"Regression"})
	public void different_user_skills_vm_drop_last_agent() {
		System.out.println("Test case --different_user_skills_vm_drop_last_agent-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //ignoring call from the first device
	    System.out.println("ignoring call from the first device");
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);
	    
	    //ignoring call from other device as well
	    System.out.println("ignoring call from other device as well");
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);
	    
	    //disconnect call after dropping vm
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//verify data for the call
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups={"Regression"})
	public void different_user_skills_agent_logout() {
		System.out.println("Test case --different_user_skills_agent_logout-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();
		
		//Make user with highest skill busy on  another call
	    softPhoneSettingsPage.logoutSoftphone(driver1);

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);

	    //verifying that call is coming to other agent directly
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
		//verify that voicemail play icon not appearing for second caller
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + " - Last Step: Dial, AutomationSkill1";
	    callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //login back first user
	    SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
	

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_different_skill_level_after_decline_call() {
		System.out.println("Test case --verify_multiple_users_with_different_skill_level_after_decline_call-- started ");
		
		//initialising drivers
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //declining call from the first device
	    System.out.println("ignoring call from the first device");
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
	    softPhoneCalling.declineCall(driver1);
	    
	    //verifying that call is coming to the agent with second highest skill
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);

		assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driver4), "Completed");
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver4);
		sfTaskDetailPage.closeTab(driver4);
		sfTaskDetailPage.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test case --verify_multiple_users_with_different_skill_level_after_decline_call-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_different_skill_level_after_higher_skill_set_busy() {
		System.out.println("Test case --verify_multiple_users_with_different_skill_level_after_higher_skill_set_busy-- started ");
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();
		
		//set user1 image busy
		callScreenPage.setUserImageBusy(driver1);

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //verify that call is not appearing for the highest skill caller as he is busy
	    System.out.println("ignoring call from the first device");
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));
	    
	    //verifying that call is coming to the agent with second highest skill
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);

		assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driver4), "Completed");
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver4);
		sfTaskDetailPage.closeTab(driver4);
		sfTaskDetailPage.switchToTab(driver4, 1);
		
		//set user1 image Available
		callScreenPage.setUserImageAvailable(driver1);

		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_multiple_users_with_different_skill_level_after_decline_call-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_hold_resume_merge_call_from_skill() {
		System.out.println("Test case --verify_hold_resume_merge_call_from_skill-- started ");
		
		//initialising drivers
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_number").trim();

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //Verify that call is not present for user with second highest call skills
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
	    
	    //declining call from the first device
	    System.out.println("declining call from the first device");
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
	    softPhoneCalling.declineCall(driver1);
	    
	    //verifying that call is coming to the agent with second highest skill
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	  //making an outbound call
  		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
  		softPhoneCalling.isCallHoldButtonVisible(driver1);
  		
  		softPhoneCalling.isAcceptCallButtonVisible(driver4);
  		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hold, resume and merge call
		softPhoneCalling.clickHoldButton(driver4);
		softPhoneCalling.clickOnHoldButton(driver4);
		softPhoneCalling.clickResumeButton(driver4);
		softPhoneCalling.clickOnHoldButton(driver4);
		softPhoneCalling.clickMergeButton(driver4);
		softPhoneCalling.idleWait(2);

		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver4);

		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test case --verify_hold_resume_merge_call_from_skill-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_dial_to_skill_associated_with_single_user() {
		System.out.println("Test case --verify_dial_to_skill_associated_with_single_user-- started ");

		//initialising drivers
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = CONFIG.getProperty("qa_user_skill_call_flow_lp_number").trim();

		//Making an outbound call from softphone
	    System.out.println("caller making call to user skill call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.idleWait(5);
	    
	    //verifying that call is coming to the agent with second highest skill
	    System.out.println("picking up with the first device");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //Hangup active call
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	  //Open call entry in salesforce and verify details
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);

		assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driver4), "Completed");
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver4);
		sfTaskDetailPage.closeTab(driver4);
		sfTaskDetailPage.switchToTab(driver4, 1);

		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_dial_to_skill_associated_with_single_user-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_caller_end_skill_after_call_ringing() {
		System.out.println("Test case --verify_caller_end_skill_after_call_ringing-- started ");
		
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//enable call forwarding
	    softPhoneCalling.switchToTab(driver4, 1);
	    softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.setCallForwardingNumber(driver4, driver5, "", CONFIG.getProperty("prod_user_2_number"));
		
		//calling from agent user to call flow number
		String skillNumber = CONFIG.getProperty("qa_user_skill_call_flow_lp_number").trim();

		softPhoneCalling.softphoneAgentCall(driver2, skillNumber);
		callScreenPage.verifyUserImageBusy(driver2);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(driver2));

		//verifying on skill user
		dashboard.switchToTab(driver4, 1);
		callScreenPage.verifyUserImageBusy(driver4);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(driver4));
		
		//verifying call on forwarding driver
		softPhoneCalling.isAcceptCallButtonVisible(driver5);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver5));
		
		softPhoneCalling.hangupIfInActiveCall(driver2);
			
		// verifying call on forwarding agent
		softPhoneCalling.idleWait(2);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver5));
		
		//disable call forwarding
	    softPhoneCalling.switchToTab(driver4, 1);
	    softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);
		
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test case --verify_caller_end_skill_after_call_ringing-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_level_when_lwa_do_not_pick_call() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_level_when_lwa_do_not_pick_call-- started ");
		
		//initialize drivers
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		// verifying that call is appearing to other device and declining the call
		System.out.println("picking up with the first device");
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		String frwrdingNumberSkype = CONFIG.getProperty("skype_number");
		
		// enabling call forwarding skype number with dnd setting off
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driverCallReceived1);
		softPhoneSettingsPage.selectForwardingNumberAccToIndex(driverCallReceived1, 0);
		softPhoneSettingsPage.editCallForwardingNumbers(driverCallReceived1, frwrdingNumberSkype);
		softPhoneSettingsPage.unCheckDoNotFrwrdWhenLogoutCheckBox(driverCallReceived1);

		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);

		// picking up with the first device
		softPhoneCalling.isCallHoldButtonVisible(driverCallReceived1);
		softPhoneCalling.verifyCallHoldButtonInvisible(driverCallReceived1);

		// verifying that call is appearing to other device and declining the call
		System.out.println("picking up with the first device");
		softPhoneCalling.isDeclineButtonVisible(driverCallReceived2);
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(driverCallReceived2);
		
		// enabling call forwarding skype number with dnd setting off
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived2);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);	
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);

		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived2), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived2), "Completed");
		sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived2);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived2);
		sfTaskDetailPage.closeTab(driverCallReceived2);
		sfTaskDetailPage.switchToTab(driverCallReceived2, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.disableCallForwardingSettings(driverCallReceived1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_multiple_users_with_same_skill_level_when_lwa_do_not_pick_call-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_when_lwa_busy_on_call() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_busy_on_call-- started ");
		
		//initialize drivers
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		String callReceivedNumber1 = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
			callReceivedNumber1 = CONFIG.getProperty("qa_admin_user_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
			callReceivedNumber1 = CONFIG.getProperty("qa_support_user_number");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.isCallBackButtonVisible(agentDriver);

		// making second call to Call Flow with Skill
		System.out.println("making second call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// verifying that call is appearing to other device and declining the call
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		//making lwa busy on call
		System.out.println("making first call to Call Flow with Skill");
		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentCall(driver4, callReceivedNumber1);
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// verifying call now present on second lwa
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    
		//get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driverCallReceived2);
		callToolsPanel.enterCallNotesText(driverCallReceived2, callNotes);
		callToolsPanel.appendCallNotesSubject(driverCallReceived2, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(driverCallReceived2);
		callToolsPanel.clickCallNotesSaveBtn(driverCallReceived2);
		callToolsPanel.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		//saving contact if not already
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		if (callScreenPage.isCallerUnkonwn(driverCallReceived2)) {
			callToolsPanel.callNotesSectionVisible(driverCallReceived2);
			callScreenPage.addCallerAsLead(driverCallReceived2, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		}

		//Navigating to task detail page to check url
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived2);
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived2, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived2);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived2);

		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived2), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived2), "Completed");
		sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived2);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived2);
		sfTaskDetailPage.closeTab(driverCallReceived2);
		sfTaskDetailPage.switchToTab(driverCallReceived2, 1);
		
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_busy_on_call-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_when_lwa_no_answer_enabled() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_no_answer_enabled-- started ");
		
		//initialize drivers
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//login as agent user
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.isCallBackButtonVisible(agentDriver);

		// making second call to Call Flow with Skill
		System.out.println("making second call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// verifying that call is appearing to other device and declining the
		// call
		System.out.println("picking up with the first device");
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		//enabling no answer number on lwa
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.setNoAnswerNumber(driverCallReceived1, CONFIG.getProperty("qa_user_2_number"));
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		softPhoneCalling.idleWait(5);

		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		// verifying call now present on second lwa
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		//get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driverCallReceived2);
		callToolsPanel.enterCallNotesText(driverCallReceived2, callNotes);
		callToolsPanel.appendCallNotesSubject(driverCallReceived2, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(driverCallReceived2);
		callToolsPanel.clickCallNotesSaveBtn(driverCallReceived2);
		callToolsPanel.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);

		//saving contact if not already
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		if (callScreenPage.isCallerUnkonwn(driverCallReceived2)) {
			callToolsPanel.callNotesSectionVisible(driverCallReceived2);
			callScreenPage.addCallerAsLead(driverCallReceived2, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		}

		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived2);
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived2, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived2);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived2);

		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived2), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived2), "Completed");
		sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived2);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived2);
		sfTaskDetailPage.closeTab(driverCallReceived2);
		sfTaskDetailPage.switchToTab(driverCallReceived2, 1);
		
		//enabling no answer number on lwa
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.disableNoAnswerSettings(driverCallReceived1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_no_answer_enabled-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_when_lwa_logout() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_logout-- started ");
		
		//initialize drivers
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentCall(driver4, skillNumber);
		seleniumBase.idleWait(5);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// making second call to Call Flow with Skill
		System.out.println("making second call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(driver4, skillNumber);
		seleniumBase.idleWait(5);

		// verifying that call is appearing to other device and declining the
		// call
		System.out.println("picking up with the first device");
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(driver4);
		
		//logging out lwa softphone
		softPhoneSettingsPage.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
		softPhoneSettingsPage.logoutSoftphone(driverCallReceived1);

		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(driver4, skillNumber);
		seleniumBase.idleWait(5);

		// verifying call now present on second lwa
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		//get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driverCallReceived2);
		callToolsPanel.enterCallNotesText(driverCallReceived2, callNotes);
		callToolsPanel.idleWait(1);
		callToolsPanel.appendCallNotesSubject(driverCallReceived2, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(driverCallReceived2);
		callToolsPanel.clickCallNotesSaveBtn(driverCallReceived2);
		callToolsPanel.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(driver4);

		//saving contact if not already
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		if (callScreenPage.isCallerUnkonwn(driverCallReceived2)) {
			callToolsPanel.callNotesSectionVisible(driverCallReceived2);
			callScreenPage.addCallerAsLead(driverCallReceived2, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		}

		//Navigating to task detail page to check url
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived2);
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived2, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived2);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived2);

		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived2), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived2), "Completed");
		sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived2);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived2);
		sfTaskDetailPage.closeTab(driverCallReceived2);
		sfTaskDetailPage.switchToTab(driverCallReceived2, 1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_logout-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_when_lwa_logout_with_cf_and_dnd_setting_on() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_logout_with_cf_and_dnd_setting_on-- started ");
		
		//initialize drivers
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.isCallBackButtonVisible(agentDriver);

		// making second call to Call Flow with Skill
		System.out.println("making second call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// verifying that call is appearing to other device and declining the
		// call
		System.out.println("picking up with the first device");
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		String frwrdingNumberSkype = CONFIG.getProperty("skype_number");
		
		//enabling call forwarding skype number with dnd setting on
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driverCallReceived1);
		softPhoneSettingsPage.selectForwardingNumberAccToIndex(driverCallReceived1, 0);
		softPhoneSettingsPage.editCallForwardingNumbers(driverCallReceived1, frwrdingNumberSkype);
		softPhoneSettingsPage.checkDoNotFrwrdWhenLogoutCheckBox(driverCallReceived1);
		
		//logging out lwa softphone
		softPhoneSettingsPage.logoutSoftphone(driverCallReceived1);

		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		seleniumBase.idleWait(5);

		// verifying call now present on second lwa
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		//get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driverCallReceived2);
		callToolsPanel.enterCallNotesText(driverCallReceived2, callNotes);
		callToolsPanel.appendCallNotesSubject(driverCallReceived2, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(driverCallReceived2);
		callToolsPanel.clickCallNotesSaveBtn(driverCallReceived2);
		callToolsPanel.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);

		//saving contact if not already
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		if (callScreenPage.isCallerUnkonwn(driverCallReceived2)) {
			callToolsPanel.callNotesSectionVisible(driverCallReceived2);
			callScreenPage.addCallerAsLead(driverCallReceived2, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		}

		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived2);
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived2, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived2);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived2);

		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived2), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived2), "Completed");
		sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived2);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived2);
		sfTaskDetailPage.closeTab(driverCallReceived2);
		sfTaskDetailPage.switchToTab(driverCallReceived2, 1);
		
		resetApplication();
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.disableCallForwardingSettings(driverCallReceived1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_logout_with_cf_and_dnd_setting_on-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multiple_users_with_same_skill_when_lwa_on_hold_call() {
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_on_hold_call-- started ");
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// delete first voice mail
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softphoneCallHistoryPage.switchToTab(driver4, 1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickMyCallsLink(driver4);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(driver4, 1);
		}

		String skillNumber = CONFIG.getProperty("qa_user_same_skill_call_flow_number");

		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		String driverCallReceived = null;
		String callReceivedNumber1 = null;
		
		// making first call to Call Flow with Skill
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(driver1, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(adminDriver)) {
			driverCallReceived = "adminDriver";
			driverCallReceived1 = adminDriver;
			callReceivedNumber1 = CONFIG.getProperty("qa_admin_user_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(webSupportDriver)) {
			driverCallReceived = "webSupportDriver";
			driverCallReceived1 = webSupportDriver;
			callReceivedNumber1 = CONFIG.getProperty("qa_support_user_number");
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// making second call to Call Flow with Skill
		System.out.println("making second call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(driver1, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		// verifying that call is appearing to other device and declining the
		// call
		System.out.println("picking up with the first device");
		if (!driverCallReceived.equals("adminDriver")) {
			driverCallReceived = "adminDriver";
			driverCallReceived2 = adminDriver;
		} else {
			driverCallReceived = "webSupportDriver";
			driverCallReceived2 = webSupportDriver;
		}
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.hangupActiveCall(driver1);
		
		//making lwa on hold call
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentCall(driver4, callReceivedNumber1);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		softPhoneCalling.clickHoldButton(driverCallReceived1);
		callScreenPage.verifyUserImageBusy(driverCallReceived1);
		assertTrue(softPhoneSettingsPage.isSettingsIconVisible(driverCallReceived1));
		softPhoneCalling.clickDialPadIcon(driverCallReceived1);
	
		// making first call to Call Flow with Skill
		seleniumBase.idleWait(2);
		System.out.println("making first call to Call Flow with Skill");
		softPhoneCalling.softphoneAgentCall(driver1, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		// verifying call present on second lwa
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		softPhoneCalling.declineCall(driverCallReceived2);
	    
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(5);
		
		// Open call entry for the users to whom voicemail is dropped
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickMyCallsLink(driver4);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test case --verify_multiple_users_with_same_skill_when_lwa_on_hold_call-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_dial_to_skill_associated_with_no_user() {
		System.out.println("Test case --verify_dial_to_skill_associated_with_no_user-- started ");
		driverUsed.put("driver4", true);
		
		loginSupport(driver4);
		dashboard.clickOnUserProfile(driver4);
		usersPage.clickAccountLink(driver4);
		accountSkillsTab.navigateToAccountSkillsTab(driver4);
		String skill_1 = "AutoSkill".concat(HelperFunctions.GetRandomString(4));
		accountSkillsTab.addNewSkill(driver4, skill_1);

		//adding skill on call flow
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, "");
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		callFlowPage.dragAndDropDialImage(driver4);
		callFlowPage.selectGroupFromDialSection(driver4, CallFlowPage.DialCallRDNATOCat.Skill, skill_1);
		callFlowPage.saveCallFlowSettings(driver4);
		
		// login as agent user
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent user to call flow number
		String callFlowNumber = CONFIG.getProperty("low_priority_call_flow_number");
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);

		// verifying call not present on user1
		softPhoneCalling.switchToTab(driver4, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		softPhoneCalling.hangupIfInActiveCall(agentDriver);

		driverUsed.put("driver4", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_dial_to_skill_associated_with_no_user-- passed");
	}
	
	@AfterMethod(groups={"Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity"}, dependsOnMethods = {"resetSetupDefault"})
	public void  afterMethod(ITestResult result){
		if(result.getStatus() == 2 || result.getStatus() == 3) {
			if(result.getName().equals("verify_caller_end_skill_after_call_ringing()")){
				
				initializeDriverSoftphone("driver4");
				driverUsed.put("driver4", true);
				
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(driver4);
				softPhoneSettingsPage.disableCallForwardingSettings(driver4);		
			}else if(result.getName().equals("verify_multiple_users_with_same_skill_level_when_lwa_do_not_pick_call")){
				
				initializeDriverSoftphone("webSupportDriver");
				driverUsed.put("webSupportDriver", true);
				initializeDriverSoftphone("adminDriver");
				driverUsed.put("adminDriver", true);
				
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
				softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);
				
				softPhoneSettingsPage.clickSettingIcon(adminDriver);
				softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
			}
		}
	}
}