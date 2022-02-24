package support.cases.accountCases;

import static org.testng.Assert.assertFalse;

import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneMessagePage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftPhoneWebLeadsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewEvent;
import softphone.source.callTools.SoftPhoneNewTask;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.commonpages.Dashboard;
import support.source.teams.GroupsPage;
import utility.HelperFunctions;

public class IntelligentDialerTabCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AccountIntelligentDialerTab intelligentDialerTab = new AccountIntelligentDialerTab();
	SoftphoneBase softPhoneBase = new SoftphoneBase();
	SoftphoneCallHistoryPage softphoneCallHistory = new SoftphoneCallHistoryPage();
	SoftPhoneSettingsPage softphoneSettings	= new SoftPhoneSettingsPage();
	SoftPhoneMessagePage softPhoneMsging	= new SoftPhoneMessagePage();
	SoftPhoneNewEvent newEvent = new SoftPhoneNewEvent();
	SoftPhoneNewTask newTask = new SoftPhoneNewTask();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneContactsPage softPhoneContactsPage = new SoftPhoneContactsPage();
	HelperFunctions helperFunctions = new HelperFunctions();
	GroupsPage groupsPage = new GroupsPage();
	SoftPhoneWebLeadsPage webLeadsPage = new SoftPhoneWebLeadsPage();
	AccountSalesforceTab salesforce = new AccountSalesforceTab();
	
	private String leadFirstName;
	private String contactAccountName;
	private String randomGroup;
	private String numberToDial;
	private String qa_user_account;
	
	@BeforeClass(groups = { "Regression", "MediumPriority"})
	public void beforeClass() {
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			numberToDial = CONFIG.getProperty("qa_support_user_number");
			qa_user_account = "";
		} else if (SupportBase.drivername.toString().equals("supportDriver")) {
			numberToDial = CONFIG.getProperty("qa_admin_user_number");
			qa_user_account = CONFIG.getProperty("qa_user_account");
		}
		leadFirstName = CONFIG.getProperty("lead_first_name");
		contactAccountName = CONFIG.getProperty("contact_account_name");
		randomGroup = CONFIG.getProperty("automation_random_group");
	}
	
	@Test(groups = { "Regression" })
	public void add_update_delete_custom_link() {
		System.out.println("Test case --add_update_delete_custom_link-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		// creating custom link
		String customLinkName = CONFIG.getProperty("softphone_custom_link").concat(HelperFunctions.GetRandomString(3));
		String customLinkUrl = "https://www.google".concat(HelperFunctions.GetRandomString(3)).concat(".com/");
		System.out.println("Starting creating custom link");
		intelligentDialerTab.createCustomLink(supportDriver, customLinkName, customLinkUrl);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		
		//Adding if contact not there
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(supportDriver);
		callToolsPanel.idleWait(1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		if(callScreenPage.isCallerUnkonwn(supportDriver) ) {
			softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
			callScreenPage.addCallerAsLead(supportDriver, leadFirstName, contactAccountName);
		}
		callToolsPanel.enterCustomLink(supportDriver, customLinkName);
		callToolsPanel.selectCustomLinkByText(supportDriver, customLinkName);
		intelligentDialerTab.switchToTab(supportDriver, intelligentDialerTab.getTabCount(supportDriver));
		assertTrue(supportDriver.getCurrentUrl().equalsIgnoreCase(customLinkUrl), String.format("Custom link record: %s does not exists after creating", customLinkName));
		intelligentDialerTab.closeTab(supportDriver);
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// updating custom link
		System.out.println("Starting updating custom link");
		String newcustomLinkName = CONFIG.getProperty("softphone_custom_link").concat(HelperFunctions.GetRandomString(3));
		String newcustomLinkUrl = "https://www.google".concat(HelperFunctions.GetRandomString(3)).concat(".com/");
		intelligentDialerTab.updateRecords(supportDriver, customLinkName, newcustomLinkName, newcustomLinkUrl);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callToolsPanel.enterCustomLink(supportDriver, newcustomLinkName);
		callToolsPanel.selectCustomLinkByText(supportDriver, newcustomLinkName);
		intelligentDialerTab.switchToTab(supportDriver, intelligentDialerTab.getTabCount(supportDriver));
		assertTrue(supportDriver.getCurrentUrl().equalsIgnoreCase(newcustomLinkUrl), String.format("Custom link record: %s does not exists after updating", newcustomLinkName));
		intelligentDialerTab.closeTab(supportDriver);
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// deleting custom link
		System.out.println("Starting deleting custom link");
		intelligentDialerTab.deleteRecords(supportDriver, newcustomLinkName);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callToolsPanel.clickCustomLinkIcon(supportDriver);
		boolean recordExists = callToolsPanel.checkCustomLinkExists(supportDriver, newcustomLinkName);
		assertFalse(recordExists, String.format("Custom link record: %s exists even after deleting", newcustomLinkName));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_custom_link-- passed ");
	}

	@Test(groups = { "Regression" })
	public void add_update_delete_event_subject() {
		System.out.println("Test case --add_update_delete_event_subject-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		// creating event subject
		System.out.println("Starting creating event subject");
		String eventName = "AutoEventSubject".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.createEventSubject(supportDriver, eventName);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		
		//Adding if contact not there
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(supportDriver);
		callToolsPanel.idleWait(1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
			callScreenPage.addCallerAsLead(supportDriver, leadFirstName, contactAccountName);
		}
		
		boolean eventExists = newEvent.checkEventSubjectExists(supportDriver, eventName);
		assertTrue(eventExists, String.format("Event Subject: %s does not exists after creating", eventName));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// updating event subject
		System.out.println("Starting updating event subject");
		String eventNameUpdated = "AutoEventSubjectUpdate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.updateEventSubjectRecords(supportDriver, eventName, eventNameUpdated);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = newEvent.checkEventSubjectExists(supportDriver, eventNameUpdated);
		assertTrue(eventExists, String.format("Event Subject: %s does not exists after updating", eventNameUpdated));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		//deleting event subject
		System.out.println("Starting deleting event subject");
		intelligentDialerTab.deleteRecords(supportDriver, eventNameUpdated);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = newEvent.checkEventSubjectExists(supportDriver, eventNameUpdated);
		assertFalse(eventExists, String.format("Event Subject record: %s exists even after deleting", eventNameUpdated));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_event_subject-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void add_update_delete_task_note_subject() {
		System.out.println("Test case --add_update_delete_task_note_subject-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// creating task note subject
		System.out.println("Starting creating task note subject");
		String taskNoteName = "AutoTaskNote".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.createTask_NoteSubject(supportDriver, taskNoteName);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);

		// Adding if contact not there
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(supportDriver);
		callToolsPanel.idleWait(1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
			callScreenPage.addCallerAsLead(supportDriver, leadFirstName, contactAccountName);
		}
		
		boolean eventExists = newTask.checkTask_NoteSubjectExistsInTasksTab(supportDriver, taskNoteName);
		assertTrue(eventExists, String.format("Task Note Subject: %s does not exists after creating in tasks tab", taskNoteName));
		
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = callToolsPanel.checkTask_NoteSubjectExistsInCallNotes(supportDriver, taskNoteName);
		assertTrue(eventExists, String.format("Task Note Subject: %s does not exists after creating in call notes section", taskNoteName));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// updating task note subject
		System.out.println("Starting updating task note subject");
		String taskNoteNameUpdate = "AutoTaskNoteUpdate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.updateEventSubjectRecords(supportDriver, taskNoteName, taskNoteNameUpdate);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = newTask.checkTask_NoteSubjectExistsInTasksTab(supportDriver, taskNoteNameUpdate);
		assertTrue(eventExists, String.format("Task Note Subject: %s does not exists after updating in tasks tab", taskNoteNameUpdate));
		
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = callToolsPanel.checkTask_NoteSubjectExistsInCallNotes(supportDriver, taskNoteNameUpdate);
		assertTrue(eventExists, String.format("Task Note Subject: %s does not exists after creating in call notes section", taskNoteNameUpdate));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// deleting task note subject
		System.out.println("Starting deleting task note subject");
		intelligentDialerTab.deleteRecords(supportDriver, taskNoteNameUpdate);
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = newTask.checkTask_NoteSubjectExistsInTasksTab(supportDriver, taskNoteNameUpdate);
		assertFalse(eventExists, String.format("Task Note Subject record: %s exists even after deleting in tasks tab", taskNoteNameUpdate));
		
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		eventExists = callToolsPanel.checkTask_NoteSubjectExistsInCallNotes(supportDriver, taskNoteNameUpdate);
		assertFalse(eventExists, String.format("Task Note Subject record: %s exists even after deleting in call notes section", taskNoteNameUpdate));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_task_note_subject-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void add_update_delete_call_notes_templates() {
		System.out.println("Test case --add_update_delete_call_notes_templates-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// creating call notes templates with default All Group
		System.out.println("Starting creating call notes templates");
		String callNoteName = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		String callNoteTemplate = "AutoCallTemplate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.enableCallNotesTemplateSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		intelligentDialerTab.createCallNotesTemplate(supportDriver, callNoteName, callNoteTemplate, null);
		boolean callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteName);
		assertTrue(callNoteExists, String.format("Call Notes Templates: %s does not exists after creating", callNoteName));
		
		//softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		
		// Adding if contact not there
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(supportDriver);
		callToolsPanel.idleWait(1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			callScreenPage.addCallerAsLead(supportDriver, leadFirstName, contactAccountName);
		}
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		boolean callNoteTemplateExists = callToolsPanel.checkCallNotesExistsInTemplatePicker(supportDriver, callNoteName);
		assertTrue(callNoteTemplateExists, String.format("Call Note: %s does not exists after creating ", callNoteName));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// updating call notes templates
		System.out.println("Starting updating call notes templates");
		String callNoteNameUpdate = "AutoCallNotesUpdate".concat(HelperFunctions.GetRandomString(3));
		String callNoteTemplateUpdate = "AutoCallTemplateUpdate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.updateCallNotesTemplateRecords(supportDriver, callNoteName, callNoteNameUpdate, callNoteTemplate, callNoteTemplateUpdate);
		callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteNameUpdate);
		assertTrue(callNoteExists, String.format("Call Notes Templates: %s does not exists after updating", callNoteNameUpdate));
		
		// deleting call notes templates
		System.out.println("Starting deleting call notes templates");
		intelligentDialerTab.deleteRecords(supportDriver, callNoteNameUpdate);
		callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteNameUpdate);
		assertFalse(callNoteExists, String.format("Call Notes Templates: %s exists after deleting", callNoteNameUpdate));
		
		// creating call notes templates with selecting random group
		System.out.println("Starting creating call notes templates");
		callNoteName = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		callNoteTemplate = "AutoCallTemplate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.enableCallNotesTemplateSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		intelligentDialerTab.createCallNotesTemplate(supportDriver, callNoteName, callNoteTemplate, randomGroup);
		callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteName);
		assertTrue(callNoteExists, String.format("Call Notes Templates: %s does not exists after creating", callNoteName));
		
		// softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callNoteTemplateExists = callToolsPanel.checkCallNotesExistsInTemplatePicker(supportDriver,	callNoteName);
		assertFalse(callNoteTemplateExists, String.format("Call Note: %s exists for group:%s ", callNoteName, randomGroup));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// deleting call notes templates
		System.out.println("Starting deleting call notes templates");
		intelligentDialerTab.deleteRecords(supportDriver, callNoteName);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_call_notes_templates-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void add_update_delete_voicemail_drop_by_uploading_file() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --add_update_delete_voicemail_drop_by_uploading_file-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//adding voicemail drop by uploading file
		System.out.println("Starting adding voicemail drop");
		String voiceMailName = "AutoVoiceMail".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		boolean voiceMailPlayable = intelligentDialerTab.checkVoiceMailIsPlayable(supportDriver, voiceMailName);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after creating", voiceMailName));
		
		//softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
		softphoneSettings.verifyGlobalVoicemaildropOnSoftphone(supportDriver, voiceMailName);
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		//adding voicemail drop by recording file
		String voiceMailRecord = "AutoVoiceMailRecord".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.createVoiceMail(supportDriver, voiceMailRecord, 5);
		voiceMailPlayable = intelligentDialerTab.checkVoiceMailIsPlayable(supportDriver, voiceMailRecord);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after creating", voiceMailRecord));
		
		// softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailRecord);
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		//updating voicemail drop
		System.out.println("Starting updating voicemail drop");
		String voiceMailNameUpdate = "AutoVoiceMailUpdate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.updateVoiceMailRecordsByUploadingFile(supportDriver, voiceMailName, voiceMailNameUpdate);
		voiceMailPlayable = intelligentDialerTab.checkVoiceMailIsPlayable(supportDriver, voiceMailNameUpdate);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after updating", voiceMailNameUpdate));
		
		// softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailNameUpdate);
		softphoneSettings.verifyGlobalVoicemaildropOnSoftphone(supportDriver, voiceMailNameUpdate);
		intelligentDialerTab.switchToTab(supportDriver, 2);
				
		//deleting voicemail drop
		System.out.println("Starting deleting voicemail drop");
		intelligentDialerTab.deleteRecords(supportDriver, voiceMailNameUpdate);
		voiceMailPlayable = intelligentDialerTab.checkVoiceMailIsPlayable(supportDriver, voiceMailNameUpdate);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailNameUpdate));
		
		// deleting voicemail drop recording
		intelligentDialerTab.deleteRecords(supportDriver, voiceMailRecord);
		voiceMailPlayable = intelligentDialerTab.checkVoiceMailIsPlayable(supportDriver, voiceMailRecord);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailRecord));
		
		// softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailNameUpdate));
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailRecord));
		intelligentDialerTab.switchToTab(supportDriver, 2);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_voicemail_drop_by_uploading_file-- passed ");
	}
	
	@Test(groups = { "Regression"})
	public void add_update_delete_call_scripts() {
		System.out.println("Test case --add_update_delete_call_scripts-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// adding call script
		System.out.println("Starting adding call scripts");
		String callScriptName = "AutoCallScriptName".concat(HelperFunctions.GetRandomString(3));
		String callScriptDescription = "AutoCallScriptDescr".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.createCallScripts(supportDriver, callScriptName, callScriptDescription, null);
		boolean callScriptExists = intelligentDialerTab.checkCallScriptsSaved(supportDriver, callScriptName);
		assertTrue(callScriptExists, String.format("Call Script: %s does not exists after creating", callScriptName));
		
		// updating call script
		System.out.println("Starting updating call scripts");
		String callScriptNameUpdate = "AutoCallScriptNameUpdate".concat(HelperFunctions.GetRandomString(3));
		String callScriptDescriptionUpdate = "AutoCallScriptDescrUpdate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.updateCallScriptsRecords(supportDriver, callScriptName, callScriptNameUpdate, callScriptDescriptionUpdate);
		callScriptExists = intelligentDialerTab.checkCallScriptsSaved(supportDriver, callScriptNameUpdate);
		assertTrue(callScriptExists, String.format("Call Script: %s does not exists after updating", callScriptNameUpdate));
		
		//deleting call script
		System.out.println("Starting deleting call scripts");
		intelligentDialerTab.deleteRecords(supportDriver, callScriptNameUpdate);
		callScriptExists = intelligentDialerTab.checkCallScriptsSaved(supportDriver, callScriptNameUpdate);
		assertFalse(callScriptExists, String.format("Call Script: %s exists even after deleting", callScriptNameUpdate));
	
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_call_scripts-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_call_disposition_not_visible_when_setting_disabled() {
		System.out.println("Test case --verify_call_disposition_not_visible_when_setting_disabled-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//enabling setting and verifying details
		intelligentDialerTab.disableManageCallDispositions(supportDriver);
		intelligentDialerTab.verifyCallDispositionSectionNotVisible(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_disposition_not_visible_when_setting_disabled-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void manage_call_dispositions_functionality() {
		System.out.println("Test case --manage_call_dispositions_functionality-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		String callDisposition = "AutoCallDisposition".concat(HelperFunctions.GetRandomString(2));
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		//enabling setting and verifying details
		intelligentDialerTab.enableManageCallDispositions(supportDriver);
		intelligentDialerTab.isCallDispositionSectionVisible(supportDriver);
		
		//adding and verifying call disposition
		intelligentDialerTab.addCallDisposition(supportDriver, callDisposition);
		assertTrue(intelligentDialerTab.isCallDispositionExists(supportDriver, callDisposition));

		//Verify added call disposition for New Group Call Disposition
        groupsPage.openAddNewGroupPage(supportDriver);
        String groupDetailName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(3));
        String groupDetailDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(3));
        groupsPage.addNewGroupDetails(supportDriver, groupDetailName, groupDetailDesc);
        assertTrue(intelligentDialerTab.isCallDispositionExists(supportDriver, callDisposition));
        groupsPage.deleteGroup(supportDriver);
        
        //Verify added call disposition for Existing Group Call Disposition
        groupsPage.openGroupSearchPage(supportDriver);
        groupsPage.searchGroups(supportDriver, CONFIG.getProperty("new_qa_automation_team"), qa_user_account);
        groupsPage.clickFirstGroup(supportDriver);
        assertTrue(intelligentDialerTab.isCallDispositionExists(supportDriver, callDisposition));
        
		//deleting and verifying call disposition
        dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		intelligentDialerTab.deleteCallDisposition(supportDriver, callDisposition);
		assertFalse(intelligentDialerTab.isCallDispositionExists(supportDriver, callDisposition));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --manage_call_dispositions_functionality-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void add_delete_related_records() {
		System.out.println("Test case --add_delete_related_records-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// Adding related records and getting index
		intelligentDialerTab.addRelatedRecordRulesIcon(supportDriver);
		int index = intelligentDialerTab.selectMachType_Query(supportDriver, "Contact", "Search:Case-All");
		
		//deleting records with index
		intelligentDialerTab.deleteRelatedRecords(supportDriver, index);
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_records-- passed ");
	}
	
	@Test(groups = {"AdminOnly", "MediumPriority"})
	public void verify_no_msg_section_for_admin_when_disabled_from_support() {
		System.out.println("Test case --verify_no_msg_section_for_admin_when_disabled_from_support-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(webSupportDriver);
		intelligentDialerTab.openIntelligentDialerTab(webSupportDriver);

		//disabling msg feature
		intelligentDialerTab.disableMessagingSetting(webSupportDriver);
		intelligentDialerTab.saveAcccountSettings(webSupportDriver);
		
		// updating the adminDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
	
		//verifying msg section not visible
		assertFalse(intelligentDialerTab.isMsgSectionVisible(supportDriver));
		
		//enabling msg feature
		intelligentDialerTab.enableMessagingSetting(webSupportDriver);
		intelligentDialerTab.saveAcccountSettings(webSupportDriver);
		
		//verifying msg section visible
		intelligentDialerTab.refreshCurrentDocument(supportDriver);
		assertTrue(intelligentDialerTab.isMsgSectionVisible(supportDriver));
		intelligentDialerTab.verifySMSMSgDefaultOff(supportDriver);
		
		// verifying msg icon not visible on softphone
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		assertFalse(softPhoneMsging.isMsgIconVisible(supportDriver));
		
		//enabling sms setting
		intelligentDialerTab.switchToTab(supportDriver, 2);
		intelligentDialerTab.enableSMSSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		//verifying msg icon visible on softphone
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		assertTrue(softPhoneMsging.isMsgIconVisible(supportDriver));
		
		driverUsed.put("supportDriver", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_no_msg_section_for_admin_when_disabled_from_support-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_uncheck_call_notes_template_disables_on_softphone() {
		System.out.println("Test case --verify_uncheck_call_notes_template_disables_on_softphone-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);

		// creating call notes templates with default All Group
		System.out.println("Starting creating call notes templates");
		String callNoteName = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		String callNoteTemplate = "AutoCallTemplate".concat(HelperFunctions.GetRandomString(3));
		intelligentDialerTab.enableCallNotesTemplateSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		intelligentDialerTab.createCallNotesTemplate(supportDriver, callNoteName, callNoteTemplate, "");
		boolean callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteName);
		assertTrue(callNoteExists, String.format("Call Notes Templates: %s does not exists after creating", callNoteName));
		
		//softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		
		// Adding if contact not there
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(supportDriver);
		callToolsPanel.idleWait(1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			callScreenPage.addCallerAsLead(supportDriver, leadFirstName, contactAccountName);
		}
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		boolean callNoteTemplateExists = callToolsPanel.checkCallNotesExistsInTemplatePicker(supportDriver, callNoteName);
		assertTrue(callNoteTemplateExists, String.format("Call Note: %s does not exists after creating ", callNoteName));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// uncheck call notes templates box
		System.out.println("check box call notes");
		intelligentDialerTab.checkBoxFilter(supportDriver, callNoteName);
		
		// softphone verification
		intelligentDialerTab.switchToTab(supportDriver, 1);
		softPhoneBase.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		callNoteTemplateExists = callToolsPanel.checkCallNotesExistsInTemplatePicker(supportDriver,	callNoteName);
		assertFalse(callNoteTemplateExists, String.format("Call Note: %s exists for group:%s ", callNoteName, randomGroup));
		intelligentDialerTab.switchToTab(supportDriver, 2);
		
		// deleting call notes templates
		System.out.println("Starting deleting call notes templates");
		intelligentDialerTab.deleteRecords(supportDriver, callNoteName);
		callNoteExists = intelligentDialerTab.checkCallNotesTemplateSaved(supportDriver, callNoteName);
		assertFalse(callNoteExists, String.format("Call Notes Templates: %s exists after deleting", callNoteName));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_uncheck_call_notes_template_disables_on_softphone-- passed ");
	}
	
	//Check visibility of Call Task fields on dialer after ON / OFF
	@Test(groups = { "Regression"})
	public void check_visiblity_of_call_task_on_dialer_after_on_off() {
		// updating the adminDriver used
		System.out.println("Test case --check_visiblity_of_call_task_on_dialer_after_on_off-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		//enable Call task features
		intelligentDialerTab.enableCallTasksTypeSetting(supportDriver);
		intelligentDialerTab.enableCallTasksPrioritySetting(supportDriver);
		intelligentDialerTab.enableCallNotesRelatedRecordSetting(supportDriver);
		intelligentDialerTab.enableCallTasksReminderSetting(supportDriver);
		intelligentDialerTab.enableCallTasksDueDateSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		//open new task window
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		newTask.clickTaskIcon(supportDriver);
		
		//verify Call task features disabled
		newTask.verifyCallTasksFeaturesDisabledInSoftphone(supportDriver);
		
		//set default settings
		intelligentDialerTab.switchToTab(supportDriver, 2);
		intelligentDialerTab.disableCallTasksDueDateSetting(supportDriver);
		intelligentDialerTab.disableCallTasksTypeSetting(supportDriver);
		intelligentDialerTab.disableCallTasksReminderSetting(supportDriver);
		intelligentDialerTab.disableCallTasksPrioritySetting(supportDriver);
		intelligentDialerTab.disableCallNotesRelatedRecordSetting(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --check_visiblity_of_call_task_on_dialer_after_on_off-- passed ");
	}
	
	//Enable / Disable Features and verify on softphone
	@Test(groups = { "Regression"})
	public void on_off_features_on_id_and_verify_on_softphone() {
		// updating the adminDriver used
		System.out.println("Test case --check_visiblity_of_call_task_on_dialer_after_on_off-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		//disable features
		intelligentDialerTab.disableWebLeadsSetting(supportDriver);
		intelligentDialerTab.disableCallToolsSetting(supportDriver);
		intelligentDialerTab.disableCallNotesTemplateSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		//open new task window
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		assertFalse(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		assertFalse(callToolsPanel.isTemplatePickerVisible(supportDriver));
		assertFalse(callToolsPanel.isCallToolsIconVisible(supportDriver));
		
		//set default settings
		intelligentDialerTab.switchToTab(supportDriver, 2);
		//enable features
		intelligentDialerTab.enableWebLeadsSetting(supportDriver);
		intelligentDialerTab.enableCallNotesTemplateSetting(supportDriver);
		intelligentDialerTab.enableCallToolsSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --check_visiblity_of_call_task_on_dialer_after_on_off-- passed ");
	}
	
	//Verify field visible only Support user
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_id_settings_visible_for_support() {
		// updating the adminDriver used
		System.out.println("Test case --verify_id_settings_visible_for_support-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		intelligentDialerTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//verify id settings for support role
		intelligentDialerTab.verifyIDSettingsVisibleForSupport(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_id_settings_visible_for_support-- passed ");
	}
	
	//Verify field visible only Admin user
	@Test(groups = { "Regression", "AdminOnly"})
	public void verify_id_settings_not_visible_for_admin() {
		// updating the adminDriver used
		System.out.println("Test case --verify_id_settings_not_visible_for_admin-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		intelligentDialerTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//verify id settings not visible for admin role
		intelligentDialerTab.verifyIDSettingsNotVisibleForAdmin(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_id_settings_not_visible_for_admin-- passed ");
	}
	
	//Verify warning dialogue box on Call Queue setting
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_warning_dialogue_box_on_call_queue_setting() {
		// updating the adminDriver used
		System.out.println("Test case --verify_warning_dialogue_box_on_call_queue_setting-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		intelligentDialerTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//enable
		intelligentDialerTab.enableCallQueueSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		supportDriver.navigate().refresh();
		
		//disable
		intelligentDialerTab.disableCallQueueSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		supportDriver.navigate().refresh();
		
		//enable
		intelligentDialerTab.enableCallQueueSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_warning_dialogue_box_on_call_queue_setting-- passed ");
	}
	
	//Toggle Settings-OFF, Verified on Softphone
	//Toggle Settings-ON, Verified on Softphone
	@Test(groups = { "Regression", "SupportOnly"})
	public void on_off_toggle_settings_and_verify_on_softphone() {
		// updating the adminDriver used
		System.out.println("Test case --on_off_toggle_settings_and_verify_on_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickAccountsLink(supportDriver);
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		//enable features
		intelligentDialerTab.enableWebLeadsSetting(supportDriver);
		intelligentDialerTab.enableClickToVoicemailSetting(supportDriver);
		intelligentDialerTab.enableEmailButtonSetting(supportDriver);
		intelligentDialerTab.enableCallForwardingSetting(supportDriver);
		intelligentDialerTab.enableMessagingSetting(webSupportDriver);
		intelligentDialerTab.enableCallNotifications(supportDriver);
		intelligentDialerTab.enableStayConnectedSetting(supportDriver);
		intelligentDialerTab.enableSMSSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		//open new task window
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		assertTrue(softPhoneMsging.isMsgIconVisible(supportDriver));
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		assertTrue(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		assertTrue(callToolsPanel.isTemplatePickerVisible(supportDriver));
		assertTrue(callToolsPanel.isCallToolsIconVisible(supportDriver));
		
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		//call forwarding
		softPhoneSettingsPage.verifyCallForwardingIsVisible(supportDriver);
		//continous bridge
		softPhoneSettingsPage.verifyStayConnectedIsVisible(supportDriver);
		
		//set disable settings
		intelligentDialerTab.switchToTab(supportDriver, 2);
		intelligentDialerTab.disableMessagingSetting(webSupportDriver);
		intelligentDialerTab.disableWebLeadsSetting(supportDriver);
		intelligentDialerTab.disableClickToVoicemailSetting(supportDriver);
		intelligentDialerTab.disableCallForwardingSetting(supportDriver);
		intelligentDialerTab.disableCallNotifications(supportDriver);
		intelligentDialerTab.disableStayConnectedSetting(supportDriver);
		intelligentDialerTab.disableSMSSetting(supportDriver);
		intelligentDialerTab.saveAcccountSettings(supportDriver);
		
		//open new task window
		intelligentDialerTab.switchToTab(supportDriver, 1);
		intelligentDialerTab.reloadSoftphone(supportDriver);
		assertFalse(softPhoneMsging.isMsgIconVisible(supportDriver));
		softphoneCallHistory.openRecentContactCallHistoryEntry(supportDriver);
		assertFalse(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		assertFalse(callToolsPanel.isTemplatePickerVisible(supportDriver));
		assertFalse(callToolsPanel.isCallToolsIconVisible(supportDriver));

		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		// call forwarding
		softPhoneSettingsPage.verifyCallForwardingIsInvisible(supportDriver);
		// continous bridge
		softPhoneSettingsPage.verifyStayConnectedIsInvisible(supportDriver);
		softPhoneSettingsPage.verifyCallNotificationIsInvisible(driver1);
		
		//set default settings
		intelligentDialerTab.switchToTab(supportDriver, 2);
		intelligentDialerTab.SetDefaultIntelligentDialerSettings(supportDriver, "30", "1", "30");
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --on_off_toggle_settings_and_verify_on_softphone-- passed ");
	}
	
	//Verify availability of Moved assorted Account>Overview tab settings to Account>Intelligent Dialer tab
	@Test(groups = { "Regression" })
	public void verify_availability_of_oved_assorted_overview_tab_to_intelligent_dialer_tab() {
		System.out.println("Test case --verify_availability_of_moved_assorted_overview_tab_to_intelligent_dialer_tab-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// open account
		dashboard.clickAccountsLink(supportDriver);
		
		//verify items not on overview
		intelligentDialerTab.verifySettingNotVisibleOnOverview(supportDriver);
		
		// open id tab
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		//verify items on id tab
		intelligentDialerTab.verifySettingVisibleOnIDTab(supportDriver);
		
		//verify id tool tips
		intelligentDialerTab.verifyToolTipWebLeadsLock(supportDriver);
		intelligentDialerTab.verifyToolTipWebLeadsUnLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipCallFrwrdingLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipDisableOfflineFrwrdingLock(supportDriver);
		intelligentDialerTab.verifyToolTipDisableOfflineFrwrdingUnLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipCallForwardingPromptLock(supportDriver);
		intelligentDialerTab.verifyToolTipCallForwardingPromptUnLock(supportDriver);
		
		intelligentDialerTab.verifyToolTipUnavailableFlowLock(supportDriver);
		intelligentDialerTab.verifyToolTipUnavailableFlowUnLock(supportDriver);
		
		intelligentDialerTab.verifyAllIntelligentDialerTabToolTip(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify (Support role) availability of Move assorted Account>Salesforce>Settings tab settings to Account>Intelligent Dialer tab
	@Test(groups = { "Regression","SupportOnly"})
	public void verify_availability_of_moved_assorted_salesforce_tab_to_id_tab_for_support() {
		System.out.println("Test case --verify_availability_of_moved_assorted_salesforce_tab_to_id_tab_for_support-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// open account
		dashboard.clickAccountsLink(supportDriver);

		// open salesforce tab
		salesforce.openSalesforceTab(supportDriver);
		//verify items not visible on salesforce
		intelligentDialerTab.verifySettingNotVisibleOnSalesforceTab(supportDriver);
		
		// open id tab
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		//verify items on id tab
		intelligentDialerTab.verifySettingVisibleOnIDTabForSupport(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify (ADMIN t role) availability of Move assorted Account>Salesforce>Settings tab settings to Account>Intelligent Dialer tab
	@Test(groups = { "Regression","AdminOnly"})
	public void verify_availability_of_moved_assorted_salesforce_tab_to_id_tab_for_admin() {
		System.out.println("Test case --verify_availability_of_moved_assorted_salesforce_tab_to_id_tab_for_admin-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		// open account
		dashboard.clickAccountsLink(supportDriver);

		// open salesforce tab
		salesforce.openSalesforceTab(supportDriver);
		//verify items not visible on salesforce
		intelligentDialerTab.verifySettingNotVisibleOnSalesforceTab(supportDriver);
		
		// open id tab
		intelligentDialerTab.openIntelligentDialerTab(supportDriver);
		//verify items on id tab
		intelligentDialerTab.verifySettingVisibleOnIDTabForAdmin(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = { "AdminOnly", "MediumPriority" })
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "verify_no_msg_section_for_admin_when_disabled_from_support":

				// enabling msg feature
				initializeSupport("webSupportDriver");
				driverUsed.put("webSupportDriver", true);
				
				dashboard.switchToTab(webSupportDriver, 2);
				dashboard.clickAccountsLink(webSupportDriver);
				intelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
				intelligentDialerTab.enableMessagingSetting(webSupportDriver);
				intelligentDialerTab.saveAcccountSettings(webSupportDriver);

				// Opening Intelligent Dialer Tab
				dashboard.switchToTab(supportDriver, 2);
				dashboard.clickAccountsLink(supportDriver);
				intelligentDialerTab.openIntelligentDialerTab(supportDriver);

				// enabling sms setting
				intelligentDialerTab.enableSMSSetting(supportDriver);
				intelligentDialerTab.saveAcccountSettings(supportDriver);
				
				driverUsed.put("webSupportDriver", false);
				break;
			}
		}
	}
}
