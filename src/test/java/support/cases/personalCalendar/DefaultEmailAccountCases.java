package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;

public class DefaultEmailAccountCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	
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
	
	
	//Default email account drop-down is disabled when there is no account connected
	//Verify default Email account drop down when user have one account integrated
	//Verify by removing default connected account, next account set as Default Email Account
	@Test(groups = { "Regression" })
	public void verify_default_cal_and_integration_drop_down() {
		System.out.println("Test case --verify_default_cal_and_integration_drop_down-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeIntegration(webSupportDriver);
		
		// default calendar visible
		assertTrue(calendar.isDefaultCalOnIntegrationTab(webSupportDriver));
		// check save button is disabled by default
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));
		
		//get default cal of integration tab
		String integrationDefaultCal = calendar.getNoCalTextOnIntegrationTab(webSupportDriver);
		assertEquals("Default Email Account", integrationDefaultCal);
		
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// default calendar visible
		assertTrue(calendar.isDefaultCalOnIntegrationTab(webSupportDriver));
		// check save button is disabled by default
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		//get default cal of integration tab
		integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);
		
		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);
		
		//get default cal
		String calDefaultCal = calendar.getAvailablityDefaultCalendar(webSupportDriver);
		
		//assert
		assertEquals(calDefaultCal, integrationDefaultCal);
		
		//add exchange integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// remove gmail integration
		calendar.removeGmailIntegration(webSupportDriver);
		
		// get default cal of integration tab
		integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// get default cal
		calDefaultCal = calendar.getAvailablityDefaultCalendar(webSupportDriver);

		// assert
		assertEquals(calDefaultCal, integrationDefaultCal);
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	
	//Verify default Email Account drop down when user have two accounts integrated
	@Test(groups = { "Regression" })
	public void verify_email_drop_down_when_2account_integrated() {
		System.out.println("Test case --verify_email_drop_down_when_2account_integrated-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		// remove integration
		calendar.removeIntegration(webSupportDriver);
		
		// add integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// default calendar visible
		assertTrue(calendar.isDefaultCalOnIntegrationTab(webSupportDriver));
		// check save button is disabled by default
		assertTrue(calendar.isSaveButtonDisabled(webSupportDriver));

		// get default cal of integration tab
		String integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		// open calendar tab
		calendar.openCalendarTab(webSupportDriver);

		// get default cal
		String calDefaultCal = calendar.getAvailablityDefaultCalendar(webSupportDriver);

		// assert
		assertEquals(calDefaultCal, integrationDefaultCal);
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		//select exchange account as default calendar
		calendar.selectExchangeAsDefaultAccount(webSupportDriver);
		calendar.clickIntegrationSaveButton(webSupportDriver);
		
		// get default cal of integration tab
		integrationDefaultCal = calendar.getDefaultCalOnIntegrationTab(webSupportDriver);

		//assert
		assertEquals(exchangeEmail, integrationDefaultCal);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}

}
