package softphone.cases.team;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Ordering;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneTeamPage;


public class TeamSearchAndFilters extends SoftphoneBase{
	
	@Test(priority=501, groups={"Regression"}) 
	public void search_team_members_with_name() {
		
		System.out.println("---------------- Test Case search_team_agents Started ------------------");
	
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));

		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
	
		//Verifying default selected status
		assertEquals(softPhoneTeamPage.getSelectedStatus(driver1), SoftPhoneTeamPage.Status.all.toString());
		
		//Verifying default selected sorting
		assertEquals(softPhoneTeamPage.getSelectedSorting(driver1), SoftPhoneTeamPage.Sort.recent.toString());

		//Verifying that agent is visible there
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), true);
		
		//searching user 
		softPhoneTeamPage.searchUser(driver1, agentName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);

		System.out.println("Test Case is Pass");
	}
	
	
	@Test(priority=502, groups={"Regression"})
	public void sort_team_members() {
		
		System.out.println("---------------- Test Case search_team_agents Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//getting names of agents
		String agentName 	= CONFIG.getProperty("qa_user_3_name");
		String agentName1 	= CONFIG.getProperty("qa_user_2_name");
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);

		//Setting sorting to ascending order and verifying the results
		softPhoneTeamPage.setSorting(driver1, SoftPhoneTeamPage.Sort.asc);
		System.out.println("List in desc is = "+ softPhoneTeamPage.getAgentsFirstName(driver1));
		assertEquals(Ordering.natural().isOrdered(softPhoneTeamPage.getAgentsFirstName(driver1)), true);
		assertEquals(Ordering.natural().reverse().isOrdered(softPhoneTeamPage.getAgentsFirstName(driver1)), false);
		
		//Setting sorting to descending order and verifying the results
		softPhoneTeamPage.setSorting(driver1, SoftPhoneTeamPage.Sort.desc);
		System.out.println("List in desc is = "+ softPhoneTeamPage.getAgentsFirstName(driver1));
		assertEquals(Ordering.natural().isOrdered(softPhoneTeamPage.getAgentsFirstName(driver1)), false);
		assertEquals(Ordering.natural().reverse().isOrdered(softPhoneTeamPage.getAgentsFirstName(driver1)), true);
		
		//Making an outbound call from qa user 3
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));
		
		//picking up call from softphone
		softPhoneCalling.pickupIncomingCall(driver5);

		//setting filter to online 
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.online);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), true);
		
		//Searching the on call agent with name 
		softPhoneTeamPage.searchUser(driver1, agentName);
		
		//listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);
		
		//Disconnect listening
		softPhoneTeamPage.disconnectListening(driver1, agentName);
		
		//Clicking on monitor
		softPhoneTeamPage.clickMonitor(driver1, agentName);
		
		//Verifying monitoring is fine
		seleniumBase.idleWait(5);
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
		
		//Hanging up call from softphone
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//navigating to contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);

		//setting sorting to recent in team
		softPhoneTeamPage.setSorting(driver1, SoftPhoneTeamPage.Sort.recent);
		
		//verifying that qa user 3 is showing at first position and second at third 
		softPhoneTeamPage.getAgents(driver1).get(0).contains(agentName);
		softPhoneTeamPage.getAgents(driver1).get(1).contains(agentName1);
		
		//Making an outbound call from qa user 3
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_3_number"));
		
		//picking up call from softphone
		softPhoneCalling.pickupIncomingCall(driver6);

		//Hanging up call from softphone
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver6);

		//verifying that qa user 3 is showing at first position and second at third 
		softPhoneTeamPage.getAgents(driver1).get(0).contains(agentName1.trim());
		softPhoneTeamPage.getAgents(driver1).get(1).contains(agentName.trim());

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");
		
	}
		
	@Test(priority=503, groups={"Regression"})
	public void search_team_members_by_status(){
		
		System.out.println("---------------- Test Case search_team_agents Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));

		//getting names of agents
		String agentName 	= CONFIG.getProperty("qa_user_3_name");
		String agentName1 	= CONFIG.getProperty("qa_user_2_name");
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
			
		//Verifying that status of the members are visible as online
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.Available.toString());
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName1), SoftPhoneTeamPage.Status.Available.toString());

		//logging out from qa 2 user
		softPhoneSettingsPage.logoutSoftphone(driver4);
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
				
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Verifying that status of the 2nd user is visible as offline now
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName1), SoftPhoneTeamPage.Status.Offline.toString());
	
		//Verifying that status of the 1st user is visible as offline now
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.Available.toString());
				
		//selecting status filter to online 
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.online);
	
		//Verifying that status of the 2 member is visible as offline now
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.Available.toString());
		
		//verifying that offline agent is not visible
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName1), false);

		//selecting status filter to online 
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.offline);
	
		//Verifying that status of the 2 member is visible as offline now
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName1), SoftPhoneTeamPage.Status.Offline.toString());
		
		//verifying that offline agent is not visible
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), false);

		//login to Softphone
		SFLP.softphoneLogin(driver4, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Make user as supervisor of Team, verify Team in filter dropdown on Team page
	//By default ALL Team members should come on Team page without any filter
	//Select any Team in filter dropdown, verify all members of Team should filter in Team
	@Test(priority=504, groups={"Regression"})
	public void verify_team_present_in_dropdown(){
		
		System.out.println("---------------- Test Case verify_team_present_in_dropdown Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		
		loginSupport(driver1);
		
		String teamName = CONFIG.getProperty("qa_group_4_name");
		
		//Opening group tab
		groupsPage.openGroupSearchPage(driver1);

		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_4_name"), CONFIG.getProperty("qa_user_account"));

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, false);
		
		//adding members if not already added
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
			
		//adding supervisor if not already added
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//getting names of agents
		String agentName 	= CONFIG.getProperty("qa_user_3_name");
		String agentName1 	= CONFIG.getProperty("qa_user_2_name");
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Verify that by default all option is selected
		String selectedTeam = softPhoneTeamPage.getSelectedTeam(driver1);
		assertEquals(selectedTeam, "All");
		List<String> agents = softPhoneTeamPage.getAgents(driver1);
		assertTrue(agents.size() > 2);
			
		//Set the above team in team filter. It verifies that team is present in the dropdown
		softPhoneTeamPage.setTeam(driver1, teamName);
		
		//Verify that only team members of selected team is showing
		agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 2);
		assertTrue(agents.contains(agentName) && agents.contains(agentName1));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Make user as member of Team with open barge setting ON, verify Team in filter dropdown
	@Test(priority=505, groups={"Regression"})
	public void verify_team_open_barge_present_in_dropdown(){
		
		System.out.println("---------------- Test Case verify_team_open_barge_present_in_dropdown Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		
		loginSupport(driver1);
		
		String teamName = CONFIG.getProperty("qa_group_4_name");
		
		//Opening group tab
		groupsPage.openGroupSearchPage(driver1);

		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_4_name"), CONFIG.getProperty("qa_user_account"));

		//setting open barge to true
		groupsPage.setOpenBarge(driver1, true);
		
		//adding members if not already added
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
			
		//adding supervisor if not already added
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//getting names of agents
		String agentName 	= CONFIG.getProperty("qa_user_3_name");
		String agentName1 	= CONFIG.getProperty("qa_user_2_name");
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Set the above team in team filter.
		softPhoneTeamPage.setTeam(driver1, teamName);
		
		//Verify that only team members of selected team is showing
		List<String> agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 2);
		assertTrue(agents.contains(agentName) && agents.contains(agentName1));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//filter by Team and Status combined - verify result
	//Filter by Team and filter by On Call- one team member come on call- should visible in result dynamically
	@Test(priority=506, groups={"Regression"})
	public void verify_team_and_status_filter(){
		
		System.out.println("---------------- Test Case verify_team_and_status_filter Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		
		loginSupport(driver1);
		
		String teamName = CONFIG.getProperty("qa_group_4_name");
		
		//Opening group tab
		groupsPage.openGroupSearchPage(driver1);

		//Opening group detail page
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_4_name"), CONFIG.getProperty("qa_user_account"));

		//setting open barge to false
		groupsPage.setOpenBarge(driver1, true);
		
		//adding members if not already added
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
			
		//adding supervisor if not already added
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_1_name"));
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//getting names of agents
		String agentName 	= CONFIG.getProperty("qa_user_3_name");
		String agentName1 	= CONFIG.getProperty("qa_user_2_name");
		
		//Opening contact seach page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Opening team section page
		softPhoneTeamPage.openTeamSection(driver1);
		
		//logging out from qa 2 user
		softPhoneSettingsPage.logoutSoftphone(driver4);
		
		//Set the above team in team filter
		//selecting status filter to online 
		softPhoneTeamPage.setTeam(driver1, teamName);
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.online);
		
		//Verify that only Available team members of selected team is showing
		List<String> agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 1);
		assertTrue(agents.contains(agentName));
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.Available.toString());
		
		//selecting status filter to Offline 
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.offline);
		
		//Verify that only offline team members of selected team is showing
		agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 1);
		assertTrue(agents.contains(agentName1));
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName1), SoftPhoneTeamPage.Status.Offline.toString());
		
		//selecting status filter to On Call and verify no user is appearing in the list
		softPhoneTeamPage.setStatus(driver1, SoftPhoneTeamPage.Status.OnCall);
		assertEquals(softPhoneTeamPage.getAgents(driver1), null);
		
		//Make an outbound call from Team's agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
		
		// Verify that the above agent who connects the call and the member of the
		// should start appearing in the list
		agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 1);
		assertTrue(agents.contains(agentName));
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.Connecting.toString());
				
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Verify that the above agent who connects the call and the member of the should start appearing in the list
		agents = softPhoneTeamPage.getAgents(driver1);
		assertEquals(agents.size(), 1);
		assertTrue(agents.contains(agentName));
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), SoftPhoneTeamPage.Status.OnCall.toString());
		
		//hangup active call
		softPhoneCalling.hangupActiveCall(driver3);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		resetApplication();
		driverUsed.put("driver4", false);
		
		System.out.println("Test Case is Pass");
	}
}