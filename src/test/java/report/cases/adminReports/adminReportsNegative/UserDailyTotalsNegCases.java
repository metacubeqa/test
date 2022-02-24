//package report.cases.adminReports.adminReportsNegative;
//
//import static org.testng.Assert.assertFalse;
//
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import report.base.ReportBase;
//import report.source.ReportMetabaseCommonPage;
//import report.source.ReportMetabaseCommonPage.HeaderNames;
//import support.source.callRecordings.CallRecordingReportPage;
//import support.source.commonpages.Dashboard;
//import utility.HelperFunctions;
//
//public class UserDailyTotalsNegCases extends ReportBase {
//
//	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
//	Dashboard dashboard = new Dashboard();
//	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
//
//	private String accountName;
//	private String agentNameInvalid;
//	private String teamNameInvalid;
//
//	@BeforeClass(groups = { "Regression" })
//	public void beforeClass() {
//		accountName = CONFIG.getProperty("qa_user_account_cai_report");
//		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
//		teamNameInvalid = CONFIG.getProperty("qa_report_invalid_team_name");
//	}
//
//	@Test(groups = { "Regression", "SupportOnly" })
//	public void verify_user_busy_summary_with_invalid_accountName_filter() {
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_accountName_filter-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//
//		callRecordingReportPage.selectAccount(supportDriver, accountName);
//		callRecordingReportPage.clickRefreshButton(supportDriver);
//
//		//// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver,
//				HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		// verifying Account Name exist or not
//		dashboard.isPaceBarInvisible(supportDriver);
//		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(supportDriver,
//				HelperFunctions.GetRandomString(5)));
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_accountName_filter-- passed ");
//	}
//
//	@Test(groups = { "Regression" })
//	public void verify_user_busy_summary_with_invalid_agent_name_filter() {
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_agent_name_filter-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//
//		if (ReportBase.drivername.equals("supportDriver")) {
//			callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//			callRecordingReportPage.verifyEmptyAccountError(supportDriver);
//
//			callRecordingReportPage.selectAccount(supportDriver, accountName);
//			callRecordingReportPage.clickRefreshButton(supportDriver);
//		}
//
//		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
//			callRecordingReportPage.switchToReportFrame(supportDriver);
//		}
//
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver,
//				HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		// Select account
//		callRecordingReportPage.selectAccount(supportDriver, accountName);
//
//		// verifying Agent Name exist or not
//		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_agent_name_filter-- passed ");
//	}
//
//	@Test(groups = { "Regression" })
//	public void verify_user_busy_summary_with_invalid_team_name_filter() {
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_team_name_filter-- started ");
//
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickOnUserProfile(supportDriver);
//		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
//
//		if (ReportBase.drivername.equals("supportDriver")) {
//			callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
//			callRecordingReportPage.verifyEmptyAccountError(supportDriver);
//
//			callRecordingReportPage.selectAccount(supportDriver, accountName);
//			callRecordingReportPage.clickRefreshButton(supportDriver);
//		}
//
//		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
//			callRecordingReportPage.switchToReportFrame(supportDriver);
//		}
//
//		// open user presence daily total
//		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver,
//				HeaderNames.UserPresenceDetail);
//		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
//		supportDriver.switchTo().defaultContent();
//
//		// Select account
//		callRecordingReportPage.selectAccount(supportDriver, accountName);
//
//		// verifying Team Name exist or not
//		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
//
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_user_busy_summary_with_invalid_team_name_filter-- passed ");
//	}
//
//}
