package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallDistributionNegativeCases extends ReportBase {

	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();

	private String accountName;
	private String agentNameInvalid;
	private String locationNameInvalid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_call_distribution_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_call_distribution_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		// verifying Account Name exist or not
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_with_invalid_accountName_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_distribution_with_invalid_agent_name_filter() {
		System.out.println("Test case --verify_call_distribution_with_invalid_agent_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying Agent Name exist or not
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_with_invalid_agent_name_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_distribution_with_invalid_location_name_filter() {
		System.out.println("Test case --verify_call_distribution_with_invalid_location_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying Location Name exist or not
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_with_invalid_location_name_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_distribution_by_invalid_queue_name_filter() {
		System.out.println("Test case --verify_call_distribution_by_invalid_queue_name_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);

		// verifying CallQueue Name exist or not
		assertFalse(reportCommonPage.verifyCallQueueExistsInDropDown(supportDriver, HelperFunctions.GetRandomString(6)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_by_invalid_queue_name_filter-- passed ");
	}

}
