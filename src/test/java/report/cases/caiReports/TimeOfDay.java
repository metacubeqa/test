package report.cases.caiReports;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.caiReportPage.CAIReportPage;
import report.source.caiReportPage.CAIReportPage.CreatedDateFilter;
import report.source.caiReportPage.CAIReportPage.FilterType;
import report.source.caiReportPage.CAIReportPage.ReportsType;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import utility.HelperFunctions;

public class TimeOfDay extends ReportBase{

	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CAIReportPage caiReportPage = new CAIReportPage();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	
	static String dateFormat = "MMMMM dd, yyyy";
	
	private String accountName;
	private String accountNameOther;
	private String userNameValid;
	private String userNameInValid;
	private String locationNameValid;
	private String locationNameInValid;
	private String teamNameValid;
	private String teamNameInValid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		
		userNameValid = CONFIG.getProperty("qa_cai_report_valid_user_name");
		userNameInValid = CONFIG.getProperty("qa_report_invalid_user_name");

		locationNameValid = CONFIG.getProperty("qa_cai_report_valid_location_name");
		locationNameInValid = CONFIG.getProperty("qa_report_invalid_location_name");
		
		teamNameValid = CONFIG.getProperty("qa_cai_report_valid_team_name");
		teamNameInValid = CONFIG.getProperty("qa_report_invalid_team_name");
		
	}
	
	// Verify default filter on Time of Day Report.
	@Test(groups = { "Regression" })
	public void verify_default_filer_for_time_of_day_reports() {
		System.out.println("Test case --verify_default_filer_for_time_of_day_reports-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);

		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);

		// verifying for monthly filter by mouse hover
		supportDriver.switchTo().defaultContent();
		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Month);
		caiReportPage.clickRefreshButton(supportDriver);

		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);

//		verifying for quarterly filter
		supportDriver.switchTo().defaultContent();
		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Last90days);
		caiReportPage.clickRefreshButton(supportDriver);

		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);
		  
		// verifying for annual filter
		supportDriver.switchTo().defaultContent();
		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		caiReportPage.clickRefreshButton(supportDriver);

		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_filer_for_time_of_day_reports-- passed ");
	}
	
	//Verify Username filter for Time of Day Report.
	@Test(groups = { "Regression" })
	public void verify_username_filter_for_time_of_day() {
		System.out.println("Test case --verify_username_filter_for_time_of_day-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		callRecordingReportPage.selectUser(supportDriver, userNameValid);
		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for username
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_username_filter_for_time_of_day-- passed ");
	}
	
	//Verify Teamname filter for Time of Day Report.
	@Test(groups = { "Regression" })
	public void verify_teamname_filter_for_time_of_day() {
		System.out.println("Test case --verify_teamname_filter_for_time_of_day-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		reportCommonPage.clearTeamNameFilter(supportDriver);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_teamname_filter_for_time_of_day-- passed ");
	}
	
	//Verify Location Filter for Time of Day Report.
	@Test(groups = { "Regression" })
	public void verify_location_filter_for_time_of_day() {
		System.out.println("Test case --verify_location_filter_for_time_of_day-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectLocation(supportDriver, locationNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded 
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTimeOfDayData(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_location_filter_for_time_of_day-- passed ");
	}
	
//	Verify Call Context filter on Time of Day Report.
	@Test(groups = { "Regression"})
	public void verify_call_context_filter_for_time_of_day(){
		System.out.println("Test case --verify_call_context_filter_for_time_of_day-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);
		
		// verifying data different for call context
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.CallContext, ReportsType.TimeOfDay);
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_context_filter_for_time_of_day-- passed ");
	}
	
//	Verify Management Level filter on Time of Day Report.
	@Test(groups = { "Regression"})
	public void verify_management_filter_for_time_of_day(){
		System.out.println("Test case --verify_management_filter_for_time_of_day-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);
		
		// verifying data different for Management level
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.ManagementLevel, ReportsType.TimeOfDay);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_management_filter_for_time_of_day-- passed ");
	}
	
	//Verify access filter for Time Of Day Report with support User role.
	@Test(groups = { "Regression"})
	public void verify_access_filter_with_support_role_for_time_of_day() {
		System.out.println("Test case --verify_access_filter_with_support_role_for_time_of_day-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTimeOfDay(webSupportDriver);

		caiReportPage.clickRefreshButton(webSupportDriver);
		caiReportPage.verifyEmptyAccountError(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountNameOther);
		caiReportPage.selectCreatedDateFilter(webSupportDriver, CreatedDateFilter.Annual);
		caiReportPage.clickRefreshButton(webSupportDriver);
		caiReportPage.switchToReportFrame(webSupportDriver);
		caiReportPage.verifyTimeOfDayData(webSupportDriver);

		//user
		webSupportDriver.switchTo().defaultContent();
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameValid));
		assertTrue(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameInValid));
		
		//team
		callRecordingReportPage.clearAgentFilter(webSupportDriver);
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameValid));
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameInValid));
		
		//location
		reportCommonPage.clearTeamNameFilter(webSupportDriver);
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameValid));
		assertTrue(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_access_filter_with_support_role_for_time_of_day-- passed ");
	}
	
	//Verify Time of Day Report with Account Name which is not exists
	@Test(groups = { "Regression"})
	public void verify_invalid_account_name_not_exists_for_time_of_day() {
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_time_of_day-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTimeOfDay(webSupportDriver);

		// verifying invalid Account Name not exits
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(webSupportDriver,HelperFunctions.GetRandomString(5)));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_time_of_day-- passed ");
	}
	
	//Verify Time of Day Report with User Name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_username_not_exists_for_selected_account_time_of_day() {
		System.out.println("Test case --verify_username_not_exists_for_selected_account_time_of_day-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTimeOfDay(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Agent Name not exits
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_username_not_exists_for_selected_account_time_of_day-- passed ");
	}
	
	//Verify Time of Day Report with Location name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_location_not_exists_for_selected_account_time_of_day() {
		System.out.println("Test case --verify_location_not_exists_for_selected_account_time_of_day-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTimeOfDay(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Location Name not exits
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameInValid));

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_location_not_exists_for_selected_account_time_of_day-- passed ");
	}
	
	//Verify Time of Day Report with Team name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_teamname_not_exists_for_selected_account_time_of_day() {
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_time_of_day-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTimeOfDay(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_time_of_day-- passed ");
	}
}
