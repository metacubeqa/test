package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class ConferenceCallCases extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommon = new ReportMetabaseCommonPage();

	private String accountNameOther;
	private String accountName;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	private static final String outputFormat = "MMMM dd, yyyy, hh:mm aa";
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

	@Test(groups = { "Regression" })
	public void verify_conference_calls_report_date_filter_test() {
		System.out.println("Test case --verify_conference_calls_report_date_filter_test-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToConferenceCallTab(supportDriver);
		
		reportCommon.verifyDefaultDaysInputTab(supportDriver, inputFormat);
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		if (ReportBase.drivername.toString().equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}

		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		int startTimeIndex1 = 6;
		int endTimeIndex1 = 8;
		int startTimeIndex2 = 11;
		int endTimeIndex2 = 12;

		//verifying for default time
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex1, startDateInputValue, endDateInputValue);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex1, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex2, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex2, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();

		//verifying for one month
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

		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex1, startDateInputValue, endDateInputValue);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex1, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex2, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex2, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();

		//verifying for 90 days
		String startDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -3, 0);
		String startYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);

		String endDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String endYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate2, endMonth2, endYear2);
		
		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);

		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex1, startDateInputValue, endDateInputValue);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex1, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex2, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex2, startDateInputValue, endDateInputValue);
		
		String ToNumber = reportCommon.getFirstResultTableAccToColumn(supportDriver, 10);
		
		supportDriver.switchTo().defaultContent();

		//verifying for one year
		String startDate3 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth3 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String startYear3 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate3, startMonth3, startYear3);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate3, startMonth3, startYear3);

		String endDate3 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth3 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String endYear3 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate3, endMonth3, endYear3);
		
		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
	
		reportCommon.selectToNumber(supportDriver, ToNumber);

		callRecordingPage.clickRefreshButton(supportDriver);

		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex1, startDateInputValue, endDateInputValue);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex1, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex2, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex2, startDateInputValue, endDateInputValue);

		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);
		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);
		reportCommon.clickMultipleToNumberToSort(supportDriver);
		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);
		
		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_conference_calls_report_date_filter_test-- passed ");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_conference_calls_report_test() {
		System.out.println("Test case --verify_conference_calls_report_test-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToConferenceCallTab(supportDriver);

		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);
		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		callRecordingPage.clickRefreshButton(supportDriver);
		String ToNumber = reportCommon.getFirstResultTableAccToColumn(supportDriver, 10);
		
		supportDriver.switchTo().defaultContent();
		reportCommon.selectToNumber(supportDriver, ToNumber);
		
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);

		int startTimeIndex1 = 6;
		int endTimeIndex1 = 8;
		int startTimeIndex2 = 11;
		int endTimeIndex2 = 12;

		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex1, startDateInputValue, endDateInputValue);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex1, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startTimeIndex2, startDateInputValue, endDateInputValue);
//		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endTimeIndex2, startDateInputValue, endDateInputValue);

		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);
		reportCommon.clickMultipleToNumberToSort(supportDriver);
		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);
		reportCommon.clickMultipleToNumberToSort(supportDriver);
		assertEquals(reportCommon.getFirstResultTableAccToColumn(supportDriver, 10), ToNumber);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_conference_calls_report_test-- passed ");
	}
	
	
	//Verify no duplicate rows for call keys within Conference report
	@Test(groups = { "Regression" })
	public void verify_no_duplicate_call_key_in_call_conference_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_conference_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToConferenceCallTab(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		//verify duplicate rc key
//		int callKeyIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
//		int subStringIndex = 3;
//		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//		assertFalse(HelperFunctions.hasDuplicateItems(intListAsec));
//		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//		assertFalse(HelperFunctions.hasDuplicateItems(intListDesc));
//		reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		
		// click next button 3 times
//		boolean flag = reportCommon.navigateToNextPage(supportDriver, pageCount);
		
		//again verify duplicate rc key
//		if (flag) {
//			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//			assertFalse(HelperFunctions.hasDuplicateItems(intListAsec2));
//			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//			assertFalse(HelperFunctions.hasDuplicateItems(intListDesc2));
//			reportCommon.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		}
		
		//verify duplicate rc key merged from call key
		int mergedFromCallKeyIndex  = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.MergedFromCallKey);
		int subStringIndex = 3;
		reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedFromCallKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListAsec));
		reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedFromCallKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListDesc));
		reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
		
		// click next button 3 times
				boolean flag = reportCommon.navigateToNextPage(supportDriver, pageCount);
				
				if (flag) {
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
					List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedFromCallKeyIndex), subStringIndex));
					assertFalse(HelperFunctions.hasDuplicateItems(intListAsec2));
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
					List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedFromCallKeyIndex), subStringIndex));
					assertFalse(HelperFunctions.hasDuplicateItems(intListDesc2));
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedFromCallKeyIndex);
				}
				
				supportDriver.switchTo().defaultContent();
				supportDriver.navigate().refresh();
				dashboard.isPaceBarInvisible(supportDriver);
				
				callRecordingPage.selectAccount(supportDriver, accountName);
				callRecordingPage.clickRefreshButton(supportDriver);
				
				//verify duplicate rc key merged to call key
				int mergedToCallKeyIndex  = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.MergedToCallKey);
		//		int subStringIndex = 3;
				reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
				List<Float> intListAsecTo = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedToCallKeyIndex), subStringIndex));
				assertFalse(HelperFunctions.hasDuplicateItems(intListAsecTo));
				reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
				List<Float> intListDescTo = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedToCallKeyIndex), subStringIndex));
				assertFalse(HelperFunctions.hasDuplicateItems(intListDescTo));
				reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
				
				
				// click next button 3 times
				boolean flag1 = reportCommon.navigateToNextPage(supportDriver, pageCount);
				
				if (flag1) {
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
					List<Float> intListAsecTo1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedToCallKeyIndex), subStringIndex));
					assertFalse(HelperFunctions.hasDuplicateItems(intListAsecTo1));
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
					List<Float> intListDescTo1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommon.getTableResultListAccToColumn(supportDriver, mergedToCallKeyIndex), subStringIndex));
					assertFalse(HelperFunctions.hasDuplicateItems(intListDescTo1));
					reportCommon.clickHeaderToSortByIndex(supportDriver, mergedToCallKeyIndex);
				}
				
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_conference_report-- passed ");
	}
}
