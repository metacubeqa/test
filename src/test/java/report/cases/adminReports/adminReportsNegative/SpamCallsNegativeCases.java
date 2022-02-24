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

public class SpamCallsNegativeCases extends ReportBase {

	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();

	private String accountName;
	private String agentNameInvalid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_spam_calls_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_spam_calls_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);

		// verifying Account Name exist or not
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_spam_calls_with_invalid_accountName_filter-- passed ");
	}
	
	@Test(groups = { "Regression" , "SupportOnly" })
	public void verify_spam_calls_with_invalid_agent_name_filter() {
		System.out.println("Test case --verify_spam_calls_with_invalid_agent_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying Agent Name exist or not
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_spam_calls_with_invalid_agent_name_filter-- passed ");
	}
	
	@Test(groups = { "Regression" , "SupportOnly"})
	public void verify_spam_calls_by_wrong_date_filter() {
		System.out.println("Test case --verify_spam_calls_by_wrong_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// select start date bigger than end date
		reportCommonPage.selectStartDateBiggerEndDate(supportDriver);
		callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);

		// verify date validation error occur
		reportCommonPage.verifySelectStartEndDateError(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_spam_calls_by_wrong_date_filter-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_spam_calls_by_future_date_filter() {
		System.out.println("Test case --verify_spam_calls_by_future_date_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);
		reportCommonPage.selectTodayStartDateEndDate(supportDriver);

		// verify future date is disabled in start date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDate.StartDate);

		// verify future date is disabled in end date
		reportCommonPage.checkFutureDateIsDisabled(supportDriver, SelectDate.EndDate);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_spam_calls_by_future_date_filter-- passed ");
	}

}
