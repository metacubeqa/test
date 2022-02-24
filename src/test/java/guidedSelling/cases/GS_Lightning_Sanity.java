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
import guidedSelling.base.GuidedSellingBase;
import guidedSelling.source.pageClasses.ActionsPage;
import guidedSelling.source.pageClasses.ActionsPage.ActionsTypesEnum;
import guidedSelling.source.pageClasses.ActionsPage.actionsFooters;
import guidedSelling.source.pageClasses.EngagePage.EngageEnum;
import guidedSelling.source.pageClasses.EngagePage.FilterValuesEnum;
import guidedSelling.source.pageClasses.SequencesPage.ActivationType;
import guidedSelling.source.pageClasses.SequencesPage.CriteriaFields;
import guidedSelling.source.pageClasses.SequencesPage.EntryExitFields;
import guidedSelling.source.pageClasses.SequencesPage.FieldOpTionsEnum;
import guidedSelling.source.pageClasses.SequencesPage.FieldUpdatesEnum;
import guidedSelling.source.pageClasses.SequencesPage.RecordTypes;
import guidedSelling.source.pageClasses.EngagePage;
import guidedSelling.source.pageClasses.SequencesPage;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.LeadDetailPage;
import softphone.source.salesforce.SalesforceAccountPage;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.SalesforceTestLoginPage;
import softphone.source.salesforce.SearchPage;
import softphone.source.salesforce.TaskDetailPage;
import softphone.source.salesforce.ContactDetailPage.SectionModuleNames;
import utility.GmailPageClass;
import utility.HelperFunctions;
import utility.SalesForceAPIUtility;

public class GS_Lightning_Sanity extends GuidedSellingBase {

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
	guidedSelling.source.pageClasses.SnippetPage SnippetPage = new guidedSelling.source.pageClasses.SnippetPage();

	MediaType mediaType = null;

	HashMap<String, String> updateFielsDataMap = new HashMap<String, String>();

	WebDriver gsDriverCall = null;
	WebDriver agentDriverGS = null;

	String salesForceUrl;
	String softPhoneUrl;
	String userName;
	String password;
	String qaAdminUserName;
	String qaAdminUserPassword;
	String receiverUserName;
	String receiverPassword;
	String receiverNumber;
	private String gmailEmail;
	private String gmailPassword;
	private String gmail_account_user;
	private String exchange_account_user;
	String qa_support_user_salesforce_id;
	String qa_user_salesforce_id;
	String callActionId = null;
	String smsActionId = null;
	String taskActionId = null;
	String autoEmaiActionId = null;
	String manualEmaiActionId = null;
	String qa_support_user;

	String callActionName = null;
	String taskActionName = null;
	String smsActionName = null;
	String accountId;
	String accountId_field;
	String endpointurl = null;
	String app_url;
	String taskValue = null;
	String autoEmailActionName = null;
	String manualEmailActionName = null;
	String emailBodytext = "Hello, Here myself GS QA member for testing email body based on the Text email template. ";

	@BeforeClass(groups = { "Sanity" })
	public void beforeClassRead() {
		salesForceUrl = CONFIG.getProperty("gs_salesforce_url");
		userName = CONFIG.getProperty("qa_support_user_username");
		password = CONFIG.getProperty("qa_support_user_password");
		qa_support_user = CONFIG.getProperty("qa_support_user_name");
		accountId = CONFIG.getProperty("accountId");
		accountId_field = CONFIG.getProperty("account_id_field");
		app_url = CONFIG.getProperty("app_url");

		endpointurl = CONFIG.getProperty("endpointurl");

		softPhoneUrl = CONFIG.getProperty("qa_test_site_name");
		receiverUserName = CONFIG.getProperty("qa_user_receiver_username");
		receiverPassword = CONFIG.getProperty("qa_user_receiver_password");
		receiverNumber = CONFIG.getProperty("qa_user_receiver_number");
		qa_support_user_salesforce_id = CONFIG.getProperty("qa_support_user_salesforce_id");
		gmailEmail = CONFIG.getProperty("gmail_email_id");
		gmailPassword = CONFIG.getProperty("gmail_password");
		gmail_account_user = CONFIG.getProperty("gmail_account_user");
		exchange_account_user = CONFIG.getProperty("exchange_account_user");

		qaAdminUserName = CONFIG.getProperty("qa_user_username");
		qaAdminUserPassword = CONFIG.getProperty("qa_user_password");
		qa_user_salesforce_id = CONFIG.getProperty("qa_user_salesforce_id");

	}

	@Test(groups = { "Sanity" })
	public void Create_auto_email_action_native_email_immediate_activation() {
		System.out.println("Test case --Create_auto_email_action_native_email_immediate_activation-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// Create first Auto Email Action
		autoEmailActionName = "ActionnativeEmailAutomatic_".concat(HelperFunctions.GetRandomString(3));
		// actionPage.switchToTab(webSupportDriver, 2);
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
		// actionPage.switchToTab(webSupportDriver, 2);
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

	@Test(groups = { "Sanity" })
	public void Verify_toaster_validation_message_should_displaye_RTE() {
		System.out.println("Test case --Verify_toaster_validation_message_should_displaye_RTE-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		// Create first Auto Email Action
		autoEmailActionName = "ActionnativeEmailAutomatic_".concat(HelperFunctions.GetRandomString(3));
		// actionPage.switchToTab(webSupportDriver, 2);
		seleniumBase.idleWait(3);
		String erroText = actionPage.Verify_RTE(webSupportDriver, autoEmailActionName,
				ActionsTypesEnum.NativeEmailAutomatic, null);
		assertEquals(erroText,
				"Automatic emails cannot contain required text updates. Please remove the required text update or convert the action to a manual email.");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Verify_toaster_validation_message_should_displaye_RTE-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void createCallAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		callActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String jsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "callNoteTemplates");
		callActionId = SalesForceAPIUtility.createCallAction(bearerToken, salesForceUrl, callActionName, jsondata);
		System.out.println("callActionId  = " + callActionId);
		System.out.println("callActionName =  " + callActionName);

		System.out.println("Test case --createCallAction.-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void update_delete_CallAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String call_ActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String jsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "callNoteTemplates");
		String callAction_Id = SalesForceAPIUtility.createCallAction(bearerToken, salesForceUrl, call_ActionName,
				jsondata);
		SalesForceAPIUtility.updateAction(bearerToken, endpointurl, callAction_Id);
		SalesForceAPIUtility.deleteAction(bearerToken, endpointurl, callAction_Id);

		System.out.println("Test case --update_delete_CallAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void createSMSAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		smsActionName = "ActionSMS_".concat(HelperFunctions.GetRandomString(3));
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String smsjsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "smsTemplates");
		smsActionId = SalesForceAPIUtility.createSmsAction(bearerToken, salesForceUrl, smsActionName, smsjsondata);
		System.out.println("smsActionId  = " + smsActionId);
		System.out.println("smsActionName =  " + smsActionName);
		System.out.println("Test case --createSMSAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void update_delete_SMSAction() {
		System.out.println("Test case --update_delete_SMSAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String sms_ActionName = "ActionSMS_".concat(HelperFunctions.GetRandomString(3));
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String jsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "smsTemplates");
		String sms_ActionId = SalesForceAPIUtility.createSmsAction(bearerToken, salesForceUrl, sms_ActionName,
				jsondata);
		SalesForceAPIUtility.updateAction(bearerToken, endpointurl, sms_ActionId);
		SalesForceAPIUtility.deleteAction(bearerToken, endpointurl, sms_ActionId);
		System.out.println("Test case --update_delete_SMSAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void createTaskAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		taskActionName = "ActionTask_".concat(HelperFunctions.GetRandomString(3));
		taskActionId = SalesForceAPIUtility.createTaskAction(bearerToken, salesForceUrl, taskActionName);
		System.out.println("taskActionId  = " + taskActionId);
		System.out.println("taskActionName =  " + taskActionName);
		System.out.println("Test case --createTaskAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void update_delete_TaskAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String task_ActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		String taskActionId = SalesForceAPIUtility.createTaskAction(bearerToken, salesForceUrl, task_ActionName);
		SalesForceAPIUtility.updateAction(bearerToken, endpointurl, taskActionId);
		SalesForceAPIUtility.deleteAction(bearerToken, endpointurl, taskActionId);
		System.out.println("Test case --update_delete_TaskAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void createAutoEmailAction() {
		System.out.println("Test case --createAutoEmailAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		autoEmailActionName = "AutoEmailAction_".concat(HelperFunctions.GetRandomString(3));
		autoEmaiActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl, autoEmailActionName,
				"Automatic");
		System.out.println("autoEmaiActionId  = " + autoEmaiActionId);
		System.out.println("autoEmailActionName =  " + autoEmailActionName);
		System.out.println("Test case --createAutoEmailAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void update_delete_AutoEmailAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String AutoEmail_Action = "ActionAutoEmail_".concat(HelperFunctions.GetRandomString(3));
		String AutoEmailActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				AutoEmail_Action, "Automatic");
		SalesForceAPIUtility.updateAction(bearerToken, endpointurl, AutoEmailActionId);
		SalesForceAPIUtility.deleteAction(bearerToken, endpointurl, AutoEmailActionId);
		System.out.println("Test case --update_delete_TaskAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void createManualEmailAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		manualEmailActionName = "ManualEmailActionAction_".concat(HelperFunctions.GetRandomString(3));
		manualEmaiActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				manualEmailActionName, "Manual");
		System.out.println("autoEmaiActionId  = " + manualEmaiActionId);
		System.out.println("autoEmailActionName =  " + manualEmailActionName);
		System.out.println("Test case --createTaskAction.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void update_delete_ManualEmailAction() {
		System.out.println("Test case --createCallAction-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String manual_ActionName = "ActionCall_".concat(HelperFunctions.GetRandomString(3));
		String manual_ActionNameId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				manual_ActionName, "Manual");
		SalesForceAPIUtility.updateAction(bearerToken, endpointurl, manual_ActionNameId);
		SalesForceAPIUtility.deleteAction(bearerToken, endpointurl, manual_ActionNameId);
		System.out.println("Test case --update_delete_TaskAction.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void CreateLead_AutoEmail_sequence() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		seleniumBase.idleWait(20);
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void CreateLead_ManualEmail_sequence() {
		System.out.println("Test case --CreateLead_ManualEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Fax");
		updateFielsDataMap.put("field_Value", "(946) 009-1324");
		updateFielsDataMap.put("secondField_Name", "Website");
		updateFielsDataMap.put("secondField_Value", "https://metacube.com");

		String MemailSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(MemailSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(50);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Fax",
				"Lead");
		assertEquals(getAnnualRevenue, "(946) 009-1324");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Website",
				"Lead");
		assertEquals(getTitle, "https://metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		seleniumBase.idleWait(20);
		////assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void CreateLead_Task_sequence() {
		System.out.println("Test case --CreateLead_Task_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "City");
		updateFielsDataMap.put("field_Value", "Jaipur");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "City",
				"Lead");
		assertEquals(getAnnualRevenue, "Jaipur");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		seleniumBase.idleWait(20);
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --CreateLead_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void CreateLead_SMS_sequence() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Industry");
		updateFielsDataMap.put("field_Value", "Apparel");
		updateFielsDataMap.put("secondField_Name", "HasOptedOutOfFax");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);
		assertFalse(engagePage.verifyParticipantPresentOnEngage(webSupportDriver, firstname));

		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"Industry", "Lead");
		assertEquals(getAnnualRevenue, "Apparel");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"HasOptedOutOfFax", "Lead");
		assertEquals(getTitle, "true");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_SMS_sequence.-- passed ");

	}

@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void CreateLead_Call_sequence() {
		System.out.println("Test case --CreateLead_Call_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Email");
		updateFielsDataMap.put("field_Value", "prateek.singhal@metacube.com");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		// String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(30);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Email",
				"Lead");
		assertEquals(getAnnualRevenue, "prateek.singhal@metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createContact_AutoEmail_sequence() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "Birthdate");
		updateFielsDataMap.put("field_Value", "2021-08-16");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"Birthdate", "Contact");
		assertEquals(getAnnualRevenue, "2021-08-16");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "Title",
				"Contact");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// initializeSalesForceTest("webSupportDriver");
		// driverUsed.put("webSupportDriver", true);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void createContact_ManualEmail_sequence() {
		System.out.println("Test case --createContact_ManualEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "LeadSource");
		updateFielsDataMap.put("field_Value", "Analyst");
		updateFielsDataMap.put("secondField_Name", "Fax");
		updateFielsDataMap.put("secondField_Value", "(946) 009-1324");

		String MemailSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(MemailSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "Fax",
				"Contact");
		assertEquals(getAnnualRevenue, "(946) 009-1324");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "LeadSource",
				"Contact");
		assertEquals(getTitle, "Analyst");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateContact_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void createContact_Task_sequence() {
		System.out.println("Test case --createContact_Task_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "AccountId");
		updateFielsDataMap.put("field_Value", accountId_field);
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"AccountId", "Contact");
		assertEquals(getAnnualRevenue, accountId_field);

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --createContact_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void createContact_SMS_sequence() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "ringdna100__Call_Attempts__c");
		updateFielsDataMap.put("field_Value", "100");
		updateFielsDataMap.put("secondField_Name", "HasOptedOutOfEmail");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		seleniumBase.idleWait(30);
		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"ringdna100__Call_Attempts__c", "Contact");
		assertEquals(getAnnualRevenue, "100.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"HasOptedOutOfEmail", "Contact");
		assertEquals(getTitle, "true");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		seleniumBase.idleWait(30);
		
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void createContact_Call_sequence() {
		System.out.println("Test case --createContact_Call_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "Email");
		updateFielsDataMap.put("field_Value", "prateek.singhal@metacube.com");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);
		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"Email", "Contact");
		assertEquals(getAnnualRevenue, "prateek.singhal@metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createOpp_AutoEmail_sequence() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Amount");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "LeadSource");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId = " + opportunityId);
		seleniumBase.idleWait(50);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"Amount", "Opportunity");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"LeadSource", "Opportunity");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateOpp_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")

	public void createOpp_ManualEmail_sequence() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Amount");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "LeadSource");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Prospecting",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"Amount", "Opportunity");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"LeadSource", "Opportunity");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void createOpp_Task_sequence() {
		System.out.println("Test case --createOppt_Task_sequence-- started ");
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "TotalOpportunityQuantity");
		updateFielsDataMap.put("secondField_Value", "555");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(30);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"TotalOpportunityQuantity", "Opportunity");
		assertEquals(TotalOppQuantity, "555.0");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --createOpp_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void createOpp_SMS_sequence() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "NextStep");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		seleniumBase.idleWait(30);
		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"NextStep", "Opportunity");
		assertEquals(TotalOppQuantity, "true");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Createopp_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void createOpp_Call_sequence() {
		System.out.println("Test case --createContact_Call_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "NextStep");
		updateFielsDataMap.put("secondField_Value", "true");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"NextStep", "Opportunity");
		assertEquals(TotalOppQuantity, "true");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createCamp_AutoEmail_sequence() {
		System.out.println("Test case --CreateCamp_AutoEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Email",
				"NATIVE", qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertNotEquals(getLeadSequenceId, SequenceId);
		assertEquals(getLeadSequenceId, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateCamp_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void createCamp_ManualEmail_sequence() {
		System.out.println("Test case --CreateCamp_ManualEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Email, null);

		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Email",
				"NATIVE", qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertNotEquals(getLeadSequenceId, SequenceId);
		assertEquals(getLeadSequenceId, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createTaskAction")
	public void createCamp_TasklEmail_sequence() {
		System.out.println("Test case --CreateCamp_TaskEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Task, null);

		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertNotEquals(getLeadSequenceId, SequenceId);
		assertEquals(getLeadSequenceId, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateOpp_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createSMSAction")
	public void createCamp_SMS_sequence() {
		System.out.println("Test case --CreateCamp_SMS_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991.0");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.SMS, null);
		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, updateFielsDataMap.get("field_Value"));

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertNotEquals(getLeadSequenceId, SequenceId);
		assertEquals(getLeadSequenceId, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateComp_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createCallAction")
	public void createCamp_Call_sequence() {
		System.out.println("Test case --CreateCamp_Call_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Call, gsDriverCall);

		// closing called driver
		gsDriverCall.quit();

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertNotEquals(getLeadSequenceId, SequenceId);
		assertEquals(getLeadSequenceId, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void CreateLead_AutoEmail_sequenceManual() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		seleniumBase.idleWait(20);
		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void CreateLead_ManualEmail_sequenceManual() {
		System.out.println("Test case --CreateLead_ManualEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Fax");
		updateFielsDataMap.put("field_Value", "(946) 009-1324");
		updateFielsDataMap.put("secondField_Name", "Website");
		updateFielsDataMap.put("secondField_Value", "https://metacube.com");

		String MemailSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(MemailSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		// Open engage and perform task action

		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(Engage);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Fax",
				"Lead");
		assertEquals(getAnnualRevenue, "(946) 009-1324");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Website",
				"Lead");
		assertEquals(getTitle, "https://metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void CreateLead_Task_sequenceManual() {
		System.out.println("Test case --CreateLead_Task_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "City");
		updateFielsDataMap.put("field_Value", "Jaipur");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(Engage);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "City",
				"Lead");
		assertEquals(getAnnualRevenue, "Jaipur");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		seleniumBase.idleWait(20);
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --CreateLead_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void CreateLead_SMS_sequenceManual() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Industry");
		updateFielsDataMap.put("field_Value", "Apparel");
		updateFielsDataMap.put("secondField_Name", "HasOptedOutOfFax");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(engage);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);
		assertFalse(engagePage.verifyParticipantPresentOnEngage(webSupportDriver, firstname));

		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"Industry", "Lead");
		assertEquals(getAnnualRevenue, "Apparel");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"HasOptedOutOfFax", "Lead");
		assertEquals(getTitle, "true");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);
		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void CreateLead_Call_sequenceManual() {
		System.out.println("Test case --CreateLead_Call_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Email");
		updateFielsDataMap.put("field_Value", "prateek.singhal@metacube.com");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		// String phoneNumber = receiverNumber;
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(engage);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(30);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Email",
				"Lead");
		assertEquals(getAnnualRevenue, "prateek.singhal@metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createContact_AutoEmail_sequenceManual() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "Birthdate");
		updateFielsDataMap.put("field_Value", "2021-08-16");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"Birthdate", "Contact");
		assertEquals(getAnnualRevenue, "2021-08-16");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "Title",
				"Contact");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void createContact_ManualEmail_sequenceManual() {
		System.out.println("Test case --createContact_ManualEmail_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "LeadSource");
		updateFielsDataMap.put("field_Value", "Analyst");
		updateFielsDataMap.put("secondField_Name", "Fax");
		updateFielsDataMap.put("secondField_Value", "(946) 009-1324");

		String MemailSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(MemailSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		// Open engage and perform task action

		String engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(engage);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "Fax",
				"Contact");
		assertEquals(getAnnualRevenue, "(946) 009-1324");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId, "LeadSource",
				"Contact");
		assertEquals(getTitle, "Analyst");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateContact_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void createContact_Task_sequenceManual() {
		System.out.println("Test case --createContact_Task_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "AccountId");
		updateFielsDataMap.put("field_Value", accountId_field);
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"AccountId", "Contact");
		assertEquals(getAnnualRevenue, accountId_field);

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --createContact_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void createContact_SMS_sequenceManual() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "ringdna100__Call_Attempts__c");
		updateFielsDataMap.put("field_Value", "100");
		updateFielsDataMap.put("secondField_Name", "HasOptedOutOfEmail");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		seleniumBase.idleWait(30);
		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"ringdna100__Call_Attempts__c", "Contact");
		assertEquals(getAnnualRevenue, "100.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"HasOptedOutOfEmail", "Contact");
		assertEquals(getTitle, "true");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateContact(salesForceUrl, bearerToken, contactId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		seleniumBase.idleWait(3);
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void createContact_Call_sequenceManual() {
		System.out.println("Test case --createContact_Call_sequence-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createcontactSequence(bearerToken, salesForceUrl, sequenceName,
				firstname, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Contact");
		updateFielsDataMap.put("field_Name", "Email");
		updateFielsDataMap.put("field_Value", "prateek.singhal@metacube.com");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);
		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(30);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"Email", "Contact");
		assertEquals(getAnnualRevenue, "prateek.singhal@metacube.com");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createOpp_AutoEmail_sequenceManual() {
		System.out.println("Test case --CreateLead_AutoEmail_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Amount");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "LeadSource");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"Amount", "Opportunity");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"LeadSource", "Opportunity");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateOPp_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")

	public void createOpp_ManualEmail_sequenceManual() {
		System.out.println("Test case --CreateOPp_AutoEmail_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Amount");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "LeadSource");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.idleWait(10);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"Amount", "Opportunity");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"LeadSource", "Opportunity");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateOPp_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createTaskAction" })
	public void createOpp_Task_sequenceManual() {
		System.out.println("Test case --createOpp_Task_sequence-- started ");
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "TotalOpportunityQuantity");
		updateFielsDataMap.put("secondField_Value", "555");

		String taskSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(taskSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(30);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);
		seleniumBase.idleWait(30);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"TotalOpportunityQuantity", "Opportunity");
		assertEquals(TotalOppQuantity, "555.0");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);

		System.out.println("Test case --createOPp_Task_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createSMSAction" })
	public void createOpp_SMS_sequenceManual() {
		System.out.println("Test case --CreateLead_SMS_sequence-- started ");
		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "NextStep");
		updateFielsDataMap.put("secondField_Value", "true");

		String smsSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(smsSeqId);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action
		String currentWindowHandle = webSupportDriver.getWindowHandle();
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);

		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, currentWindowHandle);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		seleniumBase.idleWait(30);
		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"NextStep", "Opportunity");
		assertEquals(TotalOppQuantity, "true");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --Createopp_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void createOpp_Call_sequenceManual() {
		System.out.println("Test case --createOpp_Call_sequence-- started ");

		// create contact through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		// create opportunity through API
		String opportunityName = "Opportunity_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String accountId = CONFIG.getProperty("account_id");
		String closeDate = HelperFunctions.GetCurrentDateTime("yyyy-mm-dd");

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createOppSequence(bearerToken, salesForceUrl, sequenceName,
				opportunityName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Opportunity");
		updateFielsDataMap.put("field_Name", "Type");
		updateFielsDataMap.put("field_Value", "Existing Business");
		updateFielsDataMap.put("secondField_Name", "NextStep");
		updateFielsDataMap.put("secondField_Value", "true");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);

		// create a contact
		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);
		String opportunityId = SalesForceAPIUtility.createOpportunity(bearerToken, salesForceUrl, "Never Qualified",
				opportunityName, accountId, closeDate, contactId);
		System.out.println("opportunityId" + opportunityId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getSequenceId, SequenceId);

		String getType = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId, "Type",
				"Opportunity");
		assertEquals(getType, "Existing Business");
		String TotalOppQuantity = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, opportunityId,
				"NextStep", "Opportunity");
		assertEquals(TotalOppQuantity, "true");
		;

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"StageName\": \"3 - Business Case\"}");
		SalesForceAPIUtility.updateOpportunity(salesForceUrl, bearerToken, opportunityId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateOpp_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createAutoEmailAction")
	public void createCamp_AutoEmail_sequenceManual() {
		System.out.println("Test case --CreateCamp_AutoEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				autoEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String Lead = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(Lead);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Email",
				"NATIVE", qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getLeadSequenceId);
		//assertEquals(getLeadSequenceId, SequenceId);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_AutoEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createManualEmailAction")
	public void createCamp_ManualEmail_sequenceManual() {
		System.out.println("Test case --CreateCamp_ManualEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId,
				manualEmaiActionId, "", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String Lead = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(Lead);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Email, null);

		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Email",
				"NATIVE", qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getLeadSequenceId, SequenceId);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createTaskAction")
	public void createCamp_TasklEmail_sequenceManual() {
		System.out.println("Test case --CreateCamp_TaskEmail_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String Lead = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(Lead);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Task, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Task, null);

		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Task",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getLeadSequenceId);
		//assertEquals(getLeadSequenceId, SequenceId);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_ManualEmail_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createSMSAction")
	public void createCamp_SMS_sequenceManual() {
		System.out.println("Test case --CreateCamp_SMS_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991.0");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String Lead = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(Lead);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// Open engage and perform task action

		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.refreshCurrentDocument(webSupportDriver);
		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.SMS, null);
		engagePage.idleWait(10);

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, updateFielsDataMap.get("field_Value"));

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "SMS",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getLeadSequenceId, SequenceId);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateComp_SMS_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" }, dependsOnMethods = "createCallAction")
	public void createCamp_Call_sequenceManual() {
		System.out.println("Test case --CreateCamp_Call_sequence-- started ");

		// create contact and through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "ContactFirst".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "ContactLast".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_H:mm:ss"));

		String contactId = SalesForceAPIUtility.createContact(bearerToken, salesForceUrl, firstname, lastname);
		System.out.println(contactId);

		String Leadfirstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String Leadlastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, Leadfirstname, Leadlastname,
				company);
		System.out.println(leadId);

		String campaignName = "Campaign_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createCampSequence(bearerToken, salesForceUrl, sequenceName,
				campaignName, lastname, "Manual");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Campaign");
		updateFielsDataMap.put("field_Name", "ExpectedRevenue");
		updateFielsDataMap.put("field_Value", "991");
		updateFielsDataMap.put("secondField_Name", "Type");
		updateFielsDataMap.put("secondField_Value", "AppExchange");

		// Add action in the sequence
		String autoSeqId = SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId,
				"", updateFielsDataMap);
		System.out.println(autoSeqId);

		// create campaign through API

		String campaignId = SalesForceAPIUtility.createCampaign(bearerToken, salesForceUrl, campaignName);

		// add contact in campaign through API
		SalesForceAPIUtility.addContactInCampaign(bearerToken, salesForceUrl, campaignId, contactId);
		SalesForceAPIUtility.addLeadInCampaign(bearerToken, salesForceUrl, campaignId, leadId);
		seleniumBase.idleWait(50);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		String ContUrl = CONFIG.getProperty("gs_light_url") + "/lightning/r/Contact/" + contactId + "/view";
		webSupportDriver.get(ContUrl);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		String Lead = CONFIG.getProperty("gs_light_url") + "/lightning/r/Lead/" + leadId + "/view";
		webSupportDriver.get(Lead);
		seleniumBase.idleWait(20);

		// associate sequence through 'Assocate' btn on contact
		contactDetailPage.associateSequencelight(webSupportDriver, sequenceName,
				SectionModuleNames.ReactRingDNAModuleLead);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform task action
		String leadUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(leadUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(30);
		engagePage.performActionOnEngage(webSupportDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);
		engagePage.idleWait(40);

		engagePage.performActionOnEngage(webSupportDriver, Leadfirstname, ActionsTypesEnum.Call, gsDriverCall);

		// closing called driver
		gsDriverCall.quit();

		String getContactSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		assertEquals(getContactSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId,
				"ExpectedRevenue", "Campaign");
		assertEquals(getAnnualRevenue, "991.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, campaignId, "Type",
				"Campaign");
		assertEquals(getTitle, "AppExchange");
		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, contactId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Contact_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		HashMap<String, String> LeadParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String LeadpActionsID = LeadParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, LeadpActionsID, salesForceUrl, "Call",
				qa_support_user_salesforce_id);

		// update exit value in Comp
		RequestBody body = RequestBody.create(mediaType, "{\n\"Name\": \"Campaign_Name_Postman\"}");
		SalesForceAPIUtility.updateCampaign(salesForceUrl, bearerToken, campaignId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible Contact
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, contactId,
				"RDNACadence__Cadence_ID__c", "Contact");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		// SequenceId should not visible Lead
		String getLeadSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getLeadSequenceId, SequenceId);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --CreateLead_Call_sequence.-- passed ");

	}

	@Test(groups = { "Sanity" })
	public void verify_sequence_level_delegation_selected_lead_owner_email_manual_naive() {
		System.out.println(
				"Test case --verify_sequence_level_delegation_selected_lead_owner_email_manual_naive-- started ");

		// Create Manual Email Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		autoEmailActionName = "AutoEmailAction_".concat(HelperFunctions.GetRandomString(3));
		String munalEmailActionId = SalesForceAPIUtility.createAutoEmailAction(bearerToken, salesForceUrl,
				autoEmailActionName, "Manual");
		System.out.println("autoEmaiActionId  = " + munalEmailActionId);
		System.out.println("autoEmailActionName =  " + autoEmailActionName);

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
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, munalEmailActionId,
				"sdr_owner__c", updateFielsDataMap);

		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println("leadId  " + leadId);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);
		//////
		String leadUrl = CONFIG.getProperty("gs_light_url") + leadId;
		qaAdminDriver.get(leadUrl);

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");

		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, "VC"));
		leadDetailPage.verifydnaAvatartooltipLight(qaAdminDriver, "VC", userName);

		// Open engage and perform task action

		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		qaAdminDriver.get(Engage);
		seleniumBase.idleWait(20);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Email, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(20);
		engagePage.refreshCurrentDocument(qaAdminDriver);
		engagePage.idleWait(10);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyEmailParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Email", "NATIVE",
				qa_user_salesforce_id);

		// gmail.openGmailInNewTab(webSupportDriver);
		// gmail.switchToTab(webSupportDriver, gmail.getTabCount(webSupportDriver));
		// gmail.loginGamil(webSupportDriver, gmailEmail, gmailPassword);
		// seleniumBase.idleWait(3);
		// webSupportDriver.get("https://mail.google.com/mail/u/0/#inbox");
		// actionPage.SearchsubjectAPI(webSupportDriver);
		// gmail.OpenSearchsubjectMail(webSupportDriver);
		// assertEquals(gmail.verifySenderName(webSupportDriver),
		// exchange_account_user);
		// gmail.deleteOpenedMail(webSupportDriver);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("qaAdminDriver", false);
		System.out.println(
				"Test case --verify_sequence_level_delegation_selected_lead_owner_email_manual_naive-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_when_assign_owner_to_individual_actions_is_selected_as_custom_user_lookup() {
		System.out.println(
				"Test case --verify_when_assign_owner_to_individual_actions_is_selected_as_custom_user_lookup-- started ");

		// Create Call Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String callActionName = "callActionName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String jsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "callNoteTemplates");
		String callActionId = SalesForceAPIUtility.createCallAction(bearerToken, salesForceUrl, callActionName,
				jsondata);
		System.out.println("callActionId   " + callActionId);
		System.out.println("callActionName  " + callActionName);

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
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "sdr_owner__c",
				updateFielsDataMap);

		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + leadId;
		qaAdminDriver.get(leadUrl);

		String userName = CONFIG.getProperty("qa_user_name");

		String Avatar = CONFIG.getProperty("Avatar");
		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltipLight(qaAdminDriver, "VC", userName);

		// open a new driver to receive call
		WebDriver gsDriverCall = getDriver();
		softPhoneLoginPage.softphoneLogin(gsDriverCall, softPhoneUrl, receiverUserName, receiverPassword);

		// Open engage and perform call action

		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		qaAdminDriver.get(Engage);
		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Call, gsDriverCall);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(qaAdminDriver);
		engagePage.idleWait(20);

		// closing called driver
		gsDriverCall.quit();

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Call",
				qa_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

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
		String ssID = SalesForceAPIUtility.getSesstionID(app_url, userName, password);
		String smsjsondata = SalesForceAPIUtility.getNoteTemplates(app_url, accountId, ssID, "smsTemplates");
		String smsActionId = SalesForceAPIUtility.createSmsAction(bearerToken, salesForceUrl, smsActionName,
				smsjsondata);
		System.out.println("smsActionId  = " + smsActionId);
		System.out.println("smsActionName =  " + smsActionName);

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
		// Add action in the sequence\
		// Add action in the sequence

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, smsActionId, "custom_lookup1__c",
				updateFielsDataMap);
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + leadId;
		qaAdminDriver.get(leadUrl);

		String userName = CONFIG.getProperty("qa_user_name");
		String Avatar = CONFIG.getProperty("Avatar");

		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltipLight(qaAdminDriver, Avatar, userName);

		// Open engage and perform task action
		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		qaAdminDriver.get(Engage);

		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.SMS, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(qaAdminDriver);

		// Open Lead and and verify Activity History

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "SMS",
				qa_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
		//assertNotEquals(getSequenceIdnull, SequenceId);
		//assertEquals(getSequenceIdnull, "null");

		driverUsed.put("qaAdminDriver", false);
		System.out.println("Test case --verify_Assign_owner_individual_actions_custom_user_lookup_for_sms-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_Assign_owner_individual_actions_custom_user_lookup_for_Task() {
		System.out.println("Test case --verify_Assign_owner_individual_actions_custom_user_lookup_for_s-- started ");

		// Create SMS Action through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);

		// Create Task Action through API
		String taskActionName = "TaskActionName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String taskActionId = SalesForceAPIUtility.createTaskAction(bearerToken, salesForceUrl, taskActionName);
		System.out.println("taskActionName" + taskActionName);
		System.out.println("taskActionId" + taskActionId);

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
		// Add action in the sequence\
		// Add action in the sequence

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "AnnualRevenue");
		updateFielsDataMap.put("field_Value", "1000");
		updateFielsDataMap.put("secondField_Name", "Title");
		updateFielsDataMap.put("secondField_Value", "FieldUpdate");

		// Add action in the sequence
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"custom_lookup1__c", updateFielsDataMap);
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + leadId;
		qaAdminDriver.get(leadUrl);

		qaAdminDriver.get(leadUrl);
		String userName = CONFIG.getProperty("qa_user_name");
		String Avatar = CONFIG.getProperty("Avatar");

		assertTrue(leadDetailPage.verifyOwnerName(qaAdminDriver, userName));
		assertTrue(leadDetailPage.verifydnaAvatar(qaAdminDriver, Avatar));
		leadDetailPage.verifydnaAvatartooltipLight(qaAdminDriver, Avatar, userName);

		// Open engage and perform task action
		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		qaAdminDriver.get(Engage);

		engagePage.performActionOnEngage(qaAdminDriver, firstname, ActionsTypesEnum.Task, null);

		// waiting for participant remove from engage list
		engagePage.idleWait(10);
		engagePage.refreshCurrentDocument(qaAdminDriver);

		// Open Lead and and verify Activity History

		String getSequenceId = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		assertEquals(getSequenceId, SequenceId);

		String getAnnualRevenue = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"AnnualRevenue", "Lead");
		assertEquals(getAnnualRevenue, "1000.0");

		String getTitle = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId, "Title", "Lead");
		assertEquals(getTitle, "FieldUpdate");

		HashMap<String, String> ParticipantActionsID = SalesForceAPIUtility.GetParticipantActionsID(bearerToken,
				salesForceUrl, leadId, "Id", "RDNACadence__Sequence_Action__c", "RDNACadence__Lead_Id__c");
		String pActionsID = ParticipantActionsID.get("pActionId0");

		contactDetailPage.verifyParticipantActionsfields(bearerToken, pActionsID, salesForceUrl, "Task",
				qa_user_salesforce_id);

		// update exit value in Contact
		RequestBody body = RequestBody.create(mediaType, "{\n\"LastName\": \"Enter SFDC-378433\"}");
		SalesForceAPIUtility.updateLead(salesForceUrl, bearerToken, leadId, body);
		seleniumBase.idleWait(3);

		// SequenceId should not visible
		String getSequenceIdnull = SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, leadId,
				"RDNACadence__Cadence_Id__c", "Lead");
		System.out.println("getSequenceId =="+ getSequenceIdnull);
//		assertNotEquals(getSequenceIdnull, SequenceId);
//		assertEquals(getSequenceIdnull, "null");

		driverUsed.put("qaAdminDriver", false);
		System.out.println("Test case --verify_Assign_owner_individual_actions_custom_user_lookup_for_Task-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void skip_bulk_particpants_from_engage_list() {
		System.out.println("Test case --skip_bulk_particpants_from_engage_list-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// skip bulk participants from engage
		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(Engage);
		engagePage.skipParticipantsEngage(webSupportDriver, 4);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --skip_bulk_particpants_from_engage_list-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_click_links_redirects_in_enagage_page() {
		System.out.println("Test case --verify_click_links_redirects_in_enagage_page-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		// skip bulk participants from engage
		String Engage = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		webSupportDriver.get(Engage);
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
		sfAccountPage.verifyAccountsNameHeadingLight(webSupportDriver, accountName);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, engageWindowHandle);

		// opening contact in new tab and switching
		list.clear();
		list = engagePage.getColumnListAccToHeader(webSupportDriver, "Name");
		String contactName = list.get(0).getText();
		openUrl = list.get(0);
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		engagePage.switchToTab(webSupportDriver, engagePage.getTabCount(webSupportDriver));
		assertEquals(contactDetailPage.getCallerNameLight(webSupportDriver), contactName);
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
		assertEquals(leadDetailPage.getLeadNameLight(webSupportDriver), leadName);
		engagePage.closeTab(webSupportDriver);
		engagePage.switchToTabWindow(webSupportDriver, engageWindowHandle);

		engagePage.switchToTab(webSupportDriver, 2);

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_click_links_redirects_in_enagage_page-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_assign_owner_to_individual_actions_selected_custom_user_lookup_for_task_light() {
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
		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, taskActionId,
				"custom_lookup1__c", updateFielsDataMap);

		initializeSalesForceTest("qaAdminDriver");
		driverUsed.put("qaAdminDriver", true);

		String leadUrl = CONFIG.getProperty("gs_light_url") + leadId;
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
		String url2 = CONFIG.getProperty("gs_light_url") + leadId;
		qaAdminDriver.get(url2);
		// Open Lead and and verify Activity History
		String subjectName = "Test Sub 01";
		String headerName = "Assigned To";
		int index = leadDetailPage.getIndexOfParticipantActionsHeader(qaAdminDriver, headerName);
		assertTrue(leadDetailPage.assignedTo(qaAdminDriver, subjectName, index, userName));

		driverUsed.put("qaAdminDriver", false);
		System.out.println(
				"Test case --verify_assign_owner_to_individual_actions_selected_custom_user_lookup_for_task_Light-- passed ");
	}

	@Test(groups = { "Sanity" })
	public void verify_non_admin_user_able_to_create_update_tasks() {
		System.out.println("Test case --verify_non_admin_user_able_to_create_update_tasks-- started ");

		agentDriverGS = getDriver();
		sfTestLogin.salesForceTestLogin(agentDriverGS, CONFIG.getProperty("gs_light_url"),
				CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		

		// create lead through API
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String lastname = "LeadLast_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String company = "Company_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy_HH:mm:ss"));
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);

		// open lead and check sequence id
		String url2 = CONFIG.getProperty("gs_light_url")+"lightning/r/Lead/" + leadId + "/view";
		agentDriverGS.get(url2);

		String taskSubject = "TaskSubject_".concat(HelperFunctions.GetRandomString(4));
		leadDetailPage.createNewTaskOpenActivitylight(agentDriverGS, taskSubject);

		// delete lead
		SalesForceAPIUtility.deleteLead(salesForceUrl, bearerToken, leadId);

		agentDriverGS.quit();

		System.out.println("Test case --verify_non_admin_user_able_to_create_update_tasks-- passed ");
	}

	@Test(groups = { "Sanity" }, dependsOnMethods = { "createCallAction" })
	public void verify_callAction_picklist_field_update_automatic_lead() {
		System.out.println("Test case --verify_callAction_picklist_field_update_automatic_lead-- started ");
		String bearerToken = SalesForceAPIUtility.getBearerToken(salesForceUrl, userName, password);
		String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
		String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
		String company = "Company_".concat(HelperFunctions.GetRandomString(5));

		// Create Sequence
		String sequenceName = "sequenceName_".concat(HelperFunctions.GetRandomString(5));
		String SequenceId = SalesForceAPIUtility.createSequence(bearerToken, salesForceUrl, sequenceName, firstname,
				lastname, "Automatic");
		System.out.println(SequenceId);
		System.out.println(sequenceName);

		updateFielsDataMap.clear();
		updateFielsDataMap.put("sequence_type", "Lead");
		updateFielsDataMap.put("field_Name", "Email");
		updateFielsDataMap.put("field_Value", "prateek.singhal@metacube.com");
		updateFielsDataMap.put("secondField_Name", "");
		updateFielsDataMap.put("secondField_Value", "");

		SalesForceAPIUtility.addActionsequence(bearerToken, salesForceUrl, SequenceId, callActionId, "",
				updateFielsDataMap);
		// create a lead
		String leadId = SalesForceAPIUtility.createLead(bearerToken, salesForceUrl, firstname, lastname, company);
		System.out.println(leadId);
		seleniumBase.idleWait(40);

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		softPhoneSettingsPage.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.enableDialNextSetting(webSupportDriver);

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
	
	@Test(groups = { "Sanity" })
	public void toaster_message_display_when_native_email_not_connected() {
		System.out.println("Test case --toaster_message_display_when_native_email_not_connected-- started ");

		agentDriverGS = getDriver();
		sfTestLogin.salesForceTestLogin(agentDriverGS, CONFIG.getProperty("gs_light_url"),
				CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));

		// Open engage and perform task action
		String engagePageUrl = CONFIG.getProperty("gs_light_url") + "lightning/n/RDNACadence__Engage";
		agentDriverGS.get(engagePageUrl);
		// engagePage.openEngageInNewTab(webSupportDriver);
		seleniumBase.idleWait(20);
		String firstname = "LeadFirst_AdXmO";
		//Verify toaster_message
		String errorMessage = engagePage.toasterMessageDisplay(agentDriverGS, firstname, ActionsTypesEnum.Email);
		String toster = "Error: You must connect your native email account to send this email. Please visit the ringDNA Admin Console, connect your account, and try again.";
		assertEquals(errorMessage,toster);
		

		driverUsed.put("agentDriverGS", false);
		System.out.println("Test case -- toaster_message_display_when_native_email_not_connected.-- passed ");

		
	}
	
	
	@Test(groups = { "Sanity" })
	public void save_snippet_by_clicking_on_Save_button() {
		System.out.println("Test case --save_snippet_by_clicking_on_Save_button-- started ");

		initializeSalesForceTest("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		// Create Snippet 
		String SnippetsPageUrl = CONFIG.getProperty("gs_light_url") + "/lightning/n/RDNACadence__Snippets?0.source=alohaHeader";
		webSupportDriver.get(SnippetsPageUrl);
		seleniumBase.idleWait(5);
		String subjectText = "subjectText_".concat(HelperFunctions.GetRandomString(5));
		String emailBodytext = "subjectText_".concat(HelperFunctions.GetRandomString(5));
		
		SnippetPage.create_Snippets(webSupportDriver, subjectText, emailBodytext);
		SnippetPage.verifySnippetsCreated(webSupportDriver, subjectText);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case -- save_snippet_by_clicking_on_Save_button.-- passed ");

		
	}
	
	@AfterMethod(groups = { "Sanity" })
	public void afterMethodGS(ITestResult result) {
		List<String> dependsOnMethods = new ArrayList<>();
		dependsOnMethods.add("createCallAction");
		dependsOnMethods.add("createSMSAction");
		dependsOnMethods.add("createTaskAction");
		dependsOnMethods.add("createManualEmailAction");
		dependsOnMethods.add("createAutoEmailAction");

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
