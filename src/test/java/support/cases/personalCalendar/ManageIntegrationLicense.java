package support.cases.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.accounts.AccountLicenseTab;
import support.source.accounts.AccountLicenseTab.LicenseReason;
import support.source.accounts.AccountLicenseTab.LicenseType;
import support.source.commonpages.Dashboard;
import support.source.personalCalendar.PersonalCalendarPage;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class ManageIntegrationLicense extends SupportBase {

	Dashboard dashboard = new Dashboard();
	PersonalCalendarPage calendar = new PersonalCalendarPage();
	AccountLicenseTab licensePage = new AccountLicenseTab();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	UsersPage usersPage = new UsersPage();
	
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
	
	//Click on banner switch user to License tab to request and provision new licenses
	//Verify banner is displayed with the number of licenses available on account's integration
	//Add a new email/calendar user license
	//Verify License Line Items with any new license being provisioned by Support
	@Test(groups = { "Regression" })
	public void verify_managing_integration_license_request() {
		System.out.println("Test case --verify_managing_integration_license_request-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// navigating to free user account
		dashboard.clickAccountsLink(webSupportDriver);
		
		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		
		//get license available Count
		int count = accountsTab.getLicenseAvailableCount(webSupportDriver);
		
		//click on License available
		accountsTab.clickOnLicenseAvailable(webSupportDriver);
		
		// get remaining count
		String type = "Email / Calendar Users";
		int remainingCount = licensePage.getRemainingLicenseCount(webSupportDriver, type);
		
		//assert
		assertEquals(count, remainingCount);
		
		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		int allowedCount = licensePage.getAllowedLicenseCount(webSupportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(webSupportDriver, LicenseType.MaxNylasUsers.toString(), licenseAmount,
				LicenseReason.CustomerService.toString(), licenseNotes);
		
		// navigating to user account
		dashboard.clickAccountsLink(webSupportDriver);
		licensePage.openLicensingTab(webSupportDriver);

		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(webSupportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(webSupportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(webSupportDriver, licenseNotes, "Email / Calendar Users",
				licenseAmount, userName, LicenseReason.CustomerService.toString());
		
		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_managing_integration_license_request-- passed");
	}
	
	//Requesting New Calendar / Email Licenses
	@Test(groups = { "Regression" })
	public void verify_license_request_for_email_calendar_users() {
		System.out.println("Test case --verify_license_request_for_email_calendar_users-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(webSupportDriver, CONFIG.getProperty("qa_free_user_account"),
				CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(webSupportDriver);

		String additionalDetails = "AutoAdditionalDetails".concat(HelperFunctions.GetRandomString(3));
		
		//adding and verifying request message
		String email_CalendarUsers = "1";
		
		licensePage.addRequestBtn(webSupportDriver);
		licensePage.addLicenseRequest(webSupportDriver, additionalDetails, null, null, null, email_CalendarUsers, null, null, null);
		licensePage.verifyRequestMsg(webSupportDriver);
		
		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_license_request_for_email_calendar_users-- passed");
	}
	
	//Verify click on Calendar Users license link will redirect user to show the list of connected users with Calendar account
	@Test(groups = { "Regression"})
	public void verify_email_calendar_users_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_email_calendar_users_on_licensing_tab_and_users-- started");
	
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(webSupportDriver);
		accountsTab.openAccountIntegrationsTab(webSupportDriver);
		accountsTab.enableExchangeAccount(webSupportDriver);
		accountsTab.enableGmailAccount(webSupportDriver);
		
		licensePage.openLicensingTab(webSupportDriver);
		int assignAgentUser = licensePage.getAssignedLicenseCount(webSupportDriver, "Email / Calendar Users");
		
		//click email/cal users link
		licensePage.clickEmailCalendarUsersLink(webSupportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(webSupportDriver);

		//verifying assigned users
		int actualAssignedUser = usersPage.getAllUserAccountLink(webSupportDriver).size();
		assertEquals(actualAssignedUser, assignAgentUser);
		
		//verify account name on list
		List<String> accountName = usersPage.getAllAccountNameFromList(webSupportDriver);
		for(String name : accountName) {
			assertTrue(name.equals(CONFIG.getProperty("qa_user_account")));
		}
		
		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_email_calendar_users_on_licensing_tab_and_users-- passed");
	}
	
	//Verify Available licenses on banner upon connect and Remove the account
	//Viewing Calendar / Email Licenses
	@Test(groups = { "Regression" })
	public void add_integration_and_verify_email_calendar_users_on_licensing_tab() {
		System.out.println("Test case --add_integration_and_verify_email_calendar_users_on_licensing_tab-- started");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);

		//remove Integration
		calendar.openIntegrationTab(webSupportDriver);
		calendar.removeIntegration(webSupportDriver);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(webSupportDriver);
		licensePage.openLicensingTab(webSupportDriver);

		int assignAgentUser = licensePage.getAssignedLicenseCount(webSupportDriver, "Email / Calendar Users");
		int remainingUser = licensePage.getRemainingLicenseCount(webSupportDriver, "Email / Calendar Users");

		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// get license available Count
		int count = accountsTab.getLicenseAvailableCount(webSupportDriver);

		// assert
		assertEquals(count, remainingUser);
		
		//add integration
		dashboard.clickOnUserProfile(webSupportDriver);
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(webSupportDriver);
		licensePage.openLicensingTab(webSupportDriver);

		int assignAgentUser2 = licensePage.getAssignedLicenseCount(webSupportDriver, "Email / Calendar Users");
		int remainingUser2 = licensePage.getRemainingLicenseCount(webSupportDriver, "Email / Calendar Users");

		// navigating to license tab and editing license
		accountsTab.openAccountIntegrationsTab(webSupportDriver);

		// get license available Count
		int count2 = accountsTab.getLicenseAvailableCount(webSupportDriver);
		
		//assert
		assertEquals((count -1), count2);
		assertEquals((remainingUser - 1), remainingUser2);
		assertEquals((assignAgentUser + 1), assignAgentUser2);
		
		dashboard.clickOnUserProfile(webSupportDriver);
		// open integration tab
		calendar.openIntegrationTab(webSupportDriver);
		calendar.addGmailIntegration(webSupportDriver, gmailEmail, gmailPassword);

		// updating the supportDriver used
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --add_integration_and_verify_email_calendar_users_on_licensing_tab-- passed");
	}

}
