/**
 * 
 */
package softphone.cases.softPhoneFunctional.callRecording;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;
import utility.HelperFunctions;

/**
 * @author admin
 *
 */
public class CallRecordingChangeLogs extends SoftphoneBase{
	
	static String dateTimeFormat = "MM/dd/yyyy hh:mm a";
	
	//Verify Call rec->Advanced setting changes displayed Under changelog sub tab
	//Verify Account Call Recording Advanced settings changes displayed/available on Account Logs Tab
	@Test(groups = { "MediumPriority"})
	public void verify_change_logs_recording_advanced_tab(){
		System.out.println("Test case --verify_change_logs_recording_advanced_tab-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Set the recoding setting on advanced tab to default
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToAdvancedTab(driver4);
		accountCallRecordingTab.disableCallRecordingPauseSetting(driver4);
		accountCallRecordingTab.enableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.disableDisplayRecordingStatusSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//change the recording setting as per the test
		String logChangeTime 	= HelperFunctions.GetCurrentDateTime(dateTimeFormat);
		String userName			= CONFIG.getProperty("qa_user_2_name");
		accountCallRecordingTab.enableCallRecordingPauseSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.enableDisplayRecordingStatusSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify the change logs
		accountLogsTab.navigateToAccountLogTab(driver4);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 0, logChangeTime, "Account Recording Settings", "display recording status on intelligent dialer", userName, "disabled", "enabled", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 1, logChangeTime, "Account Recording Settings", "restrict recording download", userName, "enabled", "disabled", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 2, logChangeTime, "Account Recording Settings", "start/stop recording", userName, "disabled", "enabled", null);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}

	//	Verify Call rec->Overview settings changes displayed under changelog
	//Verify Account Call Recording Overview settings changes displayed/available on Account Logs Tab
	@Test(groups = { "MediumPriority"})
	public void verify_change_logs_recording_overview_tab(){
		System.out.println("Test case --verify_change_logs_recording_overview_tab-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Set the recoding setting on overview tab to default
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToOverviewTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		seleniumBase.idleWait(1);
		accountCallRecordingTab.disableInboundCallRecordingAnnoncement(driver4);
		seleniumBase.idleWait(1);
		accountCallRecordingTab.enableOutboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//change the recording setting as per the test
		String logChangeTime 	= HelperFunctions.GetCurrentDateTime(dateTimeFormat);
		String userName			= CONFIG.getProperty("qa_user_2_name");
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.enableInboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableOutboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify the change logs
		accountLogsTab.navigateToAccountLogTab(driver4);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 0, logChangeTime, "Account Recording Settings", "outbound call recording announcement", userName, "enabled", "disabled", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 1, logChangeTime, "Account Recording Settings", "inbound call recording announcement", userName, "disabled", "enabled", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 2, logChangeTime, "Account Recording Settings", "changed call recording", userName, "None", "All", null);
		
		//change the setting back to default status
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToOverviewTab(driver4);
		accountCallRecordingTab.enableCallRecordingSetting(driver4);
		accountCallRecordingTab.enableInboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.enableOutboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}
	
	//verify Call rec->United states setting changes displayed under changelog sub tab
	//Verify Account Call Recording Unites State settings changes displayed/available on Account Logs Tab
	@Test(groups = { "MediumPriority"})
	public void verify_change_logs_recording_united_state_tab(){
		System.out.println("Test case --verify_change_logs_recording_united_state_tab-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Set the recoding setting on united states tab to default
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "Alabama");
		accountCallRecordingTab.checkAreasForPlayOutboundRecording(driver4, "Alabama");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//change the recording setting as per the test
		String logChangeTime 	= HelperFunctions.GetCurrentDateTime(dateTimeFormat);
		String userName			= CONFIG.getProperty("qa_user_2_name");
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Alabama");
		accountCallRecordingTab.uncheckAreasForPlayOutboundRecording(driver4, "Alabama");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify the change logs
		accountLogsTab.navigateToAccountLogTab(driver4);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 0, logChangeTime, "Account Recording Settings (State)", "Alabama", userName, "disabled call recording, enabled outbound announcement", "enabled call recording, disabled outbound announcement", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 1, logChangeTime, "Account Recording Settings", "allow granular control by US State", userName, "disabled", "enabled", null);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}
	
	//verify Call rec->International setting changes displayed under changelog sub tab
	//Verify Account Call Recording International settings changes displayed/available on Account Logs Tab
	@Test(groups = { "MediumPriority"})
	public void verify_change_logs_recording_international_tab(){
		System.out.println("Test case --verify_change_logs_recording_international_tab-- started");
		
		// updating the driver4 used
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Set the recoding setting on International tab to default
	    loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToInternationalTab(driver4);
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "North America");
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.checkAreasForPlayOutboundRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//change the recording setting as per the test
		String logChangeTime 	= HelperFunctions.GetCurrentDateTime(dateTimeFormat);
		String userName			= CONFIG.getProperty("qa_user_2_name");
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "North America");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.uncheckAreasForPlayOutboundRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//verify the change logs
		accountLogsTab.navigateToAccountLogTab(driver4);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 0, logChangeTime, "Account Recording Settings (Country)", "Antigua and Barbuda", userName, "disabled call recording, enabled outbound announcement", "enabled call recording, disabled outbound announcement", null);
		accountLogsTab.verifyRecordingLogsPresent(driver4, 1, logChangeTime, "Account Recording Settings", "allow granular control by Country", userName, "disabled", "enabled", null);
		
		//Change the setting back to default
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToInternationalTab(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "North America");
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.uncheckAreasForPlayOutboundRecording(driver4, "Antigua and Barbuda");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
				
		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// updating the driver4 used
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
	}
}
