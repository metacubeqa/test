package report.cases.adminReports;

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

public class CallDurationByDay extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommon = new ReportMetabaseCommonPage();

	private String accountName;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result_20";
	private static final String outputFormat = "MM/dd/yyyy";
	private static final String inputFormat = "MM/dd/yyyy";
	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}

	@Test(groups = { "Regression"})
	public void verify_call_duration_by_day_tabular() {
		System.out.println("Test case --verify_call_duration_by_day_tabular-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDayTabular(supportDriver);

		reportCommon.verifyDefaultDaysInputTab(supportDriver, inputFormat);
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

		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -2, 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);

		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -1, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);

		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);

		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();
		startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, -7, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, currentMonth, currentYear);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, currentMonth, currentYear);

		reportCommon.selectStartEndDate(supportDriver, "End", endDate, currentMonth, currentYear);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate, currentMonth, currentYear);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_duration_by_day_tabular-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_call_duration_by_day_tabular() {
		System.out.println("Test case --verify_sorting_call_duration_by_day_tabular-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallDurationByDayTabular(supportDriver);

		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//Sort Date Created
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		//sort total call duration
		int totalCallDurationIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.TotalCallDuration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, totalCallDurationIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, totalCallDurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommon.clickHeaderToSortByIndex(supportDriver, totalCallDurationIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, totalCallDurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommon.clickHeaderToSortByIndex(supportDriver, totalCallDurationIndex);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_call_duration_by_day_tabular-- passed ");
	}
}
