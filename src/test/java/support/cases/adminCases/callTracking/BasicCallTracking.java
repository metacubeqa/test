package support.cases.adminCases.callTracking;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

public class BasicCallTracking extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	
	private String callFlowName;
	private String campaignName;
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String basicFileNameDownload = "basic-export";

	private String domainNameInvalid = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).toString();
	private String customChannelInvalid = new StringBuilder("custom").append(HelperFunctions.GetRandomString(6)).toString();

	private String domainName;
	private String customChannel;

	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void beforeClass() {
		System.out.println("In Before Class");
		callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
	}
	
	@Test(groups = { "Regression"})
	public void add_and_verify_basic_tracking_number_local() {
		System.out.println("Test case --add_and_verify_basic_tracking_number_local-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Basic Call Tracking page
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openBasicCallTrackingPage(supportDriver);
		
		//adding basic tracking data
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		HashMap<AdminCallTracking.trackingNoFields, String> basicTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.Local.displayName());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		List<AdminCallTracking.Channels> list = new ArrayList<AdminCallTracking.Channels>();
		list.add(AdminCallTracking.Channels.Facebook);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);
		
		//verifying basic smart number
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Facebook.toString(),
				domainName, callFlowName);
		int basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName,
				callFlowName);
		
		String basicTrackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, basicCallSmartNumberIndex);
		
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, "", domainName, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, domainName);
		
		//Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if(callFlowPage.isSmartNumberPresent(supportDriver, basicTrackingNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, basicTrackingNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression"})
	public void add_and_verify_basic_tracking_number_tollfree() {
		System.out.println("Test case --add_and_verify_basic_tracking_number_tollfree-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Basic Call Tracking page
		dashboard.openBasicCallTrackingPage(supportDriver);

		//adding basic data
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		HashMap<AdminCallTracking.trackingNoFields, String> basicTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);

		List<AdminCallTracking.Channels> list = new ArrayList<AdminCallTracking.Channels>();
		list.add(AdminCallTracking.Channels.Instagram);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);

		//verifying basic smart number
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Instagram.toString(),
				domainName, callFlowName);
		int basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName,
				callFlowName);
		String basicTrackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, basicCallSmartNumberIndex);
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");
		
		// downloading and verifying csv
		HelperFunctions.deletingExistingFiles(downloadPath, basicFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		boolean fileDownloaded = adminCallTracking.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, basicFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");

		// Deleting all smart numbers of that domain
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, "", domainName, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, domainName);
		
		// Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if (callFlowPage.isSmartNumberPresent(supportDriver, basicTrackingNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, basicTrackingNumber);
		}

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression"})
	public void verify_valid_domain_and_custom_url() {
		System.out.println("Test case --verify_valid_domain_and_custom_url -- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Basic Call Tracking page
		dashboard.openBasicCallTrackingPage(supportDriver);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainNameInvalid);
		
		//verifying error messages
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		String actualDomainErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualDomainErrorMessage,
				domainNameInvalid + " is invalid. Please enter a valid destination domain.");
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.enterCustomChannel(supportDriver, customChannelInvalid);
		String actualCustomErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualCustomErrorMessage, customChannelInvalid + " is invalid. Please enter a valid channel.");
		adminCallTracking.closeAddAdvTrackingWindow(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression"})
	public void verify_validation_message_when_selected_empty_fields() {
		System.out.println("Test case --verify_validation_message_when_selected_empty_fields-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Open Basic Call Tracking page
		dashboard.openBasicCallTrackingPage(supportDriver);
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		customChannel = new StringBuilder("custom").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		
		//verifying validation messages
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, "");
		String actualDomainErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualDomainErrorMessage, "Please enter a destination domain.");
		adminCallTracking.waitUntilMessageDisappears(supportDriver);
		
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.enterCustomChannel(supportDriver, "");
		adminCallTracking.clickNextButtonBasic(supportDriver);
		String actualCustomErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualCustomErrorMessage, "Please select a channels");
		adminCallTracking.waitUntilMessageDisappears(supportDriver);
		
		adminCallTracking.enterCustomChannel(supportDriver, customChannel);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		adminCallTracking.clickFinishButtonBasic(supportDriver);
		actualCustomErrorMessage = adminCallTracking.findErrorMessageInvalidUrl(supportDriver);
		assertEquals(actualCustomErrorMessage, "Please correct the highlighted errors.");
		adminCallTracking.waitUntilMessageDisappears(supportDriver);
		
		adminCallTracking.verifyMandatoryFieldsBasic(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression", "Product Sanity"})
	public void verify_smart_number_after_selecting_adding_channels() {
		System.out.println("Test case --verify_smart_number_after_selecting_adding_channels-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Open Basic Call Tracking page
		dashboard.openBasicCallTrackingPage(supportDriver);
		
		//entering custom channels
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		customChannel = new StringBuilder("custom").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		
		List<AdminCallTracking.Channels> list = new ArrayList<AdminCallTracking.Channels>();
		list.add(AdminCallTracking.Channels.Google);
		list.add(AdminCallTracking.Channels.Twitter);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.enterCustomChannel(supportDriver, customChannel);
		String customChannel2 = new StringBuilder("custom").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		adminCallTracking.enterCustomChannel(supportDriver, customChannel2);
		adminCallTracking.deleteCustomChannelBasic(supportDriver, customChannel);
		adminCallTracking.enterCustomChannel(supportDriver, customChannel);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		
		//entering custom data
		HashMap<AdminCallTracking.trackingNoFields, String> basicTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.selectMultipleTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);
		
		//verifying smart number
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Google.toString(),
				domainName, callFlowName);
		int basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName,
				callFlowName);
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");

		//verifying channel unselectables
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		boolean twitterUnselected = adminCallTracking.isChannelUnSelectable(supportDriver, AdminCallTracking.Channels.Twitter.name());
		assertTrue(twitterUnselected, "Twitter is not unselected");
		boolean googleUnselected = adminCallTracking.isChannelUnSelectable(supportDriver, AdminCallTracking.Channels.Google.name());
		assertTrue(googleUnselected, "Google is not unselected");
		adminCallTracking.closeAddAdvTrackingWindow(supportDriver);
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, "", domainName, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, domainName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void verify_smart_number_after_deleting_smart_numbers() {
		System.out.println("Test case --verify_smart_number_after_deleting_smart_numbers-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Basic Call Tracking page
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();

		dashboard.openBasicCallTrackingPage(supportDriver);
		List<AdminCallTracking.Channels> list = new ArrayList<AdminCallTracking.Channels>();
		list.add(AdminCallTracking.Channels.Google);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		
		HashMap<AdminCallTracking.trackingNoFields, String> basicTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);

		//verfying and searching smart number
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Google.toString(),
				domainName, callFlowName);
		int basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName,
				callFlowName);
		
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting smart Number
		smartNumbersPage.deleteSmartNumberComplete(supportDriver, basicCallSmartNumberIndex);

		//verifying channel unselected
		dashboard.openBasicCallTrackingPage(supportDriver);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		boolean googleUnselected = adminCallTracking.isChannelUnSelectable(supportDriver, AdminCallTracking.Channels.Google.name());
		assertFalse(googleUnselected, "Google is not selected");
		
		//verifying smart number
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Google.toString(),
				domainName, callFlowName);
		basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName,
				callFlowName);
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, "", domainName, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, domainName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_add_icon_enable_disable_for_basic_tracking_for_same_and_diff_account() {
		System.out.println("Test case --verify_add_icon_enable_disable_for_basic_tracking_for_same_and_diff_account-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Basic Call Tracking page
		dashboard.openBasicCallTrackingPage(supportDriver);
		assertEquals(adminCallTracking.getAccountName(supportDriver), CONFIG.getProperty("qa_user_account"));
		assertFalse(adminCallTracking.isBasicAddIconDisabled(supportDriver));
		
		//selecting diff account
		adminCallTracking.selectAccount(supportDriver, "QA v3");
		assertTrue(adminCallTracking.isBasicAddIconDisabled(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_icon_enable_disable_for_basic_tracking_for_same_and_diff_account-- passed ");
	}
	
	//Verify Admin users should be able to create call tracking using existing unassigned smart number
	@Test(groups = { "Regression"})
	public void create_call_tracking_using_existing_unassigned_smart_number() {
		System.out.println("Test case --create_call_tracking_using_existing_unassigned_smart_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Basic Call Tracking page
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openBasicCallTrackingPage(supportDriver);
		
		//adding basic tracking data
		domainName = new StringBuilder("Domain").append(HelperFunctions.GetRandomString(6)).append(".com").toString();
		HashMap<AdminCallTracking.trackingNoFields, String> basicTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType, AdminCallTracking.newAdvTrackSmartNoTypes.UseExisting.displayName());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.AreaCode, HelperFunctions.getRandomAreaCode());
		basicTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		List<AdminCallTracking.Channels> list = new ArrayList<AdminCallTracking.Channels>();
		list.add(AdminCallTracking.Channels.Facebook);
		adminCallTracking.clickAddNewBasicTrackbtn(supportDriver);
		adminCallTracking.enterDomainName(supportDriver, domainName);
		adminCallTracking.selectCustomChannel(supportDriver, list);
		adminCallTracking.clickNextButtonBasic(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		
		//remove selected smart number
		adminCallTracking.removeAssignedSmartNumber(supportDriver);
		
		//re-assign
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, basicTrackingNumberData);
		adminCallTracking.clickFinishButtonBasic(supportDriver);
		
		//verifying basic smart number
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, AdminCallTracking.Channels.Facebook.toString(), domainName, callFlowName);
		int basicCallSmartNumberIndex = adminCallTracking.getBasicCallSmartNumberIndex(supportDriver, domainName, callFlowName);
		
		String basicTrackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, basicCallSmartNumberIndex);
		
		assertTrue(basicCallSmartNumberIndex >= 0, "smart number is not present");
		
		// Deleting all smart numbers of that domain
		adminCallTracking.searchBasicCallTrackNumber(supportDriver, "", domainName, "");
		adminCallTracking.deleteSmartNumbersOfDomainName(supportDriver, domainName);
		
		//Deleting number from call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		if(callFlowPage.isSmartNumberPresent(supportDriver, basicTrackingNumber)) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, basicTrackingNumber);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
}
