package support.cases.adminCases.callTracking;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import support.base.SupportBase;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

public class OfflineCallTracking extends SupportBase{

	Dashboard dashboard = new Dashboard();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	HelperFunctions helperFunctions = new HelperFunctions();

	private String callFlowName;
	private String campaignName;
	private String offlineTrackName;

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String offlineFileNameDownload = "offline-export";

	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void beforeMethod() {
		callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
	}
	
	@Test(groups = { "Regression"})
	public void add_and_verify_offline_tracking_number_local() {
		System.out.println("Test case --add_and_verify_offline_tracking_number_local-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);

		offlineTrackName = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		HashMap<AdminCallTracking.trackingNoFields, String> offlineTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.TV.toString());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		Pair<String,String> pair = adminCallTracking.getNameWithSmartNoFirstIndex(supportDriver);
		assertEquals(pair.first(), offlineTrackName, "track name does not match");
		assertTrue(!pair.second().isEmpty(), "smart number does not exist");
		
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, AdminCallTracking.offlineTrackSourceTypes.TV.toString(), callFlowName);
		int offlineCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName, callFlowName);
		assertTrue(offlineCallSmartNumberIndex >= 0, "smart number is not present");
		String smartNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, offlineCallSmartNumberIndex);
		
		//Verifying added smart is available on smart number tabs and it is active
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, offlineTrackName, smartNumber);
		assertTrue(smartNoIndex >= 0);
		
		// Deleting smart number
		smartNumbersPage.deleteSmartNumberComplete(supportDriver, smartNoIndex);
	
		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if(callFlowPage.isSmartNumberPresent(supportDriver, smartNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, smartNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void add_and_verify_offline_tracking_number_toll_free() {
		System.out.println("Test case --add_and_verify_offline_tracking_number_toll_free-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);
		offlineTrackName = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		HashMap<AdminCallTracking.trackingNoFields, String> offlineTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.TV.toString());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		
		//Verifying offline call track smart number present
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, AdminCallTracking.offlineTrackSourceTypes.TV.toString(), callFlowName);
		int offlineCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName, callFlowName);
		assertTrue(offlineCallSmartNumberIndex >= 0, "smart number is not present");
		
		// deleting existing files and verifying file download
		HelperFunctions.deletingExistingFiles(downloadPath, offlineFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		boolean fileDownloaded = adminCallTracking.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, offlineFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");
				
		// Deleting all smart numbers of that domain
	    adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, "", "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, offlineTrackName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void verify_validation_message_when_selected_empty_fields_offline_call() {
		System.out.println("Test case --verify_validation_message_when_selected_empty_fields_offline_call-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		String actualErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualErrorMessage, "Please correct the highlighted errors.");

		//verifying border color and message
		Pair<String,String> pair = adminCallTracking.findBorderColorandInvalidMsg(supportDriver);
		assertEquals(pair.first(), "#a94442", "Border color is not red");
		assertEquals(pair.second(), "Please correct the highlighted errors.", "Invalid error text does not match");
		adminCallTracking.verifyMandatoryFieldsOffline(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void verify_source_field_editable_and_updated_after_changing() {
		System.out.println("Test case --verify_source_field_editable_and_updated_after_changing-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);
		String actualSourceType = AdminCallTracking.offlineTrackSourceTypes.Print.toString();
		adminCallTracking.upDateSourceFields(supportDriver, actualSourceType);
		dashboard.openOfflineCallTrackingPage(supportDriver);
		String expectedSource = adminCallTracking.verifyUpdateSource(supportDriver, actualSourceType);
		assertEquals(actualSourceType, expectedSource, "source types does not match");
		
		actualSourceType = AdminCallTracking.offlineTrackSourceTypes.TV.toString();
		adminCallTracking.upDateSourceFields(supportDriver, actualSourceType);
		dashboard.openOfflineCallTrackingPage(supportDriver);
		expectedSource = adminCallTracking.verifyUpdateSource(supportDriver, actualSourceType);
		assertEquals(actualSourceType, expectedSource, "source types does not match");
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity"})
	public void add_and_verify_multiple_offline_tracking_numbers() {
		System.out.println("Test case --add_and_verify_multiple_offline_tracking_numbers-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);

		//adding multiple offline data
		offlineTrackName = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		HashMap<AdminCallTracking.trackingNoFields, String> offlineTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.TV.toString());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType, AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickAddAnotherOfflineTrack(supportDriver);
		
		String offlineTrackName2 = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName2);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.Print.toString());
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		
		//searching first offline tracking
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, AdminCallTracking.offlineTrackSourceTypes.TV.toString(), callFlowName);
		int offlineCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName, callFlowName);
		assertTrue(offlineCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, "", "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, offlineTrackName);
		
        //searching second offline tracking
        dashboard.openOfflineCallTrackingPage(supportDriver);
        adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName2, AdminCallTracking.offlineTrackSourceTypes.Print.toString(), callFlowName);
		int offlineCallSmartNumberIndex2 = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName2, callFlowName);
		assertTrue(offlineCallSmartNumberIndex2 >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName2, "", "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, offlineTrackName2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_add_icon_enable_disable_for_offline_tracking_for_same_and_diff_account() {
		System.out.println("Test case --verify_add_icon_enable_disable_for_offline_tracking_for_same_and_diff_account-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);
		assertEquals(adminCallTracking.getAccountName(supportDriver), CONFIG.getProperty("qa_user_account"));
		assertFalse(adminCallTracking.isOfflineAddIconDisabled(supportDriver));
		
		//selecting diff account
		adminCallTracking.selectAccount(supportDriver, "QA v3");
		assertTrue(adminCallTracking.isOfflineAddIconDisabled(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_icon_enable_disable_for_offline_tracking_for_same_and_diff_account-- passed ");
	}
	
	//Verify Admin users should be able to create call tracking using existing unassigned smart number
	@Test(groups = { "Regression" })
	public void Verify_user_able_to_create_offline_call_tracking_existing_smart_number() {
		System.out.println("Test case --Verify_user_able_to_create_offline_call_tracking_existing_smart_number-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Offline Call Tracking page
		dashboard.openOfflineCallTrackingPage(supportDriver);

		offlineTrackName = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		HashMap<AdminCallTracking.trackingNoFields, String> offlineTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.TV.toString());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		Pair<String,String> pair = adminCallTracking.getNameWithSmartNoFirstIndex(supportDriver);
		assertEquals(pair.first(), offlineTrackName, "track name does not match");
		assertTrue(!pair.second().isEmpty(), "smart number does not exist");
		
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, AdminCallTracking.offlineTrackSourceTypes.TV.toString(), callFlowName);
		int offlineCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName, callFlowName);
		assertTrue(offlineCallSmartNumberIndex >= 0, "smart number is not present");
		String smartNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, offlineCallSmartNumberIndex);
		
		//Verifying added smart is available on smart number tabs and it is active
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, offlineTrackName, smartNumber);
		assertTrue(smartNoIndex >= 0);
		
		// Deleting smart number
		smartNumbersPage.deleteSmartNumberComplete(supportDriver, smartNoIndex);
	
		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if(callFlowPage.isSmartNumberPresent(supportDriver, smartNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, smartNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --Verify_user_able_to_create_offline_call_tracking_existing_smart_number-- passed ");
	}
}
