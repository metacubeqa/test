package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportsNonMetabaseCommonPage;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftphoneCallHistoryPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallReportsUTC extends ReportBase{
	
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	ReportThisCallPage reportThisCallPage = new ReportThisCallPage(); 
	ReportMetabaseCommonPage reportMetabase = new ReportMetabaseCommonPage();
	
	private String accountName;
	private String accountNameOther;
	private String validLocation;
	private static final String format = "dd/MM/yyyy HH:mm";
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "call-reports-export-UTC";
	
	@BeforeClass(groups = {"Regression"})
	public void beforeClass(){
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		validLocation    = CONFIG.getProperty("qa_support_report_valid_location");
	}
	
	@Test(groups = "Regression")
	public void verify_call_reports_utc_sorting_on_all_columns() {
		System.out.println("Test case --verify_call_reports_utc_sorting_on_all_columns-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);

		reportCommonPage.clickSearchBtn(supportDriver);

		// verifying data for account name
		if (ReportBase.drivername.equals("supportDriver")) {
			reportCommonPage.clickAccountToSort(supportDriver);
			assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getAccountNameList(supportDriver)));
			reportCommonPage.clickAccountToSort(supportDriver);
			assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getAccountNameList(supportDriver)));
			reportCommonPage.clickAccountToSort(supportDriver);
		}

		// verifying data for callKey name
		int subStringIndex = 3;
		reportCommonPage.clickCallKeyToSort(supportDriver);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getCallKeyList(supportDriver), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickCallKeyToSort(supportDriver);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getCallKeyList(supportDriver), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickCallKeyToSort(supportDriver);
		
		
		// verifying data for user name
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getUserNameList(supportDriver)));
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getUserNameList(supportDriver)));
		reportCommonPage.clickUserNameToSort(supportDriver);

		// verifying data for direction
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getDirectionList(supportDriver)));
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getDirectionList(supportDriver)));
		reportCommonPage.clickDirectionToSort(supportDriver);

		// verifying data for category
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getCategoryList(supportDriver)));
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getCategoryList(supportDriver)));
		reportCommonPage.clickCategoryToSort(supportDriver);
		
		// verifying data for rating
		reportCommonPage.clickRatingToSort(supportDriver);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getRatingList(supportDriver));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommonPage.clickRatingToSort(supportDriver);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getRatingList(supportDriver));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommonPage.clickRatingToSort(supportDriver);
		
		// verifying data for date
		reportCommonPage.clickDateCreatedToSort(supportDriver);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(format, reportCommonPage.getDateCreatedList(supportDriver))));
		reportCommonPage.clickDateCreatedToSort(supportDriver);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(HelperFunctions.getStringListInDateFormat(format, reportCommonPage.getDateCreatedList(supportDriver))));
		reportCommonPage.clickDateCreatedToSort(supportDriver);
		
		// verifying data for location
		reportCommonPage.clickLocationToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getLocationList(supportDriver)));
		reportCommonPage.clickLocationToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getLocationList(supportDriver)));
		reportCommonPage.clickLocationToSort(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reports_utc_sorting_on_all_columns-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_support_able_to_search_download_call_reports_utc() {
		System.out.println("Test case --verify_support_able_to_search_download_call_reports_utc-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsUTC(supportDriver);
		
		// verifying default days selected
		//String inputFormat = "MM/dd/yyyy";
		//reportMetabase.verifyDefaultDaysInputTab(supportDriver, inputFormat);
		
		// verifying date format
		assertTrue(HelperFunctions.isDateInGivenFormat(format, reportCommonPage.getDateCreated(supportDriver)));
		reportCommonPage.enterAndSelectAccountName(supportDriver, accountName);
		reportCommonPage.clickSearchBtn(supportDriver);
		
	//	reportCommonPage.clickLocationToSort(supportDriver);
		reportCommonPage.clickDirectionToSort(supportDriver);
		String userName = reportCommonPage.getUserName(supportDriver);
		String direction = reportCommonPage.getDirection(supportDriver);
		String category = reportCommonPage.getCategory(supportDriver);
		
	//	int locationIndex = reportMetabase.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
	//	String location  = reportCommonPage.getLocation(supportDriver);
		
		String userID = reportCommonPage.clickUrlAndGetUserId(supportDriver);
		
		//verify By default Start date and End date is selected for 1 month duration
		String currentDate = HelperFunctions.GetCurrentDateTime("dd/MM/yyyy");
		String startDate = HelperFunctions.addMonthYearDateToExisting("dd/MM/yyyy", currentDate, -1, -1, 0);
		String endDate = HelperFunctions.addMonthYearDateToExisting("dd/MM/yyyy", currentDate, 0, 0, 0);
		
		reportCommonPage.selectCategory(supportDriver, category);
		reportCommonPage.selectDirection(supportDriver, direction);
		reportCommonPage.enterAndSelectUserName(supportDriver, userName, userID);
		reportCommonPage.enterAndSelectLocation(supportDriver, validLocation);
		
		reportCommonPage.enterStartDateText(supportDriver, startDate);
		reportCommonPage.enterEndDateText(supportDriver, endDate);
		reportCommonPage.clickSearchBtn(supportDriver);
		
		// verifying data for account name
		assertEquals(reportCommonPage.getAccountName(supportDriver), accountName);
		reportCommonPage.clickAccountToSort(supportDriver);
		assertEquals(reportCommonPage.getAccountName(supportDriver), accountName);
		reportCommonPage.clickAccountToSort(supportDriver);
		assertEquals(reportCommonPage.getAccountName(supportDriver), accountName);
		reportCommonPage.clickAccountToSort(supportDriver);

		//verifying data for user name
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		
		//verifying data for direction
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		
		//verifying data for category
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		
		// verifying data for location
		assertEquals(reportCommonPage.getLocation(supportDriver), validLocation);
		reportCommonPage.clickLocationToSort(supportDriver);
		assertEquals(reportCommonPage.getLocation(supportDriver), validLocation);
		reportCommonPage.clickLocationToSort(supportDriver);
		assertEquals(reportCommonPage.getLocation(supportDriver), validLocation);
		reportCommonPage.clickLocationToSort(supportDriver);
		
		reportCommonPage.verifyCreatedDateSorted(supportDriver, format, reportDuration.Month);
		
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_able_to_search_download_call_reports_utc-- passed ");
	}
	
}
