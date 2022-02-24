package support.cases.accountCases;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountLicenseTab;
import support.source.accounts.AccountLicenseTab.LicenseReason;
import support.source.accounts.AccountLicenseTab.LicenseType;
import support.source.accounts.AccountYodaAITab;
import support.source.accounts.AccountsPage;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class LicenseTabCases extends SupportBase {

	AccountsPage accountsPage = new AccountsPage();
	Dashboard dashboard = new Dashboard();
	AccountLicenseTab licensePage = new AccountLicenseTab();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	SmartNumbersPage smartPage = new SmartNumbersPage();
	AccountYodaAITab yodaTab = new AccountYodaAITab();
	
	//Verify support user able to Update Expiration Date of an account
	@Test(groups = { "Regression", "SupportOnly"})
	public void update_expiration_date() {
		System.out.println("Test case --update_expiration_date-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		
		// navigating to license tab and editing license
		licensePage.openLicensingTab(supportDriver);
		String oldExpirationDate = licensePage.getExpirationDateText(supportDriver).split(" ")[0];
		String newExpirationDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyy", oldExpirationDate, 4, 2, 2);
		licensePage.editLicense(supportDriver, newExpirationDate);
		licensePage.idleWait(3);

		//verifying dates changes
		String actualExpirationDate = licensePage.getExpirationDateText(supportDriver).split(" ")[0];
		String expectedExpirationDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyy", actualExpirationDate, -1, 0, 0);
		assertEquals(newExpirationDate, expectedExpirationDate);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --update_expiration_date-- passed");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void add_license_smart_number_type() {
		System.out.println("Test case --add_license_smart_number_type-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);
		
		//Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		String type = "Smart Numbers";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);
		
		String licenseAmount = "1";
		String licenseNotes = "AutoLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxSmartNumbers.name(), licenseAmount, LicenseReason.SalesRequested.name(), licenseNotes);
		
		//verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);
		
		//verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, type, licenseAmount, userName, LicenseReason.SalesRequested.name());
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_smart_number_type-- passed");
	}
	
	@Test(groups = { "Regression" })
	public void verify_license_request() {
		System.out.println("Test case --verify_license_request-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		String additionalDetails = "AutoAdditionalDetails".concat(HelperFunctions.GetRandomString(3));
		
		//adding and verifying request message
		String smartNumbers = "1";
		String adminUsers = "1";
		
		licensePage.addRequestBtn(supportDriver);
		licensePage.addLicenseRequest(supportDriver, additionalDetails, smartNumbers, adminUsers, null, null, null, null, null);
		licensePage.verifyRequestMsg(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_license_request-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_smart_numbers_tab_from_licensing_tab() {
		System.out.println("Test case --verify_smart_numbers_tab_from_licensing_tab-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);
		
		//click and verify smart numbers link
		licensePage.clickSmartNumbersLink(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_smart_numbers_tab_from_licensing_tab-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_users_tab_from_licensing_tab() {
		System.out.println("Test case --verify_users_tab_from_licensing_tab-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);
		
		//click admin users link
		licensePage.clickAdminUsersLink(supportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(supportDriver);
		assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_user_account"), "Account name not matching on users page");
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_users_tab_from_licensing_tab-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void add_license_admin_user_type() {
		System.out.println("Test case --add_license_admin_users_type-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		String type = "Admin Users";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxAdmins.toString(), licenseAmount,
				LicenseReason.CustomerService.toString(), licenseNotes);

		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, LicenseType.MaxAdmins.toString(), licenseAmount, userName,
				LicenseReason.CustomerService.toString());

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_admin_user_type-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void add_license_agent_user_type() {
		System.out.println("Test case --add_license_agent_user_type-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		String type = "Agent Users";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxAgents.toString(), licenseAmount,
				LicenseReason.CustomerService.toString(), licenseNotes);

		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, LicenseType.MaxAgents.toString(), licenseAmount, userName,
				LicenseReason.CustomerService.toString());

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_agent_user_type-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void add_license_email_calendar_user_type() {
		System.out.println("Test case --add_license_email_calendar_user_type-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		String type = "Email / Calendar Users";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoEmailLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxNylasUsers.toString(), licenseAmount,
				LicenseReason.TechnicalIssues.toString(), licenseNotes);

		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, type, licenseAmount, userName,
				LicenseReason.TechnicalIssues.toString());

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_email_calendar_user_type-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_license_request_for_email_calendar_users() {
		System.out.println("Test case --verify_license_request_for_email_calendar_users-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		String additionalDetails = "AutoAdditionalDetails".concat(HelperFunctions.GetRandomString(3));
		
		//adding and verifying request message
		String email_CalendarUsers = "1";
		
		licensePage.addRequestBtn(supportDriver);
		licensePage.addLicenseRequest(supportDriver, additionalDetails, null, null, null, email_CalendarUsers, null, null, null);
		licensePage.verifyRequestMsg(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_license_request_for_email_calendar_users-- passed");
	}
	
	//Verify "Sequence" renamed to "Guided Selling"
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void add_license_sequence_user_type() {
		System.out.println("Test case --add_license_sequence_user_type-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);
		
		//is sequence user visible
		assertFalse(licensePage.isSequenceUsersLinkVisible(supportDriver));
		
		//verify sequence user available
		licensePage.addLicenseBtn(supportDriver);
		List<String> licenceTypes = licensePage.getLicenseTypeListfromDropDown(supportDriver);
		//assert false
		assertFalse(licenceTypes.contains("Sequence Users"));
		licensePage.closeAddLicenseForm(supportDriver);
		
		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_support_user_name");
		String type = "Guided Selling Users";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoSequenceLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxSequenceUsers.toString(), licenseAmount, LicenseReason.CustomerService.toString(), licenseNotes);
		
		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, type, licenseAmount, userName, LicenseReason.CustomerService.toString());

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_sequence_user_type-- passed");
	}
	
	//Verify clicking on Agent Users license type should redirected user to all available active Agent Users in account
	@Test(groups = { "Regression"})
	public void verify_agent_users_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_agent_users_on_licensing_tab_and_users-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);
		
		int assignAgentUser = licensePage.getAssignedLicenseCount(supportDriver, "Agent Users");
		
		//click agent users link
		licensePage.clickAgentUsersLink(supportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(supportDriver);
		if (SupportBase.drivername.equals("supportDriver")) {
			assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_user_account"), "Account name not matching on users page");
		}		
		//verifying assigned users
		int actualAssignedUser = usersPage.getAllUserAccountLink(supportDriver).size();
		assertEquals(actualAssignedUser, assignAgentUser);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_agent_users_on_licensing_tab_and_users-- passed");
	}
	
	
	//Verify clicking on Admin Users license type should redirected user to all available active Admin Users in account
	@Test(groups = { "Regression"})
	public void verify_admin_users_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_admin_users_on_licensing_tab_and_users-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);
		
		int assignAdminUser = licensePage.getAssignedLicenseCount(supportDriver, "Admin Users");
		
		//click admin users link
		licensePage.clickAdminUsersLink(supportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(supportDriver);
		if (SupportBase.drivername.equals("supportDriver")) {
			assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_user_account"), "Account name not matching on users page");
		}		
		//verifying assigned users
		int actualAssignedUser = usersPage.getAllUserAccountLink(supportDriver).size();
		assertEquals(actualAssignedUser, assignAdminUser);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_admin_users_on_licensing_tab_and_users-- passed");
	}
	
	
	//Verify clicking on Smart Numbers license type should redirected user to all available active Smart Numbers in account
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_smart_numbers_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_smart_numbers_on_licensing_tab_and_users-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);
		
		int assignSmartNumber = licensePage.getAssignedLicenseCount(supportDriver, "Smart Numbers");
		
		//click smart number link
		licensePage.clickSmartNumbersLink(supportDriver);
		
		//verifying account name on users page
		assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_free_user_account"),"Account name not matching on users page");	
		
		//verifying assigned users
		smartPage.clickStatusHeader(supportDriver);
		
		List<String> actualAssignedUser = (smartPage.getStatusList(supportDriver));
		int count = 0;
		for (String status : actualAssignedUser) 
		{ 
		    if (status.equals("Active")) 
		    {
		        count++; 
		    }
		}
		
		//assert
		assertEquals(assignSmartNumber, count);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_smart_numbers_on_licensing_tab_and_users-- passed");
	}
	
	
	//Verify clicking on Sequence Users license type should redirected user to all available active Sequence Users in account
	@Test(groups = { "Regression"})
	public void verify_sequence_user_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_sequence_user_on_licensing_tab_and_users-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);
		
		int assignSmartNumber = licensePage.getAssignedLicenseCount(supportDriver, "Sequence Users");
		
		//click sequence users link
		licensePage.clickSequenceUsersLink(supportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(supportDriver);
		
		if (SupportBase.drivername.equals("supportDriver")) {
		assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_user_account"), "Account name not matching on users page");
		}
		
		//verifying assigned users
		if (assignSmartNumber > 0) {
			int actualAssignedUser = usersPage.getAllUserAccountLink(supportDriver).size();
			assertEquals(actualAssignedUser, assignSmartNumber);
		}
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_sequence_user_on_licensing_tab_and_users-- passed");
	}
	
	//Add License for Conversation AI Users type, verify log in License Line Items
	@Test(groups = { "Regression"})
	public void verify_conversationAI_user_on_licensing_tab_and_users() {
		System.out.println("Test case --verify_conversationAI_user_on_licensing_tab_and_users-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);
		
		int assignSmartNumber = licensePage.getAssignedLicenseCount(supportDriver, "Conversation AI Users");
		
		//click conversationAI users link
		licensePage.clickConversationAIUsersLink(supportDriver);
		
		//verifying page title and account name on users page
		usersPage.verifyManageUsersPageTitle(supportDriver);
		if (SupportBase.drivername.equals("supportDriver")) {
			assertEquals(usersPage.getAccountNameText(supportDriver), CONFIG.getProperty("qa_user_account"), "Account name not matching on users page");
		}
		//verifying assigned users
		int actualAssignedUser = usersPage.getAllUserAccountLink(supportDriver).size();
		assertEquals(actualAssignedUser, assignSmartNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_conversationAI_user_on_licensing_tab_and_users-- passed");
	}
	
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void add_license_conversation_ai_user_type() {
		System.out.println("Test case --add_license_conversation_ai_user_type-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		// Get Allowed and Remaining count
		String userName = CONFIG.getProperty("qa_free_user_name");
		String type = "Conversation AI Users";
		int allowedCount = licensePage.getAllowedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);

		String licenseAmount = "1";
		String licenseNotes = "AutoSequenceLicenseNotes".concat(HelperFunctions.GetRandomString(3));
		licensePage.addLicense(supportDriver, LicenseType.MaxConversationAiUsers.toString(), licenseAmount,
				LicenseReason.CustomerService.toString(), licenseNotes);

		// verifying allowed and remaining count is increased
		licensePage.verifyAllowedCountIncreased(supportDriver, type, allowedCount);
		licensePage.verifyRemainingCountIncreased(supportDriver, type, remainingCount);

		// verifying license line items
		licensePage.verifyLicenseLineItemSections(supportDriver, licenseNotes, type, licenseAmount, userName,
				LicenseReason.CustomerService.toString());

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_license_conversation_ai_user_type-- passed");
	}
	
	// Enable CAI for users when you have CAI users license available , verify remaining license now
	@Test(groups = {"Regression", "SupportOnly" })
	public void enable_cai_for_user_and_verify_count_of_cai_license() {
		System.out.println("Test case --enable_cai_for_user_and_verify_count_of_cai_license-- started");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open profile in new tab
		dashboard.openUserProfileInNewTab(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.disableConversationAnalyticsBtn(supportDriver);
		usersPage.savedetails(supportDriver);

		dashboard.switchToTab(supportDriver, 2);

		// navigating to license tab
		licensePage.openLicensingTab(supportDriver);

		// Get Allowed and Remaining count
		supportDriver.navigate().refresh();
		String type = "Conversation AI Users";
		int assignedCount = licensePage.getAssignedLicenseCount(supportDriver, type);
		int remainingCount = licensePage.getRemainingLicenseCount(supportDriver, type);
		
		//open profile in new tab
		dashboard.switchToTab(supportDriver, 3);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.enableConversationAnalyticsBtn(supportDriver);
		usersPage.savedetails(supportDriver);
		usersPage.closeTab(supportDriver);
		dashboard.switchToTab(supportDriver, 2);

		// verifying allowed and remaining count is increased
		supportDriver.navigate().refresh();
		licensePage.verifyAssignedCountIncreased(supportDriver, type, assignedCount);
		licensePage.verifyRemainingCountDecreased(supportDriver, type, remainingCount);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --enable_cai_for_user_and_verify_count_of_cai_license-- passed");
	}
	
	//Add License Request for Conversation AI users
	@Test(groups = { "Regression" })
	public void verify_license_request_of_conversation_ai_users() {
		System.out.println("Test case --verify_license_request_of_conversation_ai_users-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		licensePage.openLicensingTab(supportDriver);

		String additionalDetails = "AutoAdditionalDetails".concat(HelperFunctions.GetRandomString(3));
		
		//adding and verifying request message
		String caiUsers = "1";
		
		licensePage.addRequestBtn(supportDriver);
		licensePage.addLicenseRequest(supportDriver, additionalDetails, null, null, null, null, caiUsers, null, null);
		licensePage.verifyRequestMsg(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_license_request_of_conversation_ai_users-- passed");
	}
	
	//Verify Support able to load multiple tabs on the Manage User page
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_multiple_click_on_agent_users_link_in_licensing_tab() {
		System.out.println("Test case --verify_multiple_click_on_agent_users_link_in_licensing_tab-- started");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// navigating to free user account and clicking license tab
		dashboard.clickAccountsLink(supportDriver);
		licensePage.openLicensingTab(supportDriver);

		// click agent users link
		licensePage.clickAgentUsersLinkInNewTab(supportDriver);
		licensePage.clickAgentUsersLinkInNewTab(supportDriver);
		licensePage.clickAgentUsersLinkInNewTab(supportDriver);
		
		//verifying page title and account name on users page
		dashboard.switchToTab(supportDriver, dashboard.getTabCount(supportDriver));
		usersPage.verifyManageUsersPageTitle(supportDriver);
		
		int tabCount = dashboard.getTabCount(supportDriver);
		assertEquals(tabCount, 5);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);

		System.out.println("Test case --verify_multiple_click_on_agent_users_link_in_licensing_tab-- passed");
	}
}
