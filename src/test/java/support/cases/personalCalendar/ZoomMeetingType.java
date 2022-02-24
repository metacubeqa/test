package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.accounts.AccountAccountsTab.UserTypeEnum;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.personalCalendar.PersonalCalendarPage.meetingTypeEnum;
import utility.GmailPageClass;
import utility.HelperFunctions;

public class ZoomMeetingType extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	GmailPageClass gmail = new GmailPageClass();
	
	private String gmailEmail;
	private String gmailPassword;
	private String zoomEmail;
	private String zoomPassword;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
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
	
	
	//Verify connected zoom users on accounts tab
	//Verify UI of Zoom users on accounts tab
	//Verify Zoom User Management page accessible from Account tab
	@Test(groups = { "Regression" })
	public void verify_zoom_user_management_on_account_tab() {
		System.out.println("Test case --verify_zoom_user_management_on_account_tab-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// navigating to free user account
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));

		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//get userCount
		int count = accountsTab.getUserCount(webSupportDriver);
		
		//click on manage button
		accountsTab.clickOnManageConnUserButton(webSupportDriver);
		
		//click on back to integartion
		accountsTab.clickOnBackToIntegration(webSupportDriver);
		
		//click on user count
		accountsTab.clickOnUserCount(webSupportDriver);
		
		//select user type
		accountsTab.selectZoomUserFromDropDown(webSupportDriver, UserTypeEnum.Connected);
		
		//get user list
		int users = accountsTab.getAccountNameListSize(webSupportDriver);
		
		//assert
		assertEquals(count, users);

		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_zoom_user_management_on_account_tab-- passed");
	}
	
	
	//Verify Zoom User Management page accessible from Account tab without Zoom Administrator privileges
	//Verify view users link on Account tab
	//Verify Zoom account integrate from integration tab via OAuth
	//@Test(groups = { "Regression" })
	public void verify_zoom_user_and_add_zoom_integration() {
		System.out.println("Test case --verify_zoom_user_and_add_zoom_integration-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// add zoom integration
		calendar.addZoomIntegration(webSupportDriver, zoomEmail, zoomPassword);

		// navigating to user account
		dashboard.clickAccountsLink(supportDriver);

		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//click on manage button
		accountsTab.clickOnManageButton(webSupportDriver);
		calendar.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		assertTrue(calendar.getAcessErrorOnZoomPage(webSupportDriver).contains("Access restricted"));
		calendar.closeTab(webSupportDriver);
		calendar.switchToTab(webSupportDriver, 2);
		
		//select user type
		accountsTab.clickOnZoomViewUser(webSupportDriver);
		
		//get user list
		int users = accountsTab.getAccountNameListSize(webSupportDriver);
		
		//assert
		assertTrue(users > 0);
		
		// open integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// remove integration if present
		calendar.removeZoomIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_zoom_user_and_add_zoom_integration-- passed");
	}
	
	//Verify Event Name field of event details on invitation email's body with zoom account
	@Test(groups = { "Regression" })
	public void create_event_and_verify_event_details_on_zoom_mail() {
		System.out.println("create_event_and_verify_event_details_on_zoom_mail started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addZoomIntegration(webSupportDriver, zoomEmail, zoomPassword);
		
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
		
		calendar.verifyRingDnaLogo(webSupportDriver, meeting);
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
		System.out.println("create_event_and_verify_event_details_on_zoom_mail is Passed!");
	}

}
