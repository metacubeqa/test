package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import support.source.users.UsersPage;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class CalendarConnectionsCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	UsersPage usersPage = new UsersPage();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	GmailPageClass gmail = new GmailPageClass();
	GoogleCalendar googleCalendar = new GoogleCalendar();
	
	private String qa_user_name;
	private String qa_user_email;
	private String qa_salesForce_id;
	private String gmailEmail;
	private String gmailPassword;
	private String exchangeEmail;
	private String exchangePassword;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
		exchangeEmail = CONFIG.getProperty("exchnage_email_id");
		exchangePassword = CONFIG.getProperty("exchnage_password");
		qa_user_name = CONFIG.getProperty("qa_support_user_name");
		qa_user_email = CONFIG.getProperty("qa_support_user_email");
		qa_salesForce_id = CONFIG.getProperty("qa_support_user_salesforce_id");
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		driverUsed.put("webSupportDriver", false);
	}
	
	//Verify a user can update their welcome Message to its max characters
	//Verify agent can remove their PC welcome message
	//Verify a user with a calendar integrated can update their Welcome Message
	//Verify that Calendar link opens correctly
	//Verify that a users information is correctly Displayed on the personalize sub tab
	@Test(groups = { "Regression" })
	public void verify_welcome_message_on_personalize_tab() {
		// updating the webSupportDriver used
		System.out.println("Test case --verify_welcome_message_on_personalize_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		String username = calendar.getUserName(webSupportDriver);
		String timeZone = calendar.getTimeZone(webSupportDriver);

		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(webSupportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		calendar.openIntegrationTab(webSupportDriver);
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		
		//verify name country timezone
		String user = username.replace(" ", "-").toLowerCase();
		calendar.verifyPersonalizePage(webSupportDriver, username, "https://my-qa.ringdna.net/"+user+"/calendar", timeZone);
		
		String randomMessage = HelperFunctions.GetRandomString(6);
		calendar.editPersonalizeWelcomeMessage(webSupportDriver, randomMessage);
		
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//assert user name
		assertEquals(username, calendar.getPersonalCalPageUserName(webSupportDriver));
		
		//get message
		String message = calendar.getWelcomeMessageFromSoftphone(webSupportDriver);
		assertEquals(randomMessage, message);
		
		//check 255 char message
		calendar.switchToTab(webSupportDriver, 2);
		randomMessage = HelperFunctions.GetRandomString(256);
		calendar.editPersonalizeWelcomeMessage(webSupportDriver, randomMessage);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.navigate().refresh();
		
		message = calendar.getWelcomeMessageFromSoftphone(webSupportDriver);
		assertEquals(255, message.length());
		
		randomMessage = randomMessage.substring(0, randomMessage.length()-1);
		assertEquals(randomMessage, message);
		
		// check 0 char message
		calendar.switchToTab(webSupportDriver, 2);
		randomMessage = "";
		calendar.editPersonalizeWelcomeMessage(webSupportDriver, randomMessage);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.navigate().refresh();

		message = calendar.getWelcomeMessageFromSoftphone(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		assertNull(message);

		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_welcome_message_on_personalize_tab-- passed ");
	}
	
	//Verify user should navigate to calendar tab from "My Calendar" link of Admin drop down when at least one calendar integrated
	//Verify when no account integrated then "My Calendar" link should be hidden
	//Verify removing any one of account from integration tab "My calendar" link should not get removed
	//Verify "My Calendar" link should get removed when all integrated acc gets disabled
	//Verify "My Calendar" link should get removed when all integrated acc removed from Accounts tab
	@Test(groups = { "Regression" })
	public void verify_my_calendar_link_on_user_profile_drop_down() {
		// updating the webSupportDriver used
		System.out.println("Test case --verify_my_calendar_link_on_user_profile_drop_down-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail and exchange accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		
		//get user name
		String username = calendar.getUserName(webSupportDriver);
				
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);

		// open calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertFalse(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertTrue(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.openCalendarThroughUserProfileDropDown(webSupportDriver);
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		calendar.removeGmailIntegration(webSupportDriver);
		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertTrue(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);
		
		// open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail and exchange accounts
		accountsTab.disableGmailAccount(webSupportDriver);
		accountsTab.disableExchangeAccount(webSupportDriver);
		webSupportDriver.navigate().refresh();
		
		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertFalse(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);

		// enable gmail and exchange accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertTrue(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);
		
		// navigating to user account
		dashboard.clickOnUserProfile(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);

		// navigating to accounts tab
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//click on user count
		accountsTab.clickOnUserCount(webSupportDriver);
		
		//remove account from account tab
		accountsTab.removeAllAccountFromAccountsTab(webSupportDriver, username);
		webSupportDriver.navigate().refresh();
		
		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertFalse(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_my_calendar_link_on_user_profile_drop_down-- passed ");
	}
	
	//Verify messaging on personal calendar pages where no provider exists
	@Test(groups = { "Regression" })
	public void verify_disabled_personal_link_test() {
		System.out.println("disabled_personal_link_test started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeIntegration(webSupportDriver);
		
		// got to cal tab by url
		dashboard.clickOnUserProfile(webSupportDriver); 
		String url= webSupportDriver.getCurrentUrl()+"/calendar";
		webSupportDriver.get(url);
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify personal link
		calendar.verifyPersonalLinkDisabled(webSupportDriver);
		
		calendar.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		driverUsed.put("webSupportDriver", false);
		System.out.println("disabled_personal_link_test is Passed!");
	}
	
	//Verify Update "This Week" and 'This Month" Tabs with 15 and 30 min meeting.
	@Test(groups = { "Regression" })
	public void create_15_and_30_min_meeting_and_verify_link_test() {
		System.out.println("create_15_and_30_min_meeting_and_verify_link_test started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_15_and_30_min_meeting_and_verify_link_test is Passed!");
	}
	
	//Verify Update "This Week" and 'This Month" Tabs with 45 and 60 min meeting
	@Test(groups = { "Regression" })
	public void create_45_and_60_min_meeting_and_verify_link_test() {
		System.out.println("create_45_and_60_min_meeting_and_verify_link_test started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		calendar.closeTab(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		calendar.closeTab(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		driverUsed.put("webSupportDriver", false);
		System.out.println("create_45_and_60_min_meeting_and_verify_link_test is Passed!");
	}
	
	//Verify that a user who has multiple calendar providers attached that the changes can be made and saved
	//Verify that a user who has multiple calendar providers attached that the changes can be made and are not saved if the user navigates to another tab
	@Test(groups = { "Regression" })
	public void verify_multiple_calendar_attached_to_user() {
		// updating the webSupportDriver used
		System.out.println("Test case --verify_multiple_calendar_attached_to_user-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(webSupportDriver, qa_user_name, qa_user_email, qa_salesForce_id);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);

		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);
		
		calendar.openCalendarTab(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		int checkedConflict = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);

		// check Conflict Calendar boxes
		calendar.checkConflictCalendar(webSupportDriver, 2);
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, integrationDefaultCal);

		// open calendar tab
		calendar.clickMeetingSettings(webSupportDriver);
		// open avail tab
		calendar.clickCalendarAvailablity(webSupportDriver);
		
		// is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, integrationDefaultCal));
		int checkedConflictUpdated = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		
		// check conflict cal selected are same
		assertEquals(checkedConflict, checkedConflictUpdated);
		
		//is Save button disabled
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		// check Conflict Calendar boxes
		calendar.checkConflictCalendar(webSupportDriver, 2);
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, integrationDefaultCal);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, integrationDefaultCal));
		
		checkedConflict = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		assertEquals(1, checkedConflict);
		
		//is Save button disabled
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);

		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_multiple_calendar_attached_to_user-- passed ");
	}
	
	//Verify an Support User lands on the availability tab and its shows the default calendar set
	//Verify an Agent User lands on the availability tab and its shows the default calendar set
	@Test(groups = { "Regression" })
	public void verify_on_calendar_tab_default_availablity_sub_tab_should_be_open_support() {
		System.out.println("Test case --verify_on_calendar_tab_default_availablity_sub_tab_should_be_open_support-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);

		// remove integration
		calendar.removeIntegration(webSupportDriver);

		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));

		// is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, integrationDefaultCal));
		
		// check gmail is in expanded mode
		assertTrue(calendar.isCalendarInExpandMode(webSupportDriver, gmailEmail));

		// select default cal in conflict cal
		int expected = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		assertTrue(expected > 0);
		
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, integrationDefaultCal);
				
		// toast message
		calendar.verifyAtLeastOneConflictCalToastMessage(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify an Admin User lands on the availability tab and its shows the default calendar set
	@Test(groups = { "Regression" })
	public void verify_on_calendar_tab_default_availablity_sub_tab_should_be_open_admin() {
		System.out.println("Test case --verify_on_calendar_tab_default_availablity_sub_tab_should_be_open_admin-- started ");

		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		dashboard.switchToTab(adminDriver, 2);

		dashboard.clickOnUserProfile(adminDriver);

		// open integration
		calendar.openIntegrationTab(adminDriver);

		// remove integration
		calendar.removeExchangeIntegration(adminDriver);

		// add gmail integration
		calendar.addGmailIntegration(adminDriver, gmailEmail, gmailPassword);

		// get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(adminDriver);

		// open calendar tab
		calendar.openCalendarTab(adminDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(adminDriver));

		// is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(adminDriver, integrationDefaultCal));

		// select default cal in conflict cal
		int expected = calendar.getNumberOfCheckedConflictCalSelected(adminDriver);
		assertTrue(expected > 0);
		
		// select default cal in conflict cal
		calendar.checkConflictCalByName(adminDriver, integrationDefaultCal);
				
		// toast message
		calendar.verifyAtLeastOneConflictCalToastMessage(adminDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("adminDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify if a user has multiple calendar providers attached the user can click to expand any of the Conflict Calendar Checkbox Groups on the page
	@Test(groups = { "Regression" })
	public void verify_expand_of_conflict_cal_when_multiple_cal_providers() {
		// updating the webSupportDriver used
		System.out.println("Test case --verify_expand_of_conflict_cal_when_multiple_cal_providers-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(webSupportDriver, qa_user_name, qa_user_email, qa_salesForce_id);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);

		// add gmail integration
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		
		int actual = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		
		// check collpase mode
		assertTrue(calendar.isCalendarInExpandMode(webSupportDriver, gmailEmail));
		assertFalse(calendar.isCalendarInExpandMode(webSupportDriver, exchangeEmail));
		
		// go to meeting type and select default cal
		calendar.clickMeetingTypesTab(webSupportDriver);
		
		// select exchnage
		calendar.selectDefaultCalByName(webSupportDriver, "Calendar");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// check collpase mode
		calendar.clickCalendarAvailablity(webSupportDriver);
		assertTrue(calendar.isCalendarInExpandMode(webSupportDriver, exchangeEmail));
		assertFalse(calendar.isCalendarInExpandMode(webSupportDriver, gmailEmail));
		
		//click on gmail expand mode
		calendar.clickCalendarforExpandMode(webSupportDriver, gmailEmail);
		
		int expected = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		assertTrue(expected == actual);
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);

		// add gmail integration
		calendar.removeExchangeIntegration(webSupportDriver);

		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_expand_of_conflict_cal_when_multiple_cal_providers-- passed ");
	}
	
	//Verify 'My Personal Link' enable when user connected with any calendar
	@Test(groups = { "Regression" })
	public void verify_my_personal_cal_link_is_enabled_when_cal_is_connected() {
		System.out.println("Test case --verify_my_personal_cal_link_is_enabled_when_cal_is_connected-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// remove integration
		calendar.removeIntegration(webSupportDriver);

		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertFalse(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.clickUserProfileDropDown(webSupportDriver);
		
		// is cal tab visible
		assertFalse(calendar.isCalendarTabVisible(webSupportDriver));

		// add gmail integration
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		assertTrue(calendar.isUserProfileCalendarVisibleInDropDown(webSupportDriver));
		calendar.openCalendarThroughUserProfileDropDown(webSupportDriver);
		
		int initial = calendar.getTabCount(webSupportDriver);
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		
		// assert tab count
		assertEquals((initial+1), calendar.getTabCount(webSupportDriver));
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// check default cal on meeting type
		calendar.clickMeetingTypesTab(webSupportDriver);
		assertTrue(calendar.isMeetingTypeDefaultCalendarVisible(webSupportDriver));
		
		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_my_personal_cal_link_is_enabled_when_cal_is_connected-- passed ");
	}
	
	//Verify Calendars displayed in Default Calendar drop down
	//Calendar associated with connected providers should display
	@Test(groups = { "Regression" })
	public void create_new_cal_on_google_and_verify_on_admin_calendar_tab() {
		System.out.println("Test case --create_new_cal_on_google_and_verify_on_admin_calendar_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		
		// create new cal
		String calName = HelperFunctions.GetRandomString(5);
		googleCalendar.addNewMyCalendar(webSupportDriver, calName);
		calendar.switchToTab(webSupportDriver, 2);
		
		calendar.openCalendarTab(webSupportDriver);
		// get conflict calendar list
		List<String> conflictCal = calendar.getConflictCalendarListText(webSupportDriver);
		assertTrue(conflictCal.contains(calName));

		// check on meeting type
		calendar.clickMeetingTypesTab(webSupportDriver);
		// get conflict calendar list
		conflictCal = calendar.getMeetingTypeDefaultCalList(webSupportDriver);
		assertTrue(conflictCal.contains(calName));

		// switch to cal
		googleCalendar.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		googleCalendar.deleteMyCalendar(webSupportDriver, calName);
		googleCalendar.closeTab(webSupportDriver);

		// open calendar tab
		calendar.switchToTab(webSupportDriver, 2);
		webSupportDriver.navigate().refresh();
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// get conflict calendar list
		conflictCal = calendar.getConflictCalendarListText(webSupportDriver);
		assertFalse(conflictCal.contains(calName));

		// check on meeting type
		calendar.clickMeetingTypesTab(webSupportDriver);
		// get conflict calendar list
		conflictCal = calendar.getMeetingTypeDefaultCalList(webSupportDriver);
		assertFalse(conflictCal.contains(calName));

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

}
