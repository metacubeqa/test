package report.cases.caiReports;

import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

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

public class CoachingGiven extends ReportBase{

	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CAIReportPage caiReportPage = new CAIReportPage();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();

	List<String> hoverCheckList = new ArrayList<String>();
	
	static String dateFormat = "MMMMM dd, yyyy";
	
	private String accountName;
//	private String accountNameOther;
	private String userNameValid;
	private String userNameInValid;
	private String locationNameValid;
	private String locationNameInValid;
	private String teamNameValid;
	private String teamNameInValid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
//		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		
		userNameValid = CONFIG.getProperty("qa_cai_report_valid_user_name");
		userNameInValid = CONFIG.getProperty("qa_report_invalid_user_name");

		locationNameValid = CONFIG.getProperty("qa_cai_report_valid_location_name");
		locationNameInValid = CONFIG.getProperty("qa_report_invalid_location_name");
		
		teamNameValid = CONFIG.getProperty("qa_cai_report_valid_team_name");
		teamNameInValid = CONFIG.getProperty("qa_report_invalid_team_name");
		
		hoverCheckList.clear();
		hoverCheckList.add("reviewer name");
		hoverCheckList.add("metric");
		hoverCheckList.add("Sum");
	}
	
	//Verify Username filter for Coaching Given Report.
	@Test(groups = { "Regression" })
	public void verify_username_filter_for_coaching_given() {
		System.out.println("Test case --verify_username_filter_for_coaching_given-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		callRecordingReportPage.selectUser(supportDriver, userNameValid);
		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for username
		caiReportPage.switchToReportFrame(supportDriver);
		if(caiReportPage.isElementVisible(supportDriver, caiReportPage.noResultsSectionList, 5)){
			supportDriver.switchTo().defaultContent();
			callRecordingReportPage.selectUser(supportDriver, CONFIG.getProperty("qa_cai_report_valid_user_name2"));
			caiReportPage.clickRefreshButton(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
		}
		
		caiReportPage.verifyCoachingGivenData(supportDriver);
		caiReportPage.verifyMultipleBarsData(supportDriver, ReportsType.CoachingGiven, caiReportPage.getUserNameAgentsListToVerify(), hoverCheckList);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_username_filter_for_coaching_given-- passed ");
	}
	
	//Verify Teamname filter for Coaching Given Report.
	@Test(groups = { "Regression" })
	public void verify_teamname_filter_for_coaching_given() {
		System.out.println("Test case --verify_teamname_filter_for_coaching_given-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		reportCommonPage.clearTeamNameFilter(supportDriver);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for Coaching Given location
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyCoachingGivenData(supportDriver);
		caiReportPage.verifyMultipleBarsData(supportDriver, ReportsType.CoachingGiven, caiReportPage.getTeamAgentsListToVerify(), hoverCheckList);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_teamname_filter_for_coaching_given-- passed ");
	}
	
	//Verify Location Filter for Coaching Given Report.
	@Test(groups = { "Regression" })
	public void verify_location_filter_for_coaching_given() {
		System.out.println("Test case --verify_location_filter_for_coaching_given-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectLocation(supportDriver, locationNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for Coaching Given location
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyCoachingGivenData(supportDriver);
		caiReportPage.verifyMultipleBarsData(supportDriver, ReportsType.CoachingGiven, caiReportPage.getLocationAgentsListToVerify(), hoverCheckList);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_location_filter_for_coaching_given-- passed ");
	}
	
	// Verify Call Context filter for Coaching Given Report.
	@Test(groups = { "Regression" })
	public void verify_call_context_filter_for_coaching_given() {
		System.out.println("Test case --verify_call_context_filter_for_coaching_given-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);

		// verifying data different for call context
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.CallContext, ReportsType.CoachingGiven);
		supportDriver.switchTo().defaultContent();
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_context_filter_for_coaching_given-- passed ");
	}

	// Verify Management Level Filter for Coaching Given Report.
	@Test(groups = { "Regression" })
	public void verify_management_filter_for_coaching_given() {
		System.out.println("Test case --verify_management_filter_for_coaching_given-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);
		
		// verifying data different for Management level
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.ManagementLevel, ReportsType.CoachingGiven);
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_management_filter_for_coaching_given-- passed ");
	}
	
	//Verify CoachingGiven Report with Account Name which is not exists
	@Test(groups = { "Regression"})
	public void verify_invalid_account_name_not_exists_for_coaching_given() {
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_coaching_given-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToCoachingGiven(webSupportDriver);

		// verifying invalid Account Name not exits
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(webSupportDriver, HelperFunctions.GetRandomString(5)));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_coaching_given-- passed ");
	}
	
	//Verify CoachingGiven Report with User Name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_username_not_exists_for_selected_account_coaching_given() {
		System.out.println("Test case --verify_username_not_exists_for_selected_account_coaching_given-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToCoachingGiven(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Agent Name not exits
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_username_not_exists_for_selected_account_coaching_given-- passed ");
	}
	
	//Verify CoachingGiven Report with Location name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_location_not_exists_for_selected_account_coaching_given() {
		System.out.println("Test case --verify_location_not_exists_for_selected_account_coaching_given-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToCoachingGiven(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Location Name not exits
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameInValid));

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_location_not_exists_for_selected_account_coaching_given-- passed ");
	}
	
	//Verify CoachingGiven Report with Team name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_teamname_not_exists_for_selected_account_coaching_given() {
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_coaching_given-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToCoachingGiven(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_coaching_given-- passed ");
	}
}
