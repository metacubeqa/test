package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class BillingInternationalNegCases extends ReportBase {

	ReportsNonMetabaseCommonPage reportNonMetabasePage =  new ReportsNonMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecording = new CallRecordingReportPage();

	private String accountName;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_billing_international_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_billing_international_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToBillingInternational(supportDriver);

		// verifying Account Name exist or not
		assertFalse(reportNonMetabasePage.verifyAccountNameInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_billing_international_with_invalid_accountName_filter-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_billing_international_by_future_date_filter() {
		System.out.println("Test case --verify_billing_international_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToBillingInternational(supportDriver);

		reportNonMetabasePage.enterAndSelectAccountName(supportDriver, accountName);

		// verify future date is disabled in end date
		reportNonMetabasePage.checkBillingFutureDateIsDisabled(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_billing_international_by_future_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_billing_international_report_by_email_filter() {
		System.out.println("Test case --verify_billing_international_report_by_email_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToBillingInternational(supportDriver);

		callRecording.selectAccount(supportDriver, accountName);
		

		// select Billing month
		String prevMonth = HelperFunctions.addMonthYearDateToExisting("MMM", HelperFunctions.GetCurrentDateTime("MMM"),0 , -1 , 0);
		String year = HelperFunctions.GetCurrentDateTime("yyyy");
		reportNonMetabasePage.selectBillingMonth(supportDriver, prevMonth,year);
		reportNonMetabasePage.clickDownLoadBtn(supportDriver);
		
		// verify Email toast message
		reportNonMetabasePage.verifyEmailToastMessage(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_billing_international_report_by_email_filter-- passed ");
	}
}

