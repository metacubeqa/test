package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.time.Month;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.GoogleCalendar;
import utility.HelperFunctions;

public class CalendarManagementCases extends SupportBase {

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
	
	//By default Availabilty sub tab open after clicking on Calendar tab
	@Test(groups = { "Regression" })
	public void verify_on_calendar_tab_default_availablity_sub_tab_should_be_open() {
		System.out.println("Test case --verify_on_calendar_tab_default_availablity_sub_tab_should_be_open-- started ");
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

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify not create multiple events on clicking multiple times on Schedule event button while creating an event
	//Verify "See ringDNA Support" button on calendar Confirmation pages updated to Sales Academy Blog
	//Verify user should not be able to enter bad data while creating calendar event
	//Verify Calendar Booking page visitors can add a meeting agenda when scheduling a meeting
	@Test(groups = { "Regression" })
	public void schedule_event_and_verify_sales_academy_blog_page() {
		System.out.println("schedule_event_and_verify_sales_academy_blog_page started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
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
		
		// verify place holders
		calendar.verifyPlaceHolderValueOfEventSummaryFields(webSupportDriver);
		
		// get description max value
		String length = calendar.getMaxLengthOfEventSummaryDescriptionBox(webSupportDriver);
		assertEquals("255", length);
		
		String event = HelperFunctions.GetRandomString(5); 
		String email = "pranshu.ambwani@gmail.com";
		String number = "1111111"; 
		String description = HelperFunctions.GetRandomString(5);
		
		// invalid event
		calendar.scheduleEventSummary(webSupportDriver, HelperFunctions.GetRandomString(5), HelperFunctions.GetRandomString(5), HelperFunctions.GetRandomString(5), HelperFunctions.GetRandomString(5), "");
		calendar.clickEventScheduleButton(webSupportDriver);
		assertTrue(calendar.isScheduleButtonVisible(webSupportDriver));
		
		// verify errors for bad char
		calendar.isEventSummaryFieldErrorsVisible(webSupportDriver);
		
		// valid event
		webSupportDriver.navigate().refresh();
		calendar.scheduleEventSummary(webSupportDriver, event, email, number, HelperFunctions.GetRandomString(5), description);
		calendar.clickEventScheduleButton(webSupportDriver);
		assertFalse(calendar.isScheduleButtonVisible(webSupportDriver));
		
		//click Sales Academy Blog Button Visible
		assertTrue(calendar.isSalesAcademyBlogButtonVisible(webSupportDriver));
		
		calendar.clickSalesAcademyBlogButton(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		//get url
		calendar.waitUntilSalesLogoVisibleOnPage(webSupportDriver);
		String url = webSupportDriver.getCurrentUrl();
		System.out.println(url);
		assertEquals("https://www.ringdna.com/sales-strategy", url);
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		calendar.closeTab(webSupportDriver);
		
		//click meeting settings
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
		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("schedule_event_and_verify_sales_academy_blog_page is Passed!");
	}
	
	//Verify user able to navigate between dates on Week/Month view through Next and Prev icons/links
	//Verify the percentage of available time for an individual day displayed on an external landing page
	@Test(groups = { "Regression" })
	public void verify_next_prev_icons_on_personal_cal_page_in_week_view() {
		System.out.println("Test case --verify_next_prev_icons_on_personal_cal_page_in_week_view-- started ");
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
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// select month
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		//calendar.verifyPickWeekMonth(webSupportDriver, HelperFunctions.GetCurrentDateTime("MMMM"));
		
		//open slot
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		int unavailable = calendar.getUnavailableBlockedDays(webSupportDriver);
		int available = calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver);
		
		assertEquals(7, (unavailable+available));
		
		calendar.verifyColorOfAvailableDays(webSupportDriver);
		calendar.verifyColorOfUnavailableBlockedDays(webSupportDriver);
		
		calendar.clickPersonalCalBackButton(webSupportDriver);
		String title = calendar.getMeetingTypeTitleOnCalPage(webSupportDriver);
		assertEquals(name, title);
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		calendar.verifyAmPmTextOnSlotsPage(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		assertTrue(timeAvail.size() > 1);
		
		calendar.closeTab(webSupportDriver);		
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify available meeting times for an individual day displayed on an external landing page.
	@Test(groups = { "Regression" })
	public void verify_next_prev_icons_on_personal_cal_page_in_month_view() {
		System.out.println("Test case --verify_next_prev_icons_on_personal_cal_page_in_week_view-- started ");
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
		
		// got to personalize sub section
		calendar.clickCalendarPersonalize(webSupportDriver);
		calendar.clickYourCalendarLink(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		
		// select month
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.verifyPickWeekMonth(webSupportDriver);
		
		//open slot
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		int unavailable = calendar.getUnavailableBlockedDays(webSupportDriver);
		int available = calendar.getAvilableDaysCountOnCalendarPage(webSupportDriver);
		
		Month month = Month.valueOf(HelperFunctions.GetCurrentDateTime("MMMM"));
		assertEquals(month.getValue(), (unavailable+available));
		
		calendar.verifyColorOfAvailableDays(webSupportDriver);
		calendar.verifyColorOfUnavailableBlockedDays(webSupportDriver);
		
		calendar.clickPersonalCalBackButton(webSupportDriver);
		String title = calendar.getMeetingTypeTitleOnCalPage(webSupportDriver);
		assertEquals(name, title);
		
		// verify slot time
		calendar.openMeetingTypeOnPersonalCalendarPage(webSupportDriver);
		calendar.clickNextButtonOnCalendarPage(webSupportDriver);
		calendar.openSlotTimePageOnPersonalCalPage(webSupportDriver, 1);
		
		calendar.verifyAmPmTextOnSlotsPage(webSupportDriver);
		calendar.verifyRingDnaLogo(webSupportDriver, name);
		
		List<WebElement> timeAvail = calendar.getAllSlotsAvialableForDay(webSupportDriver);
		assertTrue(timeAvail.size() > 1);
		
		calendar.closeTab(webSupportDriver);		
		calendar.switchToTab(webSupportDriver, 2);

		// delete meeting if present
		calendar.deleteMeetingType(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

}
