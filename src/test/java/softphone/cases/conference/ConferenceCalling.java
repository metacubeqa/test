package softphone.cases.conference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

public class ConferenceCalling extends SoftphoneBase{
  
  //In this test case we are doing conferencing of inbound calls and then checking conference button and also checking the sfdc data
  @Test(groups={"Regression","Sanity", "Product Sanity"})
  public void inbound_call_conference() {
    System.out.println("Test case --inbound_call_conference-- started ");
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);

	//taking incoming call
    System.out.println("Taking call from first caller");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call
    System.out.println("Picking up call from agent");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
	//checking that first call go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver3);
    seleniumBase.idleWait(2);
    
	//checking that hold and transfer button are enable
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//checking that  conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from agent");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //In this test case we are doing conferencing of inbound calls and then checking conference button and also checking the sfdc data
  @Test(groups={"Regression"})
  public void inbound_call_conference_end_participant_2_call() {
    System.out.println("Test case --inbound_call_conference_end_participant_2_call-- started ");
 
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);

	//taking incoming call
    System.out.println("Taking call from first caller");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
	//checking that first call go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2, "number of participant end buttons in Conference window is not equals to 2");
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1, "No select button is available");
    
   //ending participant 2 call from conference from agent's softphone 
    System.out.println("ending up second caller from Conference window");
    softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();
    
	//verifying that participant 2 is removed
    System.out.println("verifying that second caller is removed");
    softPhoneCalling.isCallBackButtonVisible(driver3);
    
	//verifying that conference button is removed
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//verifying that participants are still connected with agent
    System.out.println("verifying that participants are still connected on the call");
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hangup with the caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    System.out.println("verifying that call from agents screen is removed");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case we are doing conferencing of inbound calls and then checking conference button and also checking the sfdc data
  @Test(groups={"Regression"})
  public void inbound_calls_conference_agent_leave() {
    System.out.println("Test case --inbound_calls_conference_agent_leave-- started ");
 
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);

	//taking incoming call
    System.out.println("Taking call from first caller");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
	//checking that first call go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2, "Softphone end buttons are not equals to 2");
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1,"select button is not present");
    
   //Agent leaves the conference calls
    System.out.println("agent is leaving from the conference");
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//Verify that agents call is ended
    System.out.println("verifying that call is removed from agents");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//verifying that conference button is removed
    softPhoneCalling.isConferenceButtonInvisible(driver1);
    
	//verifying that other participant are still connected
    System.out.println("verifying that callers are still on the call");
    softPhoneCalling.isHangUpButtonVisible(driver2);
    softPhoneCalling.isHangUpButtonVisible(driver3);
    
	//hangup with the caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that caller 2 is hangup
    System.out.println("verifying that call is removed from caller 2");
    softPhoneCalling.isCallBackButtonVisible(driver3);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying outbound call to 2nd participant after multiple merge and leave
  @Test(groups={"Regression"})
  public void inbound_conference_outbound_call_to_2_participant_after_multiple_merge_and_leave()
  {
    System.out.println("Test case --inbound_conference_outbound_call_to_2_participant_after_multiple_merge_and_leave-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
    //taking incoming call
    System.out.println("Taking call from caller 1");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hanging up with caller 2
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver3);
  
	//checking that conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//taking first incoming call
    System.out.println("Taking first incoming call again");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up first incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//clicking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
  	
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hanging up with the first caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupActiveCall(driver3);
    seleniumBase.idleWait(5);
    
    //Making second outbound call again from softphone
    System.out.println("making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
    
	//receiving call from app softphone
    System.out.println("receiving call from first caller");
    softPhoneCalling.pickupIncomingCall(driver3);
    
	//hanging up with the second caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//hanging up with the first caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //In this test case we will merge the inbound call after resume and then we will verify the conference icon
  @Test(groups={"Regression"})
  public void inbound_conference_after_resume()
  {
    System.out.println("Test case --inbound_conference_after_resume()-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//taking incoming call
    System.out.println("Taking call from first caller");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call	
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
    
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2, "Conference end participants buttons count is not 2");
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1, "select button is not available");
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//checking that hold and transfer button are enable
    seleniumBase.idleWait(2);
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//checking that  conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case we will merge the inbound call and verify agents phone when participant 1 leaves conference after multiple merge
  @Test(groups={"Regression"})
  public void inbound_conference_verify_call_screen_when_1_participant_leave()
  {
    System.out.println("Test case --inbound_conference_verify_call_screen_when_1_participant_leave-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//taking incoming call
    System.out.println("Taking call from first caller");
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call	
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//taking second incoming call
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
	//checking that  second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//Merge the calls
    softPhoneCalling.clickMergeButton(driver1);
    
    //participant 2 leaves the conference
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//taking incoming call
    System.out.println("Taking call from second caller again");
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    
	//receiving call	
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"Call is not resumed");
    
    //merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//Merge the calls
    softPhoneCalling.clickMergeButton(driver1);
  	
    //hanging up with the first caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
    //verify that conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"2nd caller is number is not appearing on caller screen");
    
	//hanging up with caller 2
    System.out.println("hanging up with caller 2");
    softPhoneCalling.hangupActiveCall(driver3);
    
    //verifying that call has been ended from agents phone
    System.out.println("verifying that call has been removed from caller 1");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //in this test case we are verifying outbound conference calling  
  @Test(groups={"Regression"})
  public void outbound_conference_calling()
  {
    System.out.println("Test case --outbound_conference_calling-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("Calling to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("Receiving call from first caller");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("Calling to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("receiving call from second caller");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2, "number of conference end button are not 2");
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1, "no select button is visible");
    
	//select first caller
    System.out.println("Selecting first caller from Conference screen");
    softPhoneCalling.availableSelectButton(driver1).get(0).click();
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//checking that hold and transfer button are enable
    seleniumBase.idleWait(5);
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//checking that  conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //In this test case agent end participant 2 in outbound conference call
  @Test(groups={"Regression"})
  public void outbound_conference_agent_end_participant_2_call()
  {
    System.out.println("Test case --outbound_conference_agent_end_participant_2_call-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("Calling to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call
    System.out.println("picking up call from first caller");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("Making second call from softphone agent");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//ending participant 2 call from conference from agent's softphone 
    System.out.println("end second caller from the conference window");
    softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();
    
	//verifying that participant 2 is removed
    System.out.println("verifying that call is ended for second caller");
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//verifying that conference button is removed
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//verifying that hold and transfer button are enable
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//verifying that participant 1 details are showing on agent's call screen
    assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
    
	//verifying that participant 1 is still connected with agent
    System.out.println("verifying that agent and participant one are still on the call");
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hangup with the caller 1
    System.out.println("hanging up from the caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    System.out.println("verifying that call is removed from agents");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //In this test case we are checking that agent can leave from conference  
  @Test(groups={"Regression", "Sanity", "Product Sanity"})
  public void outbound_conference_agent_leave()
  {
    System.out.println("Test case --outbound_conference_agent_leave-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));;
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);
 
	//checking that first call go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//agent leaving call from conference
    System.out.println("agent is leaving the call from conference window");
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//verifying that conference button is removed 
    softPhoneCalling.isConferenceButtonInvisible(driver1);
    
	//verifying that conference call is removed from agent's softphone
    System.out.println("verifying that agent is removed from the conference");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//verifying that rest of two participant are on call
    System.out.println("verifying that callers are still on call after 5 seconds");
    seleniumBase.idleWait(5);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hanging up with the one participants
    System.out.println("hanging up from caller 2");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//verifying that another participant got hanged up
    System.out.println("verifying that second caller is also disconnected");
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //in this test case we are verifying outbound conference calling with multiple merge
  @Test(groups={"Regression"})
  public void outbound_conference_2_participant_disconnect_after_multiple_merge()
  {
    System.out.println("Test case --outbound_conference_2_participant_disconnect_after_multiple_merge-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge 	
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hanging up with caller 2
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver4);
  
	//checking that conference button is disabled
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
    //Making second outbound call again from softphone
    System.out.println("making call to second caller again");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call again");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//clicking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
    //resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))), "call is not resumed");
    
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//clicking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
  	
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up with the first caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver1);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are dropping voice mail to last caller in an outbound conference calling  
  @Test(groups={"Regression"})
  public void outbound_conference_agent_drop_voicemail_to_remaining_participant()
  {
    System.out.println("Test case --outbound_conference_agent_drop_voicemail_to_remaining_participant-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
     
    //Resume first call
    System.out.println("resuming first call");
    softPhoneCalling.clickResumeButton(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//dropping Voice Mail
    System.out.println("dropping voice mail to first caller");
    callToolsPanel.dropFirstVoiceMail(driver1);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//hanging up the first caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupIfInActiveCall(driver2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }

//in this test case we are dropping voice mail to last caller in an outbound conference calling  
  @Test(groups={"Regression"})
  public void outbound_conference_agent_drop_voicemail_to_all_participant()
  {
    System.out.println("Test case --outbound_conference_agent_drop_voicemail_to_all_participant-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
     
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//select first caller
    softPhoneCalling.availableSelectButton(driver1).get(0).click();

	//dropping Voice Mail
    System.out.println("dropping voice mail to second caller");
    callToolsPanel.dropFirstVoiceMail(driver1);
    
    seleniumBase.idleWait(5);
    
	//dropping Voice Mail
    System.out.println("dropping voice mail to first caller");
    callToolsPanel.dropFirstVoiceMail(driver1);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
    //End Call from participants screen
    System.out.println("remove caller if in active call");
    softPhoneCalling.hangupIfInActiveCall(driver2);
    softPhoneCalling.hangupIfInActiveCall(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are dropping voice mail to 2nd caller and taking an inbound call from it  
  @Test(groups={"Regression"})
  public void outbound_conference_agent_drop_voicemail_and_receive_inbound_call()
  {
    System.out.println("Test case --outbound_conference_agent_drop_voicemail_and_receive_inbound_call-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//dropping Voice Mail
    callToolsPanel.dropFirstVoiceMail(driver1);
  
	//Call is removing from participant 2
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
    //verifying that call is not removed from remaining participants
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//Taking an inbound call from participant 2
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("Making call from participant 2");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    
    //verifiying that call is appearing on participant 2's softphone
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
    //End Call from participants screen
    System.out.println("hanging up from callers");
    softPhoneCalling.hangupActiveCall(driver2);
    softPhoneCalling.hangupActiveCall(driver4);
    
    //verifiying that call is removed from agents softphone
    System.out.println("verifying that call is removed from agents machine");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case we will merge the outbound call after resume and then we will verify the conference icon
  @Test(groups={"Regression", "Sanity", "QuickSanity", "Product Sanity"})
  public void outbound_conference_after_resume()
  {
    System.out.println("Test case --outbound_conference_after_resume-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
   	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
       
   	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
       
   	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
       
   	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver3);
    
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
    
	//checking that  second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//checking that  conference button is disabled
    seleniumBase.idleWait(5);
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //in this test case we are verifying tranfer to call flow in outbound conference calling
  @Test(groups={"Regression"})
  public void outbound_conference_calling_tranfer_to_call_flow()
  {
    System.out.println("Test case --outbound_conference_calling_tranfer_to_call_flow-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hanging up with caller 2
    System.out.println("hanging up with caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//checking that hold and transfer button are enable
    seleniumBase.idleWait(2);
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//checking that  conference button is disabled
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
    //tranfer call to Conference Call Flow
    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
    
	//hanging up with caller 2
    System.out.println("hanging up with caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupIfInActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying outbound conference calling with outbound number
  @Test(groups={"Regression"})
  public void outbound_conference_calling_with_outbound_number()
  {
    System.out.println("Test case --outbound_conference_calling_with_outbound_number-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
    //Select an outbound Number
    System.out.println("selcting last outbound number");
    String selectedOutboundNumber = softPhoneSettingsPage.selectLastOutboundNumberAndGetNumber(driver1);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
    //Verify that outbound number appearing on Outbound Number tab is correcect
    System.out.println("verifying that call is going from outbound number");
    assertEquals(callScreenPage.getOutboundNumber(driver1), selectedOutboundNumber);
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//checking that hold and transfer button are enable
    seleniumBase.idleWait(2);
    assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
    
	//chekcing that  conference button is disabled
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
    //select default smart number
	softPhoneSettingsPage.clickSettingIcon(driver1);
	softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying outbound conference calling and verify monitor and listen buttons
  @Test(groups={"Regression"})
  public void outbound_conference_verify_monitor_listen_buttons()
  {
    System.out.println("Test case --outbound_conference_verify_monitor_listen_buttons-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
    String Participant1 = CONFIG.getProperty("qa_user_2_name");
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //Verify monitor and listen buttons are disabled under team section
    softPhoneTeamPage.openTeamSection(driver1);
    assertFalse(softPhoneTeamPage.isListenButtonEnable(driver1, Participant1));
    assertFalse(softPhoneTeamPage.isMonitorButtonEnable(driver1, Participant1));
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);

	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //in this test case we are verifying outbound conference calling with local presence number
  @Test(groups={"Regression"})
  public void outbound_conference_calling_with_local_presence_number()
  {
    System.out.println("Test case --outbound_conference_calling_with_local_presence_number-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
    //Select local presence number as outbound Number
    System.out.println("enabling local presence setting");
    softPhoneSettingsPage.clickSettingIcon(driver1);
    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);
    
    //Verify that area code appearing on local presence tab is correct
    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
    assertEquals(CONFIG.getProperty("qa_user_2_number").substring(2, 5) ,localPresenceNumber.substring(0,3));

 	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);

    //hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
 
    //Verifying call back on local presence number functionality
	//taking incoming call
    System.out.println("Taking call from twilio");
    softPhoneCalling.softphoneAgentCall(driver4, localPresenceNumber);
    
	//receiving call
    System.out.println("Picking up call from softphone");
    softPhoneCalling.pickupIncomingCall(driver1);
    assertFalse(callScreenPage.isOutboundBarVisible(driver1));
    
	//taking second incoming call1
    System.out.println("Taking second incoming call");
    softPhoneCalling.softphoneAgentCall(driver2, localPresenceNumber);
    
	//picking up second call
    System.out.println("picking up second incoming call");
    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
    assertFalse(callScreenPage.isOutboundBarVisible(driver1));

	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
 
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
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
  
//in this test case we are verifying message with outbound conference calling  
 // @Test(groups={"Regression"})
  public void outbound_conference_calling_send_receive_message()
  {
    System.out.println("Test case --outbound_conference_calling_send_receive_message()-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    reloadSoftphone(driver4);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //send a text message to caller 2
    softPhoneActivityPage.openMessageTab(driver1);
    softPhoneActivityPage.sendMessage(driver1, message, 0);
    
    //verify that message is received by caller 2
    softPhoneActivityPage.openMessageTab(driver4);
    softPhoneActivityPage.verifyInboundMessage(driver4, message);
    
    //send a text message from caller 2
    softPhoneActivityPage.sendMessage(driver4, message, 0);
    
    //verify that message is received by agent
    softPhoneActivityPage.verifyInboundMessage(driver1, message);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying outbound conference calling and verify my calls section
  @Test(groups={"Regression"})
  public void outbound_conference_verify_my_call_history_page()
  {
    System.out.println("Test case --outbound_conference_verify_my_calls_history_page-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //Navigate to Calls History page and verify all three tabs are accessible
    System.out.println("Verifying My Calls History section");
    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
    softphoneCallHistoryPage.clickMyCallsLink(driver1);
    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);

	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying outbound conference calling and verify group calls section
  @Test(groups={"Regression"})
  public void outbound_conference_verify_group_calls_history_page()
  {
    System.out.println("Test case --outbound_conference_verify_group_calls_history_page-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //Navigate to Calls History page and verify all three tabs are accessible
    System.out.println("Verifying My Calls History section");
    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);

	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying add/remove groups with outbound conference calling  
  @Test(groups={"Regression"})
  public void outbound_conference_calling_add_remove_groups()
  {
    System.out.println("Test case --outbound_conference_calling_add_remove_groups()-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
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
    
	//hanging up with the caller 1
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	// hanging up with caller 2
	System.out.println("hanging up with caller 1");
	softPhoneCalling.hangupActiveCall(driver4);
    seleniumBase.idleWait(2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying user should not be able to pick call from Queue with outbound conference calling  
  @Test(groups={"Regression"})
  public void outbound_conference_calling_pick_call_from_queue()
  {
    System.out.println("Test case --outbound_conference_calling_pick_call_from_queue()-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //subscribe a queue
    softPhoneCallQueues.subscribeQueue(driver1, queueName);
    
    //verify that Queue is appearing in subscribed queues list
    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
    
    //call to the subscribed queue
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
    
    //verify that disabled pick call from queue button should appear.
    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    seleniumBase.idleWait(2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //in this test case we are verifying outbound conference calling and adding call notes
  @Test(groups={"Regression"})
  public void outbound_conference_calling_add_call_notes()
  {
    System.out.println("Test case --outbound_conference_calling_add_call_notes-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an outbound call from softphone
    System.out.println("agent making call to first caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//receiving call from app softphone
    System.out.println("first caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making second outbound call from softphone
    System.out.println("agent making call to second caller");
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    System.out.println("second caller picking up the call");
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    seleniumBase.idleWait(2);
    
    //Adding Call Notes to second Caller
    String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    String taskComment = "Comment " + new SimpleDateFormat("yyyyMM/dd_HHmmss").format(Calendar.getInstance().getTime());
    String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
    String relatedTask = CONFIG.getProperty("softphone_task_related_opportunity");
    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),relatedTask);
    softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Meeting.toString());
    callToolsPanel.giveCallRatings(driver1, 5);
    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
    reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Conference Calling with Call Forwarding Test Notes");
    
	//hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
   
	//Opening the caller detail page
    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);

	//clicking on recent call entry
	contactDetailPage.openRecentCallEntry(driver1, callSubject);
	
	//Verifying Recent Calls Detail
    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
    assertEquals(sfTaskDetailPage.getRating(driver1), "5");
    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), relatedTask);
    seleniumBase.idleWait(5);
    sfTaskDetailPage.clickRecordingURL(driver1);
    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
    seleniumBase.closeTab(driver1);
    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
    
    //Verifying Added tasks details
    driver1.navigate().back();
    contactDetailPage.openActivityFromList(driver1, taskSubject);
    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
    assertEquals(sfTaskDetailPage.getSubject(driver1), taskSubject);
    assertEquals(sfTaskDetailPage.getComments(driver1), taskComment);
    assertEquals(sfTaskDetailPage.getDueDate(driver1), dueDate);
    seleniumBase.closeTab(driver1);
    seleniumBase.switchToTab(driver1, 1);
    
    //Verify call recording for 1st caller
    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
    callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
    contactDetailPage.openRecentCallEntry(driver1, callSubject);
    sfTaskDetailPage.clickRecordingURL(driver1);
    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
    seleniumBase.closeTab(driver1);
    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
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
  
  //In this test case agent end participant 1 in inbound outbound conference call
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_participant_callers_disconnect()
  {
    System.out.println("Test case --outbound_inbound_conference_participant_callers_disconnect-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
   //hanging up with caller 1
    System.out.println("hanging up with caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
  //verifying that conference button is removed
    seleniumBase.idleWait(2);
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//hanging up with the second caller
    System.out.println("hanging up with the second caller");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removing from softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  
//in this test case we are verifying inbound, outbound call conference calling then agent end calls from softphone 
  @Test(groups={"Regression", "Sanity", "QuickSanity", "Product Sanity"})
  public void outbound_inbound_softphone_call_end_conference_calling()
  {
    System.out.println("Test case --outbound_inbound_softphone_call_end_conference_calling-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up from agents softphone
    System.out.println("hanging up with agents softphone");
    softPhoneCalling.hangupActiveCall(driver1);
    
	//Call is removing from softphone
    System.out.println("Call is removed from caller2");
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Call is not removed from first caller
    System.out.println("verifyig that call is not removed from caller1");
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hanging up with the first caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }

//In this test case agent end participant 1 in inbound outbound conference call
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_agent_end_participant_1_call()
  {
    System.out.println("Test case --outbound_inbound_conference_agent_end_participant_1_call-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
    //select first caller
    softPhoneCalling.availableSelectButton(driver1).get(0).click();
    
	//hangup with the caller 1
    softPhoneCalling.hangupActiveCall(driver1);
    
	//verifying that participant 1 is removed
    softPhoneCalling.isCallBackButtonVisible(driver2);
    
	//verifying that conference button is removed
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
  
	//verifying that participant 2 is still connected with agent
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case agent end participant 1 in inbound outbound conference call
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_agent_end_participant_2_call()
  {
    System.out.println("Test case --outbound_inbound_conference_agent_end_participant_2_call-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//ending participant 2 call from conference from agent's softphone 
    softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();
    
	//verifying that participant 2 is removed
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//verifying that conference button is removed
    seleniumBase.idleWait(2);
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
    
	//verifying that participant 1 is still connected with agent
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case agent leaves from a inbound outbound conference call
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_agent_leaves()
  {
    System.out.println("Test case --outbound_inbound_conference_agent_leaves-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//ending participant 1 call from conference from agent's softphone 
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//verifying that conference button is removed 
   softPhoneCalling.isConferenceButtonInvisible(driver1);
    
	//verifying that conference call is removed from agent's softphone
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//verifying that rest of two participant are on call
    seleniumBase.idleWait(5);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case agent leaves from a inbound outbound conference call
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_verify_hold_window_after_participant_leave()
  {
    System.out.println("Test case --outbound_inbound_conference_verify_hold_window_after_participant_leave-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from participant softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that paticipant 2 is hangup
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Making an inobund call to softphone again
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
  	//click hold button
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that  caller number  is visible
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
    
  	//click on hold button and verify resume and merge buttons
  	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
  	
	//hangup with the caller 1
    softPhoneCalling.hangupActiveCall(driver2);
  	
  	//hangup with the caller 1
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that no call is active on agent's softphone
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//In this test case agent leaves from a inbound outbound conference call after navigating to contacts page
  @Test(groups={"Regression"})
  public void outbound_inbound_conference_agent_leaves_after_navigation()
  {
    System.out.println("Test case --outbound_inbound_conference_agent_leaves_after_navigation-- started ");
    
	//updating the driver used 
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver2);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
  
	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //navigating to contacts page
    softPhoneContactsPage.clickActiveContactsIcon(driver1);
    
	//ending participant 1 call from conference from agent's softphone 
    softPhoneCalling.clickConferenceButton(driver1);
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//verifying that conference button is removed from agent's softphone
    softPhoneCalling.isConferenceButtonInvisible(driver1);
    
	//verifying that rest of two participant are on call
    softPhoneCalling.isHangUpButtonVisible(driver2);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying inbound, outbound call conference calling then agent end calls from softphone 
  @Test(groups={"Regression"})
  public void inbound_outbound_softphone_call_end_conference_calling()
  {
    System.out.println("Test case --inbound_outbound_softphone_call_end_conference_calling-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up from agents softphone
    System.out.println("hanging up with agents softphone");
    softPhoneCalling.hangupActiveCall(driver1);
    
	//Call is removing from softphone
    System.out.println("Call is removed from caller2");
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//Call is not removed from first caller
    System.out.println("verifyig that call is not removed from caller1");
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hanging up with the second caller
    System.out.println("hanging up with the first caller");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying inbound, outbound call conference calling then agent end calls from softphone 
  @Test(groups={"Regression"})
  public void inbound_outbound_1_participant_disconnected()
  {
    System.out.println("Test case --inbound_outbound_1_participant_disconnected-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
    //Select first caller
    softPhoneCalling.availableSelectButton(driver1).get(0).click();
    
	//hangup with the caller 1
    softPhoneCalling.hangupActiveCall(driver1);
    
	//verifying that participant 1 is removed
    softPhoneCalling.isCallBackButtonVisible(driver2);
    
	//verifying that conference button is removed
    softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
  
	//verifying that participant 2 is still connected with agent
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying inbound, outbound call conference calling then 2nd participant is disconnected 
  @Test(groups={"Regression", "Sanity"})
  public void inbound_outbound_2_participant_disconnected()
  {
    System.out.println("Test case --inbound_outbound_2_participant_disconnected-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
  //ending participant 2 call from conference from agent's softphone 
    softPhoneCalling.getParticipantConferenceEndButtons(driver1).get(1).click();
    
	//verifying that participant 1 is removed
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
	//verifying that conference button is removed
    seleniumBase.idleWait(2);
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
  
	//verifying that participant 1 is still connected with agent
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver2);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying inbound, outbound call conference calling then 2nd participant is disconnected 
  @Test(groups={"Regression"})
  public void inbound_outbound_conference_agent_leaves()
  {
    System.out.println("Test case --inbound_outbound_conference_agent_leaves-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
  //ending participant 2 call from conference from agent's softphone 
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//verifying that participant 1 is removed
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//verifying that conference button is removed
    seleniumBase.idleWait(2);
   softPhoneCalling.isConferenceButtonInvisible(driver1);
  
	//verifying that participants are still on the call
    softPhoneCalling.isHangUpButtonVisible(driver2);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 1
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that caller 2 is disconnected
    softPhoneCalling.isCallBackButtonVisible(driver4);
    
    // verify data for outbound call
    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	contactDetailPage.openRecentCallEntry(driver1, callSubject);
	assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	seleniumBase.closeTab(driver1);
	seleniumBase.switchToTab(driver1, 1);
	
	// verify data for inbound call
    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	contactDetailPage.openRecentCallEntry(driver1, callSubject);
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
  
//in this test case we are verifying inbound, outbound call conference calling then agent end calls from softphone 
  @Test(groups={"Regression"})
  public void inbound_outbound_conference_after_resume()
  {
    System.out.println("Test case --inbound_outbound_congference_after_resume-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
    
    //merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
    //resuming first call
    softPhoneCalling.clickResumeButton(driver1);
    
    //checking that caller number  is visible
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
    
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that two end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select button is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
    //ending participant 1 call
    softPhoneCalling.hangupActiveCall(driver2);
    
	//verifying that participant 1 is removed
    softPhoneCalling.isCallBackButtonVisible(driver2);
    
	//verifying that conference button is removed
    seleniumBase.idleWait(2);
   softPhoneCalling.verifyConferenceBtnDisappeared(driver1);
  
	//verifying that participant 2 is still connected with agent
    softPhoneCalling.isHangUpButtonVisible(driver1);
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//in this test case we are verifying inbound, outbound call conference and accepting new incoming call.
  @Test(groups={"Regression"})
  public void inbound_outbound_conference_accept_new_call()
  {
    System.out.println("Test case --inbound_outbound_conference_accept_new_call-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//Making an inbound call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
	//Making an inbound call to softphone from another device
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
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
    softPhoneCalling.hangupActiveCall(driver2);
    
    //verifying number of callers on call screen
    seleniumBase.idleWait(2);
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
	//hanging up from first caller 2
    System.out.println("hanging up from caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up from third caller
    System.out.println("hanging up from caller 3");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//Call is removing from softphone
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
  public void inbound_outbound_conference_reject_new_call()
  {
    System.out.println("Test case --inbound_outbound_conference_reject_new_call-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//Making an inbound call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//Making an inbound call to softphone from another device
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.declineAdditionalCall(driver1);
    softPhoneCalling.isAdditionalCallSendToVMInvisible(driver1);
    
	//verifying that conference button is visible
   softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//verifying that all the conference buttons are visible correctly
    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
    
	//Verifying that only end buttons are visible 
    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
    
	//verifying that one select buttonz is visible
    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
    
	//closing the conference window
    System.out.println("closing the conference window");
    softPhoneCalling.closeConferenceWindow(driver1);
    
	//hanging up from first caller 1
    System.out.println("hanging up from caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
    
	//hanging up from first caller 2
    System.out.println("hanging up from caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up from third caller
    System.out.println("hanging up from caller 3");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//Call is removing from softphone
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
  
//in this test case we are verifying inbound, outbound call conference and dial new call with local presence setting on.
  @Test(groups={"Regression"})
  public void inbound_outbound_conference_dial_new_call_with_local_presence_enable()
  {
    System.out.println("Test case --inbound_outbound_conference_dial_new_call_with_local_presence_enable-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
    //Select local presence number as outbound Number
    softPhoneSettingsPage.clickSettingIcon(driver1);
    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
    
	//Making an inbound call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making an outbound call to softphone to another device
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
    System.out.println("making call to caller 3");
    
	//receiving call from caller 3
    softPhoneCalling.pickupIncomingCall(driver3);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
	//checking hold call actions button resume and merge
    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
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
    softPhoneCalling.hangupActiveCall(driver2);
    
	//hanging up from first caller 2
    System.out.println("hanging up from caller 3");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//hanging up from caller 3
    System.out.println("hanging up from caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Call is removing from softphone
    System.out.println("Call is removed from agents softphone");
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
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

//in this test case we are verifying inbound, outbound call conference and selecting new incoming call.
  @Test(groups={"Regression"})
  public void inbound_outbound_conference_select_new_call()
  {
    System.out.println("Test case --inbound_outbound_conference_select_new_call-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//Making an inbound call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);

	//checking that first call go on hold 
    softPhoneCalling.isCallOnHold(driver1);
    
 	// Resume first caller
 	softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	softPhoneCalling.clickResumeButton(driver1);
	
	// checking that caller number is visible
	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));

 	// merging the second call with the first call
	softPhoneCalling.clickOnHoldButton(driver1);
    softPhoneCalling.clickMergeButton(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
	//Making an inbound call to softphone from another device
    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
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
    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
    
	//hanging up from first caller 1
    System.out.println("hanging up from caller 1");
    softPhoneCalling.hangupActiveCall(driver2);
   
	//hanging up from first caller 2
    System.out.println("hanging up from caller 2");
    softPhoneCalling.hangupActiveCall(driver4);
    
	//hanging up from caller 3
    System.out.println("hanging up from caller 3");
    softPhoneCalling.hangupActiveCall(driver3);
    
	//Call is removing from softphone
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
  public void inbound_outbound_conference_new_call_on_conference_window()
  {
    System.out.println("Test case --inbound_outbound_conference_new_call_on_conference_window-- started ");
    
    // updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
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

	// receiving call from agent's softphone
	softPhoneCalling.pickupIncomingCall(driver1);

 	// Making outbound call to participant 2
 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

 	// picking up second call
 	System.out.println("picking up call from participant 2");
 	softPhoneCalling.pickupIncomingCall(driver4);

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
    softPhoneCalling.pickupIncomingCall(driver1);
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
  public void inbound_outbound_conference_leave_agent_with_more_callers()
  {
    System.out.println("Test case --inbound_outbound_conference_leave_agent_with_more_callers-- started ");
    
    // updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
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

	// receiving call from call agent's softphone
	softPhoneCalling.pickupIncomingCall(driver1);

 	// Making outbound call to participant 2
 	softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("prod_user_3_number"));

 	// picking up second call
 	System.out.println("picking up call from participant 2");
 	softPhoneCalling.pickupIncomingCall(driver6);

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
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

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
  public void conference_header_count_after_resume_merge()
  {
    System.out.println("Test case --conference_header_count_after_resume_merge-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
    String callerName = callScreenPage.getCallerName(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("making call to caller2");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
        
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
    //verify number of caller not displayed
    callScreenPage.verifyNumberOfCallersNotDisplayed(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver1), callerName);
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), callerName + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
    //second agent resumes the call
    softPhoneCalling.clickHoldButton(driver4);
    softPhoneCalling.clickOnHoldButton(driver4);
    softPhoneCalling.clickResumeButton(driver4);
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), callerName + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
    //agent 1 leave the conference
    softPhoneCalling.clickConferenceButton(driver1);
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    seleniumBase.idleWait(4);
    
    //checking that the screen refreshes and caller number appears
  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver4))));
  
    softPhoneCalling.isHangUpButtonVisible(driver4);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver2);
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //Verify +1 header persist after hold/ resume with the second agent after first agent conf 
  @Test(groups={"Regression"})
  public void conference_header_count_multiple_match_caller()
  {
    System.out.println("Test case --conference_header_count_multiple_match_caller-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver5");
    driverUsed.put("driver5", true);
    
    //Verify sender details for Multi known contact
	//getting caller name to add as contact
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
    softPhoneCalling.pickupIncomingCall(driver5);
    softPhoneCalling.hangupActiveCall(driver1);
    String existingContact = callScreenPage.getCallerName(driver1);
    
	// Make a call to the agent
	softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	softPhoneCalling.pickupIncomingCall(driver2);
	softPhoneCalling.hangupActiveCall(driver1);
    
    //adding caller as multiple contact
 	callScreenPage.clickOnUpdateDetailLink(driver1);
 	callScreenPage.addCallerToExistingContact(driver1, existingContact);
 	softPhoneMessagePage.idleWait(10);
 	softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("prod_user_1_number"));
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
    
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
    
    //verify number of caller not displayed
    callScreenPage.verifyNumberOfCallersNotDisplayed(driver1);
    
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver1), "Multiple");
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), "Multiple" + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
	//select first caller
    System.out.println("Selecting first caller from Conference screen");
    softPhoneCalling.clickConferenceButton(driver1);
    softPhoneCalling.availableSelectButton(driver1).get(0).click();
    
    //Select first call from the multiple contact list
    callScreenPage.selectFirstContactFromMultiple(driver1);
    String callerName = callScreenPage.getCallerName(driver1);
    
    //Go to history tab and open the ongoing calls entry
    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), callerName + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
    //agent 1 leave the conference
    softPhoneCalling.hangupActiveCall(driver1);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver1);
    
    aa_AddCallersAsContactsAndLeads();
    
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  //Verify +1 header persist after hold/ resume with the second agent after first agent conf 
  @Test(groups={"Regression"})
  public void multiple_conference_header_count()
  {
    System.out.println("Test case --_multiple_conference_header_count-- started ");
    
    //updating the driver used
    initializeDriverSoftphone("driver1");
    driverUsed.put("driver1", true);
    initializeDriverSoftphone("driver2");
    driverUsed.put("driver2", true);
    initializeDriverSoftphone("driver4");
    driverUsed.put("driver4", true);
    initializeDriverSoftphone("driver3");
    driverUsed.put("driver3", true);
    
	//Making an inobund call to softphone
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
    String callerName = callScreenPage.getCallerName(driver1);
    
	//Making second outbound call from softphone
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("Taking call from unknown caller");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
    
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
  
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver4);
    
    //verify number of caller not displayed for agent 1
    seleniumBase.idleWait(2);
    callScreenPage.verifyNumberOfCallersNotDisplayed(driver1);
    
    //verify number of caller not displayed for agent 2
    callScreenPage.verifyNumberOfCallersNotDisplayed(driver4);
    
    //Making second outbound call from softphone to agent 3
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver3);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
  
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver1), callerName);
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver3), "1");
    
    //get the hovered text for the number of callers for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver3), callerName + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
    //agent 1 leave the conference
    softPhoneCalling.hangupActiveCall(driver1);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver2);
    
	//Verifying that agent is hangup
    softPhoneCalling.isCallBackButtonVisible(driver3);
       
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
//Verify +1 header persist after hold/ resume with the second agent after first agent conf 
  @Test(groups={"Regression"})
  public void conference_3_callers_header_count()
  {
    System.out.println("Test case --conference_3_callers_header_count-- started ");
    
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
    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
    System.out.println("taking call from caller1");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver1);
    
    String callerName = callScreenPage.getCallerName(driver1);
    
	//Making outbound call from agent 1 to agent 2
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
    System.out.println("making call to second agent");
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver4);
    
	//checking that second call should go on hold
    softPhoneCalling.isCallOnHold(driver1);
    
	//merging the second call with the first call
    softPhoneCalling.clickOnHoldButton(driver1);
  
	//verifying that conference button is visible
    softPhoneCalling.clickMergeButton(driver1);
      
	//verifying that conference button is visible
    softPhoneCalling.isConferenceButtonDisplayed(driver1);
    
    //verifying number of callers on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
    
    //verifying number of callers on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //Making second outbound call from softphone to caller 2
    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
    
	//receiving call from app softphone
    softPhoneCalling.pickupIncomingCall(driver5);
    
    //verifying number of callers are 2 on call screen for agent 1
    assertEquals(callScreenPage.getNumberOfCallers(driver1), "2");
    
    //verifying number of callers are 1 on call screen for agent 2
    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
    
    //Verify the text on header count for agent 2
    assertEquals(callScreenPage.getNumberOfCallersHoverText(driver4), callerName + " (" + CONFIG.getProperty("prod_user_1_number").trim() + ")");
    
    //agent 1 leave the conference
    softPhoneCalling.clickConferenceButton(driver1);
    softPhoneCalling.clickAgentConferenceLeaveButton(driver1);
    
	//hangup with the caller 2
    softPhoneCalling.hangupActiveCall(driver5);
    
    //verify number of caller not displayed for agent 2
    seleniumBase.idleWait(2);
    callScreenPage.verifyNumberOfCallersNotDisplayed(driver4);
    
    //agent 2  hangup the call
    softPhoneCalling.hangupActiveCall(driver4);
    
	//Verifying that call is over
    softPhoneCalling.isCallBackButtonVisible(driver2);
       
	//Setting driver used to false as this test case is pass
    driverUsed.put("driver1", false);
    driverUsed.put("driver2", false);
    driverUsed.put("driver3", false);
    driverUsed.put("driver4", false);
    driverUsed.put("driver5", false);
    
    System.out.println("Test case is pass");
  }
  
  @AfterMethod(groups = {"Regression", "MediumPriority", "Product Sanity"}, dependsOnMethods={"resetSetupDefault"})
  public void afterMethod(ITestResult result){
	if (result.getStatus() == 2 || result.getStatus() == 3) {
		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
		if (result.getName().equals("conference_header_count_multiple_match_caller")) {
			  aa_AddCallersAsContactsAndLeads();
		}
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	}
  }
}