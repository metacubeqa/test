package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;

public class CallDistributionCases extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	private static final String format = "yyyy-MM-dd";
	private static final int startTimeIndex = 1;
	private String accountName;
	private String accountNameOther;
	private String locationNameInvalid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
	}

	@Test(groups = { "Regression" })
	public void verify_call_distribution_reports_download() {
		System.out.println("Test case --verify_call_distribution_reports_download-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		reportCommonPage.defaultDaysSelected(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_reports_download-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_acess_and_download_call_distribution_reports() {
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		reportCommonPage.defaultDaysSelected(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Week);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, startTimeIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_call_distribution_reports_using_multiple_location() {
		System.out.println("Test case --verify_call_distribution_reports_using_multiple_location-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}
		
		reportCommonPage.selectLocation(supportDriver, locationNameValid);
		callRecordingPage.clickRefreshButton(supportDriver);

		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_distribution_reports_using_multiple_location-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_acess_and_download_call_distribution_reports_admin_filters() {
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports_admin_filters-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		reportCommonPage.defaultDaysSelected(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			supportDriver.switchTo().defaultContent();
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, startTimeIndex);

		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String username = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);
		supportDriver.switchTo().defaultContent();
		
		callRecordingPage.selectUser(supportDriver, username);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports_admin_filters-- passed ");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_acess_and_download_call_distribution_reports_using_filters() {
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports_using_filters-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		reportCommonPage.defaultDaysSelected(supportDriver);

		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, format, startTimeIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, startTimeIndex);

		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String username = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);
		supportDriver.switchTo().defaultContent();

		callRecordingPage.selectUser(supportDriver, username);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);

		supportDriver.navigate().refresh();

		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		locationNameInvalid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, locationIndex);
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.selectLocation(supportDriver, locationNameInvalid);
		callRecordingPage.clickRefreshButton(supportDriver);

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);

		int callQueueIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallQueueName);
		String CallQueueName = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex);

		supportDriver.navigate().refresh();

		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		reportCommonPage.selectCallQueue(supportDriver, CallQueueName);

		callRecordingPage.clickRefreshButton(supportDriver);
	
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callQueueIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callQueueIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callQueueIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountName);
		assertTrue(Strings.isNullOrEmpty(reportCommonPage.getUserNamefieldValue(supportDriver)));

		callRecordingPage.clickRefreshButton(supportDriver);

		String username2 = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, username2);

		callRecordingPage.clickRefreshButton(supportDriver);

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username2);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username2);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username2);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_acess_and_download_call_distribution_reports_using_filters-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_user_able_to_filter_call_distribution_reports_by_call_queue() {
		System.out.println("Test case --verify_user_able_to_filter_call_distribution_reports_by_call_queue-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDistribution(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String username = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, username);
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);

		int callQueueIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallQueueName);
		String CallQueueName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, callQueueIndex);

		supportDriver.switchTo().defaultContent();
		reportCommonPage.selectCallQueue(supportDriver, CallQueueName);
		callRecordingPage.clickRefreshButton(supportDriver);

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callQueueIndex);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callQueueIndex);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, callQueueIndex), CallQueueName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), username);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_filter_call_distribution_reports_by_call_queue-- passed ");
	}

}
