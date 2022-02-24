/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;
import support.source.users.UserIntelligentDialerTab;
import utility.HelperFunctions;

public class CallRecordingOverview  extends SoftphoneBase{

	//Verify if call recording setting locked with Outbound, agent not able to record inbound calls
	@Test(groups={"Regression"})
	public void call_recording_override_allow_outbound_calls(){
		System.out.println("Test case --call_recording_override_allow_outbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
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
	
		// Switch to softphone
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
		
		//verify play icon is not visible for the activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
		softPhoneActivityPage.playRecordingBtnIsInvisible(driver1, callSubject);

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
	
	//Verify agent able to update call recording setting from drop down even when its locked
	//Verify if call recording setting locked with Inbound, agent not able to record Outbound calls
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
		
		//verify call recording is locked on add new groups page
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideDisabled(driver4);
		
		//verify call recording is locked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideDisabled(driver4, UserIntelligentDialerTab.LockedByOptions.Admin, "");;
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);				
		
		//close the tab
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
		
		//verify play icon is not visible for the activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
		softPhoneActivityPage.playRecordingBtnIsInvisible(driver1, callSubject);

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
	    
	    //update call recordings setting to default values
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify agent able to unlock call recording locked with outbound and verify changes
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
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.verifyRecordingUnlockToolTip(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		//verify call recording is Unlocked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		
		//Verify that recording option is appearing for User
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	    softPhoneSettingsPage.clickSettingIcon(driver4);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver4);
	    
		//enable call recording setting
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
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
	
	//Verify agent able to unlock call recording locked with inbound and verify changes
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
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.verifyRecordingUnlockToolTip(driver4);
	
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		groupsPage.openAddNewGroupPage(driver4);
		groupsPage.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.verifyCallRecordOverrideEnabled(driver4);
		
		//verify call recording is Unlocked on users settings
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
		userIntelligentDialerTab.verifyRecordingOverrideValue(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.verifyCallRecordOverrideEnabled(driver4);
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
	
	//Verify if call recording setting locked with None, agent not able to record any type of calls
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
		
		//switch to softphone
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
		
		//verify play icon is not visible for the activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
		softPhoneActivityPage.playRecordingBtnIsInvisible(driver1, callSubject);

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
		
		//verify play icon is not visible for the activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
		softPhoneActivityPage.playRecordingBtnIsInvisible(driver1, callSubject);

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
	
	//Verify call recorded when user have recording off from softphone but locked with ALL at account level
	@Test(groups={"Regression"})
	public void call_recording_override_user_settings(){
		System.out.println("Test case --call_recording_override_user_settings-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    //disable disable record call setting
		seleniumBase.switchToTab(driver1, 1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableRecordCallsSetting(driver1);
	    
	    //disable call recording setting for user 1
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
	    //Verify that recording option is not appearing for User
		reloadSoftphone(driver1);
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
	
	//Verify recordings of your account should be accessible by other account's Support users
	@Test(groups = { "MediumPriority"})
	public void verify_recording_url_access_other_org_user() 
	{		
		System.out.println("Test case --verify_recording_url_access_other_org_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

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
	    
	    //verify that other org support user is able to access the url
	    WebDriver qaAutoSupportDriver = getDriver();
		SFLP.softphoneLogin(qaAutoSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qaAuto_support_user_username"), CONFIG.getProperty("qaAuto_support_user_password"));
	    
  		seleniumBase.openNewBlankTab(qaAutoSupportDriver);		
		seleniumBase.switchToTab(qaAutoSupportDriver ,seleniumBase.getTabCount(qaAutoSupportDriver));
		qaAutoSupportDriver.get(callRecordingURl);
		callRecordingPage.getNumberOfCallRecordings(qaAutoSupportDriver);
		callRecordingPage.clickPlayButton(qaAutoSupportDriver, 0);
		callRecordingPage.verifyCallerName(qaAutoSupportDriver, callerName);
	    callRecordingPage.verifyRecordingDateAsToday(qaAutoSupportDriver);
	    callRecordingPage.verifyDurationNotZero(qaAutoSupportDriver);
		callRecordingPage.clickPlayButton(qaAutoSupportDriver, 0);
	    seleniumBase.closeTab(qaAutoSupportDriver);
	    seleniumBase.switchToTab(qaAutoSupportDriver, 1);
	    
	    qaAutoSupportDriver.quit();
	    qaAutoSupportDriver = null;
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//Verify access caller from call recording player page
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
	
	//Supervisor notes in sfdc task and recording player should be in sync
	//Save supervisor notes from Call Recording player, Verify in task
	@Test( groups={"Regression"})
	public void call_create_and_update_supervisor_notes() {

		System.out.println("--call_create_and_update_supervisor_notes--");

		String supervisorNotes;
		String newSupervisorNotes;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

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
	
	//Verify if call recording setting unlocked with ALL, similar setting displayed at user details and team details
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
	
	//Voicemail call recording should open in admin console and verify details
	@Test(groups = {"MediumPriority"})
	public void verify_voicemail_drop_recording_when_call_missed() {
		System.out.println("Test case --verify_voicemail_drop_recording_when_call_missed-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		// verify that call is appearin
		System.out.println("declining call");
		seleniumBase.idleWait(5);
		
		String callerName	= callScreenPage.getCallerName(driver1);
		String company		= callScreenPage.getCallerCompany(driver1);
		String title		= callScreenPage.getCallerTitle(driver1);
		
		//declining the call
		softPhoneCalling.declineCall(driver1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(3);
		
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    
	    String agentName = sfTaskDetailPage.getAssignedToUser(driver1);
	    
	    sfTaskDetailPage.clickRecordingURL(driver1);
	    callRecordingPage.verifyCallerName(driver1, callerName);
	    callRecordingPage.verifyCallerCompany(driver1, company);
	    callRecordingPage.verifyCallDirection(driver1, "Inbound");
	    callRecordingPage.verifyCallerTitle(driver1, title);
	    callRecordingPage.verifyAgentName(driver1, agentName);
	    callRecordingPage.verifyRecordingDateAsToday(driver1);
	    callRecordingPage.verifyDurationNotZero(driver1);
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
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
		String date = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a"); 

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
		int logIndex = accountLogsTab.getRecordingLogIndex(driver1, callObjectID);
		accountLogsTab.verifyRecordingLogsPresent(driver1, logIndex, date, category, null, contactName, null, newValue, callObjectID);
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
		date = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a"); 

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
		logIndex = accountLogsTab.getRecordingLogIndex(driver1, callObjectID);
		accountLogsTab.verifyRecordingLogsPresent(driver1, logIndex, date, category, null, contactName, null, newValue, callObjectID);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
			
		driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "Product Sanity", "ExludeForProd"})
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
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority", "Product Sanity" }, alwaysRun = true)
	public void enableRecording() {
		
		//initialise drivers
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enaable call recording setting on users settings
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		driverUsed.put("adminDriver", false);
	}
}