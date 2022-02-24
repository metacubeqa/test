package support.cases.teamsCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.SoftPhoneCallQueues;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftPhoneTeamPage;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.Dashboard;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class TeamsCases extends SupportBase{

	Dashboard dashboard = new Dashboard();
	SoftPhoneSettingsPage softphoneSettings = new SoftPhoneSettingsPage();
	SoftPhoneCallQueues softPhoneCallQueues = new SoftPhoneCallQueues();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new  UserIntelligentDialerTab();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	GroupsPage groupsPage = new GroupsPage();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	UsersPage usersPage = new UsersPage();
	AddSmartNumberPage addSmartNoPage = new AddSmartNumberPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	SoftPhoneTeamPage softPhoneTeamPage = new SoftPhoneTeamPage();
	
	private String qa_user_name;
	private String qa_user_email;
	private String qa_user_account;
	private String qa_salesForce_id;
	private String teamToSearch;
	private String teamToSearchEmail;
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String teamsFileNameDownload = "teams";
	
	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			qa_user_name = CONFIG.getProperty("qa_admin_user_name");
			qa_user_email = CONFIG.getProperty("qa_admin_user_email");
			qa_salesForce_id = CONFIG.getProperty("qa_admin_user_salesforce_id");
			qa_user_account = "";
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			qa_user_name = CONFIG.getProperty("qa_support_user_name");
			qa_user_email = CONFIG.getProperty("qa_support_user_email");
			qa_salesForce_id = CONFIG.getProperty("qa_support_user_salesforce_id");
			qa_user_account = CONFIG.getProperty("qa_user_account");
		}
		teamToSearch = CONFIG.getProperty("qa_user_to_search");
		teamToSearchEmail = CONFIG.getProperty("qa_user_to_search_email");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		driverUsed.put("supportDriver", false);
	}
	
	@Test(groups = { "Regression" })
	public void other_user_team_voicemail_not_visible() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();
		
		//updating the supportDriver used
		System.out.println("Test case --other_user_team_voicemail_not_visible-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addMember(supportDriver, CONFIG.getProperty("qa_user_3_name"));
		String voiceMailName = "AutoVoiceMailGrp".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.getVoiceMailIndex(supportDriver, voiceMailName);
		int voiceMailIndex = softphoneSettings.getVoiceMailIndex(supportDriver, voiceMailName);
		assertEquals(-1, voiceMailIndex, String.format("Voicemail drop: %s exists even after removing member", voiceMailName));
		
		//verifying group details
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickFirstGroup(supportDriver);
		groupsPage.verifyGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		
		// deleting group
		groupsPage.deleteGroup(supportDriver);
		
		//after delete group assertions
		groupsPage.verifyAfterDeleteGroupAssertions(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --other_user_team_voicemail_not_visible-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void add_update_delete_voicemail_drop_for_teams() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_voicemail_drop_for_teams-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, CONFIG.getProperty("new_qa_automation_team"), qa_user_account);
		HelperFunctions.deletingExistingFiles(downloadPath, teamsFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		groupsPage.waitForFileToDownload(supportDriver, downloadPath, teamsFileNameDownload, ".csv");
		boolean fileDownloaded = groupsPage.isFileDownloaded(downloadPath, teamsFileNameDownload, ".csv");
		assertEquals(fileDownloaded, true, "file is not downloaded");
		
		groupsPage.clickFirstGroup(supportDriver);
		
		// adding voicemail drop by recording file
		String voiceMailRecord = "AutoVoiceMailGrpRecord".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMail(supportDriver, voiceMailRecord, 5);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailRecord);
		softphoneSettings.verifyGroupVoicemailDropOnDialer(supportDriver, voiceMailRecord);
		groupsPage.switchToTab(supportDriver, 2);
		
		// adding voicemail drop by uploading file
		System.out.println("Starting adding voicemail drop");
		String voiceMailName = "AutoVoiceMailGrp".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
		softphoneSettings.verifyGroupVoicemailDropOnDialer(supportDriver, voiceMailName);
		groupsPage.switchToTab(supportDriver, 2);
	
		// updating voicemail drop
		System.out.println("Starting updating voicemail drop");
		String voiceMailNameUpdate = "AutoVoiceMailGrpUpdate".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.updateVoiceMailRecordsByUploadingFile(supportDriver, voiceMailName, voiceMailNameUpdate);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailNameUpdate);
		softphoneSettings.verifyGroupVoicemailDropOnDialer(supportDriver, voiceMailNameUpdate);
		groupsPage.switchToTab(supportDriver, 2);

		// deleting voicemail drop
		System.out.println("Starting deleting voicemail drop");
		accountIntelligentTab.deleteRecords(supportDriver, voiceMailNameUpdate);
		boolean voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailNameUpdate);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailNameUpdate));
		
		// deleting voicemail drop recording
		accountIntelligentTab.deleteRecords(supportDriver, voiceMailRecord);
		voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailRecord);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailRecord));
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailNameUpdate));
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailRecord));
		softphoneSettings.switchToTab(supportDriver, 2);
		groupsPage.switchToTab(supportDriver, 2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_voicemail_drop_for_teams-- passed ");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void add_update_delete_teams_operations() {
		
		//closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_teams_operations-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//deleting teams if present for team to search
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, teamToSearch, teamToSearchEmail, CONFIG.getProperty("qa_user_to_search_salesforce_id"));
		usersPage.deleteAllTeams(supportDriver);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding member and supervisor
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.setOpenBarge(supportDriver, true);
		groupsPage.addMember(supportDriver, CONFIG.getProperty("qa_admin_user_name"));
		groupsPage.addMember(supportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.addMember(supportDriver, teamToSearch);
		groupsPage.addSupervisor(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		
		//adding voicemail and saving group
		System.out.println("Starting adding voicemail drop");
		String voiceMailName = "AutoVoiceMailGrp".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		groupsPage.saveGroup(supportDriver);
		
		//verifying group details
		groupsPage.verifyGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		assertEquals(groupsPage.getBargeValue(supportDriver), true);
		
		//searching created group and deleting supervisor
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickFirstGroup(supportDriver);
		groupsPage.deleteSuperviosr(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		groupsPage.saveGroup(supportDriver);
		
		// verifying team to search is present in teams in softphone
		groupsPage.switchToTab(supportDriver, 1);
		softPhoneTeamPage.closeSupervisorNotes(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertTrue(softPhoneTeamPage.isTeamMemberVisible(supportDriver, teamToSearch));

		// verifying after deleting group
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.deleteGroup(supportDriver);
		
		// softphone verification in queues and voicemail section
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		int deleteIndex = softphoneSettings.getVoiceMailIndex(supportDriver, voiceMailName);
		assertEquals(-1, deleteIndex, String.format("Voicemail drop: %s exists even after deleting", voiceMailName));
		assertFalse(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, teamDetailName), String.format("Group name:%s exists in queue section after deleting group", teamDetailName));
	
		//verifying team to search is not visible in teams section
		softPhoneTeamPage.closeSupervisorNotes(supportDriver);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertFalse(softPhoneTeamPage.isTeamMemberVisible(supportDriver, teamToSearch));
		
		// verifying Group membership in OverView Tab
		groupsPage.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(supportDriver);
		assertFalse(usersPage.isTeamExists(supportDriver, teamDetailName), String.format("Group:%s saved in group membership even after deleting", teamDetailName));
		
		// verifying in Intelligence dialer Tab
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		groupsPage.idleWait(2);
		assertFalse(accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailName), String.format("voice mail: %s exists after deleting group", voiceMailName));
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_teams_operations-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_create_call_queue_from_existing_team() {
		// updating the supportDriver used
		System.out.println("Test case --verify_create_call_queue_from_existing_team-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding member and supervisor
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addMember(supportDriver, CONFIG.getProperty("qa_user_3_name"));
		groupsPage.saveGroup(supportDriver);
		
		// Clicking create queue link and verifying details
		groupsPage.clickCreateQueueLink(supportDriver);
		callQueuePage.verifyAfterCreateCallQueueDetails(supportDriver, teamDetailName, CONFIG.getProperty("qa_user_3_name"));
		
		//searching from call queue page and deleting queue
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, teamDetailName.concat(" Queue"), qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, teamDetailName.concat(" Queue"));
		callQueuePage.deleteCallQueue(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_create_call_queue_from_existing_team-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_team_not_cloned_multiple_time() {
		// updating the supportDriver used
		System.out.println("Test case --verify_team_not_cloned_multiple_time-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//cloning team and verified it is copied
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		String headerText = groupsPage.clickCloneTeamLink(supportDriver);
		assertEquals(headerText, "Team copied.");
		
		//cloning team and verify already exists msg
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		headerText = groupsPage.clickCloneTeamLink(supportDriver);
		assertEquals(headerText, String.format("A team with the name %s Copy already exists", teamDetailName));
		
		//deleting group
		groupsPage.deleteGroup(supportDriver);
		
		//deleting copied team
		String teamCopied = teamDetailName.concat(" Copy"); 
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamCopied, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamCopied);
		groupsPage.deleteGroup(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_team_not_cloned_multiple_time-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_voicemail_add_remove_supervisor_to_team() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --verify_voicemail_add_remove_supervisor_to_team-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding voicemail to team
		String voiceMailName = "AutoVoicemailTeam".concat(HelperFunctions.GetRandomString(2));
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addSupervisor(supportDriver, qa_user_name);
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
	
		//deleting superviosr
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.deleteSuperviosr(supportDriver, qa_user_name);
		groupsPage.saveGroup(supportDriver);
		
		// softphone verification after deleting supervisor
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailName));
		
		//deleting group
		usersPage.switchToTab(supportDriver, 2);
		groupsPage.deleteGroup(supportDriver);
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_voicemail_add_remove_supervisor_to_team-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_voicemail_details_after_restore_team() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --verify_voicemail_details_after_restore_team-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding voicemail to team
		String voiceMailName = "AutoVoicemailTeam".concat(HelperFunctions.GetRandomString(2));
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addSupervisor(supportDriver, qa_user_name);
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		
		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
	
		//verifying on user overview tab after creating
		usersPage.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(usersPage.isTeamExists(supportDriver, teamDetailName));
		
		//deleting team
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);
		
		// verifying on user overview tab after deleting
		dashboard.clickOnUserProfile(supportDriver);
		assertFalse(usersPage.isTeamExists(supportDriver, teamDetailName));

		// softphone verification after deleting team
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailName));
		
		// restoring team
		usersPage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.restoreGroup(supportDriver);
	
		// verifying on user overview tab after restoring
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(usersPage.isTeamExists(supportDriver, teamDetailName));

		// softphone verification
		groupsPage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
	
		// deleting team
		usersPage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_voicemail_details_after_restore_team-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_restored_team_on_users_tab_and_softphone() {
		// updating the supportDriver used
		System.out.println("Test case --verify_restored_team_on_users_tab_and_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//deleting teams if present for team to search
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, teamToSearch, teamToSearchEmail, CONFIG.getProperty("qa_user_to_search_salesforce_id"));
		usersPage.deleteAllTeams(supportDriver);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding member and supervisor
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.setOpenBarge(supportDriver, true);
		groupsPage.addMember(supportDriver, qa_user_name);
		groupsPage.addMember(supportDriver, teamToSearch);
		groupsPage.saveGroup(supportDriver);
		
		// deleting group
		groupsPage.deleteGroup(supportDriver);
		groupsPage.verifyAfterDeleteGroupAssertions(supportDriver);
		
		// verifying team does not exists after deleting
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, teamToSearch, teamToSearchEmail, CONFIG.getProperty("qa_user_to_search_salesforce_id"));
		usersPage.openOverViewTab(supportDriver);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(supportDriver);
		assertFalse(usersPage.isTeamExists(supportDriver, teamDetailName), String.format("Team:%s exists in users page after deleting", teamDetailName));
		
		// verifying team to search is not present in teams in softphone
		groupsPage.switchToTab(supportDriver, 1);
		softPhoneTeamPage.closeSupervisorNotes(supportDriver);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertFalse(softPhoneTeamPage.isTeamMemberVisible(supportDriver, teamToSearch));

		//restoring team
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.restoreGroup(supportDriver);
		groupsPage.verifyAfterRestoreGroupAssertions(supportDriver);

		//verifying team exists after restoring
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, teamToSearch, teamToSearchEmail, CONFIG.getProperty("qa_user_to_search_salesforce_id"));
		usersPage.openOverViewTab(supportDriver);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(supportDriver);
		assertTrue(usersPage.isTeamExists(supportDriver, teamDetailName), String.format("Team:%s does not exists in users page after restoring", teamDetailName));
		
		// verifying team to search is present in teams in softphone
		groupsPage.switchToTab(supportDriver, 1);
		softPhoneTeamPage.closeSupervisorNotes(supportDriver);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertTrue(softPhoneTeamPage.isTeamMemberVisible(supportDriver, teamToSearch));

		// deleting team
		groupsPage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_restored_team_on_users_tab_and_softphone-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_clone_team_functionality() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();
		
		// updating the supportDriver used
		System.out.println("Test case --verify_clone_team_functionality-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding member and supervisor
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.setOpenBarge(supportDriver, true);
		groupsPage.addMember(supportDriver, qa_user_name);
		groupsPage.addMember(supportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.addSupervisor(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		
		//adding call reports user and mail
		String mail = "AutoMail".concat(HelperFunctions.GetRandomString(3)).concat("@ringdna.com");
		groupsPage.selectUserFromCallReportNotification(supportDriver, qa_user_name);
		groupsPage.selectUserFromCallReportNotification(supportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.clickHeaderTeamDetails(supportDriver);
		groupsPage.addCallReportsCCMail(supportDriver, mail);
		
		//adding voicemail
		String voiceMailName = "AutoVoiceMailGrp".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		
		//unchecking dispositions and saving team
		groupsPage.unCheckInboundCallDisposition(supportDriver, "Set Appointment");
		groupsPage.unCheckOutboundCallDisposition(supportDriver, "Wrong Number");
		groupsPage.saveGroup(supportDriver);

		//clicking clone link
		groupsPage.clickCloneTeamLink(supportDriver);

		//verifying voicemail
		assertTrue(accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailName));
		
		//verifying clone details
		groupsPage.verifyGroupDetails(supportDriver, teamDetailName.concat(" Copy"), teamDetailDesc);
		assertTrue(groupsPage.isAgentAddedAsMember(supportDriver, qa_user_name));
		assertTrue(groupsPage.isAgentAddedAsMember(supportDriver, CONFIG.getProperty("qa_agent_user_name")));
		assertTrue(groupsPage.isAgentAddedAsSupervisor(supportDriver, CONFIG.getProperty("qa_admin_user2_name")));
		
		//verifying call reports users and mail
		assertTrue(groupsPage.isListContainsText(supportDriver, groupsPage.getCallReportsNotificationItems(supportDriver), qa_user_name));
		assertTrue(groupsPage.isListContainsText(supportDriver, groupsPage.getCallReportsNotificationItems(supportDriver), CONFIG.getProperty("qa_agent_user_name")));
		assertTrue(groupsPage.getCallReportsMail(supportDriver).contains(mail));
		
		//verifying dispositions unchecked
		assertFalse(groupsPage.isInboundCallDispositionChecked(supportDriver, "Set Appointment"));
		assertFalse(groupsPage.isOutboundCallDispositionChecked(supportDriver, "Wrong Number"));
		
		//updating data by deleting member and verifying
		groupsPage.deleteAgentFromGroup(supportDriver, qa_user_name);
		groupsPage.saveGroup(supportDriver);
		assertFalse(groupsPage.isAgentAddedAsMember(supportDriver, qa_user_name));
		
		//deleting team
		groupsPage.deleteGroup(supportDriver);
		
		//deleting original team
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", true);
		System.out.println("Test case --verify_clone_team_functionality-- passed ");
	}
	
	@AfterClass(groups = {"Regression", "MediumPriority", "Product Sanity"}, alwaysRun = true)
	public void afterClass() {
		System.out.println("cleaning groups");
		try {
			initializeSupport();
			driverUsed.put("supportDriver", true);
		
			groupsPage.openGroupSearchPage(supportDriver);
			groupsPage.cleanGroups(supportDriver, "AutoTeamName");
			
			driverUsed.put("supportDriver", false);
		
		}
		catch(Exception e) {
			System.out.println("Exception occured in after class while cleaning up groups:"+e);
		}
	}
}
