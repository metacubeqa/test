package report.cases.adminReports;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class NewAccounts extends ReportBase {
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommon = new ReportMetabaseCommonPage();

	private static final String outputFormat = "MMMM d, yyyy, hh:mm a";
	
	@Test(groups = { "MediumPriority", "SupportOnly"})
	public void verify_sorting_new_accounts() {
		System.out.println("Test case --verify_sorting_new_accounts-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToNewAccounts(supportDriver);

		String endDateInputValue = HelperFunctions.GetCurrentDateTime(outputFormat);
		String startDateInputValue = HelperFunctions.addMonthYearDateToExisting(outputFormat, endDateInputValue, -30, 0, 0);

		callRecordingPage.clickRefreshButton(supportDriver);

		int idIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Id);
		reportCommon.clickHeaderToSortByIndex(supportDriver, idIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, idIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, idIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, idIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, idIndex);

		int nameIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.Name);
		reportCommon.clickHeaderToSortByIndex(supportDriver, nameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, nameIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, nameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommon.getTableResultListAccToColumn(supportDriver, nameIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, nameIndex);

		int sfIDIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.SalesforceId);
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfIDIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sfIDIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfIDIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sfIDIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sfIDIndex);

		int sandBox = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.SandBox);
		reportCommon.clickHeaderToSortByIndex(supportDriver, sandBox);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sandBox)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sandBox);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, sandBox)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, sandBox);

		int smartNumber = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.SmartNumberCount);
		reportCommon.clickHeaderToSortByIndex(supportDriver, smartNumber);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, smartNumber));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, smartNumber);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommon.getTableResultListAccToColumn(supportDriver, smartNumber));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommon.clickHeaderToSortByIndex(supportDriver, smartNumber);


		int inboundCallDurationIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.InboundCallDuration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundCallDurationIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, inboundCallDurationIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundCallDurationIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, inboundCallDurationIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, inboundCallDurationIndex);

		int outboundCallDurationIndex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.OutboundCallDuration);
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundCallDurationIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, outboundCallDurationIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundCallDurationIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommon.getTableResultListAccToColumn(supportDriver, outboundCallDurationIndex)));
		reportCommon.clickHeaderToSortByIndex(supportDriver, outboundCallDurationIndex);

		int createdDateindex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommon.verifyDateSortedCustom(supportDriver, outputFormat, outputFormat, createdDateindex, startDateInputValue, endDateInputValue);
		
		int updatedDateindex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.UpdatedDate);
		reportCommon.verifyDateSortedCustom(supportDriver, outputFormat, outputFormat, updatedDateindex, startDateInputValue, endDateInputValue);
		
		int lastLoginindex = reportCommon.getIndexNoByHeaderName(supportDriver, HeaderNames.LastLogin);
		reportCommon.verifyDateSortedCustom(supportDriver, outputFormat, outputFormat, lastLoginindex, startDateInputValue, endDateInputValue);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_new_accounts-- passed ");
	}
}
