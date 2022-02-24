package support.cases.callFlowCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import support.base.SupportBase;
import support.source.callFlows.CallFlowPage;
import support.source.callFlows.CallFlowPage.CallFlowStatus;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.smartNumbers.SmartNumbersPage.SmartNumberFields;
import support.source.smartNumbers.SmartNumbersPage.smartNumberType;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CallFlowCases extends SupportBase{

	Dashboard dashboard = new Dashboard();
	CallFlowPage callFlowPage = new CallFlowPage();
	AddSmartNumberPage addSmartNoPage = new AddSmartNumberPage();
	SmartNumbersPage smartNoPage = new SmartNumbersPage();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	
	private String user_account;
	private String callFlowName;
	
	@BeforeClass(groups = { "Regression", "MediumPriority" })
	public void beforeClass() {
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			user_account = "";
		} else if (SupportBase.drivername.toString().equals("supportDriver")) {
			user_account = CONFIG.getProperty("qa_user_account");
		}
		callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
	}
	
	@Test(groups = { "Regression" })
	public void create_search_delete_call_flow_number() {
		System.out.println("Test case --create_search_delete_call_flow_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(2));

		//Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		
		//verifying call flow present and other details
		boolean callFlowPresent = callFlowPage.isCallFlowPresent(supportDriver, callFlowName);
		assertTrue(callFlowPresent);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		callFlowPage.verifyCallFlowPageDetails(supportDriver, callFlowName, CONFIG.getProperty("qa_user_account"));
		
		//adding smart number
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		Pair<String,List<String>> smartNumber = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, "22", smartLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());

		//verifying smart number
		boolean smartNumberPresent = callFlowPage.isSmartNumberPresent(supportDriver, smartNumber.first());
		assertTrue(smartNumberPresent);
		
		//deleting smart number on call flow page
		//callFlowPage.refreshCurrentDocument(supportDriver);
		callFlowPage.deleteSmartNumberOnPage(supportDriver, smartNumber.first());
		
		//deleting smart number on smart Numbers tab
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, smartNumber.first());
		smartNoPage.clickSmartNumber(supportDriver, smartNumber.first());
		smartNoPage.deleteSmartNumber(supportDriver);
		
		// verifying color of deleted number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, smartNumber.first());
		smartNoPage.verifyDeletedSmartNumberColor(supportDriver, smartNumber.first());
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void reassign_numbers_and_verify_on_page() {
		System.out.println("Test case --reassign_numbers_and_verify_on_page-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		
		//adding smart number
		String label = "AutoLabel".concat(HelperFunctions.GetRandomString(2));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		Pair<String, List<String>> numberPair = addSmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", label, Type.Additional.toString(), SmartNumberCount.Multiple.toString());

		// verifying smart number and deleting smart number
		callFlowPage.refreshCurrentDocument(supportDriver);
		callFlowPage.idleWait(2);
		for (String smartNo : numberPair.second()) {
			boolean smartNumberPresent = callFlowPage.isSmartNumberPresent(supportDriver, smartNo);
			assertTrue(smartNumberPresent);
			// deleting smart number
			callFlowPage.deleteSmartNumberOnPage(supportDriver, smartNo);
		}
		
		//reassigning/adding the number and verifying
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		
		for(String smartNo: numberPair.second()) {
			addSmartNoPage.clickOnReAssignForNumber(supportDriver, smartNo);
		}
		
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.enterLabelMultiple(supportDriver, label, numberPair.second());
		addSmartNoPage.saveSmartNo(supportDriver);
		
		// deleting smart number on call flow page
		for (String smartNo : numberPair.second()) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, smartNo);
		}

		// deleting smart number on smart numbers tab
		dashboard.openSmartNumbersTab(supportDriver);
		for (String smartNo : numberPair.second()) {
			smartNoPage.searchSmartNumber(supportDriver, smartNo);
			smartNoPage.clickSmartNumber(supportDriver, smartNo);
			smartNoPage.deleteSmartNumber(supportDriver);								   
			dashboard.openSmartNumbersTab(supportDriver);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void call_flow_operations_with_copy_delete() {
		System.out.println("Test case --call_flow_operations_with_copy_delete-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.navigateToAddNewCallFlow(supportDriver);
		String autoCallFlowName = "AutomationCallFlowCopy";
		
		//navigating to manage call flow and searching
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, autoCallFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, autoCallFlowName);
		
		assertTrue(callFlowPage.isDeleteBtnDisabled(supportDriver), "Delete button is not disabled");
		String descriptionCallFlow = callFlowPage.getTextCallFlowDesc(supportDriver);
		callFlowPage.clickCopyEditBtn(supportDriver);
		
		String actualCallFlowName = callFlowPage.getTextCallFlowName(supportDriver);
		String expectedCallFlowName = "Copy of ".concat(autoCallFlowName);
		String descriptionCallFlowAfter = callFlowPage.getTextCallFlowDesc(supportDriver);
		
		assertEquals(actualCallFlowName, expectedCallFlowName, "call flow name not matching after copy");
		assertEquals(descriptionCallFlow, descriptionCallFlowAfter, "description not matching");
		
		// verifying status remain active
		assertTrue(callFlowPage.getCurrentStatus(supportDriver).equals("Active"));
		callFlowPage.verifyNoSmartNumbersPresent(supportDriver);
		
		//selecting paused status and saving call flow
		callFlowPage.selectStatus(supportDriver, CallFlowStatus.Paused.name());
		callFlowPage.clickSaveBtn(supportDriver);
		assertFalse(callFlowPage.isDeleteBtnDisabled(supportDriver), "Delete button is disabled");
		
		//deleting call flow
		callFlowPage.clickDeleteCallFlowBtn(supportDriver);
		
		//verifying back on calls page
		callFlowPage.verifyOnCallFlowSearchPage(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_add_button_with_tracking_when_no_call_flow_assigned() {
		System.out.println("Test case --verify_add_button_with_tracking_when_no_call_flow_assigned-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//verifying number tracking type
		String trackingNumber = CONFIG.getProperty("automation_tracking_number2");
		String callFlow		  = CONFIG.getProperty("qa_call_flow_new_org");
		
		// undeleting smart number if deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);
		if (smartNoPage.isUndeleteBtnVisible(supportDriver)) {
			smartNoPage.clickUndeleteBtn(supportDriver);
		}
		
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);

		//converting to tracking type if number not of tracking type
		if (!smartNoPage.getNumberType(supportDriver, trackingNumber).equals(smartNumberType.Tracking.toString())
				|| smartNoPage.isNumberCallFlowEmpty(supportDriver, trackingNumber)) {
			String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));
			dashboard.navigateToManageCallFlow(supportDriver);
			
			//search and select call flow
			callFlowPage.searchCallFlow(supportDriver, callFlow, user_account);
			callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);
		}
		
		//removing call flow from number from number if present
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);
		if(smartNoPage.isCallFlowAddIconDisabled(supportDriver)){
			smartNoPage.deleteFields(supportDriver, SmartNumberFields.CallFlows);
		}
		
		//searching any call flow and verifying add button is visible
		String source = "AutoSource".concat(HelperFunctions.GetRandomString(3));
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"), user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		callFlowPage.clickAddSmartNoIcon(supportDriver);

		addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, null, source, null);
		
		//verifying and deleting number on call flow
		assertTrue(callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber));
		callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber);
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_button_with_tracking_when_no_call_flow_assigned-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void reassign_other_call_flow_tracking_number_to_another_call_flow() {
		System.out.println("Test case --reassign_other_call_flow_tracking_number_to_another_call_flow-- started");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(2));
		String callFlowDesc = "AutoCallFlowDesc".concat(HelperFunctions.GetRandomString(2));
		String trackingNumber = CONFIG.getProperty("automation_tracking_number2");
		
		if (SupportBase.drivername.equals("adminDriver")) {
			initializeSupport("webSupportDriver");
			driverUsed.put("webSupportDriver", true);
			dashboard.switchToTab(webSupportDriver, 2);
			dashboard.openSmartNumbersTab(webSupportDriver);
			smartNoPage.searchSmartNumber(webSupportDriver, trackingNumber);
			smartNoPage.clickSmartNumber(webSupportDriver, trackingNumber);
			if (smartNoPage.isUndeleteBtnVisible(webSupportDriver)) {
				smartNoPage.clickUndeleteBtn(webSupportDriver);
			}
			driverUsed.put("webSupportDriver", false);
		}
		
		// undeleting smart number if deleted
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);
		if (smartNoPage.isUndeleteBtnVisible(supportDriver)) {
			smartNoPage.clickUndeleteBtn(supportDriver);
		}

		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		//verifying number tracking type
		if (!smartNoPage.getNumberType(supportDriver, trackingNumber).equals(smartNumberType.Tracking.toString())
				|| smartNoPage.isNumberCallFlowEmpty(supportDriver, trackingNumber)) {

			//converting to tracking type
			String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));
			dashboard.navigateToAddNewCallFlow(supportDriver);
			callFlowPage.createCallFlow(supportDriver, callFlowName, callFlowDesc);

			//adding smart number to the call flow
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);
		}
		
		//verifying call flow present for tracking number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);
		assertTrue(smartNoPage.isCallFlowAddIconDisabled(supportDriver));

		//searching a different call flow and verifying reassign btn visible
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"), user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		assertFalse(addSmartNoPage.getTypeOfNumberList(supportDriver).contains(smartNumberType.LocalPresence.toString()));
		addSmartNoPage.searchSmartNo(supportDriver, trackingNumber);
		addSmartNoPage.clickOnReAssignForNumber(supportDriver, trackingNumber);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.saveSmartNo(supportDriver);
		
		//verifying and deleting number on call flow
		assertTrue(callFlowPage.isSmartNumberPresent(supportDriver, trackingNumber));
		callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingNumber);
		
		//deleting call flow created
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		callFlowPage.deleteCallFlow(supportDriver);
				
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --reassign_other_call_flow_tracking_number_to_another_call_flow-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_reassign_default_to_call_flow() {
		System.out.println("Test case --verify_existing_number_reassign_default_to_call_flow-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		Pair<String, List<String>> smartNumberPair;
		String defaultNumber;
		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		// Opening Users Tab
		String qa_user2_name = CONFIG.getProperty("qa_admin_user2_name");
		String qa_user2_email = CONFIG.getProperty("qa_admin_user2_email");
		String qa_salesForce2_id = CONFIG.getProperty("qa_admin_user2_salesforce_id");

		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		if(!userIntelligentDialerTab.isDefaultSmartNumberPresent(supportDriver)){
			userIntelligentDialerTab.clickSmartNoIcon(supportDriver);
			smartNumberPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
			defaultNumber = smartNumberPair.first();
		}
		else{
			defaultNumber = userIntelligentDialerTab.getDefaultNo(supportDriver);
		}
		
		//searching any call flow and assigning it the default number
		String label = "AutoDefaultToTracking".concat(HelperFunctions.GetRandomString(2));
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"), user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, defaultNumber, label, null, null);
		
		//verifying and deleting number on call flow
		assertTrue(callFlowPage.isSmartNumberPresent(supportDriver, defaultNumber));
		callFlowPage.deleteSmartNumberOnPage(supportDriver, defaultNumber);
		
		//verifying type tracking of number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, defaultNumber);
		smartNoPage.clickSmartNumber(supportDriver, defaultNumber);
		assertEquals(smartNoPage.getTypeDetail(supportDriver), smartNumberType.Tracking.name());
		
		//deleting number
		smartNoPage.deleteSmartNumber(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_reassign_default_to_call_flow-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_reassign_additional_to_call_flow() {
		System.out.println("Test case --verify_existing_number_reassign_additional_to_call_flow-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String smartlabel = "AutoLabelAdditional".concat(HelperFunctions.GetRandomString(2));
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		if(userIntelligentDialerTab.getAdditionalNumberList(supportDriver) == null || userIntelligentDialerTab.getAdditionalNumberList(supportDriver).isEmpty()){
			userIntelligentDialerTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartlabel, Type.Additional.toString(),
					SmartNumberCount.Single.toString());
		}
		
		String additionalSmartNo = userIntelligentDialerTab.getAdditionalNumberList(supportDriver).get(0);

		// searching any call flow and assigning it the default number
		String label = "AutoAdditionalToTracking".concat(HelperFunctions.GetRandomString(2));
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"), user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_emergency_route"));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		
		addSmartNoPage.selectAndReassignNumber(supportDriver, additionalSmartNo, label, null, null);

		// verifying and deleting number on call flow
		assertTrue(callFlowPage.isSmartNumberPresent(supportDriver, additionalSmartNo));
		callFlowPage.deleteSmartNumberOnPage(supportDriver, additionalSmartNo);

		// verifying type tracking of number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, additionalSmartNo);
		smartNoPage.clickSmartNumber(supportDriver, additionalSmartNo);
		assertEquals(smartNoPage.getTypeDetail(supportDriver), smartNumberType.Tracking.name());

		// deleting number
		smartNoPage.deleteSmartNumber(supportDriver);
				
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_reassign_additional_to_call_flow-- passed ");
	}
	
	//New smart numbers- Add multiple smart numbers
	//Verify Frontend: convert plus icon to button labeled 'Add New"
	@Test(groups = { "Regression" })
	public void add_multiple_smart_number_for_call_flow() {
		System.out.println("Test case --add_multiple_smart_number_for_call_flow-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		String smartLabel = "AutoCallFlowLabel".concat(HelperFunctions.GetRandomString(2));

		//Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		
		//adding multiple smart numbers
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		Pair<String,List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, AddSmartNumberPage.Country.UNITED_STATES.displayName(), null, null, smartLabel, Type.Additional.toString(), SmartNumberCount.Multiple.toString());
		for(String callFlowNo: smartNoPair.second()) {
			boolean callFLowNoExists = callFlowPage.isSmartNumberPresent(supportDriver, callFlowNo);
			assertTrue(callFLowNoExists, String.format("Call Flow number name:%s does not exists after creating", smartLabel));
		}
		
		//deleting call flow number
		for(String callFlowNo: smartNoPair.second()) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, callFlowNo);
		}
		
		// deleting smart numbers on page
		dashboard.openSmartNumbersTab(supportDriver);
		for (String smartNo : smartNoPair.second()) {
			smartNoPage.searchSmartNumber(supportDriver, smartNo);
			smartNoPage.clickSmartNumber(supportDriver, smartNo);
			smartNoPage.deleteSmartNumber(supportDriver);
			dashboard.openSmartNumbersTab(supportDriver);
		}

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_multiple_smart_number_for_call_flow-- passed ");
	}
	
	//Verify Delete Smart number from react smart number table for call flow
	//Verify Pagination on React Smart number table for call flow
	//Verify React Smart number table for call flow
	@Test(groups = { "Regression" })
	public void add_delete_multiple_smart_number_and_check_pagination() {
		System.out.println("Test case --add_delete_multiple_smart_number_and_check_pagination-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		String smartLabel = "AutoCallFlowLabel".concat(HelperFunctions.GetRandomString(2));

		//Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		
		//adding multiple smart numbers
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		Pair<String,List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, AddSmartNumberPage.Country.CANADA.displayName(), null, null, smartLabel, Type.Additional.toString(), SmartNumberCount.Multiple.toString());
		for(String callFlowNo: smartNoPair.second()) {
			boolean callFLowNoExists = callFlowPage.isSmartNumberPresent(supportDriver, callFlowNo);
			assertTrue(callFLowNoExists, String.format("Call Flow number name:%s does not exists after creating", smartLabel));
		}
		
		//assert pagination
		callFlowPage.assertCallFlowPagination(supportDriver);
		
		//deleting call flow number
		for(String callFlowNo: smartNoPair.second()) {
			callFlowPage.deleteSmartNumberOnPage(supportDriver, callFlowNo);
		}
		
		// deleting smart numbers on page
		dashboard.openSmartNumbersTab(supportDriver);
		for (String smartNo : smartNoPair.second()) {
			smartNoPage.searchSmartNumber(supportDriver, smartNo);
			smartNoPage.clickSmartNumber(supportDriver, smartNo);
			smartNoPage.deleteSmartNumber(supportDriver);
			dashboard.openSmartNumbersTab(supportDriver);
		}

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_multiple_smart_number_and_check_pagination-- passed ");
	}
}