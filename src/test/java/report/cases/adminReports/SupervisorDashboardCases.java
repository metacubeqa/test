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
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class SupervisorDashboardCases extends ReportBase {

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	private static final String outputFormat = "MMMM dd, yyyy, hh:mm aa";
	private static final String inputFormat = "MM/dd/yyyy";
	
	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
	int pageCount = 3;

	private String accountName;
	private String accountNameOther;
	private String locationNameInvalid;
	private String teamNameInvalid;
	
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		locationNameInvalid = CONFIG.getProperty("qa_report_invalid_location_name");
		teamNameInvalid = CONFIG.getProperty("qa_report_invalid_team_name");
	}

	@Test(groups = { "Regression" })
	public void verify_supervisor_dashboard_multiple_locations() {
		System.out.println("Test case --verify_supervisor_dashboard_multiple_locations-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);

		int locationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		locationNameValid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, locationIndex);
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.selectLocation(supportDriver, locationNameValid);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex) ,locationNameValid);

		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex) ,locationNameValid);

		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, locationIndex) ,locationNameValid);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_dashboard_multiple_locations-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_supervisor_dashboard_multiple_team_names() {
		System.out.println("Test case --verify_supervisor_dashboard_multiple_teamNames-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
		
		int teamNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
		
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		teamNameValid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, teamNameIndex);
		teamNameValid = reportCommonPage.getSupervisorDashboardTeamName(supportDriver, teamNameValid);
		supportDriver.switchTo().defaultContent();
		
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));
		
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
	
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_dashboard_multiple_teamNames-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_supervisor_dashboard_duration_filter() {
		System.out.println("Test case --verify_supervisor_dashboard_duration_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);

		reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}
		
		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , -2 , 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);
		
		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		supportDriver.switchTo().defaultContent();
		
		String startDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 1 , 0);
		String startYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, -1);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);
		
		String endDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate2, endMonth2, endYear2);
		
		startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		supportDriver.switchTo().defaultContent();
		
		String startDate3 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth3 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , -3 , 0);
		String startYear3 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate3, startMonth3, startYear3);
		
		String endDate3 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth3 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear3 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate3, endMonth3, endYear3);
		
		startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);

		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
        
        callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
        
        driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_dashboard_duration_filter-- passed ");
	}
	
	@Test(groups = { "Regression" , "SupportOnly" })
	public void verify_supervisor_dashboard_based_default_filter() {
		System.out.println("Test case --verify_supervisor_dashboard_based_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);
		
		reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		supportDriver.switchTo().defaultContent();
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameValid));
		assertTrue(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));
		reportCommonPage.clearLocationFilter(supportDriver);
		callRecordingPage.selectAccount(supportDriver, accountName);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);

		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		
		int teamNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		teamNameInvalid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, teamNameIndex);
		teamNameInvalid = reportCommonPage.getSupervisorDashboardTeamName(supportDriver, teamNameInvalid);
		supportDriver.switchTo().defaultContent();
		
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
		
		reportCommonPage.selectTeamName(supportDriver, teamNameInvalid);
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
		
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex), teamNameInvalid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex), teamNameInvalid);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex), teamNameInvalid);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_dashboard_based_default_filter-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_supervisor_dashboard_based_admin_default_filter() {
		System.out.println("Test case --verify_supervisor_dashboard_based_admin_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);
		
		reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			callRecordingPage.verifyEmptyAccountError(supportDriver);

			callRecordingPage.selectAccount(supportDriver, accountName);
		}
		
		String startDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String startMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 1 , 0);
		String startYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, -1);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate2, startMonth2, startYear2);
		
		String endDate2 = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0 , 0, 0);
		String endMonth2 = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , 0, 0);
		String endYear2 = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear,0 , 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate2, endMonth2, endYear2);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
		
		int supervisorIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Supervisor);
		
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, supervisorIndex);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, supervisorIndex);
		String supervisorName = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, supervisorIndex);
		
		int teamNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
		teamNameValid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, teamNameIndex);
		teamNameValid = reportCommonPage.getSupervisorDashboardTeamName(supportDriver, teamNameValid);
		supportDriver.switchTo().defaultContent();
		
		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		reportCommonPage.selectSupervisorName(supportDriver, supervisorName);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		callRecordingPage.isStillWaitingErrorInVisible(supportDriver);
			
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamNameIndex).contains(teamNameValid));

		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, supervisorIndex), supervisorName);

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Supervisor);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, supervisorIndex), supervisorName);

		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Supervisor);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, supervisorIndex), supervisorName);
		
		//Sort Date Created
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue,endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
        driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_dashboard_based_admin_default_filter-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_supervisor_dashboard_columns() {
		System.out.println("Test case --verify_sorting_supervisor_dashboard_columns-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//sort callKey
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3; 
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);

		//sort user id
		int userIdIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.UserId);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, userIdIndex);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, userIdIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, userIdIndex);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, userIdIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, userIdIndex);

		//sort account id
		int accountIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AccountId);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, accountIndex);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, accountIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, accountIndex);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, accountIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, accountIndex);

		//sort call owner
		int callOwnerIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallOwner);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callOwnerIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, callOwnerIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callOwnerIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, callOwnerIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callOwnerIndex);
		
		//Sort Date Created
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		
		//sort call duration
		int callDurationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallDurationSeconds);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);
		List<Float> intListAsec4 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, callDurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec4));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);
		List<Float> intListDesc4 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, callDurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc4));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);

		//sort listener name
		int listenerNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ListenerName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenerNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenerNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenerNameIndex);
		
		//sort listen Start time
		int listenStartTimeIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ListenStartTime);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenStartTimeIndex);
		List<String> listenStartTimeStringList = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenStartTimeIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, listenStartTimeStringList)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenStartTimeIndex);
		listenStartTimeStringList = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenStartTimeIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, listenStartTimeStringList)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenStartTimeIndex);

		//sort listen end time
		int listenEndTimeIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ListenEndTime);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenEndTimeIndex);
		List<String> listenEndTimeStringList = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenEndTimeIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, listenEndTimeStringList)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenEndTimeIndex);
		listenEndTimeStringList = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenEndTimeIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, listenEndTimeStringList)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenEndTimeIndex);
		
		//sort listen duration
		int listenDurationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ListenDuration);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenDurationIndex);
		List<Float> intListAsec5 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, listenDurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec5));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenDurationIndex);
		List<Float> intListDesc5 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, listenDurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc5));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, listenDurationIndex);
		
		//sort location name
		int locationNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Location);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationNameIndex);

		//sort team name
		int teamNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, teamNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, teamNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, teamNameIndex);

		//sort supervisor name
		int superVisorNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Supervisor);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, superVisorNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, superVisorNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, superVisorNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, superVisorNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, superVisorNameIndex);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_call_transfer_reports-- passed ");
	}
	
	
	//Verify no duplicate rows for call keys within Supervisor dashboard reports under Call Shadowing Details section
	@Test(groups = { "Regression" })
	public void verify_no_duplicate_call_key_in_supervisor_dashboard_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_supervisor_dashboard_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSupervisorDashboardTab(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		//verify duplicate rc key
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int listenerNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ListenerName);
		int subStringIndex = 3;
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		List<String> listenerName1 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec1, listenerName1));
		
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		List<String> listenerName2 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc1, listenerName2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		
		// click next button 3 times
		boolean flag = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
		
		//again verify duplicate rc key
		if (flag) {
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			List<String> listenerName3 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec2, listenerName3));
			
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			List<String> listenerName4 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc2, listenerName4));
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_supervisor_dashboard_report-- passed ");
	}
}
