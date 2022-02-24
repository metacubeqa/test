package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallReports extends ReportBase{
	
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	ReportThisCallPage reportThisCallPage = new ReportThisCallPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	
	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private String validLocation;
	private static final String format = "MM/dd/yyyy h:mm a";
	
	@BeforeClass(groups = {"Regression"})
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
		validLocation    =  CONFIG.getProperty("qa_support_report_valid_location");
	}
	
	@Test(groups = "Regression")
	public void verify_call_reports_sorting_on_all_columns() {
		System.out.println("Test case --verify_call_reports_sorting_on_all_columns-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReports(supportDriver);

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
		System.out.println("Test case --verify_call_reports_sorting_on_all_columns-- passed ");
	}

	@Test(groups = { "Regression", "AdminOnly", "ExcludeProd"})
	public void admin_able_to_search_and_download_call_reports() {
		System.out.println("Test case --admin_able_to_search_and_download_call_reports-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReports(supportDriver);
		
		// get smart number from softphone 
		String smartNumber = CONFIG.getProperty("qa_report_admin_user_number");
		
		// created new webdriver 
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		//calling from agent to admin/support
		reportCommonPage.switchToTab(webSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		softPhoneCalling.softphoneAgentCall(webSupportDriver, smartNumber);
		softPhoneCalling.isCallHoldButtonVisible(webSupportDriver);
		
		reportCommonPage.switchToTab(supportDriver, 1);
		softPhoneCalling.pickupIncomingCall(supportDriver);
		softPhoneCalling.hangupActiveCall(supportDriver);
		
		// report the call
		HashMap<String, String> reportCall = new HashMap<String, String>();
		reportCall.put("Category", "Audio Latency");
		reportCall.put("Notes", "AutoCallReportNotes".concat(HelperFunctions.GetRandomString(4)));
		reportCall.put("Rating", "4");
		reportCall.put("Direction", "inbound");
		reportThisCallPage.giveCallReport(supportDriver, 4, reportCall.get("Category"), reportCall.get("Notes"));
		
		//if any active call present
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		reportCommonPage.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReports(supportDriver);
				
		// verify category 
		assertEquals(reportCommonPage.getCategory(supportDriver), reportCall.get("Category"));
		// verifying direction
		assertEquals(reportCommonPage.getDirection(supportDriver), reportCall.get("Direction"));
		// verifying rating
		assertEquals(reportCommonPage.getRating(supportDriver), reportCall.get("Rating"));
		// verifying notes
		assertEquals(reportCommonPage.getNotes(supportDriver), reportCall.get("Notes"));
		
		reportCommonPage.clickLocationToSort(supportDriver);
		String userName = reportCommonPage.getUserName(supportDriver);
		String direction = reportCommonPage.getDirection(supportDriver);
		String category = reportCommonPage.getCategory(supportDriver);
		String location  = reportCommonPage.getLocation(supportDriver);
		String userID = reportCommonPage.clickUrlAndGetUserId(supportDriver);

		// verify By default Start date and End date is selected for 1 month duration
		String currentDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String startDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", currentDate, -1, -1, 0);
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", currentDate, 0, 0, 0);

		reportCommonPage.selectCategory(supportDriver, category);
		reportCommonPage.selectDirection(supportDriver, direction);
		reportCommonPage.enterAndSelectUserName(supportDriver, userName, userID);
		reportCommonPage.enterAndSelectLocation(supportDriver, location);

		reportCommonPage.enterStartDateText(supportDriver, startDate);
		reportCommonPage.enterEndDateText(supportDriver, endDate);

		reportCommonPage.clickSearchBtn(supportDriver);

		// verifying data for user name
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);

		// verifying data for direction
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);

		// verifying data for category
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		assertEquals(reportCommonPage.getCategory(supportDriver), category);
		reportCommonPage.clickCategoryToSort(supportDriver);
		
		// verifying data for location
		assertEquals(reportCommonPage.getLocation(supportDriver), location);
		reportCommonPage.clickLocationToSort(supportDriver);
		assertEquals(reportCommonPage.getLocation(supportDriver), location);
		reportCommonPage.clickLocationToSort(supportDriver);
		assertEquals(reportCommonPage.getLocation(supportDriver), location);
		reportCommonPage.clickLocationToSort(supportDriver);

		// verifying date
		reportCommonPage.verifyCreatedDateSorted(supportDriver, format, reportDuration.Month);
		
		driverUsed.put("supportDriver", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --admin_able_to_search_and_download_call_reports-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void support_able_to_search_and_download_call_reports() {
		System.out.println("Test case --support_able_to_search_and_download_call_reports-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReports(supportDriver);
		
		// get smart number from softphone 
		String smartNumber = CONFIG.getProperty("qa_report_support_user_number");
		
		// created new webdriver 
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		
		//calling from agent to admin/support
		reportCommonPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.softphoneAgentCall(adminDriver, smartNumber);
		softPhoneCalling.isCallHoldButtonVisible(adminDriver);
		
		reportCommonPage.switchToTab(supportDriver, 1);
		softPhoneCalling.pickupIncomingCall(supportDriver);
		softPhoneCalling.hangupActiveCall(supportDriver);
		
		// report the call
		HashMap<String, String> reportCall = new HashMap<String, String>();
		reportCall.put("Category", "Audio Latency");
		reportCall.put("Notes", "AutoCallReportNotes".concat(HelperFunctions.GetRandomString(4)));
		reportCall.put("Rating", "4");
		reportCall.put("Direction", "inbound");
		reportThisCallPage.giveCallReport(supportDriver, 4, reportCall.get("Category"), reportCall.get("Notes"));
		
		//if any active call present
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		reportCommonPage.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReports(supportDriver);
				
		// verify category 
		assertEquals(reportCommonPage.getCategory(supportDriver), reportCall.get("Category"));
		// verifying direction
		assertEquals(reportCommonPage.getDirection(supportDriver), reportCall.get("Direction"));
		// verifying rating
		assertEquals(reportCommonPage.getRating(supportDriver), reportCall.get("Rating"));
		// verifying notes
		assertEquals(reportCommonPage.getNotes(supportDriver), reportCall.get("Notes"));
		
		reportCommonPage.enterAndSelectAccountName(supportDriver, accountNameOther);
		reportCommonPage.clickSearchBtn(supportDriver);
		
		reportCommonPage.clickLocationToSort(supportDriver);
		String userName = reportCommonPage.getUserName(supportDriver);
		String direction = reportCommonPage.getDirection(supportDriver);
		String category = reportCommonPage.getCategory(supportDriver);
		String location  = reportCommonPage.getLocation(supportDriver);
		String userID = reportCommonPage.clickUrlAndGetUserId(supportDriver);

		// verify By default Start date and End date is selected for 1 month duration
		String currentDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String startDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", currentDate, -1, -1, 0);
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", currentDate, 0, 0, 0);

		reportCommonPage.selectCategory(supportDriver, category);
		reportCommonPage.selectDirection(supportDriver, direction);
		reportCommonPage.enterAndSelectUserName(supportDriver, userName, userID);
		reportCommonPage.enterAndSelectLocation(supportDriver, validLocation);

		reportCommonPage.enterStartDateText(supportDriver, startDate);
		reportCommonPage.enterEndDateText(supportDriver, endDate);

		reportCommonPage.clickSearchBtn(supportDriver);

		// verifying data for user name
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);
		assertEquals(reportCommonPage.getUserName(supportDriver), userName);
		reportCommonPage.clickUserNameToSort(supportDriver);

		// verifying data for direction
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);
		assertEquals(reportCommonPage.getDirection(supportDriver), direction);
		reportCommonPage.clickDirectionToSort(supportDriver);

		// verifying data for category
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

		// verifying date
		reportCommonPage.verifyCreatedDateSorted(supportDriver, format, reportDuration.Month);
		
		// verifying agents name
		dashboard.navigateToCallReportsUTC(supportDriver);
		dashboard.navigateToCallReports(supportDriver);
		reportCommonPage.enterAndSelectAccountName(supportDriver, accountName);
		assertTrue(reportCommonPage.verifyAgenNameInDropDown(supportDriver, agentNameInvalid));
		
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --support_able_to_search_and_download_call_reports-- passed ");
	}
	
}
