/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class SipClient extends SoftphoneBase{
	
	WebDriver sipDriver = null;
	
	@BeforeClass(groups = { "Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity" })
	public void beforeClass() {
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		sipDriver = getDriver();
		sipCallingPage.loginToSipApp(sipDriver, CONFIG);
	
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, sipDriver, "SIP Number", "sip:" + CONFIG.getProperty("sip_account_username"));

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}

	//Verify inbound call on hold with SIP call forwarding enabled
	@Test(groups = {"Regression"})
	public void sip_inbound_calling() {
		System.out.println("Test case --sip_inbound_calling_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		sipCallingPage.acceptCall(sipDriver);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		sipCallingPage.verifyCallIsConnected(sipDriver);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// Hanging up call from called agent
		softPhoneCalling.hangupActiveCall(driver3);

		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verify that call has been ended from sip
	    sipCallingPage.verifyCallHasEnded(sipDriver);
	    
	    //verify data for outbound call
  		callScreenPage.openCallerDetailPage(driver1);
  		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
  	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
  	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
  	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
  	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
  	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
  	    seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);

  	    //verify data for inbound call
  	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
  	    callScreenPage.openCallerDetailPage(driver1);
  	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
  	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
  	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
  	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
  	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
  	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
  	    seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);
  	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//Verify inbound call cold transfer with SIP call forwarding enabled
	@Test(groups = {"Regression"})
	public void sip_inbound_call_tansfer() {
		System.out.println("Test case --sip_inbound_call_tansfer-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		sipCallingPage.acceptCall(sipDriver);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		
		// Putting call on hold
		softPhoneCalling.clickHoldButton(driver1);
		
		// resume call from hold
		softPhoneCalling.clickOnHoldButton(driver1);
		softPhoneCalling.clickResumeButton(driver1);

		// transfer call to participant 2
		softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_number"));
		
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //Verify that call has been ended from sip
	    sipCallingPage.verifyCallHasEnded(sipDriver);

		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver3);
  	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//Verify inbound call merge with SIP call forwarding enabled
	@Test(groups = {"Regression"})
	public void sip_inbound_call_merge() {
		System.out.println("Test case --sip_inbound_call_merge-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		sipCallingPage.acceptCall(sipDriver);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		
		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		//verify that forwarding device is still connected
		sipCallingPage.verifyCallIsConnected(sipDriver);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeWithoutLoop(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// Hanging up call from called agent
		softPhoneCalling.hangupActiveCall(driver3);

		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verify that call has been ended from sip
	    sipCallingPage.verifyCallHasEnded(sipDriver);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	//Verify inbound sequential queue calls going to 'no answer' step after decline by both agent
	@Test(groups = {"Regression"})
	public void sip_sequential_call_decline() {
		System.out.println("Test case --sip_sequential_call_decline-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String sequentialQueueName		= CONFIG.getProperty("qa_sequential_queue_name");
		String sequentialQueueNumber	= CONFIG.getProperty("qa_sequential_queue_number");
		
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, sequentialQueueName);
		
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, sequentialQueueName);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, sequentialQueueNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver3);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, sequentialQueueNumber);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		sipCallingPage.verifyCallAppearing(sipDriver);
		
		// declining call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		sipCallingPage.declineCall(sipDriver);
		seleniumBase.idleWait(2);
		sipCallingPage.declineCall(sipDriver);
		
		// declining call from  second device
		System.out.println("picking up call from participant 2");
		softPhoneCalling.isDeclineButtonVisible(driver4);
		seleniumBase.idleWait(2);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
		softPhoneCalling.declineCall(driver4);

		// hangup from caller
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		seleniumBase.idleWait(5);

	    //verify data for agent 1
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    softPhoneActivityPage.playTaskRecording(driver1, callSubject2);
  		callScreenPage.openCallerDetailPage(driver1);
  		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
  		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
 	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
 	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
 	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
 	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);

  	    //verify data for agent 2
 	    softphoneCallHistoryPage.openCallsHistoryPage(driver4);
 	    softphoneCallHistoryPage.clickGroupCallsLink(driver4);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
	    softphoneCallHistoryPage.openCallEntryByIndex(driver4, 0);
	    softPhoneActivityPage.navigateToAllActivityTab(driver4);
	    softPhoneActivityPage.playTaskRecording(driver4, callSubject2);
  	    callScreenPage.openCallerDetailPage(driver4);
  	    contactDetailPage.openRecentCallEntry(driver4, callSubject2);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver4), "Inbound");
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
  	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	//Verify inbound sequential queue calls should not go to 'no answer' step after first attempt dial
	@Test(groups = {"Regression"})
	public void sip_sequential_call_flow_decline() {
		System.out.println("Test case --sip_sequential_call_flow_decline-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String sequentialQueueName		= CONFIG.getProperty("qa_sequential_queue_name");
		String sequentialCallFlowNumber	= CONFIG.getProperty("qa_seq_queue_call_flow_number");
		
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, sequentialQueueName);
		
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, sequentialQueueName);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, sequentialCallFlowNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver3);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, sequentialCallFlowNumber);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		sipCallingPage.verifyCallAppearing(sipDriver);
		
		// declining call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		sipCallingPage.declineCall(sipDriver);
		seleniumBase.idleWait(2);
		sipCallingPage.declineCall(sipDriver);
		
		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.isDeclineButtonVisible(driver4);
		softPhoneCalling.pickupIncomingCall(driver4);

		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver3);
  	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
}