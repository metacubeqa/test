package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import softphone.source.salesforce.TaskDetailPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class SpamCalls extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();

	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private static final String outputFormat = "MM/dd/yyyy hh:mm:ss a";
	private static final String inputFormat = "MM/dd/yyyy";
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";

	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName 	 = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_data_for_default_filters() {
		System.out.println("Test case --verify_data_for_default_filters-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);

		// verify By default Start date and End date is selected for 1 month duration
		reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		// verify empty account error
		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		//verifying agent name filters with same account
		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String agentName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentNameIndex);
		String otherOrgAgentName = agentNameInvalid;
		
		supportDriver.switchTo().defaultContent();
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));

		callRecordingPage.selectUser(supportDriver, agentName);
		callRecordingPage.clickRefreshButton(supportDriver);

		// verifying data for agent
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		// selecting other account and verifying user name in dropdown
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentName));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_data_for_default_filters-- passed ");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_data_after_update_date_filter_range() {
		System.out.println("Test case --verify_data_after_update_date_filter_range-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSpamCallsReport(supportDriver);
		callRecordingPage.selectAccount(supportDriver, accountName);

		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -2, 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);

		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -1, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);

		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);;
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_data_after_update_date_filter_range-- passed ");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_spam_calls_reports_download() {
		System.out.println("Test case --verify_spam_calls_reports_download-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToSpamCallsReport(supportDriver);
		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_spam_calls_reports_download-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_sorting_spam_calls() {
		System.out.println("Test case --verify_sorting_spam_calls-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToSpamCallsReport(supportDriver);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//sorting on agent
		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, agentIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, agentIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		
		//sorting on direction
		int directionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Direction);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, directionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, directionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);
		
		//sort id
		int idIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Id);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		
		//sort fromNumber
		int fromNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		
		//sort toNumber
		int toNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);

		//sort smartNumberId
		int smartNumberId = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.SmartNumberId);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, smartNumberId);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, smartNumberId)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, smartNumberId);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, smartNumberId)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, smartNumberId);

		//Sort Date Created
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
				
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_spam_calls-- passed ");
	}
}
