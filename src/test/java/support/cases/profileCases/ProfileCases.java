package support.cases.profileCases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.testng.annotations.Test;

import base.TestBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.salesforce.SalesforceHomePage;
import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSalesforceTab.SalesForceFields;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.profilePage.ProfilePage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class ProfileCases extends SupportBase{

	Dashboard dashboard = new Dashboard();
	ProfilePage profilePage = new ProfilePage();
	AccountSalesforceTab accountSalesForceTab = new AccountSalesforceTab();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	SalesforceHomePage sfHomePage = new SalesforceHomePage();
	CallScreenPage callScreenPage = new CallScreenPage();
	AccountSalesforceTab salesForceTab = new AccountSalesforceTab();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	UsersPage usersPage = new UsersPage();
	AccountIntelligentDialerTab intelligentDialerTab = new AccountIntelligentDialerTab();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	AddSmartNumberPage smartNumbersPage = new AddSmartNumberPage();
	Random random = new Random();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	
	//Red Exclamation Marks visible with Profile tab if the custom profile is visible on the Profile page the first time.
	//New Profile add after user login in softphone
	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_profile_section_functionality() {
		System.out.println("Test case --verify_profile_section_functionality-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(dashboard.isProfileSectionVisible(supportDriver));
		
		dashboard.navigateToProfilesSection(supportDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}
		assertTrue(profilePage.isProfileNameListVisible(supportDriver));
		assertTrue(profilePage.isRedExclamationListVisible(supportDriver));
		
		if (profilePage.isProfileNameListVisible(supportDriver)) {
			assertTrue(profilePage.isProfileNameListVisible(supportDriver));
		}
		
		profilePage.clickPencilEditIcon(supportDriver);
		assertTrue(profilePage.isAccountFieldSectionDefault(supportDriver));
		
		profilePage.clickAddFieldIcon(supportDriver);
		profilePage.closeField(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_section_functionality-- passed ");
	}
	
	//User should not able to Add more than 15 field
	@Test(groups = { "MediumPriority" })
	public void verify_profile_section_user_add_not_more_than_15_field() {
		System.out.println("Test case --verify_profile_section_user_add_not_more_than_15_field-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		
		dashboard.navigateToProfilesSection(supportDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}
		
		profilePage.clickPencilEditIcon(supportDriver);
		profilePage.navigateToContactFieldsSection(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 15) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profilePage.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profilePage.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_section_user_add_not_more_than_15_field-- passed ");
	}
	
	//Verify updated salesforce profile name in Admin
	@Test(groups = { "MediumPriority" })
	public void verify_updated_salesforce_name_in_admin() {
		System.out.println("Test case --verify_updated_salesforce_name_in_admin-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.refreshCurrentDocument(supportDriver);

		dashboard.switchToTab(supportDriver, 1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(supportDriver);
		callScreenPage.clickCallerName(supportDriver);
		callScreenPage.switchToTab(supportDriver, callScreenPage.getTabCount(supportDriver));
		callScreenPage.closeLightningDialogueBox(supportDriver);
		
		String profileName = "Custom: Marketing Profile";
		String profileNameUpdated = profileName.concat(HelperFunctions.GetRandomString(3));
		String accountName = CONFIG.getProperty("qa_user_account");
		
		sfHomePage.enterSetupSearchAndSelectContainer(supportDriver, "Profiles");
		sfHomePage.clickProfileName(supportDriver, profileName);
		sfHomePage.editProfileName(supportDriver, profileNameUpdated);
		callScreenPage.closeTab(supportDriver);
		callScreenPage.switchToTab(supportDriver, 2);
		
		dashboard.switchToTab(agentDriver, 1);
		dashboard.reloadSoftphone(agentDriver);
		
		dashboard.switchToTab(supportDriver, 1);
		dashboard.reloadSoftphone(supportDriver);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.refreshCurrentDocument(supportDriver);
		
		int i = 3;
		boolean profileNameVisible = false;
		while (i >= 1 && profileNameVisible != true) {
			profilePage.idleWait(10);
			dashboard.navigateToProfilesSection(supportDriver);
			if (profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
				profilePage.clickGetStartedButton(supportDriver);
			}
			profilePage.enterAccountAndSearch(supportDriver, accountName);
			profileNameVisible = profilePage.isProfileNameVisible(supportDriver, profileNameUpdated);
			i--;
		}

		assertTrue(profilePage.isProfileNameVisible(supportDriver, profileNameUpdated));
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_updated_salesforce_name_in_admin-- passed ");
	}
	
	
	//Overview section > Profile Name, Profile ID and Users numbe fields should show in readonly mode
	@Test(groups = { "MediumPriority" })
	public void verify_profile_overview_section_is_in_read_only(){
		System.out.println("Test case --verify_profile_overview_section_is_in_read_only-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);

		dashboard.navigateToProfilesSection(supportDriver);
		if (profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}

		profilePage.clickPencilEditIcon(supportDriver);
		
		profilePage.isProfileNameDisabled(supportDriver);
		profilePage.isProfileIdDisabled(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_overview_section_is_in_read_only-- passed ");
	}
	
	//Select field and Check Always Visible , Editable and Required
	@Test(groups = { "MediumPriority" })
	public void verify_profile_check_boxes_are_visible() {
		System.out.println("Test case --verify_profile_check_boxes_are_visible-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);

		dashboard.navigateToProfilesSection(supportDriver);
		if (profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}

		profilePage.clickPencilEditIcon(supportDriver);
		profilePage.navigateToContactFieldsSection(supportDriver);

		// verify check boxes
		profilePage.isAllCheckBoxesVisible(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_check_boxes_are_visible-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_profile_sfdc_objects_are_visible() {
		System.out.println("Test case --verify_profile_check_boxes_are_visible-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);

		dashboard.navigateToProfilesSection(supportDriver);
		if (profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}

		profilePage.clickPencilEditIcon(supportDriver);
		profilePage.navigateToContactFieldsSection(supportDriver);

		// verify Manage Account Fields
		profilePage.isAllManageAccountFieldsVisible(supportDriver);
		// verify overview fields
		profilePage.isOverviewFieldsVisible(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_check_boxes_are_visible-- passed ");
	}
		
	//Verify Update naming of "Accounts" to "Integrations"
	@Test(groups = { "Regression" })
	public void verify_update_naming_of_accounts_tab_to_integrations() {
		System.out.println("Test case --verify_update_naming_of_accounts_tab_to_integrations-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		
		//is account accounts tab visible
		assertFalse(accountsTab.isAccountAccountsTabVisible(supportDriver));
		
		//open account integration tab
		accountsTab.openAccountIntegrationsTab(supportDriver);
		accountsTab.disableGmailAccount(supportDriver);
		accountsTab.enableGmailAccount(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify the same links in the left nav show up based on the User's Role. Admin, Agent, Support. CAI turned on/off
	@Test(groups = { "Regression" })
	public void verify_enable_disable_conversation_ai() {
		System.out.println("Test case --verify_enable_disable_conversation_ai-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// disable cai support
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.disableConversationAnalyticsBtn(supportDriver);
		usersPage.disableConversationAIManagerBtn(supportDriver);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);

		// verifying cai not visible
		dashboard.refreshCurrentDocument(supportDriver);
		assertFalse(dashboard.isConversationAIVisible(supportDriver));

		// Opening Intelligent Dialer Tab
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.enableConversationAnalyticsBtn(supportDriver);
		usersPage.enableConversationAIManagerBtn(supportDriver);
		userIntelligentDialerTab.saveAcccountSettings(supportDriver);

		// verifying cai visible
		assertTrue(dashboard.isConversationAIVisible(supportDriver));

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Tooltip on web app
	@Test(groups = { "Regression" })
	public void verify_all_tool_tips_on_overview_salesforce_intelligent_dialer_tab() {
		System.out.println("Test case --verify_all_tool_tips_on_overview_salesforce_intelligent_dialer_tab-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// open account
		dashboard.clickAccountsLink(supportDriver);
		//verify overview tool tips
		overViewTab.verifyAllOverviewTabToolTip(supportDriver);

		// open salesforce tab
		salesForceTab.openSalesforceTab(supportDriver);
		//verify salesforce tool tips
		salesForceTab.verifyAllSalesforceTabToolTip(supportDriver);
		
		// open id tab
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		//verify id tool tips
		intelligentDialerTab.verifyToolTipWebLeadsLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipCallFrwrdingLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipDisableOfflineFrwrdingLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipCallForwardingPromptLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipUnavailableFlowLock(supportDriver);
		
		intelligentDialerTab.verifyAllIntelligentDialerTabToolTip(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify unified-admin-nav : Display tooltip on hover of collapsed NAV bar
	//Verify unified-admin-nav : Add icons to legacy nav
	@Test(groups = { "Regression" })
	public void verify_tool_tip_and_navigation_expand_collapse() {
		System.out.println("Test case --verify_tool_tip_and_navigation_expand_collapse-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(dashboard.isExpandedSideBarVisible(supportDriver));
		// click bars
		dashboard.clickHomeBars(supportDriver);
		assertFalse(dashboard.isExpandedSideBarVisible(supportDriver));
		
		// verify tool tips
		dashboard.verifyToolTipOnDashboardLinks(supportDriver);
		
		// open integration tab
		calendar.openIntegrationTab(supportDriver);
		// verify tool tips
		dashboard.verifyToolTipOnDashboardLinks(supportDriver);
		
		// open smart number
		dashboard.openSmartNumbersTab(supportDriver);
		// verify tool tips
		dashboard.verifyToolTipOnDashboardLinks(supportDriver);
		
		//open teams in collapsed
		dashboard.openTeamsInCollapsed(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		// click home bars
		dashboard.clickHomeBars(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify routes for both the new react nav and backbone nav to always remain in sync
	@Test(groups = { "Regression" })
	public void verify_teams_options_in_expand_collapse() {
		System.out.println("Test case --verify_teams_options_in_expand_collapse-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(dashboard.isExpandedSideBarVisible(supportDriver));
		// click bars
		dashboard.clickHomeBars(supportDriver);
		assertFalse(dashboard.isExpandedSideBarVisible(supportDriver));
		
		// click bars
		dashboard.clickHomeBars(supportDriver);
		assertTrue(dashboard.isExpandedSideBarVisible(supportDriver));
		
		//open smart number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNo(supportDriver, HelperFunctions.GetRandomString(5));
		
		//open teams
		dashboard.openTeamsInCollapsed(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		//click add new team 
		dashboard.openAddNewTeamsPage(supportDriver);
		dashboard.openSmartNumbersTab(supportDriver);
		
		//open teams
		dashboard.openTeamsInCollapsed(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		// click home bars
		dashboard.clickHomeBars(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Restyle Backbone/Sass Nav
	@Test(groups = { "Regression" })
	public void verify_restyle_backbone_sass_nav() {
		System.out.println("Test case --verify_restyle_backbone_sass_nav-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(dashboard.isExpandedSideBarVisible(supportDriver));
		// click bars
		dashboard.clickHomeBars(supportDriver);
		assertFalse(dashboard.isExpandedSideBarVisible(supportDriver));
		
		// click bars
		dashboard.clickHomeBars(supportDriver);
		assertTrue(dashboard.isExpandedSideBarVisible(supportDriver));
		
		//open teams
		dashboard.openTeamsInCollapsed(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		// click home bars
		dashboard.clickHomeBars(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		// click home bars in expand mode
		dashboard.clickHomeBars(supportDriver);
		dashboard.verifyTeamsSubOptionsVisible(supportDriver);
		
		//click add new team 
		dashboard.openAddNewTeamsPage(supportDriver);
		supportDriver.navigate().back();
		
		//verify page
		String username = calendar.getUserName(supportDriver);
		assertNotNull(username);
		
		//open account and verify tooltips
		dashboard.clickAccountsLink(supportDriver);
		// verify overview tool tips
		overViewTab.verifyAllOverviewTabToolTip(supportDriver);

		// open salesforce tab
		salesForceTab.openSalesforceTab(supportDriver);
		// verify salesforce tool tips
		salesForceTab.verifyAllSalesforceTabToolTip(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify legal text wherever admin able to add a new smart number
	@Test(groups = { "Regression" })
	public void verify_legal_text_while_adding_new_smart_number() {
		System.out.println("Test case --verify_legal_text_while_adding_new_smart_number-- started ");
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// call queue case
		String callQueueName = CONFIG.getProperty("automation_emergency_call_queue");
		// restoring call queue if deleted
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, "");
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		if (!callQueuePage.isDeleteCallQueueBtnVisible(supportDriver)) {
			callQueuePage.restoreCallQueue(supportDriver);
		}

		// creating smart number
		callQueuePage.clickSmartNumberIcon(supportDriver);
		//verify legal text
		smartNumbersPage.verifyLegalText(supportDriver);
		
		callQueuePage.deleteCallQueue(supportDriver);

		// call flow case
		// Opening manage call flow and searching call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_new_org"), "");
		callFlowPage.clickSelectedCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_new_org"));

		// adding smart number
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		//verify legal text
		smartNumbersPage.verifyLegalText(supportDriver);

		// account id tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// adding central number
		intelligentDialerTab.clickCentralNoAddIcon(supportDriver);
		//verify legal text
		smartNumbersPage.verifyLegalText(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
    //Search customer by Email ID
	@Test(groups = { "Regression" })
	public void verify_customer_data_by_email_id() {
		System.out.println("Test case --verify_customer_data_by_email_id-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// Opening navigate To Customer Data Page
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCustomerDataPage(supportDriver);
		
		//search email id 
		profilePage.enterCustomerEmail(supportDriver, CONFIG.getProperty("qa_report_admin_user_email"));
		profilePage.clickCustomerSearch(supportDriver);
		
		//verify data
		profilePage.verifyRecordsFound(supportDriver, CONFIG.getProperty("qa_report_admin_user_email"));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//first time open profile the should show First Launch Dialog tooltip in Blue color
	@Test(groups = { "MediumPriority" })
	public void verify_first_time_open_profile_the_should_show_first_launch_dialog() {
		System.out.println("Test case --verify_first_time_open_profile_the_should_show_first_launch_dialog-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		
		dashboard.navigateToProfilesSection(supportDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}
		
		profilePage.clickPencilEditIcon(supportDriver);
		profilePage.verifyfirstLaunchDialogTooltip(supportDriver);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_first_time_open_profile_the_should_show_first_launch_dialog-- passed ");
  }

	//Profile tab > New Profle > Edit > Overview and Manage Account Fields section should visible with all sfdc object
	@Test(groups = { "MediumPriority"})
	public void verify_profile_manage_and_overview_sfdc_objects_are_visible() {
		System.out.println("Test case --verify_profile_manage_and_overview_sfdc_objects_are_visible-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		dashboard.clickOnUserProfile(supportDriver);
		String name = profilePage.getProfileValueFromDropDown(supportDriver);
		
		//opening profile
		dashboard.navigateToProfilesSection(supportDriver);
		if (profilePage.isWelcomeProfilesSectionVisible(supportDriver)) {
			profilePage.clickGetStartedButton(supportDriver);
		}
		
		String qaUserGroup = TestBase.CONFIG.getProperty("qa_user_account");
		String qaUserID = TestBase.CONFIG.getProperty("qa_user_account_id");
		profilePage.enterAccountWithIdAndSearch(supportDriver, qaUserGroup, qaUserID);
		
		profilePage.clickUpddateBtn(supportDriver, name);
		
		//verify overview account fileds
		dashboard.isPaceBarInvisible(supportDriver);
		profilePage.isAllOverviewAccountFieldsVisible(supportDriver);
		
		// verify Manage Account Fields
		profilePage.isAllManageAccountFieldsVisible(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_profile_manage_and_overview_sfdc_objects_are_visible-- passed ");
	}
}
