package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import report.source.ReportsNonMetabaseCommonPage.SelectDateType;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallReportsUTCNegativeCases extends ReportBase {

	ReportsNonMetabaseCommonPage reportNonMetabasePage =  new ReportsNonMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	
	private String accountName;
	private String locationNameInvalid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_reportsUTC_with_invalid_location_name_filter() {
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_location_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		reportNonMetabasePage.enterAndSelectAccountName(supportDriver, accountName);

		// verifying Location Name exist or not
		assertFalse(reportNonMetabasePage.verifyLocationInDropDown(supportDriver, locationNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_location_name_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_reportsUTC_by_wrong_date_filter() {
		System.out.println("Test case --verify_call_reportsUTC_by_wrong_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		reportNonMetabasePage.enterAndSelectAccountName(supportDriver, accountName);

		// select start date bigger than end date
		reportNonMetabasePage.selectStartDateBiggerEndDate(supportDriver, "dd/MM/yyyy");
		reportNonMetabasePage.clickSearchBtn(supportDriver);

		// verify date validation error occur
		reportNonMetabasePage.verifyDateToastMessage(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reportsUTC_by_wrong_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_reportsUTC_by_future_date_filter() {
		System.out.println("Test case --verify_call_reportsUTC_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		reportNonMetabasePage.enterAndSelectAccountName(supportDriver, accountName);
		
		String currentDate = HelperFunctions.GetCurrentDateTime("dd/MM/yyyy");
		reportNonMetabasePage.enterStartDateText(supportDriver,currentDate);
		reportNonMetabasePage.enterEndDateText(supportDriver,currentDate);

		// verify future date is disabled in start date
		reportNonMetabasePage.checkFutureDateIsDisabled(supportDriver, SelectDateType.StartDate);

		// verify future date is disabled in end date
		reportNonMetabasePage.checkFutureDateIsDisabled(supportDriver, SelectDateType.EndDate);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reportsUTC_by_future_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_call_reportsUTC_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		// verifying Account Name exist or not
		assertFalse(reportNonMetabasePage.verifyAccountNameInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_accountName_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_reportsUTC_with_invalid_agent_name_filter() {
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_agent_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		reportNonMetabasePage.enterAndSelectAccountName(supportDriver, accountName);

		// verifying Agent Name exist or not
		assertFalse(reportNonMetabasePage.verifyAgenNameInDropDown(supportDriver, HelperFunctions.GetRandomString(6)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reportsUTC_with_invalid_agent_name_filter-- passed ");
	}

}

