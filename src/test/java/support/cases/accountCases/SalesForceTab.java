package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.Test;

import base.TestBase;
import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSalesforceTab.SalesForceFields;
import support.source.commonpages.Dashboard;
import support.source.profilePage.ProfilePage;
import utility.HelperFunctions;

public class SalesForceTab extends SupportBase{

	Dashboard dashboard = new Dashboard();
	AccountSalesforceTab salesForceTab = new AccountSalesforceTab();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallScreenPage callScreenPage = new CallScreenPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	SoftphoneBase softPhoneBase = new SoftphoneBase();
	SoftPhoneContactsPage softphoneContactPage = new SoftPhoneContactsPage();
	SoftPhoneSettingsPage softPhoneSettingPage = new SoftPhoneSettingsPage();
	SoftphoneCallHistoryPage softphoneCallHistory = new SoftphoneCallHistoryPage();
	ProfilePage profile = new ProfilePage();
	Random random = new Random();
	AccountIntelligentDialerTab intelligentDialerTab = new AccountIntelligentDialerTab();
	
	@Test(groups = { "Regression" })
	public void add_new_default_contact_lead() {
		// updating the supportDriver used
		System.out.println("Test case --add_new_default_contact_lead-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);		
		//Adding Lead Default
		intelligentDialerTab.selectNewDefaultContact(supportDriver, AccountSalesforceTab.defaultContacts.Lead.name());
		
		//softphone verification for Lead Default
		salesForceTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softPhoneSettingPage.clickSettingIcon(supportDriver);
		softPhoneSettingPage.disableCallForwardingSettings(supportDriver);
		softPhoneBase.reloadSoftphone(supportDriver);
		softPhoneCalling.softphoneAgentCall(supportDriver, HelperFunctions.generateTenDigitNumber());
		callToolsPanel.callNotesSectionVisible(supportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		callScreenPage.clickAddNewButton(supportDriver);
		boolean contactSelected = callScreenPage.isNewDefaultSelected(supportDriver,  AccountSalesforceTab.defaultContacts.Lead.name());
		assertTrue(contactSelected, String.format("Default contact:%s not selected on softphone", AccountSalesforceTab.defaultContacts.Lead.name()));
		salesForceTab.switchToTab(supportDriver, 2);
		
		//Adding Contact Default
		intelligentDialerTab.selectNewDefaultContact(supportDriver, AccountSalesforceTab.defaultContacts.Contact.name());
		intelligentDialerTab.switchToTab(supportDriver, 1);
		
		//softphone verification for Contact Default
		softPhoneBase.reloadSoftphone(supportDriver);
		softPhoneCalling.softphoneAgentCall(supportDriver, HelperFunctions.generateTenDigitNumber());
		callToolsPanel.callNotesSectionVisible(supportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		callScreenPage.clickAddNewButton(supportDriver);
		contactSelected = callScreenPage.isNewDefaultSelected(supportDriver,  AccountSalesforceTab.defaultContacts.Contact.name());
		assertTrue(contactSelected, String.format("Default contact:%s not selected on softphone", AccountSalesforceTab.defaultContacts.Contact.name()));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_new_default_contact_lead-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void add_update_delete_additional_lead_fields_operations() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_additional_lead_fields_operations-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning additional fields section of leads section
		salesForceTab.navigateToLeadFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoLeadField");
		
		//Creating additional lead field
		String leadFieldName = "AutoLeadField".concat(HelperFunctions.GetRandomString(3));
		String updatedLeadFieldName = "AutoLeadFieldUpdate".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, leadFieldName, true, false, false);
		
		//verifying lead field name created
		boolean leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, leadFieldName);
		assertTrue(leadFieldNameExists, String.format("Additional lead field with label name:%s not created", leadFieldName));
		
		//verifying salesforce field field name created
		boolean salesForceFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, SalesForceFields.CreatedByEmail.toString());
		assertTrue(salesForceFieldExists, String.format("Additional lead field with sales force field:%s not created", SalesForceFields.CreatedByEmail.toString()));
		
		//updating additional lead
		salesForceTab.updateAdditionalField(supportDriver, leadFieldName, updatedLeadFieldName);
		
		//verifying updated fields
		leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedLeadFieldName);
		assertTrue(leadFieldNameExists, String.format("Additional lead field with labal name:%s not updated", updatedLeadFieldName));

		// softphone verification for Lead Default
		salesForceTab.switchToTab(supportDriver, 1);
		softphoneContactPage.searchSalesForce(supportDriver, CONFIG.getProperty("test_contact"));
		softphoneContactPage.clickFirstLeadContactsMsgIcon(supportDriver);

		boolean leadNameVisible = callScreenPage.isSalesForceAdditionalNamePresent(supportDriver, updatedLeadFieldName);
		assertTrue(leadNameVisible, "Additional lead name not visible:" + updatedLeadFieldName);
		
		//deleting additional lead
		salesForceTab.switchToTab(supportDriver, 2);
		salesForceTab.deleteAdditionalField(supportDriver, updatedLeadFieldName);
		leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedLeadFieldName);
		assertFalse(leadFieldNameExists, String.format("Additional lead field with labal name:%s not deleted", updatedLeadFieldName));
		
		// verifying deleted field on softphone
		salesForceTab.switchToTab(supportDriver, 1);
		softphoneContactPage.searchSalesForce(supportDriver, CONFIG.getProperty("test_contact"));
		softphoneContactPage.clickFirstLeadContactsMsgIcon(supportDriver);
		
		leadNameVisible = callScreenPage.isSalesForceAdditionalNamePresent(supportDriver, updatedLeadFieldName);
		assertFalse(leadNameVisible, "Additional lead name visible: even after deleting" + updatedLeadFieldName);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_additional_lead_fields_operations-- passed ");
	}
	
	//After clicking on save button Field should save to particular profile and verify the Softphone
	//Verify softphone readonly field should read only mode
	//<Field> readonly for custom profile it should be read only on webapp
	@Test(groups = {"Regression"})
	public void add_update_delete_additional_contact_fields_operations() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_additional_contact_fields_operations-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning additional fields section of contact section
		salesForceTab.navigateToContactFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoContactField");
		
		//Creating additional contact field
		String contactFieldName = "AutoContactField".concat(HelperFunctions.GetRandomString(3));
		String updatedContactFieldName = "AutoContactFieldUpdate".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, contactFieldName, true, false, false);
		
		// verifying contact field field name created
		boolean contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, contactFieldName);
		assertTrue(contactFieldExists, String.format("Additional contact field with label name:%s not created", contactFieldName));

		// verifying salesforce field field name created
		boolean salesForceFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, SalesForceFields.CreatedByEmail.toString());
		assertTrue(salesForceFieldExists, String.format("Additional lead field with sales force field:%s not created", SalesForceFields.CreatedByEmail.toString()));

		// updating additional contact
		salesForceTab.updateAdditionalField(supportDriver, contactFieldName, updatedContactFieldName);
		
		// verifying contact field field name created
		contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedContactFieldName);
		assertTrue(contactFieldExists, String.format("Additional lead field with labal name:%s not updated", updatedContactFieldName));

		// softphone verification for Contact
		salesForceTab.switchToTab(supportDriver, 1);
		softphoneContactPage.searchSalesForce(supportDriver, CONFIG.getProperty("test_contact"));
		softphoneContactPage.clickFirstContactsMsgIcon(supportDriver);

		boolean contactVisible = callScreenPage.isSalesForceAdditionalNamePresent(supportDriver, updatedContactFieldName);
		assertTrue(contactVisible, "Additional contact not visible:"+updatedContactFieldName);		
		
		// deleting additional contact
		salesForceTab.switchToTab(supportDriver, 2);
		salesForceTab.deleteAdditionalField(supportDriver, updatedContactFieldName);
		contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedContactFieldName);
		assertFalse(contactFieldExists, String.format("Additional lead field with labal name:%s not deleted", updatedContactFieldName));

		// verifying deleted field on softphone
		salesForceTab.switchToTab(supportDriver, 1);
		softPhoneCalling.reloadSoftphone(supportDriver);

		softphoneContactPage.searchSalesForce(supportDriver, CONFIG.getProperty("test_contact"));
		softphoneContactPage.clickFirstContactsMsgIcon(supportDriver);
		
		contactVisible = callScreenPage.isSalesForceAdditionalNamePresent(supportDriver, updatedContactFieldName);
		assertFalse(contactVisible, "Additional contact name visible: even after deleting" + updatedContactFieldName);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_additional_contact_fields_operations-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void add_update_delete_additional_lead_fields_operations_for_other_accounts() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_additional_lead_fields_operations_for_other_accounts-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_user_account_cai_report"), CONFIG.getProperty("qa_user_account_qav2_salesforce_id"));
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning additional fields section of leads section
		salesForceTab.navigateToLeadFieldsSection(supportDriver);
		
		if(salesForceTab.isFieldsAddBtnDisabled(supportDriver)){
			salesForceTab.deleteFieldsAccToIndex(supportDriver, 0);
		}
		
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		//Creating additional lead field
		String leadFieldName = "AutoLeadField".concat(HelperFunctions.GetRandomString(3));
		String updatedLeadFieldName = "AutoLeadFieldUpdate".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, leadFieldName, true, false, false);
		
		//verifying lead field name created
		boolean leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, leadFieldName);
		assertTrue(leadFieldNameExists, String.format("Additional lead field with label name:%s not created", leadFieldName));
		
		//verifying salesforce field field name created
		boolean salesForceFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, SalesForceFields.CreatedByEmail.toString());
		assertTrue(salesForceFieldExists, String.format("Additional lead field with sales force field:%s not created", SalesForceFields.CreatedByEmail.toString()));
		
		//updating additional lead
		salesForceTab.updateAdditionalField(supportDriver, leadFieldName, updatedLeadFieldName);
		
		//verifying updated fields
		leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedLeadFieldName);
		assertTrue(leadFieldNameExists, String.format("Additional lead field with labal name:%s not updated", updatedLeadFieldName));

		//deleting additional lead
		salesForceTab.switchToTab(supportDriver, 2);
		salesForceTab.deleteAdditionalField(supportDriver, updatedLeadFieldName);
		leadFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedLeadFieldName);
		assertFalse(leadFieldNameExists, String.format("Additional lead field with labal name:%s not deleted", updatedLeadFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_additional_lead_fields_operations_for_other_accounts-- passed ");
	}
	
	@Test(groups = {"SupportOnly", "MediumPriority"})
	public void add_update_delete_additional_contact_fields_operations_for_other_accounts() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_additional_contact_fields_operations_for_other_accounts-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab of qav2 account
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_user_account_cai_report"), CONFIG.getProperty("qa_user_account_qav2_salesforce_id"));
		salesForceTab.openSalesforceTab(supportDriver);
		
		// Navigate to contacts field section
		salesForceTab.navigateToContactFieldsSection(supportDriver);
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		//Creating additional contact field
		String contactFieldName = "AutoContactField".concat(HelperFunctions.GetRandomString(3));
		String updatedContactFieldName = "AutoContactFieldUpdate".concat(HelperFunctions.GetRandomString(3));
		
		if(salesForceTab.isFieldsAddBtnDisabled(supportDriver)){
			salesForceTab.deleteFieldsAccToIndex(supportDriver, 0);
		}
		
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, contactFieldName, true, false, false);
		
		// verifying contact field field name created
		boolean contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, contactFieldName);
		assertTrue(contactFieldExists, String.format("Additional contact field with label name:%s not created", contactFieldName));

		// verifying salesforce field field name created
		boolean salesForceFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, SalesForceFields.CreatedByEmail.toString());
		assertTrue(salesForceFieldExists, String.format("Additional lead field with sales force field:%s not created", SalesForceFields.CreatedByEmail.toString()));

		// updating additional contact
		salesForceTab.updateAdditionalField(supportDriver, contactFieldName, updatedContactFieldName);
		
		// verifying contact field field name created
		contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedContactFieldName);
		assertTrue(contactFieldExists, String.format("Additional lead field with labal name:%s not updated", updatedContactFieldName));

		// deleting additional contact
		salesForceTab.deleteAdditionalField(supportDriver, updatedContactFieldName);
		contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, updatedContactFieldName);
		assertFalse(contactFieldExists, String.format("Additional lead field with labal name:%s not deleted", updatedContactFieldName));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_additional_contact_fields_operations_for_other_accounts-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_salesforce_package_check() {
		System.out.println("Test case --verify_salesforce_package_check-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//click and verify package check
		intelligentDialerTab.clickPackageRun(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_package_check-- passed");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_salesforce_clear_cache() {
		System.out.println("Test case --verify_salesforce_clear_cache-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//click and verify clear btn cache
		intelligentDialerTab.clickClearBtnCache(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_clear_cache-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_salesforce_connect_user() {
		System.out.println("Test case --verify_salesforce_connect_user-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//click and verify package check
		intelligentDialerTab.clickSalesForceConnect(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_connect_user-- passed");
	}
	
	//Verify system default fields set to true for Contacts Section [ check with New Account]
	@Test(groups = { "MediumPriority"})
	public void verify_system_default_fields_set_to_true_for_contacts_section() {
		System.out.println("Test case --verify_system_default_fields_set_to_true_for_contacts_section-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToContactFieldsSection(supportDriver);

		//verify fields for Account Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.AccountName.displayName()));
		
		//verify fields for First Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.FirstName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.FirstName.displayName()));
		
		//verify fields for Last Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.LastName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.LastName.displayName()));
		
		//verify fields for title
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.Title.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.Title.displayName()));
		
		//verifying always visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.Email.displayName()));
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.Phone.displayName()));
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, "Assistant"));
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, "Home"));
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, "Mobile"));
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, "Other"));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_system_default_fields_set_to_true_for_contacts_section-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void add_additional_field_of_contact_always_visible() {
		System.out.println("Test case --add_additional_field_of_contact_always_visible-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToContactFieldsSection(supportDriver);
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		//Creating case field
		String contactFieldName = "AutoContactField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.OwnerFirstName, contactFieldName, true, false, false);
		
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, contactFieldName));
		assertFalse(salesForceTab.isEditableFieldYes(supportDriver, contactFieldName));
		assertFalse(salesForceTab.isRequiredFieldYes(supportDriver, contactFieldName));
		
		// deleting additional contact
		salesForceTab.deleteAdditionalField(supportDriver, contactFieldName);
		boolean contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, contactFieldName);
		assertFalse(contactFieldExists, String.format("Additional lead field with labal name:%s not deleted", contactFieldName));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_additional_field_of_contact_always_visible-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_add_additional_field_of_contact_editable_required() {
		System.out.println("Test case --verify_add_additional_field_of_contact_editable_required-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToContactFieldsSection(supportDriver);
		
		String contactFieldName = "AutoContactField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AssistantName, contactFieldName, true, false, false);
		
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, contactFieldName));
		assertFalse(salesForceTab.isEditableFieldYes(supportDriver, contactFieldName));
		assertFalse(salesForceTab.isRequiredFieldYes(supportDriver, contactFieldName));
		boolean contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, contactFieldName);
		assertTrue(contactFieldExists, String.format("Additional lead field with labal name:%s not deleted", contactFieldName));
		
		// deleting additional contact
		salesForceTab.deleteAdditionalField(supportDriver, contactFieldName);
		contactFieldExists = salesForceTab.isAddditionalFieldCreated(supportDriver, contactFieldName);
		assertFalse(contactFieldExists, String.format("Additional lead field with labal name:%s not deleted", contactFieldName));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_additional_field_of_contact_editable_required-- passed");
	}
	
	//Verify Default Account fields, User can't sort and Delete
	@Test(groups = { "MediumPriority"})
	public void verify_default_account_fields_user_cant_sort_delete() {
		System.out.println("Test case --verify_default_account_fields_user_cant_sort_delete-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToAccountFieldsSection(supportDriver);

		// verify fields for Account Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.AccountName.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.AccountName.displayName()));
		
		// verify fields for Account Phone
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountPhone.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.AccountPhone.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.AccountPhone.displayName()));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_account_fields_user_cant_sort_delete-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_default_campaign_fields() {
		System.out.println("Test case --verify_default_campaign_fields-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToCampaignFieldsSection(supportDriver);

		// verify fields for Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.Name.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.Name.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.Name.displayName()));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_campaign_fields-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void add_delete_account_fields() {
		
		System.out.println("Test case --add_delete_account_fields-- started ");
		
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning account fields section
		salesForceTab.navigateToAccountFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoAccountField");
		
		//Creating account field
		String accountFieldName = "AutoAccountField".concat(HelperFunctions.GetRandomString(3));
		String createdDate = salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AccountSite, accountFieldName, true, false, false);
		
		//verifying always visible visible, and rest fields not visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, accountFieldName));
		salesForceTab.clickUpddateBtn(supportDriver, accountFieldName);
		assertTrue(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		//verifying updated date
		createdDate= createdDate.substring(0, createdDate.indexOf(" "));
		
		String updatedDate = salesForceTab.getUpdatedDate(supportDriver, accountFieldName);
		updatedDate= updatedDate.substring(0, updatedDate.indexOf(" "));
		
		assertEquals(updatedDate, createdDate);
		
		//verifying account field name created
		boolean accountFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, accountFieldName);
		assertTrue(accountFieldNameExists, String.format("Account field with label name:%s not created", accountFieldName));
		
		//deleting account field
		salesForceTab.deleteAdditionalField(supportDriver, accountFieldName);
		accountFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, accountFieldName);
		assertFalse(accountFieldNameExists, String.format("Account lead field with labal name:%s not deleted", accountFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_account_fields-- passed ");
	}
	
	//Add Additional account field, editable and required
	@Test(groups = { "MediumPriority"})
	public void add_delete_account_fields_for_editable_required() {

		System.out.println("Test case --add_delete_account_fields_for_editable_required-- started ");
		
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning account fields section
		salesForceTab.navigateToAccountFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoAccountField");
		
		//Creating account field
		String accountFieldName = "AutoAccountField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AccountSite, accountFieldName, true, false, false);
		
		//verifying account field name created
		boolean accountFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, accountFieldName);
		assertTrue(accountFieldNameExists, String.format("Account field with label name:%s not created", accountFieldName));

		//verifying always visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, accountFieldName));
		salesForceTab.clickUpddateBtn(supportDriver, accountFieldName);
		assertTrue(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		//deleting account field
		salesForceTab.deleteAdditionalField(supportDriver, accountFieldName);
		accountFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, accountFieldName);
		assertFalse(accountFieldNameExists, String.format("Account field with labal name:%s not deleted", accountFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_account_fields_for_editable_required-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_default_case_fields() {
		System.out.println("Test case --verify_default_case_fields-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToCaseFieldsSection(supportDriver);

		// verify fields for Case Number
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.CaseNumber.displayName()));
		
		// verify fields for Subject
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.Subject.displayName()));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_case_fields-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void add_delete_case_fields_for_always_visible() {
		System.out.println("Test case --add_delete_case_fields_for_always_visible-- started ");
  
		//updating the supportDriver used							
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning case fields section
		salesForceTab.navigateToCaseFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoCaseField");
		
		//Creating case field
		String caseFieldName = "AutoCaseField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.SuppliedName, caseFieldName, true, false, false);
		
		//verifying always visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, caseFieldName));
		assertFalse(salesForceTab.isEditableFieldYes(supportDriver, caseFieldName));
		assertFalse(salesForceTab.isRequiredFieldYes(supportDriver, caseFieldName));
	
		//verifying case field name created
		boolean caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, caseFieldName);
		assertTrue(caseFieldNameExists, String.format("Account field with label name:%s not created", caseFieldName));
		
		//deleting account field
		salesForceTab.deleteAdditionalField(supportDriver, caseFieldName);
		caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, caseFieldName);
		assertFalse(caseFieldNameExists, String.format("Account case field with label name:%s not deleted", caseFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_case_fields_for_always_visible-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void add_delete_case_fields_for_editable_required() {
		// updating the supportDriver used
		System.out.println("Test case --add_delete_case_fields_for_editable_required-- started ");
									
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning case fields section
		salesForceTab.navigateToCaseFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoCaseField");
		
		//Creating case field
		String caseFieldName = "AutoCaseField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.SuppliedName, caseFieldName, true, false, false);
		
		//verifying case field name created
		boolean caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, caseFieldName);
		assertTrue(caseFieldNameExists, String.format("Account field with label name:%s not created", caseFieldName));

		//verifying always visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, caseFieldName));
		salesForceTab.clickUpddateBtn(supportDriver, caseFieldName);
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		//deleting account field
		salesForceTab.deleteAdditionalField(supportDriver, caseFieldName);
		caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, caseFieldName);
		assertFalse(caseFieldNameExists, String.format("Account case field with label name:%s not deleted", caseFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_case_fields_for_editable_required-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void add_delete_campaign_fields_for_always_visible() {
		// updating the supportDriver used
		System.out.println("Test case --add_delete_campaign_fields_for_always_visible-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning case fields section
		salesForceTab.navigateToCampaignFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoCampaignField");
		
		//Creating case field
		String campaignFieldName = "AutoCampaignField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, campaignFieldName, true, false, false);
		
		//verifying always visible
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, campaignFieldName));
		salesForceTab.clickUpddateBtn(supportDriver, campaignFieldName);
		assertTrue(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		//verifying case field name created
		boolean caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, campaignFieldName);
		assertTrue(caseFieldNameExists, String.format("Account field with label name:%s not created", campaignFieldName));
		
		//deleting account field
		salesForceTab.deleteAdditionalField(supportDriver, campaignFieldName);
		caseFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, campaignFieldName);
		assertFalse(caseFieldNameExists, String.format("Account campaign field with label name:%s not deleted", campaignFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_campaign_fields_for_always_visible-- passed ");
	}
	
	//Verify Default Opportunity fields
	@Test(groups = { "MediumPriority"})
	public void verify_default_opportunity_fields() {
		System.out.println("Test case --verify_default_opportunity_fields-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening sales force tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToOpportunityFieldsSection(supportDriver);

		// verify fields for Account Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.AccountName.displayName()));
		
		// verify fields for Name
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.Name.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.Name.displayName()));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_default_opportunity_fields-- passed");
	}
	
	
	//Add New opportunity additional field, always visible checked
	//Delete existing opportunity additional field
	//Add New additional field for opportunity which should editable and required checked
	@Test(groups = { "MediumPriority"})
	public void add_delete_opportunity_fields_for_always_visible() {
		// updating the supportDriver used
		System.out.println("Test case --add_delete_opportunity_fields_for_always_visible-- started ");
  
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning opportunity fields section
		salesForceTab.navigateToOpportunityFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoOpportunityField");
		
		//Creating opportunity field
		String opportunityFieldName = "AutoOpportunityField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.CreatedByEmail, opportunityFieldName, true, false, false);
		
		//verifying opportunity field name created
		boolean opportunityFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, opportunityFieldName);
		assertTrue(opportunityFieldNameExists, String.format("Opportunity field with label name:%s not created", opportunityFieldName));
		
		//deleting opportunity field
		salesForceTab.deleteAdditionalField(supportDriver, opportunityFieldName);
		opportunityFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, opportunityFieldName);
		assertFalse(opportunityFieldNameExists, String.format("Opportunity field with label name:%s not deleted", opportunityFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_opportunity_fields_for_always_visible-- passed ");
	}
	
//	@Test(groups = { "MediumPriority"})
	public void add_delete_opportunity_fields_for_editable_required() {
		System.out.println("Test case --add_delete_opportunity_fields_for_editable_required-- started ");
  
		// updating the supportDriver used							
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		
		// cleaning opportunity fields section
		salesForceTab.navigateToOpportunityFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoOpportunityField");
		
		//Creating opportunity field
		String opportunityFieldName = "AutoOpportunityField".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.TotalOpportunityQuantity, opportunityFieldName, false, true, true);
		
		//verifying opportunity field name created
		boolean opportunityFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, opportunityFieldName);
		assertTrue(opportunityFieldNameExists, String.format("Account field with label name:%s not created", opportunityFieldName));

		//verifying always visible
		assertFalse(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, opportunityFieldName));
		assertTrue(salesForceTab.isEditableFieldYes(supportDriver, opportunityFieldName));
		assertTrue(salesForceTab.isRequiredFieldYes(supportDriver, opportunityFieldName));
		
		//deleting opportunity field
		salesForceTab.deleteAdditionalField(supportDriver, opportunityFieldName);
		opportunityFieldNameExists = salesForceTab.isAddditionalFieldCreated(supportDriver, opportunityFieldName);
		assertFalse(opportunityFieldNameExists, String.format("Opportunity field with label name:%s not deleted", opportunityFieldName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_opportunity_fields_for_editable_required-- passed ");
	}
 
	@Test(groups = { "MediumPriority"})
	public void verify_restrict_calls_to_smart_number_enable_disable_with_multi_match_setting() {
		System.out.println("Test case --verify_restrict_calls_to_smart_number_enable_disable_with_multi_match_setting-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		intelligentDialerTab.enableCreateLeadOnMultiSearchSetting(supportDriver);
		assertFalse(intelligentDialerTab.isCreateLeadSFDCCampaignDisabled(supportDriver));
		assertFalse(intelligentDialerTab.isCreateLeadUnansweredCallDisabled(supportDriver));
		
		intelligentDialerTab.disableCreateLeadOnMultiSearchSetting(supportDriver);
		intelligentDialerTab.idleWait(1);
		assertTrue(intelligentDialerTab.isCreateLeadSFDCCampaignDisabled(supportDriver));
		assertTrue(intelligentDialerTab.isCreateLeadUnansweredCallDisabled(supportDriver));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_restrict_calls_to_smart_number_enable_disable_with_multi_match_setting-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_only_admin_users_search_in_salesforce_connect_user_notifications() {
		System.out.println("Test case --verify_only_admin_users_search_in_salesforce_connect_user_notifications-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.unCheckSendToAllAdmins(supportDriver);
		
		//verification for admin user
		String user = CONFIG.getProperty("qa_admin_user_name");
		assertTrue(salesForceTab.isUserInSalesforceNotificationsDropdown(supportDriver, user));
		salesForceTab.selectSalesforceConnectUser(supportDriver, user);
		salesForceTab.saveAcccountSettings(supportDriver);
		assertTrue(salesForceTab.isUserSavedInSalesforceNotifications(supportDriver, user));
		
		//verification for support user
		user = CONFIG.getProperty("qa_support_user_name");
		assertFalse(salesForceTab.isUserInSalesforceNotificationsDropdown(supportDriver, user));
		
		//verification for agent user
		user = CONFIG.getProperty("qa_agent_user_name");
		assertFalse(salesForceTab.isUserInSalesforceNotificationsDropdown(supportDriver, user));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_only_admin_users_search_in_salesforce_connect_user_notifications-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_upon_check_send_to_alladmins_notifications_gets_disabled() {
		System.out.println("Test case --verify_upon_check_send_to_alladmins_notifications_gets_disabled-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.verifyToolTipNotificationSalesforceConnectUser(supportDriver);
		
		salesForceTab.checkSendToAllAdmins(supportDriver);
		assertTrue(salesForceTab.verifyNotificationTabDisabled(supportDriver));
		
		salesForceTab.unCheckSendToAllAdmins(supportDriver);
		assertFalse(salesForceTab.verifyNotificationTabDisabled(supportDriver));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_only_admin_users_search_in_salesforce_connect_user_notifications-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_case_fields_functionality() {
		System.out.println("Test case --verify_case_fields_functionality-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToCaseFieldsSection(supportDriver);
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.CaseNumber.name());
		
		assertTrue(salesForceTab.isSalesForceFieldDisabled(supportDriver));
		assertTrue(salesForceTab.isNameFieldEditable(supportDriver));
		assertTrue(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isAlwaysVisibleCheckboxVisible(supportDriver));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_case_fields_functionality-- passed ");
	}

//	@Test(groups = { "MediumPriority"})
	public void verify_send_to_top_button_add_field_on_custom_drop() {
		System.out.println("Test case --verify_send_to_top_button_add_field_on_custom_drop-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening salesforce tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToAccountFieldsSection(supportDriver);

		salesForceTab.deleteAllFieldsIfExists(supportDriver);
	
		// Creating account field
		String accountFieldName1 = "AutoAccountFieldSite".concat(HelperFunctions.GetRandomString(3));
		String accountFieldName2 = "AutoAccountFieldType".concat(HelperFunctions.GetRandomString(3));
		String accountFieldName3 = "AutoAccountFieldType".concat(HelperFunctions.GetRandomString(3));
		
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AccountSite, accountFieldName1, true, false, false);
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AccountType, accountFieldName2, true, false, false);
		salesForceTab.createAccountSalesForceField(supportDriver, SalesForceFields.AccountFax, accountFieldName3, true, false, false);
		
		int expectedIndex = salesForceTab.getAddedSfFieldIndex(supportDriver, accountFieldName1);
		assertNotEquals(salesForceTab.getAddedSfFieldIndex(supportDriver, accountFieldName3), expectedIndex);

		//long press and verifying
		salesForceTab.longPressMenuUpIconSfField(supportDriver, accountFieldName3);
		salesForceTab.idleWait(1);
		assertEquals(salesForceTab.getAddedSfFieldIndex(supportDriver, accountFieldName3), expectedIndex);
		
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_send_to_top_button_add_field_on_custom_drop-- passed");
	}
	
	//Update Default Accounts fields
	@Test(groups = { "MediumPriority"})
	public void update_default_account_fields() {
		System.out.println("Test case --update_default_account_fields-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToAccountFieldsSection(supportDriver);
		salesForceTab.cleanAdditionalFieldsSection(supportDriver, "AutoAccountField");

		//updating account name default field with uncheck editable
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountName.displayName());
		salesForceTab.unCheckEditableCheckBox(supportDriver);
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		// verifying fields
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountName.displayName()));
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountName.displayName());
		assertFalse(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		// updating account name default field with check editable
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountName.displayName());
		salesForceTab.checkEditableCheckBox(supportDriver);
		salesForceTab.checkRequiredCheckBox(supportDriver);
		salesForceTab.clickSalesForceSaveBtn(supportDriver);

		// verifying fields
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountName.displayName()));
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountName.displayName());
		assertFalse(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertFalse(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
				
		//updating account phone default field with uncheck editable
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountPhone.displayName());
		salesForceTab.unCheckEditableCheckBox(supportDriver);
		salesForceTab.clickSalesForceSaveBtn(supportDriver);
		
		// verifying fields for phone
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountPhone.displayName()));
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountPhone.displayName());
		assertFalse(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertTrue(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);

		//updating account phone default field with check editable
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountPhone.displayName());
		salesForceTab.checkEditableCheckBox(supportDriver);
		salesForceTab.checkRequiredCheckBox(supportDriver);
		salesForceTab.clickSalesForceSaveBtn(supportDriver);

		// verifying fields
		assertTrue(salesForceTab.isAlwaysVisibleFieldYes(supportDriver, SalesForceFields.AccountPhone.displayName()));
		salesForceTab.clickUpddateBtn(supportDriver, SalesForceFields.AccountPhone.displayName());
		assertFalse(salesForceTab.isEditableCheckBoxDisabled(supportDriver));
		assertFalse(salesForceTab.isRequiredCheckBoxDisabled(supportDriver));
		salesForceTab.clickSalesForceSaveBtn(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --update_default_account_fields-- passed ");
	}
	
	@Test(groups = { "MediumPriority", "SupportOnly"})
	public void verify_internal_ringdna_sfdc_account_id() {
		System.out.println("Test case --verify_internal_ringdna_sfdc_account_id-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening sales force tab
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToSettingsSection(supportDriver);

		//entering value and then verifying
		String id = "AutoID".concat(HelperFunctions.GetRandomString(3));
		salesForceTab.enterRingDNASFDCAccountId(supportDriver, id);
		salesForceTab.saveAcccountSettings(supportDriver);
		
		salesForceTab.navigateToAccountFieldsSection(supportDriver);
		salesForceTab.navigateToSettingsSection(supportDriver);
		assertEquals(salesForceTab.getRingDNASFDCAccountId(supportDriver), id);
		
		//making it blank and then verifying
		salesForceTab.enterRingDNASFDCAccountId(supportDriver, "");
		salesForceTab.saveAcccountSettings(supportDriver);
		assertEquals(salesForceTab.getRingDNASFDCAccountId(supportDriver), "");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_internal_ringdna_sfdc_account_id-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void update_the_order_of_case_fields() {
		System.out.println("Test case --update_the_order_of_case_fields-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		
		String name = profile.getProfileValueFromDropDown(supportDriver);
		
		//opening profile
		dashboard.navigateToProfilesSection(supportDriver);
		if (profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		String qaUserGroup = TestBase.CONFIG.getProperty("qa_user_account");
		String qaUserID = TestBase.CONFIG.getProperty("qa_user_account_id");
		profile.enterAccountWithIdAndSearch(supportDriver, qaUserGroup, qaUserID);
		
		profile.clickUpddateBtn(supportDriver, name);
		
		profile.navigateToCaseFieldsSection(supportDriver);

		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		// Creating account field
		String accountFieldName1 = "AutoAccountFieldSite".concat(HelperFunctions.GetRandomString(3));
		String accountFieldName2 = "AutoAccountFieldType".concat(HelperFunctions.GetRandomString(3));
		
		// creating city field
		if (!profile.isDeleteIconVisible(supportDriver, SalesForceFields.City.displayName())) {
			// creating the new field
			profile.createAccountSalesForceField(supportDriver, SalesForceFields.City, accountFieldName1, true, false, false);
		}

		// creating about mefield
		if (!profile.isDeleteIconVisible(supportDriver, SalesForceFields.AboutMe.displayName())) {
			profile.createAccountSalesForceField(supportDriver, SalesForceFields.AboutMe, accountFieldName2, true, false, false);
		}
		
		int fieldIndex1 = profile.getAddedSfFieldIndex(supportDriver, accountFieldName1);
		int fieldIndex2 = profile.getAddedSfFieldIndex(supportDriver, accountFieldName2);
		assertNotEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName2), fieldIndex1);
		assertNotEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName1), fieldIndex2);
		
		//press and verifying
		profile.clickArrowDownIcon(supportDriver);
		assertEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName2), fieldIndex1);
		assertEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName1), fieldIndex2);
		
		// delete all fields
		profile.clickOpportunityFields(supportDriver);
		profile.navigateToCaseFieldsSection(supportDriver);

		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --update_the_order_of_case_fields-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void update_the_order_of_campaign_fields() {
		System.out.println("Test case --update_the_order_of_campaign_fields-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		
		String name = profile.getProfileValueFromDropDown(supportDriver);
		
		//opening profile
		dashboard.navigateToProfilesSection(supportDriver);
		if (profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}

		String qaUserGroup = TestBase.CONFIG.getProperty("qa_user_account");
		String qaUserID = TestBase.CONFIG.getProperty("qa_user_account_id");
		profile.enterAccountWithIdAndSearch(supportDriver, qaUserGroup, qaUserID);

		profile.clickUpddateBtn(supportDriver, name);
		
		profile.navigateToCampaignFieldsSection(supportDriver);

		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		// Creating account field
		String accountFieldName1 = "AutoAccountFieldSite".concat(HelperFunctions.GetRandomString(3));
		String accountFieldName2 = "AutoAccountFieldType".concat(HelperFunctions.GetRandomString(3));
		
		//creating city field
		if (!profile.isDeleteIconVisible(supportDriver, SalesForceFields.City.displayName())) {
			// creating the new field
			profile.createAccountSalesForceField(supportDriver, SalesForceFields.City, accountFieldName1, true, true,
					false);
		}

		// creating about mefield
		if (!profile.isDeleteIconVisible(supportDriver, SalesForceFields.AboutMe.displayName())) {
			profile.createAccountSalesForceField(supportDriver, SalesForceFields.AboutMe, accountFieldName2, true,
					true, false);
		}
		
		//profile.createAccountSalesForceField(supportDriver, accountFieldName1, true, false, false, name);
		//profile.createAccountSalesForceField(supportDriver, accountFieldName2, true, false, false, name);
		
		int fieldIndex1 = profile.getAddedSfFieldIndex(supportDriver, accountFieldName1);
		int fieldIndex2 = profile.getAddedSfFieldIndex(supportDriver, accountFieldName2);
		assertNotEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName2), fieldIndex1);
		assertNotEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName1), fieldIndex2);
		
		//press and verifying
		profile.clickArrowDownIcon(supportDriver);
		assertEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName2), fieldIndex1);
		assertEquals(profile.getAddedSfFieldIndex(supportDriver, accountFieldName1), fieldIndex2);
		
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --update_the_order_of_campaign_fields-- passed ");
	}
	
	//Match Accounts/ Contact/ Lead/ Oppt ON
	@Test(groups = { "Regression" })
	public void on_phone_match_options() {
		System.out.println("Test case --on_off_phone_match_options-- started ");
		// updating the supportDriver used
		initializeSupport();
		
		driverUsed.put("supportDriver", true);
		
		//open account salesforce page
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		//toggle on phone match options
		intelligentDialerTab.enablePhoneMatchAccountSetting(supportDriver);
		intelligentDialerTab.enablePhoneMatchContactSetting(supportDriver);
		intelligentDialerTab.enablePhoneMatchLeadsToggleSetting(supportDriver);
		intelligentDialerTab.enablePhoneMatchOpportunitiesSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		dashboard.switchToTab(supportDriver, 1);
		supportDriver.navigate().refresh();
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callScreenPage.clickOnUpdateDetailLink(supportDriver);
		callScreenPage.clickAddToExistingButton(supportDriver);
		callScreenPage.searchExistingContact(supportDriver, "test");
		
		assertTrue(callScreenPage.isPhoneMatchAccountOptionAvailable(supportDriver));
		assertTrue(callScreenPage.isPhoneMatchLeadsOptionAvailable(supportDriver));
		assertTrue(callScreenPage.isPhoneMatchContactsOptionAvailable(supportDriver));
		
		dashboard.switchToTab(supportDriver, 2);
		salesForceTab.setDefaultSalesforceSettings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --on_off_phone_match_options-- passed ");
	}
	
	//Match Accounts/ Contact/ Lead/ Oppt OFF
	@Test(groups = { "Regression" })
	public void off_phone_match_options() {
		System.out.println("Test case --on_off_phone_match_options-- started ");
		// updating the supportDriver used
		initializeSupport();
		
		driverUsed.put("supportDriver", true);
		
		//open account salesforce page
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		//toggle on phone match options
		intelligentDialerTab.disablePhoneMatchAccountSetting(supportDriver);
		intelligentDialerTab.disablePhoneMatchContactSetting(supportDriver);
		intelligentDialerTab.disablePhoneMatchLeadsToggleSetting(supportDriver);
		intelligentDialerTab.disablePhoneMatchOpportunitiesSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		dashboard.switchToTab(supportDriver, 1);
		supportDriver.navigate().refresh();
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callScreenPage.clickOnUpdateDetailLink(supportDriver);
		callScreenPage.clickAddToExistingButton(supportDriver);
		callScreenPage.searchExistingContact(supportDriver, "test");
		
		assertFalse(callScreenPage.isPhoneMatchAccountOptionAvailable(supportDriver));
		assertFalse(callScreenPage.isPhoneMatchLeadsOptionAvailable(supportDriver));
		assertFalse(callScreenPage.isPhoneMatchContactsOptionAvailable(supportDriver));
		
		dashboard.switchToTab(supportDriver, 2);
		salesForceTab.setDefaultSalesforceSettings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --on_off_phone_match_options-- passed ");
	}
	
	//User should able to add 15 maximum additional fields with objects
	//Disable Add Additional field icon after adding 15 fields
	@Test(groups = { "Regression" })
	public void verify_salesforce_tab_user_add_not_more_than_15_field() {
		System.out.println("Test case --verify_salesforce_tab_user_add_not_more_than_15_field-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		
		dashboard.navigateToProfilesSection(supportDriver);
		if(profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		profile.clickPencilEditIcon(supportDriver);
		profile.navigateToContactFieldsSection(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 15) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profile.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profile.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_tab_user_add_not_more_than_15_field-- passed ");
	}
	
	//Add Additional field icon disable after adding 15 fields
	//Verify Default Additional Fields of Lead [check with New account]
	@Test(groups = { "Regression" })
	public void verify_salesforce_lead_field_user_add_not_more_than_15_field() {
		System.out.println("Test case --verify_salesforce_lead_field_user_add_not_more_than_15_field-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// open account salesforce page
		dashboard.clickAccountsLink(supportDriver);
		salesForceTab.openSalesforceTab(supportDriver);
		salesForceTab.navigateToLeadFieldsSection(supportDriver);
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.FirstName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.LastName.displayName()));
		assertFalse(salesForceTab.isDeleteIconVisible(supportDriver, SalesForceFields.Company.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.FirstName.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.LastName.displayName()));
		assertFalse(salesForceTab.isSortFieldsVisible(supportDriver, SalesForceFields.Company.displayName()));

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToProfilesSection(supportDriver);
		if(profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		profile.clickPencilEditIcon(supportDriver);
		profile.clickLeadFields(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 15) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profile.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profile.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_lead_field_user_add_not_more_than_15_field-- passed ");
	}
	
	//Add button disable when we have 10 additional fields for Opportunity
	@Test(groups = { "Regression" })
	public void verify_salesforce_opportunity_field_user_add_not_more_than_10_field() {
		System.out.println("Test case --verify_salesforce_opportunity_field_user_add_not_more_than_10_field-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToProfilesSection(supportDriver);
		if(profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		profile.clickPencilEditIcon(supportDriver);
		profile.clickOpportunityFields(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 10) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profile.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profile.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_opportunity_field_user_add_not_more_than_10_field-- passed");
	}
	
	//Maximum Ten Additional fields should be addable , verify disable plus icon
	//Add Additional field as editable and required
	@Test(groups = { "Regression" })
	public void verify_salesforce_campaign_field_user_add_not_more_than_10_field() {
		System.out.println("Test case --verify_salesforce_campaign_field_user_add_not_more_than_10_field-- started");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToProfilesSection(supportDriver);
		if(profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		profile.clickPencilEditIcon(supportDriver);
		profile.navigateToCampaignFieldsSection(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 10) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profile.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profile.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_campaign_field_user_add_not_more_than_10_field-- passed");
	}
	
	//Verify 15 on Account Salesforse tab page
	@Test(groups = { "MediumPriority"})
	public void verify_salesforce_account_field_user_add_not_more_than_15_field() {
		System.out.println("Test case --verify_salesforce_account_field_user_add_not_more_than_15_field-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToProfilesSection(supportDriver);
		if(profile.isWelcomeProfilesSectionVisible(supportDriver)) {
			profile.clickGetStartedButton(supportDriver);
		}
		
		profile.clickPencilEditIcon(supportDriver);
		profile.clickAccountFields(supportDriver);
		
		int fields = salesForceTab.getNumberOfDeleteAllFields(supportDriver);

		int i = fields;
		while (i < 15) {
			Enum<?> field = SalesForceFields.Random;
			if (!salesForceTab.isDeleteIconVisible(supportDriver, field.toString())) {
				profile.createAccountSalesForceField(supportDriver, field, ("AutoContact".concat(HelperFunctions.GetRandomString(3))), true, false, false);
			i++;
			}
		}

		// verify add icon disabled
		assertTrue(profile.isAddFieldIconDisabled(supportDriver));
		
		// delete all fields
		salesForceTab.deleteAllFieldsIfExists(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_salesforce_account_field_user_add_not_more_than_15_field-- passed ");
	}
	
	//@AfterMethod(groups = { "Regression", "SupportOnly" })
		public void deleteLPNumbers(ITestResult result) {

			if (result.getMethod().getMethodName().equals("verify_salesforce_tab_user_add_not_more_than_15_field")) {
				if (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) {

					System.out.println("Running clean up code for failed long presence case");
					// navigating to free user account
					initializeSupport();
					driverUsed.put("supportDriver", true);
					dashboard.switchToTab(supportDriver, 2);
					
					dashboard.clickAccountsLink(supportDriver);
					salesForceTab.openSalesforceTab(supportDriver);
					
					// cleaning additional fields section of contact section
					salesForceTab.navigateToContactFieldsSection(supportDriver);
					// delete all fields
					salesForceTab.deleteAllFieldsIfExists(supportDriver);
					driverUsed.put("supportDriver", false);
				}
			}
		}
}
