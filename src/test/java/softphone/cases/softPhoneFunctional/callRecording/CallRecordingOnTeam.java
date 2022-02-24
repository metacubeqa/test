/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

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

public class CallRecordingOnTeam extends SoftphoneBase{
	
	String teamName = null;
	
	//Call Recording Unlocked at Account and Team- Verify Record Calls at Softphone
	@Test(groups={"Regression"})
	public void call_recording_ulocked_account_team(){
		System.out.println("Test case --call_recording_ulocked_account_team-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Select Call Recording override type for account to All
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	
	    //unlock call recording override setting from groups
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
		
	    //Verify that recording option is appearing for User
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableRecordCallsSetting(driver1);
	    
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	//Team Recording locked with Inbound- Verify Inbound / Outbound call recording with Team Members
	@Test(groups={"Regression"})
	public void call_recording_group_override_inbound_calls(){
		System.out.println("Test case --call_recording_group_override_inbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Select Call Recording override type to inbound for group 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.verifyRecordingLockTooLTip(driver4);
		groupsPage.saveGroup(driver4);
		
		//verify call recording is inound and locked on users settings
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
	    
	    //Verify that call recording is not getting created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

	    //Verifying that call recording URL is not created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that call recording is created for Inbound calls
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

	//Team recording Locked with ALL- verify all direct incoming/ outgoing calls recorded
	@Test(groups={"Regression"})
	public void call_recording_group_override_all(){
		System.out.println("Test case --call_recording_group_override_all-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Select Call Recording override type to All for group 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
		
		//verify call recording is All and locked on users settings
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
	
	    //Verify that call recording is created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//verify that call recording is created for inbound call as well
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Verify that play icon is visible for both activities
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject2);
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject1));
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject2));
	    
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
	
	//Team Recording locked with Outbound- Verify Inbound/Outbound call recording with Team Members
	@Test(groups={"MediumPriority"})
	public void call_recording_team_override_outbound_calls() {
		System.out.println("Test case --call_recording_group_override_outbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable call recording setting for user 1
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //Select Call Recording override type to outbound for group 1
 		groupsPage.openGroupSearchPage(driver4);
 		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
 		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
 		groupsPage.lockRecordingSetting(driver4);
 		groupsPage.verifyRecordingLockTooLTip(driver4);
 		groupsPage.saveGroup(driver4);
 		
 		//verify call recording is outbound and locked on users settings
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
	    
 	    //verify that call recording is getting created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//verify that call recording is not getting created for inbound call
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Verify that play icon is visible for only outbound call activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject2);
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject1));
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject2));
	    
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
	
	//Call Recording TEAM locked with ALL- Caller's area code state not checked in table
	@Test(groups={"selectedStates", "Regression"})
	public void call_recording_group_override_area_code_disable(){
		System.out.println("Test case --call_recording_group_override_area_code_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	  		
	    //Select Call Recording override type to All for group 1
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		groupsPage.lockRecordingSetting(driver4);
		groupsPage.saveGroup(driver4);
		
	    //uncheck area code to disable recording
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
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

	    //Verifying that call recording URL is not created in salesforce for above call
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
	    
	    //uncheck area code to disable recording
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "New Jersey");
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
	//Call Recording at Team set to None- Record calls by Call Flow for Team Member
	@Test(groups = { "MediumPriority" })
	public void call_recording_team_none_call_flow() {
		System.out.println("Test case --call_recording_team_none_call_flow-- started ");
	
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	
	    //Select Call Recording override type to None for group 1
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
	
		//Verify that play icon is visible for call activity
		softPhoneActivityPage.isTaskVisibleInActivityList(driver4, callSubject);
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver4, callSubject));
	
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
	
	//Any Team of user have set Call Recording override NONE, no calls will be record for team members
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
	
	//One Team have None and Second have ALL- No Call record-None  Team deleted
	//Delete the User's team who have set recording override setting None, call should start to record as per other Teams
	@Test(groups={"MediumPriority"})
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
		teamName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(4));
		String teamDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(4));
		groupsPage.addNewGroupDetails(driver4, teamName, teamDesc);
		groupsPage.addMember(driver4, CONFIG.getProperty("qa_standard_user_name"));
		
	    //Select Call Recording override type to none
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

		// verify call recording icon is green
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
		
		//verify play icon is not visible
		softPhoneActivityPage.isTaskVisibleInActivityList(standardUserDriver, callSubject);
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(standardUserDriver, callSubject));

		// delete team which has call override setting on
		loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.deleteGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(standardUserDriver);
	    
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
	    softPhoneActivityPage.isTaskVisibleInActivityList(standardUserDriver, callSubject);
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
	
	//Lock the Account setting with ALL when Team have locked with None- all calls recorded for members
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
	
		//verify call recording is created for outbound call
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
	
		//verify that call recording is created for inbond calls as well
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

	//Team Recording clashing- Team 1 have inbound, Team 2 have Outbound- No Call Record
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
	    
	    //verify that call recording is not getting created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//verify that call recording is not getting created for ibound calls as well
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Verify that play icon is not visible for call activities
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject1);
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject1));
		
		//Verify that play icon is not visible for call activities
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject2);
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject2));
		
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
	
	//Team Recording clashing- Team 1 have Inbound- second Team have ALL- Inbound call recorded
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
	    
	    //verify that call recordig is not getting created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//verify that call recording is getting created for inbound call
	    contactNumber = CONFIG.getProperty("qa_user_1_number");

		// Calling to Agent's
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);

		// receiving call from agent
		softPhoneCalling.pickupIncomingCall(driver1);

		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Verify that play icon is visible only for incoming call activities
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject1);
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject1));
		
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject2);
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject2));
	    
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
	
	//Two teams have Inbound - Inbound call recorded
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
	    
	    //verify that call recording is not getting created for outbound call
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

	    //Verifying that call recording URL is not created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify taht call recording is getting created for inbound call
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
	    
	    //Verifying that call recording URL is created in salesforce for above call
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
	
	//Two Teams have Outbound- Outbound call recorded
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
	    
	    //verify that call recording is getting created for outbound calls
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

	    //Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that call recording is not getting created for inbound call
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

	    //Verifying that call recording URL is not created in salesforce for above call
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
	
	//Two team have set ALL- ALL Calls recorded
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
	    
	    //verify that call recording is getting created for outbound call
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
	    
	    //verify that call recording is getting created for inboud call as well
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
			
			if(result.getName().equals("call_recording_group_override_area_code_disable")) {
				accountCallRecordingTab.swtichToUnitedStateTab(driver4);
				accountCallRecordingTab.checkAreasForCallRecording(driver4, "New Jersey");
				accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
			}
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
			if(groupsPage.isCallRecordingOverrideLocked(driver4)) {
				groupsPage.unlockRecordingSetting(driver4);
				groupsPage.saveGroup(driver4);	
			}
		}
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
		driverUsed.put("driver4", false);
	}
}