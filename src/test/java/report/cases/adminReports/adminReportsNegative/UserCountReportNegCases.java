package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage.SelectDateType;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class UserCountReportNegCases extends ReportBase {

	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	
	private String accountName;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_user_count_report_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_user_count_report_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserCountsReport(supportDriver);

		// verifying Account Name exist or not
		assertFalse(reportCommonPage.verifyAccountNameInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_count_report_with_invalid_accountName_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_count_by_future_date_filter() {
		System.out.println("Test case --verify_user_count_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserCountsReport(supportDriver);

		reportCommonPage.enterAndSelectAccountName(supportDriver, accountName);
		
		reportCommonPage.clickDatePickerInputTab(supportDriver);
		reportCommonPage.selectTodayDateAsEndDate(supportDriver);

		// verify future date is disabled in end date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDateType.EndDate);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_count_by_future_date_filter-- passed ");
	}
	
}
