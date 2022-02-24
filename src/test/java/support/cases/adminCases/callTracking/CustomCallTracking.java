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

public class CustomCallTracking extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	CallFlowPage callFlowPage = new CallFlowPage();

	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String customFileNameDownload = "custom-export";
	
	private String callFlowName;
	private String campaignName;
	
	private String customTrackName;
	private String originatingUrl;
	private String destinationUrl;
	
	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void beforeMethod() {
		callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
	}
	
	//Verify Admin users should be able to create call tracking using existing unassigned smart number
	@Test(groups = { "Regression", "Product Sanity"})
	public void add_and_verify_custom_tracking_number_local() {
		System.out.println("Test case --add_and_verify_custom_tracking_number_local-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Custom Call Tracking page
		customTrackName = new StringBuilder("Custom").append(HelperFunctions.GetRandomString(4)).toString();
		originatingUrl = new StringBuilder("Origin").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		destinationUrl = new StringBuilder("Destination").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		
		dashboard.openCustomCallTrackingPage(supportDriver);
		HashMap<AdminCallTracking.trackingNoFields, String> customTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.NameCustom, customTrackName);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.OriginatingUrl, originatingUrl);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.DestinationUrl, destinationUrl);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		
		//adding data
		adminCallTracking.clickAddNewCustomTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, customTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		
		//verifying smart number custom present
		Pair<String,String> pair = adminCallTracking.getNameWithSmartNoFirstIndex(supportDriver);
		assertEquals(pair.first(), customTrackName, "Custom track name does not match");
		assertTrue(!pair.second().isEmpty(), "smart number does not exist");
		adminCallTracking.searchCustomCallTrackNumber(supportDriver, originatingUrl, callFlowName);
		int customCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, originatingUrl, callFlowName);
		assertTrue(customCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that url
		adminCallTracking.searchCustomCallTrackNumber(supportDriver, originatingUrl, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, originatingUrl);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity"})
	public void add_and_verify_custom_tracking_number_toll_free() {
		System.out.println("Test case --add_and_verify_custom_tracking_number_toll_free-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Custom Call Tracking page
		customTrackName = new StringBuilder("Custom").append(HelperFunctions.GetRandomString(4)).toString();
		originatingUrl = new StringBuilder("Origin").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		destinationUrl = new StringBuilder("Destination").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		
		dashboard.openCustomCallTrackingPage(supportDriver);
		HashMap<AdminCallTracking.trackingNoFields, String> customTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.NameCustom, customTrackName);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.OriginatingUrl, originatingUrl);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.DestinationUrl, destinationUrl);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		customTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.clickAddNewCustomTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, customTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		adminCallTracking.searchCustomCallTrackNumber(supportDriver, originatingUrl, callFlowName);
		int customCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, originatingUrl, callFlowName);
		assertTrue(customCallSmartNumberIndex >= 0, "smart number is not present");
		
		//Verifying custom call numbers downloaded
		HelperFunctions.deletingExistingFiles(downloadPath, customFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		boolean fileDownloaded = adminCallTracking.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, customFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");

		// Deleting all smart numbers of that url
		adminCallTracking.searchCustomCallTrackNumber(supportDriver, originatingUrl, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, originatingUrl);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void verify_validation_message_when_selected_empty_fields_custom_call() {
		System.out.println("Test case --verify_validation_message_when_selected_empty_fields_custom_call-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Custom Call Tracking page
		dashboard.openCustomCallTrackingPage(supportDriver);
		adminCallTracking.clickAddNewCustomTrackNobtn(supportDriver);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		String actualErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualErrorMessage, "Please correct the highlighted errors.");
		adminCallTracking.verifyMandatoryFieldsCustom(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_add_icon_enable_disable_for_custom_tracking_for_same_and_diff_account() {
		System.out.println("Test case --verify_add_icon_enable_disable_for_custom_tracking_for_same_and_diff_account-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Custom Call Tracking page
		dashboard.openCustomCallTrackingPage(supportDriver);
		assertEquals(adminCallTracking.getAccountName(supportDriver), CONFIG.getProperty("qa_user_account"));
		assertFalse(adminCallTracking.isCustomAddIconDisabled(supportDriver));
		
		//selecting diff account
		adminCallTracking.selectAccount(supportDriver, "QA v3");
		assertTrue(adminCallTracking.isCustomAddIconDisabled(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_icon_enable_disable_for_custom_tracking_for_same_and_diff_account-- passed ");
	}
}
