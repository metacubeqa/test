package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.personalCalendar.SoftphoneCalendarPage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;

public class ConflictCalForIntegratedAccount extends SupportBase {
	
	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	SoftphoneCalendarPage softphoneCal = new SoftphoneCalendarPage();
	
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
	
	//Verify user able to select 5 conflict calendars of default account on Admin page
	//Show warning when user has more than 5 conflict calendars
	//Show tooltip on checked of 6th Conflict Calendar
	//Verify user not able to select more than 5 conflict calendars on Admin page when 2 or more accounts integrated
	@Test(groups = { "Regression" })
	public void verify_conflict_calendar_limit_on_calendar_tab() {
		System.out.println("Test case --verify_conflict_calendar_limit_on_calendar_tab-- started ");
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		
		// check Conflict Calendar boxes
		calendar.checkConflictCalendar(webSupportDriver, 5);
		assertFalse(calendar.isToastMessageVisible(webSupportDriver));
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// verify Conflict Cal Toast Message
		calendar.checkConflictCalendar(webSupportDriver, 6);
		calendar.verifyConflictCalToastMessage(webSupportDriver);
		
		//verify heading text
		calendar.verifyAvailablityTabHeadingText(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);

		// remove integration
		calendar.removeGmailIntegration(webSupportDriver);

		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		//get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		//is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, integrationDefaultCal));
		int actual = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);

		// go to meeting type and select default cal
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.selectAnotherDefaultCal(webSupportDriver);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		// open calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		
		//is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, integrationDefaultCal));
		int expected = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		assertEquals(actual, expected);
		
		// go to meeting type and select default cal
		calendar.clickMeetingTypesTab(webSupportDriver);
		calendar.selectDefaultCalByName(webSupportDriver, integrationDefaultCal);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify the provider email Conflict Calendar is checked automatically on default calendar
	//Verify Default conflict calendar should checked when first connected account removed
	//Verify User have to manually check default and conflict calendar other than primary ,after account connected
	//Verify conflict calendar other than primary not gets checked automatically after selecting as Default Calendar (For one account)
	//Verify an Admin User lands on the availability tab and its shows the default calendar set
	@Test(groups = { "Regression" })
	public void verify_on_calendar_tab_default_availablity_sub_tab_should_be_open() {
		System.out.println("Test case --verify_on_calendar_tab_default_availablity_sub_tab_should_be_open-- started ");
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		
		//remove integration
		calendar.removeExchangeIntegration(webSupportDriver);
		
		//add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
				
		// default calendar visible
		assertTrue(calendar.isDefaultCalOnIntegrationTab(webSupportDriver));
		
		//get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// default calendar visible
		assertTrue(calendar.isDefaultCalendarVisible(webSupportDriver));
		
		// get deafult cal
		String defaultCal = calendar.getAvailablityDefaultCalendar(webSupportDriver);
		
		//assert
		assertEquals(defaultCal, integrationDefaultCal);
		
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, defaultCal);
		
		// toast message
		calendar.verifyAtLeastOneConflictCalToastMessage(webSupportDriver);
		
		// check Conflict Calendar boxes
		calendar.clickCalendarforExpandMode(webSupportDriver, exchangeEmail);
		calendar.checkConflictCalendar(webSupportDriver, 2);
		
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, defaultCal);

		// verify Conflict Cal Toast Message
		assertFalse(calendar.isToastMessageVisible(webSupportDriver));
		calendar.checkConflictCalByName(webSupportDriver, defaultCal);
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		
		//remove Gmail integration
		calendar.removeGmailIntegration(webSupportDriver);
		
		//get default cal of integration tab
		integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
				
		// get deafult cal
		defaultCal = calendar.getAvailablityDefaultCalendar(webSupportDriver);
				
		//assert
		assertEquals(defaultCal, integrationDefaultCal);
		
		// select default cal in conflict cal
		calendar.checkConflictCalByName(webSupportDriver, "Calendar");
				
		//is default cal selected
		assertTrue(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, "Calendar"));
		
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		
		//add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Conflict calendar checked on Web app and softphone are not interlinked
	//Verify calendars added by using softphone and Admin console are not impacting on each other
	@Test(groups = { "Regression" })
	public void verify_conflict_calendar_checked_not_impacting_on_softphone() {
		System.out.println("Test case --verify_conflict_calendar_checked_not_impacting_on_softphone-- started ");
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		
		//add gmail integration
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		//calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		List<String> conflict = calendar.getConflictCalendarListText(webSupportDriver);
		calendar.checkConflictCalByName(webSupportDriver, conflict.get(0));
		calendar.checkConflictCalByName(webSupportDriver, conflict.get(2));
		calendar.clickMeetingSettingsSaveButton(webSupportDriver);
		
		int initial = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		dashboard.switchToTab(webSupportDriver, 1);
		
		//softphone 
		softphoneCal.clickCalendarIcon(webSupportDriver);
		softphoneCal.clickCalendarSetting(webSupportDriver);
		
		//click 3 cal
		int softphone = softphoneCal.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		assertEquals(0, softphone);
		
		softphoneCal.checkConflictCalByName(webSupportDriver, conflict.get(3));
		
		//switch back and check conflict cal
		dashboard.switchToTab(webSupportDriver, 2);
		webSupportDriver.navigate().refresh();
		int latest = calendar.getNumberOfCheckedConflictCalSelected(webSupportDriver);
		
		assertFalse(calendar.checkConflictCalIsSelectedOrNot(webSupportDriver, conflict.get(3)));
		//assert
		assertEquals(initial, latest);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration
		calendar.openIntegrationTab(webSupportDriver);
		
		//add gmail integration
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

}
