package support.cases.personalCalendar;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.accounts.AccountAccountsTab.UserTypeEnum;
import support.source.accounts.AccountsPage;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import utility.GmailPageClass;
import utility.HelperFunctions;

public class ConnectToGmailCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	AccountsPage accountsPage = new AccountsPage();
	GmailPageClass gmail = new GmailPageClass();
	
	private String exchangeEmail;
	private String exchangePassword;
	private String gmailEmail;
	private String gmailPassword;
	
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
	
	//Connect ringdna user to gmail account
	//Remove Connected Gmail account from RingDNA user under users integration tab
	//@Test(groups = { "Regression" })
	public void verify_default_cal_and_integration_drop_down() {
		System.out.println("Test case --verify_default_cal_and_integration_drop_down-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		// open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		// remove integration if present
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		
		//is connection visible
		assertFalse(calendar.isGmailIntegrationVisible(webSupportDriver));
		assertFalse(calendar.isExchangeIntegrationVisible(webSupportDriver));
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openCalendarTab(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		
		//is connection visible
		assertTrue(calendar.isGmailIntegrationConnected(webSupportDriver));
		assertTrue(calendar.isExchangeIntegrationConnected(webSupportDriver));

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify message on Connect same calendar account for multiple ringdna user
	//@Test(groups = { "Regression" })
	public void verify_message_on_connect_same_account_for_multiple_user() {
		System.out.println("Test case --verify_message_on_connect_same_account_for_multiple_user-- started");
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		dashboard.switchToTab(adminDriver, 2);
		dashboard.clickOnUserProfile(adminDriver);

		// open integration tab
		calendar.openIntegrationTab(adminDriver);
		
		// remove integration if present
		calendar.removeExchangeIntegration(adminDriver);
		
		// add gmail integration
		calendar.addMultipleGmailIntegration(adminDriver, gmailEmail, gmailPassword);
		
		//verify message
		calendar.verifyMultipleAccountErrorMessage(adminDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Remove Connected Exchange account from RingDNA user under users integration tab
	//Connect with Exchange with User, Server name and id, password
	@Test(groups = { "Regression" })
	public void remove_exchange_account_in_integration_tab() {
		System.out.println("Test case --remove_exchange_account_in_integration_tab-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);

		// add exchange integration
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// is connection visible
		assertTrue(calendar.isExchangeIntegrationConnected(webSupportDriver));
		
		//remove exchnage but cancel on warning message
		calendar.clickExchangeIntRemoveButton(webSupportDriver);
		calendar.clickConfirmIntegrationCancelButton(webSupportDriver);
		
		// is connection visible
		assertTrue(calendar.isExchangeIntegrationConnected(webSupportDriver));

		// remove integration if present
		calendar.removeExchangeIntegration(webSupportDriver);

		// is connection visible
		assertFalse(calendar.isExchangeIntegrationVisible(webSupportDriver));

		// add exchange
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// open exchange and integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		
		// is connection visible
		assertTrue(calendar.isExchangeIntegrationConnected(webSupportDriver));
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.removeExchangeIntegration(webSupportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Delete the Gmail Connection from ringdna user by Account integration tab
	//Verify enable Toggle setting from Account's integration tab to Connect to Gmail
	@Test(groups = { "Regression" })
	public void delete_gmail_and_exchnage_verify_on_integration_tab() {
		System.out.println("Test case --delete_gmail_and_exchnage_verify_on_integration_tab-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		//get user name
		String username = calendar.getUserName(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		//is connection visible
		assertTrue(calendar.isGmailIntegrationConnected(webSupportDriver));
		assertTrue(calendar.isExchangeIntegrationConnected(webSupportDriver));
		
		//open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//disable gmail and exchange accounts
		accountsTab.disableGmailAccount(webSupportDriver);
		accountsTab.disableExchangeAccount(webSupportDriver);
		
		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		
		//is connection visible
		assertFalse(calendar.isGmailIntegrationVisible(webSupportDriver));
		assertFalse(calendar.isExchangeIntegrationVisible(webSupportDriver));
		
		// check request for connection 
		assertTrue(calendar.isGamilRequestConnectionVisible(webSupportDriver));
		assertTrue(calendar.isExchangeRequestConnectionVisible(webSupportDriver));
		
		//open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
				
		//enable gmail and exchange accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		
		dashboard.clickOnUserProfile(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		
		// navigating to user account
		dashboard.clickAccountsLink(supportDriver);

		// navigating to accounts tab
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//click on user count
		accountsTab.clickOnUserCount(webSupportDriver);
		
		//select user type
		accountsTab.selectUserFromDropDown(webSupportDriver, UserTypeEnum.Gmail);
				
		//remove account from account tab
		accountsTab.removeAllAccountFromAccountsTab(webSupportDriver, username);
		
		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// is connection visible
		assertFalse(calendar.isGmailIntegrationVisible(webSupportDriver));
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Delete the Exchange account Connection from ringdna user by Account integration tab
	@Test(groups = { "Regression" })
	public void delete_exchnage_verify_on_integration_tab() {
		System.out.println("Test case --delete_exchnage_verify_on_integration_tab-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		//get user name
		String username = calendar.getUserName(webSupportDriver);

		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);

		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);

		// navigating to accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// click on user count
		accountsTab.clickOnUserCount(webSupportDriver);

		// select user type
		accountsTab.selectUserFromDropDown(webSupportDriver, UserTypeEnum.Exchange);

		// remove account from account tab
		accountsTab.removeAllAccountFromAccountsTab(webSupportDriver, username);

		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// is connection visible
		assertFalse(calendar.isExchangeIntegrationVisible(webSupportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify user friendly error message when entered Invalid credentials
	//Ringdna user Manage Exchange account, verify Account details
	//@Test(groups = { "Regression" })
	public void verify_invalid_creds_and_manage_exchange_account() {
		System.out.println("Test case --verify_invalid_creds_and_manage_exchange_account-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		calendar.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		
		// open integration tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// navigating to account
		dashboard.clickAccountsLink(supportDriver);

		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//click on manage button
		accountsTab.clickOnUserCount(webSupportDriver);
		
		int actual = accountsTab.getAccountNameListSize(webSupportDriver);
		
		//select user type
		accountsTab.selectUserFromDropDown(webSupportDriver, UserTypeEnum.Exchange);
		
		//get user list
		int users = accountsTab.getExchnageUserListSize(webSupportDriver);
		assertTrue(actual > users);
		
		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		
		// remove integration if present
		calendar.removeExchangeIntegration(webSupportDriver);
		
		//add invalid int
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, HelperFunctions.GetRandomString(6));
		assertTrue(calendar.verifyInvalidExchnageError(webSupportDriver));
		
		webSupportDriver.navigate().back();
		webSupportDriver.navigate().back();
		webSupportDriver.navigate().back();
		
		// add gmail integration
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_invalid_creds_and_manage_exchange_account-- passed");
	}
	
	//Verify Request Integration Email, switch to integration tab after click on integrated account button
	//Ringdna user Request Integration for Connect to GMAIL when Integration Disabled
	//Disable the Google account toggle setting, verify the Email
	//@Test(groups = { "Regression" })
	public void request_for_gmail_int_and_connect_with_gmail() {
		System.out.println("Test case --request_for_gmail_int_and_connect_with_gmail-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeExchangeIntegration(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.disableGmailAccount(webSupportDriver);

		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// is connection visible
		assertFalse(calendar.isGmailIntegrationConnected(webSupportDriver));

		// check request for connection
		assertTrue(calendar.isGamilRequestConnectionVisible(webSupportDriver));
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//verify disconnected message
		gmail.verifyGmailAccountDisconnectedMail(webSupportDriver);
		gmail.deleteOpenedMail(webSupportDriver);
		
		//click gmail request button
		dashboard.switchToTab(webSupportDriver, 2);
		calendar.clickGamilRequestConnection(webSupportDriver);
		
		//open gmail
		dashboard.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//click integrated accounts
		gmail.clickIntegratedAccountsButton(webSupportDriver);

		// enable gmail and exchange accounts
		dashboard.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		accountsTab.enableGmailAccount(webSupportDriver);

		// add gmail integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.closeTab(webSupportDriver);

		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --request_for_gmail_int_and_connect_with_gmail-- passed");
	}
	
	//Ringdna user Request Integration for Connect to Exchange account when Integration Disabled
	//Verify Request Integration Email, switch to integration tab after click on integrated account button
	//Disable the Exchange account toggle setting,verify email
	//@Test(groups = { "Regression" })
	public void request_for_exchange_int_and_connect_with_exchange() {
		System.out.println("Test case --request_for_exchange_int_and_connect_with_exchange-- started");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable gmail accounts
		accountsTab.enableGmailAccount(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeGmailIntegration(webSupportDriver);
		calendar.addExchangeIntegration(webSupportDriver, exchangeEmail, exchangePassword);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// open accounts tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// disable exchange accounts
		accountsTab.disableExchangeAccount(webSupportDriver);

		// open integration and calendar tab
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);

		// is connection visible
		assertFalse(calendar.isExchangeIntegrationConnected(webSupportDriver));

		// check request for connection
		assertTrue(calendar.isExchangeRequestConnectionVisible(webSupportDriver));
		
		//open gmail
		gmail.openGmailInNewTab(webSupportDriver);
		gmail.openNewMailInGmail(webSupportDriver, 1);
				
		//verify disconnected message
		gmail.verifyExchangeAccountDisconnectedMail(webSupportDriver);	
		gmail.deleteOpenedMail(webSupportDriver);
				
		//click exchange request button
		dashboard.switchToTab(webSupportDriver, 2);
		calendar.clickExchangeRequestConnection(webSupportDriver);
		
		//open gmail
		dashboard.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		gmail.openNewMailInGmail(webSupportDriver, 1);
		
		//click integrated accounts
		gmail.clickIntegratedAccountsButton(webSupportDriver);

		// enable gmail and exchange accounts
		dashboard.switchToTab(webSupportDriver, calendar.getTabCount(webSupportDriver));
		accountsTab.enableExchangeAccount(webSupportDriver);

		// add gmail integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		calendar.closeTab(webSupportDriver);

		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --request_for_exchange_int_and_connect_with_exchange-- passed");
	}
	

}
