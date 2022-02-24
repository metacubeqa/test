/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;
import support.source.users.UserIntelligentDialerTab;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallRecordings extends SoftphoneBase{
	
	/*String teamName = null;
	
	@Test(groups={"Sanity", "selectedStates", "Regression"})
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
	   
		//Verifying Recent Calls Detail
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
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
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
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	@Test(groups={"Regression", "selectedStates"})
	public void call_recording_disable_select_state(){
		System.out.println("Test case --call_recording_disable_select_state-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
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
	   
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//@Test(groups={"Regression", "selectedStates"})
	public void verify_call_recording_when_additional_area_code_checked_and_default_unchecked(){
		System.out.println("Test case --verify_call_recording_when_additional_area_code_checked_and_default_unchecked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
	    initializeDriverSoftphone("webSupportDriver");
	    driverUsed.put("webSupportDriver", true);
	    
	    String defaultNumberState = "New Jersey";
	    String additionalState = "California";
	    String receiverAreaCodeState = "California";
	    String additionalNumberAreaCode = "415";
	    
	    String contactNumber = CONFIG.getProperty("qa_support_user_number");
	    
	    //disable call recording setting for user 1
	    loginSupport(adminDriver);
		accountCallRecordingTab.openCallRecordingTab(adminDriver);
		accountCallRecordingTab.enableAllowGranularControlSetting(adminDriver);
		accountCallRecordingTab.uncheckAreasForCallRecording(adminDriver, defaultNumberState);
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, additionalState);
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, receiverAreaCodeState);
		accountCallRecordingTab.saveCallRecordingTabSettings(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
	    
		//reload the page
		reloadSoftphone(adminDriver);
		
		//select number according to area code on softphone
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.clickOutboundNumbersTab(adminDriver);
		softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(adminDriver, additionalNumberAreaCode);
		String actualNumber = softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(adminDriver);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(adminDriver).contains(actualNumber));
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(adminDriver, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(adminDriver);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(webSupportDriver);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(adminDriver);
		String callSubject = callToolsPanel.changeAndGetCallSubject(adminDriver);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(webSupportDriver);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(adminDriver);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(adminDriver);
		contactDetailPage.openRecentCallEntry(adminDriver, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(adminDriver);
		sfTaskDetailPage.verifyCallStatus(adminDriver, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(adminDriver).contains("recordings");
		seleniumBase.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);

		// verify play icon is there for the newly created entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(adminDriver, callSubject));
	 	
		accountCallRecordingTab.switchToTab(adminDriver, 2);
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(adminDriver);
		accountCallRecordingTab.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
		
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.clickOutboundNumbersTab(adminDriver);
		softPhoneSettingsPage.selectDefaultNumber(adminDriver);
		reloadSoftphone(adminDriver);
		
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("webSupportDriver", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	//@Test(groups={"Regression", "selectedStates"})
	public void verify_call_recording_when_additional_area_code_unchecked_and_default_checked(){
		System.out.println("Test case --verify_call_recording_when_additional_area_code_unchecked_and_default_checked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String defaultNumberState = "New Jersey";
	    String additionalState = "California";
	    String receiverAreaCodeState = "New Jersey";
	    String additionalNumberAreaCode = "415";
	    
	    String contactNumber = CONFIG.getProperty("qa_agent_user_number");
	    
	    //disable call recording setting for user 1
	    loginSupport(adminDriver);
		accountCallRecordingTab.openCallRecordingTab(adminDriver);
		accountCallRecordingTab.enableAllowGranularControlSetting(adminDriver);
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, defaultNumberState);
		accountCallRecordingTab.uncheckAreasForCallRecording(adminDriver, additionalState);
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, receiverAreaCodeState);
		accountCallRecordingTab.saveCallRecordingTabSettings(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
	    
		//reload the page
		reloadSoftphone(adminDriver);
		
		//select number according to area code on softphone
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.clickOutboundNumbersTab(adminDriver);
		softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(adminDriver, additionalNumberAreaCode);
		String actualNumber = softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(adminDriver);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(adminDriver).contains(actualNumber));
		
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(adminDriver, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(adminDriver);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(agentDriver);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(adminDriver);
		
		if (!callScreenPage.isCallerMultiple(adminDriver) && callScreenPage.isCallerUnkonwn(adminDriver)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(adminDriver, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}
		
		String callSubject = callToolsPanel.changeAndGetCallSubject(adminDriver);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(agentDriver);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(adminDriver);

		// Verifying Recent Calls Detail
		callScreenPage.selectFirstContactFromMultiple(adminDriver);
		callScreenPage.openCallerDetailPage(adminDriver);
		contactDetailPage.openRecentCallEntry(adminDriver, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(adminDriver);
		sfTaskDetailPage.verifyCallStatus(adminDriver, "Connected");
		sfTaskDetailPage.isCallRecordingURLInvisible(adminDriver);;
		seleniumBase.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);

		// verify play icon is there for the newly created entry
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(adminDriver, callSubject));
	 	
		accountCallRecordingTab.switchToTab(adminDriver, seleniumBase.getTabCount(adminDriver));
		accountCallRecordingTab.checkAreasForCallRecording(adminDriver, additionalState);
		accountCallRecordingTab.saveCallRecordingTabSettings(adminDriver);
		accountCallRecordingTab.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
		
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.clickOutboundNumbersTab(adminDriver);
		softPhoneSettingsPage.selectDefaultNumber(adminDriver);
		reloadSoftphone(adminDriver);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("agentDriver", false);

		System.out.println("verify_call_recording_when_additional_area_code_unchecked_and_default_checked");
	}
	
	@AfterMethod(alwaysRun = true, groups = { "selectedStates" })
	public void enableSettingForSelectedState(ITestResult result) {

		switch (result.getName()) {
		case "call_recording_disable_select_state":
		case "call_recording_for_selected_states":
		case "call_recording_group_override_area_code_disable":
		case "verify_call_recording_when_additional_area_code_checked_and_default_unchecked":
		case "verify_call_recording_when_additional_area_code_unchecked_and_default_checked":

			initializeDriverSoftphone("driver4");
			driverUsed.put("driver4", true);

			// enable call recording setting for user 1
			loginSupport(driver4);
			accountCallRecordingTab.openCallRecordingTab(driver4);
			accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
			accountCallRecordingTab.enableCallRecordingSetting(driver4);
			accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
			accountCallRecordingTab.checkAreasForCallRecording(driver4, "New Jersey");
			accountCallRecordingTab.checkAreasForCallRecording(driver4, "California");
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
	
	@Test(groups={"Regression"})
	 public void call_recording_verify_related_contact(){
		System.out.println("Test case --call_recording_verify_related_contact-- started ");

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
		
		//get caller name
		String callerName = callScreenPage.getCallerName(driver1);
		
		//verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    
	    //Verifying call recording and related contact
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver1), "number of call recordings are not same");
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.openCallerSFDCLink(driver1);
	    assertEquals(contactDetailPage.getCallerName(driver1), callerName);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void disable_call_recording_from_setting_page(){
		System.out.println("Test case --disable_call_recording_from_setting_page-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable disable record call setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableRecordCallsSetting(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		//Verifying Recent Calls Detail
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);		
	    
	    //enable Call recording setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_group_override_all(){
		System.out.println("Test case --call_recording_group_override_all-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Team, CONFIG.getProperty("qa_group_1_name"));
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");		
		groupsPage.disableCallRecordingOverrideSetting(driver4);
	    groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_group_override_inbound_calls(){
		System.out.println("Test case --call_recording_group_override_inbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.verifyRecordingLockTooLTip(driver4);
		groupsPage.saveGroup(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Team, CONFIG.getProperty("qa_group_1_name"));
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");		
		groupsPage.disableCallRecordingOverrideSetting(driver4);
	    groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups={"selectedStates", "Regression"})
	public void call_recording_group_override_area_code_disable(){
		System.out.println("Test case --call_recording_group_override_area_code_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	  		
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
		
	    //uncheck area code to disable recording
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "New Jersey");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");		
		groupsPage.disableCallRecordingOverrideSetting(driver4);
	    groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups={"Regression"})
	public void call_recording_team_override_outbound_calls() {
		System.out.println("Test case --call_recording_group_override_outbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
 		groupsPage.openGroupSearchPage(driver4);
 		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
 		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
 		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.verifyRecordingLockTooLTip(driver4);
 		groupsPage.saveGroup(driver4);
 		
 		//verify call recording is locked on users settings
 		dashboard.clickOnUserProfile(driver4);
 		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
 		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Team, CONFIG.getProperty("qa_group_1_name"));
 		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
 		
 		//close the tab
 		seleniumBase.closeTab(driver4);
 		seleniumBase.switchToTab(driver4, 1);
 	    
 	    //reload the page
 	    reloadSoftphone(driver1);
 	    
 	    //Verify that recording option is not appearing for User
 	    softPhoneSettingsPage.clickSettingIcon(driver1);
 	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");		
		groupsPage.disableCallRecordingOverrideSetting(driver4);
	    groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //enable Call recording setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
			
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	
	}

	@Test(groups = { "Regression" })
	public void call_recording_team_override_none_account_all_calls() {
		System.out.println("Test case --call_recording_group_override_outbound_calls-- started ");
	
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	
		// None override setting set for Recording on Team
		loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.verifyRecordingLockTooLTip(driver4);
		groupsPage.saveGroup(driver4);
	
		// verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4,
		UserIntelligentDialerTab.LockedByOptions.Team, CONFIG.getProperty("qa_group_1_name"));
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4,
		AccountCallRecordingTab.CallRecordingOverrideOptions.None);
	
		// All override setting Locked at Account
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4,
		AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		// verify call recording now locked with ALL on Group
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
	
		// verify call recording is now locked with ALL on users
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4,
		AccountCallRecordingTab.CallRecordingOverrideOptions.All);
	
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		// reload the page
		reloadSoftphone(driver1);
	
		// Verify that recording option is not appearing for User
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	
		String contactNumber = CONFIG.getProperty("qa_user_2_number");
	
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
	
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
	
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	
		// Verifying Recent Calls Detail
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
	
		contactNumber = CONFIG.getProperty("qa_user_1_number");
	
		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
	
		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
	
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
	
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	
		// Verifying Recent Calls Detail
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
	
		// Disable recording from Account
		loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		// Disable recording from Team
		driver4.get(CONFIG.getProperty("qa_support_tool_site"));
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.disableCallRecordingOverrideSetting(driver4);
		groupsPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_override_enable_all_calls(){
		System.out.println("Test case --call_recording_override_enable_all_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify call recording is locked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");;
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_override_disable_all_calls(){
		System.out.println("Test case --call_recording_override_disable_all_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify call recording is locked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");;
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		seleniumBase.idleWait(4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(4);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_override_allow_inbound_calls(){
		System.out.println("Test case --call_recording_override_allow_inbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify call recording is locked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");;
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);				
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_override_allow_outbound_calls(){
		System.out.println("Test case --call_recording_override_allow_outbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable disable record call setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableRecordCallsSetting(driver1);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify call recording is locked on groups page
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");;
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);				
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//disable call recording setting for user 1
		 loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountCallRecordingTab.openCallRecordingTab(driver1);
		accountCallRecordingTab.verifySupportDisabledSetttings(driver1);
		accountCallRecordingTab.verifyRecordingOverrideValue(driver1, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);

		// close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	    
	    //enable Call recording setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_override_user_settings(){
		System.out.println("Test case --call_recording_override_user_settings-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable disable record call setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableRecordCallsSetting(driver1);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
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
	    
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	    
	    //enable Call recording setting
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void verify_call_restrict_download_setting(){
		System.out.println("Test case --verify_call_restrict_download_setting-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.restrictCallRecordingSettingVisible(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//updating the driver used
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_download_admin(){
		System.out.println("Test case --call_recording_download_admin-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);

	    //enable call recording restrictions
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	  	
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    sfTaskDetailPage.clickRecordingURL(driver4);
	    assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(driver4));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //disable call recording restrictions
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.disableRestrictCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	    
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_download_agent(){
		System.out.println("Test case --call_recording_download_agent-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected");
	    sfTaskDetailPage.clickRecordingURL(driver3);
	    assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(driver3));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_download_agent_disable(){
		System.out.println("Test case --call_recording_download_agent_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);

	    //enable call recording restrictions
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	  	
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver3);
	    sfTaskDetailPage.verifyCallStatus(driver3, "Connected");
	    sfTaskDetailPage.clickRecordingURL(driver3);
	    assertFalse(callRecordingPage.isDownloadRecordingButtonVisible(driver3));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    seleniumBase.closeTab(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    
	    //disable call recording restrictions
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.disableRestrictCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
	    
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test( groups={"Regression"})
	public void call_create_and_update_supervisor_notes() {

		System.out.println("--call_create_and_update_supervisor_notes--");

		String supervisorNotes;
		String newSupervisorNotes;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//giving supervisor notes
		supervisorNotes = "This is SupervisorNotes: " + helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, supervisorNotes);

		//hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify supervisor notes
		newSupervisorNotes = "This is updated SupervisorNotes: " + helperFunctions.GetCurrentDateTime();
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
		String taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver3);
		assertEquals(supervisorNotes.trim(), taskSupervisorNotes.trim());
		sfTaskDetailPage.clickRecordingURL(driver3);
		callRecordingPage.verifySupervisorNotes(driver3, supervisorNotes);
		
		//Update supervisor notes and verify them in sfdc tasks
		callRecordingPage.updateSuperVisorNotes(driver3, newSupervisorNotes);
		
		//adding the delay required for data sync
		softPhoneCalling.idleWait(30);
		
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
		driver3.navigate().refresh();
		taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver3);
		assertEquals(taskSupervisorNotes.trim(), newSupervisorNotes.trim());
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");
	}
	
	@Test(groups={"Regression", "MediumPriority"})
	public void call_recording_account_log_softphone(){
		System.out.println("Test case --call_recording_account_log_softphone-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	  	
	    String contactNumber	= CONFIG.getProperty("prod_user_1_number");
	    String contactName 		= CONFIG.getProperty("qa_user_1_name");
	    String date 			= HelperFunctions.GetCurrentDateTime("MM/dd/yyyy"); 
	    String category 		= "Recording";
	    String dateRange		= "Today";
	    String newValue			= "Played";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    String callObjectID = sfTaskDetailPage.getCallObjectId(driver1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(driver1));
	    callRecordingPage.clickPlayButton(driver1, 0);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountLogsTab.navigateToAccountLogTab(driver1);
		accountLogsTab.searchAccountLogs(driver1, contactName, category, dateRange);
		accountLogsTab.verifyRecordingLogsPresent(driver1, category, date, contactName, callObjectID, newValue);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    callObjectID = sfTaskDetailPage.getCallObjectId(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    softPhoneActivityPage.playTaskRecording(driver1, callSubject);
	    seleniumBase.idleWait(1);
	    softPhoneActivityPage.getRecordingPlayerCurrentTime(driver1, callSubject);
	    
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountLogsTab.navigateToAccountLogTab(driver1);
		accountLogsTab.searchAccountLogs(driver1, contactName, category, dateRange);
		accountLogsTab.verifyRecordingLogsPresent(driver1, category, date, contactName, callObjectID, newValue);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
			
		driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void verify_recording_url_access_other_user() 
	{		
		System.out.println("---------------- Test Case verify_recording_url_access_other_user Started ------------------");

	    //updating the driver used
	    initializeDriverSoftphone("standardUserDriver");
	    driverUsed.put("standardUserDriver", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.addSupervisor(driver4, CONFIG.getProperty("qa_standard_user_name"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(standardUserDriver);
	
  		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
  		callToolsPanel.dropFirstVoiceMail(driver4);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    callScreenPage.openCallerDetailPage(driver4);
  		contactDetailPage.openRecentCallEntry(driver4, callSubject);
  	    String automatedRecordingURl = sfTaskDetailPage.getAutomatedVMURL(driver4);
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
		
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(standardUserDriver);		
		seleniumBase.switchToTab(standardUserDriver,seleniumBase.getTabCount(standardUserDriver));
		standardUserDriver.get(automatedRecordingURl);
		callRecordingPage.getNumberOfCallRecordings(standardUserDriver);
		callRecordingPage.clickPlayButton(standardUserDriver, 0);
	    seleniumBase.closeTab(standardUserDriver);
	    seleniumBase.switchToTab(standardUserDriver, 1);
	    
	    //Deleting member from the group
	    loginSupport(driver4);
  		dashboard.isPaceBarInvisible(driver4);
  		groupsPage.openGroupSearchPage(driver4);
  		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
  		//deleting newly added member from group
  		groupsPage.addMember(driver4, CONFIG.getProperty("qa_user_2_name"));
  		groupsPage.deleteSuperviosr(driver4, CONFIG.getProperty("qa_standard_user_name"));
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
  		reloadSoftphone(standardUserDriver);
  	    
  	    //verify that user is not able to access the url
  		seleniumBase.openNewBlankTab(standardUserDriver);		
  		seleniumBase.switchToTab(standardUserDriver, seleniumBase.getTabCount(standardUserDriver));
  		standardUserDriver.get(automatedRecordingURl);
  		callRecordingPage.verifyAccessDeniedMessage(standardUserDriver);
  	    seleniumBase.closeTab(standardUserDriver);
  	    seleniumBase.switchToTab(standardUserDriver, 1);
		
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("standardUserDriver", false);
		driverUsed.put("driver4", false);

		System.out.println("Test Case is Pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_recording_url_access_other_org_user() 
	{		
		System.out.println("Test case --verify_recording_url_access_other_org_user-- started ");

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
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//hangup active call
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
	    //Verify that the date format is proper on call recording page and get url
		String callerName = callScreenPage.getCallerName(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    String callRecordingURl = sfTaskDetailPage.getCallRecordingUrl(driver1);
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    sfTaskDetailPage.idleWait(2);
	    HelperFunctions.isDateInGivenFormat("MM/dd/yyyy hh:mm a", callRecordingPage.getRecordingDate(driver1));
	    callRecordingPage.openCallerTaskLink(driver1);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(webSupportDriver);		
		seleniumBase.switchToTab(webSupportDriver,seleniumBase.getTabCount(webSupportDriver));
		webSupportDriver.get(callRecordingURl);
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
	
	@Test(groups={"Regression"})
	public void call_recording_group_override_setting_state(){
		System.out.println("Test case --call_recording_group_override_setting_state-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable call recording restrictions
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	
	    //disable call recording override setting from group
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.unlockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.unlockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
		
	    //Verify that recording option is not appearing for User
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
	    
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "MediumPriority"})
	public void call_recording_multiple_group_override(){
		System.out.println("Test case --call_recording_multiple_group_override-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("standardUserDriver");
	    driverUsed.put("standardUserDriver", true);
	    
	    String groupNumber = CONFIG.getProperty("qa_group_5_number");
	    
	    //disable call recording for the user
	    softPhoneSettingsPage.clickSettingIcon(standardUserDriver);
	    softPhoneSettingsPage.disableRecordCallsSetting(standardUserDriver);
	    
	    // create a group and add the same member
	    loginSupport(driver4);
		groupsPage.openAddNewGroupPage(driver4);
		String teamName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(4));
		String teamDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(4));
		groupsPage.addNewGroupDetails(driver4, teamName, teamDesc);
		groupsPage.addMember(driver4, CONFIG.getProperty("qa_standard_user_name"));
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);	
		
		//lock call recording for other group for all calls
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    reloadSoftphone(standardUserDriver);

	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(standardUserDriver, CONFIG.getProperty("qa_group_5_name"));
	    softPhoneCallQueues.isQueueSubscribed(standardUserDriver, CONFIG.getProperty("qa_group_5_name"));
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, groupNumber);
		softPhoneCallQueues.pickCallFromQueue(standardUserDriver);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(standardUserDriver);
		seleniumBase.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(standardUserDriver);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(standardUserDriver);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(standardUserDriver);
		contactDetailPage.openRecentCallEntry(standardUserDriver, callSubject);
		sfTaskDetailPage.isCallRecordingURLInvisible(standardUserDriver);
		seleniumBase.closeTab(standardUserDriver);
		seleniumBase.switchToTab(standardUserDriver, 1);

		// delete team which has call override setting on
		loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.deleteGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(standardUserDriver);

	    //disable call recording for the user
	    softPhoneSettingsPage.clickSettingIcon(standardUserDriver);
	    softPhoneSettingsPage.enableRecordCallsSetting(standardUserDriver);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, groupNumber);
		softPhoneCallQueues.openCallQueuesSection(standardUserDriver);
		softPhoneCallQueues.pickCallFromQueue(standardUserDriver);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(standardUserDriver);
		seleniumBase.idleWait(5);
		callSubject = callToolsPanel.changeAndGetCallSubject(standardUserDriver);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(standardUserDriver);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(standardUserDriver);
		contactDetailPage.openRecentCallEntry(standardUserDriver, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(standardUserDriver);
	    sfTaskDetailPage.verifyCallStatus(standardUserDriver, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(standardUserDriver).contains("recordings");
	    seleniumBase.closeTab(standardUserDriver);
	    seleniumBase.switchToTab(standardUserDriver, 1);
	    
	    //verify play icon is there for the newly created entry
	    assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(standardUserDriver, callSubject));
	    
	    //disable reocrding for other team as well
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.unlockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    reloadSoftphone(standardUserDriver);
	    
	    driverUsed.put("driver2", false);
	    driverUsed.put("standardUserDriver", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"})
	public void call_recording_three_groups_override(){
		System.out.println("Test case --call_recording_three_groups_override-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override All call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
 		
	    // Set call recording override None call for first group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying in Recent Calls Detail that recording is not present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail for recording url is invisible
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    enableRecording();
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}
	
	//@Test(groups={"selectedStates", "Regression", "MediumPriority"})
	public void call_recording_country_code_disable(){
		System.out.println("Test case --call_recording_country_code_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true); 
	  		
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		
	    //uncheck area code to disable recording
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "EUROPE");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "United Kingdom");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    	
	    String contactNumber = CONFIG.getProperty("qa_user_2_Japan_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_uk_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //disable call recording by country granular setting
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
	    accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    //reload the page
	    reloadSoftphone(driver1);

 		// Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
 		softPhoneCalling.pickupIncomingCall(driver4);
 		callScreenPage.verifyRecordingisActive(driver1);
 		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
 		
 		// hanging up with caller 1
 		System.out.println("hanging up with caller 1");
 		softPhoneCalling.hangupActiveCall(driver4);
 		System.out.println("Call is removing from softphone");
 		softPhoneCalling.isCallBackButtonVisible(driver1);

 		// Verifying Recent Calls Detail
 		callScreenPage.openCallerDetailPage(driver1);
 		contactDetailPage.openRecentCallEntry(driver1, callSubject);
 	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
 	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
 	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);
 	    
 		// Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_uk_number"));
 		softPhoneCalling.pickupIncomingCall(driver3);
 		callScreenPage.verifyRecordingisActive(driver1);
 		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
 		
 		// hanging up with caller 1
 		System.out.println("hanging up with caller 1");
 		softPhoneCalling.hangupActiveCall(driver3);
 		System.out.println("Call is removing from softphone");
 		softPhoneCalling.isCallBackButtonVisible(driver1);

 		// Verifying Recent Calls Detail
 		callScreenPage.openCallerDetailPage(driver1);
 		contactDetailPage.openRecentCallEntry(driver1, callSubject);
 	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
 	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
 	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);
 	    
	    //disable call recording setting for user 1
 	   loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_recording_unlock_verify_tooltips(){
		System.out.println("Test case --call_recording_unlock_verify_tooltips-- started ");

		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Unlock call recording setting from admin user
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.verifyRecordingLockToolTip(driver4);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.verifyRecordingUnlockToolTip(driver4);
		
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		//verify call recording is Unlocked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
		
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
		
		//enable call recording setting
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups={"MediumPriority"})
	public void call_recording_inbound_verify_tooltips(){
		System.out.println("Test case --call_recording_inbound_verify_tooltips-- started ");

		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Unlock call recording setting from admin user
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is Unlocked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		
		//enable call recording setting
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_recording_outbound_verify_tooltips(){
		System.out.println("Test case --call_recording_outbound_verify_tooltips-- started ");

		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Unlock call recording setting from admin user
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is Unlocked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		
		//enable call recording setting
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_recording_team_none_call_flow() {
		System.out.println("Test case --call_recording_team_none_call_flow-- started ");
	
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	
		// None override setting set for Recording on Team
		loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.verifyRecordingLockTooLTip(driver4);
		groupsPage.saveGroup(driver4);
	
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		// reload the page
		reloadSoftphone(driver1);
	
		// Verify that recording option is not appearing for User
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	
		String contactNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
	
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
	
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);
	
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
	
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	
		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.clickRecordingURL(driver4);
		assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver4), "number of call recordings are not same");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver1));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		// Disable recording from Account
		loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		// Disable recording from Team
		driver4.get(CONFIG.getProperty("qa_support_tool_site"));
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.disableCallRecordingOverrideSetting(driver4);
		groupsPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_recording_teams_inbound_outbound(){
		System.out.println("Test case --call_recording_teams_inbound_outbound-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override All call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying in Recent Calls Detail that recording is not present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisInactive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail for recording url is invisible
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    // Set call recording override All call for first group
	    loginSupport(driver4);		
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups={"MediumPriority"})
	public void call_recording_teams_inbound_all(){
		System.out.println("Test case --call_recording_teams_inbound_all-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override All call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying in Recent Calls Detail that recording is not present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify play icon is there for the newly created entry
	    assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject));
	    
	    // Set call recording override All call for first group
	    loginSupport(driver4);		
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_recording_teams_inbound_inbound(){
		System.out.println("Test case --call_recording_teams_inbound_inbound-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override to Inbound call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override to Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// outbound Call from agent
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording is inactive
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying in Recent Calls Detail that recording is not present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //inbound call to agent
	    contactNumber = CONFIG.getProperty("qa_user_1_number");
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is active
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//verify that call recording url is present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    // Set call recording override All call for first group	
	    loginSupport(driver4);		
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override All call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_recording_teams_outbound_outbound(){
		System.out.println("Test case --call_recording_teams_outbound_outbound-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override to Outbound call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override to Outbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// outbound Call from agent
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording is Active
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//verify that call recording url is present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //inbound call to agent
	    contactNumber = CONFIG.getProperty("qa_user_1_number");
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is inactive
		callScreenPage.verifyRecordingisInactive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Verifying in Recent Calls Detail that recording is not present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    // Set call recording override Inbound call for first group	
	    loginSupport(driver4);		
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}
	

	@Test(groups={"MediumPriority"})
	public void call_recording_teams_all_all(){
		System.out.println("Test case --call_recording_teams_all_all-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    // Set call recording override to Outbound call for first group
	    loginSupport(driver4);		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override to Outbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// outbound Call from agent
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording is Active
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//verify that call recording url is present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //inbound call to agent
	    contactNumber = CONFIG.getProperty("qa_user_1_number");
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is active
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//verify that call recording url is present
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    // Set call recording override Inbound call for first group	
	    loginSupport(driver4);		
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4); 		
		
	    // Set call recording override Inbound call for second group		
 		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.unlockRecordingSetting(driver4);
 		groupsPage.saveGroup(driver4);
		
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);	   
	    
	    System.out.println("Test case is pass");
	}

	@AfterMethod(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" })
	public void afterMethod(ITestResult result) {
		if(result.getName().equals("call_recording_multiple_group_override") && (result.getStatus() == 2 || result.getStatus() == 3)) {
			if (teamName != null) {
				initializeDriverSoftphone("driver1");
			    driverUsed.put("driver1", true);
			    
			    // delete team which has call override setting on
			    loginSupport(driver1);
				groupsPage.openGroupSearchPage(driver1);
				groupsPage.openGroupDetailPage(driver1, teamName, CONFIG.getProperty("qa_user_account"));
				groupsPage.deleteGroup(driver1);
				seleniumBase.closeTab(driver1);
				seleniumBase.switchToTab(driver1, 1);
				
				teamName = null;	
			}
			
			initializeDriverSoftphone("driver4");
		    driverUsed.put("driver4", true);
		    
			loginSupport(driver4);
			groupsPage.openGroupSearchPage(driver4);
			groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
			groupsPage.unlockRecordingSetting(driver4);
			groupsPage.saveGroup(driver4);
		    seleniumBase.closeTab(driver4);
		    seleniumBase.switchToTab(driver4, 1);
		    
		 // Setting driver used to false as this test case is pass
		 driverUsed.put("driver1", false);	
		}else if(result.getStatus() == 2 || result.getStatus() == 3) {
			//initialise drivers
		    initializeDriverSoftphone("driver4");
		    driverUsed.put("driver4", true);
		    
		    loginSupport(driver4);
			accountCallRecordingTab.openCallRecordingTab(driver4);
			accountCallRecordingTab.enableCallRecordingSetting(driver4);
			accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
			seleniumBase.closeTab(driver4);
			seleniumBase.switchToTab(driver4, 1);
		}
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" }, alwaysRun = true)
	public void enableRecording() {
		
		//initialise drivers
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enaable call recording setting on users settings
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	
	  	List<String> groups = new ArrayList<>();
	  	groups.add(CONFIG.getProperty("qa_group_1_name"));
	  	groups.add(CONFIG.getProperty("qa_group_2_name"));
	  	groups.add(CONFIG.getProperty("qa_group_3_name"));
	  	groups.add(CONFIG.getProperty("qa_group_5_name"));
	  
	  	for (String group : groups) {
	  		groupsPage.openGroupSearchPage(driver4);
			groupsPage.openGroupDetailPage(driver4, group, "");
			groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
			groupsPage.lockRecordingSetting(driver4);
			groupsPage.saveGroup(driver4);
			groupsPage.unlockRecordingSetting(driver4);
			groupsPage.saveGroup(driver4);	
		}
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.clickOutboundNumbersTab(adminDriver);
		softPhoneSettingsPage.selectDefaultNumber(adminDriver);
		
		driverUsed.put("adminDriver", false);
	}*/
}