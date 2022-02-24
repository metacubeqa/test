//package report.cases.adminReports;
//
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertFalse;
//import static org.testng.Assert.assertTrue;
//
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//import org.testng.util.Strings;
//
//import report.base.ReportBase;
//import report.source.ReportMetabaseCommonPage;
//import report.source.ReportMetabaseCommonPage.HeaderNames;
//import softphone.source.salesforce.TaskDetailPage;
//import support.source.callRecordings.CallRecordingReportPage;
//import support.source.commonpages.Dashboard;
//import utility.HelperFunctions;
//
//public class UserPresenceDailyTotals extends ReportBase{
//	Dashboard dashboard = new Dashboard();
//	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
//	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
//	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
//	
//	private String accountName;
//	private String accountNameOther;
//	private String agentNameValid;
//	private String agentNameInvalid;
//	private String teamNameValid;
//	private String teamNameInvalid;
//	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
//	private static final String reportFileNameDownload = "query_result";
//	private static final String inputFormat = "MM/dd/yyyy";
//	private static final String outputFormat = "MM/dd/yyyy hh:mm:ss a";
//	
//	String curentDate = HelperFunctions.GetCurrentDateTime("d");
//	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
//	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
//	
//	@BeforeClass(groups = { "Regression" })
//	public void beforeClass() {
//		accountName 	 = CONFIG.getProperty("qa_user_account_cai_report");
//		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
//		agentNameValid   = CONFIG.getProperty("qa_report_valid_user_name");
//		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
//		teamNameValid = CONFIG.getProperty("qa_report_valid_team_name");
//		teamNameInvalid = CONFIG.getProperty("qa_report_invalid_team_name");
//	}
//	
//	@Test(groups = { "Regression", "SupportOnly" })
//	public void verify_support_user_able_to_access_user_presence_daily_totals() {
//		System.out.println("Test case --verify_support_user_able_to_access_user_presence_daily_totals-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//
//		// verify empty account error
//		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//		callRecordingPage.verifyEmptyAccountError(supportDriver);
//		callRecordingPage.selectAccount(supportDriver, accountName);
//		callRecordingPage.clickRefreshButton(supportDriver);
//		
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//		
//		//selecting different account
//		callRecordingPage.selectAccount(supportDriver, accountNameOther);
//		
////		//verifying for team
////		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));
////		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
////		
////		reportCommonPage.selectTeamName(supportDriver, teamNameInvalid);
//		callRecordingPage.clickRefreshButton(supportDriver);
//		
////		int teamIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
////		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameInvalid);
////		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
////		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameInvalid);
////		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
////		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameInvalid);
////		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
//		
//		//verifying for agent
//		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
//		agentNameInvalid = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentNameIndex);
//		
//		supportDriver.switchTo().defaultContent();
//		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
//		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameValid));
//		
//		callRecordingPage.selectUser(supportDriver, agentNameInvalid);
//		callRecordingPage.clickRefreshButton(supportDriver);
//		
//		//verifying data for agent
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameInvalid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameInvalid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameInvalid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//
//		//selecting other account and verifying user name in dropdown
//		supportDriver.switchTo().defaultContent();
//		assertFalse(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
//		callRecordingPage.selectAccount(supportDriver, accountName);
//		assertTrue(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
//				
//		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameValid));
//		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
//
//		callRecordingPage.selectUser(supportDriver, agentNameValid);
//		
//		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -1, 0);
//		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
//		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
//
//		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -1, 0);
//		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
//
//		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
//		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
//
//		callRecordingPage.clickRefreshButton(supportDriver);
//
//		int dateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
//		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, dateIndex, startDateInputValue, endDateInputValue);
//
//		// verifying data for agent
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		callRecordingPage.clickAgentToSort(supportDriver);
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_support_user_able_to_access_user_presence_daily_totals-- passed ");
//	}
//
//	@Test(groups = { "Regression"})
//	public void verify_username_filter_user_presence_totals() {
//		System.out.println("Test case --verify_username_filter_user_presence_totals-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//		
//		if (ReportBase.drivername.equals("supportDriver")) {
//			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//			callRecordingPage.verifyEmptyAccountError(supportDriver);
//
//			callRecordingPage.selectAccount(supportDriver, accountName);
//			callRecordingPage.clickRefreshButton(supportDriver);
//		}
//		
//		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
//			callRecordingPage.switchToReportFrame(supportDriver);
//		}
//		
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		callRecordingPage.selectAccount(supportDriver, accountName);
//		callRecordingPage.selectUser(supportDriver, agentNameValid);
//		callRecordingPage.clickRefreshButton(supportDriver);
//		
//		//verifying data for agent
//		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
//		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_username_filter_user_presence_totals-- passed ");
//	}
//
//	@Test(groups = { "Regression"})
//	public void verify_team_filter_user_presence_totals() {
//		System.out.println("Test case --verify_team_filter_user_presence_totals-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//		
//		if (ReportBase.drivername.equals("supportDriver")) {
//			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//			callRecordingPage.verifyEmptyAccountError(supportDriver);
//
//			callRecordingPage.selectAccount(supportDriver, accountName);
//			callRecordingPage.clickRefreshButton(supportDriver);
//		}
//		
//		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
//			callRecordingPage.switchToReportFrame(supportDriver);
//		}
//		
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		callRecordingPage.selectAccount(supportDriver, accountName);
//		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
//		callRecordingPage.clickRefreshButton(supportDriver);
//		
//		int teamIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
//		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameValid);
//		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
//		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameValid);
//		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
//		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex), teamNameValid);
//		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_team_filter_user_presence_totals-- passed ");
//	}
//	
//	
//	@Test(groups = { "Regression"})
//	public void verify_user_able_to_download_user_presence_totals_report() {
//		System.out.println("Test case --verify_user_able_to_download_user_presence_totals_report-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//		
//		if (ReportBase.drivername.equals("supportDriver")) {
//			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//			callRecordingPage.verifyEmptyAccountError(supportDriver);
//
//			callRecordingPage.selectAccount(supportDriver, accountName);
//			callRecordingPage.clickRefreshButton(supportDriver);
//		}
//		
//		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
//			callRecordingPage.switchToReportFrame(supportDriver);
//		}
//		
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		callRecordingPage.selectAccount(supportDriver, accountName);
//		
//		// verifying date for 1 year
//		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
//		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
//		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
//
//		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
//		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
//
//		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
//		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
//
//		callRecordingPage.clickRefreshButton(supportDriver);
//
//		int dateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
//		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, dateIndex, startDateInputValue, endDateInputValue);
//		
//		// verifying date for 90 days
//		supportDriver.switchTo().defaultContent();
//		startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -3, 0);
//		startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
//
//		endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
//		endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
//
//		startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
//		endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
//
//		callRecordingPage.clickRefreshButton(supportDriver);
//
//		dateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
//		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, dateIndex, startDateInputValue, endDateInputValue);
//		
//		// verifying date for 7 days
//		supportDriver.switchTo().defaultContent();
//		startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, -7, 0, 0);
//		startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
//		startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
//
//		endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
//		endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
//		endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
//		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
//
//		startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
//		endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
//
//		callRecordingPage.clickRefreshButton(supportDriver);
//
//		dateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
//		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, dateIndex, startDateInputValue, endDateInputValue);
//		
//		//download file in xls format
//		callRecordingPage.scrollTillEndOfPage(supportDriver);
//		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
//		
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_user_able_to_download_user_presence_totals_report-- passed ");
//	}
//		
//
//}
