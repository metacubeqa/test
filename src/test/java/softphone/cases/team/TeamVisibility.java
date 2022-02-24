package softphone.cases.team;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.users.UsersPage.UserRole;


public class TeamVisibility extends SoftphoneBase{
		
	@BeforeClass(groups={"Regression", "Sanity"}) 
	public void openGroupDetails() {

		//Initializing drivers  
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));

		//Opening new blank tab
		seleniumBase.openNewBlankTab(driver1);		

		//switching to new opened tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));

		//Login to support 
		SFLP.supportLoginWhenSoftphoneLogin(driver1, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));

		//Opening group tab
		groupsPage.openGroupSearchPage(driver1);

		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, false);

		//Setting up driver used to false state
		driverUsed.put("driver1", false);

	}

	@Test(priority=301,  groups={"Regression"}) 
	public void team_visibility_for_groups_member() 
	{
		System.out.println("---------------- Test Case team_visibility_for_groups_member Started ------------------");

		if(seleniumBase.getTabCount(driver1)<2) {
			openGroupDetails();
		} 
		
		//Initializing drivers  
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));

		//Switching to support tab 
		seleniumBase.switchToTab(driver1, 2);
		
		//setting open barge to false
		groupsPage.setOpenBarge(driver1, false);

		//adding members if not already added
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));

		//adding supervisors if not already added
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));

		//deleting members or supervisor if added in group
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Reloading softphone
		reloadSoftphone(driver4);

		//verifying that team should not be visible 
		assertEquals(softPhoneTeamPage.isTeamSectionVisible(driver4), false);

		//set open barge setting as true
		groupsPage.setOpenBarge(driver1, true);

		//reloading softphone
		reloadSoftphone(driver4);

		//verifying that team should be visible now
		assertEquals(softPhoneTeamPage.isTeamSectionVisible(driver4), true);

		//Opening team section
		softPhoneTeamPage.openTeamSection(driver4);

		//verifying that in team agent itself should not visible
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);

		//Verifying that other members and supervisors are visible 
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, false);

		//Setting up driver used to false state
		driverUsed.put("driver2", false);

		System.out.println("Test Case is Pass");

	}	

	@Test(priority=302,  groups={"Regression"})
	public void team_visibility_for_group_supervisor() 
	{		
		System.out.println("---------------- Test Case team_visibility_for_group_supervisor Started ------------------");

		if(seleniumBase.getTabCount(driver1)<2) {
			openGroupDetails();
		} 

		//Initializing drivers  
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));

		//Switching to support tab 
		seleniumBase.switchToTab(driver1, 2);

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, false);

		//adding agent as supervisor
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));

		//adding another agent as member
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));

		//Deleting other members of groups if added
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Reloading softphone
		reloadSoftphone(driver4);

		//verifying that team should be visible 
		assertEquals(softPhoneTeamPage.isTeamSectionVisible(driver4), true);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying that member is visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);

		//Verifying remaining supervisor is not visible
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), false);

		//adding new member to group
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying member visibility
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), true);

		//Verifying supervisor visibility
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);

		//deleting newly added member from group
		groupsPage.deleteMember(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying member visibility
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), false);

		//Verifying supervisor visibility
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);

		//Setting up driver used to false state
		driverUsed.put("driver2", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=303,  groups={"Regression"})
	public void team_visibility_for_group_supervisor_open_barge() 
	{ 

		System.out.println("---------------- Test Case team_visibility_for_group_supervisor_open_barge Started ------------------");

		if(seleniumBase.getTabCount(driver1)<2) {
			openGroupDetails();
		} 

		//Initializing drivers  
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		
		//Switching to support tab 
		seleniumBase.switchToTab(driver1, 2);

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, true);

		//Adding members
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
		//Adding supervisors
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		//Deleting agents from group
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Reloading softphone
		reloadSoftphone(driver4);

		//opening team section
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), false);

		//adding one member and checking visibility
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_4_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), true);

		//Removing member
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_3_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), true);

		//adding supervisor
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_3_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), true);

		//Removing member
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_1_name"));

		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver4);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver4);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_2_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver4, CONFIG.getProperty("qa_user_4_name")), true);

		//Setting up driver used to false state
		driverUsed.put("driver2", false);

		System.out.println("Test Case is Pass");


	}


	@Test(priority=304,  groups={"Regression"})
	public void team_visibility_for_supervisor_of_multiple_groups() {
		
		System.out.println("---------------- Test Case team_visibility_for_group_supervisor_open_barge Started ------------------");
	
		//Setting up driver used to false state
		driverUsed.put("driver1", true);
		
		if(seleniumBase.getTabCount(driver1)<2) {
			openGroupDetails();
		} 

		//Switching to support tab 
		seleniumBase.switchToTab(driver1, 2);
		
		//setting open barge to false
		groupsPage.setOpenBarge(driver1, true);
		
		//adding agent as member
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		//adding agent as supervisor
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_4_name"));
		//Deleting other members of groups if added
		groupsPage.deleteAgentFromGroup(driver1, CONFIG.getProperty("qa_user_3_name"));
	
		//Opening another group
		groupsPage.openGroupSearchPage(driver1);
		
		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_1_name"), CONFIG.getProperty("qa_user_account"));
	
		//Setting open barge as true
		groupsPage.setOpenBarge(driver1, true);
		
		//add agent 1 a supervisor
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		//add members
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
		
		//switch to softphone tab
		seleniumBase.switchToTab(driver1, 1);
		
		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver1);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_4_name")), true);

		//switching to support tab again
		seleniumBase.switchToTab(driver1, 2);
		
		//setting open barge as false 
		groupsPage.setOpenBarge(driver1, false);
		
		groupsPage.openGroupSearchPage(driver1);
		
		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
	
		//Setting open barge as true
		groupsPage.setOpenBarge(driver1, false);
		
		//switch to softphone tab
		seleniumBase.switchToTab(driver1, 1);
		
		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver1);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_4_name")), false);

		//switching to support tab again
		seleniumBase.switchToTab(driver1, 2);
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_4_name"));
		
		//switch to softphone tab
		seleniumBase.switchToTab(driver1, 1);
		
		//Opening contact search page on driver4
		softPhoneContactsPage.clickActiveContactsIcon(driver1);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, CONFIG.getProperty("qa_user_4_name")), true);
		
		//Setting up driver used to false state
		driverUsed.put("driver1", false);

		System.out.println("Test Case is Pass");
	}


	@Test(priority=305,  groups={"Regression", "ExludeForProd"}) 
	public void team_visibility_for_group_supervisor_for_all_roles() 
	{	
		System.out.println("---------------- Test Case team_visibility_for_group_supervisor_for_all_roles Started ------------------");

		if(seleniumBase.getTabCount(driver1)<2) {
			openGroupDetails();
		} 

		//Initializing drivers  
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));

		//Switching to support tab 
		seleniumBase.switchToTab(driver1, 2);
		
		//Adding members
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_4_name"));
		//set agent 3 as supervisor
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_3_name"));
		
		//open users page
		dashboard.openManageUsersPage(driver1);
		
		//opening user setting page
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("qa_user_3_email"));

		//Setting user role to agent for agent 2
		usersPage.selectRole(driver1, UserRole.Agent);
		usersPage.saveUserOverviewPage(driver1);
		
		//Reloading softphone
		reloadSoftphone(driver3);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver3);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_4_name")), true);
 
		//Setting user role to agent for agent 2
		usersPage.selectRole(driver1, UserRole.Admin);
		usersPage.saveUserOverviewPage(driver1);
		
		//Reloading softphone
		reloadSoftphone(driver3);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver3);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_4_name")), true);
			
		//Setting user role to agent for agent 2
		usersPage.selectRole(driver1, UserRole.Support);
		usersPage.saveUserOverviewPage(driver1);
		
		//Reloading softphone
		reloadSoftphone(driver3);

		//Opening team section 
		softPhoneTeamPage.openTeamSection(driver3);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver3, CONFIG.getProperty("qa_user_4_name")), true);
		
		//setting driver user to false state
		driverUsed.put("driver3", Boolean.valueOf(false));
				
		System.out.println("Test Case is Pass");
		
	}
	
	@AfterClass(groups={"Regression", "Sanity"}) 
	public void closeSupport() {
	
		//Setting driver used
		driverUsed.put("driver1", Boolean.valueOf(true));
		
		//Closing support tab from driver1
		seleniumBase.closeTab(driver1);

		//Switching to Softphone tab
		seleniumBase.switchToTab(driver1, 1);
		
		//Setting up driver used to false state
		driverUsed.put("driver1", false);
	
	}

}
