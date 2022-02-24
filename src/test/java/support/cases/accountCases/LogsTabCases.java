package support.cases.accountCases;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountBlockedNumbersTab.BlockType;
import support.source.accounts.AccountLogsTab;
import support.source.accounts.AccountLogsTab.BlockNumberValue;
import support.source.accounts.AccountsEmergencyTab;
import support.source.admin.AccountCallRecordingTab;
import support.source.admin.AccountCallRecordingTab.CallRecordingOverrideOptions;
import support.source.commonpages.Dashboard;
import support.source.users.UserIntelligentDialerTab;
import utility.HelperFunctions;

public class LogsTabCases extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	AccountLogsTab logsTab = new AccountLogsTab();
	AccountBlockedNumbersTab blockNoTab = new AccountBlockedNumbersTab();
	AccountsEmergencyTab emergencyTab = new AccountsEmergencyTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	AccountCallRecordingTab accountCallRecording = new AccountCallRecordingTab();
	
	private String qa_user_name;
	private String callFlow;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "logs-export";
	
	@BeforeClass(groups = { "Regression" })
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			qa_user_name = CONFIG.getProperty("qa_admin_user_name");
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			qa_user_name = CONFIG.getProperty("qa_support_user_name");
		}
		callFlow = CONFIG.getProperty("qa_call_flow_emergency_route");
	}
	
	@Test(groups= {"MediumPriority"})
	public void verify_account_logs_tab_for_blocked_numbers() {
		System.out.println("Test case --verify_account_logs_tab_for_blocked_numbers-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to block numbers tab
		String numberToBlock = HelperFunctions.generateTenDigitNumber();
		dashboard.clickAccountsLink(supportDriver);
		blockNoTab.navigateToBlockedNumbersTab(supportDriver);
		blockNoTab.deleteAllBlockedNumber(supportDriver);
		blockNoTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Call, "", "");
		
		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);
		
		// searching and verifying blocked Numbers logs 
		String category = "Blocked Numbers";
		String dateRange = "Today";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		logsTab.verifyBlockedNumbersLogs(supportDriver, category, qa_user_name, numberToBlock, BlockNumberValue.Blocked);
	
		//deleting blocked numbers
		blockNoTab.navigateToBlockedNumbersTab(supportDriver);
		blockNoTab.deleteAllBlockedNumber(supportDriver);

		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		logsTab.verifyBlockedNumbersLogs(supportDriver, category, qa_user_name, numberToBlock, BlockNumberValue.Unblocked);
				
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_account_logs_tab_for_blocked_numbers-- passed");
	}
	
	@Test(groups = { "MediumPriority", "SupportOnly"})
	public void verify_account_logs_tab_for_emergency_call_flow() {
		
		System.out.println("Test case --verify_account_logs_tab_for_emergency_call_flow-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		
		// Opening Emergency Tab
		emergencyTab.navigateToEmergencyTab(supportDriver);
		
		// clean up code
		emergencyTab.cleanUpToggleRoutings(supportDriver);
		emergencyTab.deleteAllRouting(supportDriver);

		//getting current date
		emergencyTab.clickAddIcon(supportDriver);
		emergencyTab.selectCallFlow(supportDriver, callFlow);
		emergencyTab.saveEmergencyRouting(supportDriver);
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		
	
		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);

		// searching and verifying blocked Numbers logs
		String category  = "Emergency Call Flow";
		String dateRange = "Today";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		logsTab.verifyEmergencyCallFlowLogs(supportDriver, category, qa_user_name, createdDate);
		
		// clean up code
		dashboard.clickAccountsLink(supportDriver);
		emergencyTab.navigateToEmergencyTab(supportDriver);
		emergencyTab.cleanUpToggleRoutings(supportDriver);
		emergencyTab.deleteAllRouting(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_account_logs_tab_for_emergency_call_flow-- passed");
	}
	
	//Verify admin/support able to get category type "User Recording Settings" select option in the logs tab
	//Verify admin/support able to get category type "Account Recording Settings" select option in the logs tab
	//Verify admin/support able to get category type "Data Deletion Policy" select option in the logs tab
	//Verify admin/support able to search Account Logs based on Category, Username and time
	//Verify Account Logs details for Recording
	@Test(groups = { "Regression"})
	public void verify_logs_record_with_different_category_and_range_types() {
		System.out.println("Test case --verify_logs_record_with_different_category_and_range_types-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);		
	
		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);

		// searching and verifying blocked Numbers logs
		String category  = "User Recording Settings";
		String dateRange = "Today";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		assertTrue(logsTab.isNoLogsFoundVisible(supportDriver) || logsTab.isLogRecordsVisible(supportDriver));
		
		// searching and verifying blocked Numbers logs
		category = "Account Recording Settings";
		dateRange = "Current Month";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		assertTrue(logsTab.isNoLogsFoundVisible(supportDriver) || logsTab.isLogRecordsVisible(supportDriver));
		
		// searching and verifying blocked Numbers logs
		category = "Data Deletion Policy";
		dateRange = "Current Month";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		assertTrue(logsTab.isNoLogsFoundVisible(supportDriver) || logsTab.isLogRecordsVisible(supportDriver));
		
		// searching and verifying blocked Numbers logs
		category = "Recording";
		dateRange = "Current Month";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		assertTrue(logsTab.isNoLogsFoundVisible(supportDriver) || logsTab.isLogRecordsVisible(supportDriver));

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_logs_record_with_different_category_and_range_types-- passed");
	}
	
	//Verify User Call Recording "Inbound" settings changes displayed/available on Account Logs Tab
	//Verify User Call Recording "Outbound" settings changes displayed/available on Account Logs Tab
	//Verify User Call Recording "None" settings changes displayed/available on Account Logs Tab
	@Test(groups = {"Regression"})
	public void change_call_recording_value_and_verify_on_logs_tab() {
		
		System.out.println("Test case --change_call_recording_value_and_verify_on_logs_tab-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		userIntelligentDialerTab.selectCallRecordingValue(supportDriver, CallRecordingOverrideOptions.All);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);
		userIntelligentDialerTab.verifyRecordingOverrideValue(supportDriver, CallRecordingOverrideOptions.All);
		
		//case 1 set value inbound
		userIntelligentDialerTab.selectCallRecordingValue(supportDriver, CallRecordingOverrideOptions.Inbound);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);
		String date = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		
		// navigating to accounts and searching your account
		dashboard.clickAccountsLink(supportDriver);
		logsTab.navigateToAccountLogTab(supportDriver);
		String category = "User Recording Settings";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, "");
		
		// verifying logs
		logsTab.verifyRecordingLogsPresent(supportDriver, 0, date, category, "User Call Recording Setting changed", qa_user_name, CallRecordingOverrideOptions.All.toString(), CallRecordingOverrideOptions.Inbound.toString(), null);
		
		//download and verify report
		logsTab.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
				
		// case 2 set value outbound
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerTab.selectCallRecordingValue(supportDriver, CallRecordingOverrideOptions.Outbound);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);
		date = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		
		// navigating to accounts and searching your account
		dashboard.clickAccountsLink(supportDriver);
		logsTab.navigateToAccountLogTab(supportDriver);
		category = "User Recording Settings";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, "");
		
		// verifying logs
		logsTab.verifyRecordingLogsPresent(supportDriver, 0, date, category, "User Call Recording Setting changed", qa_user_name, CallRecordingOverrideOptions.Inbound.toString(), CallRecordingOverrideOptions.Outbound.toString(), null);
		
		//download and verify report
		logsTab.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
				
		// case 3 set value none
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerTab.selectCallRecordingValue(supportDriver, CallRecordingOverrideOptions.None);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);
		date = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		
		// navigating to accounts and searching your account
		dashboard.clickAccountsLink(supportDriver);
		logsTab.navigateToAccountLogTab(supportDriver);
		category = "User Recording Settings";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, "");
		
		// verifying logs
		logsTab.verifyRecordingLogsPresent(supportDriver, 0, date, category, "User Call Recording Setting changed", qa_user_name, CallRecordingOverrideOptions.Outbound.toString(), CallRecordingOverrideOptions.None.toString(), null);

		//download and verify report
		logsTab.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		// set default
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerTab.selectCallRecordingValue(supportDriver, CallRecordingOverrideOptions.All);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);	
	
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --change_call_recording_value_and_verify_on_logs_tab-- passed");
	}
	
	//Verify data availability for default applied filters & search by username in the logs tab
	@Test(groups = {"Regression"})
	public void verify_data_with_default_filters_in_the_logs_tab() {
		
		System.out.println("Test case --verify_data_with_default_filters_in_the_logs_tab-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		
		// navigating to accounts and searching your account
		dashboard.clickAccountsLink(supportDriver);
		logsTab.navigateToAccountLogTab(supportDriver);
		
		String dateRange = "Current Week";
//		logsTab.searchAccountLogs(supportDriver, "Sumit", "", dateRange);
		
		//download and verify report
		logsTab.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		
		// Opening Emergency Tab
		emergencyTab.navigateToEmergencyTab(supportDriver);
		
		// clean up code
		emergencyTab.cleanUpToggleRoutings(supportDriver);
		emergencyTab.deleteAllRouting(supportDriver);

		//getting current date
		emergencyTab.clickAddIcon(supportDriver);
		emergencyTab.selectCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		emergencyTab.saveEmergencyRouting(supportDriver);
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);

		// searching and verifying blocked Numbers logs
		String category  = "Emergency Call Flow";
		dateRange = "Today";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		logsTab.verifyEmergencyCallFlowLogs(supportDriver, category, qa_user_name, createdDate);
		
		//download and verify report
		logsTab.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		// clean up code
		dashboard.clickAccountsLink(supportDriver);
		emergencyTab.navigateToEmergencyTab(supportDriver);
		emergencyTab.cleanUpToggleRoutings(supportDriver);
		emergencyTab.deleteAllRouting(supportDriver);
	
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_data_with_default_filters_in_the_logs_tab-- passed");
	}
	
	//Verify Account Logs details for Salesforce Error
	@Test(groups = { "Regression"})
	public void verify_salesforce_error_logs_record_with_last_month_range() {
		System.out.println("Test case --verify_salesforce_error_logs_record_with_last_month_range-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);		
	
		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(supportDriver);

		// searching and verifying Salesforce Error logs
		String category = "Salesforce Error";
		String dateRange = "Last 30 days";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		logsTab.verifySalesforceErrorLogs(supportDriver, category, qa_user_name);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_error_logs_record_with_last_month_range-- passed");
	}
	
	//Verify Account Logs details for Agent-Only Call Recording setting
	@Test(groups={"Regression", "AdminOnly"})
	public void verify_account_logs_details_for_agent_only_call_recording_setting(){
		System.out.println("Test case --verify_account_logs_details_for_agent_only_call_recording_setting-- started ");
  
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		
		accountCallRecording.openCallRecordingTab(supportDriver);
		//disable
		accountCallRecording.disableSingleChannelRecordingsSetting(supportDriver);
		accountCallRecording.saveCallRecordingTabSettings(supportDriver);
		String createdDate1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		//enable
		accountCallRecording.enableSingleChannelRecordingsSetting(supportDriver);
		accountCallRecording.saveCallRecordingTabSettings(supportDriver);
		String createdDate2 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		
		// searching and verifying blocked Numbers logs
		String category = "Account Recording Settings";
		String dateRange = "Today";
		logsTab.searchAccountLogs(supportDriver, qa_user_name, category, dateRange);
		
		String userName	= CONFIG.getProperty("qa_admin_user_name");
		logsTab.verifyRecordingLogsPresent(supportDriver, 1, createdDate1, category, "allow single-channel Agent-only call recordings", userName, "enabled", "disabled", null);
		logsTab.verifyRecordingLogsPresent(supportDriver, 0, createdDate2, category, "allow single-channel Agent-only call recordings", userName, "disabled", "enabled",null);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_account_logs_details_for_agent_only_call_recording_setting-- passed");
	}
	
}
