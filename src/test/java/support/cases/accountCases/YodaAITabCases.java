package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLicenseTab;
import support.source.accounts.AccountYodaAITab;
import support.source.accounts.AccountLicenseTab.LicenseReason;
import support.source.accounts.AccountLicenseTab.LicenseType;
import support.source.accounts.AccountYodaAITab.NotificationTarget;
import support.source.accounts.AccountYodaAITab.NotificationType;
import support.source.accounts.AccountYodaAITab.NotifyType;
import support.source.commonpages.Dashboard;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class YodaAITabCases extends SupportBase { 

	Dashboard dashboard = new Dashboard();
	AccountYodaAITab yodaTab = new AccountYodaAITab();
	AccountIntelligentDialerTab accountIntelligent = new AccountIntelligentDialerTab();
	GroupsPage groupsPage = new GroupsPage();
	AccountLicenseTab licensePage = new AccountLicenseTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	UsersPage usersPage = new UsersPage();
	
	String SupportTeam = "RTN Please Don't Delete";
	String SupportTeam2 = "AutomationSuperVisorGroup";
	String SupportProfile = "System Administrator";
	String SupportProfile2 = "Chatter Free User";
	String SupportRole = "CEO";
	String SupportRole2 = "Channel Sales Team";
	String SupportRole3 = "Western Sales Team";
	String SupportUser = "Automation User 1";
	String SupportUser2 = "Agent Automation Automation";
	String none = "None";

	// Verify Real Time Notifications tab & page header renamed as YODA AI
	// Verify viewing a YODA-AI notification ( earlier named as realtime
	// notification) for a team
	// Verify Support user is able to get "RTN" tab for a searched account
	// Validate Support user should be able to see all the real-time notifications
	// that have been created in that searched account
	//Verify Support user not able to get "YODA-AI" tab within left nav
	@Test(groups = { "Regression", "SupportOnly" })
	public void open_yoda_ai_tab_and_verify_yoda_ai_heading() {
		// updating the supportDriver used
		System.out.println("Test case --open_yoda_ai_tab_and_verify_yoda_ai_heading-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//yoda ai not visible on navigation for support role
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		assertFalse(dashboard.isYodaAiTabVisible(supportDriver));

		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		yodaTab.verifyYodaAiDescription(supportDriver);
		yodaTab.isYodaAIHeadersVisible(supportDriver);
		yodaTab.verifyDeleteAndEditOptions(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --open_yoda_ai_tab_and_verify_yoda_ai_heading-- passed ");
	}

	// Update already created YODA-AI notification and verified again
	// Verify on deleting team from detail page it is removed from real time call
	// notification section
	// Verify Link field on the YODA AI form
	// Delete created Real-Time Call Alerts from account
	// Verify no error should appear while creating and updating YODA AI notifications
	@Test(groups = { "Regression" })
	public void add_update_real_time_notification_and_verify_team_details() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_real_time_notification_and_verify_team_details-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName2 = new ArrayList<String>();
		teamName2.add(0, SupportTeam);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, teamName2, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		yodaTab.selectTeam(supportDriver, teamName2.get(0));
		yodaTab.clickAndEnterPhrase(supportDriver, phrase2);
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);

		// delete then cancel while confimration
		yodaTab.deleteCancelNotification(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName2);

		// verify after deleting notification
		yodaTab.verifyYodaAiDescription(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_real_time_notification_and_verify_team_details-- passed ");
	}

	// Verify no YODA AI notification setting displayed on account's intelligent dialer tab now
	@Test(groups = { "Regression" })
	public void verify_no_yoda_ai_notification_on_account_id_tab() {
		// updating the supportDriver used
		System.out.println("Test case --verify_no_yoda_ai_notification_on_account_id_tab-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligent.openIntelligentDialerTab(supportDriver);

		assertFalse(accountIntelligent.isYodaAINotificationVisible(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_no_yoda_ai_notification_on_account_id_tab-- passed ");
	}

	// YODA-AI page
	// Verify notification created with default All teams from Account Intelligent dialer page is visible on Team detail page and vice versa
	// dialer page is visible on Team detail page and vice versa
	// Verify space & special character in Search bar to search by NAME/TEAM on YODA-AI page
	@Test(groups = { "Regression" })
	public void create_notification_and_search_with_different_characters() {
		// updating the supportDriver used
		System.out.println("Test case --create_notification_and_search_with_different_characters-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, ";");
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchNotificationOnYodaAi(supportDriver, ".");
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchNotificationOnYodaAi(supportDriver, "*");
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchNotificationOnYodaAi(supportDriver, "+");
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchNotificationOnYodaAi(supportDriver, "/");
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchNotificationOnYodaAi(supportDriver, "\t'");

		// verify notification
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_notification_and_search_with_different_characters-- passed ");
	}

	// Verify user able to type/enter text flawless or without lagging in phrases & in notification text
	// Verify user able to type/enter text flawless or without lagging in notification name
	@Test(groups = { "Regression" })
	public void verify_user_able_to_type_enter_text_flawless_without_lagging() {
		// updating the supportDriver used
		System.out.println("Test case --verify_user_able_to_type_enter_text_flawless_without_lagging-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName = new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// check with char and delete
		yodaTab.enterNotificationName(supportDriver, HelperFunctions.GetRandomString(20));
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(20));
		yodaTab.clickAndEnterPhrase(supportDriver, HelperFunctions.GetRandomString(20));

		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName2 =new ArrayList<String>();
		teamName2.add(0, SupportTeam);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);

		// check with char and delete
		yodaTab.enterNotificationName(supportDriver, HelperFunctions.GetRandomString(20));
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(20));
		yodaTab.clickAndEnterPhrase(supportDriver, HelperFunctions.GetRandomString(20));

		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, teamName2, phrase2, phrase2, NotificationType.Information);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_type_enter_text_flawless_without_lagging-- passed ");
	}

	// Verify availability of "Last Updated" column to RTN list and verify on admin
	// Verify Created RTN by support user availability in RTN list
	// Validate Support user is able to enable/disable an existing RTN
	// Verify if existing RTN edited then "Last Updated" column should display
	// latest time-stamp to RTN list
	// Verify Support user is able to edit an existing RTN for a searched account.
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_availability_of_last_updated_column_to_rtn_list_on_admin() {
		// updating the supportDriver used
		System.out.println("Test case --verify_availability_of_last_updated_column_to_rtn_list_on_admin-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName2 =new ArrayList<String>();
		teamName2.add(0, SupportTeam);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, teamName2, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName2));

		// initialize admin
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(adminDriver, 2);
		dashboard.clickOnUserProfile(adminDriver);
		dashboard.openYodaAiTab(adminDriver);

		// verify notification
		dashboard.isPaceBarInvisible(adminDriver);
		yodaTab.verifyNotificationIsPresent(adminDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, false);

		assertFalse(yodaTab.verifyNotificationIsEnabled(adminDriver, notificationName2));
		driverUsed.put("adminDriver", false);

		// delete
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_availability_of_last_updated_column_to_rtn_list_on_admin-- passed ");
	}

	// Verify availability of "Last Updated" column to RTN list and verify on
	// support
	// Verify the fields of accessed "RTN" tab for a searched account: should be
	// same for admin & support
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_availability_of_last_updated_column_to_rtn_list_on_support() {
		// updating the supportDriver used
		System.out.println("Test case --verify_availability_of_last_updated_column_to_rtn_list_on_support-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		// number of notification on page
		int supportNotification = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// initialize admin
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(adminDriver, 2);
		dashboard.clickOnUserProfile(adminDriver);
		dashboard.openYodaAiTab(adminDriver);

		// number of notification on page
		assertEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver), supportNotification);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(adminDriver);
		yodaTab.createNotificationInYodaAI(adminDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(adminDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName2 =new ArrayList<String>();
		teamName2.add(0, SupportTeam);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.createNotificationInYodaAI(adminDriver, notificationName2, NotificationTarget.Teams, teamName2, phrase2, phrase2, NotificationType.Information);

		// verify notification		
		yodaTab.verifyNotificationIsPresent(adminDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);
		driverUsed.put("adminDriver", false);

		// verify notification
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);
		// delete
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_availability_of_last_updated_column_to_rtn_list_on_support-- passed ");
	}

	// Verify Search bar to search by Team on YODA-AI page
	// Verify Search bar to search by NAME on YODA-AI page
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_team_on_yoda_ai_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_team_on_yoda_ai_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, HelperFunctions.GetRandomString(5), NotificationTarget.All, NotificationType.All);
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));

		yodaTab.searchAccountYodaAINotification(supportDriver, "", NotificationTarget.All, NotificationType.All);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		yodaTab.searchAccountYodaAINotification(supportDriver, notificationName, NotificationTarget.All, NotificationType.All);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.None, NotificationType.None, false);
		
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.Teams, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.Teams, NotificationType.Information, false);
		
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.Teams, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.Teams, NotificationType.Warning, false);

		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, notificationName, NotificationTarget.All, NotificationType.All);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		
		//search by team name
		yodaTab.searchAccountYodaAINotification(supportDriver, SupportTeam, NotificationTarget.All, NotificationType.All);
		dashboard.isPaceBarInvisible(supportDriver);
		dashboard.idleWait(3);
		
		//verify items
		yodaTab.expandAllNotificationForVerify(supportDriver, SupportTeam);

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_team_on_yoda_ai_page-- passed ");
	}

	// Verify filter by type on YODA AI (RTN ) page
	// Verify filter by type value selection based result on YODA AI (RTN ) page
	@Test(groups = { "Regression" })
	public void verify_filter_by_type_value_selection_based_result_on_yoda_ai_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_filter_by_type_value_selection_based_result_on_yoda_ai_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));

		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Information, false);

		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Warning, false);

		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.Alert);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Alert, false);

		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.All);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.All, false);

		// assert pagination
		supportDriver.navigate().refresh();
		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		yodaTab.verifyNotificationIsPresent(supportDriver);
		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_filter_by_type_value_selection_based_result_on_yoda_ai_page-- passed ");
	}

	// Verify filter by type for created/edited RTN on YODA AI (RTN ) page
	// Verify filter by type previous value not retained on new create notification on YODA AI (RTN ) page
	// Verify filter by type previous value not retained on edit notification on YODA AI (RTN ) page
	@Test(groups = { "Regression" })
	public void verify_filter_by_type_for_created_edited_on_yoda_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_filter_by_type_for_created_edited_on_yoda_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Information, false);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Alert);

		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		
		//get all notifications
		int initial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, notificationName, NotificationTarget.None, NotificationType.Alert);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Alert, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName2 =new ArrayList<String>();
		teamName2.add(0, SupportTeam);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, teamName2, phrase2, phrase2, NotificationType.Warning);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, notificationName2, NotificationTarget.None, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Warning, true);

		supportDriver.navigate().refresh();
		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		yodaTab.verifyNotificationIsPresent(supportDriver);
		
		//get all notification
		int end = yodaTab.getTotalYodaAINotificationVisible(supportDriver);
		
		//verify
		assertEquals(initial, end);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_filter_by_type_for_created_edited_on_yoda_page-- passed ");
	}

	// Verify search bar & filter type combination based result on YODA AI (RTN )
	// page
	@Test(groups = { "Regression" })
	public void verify_search_bar_and_filter_type_combination_based_result_on_yoda_ai_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_and_filter_type_combination_based_result_on_yoda_ai_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		yodaTab.verifyNotificationIsPresent(supportDriver);
		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// search 13
		yodaTab.searchNotificationOnYodaAi(supportDriver, "13");
		yodaTab.verifyNoNotificationFound(supportDriver);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, "13", NotificationTarget.None, NotificationType.Warning);
		yodaTab.verifyNoNotificationFound(supportDriver);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, "13", NotificationTarget.None, NotificationType.Alert);
		yodaTab.verifyNoNotificationFound(supportDriver);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, "", NotificationTarget.None, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Information, false);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.All);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.All, false);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, "1", NotificationTarget.None, NotificationType.Alert);
		yodaTab.verifyNoNotificationFound(supportDriver);

		// search notification
		yodaTab.searchAccountYodaAINotification(supportDriver, "", NotificationTarget.None, NotificationType.Alert);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Alert, false);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_and_filter_type_combination_based_result_on_yoda_ai_page-- passed ");
	}

	// Validate Support user able to create RTN for include phrase for a searched  account.
	// Validate Support user able to create RTN for Exclude phrase for a searched account.
	// Verify Support user is able to edit recently self created RTN
	// Verify Support user is able to remove recently self created RTN
	// Verify user add Real-Time Call Alerts data for account
	// Verify validations on creating a notification
	// Verify validations on updating a notification
	@Test(groups = { "Regression" })
	public void validate_support_user_able_to_create_rtn_with_phrase_without_phrase() {
		// updating the supportDriver used
		System.out.println("Test case --validate_support_user_able_to_create_rtn_with_phrase_without_phrase-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);

		// create without phrase
		yodaTab.clickCreateNotification(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		//get empty fields
		assertNull(yodaTab.getPharsesSelected(supportDriver));
		assertTrue(yodaTab.getNotificationNameEntered(supportDriver).equals(""));
		//verify cancel and save button visible
		yodaTab.verifyCancelSaveButton(supportDriver);
		
		//without phrase
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, "", phrase, NotificationType.Alert);
		assertEquals(yodaTab.getErrorMessage(supportDriver), "You need to enter at least one phrase.");
		
		// create notification without notification target
		yodaTab.clickBackToYodaAi(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickCreateNotification(supportDriver);

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		//enable notification in create/edit mode 
		yodaTab.enableNotificationInCreationMode(supportDriver);
		
		//assertTrue(yodaTab.getErrorMessage(supportDriver).contains("Please select a Notification Targeting group and click save."));
		
		// select notifcation target without team name
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		yodaTab.clickSaveButton(supportDriver);
		
		assertTrue(yodaTab.getErrorMessage(supportDriver).equals("Invalid targeting type payload. Targeting type 'Teams' must have a value for 'teams'."));
		
		// enter team
		yodaTab.selectTeam(supportDriver, SupportTeam);
		//select null time
		yodaTab.selectPhrasesNotMentioned(supportDriver, "", "");
		yodaTab.clickSaveButton(supportDriver);

		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "You need to enter a duration for the notification.");
		
		yodaTab.selectPhrasesNotMentioned(supportDriver, "2", "2");
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		yodaTab.clickSaveButton(supportDriver);

		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "Both the URL and Display Text fields are required in order to add a Link to your YODA AI Notification.");
		
		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit rtn
		yodaTab.EditNotification(supportDriver);
		assertTrue(yodaTab.getPharsesSelected(supportDriver).contains(SupportTeam));
		assertTrue(yodaTab.getNotificationNameEntered(supportDriver).equals(notificationName));
		
		//remove all phrases selected
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		yodaTab.clickSaveButton(supportDriver);
		assertTrue(yodaTab.getErrorMessage(supportDriver).equals("You need to enter at least one phrase."));
		
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);

		// select notifcation target without profile name
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.SalesforceProfiles);
		yodaTab.clickSaveButton(supportDriver);

		assertTrue(yodaTab.getErrorMessage(supportDriver).equals("Invalid targeting type payload. Targeting type 'SalesforceProfiles' must have a value for 'salesforceProfiles'."));
		// enter profile
		yodaTab.selectSalesforceProfiles(supportDriver, SupportProfile);
		
		// enter empty link label
		yodaTab.enterNotificationLabelName(supportDriver, "");
		yodaTab.clickSaveButton(supportDriver);
		assertTrue(yodaTab.getErrorMessage(supportDriver).equals("Both the URL and Display Text fields are required in order to add a Link to your YODA AI Notification."));
		
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, HelperFunctions.GetRandomString(5));
		yodaTab.clickSaveButton(supportDriver);
		
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, NotificationType.Information, true);

		// create empty notification
		yodaTab.clickCreateNotification(supportDriver);
		//enable notification in create/edit mode 
		yodaTab.enableNotificationInCreationMode(supportDriver);
		yodaTab.clickSaveButton(supportDriver);
		assertEquals(yodaTab.getErrorMessage(supportDriver), "The name field cannot be blank.");
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);

		// create same notification
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Alert);
		assertEquals(yodaTab.getErrorMessage(supportDriver), "An alert already exists with the name '" +notificationName+ "'. Please use a different name.");
		yodaTab.clickBackToYodaAi(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);

		// delete notification
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --validate_support_user_able_to_create_rtn_with_phrase_without_phrase-- passed ");
	}

	// Verify fresh default new create form when user create another yoda ai after
	// save one or go back to YODA AI page during creation
	@Test(groups = { "Regression" })
	public void verify_yoda_ai_after_save_or_go_back_to_yoda_page_during_creation() {
		// updating the supportDriver used
		System.out.println("Test case --verify_yoda_ai_after_save_or_go_back_to_yoda_page_during_creation-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		yodaTab.verifyNotificationIsPresent(supportDriver);
		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// click create and click back button
		yodaTab.clickCreateNotification(supportDriver);
		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.enterNotificationName(supportDriver, notificationName);
		yodaTab.clickBackToYodaAi(supportDriver);
		
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);

		// click and check no data is saved
		yodaTab.clickCreateNotification(supportDriver);
		assertTrue(yodaTab.getNotificationNameEntered(supportDriver).isEmpty());
		
		// click and reload
		yodaTab.clickBackToYodaAi(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickCreateNotification(supportDriver);
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_yoda_ai_after_save_or_go_back_to_yoda_page_during_creation-- passed ");
	}

	// Verify Deleted team not visible in existing real time notification list
	@Test(groups = { "Regression" })
	public void verify_deleted_team_not_visible_in_existing_real_time_notification() {
		// updating the supportDriver used
		System.out.println("Test case --verify_deleted_team_not_visible_in_existing_real_time_notification-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// opening sales force tab of qav2 account
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// deleting group
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamName.get(0), CONFIG.getProperty("qa_user_account"));
		groupsPage.clickTeam(supportDriver, teamName.get(0));
		groupsPage.deleteGroup(supportDriver);
		groupsPage.verifyAfterDeleteGroupAssertions(supportDriver);

		// verify notification
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// enable team
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamName.get(0), CONFIG.getProperty("qa_user_account"));
		groupsPage.clickTeam(supportDriver, teamName.get(0));
		groupsPage.restoreGroup(supportDriver);
		groupsPage.verifyAfterRestoreGroupAssertions(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_deleted_team_not_visible_in_existing_real_time_notification-- passed ");
	}

	// Verify create RTN with Enable Real Time Notification toggle ->ON/OFF
	// Verify Support user is able to remove an existing RTN for a searched account
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_create_rtn_and_enable_rtn_toggle_on_off() {
		// updating the supportDriver used
		System.out.println("Test case --verify_create_rtn_and_enable_rtn_toggle_on_off-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName= new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));

		// disable
		yodaTab.disableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));

		// enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));

		// delete
		int answer = yodaTab.getPaginationCount(supportDriver);
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		assertEquals(answer, yodaTab.getPaginationCount(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_create_rtn_and_enable_rtn_toggle_on_off-- passed ");
	}

	// Verify filter by type impact on pagination list available YODA AI (RTN ) page
	// Verify pagination when user have more then 10 alerts added for account
	@Test(groups = { "Regression" })
	public void verify_filter_by_type_impact_on_pagination_list_available_yoda_ai_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_filter_by_type_impact_on_pagination_list_available_yoda_ai_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		
		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// click pagination
		yodaTab.clickNextPage(supportDriver);

		// search
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.None, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.None, NotificationType.Information, false);

		// assert pagination
		supportDriver.navigate().refresh();
		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// check pagination
		int answer = yodaTab.getPaginationCount(supportDriver);
		assertEquals(answer, 1);

		// click pagination
		yodaTab.clickNextPage(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickPreviousPage(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);

		// check pagination
		assertEquals(answer, yodaTab.getPaginationCount(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_filter_by_type_impact_on_pagination_list_available_yoda_ai_page-- passed ");
	}

	// Update specific team(s)/ individual user targeting type notification to all users targeting
	// Verify user create Salesforce profile yoda AI notification with add multiple roles
	@Test(groups = { "Regression" })
	public void create_and_update_yoda_ai_notification_add_multiple_roles() {
		// updating the supportDriver used
		System.out.println("Test case --create_and_update_yoda_ai_notification_add_multiple_roles-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role= new ArrayList<String>();
		role.add(0, SupportRole);
		role.add(1, SupportRole2);
		role.add(2, SupportRole3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.AllUsers, null, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.AllUsers, NotificationType.Information, true);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_and_update_yoda_ai_notification_add_multiple_roles-- passed ");
	}

	// Validate User selection process: support users can only select users from the account they are currently viewing
	// account they are currently viewing.
	@Test(groups = { "Regression" })
	public void validate_user_selection_while_creating_rtn() {
		// updating the supportDriver used
		System.out.println("Test case --validate_user_selection_while_creating_rtn-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role= new ArrayList<String>();
		role.add(0, SupportRole);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Information, true);

		// valid user
		List<String> user= new ArrayList<String>();
		user.add(0, SupportUser);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.IndividualUsers, user, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.IndividualUsers, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);

		// verify profile exist or not
		// in valid user
		assertFalse(yodaTab.verifyUserExistsInDropDown(supportDriver, CONFIG.getProperty("qa_report_invalid_user_name")));
		assertFalse(yodaTab.verifyUserExistsInDropDown(supportDriver, SupportUser));
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --validate_user_selection_while_creating_rtn-- passed ");
	}

	// Validate Team selection process: support users can only select teams from the account they are currently viewing
	// account they are currently viewing.
	@Test(groups = { "Regression" })
	public void validate_team_selection_while_creating_rtn() {
		// updating the supportDriver used
		System.out.println("Test case --validate_team_selection_while_creating_rtn-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role= new ArrayList<String>();
		role.add(0, SupportRole);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Information, true);

		// valid team
		List<String> team= new ArrayList<String>();
		team.add(0, SupportTeam);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, team, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);

		// verify profile exist or not
		// in valid team
		assertFalse(yodaTab.verifyTeamExistsInDropDown(supportDriver, CONFIG.getProperty("qa_report_invalid_user_name")));
		assertFalse(yodaTab.verifyTeamExistsInDropDown(supportDriver, SupportTeam));
		
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --validate_team_selection_while_creating_rtn-- passed ");
	}

	// Validate Specific Salesforce PROFILE selection process: support users can only select teams from the account they are currently viewing.
	@Test(groups = { "Regression" })
	public void validate_salesforce_profile_selection_while_creating_rtn() {
		// updating the supportDriver used
		System.out.println("Test case --validate_salesforce_profile_selection_while_creating_rtn-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role= new ArrayList<String>();
		role.add(0, SupportRole);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Information, true);

		// valid profile
		List<String> profile= new ArrayList<String>();
		profile.add(0, SupportProfile);

		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.SalesforceProfiles, profile, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.SalesforceProfiles, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);

		// verify profile exist or not
		// in valid profile
		assertFalse(yodaTab.verifyProfileExistsInDropDown(supportDriver, CONFIG.getProperty("qa_report_invalid_user_name")));
		assertFalse(yodaTab.verifyProfileExistsInDropDown(supportDriver, SupportProfile));
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --validate_salesforce_profile_selection_while_creating_rtn-- passed ");
	}

	// Validate Specific Salesforce ROLE selection process: support users can only select teams from the account they are currently viewing.
	@Test(groups = { "Regression" })
	public void validate_salesforce_role_selection_while_creating_rtn() {
		// updating the supportDriver used
		System.out.println("Test case --validate_salesforce_role_selection_while_creating_rtn-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName= new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		
		// valid role
		List<String> role= new ArrayList<String>();
		role.add(0, SupportRole);
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.SalesforceRoles, role, phrase2, phrase2, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.SalesforceRoles, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);

		// verify profile exist or not
		// in valid role
		assertFalse(yodaTab.verifyRoleExistsInDropDown(supportDriver, CONFIG.getProperty("qa_report_invalid_user_name")));
		assertFalse(yodaTab.verifyRoleExistsInDropDown(supportDriver, SupportRole));
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --validate_salesforce_role_selection_while_creating_rtn-- passed ");
	}

	//Verify clickable links to YODA-AI [Real Time Notifications]
	//Verify validations of links and url to YODA-AI [Real Time Notifications]
	@Test(groups = { "Regression" })
	public void verify_validations_of_links_and_url_to_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_validations_of_links_and_url_to_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String teamName = SupportTeam;
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// verify placeholder value
		assertEquals(yodaTab.getNotifyUrlPlaceholderValue(supportDriver), "e.g. https://www.ringdna.com");
		assertEquals(yodaTab.getNotifyLabelPlaceholderValue(supportDriver), "e.g. Click Here");

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		// enter team
		yodaTab.selectTeam(supportDriver, teamName);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, phrase);
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		yodaTab.clickSaveButton(supportDriver);

		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "Both the URL and Display Text fields are required in order to add a Link to your YODA AI Notification.");

		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		yodaTab.clickSaveButton(supportDriver);

		// assert error of invalid url
		assertEquals(yodaTab.getErrorMessage(supportDriver), "please enter a valid HTTPS URL (e.g. https://www.ringdna.com)");

		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_validations_of_links_and_url_to_yoda_ai-- passed ");
	}
	
	//Verify user create/update individual users yoda AI notification with multiple users
	@Test(groups = { "Regression" })
	public void create_rtn_with_multiple_individual_users() {
		// updating the supportDriver used
		System.out.println("Test case --create_rtn_with_multiple_individual_users-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> userName =new ArrayList<String>();
		userName.add(0, SupportUser);
		userName.add(1, SupportUser2);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.IndividualUsers, userName, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.IndividualUsers, NotificationType.Information, true);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_rtn_with_multiple_individual_users-- passed ");
	}
	
	//Verify user create/ update Salesforce profile yoda AI notification with multiple sfdc profile
	@Test(groups = { "Regression" })
	public void create_rtn_with_multiple_salesforce_profiles() {
		// updating the supportDriver used
		System.out.println("Test case --create_rtn_with_multiple_salesforce_profiles-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> profile =new ArrayList<String>();
		profile.add(0, SupportProfile);
		profile.add(1, SupportProfile2);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, profile, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, NotificationType.Information, true);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_rtn_with_multiple_salesforce_profiles-- passed ");
	}
	
	//Verify user create/ update teams yoda AI notification with multiple teams
	@Test(groups = { "Regression" })
	public void create_rtn_with_multiple_teams() {
		// updating the supportDriver used
		System.out.println("Test case --create_rtn_with_multiple_teams-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> team =new ArrayList<String>();
		team.add(0, SupportTeam2);
		team.add(1, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, team, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_rtn_with_multiple_teams-- passed ");
	}
	
	//Verify validations within exclude time fields during add and update YODA AI notification
	@Test(groups = { "Regression" })
	public void verify_validations_of_exclude_time_fields_to_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_validations_of_exclude_time_fields_to_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String teamName = SupportTeam;
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		// enter team
		yodaTab.selectTeam(supportDriver, teamName);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, phrase);
		
		//select null time
		yodaTab.selectPhrasesNotMentioned(supportDriver, " ", " ");
		yodaTab.clickSaveButton(supportDriver);

		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "You need to enter a duration for the notification.");
		
		//select time
		yodaTab.selectPhrasesNotMentioned(supportDriver, " ", "20");
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		//select time
		yodaTab.selectPhrasesNotMentioned(supportDriver, " ", " ");
		yodaTab.clickSaveButton(supportDriver);
		
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "You need to enter a duration for the notification.");
		
		//select time
		yodaTab.selectPhrasesNotMentioned(supportDriver, "2", " ");
		
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_validations_of_exclude_time_fields_to_yoda_ai-- passed ");
	}
	
	//Verify Character counter added to YODA Notification Text field
	@Test(groups = { "Regression" })
	public void verify_character_counter_added_to_yoda_notification_text_field() {
		// updating the supportDriver used
		System.out.println("Test case --verify_character_counter_added_to_yoda_notification_text_field-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String teamName = SupportTeam;
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		// enter team
		yodaTab.selectTeam(supportDriver, teamName);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		//assert
		assertEquals(yodaTab.getnotificationTextCount(supportDriver), "10/180");
		
		//enter 180 char
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(181));
		//assert
		assertEquals(yodaTab.getnotificationTextCount(supportDriver), "180/180");
		assertEquals(yodaTab.getNotificationText(supportDriver).length(), 180);
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		//enter more than 180 char
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(190));

		assertEquals(yodaTab.getnotificationTextCount(supportDriver), "180/180");
		assertEquals(yodaTab.getNotificationText(supportDriver).length(), 180);
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_character_counter_added_to_yoda_notification_text_field-- passed ");
	}
	
	//Verify Search bar to search by TARGET > All Users on YODA-AI page
	//YODA AI notification list- filter by "All Users" targeting group type
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_all_users_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_all_users_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.AllUsers, null, phrase, phrase, NotificationType.Warning);

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		
		//search all users
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.AllUsers, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.AllUsers, NotificationType.None, false);
		
		//remove targeting type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.All, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.AllUsers, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Warning, false);
		
		int notificationInitial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, HelperFunctions.GetRandomString(5));
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));
		
		//search all and information filter type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.AllUsers, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.AllUsers, NotificationType.Information, false);
		
		// number of notification on page
		if(yodaTab.getTotalYodaAINotificationVisible(supportDriver) != 10 && notificationInitial != 10) {
			assertNotEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver) , notificationInitial);
		}

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		assertEquals(initial, end);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_all_users_on_yoda_ai-- passed ");
	}
	
	//Verify Search bar to search by TARGET > Teams on YODA-AI page
	//YODA AI notification list- filter by "Team(s)" targeting group type
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_teams_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_teams_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Warning);

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		
		//search all users
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.Teams, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.Teams, NotificationType.None, false);
		
		//remove targeting type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.All, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.Teams, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Warning, true);
		
		int notificationInitial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, HelperFunctions.GetRandomString(5));
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));
		
		//search all and information filter type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.Teams, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.Teams, NotificationType.Information, false);
		
		// number of notification on page
		if (yodaTab.getTotalYodaAINotificationVisible(supportDriver) != 10 && notificationInitial != 10) {
			assertNotEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver), notificationInitial);
		}

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		assertEquals(initial, end);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_teams_on_yoda_ai-- passed ");
	}
	
	//Verify Search bar to search by TARGET > Salesforce Role on YODA-AI page
	//YODA AI notification list- filter by "salesforce role users" targeting group type
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_salesforce_roles_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_salesforce_roles_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);
		
		//default targeting filter selected
		assertTrue(yodaTab.getDefaultTargetFilterSelected(supportDriver).equals(NotificationType.All.toString()));

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role = new ArrayList<String>();
		role.add(0, SupportRole);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Warning);

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		
		//search all users
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceRoles, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.SalesforceRoles, NotificationType.None, false);
		
		//remove targeting type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.All, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceRoles, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Warning, true);
		
		int notificationInitial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, HelperFunctions.GetRandomString(5));
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));
		
		//search all and information filter type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceRoles, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.SalesforceRoles, NotificationType.Information, false);
		
		// number of notification on page
		if (yodaTab.getTotalYodaAINotificationVisible(supportDriver) != 10 && notificationInitial != 10) {
			assertNotEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver), notificationInitial);
		}

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		assertEquals(initial, end);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_salesforce_roles_on_yoda_ai-- passed ");
	}
	
	//Verify Search bar to search by TARGET > Salesforce Profile on YODA-AI page
	//YODA AI notification list- filter by "salesforce profile" targeting group type
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_salesforce_profiles_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_salesforce_profiles_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> profile = new ArrayList<String>();
		profile.add(0, SupportProfile);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, profile, phrase, phrase, NotificationType.Warning);

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		
		//search all users
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceProfiles, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.SalesforceProfiles, NotificationType.None, false);
		
		//remove targeting type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.All, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceProfiles, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, NotificationType.Warning, true);
		
		int notificationInitial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, HelperFunctions.GetRandomString(5));
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));
		
		//search all and information filter type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.SalesforceProfiles, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.SalesforceProfiles, NotificationType.Information, false);
		
		// number of notification on page
		if (yodaTab.getTotalYodaAINotificationVisible(supportDriver) != 10 && notificationInitial != 10) {
			assertNotEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver), notificationInitial);
		}

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		assertEquals(initial, end);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_salesforce_profiles_on_yoda_ai-- passed ");
	}
	
	//Verify Search bar to search by TARGET > Individual Users on YODA-AI page
	//YODA AI notification list- filter by "Individual users" targeting group type
	//Verify combine filter by targeting and type with search
	@Test(groups = { "Regression" })
	public void verify_search_bar_to_search_by_individual_users_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_search_bar_to_search_by_individual_users_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> user = new ArrayList<String>();
		user.add(0, "Automation User 1");
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.IndividualUsers, user, phrase, phrase, NotificationType.Warning);

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		
		//search all users
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.IndividualUsers, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.IndividualUsers, NotificationType.None, false);
		
		//remove targeting type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.All, NotificationType.None);
		yodaTab.verifyNotificationIsPresent(supportDriver);

		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.IndividualUsers, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.IndividualUsers, NotificationType.Warning, true);
		
		//get name from ui
		String name = yodaTab.getAllYodaAINotificationList(supportDriver).get(0);
				
		// verify notification
		yodaTab.searchAccountYodaAINotification(supportDriver, name, NotificationTarget.IndividualUsers, NotificationType.Warning);
		yodaTab.verifyNotificationIsPresent(supportDriver, name, NotificationTarget.IndividualUsers, NotificationType.Warning, true);

		int notificationInitial = yodaTab.getTotalYodaAINotificationVisible(supportDriver);
		
		// search notification
		yodaTab.searchNotificationOnYodaAi(supportDriver, HelperFunctions.GetRandomString(5));
		assertTrue(yodaTab.verifyNoNotificationFound(supportDriver));
		
		//search all and information filter type
		yodaTab.searchAccountYodaAINotification(supportDriver, none, NotificationTarget.IndividualUsers, NotificationType.Information);
		yodaTab.verifyNotificationIsPresent(supportDriver, none, NotificationTarget.IndividualUsers, NotificationType.Information, false);
		
		// number of notification on page
		if (yodaTab.getTotalYodaAINotificationVisible(supportDriver) != 10 && notificationInitial != 10) {
			assertNotEquals(yodaTab.getTotalYodaAINotificationVisible(supportDriver), notificationInitial);
		}

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.verifyNoNotificationIsPresent(supportDriver, notificationName);
		
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		assertEquals(initial, end);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_bar_to_search_by_individual_users_on_yoda_ai-- passed ");
	}
	
	//Verify Admin/Support user able to get YODA notification targeting 'All Users' details on notification row click
	@Test(groups = { "Regression" })
	public void verify_all_users_notification_details_after_expanding_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_all_users_notification_details_after_expanding_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.AllUsers, null, phrase, phrase, NotificationType.Warning);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//verify items
		assertTrue(yodaTab.isAllUserNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains("Any user with YODA AI enabled can trigger this notification."));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//edit notification
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.EditNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.AllUsers, null, phrase, phrase, NotificationType.Warning);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.AllUsers, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		//verify items
		assertTrue(yodaTab.isAllUserNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains("Any user with YODA AI enabled can trigger this notification."));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		
		//disable notification
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		//is notification expanded
		assertFalse(yodaTab.isAllUserNotificationExpanded(supportDriver));
		
		//enable notification
		yodaTab.enableNotificationByName(supportDriver, notificationName2);
		
		//is Notification expanded
		assertFalse(yodaTab.isAllUserNotificationExpanded(supportDriver));
		
		//assert 3 dots options
		yodaTab.verifyDeleteAndEditOptions(supportDriver);
		
		//is Notification expanded
		assertFalse(yodaTab.isAllUserNotificationExpanded(supportDriver));
		yodaTab.deleteNotification(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_all_users_notification_details_after_expanding_on_yoda_ai-- passed ");
	}
	
	//Verify Admin/Support user able to get YODA notification targeting 'Teams' details on notification row click
	@Test(groups = { "Regression" })
	public void verify_teams_notification_details_after_expanding_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_teams_notification_details_after_expanding_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> teamName=new ArrayList<String>();
		teamName.add(0, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Warning);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//verify items
		assertTrue(yodaTab.isTeamsNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportTeam));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//edit notification
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.EditNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.Teams, teamName, phrase, phrase, NotificationType.Warning);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.Teams, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		//verify items
		assertTrue(yodaTab.isTeamsNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportTeam));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		
		//disable notification
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		//is notification expanded
		assertFalse(yodaTab.isTeamsNotificationExpanded(supportDriver));
		
		//enable notification
		yodaTab.enableNotificationByName(supportDriver, notificationName2);
		
		//is Notification expanded
		assertFalse(yodaTab.isTeamsNotificationExpanded(supportDriver));
		
		//assert 3 dots options
		yodaTab.verifyDeleteAndEditOptions(supportDriver);
		
		//is Notification expanded
		assertFalse(yodaTab.isTeamsNotificationExpanded(supportDriver));
		yodaTab.deleteNotification(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_teams_notification_details_after_expanding_on_yoda_ai-- passed ");
	}
	
	//Verify Admin/Support user able to get YODA notification targeting 'SalesForce Role' details on notification row click
	@Test(groups = { "Regression" })
	public void verify_salesforce_roles_notification_details_after_expanding_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_salesforce_roles_notification_details_after_expanding_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> role = new ArrayList<String>();
		role.add(0, SupportRole);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Warning);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceRoles, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//verify items
		assertTrue(yodaTab.isSalesforceRolesNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportRole));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//edit notification
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.EditNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.SalesforceRoles, role, phrase, phrase, NotificationType.Warning);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.SalesforceRoles, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		//verify items
		assertTrue(yodaTab.isSalesforceRolesNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportRole));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		
		//disable notification
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		//is notification expanded
		assertFalse(yodaTab.isSalesforceRolesNotificationExpanded(supportDriver));
		
		//enable notification
		yodaTab.enableNotificationByName(supportDriver, notificationName2);
		
		//is Notification expanded
		assertFalse(yodaTab.isSalesforceRolesNotificationExpanded(supportDriver));
		
		//assert 3 dots options
		yodaTab.verifyDeleteAndEditOptions(supportDriver);
		
		//is Notification expanded
		assertFalse(yodaTab.isSalesforceRolesNotificationExpanded(supportDriver));
		yodaTab.deleteNotification(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_roles_notification_details_after_expanding_on_yoda_ai-- passed ");
	}
	
	//Verify Admin/Support user able to get YODA notification targeting 'SalesForce Profile' details on notification row click
	@Test(groups = { "Regression" })
	public void verify_salesforce_profile_notification_details_after_expanding_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_salesforce_profile_notification_details_after_expanding_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> profile = new ArrayList<String>();
		profile.add(0, SupportProfile);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, profile, phrase, phrase, NotificationType.Warning);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//verify items
		assertTrue(yodaTab.isSalesforceProfileNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportProfile));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//edit notification
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.EditNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.SalesforceProfiles, profile, phrase, phrase, NotificationType.Warning);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.SalesforceProfiles, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		//verify items
		assertTrue(yodaTab.isSalesforceProfileNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportProfile));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		
		//disable notification
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		//is notification expanded
		assertFalse(yodaTab.isSalesforceProfileNotificationExpanded(supportDriver));
		
		//enable notification
		yodaTab.enableNotificationByName(supportDriver, notificationName2);
		
		//is Notification expanded
		assertFalse(yodaTab.isSalesforceProfileNotificationExpanded(supportDriver));
		
		//assert 3 dots options
		yodaTab.verifyDeleteAndEditOptions(supportDriver);
		
		//is Notification expanded
		assertFalse(yodaTab.isSalesforceProfileNotificationExpanded(supportDriver));
		yodaTab.deleteNotification(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_profile_notification_details_after_expanding_on_yoda_ai-- passed ");
	}
	
	//Verify Admin/Support user able to get YODA notification targeting 'Individual Users' details on notification row click
	@Test(groups = { "Regression" })
	public void verify_individual_users_notification_details_after_expanding_on_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_individual_users_notification_details_after_expanding_on_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> user = new ArrayList<String>();
		user.add(0, SupportUser);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.IndividualUsers, user, phrase, phrase, NotificationType.Warning);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.IndividualUsers, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//verify items
		assertTrue(yodaTab.isIndividualUsersNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportUser));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		//edit notification
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.EditNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.IndividualUsers, user, phrase, phrase, NotificationType.Warning);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.IndividualUsers, NotificationType.Warning, true);
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		//verify items
		assertTrue(yodaTab.isIndividualUsersNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains(SupportUser));
		
		yodaTab.expandNotificationByName(supportDriver, notificationName2);
		
		//disable notification
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		
		//is notification expanded
		assertFalse(yodaTab.isIndividualUsersNotificationExpanded(supportDriver));
		
		//enable notification
		yodaTab.enableNotificationByName(supportDriver, notificationName2);
		
		//is Notification expanded
		assertFalse(yodaTab.isIndividualUsersNotificationExpanded(supportDriver));
		
		//assert 3 dots options
		yodaTab.verifyDeleteAndEditOptions(supportDriver);
		
		//is Notification expanded
		assertFalse(yodaTab.isIndividualUsersNotificationExpanded(supportDriver));
		yodaTab.clickDeleteOption(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_individual_users_notification_details_after_expanding_on_yoda_ai-- passed ");
	}
	
	//Verify ADMIN user is able to get "YODA-AI" tab in left hand menu/nav bar
	@Test(groups = { "Regression", "AdminOnly" })
	public void open_yoda_ai_tab_and_verify_yoda_ai_heading_for_admin() {
		// updating the supportDriver used
		System.out.println("Test case --open_yoda_ai_tab_and_verify_yoda_ai_heading_for_admin-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		//open yoda ai through navigation tab
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.openYodaAiTab(supportDriver);

		yodaTab.verifyYodaAiDescription(supportDriver);
		yodaTab.isYodaAIHeadersVisible(supportDriver);
		yodaTab.verifyDeleteAndEditOptions(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --open_yoda_ai_tab_and_verify_yoda_ai_heading_for_admin-- passed ");
	}
	
	//Verify after/during deleting an RTN on last page user should remain on same page from which RTN is deleted
	//Verify after/during deleting last/only RTN on last page user should redirect to previous page from which RTN is deleted
	@Test(groups = { "Regression" })
	public void verify_pagination_after_deleting_rtn_on_last_page() {
		// updating the supportDriver used
		System.out.println("Test case --verify_pagination_after_deleting_rtn_on_last_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		// default filter selected
		assertTrue(yodaTab.getDefaultTypeFilterSelected(supportDriver).equals(NotificationType.All.toString()));
		assertTrue(yodaTab.getDefaultTargetFilterSelected(supportDriver).equals(NotificationType.All.toString()));

		// number of notification on page
		assertTrue(yodaTab.getTotalYodaAINotificationVisible(supportDriver) <= 10);

		// click pagination
		yodaTab.goToPageByNumber(supportDriver, yodaTab.getTotalPageCount(supportDriver));

		// get notification count
		int notification = yodaTab.getTotalYodaAINotificationVisible(supportDriver);

		if (notification == 1) {
			String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
			List<String> user = new ArrayList<String>();
			user.add(0, SupportUser);
			String phrase = "Automate" + HelperFunctions.GetRandomString(3);
			yodaTab.clickCreateNotification(supportDriver);
			yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.IndividualUsers, user, phrase, phrase, NotificationType.Warning);

			// verify notification
			yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.IndividualUsers, NotificationType.Warning, true);
			// click pagination
			yodaTab.goToPageByNumber(supportDriver, yodaTab.getTotalPageCount(supportDriver));
		}

		// check pagination
		int initial = yodaTab.getPaginationCount(supportDriver);
		// get notification count
		notification = yodaTab.getTotalYodaAINotificationVisible(supportDriver);
		assertTrue(notification > 1);

		yodaTab.deleteNotification(supportDriver);
		// check pagination
		int end = yodaTab.getPaginationCount(supportDriver);
		// assert
		assertTrue(initial == end);

		notification = yodaTab.getTotalYodaAINotificationVisible(supportDriver);
		for (int index = 0; index < notification; index++) {
			yodaTab.deleteNotification(supportDriver);
		}
		// check pagination
		// assert
		dashboard.isPaceBarInvisible(supportDriver);
		assertEquals(yodaTab.getPaginationCount(supportDriver), (end - 1));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_pagination_after_deleting_rtn_on_last_page-- passed ");
	}
	
	//Verify user able to create chip on Phrases field
	@Test(groups = { "Regression" })
	public void verify_user_able_to_create_chip_on_phrases_field() {
		// updating the supportDriver used
		System.out.println("Test case --verify_user_able_to_create_chip_on_phrases_field-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String teamName = SupportTeam;
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		String phrase2 = "Automate" + HelperFunctions.GetRandomString(3);
		String phrase3 = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		// enter team
		yodaTab.selectTeam(supportDriver, teamName);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		//enter phrase by arrow button
		yodaTab.enterPhraseByArrow(supportDriver, phrase2);
		//enter phrase by clicking outside
		yodaTab.enterPhraseByClickingOutside(supportDriver, phrase3);
		
		yodaTab.clickSaveButton(supportDriver);
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);

		// edit notification
		yodaTab.EditNotification(supportDriver);
		//check all phrases selected
		List<String> pharses = yodaTab.getPharsesSelected(supportDriver);
		assertTrue(pharses.contains(phrase));
		assertTrue(pharses.contains(phrase2));
		assertTrue(pharses.contains(phrase3));
		
		yodaTab.clickSaveButton(supportDriver);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_create_chip_on_phrases_field-- passed ");
	}
	
	//Verify sorting on "Enable" column in notification list
	// Verify user able to sorts after click on enable column name of created alerts
	@Test(groups = { "Regression" })
	public void verify_sorting_on_enable_column_in_notification_list() {
		// updating the supportDriver used
		System.out.println("Test case --verify_sorting_on_enable_column_in_notification_list-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.AllUsers, null, phrase, phrase, NotificationType.Information);

		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, true);
		
		String notificationName2 = "Automation" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName2, NotificationTarget.AllUsers, null, phrase, phrase, NotificationType.Information);

		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName2, NotificationTarget.AllUsers, NotificationType.Information, true);
		
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.disableNotificationByName(supportDriver, notificationName2);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//verify sorting
		yodaTab.verifyEnableSorting(supportDriver);

		// delete
		yodaTab.deleteNotification(supportDriver);
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_sorting_on_enable_column_in_notification_list-- passed ");
	}
	
	//Verify user create/ update teams yoda AI notification with multiple sfdc profile
	@Test(groups = { "Regression" })
	public void create_teams_yoda_ai_notification_and_update_with_multiple_sfdc_profile() {
		// updating the supportDriver used
		System.out.println("Test case --create_teams_yoda_ai_notification_and_update_with_multiple_sfdc_profile-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		List<String> team =new ArrayList<String>();
		team.add(0, SupportTeam2);
		team.add(1, SupportTeam);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.Teams, team, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);
		// check all phrases selected
		List<String> profiles = yodaTab.getPharsesSelected(supportDriver);
		assertTrue(profiles.contains(SupportTeam));
		assertTrue(profiles.contains(SupportTeam2));
		
		List<String> profile =new ArrayList<String>();
		profile.add(0, SupportProfile);
		profile.add(1, SupportProfile2);
		
		yodaTab.createNotificationInYodaAI(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, profile, phrase, phrase, NotificationType.Information);

		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.SalesforceProfiles, NotificationType.Information, true);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);
		// check all phrases selected
		profiles = yodaTab.getPharsesSelected(supportDriver);
		assertTrue(profiles.contains(SupportProfile));
		assertTrue(profiles.contains(SupportProfile2));
		yodaTab.clickSaveButton(supportDriver);
		
		yodaTab.deleteNotification(supportDriver);
				
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_teams_yoda_ai_notification_and_update_with_multiple_sfdc_profile-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_validations_of_disable_notification_in_yoda_ai() {
		// updating the supportDriver used
		System.out.println("Test case --verify_validations_of_disable_notification_in_yoda_ai-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);

		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);
		
		//enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "To enable, edit this YODA notification, complete all required fields on the form, and click Save. The YODA notification can then be enabled.");
		
		// edit notification
		yodaTab.EditNotification(supportDriver);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);
				
		//enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "To enable, edit this YODA notification, complete all required fields on the form, and click Save. The YODA notification can then be enabled.");
		
		// edit notification
		yodaTab.EditNotification(supportDriver);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, phrase);
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);
				
		//enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		
		//disable
		yodaTab.disableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// edit notification
		yodaTab.EditNotification(supportDriver);
		
		//select null time
		yodaTab.selectPhrasesNotMentioned(supportDriver, "", "");
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);

		// enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "To enable, edit this YODA notification, complete all required fields on the form, and click Save. The YODA notification can then be enabled.");

		// edit notification
		yodaTab.EditNotification(supportDriver);
		yodaTab.enableNotificationInCreationMode(supportDriver);
		yodaTab.clickSaveButton(supportDriver);

		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "You need to enter a duration for the notification.");
		
		//enter time
		yodaTab.selectPhrasesNotMentioned(supportDriver, "2", "2");
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		
		//disable
		yodaTab.disableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// edit notification
		yodaTab.EditNotification(supportDriver);
		
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.Teams);
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.Teams, NotificationType.Information, false);

		// enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "To enable, edit this YODA notification, complete all required fields on the form, and click Save. The YODA notification can then be enabled.");
		
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		// edit notification
		yodaTab.EditNotification(supportDriver);
		// select notifcation target
		yodaTab.selectNotificationTargeting(supportDriver, NotificationTarget.AllUsers);
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		yodaTab.clickSaveButton(supportDriver);
		
		// enable
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "To enable, edit this YODA notification, complete all required fields on the form, and click Save. The YODA notification can then be enabled.");

		// edit notification
		yodaTab.EditNotification(supportDriver);
		yodaTab.enableNotificationInCreationMode(supportDriver);
		yodaTab.clickSaveButton(supportDriver);
		// assert error
		assertEquals(yodaTab.getErrorMessage(supportDriver), "Both the URL and Display Text fields are required in order to add a Link to your YODA AI Notification.");
		
		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		yodaTab.clickSaveButton(supportDriver);
		
		// verify notification
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.Information, false);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		
		yodaTab.deleteNotification(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_validations_of_disable_notification_in_yoda_ai-- passed ");
	}
	
	//Verify Toggle button Enable/Disable can be update when notification created as Notify=Supervisors
	//Verify Agent settings available when Updated/selected Notify=Agent or Both
	//Verify Agent settings hidden when selected Notify=Supervisors
	//Verify validation behavior when selected or updated Notify=Supervisors
	//Verify Agent settings hidden for OLD/Existing notification having selected Notify=Supervisors
	//Verify label on toggle updated from "Enable YODA AI" to "Enable this YODA AI Notification"
	@Test(groups = { "Regression" })
	public void verify_notify_supervisor_agent_both_notification() {
		// updating the supportDriver used
		System.out.println("Test case --verify_notify_supervisor_agent_both_notification-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);
		
		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Supervisors);
		dashboard.isPaceBarInvisible(supportDriver);
		//enable notification in create/edit mode 
		yodaTab.enableNotificationInCreationMode(supportDriver);

		assertEquals(yodaTab.getEnableNotificationLabel(supportDriver), "Enable this YODA AI Notification");
		assertFalse(yodaTab.isNotificationTextVisible(supportDriver));
		
		yodaTab.clickSaveButton(supportDriver);
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.None, false);
		
		//change state
		yodaTab.disableNotificationByName(supportDriver, notificationName);
		assertFalse(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		yodaTab.enableNotificationByName(supportDriver, notificationName);
		assertTrue(yodaTab.verifyNotificationIsEnabled(supportDriver, notificationName));
		
		//expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);
				
		//verify items
		assertTrue(yodaTab.isAllUserNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains("Any user with YODA AI enabled can trigger this notification."));
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		yodaTab.EditNotification(supportDriver);
		
		//verify notification is enabled
		assertTrue(yodaTab.isNotificationEnabledInCreationMode(supportDriver));
		
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Agent);
		dashboard.isPaceBarInvisible(supportDriver);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		yodaTab.clickSaveButton(supportDriver);
		
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.None, false);
		
		yodaTab.EditNotification(supportDriver);
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Both);
		
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		// enter link url
		yodaTab.enterNotificationUrlName(supportDriver, "https://www.ringdna.com");
		//select notification type
		yodaTab.selectNotificationType(supportDriver, NotificationType.Information);
		
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickSaveButton(supportDriver);
		
		yodaTab.verifyNotificationIsPresent(supportDriver, notificationName, NotificationTarget.AllUsers, NotificationType.None, false);
		
		// expand notification
		yodaTab.expandNotificationByName(supportDriver, notificationName);

		// verify items
		assertTrue(yodaTab.isAllUserNotificationExpanded(supportDriver));
		assertTrue(yodaTab.getExpandNotificationBody(supportDriver).contains("Any user with YODA AI enabled can trigger this notification."));
		yodaTab.expandNotificationByName(supportDriver, notificationName);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_notify_supervisor_agent_both_notification-- passed ");
	}
	
	//Preview the YODA AI Information notification who notify to agents
	//Preview the YODA AI Alert notification who notify to both agents and supervisors
	//Preview the YODA AI Warning notification who notify to Supervisors
	@Test(groups = { "Regression" })
	public void preview_notify_supervisor_agent_both_notification() {
		// updating the supportDriver used
		System.out.println("Test case --preview_notify_supervisor_agent_both_notification-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		yodaTab.openYodaAiTab(supportDriver);
		
		String notificationName = "Automation" + HelperFunctions.GetRandomString(3);
		String phrase = "Automate" + HelperFunctions.GetRandomString(3);
		yodaTab.clickCreateNotification(supportDriver);

		// remove all team and phrase
		yodaTab.removeAllTeamsAndPhraseSelected(supportDriver);
		// create notification
		yodaTab.enterNotificationName(supportDriver, notificationName);
		// enter phrases
		yodaTab.clickAndEnterPhrase(supportDriver, phrase);
		// enter notification text
		yodaTab.enterNotificationText(supportDriver, HelperFunctions.GetRandomString(10));
		// enter link label
		yodaTab.enterNotificationLabelName(supportDriver, "Automation" + HelperFunctions.GetRandomString(3));
		// enter link url
		String url = "https://www.ringdna.com";
		yodaTab.enterNotificationUrlName(supportDriver, url);
		//select notification type
		yodaTab.selectNotificationType(supportDriver, NotificationType.Warning);
		
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Supervisors);
		dashboard.isPaceBarInvisible(supportDriver);
		
		// click on preview
		yodaTab.clickOnPreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickUrlLinkInPreview(supportDriver, url);
		
		int tab = dashboard.getTabCount(supportDriver);
		assertTrue(tab > 2);
		dashboard.switchToTab(supportDriver, tab);
		dashboard.closeTab(supportDriver);
		dashboard.switchToTab(supportDriver, 2);
		
		//close preview
		yodaTab.closePreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Agent);
		dashboard.isPaceBarInvisible(supportDriver);
		
		// click on preview
		yodaTab.clickOnPreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickUrlLinkInPreview(supportDriver, url);

		tab = dashboard.getTabCount(supportDriver);
		assertTrue(tab > 2);
		dashboard.switchToTab(supportDriver, tab);
		dashboard.closeTab(supportDriver);
		dashboard.switchToTab(supportDriver, 2);

		// close preview
		yodaTab.closePreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//select notify type
		yodaTab.selectNotifyType(supportDriver, NotifyType.Both);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//select notification type
		yodaTab.selectNotificationType(supportDriver, NotificationType.Alert);

		// click on preview
		yodaTab.clickOnPreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		yodaTab.clickUrlLinkInPreview(supportDriver, url);

		tab = dashboard.getTabCount(supportDriver);
		assertTrue(tab > 2);
		dashboard.switchToTab(supportDriver, tab);
		dashboard.closeTab(supportDriver);
		dashboard.switchToTab(supportDriver, 2);
		
		assertTrue(yodaTab.getPreviewBackgroundColor(supportDriver).equals("rgba(255, 181, 181, .12)"));   //red color
		
		//click on view supervisor notification
		yodaTab.clickOnSupervisorNotification(supportDriver);
		
		// close preview
		yodaTab.closePreview(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --preview_notify_supervisor_agent_both_notification-- passed ");
	}
	
	// Verify try to enable YODA AI Notification setting for the user when account not have licenses
	// Verify the YODA AI placeholder text is displayed on the tab when an account has 0 YODA AI user licenses
	// Verify user should not able to enable the YODA AI setting for the users when all available license are assigned
	// Verify the YODA AI placeholder text & embedded video is displayed on the tab when an account has 0 YODA AI user licenses
	@Test(groups = { "Regression" })
	public void try_to_enable_yoda_ai_without_having_yoda_ai_licence() {
		System.out.println("Test case --try_to_enable_yoda_ai_without_having_yoda_ai_licence-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// opening account
		String accountName = CONFIG.getProperty("test_account");
		String salesForceId = CONFIG.getProperty("test_account_sf_id");
		dashboard.clickAccountsLink(supportDriver, accountName, salesForceId);
		licensePage.openLicensingTab(supportDriver);

		String type = "YODA AI Users";
		int assignCount = licensePage.getAssignedLicenseCount(supportDriver, type);
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);
		assertTrue(allowedCount == 0 && assignCount == 0 && remainingCount == 0);

		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, "User3 23March", "user323@march.com", "0050a00000JAYOpAAP");
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);

		userIntelligentDialerTab.enableYodaAINotifications(supportDriver);
		userIntelligentDialerTab.saveAcccountSettingsDialer(supportDriver);

		// error message
		String message = userIntelligentDialerTab.getSaveAccountsSettingMessage(supportDriver);
		assertTrue(message.contains(
				"reached the maximum number of YODA-AI users. Please go to your account information page to request additional users."));

		// opening account
		dashboard.clickAccountsLink(supportDriver, accountName, salesForceId);
		licensePage.openLicensingTab(supportDriver);

		assignCount = licensePage.getAssignedLicenseCount(supportDriver, type);
		allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);
		assertTrue(allowedCount == 0 && assignCount == 0 && remainingCount == 0);

		// open yoda ai tab
		yodaTab.openYodaAiTab(supportDriver);
		// click here link
		yodaTab.clickYodaAiHereLink(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --try_to_enable_yoda_ai_without_having_yoda_ai_licence-- passed");
	}

	// Verify enable YODA AI setting for the user based on YODA AI user licenses via the Licensing tab
	// Verify disable YODA AI Notification setting at user level will not trigger any notification to users
	@Test(groups = { "Regression" })
	public void try_to_enable_disable_yoda_ai_with_yoda_ai_licence() {
		System.out.println("Test case --try_to_enable_disable_yoda_ai_with_yoda_ai_licence-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);

		userIntelligentDialerTab.enableYodaAINotifications(supportDriver);
		userIntelligentDialerTab.saveAcccountSettingsDialer(supportDriver);

		// opening account
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);

		String type = "YODA AI Users";
		int assignCount = licensePage.getAssignedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);

		userIntelligentDialerTab.disableYodaAINotifications(supportDriver);
		userIntelligentDialerTab.saveAcccountSettingsDialer(supportDriver);

		// opening account
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);

		// verifying remaining count is increased
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);

		userIntelligentDialerTab.enableYodaAINotifications(supportDriver);
		userIntelligentDialerTab.saveAcccountSettingsDialer(supportDriver);

		// opening account
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);

		licensePage.verifyRemainingCountDecreased(supportDriver, type, remainingCount);
		licensePage.verifyAssignedCountIncreased(supportDriver, type, assignCount);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --try_to_enable_disable_yoda_ai_with_yoda_ai_licence-- passed");
	}
	
	// Verify Admins can view and request for YODA AI Licenses
	// Verify validation on add request Yoda AI license form
	// Verify support users can provision YODA AI Licenses
	@Test(groups = { "Regression" })
	public void verify_yoda_ai_license_request_and_add_licence() {
		System.out.println("Test case --verify_yoda_ai_license_request-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		String type = "YODA AI Users";
		int assignCount = licensePage.getAssignedLicenseCount(supportDriver, type);
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);
		assertTrue(allowedCount == (remainingCount + assignCount));

		// adding and verifying request message
		String yodaUsers = "1";
		String additionalDetails = "AutoAdditionalDetails".concat(HelperFunctions.GetRandomString(3));

		// enter negative yoda ai user
		licensePage.addRequestBtn(supportDriver);
		licensePage.clickYodaCheckBox(supportDriver);
		licensePage.enterYodaAiUsers(supportDriver, "-3");
		licensePage.clickRequestBtn(supportDriver);
		// verify error msg
		assertEquals(licensePage.getRequestMsg(supportDriver), "Please select at least one feature you'd like to add and enter correct quantity value.");

		// enter empty yoda ai user
		licensePage.enterYodaAiUsers(supportDriver, "");
		licensePage.clickRequestBtn(supportDriver);
		// verify error msg
		licensePage.verifyRequestMsg(supportDriver);
		assertEquals(licensePage.getRequestMsg(supportDriver), "Please select at least one feature you'd like to add and enter correct quantity value.");

		// enter yoda ai user 1
		licensePage.addRequestBtn(supportDriver);
		licensePage.addLicenseRequest(supportDriver, additionalDetails, null, null, null, null, null, null, yodaUsers);
		licensePage.verifyRequestMsg(supportDriver);

		// add license
		licensePage.addLicenseBtn(supportDriver);
		licensePage.addLicense(supportDriver, LicenseType.MaxYodaAIUsers.toString(), "1", LicenseReason.CustomerService.toString(), additionalDetails);

		// verifying license line items
		String userName = CONFIG.getProperty("qa_free_user_name");
		licensePage.verifyLicenseLineItemSections(supportDriver, additionalDetails, type, "1", userName, LicenseReason.CustomerService.toString());

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_yoda_ai_license_request-- passed");
	}

}
