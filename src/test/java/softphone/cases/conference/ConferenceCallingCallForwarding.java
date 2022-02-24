package softphone.cases.conference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

public class ConferenceCallingCallForwarding extends SoftphoneBase{

	@BeforeClass(groups={"Regression", "Sanity", "QuickSanity", "Product Sanity"})
	public void beforeClass(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	  
	 // In this test case we are doing conference of outbound calls with call forwarding
	@Test(groups = { "Regression" })
	public void outbound_conference_call_with_call_forwarding() {
		System.out.println("Test case --outbound_conference_call_with_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver5);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// search and make a call
		softPhoneContactsPage.ringDNASearchAndCall(driver1, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());

		// receiving second call
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);
		
		//verify that the status is busy
		callScreenPage.verifyUserImageBusy(driver1);
		
		// verify that end button is visible
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// closing the conference window
		System.out.println("closing the conference window");
		softPhoneCalling.closeConferenceWindow(driver1);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// checking that hold and transfer button are enable
		seleniumBase.idleWait(5);
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));

		// checking that conference button is disabled
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// hanging up with the first caller
		System.out.println("hanging up with the first caller");
		softPhoneCalling.hangupActiveCall(driver5);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	// In this test case participants leave from conference after resume when call forwarding is ON
	@Test(groups = { "Regression", "Sanity", "QuickSanity", "Product Sanity"})
	public void outbound_conference_wtih_call_forwarding_resume_merge() {
		System.out.println("Test case --outbound_conference_wtih_call_forwarding_resume_merge-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		callScreenPage.verifyUserImageBusy(driver1);
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		//click resume button
		softPhoneCalling.clickResumeButton(driver1);
		
		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		//Click on hold button
		softPhoneCalling.clickOnHoldButton(driver1);
		
		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	
	// In this test case agent end participant 1 after selection from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_conference_wtih_call_forwarding_end_1_caller_after_select() {
		System.out.println("Test case --outbound_conference_wtih_call_forwarding_end_1_caller_after_select-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		//select first caller
	    softPhoneCalling.availableSelectButton(driver1).get(0).click();
	    
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent end participant 2 call from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_conference_wtih_call_forwarding__participant_2_end() {
		System.out.println("Test case --call_forwarding_outbound_conference_participant_2_end-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// ending participant 2 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent leaves call from conference when call forwarding
	// is ON
	@Test(groups = { "Regression" })
	public void outbound_conference_with_call_forwarding_agent_leave() {
		System.out.println("Test case --outbound_conference_with_call_forwarding_agent_leave-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// agent leaving call from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver1);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing conference of outbound calls with call forwarding and ending call from softphone
	@Test(groups = { "Regression", "Sanity", "Product Sanity"})
	public void outbound_conference_call_with_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --outbound_conference_call_with_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that other participants are still on the call		
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing outbound conferencing and dropping voicemail to remaining participants
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_drop_voicemail_to_remaining_participants() {
		System.out.println("Test case --outbound_conference_call_forwarding_drop_voicemail_to_remaining_participants-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		// checking that second call should go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// hanging up with caller 2
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// checking that conference button is disabled
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);
		
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupIfInActiveCall(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing outbound conferencing and dropping voicemail to all participants
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_drop_voicemail_to_all_participants() {
		System.out.println("Test case --outbound_conference_call_forwarding_drop_voicemail_to_all_participants-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//click on conference button
		softPhoneCalling.clickConferenceButton(driver1);
		
		//verifying that one select button is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
	    
	    //select first caller
	    softPhoneCalling.availableSelectButton(driver1).get(0).click();

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);
	    seleniumBase.idleWait(5);

	    //verify that hangup button is visible
	    softPhoneCalling.isHangUpButtonVisible(driver1);
	    
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);

		//Call is removing from forwarding device
	    System.out.println("Call is removing from forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //End Call from participants screen
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we are doing conference of inbound outbound calls with call forwarding and ending call from softphone after second merge
	@Test(groups = { "Regression", "Product Sanity" })
	public void outbound_multiple_merge_with_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --inbound_outbound_multiple_merge_with_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);
				
		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
		
		//end call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);
		
		// verifying that conference button is invisible
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
		
		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Call is removing from caller 1
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we are verifying conference using outbound number when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_conference_wtih_call_forwarding_using_outbound_number() {
		System.out.println("Test case --outbound_conference_wtih_call_forwarding_using_outbound_number-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

	    //Select an outbound Number
	    String selectedOutboundNumber = softPhoneSettingsPage.selectLastOutboundNumberAndGetNumber(driver1);
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		
	    //Verify that outbound number appearing on Outbound Number tab is correcect
	    assertEquals(callScreenPage.getOutboundNumber(driver1), selectedOutboundNumber);

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// hanging up with the first caller
		System.out.println("hanging up with the first caller");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Select default Number
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
		softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are verifying conference using outbound number when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_conference_wtih_call_forwarding_using_local_presence_number() {
		System.out.println("Test case --outbound_conference_wtih_call_forwarding_using_local_presence_number-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

	    //Select local presence number as outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		
		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(CONFIG.getProperty("qa_user_2_number").substring(2, 5) ,localPresenceNumber.substring(0,3));

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// disconnect call forwarding device if active
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// hanging up with the first caller
		System.out.println("hanging up with the first caller");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupIfInActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, localPresenceNumber);

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, localPresenceNumber);

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing outbound conferencing and, receiving call after dropping voicemail to 2nd participant
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_drop_voicemail_and_receive_inbound_call() {
		System.out.println("Test case --outbound_conference_call_forwarding_drop_voicemail_and_receive_inbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

	    //verify that hangup button is visible for agent
	    softPhoneCalling.isHangUpButtonVisible(driver1);
	    
	    //verify that call is reomved from 2nd caller
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
	    //take incoming call from caller 2 again
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
	    //End Call from participants screen
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver4);
	    
	    //verify that hangup button is visible for agent and forwarding device
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing outbound conferencing and transfer remaining call to call flow
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_tranfer_remaining_participants_to_call_flow() {
		System.out.println("Test case --outbound_conference_call_forwarding_tranfer_remaining_participants_to_call_flow-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// checking that conference button is disabled
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// call is removed from caller 1
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are doing conference of outbound calls with call forwarding and verifying monitor and listen button in team section
	@Test(groups = { "Regression" })
	public void outbound_conference_call_verify_monitor_listen_buttons() {
		System.out.println("Test case --outbound_conference_call_verify_monitor_listen_buttons-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String Participant1 = CONFIG.getProperty("qa_user_3_name");
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
	    //Verify monitor and listen buttons are disabled under team section
	    softPhoneTeamPage.openTeamSection(driver1);
	    assertFalse(softPhoneTeamPage.isListenButtonEnable(driver1, Participant1));
	    assertFalse(softPhoneTeamPage.isMonitorButtonEnable(driver1, Participant1));

		// hanging up with the first caller
		System.out.println("hanging up with the first caller");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are doing conference of outbound calls and verifying call history section
	@Test(groups = { "Regression" })
	public void outbound_conference_call_verify_my_call_history_section() {
		System.out.println("Test case --outbound_conference_call_verify_my_call_history_section-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
	    //Navigate to Calls History page and verify all three tabs are accessible
	    System.out.println("Verifying My Calls History section");
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickMyCallsLink(driver1);
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are doing conference of outbound calls and verifying group call history section
	@Test(groups = { "Regression" })
	public void outbound_conference_call_verify_group_call_history_section() {
		System.out.println("Test case --outbound_conference_call_verify_group_call_history_section-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
	    //Navigate to Calls History page and verify all three tabs are accessible
	    System.out.println("Verifying My Calls History section");
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are verifying messaging while doing conference of outbound calls
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_send_receive_message() {
		System.out.println("Test case --outbound_conference_call_forwarding_send_receive_message-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		 //send a text message to caller 2
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    
	    //verify that message is received by caller 2
	    softPhoneActivityPage.verifyInboundMessage(driver4, message);
	    
	    //send a text message from caller 2
	    softPhoneActivityPage.sendMessage(driver4, message, 0);
	    
	    //verify that message is received by agent
	    softPhoneActivityPage.verifyInboundMessage(driver1, message);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupIfInActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are verifying add remove queue while doing conference of outbound calls
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_add_remove_queue() {
		System.out.println("Test case --outbound_conference_call_forwarding_add_remove_queue-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //Unsubscribe a queue
	    softPhoneCallQueues.unSubscribeQueue(driver1, queueName);
	    seleniumBase.idleWait(1);
	    
	    //verify that Queue is not appearing in subscribed queues list
	    softPhoneCallQueues.isQueueUnsubscribed(driver1, queueName);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are trying to pick call from queue while doing conference of outbound calls
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_pick_call_from_queue() {
		System.out.println("Test case --outbound_conference_call_forwarding_pick_call_from_queue-- started ");

		// updating the driver used
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
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //call to the subscribed queue
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify that disabled pick call from queue button should appear.
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// hanging up with the queue caller
		System.out.println("hanging up with the queue caller");
		softPhoneCalling.hangupActiveCall(driver5);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 // In this test case we are doing conference of outbound calls and adding call notes
	@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_add_call_notes() {
		System.out.println("Test case --outbound_conference_call_forwarding_add_call_notes-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//Adding Call Notes to second Caller
	    String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String taskComment = "Comment " + new SimpleDateFormat("yyyyMM/dd_HHmmss").format(Calendar.getInstance().getTime());
	    String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
	    String relatedTask = CONFIG.getProperty("softphone_task_related_opportunity");
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),relatedTask);
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
	    softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Meeting.toString());
	    callToolsPanel.giveCallRatings(driver1, 5);
	    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
	    reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Conference Calling with Call Forwarding Test Notes");

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Opening the caller detail page
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying Recent Calls Detail
	    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
	    assertEquals(sfTaskDetailPage.getRating(driver1), "5");
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), relatedTask);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    //verify that one recording is present
	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verifying Added tasks details
	    driver1.navigate().back();
	    contactDetailPage.openActivityFromList(driver1, taskSubject);
	    seleniumBase.switchToTab(driver1, 3);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), taskSubject);
	    assertEquals(sfTaskDetailPage.getComments(driver1), taskComment);
	    assertEquals(sfTaskDetailPage.getDueDate(driver1), dueDate);
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
	
	 // In this test case we are doing conference of outbound calls and verifying that saleforce email template opens 
	//@Test(groups = { "Regression" })
	public void outbound_conference_call_forwarding_email_to_salesforce() {
		System.out.println("Test case --outbound_conference_call_forwarding_email_to_salesforce-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Opening the support tool
		loginSupport(driver1);
		
		//open Support Page and enable send email through salesfore setting
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableSendEmailUsingSalesforce(driver1);
		accountIntelligentDialerTab.enableLightningEmailTemplates(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.enableSendEmailUsingSalesforce(driver1);	
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//open email template
		callScreenPage.clickEmailButton(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		
	    //Verifying email page has opened
	    sfLightningEmailTemplate.verifyPageHeading(driver1);
		seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing conference of inbound calls with call forwarding
	@Test(groups = { "Regression", "Sanity", "Product Sanity"})
	public void inbound_conference_call_with_call_forwarding() {
		System.out.println("Test case --inbound_conference_call_with_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);
		callScreenPage.verifyUserImageBusy(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		assertTrue(softPhoneCalling.isAgentLeaveButtonEnabled(driver1));

		// closing the conference window
		System.out.println("closing the conference window");
		softPhoneCalling.closeConferenceWindow(driver1);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// checking that hold and transfer button are enable
		seleniumBase.idleWait(2);
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));

		// checking that conference button is disabled
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent ends participant 1 from conference in inbound conference call
	@Test(groups = { "Regression" })
	public void inbound_conference_with_call_forwarding_agent_end_participant_1() {

		System.out.println("Test case --inbound_conference_with_call_forwarding_agent_end_participant_1-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// ending participant 1 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(0).click();

		// verifying that participant 1 is removed
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent end participant 2 call from conference when call
	// forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_conference_with_call_forwarding_with_agent_end_participant_2() {
		System.out.println("Test case --inbound_conference_with_call_forwarding_with_agent_end_participant_2-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// ending participant 2 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent is leaving from the conference while call
	// forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_conference_with_call_forwarding_agent_leave() {
		System.out.println("Test case --inbound_conference_with_call_forwarding_agent_leave-- started ");
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// agent leaving call from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver1);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// verifying that conference call is removed from agent's softphone
		seleniumBase.idleWait(1);
		softPhoneCalling.isConferenceButtonInvisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// verifying that rest of two participant are on call
		seleniumBase.idleWait(5);
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we are doing inbound conferencing after resume the call when call forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_conference_after_resume_with_call_forwarding() {
		System.out.println("Test case --inbound_conference_after_resume_with_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);


		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		// checking that second call should go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// closing the conference window
		System.out.println("closing the conference window");
		softPhoneCalling.closeConferenceWindow(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// checking that hold and transfer button are enable
		seleniumBase.idleWait(2);
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));

		// checking that conference button is disabled
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing conference of inbound calls with call forwarding and ending call from forwarding device
	@Test(groups = { "Regression" })
	public void inbound_conference_call_with_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --inbound_conference_call_with_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that other participants are still on the call
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we are doing conference of inbound calls with call forwarding and ending call from forwarding device for remaining calls
	@Test(groups = { "Regression" })
	public void inbound_conference_with_call_forwarding_end_forwarding_device_remaining_calls() {
		System.out.println("Test case --inbound_conference_with_call_forwarding_end_forwarding_device_remaining_calls-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// ending call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);
		
		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		//End call from call fowrwarding device
		softPhoneCalling.hangupActiveCall(driver2);
		
		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Call is removing from caller 1
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing conference of inbound calls with call forwarding and doing outbound call to 2nd caller after 2 time disconnect
	@Test(groups = { "Regression" })
	public void inbound_multiple_merge_with_call_forwarding_outbound_call_2nd_caller() {
		System.out.println("Test case --inbound__multiple_merge_with_call_forwarding_outbound_call_2nd_caller-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//end call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);
		
		// verifying that conference button is invisible
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
		
		//receiving call from second caller again
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from agent's softphone
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// click hold button
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//end call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);

		// Checking that other participants are still on the call
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//put call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to second caller
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// Call is removing from call forwarding device
		System.out.println("Call is removing from call forwarding device");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Call is removing from agents softphone
		System.out.println("Call is removing from agents softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are doing conference of inbound calls with call forwarding and doing outbound call to 1st caller after 2 time disconnect
	@Test(groups = { "Regression" })
	public void inbound__multiple_merge_with_call_forwarding_1st_caller_leave() {
		System.out.println("Test case --inbound__multiple_merge_with_call_forwarding_outbound_call_1st_caller-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// taking second incoming call
		System.out.println("Taking second incoming call");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//end call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);
		
		// verifying that conference button is invisible
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
		
		//receiving call from second caller again
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from agent's softphone
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// click hold button
		seleniumBase.idleWait(5);
		softPhoneCalling.clickOnHoldButton(driver1);
		
		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
		
		// click hold button
		seleniumBase.idleWait(10);
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		// ending call from first participant
		softPhoneCalling.hangupActiveCall(driver3);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
		
		// ending call from second participant
		softPhoneCalling.hangupActiveCall(driver4);		

		// Call is removing from call forwarding device
		System.out.println("Call is removing from call forwarding device");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Call is removing from agents softphone
		System.out.println("Call is removing from agents softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case we are taking resuming an inbound call after discnnected from call forwarding device
	@Test(groups = { "Regression" })
	public void inbound_resume_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --inbound_resume_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);
		
		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButton(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);
		
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from forwarding device
		System.out.println("Call is removing from forwarding device");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent end calls from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_conference_wtih_call_forwarding_agent_end_call() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_agent_end_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4,CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		//close conference window
		softPhoneCalling.closeConferenceWindow(driver1);
		
		// Hanging up call from agent's softphone
		softPhoneCalling.hangupActiveCall(driver1);

		// checking that conference button is disabled
		seleniumBase.idleWait(2);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case first participant disconnected call from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_conference_wtih_call_forwarding_1_participant_disconnect() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_1_participant_disconnect-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// close conference window
		softPhoneCalling.closeConferenceWindow(driver1);

		// Participant 1 ending call
		softPhoneCalling.hangupActiveCall(driver3);

		// checking that conference button is disabled
		seleniumBase.idleWait(2);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that participant 2 is still on the call
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	
	// In this test case agent disconnects second participant from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_conference_wtih_call_forwarding_participant_2_end() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_participant_2_end-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		
		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// ending participant 2 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent leaves from conference when call forwarding is ON
	@Test(groups = { "Regression", "Sanity"})
	public void outbound_inbound_conference_wtih_call_forwarding_agent_leave() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_agent_leave-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// agent leaving call from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver1);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// verifying that conference call is removed from agent's softphone
		seleniumBase.idleWait(1);
		softPhoneCalling.isConferenceButtonInvisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// verifying that rest of two participant are on call
		seleniumBase.idleWait(5);
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent end calls from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_conference_wtih_call_forwarding_resume() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_resume-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4,CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
		softPhoneCalling.clickResumeButton(driver1);
		    
		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		// checking that second call should go on hold
		softPhoneCalling.isCallOnHold(driver1);
		
		// checking hold call actions
		softPhoneCalling.clickOnHoldButton(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		//close conference window
		softPhoneCalling.closeConferenceWindow(driver1);
		
		// Hanging up call from caller 1 softphone
		softPhoneCalling.hangupActiveCall(driver3);

		// checking that conference button is disabled
		seleniumBase.idleWait(2);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that participant 1 is removed
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent end calls from conference when call forwarding is ON
	@Test(groups = { "Regression", "QuickSanity"})
	public void outbound_inbound_conference_wtih_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneContactsPage.ringDNASearchAndCall(driver1, CONFIG.getProperty("qa_user_3_name"), SoftPhoneContactsPage.searchTypes.Users.toString());

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// search and make a call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		
		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
		

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		//close conference window
		softPhoneCalling.closeConferenceWindow(driver1);
		
		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that other participants are still on the call		
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we verify hold window after after participant leave from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_call_forwarding_verify_hold_window_after_participant_leave() {
		System.out.println("Test case --outbound_inbound_call_forwarding_verify_hold_window_after_participant_leave-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4,CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
		
		// Hanging up call from 2nd participant softphone
		softPhoneCalling.hangupActiveCall(driver4);

		//Making an inobund call to softphone again
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller1");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//click hold button
	    softPhoneCalling.clickOnHoldButton(driver1);
	    
		//resuming first call
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
	    
	  	//click on hold button and verify resume and merge buttons
	  	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
		
	  	//End call from caller 1
	  	System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		
		//End call from caller 2
	  	System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);
	  	
		// verifying that softphone is disconnected
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// verifying that call forwarding device is disconnected
		softPhoneCalling.isCallBackButtonVisible(driver2);
				
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	// In this test case agent leaves from conference after navigating to contact page when call forwarding is ON
	@Test(groups = { "Regression" })
	public void outbound_inbound_conference_wtih_call_forwarding_agent_leave_after_navigation() {
		System.out.println("Test case --outbound_inbound_conference_wtih_call_forwarding_agent_leave_after_navigation-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// verifying that mute button is disable on agent's softphone
		System.out.println("Veirfying that mute button is disable for call forwarding call");
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Making inbound call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
		
	    //navigating to contacts page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// agent leaving call from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver1);

		// verifying that conference call is removed from agent's softphone
		softPhoneCalling.isConferenceButtonInvisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// verifying that rest of two participant are on call
		seleniumBase.idleWait(5);
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent end calls from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_outbound_conference_wtih_call_forwarding_agent_end_call() {
		System.out.println("Test case --inbound_outbound_conference_wtih_call_forwarding_agent_end_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		//close conference window
		softPhoneCalling.closeConferenceWindow(driver1);
		
		// Hanging up call from agent's softphone
		softPhoneCalling.hangupActiveCall(driver1);

		// checking that conference button is disabled
		seleniumBase.idleWait(2);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	// In this test case agent end participant 1's call from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_outbound_conference_wtih_call_forwarding_agent_end_participant_1() {
		System.out.println("Test case --inbound_outbound_conference_wtih_call_forwarding_agent_end_participant_1-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		//click select button to select first caller
		softPhoneCalling.availableSelectButton(driver1).get(0).click();
		
		//click conference button
		softPhoneCalling.clickConferenceButton(driver1);
		
		// ending participant 1 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();

		// verifying that participant 1 is removed
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent end participant 2's call from conference when call forwarding is ON
	@Test(groups = { "Regression", "Sanity"})
	public void inbound_outbound_conference_wtih_call_forwarding_agent_end_participant_2() {
		System.out.println("Test case --inbound_outbound_conference_wtih_call_forwarding_agent_end_participant_2-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
		
		// ending participant 2 call from conference from agent's softphone
		softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// checking that conference button is disabled
		seleniumBase.idleWait(1);
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

		// verifying that call forwarding device is active
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case agent end's call from conference when call forwarding is ON
	@Test(groups = { "Regression" })
	public void inbound_outbound_conference_wtih_call_forwarding_agent_leave() {
		System.out.println("Test case --inbound_outbound_conference_wtih_call_forwarding_agent_leave-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// verifying that two end buttons are visible
		assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

		// verifying that one select button is visible
		assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);

		// agent leaving call from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver1);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// verifying that conference call is removed from agent's softphone
		seleniumBase.idleWait(1);
		softPhoneCalling.isConferenceButtonInvisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// verifying that rest of two participant are on call
		seleniumBase.idleWait(5);
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}	
	
	// In this test case agent resumes first call and then merge both calls in inbound outbound conference
		@Test(groups = { "Regression" })
		public void inbound_outbound_conference_wtih_call_forwarding_resume() {
			System.out.println("Test case --inbound_outbound_conference_wtih_call_forwarding_resume-- started ");

			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver2");
			driverUsed.put("driver2", true);
			initializeDriverSoftphone("driver3");
			driverUsed.put("driver3", true);
			initializeDriverSoftphone("driver4");
			driverUsed.put("driver4", true);

			// Making first call to agent
			softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

			// receiving call from call forwarding device
			softPhoneCalling.pickupIncomingCall(driver2);

			// verifying call is visible on agent's softphone
			System.out.println("Verifing that call is appearing at agent's softphone");
			softPhoneCalling.isHangUpButtonVisible(driver1);
			assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

			// Putting first call on hold
			softPhoneCalling.clickHoldButton(driver1);

			// Making outbound call to participant 2
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

			// picking up second call
			System.out.println("picking up call from participant 2");
			softPhoneCalling.pickupIncomingCall(driver4);

			// verifying that call forwarding device is not disconnected
			softPhoneCalling.isHangUpButtonVisible(driver2);

			// checking hold call actions button resume and merge
			softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
			
			 softPhoneCalling.clickResumeButton(driver1);
			    
			// checking that caller number is visible
			assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

			// checking that second call should go on hold
			softPhoneCalling.isCallOnHold(driver1);
			
			// checking hold call actions
			softPhoneCalling.clickOnHoldButton(driver1);

			// merging the second call with the first call
			softPhoneCalling.clickMergeButton(driver1);

			// verifying that conference button is visible
			softPhoneCalling.isConferenceButtonDisplayed(driver1);

			// verifying that all the conference buttons are visible correctly
			softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

			// verifying that two end buttons are visible
			assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);

			// verifying that one select button is visible
			assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
			
			//close conference window
			softPhoneCalling.closeConferenceWindow(driver1);
			
			// Hanging up call from caller 1
			softPhoneCalling.hangupActiveCall(driver3);

			// checking that conference button is disabled
			seleniumBase.idleWait(2);
			softPhoneCalling.verifyConferenceBtnDisappeared(driver1);

			// verifying that call forwarding device is active
			softPhoneCalling.isHangUpButtonVisible(driver2);

			// verifying that participant 1 is removed
			softPhoneCalling.isCallBackButtonVisible(driver3);
			
			// hanging up with caller 2
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(driver4);

			// Call is removing from softphone
			System.out.println("Call is removing from softphone");
			softPhoneCalling.isCallBackButtonVisible(driver1);

			// Checking that call forwarding device is getting disconnected
			System.out.println("Checking that call forwarding device is getting disconnected");
			softPhoneCalling.isCallBackButtonVisible(driver2);

			// Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);
			driverUsed.put("driver2", false);
			driverUsed.put("driver3", false);
			driverUsed.put("driver4", false);

			System.out.println("Test case is pass");
		}

	// In this test case we are doing conference of inbound outbound calls with call forwarding and ending call from softphone
	@Test(groups = { "Regression" })
	public void inbound_outbound_with_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --inbound_outbound_with_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);

		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that other participants are still on the call
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	// In this test case we are doing conference of inbound outbound calls with call forwarding and ending call from softphone after second merge
	@Test(groups = { "Regression" })
	public void inbound_outbound_multiple_merge_with_call_forwarding_end_forwarding_device() {
		System.out.println("Test case --inbound_outbound_multiple_merge_with_call_forwarding_end_forwarding_device-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// checking that first call go on hold
		softPhoneCalling.isCallOnHold(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);

		// verifying that all the conference buttons are visible correctly
		softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
		
		//end call from 2nd caller
		softPhoneCalling.hangupActiveCall(driver4);
		
		// verifying that conference button is invisible
		softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
		
		//receiving call from second caller again
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from agent's softphone
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.idleWait(3);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// resuming first call
		softPhoneCalling.clickResumeButton(driver1);

		// verifying the resume is done by verifying the hangup button and call screen number
		System.out.println("checking that call is resumed");
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		// ending call from call forwarding device
		softPhoneCalling.hangupActiveCall(driver2);

		// verifying that agent is removed
		System.out.println("Checking that agent has been disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that other participants are still on the call
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from caller 2
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//in this test case we are verifying inbound, outbound call conference and accepting new incoming call.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_conference_with_call_forwarding_accept_new_call()
	  {
	    System.out.println("Test case --inbound_outbound_conference_accept_new_call-- started ");
	    
	    // updating the driver used
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

 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver4);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// checking hold call actions button resume and merge
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

	 	// merging the second call with the first call
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller3");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 3);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 2);
	    
		//closing the conference window
	    System.out.println("closing the conference window");
	    softPhoneCalling.closeConferenceWindow(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "2");
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //verifying number of callers on call screen
	    seleniumBase.idleWait(2);
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up from third caller
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//Call is removed from agent's softphone
	    System.out.println("Call is removed from agents softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	//in this test case we are verifying inbound, outbound call conference and rejecting new incoming call.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_conference_with_call_forwarding_reject_new_call()
	  {
	    System.out.println("Test case --inbound_outbound_conference_reject_new_call-- started ");
	    
	    // updating the driver used
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

 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver4);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// checking hold call actions button resume and merge
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

	 	// merging the second call with the first call
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller3");
	    
		//receiving call from app softphone
	    softPhoneCalling.declineAdditionalCall(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
	    
		//closing the conference window
	    System.out.println("closing the conference window");
	    softPhoneCalling.closeConferenceWindow(driver1);
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//Call is removed from agent's softphone
	    System.out.println("Call is removed from agents softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//hanging up from caller 3
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //in this test case we are verifying inbound, outbound call conference and dial new call with local presence setting on.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_with_call_forwarding_dial_new_call_with_local_presence()
	  {
	    System.out.println("Test case --inbound_outbound_with_call_forwarding_dial_new_call_with_local_presence-- started ");
	    
	    // updating the driver used
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

	    //Select local presence number as outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_3_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver3);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// checking hold call actions button resume and merge
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

	 	// merging the second call with the first call
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
	 	// calling to caller 3
	    softPhoneContactsPage.ringDNASearchAndCall(driver1, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());

	 	// picking up call from caller 3
	 	System.out.println("picking up call from participant 3");
	 	softPhoneCalling.pickupIncomingCall(driver4);
	 	
	 	//Select caller from conference list
	 	softPhoneCalling.clickConferenceButton(driver1);
	 	softPhoneCalling.availableSelectButton(driver1).get(1).click();
	 	
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(CONFIG.getProperty("qa_user_2_number").substring(2, 5) ,localPresenceNumber.substring(0,3));
	 	
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 3);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 2);
	    
		//closing the conference window
	    System.out.println("closing the conference window");
	    softPhoneCalling.closeConferenceWindow(driver1);
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//hanging up from caller 3
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //Disable Local Presence Setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
 	  //in this test case we are verifying inbound, outbound call conference and select new incoming caller.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_conference_with_call_forwarding_select_new_call()
	  {
	    System.out.println("Test case --inbound_outbound_conference_with_call_forwarding_select_new_call-- started ");
	    
	    // updating the driver used
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

 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver4);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// Resume first caller
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
		softPhoneCalling.clickResumeButton(driver1);
		
		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	 	// merging the second call with the first call
		softPhoneCalling.clickOnHoldButton(driver1);
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller3");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 3);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 2);
	    
		//select newly added caller
	    System.out.println("selecting the newly added caller");
	    softPhoneCalling.availableSelectButton(driver1).get(1).click();
	    
	    //verify that newly added caller's details appearing on screen
	    assertTrue(CONFIG.getProperty("prod_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up from third caller
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//Call is removed from agent's softphone
	    System.out.println("Call is removed from agents softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
 	  //in this test case we are verifying inbound, outbound call conference and take new call when conference window is open.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_with_call_forwarding_new_call_on_conference_window()
	  {
	    System.out.println("Test case --inbound_outbound_with_call_forwarding_new_call_on_conference_window-- started ");
	    
	    // updating the driver used
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
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);

 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver4);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// Resume first caller
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
		softPhoneCalling.clickResumeButton(driver1);
		
		// checking that caller number is visible
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	 	// merging the second call with the first call
		softPhoneCalling.clickOnHoldButton(driver1);
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller3");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);

		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 3);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 2);
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller4");
	    
		//receiving call from app softphone
	    softPhoneCalling.clickAcceptCallButton(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 4);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 3);
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);

		//hanging up from caller 3
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupActiveCall(driver6);
	    
		//hanging up from caller 4
	    System.out.println("hanging up from caller 4");
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//Call is removed from agent's softphone
	    System.out.println("Call is removed from agents softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //in this test case we are verifying inbound, outbound call conference and accepting and making 2 new calls then agent leaves the conference.
	  @Test(groups={"Regression"})
	  public void inbound_outbound_with_call_forwarding_leave_agent_with_more_callers()
	  {
	    System.out.println("Test case --inbound_outbound_with_call_forwarding_leave_agent_with_more_callers-- started ");
	    
	    // updating the driver used
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
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    
 		// Making first call to agent
	 	softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));

	 	// Putting first call on hold
	 	softPhoneCalling.clickHoldButton(driver1);

	 	// Making outbound call to participant 2
	 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("prod_user_3_number"));

	 	// picking up second call
	 	System.out.println("picking up call from participant 2");
	 	softPhoneCalling.pickupIncomingCall(driver6);

	 	// verifying that call forwarding device is not disconnected
	 	softPhoneCalling.isHangUpButtonVisible(driver2);

	 	// checking that first call go on hold
	 	softPhoneCalling.isCallOnHold(driver1);

	 	// checking hold call actions button resume and merge
	 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

	 	// merging the second call with the first call
	 	softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//Making an inbound call to softphone from another device
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("taking call from caller3");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
	 	// calling to caller 4
	    softPhoneContactsPage.ringDNASearchAndCall(driver1, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());

	 	// picking up call from caller 3
	 	System.out.println("picking up call from participant 4");
	 	softPhoneCalling.pickupIncomingCall(driver4);
	    
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that three end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 4);
	    
		//verifying that two select buttonz is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 3);
	    
		//agent leaves the conference
	    System.out.println("Agent leaves the conference");
	    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
	    
		//Call is removed from agent's softphone
	    System.out.println("Call is removed from agents softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Call is removed from call forwarding device
	    System.out.println("Call is removed from call forwarding device");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//hanging up from first caller 1
	    System.out.println("hanging up from caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//hanging up from first caller 2
	    System.out.println("hanging up from caller 2");
	    softPhoneCalling.hangupActiveCall(driver6);
	    
		//hanging up from caller 3
	    System.out.println("hanging up from caller 3");
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//hanging up from caller 4
	    System.out.println("hanging up from caller 4");
	    softPhoneCalling.hangupIfInActiveCall(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //Verify +1 header persist after hold/ resume with the second agent after first agent conf 
	  @Test(groups={"Regression"})
	  public void conference_with_forwarding_header_count_after_resume_merge()
	  {
	    System.out.println("Test case --conference_with_forwarding_header_count_after_resume_merge-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
		//Making an inobund call to softphone
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_2_number"));
	    System.out.println("taking call from caller1");
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    String callerName = callScreenPage.getCallerName(driver4);
	    
		//Making second outbound call from softphone
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
	    System.out.println("making call to caller2");
	    
		//receiving call from forwarding device
	    softPhoneCalling.pickupIncomingCall(driver2);
	        
		//checking that second call should go on hold
	    softPhoneCalling.isCallOnHold(driver4);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickOnHoldButton(driver4);
	    
	    //verify number of caller not displayed
	    callScreenPage.verifyNumberOfCallersNotDisplayed(driver4);
	    
		//verifying that conference button is visible
	    softPhoneCalling.clickMergeButton(driver4);
	      
		//verifying that conference button is visible
	    softPhoneCalling.isConferenceButtonDisplayed(driver4);
	    
	    //verifying number of callers on call screen for agent 1
	    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
	    
	    //get the hovered text for the number of callers for agent 2
	    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), callerName);
	    
	    //verifying number of callers on call screen for agent 2
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
	    //get the hovered text for the number of callers for agent 2
	    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver1), callerName + " (" + CONFIG.getProperty("prod_user_2_number").trim() + ")");
	    
	    //second agent holds and resumes the call
	    softPhoneCalling.clickHoldButton(driver1);
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //verifying number of callers on call screen for agent 2
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
	    //get the hovered text for the number of callers for agent 2
	    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver1), callerName + " (" + CONFIG.getProperty("prod_user_2_number").trim() + ")");
	    
	    //agent 1 leave the conference
	    softPhoneCalling.clickConferenceButton(driver4);
	    softPhoneCalling.clickAgentConferenceLeaveButton(driver4);
	    
	    //checking that the screen refreshes and caller number appears
	  	assertTrue(CONFIG.getProperty("prod_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
	  
	    softPhoneCalling.isHangUpButtonVisible(driver1);
	    
		//hangup with the forwarding device
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Verifying that agent is hangup
	    softPhoneCalling.isCallBackButtonVisible(driver5);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	@AfterClass(groups={"Regression", "Sanity", "QuickSanity", "Product Sanity"})
	public void afterClass(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		// updating the driver used
		driverUsed.put("driver1", false);
	}
}
