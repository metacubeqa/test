/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;

public class CallRecordingUnitedStates extends SoftphoneBase{
	
	//Verify call not recorded when related area code checked but recording set to None at account level
	@Test(groups={"selectedStates", "Regression"})
	public void call_recording_override_none_selected_states(){
		System.out.println("Test case --call_recording_override_none_selected_states-- started ");
  
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    //set the call override type as none and unlock it
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //Checking New Jersey state for Call Recording from the Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		// Verifying that call recording URL is not created in salesforce for above call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that play icon is not visible for call activities
	  	softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
	  	assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject));
	   
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify call not recorded when user number's area code unchecked under recording table
	@Test(groups={"selectedStates", "Regression"})
	public void call_recording_for_unselected_states(){
		System.out.println("Test case --call_recording_for_unselected_states-- started ");
  
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    //Unchecking New Jersey state for Call Recording from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		// Verifying that call recording URL is not created in salesforce for above call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that play icon is not visible for call activities
	  	softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
	  	assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject));
	   
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify call not recorded when area code unchecked however call recording setting locked with ALL
	@Test(groups={"selectedStates", "MediumPriority"})
	public void call_recording_all_area_code_unchecked(){
		System.out.println("Test case --call_recording_all_area_code_unchecked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	   
	    //Select call override type as All
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //Unchecking New Jersey state for Call Recording from the Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		// Verifying that call recording URL is not created in salesforce for above call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify Remove additional (confusing) text from Call Recording > United States tab
	//Verify all US calls recorded when 'Call Recording by US State' OFF
	@Test(groups={"MediumPriority", "selectedStates"})
	public void call_recording_disable_select_state(){
		System.out.println("Test case --call_recording_disable_select_state-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    //Unchecking New Jersey state for Call Recording from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//Now disable the granular control setting for US states
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
	    //Verifying that call recording URL is created in salesforce for above call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify call recorded when area code checked with Call recording by state ON
	@Test(groups={"selectedStates", "MediumPriority"})
	public void call_recording_for_selected_states(){
		System.out.println("Test case --call_recording_for_selected_states-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
	    //Verifying that call recording URL is created in salesforce for above call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //set driver false
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify call recording when additional number area code checked but default area code of user not checked
	//Verify All Party under special consent of "United States" tab under Call Recording.
	@Test(groups={"MediumPriority", "selectedStates"})
	public void call_recording_additional_area_code_checked(){
		System.out.println("Test case --verify_call_recording_additional_area_code_checked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Set data for the test
	    String defaultNumberState = "California";
	    String additionalState = "Arizona";
	    String additionalNumberAreaCode = "480";
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");
	    
	    //Unchecking Default number state and Checking Additional Number Area Call Recording for User from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, additionalState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver4);
		
		//select additional number for the area code for which we have selected call recording
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver4, additionalNumberAreaCode);
		String actualNumber = softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(driver4);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver4).contains(actualNumber));
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

	    //Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly created entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubject));
	 	
	    //Setting default Call Recording Settings for the account
		accountCallRecordingTab.switchToTab(driver4, 2);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectDefaultNumber(driver4);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	//Verify call recording when additional number area code of US state is not checked but default area code of user is checked
	@Test(groups={"Regression", "selectedStates"})
	public void call_recording_additional_area_code_unchecked(){
		System.out.println("Test case --call_recording_additional_area_code_unchecked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Set data for the test
	    String defaultNumberState = "California";
	    String additionalState = "Arizona";
	    String additionalNumberAreaCode = "480";
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");
	    
	    //Checking Default number state and unchecking Additional Number Area Call Recording for User from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, additionalState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver4);
		
		//select number according to area code on softphone
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver4, additionalNumberAreaCode);
		String actualNumber = softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(driver4);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver4).contains(actualNumber));
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

	    //Verifying that call recording URL is not created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.isCallRecordingURLInvisible(driver4);;
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly created entry
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubject));
	 	
	    //Setting default Call Recording Settings for the account
		accountCallRecordingTab.switchToTab(driver4, 2);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectDefaultNumber(driver4);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	//Verify call recording when Participant and user area code checked but dialed queue number area code unchecked
	@Test(groups={"Regression", "selectedStates"})
	public void call_recording_queue_area_code_unchecked(){
		System.out.println("Test case --call_recording_queue_area_code_unchecked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Set data for the test
	    String defaultNumberState = "California";
	    String queueState = "Michigan";
	    
		String queueName = CONFIG.getProperty("qa_group_2_name").trim();
	    String contactNumber = CONFIG.getProperty("qa_group_2_number");
	    
	    //checking Default number state and unhecking queue number Area Call Recording for User from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, queueState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver4);

		//Subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		//pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

	    //Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// verify play icon is there for the newly created entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject));
	 	
	    //Setting default Call Recording Settings for the account
		accountCallRecordingTab.switchToTab(driver4, 2);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectDefaultNumber(driver4);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	//Verify call recording when Participant and user area code checked but dialed Tracking number area code unchecked
	@Test(groups={"MediumPriority", "selectedStates"})
	public void call_recording_call_flow_area_code_unchecked(){
		System.out.println("Test case --call_recording_call_flow_area_code_unchecked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Set data for the test
	    String defaultNumberState = "New Jersey";
	    String callFlowState = "Georgia"; 
	    String contactNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
	    
	    //checking Default number state and unchecking call flow number Area Call Recording for User from the Granular control setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, callFlowState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver1);
		
		// Calling from Agent's SoftPhone to the call flow
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		//pickup the call from the user
	    softPhoneCalling.pickupIncomingCall(driver4);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

	    //Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly created entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubject));
	 	
	    //Setting default Call Recording Settings for the account
		accountCallRecordingTab.switchToTab(driver4, 2);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectDefaultNumber(driver4);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	@AfterMethod(alwaysRun = true, groups = { "selectedStates" })
	public void enableSettingForSelectedState(ITestResult result) {

		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// enable call recording setting for user 1
		loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "California");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Georgia");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Michigan");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Arizona");
		
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		// close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// reload the page
		reloadSoftphone(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("adminDriver", false);
	}
}