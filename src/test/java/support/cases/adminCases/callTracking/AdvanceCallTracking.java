package support.cases.adminCases.callTracking;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.HashMap;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.admin.AdminCallTracking;
import support.source.admin.AdminCallTracking.trackingNoFields;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class AdvanceCallTracking extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	HelperFunctions helperFunctions = new HelperFunctions();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	AddSmartNumberPage addsmartNoPage = new AddSmartNumberPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String advanceFileNameDownload = "advanced-export";
	
	@Test(groups = { "Regression" })
	public void add_and_verify_advanced_tracking_number() {
		System.out.println("Test case --add_and_verify_advanced_tracking_number-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Advance Call Tracking page
		dashboard.openAdvanceCallTrackingPage(supportDriver);

		// Verifying the mandatory fields
		adminCallTracking.verifyMandatoryFields(supportDriver);

		// Adding advacnce call tracking number of Toll Free type
		String trackingName = "AutoTrackNumber " + helperFunctions.GetCurrentDateTime();
		String callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.Name, trackingName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		adminCallTracking.clickAddNewAdvTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData);
		adminCallTracking.clickFinishButton(supportDriver);

		// Deleting and verifying download report
		HelperFunctions.deletingExistingFiles(downloadPath, advanceFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		boolean fileDownloaded = adminCallTracking.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, advanceFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");

		// getting the assigned smart number to advanced tracking number
		int trackingNumberIndex = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName, callFlowName);
		assertTrue(trackingNumberIndex >= 0);
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex);

		// Verifying added smart is available on smart number tabs and it is active
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, advTrackingNumberData.get(trackingNoFields.CustomParameterValue), trackingNumber);
		assertTrue(smartNoIndex >= 0);

		// Deleting smart Number
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		// verifying that smart number has been deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		assertEquals(smartNumbersPage.getStatus(supportDriver, smartNoIndex),
				SmartNumbersPage.smartNumberStatus.Deleted.toString());

		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if (callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --add_and_verify_advanced_tracking_number-- passed ");
	}

	@Test(groups = { "Regression", "Product Sanity"})
	public void add_local_type_advance_tracking_no() {
		System.out.println("Test case --add_local_type_advance_tracking_no-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Advance Call Tracking page
		dashboard.openAdvanceCallTrackingPage(supportDriver);

		String trackingName = "AutoLocalNumber " + helperFunctions.GetCurrentDateTime();
		String callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		
		// Adding advacnce call tracking number of Toll Free type
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.Name, trackingName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		adminCallTracking.clickAddNewAdvTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData);
		adminCallTracking.clickFinishButton(supportDriver);

		// Verifying that tracking number has been added
		adminCallTracking.searchAdvCallTrackNumber(supportDriver, trackingName, callFlowName);

		// getting the assigned smart number to advanced tracking number
		int trackingNumberIndex = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName, callFlowName);
		assertTrue(trackingNumberIndex >= 0);
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex);

		// Verifying added smart is available on smart number tabs and it is active
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, advTrackingNumberData.get(trackingNoFields.CustomParameterValue), trackingNumber);
		assertTrue(smartNoIndex >= 0);

		// opening overview tab and reassigning tracking to central number
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		String centralLabelName = "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(2));
		addsmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, centralLabelName, null, null);
		boolean centralReassignedNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, trackingNumber);
		assertTrue(centralReassignedNoExists,
				String.format("Central number :%s does not exists after reassigning", trackingNumber));

		// Deleting smart Number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		// verifying that smart number has been deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		assertEquals(smartNumbersPage.getStatus(supportDriver, smartNoIndex),
				SmartNumbersPage.smartNumberStatus.Deleted.toString());

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_local_type_advance_tracking_no-- passed ");
	}

	@Test(groups = { "Regression"})
	public void add_multiple_advance_tracking_no() {
		System.out.println("Test case --add_multiple_advance_tracking_no-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Advance Call Tracking page
		dashboard.openAdvanceCallTrackingPage(supportDriver);

		String trackingName1 = "AutoLocalNumber " + helperFunctions.GetCurrentDateTime();
		String trackingName2 = "AutoTollNumber " + helperFunctions.GetCurrentDateTime();
		String callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		String campaignName = CONFIG.getProperty("softphone_task_related_campaign");

		// Adding advacnce call tracking number of Toll Free type
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData1 = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.Name, trackingName1);
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		advTrackingNumberData1.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.clickAddNewAdvTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData1);

		// Verifying remove buttons
		adminCallTracking.isRemoveButtonInvisible(supportDriver);
		adminCallTracking.clickAddAddtionalNumberLink(supportDriver);
		assertEquals(adminCallTracking.getRemoveButtonsCount(supportDriver), 2);
		adminCallTracking.clickRemoveButton(supportDriver, 0);
		adminCallTracking.isRemoveButtonInvisible(supportDriver);

		// Adding advacnce call tracking number of Toll Free type
		adminCallTracking.clickAddAddtionalNumberLink(supportDriver);
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData2 = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData2.put(AdminCallTracking.trackingNoFields.Name, trackingName2);
		advTrackingNumberData2.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData2.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		advTrackingNumberData2.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData2);
		adminCallTracking.clickFinishButton(supportDriver);

		// verifying and getting the assigned smart numbers to advanced tracking number
		adminCallTracking.searchAdvCallTrackNumber(supportDriver, trackingName1, callFlowName);
		int trackingNumberIndex1 = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName1, callFlowName);
		assertTrue(trackingNumberIndex1 >= 0);
		String trackingNumber1 = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex1);

		adminCallTracking.searchAdvCallTrackNumber(supportDriver, trackingName2, callFlowName);
		int trackingNumberIndex2 = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName2, callFlowName);
		assertTrue(trackingNumberIndex2 >= 0);
		String trackingNumber2 = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex2);

		// Deleting smart Number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber1);
		int smartNoIndex1 = smartNumbersPage.getSmartNumbersIndex(supportDriver, advTrackingNumberData1.get(trackingNoFields.CustomParameterValue), trackingNumber1);
		assertTrue(smartNoIndex1 >= 0);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex1);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber2);
		int smartNoIndex2 = smartNumbersPage.getSmartNumbersIndex(supportDriver, advTrackingNumberData2.get(trackingNoFields.CustomParameterValue), trackingNumber2);
		assertTrue(smartNoIndex2 >= 0);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex2);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		// verifying that smart number has been deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber1);
		assertEquals(smartNumbersPage.getStatus(supportDriver, smartNoIndex1), SmartNumbersPage.smartNumberStatus.Deleted.toString());

		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber2);
		assertEquals(smartNumbersPage.getStatus(supportDriver, smartNoIndex2), SmartNumbersPage.smartNumberStatus.Deleted.toString());

		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if(callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber1)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber1);
		}
		if(callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber2)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber2);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --add_multiple_advance_tracking_no-- passed ");
	}

	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_add_icon_enable_disable_for_advance_tracking_for_same_and_diff_account() {
		System.out.println("Test case --verify_add_icon_enable_disable_for_advance_tracking_for_same_and_diff_account-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Advance Call Tracking page
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openAdvanceCallTrackingPage(supportDriver);
		assertEquals(adminCallTracking.getAccountName(supportDriver), CONFIG.getProperty("qa_user_account"));
		assertFalse(adminCallTracking.isAdvancedAddIconDisabled(supportDriver));
		
		//selecting diff account
		adminCallTracking.selectAccount(supportDriver, "QA v3");
		assertTrue(adminCallTracking.isAdvancedAddIconDisabled(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_icon_enable_disable_for_advance_tracking_for_same_and_diff_account-- passed ");
	}
	
	//Verify Admin users should be able to create advance call tracking using existing unassigned smart number
	@Test(groups = { "Regression" })
	public void verify_user_able_to_create_advance_call_tracking_existing_smart_number() {
		System.out.println("Test case --verify_user_able_to_create_advance_call_tracking_existing_smart_number-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Advance Call Tracking page
		dashboard.openAdvanceCallTrackingPage(supportDriver);

		// Verifying the mandatory fields
		adminCallTracking.verifyMandatoryFields(supportDriver);

		// Adding advacnce call tracking number of Toll Free type
		String trackingName = "AutoTrackNumber " + helperFunctions.GetCurrentDateTime();
		String callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.Name, trackingName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.UseExisting.displayName());
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		adminCallTracking.clickAddNewAdvTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData);
		adminCallTracking.clickFinishButton(supportDriver);
		
		// getting the assigned smart number to advanced tracking number
		int trackingNumberIndex = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName, callFlowName);
		assertTrue(trackingNumberIndex >= 0);
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex);
		
		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if (callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_user_able_to_create_advance_call_tracking_existing_smart_number-- passed ");
	}
}
