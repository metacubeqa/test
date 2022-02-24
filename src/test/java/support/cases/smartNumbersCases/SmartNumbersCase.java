package support.cases.smartNumbersCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountsPage;
import support.source.admin.AdminCallTracking;
import support.source.admin.AdminCallTracking.trackingNoFields;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.smartNumbers.SmartNumbersPage.smartNumberStatus;
import support.source.smartNumbers.SmartNumbersPage.smartNumberType;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class SmartNumbersCase extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	AccountsPage accountsPage = new AccountsPage();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	CallFlowPage callFlowPage = new CallFlowPage();
	AddSmartNumberPage addSmartNumberPage  = new AddSmartNumberPage();
	UsersPage usersPage = new UsersPage();
	
	private String callFlowName;
	private String campaignName;
	private String accountName;
	
	@BeforeClass(groups = { "Regression", "MediumPriority"})
	public void beforeClass(){
		callFlowName = CONFIG.getProperty("qa_call_flow_for_call_tracking");
		campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
		accountName = CONFIG.getProperty("qa_user_account");
	}
	
	@Test(groups = { "Regression"})
	public void add_and_verify_call_flow_to_tracking_type() {
		System.out.println("Test case --add_and_verify_call_flow_to_number_tracking_type-- started ");
	
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String trackingName = new StringBuilder("Tracking").append(HelperFunctions.GetRandomString(3)).toString();
		
		// Open Advance Call Tracking page
		dashboard.openAdvanceCallTrackingPage(supportDriver);

		//adding advance data
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.Name, trackingName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, "Automation");
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType, AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.clickAddNewAdvTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, advTrackingNumberData);
		adminCallTracking.clickFinishButton(supportDriver);
		
		//getting the assigned smart number to advanced tracking number
		int trackingNumberIndex = adminCallTracking.getAdvTrackNumberIndex(supportDriver, trackingName,  callFlowName);
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, trackingNumberIndex);
		
		//Verifying added smart is available on smart number tabs and it is active
		dashboard.openSmartNumbersTab(supportDriver);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, advTrackingNumberData.get(trackingNoFields.CustomParameterValue), trackingNumber);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
	
		//verifying status active before deleting
		assertFalse(smartNumbersPage.isStatusInactive(supportDriver));
		
		//verifying call flow to tracking type
		smartNumbersPage.verifyCallFlowAfterTracking(supportDriver, callFlowName);
		
		//verifying salesforce campaign to tracking type
		smartNumbersPage.verifySalesForceCampaignAfterTracking(supportDriver, campaignName);
		smartNumbersPage.removeCampaign(supportDriver, campaignName);
		
		//verifying call scripts to tracking type
		String callScriptName = CONFIG.getProperty("call_script_name");
		smartNumbersPage.verifyCallScriptsAfterTracking(supportDriver, callScriptName);
		smartNumbersPage.removeCallScripts(supportDriver, callScriptName);
		
		//deleting smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		//verifying status inactive after deleting
		assertTrue(smartNumbersPage.isStatusInactive(supportDriver));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void add_and_verify_call_flow_to_central_type() {
		System.out.println("Test case --add_and_verify_call_flow_to_central_type-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//creating central number
		if (accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()) {
			String centralLabelName = "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
		}
		
		String centralNumber = accountIntelligentTab.getCentralNoList(supportDriver).get(0);
		
		//searching smart number of type central number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, centralNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, centralNumber);
		
		//creating and verifying call flow on smart number page
		String callFlowName = CONFIG.getProperty("qa_call_flow_for_call_tracking");
		smartNumbersPage.createCallFlow(supportDriver, callFlowName);
		assertTrue(smartNumbersPage.isCallFlowPresent(supportDriver, callFlowName));
		
		//deleting smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// verifying status inactive after deleting
		assertTrue(smartNumbersPage.isStatusInactive(supportDriver));

		// verifying for support user
		if (SupportBase.drivername.toString().equals("supportDriver")) {
			assertTrue(smartNumbersPage.isUndeleteBtnVisible(supportDriver));

			// clicking undelete btn
			smartNumbersPage.clickUndeleteBtn(supportDriver);
			smartNumbersPage.idleWait(2);
			assertFalse(smartNumbersPage.isUndeleteBtnVisible(supportDriver));
			assertFalse(smartNumbersPage.isStatusInactive(supportDriver));
			
			// deleting smart number
			smartNumbersPage.deleteSmartNumber(supportDriver);
		}
		else {
			smartNumbersPage.deleteOnSmartNumberPage(supportDriver, callFlowName);
		}
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void search_and_edit_local_presence_type_number() {
		System.out.println("Test case --search_and_edit_local_presence_type_number-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String tag = "AutoTag".concat(HelperFunctions.GetRandomString(2));
		String description = "AutoDesc".concat(HelperFunctions.GetRandomString(2));
		String label = "AutoLabel".concat(HelperFunctions.GetRandomString(2));
		String trackingType = "Local Presence";

		// getting smart number of type local presence
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingType);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, 0);
		
		// updating tag,description and label details
		smartNumbersPage.updateTagDescrLabel(supportDriver, tag, description, label);

		// verifying the above made changes
		smartNumbersPage.verifyTagDescLabelUpdated(supportDriver, tag, description, label);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void add_smart_number_by_sid() {
		System.out.println("Test case --add_smart_number_by_sid-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//creating and verifying call flow on smart number page
		String callFlowName = CONFIG.getProperty("qa_call_flow_for_call_tracking");
		String user_account = CONFIG.getProperty("qa_user_account");
		
		//Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		//adding smart number
		String label = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(2));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		Pair<String, List<String>> numberPair = addSmartNumberPage.addNewSmartNumber(supportDriver, null, HelperFunctions.getRandomAreaCode(), "22", label, Type.Additional.toString(), SmartNumberCount.Single.toString());

		//searching smart number
		dashboard.openSmartNumbersTab(supportDriver);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, numberPair.first());
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		
		//deleting smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// verifying status inactive after deleting
		assertTrue(smartNumbersPage.isStatusInactive(supportDriver));
		assertEquals(smartNumbersPage.getDeletedValue(supportDriver), "true", "Number not deleted");
		
		//getting sid value
		String sidValue = smartNumbersPage.getSIDValue(supportDriver);
		smartNumbersPage.clickAccount(supportDriver);
		
		//Going to overview tab and entering sid
		overViewTab.clickAddSmartNumberBtn(supportDriver);
		overViewTab.enterSID(supportDriver, sidValue);
		overViewTab.clickOpenSmartNumber(supportDriver);
		
		//verifying after opening number gets undeleted
		assertEquals(smartNumbersPage.getDeletedValue(supportDriver), "false", "Number is deleted");
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_smart_number_by_sid-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_smart_number_filter_through_account_name() {
		System.out.println("Test case --verify_smart_number_filter_through_account_name-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumberByAccount(supportDriver, CONFIG.getProperty("qa_user_account"));
		assertTrue(smartNumbersPage.verifyAccountNameList(supportDriver, CONFIG.getProperty("qa_user_account")));

		//updating drivers used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_smart_number_filter_through_account_name-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_tracking_call_flow_remove_after_deleting_call_flow() {
		System.out.println("Test case --verify_tracking_call_flow_remove_after_deleting_call_flow-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String trackingNumber = CONFIG.getProperty("automation_tracking_number2");
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(2));
		String callFlowDesc = "AutoCallFlowDesc".concat(HelperFunctions.GetRandomString(2));
		String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));

		//undeleting smart number if deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingNumber);
		if(smartNumbersPage.isUndeleteBtnVisible(supportDriver)){
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}
		
		// converting to tracking type
		dashboard.navigateToAddNewCallFlow(supportDriver);
		callFlowPage.createCallFlow(supportDriver, callFlowName, callFlowDesc);
		callFlowPage.saveCallFlowSettings(supportDriver);

		// adding smart number to the call flow
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNumberPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);
		
		//verifying call flow present for tracking number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingNumber);
		assertTrue(smartNumbersPage.isCallFlowAddIconDisabled(supportDriver));

		// searching same call flow and verifying reassign btn disabled
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, accountName);
		callFlowPage.clickSelectedCallFlow(supportDriver,callFlowName);
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNumberPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNumberPage.clickNextButton(supportDriver);
		addSmartNumberPage.searchSmartNo(supportDriver, trackingNumber);
		assertTrue(addSmartNumberPage.isReAssignBtnDisabled(supportDriver, trackingNumber));
		addSmartNumberPage.closeSmartNumberWindow(supportDriver);
				
		//searching a different call flow and verifying reassign btn visible
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"), accountName);
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNumberPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNumberPage.clickNextButton(supportDriver);
		addSmartNumberPage.searchSmartNo(supportDriver, trackingNumber);
		addSmartNumberPage.clickOnReAssignForNumber(supportDriver, trackingNumber);
		addSmartNumberPage.closeSmartNumberWindow(supportDriver);
		
		// deleting call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, accountName);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		callFlowPage.deleteCallFlow(supportDriver);
		
		// verifying call flow not present for tracking number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingNumber);
		assertFalse(smartNumbersPage.isCallFlowPresent(supportDriver, callFlowName));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_tracking_call_flow_remove_after_deleting_call_flow-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_validation_msg_when_label_more_than_255_smartnumber_details() {
		System.out.println("Test case --verify_validation_msg_when_label_more_than_255_smartnumber_details-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String smartNo = CONFIG.getProperty("automation_tracking_number2");
		String label = "AutoLabel".concat(new String(new char[255]).replace("\0", HelperFunctions.GetRandomString(1)));

		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, CONFIG.getProperty("automation_tracking_number2"));
		smartNumbersPage.clickSmartNumber(supportDriver, smartNo);
		smartNumbersPage.enterLabel(supportDriver, label);
		smartNumbersPage.clickSaveBtn(supportDriver);
		assertEquals(smartNumbersPage.getToastMsg(supportDriver), "Label is too long");
		smartNumbersPage.closeToastMsg(supportDriver);
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_validation_msg_when_label_more_than_255_smartnumber_details-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void access_smart_number_details_page_after_open_direct_url() {
		System.out.println("Test case --access_smart_number_details_page_after_open_direct_url-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//getting url of smart number
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, 0);
		smartNumbersPage.waitForPageLoaded(supportDriver);
		String smartNoUrl = supportDriver.getCurrentUrl();
		
		//opening url in new tab
		dashboard.openNewBlankTab(supportDriver);
		dashboard.switchToTab(supportDriver, dashboard.getTabCount(supportDriver));
		supportDriver.get(smartNoUrl);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//verifying no errors are there
		assertFalse(smartNumbersPage.isToastMsgVisible(supportDriver));
		assertFalse(smartNumbersPage.isToastMsgCloseBtnVisible(supportDriver));
		
		smartNumbersPage.closeTab(supportDriver);
		smartNumbersPage.switchToTab(supportDriver, 2);
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --access_smart_number_details_page_after_open_direct_url-- passeds");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_script_removed_from_number_after_delete_call_script() {
		System.out.println("Test case --verify_call_script_removed_from_number_after_delete_call_script-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String smartNumber = CONFIG.getProperty("automation_tracking_number2");
		String callScriptName;

		//undeleting number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);
		if (smartNumbersPage.isUndeleteBtnVisible(supportDriver)) {
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}		
		
		//converting to tracking type if not
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		if (!smartNumbersPage.getNumberType(supportDriver, smartNumber).equals(smartNumberType.Tracking.name())
				|| smartNumbersPage.isNumberCallFlowEmpty(supportDriver, smartNumber)) {

			//converting to tracking type
			String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));
			dashboard.navigateToManageCallFlow(supportDriver);
			callFlowPage.searchCallFlow(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
			callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

			//adding smart number to the call flow
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNumberPage.selectAndReassignNumber(supportDriver, smartNumber, label, null, null);
		}
		
		// verifying call scripts present
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);

		if (smartNumbersPage.isAddCallScriptsIconDisabled(supportDriver)) {
			callScriptName = smartNumbersPage.getCallScriptName(supportDriver);
		}
		else {
			
			//Adding Call script and smart number
			dashboard.clickAccountsLink(supportDriver);
			accountIntelligentTab.openIntelligentDialerTab(supportDriver);

			// adding call script
			callScriptName = "AutoCallScriptName".concat(HelperFunctions.GetRandomString(3));
			String callScriptDescription = "AutoCallScriptDescr".concat(HelperFunctions.GetRandomString(3));
			accountIntelligentTab.createCallScripts(supportDriver, callScriptName, callScriptDescription, smartNumber);
			boolean callScriptExists = accountIntelligentTab.checkCallScriptsSaved(supportDriver, callScriptName);
			assertTrue(callScriptExists, String.format("Call Script: %s does not exists after creating", callScriptName));
		}
		
		// verifying on smart numbers page , call script present
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);
		assertTrue(smartNumbersPage.isSalesForceCampaignVisible(supportDriver, callScriptName));	
				
		//deleting call script from intelligent dialer
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.deleteRecords(supportDriver, callScriptName);
		
		//verifying on smart numbers page , no call script present
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);
		assertFalse(smartNumbersPage.isSalesForceCampaignVisible(supportDriver, callScriptName));	
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_script_removed_from_number_after_delete_call_script-- passed");
	}
	
	//Tracking Type - update status
	@Test(groups = { "Regression" })
	public void verify_tracking_type_number_status_update() {
		System.out.println("Test case --verify_tracking_type_number_status_update-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String smartNumber = CONFIG.getProperty("automation_tracking_number");
		
		// undeleting number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);
		if (smartNumbersPage.isUndeleteBtnVisible(supportDriver)) {
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}
		
		//Select Status and save
		smartNumbersPage.selectStatusOfSmartNumber(supportDriver, smartNumberStatus.Inactive.toString());
		smartNumbersPage.clickSaveBtn(supportDriver);
		
		//Verify status of number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		assertEquals(smartNumbersPage.getStatus(supportDriver, 0), smartNumberStatus.Inactive.toString());
		
		//Reset Status
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, smartNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, smartNumber);
		
		//Select Status and save
		smartNumbersPage.selectStatusOfSmartNumber(supportDriver, smartNumberStatus.Active.toString());
		smartNumbersPage.clickSaveBtn(supportDriver);
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_tracking_type_number_status_update-- passed");
	}

}