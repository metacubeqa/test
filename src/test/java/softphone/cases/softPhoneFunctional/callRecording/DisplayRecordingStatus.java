/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class DisplayRecordingStatus extends SoftphoneBase{

	//Display Recording Status on Intelligent Dialer- OFF
	//Display Recording Status on Intelligent Dialer- ON
	@Test(groups = { "MediumPriority", "Regression"})
	public void verify_dispaly_recording_status_intelligent_dialer(){
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    //disabling call recording status setting
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.disableDisplayRecordingStatusSetting(driver4);;
		accountCallRecordingTab.disableCallRecordingPauseSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//Make an outbound call from the agent
		dashboard.switchToTab(driver4, 1);
		dashboard.reloadSoftphone(driver4);
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		//picking up call on web support driver
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verify that green icon appearing for recording
		callScreenPage.verifyRecordingisInactive(driver4);
		
		//Hangup active call
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		//enable display call recording setting
		dashboard.switchToTab(driver4, dashboard.getTabCount(driver4));
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableDisplayRecordingStatusSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//Make an outbound call from the agent
		dashboard.switchToTab(driver4, 1);
		dashboard.reloadSoftphone(driver4);
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		//picking up call on web support driver
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verify that recording is active
		callScreenPage.verifyRecordingisActive(driver4);
		
		//Hangup active call
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		//enable display call recording setting
		dashboard.switchToTab(driver4, dashboard.getTabCount(driver4));
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableDisplayRecordingStatusSetting(driver4);
		accountCallRecordingTab.enableCallRecordingPauseSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}
	
	//Verify Display Recording Status on Intelligent Dialer- Off with Pause /Play ?stop recording set as Yes
	//eVerify Display Recording Status on Intelligent Dialer- ON with Pause /Play ?stop recording set as Yes
	@Test(groups = { "MediumPriority"})
	public void verify_dispaly_recording_status_pause_recording(){
		System.out.println("Test case --verify_dispaly_recording_status_pause_recording-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    //disabling call recording status and enable call recording pause setting
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.disableDisplayRecordingStatusSetting(driver4);
		accountCallRecordingTab.enableCallRecordingPauseSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//Make an outbound call from the agent
		dashboard.switchToTab(driver4, 1);
		dashboard.reloadSoftphone(driver4);
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		//picking up call on web support driver
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verify that only pause recording icon visible
		callScreenPage.verifyOnlyPauseRecordingBtnVisible(driver4);
		
		//Hangup active call
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		//enable display call recording setting
		dashboard.switchToTab(driver4, dashboard.getTabCount(driver4));
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableDisplayRecordingStatusSetting(driver4);;
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//Make an outbound call from the agent
		dashboard.switchToTab(driver4, 1);
		dashboard.reloadSoftphone(driver4);
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		//picking up call on web support driver
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verify that only pause recording icon visible
		callScreenPage.verifyOnlyPauseRecordingBtnVisible(driver4);
		
		//Hangup active call
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}
}