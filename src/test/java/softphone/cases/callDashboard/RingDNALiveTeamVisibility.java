/**
 * 
 */
package softphone.cases.callDashboard;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.users.UsersPage.UserRole;

/**
 * @author admin
 *
 */
public class RingDNALiveTeamVisibility extends SoftphoneBase{
	CallDashboardReact callDashboardReact = new CallDashboardReact();
	
	WebDriver agentDriver1 = null;
	WebDriver agentDriver2 = null;

	@BeforeClass(groups={"Regression", "Sanity"}) 
	public void openGroupDetails() {

		//Initializing drivers  
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", Boolean.valueOf(true));
		
		//Login agent into the softphone
		if(agentDriver1 == null){
			agentDriver1 = getDriver();
			SFLP.softphoneLogin(agentDriver1,CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
		}
		
		if(agentDriver2 == null){
			agentDriver2 = getDriver();
			SFLP.softphoneLogin(agentDriver2,CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		}

		//Login to support 
		loginSupport(webSupportDriver);

		//Opening group tab
		groupsPage.openGroupSearchPage(webSupportDriver);
		
		//delete agent to be supervisor from a group
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));

		//Opening group detail page
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_3"), CONFIG.getProperty("qa_user_account"));
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		
		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, false);

		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", false);

	}

	//Verify only members of team should be displayed to supervisor when open barge setting false of Team
	//Verify newly added members should be displayed in team to supervisor when open barge set to false for Team
	//Open barge false- member removed from Team - verify member removed from Team
	@Test(priority=10401, groups={"Regression"})
	public void ringdna_live_team_member_add_delete() 
	{		
		System.out.println("---------------- Test Case ringdna_live_team_member_add_delete Started ------------------");

		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 

		//Switching to support tab 
		seleniumBase.switchToTab(webSupportDriver, 2);

		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, false);

		//add user as supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
		
		//add one user as member
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));

		callDashboardReact.openCallDashBoardPage(agentDriver1);
		agentDriver1.navigate().refresh();
		
		//Verifying that member is visible 	on ringDNA live app
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_agent_user_name")));
		
		//verify that supervisor are not visible
		assertFalse(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")));
		assertFalse(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")));

		//adding new member to group
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));

		//Verifying that both members are visible 	
		agentDriver1.navigate().refresh();
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_agent_user_name")));
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")));

		//deleting newly added member from group
		groupsPage.deleteMember(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));

		//Verifying that member is visible 	
		agentDriver1.navigate().refresh();
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")));

		//Verifying removed agent is not visible
		assertFalse(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_agent_user_name")));

		System.out.println("Test Case is Pass");
	}
	
	//Verify no team tab should be visible to member of Team on softphone
	//Open Barge True- Login by Team Member- verify all remaining Members and supervisor into Team
	@Test(priority=10402,  groups={"Regression"}) 
	public void ringdna_live_team_visibility_for_groups_member() 
	{
		System.out.println("---------------- Test Case ringdna_live_team_visibility_for_groups_member Started ------------------");

		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 

		//Switching to support tab 
		seleniumBase.switchToTab(webSupportDriver, 2);
		
		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, false);

		//adding members if not already added
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));

		//adding supervisors if not already added
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));

		//deleting members or supervisor if added in group
		groupsPage.deleteAgentFromGroup(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));

		//Reloading softphone
		seleniumBase.openNewBlankTab(agentDriver2);
		seleniumBase.switchToTab(agentDriver2, seleniumBase.getTabCount(agentDriver2));
		agentDriver2.get(CONFIG.getProperty("call_dashboard_react_url"));

		//since agent is not the supervisor then no permission image and texts should appear
		callDashboardReactPage.verifyNoPermissionPage(agentDriver2);
		
		//set open barge setting as true
		groupsPage.setOpenBarge(webSupportDriver, true);

		//reloading softphone
		reloadSoftphone(agentDriver2);

		//verifying that in team agent itself should not visible
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_agent_user_name")), false);

		//Verifying that other members and supervisors are visible 
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_4_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_3_name")), true);

		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, false);
		
		//close agent driver and switch to softphone
		seleniumBase.closeTab(agentDriver2);
		seleniumBase.switchToTab(agentDriver2, 1);

		System.out.println("Test Case is Pass");

	}	
	
	//Verify all members and supervisor should be visible in Team to supervisor when open barge setting True
	//Open Barge True- new member added into group- verify members added into Team
	//Open Barge True- New supervisor added into Team- verify into LIVE
	//Open Barge true- existing supervisor removed from Team- should be removed from Team
	//Open barge True- existing member removed from Group- Removed from Team
	@Test(priority=10403,  groups={"Regression"})
	public void ringdna_live_team_visibility_for_group_supervisor_open_barge() 
	{ 

		System.out.println("---------------- Test Case ringdna_live_team_visibility_for_group_supervisor_open_barge Started ------------------");

		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 

		//Switching to support tab 
		seleniumBase.switchToTab(webSupportDriver, 2);

		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, true);

		//Adding members
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
		
		//Adding supervisors
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));
		
		//Deleting agents from group
		groupsPage.deleteAgentFromGroup(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));

		//Reloading softphone
		callDashboardReact.openCallDashBoardPage(agentDriver1);
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), false);

		//adding one member and checking visibility
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));

		//Opening team section 
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);

		//Removing member
		groupsPage.deleteAgentFromGroup(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));

		//Opening contact search page on agentDriver1
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);

		//adding supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));

		//Opening contact search page on agentDriver1
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);

		//Removing member
		groupsPage.deleteAgentFromGroup(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));

		//Opening contact search page on agentDriver1
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);

		//Setting up driver used to false state
		driverUsed.put("driver2", false);

		System.out.println("Test Case is Pass");


	}
	
	//Agent role who is supervisor of any team, verify members in Team section on softphone
	//Admin role who is supervisor of any team, verify members in Team section on softphone
	@Test(priority=10404,  groups={"Regression", "ExludeForProd"}) 
	public void ringdna_live_team_visibility_for_group_supervisor_for_all_roles() 
	{	
		System.out.println("---------------- Test Case ringdna_live_team_visibility_for_group_supervisor_for_all_roles Started ------------------");

		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 
		
		//Adding members
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
		
		//set agent 3 as supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));
		
		//open users page
		dashboard.openManageUsersPage(webSupportDriver);
		
		//opening user setting page
		usersPage.OpenUsersSettings(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_email"));

		//Setting user role to agent for agent 2
		usersPage.selectRole(webSupportDriver, UserRole.Agent);
		usersPage.saveUserOverviewPage(webSupportDriver);
		
		//Reloading softphone
		seleniumBase.openNewBlankTab(agentDriver2);
		seleniumBase.switchToTab(agentDriver2, seleniumBase.getTabCount(agentDriver2));
		agentDriver2.get(CONFIG.getProperty("call_dashboard_react_url"));

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_3_name")), true);
 
		//Setting user role to agent for agent 2
		usersPage.selectRole(webSupportDriver, UserRole.Admin);
		usersPage.saveUserOverviewPage(webSupportDriver);
		
		//Reloading softphone
		reloadSoftphone(agentDriver2);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_3_name")), true);
			
		//Setting user role to agent for agent 2
		usersPage.selectRole(webSupportDriver, UserRole.Support);
		usersPage.saveUserOverviewPage(webSupportDriver);
		
		//Reloading softphone
		reloadSoftphone(agentDriver2);

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver2, CONFIG.getProperty("qa_user_3_name")), true);
		
		//Setting user role to agent for agent 2
		usersPage.selectRole(webSupportDriver, UserRole.Agent);
		usersPage.saveUserOverviewPage(webSupportDriver);
		
		//Opening group tab
		groupsPage.openGroupSearchPage(webSupportDriver);

		//Opening group detail page
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_3"), CONFIG.getProperty("qa_user_account"));
		
		System.out.println("Test Case is Pass");
		
	}
	
	//open barge false- Verify all members of groups when user is supervisor of multiple groups
	//open barge true- verify all members and supervisor of groups when user is supervisor of two groups
	@Test(priority=10405,  groups={"Regression"})
	public void ringdna_live_team_visibility_for_supervisor_of_multiple_groups() {
		
		System.out.println("---------------- Test Case ringdna_live_team_visibility_for_group_supervisor_open_barge Started ------------------");
	
		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", true);
		
		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 
		
		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, true);
		
		//adding agent as member
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
		
		//adding agent as supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		
		//Deleting other members of groups if added
		groupsPage.deleteAgentFromGroup(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
	
		//Opening another group
		groupsPage.openGroupSearchPage(webSupportDriver);
		
		//Opening group detail page
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_4"), CONFIG.getProperty("qa_user_account"));
	
		//Setting open barge as true
		groupsPage.setOpenBarge(webSupportDriver, true);
		
		//add agent 1 a supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		
		//add members
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
		
		//Opening team section 
		callDashboardReact.openCallDashBoardPage(agentDriver1);
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);

		//Opening group detail page
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_3"), CONFIG.getProperty("qa_user_account"));
		
		//setting open barge as false 
		groupsPage.setOpenBarge(webSupportDriver, false);
		
		groupsPage.openGroupSearchPage(webSupportDriver);
		
		//Opening group detail page
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_4"), CONFIG.getProperty("qa_user_account"));
	
		//Setting open barge as true
		groupsPage.setOpenBarge(webSupportDriver, false);
		
		//Opening contact search page on driver4
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);

		//switching to support tab again
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));
		
		//Opening contact search page on driver4
		agentDriver1.navigate().refresh();

		//Verifying all remaining members and supervisors are visible 	
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")), false);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")), true);
		assertEquals(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")), true);
		
		List<String> supervisors = groupsPage.getSupervisorsNameList(webSupportDriver);
		if(supervisors != null) {
			for (String agentName : supervisors) {
				groupsPage.deleteAgentFromGroup(webSupportDriver, agentName);
			}
		}
		
		List<String>  agents = groupsPage.getMembersNameList(webSupportDriver);
		if(agents != null) {
			for (String agentName : agents) {
				groupsPage.deleteAgentFromGroup(webSupportDriver, agentName);
			}
		}
		
		//Opening group detail page
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_3"), CONFIG.getProperty("qa_user_account"));
		
		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", false);

		System.out.println("Test Case is Pass");
	}
	
	//Open Barge False- Check the 'ListenIn' checkbox for any Team member- verify Team
	@Test(priority=10406,  groups={"Regression"}) 
	public void ringdna_live_team_visibility_for__listen_in_groups_member() 
	{
		System.out.println("---------------- Test Case ringdna_live_team_visibility_for__listen_in_groups_member Started ------------------");

		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 

		//Switching to support tab 
		seleniumBase.switchToTab(webSupportDriver, 2);

		//setting open barge to false
		groupsPage.setOpenBarge(webSupportDriver, false);

		//add user as supervisor
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_1_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		
		//add two users as member
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
		
		//enable listen in for one agent
		groupsPage.enableListenInMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));

		callDashboardReact.openCallDashBoardPage(agentDriver1);
		agentDriver1.navigate().refresh();
		
		//Verifying that member is visible 	on ringDNA live app
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_2_name")));
		assertTrue(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_3_name")));
		
		assertFalse(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_4_name")));
		assertFalse(callDashboardReactPage.isAgentInAnyList(agentDriver1, CONFIG.getProperty("qa_user_1_name")));
		
		//enable listen in for one agent
		groupsPage.disableListenInMember(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));

		System.out.println("Test Case is Pass");
	}	

	@AfterClass(groups={"Regression", "Sanity"}) 
	public void afterClass() {

		//Initializing drivers  
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", Boolean.valueOf(true));
		
		if(seleniumBase.getTabCount(webSupportDriver)<2) {
			openGroupDetails();
		} 
		
		List<String> supervisors = groupsPage.getSupervisorsNameList(webSupportDriver);
		if(supervisors.indexOf(CONFIG.getProperty("qa_user_2_name")) >= 0) {
			groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
			supervisors.remove(CONFIG.getProperty("qa_user_2_name"));
		}
		
		if(supervisors.indexOf(CONFIG.getProperty("qa_user_3_name")) >= 0) {
			groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
			supervisors.remove(CONFIG.getProperty("qa_user_3_name"));
		}
		
		for (String agentName : supervisors) {
			groupsPage.deleteSuperviosr(webSupportDriver, agentName);
		}
		
		List<String> agents = groupsPage.getMembersNameList(webSupportDriver);
		for (String agentName : agents) {
			groupsPage.deleteAgentFromGroup(webSupportDriver, agentName);
		}
		
		//Opening group detail page
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_call_dashboard_team_4"), CONFIG.getProperty("qa_user_account"));
		
		supervisors = groupsPage.getSupervisorsNameList(webSupportDriver);
		if(supervisors != null) {
			if(supervisors.indexOf(CONFIG.getProperty("qa_user_2_name")) >= 0) {
				groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_user_2_name"));
				supervisors.remove(CONFIG.getProperty("qa_user_2_name"));
			}
			
			if(supervisors.indexOf(CONFIG.getProperty("qa_user_3_name")) >= 0) {
				groupsPage.deleteSuperviosr(webSupportDriver, CONFIG.getProperty("qa_user_3_name"));
				supervisors.remove(CONFIG.getProperty("qa_user_3_name"));
			}
			
			for (String agentName : supervisors) {
					groupsPage.deleteSuperviosr(webSupportDriver, agentName);
			}
		}
		
		agents = groupsPage.getMembersNameList(webSupportDriver);
		if(agents != null) {
			for (String agentName : agents) {
				groupsPage.deleteAgentFromGroup(webSupportDriver, agentName);
			}
		}
		
		//delete agent to be supervisor from a group
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.addSupervisor(webSupportDriver, CONFIG.getProperty("qa_user_4_name"));
		
		//Setting user role to agent for agent 2
		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenUsersSettings(webSupportDriver, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_email"));
		usersPage.selectRole(webSupportDriver, UserRole.Agent);
		usersPage.saveUserOverviewPage(webSupportDriver);
		
		seleniumBase.closeTab(webSupportDriver);
		seleniumBase.switchToTab(webSupportDriver, 1);
		
		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", false);

	}
}