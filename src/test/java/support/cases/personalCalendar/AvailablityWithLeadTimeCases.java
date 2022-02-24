package support.cases.personalCalendar;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.leadTimeEnum;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class AvailablityWithLeadTimeCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	GmailPageClass gmail = new GmailPageClass();
	GoogleCalendar googleCalendar = new GoogleCalendar();
	
	private String gmailEmail;
	private String gmailPassword;
	//private String exchangeEmail;
	//private String exchangePassword;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
		//exchangeEmail = CONFIG.getProperty("exchnage_email_id");
		//exchangePassword = CONFIG.getProperty("exchnage_password");
		
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

	//Check meeting availability options when Booking Lead Time is 2 hrs
	@Test(groups = { "Regression" })
	public void check_meeting_availablity_when_lead_time_is_2hr() {
		System.out.println("check_meeting_availablity_when_lead_time_is_2hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		//select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
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
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_meeting_availablity_when_lead_time_is_2hr is Passed!");
	}
	
	//Next Availability Testing for 15min availability with 1 hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_meeting_availablity_when_lead_time_is_1hr() {
		System.out.println("check_meeting_availablity_when_lead_time_is_1hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.One);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 100) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_meeting_availablity_when_lead_time_is_1hr is Passed!");
	}
	
	//Next Availability Testing for 15min meeting type availability with Immediate Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_15_meeting_availablity_when_lead_time_is_immediate() {
		System.out.println("check_15_meeting_availablity_when_lead_time_is_immediate started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		//select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Immediate);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_15_meeting_availablity_when_lead_time_is_immediate is Passed!");
	}
	
	//Next Availability Testing for 15min availability with 3hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_meeting_availablity_when_lead_time_is_3hr() {
		System.out.println("check_meeting_availablity_when_lead_time_is_3hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Three);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 300) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_meeting_availablity_when_lead_time_is_3hr is Passed!");
	}
	
	//Next Availability Testing for 15min availability with 4hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_meeting_availablity_when_lead_time_is_4hr() {
		System.out.println("check_meeting_availablity_when_lead_time_is_4hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Four);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 400) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_meeting_availablity_when_lead_time_is_4hr is Passed!");
	}
	
	//Next Availability Testing for 15min availability with Custom Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_meeting_availablity_when_lead_time_is_custom() {
		System.out.println("check_meeting_availablity_when_lead_time_is_custom started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Five);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 500) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_meeting_availablity_when_lead_time_is_custom is Passed!");
	}
	
	//Next Availability Testing for 30 min availability with 4hr Booking Lead Time
	//Next Availability Testing for 30 min availability with 2hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_30min_meeting_availablity_when_lead_time_is_2hr_4hr() {
		System.out.println("check_30min_meeting_availablity_when_lead_time_is_2hr_4hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Four);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 400) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		remainder = addTime % 5;
		remainder = 5 - remainder;
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

		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_30min_meeting_availablity_when_lead_time_is_2hr_4hr is Passed!");
	}
	
	//Next Availability Testing for 45 min availability with 1 hr Booking Lead Time
	//Next Availability Testing for 45 min availability with 3 hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_45min_meeting_availablity_when_lead_time_is_1hr_3hr() {
		System.out.println("check_45min_meeting_availablity_when_lead_time_is_1hr_3hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.FortyFive);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.One);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 100) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Three);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 300) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}

		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_45min_meeting_availablity_when_lead_time_is_1hr_3hr is Passed!");
	}
	
	//Next Availability Testing for 60 min availability with 1hr Booking Lead Time
	//Next Availability Testing for 60 min availability with 3hr Booking Lead Time
	@Test(groups = { "Regression" })
	public void check_60min_meeting_availablity_when_lead_time_is_1hr_3hr() {
		System.out.println("check_60min_meeting_availablity_when_lead_time_is_1hr_3hr started");
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
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.One);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		int addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		int checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		int remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 100) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}
		
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Three);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);

		addTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("hmm"));
		checkTime = Integer.valueOf(HelperFunctions.GetCurrentDateTime("Hmm"));
		remainder = addTime % 5;
		remainder = 5 - remainder;
		addTime = (addTime + 300) + remainder;

		if (checkTime >= 1745) {
			assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, HelperFunctions.GetCurrentDateTime("EEE")));
		} else {

			calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
			// verify next slot is current+2
			String time = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
			time = time.replace(":", "");
			assertTrue(String.valueOf(addTime).equals(time) || time.equals("800"));
		}

		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingTypesTab(webSupportDriver);
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("check_60min_meeting_availablity_when_lead_time_is_1hr_3hr is Passed!");
	}

}
