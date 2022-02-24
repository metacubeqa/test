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
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class UserBusyPresenceSummary extends ReportBase {
	
	Dashboard dashboard = new Dashboard();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";

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
	
	@Test(groups = { "Regression" , "SupportOnly"})
	public void verify_user_presence_summary_by_default_filter() {
		System.out.println("Test case --verify_user_presence_summary_by_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);

		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameValid));
        assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentNameInvalid));
        callRecordingPage.selectUser(supportDriver, agentNameInvalid);
        
        callRecordingPage.clickRefreshButton(supportDriver);
        
        int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
        assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameInvalid);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameInvalid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		
		supportDriver.switchTo().defaultContent();
        assertFalse(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameValid));
		assertTrue(reportCommonPage.verifyLocationExistsInDropDown(supportDriver, locationNameInvalid));
		reportCommonPage.selectLocation(supportDriver, locationNameInvalid);
		
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameValid));
		assertTrue(reportCommonPage.verifyTeamNameExistsInDropDown(supportDriver, teamNameInvalid));
		reportCommonPage.selectTeamName(supportDriver, teamNameInvalid);
		
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountName);
		assertTrue(Strings.isNullOrEmpty(reportCommonPage.getUserNamefieldValue(supportDriver)));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_presence_summary_by_default_filter-- passed ");
		
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_user_presence_summary_admin_default_filter() {
		System.out.println("Test case --verify_user_presence_summary_admin_default_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);

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
        
		callRecordingPage.clickRefreshButton(supportDriver);
		        
		agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid2);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
				
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectUser(supportDriver, agentNameValid);
		
		callRecordingPage.clickRefreshButton(supportDriver);
		
		agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), agentNameValid);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_presence_summary_admin_default_filter-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_user_presence_summary_with_location_filter() {
		System.out.println("Test case --verify_user_presence_summary_with_location_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
		
		if (ReportBase.drivername.toString().equals("supportDriver")) {
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
		System.out.println("Test case --verify_user_presence_summary_with_location_filter-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_presence_summary_by_userName_filter() {
		System.out.println("Test case --verify_user_presence_summary_by_userName_filter-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
		
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
		String userName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentIndex);
		supportDriver.switchTo().defaultContent();
        callRecordingPage.selectUser(supportDriver, userName);
        
        callRecordingPage.clickRefreshButton(supportDriver);
        
        assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), userName);
        reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), userName);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentIndex), userName);
		
		callRecordingPage.scrollTillEndOfPage(supportDriver);
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_presence_summary_by_userName_filter-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_user_summary() {
		System.out.println("Test case --verify_sorting_user_summary-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);
		
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
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, locationIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, locationIndex)));
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
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_user_summary-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_user_presence_summary_filter_by_team() {
		System.out.println("Test case --verify_user_presence_summary_filter_by_team-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserBusyPresenceSummary(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		int teamIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Team);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		String teamNameValid = reportCommonPage.iterateListForNonEmptyResult(supportDriver, teamIndex);
		teamNameValid = reportCommonPage.getSupervisorDashboardTeamName(supportDriver, teamNameValid);
		supportDriver.switchTo().defaultContent();

		reportCommonPage.selectTeamName(supportDriver, teamNameValid);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex).contains(teamNameValid));
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex).contains(teamNameValid));
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Team);
		assertTrue(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, teamIndex).contains(teamNameValid));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_presence_summary_filter_by_team-- passed ");
	}
}
