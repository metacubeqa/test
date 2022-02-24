package support.cases.accountCases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountSkillsTab;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.Dashboard;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class SkillsTabCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AccountSkillsTab skillTab = new AccountSkillsTab();
	CallFlowPage callFlowPage = new CallFlowPage();
	UsersPage userPage = new UsersPage();

	@Test(groups = { "MediumPriority" })
	public void add_skill() {

		System.out.println("Test Case--Create a new manual skill--Started");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);

		String skill_1 = "Skill".concat(" ") + HelperFunctions.GetRandomString(3);
		String skill_2 = "Skill".concat(" ") + HelperFunctions.GetRandomString(3);
		String skill_3 = "Skill".concat(" ") + HelperFunctions.GetRandomString(3);

		// Get random StringName
		assertFalse(skillTab.addNewSkill(supportDriver, skill_1));
		assertFalse(skillTab.addNewSkill(supportDriver, skill_2));
		assertTrue(skillTab.skillExist(supportDriver, skill_1));
		assertTrue(skillTab.skillExist(supportDriver, skill_2));
		assertTrue(skillTab.getUserLength(supportDriver, skill_1).equals("0"));
		assertTrue(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_1));
		assertTrue(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_2));
		System.out.println("Test Case--Create a new manual skill--Passed");

		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"), "");
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"));

		System.out.println("Test Case--Newly associated skill available to associate in call flow--Started");
		assertTrue(skillTab.verifySkillExistsInCallFlow(supportDriver, skill_1));
		callFlowPage.saveCallFlowSettings(supportDriver);

		// Verify Call Flow name updated in Skill tab as well for given skill
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.getCallFlowForGivenSkill(supportDriver, skill_1).contains(CONFIG.getProperty("skill_call_flow_1")));
		assertFalse(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_1));
		System.out.println("Test Case--Newly associated skill available to associate in call flow--Passed");

		// Upon clicking call flow link user redirected to call flow detail page
		skillTab.clickCallFlowForGivenSkill(supportDriver, skill_1);
		assertTrue(supportDriver.getCurrentUrl().contains("#call-flows"));

		System.out.println(
				"Test Case--Associate skill with multiple call flows and verify associated call flows on Skills tab--Started");

		// Verify multiple call flows can be associated to a skill
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"), "");
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"));
		assertTrue(skillTab.verifySkillExistsInCallFlow(supportDriver, skill_1));
		callFlowPage.saveCallFlowSettings(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);

		// Verify Call Flow name updated in Skill tab as well for given skill
		assertTrue(skillTab.getCallFlowForGivenSkill(supportDriver, skill_1).contains(CONFIG.getProperty("skill_call_flow_1")));
		assertTrue(skillTab.getCallFlowForGivenSkill(supportDriver, skill_1).contains(CONFIG.getProperty("skill_call_flow_2")));
		assertFalse(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_1));

		System.out.println("Test Case--Associate skill with multiple call flows and verify associated call flows on Skills tab--Passed");

		System.out.println("Test Case--Associate skill with Users--Started");
		// Verify Skill added in User-Skill section as well
		dashboard.clickOnUserProfile(supportDriver);
		userPage.clickToAddSkill(supportDriver);
		assertTrue(userPage.isGivenSkillExists(supportDriver, skill_1));
		userPage.addSkill(supportDriver, skill_1, null);
		assertTrue(userPage.getSkillLevel(supportDriver, skill_1).equals("4"));
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.getUserLength(supportDriver, skill_1).equals("1"));

		System.out.println("Test Case--Associate skill with Users--Passed");

		// Add same skill to another user
		System.out.println("Test Case--Associate multiple Users with same skill and Verify No. of Users associated with skill on Skills tab--Started");
		dashboard.openManageUsersPage(supportDriver);
		userPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		if(userPage.isUnDeleteBtnVisible(supportDriver)) {
			userPage.clickUnDeleteBtn(supportDriver);
			userPage.savedetails(supportDriver);
		}
		
		dashboard.openManageUsersPage(supportDriver);
		userPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userPage.clickToAddSkill(supportDriver);
		assertTrue(userPage.isGivenSkillExists(supportDriver, skill_1));
		userPage.addSkill(supportDriver, skill_1, null);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.getUserLength(supportDriver, skill_1).equals("2"));

		System.out.println("Test Case--Associate multiple Users with same skill and Verify No. of Users associated with skill on Skills tab--Passed");

		System.out.println("Test Case--Sorting on Skill Name and Number of users--Started");
		skillTab.clickSkill(supportDriver);
		assertTrue(skillTab.verifySkillNameSorted(supportDriver));
		skillTab.clickSkill(supportDriver);
		assertFalse(skillTab.verifySkillNameSorted(supportDriver));
		skillTab.clickNoOfUser(supportDriver);
		assertTrue(skillTab.verifyNoOfUserSorted(supportDriver));
		skillTab.clickNoOfUser(supportDriver);
		assertFalse(skillTab.verifyNoOfUserSorted(supportDriver));
		System.out.println("Test Case--Sorting on Skill Name and Number of users--Passed");

		// Remove skill from user and check count, it should be reduced by 1
		System.out.println("Test Case--Remove users from the skill list from Skills tab --Started");
		dashboard.openManageUsersPage(supportDriver);
		userPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userPage.removeSkillFromUser(supportDriver, skill_1);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.getUserLength(supportDriver, skill_1).equals("1"));

		System.out.println("Test Case--Remove users from the skill list from Skills tab --Passed");

		// update Skill level
		System.out.println("Test Case--Update the skill level for a user from Skills tab--Started");
		dashboard.clickOnUserProfile(supportDriver);
		userPage.clickUpdateSkill(supportDriver, skill_1);
		userPage.updateSkillLevel(supportDriver, "Level 3");
		assertTrue(userPage.getSkillLevel(supportDriver, skill_1).equals("3"));
		System.out.println("Test Case--Update the skill level for a user from Skills tab--Passed");

		// Try to add same skill again, Duplicate Error message should come
		System.out.println("Test Case--Verify duplicate skill with same name is not added--Started");
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.addNewSkill(supportDriver, skill_1));
		System.out.println("Test Case--Verify duplicate skill with same name is not added--Passed");

		// Try to update skill with an existing skill
		System.out.println("Test Case--Verify duplicate skill with same name is not updated--Started");
		assertTrue(skillTab.updateSkill(supportDriver, skill_1, skill_2));
		skillTab.refreshCurrentDocument(supportDriver);
		assertTrue(skillTab.skillExist(supportDriver, skill_1));
		assertTrue(skillTab.skillExist(supportDriver, skill_2));
		System.out.println("Test Case--Verify duplicate skill with same name is not updated--Passed");

		// Try to update skill with new skill value
		System.out.println("Test Case--Verify able to update skill with new skill--Started");
		assertFalse(skillTab.updateSkill(supportDriver, skill_1, skill_3));
		assertTrue(skillTab.skillExist(supportDriver, skill_3));
		assertFalse(skillTab.skillExist(supportDriver, skill_1));
		System.out.println("Test Case--Verify able to update skill with new skill--Passed");

		System.out.println("Test Case--Verify Skill associated with active call flow is not deleted--Started");
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertFalse(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_3));
		System.out.println("Test Case--Verify Skill associated with active call flow is not deleted--Passed");

		System.out.println("Test Case--Delete an existing skill not associated with active call flow and --Started");
		assertTrue(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_2));
		skillTab.deleteSkill(supportDriver, skill_2);
		assertFalse(skillTab.skillExist(supportDriver, skill_2));

		System.out.println("Test Case--Delete an existing skill not associated with active call flow and --Passed");

		System.out.println("Test Case--deassociate call flow from Skill and Delete that skill --Started");

		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"), "");
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"));
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"), "");
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"));
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		assertTrue(skillTab.deleteOptionForSkillAvailable(supportDriver, skill_3));
		skillTab.deleteSkill(supportDriver, skill_3);
		assertFalse(skillTab.skillExist(supportDriver, skill_3));

		driverUsed.put("supportDriver", false);
		System.out.println("Test Case--deassociate call flow from Skill and Delete that skill --Passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_add_btn_disabled_when_no_skill_added() {

		System.out.println("Test Case--verify_add_btn_disabled_when_no_skill_added--Started");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);

		// removing skills from call flow
		if (skillTab.getCallFlowSkillLength(supportDriver) >= 1) {
			dashboard.navigateToManageCallFlow(supportDriver);
			callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"), "");
			callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_1"));
			callFlowPage.removeAllCallFlowSteps(supportDriver);
			dashboard.navigateToManageCallFlow(supportDriver);
			callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"), "");
			callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("skill_call_flow_2"));
			callFlowPage.removeAllCallFlowSteps(supportDriver);
		}
		
		//deleting all skills
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);
		skillTab.deleteAllSkills(supportDriver);

		//verifying add skills icon disabled
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(userPage.isAddUserSkillsIconDisabled(supportDriver));
		
		//verifying in call flow no skills present
		dashboard.navigateToAddNewCallFlow(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		assertTrue(callFlowPage.isDialTypeGroupsListEmpty(supportDriver, CallFlowPage.DialCallRDNATOCat.Skill));
	
		driverUsed.put("supportDriver", false);		
		System.out.println("Test Case--verify_add_btn_disabled_when_no_skill_added--passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_same_skill_not_added_for_user_multiple_times() {

		System.out.println("Test Case--verify_same_skill_not_added_for_user_multiple_times--Started");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);

		String skillName = "AutoSkill".concat(HelperFunctions.GetRandomString(3));
		
		//deleting all skills
		skillTab.deleteAllSkills(supportDriver);
		skillTab.addNewSkill(supportDriver, skillName);
		
		//adding skill on user page
		dashboard.clickOnUserProfile(supportDriver);
		userPage.clickToAddSkill(supportDriver);
		userPage.addSkill(supportDriver, skillName, null);
		assertTrue(userPage.isUserSkillPresent(supportDriver, skillName));
		
		//adding again the same skill and verifying
		userPage.verifyDuplicateSkillMsg(supportDriver, skillName);
		userPage.removeSkillFromUser(supportDriver, skillName);
		assertFalse(userPage.isUserSkillPresent(supportDriver, skillName));
		
		driverUsed.put("supportDriver", false);		
		System.out.println("Test Case--verify_same_skill_not_added_for_user_multiple_times--passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_sorting_based_on_skill_name() {

		System.out.println("Test Case--verify_sorting_based_on_skill_name--Started");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		skillTab.navigateToAccountSkillsTab(supportDriver);

		String skillName1 = "AutoSkill".concat(HelperFunctions.GetRandomString(4));
		String skillName2 = "AutoSkill".concat(HelperFunctions.GetRandomString(4));
		String skillName3 = "AutoSkill".concat(HelperFunctions.GetRandomString(4));
		
		//deleting all skills
		skillTab.deleteAllSkills(supportDriver);
		
		//adding skills
		skillTab.addNewSkill(supportDriver, skillName1);
		skillTab.addNewSkill(supportDriver, skillName2);
		skillTab.addNewSkill(supportDriver, skillName3);
		
		assertTrue(skillTab.verifyAscendingSkillsNameList(supportDriver));
		assertTrue(skillTab.verifyDescendingSkillsNameList(supportDriver));
		
		driverUsed.put("supportDriver", false);		
		System.out.println("Test Case--verify_sorting_based_on_skill_name--passed");
	}
}
