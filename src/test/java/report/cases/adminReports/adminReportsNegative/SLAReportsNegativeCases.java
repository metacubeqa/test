package report.cases.adminReports.adminReportsNegative;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage.SelectDateType;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;

public class SLAReportsNegativeCases extends ReportBase {

	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();

	private String accountName;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}
	
	@Test(groups = { "Regression" })
	public void verify_sla_reports_by_wrong_date_filter() {
		System.out.println("Test case --verify_sla_reports_by_wrong_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// select start date bigger than end date and verify date validation error occur
		reportCommonPage.verifyEndDateNotBeforeStartDateMonthly(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sla_reports_by_wrong_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_sla_reports_by_future_date_filter() {
		System.out.println("Test case --verify_sla_reports_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verify future date is disabled in start date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDateType.StartDate);

		// verify future date is disabled in end date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDateType.EndDate);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sla_reports_by_future_date_filter-- passed ");
	}
}
