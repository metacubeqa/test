package support.cases.personalCalendar;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountOverviewTab;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.leadTimeEnum;
import support.source.personalCalendar.PersonalCalendarPage.maxFutureBooking;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class AvailablitySettingsMeetTypesCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	GmailPageClass gmail = new GmailPageClass();
	GoogleCalendar googleCalendar = new GoogleCalendar();
	
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
	
	//Next Availability Testing for 15min availability (w/o native calendar events)
	@Test(groups = { "Regression" }, priority = 15 )
	public void verify_availablity_with_15_min_meeting() {
		System.out.println("verify_availablity_with_15_min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		//verify future day is available
		calendar.verifyFutureDaysAreAvailable(webSupportDriver);
		
		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5-remainder;
		addTime = (addTime + 200) + remainder;
		
		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		//open future day
		calendar.clickPersonalCalBackButton(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot is 8:00 am
		String nextTime = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		assertEquals("8:00", nextTime);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_with_15_min_meeting is Passed!");
	}
	
	//Availability With No Native Calendar Events (30 minute meeting)
	@Test(groups = { "Regression" }, priority = 16 )
	public void verify_availablity_with_30_min_meeting() {
		System.out.println("verify_availablity_with_30_min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		
		//verify future day is available
		calendar.verifyFutureDaysAreAvailable(webSupportDriver);
				
		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5-remainder;
		addTime = (addTime + 200) + remainder;
		
		if (checkTime >= 1730) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {
			//open current day
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			
			//verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		//open future day
		calendar.clickPersonalCalBackButton(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot is 8:00 am
		String nextTime = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		assertEquals("8:00", nextTime);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_with_30_min_meeting is Passed!");
	}
	
	//Availability With No Native Calendar Events (45 minute meeting)
	@Test(groups = { "Regression" }, priority = 17 )
	public void verify_availablity_with_45_min_meeting() {
		System.out.println("verify_availablity_with_45_min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		
		//verify future day is available
		calendar.verifyFutureDaysAreAvailable(webSupportDriver);
		
		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5-remainder;
		addTime = (addTime + 200) + remainder;
		
		if (checkTime >= 1715) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {
			//open current day
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			
			//verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		//open future day
		calendar.clickPersonalCalBackButton(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot is 8:00 am
		String nextTime = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		assertEquals("8:00", nextTime);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_with_45_min_meeting is Passed!");
	}
	
	//Availability With No Native Calendar Events (60 minute meeting)
	@Test(groups = { "Regression" }, priority = 18 )
	public void verify_availablity_with_60_min_meeting() {
		System.out.println("verify_availablity_with_60_min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		
		//verify future day is available
		calendar.verifyFutureDaysAreAvailable(webSupportDriver);
		
		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5-remainder;
		addTime = (addTime + 200) + remainder;
		
		if (checkTime >= 1700) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {
			//open current day
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			
			//verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		//open future day
		calendar.clickPersonalCalBackButton(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot is 8:00 am
		String nextTime = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		assertEquals("8:00", nextTime);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_with_60_min_meeting is Passed!");
	}
	
	//Verify next availability accessible on weekdays
	@Test(groups = { "Regression" }, priority = 7 )
	public void verify_availablity_on_weekdays_and_weekend_for_15min_meeting() {
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_15min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);

		// available sat and sun
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify all future days are available
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertEquals(calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver), 7);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
	
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// change to default
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_15min_meeting is Passed!");
	}
	
	//Verify next availability accessible on weekends
	@Test(groups = { "Regression" }, priority = 8 )
	public void verify_availablity_on_weekdays_and_weekend_for_30min_meeting() {
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_30min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);

		// available sat and sun
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify all future days are available
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertEquals(calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver), 7);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
	
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// change to default
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_30min_meeting is Passed!");
	}
	
	//Verify next availability accessible on weekends for 45min and 60 min meeting type
	@Test(groups = { "Regression" }, priority = 9 )
	public void verify_availablity_on_weekdays_and_weekend_for_45min_meeting() {
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_45min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);

		// available sat and sun
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify all future days are available
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertEquals(calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver), 7);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
	
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// change to default
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_45min_meeting is Passed!");
	}
	
	//Verify next availability accessible on weekdays for 45min and 60 min meeting type
	@Test(groups = { "Regression" }, priority = 10 )
	public void verify_availablity_on_weekdays_and_weekend_for_60min_meeting() {
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_60min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);

		// available sat and sun
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify all future days are available
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertEquals(calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver), 7);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
	
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// change to default
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_on_weekdays_and_weekend_for_60min_meeting is Passed!");
	}
	
	//Verify message when user have no availability left for the 15 and 30 min meeting type
	//Verify Next availabilty card disable when user have no availibility left
	@Test(groups = { "Regression" }, priority = 13 )
	public void verify_message_when_there_is_no_availablity_for_15_and_30_min_meeting() {
		System.out.println("Test case --verify_message_when_there_is_no_availablity_for_15_and_30_min_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		// deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);

		// create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);

		// creating holiday
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName,
				startDate, endDate);

		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		calendar.openCalendarThroughUserProfileDropDown(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);

		calendar.clickHolidayCalendarInput(webSupportDriver);
		// select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 1);
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		//calendar.verifySucessSaveToastMessage(webSupportDriver);

		// select future booking
		calendar.clickMeetingSettings(webSupportDriver);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.One);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		assertEquals(calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver), "No Availability");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// click meeting url
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.deleteMeetingType(webSupportDriver);
		
		//create 30min meeting
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		assertEquals(calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver), "No Availability");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// click meeting url
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.deleteMeetingType(webSupportDriver);
		
		// select future booking
		calendar.clickCalendarHours(webSupportDriver);
		calendar.clickMeetingSettings(webSupportDriver);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);

		// deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName);
		overViewTab.deleteRecords(webSupportDriver, holidayName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	// Verify message when user have no availability left for the 45 and 60 min meeting type
	@Test(groups = { "Regression" }, priority = 14 )
	public void verify_message_when_there_is_no_availablity_for_45_and_60_min_meeting() {
		System.out.println("Test case --verify_message_when_there_is_no_availablity_for_45_and_60_min_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		// deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);

		// create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);

		// creating holiday
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName,
				startDate, endDate);

		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar through user profile
		calendar.clickUserProfileDropDown(webSupportDriver);
		calendar.openCalendarThroughUserProfileDropDown(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);

		calendar.clickCalendarHours(webSupportDriver);

		calendar.clickHolidayCalendarInput(webSupportDriver);
		// select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 1);
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		//calendar.verifySucessSaveToastMessage(webSupportDriver);

		// select future booking
		calendar.clickMeetingSettings(webSupportDriver);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.One);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		assertEquals(calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver), "No Availability");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// click meeting url
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.deleteMeetingType(webSupportDriver);

		// create 60min meeting
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		assertEquals(calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver), "No Availability");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// click meeting url
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.clickMeetingTypeLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify error on personal cal page
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.deleteMeetingType(webSupportDriver);
		
		// select future booking
		calendar.clickCalendarHours(webSupportDriver);
		calendar.clickMeetingSettings(webSupportDriver);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);

		// deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName);
		overViewTab.deleteRecords(webSupportDriver, holidayName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Next availability when user create event for all day
	@Test(groups = { "Regression" }, priority = 11 )
	public void verify_availablity_when_user_create_event_from_native_calendar() {
		System.out.println("verify_availablity_when_user_create_event_from_native_calendar started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
				
		// create new event for a day
		String calName = HelperFunctions.GetRandomString(5);
		LocalDate dt = LocalDate.now();
		googleCalendar.createEventOnCalendar(webSupportDriver, calName, String.valueOf(dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))), String.valueOf(dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))), "6:00am",  "11:59pm");
		
		// open integration tab
		calendar.closeTab(webSupportDriver);
		dashboard.switchToTab(webSupportDriver, 2);

		// open calendar tab
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

		// open meeting and verify day is blocked
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, "Sun"));
		calendar.closeTab(webSupportDriver);

		calendar.switchToTab(webSupportDriver, 2);
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		// delete meeting if present
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_availablity_when_user_create_event_from_native_calendar is Passed!");
	}
	
	//Validate Infinity Calculations 50% Time Available
	@Test(groups = { "Regression" }, priority = 3 )
	public void validate_infinity_calculations_50_time_available() {
		System.out.println("validate_infinity_calculations_50_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		int timeSize = timeAvail.size();
		
		//verify start and end time
		for(int i=timeSize; i>10; i--) {
			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
			
			String event = HelperFunctions.GetRandomString(5); 
			String email = "pranshu.ambwani@gmail.com";
			String number = "1111111"; 
			String description = HelperFunctions.GetRandomString(5);
			
			// valid event
			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
			calendar.clickEventScheduleButton(webSupportDriver);
			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		}
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = 100-((int)pickTime);
		assertEquals(Time, 50);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_50_time_available is Passed!");
	}
	
	//Validate Infinity Calculations 75% Time Available
	@Test(groups = { "Regression" }, priority = 2 )
	public void validate_infinity_calculations_75_time_available() {
		System.out.println("validate_infinity_calculations_75_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		int timeSize = timeAvail.size();
		
		//verify start and end time
		for(int i=timeSize; i>15; i--) {
			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
			
			String event = HelperFunctions.GetRandomString(5); 
			String email = "pranshu.ambwani@gmail.com";
			String number = "1111111"; 
			String description = HelperFunctions.GetRandomString(5);
			
			// valid event
			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
			calendar.clickEventScheduleButton(webSupportDriver);
			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		}
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = 100-((int)pickTime);
		assertEquals(Time, 75);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_75_time_available is Passed!");
	}
	
	//Validate Infinity Calculations 25% Time Available
	@Test(groups = { "Regression" }, priority = 4 )
	public void validate_infinity_calculations_25_time_available() {
		System.out.println("validate_infinity_calculations_25_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		int timeSize = timeAvail.size();
		
		//verify start and end time
		for(int i=timeSize; i>5; i--) {
			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
			
			String event = HelperFunctions.GetRandomString(5); 
			String email = "pranshu.ambwani@gmail.com";
			String number = "1111111"; 
			String description = HelperFunctions.GetRandomString(5);
			
			// valid event
			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
			calendar.clickEventScheduleButton(webSupportDriver);
			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		}
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		//calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = 100-((int)pickTime);
		assertEquals(Time, 25);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_25_time_available is Passed!");
	}
	
	//Validate Infinity Calculations 100% Time Available
	@Test(groups = { "Regression" }, priority = 1 )
	public void validate_infinity_calculations_100_time_available() {
		System.out.println("validate_infinity_calculations_100_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		int timeSize = timeAvail.size();
		
		//verify start and end time
		for(int i=timeSize; i>20; i--) {
			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
			
			String event = HelperFunctions.GetRandomString(5); 
			String email = "pranshu.ambwani@gmail.com";
			String number = "1111111"; 
			String description = HelperFunctions.GetRandomString(5);
			
			// valid event
			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
			calendar.clickEventScheduleButton(webSupportDriver);
			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		}
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = 100-((int)pickTime);
		assertEquals(Time, 100);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_100_time_available is Passed!");
	}
	
	//Validate Infinity Calculations 1% Time Available
	@Test(groups = { "Regression" }, priority = 5 )
	public void validate_infinity_calculations_5_time_available() {
		System.out.println("validate_infinity_calculations_5_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		int timeSize = timeAvail.size();
		
		//verify start and end time
		for(int i=timeSize; i>1; i--) {
			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
			//calendar.clickNextButtonOnCalendarPage(webSupportDriver);
			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
			
			String event = HelperFunctions.GetRandomString(5); 
			String email = "pranshu.ambwani@gmail.com";
			String number = "1111111"; 
			String description = HelperFunctions.GetRandomString(5);
			
			// valid event
			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
			calendar.clickEventScheduleButton(webSupportDriver);
			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		}
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		//calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = 100-((int)pickTime);
		assertEquals(Time, 5);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_5_time_available is Passed!");
	}
	
	//Validate Infinity Calculations No Time Available
	//Validate Infinity Calculations 0% Time Available on Non Available days
	@Test(groups = { "Regression" }, priority = 6 )
	public void validate_infinity_calculations_0_time_available() {
		System.out.println("validate_infinity_calculations_0_time_available started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		
		// create new event for a day
		String calName = HelperFunctions.GetRandomString(5);
		LocalDate dt = LocalDate.now();
		googleCalendar.createEventOnCalendar(webSupportDriver, calName, String.valueOf(dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))), String.valueOf(dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))), "8:00am",  "6:00pm");
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
//		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
//		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
//		int timeSize = timeAvail.size();
//		
//		//verify start and end time
//		for(int i=timeSize; i>=0; i--) {
//			calendar.verifyRingDnaLogo(webSupportDriver, meeting);
//			calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
//			//calendar.clickNextButtonOnCalendarPage(webSupportDriver);
//			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
//			calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 1);
//			
//			String event = HelperFunctions.GetRandomString(5); 
//			String email = "pranshu.ambwani@gmail.com";
//			String number = "1111111"; 
//			String description = HelperFunctions.GetRandomString(5);
//			
//			// valid event
//			calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
//			calendar.clickEventScheduleButton(webSupportDriver);
//			assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
//		}
//		
//		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
//		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
//		//calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		float pickTime = calendar.getSlotAvailbleForDayInNumber(webSupportDriver, "Sun");
		
		pickTime = (int) ((pickTime/270.00)*100);
		int Time = (int)pickTime;
		assertEquals(Time, 0);
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("validate_infinity_calculations_0_time_available is Passed!");
	}
	
	//Verify schedule a event when user having connected with google and exchange calendars
	@Test(groups = { "Regression" }, priority = 12 )
	public void create_event_with_google_and_exchange_cal_accounts() {
		System.out.println("create_event_with_google_and_exchange_cal_accounts started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingEventPage(webSupportDriver);
			
		String event = HelperFunctions.GetRandomString(5);
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111";
		String description = HelperFunctions.GetRandomString(5);

		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5),description);
		calendar.clickEventScheduleButton(webSupportDriver);

		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open gmail calendar
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));

		// delete all events
		googleCalendar.deleteAllEventsOnCalendar(webSupportDriver);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_event_with_google_and_exchange_cal_accounts is Passed!");
	}

}
