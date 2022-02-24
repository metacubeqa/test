/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;

public class SingleCallRecording extends SoftphoneBase{
	
	@BeforeClass(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" })
	public void setDefaultSettings() {
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    loginSupport(driver4);
	    accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.setDefaultCallRecordingSettings(driver4);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	    
	}

	//Verify when granular state by state recording is enabled agent only call recordings will not be generated
	@Test(groups={"selectedStates", "MediumPriority"})
	public void call_recording_for_unselected_states(){
		System.out.println("Test case --call_recording_for_unselected_states-- started ");
  
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	   
	    //enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //checking New Jersey state for Call Recording from the Granular control setting
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
	   
		// Verifying that call recording URL is created in salesforce for above call
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
	    
	    //Verify that play icon is visible for call activities
	  	softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
	  	assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject));
	   
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent call rec for outbound calls  when recording is enabled to  Inbound and state by state granular recording is disabled
	//Verify agent recording Url for inbound calls when call recording is enabled to Inbound and state by state granular recording is disabled
	@Test(groups={"MediumPriority"})
	public void call_recording_override_allow_agent_only_inbound_calls(){
		System.out.println("Test case --call_recording_override_allow_agent_only_inbound_calls-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //set call recording override setting for only inbound calls
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.switchToTab(driver4, 1);
	    
	    //Verify that recording option is not appearing for User
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyRecordOptionIsInvisible(driver1);
	    
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
	    
	    //update call recordings setting to default values
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    accountCallRecordingTab.swtichToUnitedStateTab(driver4);
	    accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify admin user able to enable/disable agent only recording when call recording not none
	//Verify admin user able to Lock/Unlock  Agent Only Call recording setting
	@Test(groups={"MediumPriority"})
	public void call_recording_override_none_agent_only_recording(){
		System.out.println("Test case --call_recording_override_none_agent_only_recording-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //set call recording override setting for only None calls
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify that single channel Agent only call recording setting is disabled
		accountCallRecordingTab.swtichToAdvancedTab(driver4);
		assertTrue(accountCallRecordingTab.isSingleChannelRecordingDisabled(driver4));
		
		//unlock the record override setting and verifu that single channel Agent only call recording setting is enabled
		accountCallRecordingTab.swtichToOverviewTab(driver4);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToAdvancedTab(driver4);
		assertFalse(accountCallRecordingTab.isSingleChannelRecordingDisabled(driver4));
		
		//set call recording override setting for only All calls and verify that the single channel Agent only call recording setting status can be changed
		accountCallRecordingTab.swtichToOverviewTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToAdvancedTab(driver4);
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    //update call recordings setting to default values
		accountCallRecordingTab.swtichToOverviewTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	  	seleniumBase.switchToTab(driver4, 1);
		
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify Agent Only call recording setting not accessible to support role
	@Test(groups={"MediumPriority"})
	public void call_recording_agent_only_recording_disabled_support_user(){
		System.out.println("Test case --call_recording_agent_only_recording_disabled_support_user-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Login with supprt user and go to call recordings tab
	    loginSupport(driver1);
		accountCallRecordingTab.openCallRecordingTab(driver1);
		
		//verify single channgel Agent only call recording setting is disabled
		accountCallRecordingTab.swtichToAdvancedTab(driver1);
		assertTrue(accountCallRecordingTab.isSingleChannelRecordingDisabled(driver1));
	
		seleniumBase.closeTab(driver1);
	  	seleniumBase.switchToTab(driver1, 1);
		
	  	driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify recording when agent only call recording is off
	@Test(groups={"MediumPriority"})
	public void call_recording_agent_only_recording_off(){
		System.out.println("Test case --call_recording_agent_only_recording_off-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //disable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.switchToTab(driver4, 1);
	    reloadSoftphone(driver1);
	    
	    //verify that call recording is not getting created for outbound call
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// outbound Call from agent
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// verify call recording is active
		callScreenPage.verifyRecordingisActive(driver1);

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
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that call recording is not getting created for inbound call
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
		
		//Verifying that call recording URL is not created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
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
	
	//Verify agent only recording for queue inbound calls when US state granular off
	@Test(groups={"MediumPriority"})
	public void call_recording_queue_inbound_agent_only_recording(){
		System.out.println("Test case --call_recording_queue_inbound_agent_only_recording-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
		String queueName = CONFIG.getProperty("qa_group_2_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_2_number");
	    
	    //set call recording override setting to inbound
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//disable state granular setting for Call Recording from the Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver1);

		//Subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, queueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		//pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		String callSubjectInbound = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

	    //Verifying that call recording URL is created in salesforce for incoming call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubjectInbound);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// verify play icon is there for the inbound call entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubjectInbound));
		
		//enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //enable state granular setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent only recording for queue outbound calls when US state granular off
	@Test(groups={"MediumPriority"})
	public void call_recording_queue_outbound_agent_only_recording(){
		System.out.println("Test case --call_recording_queue_outbound_agent_only_recording-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
		String queueName = CONFIG.getProperty("qa_group_2_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_2_number");
	    
	    //set the call recording override setting to outbound
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);;
		
		 //disable state Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		reloadSoftphone(driver1);

		//Subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, queueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		//pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
		
		// verify call recording is inactive
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubjectOutbound = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying that call recording URL is not created in salesforce for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubjectOutbound);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		// verify play icon is not there for the outbound call entry
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubjectOutbound));

		//enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //enable state Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent only recording for call flow inbound calls when US state granular off
	@Test(groups={"MediumPriority"})
	public void call_recording_call_flow_inbound_agent_only_recording(){
		System.out.println("Test case --call_recording_call_flow_inbound_agent_only_recording-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
	    
	    //Select Call recording override option as inbound
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Inbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //disable Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
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
		String callSubjectInbound = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

	    //Verifying that call recording URL is created in salesforce for inbound call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubjectInbound);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly inbound call entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubjectInbound));
		
		//enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //enable Granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	 	
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent only recording for call flow outbound calls when US state granular off
	@Test(groups={"MediumPriority"})
	public void call_recording_call_flow_outbound_agent_only_recording(){
		System.out.println("Test case --call_recording_call_flow_outbound_agent_only_recording-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_call_flow_no_recording_smart_number");
	    
	    //Set call recording override option to outbound
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //disable granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver1);
		reloadSoftphone(driver4);
		
		// Calling from Agent's SoftPhone to the call flow
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		//pickup the call from the user
	    softPhoneCalling.pickupIncomingCall(driver4);
		
		// verify call recording icon is inactive
		callScreenPage.verifyRecordingisInactive(driver4);
		String callSubjectOutbound = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//Verifying that call recording URL is not created in salesforce for outbound call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubjectOutbound);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
		//verify play icon is not there for the outbound call entry
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubjectOutbound));
		
		//enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //enable granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	 	
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent only recording for call forwarding outbound calls when US state granular off
	@Test(groups = { "MediumPriority"})
	public void call_recording_agent_only_call_with_forwarding() {
		System.out.println("Test case --call_recording_agent_only_call_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		 //Set call recording override type to outbound
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.Outbound);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //enable single channgel Agent only call recording setting
		accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		 //disable granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//switch to softphone
		seleniumBase.switchToTab(driver4, 1);
	    
		//reload the page
		reloadSoftphone(driver1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		softPhoneCalling.isHangUpButtonVisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// verify call recording icon is inactive
		callScreenPage.verifyRecordingisInactive(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Verifying that call recording URL is not created in salesforce for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//enable single channgel Agent only call recording setting
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//enable granular control setting
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
		//reload the page
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly inbound call entry
		assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject));
	    
	    System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
}