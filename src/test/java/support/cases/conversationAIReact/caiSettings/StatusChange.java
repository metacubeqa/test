package support.cases.conversationAIReact.caiSettings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.salesforce.SalesforceHomePage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.SettingsPage;
import utility.HelperFunctions;

public class StatusChange extends SupportBase {

	Dashboard dashboard = new Dashboard();
	SettingsPage settingsPage = new SettingsPage();
	
	//Update existing Group name and verify its updated in Lead Status
	//Verify user able to add New group within Lead Status
	//Verify user add group and custom name to 'In progress' Lead Status
	//Verify user delete any group which is already associated a Lead Status
	@Test(groups = { "Regression" })
	public void verify_add_new_group_within_lead_status() {
		System.out.println("Test case --verify_add_new_group_within_lead_status-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		
		//creating status group name
		String apiName = "Unqualified";
		String groupName = "AutoStsGrp".concat(HelperFunctions.GetRandomString(3));
		settingsPage.createStatusChangeGroup(caiCallerDriver, groupName);
		
		//associating group name with api name
		String oldCstomName = settingsPage.getRDNACustomNameAccToAPI(caiCallerDriver, apiName);
		String newCustomName = "AutoCustomInprogress".concat(HelperFunctions.GetRandomString(4));
		settingsPage.editAccToSFAPIName(caiCallerDriver, apiName, oldCstomName, newCustomName, true, groupName);
		assertEquals(settingsPage.getGroupNameAccToAPI(caiCallerDriver, apiName), groupName);

		//verify group name more than 25 not accept
		String groupMoreThan25Char = "zanchigasjamqrxavxeamzfwoffsdhcksrnqszkbbsnusqrtftpcthe";
		settingsPage.verifyStatusChangeGrpNameMoreThan25NotAccept(caiCallerDriver, groupMoreThan25Char);
		
		//update existing group name  and verify
		String updatedGrpName = settingsPage.updateExistingGroupUnderManageGroup(caiCallerDriver, groupName);
		assertEquals(settingsPage.getGroupNameAccToAPI(caiCallerDriver, apiName), updatedGrpName);
		
		settingsPage.deleteGroupStatusChange(caiCallerDriver, updatedGrpName);
		assertFalse(settingsPage.isEditPencilAccToGrpNameVisible(caiCallerDriver, updatedGrpName));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_add_new_group_within_lead_status-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_edit_lead_status_under_inactive_category() {
		System.out.println("Test case --verify_user_edit_lead_status_under_inactive_category-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		
		String apiName = "Contacted";
		String oldCstomName = settingsPage.getRDNACustomNameAccToAPI(caiCallerDriver, apiName);
		String newCustomName = "AutoCustom".concat(HelperFunctions.GetRandomString(4));
		settingsPage.editAccToSFAPIName(caiCallerDriver, apiName, oldCstomName, newCustomName, false, null);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_edit_lead_status_under_inactive_category-- passed ");
	}
	
	//
	@Test(groups = { "Regression" })
	public void verify_user_edit_lead_status_under_converted_category() {
		System.out.println("Test case --verify_user_edit_lead_status_under_converted_category-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		
		String apiName = "Qualified";
		String oldCstomName = settingsPage.getRDNACustomNameAccToAPI(caiCallerDriver, apiName);
		String newCustomName = "AutoCustomInprogress".concat(HelperFunctions.GetRandomString(4));
		settingsPage.editAccToSFAPIName(caiCallerDriver, apiName, oldCstomName, newCustomName, false, null);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_edit_lead_status_under_converted_category-- passed ");
	}
	
	//Verify Existing opportunities status showing with correct category and in sync with SFDC
	@Test(groups = { "Regression" })
	public void verify_existing_opportunity_status_correct_category_in_sync_with_sfdc() {
		System.out.println("Test case --verify_existing_opportunity_status_correct_category_in_sync_with_sfdc-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		settingsPage.clickOpportunityStatusTab(caiCallerDriver);
		
		String type1 = "Close - Lost";
		List<String> closeLostList = settingsPage.getApiNameListAccToType(caiCallerDriver, type1);
		
		String type2 = "Open";
		List<String> opennList = settingsPage.getApiNameListAccToType(caiCallerDriver, type2);
		
		String type3 = "Close - Won";
		List<String> closeWonList = settingsPage.getApiNameListAccToType(caiCallerDriver, type3);
		
		SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
		SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
		CallScreenPage callScreenPage = new CallScreenPage();
		SalesforceHomePage sfHomePage = new SalesforceHomePage();
		
		//Open dial pad of driver and dial to another driver
		softPhoneCalling.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		callScreenPage.clickCallerName(caiCallerDriver);
		callScreenPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callScreenPage.closeLightningDialogueBox(caiCallerDriver);
		
		//navigating to sfdc opportunity stage
		sfHomePage.enterSetupSearchAndSelectSubContainer(caiCallerDriver, "Opportunities", "Opportunity", "Fields");
		sfHomePage.clickOpportunityFieldLabel(caiCallerDriver, "Stage");

		List<String> expectedCloseLostList = sfHomePage.getApiNameListAccToType(caiCallerDriver, "Closed/Lost");
		List<String> expectedOpenList = sfHomePage.getApiNameListAccToType(caiCallerDriver, "Open");
		List<String> expectedCloseWonList = sfHomePage.getApiNameListAccToType(caiCallerDriver, "Closed/Won");
	
		sfHomePage.closeTab(caiCallerDriver);
		sfHomePage.switchToTab(caiCallerDriver, 2);
		
		//verifying
		assertTrue(settingsPage.isListSameAfterSorting(closeLostList, expectedCloseLostList));
		assertTrue(settingsPage.isListSameAfterSorting(opennList, expectedOpenList));
		assertTrue(settingsPage.isListSameAfterSorting(closeWonList, expectedCloseWonList));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_existing_opportunity_status_correct_category_in_sync_with_sfdc-- passed ");
	}
	
	//Verify user able to update custom name of any opportunity status
	@Test(groups = { "Regression" })
	public void verify_user_edit_opportunity_status_under_any_category() {
		System.out.println("Test case --verify_user_edit_opportunity_status_under_any_category-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		settingsPage.clickOpportunityStatusTab(caiCallerDriver);
		
		String apiName = "Closed Lost";
		String oldCstomName = settingsPage.getRDNACustomNameAccToAPI(caiCallerDriver, apiName);
		String newCustomName = "AutoCustom".concat(HelperFunctions.GetRandomString(4));
		settingsPage.editAccToSFAPIName(caiCallerDriver, apiName, oldCstomName, newCustomName, false, null);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_edit_opportunity_status_under_any_category-- passed ");
	}
	
	//Verify collapse and expand opportunity status categories
	//Verify all three categories are Collapsible and Expandable under Lead Status
	@Test(groups = { "Regression" })
	public void verify_expand_collapse_lead_and_opportunity_status_settings() {
		System.out.println("Test case --verify_expand_collapse_lead_and_opportunity_status_settings-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		//lead status
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		//verify items visible
		dashboard.isPaceBarInvisible(caiCallerDriver);
		assertTrue(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));
		//collapse
		settingsPage.clickExpandCollapseOnStatusChange(caiCallerDriver);
		//verify items not visible
		assertFalse(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));
		//expand
		settingsPage.clickExpandCollapseOnStatusChange(caiCallerDriver);
		//verify items visible
		assertTrue(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));
		
		//click Opportunity Status Tab
		settingsPage.clickOpportunityStatusTab(caiCallerDriver);
		//verify items visible
		dashboard.isPaceBarInvisible(caiCallerDriver);
		assertTrue(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));
		//collapse
		settingsPage.clickExpandCollapseOnStatusChange(caiCallerDriver);
		//verify items not visible
		assertFalse(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));		
		//expand
		settingsPage.clickExpandCollapseOnStatusChange(caiCallerDriver);
		//verify items visible
		assertTrue(settingsPage.verifySalesforceApiHeaderVisible(caiCallerDriver));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_expand_collapse_lead_and_opportunity_status_settings-- passed ");
	}
	
	//Verify user dragging lead status from 'In Progress' to Inactive category Lead Status
	//Verify user dragging lead status from 'Inactive' to 'In Progress' category
	//Dragging same Group lead status from In Progress to Inactive Category together
	@Test(groups = { "Regression" })
	public void verify_drag_lead_status_from_in_progress_to_inactive_and_vice_versa() {
		System.out.println("Test case --verify_drag_lead_status_from_in_progress_to_inactive_and_vice_versa-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		//lead status
		settingsPage.clickStatusChangeTab(caiCallerDriver);
		//verify items visible
		dashboard.isPaceBarInvisible(caiCallerDriver);
		
		//verify converted status are not draggable
		assertFalse(settingsPage.isConvertedStatusDraggable(caiCallerDriver));
		
		//drag inactive status to in progress and vice versa
		settingsPage.dragAndDropInactiveToInProgress(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_drag_lead_status_from_in_progress_to_inactive_and_vice_versa-- passed ");
	}
}
