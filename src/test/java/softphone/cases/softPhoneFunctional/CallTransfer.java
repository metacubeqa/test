package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallTransfer extends SoftphoneBase{
	
	  @BeforeClass(groups = {"Sanity", "ExludeForProd", "Regression", "MediumPriority", "Product Sanity"})
	  public void disableConferenceSetting(){
		  		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableConferenceSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	  }
	  
	  @Test(groups={"Sanity", "Regression", "MediumPriority"})
	  public void call_transfer_to_user()
	  {
	    System.out.println("Test case --call_transfer_to_user-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	       
		//Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    //enter call Notes
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    callRecordingPage.openCallerTaskLink(driver1);
	    sfTaskDetailPage.idleWait(1);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //verify transferred call's recording
	    driver1.navigate().back();
	    contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that entry for transfer call is not present in Salesforce
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
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
	  
	  @Test(groups={"Sanity", "MediumPriority", "Product Sanity"})
	  public void call_transfer_to_call_flow()
	  {
	    System.out.println("Test case --call_transfer_to_call_flow-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		//Making an outbound call from softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //verify that transfer list is empty when call flow number is blank
	    softPhoneCalling.clickTranferButton(driver1);
	    softPhoneCalling.enterTranferToText(driver1, "NoNumberCallFlow");
	    softPhoneCalling.verifyTransferListBlank(driver1);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    //enter call Notes
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		// add caller as Lead
	    if (callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.addCallerAsLead(driver1, "Call Flow Number", "Metacube");
		}
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
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
	  public void call_transfer_forwarding_to_call_flow()
	  {
	    System.out.println("Test case --call_transfer_forwarding_to_call_flow-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Setting call forwarding ON
	 	System.out.println("Setting call forwarding ON");
	 	softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
	    
		//taking an inbound call to softphone
	    System.out.println("taking an inbound call to softphone");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from forwarding device
	    System.out.println("receiving call from forwarding device");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //verify that forwarding device has been disconnected
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//call is removed from called agent phone
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
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

	  @Test(groups={"Sanity", "Regression", "Product Sanity"})
	  public void call_transfer_to_user_group()
	  {
	    System.out.println("Test case --call_transfer_to_user_group-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
	    
		//Making an inbound call froom softphone
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver3);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver3, CONFIG.getProperty("qa_group_1_name"));
	    
		//receiving call from app softphone
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    seleniumBase.idleWait(3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver3);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver3);
	    //enter call Notes
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver3);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		callScreenPage.openCallerDetailPage(driver3);
		assertTrue(contactDetailPage.isActivityInvisible(driver3, callSubject2));
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
	  
	  @Test(groups={"Sanity", "ExludeForProd", "Regression"})
	  public void inbound_inbound_call_merge() {
	    System.out.println("Test case --inbound_inbound_call_merge()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//taking second incoming call
	    System.out.println("Taking second incoming call");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	     	
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from softphone
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back buttonis visible for second caller
	    System.out.println("hanging up with the second caller");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Sanity", "ExludeForProd","Regression"})
	  public void outbound_outbound_call_merge() {
	    System.out.println("Test case --outbound_outbound_call_merge-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    //Making an outbound call from softphone
	    System.out.println("Calling to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("Receiving call from first caller");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//Making second outbound call from softphone
	    System.out.println("Calling to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call from app softphone
	    System.out.println("receiving call from second caller");
	    softPhoneCalling.pickupIncomingCall(driver3);

		//checking that first call go on hold 
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//resuming first call
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);

	    seleniumBase.idleWait(3);
	    
	    //verify that second callers detail appears on agent screen
	    assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))), "call is not resumed");
	    
		//Call is ended from softphone
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//verify that call back buttonis visible for second caller
	    System.out.println("hanging up with the second caller");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
		
		//Verifying Recent Calls Details
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  

	  @Test(groups={"Sanity", "MediumPriority"})
	  public void call_transfer_to_manually_added_contact()
	  {
	    System.out.println("Test case --call_transfer_to_manually_added_contact-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    		
		String manualContactName 	= "Manually Added For Automation";
		String manualContactNumber 	= CONFIG.getProperty("qa_user_2_number");
		
		//Set Contact as fav
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName, manualContactNumber);
	       
		//Making an outbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, manualContactName);
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    //enter call Notes
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //removing favourite contact
	    softPhoneContactsPage.removeContactFavorite(driver1, manualContactName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"MediumPriority", "Product Sanity"})
	  public void inbound_call_transfer_to_manually_added_contact()
	  {
	    System.out.println("Test case --inbound_call_transfer_to_manually_added_contact-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    		
		String manualContactName 	= "Manually Added For Automation";
		String manualContactNumber 	= CONFIG.getProperty("qa_user_2_number");
		
		//Set Contact as fav
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName, manualContactNumber);
	       
		//Making an outbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, manualContactName);
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
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
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //removing favourite contact
	    softPhoneContactsPage.removeContactFavorite(driver1, manualContactName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity"})
	  public void outbound_call_transfer_to_user()
	  {
	    System.out.println("Test case --outbound_call_transfer_to_user-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	       
		//Making an outbound call to second caller
	    System.out.println("Making an outbound call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
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
	  public void outbound_call_transfer_to_user_group()
	  {
	    System.out.println("Test case --outbound_call_transfer_to_user_group-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
	    
		//Making an inbound call froom softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver3, CONFIG.getProperty("qa_group_1_name"));
	    
		//receiving call from app softphone
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    seleniumBase.idleWait(3);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver3);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver3);
	    //enter call Notes.
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver3);
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver3);
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
		callScreenPage.openCallerDetailPage(driver3);
		assertTrue(contactDetailPage.isActivityInvisible(driver3, callSubject2));
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
	  public void transfer_call_from_user_group()
	  {
	    System.out.println("Test case --transfer_call_from_user_group-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
		//Making an inbound call froom softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
		//receiving call from queue
	    System.out.println("first caller picking up the call");
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
	    //transfer call to second qa user
	    seleniumBase.idleWait(3);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_3_name"));
	    
		//receiving call from agent
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver3);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //verify data for inbound call
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	    
	    //enter call Notes
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);

	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertTrue(sfTaskDetailPage.getCallQueue(driver1).contains(queueName));
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
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
	  public void inbound_hold_resume_call()
	  {
	    System.out.println("Test case --inbound_hold_resume_call-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);

		//making call to first caller
	    System.out.println("making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);

	    //putting call on hold
	    softPhoneCalling.putActiveCallOnHold(driver1);
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
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
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_hold_resume_call()
	  {
	    System.out.println("Test case --outbound_hold_resume_call-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);

		//Make and outbound call
	    System.out.println("Make and outbound call");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//caller picking up call
	    System.out.println(" caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);

	    //putting call on hold
	    softPhoneCalling.putActiveCallOnHold(driver1);
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//hanging up with the caller
	    System.out.println("hanging up with the second caller");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //taking inbound call to agent
	    System.out.println("taking inbound call to agent");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//Agent picking up call
	    System.out.println("Agent picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);

	    //putting call on hold
	    softPhoneCalling.putActiveCallOnHold(driver1);
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//hanging up with the caller
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
	  }
	  
	  @Test(groups={"Regression", "MediumPriority"})
	  public void inbound_inbound_resume_merge_first_caller_leave() {
	    System.out.println("Test case --inbound_inbound_merge_first_caller_leave()-- started ");
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
	    
	    //Verify header incoming calls buttons are enabled
	    softPhoneCalling.isAdditionalCallAcceptBtnEnable(driver1);
	    softPhoneCalling.isAdditionalCallDeclineBtnEnable(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMBtnEnable(driver1);
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
	    //resuming first call
	    callToolsPanel.idleWait(5);
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    callRecordingPage.openCallerTaskLink(driver1);
	    callRecordingPage.idleWait(1);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_inbound_merge_second_caller_leave() {
	    System.out.println("Test case --inbound_inbound_merge_second_caller_leave()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//taking second incoming call
	    System.out.println("Taking second incoming call");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_merge_second_caller_leave_after_agent_navigate() {
	    System.out.println("Test case --inbound_merge_second_caller_leave_after_agent_navigate()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//taking second incoming call
	    System.out.println("Taking second incoming call");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
	    //navigate agent to call queue page
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_outbound_resume_merge_first_caller_leave() {
	    System.out.println("Test case --inbound_outbound_resume_merge_first_caller_leave()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//making second call as outgoing
	    System.out.println("making second call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //resuming first call	    
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_outbound_resume_merge_second_caller_leave() {
	    System.out.println("Test case --inbound_outbound_resume_merge_second_caller_leave()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//making second call as outgoing
	    System.out.println("making second call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from second caller
	    System.out.println("Call is ended from second caller");
	    softPhoneCalling.hangupActiveCall(driver2);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_outbound_agent_leave() {
	    System.out.println("Test case --inbound_outbound_agent_leave()-- started ");
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
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//making second call as outgoing
	    System.out.println("making second call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from agent
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for second caller
	    System.out.println("call back button is visible for second caller");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_inbound_resume_merge_fist_caller_leave() {
	    System.out.println("Test case --outbound_inbound_resume_merge_fist_caller_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//taking second call as incoming call
	    System.out.println("taking second call as incoming call");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from first caller");
	    softPhoneCalling.hangupActiveCall(driver2);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_inbound_second_caller_leave() {
	    System.out.println("Test case --outbound_inbound_second_caller_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//taking second call as incoming call
	    System.out.println("taking second call as incoming call");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from second caller
	    System.out.println("Call is ended from second caller");
	    softPhoneCalling.hangupActiveCall(driver3);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_inbound_resume_merge_agent_leave() {
	    System.out.println("Test case --outbound_inbound_resume_merge_fist_caller_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//taking second call as incoming call
	    System.out.println("taking second call as incoming call");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from agent
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for caller 1
	    System.out.println("call back button is visible for caller 1");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }

	  @Test(groups={"Regression"})
	  public void outbound_outbound_resume_merge_fist_caller_leave() {
	    System.out.println("Test case --outbound_outbound_resume_merge_fist_caller_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//making second call as outgoing call
	    System.out.println("making second call as outgoing call");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
		
	   //Verifying that transfer and hold buttons are disabled during merge
	    assertFalse(softPhoneCalling.isTransferButtonEnable(driver1));
		assertFalse(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from first caller");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
	    //Verifying that tranfer and hold buttons are enabled
	    seleniumBase.idleWait(5);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_outbound_second_caller_leave() {
	    System.out.println("Test case --outbound_outbound_second_caller_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//making second call as outgoing call
	    System.out.println("making second call as outgoing call");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from first caller");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void outbound_outbound_resume_merge_agent_leave() {
	    System.out.println("Test case --outbound_outbound_resume_merge_agent_leave()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    //making first call as outgoing
	    System.out.println("making first call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up first call
	    System.out.println("picking up first outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//making second call as outgoing call
	    System.out.println("making second call as outgoing call");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from agent
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for first caller
	    System.out.println("call back button is visible for first caller");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verifying Recent Calls Detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
	    //Verify call recording for 1st caller
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(callRecordingPage.getNumberOfCallRecordings(driver1), 1, "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_inbound_with_queue_resume_merge() {
	    System.out.println("Test case --inbound_inbound_with_queue_resume_merge()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
		//Making call to user group
	    System.out.println("caller making call to user group");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");;
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//taking second incoming call
	    System.out.println("Taking second incoming call");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"MediumPriority"})
	  public void inbound_outbound_with_queue_resume_merge() {
	    System.out.println("Test case --inbound_outbound_with_queue_resume_merge()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
		//Making call to user group
	    System.out.println("caller making call to user group");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");;
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//taking second incoming call
	    System.out.println("making second call as outgoing");
	    seleniumBase.idleWait(2);
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up call from agents");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from agent
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that call is still connected with other callers.
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    softPhoneCalling.isHangUpButtonVisible(driver3);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void inbound_inbound_with_call_flow() {
	    System.out.println("Test case --inbound_inbound_with_call_flow()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		//Taking first call to call flow
	    System.out.println("caller making call to call flow");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
	    
	    //verify Queue Count
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
		//taking second call on call flow
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver4);

		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver4);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver4);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver4);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver4);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver4), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity", "ExludeForProd"})
	  public void inbound_outbound_additional_calls() {
	    System.out.println("Test case --inbound_outbound_additional_calls()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		//taking incoming call
	    System.out.println("Taking call from first caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
		//making second call as outgoing
	    System.out.println("making second call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//picking up second call
	    System.out.println("picking up second outgoing call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
	    //Making additional call
	    System.out.println("making additional call as outgoing");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
	    //verify error message for additional Call
	    assertEquals(callScreenPage.getErrorText(driver1), "Cannot dial. You have reached the maximum number of conference participants");
	    callScreenPage.closeErrorBar(driver1);
	    
		//taking additional incoming call
	    System.out.println("Taking call from third caller");
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver4);
		
		//Call is ended from agent
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//verify that call back button is visible for second caller
	    System.out.println("call back button is visible for second caller");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void merge_take_new_call_after_caller_left() {
	    System.out.println("Test case --merge_take_new_call_after_caller_left()-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);

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
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//checking that  conference button is disabled
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
	    //verifying number of callers on call screen
	    assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//taking anther incoming call after after member left conference
	    System.out.println("Taking another incoming call after member left conference");
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
	    //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void call_cold_transfer_to_user()
	  {
	    System.out.println("Test case --call_cold_transfer_to_user-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable cold transfer setting
	    loginSupport(driver1);
  		dashboard.clickAccountsLink(driver1);
  		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		accountIntelligentDialerTab.enableColdTransferSetting(driver1);
  		accountIntelligentDialerTab.saveAcccountSettings(driver1);
  		seleniumBase.closeTab(driver1);
  		seleniumBase.switchToTab(driver1, 1);
	       
		//Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callToolsPanel.clickCallNotesIcon(driver1);
	  	assertEquals(callToolsPanel.getCallNotesText(driver1).trim(), "");
	  	callToolsPanel.clickCallNotesIcon(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    String callID = sfTaskDetailPage.getCallObjectId(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that entry for transfer call is not present in Salesforce
	    String callNotes = "Transferred from: " + callID;
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    callToolsPanel.clickCallNotesIcon(driver4);
	    assertEquals(callToolsPanel.getCallNotesText(driver4).trim(), callNotes);
		callToolsPanel.clickCallNotesIcon(driver4);
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject2);
	    assertEquals(sfTaskDetailPage.getComments(driver4), callNotes);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //disable cold transfer setting
	    loginSupport(driver1);
  		dashboard.clickAccountsLink(driver1);
  		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		accountIntelligentDialerTab.disableColdTransferSetting(driver1);
  		accountIntelligentDialerTab.saveAcccountSettings(driver1);
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
	  
	  @Test(groups={"MediumPriority"})
	  public void call_cold_transfer()
	  {
	    System.out.println("Test case --call_cold_transfer-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Test 1: Transfer to user without cold transfer and verify that the call notes are blank
	    //Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
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
	    
	    //Verify that for the call came through transfer the call notes are blank
	    String callNotes = "";
	    seleniumBase.idleWait(10);
	    callToolsPanel.clickCallNotesIcon(driver4);
	    assertEquals(callToolsPanel.getCallNotesText(driver4).trim(), callNotes);
	    callToolsPanel.clickCallNotesIcon(driver4);
	    
	    //Test 2: Transfer to call flow with cold transfer and verify that the call notes have the call id for the transferred call
	    //enable cold transfer setting
	    loginSupport(driver1);
  		dashboard.clickAccountsLink(driver1);
  		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		accountIntelligentDialerTab.enableColdTransferSetting(driver1);
  		accountIntelligentDialerTab.saveAcccountSettings(driver1);
  		seleniumBase.closeTab(driver1);
  		seleniumBase.switchToTab(driver1, 1);
	       
		//Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
	    
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
	    
	    //get the call id of transferred call
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    String callID = sfTaskDetailPage.getCallObjectId(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that for the call came through transfer the call id present
	    callNotes = "Transferred from: " + callID;
	    callToolsPanel.clickCallNotesIcon(driver4);
	    assertEquals(callToolsPanel.getCallNotesText(driver4).trim(), callNotes);
		callToolsPanel.clickCallNotesIcon(driver4);
		
		//Test 3: verify call for call transfered to call queue
		//Subcribe queue for user 2
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		softPhoneCallQueues.unSubscribeQueue(driver1, queueName);
		softPhoneCallQueues.subscribeQueue(driver4, queueName);
		
		//Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_group_1_number"));
	    
		//receiving call from app softphone
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //get the call id of transferred call
	    String callSubject3 = callToolsPanel.changeAndGetCallSubject(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject3);
	    String callID2 = sfTaskDetailPage.getCallObjectId(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that for the call came through transfer the call id present
	    callNotes = "Transferred from: " + callID2;
	    callToolsPanel.clickCallNotesIcon(driver4);
	    assertEquals(callToolsPanel.getCallNotesText(driver4).trim(), callNotes);
		callToolsPanel.clickCallNotesIcon(driver4);
	    
	    //disable cold transfer setting
	    loginSupport(driver1);
  		dashboard.clickAccountsLink(driver1);
  		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		accountIntelligentDialerTab.disableColdTransferSetting(driver1);
  		accountIntelligentDialerTab.saveAcccountSettings(driver1);
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
	  
	  @Test(groups={"MediumPriority"})
	  public void user_image_not_busy_anouncement_on()
	  {
	    System.out.println("Test case --user_image_not_busy_anouncement_on-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable call announcement setting
	    loginSupport(driver4);
  		dashboard.clickAccountsLink(driver4);
  		accountCallRecordingTab.openCallRecordingTab(driver4);
  		accountCallRecordingTab.enableOutboundCallRecordingAnnoncement(driver4);
  		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
  		seleniumBase.closeTab(driver4);
  		seleniumBase.switchToTab(driver4, 1);
	       
		//Making an inbound call to first caller
	    System.out.println("second caller making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //verify that user image is free
	    callScreenPage.verifyUserImageAvailable(driver1);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //Verify resume of hold call after disconnect with new active call
	  @Test(groups={"Regression"})
	  public void inbound_resume_after_new_call() {
	    System.out.println("Test case --inbound_resume_after_new_call()-- started ");
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
	    
	    //Verify header incoming calls buttons are enabled
	    softPhoneCalling.isAdditionalCallAcceptBtnEnable(driver1);
	    softPhoneCalling.isAdditionalCallDeclineBtnEnable(driver1);
	    softPhoneCalling.isAdditionalCallSendToVMBtnEnable(driver1);
	    
		//picking up second call
	    System.out.println("picking up second incoming call");
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	    
	    //checking that  caller number  is visible
	    callToolsPanel.idleWait(5);
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"new call is not connected");
	    
	  	//End first call
	  	softPhoneCalling.hangupActiveCall(driver1);
	  	
	  	//verify second caller gets disconnected
	  	softPhoneCalling.isCallBackButtonVisible(driver2);
	  	
	    //resuming first call
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	    seleniumBase.idleWait(5);
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
		//Call is ended from first caller
	    System.out.println("Call is ended from agent");
	    softPhoneCalling.hangupActiveCall(driver3);
			    
		//verify that call back button is visible for agent
	    System.out.println("call back button is visible for agent");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @AfterMethod(groups = {"Regression", "Product Sanity"}, dependsOnMethods = "resetSetupDefault")
	  public void afterMethod(ITestResult result){
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			if (result.getName().equals("call_transfer_forwarding_to_call_flow")) {
				// Setting call forwarding OFF
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(driver1);
				softPhoneSettingsPage.disableCallForwardingSettings(driver1);
			}
			else if( result.getName().equals("call_cold_transfer_to_user") || result.getName().equals("call_cold_transfer_to_call_flow")) {
				//disable cold transfer setting
				loginSupport(driver1);
		  		dashboard.clickAccountsLink(driver1);
		  		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		  		accountIntelligentDialerTab.disableColdTransferSetting(driver1);
		  		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		  		seleniumBase.closeTab(driver1);
		  		seleniumBase.switchToTab(driver1, 1);
			}
		}
	}

	  @AfterClass(groups = {"Sanity", "ExludeForProd", "Regression", "MediumPriority", "Product Sanity"})
	  public void enableConferenceSetting(){
				
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//navigating to support page 
			loginSupport(driver1);
			
			//opening up accounts setting for load test account
			dashboard.clickAccountsLink(driver1);
			System.out.println("Account editor is opened ");
			
			//disabling Task due date option
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableConferenceSetting(driver1);
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			
			//close the tab
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
	  }
}
