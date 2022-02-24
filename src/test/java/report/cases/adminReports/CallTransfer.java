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
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallTransfer extends ReportBase{

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	
	private String accountName;
	private String accountNameOther;
	private String agentNameInvalid;
	private static final String outputFormat = "MM/dd/yyyy HH:mm:ss";
	private static final String inputFormat = "MM/dd/yyyy";

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

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_user_name_results_for_same_and_different_accounts() {
		
		System.out.println("Test case --verify_user_name_results_for_same_and_different_accounts-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String agentName = agentNameValid;
		String otherOrgAgentName = agentNameInvalid;
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallTransferReport(supportDriver);

		//verify account error if support role
		callRecordingPage.clickRefreshWithoutSwitchingToFrame(supportDriver);
		callRecordingPage.verifyEmptyAccountError(supportDriver);	
		
		//verify data for same account, user
		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);
		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		agentName = reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex);
		supportDriver.switchTo().defaultContent();
		
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		
		callRecordingPage.selectUser(supportDriver, agentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		supportDriver.switchTo().defaultContent();
		
		//selecting other account and verifying user name in dropdown
		assertFalse(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		assertTrue(Strings.isNullOrEmpty(callRecordingPage.getAgentNameInputValue(supportDriver)));
		
		assertTrue(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, otherOrgAgentName));
		assertFalse(callRecordingPage.verifyAgentExistsInDropDown(supportDriver, agentName));

		callRecordingPage.selectUser(supportDriver, otherOrgAgentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		//verifying data for agent
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), otherOrgAgentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		
		driverUsed.put("supportDriver", false);		
		System.out.println("Test case --verify_user_name_results_for_same_and_different_accounts-- passed ");
	}
	
	@Test(groups="Regression")
	public void verify_filter_results_for_different_sections() {
		
		System.out.println("Test case --verify_filter_results_for_different_sections-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallTransferReport(supportDriver);

		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}

		int agentIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		String agentName = reportCommonPage.iterateListForNonEmptyResult(supportDriver, agentIndex);
		
		supportDriver.switchTo().defaultContent();
		callRecordingPage.selectUser(supportDriver, agentName);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int dispositionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Disposition);
		String callDisposition = reportCommonPage.iterateListForNonEmptyResult(supportDriver, dispositionIndex);
		supportDriver.switchTo().defaultContent();
		
		reportCommonPage.enterDisposition(supportDriver, callDisposition);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		int fromNumIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		String fromNumber = reportCommonPage.getFromNumberResult(supportDriver, fromNumIndex);
		
		int ToNumIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		String toNumber = reportCommonPage.getToNumberResult(supportDriver, ToNumIndex);
		
		int directionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Direction);
		String direction = reportCommonPage.getDirectionResult(supportDriver, directionIndex);
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.enterFromNumber(supportDriver, fromNumber);
		reportCommonPage.enterToNumber(supportDriver, toNumber);
		callRecordingPage.selectDirectionFilter(supportDriver, direction);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		if (Strings.isNullOrEmpty(callDisposition)) {
			callDisposition = "-";
		}

		// verifying data
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, dispositionIndex), callDisposition);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, dispositionIndex), callDisposition);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, dispositionIndex), callDisposition);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);

		int agentNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);
		assertEquals(reportCommonPage.getAgentNameResult(supportDriver, agentNameIndex), agentName);
		callRecordingPage.clickAgentToSort(supportDriver);

		int fromNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumberIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumberIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getFromNumberResult(supportDriver, fromNumberIndex), fromNumber);
		reportCommonPage.clickFromNumberToSort(supportDriver);

		int toNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		assertEquals(reportCommonPage.getToNumberResult(supportDriver, toNumberIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getToNumberResult(supportDriver, toNumberIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);
		assertEquals(reportCommonPage.getToNumberResult(supportDriver, toNumberIndex), toNumber);
		reportCommonPage.clickToNumberToSort(supportDriver);
		
		directionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Direction);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, directionIndex), direction);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Direction);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, directionIndex), direction);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Direction);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, directionIndex), direction);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.Direction);
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.refreshCurrentDocument(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
		int transferToNumIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferToNumber);
		String transferToNumber = reportCommonPage.iterateListForNonEmptyResult(supportDriver,transferToNumIndex);
		
		if(Strings.isNullOrEmpty(transferToNumber)) {
			transferToNumber = "-";
		
			supportDriver.switchTo().defaultContent();
			reportCommonPage.enterTransferToNumber(supportDriver, transferToNumber);
			
			callRecordingPage.clickRefreshButton(supportDriver);
			transferToNumber = reportCommonPage.iterateListForNonEmptyResult(supportDriver, transferToNumIndex);
		}
		
		supportDriver.switchTo().defaultContent();
		reportCommonPage.enterTransferToNumber(supportDriver, transferToNumber);
		callRecordingPage.clickRefreshButton(supportDriver);

		int transferNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferToNumber);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, transferNumberIndex),transferToNumber);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, transferNumberIndex),transferToNumber);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);
		assertEquals(reportCommonPage.getFirstResultTableAccToColumn(supportDriver, transferNumberIndex),transferToNumber);
		reportCommonPage.clickHeaderToSort(supportDriver, HeaderNames.TransferToNumber);

		supportDriver.switchTo().defaultContent();
		
		String startDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String startYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", startDate, startMonth, startYear);

		String endDate = HelperFunctions.addMonthYearDateToExisting("d", curentDate, 0, 0, 0);
		String endMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth, 0, 0, 0);
		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, 0);
		reportCommonPage.selectStartEndDate(supportDriver, "End", endDate, endMonth, endYear);

		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.clickRefreshButton(supportDriver);

		int startDateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.StartDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, startDateIndex, startDateInputValue, endDateInputValue);
		int endDateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.EndDate);
		reportCommonPage.verifyDateSortedCustom(supportDriver, inputFormat, outputFormat, endDateIndex, startDateInputValue, endDateInputValue);

		supportDriver.switchTo().defaultContent();
		
		driverUsed.put("supportDriver", false);		
		System.out.println("Test case --verify_filter_results_for_different_sections-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void verify_sorting_call_transfer_reports() {
		System.out.println("Test case --verify_sorting_call_transfer_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallTransferReport(supportDriver);
		
		String startDateInputValue = reportCommonPage.getStartDateInputTab(supportDriver);
		String endDateInputValue = reportCommonPage.getEndDateInputTab(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountName);
		callRecordingPage.clickRefreshButton(supportDriver);

		//sort from user name
		int fromUserNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromUserNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromUserNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromUserNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromUserNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromUserNameIndex);

		//sort fromNumber
		int fromNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.FromNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		List<Float> intListAsec = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		List<Float> intListDesc = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, fromNumberIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, fromNumberIndex);
		
		//sort toNumber
		int toNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.ToNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);
		List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, toNumberIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc2));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, toNumberIndex);

		//sort transfer to number
		int transferToNumberIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferToNumber);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToNumberIndex);
		List<Float> intListAsec3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferToNumberIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToNumberIndex);
		List<Float> intListDesc3 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferToNumberIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc3));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToNumberIndex);

		//sort transfer to user name
		int transferToUserNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferToUsername);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToUserNameIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferToUserNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToUserNameIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferToUserNameIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferToUserNameIndex);

		//sort call duration
		int callDurationIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallDuration);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);
		List<Float> intListAsec4 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, callDurationIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec4));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);
		List<Float> intListDesc4 = HelperFunctions.getStringListInNumberFormat(reportCommonPage.getTableResultListAccToColumn(supportDriver, callDurationIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc4));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callDurationIndex);
		
		//sort direction
		int directionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.Direction);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, directionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, directionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, directionIndex);

		//sort call state
		int callStateIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallState);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callStateIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, callStateIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callStateIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, callStateIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callStateIndex);

		//sort callKey
		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
		int subStringIndex = 3; 
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListAsec5 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyAscendingOrderedList(intListAsec5));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		List<Float> intListDesc5 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
		assertTrue(HelperFunctions.verifyDescendingOrderedList(intListDesc5));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);

		//sort disposition
		int dispositionIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallDisposition);
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dispositionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(reportCommonPage.getTableResultListAccToColumn(supportDriver, dispositionIndex)));
		reportCommonPage.clickHeaderToSortByIndex(supportDriver, dispositionIndex);

		//sort notes
		int notesIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallNotes);
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
		System.out.println("Test case --verify_sorting_call_transfer_reports-- passed ");
	}
	
	
	//Verify no duplicate rows for call keys within Call Transfer reports
	@Test(groups = { "Regression" })
	public void verify_no_duplicate_call_key_in_call_transfer_report() {
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_transfer_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallTransferReport(supportDriver);
		
		if (ReportBase.drivername.equals("supportDriver")) {
			callRecordingPage.selectAccount(supportDriver, accountName);
			callRecordingPage.clickRefreshButton(supportDriver);
		}
		
		if (ReportBase.drivername.toString().equals("adminDriver") || ReportBase.drivername.toString().equals("ringDNAProdDriver")) {
			callRecordingPage.switchToReportFrame(supportDriver);
		}
		
//		//verify duplicate rc key
//		int callKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.CallKey);
//		int listenerNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
//		int subStringIndex = 3;
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		List<Float> intListAsec1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//		List<String> listenerName1 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
//		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec1, listenerName1));
//		
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		List<Float> intListDesc1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//		List<String> listenerName2 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
//		assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc1, listenerName2));
//		reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
		
//		// click next button 3 times
//		boolean flag = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
//		
//		//again verify duplicate rc key
//		if (flag) {
//			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//			List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//			List<String> listenerName3 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
//			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec2, listenerName3));
//			
//			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//			List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, callKeyIndex), subStringIndex));
//			List<String> listenerName4 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
//			assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc2, listenerName4));
//			reportCommonPage.clickHeaderToSortByIndex(supportDriver, callKeyIndex);
//		}
		
		//verify duplicate rc key transferred from call key
				int transferredFromCallKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferredFromCallKey);
				int listenerNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
				int subStringIndex = 3;
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
				List<Float> intListAsec1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredFromCallKeyIndex), subStringIndex));
				List<String> listenerName1 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
				assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec1, listenerName1));
				
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
				List<Float> intListDesc1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredFromCallKeyIndex), subStringIndex));
				List<String> listenerName2 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
				assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc1, listenerName2));
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
				
				
				// click next button 3 times
				boolean flag = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
				
				//again verify duplicate rc key
				if (flag) {
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
					List<Float> intListAsec2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredFromCallKeyIndex), subStringIndex));
					List<String> listenerName3 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
					assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsec2, listenerName3));
					
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
					List<Float> intListDesc2 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredFromCallKeyIndex), subStringIndex));
					List<String> listenerName4 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
					assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDesc2, listenerName4));
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredFromCallKeyIndex);
				}
				
				
				supportDriver.switchTo().defaultContent();
				supportDriver.navigate().refresh();
				callRecordingPage.selectAccount(supportDriver, accountName);
				callRecordingPage.clickRefreshButton(supportDriver);
				
				//verify duplicate rc key transferred to call key
				int transferredToCallKeyIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.TransferredToCallKey);
			//	int listenerNameIndex = reportCommonPage.getIndexNoByHeaderName(supportDriver, HeaderNames.AgentName);
			//	int subStringIndex = 3;
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
				List<Float> intListAsecTo = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredToCallKeyIndex), subStringIndex));
				List<String> listenerNameTo = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
				assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsecTo, listenerNameTo));
				
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
				List<Float> intListDescTo = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredToCallKeyIndex), subStringIndex));
				List<String> listenerNameTo2 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
				assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDescTo, listenerNameTo2));
				reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
				
				// click next button 3 times
				boolean flag1 = reportCommonPage.navigateToNextPage(supportDriver, pageCount);
				
				//again verify duplicate rc key
				if (flag1) {
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
					List<Float> intListAsecTo1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredToCallKeyIndex), subStringIndex));
					List<String> listenerNameTo3 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
					assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListAsecTo1, listenerNameTo3));
					
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
					List<Float> intListDescTo1 = HelperFunctions.getStringListInNumberFormat(HelperFunctions.getSubstringFromWholeList(reportCommonPage.getTableResultListAccToColumn(supportDriver, transferredToCallKeyIndex), subStringIndex));
					List<String> listenerNameTo4 = reportCommonPage.getTableResultListAccToColumn(supportDriver, listenerNameIndex);
					assertFalse(reportCommonPage.isDuplicateCallKeyPresent(supportDriver, intListDescTo1, listenerNameTo4));
					reportCommonPage.clickHeaderToSortByIndex(supportDriver, transferredToCallKeyIndex);
				}
				
				

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_duplicate_call_key_in_call_transfer_report-- passed ");
	}
}

