package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.SelectDate;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class SupervisorDashNegCases extends ReportBase {

	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();

	private String accountName;
	private String teamNameInvalid;
	private String locationNameInvalid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		teamNameInvalid = CONFIG.getProperty("qa_report_invalid_team_name");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_support_user_able_to_access_supervisor_dash_with_invalid_accountName() {
		System.out.println("Test case --verify_support_user_able_to_access_supervisor_dash_with_invalid_accountName-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		// verifying Account Name exist or not
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_user_able_to_access_supervisor_dash_with_invalid_accountName-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_able_to_access_supervisor_dash_with_invalid_agent_name_filter() {
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_agent_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying Agent Name exist or not
		assertFalse(reportCommonPage.verifySupervisorNameExistsInDropDown(supportDriver, HelperFunctions.GetRandomString(5)));
		

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_agent_name_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_able_to_access_supervisor_dash_with_invalid_team_name() {
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_team_name-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_team_name-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_able_to_access_supervisor_dash_with_invalid_location() {
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_location-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_with_invalid_location-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_able_to_access_supervisor_dash_by_wrong_date_filter() {
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_by_wrong_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// select start date bigger than end date
		reportCommonPage.selectStartDateBiggerEndDate(supportDriver);
		callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);

		// verify date validation error occur
		reportCommonPage.verifySelectStartEndDateError(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_by_wrong_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_able_to_access_supervisor_dash_by_future_date_filter() {
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);
		reportCommonPage.selectTodayStartDateEndDate(supportDriver);

		// verify future date is disabled in start date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDate.StartDate);

		// verify future date is disabled in end date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDate.EndDate);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_access_supervisor_dash_by_future_date_filter-- passed ");
	}
}
