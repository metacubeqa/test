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

public class CallVolumeByDayCases extends ReportBase {
	
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommon = new ReportMetabaseCommonPage();
	
	private String accountName;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
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
	public void verify_call_volume_by_day() {
		System.out.println("Test case --verify_call_volume_by_day-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallVolumeByDay(supportDriver);
		
		String format = "MM/dd/yyyy";
		reportCommon.verifyDefaultDaysInputTab(supportDriver, format);
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		reportCommon.verifygraphDate(supportDriver, outputFormat, startDateInputValue, endDateInputValue);

		supportDriver.switchTo().defaultContent();
		
		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , -2 , 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
		
		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
		
		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		callRecordingPage.clickRefreshButton(supportDriver);

		reportCommon.verifygraphDate(supportDriver, outputFormat, startDateInputValue, endDateInputValue);
		
		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_volume_by_day-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_call_volume_by_day_tabular() {
		System.out.println("Test case --verify_call_volume_by_day_tabular-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallVolumeByDayTab(supportDriver);
		
		String format = "MM/dd/yyyy";
		reportCommon.verifyDefaultDaysInputTab(supportDriver, format);
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
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
        
        String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , -2 , 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
		
		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommon.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
		
		startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		
		index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_volume_by_day_tabular-- passed ");
	}

	@Test(groups = { "Regression"})
	public void verify_sorting_call_volumes_by_day() {
		System.out.println("Test case --verify_sorting_call_volumes_by_day-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallVolumeByDayTab(supportDriver);
		
		String startDateInputValue = reportCommon.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommon.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//Sort Date Created
		int index = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		//sort all
		int allIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.All);
		reportCommon.clickHeaderToSortByIndex(supportDriver, allIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, allIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommon.clickHeaderToSortByIndex(supportDriver, allIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, allIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommon.clickHeaderToSortByIndex(supportDriver, allIndex);
		
		//sort inbound
		int inboundIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Inbound);
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, inboundIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, inboundIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		
		//sort outbound
		int outboundIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Outbound);
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundIndex);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, outboundIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundIndex);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, outboundIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_call_volumes_by_day-- passed ");
	}
}
