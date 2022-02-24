package guidedSelling.cases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import base.SeleniumBase;
import base.TestBase;
import guidedSelling.base.GuidedSellingBase;
import guidedSelling.source.pageClasses.ActionsPage;
import guidedSelling.source.pageClasses.ActionsPage.ActionsTypesEnum;
import guidedSelling.source.pageClasses.ActionsPage.actionsFooters;
import guidedSelling.source.pageClasses.EngagePage;
import guidedSelling.source.pageClasses.EngagePage.EngageEnum;
import guidedSelling.source.pageClasses.EngagePage.FilterValuesEnum;
import guidedSelling.source.pageClasses.SequencesPage;
import guidedSelling.source.pageClasses.SequencesPage.ActivationType;
import guidedSelling.source.pageClasses.SequencesPage.CriteriaFields;
import guidedSelling.source.pageClasses.SequencesPage.EntryExitFields;
import guidedSelling.source.pageClasses.SequencesPage.FieldOpTionsEnum;
import guidedSelling.source.pageClasses.SequencesPage.FieldUpdatesEnum;
import guidedSelling.source.pageClasses.SequencesPage.RecordTypes;
import guidedSelling.source.pageClasses.SequencesPage.SeqReportCards;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.ContactDetailPage.SectionModuleNames;
import softphone.source.salesforce.LeadDetailPage;
import softphone.source.salesforce.SalesforceAccountPage;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.SalesforceTestLoginPage;
import softphone.source.salesforce.SearchPage;
import softphone.source.salesforce.TaskDetailPage;
import utility.HelperFunctions;
import utility.SalesForceAPIUtility;
import utility.GmailPageClass;

public class GS_Sanity extends GuidedSellingBase {

	ActionsPage actionPage = new ActionsPage();
	SequencesPage sequencePage = new SequencesPage();
	SalesforceHomePage sfHomePage = new SalesforceHomePage();
	SearchPage sfSearchPage = new SearchPage();
	ContactDetailPage contactDetailPage = new ContactDetailPage();
	LeadDetailPage leadDetailPage = new LeadDetailPage();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	SalesforceAccountPage sfAccountPage = new SalesforceAccountPage();
	SalesforceTestLoginPage sfTestLogin = new SalesforceTestLoginPage();
	SeleniumBase seleniumBase = new SeleniumBase();
	GmailPageClass gmail = new GmailPageClass();
	EngagePage engagePage = new EngagePage();
	TaskDetailPage taskDetail = new TaskDetailPage();
	HashMap<String, String> updateFielsDataMap = new HashMap<String, String>();

	MediaType mediaType = null;

	HashMap<CriteriaFields, String> entryExitDataMap = new HashMap<CriteriaFields, String>();

	WebDriver gsDriverCall = null;
	WebDriver agentDriverGS = null;

	String salesForceUrl;
	String softPhoneUrl;
	String userName;
	String password;
	String accountId;
	String app_url;
	String qaAdminUserName;
	String qaAdminUserPassword;
	String receiverUserName;
	String receiverPassword;
	String receiverNumber;
	private String gmailEmail;
	private String gmailPassword;
	private String gmail_account_user;
	private String exchange_account_user;
	String callActionName = null;
	String taskActionName = null;
	String smsActionName = null;
	String taskValue = null;
	String autoEmailActionName = null;
	String manualEmailActionName = null;
	String emailBodytext = "Hello, Here myself GS QA member for testing email body based on the Text email template. ";		
	@BeforeClass(groups = { "Sanity" })
	public void beforeClassRead() {
		salesForceUrl = CONFIG.getProperty("gs_salesforce_url");
		userName = CONFIG.getProperty("qa_support_user_username");
		password = CONFIG.getProperty("qa_support_user_password");
		accountId = CONFIG.getProperty("accountId");
		softPhoneUrl = CONFIG.getProperty("qa_test_site_name");
		app_url = CONFIG.getProperty("app_url");
		receiverUserName = CONFIG.getProperty("qa_user_receiver_username");
		receiverPassword = CONFIG.getProperty("qa_user_receiver_password");
		receiverNumber = CONFIG.getProperty("qa_user_receiver_number");

		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
		gmail_account_user = CONFIG.getProperty("gmail_account_user");
		exchange_account_user = CONFIG.getProperty("exchange_account_user");

		qaAdminUserName = CONFIG.getProperty("qa_user_username");
		qaAdminUserPassword = CONFIG.getProperty("qa_user_password");

	}

	@Test(groups = { "Sanity" })
	public void Create_new_Call_action() {
		System.out.println("Test case --Create_new_Call_action-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create action of type 'Call'
		callActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, callActionName, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, callActionName);
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Completed), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Overdue), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Skipped), "0%");
		driverUsed.put("webSupportDriver", false);
	}

	@Test(groups = { "Sanity" })
	public void create_sms_action() {
		System.out.println("Test case --create_sms_action-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// create action of type 'SMS'
		smsActionName = "ActionSMS_".concat(HelperFunctions.GetRandomString(3));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, smsActionName, ActionsTypesEnum.SMS, null);
		actionPage.verifyActionCreated(webSupportDriver, smsActionName);
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Completed), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Overdue), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Skipped), "0%");
		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --create_sms_action-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void Create_new_task_action() {
		System.out.println("Test case --Create_new_task_action-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create action of type 'Task'
		taskActionName = "ActionTask_".concat(HelperFunctions.GetRandomString(3));
	    taskValue = "Company_".concat(HelperFunctions.GetRandomString(4));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, taskActionName, ActionsTypesEnum.Task, taskValue);
		actionPage.verifyActionCreated(webSupportDriver, taskActionName);
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Completed), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Overdue), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Skipped), "0%");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Create_new_task_action-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void Create_auto_email_action_native_email_immediate_activation() {
		System.out.println("Test case --Create_auto_email_action_native_email_immediate_activation-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// Create first Auto Email Action
		autoEmailActionName = "ActionnativeEmailAutomatic_".concat(HelperFunctions.GetRandomString(3));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, autoEmailActionName, ActionsTypesEnum.NativeEmailAutomatic, null);
		seleniumBase.idleWait(3);
		actionPage.verifyActionCreated(webSupportDriver, autoEmailActionName);
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Email_Opened), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Email_Replied), "0%");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Create_auto_email_action_native_email_immediate_activation-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void Create_manual_email_action_native_account() {
		System.out.println("Test case --Create_manual_email_action_native_account-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// Create first Manual Email Action
		manualEmailActionName = "ActionnativeEmailManual_".concat(HelperFunctions.GetRandomString(3));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, manualEmailActionName, ActionsTypesEnum.NativeEmailManual, null);
		actionPage.verifyActionCreated(webSupportDriver, manualEmailActionName);
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Completed), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Overdue), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Skipped), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Email_Opened), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.Email_Replied), "0%");
		assertEquals(actionPage.getActionFooterPercentge(webSupportDriver, actionsFooters.AvgDaystoComplete), "0.00");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Create_manual_email_action_native_account-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_automatic_contact() {
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueRandom + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// open contact and check sequence id
		fullName = firstname + " " + exitValueRandom;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact-- passed ");
	}

	@Test (groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_automatic_lead() {
		System.out.println("Test case --verify_entry_exit_criteria_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println("leadId" + leadId);
		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		int activeParticipantCountBefore = Integer
				.valueOf(sequencePage.getSequenceReportCard(webSupportDriver, SeqReportCards.ActiveParticipants));
		int metExitCriteriaCountBefore = Integer
				.valueOf(sequencePage.getSequenceReportCard(webSupportDriver, SeqReportCards.MetExitCriteria));

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		int activeParticipantCountAfter = Integer
				.valueOf(sequencePage.getSequenceReportCard(webSupportDriver, SeqReportCards.ActiveParticipants));
		assertEquals(activeParticipantCountAfter, activeParticipantCountBefore + 1);

		// update exit value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueRandom + "\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// verify exit criteria count increases
		sequencePage.idleWait(5);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);

		int metExitCriteriaCountAfter = Integer
				.valueOf(sequencePage.getSequenceReportCard(webSupportDriver, SeqReportCards.MetExitCriteria));
		assertEquals(metExitCriteriaCountAfter, metExitCriteriaCountBefore + 1);

		// open lead and check sequence id
		fullName = firstname + " " + exitValueRandom;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_manual_lead() {
		System.out.println("Test case --verify_entry_exit_criteria_manual_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;
		String fullName = firstname + " " + lastname;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead and Manual
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id should be null for Manual type
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// add sequence through 'Add to Seq' btn on contact
		contactDetailPage.clickAddToSeqBtn(webSupportDriver);
		sequencePage.associateSequenceFromParticipant(webSupportDriver, sequenceName);

		// open contact and check sequence id
		seleniumBase.idleWait(5);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is mtaching with seq name
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueRandom + "\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);

		// open lead and check sequence id
		fullName = firstname + " " + exitValueRandom;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_manual_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_manual_contact() {
		System.out.println("Test case --verify_entry_exit_criteria_manual_contact-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact and activation type Manual
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// add sequence through 'Add to Seq' btn on contact
		contactDetailPage.clickAddToSeqBtn(webSupportDriver);
		sequencePage.associateSequenceFromParticipant(webSupportDriver, sequenceName);

		// open contact and check sequence id
		seleniumBase.idleWait(2);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is mtaching with seq name
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames has action name
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueRandom + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);

		// open contact and check sequence id
		fullName = firstname + " " + exitValueRandom;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_manual_contact-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void associate_sequence_ringdna_card_manual_contact() {
		System.out.println("Test case --associate_sequence_ringdna_card_manual_contact-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact and activation type Manual
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequence(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id is mtaching with seq name
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName);

		// check participantActionNames has action name
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueRandom + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);

		// open contact and check sequence id
		fullName = firstname + " " + exitValueRandom;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --associate_sequence_ringdna_card_manual_contact-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_additional_criteria_for_added_actions() {
		System.out.println("Test case --verify_additional_criteria_for_added_actions-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "SequenceTask_".concat(HelperFunctions.GetRandomString(4));
		;
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Company, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check value Task updated
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check company name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String leadCompanyName = leadDetailPage.getCompanyName(webSupportDriver);
		assertEquals(leadCompanyName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_additional_criteria_for_added_actions-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_taskAction_text_field_update_automatic_lead() {
		System.out.println("Test case --verify_taskAction_text_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "SequenceTask_".concat(HelperFunctions.GetRandomString(4));
		;
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Company, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check value Task updated
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check company name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String leadCompanyName = leadDetailPage.getCompanyName(webSupportDriver);
		assertEquals(leadCompanyName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_taskAction_text_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_taskAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_taskAction_picklist_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open lead and check value Task updated
		leadDetailPage.switchToTab(webSupportDriver, 2);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Industry name is null
		String industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(industryName));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "Automotive";
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Industry, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check value Task updated
		fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Industry name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertEquals(industryName, sequenceTaskValue);

		// Open Lead and and verify Activity History
		String userName = CONFIG.getProperty("qa_support_user_name");
		String subjectName = taskValue;
		String headerName = "Assigned To";
		int index = leadDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		System.out.println("index" + index);
		assertTrue(leadDetailPage.assignedTo(webSupportDriver, subjectName, index, userName));

		// delete sequence
		seleniumBase.isScrollPresentAtTopOfPage(webSupportDriver);
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);


		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_taskAction_picklist_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_taskAction_boolean_field_update_automatic_lead() {
		System.out.println("Test case --verify_taskAction_boolean_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open lead and check value Task updated
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Fax Opt out boolean not checked
		assertFalse(leadDetailPage.isFaxOptOutChecked(webSupportDriver));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);

		// add boolean value sequence true
		String sequenceTaskValue = "True";
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.FaxOptOut, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check value Task updated
		fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Industry name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);

		// check Fax Opt out boolean checked
		assertTrue(leadDetailPage.isFaxOptOutChecked(webSupportDriver));

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_taskAction_boolean_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_callAction_text_field_update_automatic_lead() {
		System.out.println("Test case --verify_callAction_text_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open a new driver to recieve call
		WebDriver gsDriverCall = getDriver();
		String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// update phone number value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "SequenceTask_".concat(HelperFunctions.GetRandomString(4));
		;
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Company, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// Open engage and perform call action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);
		engagePage.verifyParticipantNotPresentOnEngage(webSupportDriver, firstname);

		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		// closing called driver
		gsDriverCall.quit();

		// open lead and check value Task updated
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, callActionName, index));

		// check company name match with sequence task value
		String leadCompanyName = leadDetailPage.getCompanyName(webSupportDriver);
		assertEquals(leadCompanyName, sequenceTaskValue);

		// Open Lead and and verify Activity History
		String subjectName = "Outbound Call: +12054833130";
		String assignedTo = "Assigned To";
		int indexNum = leadDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, assignedTo);
		assertTrue(leadDetailPage.assignedTo(webSupportDriver, subjectName, indexNum, userName));

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_callAction_text_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_callAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_callAction_picklist_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		softPhoneSettingsPage.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.enableDialNextSetting(webSupportDriver);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open a new driver to recieve call
		gsDriverCall = getDriver();
		String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// update phone number value in first lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);

		// create a second lead through API with same first name to match entrance
		// criteria
		String lastname2 = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company2 = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId2 = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname2, company2);

		// update phone number value in second lead
		RequestBody body2 = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId2, body2);

		// create a third lead through API with same first name to match entrance
		// criteria
		String lastname3 = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company3 = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId3 = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname3, company3);

		// update phone number value in second lead
		RequestBody body3 = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId3, body3);

		// open first lead
		String fullName = firstname + " " + lastname;
		sfSearchPage.switchToTab(webSupportDriver, 2);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Industry name null
		String industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(industryName));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead and 'Industry' Picklist
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "Automotive";
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Industry, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		sequencePage.refreshCurrentDocument(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 3);

		// Open engage and performing dial next on extension
		String parentWindow = webSupportDriver.getWindowHandle();
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.clickOnCallsEngageTab(webSupportDriver, EngageEnum.Calls);
		int size = engagePage.clickOnActionToBePerformedBtn(webSupportDriver, firstname, ActionsTypesEnum.Call);
		assertTrue(size == 3);

		String enagagetWindowHandle = webSupportDriver.getWindowHandle();
		engagePage.switchToExtension(webSupportDriver);

		String firstDialUser = callScreenPage.getDialNextUser(webSupportDriver);
		callScreenPage.clickDialNextBtn(webSupportDriver);
		String secondDialUser = callScreenPage.getDialNextUser(webSupportDriver);

		callScreenPage.clickDialNextBtn(webSupportDriver);
		String thirdDialUser = callScreenPage.getDialNextUser(webSupportDriver);

		assertTrue(Strings.isNotNullAndNotEmpty(firstDialUser));
		assertTrue(Strings.isNotNullAndNotEmpty(secondDialUser));
		assertTrue(Strings.isNotNullAndNotEmpty(thirdDialUser));
		assertNotEquals(firstDialUser, secondDialUser);
		assertNotEquals(secondDialUser, thirdDialUser);
		assertNotEquals(firstDialUser, thirdDialUser);

		webSupportDriver.close();

		// switch To engage tab
		engagePage.switchToTabWindow(webSupportDriver, enagagetWindowHandle);

		// update name in second lead to remove from engage
		String name = HelperFunctions.GetRandomString(5);
		RequestBody body4 = RequestBody.create(mediaType, "{\n\"FirstName\":\"" + name + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId2, body4);

		// update name in third lead to remove from engage
		name = HelperFunctions.GetRandomString(5);
		body4 = RequestBody.create(mediaType, "{\n\"FirstName\":\"" + name + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId3, body4);

		leadDetailPage.refreshCurrentDocument(webSupportDriver);

		// perform call action complete
		engagePage.idleWait(2);
		engagePage.clickOnCallsEngageTab(webSupportDriver, EngageEnum.Calls);
		size = engagePage.clickOnActionToBePerformedBtn(webSupportDriver, firstname, ActionsTypesEnum.Call);
		assertTrue(size == 1);

		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);

		// waiting for participant remove from engage list
		engagePage.verifyParticipantNotPresentOnEngage(webSupportDriver, firstname);

		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, parentWindow);

		// closing called driver
		gsDriverCall.quit();

		// open lead and check value Task updated
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check company name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		leadDetailPage.waitUntilTextPresent(webSupportDriver, leadDetailPage.industry, sequenceTaskValue);
		industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertEquals(industryName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		// delete lead
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId2);

		// delete lead
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId3);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_callAction_picklist_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "create_sms_action" })
	public void verify_smsAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_smsAction_picklist_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// update phone number value in lead
		String phoneNumber = receiverNumber;
		RequestBody body = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);

		// open lead
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check Industry name null
		String industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(industryName));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead and 'Industry' Picklist
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "Automotive";
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Messages, smsActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Industry, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(5);
		engagePage.verifyParticipantNotPresentOnEngage(webSupportDriver, firstname);

		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		// open lead and check value Task updated
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check company name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertEquals(industryName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_smsAction_picklist_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_reports_tab_not_displayed_during_edit_create_sequence() {
		System.out.println("Test case --verify_reports_tab_not_displayed_during_edit_create_sequence-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create sequence and check reports visible or not
		sequencePage.switchToTab(webSupportDriver, 2);
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickCreateSeqBtn(webSupportDriver);
		sequencePage.verifyReportsNotVisible(webSupportDriver);

		// edit sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLinkAccToIndex(webSupportDriver, 0);
		sequencePage.clickEditSequenceBtn(webSupportDriver);
		sequencePage.verifyReportsNotVisible(webSupportDriver);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_reports_tab_not_displayed_during_edit_create_sequence-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_automatic_contact_opportunity() {
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact_opportunity-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println("contactId  = "+contactId);
		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");
		engagePage.idleWait(10);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified", opportunityName,
				accountId, closeDate, contactId);
		System.out.println("opportunityId  = "+opportunityId);
		// creating data in HashMap
		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, opportunityName);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.DoesNotEqual.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, opportunityName);

		// create sequence of type Opportunity
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Opportunity,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Opportunity
		String opportunityNameExit = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"" + opportunityNameExit + "\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// open contact and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact_opportunity-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_manual_contact_opportunity() {
		System.out.println("Test case --verify_entry_exit_criteria_manual_contact_opportunity-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl,"Never Qualified",  opportunityName,
				accountId, closeDate, contactId);

		// creating data in HashMap
		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, opportunityName);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.DoesNotEqual.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, opportunityName);

		// create sequence of type Opportunity and activation type Manual
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Opportunity,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is null
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// add sequence through 'Add to Seq' btn on contact
		contactDetailPage.clickAddToSeqBtn(webSupportDriver);
		sequencePage.associateSequenceFromParticipant(webSupportDriver, sequenceName);

		// open contact and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Opportunity
		String opportunityNameExit = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"" + opportunityNameExit + "\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// open contact and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		// delete opportunity
		SalesForceAPIUtility.deleteOpportunity(salesForceUrl, bearerToken, opportunityId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_manual_contact_opportunity-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_automatic_contact_campaign() {
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact_campaign-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// create campaign through API
		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);

		// creating data in HashMap
		String exitValueCampaign = "CampaignNameExit_"
				.concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, campaignName);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueCampaign);

		// create sequence of type Campaign
		String sequenceName = "SequenceCampaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Campaign,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Campaign
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"" + exitValueCampaign + "\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// open contact and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		engagePage.idleWait(10);
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		engagePage.idleWait(10);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		// delete campaign
		SalesForceAPIUtility.deleteCampaign(salesForceUrl, bearerToken, campaignId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_automatic_contact_campaign-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void associate_sequence_ringdna_card_manual_contact_opportunity() {
		System.out.println("Test case --associate_sequence_ringdna_card_manual_contact_opportunity-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified", opportunityName,
				accountId, closeDate, contactId);

		// creating data in HashMap
		String opportunityNameExit = "OpportunityExit_"
				.concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, opportunityName);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, opportunityNameExit);

		// create sequence of type Opportunity and activation type Manual
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Opportunity,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequence(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id is mtaching with seq name
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName);

		// check participantActionNames has action name
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Opportunity
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"" + opportunityNameExit + "\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// open contact and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --associate_sequence_ringdna_card_manual_contact-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_non_admin_user_able_to_create_update_tasks() {
		System.out.println("Test case --verify_non_admin_user_able_to_create_update_tasks-- started ");

		agentDriverGS = getDriver();
		sfTestLogin.salesForceTestLogin(agentDriverGS, CONFIG.getProperty("gs_salesforce_url"),
				CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		callScreenPage.closeLightningDialogueBox(agentDriverGS);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl,
				CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		engagePage.idleWait(10);
		sfSearchPage.enterSearchTextandSelect(agentDriverGS, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(agentDriverGS, fullName);

		String taskSubject = "TaskSubject_".concat(HelperFunctions.GetRandomString(4));
		leadDetailPage.createNewTaskOpenActivity(agentDriverGS, taskSubject);

		String newTaskSubject = "TaskSubject_".concat(HelperFunctions.GetRandomString(4));
		leadDetailPage.editTaskOpenActivity(agentDriverGS, taskSubject, newTaskSubject);

		// delete lead
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		agentDriverGS.quit();

		System.out.println("Test case --verify_non_admin_user_able_to_create_update_tasks-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_exit_criteria_automatic_lead_campaign() {
		System.out.println("Test case --verify_entry_exit_criteria_automatic_lead_campaign-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// create campaign through API
		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add lead in campaign through API
		engagePage.idleWait(10);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);

		// creating data in HashMap
		String exitValueCampaign = "CampaignNameExit_"
				.concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, campaignName);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Name.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueCampaign);

		// create sequence of type Campaign
		String sequenceName = "SequenceCampaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Campaign,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// navigate to sequence tab and click sequence link
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.clickSequenceLink(webSupportDriver, sequenceName);

		// verify participant count increases
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 1);

		// update exit value in Campaign
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"" + exitValueCampaign + "\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		// open lead and check sequence id
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id is empty
		sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(sequenceId));

		// check participantActionNames empty
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames == null || participantActionNames.isEmpty());

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		// delete campaign
		SalesForceAPIUtility.deleteCampaign(salesForceUrl, bearerToken, campaignId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_exit_criteria_automatic_lead_campaign-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void skip_bulk_particpants_from_engage_list() {
		System.out.println("Test case --skip_bulk_particpants_from_engage_list-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// skip bulk participants from engage
		engagePage.switchToTab(webSupportDriver, 2);
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.skipParticipantsEngage(webSupportDriver, 4);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --skip_bulk_particpants_from_engage_list-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_entry_criteria_for_contact_opportunity_with_stage_name() {
		System.out.println("Test case --verify_entry_criteria_for_contact_opportunity_with_stage_name-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yy yy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		engagePage.idleWait(10);
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "0 - Meeting Booked by AE", opportunityName,
		accountId, closeDate, contactId);

		// creating data in HashMap
		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.Stage.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Includes.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, EntryExitFields.MeetingBookedbyAE.getValue());

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.Stage.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Includes.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, EntryExitFields.ClosedLost.getValue());

		// create sequence of type Opportunity
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Opportunity,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		seleniumBase.idleWait(30);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);
		assertTrue(sequencePage.getParticipantsCount(webSupportDriver) > 0);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		SalesForceAPIUtility.deleteOpportunity(salesForceUrl, bearerToken, opportunityId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_entry_criteria_for_contact_opportunity_with_stage_name-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_click_links_redirects_in_enagage_page() {
		System.out.println("Test case --verify_click_links_redirects_in_enagage_page-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// skip bulk participants from engage
		engagePage.switchToTab(webSupportDriver, 2);
		engagePage.openEngageInNewTab(webSupportDriver);
		String engageWindowHandle = webSupportDriver.getWindowHandle();
		engagePage.selectFilterByType(webSupportDriver, FilterValuesEnum.Contact);
		List<WebElement> list = engagePage.getColumnListAccToHeader(webSupportDriver, "Company");
		String accountName = list.get(0).getText();

		// opening account in new tab and switching
		Actions actions = new Actions(webSupportDriver);
		WebElement openUrl = list.get(0);
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		engagePage.switchToTab(webSupportDriver, engagePage.getTabCount(webSupportDriver));
		engagePage.waitForPageLoaded(webSupportDriver);
		engagePage.isMaintananceMessageVisible(webSupportDriver);
		sfAccountPage.verifyAccountsNameHeading(webSupportDriver, accountName);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, engageWindowHandle);

		// opening contact in new tab and switching
		list.clear();
		list = engagePage.getColumnListAccToHeader(webSupportDriver, "Name");
		String contactName = list.get(0).getText();
		openUrl = list.get(0);
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		engagePage.switchToTab(webSupportDriver, engagePage.getTabCount(webSupportDriver));
		assertEquals(contactDetailPage.getCallerName(webSupportDriver), contactName);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, engageWindowHandle);

		// opening lead in new tab and switching
		list.clear();
		engagePage.selectFilterByType(webSupportDriver, FilterValuesEnum.Lead);
		list = engagePage.getColumnListAccToHeader(webSupportDriver, "Name");
		String leadName = list.get(0).getText();
		openUrl = list.get(0);
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		engagePage.switchToTab(webSupportDriver, engagePage.getTabCount(webSupportDriver));
		assertEquals(leadDetailPage.getLeadName(webSupportDriver), leadName);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, engageWindowHandle);

		engagePage.switchToTab(webSupportDriver, 2);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_click_links_redirects_in_enagage_page-- passed ");
	}

	// Deepanker
	@Test(groups = { "Sanity" }, dependsOnMethods = {"Create_auto_email_action_native_email_immediate_activation",
			"Create_manual_email_action_native_account" })
	public void add_email_action_to_perform_immediate_in_automatic_sequence() {
		System.out.println("Test case --add_email_action_to_perform_immediate_in_automatic_sequence-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);

		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);

		String[] actionNames = { manualEmailActionName };
		sequencePage.addAction(webSupportDriver, ActionsTypesEnum.Email, actionNames);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// Open engage and perform Email action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);
		assertTrue(engagePage.findAction(webSupportDriver));
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);
		webSupportDriver.navigate().refresh();

		// Verify Native Email Auto
		// Open participantAction page
		contactDetailPage.openParticipantActions(webSupportDriver, autoEmailActionName);

		assertTrue(contactDetailPage.verifyIsActionInitiated(webSupportDriver));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "expectedExecutionDate"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
		webSupportDriver.navigate().back();

		// Open participantAction page
		contactDetailPage.openParticipantActions(webSupportDriver, manualEmailActionName);

		assertTrue(contactDetailPage.verifyIsActionInitiated(webSupportDriver));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "expectedExecutionDate"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --add_email_action_to_perform_immediate_in_automatic_sequence-- passed ");

	}

	// Deepanker
	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_auto_email_action_native_email_immediate_activation",
			"Create_manual_email_action_native_account" })
	public void correct_subject_and_body_should_populate_for_both_native_manual() {
		System.out.println("Test case --correct_subject_and_body_should_populate_for_both_native_manual-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);

		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);

		String[] actionNames = { manualEmailActionName };
		sequencePage.addAction(webSupportDriver, ActionsTypesEnum.Email, actionNames);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// Open engage and perform Email action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);
		assertTrue(engagePage.findAction(webSupportDriver));
		webSupportDriver.navigate().refresh();
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

//		 Open engage and perform second Email action (commented due to Bug)
//		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);
//		assertTrue(engagePage.findAction(webSupportDriver));
//		webSupportDriver.navigate().refresh();

		// Verify Native Email Auto
		// Open participantAction page
		webSupportDriver.navigate().refresh();
		contactDetailPage.openParticipantActions(webSupportDriver, autoEmailActionName);
		assertTrue(contactDetailPage.verifyIsActionInitiated(webSupportDriver));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "expectedExecutionDate"));
		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
		webSupportDriver.navigate().back();

////		// Open participantAction page (commented due to Bug)
//		contactDetailPage.openParticipantActions(webSupportDriver, manEmailAction);
//		assertTrue(contactDetailPage.verifyIsActionInitiated(webSupportDriver));
//		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));
//		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "expectedExecutionDate"));
//		assertTrue(contactDetailPage.notNullParam(webSupportDriver, "emailSendMethod"));

		// delete sequence
		seleniumBase.isScrollPresentAtTopOfPage(webSupportDriver);
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --correct_subject_and_body_should_populate_for_both_native_manual-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_manual_email_action_native_account" })
	public void validation_message_add_sequence_actions_appears_user_clicks_Save() {
		System.out.println("Test case --validation_message_add_sequence_actions_appears_user_clicks_Save-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);

		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, manualEmailActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --validation_message_add_sequence_actions_appears_user_clicks_Save-- passed ");

	}

	// Deepanker
	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_auto_email_action_native_email_immediate_activation" })
	public void able_to_send_email_via_native_gmail_account() {
		System.out.println("Test case --able_to_send_email_via_native_gmail_account-- started ");

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		// create contact through API

		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, qaAdminUserName, qaAdminUserPassword);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println("contactId =  " + contactId);

		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(qaAdminDriver);

		sequencePage.addEntryExitCriteriaSequence(qaAdminDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);
		sequencePage.saveCompleteSequence(qaAdminDriver);
		sequencePage.verifySequenceCreated(qaAdminDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(qaAdminDriver), 0);
		sequencePage.clikActivateBtn(qaAdminDriver);
		sequencePage.waitUntilParticipantCount(qaAdminDriver, 1);

//		gmail.openGmailInNewTab(qaAdminDriver);
//		gmail.switchToTab(qaAdminDriver, gmail.getTabCount(qaAdminDriver));
//		gmail.loginGamil(qaAdminDriver, gmailEmail, gmailPassword);
//		seleniumBase.idleWait(3);
//		qaAdminDriver.get("https://mail.google.com/mail/u/0/#inbox");
//		actionPage.callSearchsubject(qaAdminDriver);
//		gmail.OpenSearchsubjectMail(qaAdminDriver);
//		assertEquals(gmail.verifySenderName(qaAdminDriver), gmail_account_user);

		driverUsed.put("webSupportDriver", false);
		driverUsed.put("qaAdminDriver", false);
		System.out.println("Test case --able_to_send_email_via_native_gmail_account-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_auto_email_action_native_email_immediate_activation" })
	public void able_to_send_email_via_native_exchange_account() {
		System.out.println("Test case --able_to_send_email_via_native_exchange_account-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println("Contact created id =" + contactId);
		
		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);

		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);
		engagePage.idleWait(10);
		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

//		gmail.openGmailInNewTab(webSupportDriver);
//		gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
//		gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
//		seleniumBase.idleWait(3);
//		webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
//		actionPage.callSearchsubject(webSupportDriver);
//		gmail.openNewMailInGmail(webSupportDriver, 1);
//		assertEquals(gmail.verifySenderName(webSupportDriver), exchange_account_user);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --able_to_send_email_via_native_exchange_account-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void verify_when_assign_owner_to_individual_actions_is_selected_as_custom_user_lookup() {
		System.out.println(
				"Test case --verify_when_assign_owner_to_individual_actions_is_selected_as_custom_user_lookup-- started ");

		// Create Call Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String callActionName = "callActionName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String ssID =	SalesForceAPIUtility.getSesstionID ( app_url, userName, password);
	    String jsondata =SalesForceAPIUtility.getNoteTemplates (app_url, accountId, ssID,"callNoteTemplates");
	    String callActionId = SalesForceAPIUtility.createCallAction(bearerToken, salesForceUrl, callActionName, jsondata);
		System.out.println("callActionId   " + callActionId);
		System.out.println("callActionName  " + callActionName);
		
		// create Lead through API
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);
		

		
		// Add action in the sequence
		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "sdr_owner__c", updateFielsDataMap);
		
		
		

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(leadUrl);
		leadDetailPage.editSavelead(qaAdminDriver);

		sequencePage.switchToTab(qaAdminDriver, 2);
		String url = CONFIG.getProperty("gs_sequence_page_url") + SequenceId;
		qaAdminDriver.get(url);
		sequencePage.waitUntilParticipantCount(qaAdminDriver, 1);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");

		String Avatar = CONFIG.getProperty("Avatar");
		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltip(qaAdminDriver, Avatar, userName);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform call action
		// String currentWindowHandle = webSupportDriver.getWindowHandle();

		engagePage.openEngageInNewTab(qaAdminDriver);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(qaAdminDriver);
		engagePage.idleWait(20);
		engagePage.verifyParticipantNotPresentOnEngage(qaAdminDriver, firstname);

		// Open Lead and and verify Activity History
		qaAdminDriver.get(leadUrl);
		String subjectName = "Outbound Call: +12054833130";
		String headerName = "Assigned To";
		engagePage.idleWait(10);
		int index = leadDetailPage.getIndexOfParticipantActionsHeader(qaAdminDriver, headerName);
		assertTrue(leadDetailPage.assignedTo(qaAdminDriver, subjectName, index, userName));

		driverUsed.put("qaAdminDriver", false);
		System.out.println(
				"Test case --verify_when_assign_owner_to_individual_actions_is_selected_as_custom_user_lookup.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void verify_Assign_owner_individual_actions_custom_user_lookup_for_sms() {
		System.out.println("Test case --verify_Assign_owner_individual_actions_custom_user_lookup_for_sms-- started ");

		// Create SMS Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		
		smsActionName = "ActionSMS_".concat(HelperFunctions.GetRandomString(3));
		String ssID =	SalesForceAPIUtility.getSesstionID ( app_url, userName, password);
	    String smsjsondata =SalesForceAPIUtility.getNoteTemplates (app_url, accountId, ssID,"smsTemplates");
	    String smsActionId = SalesForceAPIUtility.createSmsAction(bearerToken, salesForceUrl, smsActionName, smsjsondata);
	    System.out.println("smsActionId  = " + smsActionId);
	    System.out.println("smsActionName =  " + smsActionName);

		// create Lead through API
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);
		// Add action in the sequence\
		// Add action in the sequence
		
		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId, "custom_lookup1__c", updateFielsDataMap);
		

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(leadUrl);
		leadDetailPage.editSavelead(qaAdminDriver);

		sequencePage.switchToTab(qaAdminDriver, 2);
		String url = CONFIG.getProperty("gs_sequence_page_url") + SequenceId;
		qaAdminDriver.get(url);
		sequencePage.waitUntilParticipantCount(qaAdminDriver, 1);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");
		String Avatar = CONFIG.getProperty("Avatar");
		

		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltip(qaAdminDriver, Avatar, userName);

		// Open engage and perform task action
		String currentWindowHandle = qaAdminDriver.getWindowHandle();
		engagePage.openEngageInNewTab(qaAdminDriver);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(qaAdminDriver);
		engagePage.idleWait(10);
		assertFalse(engagePage.verifyParticipantPresentOnEngage(qaAdminDriver, firstname));

		engagePage.closeTab(qaAdminDriver);
		engagePage.switchToTabWindow(qaAdminDriver, currentWindowHandle);

		// Open Lead and and verify Activity History

		String url2 = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(url2);
		String userName1 = CONFIG.getProperty("qa_user_name");
		String subjectName = "Outbound Message: +12054833130";
		String headerName = "Assigned To";
		int index = leadDetailPage.getIndexOfParticipantActionsHeader(qaAdminDriver, headerName);
		assertTrue(leadDetailPage.assignedTo(qaAdminDriver, subjectName, index, userName1));

		driverUsed.put("qaAdminDriver", false);
		System.out.println("Test case --verify_Assign_owner_individual_actions_custom_user_lookup_for_sms-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_assign_owner_to_individual_actions_selected_custom_user_lookup_for_task() {
		System.out.println(
				"Test case --verify_assign_owner_to_individual_actions_selected_custom_user_lookup_for_task-- started ");

		// Create Task Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String taskActionName = "TaskActionName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String taskActionId = SalesForceAPIUtility.createTaskAction(bearerToken, salesForceUrl, taskActionName);
		System.out.println("taskActionName" + taskActionName);
		System.out.println("taskActionId" + taskActionId);

		// create Lead through API
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		// Add action in the sequence

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId, "custom_lookup1__c", updateFielsDataMap);
		
		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(leadUrl);
		leadDetailPage.editSavelead(qaAdminDriver);

		sequencePage.switchToTab(qaAdminDriver, 2);
		String url = CONFIG.getProperty("gs_sequence_page_url") + SequenceId;
		qaAdminDriver.get(url);
		sequencePage.waitUntilParticipantCount(qaAdminDriver, 1);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");
		String Avatar = CONFIG.getProperty("Avatar");
		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltip(qaAdminDriver, Avatar, userName);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(qaAdminDriver);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Task, null);
		engagePage.closeTab(qaAdminDriver);
		engagePage.switchToTab(qaAdminDriver, 2);

		// Open Lead and and verify Activity History
		String url2 = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(url2);
		// Open Lead and and verify Activity History
		String subjectName = "Test Sub 01";
		String headerName = "Assigned To";
		int index = leadDetailPage.getIndexOfParticipantActionsHeader(qaAdminDriver, headerName);
		assertTrue(leadDetailPage.assignedTo(qaAdminDriver, subjectName, index, userName));

		driverUsed.put("qaAdminDriver", false);
		System.out.println(
				"Test case --verify_assign_owner_to_individual_actions_selected_custom_user_lookup_for_task-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_assign_owner_individual_actions_custom_user_lookup_native_auto() {
		System.out.println(
				"Test case --verify_assign_owner_individual_actions_custom_user_lookup_native_auto-- started ");

		// Create Auto Email Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String autoEmailActionName = "AutoEmailActionName"
				.concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String autoEmailActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				autoEmailActionName, "Automatic");
		System.out.println("autoEmailActionName  " + autoEmailActionName);
		System.out.println("autoEmailActionId  " + autoEmailActionId);

		// create Lead through API
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		// Add action in the sequence
		
		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, autoEmailActionId, "custom_lookup1__c", updateFielsDataMap);
		
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(leadUrl);
		leadDetailPage.editSavelead(qaAdminDriver);

		sequencePage.switchToTab(qaAdminDriver, 2);
		String url = CONFIG.getProperty("gs_sequence_page_url") + SequenceId;
		qaAdminDriver.get(url);

		// Open Lead and and verify Activity History
		qaAdminDriver.get(leadUrl);
		seleniumBase.idleWait(30);
		assertTrue(leadDetailPage.verifActivityHistory(qaAdminDriver, autoEmailActionName, leadId));

		driverUsed.put("qaAdminDriver", false);
		System.out
				.println("Test case --verify_assign_owner_individual_actions_custom_user_lookup_native_auto-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void verify_sequence_level_delegation_selected_lead_owner_email_manual_naive() {
		System.out.println(
				"Test case --verify_sequence_level_delegation_selected_lead_owner_email_manual_naive-- started ");

		// Create Manual Email Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		autoEmailActionName = "AutoEmailAction_".concat(HelperFunctions.GetRandomString(3));
	    String munalEmailActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl, autoEmailActionName,"Manual");
	    System.out.println("autoEmaiActionId  = " + munalEmailActionId);
	    System.out.println("autoEmailActionName =  " + autoEmailActionName);
		
		// create Lead through API
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println("leadId  " + leadId);

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		// Add action in the sequence
		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, munalEmailActionId, "sdr_owner__c", updateFielsDataMap);
		

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_salesforce_url") + leadId;
		qaAdminDriver.get(leadUrl);
		leadDetailPage.editSavelead(qaAdminDriver);

		sequencePage.switchToTab(qaAdminDriver, 2);
		String url = CONFIG.getProperty("gs_sequence_page_url") + SequenceId;
		qaAdminDriver.get(url);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");

		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, "VC"));
		leadDetailPage.verifydnaAvatartooltip(qaAdminDriver, "VC", userName);

		// Open engage and perform Email action
		engagePage.openEngageInNewTab(qaAdminDriver);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Email, null);
		seleniumBase.idleWait(5);
		assertTrue(engagePage.findAction(qaAdminDriver));
		engagePage.closeTab(qaAdminDriver);
		engagePage.switchToTab(qaAdminDriver, 2);
		qaAdminDriver.navigate().refresh();

		// Open Lead and and verify Activity History

		qaAdminDriver.get(leadUrl);
		seleniumBase.idleWait(10);
		qaAdminDriver.navigate().refresh();
		assertTrue(leadDetailPage.verifActivityHistory(qaAdminDriver, userName, leadId));

		driverUsed.put("qaAdminDriver", false);
		System.out.println(
				"Test case --verify_sequence_level_delegation_selected_lead_owner_email_manual_naive-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_auto_email_action_native_email_immediate_activation" })
	public void verify_EmailAction_text_field_update_automatic_lead() {
		System.out.println("Test case --verify_EmailAction_text_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "SequenceTask_".concat(HelperFunctions.GetRandomString(4));
		
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Company, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		seleniumBase.idleWait(10);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check sequence id match with sequence
		engagePage.idleWait(10);
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// open lead and check value updated
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check company name match with sequence task value
		seleniumBase.idleWait(30);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String leadCompanyName = leadDetailPage.getCompanyName(webSupportDriver);
		assertEquals(leadCompanyName,sequenceTaskValue);
		
		// verify Activity History
		assertTrue(leadDetailPage.verifActivityHistory(webSupportDriver, autoEmailActionName, leadId));
		
		//Open Activity History
		contactDetailPage.openRecentCallEntry(webSupportDriver,"Emails: "+autoEmailActionName);
		
		String subjectName = taskDetail.getSubject(webSupportDriver);
		assertEquals(subjectName,"Emails: "+autoEmailActionName);
		String objecttName = taskDetail.getName(webSupportDriver);
		assertEquals(objecttName,fullName+", "+sequenceTaskValue);
		String CommentsData = taskDetail.getComments(webSupportDriver);
		assertEquals(CommentsData,emailBodytext);
		String taskSubtype = contactDetailPage.getTaskSubtype(webSupportDriver);
		assertEquals(taskSubtype,"Email");
		String taskStatus = taskDetail.getTaskStatus(webSupportDriver);
		assertEquals(taskStatus,"Completed");

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_EmailAction_text_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_auto_email_action_native_email_immediate_activation" })
	public void verify_EmailAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_EmailAction_picklist_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println("leadId =  " + leadId);

		// open lead and check value Task updated
		leadDetailPage.switchToTab(webSupportDriver, 2);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check Industry name is null
		String industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(industryName));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "Automotive";
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, autoEmailActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Industry, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// open lead and check value Task updated
		fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check Industry name match with sequence task value
		engagePage.idleWait(10);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertEquals(industryName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_EmailAction_picklist_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_manual_email_action_native_account" })
	public void verify_munalEmailAction_text_field_update_automatic_lead() {
		System.out.println("Test case --verify_MunalEmailAction_text_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		;

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "SequenceTask_".concat(HelperFunctions.GetRandomString(4));
		
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, manualEmailActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Company, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check sequence id match with sequence
		String sequenceId = contactDetailPage.getSequenceId(webSupportDriver);
		assertEquals(sequenceId, sequenceName);

		// open lead and check value updated
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check company name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String leadCompanyName = leadDetailPage.getCompanyName(webSupportDriver);
		engagePage.idleWait(20);
		assertEquals(leadCompanyName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_MunalEmailAction_text_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_manual_email_action_native_account" })
	public void verify_munalEmailAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_MunalEmailAction_picklist_field_update_automatic_lead-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println("leadId  = " + leadId);

		// open lead and check value Task updated
		leadDetailPage.switchToTab(webSupportDriver, 2);
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check Industry name is null
		String industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertTrue(Strings.isNullOrEmpty(industryName));

		// creating data in HashMap
		String exitValueRandom = "LeadLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);
		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Lead
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String sequenceTaskValue = "Automotive";
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Email, manualEmailActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.Industry, sequenceTaskValue, null);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		// Open engage and perform task action
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check value Task updated
		fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, fullName);
		sfSearchPage.clickLeadNameLink(webSupportDriver, fullName);

		// check Industry name match with sequence task value
		leadDetailPage.refreshCurrentDocument(webSupportDriver);
		industryName = leadDetailPage.getIndustry(webSupportDriver);
		assertEquals(industryName, sequenceTaskValue);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_MunalEmailAction_picklist_field_update_automatic_lead-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void delete_email_action_from_action_list_when_click_on_down_arrow() {
		System.out.println("Test case --delete_email_action_from_action_list_when_click_on_down_arrow-- started ");

		// Create Auto Email Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String autoEmailActionName = "callActionName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String autoEmailActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				autoEmailActionName, "Automatic");
		System.out.println("autoEmailActionName" + autoEmailActionName);
		System.out.println("autoEmailActionId" + autoEmailActionId);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.findCreatedAction(webSupportDriver, autoEmailActionName);
		actionPage.deleteAction(webSupportDriver, autoEmailActionName);

		assertTrue(actionPage.verifyActionDeleted(webSupportDriver, autoEmailActionName));

		// create action of type 'Call'
		callActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.switchToTab(webSupportDriver, 2);
		actionPage.createAnAction(webSupportDriver, callActionName, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, callActionName);

		// create contact through API
		String bearerToken1 = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken1, salesForceUrl, firstname, lastname);
		System.out.println("contactId = " + contactId);
		// creating data in HashMap
		String exitValueRandom = "ContactLastExit_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();

		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create sequence of type Contact
		String sequenceName = "Sequence_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);

		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName);

		assertEquals(sequencePage.getParticipantsCount(webSupportDriver), 0);
		sequencePage.clikActivateBtn(webSupportDriver);
		sequencePage.waitUntilParticipantCount(webSupportDriver, 1);

		actionPage.findCreatedAction(webSupportDriver, callActionName);
		actionPage.deleteAction(webSupportDriver, callActionName);
		assertEquals(actionPage.verifyDeleteActionInformation(webSupportDriver, callActionName),
				"Can not delete " + callActionName + " action as it is already associated with a sequence.");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --delete_email_action_from_action_list_when_click_on_down_arrow-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void mets_exit_criteria_via_action_field_update_qualifies_another_sequence() {
		System.out.println(
				"Test case --mets_exit_criteria_via_action_field_update_qualifies_another_sequence -- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.LastName, exitValueSeq1, null);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform call action for seq1

		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		String taskValue = "Company_".concat(HelperFunctions.GetRandomString(4));
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open lead and check action performed
		String fullName2 = firstname + " " + exitValueSeq1;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName2);

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, taskActionName, index));

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), taskActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println(
				"Test case --mets_exit_criteria_via_action_field_update_qualifies_another_sequence -- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_lead_qualifies_for_another_sequence_entry_criteria() {
		System.out.println("Test case --verify_lead_qualifies_for_another_sequence_entry_criteria-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Lead,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Lead,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName1, SectionModuleNames.ReactRingDNAModuleLead);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// Open engage and perform call action for seq1

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// update phone number value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.idleWait(10);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// closing called driver
		gsDriverCall.quit();

		// open lead and check action performed
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, callActionName, index));

		// update fields of lead to match exit criteria for first sequence
		body = null;
		body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueSeq1 + "\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);

		// check sequence id is null
		fullName = firstname + " " + exitValueSeq1;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		// check participantActionNames
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// update fields of lead to match entrance criteria for second sequence
		body = null;
		body = RequestBody.create(mediaType, "{\n\"FirstName\": \"" + exitValueSeq1 + "\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), callActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_lead_qualifies_for_another_sequence_entry_criteria-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_lead_qualifies_for_another_manual_sequence_entry_criteria() {
		System.out.println("Test case --verify_lead_qualifies_for_another_manual_sequence_entry_criteria-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Lead,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.LastName, exitValueSeq1, null);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Lead,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName1, SectionModuleNames.ReactRingDNAModuleLead);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform call action for seq1

		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// check sequence id is null
		fullName = firstname + " " + exitValueSeq1;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenLeadtNameLinkVisible(webSupportDriver, fullName);

		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, taskActionName, index));

		// check participantActionNames
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName2, SectionModuleNames.ReactRingDNAModuleLead);

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), taskActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_lead_qualifies_for_another_manual_sequence_entry_criteria-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void contact_mets_exit_criteria_via_action_field_update_qualifies_another_sequence() {
		System.out.println(
				"Test case --contact_mets_exit_criteria_via_action_field_update_qualifies_another_sequence -- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueSeq1 = "contactExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.LastName, exitValueSeq1, null);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open contact and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform call action for seq1

		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		String taskValue = "Company_".concat(HelperFunctions.GetRandomString(4));
		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// open contact and check action performed
		String fullName2 = firstname + " " + exitValueSeq1;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName2);

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, taskActionName, index));

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), taskActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println(
				"Test case --contact_mets_exit_criteria_via_action_field_update_qualifies_another_sequence -- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_Call_action" })
	public void verify_contact_qualifies_for_another_sequence_entry_criteria() {
		System.out.println("Test case --verify_contact_qualifies_for_another_sequence_entry_criteria-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type contact and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, callActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "ContactExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Contact,
				ActivationType.Automatic, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName1,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// Open engage and perform call action for seq1

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// update phone number value in lead
		RequestBody body = RequestBody.create(mediaType, "{\n\"Phone\": \"" + phoneNumber + "\"}");

		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		sfSearchPage.refreshCurrentDocument(webSupportDriver);

		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// closing called driver
		gsDriverCall.quit();

		// open lead and check action performed
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, callActionName, index));

		// update fields of contact to match exit criteria for first sequence
		body = null;
		body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueSeq1 + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);

		// check sequence id is null
		fullName = firstname + " " + exitValueSeq1;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		// check participantActionNames
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), callActionName);

		// update fields of lead to match entrance criteria for second sequence
		body = null;
		body = RequestBody.create(mediaType, "{\n\"FirstName\": \"" + exitValueSeq1 + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), callActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_contact_qualifies_for_another_sequence_entry_criteria-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_contact_qualifies_for_another_manual_sequence_entry_criteria() {
		System.out
				.println("Test case --verify_contact_qualifies_for_another_manual_sequence_entry_criteria-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.addAdvanceSettings(webSupportDriver, FieldUpdatesEnum.FieldUpdatesRequired,
				FieldOpTionsEnum.LastName, exitValueSeq1, null);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// associate sequence through 'Assocate' btn on lead
		engagePage.idleWait(10);
		contactDetailPage.associateSequence(webSupportDriver, sequenceName1,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform call action for seq1

		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// check sequence id is null
		fullName = firstname + " " + exitValueSeq1;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, taskActionName, index));

		// check participantActionNames
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName2,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), taskActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete lead
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_contact_qualifies_for_another_manual_sequence_entry_criteria-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "Create_new_task_action" })
	public void verify_contact_qualifies_for_another_manual_sequence_entry_criteria_enduser() {
		System.out.println(
				"Test case --verify_contact_qualifies_for_another_manual_sequence_entry_criteria_enduser-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);

		// creating data in HashMap
		String exitValueSeq1 = "LeadExitSeq1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, firstname);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueSeq1);

		// create sequence1 of type Lead and Manual
		String sequenceName1 = "Sequence1_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName1, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Task, taskActionName);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName1);

		sequencePage.clikActivateBtn(webSupportDriver);

		// adding seq 1 exit criteria name in seq2 entry
		String exitValueRandom = "LeadExitSeq2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		entryExitDataMap.clear();
		entryExitDataMap.put(CriteriaFields.EntryCriteria, EntryExitFields.LastName.getValue());
		entryExitDataMap.put(CriteriaFields.EntryOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.EntryValue, exitValueSeq1);

		entryExitDataMap.put(CriteriaFields.ExitCriteria, EntryExitFields.FirstName.getValue());
		entryExitDataMap.put(CriteriaFields.ExitOperator, EntryExitFields.Equals.getValue());
		entryExitDataMap.put(CriteriaFields.ExitValue, exitValueRandom);

		// create action2 of type 'Call' for seq2
		String actionName2 = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		actionPage.createAnAction(webSupportDriver, actionName2, ActionsTypesEnum.Call, null);
		actionPage.verifyActionCreated(webSupportDriver, actionName2);

		// create sequence2 of type Lead
		String sequenceName2 = "Sequence2_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.addEntryExitCriteriaSequence(webSupportDriver, sequenceName2, RecordTypes.Contact,
				ActivationType.Manual, entryExitDataMap, ActionsTypesEnum.Call, actionName2);
		sequencePage.saveCompleteSequence(webSupportDriver);
		sequencePage.verifySequenceCreated(webSupportDriver, sequenceName2);

		sequencePage.clikActivateBtn(webSupportDriver);

		// open lead and check sequence id
		String fullName = firstname + " " + lastname;
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		// associate sequence through 'Assocate' btn on lead
		engagePage.idleWait(10);
		contactDetailPage.associateSequence(webSupportDriver, sequenceName1,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id match with sequence1
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName1);

		// check participantActionNames match with actionName
		List<String> participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// Open engage and perform call action for seq1

		engagePage.openEngageInNewTab(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(5);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTab(webSupportDriver, 2);

		// update fields of contact to match exit criteria for first sequence
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"" + exitValueSeq1 + "\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);

		// check sequence id is null
		fullName = firstname + " " + exitValueSeq1;
		sfSearchPage.refreshCurrentDocument(webSupportDriver);
		sfSearchPage.enterSearchTextandSelect(webSupportDriver, firstname);
		sfSearchPage.clickWhenContactNameLinkVisible(webSupportDriver, fullName);

		contactDetailPage.verifySequenceId(webSupportDriver, " ");

		String headerName = "isActionPerformed";
		int index = contactDetailPage.getIndexOfParticipantActionsHeader(webSupportDriver, headerName);
		assertTrue(contactDetailPage.isActionChecked(webSupportDriver, taskActionName, index));

		// check participantActionNames
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 1);
		assertEquals(participantActionNames.get(0), taskActionName);

		// associate sequence through 'Assocate' btn on lead
		contactDetailPage.associateSequence(webSupportDriver, sequenceName2,
				SectionModuleNames.ReactRingDNAModuleContact);

		// check sequence id match with sequence2
		contactDetailPage.verifySequenceId(webSupportDriver, sequenceName2);

		// check participantActionNames match with actionName
		participantActionNames.clear();
		participantActionNames = contactDetailPage.getParticipantActionsList(webSupportDriver);
		assertTrue(participantActionNames.size() == 2);
		assertEquals(participantActionNames.get(0), taskActionName);
		assertEquals(participantActionNames.get(1), actionName2);

		// delete sequence
		sequencePage.navigateToSequencetab(webSupportDriver);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName1);
		sequencePage.deleteSequenceByBtn(webSupportDriver, sequenceName2);

		// delete contact
		bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		SalesForceAPIUtility.deleteContact(salesForceUrl, bearerToken, contactId);

		driverUsed.put("webSupportDriver", false);
		System.out.println(
				"Test case --verify_contact_qualifies_for_another_manual_sequence_entry_criteria_enduser-- passed ");
	} 
	

	@AfterMethod(groups = { "Sanity" })
	public void afterMethodGS(ITestResult result) {
		List<String> dependsOnMethods = new ArrayList<>();
		dependsOnMethods.add("Create_new_Call_action");
		dependsOnMethods.add("create_sms_action");
		dependsOnMethods.add("Create_new_task_action");
		dependsOnMethods.add("Create_auto_email_action_native_email_immediate_activation");
		dependsOnMethods.add("Create_manual_email_action_native_account");

		if (gsDriverCall != null)
			gsDriverCall.quit();

		if (agentDriverGS != null)
			agentDriverGS.quit();

		// handling special case for depends on methods, so that dependent method
		// doesn't skip in first failure
		if (dependsOnMethods.contains(result.getName())) {
			result.getTestContext().getSkippedTests().removeResult(result.getMethod());
		}
	}
}
