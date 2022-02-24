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

public class CallLogs extends ReportBase {

	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	
	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private static final String inputFormat = "MM/dd/yyyy";
	private static final String outputFormat = "MM/dd/yyyy hh:mm:ss a";
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";

	String curentDate = HelperFunctions.GetCurrentDateTime("d");
	String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
	String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
	int pageCount = 3;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName 	 = CONFIG.getProperty("qa_user_account_cai_report");
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		agentNameInvalid = CONFIG.getProperty("qa_report_invalid_user_name");
	}
	
	@Test(groups = { "Regression"})
	public void verify_call_logs_report_section() {
		System.out.println("Test case --verify_call_logs_report_section-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);
		
		reportCommonPage.verifyDefaultDaysInputTab(supportDriver, inputFormat);
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
			supportDriver.switchTo().defaultContent();
			callRecordingReportPage.verifyEmptyAccountError(supportDriver);

			callRecordingReportPage.selectAccount(supportDriver, accountName);
			callRecordingReportPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingReportPage.switchToReportFrame(supportDriver);
		}
		
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);

		supportDriver.switchTo().defaultContent();
		callRecordingReportPage.selectUser(supportDriver, agentNameValid);
		callRecordingReportPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentNameValid);
		callRecordingReportPage.clickAgentToSort(supportDriver);

		//entering from number and to number
		int fromNumIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		String fromNumber = reportCommonPage.getFromNumberResult(supportDriver, fromNumIndex);
		int ToNumIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		String toNumber = reportCommonPage.getFromNumberResult(supportDriver, ToNumIndex);
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.enterFromNumber(supportDriver, fromNumber);
		reportCommonPage.enterToNumber(supportDriver, toNumber);
		callRecordingReportPage.clickRefreshButton(supportDriver);
		
		//sorting and verifying from number
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);

		//sorting and verifying to number
		assertEquals(reportCommonPage.getToNumberResult(supportDriver, ToNumIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, ToNumIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, ToNumIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);

		//Changing start date and end date
		supportDriver.switchTo().defaultContent();
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);
		callRecordingReportPage.selectAccount(supportDriver, accountName);
		
		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -2, 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);

		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, -1, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);

		startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingReportPage.clickRefreshButton(supportDriver);
		
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		supportDriver.switchTo().defaultContent();
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_logs_report_section-- passed");
	}
	
	@Test(groups = { "Regression"})
	public void verify_acces_recording_url_call_logs_reports() {
		System.out.println("Test case --verify_acces_recording_url_call_logs_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);
		callRecordingReportPage.selectUser(supportDriver, agentNameValid);
		callRecordingReportPage.clickRefreshButton(supportDriver);
	
		//clicking call recording player and verify
		int urlResultIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Url);
		reportCommonPage.clickAndVerifyActionUrl(supportDriver, urlResultIndex);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_acces_recording_url_call_logs_reports-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_call_logs_reports_download() {
		System.out.println("Test case --verify_call_logs_reports_download-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingReportPage.selectAccount(supportDriver, accountName);
			callRecordingReportPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingReportPage.switchToReportFrame(supportDriver);
		}

		callRecordingReportPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_logs_reports_download-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_support_user_able_to_access_call_logs_default_filters() {
		System.out.println("Test case --verify_support_user_able_to_access_call_logs_default_filters-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);

		//verify empty account error
		callRecordingReportPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingReportPage.verifyEmptyAccountError(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);
		callRecordingReportPage.clickRefreshButton(supportDriver);

		String sameOrgAgentName = agentNameValid;
		String invalidAgentName = HelperFunctions.GetRandomString(5);
		String otherOrgAgentName = agentNameInvalid;
		
		supportDriver.switchTo().defaultContent();
		assertTrue(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, sameOrgAgentName));
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, invalidAgentName));
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		
		callRecordingReportPage.selectUser(supportDriver, sameOrgAgentName);
		callRecordingReportPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), sameOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), sameOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), sameOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);

		supportDriver.switchTo().defaultContent();
		
		//selecting other account and verifying user name in dropdown
		assertFalse(Strings.isNullOrEmpty(callRecordingReportPage.getAgentNameInputValue(supportDriver)));
		callRecordingReportPage.selectAccount(supportDriver, accountNameOther);
		assertTrue(Strings.isNullOrEmpty(callRecordingReportPage.getAgentNameInputValue(supportDriver)));
		
		assertTrue(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(supportDriver, sameOrgAgentName));

		callRecordingReportPage.selectUser(supportDriver, otherOrgAgentName);
		callRecordingReportPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingReportPage.clickAgentToSort(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_user_able_to_access_call_logs_default_filters-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_call_logs() {
		System.out.println("Test case --verify_sorting_call_logs-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingReportPage.selectAccount(supportDriver, accountName);
		callRecordingReportPage.clickRefreshButton(supportDriver);

		//sort inbound
		int inboundIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Inboundd);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, inboundIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, inboundIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, inboundIndex);
		
		//sort dialnext
		int dialNextIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.DialNext);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dialNextIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dialNextIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dialNextIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dialNextIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dialNextIndex);
		
		//sort agent name
		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, agentNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, agentNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, agentNameIndex);

		//sort fromNumber
		int fromNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		
		//sort toNumber
		int toNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);

		//sort duration
		int durationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Duration);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, durationIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, durationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, durationIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, durationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, durationIndex);
		
		//sort status
		int statusIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Status);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, statusIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, statusIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, statusIndex);

		//sort voicemail
		int voicemailIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.VoiceMail);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, voicemailIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, voicemailIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, voicemailIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, voicemailIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, voicemailIndex);

		//sort abandoned call
		int abandonedCallIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AbandonedCall);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, abandonedCallIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, abandonedCallIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, abandonedCallIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, abandonedCallIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, abandonedCallIndex);

		//sort disposition
		int dispositionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Disposition);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dispositionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dispositionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);

		//sort callKey
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3; 
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);

		//sort id
		int idIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Id);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, idIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, idIndex);

		//sort number type
		int numberTypeIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.NumberType);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, numberTypeIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, numberTypeIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, numberTypeIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, numberTypeIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, numberTypeIndex);

		//sort source
		int sourceIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Source);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sourceIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, sourceIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sourceIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, sourceIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sourceIndex);
		
		//sort name
		int nameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Name);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, nameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, nameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, nameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, nameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, nameIndex);

		//sort duration
		int sfCampaignIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.SFCampaignId);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sfCampaignIndex);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, sfCampaignIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sfCampaignIndex);
		assertTrue(HelperFunctions.verifyDescendingOrderedList(reportCommonPage.getTableResultListAccToColumn(supportDriver, sfCampaignIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, sfCampaignIndex);

		//sort campaign
		int campaignIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CampaignName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, campaignIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, campaignIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, campaignIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, campaignIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, campaignIndex);

		//sort notes
		int notesIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Notes);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, notesIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, notesIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, notesIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, notesIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, notesIndex);

		//Sort Date Created
		int index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
		index = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, index, startDateInputValue, endDateInputValue);
				
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_call_logs-- passed ");
	}
	
	
	//Verify no duplicate rows for call keys within Call Log report
	@Test(groups = { "Regression" })
	public void verify_no_duplicate_call_key_in_call_logs_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_logs_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallLogsReport(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingReportPage.selectAccount(supportDriver, accountName);
			callRecordingReportPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingReportPage.switchToReportFrame(supportDriver);
		}
		
		//verify duplicate rc key
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3;
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertFalse(HelperFunctions.hasDuplicateItems(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		
		// click next button 3 times
		boolean flag = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
		
		//again verify duplicate rc key
		if (flag) {
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			assertFalse(HelperFunctions.hasDuplicateItems(intListAsec2));
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
			assertFalse(HelperFunctions.hasDuplicateItems(intListDesc2));
			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_logs_report-- passed ");
	}
}
