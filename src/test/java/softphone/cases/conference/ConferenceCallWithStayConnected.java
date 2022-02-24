package softphone.cases.conference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class ConferenceCallWithStayConnected extends SoftphoneBase{
	
	@BeforeClass(groups={"Sanity", "Regression", "QuickSanity"})
	public void beforeClass(){
	// updating the driver used
	initializeDriverSoftphone("driver1");
	driverUsed.put("driver1", true);
	initializeDriverSoftphone("driver2");
	driverUsed.put("driver2", true);
	
	// Setting call forwarding ON
	System.out.println("Setting call forwarding ON");
	softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
	
	//enable stay connected setting
	softPhoneSettingsPage.enableStayConnectedSetting(driver1);
	
	// updating the driver used
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	}
	
	//In this test case we are doing outbound conferencing and dropping voicemail to all participants to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_drop_voicemail_to_all_participants() {
	System.out.println("Test case --stay_connected_drop_voicemail_to_all_participants-- started ");

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
	
	//select first caller
	softPhoneCalling.clickConferenceButton(driver1);
    softPhoneCalling.availableSelectButton(driver1).get(0).click();
    
    // checking that caller number is visible
 	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	//dropping Voice Mail
	callToolsPanel.dropFirstVoiceMail(driver1);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	//verify that hangup button is visible
	softPhoneCalling.isHangUpButtonVisible(driver1);
	
	//dropping Voice Mail
	callToolsPanel.dropFirstVoiceMail(driver1);
	
	//Call is removing from softphone
	System.out.println("Call is removing from softphone");
	softPhoneCalling.isCallBackButtonVisible(driver1);
	
	//End Call from Callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	softPhoneCalling.hangupIfInActiveCall(driver4);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}
	
	//In this test case we are doing outbound conferencing and dropping voicemail to first participant after to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_drop_voicemail_to_first_participant_after_resume() {
	System.out.println("Test case --stay_connected_drop_voicemail_to_first_participant_after_resume-- started ");

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

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// resuming first call
	softPhoneCalling.clickResumeButton(driver1);
	    
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// merging the second call with the first call
	softPhoneCalling.clickMergeButton(driver1);

	// verifying that conference button is visible
	softPhoneCalling.isConferenceButtonDisplayed(driver1);
	
	//hangup second caller
	softPhoneCalling.hangupActiveCall(driver4);

	//dropping Voice Mail
	callToolsPanel.dropFirstVoiceMail(driver1);

	//verify that conference button is removed
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	//hangup call from softphone
	System.out.println("hangup from softphone");
	softPhoneCalling.hangupIfInActiveCall(driver1);
	
	//End Call from Callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}
	
	//In this test case we are doing outbound conferencing and dropping voicemail to participant 1 after participant 2 hangup to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_drop_voicemail_to_first_caller_after_participant_2_hangup() {
	System.out.println("Test case --stay_connected_drop_voicemail_to_first_caller_after_participant_2_hangup-- started ");

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
	
	//hangup from second caller
	softPhoneCalling.hangupActiveCall(driver4);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	//dropping Voice Mail
	callToolsPanel.dropFirstVoiceMail(driver1);
    seleniumBase.idleWait(5);
	
	//Call is removing from softphone
	System.out.println("Call is removing from softphone");
	softPhoneCalling.isCallBackButtonVisible(driver1);

	//End Call from Caller 1 if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}
	
	//In this test case we are doing outbound conferencing and holding resuming participant 1 after participant 2 hangup to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_hold_resume_first_caller_after_participant_2_hangup() {
	System.out.println("Test case --stay_connected_hold_resume_first_caller_after_participant_2_hangup-- started ");

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
	
	//hangup from second caller
	softPhoneCalling.hangupActiveCall(driver4);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	//Put on hold the remaining call
	softPhoneCalling.clickHoldButton(driver1);
	
	//resuming call again
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickResumeButton(driver1);
	
	//Hangup from remaining callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	
	// verify that call is not disconnected before 60 seconds
	for (int i = 0; i < 12; i++) {
		softPhoneCalling.isHangUpButtonVisible(driver2);
		seleniumBase.idleWait(5);
		System.out.println("Call is still connected after " + ((i + 1) * 5) + " seconds");
	}
	
	//Call is  removed from call forwarding device
	System.out.println("verify that call is removed from forwarding device");
	softPhoneCalling.isCallBackButtonVisible(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}	
	
	//In this test case we are doing outbound conferencing with outbound number to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_with_outbound_verified_number() {
	System.out.println("Test case --stay_connected_with_outbound_verified_number-- started ");

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

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// resuming first call
	softPhoneCalling.clickResumeButton(driver1);
	    
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// merging the second call with the first call
	softPhoneCalling.clickMergeButton(driver1);

	// verifying that conference button is visible
	softPhoneCalling.isConferenceButtonDisplayed(driver1);
	
	//Agent leaves conference
	System.out.println("hangup from softphone");
	softPhoneCalling.clickConferenceButton(driver1);
	softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
	
	//Verify agent is disconnected
	softPhoneCalling.isCallBackButtonVisible(driver1);
	
	//Verify that callers and forwarding device are active
	softPhoneCalling.isHangUpButtonVisible(driver2);
	softPhoneCalling.isHangUpButtonVisible(driver3);
	softPhoneCalling.isHangUpButtonVisible(driver4);
	
	//End Call from Callers if in active call
	softPhoneCalling.hangupActiveCall(driver3);
	softPhoneCalling.hangupIfInActiveCall(driver4);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}

	//In this test case we are doing outbound conferencing with local presence number to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_with_local_presence_number() {
	System.out.println("Test case --stay_connected_with_local_presence_number-- started ");

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

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// resuming first call
	softPhoneCalling.clickResumeButton(driver1);
	    
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	// clicking on hold button
	softPhoneCalling.clickOnHoldButton(driver1);
	
	// merging the second call with the first call
	softPhoneCalling.clickMergeButton(driver1);

	// verifying that conference button is visible
	softPhoneCalling.isConferenceButtonDisplayed(driver1);
	
	//End Call from Callers in active call
	softPhoneCalling.hangupActiveCall(driver3);
	softPhoneCalling.hangupActiveCall(driver4);
	
	//Verify agent is disconnected
	softPhoneCalling.isCallBackButtonVisible(driver1);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);
	
    //Disable Local Presence Setting
    softPhoneSettingsPage.clickSettingIcon(driver1);
    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}
	
	//In this test case we are doing outbound conferencing and tranfering call after participant 2 hangup to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_transfer_call_after_participant_2_hangup() {
	System.out.println("Test case --stay_connected_transfer_call_after_participant_2_hangup-- started ");

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
	
	//hangup from second caller
	softPhoneCalling.hangupActiveCall(driver4);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
    //tranfer call to second caller
    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
    
	// picking up second call
	System.out.println("picking up second incoming call");
	softPhoneCalling.pickupIncomingCall(driver4);
	
	//reload Agent's softphone
	reloadSoftphone(driver1);
	
	//Hangup from remaining callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	softPhoneCalling.hangupIfInActiveCall(driver4);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}	
	
	//In this test case we are doing outbound conferencing and holding resuming participant 1 after reconnecting participant 2 to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_resume_first_caller_after_reconnecting_participant_2() {
	System.out.println("Test case --stay_connected_resume_first_caller_after_reconnecting_participant_2-- started ");

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
	
	//hangup from second caller
	softPhoneCalling.hangupActiveCall(driver4);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	// Putting first call on hold
	softPhoneCalling.clickHoldButton(driver1);
	
	// Making second outbound call again
	softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

	// picking up call from second caller
	softPhoneCalling.pickupIncomingCall(driver4);
	
	//resuming caller 1
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickResumeButton(driver1);
	
	//Hangup from remaining callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver3);
	softPhoneCalling.hangupIfInActiveCall(driver4);
	
	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);

	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}	
	
	//In this test case we are doing outbound conferencing and multiple merging to test stay connected feature.
	@Test(groups = { "Regression"})
	public void stay_connected_multiple_merge() {
	System.out.println("Test case --stay_connected_multiple_merge-- started ");

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

	// resuming first call
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickResumeButton(driver1);
	
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	// merging the second call with the first call
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickMergeButton(driver1);

	// verifying that conference button is visible
	softPhoneCalling.isConferenceButtonDisplayed(driver1);
	
	//hangup from first caller
	softPhoneCalling.hangupActiveCall(driver3);

	//check if conference button is invisible
	softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
	
	// Making outbound call to first caller again
	softPhoneCalling.clickHoldButton(driver1);
	softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

	// receiving call from the agent
	softPhoneCalling.pickupIncomingCall(driver3);

	// resuming first call
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickResumeButton(driver1);
	
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("qa_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

	// merging the second call with the first call
	softPhoneCalling.clickOnHoldButton(driver1);
	softPhoneCalling.clickMergeButton(driver1);

	// verifying that conference button is visible
	softPhoneCalling.isConferenceButtonDisplayed(driver1);

	//Selecting caller 1 from Conference Window
	softPhoneCalling.clickConferenceButton(driver1);
	softPhoneCalling.availableSelectButton(driver1).get(0).click();
	
	//Ending caller 2 from Conference Window
	softPhoneCalling.clickConferenceButton(driver1);
	softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
	
	//Hangup from remaining callers if in active call
	softPhoneCalling.hangupIfInActiveCall(driver4);
	softPhoneCalling.hangupIfInActiveCall(driver3);

	//Call is not removed from call forwarding device
	System.out.println("verify that call is not removed from forwarding device");
	softPhoneCalling.isHangUpButtonVisible(driver2);
	
	//End Call from call forwarding device
	softPhoneCalling.hangupActiveCall(driver2);
	
	// Setting driver used to false as this test case is pass
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	driverUsed.put("driver3", false);
	driverUsed.put("driver4", false);

	System.out.println("Test case is pass");
	}

	// In this test case we are verifying that forwarding device is not disconnected
	@Test(groups = { "Sanity" })
	public void stay_connected_forwarding_device_remains_connected() {
		System.out.println("Test case --stay_connected_forwarding_device_remains_connected-- started ");

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

		// disconnect caller
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify that call is disconnected from called agent
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Call is not removed from call forwarding device
		System.out.println("verify that call is not removed from forwarding device");
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// receiving call from the second called agent
		softPhoneCalling.pickupIncomingCall(driver4);

		// Call is not removed from call forwarding device
		System.out.println("verify that call is not removed from forwarding device");
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// disconnect caller
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify that call is disconnected from called agent
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Call is not removed from call forwarding device
		System.out.println("verify that call is not removed from forwarding device");
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// disconnect forwarding device
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	@AfterClass(groups={"Sanity", "Regression", "QuickSanity"})
	public void afterClass(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//disable stay connected setting
		softPhoneSettingsPage.disableStayConnectedSetting(driver1);
		
		// updating the driver used
		driverUsed.put("driver1", false);
	}
}
