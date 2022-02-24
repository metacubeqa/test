package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.HelperFunctions;

public class MeetingTypesCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	GmailPageClass gmail = new GmailPageClass();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	
	private String gmailEmail;
	private String gmailPassword;
	private String exchangeEmail;
	private String exchangePassword;
	private String zoomEmail;
	private String zoomPassword;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
		exchangeEmail = CONFIG.getProperty("exchnage_email_id");
		exchangePassword = CONFIG.getProperty("exchnage_password");
		zoomEmail = CONFIG.getProperty("zoom_email_id");
		zoomPassword = CONFIG.getProperty("zoom_password");
		
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

	// Verify UI of meeting types sub tab
	//Verify if save button not clickd then Default calendar dropdown should not changed
	@Test(groups = { "Regression" })
	public void verify_ui_of_meeting_types_sub_tab() {
		System.out.println("Test case --verify_ui_of_meeting_types_sub_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
				
		//enable gmail and exchange accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		String defaultCalendar = calendar.getAvailablityDefaultCalendar(webSupportDriver);
		
		//get conflict cal
		List<String> conflictCal = calendar.getConflictCalendarListText(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// check save button is disabled by default
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		// get Default Calendar
		String meetingTypeCal = calendar.getMeetingTypeDefaultCalendar(webSupportDriver);

		// check both default calendar are same
		assertTrue(meetingTypeCal.contains(defaultCalendar));
		
		//get meeting type cal list
		List<String> meetingTypeCalList = calendar.getMeetingTypeDefaultCalList(webSupportDriver);
		
		// assert 
		assertTrue(conflictCal.containsAll(meetingTypeCalList));
		calendar.selectAnotherDefaultCal(webSupportDriver);
		
		//assert
		assertFalse(calendar.isSaveButtonDisabled(webSupportDriver));
		calendar.clickMeetingSettings(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		
		//assert
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		// get Default Calendar
		meetingTypeCal = calendar.getMeetingTypeDefaultCalendar(webSupportDriver);

		// check both default calendar are same
		assertTrue(defaultCalendar.equals(meetingTypeCal));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webwebSupportDriver", false);
		System.out.println("Test case is pass");
	}

	
	//Verify Hide enable Email reminder setting on meeting types
	// Create 15 Minutes meeting
	@Test(groups = { "Regression" })
	public void create_15_minutes_meeting() {
		System.out.println("Test case --verify_holiday_cal_setting_on_hours_sub_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "15Test", meetingTypeEnum.Fiveteen);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Create meeting with new User
	// Create default selection 30 Minutes meeting
	@Test(groups = { "Regression" })
	public void create_30_minutes_meeting() {
		System.out.println("Test case --create_30_minutes_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "30Test", meetingTypeEnum.Thirty);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Create 45 Minutes meeting
	@Test(groups = { "Regression" })
	public void create_45_minutes_meeting() {
		System.out.println("Test case --create_45_minutes_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "45Test", meetingTypeEnum.FortyFive);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Create 60 Minutes meeting
	@Test(groups = { "Regression" })
	public void create_60_minutes_meeting() {
		System.out.println("Test case --create_60_minutes_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "60Test", meetingTypeEnum.Sixty);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Non valid url should not resolve to valid calendar page
	@Test(groups = { "Regression" })
	public void non_valid_url_not_resolve_valid_calendar_page() {
		System.out.println("Test case --non_valid_url_not_resolve_valid_calendar_page-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "60Test", meetingTypeEnum.Sixty);

		// go to personalize section
		calendar.clickCalendarPersonalize(webSupportDriver);

		String url = calendar.getYourCalendarLink(webSupportDriver) + "/snarky";

		// verify non valid url
		calendar.openNewBlankTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.get(url);
		webSupportDriver.navigate().refresh();

		// assert Error while opening non valid url
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);

		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Create meeting with long Event name text with 60 characters limit
	@Test(groups = { "Regression" })
	public void create_meeting_with_long_event_name() {
		System.out.println("Test case --create_meeting_with_long_event_name-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(61), meetingTypeEnum.Sixty);
		
		//get Meeting name
		String name = calendar.getMeetingTypeTitleOnMeetingPage(webSupportDriver);
		assertEquals(name.length(), 60);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Copy and open meeting types link
	@Test(groups = { "Regression" })
	public void copy_link_and_open_link_in_new_tab() {
		System.out.println("Test case --copy_link_and_open_link_in_new_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = "AutomationMeeting"+HelperFunctions.GetRandomString(3);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.FortyFive);

		// copy meeting link
		calendar.copyMeetingTypeLink(webSupportDriver);

		// get clip board data in String
		String data = null;
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// open link in new tab
		calendar.openNewBlankTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.get(data);

		//assert meeting type title on personal calendar page
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Verify meeting types order Multiple meeting types
	@Test(groups = { "Regression" })
	public void verify_meeting_types_order_test() {
		System.out.println("Test case --verify_meeting_types_order_test-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// create meeting types
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		// verify order of meeting types
		calendar.orderOfAllMeetingTypes(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

	// Verify Select ringDNA logo to return to calendar default page
	@Test(groups = { "Regression" })
	public void create_meeting_and_verify_link_test() {
		System.out.println("Test case --create_meeting_and_verify_link_test-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.Sixty);

		// copy meeting link
		calendar.copyMeetingTypeLink(webSupportDriver);

		// get clip board data in String
		String data = null;
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// open link in new tab
		calendar.openNewBlankTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.get(data);

		// select week and verify logo
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		// select month
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		// verify ringdna logo
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		//open slot and verify logo
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		//open event page
		calendar.openMeetingEventPage(webSupportDriver);
		// verify ringdna logo
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		calendar.closeTab(webSupportDriver);		
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Event Link under Create and Edit meeting type window
	//Verify Event Link format with created meeting types
	//Verify the updated meeting title on personal calendar
	@Test(groups = { "Regression" })
	public void verify_link_and_meeting_type_on_meeting_type_tab() {
		System.out.println("Test case --verify_link_and_meeting_type_on_meeting_type_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		//get user name
		String username = calendar.getUserName(webSupportDriver);
		username = username.replace(" ", "-").toLowerCase();

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.Sixty);

		// copy meeting link
		calendar.copyMeetingTypeLink(webSupportDriver);

		// get clip board data in String
		String data = null;
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//assert that link should contain meeting name
		String 	link = "https://my-qa.ringdna.net/"+username+"/calendar/"+name;
		assertTrue(data.equals(link));
		
		// open link in new tab
		calendar.openNewBlankTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.get(data);
		
		//assert meeting type title on personal calendar page
		assertEquals(calendar.getMeetingTypeTitleOnCalPage(webSupportDriver), name);
				
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		//verify the meeting type default page
		calendar.verifyPersonalCalendarLandingPage(webSupportDriver, name, meetingTypeEnum.Sixty.getValue());
		
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Open personal calendar by clicking meeting url from meeting type tab
	@Test(groups = { "Regression" })
	public void click_meeting_url_to_open_persoanl_calendar_page() {
		System.out.println("Test case --click_meeting_url_to_open_persoanl_calendar_page-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.Sixty);
		
		//click meeting url
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//click on back button
		calendar.clickPersonalCalBackButton(webSupportDriver);
		
		//verify the meeting type default page
		calendar.verifyPersonalCalendarLandingPage(webSupportDriver, name, meetingTypeEnum.Sixty.getValue());
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	
	//Open personal calendar by clicking meeting url from meeting type tab
	@Test(groups = { "Regression" })
	public void open_personal_calendar_page_and_verify_meetings_type_card() {
		System.out.println("Test case --open_personal_calendar_page_and_verify_meetings_type_card-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "60Test", meetingTypeEnum.Sixty);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify the meeting type default page
		calendar.verifyPersonalCalendarLandingPage(webSupportDriver, "60Test", meetingTypeEnum.Sixty.getValue());
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	
	//Verify placeholder text on Calendar Booking page when there no Meeting type enabled
	//Verify agent able to set status of meeting type from ON to OFF and vice versa
	@Test(groups = { "Regression" })
	public void change_the_status_of_meeting_and_verify_on_personal_cal_page() {
		System.out.println("Test case --change_the_status_of_meeting_and_verify_on_personal_cal_page-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.Sixty);
		
		//toggle off calendar meeting type
		calendar.clickToggleButtonMeetingType(webSupportDriver, 1);
		//assert
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//assert 
		assertTrue(calendar.checkNoMeetingTypeAvailableOnPersonalCalendar(webSupportDriver));
		calendar.switchToTab(webSupportDriver, 2);
		
		//toggle on calendar meeting type
		calendar.clickToggleButtonMeetingType(webSupportDriver, 1);
		//assert
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.navigate().refresh();
		
		//verify meeting type is available
		calendar.verifyPersonalCalendarLandingPage(webSupportDriver, name, meetingTypeEnum.Sixty.getValue());
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Changes to the Meeting Type cards do not impact the state of the SAVE button
	@Test(groups = { "Regression" })
	public void create_multiple_meeting_types_and_verify_save_button() {
		System.out.println("Test case --create_multiple_meeting_types_and_verify_save_button-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String name = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name, meetingTypeEnum.Sixty);
		// add meeting type
		String name2 = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, name2, meetingTypeEnum.Sixty);
		
		//toggle off calendar meeting type
		calendar.clickToggleButtonMeetingType(webSupportDriver, 1);
		calendar.clickToggleButtonMeetingType(webSupportDriver, 2);
		//assert
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		//toggle off calendar meeting type
		calendar.clickToggleButtonMeetingType(webSupportDriver, 1);
		calendar.clickToggleButtonMeetingType(webSupportDriver, 2);
		//assert
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Update an existing One-on One meeting type
	//Delete an existing meeting
	//Zoom meeting type option not available when no integration with zoom
	//Upon removing zoom integration , zoom meeting type option removed from meeting type
	@Test(groups = { "Regression" })
	public void create_edit_delete_meeting_type_test() {
		System.out.println("Test case --create_edit_delete_meeting_type_test-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);
		
		//clickEditMeetingTypeCreated
		calendar.clickEditMeetingTypeCreated(webSupportDriver);
		
		// edit meeting type
		calendar.editMeetingType(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Location filed hide from Meeting type sub tab
	//Verify Location filed hide from Meeting type sub tab without zoom
	@Test(groups = { "Regression" })
	public void verify_location_field_hide_from_meeting_type_sub_tab() {
		System.out.println("Test case --verify_location_field_hide_from_meeting_type_sub_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// click add meeting type button
		calendar.clickAddMeetingTypeButton(webSupportDriver);

		// is location field visible
		assertFalse(calendar.isLocationFieldAvailable(webSupportDriver));
		calendar.closeAddIntegrationWindow(webSupportDriver);
		
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		//edit
		calendar.clickEditMeetingTypeCreated(webSupportDriver);
		// is location field visible
		assertFalse(calendar.isLocationFieldAvailable(webSupportDriver));
		
//		// open integration tab
//		dashboard.clickOnUserProfile(webSupportDriver);
//		calendar.openIntegrationTab(webSupportDriver);
//		calendar.addZoomIntegration(webSupportDriver, gmailEmail, gmailPassword);
//		
//		// open calendar tab
//		calendar.openCalendarTab(webSupportDriver);
//
//		// open meeting types
//		calendar.clickMeetingTypesTab(webSupportDriver);
//		calendar.deleteMeetingType(webSupportDriver);
//
//		// click add meeting type button
//		calendar.clickAddMeetingTypeButton(webSupportDriver);
//
//		// is location field visible
//		assertFalse(calendar.isLocationFieldAvailable(webSupportDriver));
//
//		// add meeting type
//		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
//
//		// edit
//		calendar.clickEditMeetingTypeCreated(webSupportDriver);
//		// is location field visible
//		assertFalse(calendar.isLocationFieldAvailable(webSupportDriver));
//		
//		// open integration tab
//		dashboard.clickOnUserProfile(webSupportDriver);
//		calendar.openIntegrationTab(webSupportDriver);
//		calendar.removeZoomIntegration(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify on Meeting Type "Description" field having more than 255 character limit without zoom integration
	@Test(groups = { "Regression" })
	public void schedule_event_and_verify_description_limit_in_mail() {
		System.out.println("schedule_event_and_verify_description_limit_in_mail started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meetTypeDes = HelperFunctions.GetRandomString(255);
		calendar.createMeetingWithCustomDescription(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen, meetTypeDes);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		// get description max value
		String length = calendar.getMaxLengthOfEventSummaryDescriptionBox(webSupportDriver);
		assertEquals("255", length);
		
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, HelperFunctions.GetRandomString(5), email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);

		// get description message
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		// assert
		assertTrue(expectedDescription.contains(meetTypeDes));
		gmail.deleteOpenedMail(webSupportDriver);

		// open another mail
		gmail.openNewMailInGmail(webSupportDriver, 1);

		// get description message
		expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		// assert
		assertTrue(expectedDescription.contains(meetTypeDes));

		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("schedule_event_and_verify_description_limit_in_mail is Passed!");
	}
	
	//Verify on Meeting Type "Description" field having more than 255 character limit
	@Test(groups = { "Regression" })
	public void schedule_event_and_verify_description_limit_in_mail_with_zoom() {
		System.out.println("schedule_event_and_verify_description_limit_in_mail started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeIntegration(webSupportDriver);
		calendar.addZoomIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meetTypeDes = HelperFunctions.GetRandomString(255);
		calendar.createMeetingWithCustomDescription(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen, meetTypeDes);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		// get description max value
		String length = calendar.getMaxLengthOfEventSummaryDescriptionBox(webSupportDriver);
		assertEquals("255", length);
		
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(256);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, HelperFunctions.GetRandomString(5), email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(meetTypeDes));
		
		gmail.deleteOpenedMail(webSupportDriver);
		
		//open another mail
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(meetTypeDes));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("schedule_event_and_verify_description_limit_in_mail is Passed!");
	}
	
	//Verify Event Name field of event details on invitation email's body
	//Verify Event booker details on calendar events and email after scheduling an event with Gmail as cal provider
	//UI > Schedule Event Summary > confirmation Page
	//Customer: Receive a confirmation email for booking a meeting
	@Test(groups = { "Regression" })
	public void create_event_and_verify_event_details_on_mail() {
		System.out.println("create_event_and_verify_event_details_on_mail started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Fiveteen);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//get Slot time
		String initSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(6);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		
		//verify ui
		calendar.verifyScheduleEventPage(webSupportDriver, event, meetingTypeEnum.Fiveteen.getValue());
		//get Slot time
		String actualSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		
		//assert
		assertNotEquals(initSlot, actualSlot);
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		String expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		String expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		
		gmail.deleteOpenedMail(webSupportDriver);
		
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_event_and_verify_event_details_on_mail is Passed!");
	}
	
	//Meeting host: Receive a confirmation email for booking a meeting
	//Verify in event details and email of Custom Video Conference URL with Gmail
	//Verify detail provided during meeting type creation populated on calendar events and email after scheduling an event with Gmail as cal provider
	//Verify the “ringDNA” branding is consistently displayed properly in personal calendar booking emails
	@Test(groups = { "Regression" })
	public void verify_meeting_host_receive_meeting_confirmation_mail() {
		System.out.println("verify_meeting_host_receive_meeting_confirmation_mail started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		String url = HelperFunctions.GetRandomString(5);
		calendar.createMeetingWithVideoURL(webSupportDriver, meeting, meetingTypeEnum.Fiveteen, url);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 2);
		
		//getTitle
		assertEquals(gmail.getMailTitle(webSupportDriver), "A new event has been booked via your Calendar Booking page");
		
		// ring dna logo visiblity
		assertTrue(gmail.isRingDnaLogoVisibleInMail(webSupportDriver));
		assertTrue(gmail.getRingDnaLogoTextInMail(webSupportDriver).contains("ringDNA"));
		
		// CopyRight And SignOff Text visiblity
		assertTrue(gmail.isCopyRightAndSignOffTextVisible(webSupportDriver));
		assertTrue(gmail.getCopyRightAndSignOffTextVisible(webSupportDriver).contains("The ringDNA Team"));
		// get attendee mail
		assertEquals(gmail.getAttendeeMailFromMail(webSupportDriver), email);
		// ringdna sender name visible on mail
		assertTrue(gmail.isRingDnaSenderVisible(webSupportDriver, "ringDNA", "do-not-reply@ringdna.com"));
		
		//getCopy
	
		//get event details
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		String expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		String expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		String expectedVideoUrl = gmail.getVideoUrlFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		assertTrue(expectedVideoUrl.contains(url));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_meeting_host_receive_meeting_confirmation_mail is Passed!");
	}
	
	//Verify Event Name field of event details on invitation email's body with exchange account
	//Verify Event booker details on calendar events and email after scheduling an event with Exchange as cal provider
	@Test(groups = { "Regression" })
	public void create_event_and_verify_event_details_on_mail_with_exchange() {
		System.out.println("create_event_and_verify_event_details_on_mail_with_exchange started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		String expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		String expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		
		gmail.deleteOpenedMail(webSupportDriver);
		
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_event_and_verify_event_details_on_mail_with_exchange is Passed!");
	}
	
	//Verify in event details and email of Custom Video Conference URL with Exchange
	//Verify detail provided during meeting type creation populated on calendar events and email after scheduling an event with Exchange as cal provider
	@Test(groups = { "Regression" })
	public void verify_event_details_on_custom_video_url_with_exchange() {
		System.out.println("verify_event_details_on_custom_video_url_with_exchange started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		String url = HelperFunctions.GetRandomString(5);
		calendar.createMeetingWithVideoURL(webSupportDriver, meeting, meetingTypeEnum.Fiveteen, url);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 2);
		
		//getTitle
		assertEquals(gmail.getMailTitle(webSupportDriver), "A new event has been booked via your Calendar Booking page");
		// ring dna logo visiblity
		assertTrue(gmail.isRingDnaLogoVisibleInMail(webSupportDriver));
		// CopyRight And SignOff Text visiblity
		assertTrue(gmail.isCopyRightAndSignOffTextVisible(webSupportDriver));
		// get attendee mail
		assertEquals(gmail.getAttendeeMailFromMail(webSupportDriver), email);
	
		//get event details
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		String expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		String expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		String expectedVideoUrl = gmail.getVideoUrlFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		assertTrue(expectedVideoUrl.contains(url));
		
		gmail.deleteOpenedMail(webSupportDriver);
		calendar.closeTab(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_event_details_on_custom_video_url_with_exchange is Passed!");
	}
	
	//Verify that Calendar Owners automatically being marked as "Accepted" on Meeting invites for Gmail
	//Verify that Calendar owner has the ability to change default acceptance of any meeting
	@Test(groups = { "Regression" })
	public void verify_cal_owner_accept_and_decline_meeting() {
		System.out.println("verify_cal_owner_accept_and_decline_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Fiveteen);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//get Slot time
		String initSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(6);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
	
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//check yes
		gmail.clickGoingYesForCalEvent(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 3);
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		//get Slot time
		String actualSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		//assert
		assertNotEquals(initSlot, actualSlot);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//check maybe
		gmail.clickGoingMaybeForCalEvent(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 3);
		webSupportDriver.navigate().refresh();
		// get Slot time
		actualSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		// assert
		assertEquals(initSlot, actualSlot);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//check no
		gmail.clickGoingNoForCalEvent(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 3);
		webSupportDriver.navigate().refresh();
		// get Slot time
		actualSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		// assert
		assertNotEquals(initSlot, actualSlot);
		
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		gmail.deleteOpenedMail(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 3);
		calendar.closeTab(webSupportDriver);
		
		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_cal_owner_accept_and_decline_meeting is Passed!");
	}
	
	//Create Calendar event from PC where PC timezone is ahead than the host (eg ISTand UTC+8)
	//Create Calendar event from a different timezone behind than the host (eg IST and PST)
	//Create Calendar event of multiple days on actual calendar and Verify availability on PC from different timezone
	//Verify time zone for Meeting host in confirmation email for gmail
	@Test(groups = { "Regression" })
	public void create_event_and_verify_event_details_with_diff_time_zone() {
		System.out.println("create_event_and_verify_event_details_with_diff_time_zone started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Fiveteen);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(6);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String meetingTime = gmail.getMeetingTimeFromMail(webSupportDriver);
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to be
		String clientTime = formatter.format(meetingTime);
		
		gmail.deleteOpenedMail(webSupportDriver);
		
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String calOwnerTime = gmail.getMeetingTimeFromMail(webSupportDriver);
		//assert
		assertTrue(clientTime.contains(calOwnerTime));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_event_and_verify_event_details_with_diff_time_zone is Passed!");
	}
	
	//Verify time zone for a host in confirmation email for exchange
	@Test(groups = { "Regression" })
	public void verify_exchnage_event_details_with_diff_time_zone() {
		System.out.println("verify_exchnage_event_details_with_diff_time_zone started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Fiveteen);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(6);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String meetingTime = gmail.getMeetingTimeFromMail(webSupportDriver);
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to be
		String clientTime = formatter.format(meetingTime);
		
		gmail.deleteOpenedMail(webSupportDriver);
		
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//get description message
		String calOwnerTime = gmail.getMeetingTimeFromMail(webSupportDriver);
		//assert
		assertTrue(clientTime.contains(calOwnerTime));
		
		gmail.deleteOpenedMail(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_exchnage_event_details_with_diff_time_zone is Passed!");
	}
	
	//Verify in event details and email of Custom Video Conference URL with Zoom
	//@Test(groups = { "Regression" })
	public void verify_event_details_on_custom_video_url_with_zoom() {
		System.out.println("verify_event_details_on_custom_video_url_with_zoom started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addZoomIntegration(webSupportDriver, zoomEmail, zoomPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		String url = HelperFunctions.GetRandomString(5);
		calendar.createMeetingWithVideoURL(webSupportDriver, meeting, meetingTypeEnum.Fiveteen, url);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 2);
		
		//getTitle
		assertEquals(gmail.getMailTitle(webSupportDriver), "A new event has been booked via your Calendar Booking page");
		// ring dna logo visiblity
		assertTrue(gmail.isRingDnaLogoVisibleInMail(webSupportDriver));
		// CopyRight And SignOff Text visiblity
		assertTrue(gmail.isCopyRightAndSignOffTextVisible(webSupportDriver));
		// get attendee mail
		assertEquals(gmail.getAttendeeMailFromMail(webSupportDriver), email);
	
		//get event details
		String expectedDescription = gmail.getDescriptionMessageFromMail(webSupportDriver);
		String expectedEventName = gmail.getEventNameFromMail(webSupportDriver);
		String expectedMeetingType = gmail.getMeetingTypeFromMail(webSupportDriver);
		String expectedVideoUrl = gmail.getVideoUrlFromMail(webSupportDriver);
		//assert
		assertTrue(expectedDescription.contains(description));
		assertTrue(expectedEventName.contains(event));
		assertTrue(expectedMeetingType.contains(meetingTypeEnum.Fiveteen.getValue()));
		assertTrue(expectedVideoUrl.contains(url));
		
		gmail.deleteOpenedMail(webSupportDriver);
		calendar.closeTab(webSupportDriver);

		// open meeting types
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_event_details_on_custom_video_url_with_zoom is Passed!");
	}

}
