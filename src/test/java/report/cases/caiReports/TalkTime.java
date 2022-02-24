package report.cases.caiReports;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.caiReportPage.CAIReportPage;
import report.source.caiReportPage.CAIReportPage.CreatedDateFilter;
import report.source.caiReportPage.CAIReportPage.FilterType;
import report.source.caiReportPage.CAIReportPage.ReportsType;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import utility.HelperFunctions;

public class TalkTime extends ReportBase{

	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CAIReportPage caiReportPage = new CAIReportPage();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();

	List<String> hoverCheckList = new ArrayList<String>();
	
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
		
		hoverCheckList.clear();
		hoverCheckList.add("displayname");
		hoverCheckList.add("Hours");
	}
	
	
//	Verify default filter on Talk Time report. 
	@Test(groups = { "Regression"})
	public void verify_default_filer_for_talk_time(){
		System.out.println("Test case --verify_default_filer_for_talk_time-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);
		
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeData(supportDriver);
		
		//verifying for default filter 
		Pair<String, String> startEndDatePair = caiReportPage.getStartEndDateMouseHoverCircle(supportDriver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();
		
		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Week, dateFormat, dateStringList);
	
		List<String> xAxisDateList = caiReportPage.getDateListXAxis(supportDriver);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Week, dateFormat, xAxisDateList);

		//verifying for monthly filter
		supportDriver.switchTo().defaultContent();
		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Month);
		caiReportPage.clickRefreshButton(supportDriver);
		
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeData(supportDriver);
		
		startEndDatePair = caiReportPage.getStartEndDateMouseHoverCircle(supportDriver);
		startDate = startEndDatePair.first();
		endDate = startEndDatePair.second();
		
		dateStringList.clear();
		dateStringList.add(startDate);
		dateStringList.add(endDate);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Month, dateFormat, dateStringList);

		xAxisDateList.clear();
		xAxisDateList = caiReportPage.getDateListXAxis(supportDriver);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Month, dateFormat, xAxisDateList);

		//verifying for annual filter
		supportDriver.switchTo().defaultContent();
		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		caiReportPage.clickRefreshButton(supportDriver);
		
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeData(supportDriver);
		
		startEndDatePair = caiReportPage.getStartEndDateMouseHoverCircle(supportDriver);
		startDate = startEndDatePair.first();
		endDate = startEndDatePair.second();
		
		startDate = HelperFunctions.changeDateTimeFormat(startDate, "MMMMM, yyyy", dateFormat);
		endDate	  = HelperFunctions.changeDateTimeFormat(endDate, "MMMMM, yyyy", dateFormat);
		int diff  = HelperFunctions.getNumberOfDiffInDates(dateFormat, startDate, endDate);

		assertTrue(HelperFunctions.verifyIntegerInGivenRange(363, 367, diff));
		
		dateStringList.clear();
		dateStringList.add(startDate);
		dateStringList.add(endDate);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Annual, dateFormat, dateStringList);

		xAxisDateList.clear();
		xAxisDateList = caiReportPage.getDateListXAxis(supportDriver);
		
		caiReportPage.sortAndVerifyDateCreated(reportDuration.Annual, dateFormat, xAxisDateList);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_filer_for_talk_time-- passed ");
	}
	
//	Verify Call Context filter on Talk Time Report. 
	@Test(groups = { "Regression"})
	public void verify_call_context_filter_for_talk_time(){
		System.out.println("Test case --verify_call_context_filter_for_talk_time-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);
		
		// verifying data different for call context
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.CallContext, ReportsType.TalkTime);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_context_filter_for_talk_time-- passed ");
	}
	
//	Verify Management Level filter on Talk Time Report. 2915107
	@Test(groups = { "Regression"})
	public void verify_management_filter_for_talk_time(){
		System.out.println("Test case --verify_management_filter_for_talk_time-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);
		
		// verifying data different for Management level
		caiReportPage.verifyReportImageDifferentAfterFilter(supportDriver, FilterType.ManagementLevel, ReportsType.TalkTime);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_management_filter_for_talk_time-- passed ");
	}
	
	//Verify Username filter for Talk Time Report.
	@Test(groups = { "Regression" })
	public void verify_username_filter_for_talk_time() {
		System.out.println("Test case --verify_username_filter_for_talk_time-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		callRecordingReportPage.selectUser(supportDriver, userNameValid);
		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for username
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeAgentLevelFilter(supportDriver, reportDuration.Annual);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_username_filter_for_talk_time-- passed ");
	}
	
	//Verify Teamname filter for Talk Time Report.
	@Test(groups = { "Regression" })
	public void verify_teamname_filter_for_talk_time() {
		System.out.println("Test case --verify_teamname_filter_for_talk_time-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		reportCommonPage.clearTeamNameFilter(supportDriver);
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for Talk Time location
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeDataTeamLevelFilter(supportDriver);
		caiReportPage.verifySingularBarsData(supportDriver, ReportsType.TalkTime, caiReportPage.getTeamAgentsListToVerify(), hoverCheckList);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_teamname_filter_for_talk_time-- passed ");
	}
	
	//Verify Location Filter for Talk Time Report.
	@Test(groups = { "Regression" })
	public void verify_location_filter_for_talk_time() {
		System.out.println("Test case --verify_location_filter_for_talk_time-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickConversationAI(supportDriver);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);

		caiReportPage.selectCreatedDateFilter(supportDriver, CreatedDateFilter.Annual);
		reportCommonPage.selectLocation(supportDriver, locationNameValid);

		caiReportPage.clickRefreshButton(supportDriver);
	
		// verifying data is loaded for Talk Time location
		caiReportPage.switchToReportFrame(supportDriver);
		caiReportPage.verifyTalkTimeDataTeamLevelFilter(supportDriver);
		caiReportPage.verifySingularBarsData(supportDriver, ReportsType.TalkTime, caiReportPage.getLocationAgentsListToVerify(), hoverCheckList);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_location_filter_for_talk_time-- passed ");
	}
	
	//Verify access filter for Talk Time Report with support User role.
	@Test(groups = { "Regression"})
	public void verify_access_filter_with_support_role_for_talk_time() {
		System.out.println("Test case --verify_access_filter_with_support_role_for_talk_time-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTalkTime(webSupportDriver);

		caiReportPage.clickRefreshButton(webSupportDriver);
		caiReportPage.verifyEmptyAccountError(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountNameOther);
		caiReportPage.selectCreatedDateFilter(webSupportDriver, CreatedDateFilter.Annual);
		caiReportPage.clickRefreshButton(webSupportDriver);
		caiReportPage.switchToReportFrame(webSupportDriver);
		caiReportPage.verifyTalkTimeData(webSupportDriver);

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
		System.out.println("Test case --verify_access_filter_with_support_role_for_talk_time-- passed ");
	}
	
	//Verify Talk Time Report with Account Name which is not exists
	@Test(groups = { "Regression"})
	public void verify_invalid_account_name_not_exists_for_talk_time() {
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_talk_time-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTalkTime(webSupportDriver);

		// verifying invalid Account Name not exits
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(webSupportDriver,HelperFunctions.GetRandomString(5)));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_talk_time-- passed ");
	}
	
	//Verify Talk Time Report with User Name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_username_not_exists_for_selected_account_talk_time() {
		System.out.println("Test case --verify_username_not_exists_for_selected_account_talk_time-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTalkTime(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Agent Name not exits
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_username_not_exists_for_selected_account_talk_time-- passed ");
	}
	
	//Verify Talk Time Report with Location name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_location_not_exists_for_selected_account_talk_time() {
		System.out.println("Test case --verify_location_not_exists_for_selected_account_talk_time-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTalkTime(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Location Name not exits
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameInValid));

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_location_not_exists_for_selected_account_talk_time-- passed ");
	}
	
	//Verify Talk Time Report with Team name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_teamname_not_exists_for_selected_account_talk_time() {
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_talk_time-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToTalkTime(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_talk_time-- passed ");
	}

}
