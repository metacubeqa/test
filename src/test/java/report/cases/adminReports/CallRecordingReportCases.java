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
import softphone.source.salesforce.TaskDetailPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.callRecordings.CallRecordingReportPage.CallType;
import support.source.callRecordings.CallRecordingReportPage.Direction;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallRecordingReportCases extends ReportBase {
	
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	
	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private String qaUrl;
	private static final String format = "MMMM d, yyyy, h:mm a";
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	private static int dateCreatedColumnIndex = 0;
	private static int agentColumnIndex = 0;
	int pageCount = 3;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName 	 = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
		qaUrl            = CONFIG.getProperty("qa_support_tool_site");
	}
	
	@Test(groups = { "Regression"})
	public void verify_time_filters_call_recordings_reports(){
		System.out.println("Test case --verify_time_filters_call_recordings_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);
		
		callRecordingPage.selectAccount(supportDriver, accountName);

		//For week
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Week);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		dateCreatedColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, dateCreatedColumnIndex);
		
		//For 30 days
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, format, dateCreatedColumnIndex);
		
		// For 90 days
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, format, dateCreatedColumnIndex);
		
		// For last year
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.clickRefreshButton(supportDriver);

		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, dateCreatedColumnIndex);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_time_filters_call_recordings_reports-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_user_redirected_to_call_player_when_clicked_on_url() {
		System.out.println("Test case --verify_user_redirected_to_call_player_when_clicked_on_url-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		String user = callRecordingPage.getAgentName(supportDriver);
		
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, user);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		assertTrue(callRecordingPage.getURL(supportDriver).contains(qaUrl+"/#call-player"));
		callRecordingPage.clickActionsUrlToSort(supportDriver);
		assertTrue(callRecordingPage.getURL(supportDriver).contains(qaUrl+"/#call-player"));
		callRecordingPage.clickAccountToSort(supportDriver);
		assertTrue(callRecordingPage.getURL(supportDriver).contains(qaUrl+"/#call-player"));
		
		callRecordingPage.clickAndVerifyActionUrl(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_redirected_to_call_player_when_clicked_on_url-- passed ");
	}

	@Test(groups = { "Regression", "ExcludeRingDNAProd"})
	public void verify_user_redirected_to_sfdc_task_when_clicked_on_sfdc_task_url() {
		System.out.println("Test case --verify_user_redirected_to_sfdc_task_when_clicked_on_sfdc_task_url-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		String direction = callRecordingPage.getDirection(supportDriver);
		
		agentColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		agentNameValid = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentColumnIndex);
		
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, agentNameValid);
		callRecordingPage.selectDirectionFilter(supportDriver, direction);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//finding url latest by descending order
		callRecordingPage.clickDateCreatedToSort(supportDriver);
		callRecordingPage.clickDateCreatedToSort(supportDriver);
		
		String identifierId =  String.valueOf(HelperFunctions.getNumberFromString(callRecordingPage.getURL(supportDriver)));
		callRecordingPage.clickAndVerifySFTaskUrl(supportDriver, agentNameValid, direction, identifierId);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_redirected_to_sfdc_task_when_clicked_on_sfdc_task_url-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_able_to_search_reports_based_on_name_type_direction_duration() {
		System.out.println(
				"Test case --verify_user_able_to_search_reports_based_on_name_type_direction_duration-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		String agentName = agentNameValid;

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectUser(supportDriver, agentName);
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Inbound.getValue());
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);

		String callerName = callRecordingPage.getCallerName(supportDriver);

		supportDriver.switchTo().defaultContent();

		callRecordingPage.setCallerName(supportDriver, callerName);
		callRecordingPage.clickRefreshButton(supportDriver);

		// verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		// Sort Direction
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);

		// Sort Date Created 30 days
		dateCreatedColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, format, dateCreatedColumnIndex);

		List<String> callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_search_reports_based_on_name_type_direction_duration-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_call_recordings_reports_for_default_filters() {
		System.out.println("Test case --verify_call_recordings_reports_for_default_filters-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.verifyDefaultFields(supportDriver);
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		// Sort Account name
		if (ReportBase.drivername.toString().equals("supportDriver")) {
			assertEquals(callRecordingPage.getAccountName(supportDriver), accountName);
			callRecordingPage.clickAccountToSort(supportDriver);
			assertEquals(callRecordingPage.getAccountName(supportDriver), accountName);
			callRecordingPage.clickAccountToSort(supportDriver);
			assertEquals(callRecordingPage.getAccountName(supportDriver), accountName);
			callRecordingPage.clickAccountToSort(supportDriver);
		}

		// Sort Direction
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Outbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
	
		// Sort Date Created 7 days
		dateCreatedColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Week, format, dateCreatedColumnIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_recordings_reports_for_default_filters-- passed ");
	}

	@Test(groups = { "Regression"})
	public void verify_user_reports_download() {
		System.out.println("Test case --verify_user_reports_download-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		callRecordingPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_reports_download-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_on_columns() {
		System.out.println("Test case --verify_sorting_on_columns-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//sorting on agent
		callRecordingPage.clickAgentToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(callRecordingPage.getAgentNameList(supportDriver)));
		callRecordingPage.clickAgentToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(callRecordingPage.getAgentNameList(supportDriver)));
		callRecordingPage.clickAgentToSort(supportDriver);
		
		//sorting on call key
		int subStringIndex = 3; 
		callRecordingPage.clickCallKeyToSort(supportDriver);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(callRecordingPage.getCallKeyList(supportDriver), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		callRecordingPage.clickCallKeyToSort(supportDriver);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(callRecordingPage.getCallKeyList(supportDriver), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		callRecordingPage.clickCallKeyToSort(supportDriver);
		
		//sorting on caller
		callRecordingPage.clickCallerToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(callRecordingPage.getCallerNameList(supportDriver)));
		callRecordingPage.clickCallerToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(callRecordingPage.getCallerNameList(supportDriver)));
		callRecordingPage.clickCallerToSort(supportDriver);
		
		//sorting on sf task url
		callRecordingPage.clickSFTaskUrlToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(callRecordingPage.getSFTaskUrlList(supportDriver)));
		callRecordingPage.clickSFTaskUrlToSort(supportDriver);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(callRecordingPage.getSFTaskUrlList(supportDriver)));
		callRecordingPage.clickSFTaskUrlToSort(supportDriver);
		
		//sorting on actions url
		callRecordingPage.clickActionsUrlToSort(supportDriver);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(callRecordingPage.getActionsUrlList(supportDriver)));
		callRecordingPage.clickActionsUrlToSort(supportDriver);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(callRecordingPage.getActionsUrlList(supportDriver)));
		callRecordingPage.clickActionsUrlToSort(supportDriver);
		
		//sorting on direction
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(callRecordingPage.getDirectionList(supportDriver)));
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(callRecordingPage.getDirectionList(supportDriver)));
		callRecordingPage.clickDirectionToSort(supportDriver);
		
		//sorting on isAvailable
		callRecordingPage.clickIsAvailableToSort(supportDriver);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(callRecordingPage.getIsAvailableList(supportDriver)));
		callRecordingPage.clickIsAvailableToSort(supportDriver);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(callRecordingPage.getIsAvailableList(supportDriver)));
		callRecordingPage.clickIsAvailableToSort(supportDriver);
		
		//sorting on isNew
		callRecordingPage.clickIsNewToSort(supportDriver);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(callRecordingPage.getIsNewList(supportDriver)));
		callRecordingPage.clickIsNewToSort(supportDriver);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(callRecordingPage.getIsNewList(supportDriver)));
		callRecordingPage.clickIsNewToSort(supportDriver);
		
		//Sort Date Created Annual
		dateCreatedColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, dateCreatedColumnIndex);
				
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_on_columns-- passed ");
	}
	
    @Test(groups = { "Regression", "SupportOnly" })
	public void verify_support_user_able_to_access_different_account_data() {
		System.out.println("Test case --verify_support_user_able_to_access_different_account_data-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		//verify empty account error
		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);
	
		agentColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentColumnIndex);
		String sameOrgAgentName = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentColumnIndex);
		String invalidAgentName = HelperFunctions.GetRandomString(5);
		String otherOrgAgentName = agentNameInvalid;
		
		supportDriver.switchTo().defaultContent();
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, sameOrgAgentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, invalidAgentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		
		callRecordingPage.selectUser(supportDriver, sameOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		supportDriver.switchTo().defaultContent();
		
		//selecting other account and verifying user name in dropdown
		assertFalse(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		assertTrue(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
		
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, sameOrgAgentName));

		callRecordingPage.selectUser(supportDriver, otherOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_user_able_to_access_different_account_data-- passed ");
	}
    
	@Test(groups = { "Regression", "ExcludeRingDNAProd" })
	public void verify_call_type_filter_call_recording_report() {
		System.out.println("Test case --verify_call_type_filter_call_recording_report-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.verifyFieldsCallType(supportDriver);
		
		//Voicemail filter
		callRecordingPage.selectCallTypeFilter(supportDriver, CallType.Voicemail);
		callRecordingPage.clickRefreshButton(supportDriver);

		// Sort Direction
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);

		callRecordingPage.verifySFDCVoiceMailFilter(supportDriver, CallType.Voicemail);
		
		//Non Voicemail filter
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectCallTypeFilter(supportDriver, CallType.NonVoicemail);
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Inbound.getValue());
		callRecordingPage.clickRefreshButton(supportDriver);

		// Sort Direction
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);

		callRecordingPage.verifySFDCVoiceMailFilter(supportDriver, CallType.NonVoicemail);		
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_type_filter_call_recording_report-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_direction_filter_call_recording_report() {
		System.out.println("Test case --verify_direction_filter_call_recording_report-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.verifyFieldsDirection(supportDriver);
		
		//Direction filter
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Inbound.name());
		callRecordingPage.clickRefreshButton(supportDriver);

		// Sort Direction
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);

		supportDriver.switchTo().defaultContent();
		
		//Direction filter
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Outbound.name());
		callRecordingPage.clickRefreshButton(supportDriver);

		// Sort Direction
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Outbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Outbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Outbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_direction_filter_call_recording_report-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_call_recording_report_for_all_filter_combination() {
		System.out.println("Test case --verify_call_recording_report_for_all_filter_combination-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Annual);
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Inbound.name());
		callRecordingPage.selectCallTypeFilter(supportDriver, CallType.NonVoicemail);
		callRecordingPage.clickRefreshButton(supportDriver);

		agentColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentColumnIndex);
		String sameOrgAgentName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentColumnIndex);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, sameOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		String callerName = callRecordingPage.getCallerName(supportDriver);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.setCallerName(supportDriver, callerName);
		callRecordingPage.clickRefreshButton(supportDriver);

		// verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		// Sort Date Created Annual
		dateCreatedColumnIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Annual, format, dateCreatedColumnIndex);

		// verifying data for caller name
		List<String> callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCase = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCase.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);

		supportDriver.switchTo().defaultContent();
		supportDriver.navigate().refresh();
		
		dashboard.isPaceBarInvisible(supportDriver);
		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Inbound.name());
		callRecordingPage.selectCallTypeFilter(supportDriver, CallType.Voicemail);
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Last90days);
		callRecordingPage.clickRefreshButton(supportDriver);

		sameOrgAgentName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentColumnIndex);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, sameOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		callerName = callRecordingPage.getCallerName(supportDriver);
		supportDriver.switchTo().defaultContent();
		callRecordingPage.setCallerName(supportDriver, callerName);
		callRecordingPage.clickRefreshButton(supportDriver);

		// Sort Direction
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);
		assertEquals(callRecordingPage.getDirection(supportDriver), Direction.Inbound.name());
		callRecordingPage.clickDirectionToSort(supportDriver);

		// verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		// Sort Date Created Annual
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Last90days, format, dateCreatedColumnIndex);

		// verifying data for caller name
		List<String> callerListUpperCaseOther = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCaseOther.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCaseOther = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCaseOther.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		callerListUpperCaseOther = callRecordingPage.convertListToUpperCase(callRecordingPage.getCallerNameList(supportDriver));
		assertEquals(callerListUpperCaseOther.get(0), callerName.toUpperCase());
		callRecordingPage.clickAgentToSort(supportDriver);
		
		supportDriver.switchTo().defaultContent();

		// Direction filter
		callRecordingPage.selectDirectionFilter(supportDriver, Direction.Outbound.name());
		callRecordingPage.clickRefreshButtonWithoutAssertion(supportDriver);
		
		// verifying no result image and text
		reportCommonPage.verifyNoResultTextImage(supportDriver);
		
		supportDriver.switchTo().defaultContent();
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.selectCallTypeFilter(supportDriver, CallType.NonVoicemail);
		callRecordingPage.selectCreatedDateFilter(supportDriver, reportDuration.Month);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		sameOrgAgentName = reportCommonPage.getFirstResultTableAccToColumn(supportDriver, agentColumnIndex);

		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, sameOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		// verifying data for agent
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(callRecordingPage.getAgentName(supportDriver), sameOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		
		// Sort Date Created Month
		callRecordingPage.sortAndVerifyDateCreated(supportDriver, reportDuration.Month, format, dateCreatedColumnIndex);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_recording_report_for_all_filter_combination-- passed ");
	}
	
	//Verify no duplicate rows for call keys within Call recording reports
	@Test(groups = { "Regression", "ExcludeRingDNAProd" })
	public void verify_no_duplicate_call_key_in_call_recording_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_recording_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickCallRecording(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		//verify duplicate rc key
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3;
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		List<String> listenerName1 = callRecordingPage.getCallerNameList(supportDriver);
		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec1, listenerName1));
		
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		List<String> listenerName2 = callRecordingPage.getCallerNameList(supportDriver);
		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc1, listenerName2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		
		// click next button 3 times
		boolean flag = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
		
		//again verify duplicate rc key
		if (flag) {
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			List<String> listenerName3 = callRecordingPage.getCallerNameList(supportDriver);
			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec2, listenerName3));
			
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			List<String> listenerName4 = callRecordingPage.getCallerNameList(supportDriver);
			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc2, listenerName4));
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_recording_report-- passed ");
	}
}
