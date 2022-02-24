package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.leadTimeEnum;
import support.source.personalCalendar.PersonalCalendarPage.maxFutureBooking;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class MeetingSettingsCases extends SupportBase {

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
	
	//Verify meeting settings sub tab 
	//Verify "Max Meetings Per Day" setting is not visible on UI and No limit on Max. meetings per day
	@Test(groups = { "Regression" })
	public void verify_meeting_settings_sub_tab() {
		System.out.println("Test case --verify_meeting_settings_sub_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		//open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		
		//default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		calendar.clickMeetingSettings(webSupportDriver);
		
		//is lead time option visible
		calendar.isLeadTimeOptionsVisible(webSupportDriver);
		
		//is max future booking option visible
		calendar.isFutureBookingOptionsVisible(webSupportDriver);
		
		// message Setting questions visible
		calendar.isMessageSettingsQuestionsVisible(webSupportDriver);
		
		//custom lead and max future booking text
		calendar.customLeadAndFutureBookingText(webSupportDriver);
		
		// verify default message settings radio is selected
		//calendar.isDefaultLeadTimeAndMaxFutureBookingIsSelected(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify changes should persist after click on Save button 
	@Test(groups = { "Regression" })
	public void verify_changes_after_clicking_on_save_button() {
		System.out.println("Test case --verify_changes_after_clicking_on_save_button-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		calendar.clickMeetingSettings(webSupportDriver);

		// is lead time option visible
		calendar.isLeadTimeOptionsVisible(webSupportDriver);

		// is max future booking option visible
		calendar.isFutureBookingOptionsVisible(webSupportDriver);

		// message Setting questions visible
		calendar.isMessageSettingsQuestionsVisible(webSupportDriver);

		// verify default message settings radio is selected
		//calendar.isDefaultLeadTimeAndMaxFutureBookingIsSelected(webSupportDriver);
		
		//is save button disabled
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Thirty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.clickMeetingSettings(webSupportDriver);
		
		// verify radio button selected
		calendar.assertWhichFutureBookingRadioButtonIsSelected(webSupportDriver, maxFutureBooking.Thirty);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify custom taking for integer values only
	@Test(groups = { "Regression" })
	public void verify_lead_and_booking_custom_taking_integers_value_only() {
		System.out.println("Test case --verify_lead_and_booking_custom_taking_integers_value_only-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		//open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		//default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		calendar.clickMeetingSettings(webSupportDriver);
		
		//select lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Immediate);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//enter wrong input
		calendar.enterLeadTimeCustom(webSupportDriver, "!@#$%");
		
		//verify toast message for lead
		calendar.verifyCustomToastMessage(webSupportDriver);
		
		// enter wrong input
		calendar.enterLeadTimeCustom(webSupportDriver, HelperFunctions.GetRandomString(3));

		// verify toast message for lead
		calendar.verifyCustomToastMessage(webSupportDriver);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
				
		//enter wrong input
		calendar.enterMaxFutureBookingCustom(webSupportDriver, "!@#$%");
				
		//verify toast message for max future
		calendar.verifyCustomToastMessage(webSupportDriver);
		
		// enter wrong input
		calendar.enterMaxFutureBookingCustom(webSupportDriver, HelperFunctions.GetRandomString(3));

		// verify toast message for lead
		calendar.verifyCustomToastMessage(webSupportDriver);
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify UI validation to the Custom field for Max. Future Booking 
	@Test(groups = { "Regression" })
	public void verify_ui_validation_to_custom_max_future_booking() {
		System.out.println("Test case --verify_ui_validation_to_custom_max_future_booking-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
//		calendar.removeExchangeIntegration(webSupportDriver);
//		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.openCalendarTab(webSupportDriver);
//		
//		// select max future time
		calendar.clickMeetingSettings(webSupportDriver);
//		calendar.enterMaxFutureBookingCustom(webSupportDriver, "");
//		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
//		
//		// select max future time
//		calendar.clickMeetingSettings(webSupportDriver);
//		calendar.enterMaxFutureBookingCustom(webSupportDriver, "1");
//		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
//				
//		// select max future time
//		calendar.enterMaxFutureBookingCustom(webSupportDriver, "365");
//		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
//		
//		// select wrong max future time
//		calendar.enterMaxFutureBookingCustom(webSupportDriver, "380");
//		calendar.verifyCustomOutOfLimitToastMessage(webSupportDriver); 
//		
//		//select future booking
//		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
//		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.assertWhichFutureBookingRadioButtonIsSelected(webSupportDriver, maxFutureBooking.Sixty);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Max. Future Booking with 30 days selection
	@Test(groups = { "Regression" })
	public void verify_avaialble_days_30days_max_fut_book_test() {
		System.out.println("avaialble_days_30days_max_fut_book_test started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
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
		calendar.openCalendarTab(webSupportDriver);

		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		//click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Thirty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting and verify max future booking days
		calendar.verifyMaxFutureBooking(webSupportDriver, Integer.parseInt(maxFutureBooking.Thirty.getValue()));
		
		//click meeting settings
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingSettings(webSupportDriver);

		// select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("avaialble_days_30days_max_fut_book_test is Passed!");
	}
	
	//Verify Max. Future Booking with 45 days selection
	@Test(groups = { "Regression" })
	public void verify_avaialble_days_45days_max_fut_book_test() {
		System.out.println("verify_avaialble_days_45days_max_fut_book_test started");
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
		calendar.openCalendarTab(webSupportDriver);

		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, "15Test", meetingTypeEnum.Fiveteen);
		
		//click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.FortyFive);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting and verify max future booking days
		calendar.verifyMaxFutureBooking(webSupportDriver, Integer.parseInt(maxFutureBooking.FortyFive.getValue()));
		
		//click meeting settings
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingSettings(webSupportDriver);

		// select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_avaialble_days_45days_max_fut_book_test is Passed!");
	}
		
	//Verify Max. Future Booking with 60 days selection
	//Verify future days with fast click on next button
	@Test(groups = { "Regression" })
	public void verify_avaialble_days_60days_max_fut_book_test() {
		System.out.println("verify_avaialble_days_60days_max_fut_book_test started");
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
		calendar.openCalendarTab(webSupportDriver);

		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		//click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);
		
		//select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting and verify max future booking days
		calendar.verifyMaxFutureBooking(webSupportDriver, Integer.parseInt(maxFutureBooking.Sixty.getValue()));
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_avaialble_days_60days_max_fut_book_test is Passed!");
	}
		
	//Verify Max Future Booking with custom days selection
	@Test(groups = { "Regression" })
	public void verify_avaialble_days_custom_days_max_fut_book_test() {
		System.out.println("verify_avaialble_days_custom_days_max_fut_book_test started");
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
		calendar.openCalendarTab(webSupportDriver);
		
		// click calendar hour
		calendar.clickCalendarHours(webSupportDriver);
		// select checbox of Saturday and Sunday
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Seventy);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting and verify max future booking days
		calendar.verifyMaxFutureBooking(webSupportDriver, Integer.parseInt(maxFutureBooking.Seventy.getValue()));

		// click meeting settings
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickMeetingSettings(webSupportDriver);

		// select future booking
		calendar.selectMaxFutureBooking(webSupportDriver, maxFutureBooking.Sixty);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_avaialble_days_custom_days_max_fut_book_test is Passed!");
	}
	
	//Verify Calendar Buffer setting is not visible on UI and buffer time not included while booking a meeting
	//Verify if Calendar buffer option hidden from UI then No any buffer time added while scheduling an event
	@Test(groups = { "Regression" })
	public void verify_cal_buffer_setting_is_hidden_on_ui_and_while_creating_event() {
		System.out.println("verify_cal_buffer_setting_is_hidden_on_ui_and_while_creating_event started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
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
		
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Immediate);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//open personlisation tab
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot
		String initTime1 = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		String initTime2 = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 3);
		
		//pick time and shedule meeting
		calendar.clickPickTimeByIndexOnCalPage(webSupportDriver, 2);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(6);
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));

		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		//open meeting
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);
		
		//verify next slot
		String actTime1 = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 1);
		String actTime2 = calendar.getSlotTimeOnCalPageByIndex(webSupportDriver, 2);
		
		//assert
		assertEquals(initTime1, actTime1);
		assertEquals(initTime2, actTime2);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
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
		
		// open meeting types
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// click meeting settings
		calendar.clickMeetingSettings(webSupportDriver);

		// select Lead time
		calendar.selectLeadTime(webSupportDriver, leadTimeEnum.Two);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("verify_cal_buffer_setting_is_hidden_on_ui_and_while_creating_event is Passed!");
	}
}
