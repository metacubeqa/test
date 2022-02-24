package report.cases.adminReports;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallDurationCases extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommon = new ReportMetabaseCommonPage();

	private String accountNameOther;
	private String accountName;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result_20";
	private static final String outputFormat = "MMMM d, yyyy, hh:mm a";
	private static final String inputFormat = "MM/dd/yyyy";
	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
	int pageCount = 3;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_call_duration_default_filter_test() {
		System.out.println("Test case --verify_call_duration_default_filter_test-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDay(supportDriver);

		reportCommon.verifyDefaultDaysInputTab(supportDriver, inputFormat);
		
		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_duration_default_filter_test-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_call_duration_admin_default_filter_test() {
		System.out.println("Test case --verify_call_duration_admin_default_filter_test-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDay(supportDriver);

		String format = "MM/dd/yyyy";
		reportCommon.verifyDefaultDaysInputTab(supportDriver, format);
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		
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
		
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		String curentDate = HelperFunctions.GetCurrentDateTime("d");
		String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
		String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
		String prevDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, -1, 0, 0);
		
		reportCommon.selectStartEndDate(supportDriver, "Start", prevDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "End", curentDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "Start", prevDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "End", curentDate, currentMonth, currentYear);
		
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_duration_admin_default_filter_test-- passed ");

	}

	@Test(groups = { "Regression"})
	public void verify_call_duration_date_filter_test() {
		System.out.println("Test case --verify_call_duration_date_filter_test-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDay(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}

		String startDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String startYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);

		String endDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String endYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate2, endMonth2, endYear2);

		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -2, 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);

		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);

		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		supportDriver.switchTo().defaultContent();

	//	reportCommon.selectTodayStartDateEndDate(supportDriver);
		String curentDate = HelperFunctions.GetCurrentDateTime("d");
		String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
		String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
		String prevDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, -1, 0, 0);
		
		reportCommon.selectStartEndDate(supportDriver, "Start", curentDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "End", curentDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "Start", prevDate, currentMonth, currentYear);
		reportCommon.idleWait(1);
		reportCommon.selectStartEndDate(supportDriver, "End", curentDate, currentMonth, currentYear);
		
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_duration_date_filter_test-- passed ");
	}


	@Test(groups = { "MediumPriority"})
	public void verify_sorting_call_duration() {
		System.out.println("Test case --verify_sorting_call_duration-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDay(supportDriver);

		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//Sort Data
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		int agentNameIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);;
		reportCommon.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, agentNameIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, agentNameIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, agentNameIndex);

		int callKeyIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3; 
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);

		int callSIDIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.CallSId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, callSIDIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, callSIDIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callSIDIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, callSIDIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callSIDIndex);

		int actualDuration = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.ActualDuration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, actualDuration);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, actualDuration));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, actualDuration);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, actualDuration));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, actualDuration);

		int expectedDuration = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.ExpectedDuration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, expectedDuration);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, expectedDuration));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommon.clickHeaderToSortByIndex(supportDriver, expectedDuration);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, expectedDuration));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommon.clickHeaderToSortByIndex(supportDriver, expectedDuration);

		int correctIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Correct);
		reportCommon.clickHeaderToSortByIndex(supportDriver, correctIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, correctIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, correctIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, correctIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, correctIndex);

		int sfTaskIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.SFTaskId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfTaskIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sfTaskIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfTaskIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sfTaskIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfTaskIndex);

		int leg1RoleIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg1Role);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1RoleIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg1RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1RoleIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg1RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1RoleIndex);

		int leg1SidIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg1SId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1SidIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg1SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1SidIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg1SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1SidIndex);

		int leg1DurationIndex = 11;
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1DurationIndex);
		List<Float> intListAsec4 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, leg1DurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec4));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1DurationIndex);
		List<Float> intListDesc4 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, leg1DurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc4));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg1DurationIndex);

		int leg2RoleIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg2Role);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2RoleIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg2RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2RoleIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg2RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2RoleIndex);

		int leg2SidIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg2SId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2SidIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg2SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2SidIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg2SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg2SidIndex);

		int leg3RoleIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg3Role);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3RoleIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg3RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3RoleIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, leg3RoleIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3RoleIndex);

		int leg3SidIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg3SId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3SidIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg3SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3SidIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, leg3SidIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3SidIndex);

		int leg3DurationIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Leg3Duration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3DurationIndex);
		List<Float> intListAsec5 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, leg3DurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec5));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3DurationIndex);
		List<Float> intListDesc5 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, leg3DurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc5));
		reportCommon.clickHeaderToSortByIndex(supportDriver, leg3DurationIndex);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_call_duration-- passed ");
	}
	
	//Verify no duplicate rows for call keys within Call Duration reports
	@Test(groups = { "Regression" })
	public void verify_no_duplicate_call_key_in_call_duration_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_duration_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDay(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		//verify duplicate rc key
		int callKeyIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3;
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListAsec));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListDesc));
		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		
		// click next button 3 times
		boolean flag = reportCommon.navigateToNextPage(supportDriver, pageCount);
		
		//again verify duplicate rc key
		if (flag) {
			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			assertFalse(HelperFunctions.hasDuplicateItems(intListAsec2));
			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			assertFalse(HelperFunctions.hasDuplicateItems(intListDesc2));
			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_duration_report-- passed ");
	}
}
