package report.cases.adminReports;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportMetabaseCommonPage.HeaderNames;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class UserSummaryByDayCases extends ReportBase {

	Dashboard dashboard = new Dashboard();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	private static final String outputFormat = "yyyy-MM-dd";
	//private static final String outputFormat = "MMMM dd, yyyy, h:mm a";
	private static final String inputFormat = "MM/dd/yyyy";
	
	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
	
	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private String locationNameInvalid;
	private String teamNameInvalid;
	

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
		teamNameInvalid = CONFIG.getProperty("qa_report_invalid_team_name");
	}

	@Test(groups = { "Regression" })
	public void verify_user_summary_by_day_reports_using_location_filter() {
		System.out.println("Test case --verify_user_summary_by_day_reports_using_location_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}
				
		reportCommonPage.selectLocation(supportDriver, locationNameValid);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_summary_by_day_reports_using_location_filter-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_summary_by_day_reports_by_dates_filter() {
		System.out.println("Test case --verify_user_summary_by_day_reports_by_dates_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		//reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}

		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, outputFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();

		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, outputFormat, index);
		
		supportDriver.switchTo().defaultContent();

		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);

		callRecordingPage.clickRefreshButton(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, outputFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_summary_by_day_reports_by_dates_filter-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_summary_by_day_filter_by_user() {
		System.out.println("Test case --verify_user_summary_by_day_filter_by_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		int userNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String username = reportCommonPage.iterateListForNonEmptyResult(supportDriver, userNameIndex);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, username);

		callRecordingPage.clickRefreshButton(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, outputFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_summary_by_day_filter_by_user-- passed ");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_user_summary_by_day_default_filter() {
		System.out.println("Test case --verify_user_summary_by_day_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		//reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		callRecordingPage.clickRefreshButton(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, outputFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();

		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameValid));
		callRecordingPage.selectUser(supportDriver, agentNameInvalid);

		assertTrue(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameValid));
		
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));

		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);

		callRecordingPage.clickRefreshButton(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, outputFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		int userNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, userNameIndex), agentNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, userNameIndex), agentNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, userNameIndex), agentNameInvalid);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountName);
		assertTrue(Strings.isNullOrEmpty(reportCommonPage.getUserNamefieldValue(supportDriver)));
		
		//clearing filters
		callRecordingPage.refreshCurrentDocument(supportDriver);
		
		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		reportCommonPage.selectLocation(supportDriver, locationNameInvalid);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameInvalid);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_summary_by_day_default_filter-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_summary_by_day_admin_default_filter() {
		System.out.println("Test case --verify_user_summary_by_day_admin_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, outputFormat, index);

		supportDriver.switchTo().defaultContent();
		
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));
		assertTrue(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameValid));

		reportCommonPage.selectLocation(supportDriver, locationNameValid);

		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));

		callRecordingPage.clickRefreshButton(supportDriver);
		
		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		agentNameValid = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);
		supportDriver.switchTo().defaultContent();
		
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameValid));
		callRecordingPage.selectUser(supportDriver, agentNameValid);
		
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);

		callRecordingPage.clickRefreshButton(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, outputFormat, index);

		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Location);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex), locationNameValid);

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);

		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_summary_by_day_admin_default_filter-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_user_summary_by_day() {
		System.out.println("Test case --verify_sorting_user_summary_by_day-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//sort userId
		int idIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserId);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		
		//sort displayName
		int displayNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, displayNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, displayNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, displayNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, displayNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, displayNameIndex);
		
		//sort status
		int statusIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Status);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, statusIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, statusIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);

		//sort location
		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);

		//sort time in seconds
		int timeSecondsIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TimeSeconds);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeSecondsIndex);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, timeSecondsIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeSecondsIndex);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, timeSecondsIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeSecondsIndex);

		//sort time in minutes
		int timeMinIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TimeMinutes);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeMinIndex);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, timeMinIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeMinIndex);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, timeMinIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, timeMinIndex);
	
		//Sort Date Created
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
				
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_user_summary_by_day-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_user_busy_summary_by_day_able_to_open_user_presence_detail_url() {
		System.out.println("Test case --verify_user_busy_summary_by_day_able_to_open_user_presence_detail_url-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummaryByDay(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		agentNameValid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentIndex);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentIndex);
		String agentNameValid2 = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex);
		
		int userPresenceUrlIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserPresenceDetail);
		reportCommonPage.clickFirstResultTableAccToColumn(supportDriver, userPresenceUrlIndex);
		supportDriver.switchTo().defaultContent();
        
		callRecordingPage.selectAccount(supportDriver, accountName);
        assertEquals(reportCommonPage.getAgentInputValue(supportDriver) , agentNameValid2);
        
        callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		
		callRecordingPage.clickRefreshButton(supportDriver);
        
		agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		
		//int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		//callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, detailPageFormat, index);
		
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectUser(supportDriver, agentNameValid);
		
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		
		//callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, detailPageFormat, index);
		//reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, detailPageFormat, index, startDateInputValue, endDateInputValue);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_busy_summary_by_day_able_to_open_user_presence_detail_url-- passed ");
		
	}
}
