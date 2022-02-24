package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountOverviewTab;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class CalendarHoursCases extends SupportBase {

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
	
	
	//Verify Holiday calendars setting available on Hours sub tab
	@Test(groups = { "Regression" })
	public void verify_holiday_cal_setting_on_hours_sub_tab() {
		System.out.println("Test case --verify_holiday_cal_setting_on_hours_sub_tab-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);

		// create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 1, 1);

		// creating holiday
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName,
				startDate, endDate);
		
		//get holiday list
		List<String> holidayList = overViewTab.getAllHolidayListAvailable(webSupportDriver);

		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);
		
		//get holiday cal heading
		String text = calendar.getHolidayHeadLineText(webSupportDriver);
		assertTrue(text.contains("Select holiday calendars that will automatically mark you as unavailable"));
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//is holiday calendar DropDown visible
		calendar.isHolidayCalendarListVisible(webSupportDriver);

		// get holiday calendar list
		List<String> holidayCal = calendar.getHolidayCalendarListText(webSupportDriver);
		
		//is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//assert
		assertEquals(holidayCal.size(), holidayList.size());
		assertTrue(holidayCal.containsAll(holidayList));
		
		//delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
				
		//deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Holiday Calendars dropdown displayed only System calendars in list
	//Add system calendars on account's overview tab
	//Verify drop-down not populated with Connected account's calendars
	//Verify user removed selected holiday calendars on click of clip (X)
	//Verify user select one or more calendars from drop-down
	@Test(groups = { "Regression" })
	public void verify_holiday_calendar_drop_down_displayed_system_calendar() {
		System.out.println("Test case --verify_holiday_calendar_drop_down_displayed_system_calendar-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 1, 1);

		// creating holiday
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);

		//get holiday list
		List<String> holidayList = overViewTab.getAllHolidayListAvailable(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		
		//get conflict calendar list
		List<String> conflictCal= calendar.getConflictCalendarListText(webSupportDriver);

		calendar.clickCalendarHours(webSupportDriver);

		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// get holiday calendar list
		List<String> holidayCal = calendar.getHolidayCalendarListText(webSupportDriver);
		
		// assertion
		assertTrue(holidayCal.contains(holidayName));
		
		//assert
		assertFalse(holidayCal.containsAll(conflictCal));
		
		//select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 1);
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		//calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		String holiday = calendar.getHolidayNameSelected(webSupportDriver);
		//assert
		assertTrue(holidayList.contains(holiday));
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		// remove holiday calendar
		calendar.removeHolidayCalendar(webSupportDriver);
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		//calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		// opening overview tab
		//delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName);
		overViewTab.deleteRecords(webSupportDriver, holidayName);
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		
		//open cal hour
		calendar.clickCalendarHours(webSupportDriver);
		
		holiday = calendar.getHolidayNameSelected(webSupportDriver);
		//assert
		assertEquals(holiday, "");
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	
	//verify user update existing system calendar from Account's overview tab
	@Test(groups = { "Regression" })
	public void update_delete_event_into_holiday_schedule() {
		System.out.println("Test case --update_delete_event_into_holiday_schedule-- started ");
		// updating the webSupportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String holidayEventUpdate = "AutoHolidayEventUpdate".concat(HelperFunctions.GetRandomString(3));
		
		System.out.println("Starting creating holiday");
		
		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);
		
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(webSupportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));

		//updating events into holiday
		String newStartDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 0, 0);
		String newEndDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 0, 0);
		overViewTab.updateEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName, startDate, endDate, newStartDate, newEndDate, holidayEventUpdate);
	
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventUpdate);
		
		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(webSupportDriver, holidayName);
				
		//deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);
		
		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --update_delete_event_into_holiday_schedule-- passed ");
	}
	

	//Verify availability of booking for multiple holiday schedule in single holiday calendar
	@Test(groups = { "Regression" })
	public void verify_holiday_calendar_on_persoanl_cal_page_with_15_min_meeting() {
		System.out.println("Test case --verify_holiday_calendar_on_persoanl_cal_page_with_15_min_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		
		//create holiday
		String holidayName2 = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName2 = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		
		String startDate2 = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);
		String endDate2 = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);

		// creating holiday 1
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);

		// creating holiday 2
		overViewTab.AddEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName2, startDate2, endDate2);
		
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
	
		calendar.openCalendarTab(webSupportDriver);
		//open hours tab
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);

		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 1);
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		assertTrue(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate, endDate));
		assertTrue(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate2, endDate2));
		calendar.switchToTab(webSupportDriver, 2);
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// remove holiday calendar
		calendar.removeHolidayCalendar(webSupportDriver);

		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify dates are not blocked
		assertFalse(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate, endDate));
		assertFalse(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate2, endDate2));
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		//open hours tab
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// opening overview tab
		//delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName);
		overViewTab.deleteRecords(webSupportDriver, holidayName);
		
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName2, holidayEventName2);
		overViewTab.deleteRecords(webSupportDriver, holidayName2);
				
		//deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify availability of booking for more than one holiday calendars
	//Verify user's availability of booking page ,after removing holiday calendars
	//Verify availabilty of personal calendars booking page changed on selecting holiday calendars
	@Test(groups = { "Regression" })
	public void verify_holiday_calendar_on_persoanl_cal_page_with_30_min_meeting() {
		System.out.println("Test case --verify_holiday_calendar_on_persoanl_cal_page_with_30_min_meeting-- started ");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);

		// open account overview and add holiday
		// opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//create holiday
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		
		//create holiday
		String holidayName2 = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName2 = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName2 = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));

		System.out.println("Starting creating holiday");

		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		
		String startDate2 = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);
		String endDate2 = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);

		// creating holiday 1
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);

		// creating holiday 2
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName2, holidayDescriptionName2, holidayEventName2, startDate2, endDate2);
		
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		//open hours tab
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);

		calendar.clickCalendarHours(webSupportDriver);

		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 2);
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		assertTrue(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate, endDate));
		assertTrue(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate2, endDate2));
		calendar.switchToTab(webSupportDriver, 2);
		
		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// remove holiday calendar
		calendar.removeHolidayCalendar(webSupportDriver);

		// is holiday calendar option visible
		calendar.clickHolidayCalendarInput(webSupportDriver);
		
		//save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//verify dates are not blocked
		assertFalse(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate, endDate));
		assertFalse(calendar.verifyHolidayCalendarBlocksTheDay(webSupportDriver, startDate2, endDate2));
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		//open hours tab
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Sunday");
		calendar.selectDayCheckBox(webSupportDriver, "Saturday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// opening overview tab
		//delete created holiday
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName);
		overViewTab.deleteRecords(webSupportDriver, holidayName);
		
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName2, holidayEventName2);
		overViewTab.deleteRecords(webSupportDriver, holidayName2);
		//deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Hours sub tab under personal calendar
	//verify by un-checking checkbox slot should unavailable to that day
	//Verify user try to select incorrect time format
	@Test(groups = { "Regression" })
	public void set_time_on_hours_tab_and_verify_grayed_by_uncheck_day() {
		System.out.println("set_time_on_hours_tab_and_verify_grayed_by_uncheck_day started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);
		
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// open meeting and verify day is blocked
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertFalse(calendar.verifyDayIsBlocked(webSupportDriver, "Mon"));
		calendar.closeTab(webSupportDriver);
		
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickCalendarHours(webSupportDriver);
		
		//assert head line
		String hourHeadline = calendar.getHoursHeadLineText(webSupportDriver);
		assertEquals("Designate your calendar availability across days and hours", hourHeadline);
		
		// save button disabled
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		//count unavailable days
		int count = calendar.getUnavilableDaysCount(webSupportDriver);
		
		//select checbox of monday and check monday is grayed out
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		
		//count should increased by 1
		int count2 =calendar.getUnavilableDaysCount(webSupportDriver);
		assertEquals((count+1), count2);
		
		//assert text of unavailable day
		assertEquals(calendar.getUnavilableDaysText(webSupportDriver), "unavailable");
		
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting and verify day is blocked
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		assertTrue(calendar.verifyDayIsBlocked(webSupportDriver, "Mon"));
		calendar.closeTab(webSupportDriver);
		
		calendar.switchToTab(webSupportDriver, 2);
		calendar.clickCalendarHours(webSupportDriver);
		
		//enter time in monday
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//assert
		String start = calendar.getDayStartTime(webSupportDriver, "Monday");
		String end = calendar.getDayEndTime(webSupportDriver, "Monday");
		
		assertEquals("8:00 AM", start);
		assertEquals("6:00 PM", end);
		
		//click upward and downward for change time
		calendar.clickUpwardForStartTime(webSupportDriver, "Monday", "8:00 AM");
		calendar.clickDownwardforEndTime(webSupportDriver, "Monday", "6:00 PM");
		
		start = calendar.getDayStartTime(webSupportDriver, "Monday");
		end = calendar.getDayEndTime(webSupportDriver, "Monday");
		
		assertEquals("8:00 AM", start);
		assertEquals("6:00 PM", end);
		
		//enter default time
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("set_time_on_hours_tab_and_verify_grayed_by_uncheck_day is Passed!");
	}
	
	//Verify personal calendar availability affected on updating end time of the day
	//Verify personal calendar availability affected on updating start time of the day
	@Test(groups = { "Regression" })
	public void change_start_end_time_and_verify_on_personal_cal_page() {
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
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
		
		calendar.clickCalendarHours(webSupportDriver);
		
		// change time
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		assertTrue(calendar.isToastMessageVisible(webSupportDriver));
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		
		//verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "8:00", "5:45");
		calendar.switchToTab(webSupportDriver, 2);
		
		//enter time in monday
		calendar.clickCalendarHours(webSupportDriver);
		calendar.enterTimeOfDayInHoursTab(webSupportDriver, "Monday", "1:00 AM", "8:00 PM");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		assertTrue(calendar.isToastMessageVisible(webSupportDriver));
		
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		webSupportDriver.navigate().refresh();
		
		//verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "1:00", "7:45");
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
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
		calendar.switchToTab(webSupportDriver, 2);
		
		//enter default time
		calendar.clickCalendarHours(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page is Passed!");
	}
	
	//Verify availability of time slot form 12 Am to 6 Pm
	@Test(groups = { "Regression" })
	public void change_start_end_time_and_verify_on_personal_cal_page_15min_meeting() {
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_15min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Fiveteen);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "8:00", "5:45");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter time in monday
		calendar.clickCalendarHours(webSupportDriver);
		calendar.enterTimeOfDayInHoursTab(webSupportDriver, "Monday", "1:00 AM", "8:00 PM");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "1:00", "7:45");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter default time
		calendar.clickCalendarHours(webSupportDriver);
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_15min_meeting is Passed!");
	}
	
	//Verify availability with All days
	@Test(groups = { "Regression" })
	public void change_start_end_time_and_verify_on_personal_cal_page_30min_meeting() {
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_30min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "8:00", "5:30");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter time in monday
		calendar.clickCalendarHours(webSupportDriver);
		calendar.enterTimeOfDayInHoursTab(webSupportDriver, "Monday", "1:00 AM", "8:00 PM");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "1:00", "7:30");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter default time
		calendar.clickCalendarHours(webSupportDriver);
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_30min_meeting is Passed!");
	}
	
	//Verify availability with All days 45 min and 60 min meeting
	@Test(groups = { "Regression" })
	public void change_start_end_time_and_verify_on_personal_cal_page_45min_meeting() {
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_45min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
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

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "8:00", "5:00");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter time in monday
		calendar.clickCalendarHours(webSupportDriver);
		calendar.enterTimeOfDayInHoursTab(webSupportDriver, "Monday", "1:00 AM", "8:00 PM");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "1:00", "7:00");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter default time
		calendar.clickCalendarHours(webSupportDriver);
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_45min_meeting is Passed!");
	}
		
	// Verify availability with All days
	@Test(groups = { "Regression" })
	public void change_start_end_time_and_verify_on_personal_cal_page_60min_meeting() {
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_60min_meeting started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Sixty);

		calendar.clickCalendarHours(webSupportDriver);
		// change time
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));

		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "8:00", "5:00");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter time in monday
		calendar.clickCalendarHours(webSupportDriver);
		calendar.enterTimeOfDayInHoursTab(webSupportDriver, "Monday", "1:00 AM", "8:00 PM");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 2);

		// verify start and end time
		calendar.verifyStartAndEndTimeOnPersonalCalPage(webSupportDriver, "1:00", "7:00");
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);

		// enter default time
		calendar.clickCalendarHours(webSupportDriver);
		calendar.unSelectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.selectDayCheckBox(webSupportDriver, "Monday");
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("change_start_end_time_and_verify_on_personal_cal_page_60min_meeting is Passed!");
	}
	
	//Verify if holiday range extends then meeting slot should not available for already open calendar link
	@Test(groups = { "Regression" })
	public void schedule_an_event_after_holiday_range_extends() {
		System.out.println("Test case --schedule_an_event_after_holiday_range_extends-- started ");
		// updating the webSupportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String holidayEventUpdate = "AutoHolidayEventUpdate".concat(HelperFunctions.GetRandomString(3));
		
		System.out.println("Starting creating holiday");
		
		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 0, 0);
		
		overViewTab.createHolidaySchedule(webSupportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(webSupportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));

		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open meeting types
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		calendar.createMeeting(webSupportDriver, HelperFunctions.GetRandomString(5), meetingTypeEnum.Thirty);
		
		calendar.clickCalendarHours(webSupportDriver);
		
		calendar.clickHolidayCalendarInput(webSupportDriver);
		//select holiday calendar
		calendar.selectNumberOfHolidayCalendar(webSupportDriver, 1);
		calendar.clickHolidayCalendarInput(webSupportDriver);

		// save settings
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		calendar.verifySucessSaveToastMessage(webSupportDriver);
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//open meeting and select slot for event booking
		calendar.openMeetingEventPage(webSupportDriver);
		
		String event = HelperFunctions.GetRandomString(5);
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111";
		String description = HelperFunctions.GetRandomString(6);

		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		
		calendar.switchToTab(webSupportDriver, 2);
		//opening overview tab
		dashboard.clickAccountsLink(webSupportDriver);
		overViewTab.openOverViewTab(webSupportDriver);
		
		//updating events into holiday
		String newStartDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 0, 0);
		String newEndDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 0, 0);
		overViewTab.updateEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventName, startDate, endDate, newStartDate, newEndDate, holidayEventUpdate);
	
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		//click schedule event and verify error
		calendar.clickEventScheduleButton(webSupportDriver);
		calendar.verifyInvalidPersonalLinkUrlError(webSupportDriver);
		
		//deleting events into holiday
		calendar.switchToTab(webSupportDriver, 2);
		overViewTab.deleteEventsIntoHolidayScheduleRecords(webSupportDriver, holidayName, holidayEventUpdate);
				
		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(webSupportDriver, holidayName);
		
		//deleting events into holiday
		overViewTab.deleteAllHolidaysIfExists(webSupportDriver);
		
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		
		// open meeting types
		calendar.openCalendarTab(webSupportDriver);
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting webSupportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --schedule_an_event_after_holiday_range_extends-- passed ");
	}
	
	//Verify when agent have multiple config calendars enabled then upon creation of event from PC reflected on actual calendar
	//Verify Schedule event with multiple conflict calendar checked across different calendar account
	//Verify availability updated on PC by unchecking existing conflict calendar
	//Verify availability updated on PC after delete existing event on checked conflict calendar only
	@Test(groups = { "Regression" })
	public void create_event_and_verify_that_event_on_google_calendar() {
		System.out.println("create_event_and_verify_that_event_on_google_calendar started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Fiveteen);
		
		//get default cal
		String defaultCal = calendar.getMeetingTypeDefaultCalendar(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
	
		// get Slot time
		String initSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);

		// create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);

		String event = HelperFunctions.GetRandomString(5);
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111";
		String description = HelperFunctions.GetRandomString(6);

		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));

		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		// get Slot time
		String actualSlot = calendar.getNextSlotTimeOnPersonalCalPage(webSupportDriver);

		// assert
		assertNotEquals(initSlot, actualSlot);
		calendar.switchToTab(webSupportDriver, 2);
		
		//uncheck conflict cal
		calendar.clickCalendarAvailablity(webSupportDriver);
		calendar.checkConflictCalByName(webSupportDriver, defaultCal);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// verify slot time
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail2 = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		
		//assert
		assertEquals(timeAvail.size(), timeAvail2.size());
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open gmail calendar
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		
		//is event visible on cal
		assertTrue(googleCalendar.isEventVisibleOnCalendar(webSupportDriver, event));
		googleCalendar.deleteEventByNameOnCalendar(webSupportDriver, event);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		//check conflict cal
		calendar.clickCalendarAvailablity(webSupportDriver);
		calendar.checkConflictCalByName(webSupportDriver, defaultCal);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
				
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail3 = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		
		//assert
		assertEquals(timeAvail3.size(), timeAvail2.size());
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("create_event_and_verify_that_event_on_google_calendar is Passed!");
	}
	
	//Verify availability updated on PC after update existing event on checked conflict calendar only
	@Test(groups = { "Regression" })
	public void update_event_on_calendar_and_verify_on_personal_cal_page() {
		System.out.println("update_event_on_calendar_and_verify_on_personal_cal_page started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);

		// add meeting type
		String meeting = HelperFunctions.GetRandomString(5);
		calendar.createMeeting(webSupportDriver, meeting, meetingTypeEnum.Thirty);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);

		// create meeting and shedule meeting
		calendar.openMeetingEventPage(webSupportDriver);

		String event = HelperFunctions.GetRandomString(5);
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111";
		String description = HelperFunctions.GetRandomString(6);

		// valid event
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		String eventTime = calendar.getEventTimeFromScheduleFormPage(webSupportDriver);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));

		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail2 = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
		
		//assert
		assertEquals((timeAvail2.size()+1), timeAvail);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open gmail calendar
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		
		//is event visible on cal
		assertTrue(googleCalendar.isEventVisibleOnCalendar(webSupportDriver, event));
		Date endDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("h:mma");
		try {
			endDate = formatter.parse(eventTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		googleCalendar.editEventOnCalendar(webSupportDriver, event, HelperFunctions.GetCurrentDateTime("d MMMM"), HelperFunctions.GetCurrentDateTime("d MMMM"), "", formatter.format(HelperFunctions.addMinutesToDate(endDate, 60)));
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		//got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail3 = calendar.getAllSlotsAvialableForDay(webSupportDriver);

		// assert
		assertEquals((timeAvail3.size()+2), timeAvail.size());
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		// open gmail calendar
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openGoogleCalendarInNewTab(webSupportDriver);
		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		
		googleCalendar.deleteEventByNameOnCalendar(webSupportDriver, event);
		googleCalendar.closeTab(webSupportDriver);
		googleCalendar.switchToTab(webSupportDriver, 2);
		
		// open meeting types
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("update_event_on_calendar_and_verify_on_personal_cal_page is Passed!");
	}
}
