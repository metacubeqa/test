package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.internal.collections.Pair;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.base.SupportBase;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class AccountIntelligentDialerTab extends SeleniumBase{
	
Dashboard dashBoard = new Dashboard();
AddSmartNumberPage addSmartNumberPage = new AddSmartNumberPage();
	
	By intelligentDialerTab 		= By.cssSelector("[data-tab='intelligent-dialer']");
	By intelligentDialerTabParent 	= By.xpath(".//*[@data-tab='intelligent-dialer']/ancestor::li");
	By intelligentDialerTabHeading 	= By.xpath("//h2[text()='Features']");
	By msgProvisionSection			= By.cssSelector(".message-provisioning");
	By modalTitle					= By.cssSelector(".modal-title");
	By confirmLockBtn				= By.cssSelector("[data-bb-handler ='ok'].btn.locked");
	
	By acctWideOptBtn				= By.xpath(".//*[contains(@class,'messagingWideOptOut')]/parent::div");
	By messagingWideOptOutCheckBox 	= By.cssSelector(".messagingWideOptOut");
	By enableMsgWideOptBtn		   	= By.xpath("//input[contains(@class,'messagingWideOptOut')]/..//label[contains(@class,'toggle-on')]");
	By disableMsgWideOptBtn		   	= By.xpath("//input[contains(@class,'messagingWideOptOut')]/..//label[contains(@class,'toggle-off')]");
	
	By callQueue                    = By.xpath("//label [text() = 'Call Queue ']");
	By callQueueCheckBox 			= By.className("callQueue");
	By callQueueToggleButton 		= By.xpath("//input[contains(@class, 'callQueue')]/..//label[contains(@class,'toggle-off')]");
	
	By callRatingCheckBox 			= By.className("callRating");
	By callRatingToggleBox 			= By.xpath("//input[contains(@class, 'callRating')]/..//label[contains(@class,'toggle-off')]");
	
	By enableRelatedRecordsCheckBox 	= By.cssSelector("input.relatedRecordsEnabled");
	By enableRelatedRecordsToggleButton = By.xpath("//input[contains(@class, 'relatedRecordsEnabled')]/..//label[contains(@class,'toggle-off')]");
	
	By generalSettings              = By.xpath("//label[text() = 'General Settings ']");
	By generalSettingCheckBox 		= By.className("generalSettings");
	By generalSettingToggleButton 	= By.xpath("//input[contains(@class, 'generalSettings')]/..//label[contains(@class,'toggle-off')]");
	
	By chatterBoxCheckBox 			= By.className("chatterBox");
	By chatterBoxToggleButton 		= By.xpath("//input[contains(@class, 'chatterBox')]/..//label[contains(@class,'toggle-off')]");
	
	By leadStatusCheckBox			= By.className("leadStatus");
	By leadStatusToggleButton 		= By.xpath("//input[contains(@class,'leadStatus')]/..//label[contains(@class,'toggle-off')]");
	
	By newTaskEvent                 = By.xpath("//label[text() = 'New Task / Event ']");
	By newTaskCheckBox 				= By.className("newTask");
	By newTaskToggleButton 			= By.xpath("//input[contains(@class,'newTask')]/..//label[contains(@class,'toggle-off')]");
	By newTaskToggleOnButton 		= By.xpath("//input[contains(@class,'newTask')]/..//label[contains(@class,'toggle-on')]");
	
	By enableMessagingButton 		= By.xpath("//div[@class='message-provisioning']//button[text()='Enable Messaging']");
	By disableMessagingButton 		= By.xpath("//div[@class='message-provisioning']//button[text()='Disable Messaging']");
	By accountMsgEnabledText		= By.xpath(".//*[@class='message-provisioning']//pre[@class='results']//p[normalize-space(text()) = 'This account now has messaging']/b[text()='enabled']");
	By accountMsgDisabledText		= By.xpath(".//*[@class='message-provisioning']//pre[@class='results']//p[normalize-space(text()) = 'This account now has messaging']/b[text()='disabled']");
	
	By smsMsgDefaultBtn				= By.cssSelector(".message-provisioning .toggle-group label.active");
	By smsCheckBox 					= By.cssSelector(".toggle-switch.messaging");
	By smsToggleOffButton 			= By.xpath("//input[contains(@class, 'messaging')]/..//label[contains(@class,'toggle-off')]");
	By smsToggleOnButton 			= By.xpath("//input[contains(@class, 'messaging')]/..//label[contains(@class,'toggle-on')]");
	
	By smsAgreementCheckBox 		= By.cssSelector(".sms-messaging-agreement-modal [type='checkbox']");
	By smsAgreementButton 			= By.cssSelector("button.btn-success.sms-messaging-confirm");
	
	By smsTemplateCheckBox 			= By.className("smsTemplatesEnabled");
	By smsTemplateONButton 			= By.xpath("//input[contains(@class, 'smsTemplatesEnabled')]/..//label[contains(@class,'toggle-on')]");
	By smsTemplateOffButton 		= By.xpath("//input[contains(@class, 'smsTemplatesEnabled')]/..//label[contains(@class,'toggle-off')]");
	
	By enableCallsToolsCheckBox 	= By.className("callToolsMenu");
	By enableCallsToolsToggleButton = By.xpath("//input[contains(@class,'callToolsMenu')]/..//label[contains(@class,'toggle-on')]");
	By enableCallToolsToggleOffBtn	= By.xpath("//input[contains(@class,'callToolsMenu')]/..//label[contains(@class,'toggle-off')]");
	
	By hideGroupCallHistory                 = By.xpath("//label[text() = 'Hide Queue Call History']");
	By hideGroupCallHistoryCheckBox 	    = By.className("hideQueueCalls");
	By hideGroupCallHistoryToggleOnButton   = By.xpath("//input[contains(@class,'hideQueueCalls')]/..//label[contains(@class,'toggle-on')]");
	By hideGroupCallHistoryToggleOffButton  = By.xpath("//input[contains(@class,'hideQueueCalls')]/..//label[contains(@class,'toggle-off')]");
	
	By callDispositionPromptCheckBox 		= By.className("callDispositionPrompt");
	By callDispositionPromptToggleOnBtn 	= By.xpath("//input[contains(@class,'callDispositionPrompt')]/..//label[contains(@class,'toggle-on')]");
	By callDispositionPromptToggleOffBtn 	= By.xpath("//input[contains(@class,'callDispositionPrompt')]/..//label[contains(@class,'toggle-off')]");
	
	By AutomaticMultiMatchRadioBtn 			= By.cssSelector(".sfdcMultiMatchSetting[value='Automatically']");
	By manualMultiMatchRadioBtn 			= By.cssSelector(".sfdcMultiMatchSetting[value='Manually']");
	By noMultiMatchRadioBtn 				= By.cssSelector(".sfdcMultiMatchSetting[value='None']");
	
	By callDispositionRequiredDropDown 		= By.cssSelector(".callDispositionRequiredState");
	By manageByTeamCheckBox 				= By.cssSelector(".manageCallDispositionRequiredByTeam");
	By confirmManageByTeamBtn 				= By.xpath(".//*[text()='Disable Manage By Team']");
	
	By callDispostionList 					= By.cssSelector(".call-dispositions-container td.disposition");
	By callDispositionChangeDate			= By.cssSelector(".callDispositionRequiredDate");
	
	By emailButton				            = By.xpath("//label[text() = 'Email Button ']");
	By emailButtonCheckBox 					= By.className("hideEmailButton");
	By emailButtonToggleButton 				= By.xpath("//input[contains(@class,'hideEmailButton')]/..//label[contains(@class,'toggle-off')]");
	
	By taskDueDateRequiredCheckbox 			= By.className("dueDateRequired");
	By taskDueDateRequiredToggleOnButton 	= By.xpath("//input[contains(@class,'dueDateRequired')]/..//label[contains(@class,'toggle-on')]");
	By taskDueDateRequiredToggleOffButton 	= By.xpath("//input[contains(@class,'dueDateRequired')]/..//label[contains(@class,'toggle-off')]");
	
	By taskSubjectRequredCheckBox 			= By.className("sfdcSubjectRequired");
	By taskSubjectReqToggleOnBtn			= By.xpath("//input[contains(@class,'sfdcSubjectRequired')]/..//label[contains(@class,'toggle-on')]");
	By taskSubjectReqToggleOffBtn			= By.xpath("//input[contains(@class,'sfdcSubjectRequired')]/..//label[contains(@class,'toggle-off')]");
	
	By callTasksDueDateCheckBox 			= By.className("dueDate");
	By callTasksDueDateToggleButton 		= By.xpath("//input[contains(@class,'dueDate') and not(contains(@class,'dueDateRequired'))]/..//label[contains(@class,'toggle-on')]");
	By callTasksDueDateToggleOffButton 		= By.xpath("//input[contains(@class,'dueDate') and not(contains(@class,'dueDateRequired'))]/..//label[contains(@class,'toggle-off')]");
	
	By callTasksTypeCheckBox 				= By.className("type");
	By callTasksTypeToggleButton 			= By.xpath("//input[contains(@class,'type')]/..//label[contains(@class,'toggle-on')]");
	By callTasksTypeToggleOffButton         = By.xpath("//input[contains(@class,'type')]/..//label[contains(@class,'toggle-off')]");

	By callTasksReminderCheckBox 			= By.className("reminder");
	By callTasksReminderToggleButton 		= By.xpath("//input[contains(@class,'reminder')]/..//label[contains(@class,'toggle-on')]");
	By callTasksReminderToggleOffButton 		= By.xpath("//input[contains(@class,'reminder')]/..//label[contains(@class,'toggle-off')]");
	
	By callTasksPriorityCheckBox 			= By.className("priority");
	By callTasksPriorityToggleButton 		= By.xpath("//input[contains(@class,'priority')]/..//label[contains(@class,'toggle-on')]");
	By callTasksPriorityToggleOffButton 		= By.xpath("//input[contains(@class,'priority')]/..//label[contains(@class,'toggle-off')]");

	By callTasksRelatedRecordCheckBox 		= By.className("relatedRecords");
	By callTasksRelatedRecordToggleButton 	= By.xpath("//input[@class='relatedRecords']/..//label[contains(@class,'toggle-on')]");
	By callTasksRelatedRecordToggleOffButton= By.xpath("//input[@class='relatedRecords']/..//label[contains(@class,'toggle-off')]");
	
	By coldTransferCheckbox 				= By.className("appendTransferInfo");
	By coldTransferToggleOnButton 			= By.xpath("//input[contains(@class,'appendTransferInfo')]/..//label[contains(@class,'toggle-on')]");
	By coldTransferToggleOffButton 			= By.xpath("//input[contains(@class,'appendTransferInfo')]/..//label[contains(@class,'toggle-off')]");
	
	By vmTranscriptionCheckbox 				= By.className("voicemailTranscriptionEnabled");
	By vmTranscriptionToggleOnButton 		= By.xpath("//input[contains(@class,'voicemailTranscriptionEnabled')]/..//label[contains(@class,'toggle-on')]");
	By vmTranscriptionTogglOffButton 		= By.xpath("//input[contains(@class,'voicemailTranscriptionEnabled')]/..//label[contains(@class,'toggle-off')]");
	
	By vmTranscriptionGranularDisabled		= By.xpath(".//*[@class ='voiceTranscriptionByCountry']/parent::div[@disabled=\"disabled\"]");
	
	By saveAccountsSettingButton 			= By.cssSelector("button.save");
	By saveAccountsSettingMessage 			= By.className("toast-message");

	//Call Disposition Selectors
	By manageCallDispositionCheckBox	  	= By.className("manageCallDispositions");
	By manageCallDispositionsToggleOnBtn  	= By.xpath("//input[contains(@class,'manageCallDispositions')]/..//label[contains(@class,'toggle-on')]");
	By manageCallDispositionsToggleOffBtn 	= By.xpath("//input[contains(@class,'manageCallDispositions')]/..//label[contains(@class,'toggle-off')]");
	By importCallDispositionDataCheckbox  	= By.cssSelector(".import-data");
	By acceptCheckBox					  	= By.cssSelector(".accept");
	By acceptBtn						  	= By.cssSelector(".btn-success.accept");
	By callDispositionSection			  	= By.cssSelector(".call-dispositions-container");
	By callDispositionTable				  	= By.cssSelector(".disposition");	 
	By synctToSalesForceBtn				  	= By.cssSelector(".sync-call-dispositions");
	By addCallDispositionIcon			  	= By.cssSelector(".call-dispositions-container .glyphicon-plus-sign");
	By callDispositionTextBox			  	= By.cssSelector(".form-control.disposition");
	By addCallDispositionBtn			  	= By.cssSelector(".call-disposition-editor .btn-success.persist");
	
	String callDispositionPage			  	= ".//*[text()='$callDisposition$']/ancestor::tr//td[contains(@class,'disposition')]";
	String callDispositionDeletePage	  	= ".//*[text()='$callDisposition$']/ancestor::tr//td/a[@class='delete']/span";
	
	//Web Leads Section
	By webLeadsCheckbox 			= By.className("webLeadsEnabled");
	By webLeadsToggleOnButton 		= By.xpath("//input[contains(@class,'webLeadsEnabled')]/..//label[contains(@class,'toggle-on')]");
	By webLeadsToggleOffButton 		= By.xpath("//input[contains(@class,'webLeadsEnabled')]/..//label[contains(@class,'toggle-off')]");
	By webLeadsLockBtn				= By.cssSelector(".webLeadsEnabledLocked img");
	By webLeadsUnlockActive			= By.xpath(".//*[@class='webLeadsEnabledLocked']//img[contains(@src,'/unlock-active.svg')]");
	By webLeadsLockActive			= By.xpath(".//*[@class='webLeadsEnabledLocked']//img[contains(@src,'/lock-active.svg')]");
	By webLeadsFieldNameDrpDwn		= By.cssSelector(".web-leads-mapping div.field-name .selectize-input");
	By webLeadsFieldNameTxtBox		= By.cssSelector(".web-leads-mapping div.field-name .selectize-input input");
	By webLeadsFieldNameOptions		= By.cssSelector(".web-leads-mapping div.field-name .selectize-dropdown-content .option");
	By webLeadsFieldValue			= By.cssSelector(".web-leads-mapping input.field-value");
	By webLeadsTeamsDrpDwn			= By.cssSelector(".web-leads-mapping div.teams .selectize-input");
	By webLeadsTeamsOptions			= By.cssSelector(".web-leads-mapping div.teams .selectize-dropdown-content .option");
	By webLeadsTeamsTxtBox			= By.cssSelector(".web-leads-mapping div.teams .selectize-input input");
	By webLeadsAddRuleBtn			= By.cssSelector(".web-leads-mapping button.add");
	By webLeadsRuleRow				= By.cssSelector(".web-leads-container table tbody tr");
	By webLeadNameCell				= By.cssSelector(".wl-name-cell");
	By webLeadValueCell				= By.cssSelector(".wl-value-cell");
	By webLeadTeamCell				= By.cssSelector(".wl-team-cell");
	By deleteButton					= By.className("delete");
	By webLeadVisibilityDropDown	= By.cssSelector("select.recordVisibility");
	
	//Compliance Hours
	By complainceHoursCheckbox 		= By.className("compliance-hours-enabled");
	By complainceHoursToggleOnBtn	= By.xpath("//input[contains(@class,'compliance-hours-enabled')]/..//label[contains(@class,'toggle-on')]");
	By complainceHoursToggleOffBtn 	= By.xpath("//input[contains(@class,'compliance-hours-enabled')]/..//label[contains(@class,'toggle-off')]");
	By complainceHourDaysHeaders	= By.cssSelector(".compliance-hours-grid thead a");
	By complainceHourDaysRow		= By.cssSelector(".compliance-hours-grid tbody tr");
	By dayNameColumn				= By.xpath("td[1]");
	By dayRestrictionColumn			= By.xpath("td[2]");
	By updateButton					= By.cssSelector("[data-action='update']");
	
	By compHourStartTimeBox			= By.className("start-time");
	By compHourEndTimeBox			= By.className("end-time");
	By compHourAllDayCheckBox		= By.cssSelector("input.all-day");
	By modalWindowSaveButton		= By.cssSelector("button.persist");
	By compHourCloseButton			= By.cssSelector("button.close");
	
	//Custom User Status
	By customStatusCheckBox			= By.className("custom-user-status-enabled");
	By customStatusToggleOnBtn		= By.xpath("//input[contains(@class,'custom-user-status-enabled')]/..//label[contains(@class,'toggle-on')]");
	By customStatusToggleOffBtn 	= By.xpath("//input[contains(@class,'custom-user-status-enabled')]/..//label[contains(@class,'toggle-off')]");
	By customstatusDescription		= By.cssSelector(".custom-user-status-body p");
	
	By allowUsersOverrideLabel		= By.xpath(".//*[@class='custom-user-status-body']//label[text() = 'Allow Users to Override Time ']") ;
	By allowUsersCheckBox			= By.className("custom-user-status-time-override");
	By allowUsersToggleOnBtn		= By.xpath("//input[contains(@class,'custom-user-status-time-override')]/..//label[contains(@class,'toggle-on')]");
	By allowUsersToggleOffBtn 		= By.xpath("//input[contains(@class,'custom-user-status-time-override')]/..//label[contains(@class,'toggle-off')]");
	
	By customStatusRow				= By.cssSelector("[class*='custom-user-status'] tbody tr");
	By customStatusNameColumn		= By.cssSelector("td.name");
	By customStatusBusyColumn		= By.xpath("td[2]");
	By customStatusTimeColumn		= By.xpath("td[3]");
	By customStatusDescrColumn		= By.cssSelector("td.description");
	By customStatusTeamColumn		= By.xpath("td[5]/a");
	By customStatusUpdateBtn		= By.cssSelector("a.update");
	By customStatusDeleteBtn		= By.cssSelector("a.delete");
	
	By addCustomStatusBtn			= By.cssSelector(".add[data-target='#user-status-modal']");
	
	By customStatusNameModal		= By.cssSelector("#user-status-modal .name");
	By customStatusBusyModal		= By.cssSelector("#user-status-modal .busy");
	By customStatusDescModal		= By.cssSelector("#user-status-modal .description");
	By customStatusTimingModal		= By.cssSelector("#user-status-modal .defaultExpiration");
	
	By customStatusTeamDropdown		= By.cssSelector("#user-status-modal .teams-picker .selectize-control");
	By customStatusTeamTextBox		= By.cssSelector("#user-status-modal .teams-picker .selectize-input input");
	By customStatusTeamOption		= By.cssSelector("#user-status-modal .teams-picker .selectize-dropdown-content .option");
	
	By deleteRecordConfirmBtn		= By.cssSelector(".delete-ok");
	
	//Voice Mail Drop Selectors
	By addVoiceMailButton 			= By.cssSelector("[class^='voicemail-drop'] .glyphicon-plus-sign");
	By newRecordingLink 			= By.className("record-voicemail");
	By chooseFileRecordingLink		= By.cssSelector(".voicemail-modals-container .glyphicon-folder-open");
	By inputLabelTab				= By.cssSelector(".voicemail-modals-container .file-label");
  	By voiceMailNameInput			= By.cssSelector(".voicemail-label input");
  	By recordVoiceMailButton 		= By.cssSelector("button.record-voicemail");
  	By stopVoceMailRecordButton 	= By.cssSelector("button.stop-recording-voicemail");
  	By voiceMailPlayButton			= By.cssSelector(".voicemail-modals-container .glyphicon-play");
  	By uploadFilePlayIcon			= By.cssSelector(".voicemail-modals-container .glyphicon-play-circle");
  	By saveVoiceMailButton 			= By.cssSelector(".voicemail-modals-container  button.save-record");
  	String playVoiceMailDrop		= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[contains(@class,'btn btn-success play')]";
  	String updateVoiceMailDrop		= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[@class='btn btn-primary update-voicemail']";
	String stopVoiceMailDrop		= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[@class='btn btn-danger stop']";
	
	//auto disposition vm drop
	By autoDispVmDopHeading			= By.xpath(".//h2[text()='Auto Disposition Voicemail Drops']");
	By autoDispVmSubHeading			= By.xpath(".//div[h2[text()='Auto Disposition Voicemail Drops']]/following-sibling::p");
	By autoDispVmDropDownLabel		= By.xpath(".//div[h2[text()='Auto Disposition Voicemail Drops']]/following-sibling::div//label");
	By autoDispVmDropDown			= By.xpath(".//label[text()='Select Disposition']/following-sibling::div//div[contains(@class, 'selectize-input')]");
	By autoDispVmDropDownOptions	= By.xpath(".//label[text()='Select Disposition']/following-sibling::div//div[@class='selectize-dropdown-content']/div[contains(@class ,'option')]");
	By taskAndNotesHeading			= By.xpath(".//*[text()='Task & Note']");
	
   //Custom Link Selectors
  	By addCustomLinkBtn				= By.cssSelector(".call-custom-links-container .glyphicon-plus-sign");
  	By customLinkNameInput			= By.cssSelector(".call-custom-links-editor .name");
  	By customLinkURLInput			= By.cssSelector(".call-custom-links-editor .url");
  	By saveCustomLinkBtn			= By.cssSelector(".call-custom-links-editor .btn-success");
  	
  	String deleteRecordBtn			= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//a[@class='delete']";
  	By confirmDeleteBtn				= By.cssSelector("[data-bb-handler='confirm']");
  
    //Event Subject Selectors
  	By addEventSubjectBtn			= By.cssSelector(".event-subjects-container .glyphicon-plus-sign");
  	By eventSubjectNameInput  		= By.cssSelector(".subject-editor .subject");
  	By saveEventSubjectBtn			= By.cssSelector(".subject-editor .btn-success");

    //Task and Note Subject Selectors
  	By addTaskNoteSubjectBtn		= By.cssSelector(".task-note-subjects-container .glyphicon-plus-sign");
  	
  	//custom user status
  	By customUserStatusCheckbox		 = By.cssSelector(".custom-user-status-enabled");
  	By customUserStatusToggleOnBtn 	 = By.xpath("//input[@class='custom-user-status-enabled']/..//label[contains(@class,'toggle-on')]");
  	By customUserStatusToggleOffBtn  = By.xpath("//input[@class='custom-user-status-enabled']/..//label[contains(@class,'toggle-off')]");
  	
  	//Call Notes Selectors
  	By callNotesCheckBox			= By.className("callNoteTemplatesEnabled");
  	By callNotesToggleOnBtn 		= By.xpath("//input[contains(@class,'callNoteTemplatesEnabled')]/..//label[contains(@class,'toggle-on')]");
  	By callNotesToggleOffBtn 		= By.xpath("//input[contains(@class,'callNoteTemplatesEnabled')]/..//label[contains(@class,'toggle-off')]");
  	By addCallNotesTemplateBtn		= By.cssSelector(".call-note-templates-container .glyphicon-plus-sign");
  	By callNotesNameInput			= By.cssSelector(".call-note-templates-editor .note-name");
  	By callNotesTemplateInput		= By.cssSelector(".call-note-templates-editor .note-template");
  	By callNotesGroupsTab			= By.cssSelector(".teams-picker .selectize-input input");
  	By callNotesGroupsList			= By.cssSelector(".teams-picker .selectize-dropdown-content .option");
  	By saveCallNoteBtn				= By.cssSelector(".call-note-templates-editor .btn-success");
  	
  	//SMS templates
  	By addSMSTemplateBtn			= By.cssSelector(".sms-templates-container .glyphicon-plus-sign");
  	By sMSTemplateNameTxtBox		= By.cssSelector(".sms-templates-editor .template-name");
  	By sMSTemplateDesciption		= By.cssSelector(".sms-templates-editor .template-content");
  	By sMSTemplateGroupDrpDwn		= By.cssSelector(".sms-templates-editor .selectize-input");
  	By sMSTemplateGroupTxtBox		= By.cssSelector(".sms-templates-editor .selectize-input input");
  	By sMSTemplateGroupOptions		= By.cssSelector(".sms-templates-editor .selectize-dropdown-content .option");
  	By smsTemplateSaveBtn			= By.cssSelector(".sms-templates-editor .btn-success");
  	
  	//Call SCripts Selectors
  	By addCallScriptsBtn			= By.cssSelector(".call-scripts-container .glyphicon-plus-sign");
  	By callScriptsSearchTab			= By.cssSelector("input[placeholder='Search'][type='search']");
  	By callScriptNameInput			= By.cssSelector(".modals-container .name");
  	By callScriptDescription		= By.cssSelector(".modals-container .description");
  	By callScriptSmartNumberList	= By.cssSelector(".modals-container .backgrid input");
  	By saveCallScriptsBtn			= By.cssSelector(".modals-container .btn-success");
  	By nextCallScriptsBtn			= By.cssSelector(".btn-success.stepNext");
  	
  	String updateRecordBtn			= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[@class='btn btn-primary update-btn']";
  	String checkBoxRecordSelect		= ".//*[text()='$recordName$']/ancestor::tr//td/input[@type='checkbox']";
  	
  	//Related Records Selectors
  	By addRulesIcon 				= By.cssSelector(".add-rule span");
  	By deleteRulesIcon				= By.cssSelector(".btn-danger.delete");
  	By restoreRuleIcon				= By.cssSelector(".btn-info.restore:not([style*='display'])");
  	By matchTypeSelect				= By.cssSelector(".record-type");
	By querySelect 					= By.cssSelector(".query");
	
	// -------- Central Number ----------- //
	By centralNoHeading = By.xpath("//h2[text()='Central Numbers']");
	By centralNoAddIcon = By.cssSelector(".central-numbers .add");
	By centralNoNumberList = By.xpath("//div[@class='central-numbers']//a[contains(@href,'#smart-numbers')]");
	String centralNumberName = ".//*[@class='central-numbers']//following-sibling::td/a[text()='$NUMBER$']";
	String centralNumberDelete = ".//*[text()='$NUMBER$']//ancestor::tr//a[@class='delete']/span[contains(@class,'remove-sign')]";
	
	//live queue
	By liveQueuesHeading            = By.xpath("//h2[text() = 'Live Queues']");
	String liveQueueDelBtn 			= ".//*[@class='live-queue']//td[text()='$$QueueName$$']/following-sibling::td/a[@class='delete']";
	String liveQueueCurrentSize		= ".//*[@class='live-queue']//td[text()='$queueName$']/following-sibling::td[contains(@class,'currentSize')]";
	
	// s3
	By s3RecordingInput			  = By.cssSelector(".s3RecordingBucket.input-sm");
	By s3RecordingCheckBox		  = By.cssSelector(".s3RecordingBucketConfirmation.toggle-switch");
	By s3RecordingToggleOffButton = By.xpath("//input[contains(@class,'s3RecordingBucket')]/..//label[contains(@class,'toggle-off')]");
	By s3RecordingToggleOnButton  = By.xpath("//input[contains(@class,'s3RecordingBucket')]/..//label[contains(@class,'toggle-on')]");
	
	//sip client
	By sipClient                  = By.xpath("//label[text() = 'SIP Client']");
	By sipClientCheckBox 		  = By.cssSelector("input.sipClient");
	By sipClientToggleOffButton   = By.xpath("//input[contains(@class,'sipClient')]/..//label[contains(@class,'toggle-off')]");
	By sipClientToggleOnButton 	  = By.xpath("//input[contains(@class,'sipClient')]/..//label[contains(@class,'toggle-on')]");
	
	// web RTC
	By webRTC                       = By.xpath("//label[text() = 'WebRTC ']");
	By webRTCCheckbox 				= By.cssSelector("input.webRTC");
	By webRTCToggleButton 			= By.xpath("//input[contains(@class,'webRTC')]//parent::*//*[contains(@class,'toggle-off')]");
	By webRTCToggleOnButton 		= By.xpath("//input[contains(@class,'webRTC')]//parent::*//*[contains(@class,'toggle-on')]");
	
	// stay connected
	By stayConnected                = By.xpath("//label[text() = 'Stay Connected ']");
	By stayConnectedCheckBox 		= By.cssSelector("input.continuousBridge");
	By stayConnectedToggleOffButton = By.xpath("//input[contains(@class,'continuousBridge')]/..//label[contains(@class,'toggle-off')]");
	By stayConnectedTogglOnButton 	= By.xpath("//input[contains(@class,'continuousBridge')]/..//label[contains(@class,'toggle-on')]");
	By stayConnectedTimeoutTextBox  = By.cssSelector(".continuousBridgeTimeout");
	
	// call notification
	By callNotification                 = By.xpath("//label[text() = 'Call Notifications ']");
	By callNotificationCheckBox 		= By.cssSelector("input.callNotification");
	By callNotificationToggleOffButton  = By.xpath("//input[contains(@class,'callNotification')]/..//label[contains(@class,'toggle-off')]");
	By callNotificationToggleOnButton 	= By.xpath("//input[contains(@class,'callNotification')]/..//label[contains(@class,'toggle-on')]");
	
	// voice mail
	By voiceMailEnabled                 = By.xpath("//label[text() = 'Voicemail Enabled']");
	By voiceMailEnabledCheckBox 		= By.cssSelector("input.voicemailEnabled");
	By voiceMailEnabledToggleOffButton 	= By.xpath("//input[contains(@class,'voicemailEnabled')]/..//label[contains(@class,'toggle-off')]");
	By voiceMailEnabledToggleOnButton 	= By.xpath("//input[contains(@class,'voicemailEnabled')]/..//label[contains(@class,'toggle-on')]");
	
	By callForwarding                   = By.xpath("//label[text() = 'Call Forwarding ']");
	By callForwardingCheckBox 		 	= By.cssSelector("input.callForwarding");
	By callForwardingToggleOffButton 	= By.xpath("//input[contains(@class,'callForwarding')]/..//label[contains(@class,'toggle-off')]");
	By callForwardingToggleOnButton  	= By.xpath("//input[contains(@class,'callForwarding')]/..//label[contains(@class,'toggle-on')]");
	
	// call forwarding
	By callForwardingTimeOutTextBox  	= By.cssSelector("input.callForwardingTimeout");
	
	By disableOfflineForwardingCheckBox			= By.cssSelector("input.callForwardingDisabledOffline");
	By disableOfflineForwardingToggleOffButton 	= By.xpath("//input[contains(@class,'callForwardingDisabledOffline')]/..//label[contains(@class,'toggle-off')]");
	By disableOfflineForwardingToggleOnButton 	= By.xpath("//input[contains(@class,'callForwardingDisabledOffline')]/..//label[contains(@class,'toggle-on')]");
	
	By callForwardingPromptCheckBox			= By.cssSelector(".callForwardingPrompt.toggle-switch");
	By callForwardingPromptToggleOffBtn		= By.xpath("//input[@class='callForwardingPrompt toggle-switch']/..//label[contains(@class,'toggle-off')]");
	By callForwardingPromptToggleOnBtn		= By.xpath("//input[@class='callForwardingPrompt toggle-switch']/..//label[contains(@class,'toggle-on')]");
	
	//click to voicemail
	By clickToVoicemail                     = By.xpath("//label[text() = 'Click-to-Voicemail']");
	By clickToVoicemailCheckbox 			= By.cssSelector("input.clickToVoicemail");
	By clickToVoicemailToggleButton 		= By.xpath("//input[contains(@class,'clickToVoicemail')]/..//label[contains(@class,'toggle-off')]");
	By clickToVoicemailToggleOnButton 		= By.xpath("//input[contains(@class,'clickToVoicemail')]/..//label[contains(@class,'toggle-on')]");
	
	// -------- Lock values ----------- //		
	By callForwardingLockBtn				= By.cssSelector(".callForwardingLocked .changed-setting img");
	By callForwardingUnlockActive			= By.xpath(".//*[@class='callForwardingLocked']//img[contains(@src,'/unlock-active.svg')]");
	By callForwardingLockActive				= By.xpath(".//*[@class='callForwardingLocked']//img[contains(@src,'/lock-active.svg')]");
	By callForwrdingTimeoutLockBtn 			= By.cssSelector(".callForwardingTimeoutLocked .changed-setting img");
	By unavailableFlowLockBtn				= By.cssSelector(".unavailableFlowLocked .changed-setting img");
		
	By disableOfflineForwardingUnlockActive	= By.xpath(".//*[@class='callForwardingDisabledOfflineLocked']//img[contains(@src,'/unlock-active.svg')]");
	By disableOfflineForwardingLockActive	= By.xpath(".//*[@class='callForwardingDisabledOfflineLocked']//img[contains(@src,'/lock-active.svg')]");
	By disableOfflineForwrdingBtn 			= By.cssSelector(".callForwardingDisabledOfflineLocked .changed-setting img");
		
	By callForwardingPromptUnlockActive		= By.xpath(".//*[@class='callForwardingPromptLocked']//img[contains(@src,'/unlock-active.svg')]");
	By callForwardingPromptLockActive		= By.xpath(".//*[@class='callForwardingPromptLocked']//img[contains(@src,'/lock-active.svg')]");
	By callForwardingPromptBtn 				= By.cssSelector(".callForwardingPromptLocked .changed-setting img");
	
	By unavailableFlowHeading 	   			= By.xpath("//label[contains(text(), 'Unavailable Flow')]");
	By unavailableCallFlowTextBox  			= By.cssSelector(".callFlowPicker input");
	By unavailableCallFlowDropDown  		= By.cssSelector(".callFlowPicker .selectize-control");
	By searchedFlowInDropDown				= By.cssSelector(".selectize-dropdown-content .option.active");
	
	By defaultLPFlowDropdown 				= By.cssSelector(".presenceFlowPicker .selectize-input.items");
	By defaultLPFlowTextBox  				= By.cssSelector(".presenceFlowPicker .selectize-input.items input");
	By defaultLPFlowOptions  				= By.cssSelector(".presenceFlowPicker .option");
	
	By defaultLPCallFlowDropDown			= By.cssSelector(".presenceFlowPicker .selectize-control");
	By defaultLPCallFlowTextBox  			= By.cssSelector(".presenceFlowPicker input");
	
	By unavailableFlowDropdown 				= By.xpath("//label[text()='Unavailable Flow ']/..//div[contains(@class,'selectize-input items')]");
	By unavailableFlowTextBox  				= By.xpath("//label[text()='Unavailable Flow ']/..//input");
	By unavailableFlowOptions  				= By.cssSelector(".callFlowPicker .option");
	
	// advance Search
	By advancedSearchTextBox         		= By.cssSelector("input.advancedSearch");
	By advancedSearchOffToggleButton		= By.xpath("//input[contains(@class,'advancedSearch')]/..//label[contains(@class,'toggle-off')]");
	By advancedSearchOnToggleButton  		= By.xpath("//input[contains(@class,'advancedSearch')]/..//label[contains(@class,'toggle-on')]");
	
	By dualChannelLabel			  			= By.xpath(".//*[text()='Dual Channel Recording']");
	
	By voiceMailTranscBlacklist   			= By.cssSelector(".voicemailTranscriptionBlacklist");
	By dynamicCallFlow                      = By.xpath("//label[text() = 'Dynamic Call Flows']");
	By dynamicCallFlowCheckBox	  			= By.cssSelector("input.dynamicCallFlows");
	By dynamicCallFlowToggleOff  			= By.xpath("//input[contains(@class,'dynamicCallFlows')]//following-sibling::div//label[contains(@class,'toggle-off')]");
	By dynamicCallFlowToggleOn	 			= By.xpath("//input[contains(@class,'dynamicCallFlows')]//following-sibling::div//label[contains(@class,'toggle-on')]");
  	
	By callForwardingHeading 				= By.xpath("//*[@id='main']//h2[text()='Call Forwarding']");
	
	By conferenceHeading                    = By.xpath("//label[text() = 'Conference']");
	By conferenceButtonCheckBox 			= By.cssSelector("input.conference.toggle-switch");
	By conferenceToggleOffButton 			= By.xpath("//input[contains(@class,'conference')]//parent::*//*[contains(@class,'toggle-off')]");
	By conferenceToggleOnButton 			= By.xpath("//input[contains(@class,'conference')]//parent::*//*[contains(@class,'toggle-on')]");
	
	By sendEmailUsingSalesforce             = By.xpath("//label[text() = 'Send Emails using Salesforce']");
	By sendEmailUsingSalesForceCheckbox 	= By.cssSelector(".emailSendViaSalesforce");
	By sendEmailUsingSalesForceToggleButton = By.xpath(".//input[contains(@class, 'emailSendViaSalesforce')]/parent::div//*[contains(@class,'toggle-off')]");
	
	By ligntningEmailCheckbox				= By.className("lightningEmailCheckbox");
	By callTimeoutTextBox 					= By.cssSelector("input.callTimeout");
	
	By directConnect                        = By.xpath("//label[text() = 'Direct Connect']");
	By directConnectCheckBox 	 			= By.cssSelector("input.directConnect");
	By directConnectToggleButton 			= By.xpath("//input[contains(@class,'directConnect')]/..//label[contains(@class,'toggle-off')]");
	
	By dualChannelRecording                 = By.xpath("//label[text() = 'Dual Channel Recording']");
	By dualChannelRecordingCheckBox 		= By.cssSelector("input.dualChannelRecording");
	By dualChannelRecordingToggleButton 	= By.xpath("//input[contains(@class,'dualChannelRecording')]/..//label[contains(@class,'toggle-off')]");
	
	//salesforce settings > ID tab
	By packageCheckRun						= By.cssSelector(".btn.btn-info.package");
	By clearCacheBtn						= By.cssSelector(".btn.btn-info.cache");
	By salesForceConnectBtn					= By.cssSelector(".btn.btn-info.connect");
	
	By customActivityObjectCheckBox 		= By.cssSelector("input.sfdcCustomCallObject");
	By customActivityObjectToggleOnButton 	= By.xpath("//*[@id='main']//input[contains(@class, 'sfdcCustomCallObject')]/..//label[contains(text(), 'On')]");
	By customActivityObjectToggleOffButton 	= By.xpath("//*[@id='main']//input[contains(@class, 'sfdcCustomCallObject')]/..//label[contains(text(), 'Off')]");
	By customActivityObjectToggleButton 	= By.xpath("//*[@id='main']//input[@class='sfdcCustomCallObject']/..//label[contains(text(), 'On')]");
	
	By phoneMatchAccountCheckBox 			= By.cssSelector("input.sfdcMatchAccounts");
	By phoneMatchAccountToggleButton 		= By.xpath("//input[contains(@class, 'sfdcMatchAccounts')]/..//label[contains(@class,'toggle-off')]");
	By phoneMatchAccountToggleOffButton 	= By.xpath("//input[contains(@class, 'sfdcMatchAccounts')]/..//label[contains(@class,'toggle-on')]");
	By phoneMatchContactsCheckBox			= By.cssSelector("input.sfdcMatchContacts");
	By phoneMatchContactsToggleButton 		= By.xpath("//input[contains(@class, 'sfdcMatchContacts')]/..//label[contains(@class,'toggle-off')]");
	By phoneMatchContactsToggleOffButton 	= By.xpath("//input[contains(@class, 'sfdcMatchContacts')]/..//label[contains(@class,'toggle-on')]");
	By phoneMatchLeadsCheckBox 				= By.cssSelector("input.sfdcMatchLeads");
	By phoneMatchLeadsToggleButton 			= By.xpath("//input[contains(@class,'sfdcMatchLeads')]/..//label[contains(@class,'toggle-off')]");
	By phoneMatchLeadsToggleOffButton 		= By.xpath("//input[contains(@class,'sfdcMatchLeads')]/..//label[contains(@class,'toggle-on')]");
	By phoneMatchOpportunitiesCheckBox 		= By.cssSelector("input.sfdcMatchOpportunities");
	By phoneMatchOpportunitiesToggleButton 	= By.xpath("//input[contains(@class, 'sfdcMatchOpportunities')]/..//label[contains(@class,'toggle-off')]");
	By phoneMatchOpportunitiesToggleOffButton 	= By.xpath("//input[contains(@class, 'sfdcMatchOpportunities')]/..//label[contains(@class,'toggle-on')]");
	
	By soslSearchCheckBox 					= By.cssSelector("input.sfdcSoslSearch");
	By soslSearchToggleButton 				= By.xpath("//input[contains(@class, 'sfdcSoslSearch')]/..//label[contains(@class,'toggle-off')]");
	By soslSearchToggleOnButton				= By.xpath("//input[contains(@class, 'sfdcSoslSearch')]/..//label[contains(@class,'toggle-on')]");
	
	By createLeadOnMultiSearchCheckBox 		= By.cssSelector("input.sfdcCreateLeadOnMultiMatch");
	By createLeadOnMultiSearchToggleOffBtn 	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnMultiMatch')]/..//label[contains(@class,'toggle-off')]");
	By createLeadOnMultiSearchToggleOnBtn 	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnMultiMatch')]/..//label[contains(@class,'toggle-on')]");
	By createLeadSectionForSFDCCampaign		= By.xpath(".//*[contains(@class,'sfdcCreateLeadOnSFDCCampaign')]/parent::div[contains(@class,'sfdcCreateLeadSection')]");
	By createLeadSectionForUnansweredCall	= By.xpath(".//*[contains(@class, 'sfdcCreateLeadOnUnansweredCall')]/parent::div[contains(@class,'sfdcCreateLeadSection')]");
	By createLeadSFDCCampaign				= By.cssSelector(".sfdcCreateLeadOnSFDCCampaign");
	By createLeadUnansweredCall				= By.cssSelector(".sfdcCreateLeadOnUnansweredCall");
	
	By createTaskEarlyCheckBox	 			= By.cssSelector("input.sfdcCreateTasksEarly");
	By createTaskEarlyToggleOffBtn			= By.xpath("//input[contains(@class,'sfdcCreateTasksEarly')]/..//label[contains(@class,'toggle-off')]");
	By createTaskEarlyToggleOnBtn 			= By.xpath("//input[contains(@class,'sfdcCreateTasksEarly')]/..//label[contains(@class,'toggle-on')]");
	
	By disableLeadCreationCheckBox 			= By.cssSelector("input.sfdcDisableLeadCreation");
	By disableLeadCreationToggleButton 		= By.xpath("//input[contains(@class, 'sfdcDisableLeadCreation')]/..//label[contains(@class,'toggle-on')]");
	By enableLeadCreationToggleButton 		= By.xpath("//input[contains(@class, 'sfdcDisableLeadCreation')]/..//label[contains(@class,'toggle-off')]");
	
	//tool tip
	By emailNotifyToolTip                   = By.xpath(".//label[text()[normalize-space()='Email - Notify Call Queues']]//parent::label//following-sibling::span");
	By voicemailBlacklistToolTip            = By.xpath(".//label[text()[normalize-space()='Voicemail Transcription Blacklist']]//parent::label//following-sibling::span");
	By notificationsToolTip					= By.xpath(".//label[text()[normalize-space()='Notifications']]//parent::label//following-sibling::span");
		   
	// yoda ui
	By yodaAiNotification                   = By.xpath("//span[@data-original-title= 'Enable YODA AI Notifications']/parent::label");
	//visiblity
	By inboundCallTone                      = By.xpath("//label[text()= 'Inbound Call Tone ']");
	By verifiedLocalPresence                = By.xpath("//label[contains(text(), 'Numbers for Local Presence')]");

		//default contact
	By selectDeafultContact                 = By.cssSelector(".sfdcDefaultMatchTypeOption.selector.form-control");
	//sip header
	By sipRoutingHeader              = By.xpath("//h2[text() = 'SIP Routing']");
	By sipForwardingHeader           = By.xpath("//h2[text() = 'SIP Forwarding Settings']");
	
	//caller id
	By callerId                     = By.xpath("//label[text() = 'Caller ID ']");
	By callerIdCheckBox             = By.cssSelector("input.callerId.toggle-switch");
	
	//max hold time
	By maxHoldTimeInput             = By.cssSelector(".maxHoldTime.integer.form-control.input-sm");
	
	//softphone sound settings
	By soundDisconnect                      = By.xpath("//label[text() = 'Disconnect']");
	By soundDisconnectCheckBox              = By.cssSelector("input.soundDisconnect.toggle-switch");
	By extensionNonPhoneFilter              = By.cssSelector(".extensionNonPhoneFilter.form-control");
	By showSlaReport                        = By.cssSelector(".showSlaReport.toggle-switch");
	By clickToCallDomainBlacklist           = By.cssSelector(".clickToCallDomainBlacklist");
	By holdMusic                            = By.xpath("//label[text()= 'Hold Music']");
	By queuesAnnouncement                   = By.xpath("//label[text()= 'Queues Announcement']");
	By mp3Recordings                        = By.cssSelector(".mp3RecordingEnabled");
	By addNewDefault                        = By.cssSelector(".sfdcDefaultMatchTypeOption");
	By basePackageName                      = By.xpath("//label[text()= 'Base Package Name']");
	By basePackageCheck                     = By.xpath("//label[text()= 'Base Package Check']");
	By basePackageVersion                   = By.xpath("//label[text()= 'Base Package Version']");
	By ignoreIntegrityErrors                = By.xpath("//label[text()= 'Ignore Integrity Errors']");
	By honorDoNotCallOrEmail                = By.xpath("//label[text()= 'Honor Do Not Call Or Email']");
	By createLeadOnMultiMatch               = By.xpath("//label[text()= 'Create Lead on Multi-Match']");
	By phoneMatchAccounts                   = By.xpath("//label[text()= 'Phone Match Accounts']");
	By phoneMatchContacts                   = By.xpath("//label[text()= 'Phone Match Contacts']");
	By phoneMatchLeads                      = By.xpath("//label[text()= 'Phone Match Leads']");
	By phoneMatchOpportunities              = By.xpath("//label[text()= 'Phone Match Opportunities']");																								
	By phoneMatchOnExternalPhoneFields      = By.xpath("//label[text()= 'Phone Match on External Phone Fields']");
	By SOSLSearch                           = By.xpath("//label[text()= 'SOSL Search']");
	By openRecordsInSalesConsole            = By.xpath("//label[text()= 'Open Records in Sales Console']");
	By ClickToCallReuseTask                 = By.xpath("//label[text()= 'Click-to-Call Reuse Task']");
	By createTasksEarly                     = By.xpath("//label[text()= 'Create Tasks Early']");
	By customActivityObject                 = By.xpath("//label[text()= 'Custom Activity Object']");
	By transcoderPipelineId                 = By.xpath("//label[text()= 'Transcoder Pipeline ID']");
	By transcoderMp3PipelineId              = By.xpath("//label[text()= 'Transcoder Mp3 Pipeline ID']");
	By waitOnConferenceEnter                = By.xpath("//label[text()= 'Wait on Conference Enter']");
	By agentStatusCallout                   = By.xpath("//label[text()= 'Agent Status Callout']");																						   
	By packageCheck                         = By.xpath("//h2[text() = 'Package Check']");
	By cache                                = By.xpath("//h2[text() = 'Cache']");
	
	static final String tooltipWebLeadsLock = "Click to lock this setting and disable admins from setting Hot Leads preferences for individual users.";
	static final String tooltipWebLeadsUnLock = "Click to unlock this setting and allow admins to set Hot Leads preferences for individual users.";
	static final String	tooltipCallFrwrdingLock   			  = "Call Forwarding Timeout cannot be locked OFF. To lock a Call Forwarding Timeout, a Call Forwarding Timeout value must be entered.";	
	static final String	tooltipDisableOfflineForwardingLock   = "Click to lock this setting and disable users from setting individual offline forwarding preferences.";
	static final String	tooltipDisableOfflineForwardingUnLock = "Click to unlock this setting and allow users to set individual offline forwarding preferences.";
	static final String	tooltipCallForwardingPromptLock    	  = "Click to lock this setting and disable users from setting individual Call Forwarding Prompt preferences.";
	static final String	tooltipCallForwardingPromptUnLock     = "Click to unlock this setting and allow users to set individual Call Forwarding Prompt preferences.";
	static final String	tooltipUnavailableLock    	 		  = "Click to lock this setting and disable users from setting individual Unavailable Flows.";
	static final String	tooltipUnavailableUnLock    	      = "Click to unlock this setting and allow users to set individual Unavailable Flows.";

	public static enum dispositionReqStates{
		All,
		Inbound,
		Outbound,
		None;
	}
	
	
	/*public void enableTaskDueDateRequiredSetting(WebDriver driver){
		if(!findElement(driver, taskDueDateRequiredCheckbox).isSelected()) {
			clickElement(driver, taskDueDateRequiredToggleOffButton);
			System.out.println("Enabled task due date required setting");
		} else {
			System.out.println("task due date required setting is already on");
		}
	}
	
	public void disableTaskDueDateRequiredSetting(WebDriver driver){
		if(findElement(driver, taskDueDateRequiredCheckbox).isSelected()) {
			clickElement(driver, taskDueDateRequiredToggleOnButton);
			System.out.println("disabled task due date required setting");
		} else {
			System.out.println("task due date required setting is already disabled");
		}
	}*/
	
	/*public void enableTaskSubjectRequiredSetting(WebDriver driver){
		if(!findElement(driver, taskSubjectRequredCheckBox).isSelected()) {
			clickElement(driver, taskSubjectReqToggleOffBtn);
			System.out.println("Enabled task subject required setting");
		} else {
			System.out.println("task subject required setting is already on");
		}
	}
	
	public void disableTaskSubjectRequiredSetting(WebDriver driver){
		if(findElement(driver, taskSubjectRequredCheckBox).isSelected()) {
			clickElement(driver, taskSubjectReqToggleOnBtn);
			System.out.println("disabled task Subject required setting");
		} else {
			System.out.println("task Subject required setting is already disabled");
		}
	}*/
	
	public static enum Days{
		Sun("0"),
		Mon("1"),
		Tue("2"),
		Wed("3"),
		Thu("4"),
		Fri("5"),
		Sat("6");
		
		private String displayName;
		
		Days(String displayName) {
			this.displayName = displayName;
		}
		
		public String displayName() { return displayName; }
		
		@Override 
		public String toString() { return displayName; }
	}
	
	public static enum CompliancTimeType{
		StartTime,
		EndTime;
	}
	
	/*******Custom User Status section starts here******/
	public static enum CustomStatusFields{
		StatusName,
		Busy,
		Description,
		Time,
		Team;
	}
	
	public static enum CustomStatusTime{
		Five("5 Minutes"),
		Fifteen("15 Minutes (Default)"),
		Thirty("30 Minutes"),
		Hour("1 hour"),
		DoesNotExpire("Does Not Expire");

		private String displayName;

		CustomStatusTime(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}
	
	/**
	 * @param driver
	 * @return yoda ai notification
	 */
	public boolean isYodaAINotificationVisible(WebDriver driver) {
		return isElementVisible(driver, yodaAiNotification, 3);
	}
	
	/**
	 * @param driver
	 */
	public void verifyIDSettingsVisibleForSupport(WebDriver driver) {
		assertTrue(isElementVisible(driver, callQueue, 5));
		assertTrue(isElementVisible(driver, inboundCallTone, 5));
		assertTrue(isElementVisible(driver, verifiedLocalPresence, 5));
		assertTrue(isElementVisible(driver, newTaskEvent, 5));
		assertTrue(isElementVisible(driver, generalSettings, 5));
		assertTrue(isElementVisible(driver, hideGroupCallHistory, 5));
		assertTrue(isElementVisible(driver, emailButton, 5));
	}
	
	/**
	 * @param driver
	 */
	public void verifyIDSettingsNotVisibleForAdmin(WebDriver driver) {
		assertFalse(isElementVisible(driver, callQueueCheckBox, 5));
		assertFalse(isElementVisible(driver, inboundCallTone, 5));
		assertFalse(isElementVisible(driver, verifiedLocalPresence, 5));
		assertFalse(isElementVisible(driver, newTaskCheckBox, 5));
		assertFalse(isElementVisible(driver, generalSettingCheckBox, 5));
		assertFalse(isElementVisible(driver, hideGroupCallHistoryCheckBox, 5));
		assertFalse(isElementVisible(driver, emailButtonCheckBox, 5));
}
  	
	public void openIntelligentDialerTab(WebDriver driver) {
		waitUntilVisible(driver, intelligentDialerTab);
		clickElement(driver, intelligentDialerTab);
		dashBoard.isPaceBarInvisible(driver);
		findElement(driver, intelligentDialerTabHeading);
	}
	
	public boolean isIntelligentDialerTabLinkVisible(WebDriver driver) {
		return isElementVisible(driver, intelligentDialerTab, 0);
	}
	
	public boolean isUserOnIntelligentDialerTab(WebDriver driver) {
		dashBoard.isPaceBarInvisible(driver);
		return isElementVisible(driver, intelligentDialerTabHeading, 0);
	}
	
	public void enableCallQueueSetting(WebDriver driver){
		if(!findElement(driver, callQueueCheckBox).isSelected()) {
			clickElement(driver, callQueueToggleButton);
			System.out.println("Enabled call queue");
		} else {
			System.out.println("Already enabled call queue setting");
		}
	}
	
	/**
	 * @param driver
	 * disable call queue settings
	 */
	public void disableCallQueueSetting(WebDriver driver){
		if(findElement(driver, callQueueCheckBox).isSelected()) {
			clickElement(driver, callQueueToggleButton);
			System.out.println("Enabled call queue");
		} else {
			System.out.println("Already enabled call queue setting");
		}
	}
	
	public void enableCallRatingSetting(WebDriver driver){
		if(!findElement(driver, callRatingCheckBox).isSelected()) {
			clickElement(driver, callRatingToggleBox);
			System.out.println("Enabled call rating");
		} else {
			System.out.println("Already enabled call rating setting");
		}
	}
	
	public void enableRelatedRecordsSetting(WebDriver driver){
		if(!findElement(driver, enableRelatedRecordsCheckBox).isSelected()) {
			clickElement(driver, enableRelatedRecordsToggleButton);
			System.out.println("Enabled related records");
		} else {
			System.out.println("Already enabled related records setting");
		}
	}
	
	public void enableGeneralSettings(WebDriver driver){
		if(!findElement(driver, generalSettingCheckBox).isSelected()) {
			clickElement(driver, generalSettingToggleButton);
			System.out.println("Enabled general settings");
		} else {
			System.out.println("Already enabled general settings setting");
		}
	}
	
	public void enableChatterBoxSetting(WebDriver driver){
		if(!findElement(driver, chatterBoxCheckBox).isSelected()) {
			clickElement(driver, chatterBoxToggleButton);
			System.out.println("Enabled chatter box");
		} else {
			System.out.println("Already enabled chatter box setting");
		}
	}
	
	public void enableLeadStatusSetting(WebDriver driver){
		if(!findElement(driver, leadStatusCheckBox).isSelected()) {
			clickElement(driver, leadStatusToggleButton);
			System.out.println("Enabled lead status setting");
		} else {
			System.out.println("Already enabled lead status setting");
		}
	}
	
	public void enableNewTaskSetting(WebDriver driver){
		if(!findElement(driver, newTaskCheckBox).isSelected()) {
			clickElement(driver, newTaskToggleButton);
			System.out.println("Enabled new task");
		} else {
			System.out.println("Already enabled new task");
		}
	}
	
	public void disableNewTaskSetting(WebDriver driver){
		if(findElement(driver, newTaskCheckBox).isSelected()) {
			clickElement(driver, newTaskToggleOnButton);
			System.out.println("Disabled new task");
		} else {
			System.out.println("Already disabled new task");
		}
	}
	
	public void enableMessagingSetting(WebDriver driver){
		if(findElement(driver, enableMessagingButton).isDisplayed()) {
			clickElement(driver, enableMessagingButton);
			waitUntilVisible(driver, disableMessagingButton);
			System.out.println("Enabled messaging setting");
		} else {
			System.out.println("Already enabled messaging");
		}
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void disableMessagingSetting(WebDriver driver){
		if(findElement(driver, disableMessagingButton).isDisplayed()) {
			clickElement(driver, disableMessagingButton);
			waitUntilVisible(driver, enableMessagingButton);
			System.out.println("Disabled messaging setting");
		} else {
			System.out.println("Already disabled messaging");
		}
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void disableCallToolsSetting(WebDriver driver){
		if(findElement(driver, enableCallsToolsCheckBox).isSelected()) {
			clickElement(driver, enableCallsToolsToggleButton);
			System.out.println("Disabled new call tools setting");
		} else {
			System.out.println("Already disabled new call tools setting");
		}
	}
	
	public void enableCallToolsSetting(WebDriver driver){
		if(!findElement(driver, enableCallsToolsCheckBox).isSelected()) {
			clickElement(driver, enableCallToolsToggleOffBtn);
			System.out.println("enabled new call tools setting");
		} else {
			System.out.println("Already enabled new call tools setting");
		}
	}
	
	public void disableHideGroupCallHistory(WebDriver driver){
		if(findElement(driver, hideGroupCallHistoryCheckBox).isSelected()) {
			clickElement(driver, hideGroupCallHistoryToggleOnButton);
			System.out.println("Disabled hide group voicmail setting");
		} else {
			System.out.println("Already disabled hide group voicemail");
		}
	}
	
	public void enableHideGroupCallHistory(WebDriver driver){
		if(!findElement(driver, hideGroupCallHistoryCheckBox).isSelected()) {
			clickElement(driver, hideGroupCallHistoryToggleOffButton);
			System.out.println("enabled hide group voicmail setting");
		} else {
			System.out.println("Already enabled hide group voicemail");
		}
	}
	
	public void disableCallDispositionPromptSetting(WebDriver driver){
		if(findElement(driver, callDispositionPromptCheckBox).isSelected()) {
			clickElement(driver, callDispositionPromptToggleOnBtn);
			System.out.println("Disabled call disposition Prompt setting");
		} else {
			System.out.println("Already disable call disposition Prompt");
		}
	}
	
	public void enableCallDispositionPromptSetting(WebDriver driver){
		if(!findElement(driver, callDispositionPromptCheckBox).isSelected()) {
			clickElement(driver, callDispositionPromptToggleOffBtn);
			System.out.println("enabled call disposition Prompt setting");
		} else {
			System.out.println("Already enabled call disposition Prompt");
		}
	}
	
	public void disableCallDispositionRequiredSetting(WebDriver driver){
		selectFromDropdown(driver, callDispositionRequiredDropDown, SelectTypes.visibleText, dispositionReqStates.None.toString());
		System.out.println("Disabled call disposition required setting");
	}
	
	public void enableMultiMatchRequiredSetting(WebDriver driver){
		if(!findElement(driver, manualMultiMatchRadioBtn).isSelected()) {
			clickElement(driver, manualMultiMatchRadioBtn);
			System.out.println("enabled multi match required setting");
		} else {
			System.out.println("Already enabled multi match required");
		}
	}
	
	public void disableMultiMatchRequiredSetting(WebDriver driver){
		if(!findElement(driver, noMultiMatchRadioBtn).isSelected()) {
			clickElement(driver, noMultiMatchRadioBtn);
			System.out.println("Disabled multi match required setting");
		} else {
			System.out.println("Already multi match required required");
		}
	}
	
	public void enableCallDispositionRequiredSetting(WebDriver driver){
		selectFromDropdown(driver, callDispositionRequiredDropDown, SelectTypes.visibleText, dispositionReqStates.All.toString());
		System.out.println("enabled call disposition required setting");
	}
	
	public void setCallDispositionRequiredSetting(WebDriver driver, dispositionReqStates dispRequiredStates){
		selectFromDropdown(driver, callDispositionRequiredDropDown, SelectTypes.visibleText, dispRequiredStates.toString());
	}
	
	public String getCallDispositionSettingChangedTime(WebDriver driver) {
		waitUntilVisible(driver, callDispositionChangeDate);
		return getElementsText(driver, callDispositionChangeDate);
	}
	
	public void verifyCallDispositionchangedTimeInvisible(WebDriver driver) {
		waitUntilInvisible(driver, callDispositionChangeDate);
	}
	
	public List<String> getCallDispositionList(WebDriver driver){
		return getTextListFromElements(driver, callDispostionList);
	}
	
	public void enableEmailButtonSetting(WebDriver driver){
		if(!findElement(driver, emailButtonCheckBox).isSelected()) {
			clickElement(driver, emailButtonToggleButton);
			System.out.println("Enabled email button setting");
		} else {
			System.out.println("Already enabled email button ");
		}
	}
	
	public void enableManageDispositionByTeam(WebDriver driver){
		if(!findElement(driver, manageByTeamCheckBox).isSelected()) {
			clickElement(driver, manageByTeamCheckBox);
			System.out.println("enabled manage disposition by team setting");
		} else {
			System.out.println("Already enabled manage disposition by team setting");
		}
	}
	
	public void disableManageDispositionByTeam(WebDriver driver){
		if(findElement(driver, manageByTeamCheckBox).isSelected()) {
			clickElement(driver, manageByTeamCheckBox);
			waitUntilVisible(driver, confirmManageByTeamBtn);
			clickElement(driver, confirmManageByTeamBtn);
			waitUntilInvisible(driver, confirmManageByTeamBtn);
			System.out.println("disabled manage disposition by team setting");
		} else {
			System.out.println("Already disabled manage disposition by team setting");
		}
	}
	
	/*public void enableTaskDueDateRequiredSetting(WebDriver driver){
		if(!findElement(driver, taskDueDateRequiredCheckbox).isSelected()) {
			clickElement(driver, taskDueDateRequiredToggleOffButton);
			System.out.println("Enabled task due date required setting");
		} else {
			System.out.println("task due date required setting is already on");
		}
	}
	
	public void disableTaskDueDateRequiredSetting(WebDriver driver){
		if(findElement(driver, taskDueDateRequiredCheckbox).isSelected()) {
			clickElement(driver, taskDueDateRequiredToggleOnButton);
			System.out.println("disabled task due date required setting");
		} else {
			System.out.println("task due date required setting is already disabled");
		}
	}*/
	
	/*public void enableTaskSubjectRequiredSetting(WebDriver driver){
		if(!findElement(driver, taskSubjectRequredCheckBox).isSelected()) {
			clickElement(driver, taskSubjectReqToggleOffBtn);
			System.out.println("Enabled task subject required setting");
		} else {
			System.out.println("task subject required setting is already on");
		}
	}
	
	public void disableTaskSubjectRequiredSetting(WebDriver driver){
		if(findElement(driver, taskSubjectRequredCheckBox).isSelected()) {
			clickElement(driver, taskSubjectReqToggleOnBtn);
			System.out.println("disabled task Subject required setting");
		} else {
			System.out.println("task Subject required setting is already disabled");
		}
	}*/
	
	public void scrollToCallTasksSetting(WebDriver driver){
		scrollToElement(driver, callTasksDueDateToggleButton);
	}
	
	public void disableCallTasksDueDateSetting(WebDriver driver){
		scrollToCallTasksSetting(driver);
		if(findElement(driver, callTasksDueDateCheckBox).isSelected()) {
			clickElement(driver, callTasksDueDateToggleButton);
			System.out.println("Disabled call task due date setting in new task section");
		} else {
			System.out.println("Already disabled due date in new task");
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableCallTasksDueDateSetting(WebDriver driver){
		scrollToCallTasksSetting(driver);
		if(!findElement(driver, callTasksDueDateCheckBox).isSelected()) {
			clickElement(driver, callTasksDueDateToggleOffButton);
			System.out.println("enabled call task due date setting in new task section");
		} else {
			System.out.println("Already disabled due date in new task");
		}
	}
	
	public void disableCallTasksTypeSetting(WebDriver driver){
		if(findElement(driver, callTasksTypeCheckBox).isSelected()) {
			clickElement(driver, callTasksTypeToggleButton);
			System.out.println("Disabled the type field setting in new task section");
		} else {
			System.out.println("Already disabled type in new task");
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableCallTasksTypeSetting(WebDriver driver){
		if(!findElement(driver, callTasksTypeCheckBox).isSelected()) {
			clickElement(driver, callTasksTypeToggleOffButton);
			System.out.println("Disabled the type field setting in new task section");
		} else {
			System.out.println("Already disabled type in new task");
		}
	}
	
	public void disableCallTasksReminderSetting(WebDriver driver){
		if(findElement(driver, callTasksReminderCheckBox).isSelected()) {
			clickElement(driver, callTasksReminderToggleButton);
			System.out.println("Disabledd the reminder field setting in new task section");
		} else {
			System.out.println("Already disabled reminder in new task");
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableCallTasksReminderSetting(WebDriver driver){
		if(!findElement(driver, callTasksReminderCheckBox).isSelected()) {
			clickElement(driver, callTasksReminderToggleOffButton);
			System.out.println("Disabledd the reminder field setting in new task section");
		} else {
			System.out.println("Already disabled reminder in new task");
		}
	}
	
	public void disableCallTasksPrioritySetting(WebDriver driver){
		if(findElement(driver, callTasksPriorityCheckBox).isSelected()) {
			clickElement(driver, callTasksPriorityToggleButton);
			System.out.println("Disabled the priority field setting in new task section");
		} else {
			System.out.println("Already disabled priority in new task");
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableCallTasksPrioritySetting(WebDriver driver){
		if(!findElement(driver, callTasksPriorityCheckBox).isSelected()) {
			clickElement(driver, callTasksPriorityToggleOffButton);
			System.out.println("Disabled the priority field setting in new task section");
		} else {
			System.out.println("Already disabled priority in new task");
		}
	}
	
	public void disableCallNotesRelatedRecordSetting(WebDriver driver){
		if(findElement(driver, callTasksRelatedRecordCheckBox).isSelected()) {
			clickElement(driver, callTasksRelatedRecordToggleButton);
			System.out.println("Disabled related record field setting in new task section");
		} else {
			System.out.println("Already disabled related record in new task");
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableCallNotesRelatedRecordSetting(WebDriver driver){
		if(!findElement(driver, callTasksRelatedRecordCheckBox).isSelected()) {
			clickElement(driver, callTasksRelatedRecordToggleOffButton);
			System.out.println("Disabled related record field setting in new task section");
		} else {
			System.out.println("Already disabled related record in new task");
		}
	}
	
	public void enableWebLeadsSetting(WebDriver driver){
		if(!findElement(driver, webLeadsCheckbox).isSelected()) {
			clickElement(driver, webLeadsToggleOffButton);
			System.out.println("enables web leads setting");
		} else {
			System.out.println("web leads setting is already enabled");
		}
	}
	
	public void disableWebLeadsSetting(WebDriver driver){
		if(findElement(driver, webLeadsCheckbox).isSelected()) {
			clickElement(driver, webLeadsToggleOnButton);
			System.out.println("disabled web leads setting");
		} else {
			System.out.println("web leads setting is already disabled");
		}
	}
	
	public void enableColdTransferSetting(WebDriver driver){
		if(!findElement(driver, coldTransferCheckbox).isSelected()) {
			clickElement(driver, coldTransferToggleOffButton);
			System.out.println("enabled Append Transfer Info to Cold Transfer Target Calls setting");
		} else {
			System.out.println("Append Transfer Info to Cold Transfer Target Calls setting is already enabled");
		}
	}
	
	public void disableColdTransferSetting(WebDriver driver){
		if(findElement(driver, coldTransferCheckbox).isSelected()) {
			clickElement(driver, coldTransferToggleOnButton);
			System.out.println("disabled Append Transfer Info to Cold Transfer Target Calls setting");
		} else {
			System.out.println("Append Transfer Info to Cold Transfer Target Calls setting is already disabled");
		}
	}
	
	public void enableVmTranscriptionSetting(WebDriver driver){
		if(!findElement(driver, vmTranscriptionCheckbox).isSelected()) {
			clickElement(driver, vmTranscriptionTogglOffButton);
			System.out.println("enabled voicemeail transcription setting");
		} else {
			System.out.println("voicemail transcription setting is already enabled");
		}
	}
	
	public void disableVmTranscriptionSetting(WebDriver driver){
		if(findElement(driver, vmTranscriptionCheckbox).isSelected()) {
			clickElement(driver, vmTranscriptionToggleOnButton);
			System.out.println("disabled voicemeail transcription setting");
		} else {
			System.out.println("voicemeail transcription setting is already disabled");
		}
	}
	
	public boolean isVmTrascriptionByCountryEnabled(WebDriver driver) {
		if(isElementVisible(driver, vmTranscriptionGranularDisabled, 5)) {
			return false;
		}else {
			return true;
		}
	}
	
	public void verifyToolTipWebLeadsLock(WebDriver driver){
		scrollToElement(driver, webLeadsLockBtn);
		hoverElement(driver, webLeadsLockBtn);
		assertEquals(findElement(driver, webLeadsLockBtn).getAttribute("data-original-title"), tooltipWebLeadsLock);
	}
	
	public void verifyToolTipWebLeadsUnLock(WebDriver driver){
		scrollToElement(driver, webLeadsLockBtn);
		hoverElement(driver, webLeadsLockBtn);
		assertEquals(findElement(driver, webLeadsLockBtn).getAttribute("data-original-title"), tooltipWebLeadsUnLock);
	}
	
	public void lockWebLeadSetting(WebDriver driver){
		if(isElementVisible(driver, webLeadsUnlockActive, 5)){
			waitUntilVisible(driver, webLeadsUnlockActive);
			clickElement(driver, webLeadsUnlockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Lock \"Enable Hot Leads\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Web lead setting is already locked");
		}
	}
	
	public void unlockWebLeadSetting(WebDriver driver){
		if(isElementVisible(driver, webLeadsLockActive, 5)){
			waitUntilVisible(driver, webLeadsLockActive);
			clickElement(driver, webLeadsLockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Unlock \"Enable Hot Leads\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Web lead setting is already unlocked");
		}
	}
	
	public boolean isAccountWideOptSettingVisible(WebDriver driver) {
		return isElementVisible(driver, acctWideOptBtn, 5);
	}
	
	public boolean isAccountWideOptSettingDisabled(WebDriver driver) {
		return isElementDisabled(driver, acctWideOptBtn, 5);
	}
	
	public boolean isMsgSectionVisible(WebDriver driver){
		return isElementVisible(driver, msgProvisionSection, 5);
	}
	
	public void disableMsgWideOptSetting(WebDriver driver){
		if(findElement(driver, messagingWideOptOutCheckBox).isSelected()) {
			clickElement(driver, enableMsgWideOptBtn);
			System.out.println("Disabled Message Wide Opt setting");
		} else {
			System.out.println("Already disable Msg Wide Opt Setting");
		}
	}
	
	public void enableMsgWideOptSetting(WebDriver driver){
		if(!findElement(driver, messagingWideOptOutCheckBox).isSelected()) {
			clickElement(driver, disableMsgWideOptBtn);
			System.out.println("Enabled Message Wide Opt setting");
		} else {
			System.out.println("Already enabled Msg Wide Opt Setting");
		}
	}
	
	public String getSaveAccountsSettingMessage(WebDriver driver){
		return getElementsText(driver, saveAccountsSettingMessage);
	}
	
	public void isSaveAccountsSettingMessageDisappeared(WebDriver driver){
		isElementInvisible(driver, saveAccountsSettingMessage, 10);
	}
	
	public void saveAcccountSettings(WebDriver driver){
		waitUntilVisible(driver, saveAccountsSettingButton);
		scrollToElement(driver, saveAccountsSettingButton);
		clickByJs(driver, saveAccountsSettingButton);
		waitUntilTextPresent(driver, saveAccountsSettingMessage, "Intelligent Dialer settings saved.");
		hoverElement(driver, saveAccountsSettingButton);
		isSaveAccountsSettingMessageDisappeared(driver);		
	}
	
	public void enableSMSSetting(WebDriver driver){
		if(!findElement(driver, smsCheckBox).isSelected()) {
			clickElement(driver, smsToggleOffButton);
			System.out.println("Enabled SMS setting");
			scrollToElement(driver, smsAgreementCheckBox);
			clickElement(driver, smsAgreementCheckBox);
			scrollToElement(driver, smsAgreementButton);
			clickByJs(driver, smsAgreementButton);
			waitUntilVisible(driver, accountMsgEnabledText, 100);
		} else {
			System.out.println("Already enabled SMS setting");
		}
	}
	
	public void disableSMSSetting(WebDriver driver){
		if(findElement(driver, smsCheckBox).isSelected()) {
			clickElement(driver, smsToggleOnButton);
			System.out.println("Disbaled SMS setting");
		} else {
			System.out.println("Already disabled SMS setting");
		}
	}
	
	public void verifySMSMSgDefaultOff(WebDriver driver){
		waitUntilVisible(driver, smsMsgDefaultBtn);
		assertTrue(findElement(driver, smsMsgDefaultBtn).getAttribute("class").contains("toggle-off"));
	}
	
	/*******Voicemail section starts here*******/
  	public void clickAddVoicemailButton(WebDriver driver) {
  		waitUntilVisible(driver, addVoiceMailButton, 10);
		clickElement(driver, addVoiceMailButton);
	}
  	
  	public void clickRecordVoicemailLink(WebDriver driver) {
		waitUntilVisible(driver, newRecordingLink);
		clickElement(driver, newRecordingLink);
		waitUntilVisible(driver, voiceMailNameInput);
	}

	public void enterVoiceMailName(WebDriver driver, String voiceMailName) {
		enterText(driver, voiceMailNameInput, voiceMailName);
	}
 
	public void clickVoiceMailRecordButton(WebDriver driver) {
		clickElement(driver, recordVoiceMailButton);
		waitUntilVisible(driver, stopVoceMailRecordButton);
	}

	public void clickVoiceMailStopButton(WebDriver driver) {
		clickElement(driver, stopVoceMailRecordButton);
		waitUntilVisible(driver, voiceMailPlayButton);
	}

	public void clickSaveVoiceMailButton(WebDriver driver) {
		clickElement(driver, saveVoiceMailButton);
		waitUntilVisible(driver, addVoiceMailButton);
	}

	public void createVoiceMail(WebDriver driver, String voiceMailName, int duration) {
		scrollToElement(driver, addVoiceMailButton);
		clickAddVoicemailButton(driver);
		clickRecordVoicemailLink(driver);
		enterVoiceMailName(driver, voiceMailName);
		clickVoiceMailRecordButton(driver);
		idleWait(duration);
		clickVoiceMailStopButton(driver);
		clickSaveVoiceMailButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}  	
	
	public void createVoiceMailByUploadingFile(WebDriver driver, String voiceMailName) {
		idleWait(2);
		scrollToElement(driver, addVoiceMailButton);
		clickAddVoicemailButton(driver);
		enterVoiceLabelName(driver, voiceMailName);
		clickChooseFileLink(driver);
		HelperFunctions.uploadWavFileRecord();
		waitUntilVisible(driver, uploadFilePlayIcon);
		clickSaveVoiceMailButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}  	
	
	public void clickChooseFileLink(WebDriver driver) {
		waitUntilVisible(driver, chooseFileRecordingLink);
		clickElement(driver, chooseFileRecordingLink);
	}
	
	public void updateVoiceMailRecordsByUploadingFile(WebDriver driver, String oldVoiceMailName, String newVoiceMailName) {
		By updateRecordLoc = By.xpath(updateVoiceMailDrop.replace("$$RecordName$$", oldVoiceMailName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterVoiceLabelName(driver, newVoiceMailName);
		clickChooseFileLink(driver);
		HelperFunctions.uploadWavFileRecord();
		clickSaveVoiceMailButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void enterVoiceLabelName(WebDriver driver, String voiceLabelName) {
		waitUntilVisible(driver, inputLabelTab);
		enterText(driver, inputLabelTab, voiceLabelName);
	}

	public boolean checkVoiceMailIsPlayable(WebDriver driver, String voiceMailName) {
		boolean voiceMailRecord;
		By playRecordLoc = By.xpath(playVoiceMailDrop.replace("$$RecordName$$", voiceMailName));
		By stopRecordLoc = By.xpath(stopVoiceMailDrop.replace("$$RecordName$$", voiceMailName));
		if (isElementVisible(driver, playRecordLoc, 5)) {
			scrollToElement(driver, playRecordLoc);
			clickElement(driver, playRecordLoc);
			voiceMailRecord = isElementVisible(driver, stopRecordLoc, 10);
		} else {
			System.out.println("Voice Mail record does not exists");
			voiceMailRecord = false;
		}
		return voiceMailRecord;
	}
	
	/*******Voicemail section ends here*******/
	public void updateRecords(WebDriver driver, String oldRecordName, String newRecordName,String newRecordLink) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldRecordName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterCustomLinkName(driver, newRecordName);
		enterCustomLinkUrl(driver, newRecordLink);
		clickSaveCustomLinkButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}

	public void deleteRecords(WebDriver driver, String recordName) {
		By deleteRecordLoc = By.xpath(deleteRecordBtn.replace("$$RecordName$$", recordName));
		dashBoard.isPaceBarInvisible(driver);
		if (isElementVisible(driver, deleteRecordLoc, 2)) {
			scrollIntoView(driver, deleteRecordLoc);
			clickElement(driver, deleteRecordLoc);
			confirmDeleteAction(driver);
		}
	}
	
	public void confirmDeleteAction(WebDriver driver){
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
	}
	
	/*******Auto Disposition Voicemail Drop section starts here******/
	public void verifyDefaultAutoDispositionVMSection(WebDriver driver) {
		scrollToElement(driver, taskAndNotesHeading);
		waitUntilVisible(driver, autoDispVmDopHeading);
		assertEquals(getElementsText(driver, autoDispVmSubHeading), "The Call Disposition selected in the dropdown will be automatically applied after an agent leaves a Voicemail Drop.");
		assertEquals(getElementsText(driver, autoDispVmDropDownLabel), "Select Disposition");
		assertEquals(getElementsText(driver, autoDispVmDropDown), "(none)");
		clickElement(driver, autoDispVmDropDown);
		List<String> autoDispVMList = getTextListFromElements(driver, autoDispVmDropDownOptions);
		autoDispVMList.remove("(none)");
		assertEquals(getCallDispositionList(driver), autoDispVMList);
	}
	
	public void selectAutoDispositionVM(WebDriver driver, String dispToSelect) {
		scrollToElement(driver, taskAndNotesHeading);
		clickAndSelectFromDropDown(driver, autoDispVmDropDown, autoDispVmDropDownOptions, dispToSelect);
	}
	/*******Auto Disposition Voicemail Drop ends starts here******/
	
	/*******Custom Link section starts here******/
	public void clickAddCustomLinkButton(WebDriver driver) {
		waitUntilVisible(driver, addCustomLinkBtn);
		clickElement(driver, addCustomLinkBtn);
	}

	public void enterCustomLinkName(WebDriver driver, String customLinkName) {
		waitUntilVisible(driver, customLinkNameInput);
		enterText(driver, customLinkNameInput, customLinkName);
	}
	
	public void enterCustomLinkUrl(WebDriver driver, String customLinkUrl) {
		waitUntilVisible(driver, customLinkURLInput);
		enterText(driver, customLinkURLInput, customLinkUrl);
	}

	public void clickSaveCustomLinkButton(WebDriver driver) {
		waitUntilVisible(driver, saveCustomLinkBtn);
		clickElement(driver, saveCustomLinkBtn);
	}
	
	public void createCustomLink(WebDriver driver, String customLinkName, String customLinkUrl) {
		scrollToElement(driver, addCustomLinkBtn);
		clickAddCustomLinkButton(driver);
		enterCustomLinkName(driver, customLinkName);
		enterCustomLinkUrl(driver, customLinkUrl);
		clickSaveCustomLinkButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}	
	/*******Custom Link section ends here******/
	
	/*******Event Subject section starts here******/
	public void clickAddEventSubjectButton(WebDriver driver) {
		waitUntilVisible(driver, addEventSubjectBtn);
		clickElement(driver, addEventSubjectBtn);
	}

	public void enterEventSubjectName(WebDriver driver, String eventSubjectName) {
		waitUntilVisible(driver, eventSubjectNameInput);
		enterText(driver, eventSubjectNameInput, eventSubjectName);
	}
	
	public void clickSaveEventSubjectButton(WebDriver driver) {
		waitUntilVisible(driver, saveEventSubjectBtn);
		clickElement(driver, saveEventSubjectBtn);
	}
	
	public void createEventSubject(WebDriver driver, String eventSubjectName) {
		scrollToElement(driver, addEventSubjectBtn);
		clickAddEventSubjectButton(driver);
		enterEventSubjectName(driver, eventSubjectName);
		clickSaveEventSubjectButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		saveAcccountSettings(driver);
	}	
	
	public void updateEventSubjectRecords(WebDriver driver, String oldRecordName, String newRecordName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldRecordName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterEventSubjectName(driver, newRecordName);
		clickSaveEventSubjectButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	/*******Event Subject section ends here******/
	
	/*******Task and Note Subject section starts here******/
	public void clickAddTask_NoteSubjectButton(WebDriver driver) {
		waitUntilVisible(driver, addTaskNoteSubjectBtn);
		clickElement(driver, addTaskNoteSubjectBtn);
	}

	public void createTask_NoteSubject(WebDriver driver, String eventSubjectName) {
		scrollToElement(driver, addTaskNoteSubjectBtn);
		clickAddTask_NoteSubjectButton(driver);
		enterEventSubjectName(driver, eventSubjectName);
		clickSaveEventSubjectButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		saveAcccountSettings(driver);
	}	
	/*******Task and Note Subject section ends here******/
	
	/*******Call Scripts section starts here ******/
	public void createCallScripts(WebDriver driver, String callScriptName, String callScriptDescription, String number) {
		scrollToElement(driver, addCallScriptsBtn);
		clickAddCallScriptsButton(driver);
		enterCallScriptName(driver, callScriptName);
		enterCallScriptDescription(driver, callScriptDescription);
		clickNextCallScriptsButton(driver);
		if(Strings.isNullOrEmpty(number))
			selectSmartNumber(driver, 0);
		else
			searchNumberInCallScripts(driver, number);
		clickNextCallScriptsButton(driver);
		clickSaveCallScriptsButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void updateCallScriptsRecords(WebDriver driver, String oldcallScriptName, String newcallScriptName, String newcallScriptDescription) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldcallScriptName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterCallScriptName(driver, newcallScriptName);
		enterCallScriptDescription(driver, newcallScriptDescription);
		clickNextCallScriptsButton(driver);
		selectSmartNumber(driver, 1);
		clickNextCallScriptsButton(driver);
		clickSaveCallScriptsButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void clickAddCallScriptsButton(WebDriver driver) {
		waitUntilVisible(driver, addCallScriptsBtn);
		clickElement(driver, addCallScriptsBtn);
	}
	
	public void enterCallScriptName(WebDriver driver, String callScriptName) {
		waitUntilVisible(driver, callScriptNameInput);
		enterText(driver, callScriptNameInput, callScriptName);
	}

	public void enterCallScriptDescription(WebDriver driver, String callScriptDesciption) {
		waitUntilVisible(driver, callScriptDescription);
		enterText(driver, callScriptDescription, callScriptDesciption);
	}

	public void clickSaveCallScriptsButton(WebDriver driver) {
		waitUntilVisible(driver, saveCallScriptsBtn);
		clickElement(driver, saveCallScriptsBtn);
	}
	
	public void clickNextCallScriptsButton(WebDriver driver) {
		waitUntilVisible(driver, nextCallScriptsBtn);
		clickElement(driver, nextCallScriptsBtn);
	}
	
	public boolean checkCallScriptsSaved(WebDriver driver, String callScriptName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", callScriptName));
		return isElementVisible(driver, updateRecordLoc, 5);
	}
	
	public void selectSmartNumber(WebDriver driver, int index) {
		waitUntilVisible(driver, callScriptsSearchTab);
		getElements(driver, callScriptSmartNumberList).get(index).click();
	}
	
	public void searchNumberInCallScripts(WebDriver driver, String number){
		waitUntilVisible(driver, callScriptsSearchTab);
		enterText(driver, callScriptsSearchTab, number);
		checkBoxFilter(driver, number);
	}
	
	/*******Call Scripts section ends here******/

	/*******SMS Template section starts here******/
	public void enableSMSTemplateSetting(WebDriver driver){
		if(!findElement(driver, smsTemplateCheckBox).isSelected()) {
			clickElement(driver, smsTemplateOffButton);
			System.out.println("Enabled SMS Template Setting");
		} else {
			System.out.println("Already enabled SMS Template setting");
		}
	}
	
	public void disableSMSTemplateSetting(WebDriver driver){
		if(findElement(driver, smsTemplateCheckBox).isSelected()) {
			clickElement(driver, smsTemplateONButton);
			System.out.println("Disabled SMS Template Setting");
		} else {
			System.out.println("Already disabled SMS Template setting");
		}
	}
	
	
	public void clickAddSMSemplateButton(WebDriver driver) {
		waitUntilVisible(driver, addSMSTemplateBtn);
		clickElement(driver, addSMSTemplateBtn);
	}

	public void createSMSTemplate(WebDriver driver, String sMSTemplateName, String sMSTemplate, String sMSTemplateGroup) {
		scrollToElement(driver, addSMSTemplateBtn);
		clickAddSMSemplateButton(driver);
		enterSmsTemplateName(driver, sMSTemplateName);
		enterSmsTemplate(driver, sMSTemplate);
		if (Strings.isNotNullAndNotEmpty(sMSTemplateGroup)) {
				enterTextAndSelectFromDropDown(driver, sMSTemplateGroupDrpDwn, sMSTemplateGroupTxtBox, sMSTemplateGroupOptions, sMSTemplateGroup);
		}
		clickSaveSmsTemplateButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void enterSmsTemplateName(WebDriver driver, String sMSTemplateName) {
		waitUntilVisible(driver, sMSTemplateNameTxtBox);
		enterText(driver, sMSTemplateNameTxtBox, sMSTemplateName);
	}
	
	public void enterSmsTemplate(WebDriver driver, String smsTemplate) {
		waitUntilVisible(driver, sMSTemplateDesciption);
		enterText(driver, sMSTemplateDesciption, smsTemplate);
	}
	
	public void clickSaveSmsTemplateButton(WebDriver driver) {
		waitUntilVisible(driver, smsTemplateSaveBtn);
		clickElement(driver, smsTemplateSaveBtn);
	}
	
	public boolean checkSmsTemplateSaved(WebDriver driver, String sMSTemplateName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", sMSTemplateName));
		return isElementVisible(driver, updateRecordLoc, 5);
	}
	
	/*******SMS Template section ends here******/
	
	public void enableCustomUserStatus(WebDriver driver){
		if(!findElement(driver, customUserStatusCheckbox).isSelected()) {
			clickElement(driver, customUserStatusToggleOffBtn);
			System.out.println("Enabled Custom User Status");
		} else {
			System.out.println("Already enabled Custom User Status");
		}
	}
	
	public void disableCustomUserStatus(WebDriver driver){
		if(findElement(driver, customUserStatusCheckbox).isSelected()) {
			clickElement(driver, customUserStatusToggleOnBtn);
			System.out.println("disabled Custom User Status");
		} else {
			System.out.println("Already disabled Custom User Status");
		}
	}
	
	/*******Call Notes Template section starts here******/
	public void enableCallNotesTemplateSetting(WebDriver driver){
		if(!findElement(driver, callNotesCheckBox).isSelected()) {
			clickElement(driver, callNotesToggleOffBtn);
			System.out.println("Enabled call Notes Template Setting");
		} else {
			System.out.println("Already enabled call Notes Template setting");
		}
	}
	
	public void disableCallNotesTemplateSetting(WebDriver driver){
		if(findElement(driver, callNotesCheckBox).isSelected()) {
			clickElement(driver, callNotesToggleOnBtn);
			System.out.println("disabled call Notes Template Setting");
		} else {
			System.out.println("Already disabled call Notes Template setting");
		}
	}
	
	public void clickAddCallNotesTemplateButton(WebDriver driver) {
		waitUntilVisible(driver, addCallNotesTemplateBtn);
		clickElement(driver, addCallNotesTemplateBtn);
		dashBoard.isPaceBarInvisible(driver);
	}

	public void createCallNotesTemplate(WebDriver driver, String callNoteName, String callNoteTemplate, String callNoteGroup) {
		scrollToElement(driver, addCallNotesTemplateBtn);
		clickAddCallNotesTemplateButton(driver);
		enterCallNoteName(driver, callNoteName);
		enterCallNoteTemplate(driver, callNoteTemplate);
		if (Strings.isNotNullAndNotEmpty(callNoteGroup)) {
				enterTextAndSelectFromDropDown(driver, callNotesGroupsTab, callNotesGroupsTab, callNotesGroupsList, callNoteGroup);
		}
		clickSaveCallNotesButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		saveAcccountSettings(driver);
	}
	
	public void enterCallNoteName(WebDriver driver, String callNoteName) {
		waitUntilVisible(driver, callNotesNameInput);
		enterText(driver, callNotesNameInput, callNoteName);
	}
	
	public void enterCallNoteTemplate(WebDriver driver, String callNoteTemplate) {
		waitUntilVisible(driver, callNotesTemplateInput);
		enterText(driver, callNotesTemplateInput, callNoteTemplate);
	}
	
	public void clickSaveCallNotesButton(WebDriver driver) {
		waitUntilVisible(driver, saveCallNoteBtn);
		clickElement(driver, saveCallNoteBtn);
	}
	
	public boolean checkCallNotesTemplateSaved(WebDriver driver, String callNoteName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", callNoteName));
		return isElementVisible(driver, updateRecordLoc, 5);
	}

	public void updateCallNotesTemplateRecords(WebDriver driver, String oldcallNoteName, String newcallNoteName, String oldcallNoteTemplate, String newcallNoteTemplate) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldcallNoteName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterCallNoteName(driver, newcallNoteName);
		enterCallNoteTemplate(driver, newcallNoteTemplate);
		clickSaveCallNotesButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettings(driver);
	}
	
	public void checkBoxFilter(WebDriver driver, String filterValue){
		By selectFilterCheckBox = By.xpath(checkBoxRecordSelect.replace("$recordName$", filterValue));
		waitUntilVisible(driver, selectFilterCheckBox);
		clickElement(driver, selectFilterCheckBox);
	}
	
	/*******Call Notes Template section ends here******/
	
	/*******Web Leads Rule section Starts here******/
	
	public void selectWebLeadsFieldName(WebDriver driver, String fieldName){
		enterTextAndSelectFromDropDown(driver, webLeadsFieldNameDrpDwn, webLeadsFieldNameTxtBox, webLeadsFieldNameOptions, fieldName);
	}
	
	public void selectWebLeadsTeam(WebDriver driver, String teamName){
		enterTextAndSelectFromDropDown(driver, webLeadsTeamsDrpDwn, webLeadsTeamsTxtBox, webLeadsTeamsOptions, teamName);
	}
	
	public void enterWebLeadsFieldValue(WebDriver driver, String fieldValue){
		enterText(driver, webLeadsFieldValue, fieldValue);
	}
	
	public void clickWebLeadsAddRuleButton(WebDriver driver){
		clickElement(driver, webLeadsAddRuleBtn);
	}
	
	public void selectWebLeadRuleVisibility(WebDriver driver, String visibility) {
		selectFromDropdown(driver, webLeadVisibilityDropDown, SelectTypes.visibleText, visibility);
	}
	
	public void createWebLeadRule(WebDriver driver, String fieldName, String fieldValue, String teamName, String visibility){
		selectWebLeadsFieldName(driver, fieldName);
		enterWebLeadsFieldValue(driver, fieldValue);
		selectWebLeadsTeam(driver, teamName);
		clickWebLeadsAddRuleButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		assertEquals(0, getWebLeadsRuleRow(driver, fieldName, fieldValue, teamName));
		selectWebLeadRuleVisibility(driver, visibility);
		saveAcccountSettings(driver);
	}

	public int getWebLeadsRuleRow(WebDriver driver, String fieldName, String fieldValue, String teamName){
		try{
			List<WebElement> webLeadsRules = getWebLeadsRulesList(driver);
		int i=0;
		for (WebElement webLeadsRule : webLeadsRules) {
			if (webLeadsRule.findElement(webLeadNameCell).getText().equals(fieldName)
					&& webLeadsRule.findElement(webLeadValueCell).getText().equals(fieldValue)
					&& webLeadsRule.findElement(webLeadTeamCell).getText().equals(teamName)) {
				return i;
			}
			i++;
		}
		return -1;
		}catch(Exception e){
			return -1;
		}
	}
	
	public void deleteWebLeadRule(WebDriver driver, String fieldName, String fieldValue, String teamName){
		int index = getWebLeadsRuleRow(driver, fieldName, fieldValue, teamName);
		if(index > -1){
			getWebLeadsRulesList(driver).get(index).findElement(deleteButton).click();
			confirmDeleteAction(driver);
		}
	}
	
	public List<WebElement> getWebLeadsRulesList(WebDriver driver){
		List<WebElement> webLeadsRules = getElements(driver, webLeadsRuleRow);
		return webLeadsRules;
	}
	/*******Web Leads Rule section ends here******/
	
	/*******Compliance Hours Rule section starts here******/
	private static String complaineHoursDateFormatString = "hh:mma dd/MM/yyyy E";
	private static SimpleDateFormat compainceHourDateFormat = new SimpleDateFormat(complaineHoursDateFormatString);
	
	public void enableComplainceHourSetting(WebDriver driver){
		if(!findElement(driver, complainceHoursCheckbox).isSelected()) {
			clickElement(driver, complainceHoursToggleOffBtn);
			System.out.println("Enabled Complaince Hours Setting");
		} else {
			System.out.println("Already enabled Complaince Hours setting");
		}
	}
	
	public void disableComplainceHourSetting(WebDriver driver){
		if(findElement(driver, complainceHoursCheckbox).isSelected()) {
			clickElement(driver, complainceHoursToggleOnBtn);
			System.out.println("disabled Complaince Hours Setting");
		} else {
			System.out.println("Already disabled Complaince Hours setting");
		}
	}
	
	public void verifydefaultComplainceHoursDayTable(WebDriver driver) {
		//verify headers
		List<WebElement> compHoursTableHeaders = getElements(driver, complainceHourDaysHeaders);
		assertTrue(compHoursTableHeaders.get(0).getText().trim().equals("Day"));
		assertTrue(compHoursTableHeaders.get(1).getText().trim().equals("Hours"));
		assertTrue(compHoursTableHeaders.get(2).getText().trim().equals("Update"));
		
		//verify rows data
		List<WebElement> compHoursTableRows = getElements(driver, complainceHourDaysRow);
		for (WebElement compHoursTableRow : compHoursTableRows) {
		 getElementsText(driver, compHoursTableRow.findElement(dayRestrictionColumn)).trim().equals("No Restrictions");
		 assertTrue(isElementVisible(driver, compHoursTableRow.findElement(updateButton), 0));
		}
		assertTrue(compHoursTableRows.get(0).findElement(dayNameColumn).getText().trim().equals("Sunday"));
		assertTrue(compHoursTableRows.get(1).findElement(dayNameColumn).getText().trim().equals("Monday"));
		assertTrue(compHoursTableRows.get(2).findElement(dayNameColumn).getText().trim().equals("Tuesday"));
		assertTrue(compHoursTableRows.get(3).findElement(dayNameColumn).getText().trim().equals("Wednesday"));
		assertTrue(compHoursTableRows.get(4).findElement(dayNameColumn).getText().trim().equals("Thursday"));
		assertTrue(compHoursTableRows.get(5).findElement(dayNameColumn).getText().trim().equals("Friday"));
		assertTrue(compHoursTableRows.get(6).findElement(dayNameColumn).getText().trim().equals("Saturday"));
	}
	
	public void setComplainceDayAndPeriod(WebDriver driver, String timeZone, Boolean insideComplainceHours) {
		Date startTime = null;
		Date endTime = null;
		
		if(insideComplainceHours) {
			startTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), 0);
			endTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), 120);
		}else {
			startTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), -120);
			endTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), -60);
			
			String endTimeInTZ = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(endTime).toString(), timeZone, complaineHoursDateFormatString);
			String endDay = endTimeInTZ.substring(endTimeInTZ.length() - 3, endTimeInTZ.length());
			
			String todayTimeInTZ = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(HelperFunctions.GetCurrentDateTimeObj()).toString(), timeZone, complaineHoursDateFormatString);
			String today = todayTimeInTZ.substring(todayTimeInTZ.length() - 3, todayTimeInTZ.length());
			
			if(Integer.parseInt(Days.valueOf(today).displayName()) >  Integer.parseInt(Days.valueOf(endDay).displayName())) {
				startTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), +60);
				endTime = HelperFunctions.addMinutesToDate(HelperFunctions.GetCurrentDateTimeObj(), +120);
			}
		}
		String startTimeInTZ = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(startTime).toString(), timeZone, complaineHoursDateFormatString);
		String endTimeInTZ = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(endTime).toString(), timeZone, complaineHoursDateFormatString);
		
		String startDay = startTimeInTZ.substring(startTimeInTZ.length() - 3, startTimeInTZ.length());
		String endDay = endTimeInTZ.substring(endTimeInTZ.length() - 3, endTimeInTZ.length());
		
		if(startDay.equals(endDay)) {
			enterComplainceHours(driver, Days.valueOf(startDay), startTimeInTZ.substring(0, 7), endTimeInTZ.substring(0, 7), false);	
			saveComplainceHourSettings(driver);
			verifyComplainceHoursForDay(driver, Days.valueOf(startDay), startTimeInTZ.substring(0, 7), endTimeInTZ.substring(0, 7));
		}else {
			enterComplainceHours(driver, Days.valueOf(startDay), startTimeInTZ.substring(0, 7), "11:59PM", false);
			saveComplainceHourSettings(driver);
			verifyComplainceHoursForDay(driver, Days.valueOf(startDay), startTimeInTZ.substring(0, 7), "11:59PM");
			enterComplainceHours(driver, Days.valueOf(endDay), "12:00AM", endTimeInTZ.substring(0, 7), false);
			saveComplainceHourSettings(driver);
			verifyComplainceHoursForDay(driver, Days.valueOf(endDay), "12:00AM", endTimeInTZ.substring(0, 7));
		}
		saveAcccountSettings(driver);
	}
	
	public void verifyComplainceHoursForDay(WebDriver driver, Days day, String startTime, String endTime) {

		//verify rows data
		List<WebElement> compHoursTableRows = getElements(driver, complainceHourDaysRow);
		int dayNumber = Integer.parseInt(day.displayName());
		startTime	= startTime.replace(" ", "").replace("AM", "am").replace("am", " am").replace("PM", "pm").replace("pm", " pm");
		endTime 	= endTime.replace(" ", "").replace("AM", "am").replace("am", " am").replace("PM", "pm").replace("pm", " pm");

		assertEquals(compHoursTableRows.get(dayNumber).findElement(dayRestrictionColumn).getText().trim(), (startTime + " - " + endTime));
	}
	
	public void removeComplainceHoursSettings(WebDriver driver) {
		
		//verify rows data
		List<WebElement> compHoursTableRows = getElements(driver, complainceHourDaysRow);
		
		for (int i = 0; i < compHoursTableRows.size(); i++) {
			if (!compHoursTableRows.get(i).findElement(dayRestrictionColumn).getText().equals("No Restrictions")) {
				openComplainceHourModalBox(driver, Days.values()[i]);
				
				if(findElement(driver, compHourAllDayCheckBox).isSelected()) {
					findElement(driver, compHourAllDayCheckBox).click();
				}
				
				clickComplianceTimeHour(driver, compHourStartTimeBox);
				enterBackspace(driver, compHourStartTimeBox);
				clickComplianceTimeHour(driver, compHourEndTimeBox);
				enterBackspace(driver, compHourEndTimeBox);
				
				saveComplainceHourSettings(driver);
				compHoursTableRows = getElements(driver, complainceHourDaysRow);
			}
		}
	}
	
	public void openComplainceHourModalBox(WebDriver driver, Days day) {
		int dayNumber = Integer.parseInt(day.displayName());
		List<WebElement> compHoursTableRows = getElements(driver, complainceHourDaysRow);
		compHoursTableRows.get(dayNumber).findElement(updateButton).click();
		
		idleWait(2);
	}
	
	public void enterComplainceHours(WebDriver driver, Days day, String startTime, String endTime, boolean allDay ) {
		openComplainceHourModalBox(driver, day);
		startTime	= startTime.replace("am", "AM").replace("pm", "PM").replace(" ", "");
		endTime 	= endTime.replace("am", "AM").replace("pm", "PM").replace(" ", "");
		
		clickComplianceTimeHour(driver, compHourStartTimeBox);
		clearComplianceHoursValues(driver, compHourStartTimeBox);
		clickComplianceTimeHour(driver, compHourEndTimeBox);
		enterText(driver, compHourStartTimeBox, startTime);
		
		clickComplianceTimeHour(driver, compHourEndTimeBox);
		clearComplianceHoursValues(driver, compHourEndTimeBox);
		clickComplianceTimeHour(driver, compHourEndTimeBox);
		enterText(driver, compHourEndTimeBox, endTime);
		
		if(allDay) {
			findElement(driver, compHourAllDayCheckBox).click();
			assertFalse(findElement(driver, compHourStartTimeBox).isEnabled());
			assertFalse(findElement(driver, compHourEndTimeBox).isEnabled());
		}
	}
	
	public void setComplainceHourTimePeriod(WebDriver driver, Days day, String startTime, String endTime, boolean allDay ) {
		openComplainceHourModalBox(driver, day);
		
		//Set Compliance hour start Time if not null 
		if(!startTime.isEmpty() && startTime != null) {
			
			//Set Compliance hour start Time
			setComplainceTime(driver, CompliancTimeType.StartTime, startTime);
		}
		
		//Set Compliance hour end Time if not null 
		if(!endTime.isEmpty() && endTime != null) {
			
			//Set Compliance hour end Time
			setComplainceTime(driver, CompliancTimeType.EndTime, endTime);
		}
		
		if(allDay) {
			findElement(driver, compHourAllDayCheckBox).click();
		}
	}
	
	public void setComplainceTime(WebDriver driver, CompliancTimeType timeType, String time) {
		
		//Separate start time values
		int hours = Integer.parseInt(time.substring(0, 2));
		int mins = Integer.parseInt(time.substring(3, 5));
		int meridiem = (time.substring(6, 8).equals("am") ||time.substring(6, 8).equals("AM")) ? 1 : 2;
		By timeTypeLocator = null;
		
		switch (timeType) {
		case StartTime:
			timeTypeLocator = compHourStartTimeBox;
			break;

		case EndTime:
			timeTypeLocator = compHourEndTimeBox;
			break;
		default:
			break;
		}
		
		clickComplianceTimeHour(driver, timeTypeLocator);
		clearComplianceHoursValues(driver, timeTypeLocator);
		
		clickComplianceTimeHour(driver, timeTypeLocator);
		increaseComplainceHourValues(driver, timeTypeLocator, hours);
		
		clickComplianceTimeMins(driver, timeTypeLocator);
		increaseComplainceHourValues(driver, timeTypeLocator, mins);
		
		clickComplianceTimeMeridian(driver, timeTypeLocator);
		increaseComplainceHourValues(driver, timeTypeLocator, meridiem);
	}
	
	private void clickComplianceTimeHour(WebDriver driver, By startOrEndTime) {
		clickElementCordinates(driver, startOrEndTime, -55, 0);
	}
	
	private void clickComplianceTimeMins(WebDriver driver, By startOrEndTime) {
		clickElementCordinates(driver, startOrEndTime, -30, 0);
	}
	
	private void clickComplianceTimeMeridian(WebDriver driver, By startOrEndTime) {
		clickElementCordinates(driver, startOrEndTime, -10, 0);
	}
	
	private void increaseComplainceHourValues(WebDriver driver, By startOrEndTime, int value) {
		for (int i = 0; i < value; i++) {
			clickElementCordinates(driver, startOrEndTime, 55, -4);
		}
	}
	
	/*private void decreaseComplainceHourValues(WebDriver driver, By startOrEndTime, int value) {
		for (int i = 0; i < value; i++) {
			clickElementCordinates(driver, startOrEndTime, 55, 4);
		}
	}*/	
	
	private void clearComplianceHoursValues(WebDriver driver, By startOrEndTime) {
		clickElementCordinates(driver, startOrEndTime, 40, 0);
	}
	
	public String getComplainceHourStartTime(WebDriver driver) {
		return getAttribue(driver, compHourStartTimeBox, ElementAttributes.value);
	}
	
	public String getComplainceHourEndTime(WebDriver driver) {
		return getAttribue(driver, compHourEndTimeBox, ElementAttributes.value);
	}
	
	public void saveComplainceHourSettings(WebDriver driver) {
		clickElement(driver, modalWindowSaveButton);
		waitUntilInvisible(driver, modalWindowSaveButton);
	}
	
	public void closeComplainceHourWindow(WebDriver driver) {
		clickElement(driver, compHourCloseButton);
		waitUntilInvisible(driver, modalWindowSaveButton);
	}
	/*******Compliance Hours Rule section ends here******/
	
	public boolean getCustomUserStatus(WebDriver driver) {
		scrollToElement(driver, customStatusCheckBox);
		return findElement(driver, customStatusCheckBox).isSelected();
	}
	
	public void enableCustomUserStatusSetting(WebDriver driver){
		scrollToElement(driver, customStatusCheckBox);
		if(!findElement(driver, customStatusCheckBox).isSelected()) {
			clickElement(driver, customStatusToggleOffBtn);
			System.out.println("Enabled Custom Users Status Setting");
		} else {
			System.out.println("Already enabled Custom Users setting");
		}
	}
	
	public void disableCustomUserStatusSetting(WebDriver driver){
		if(findElement(driver, customStatusCheckBox).isSelected()) {
			clickElement(driver, customStatusToggleOnBtn);
			waitUntilInvisible(driver, allowUsersToggleOffBtn);
			waitUntilInvisible(driver, customStatusRow);
			System.out.println("disabled Custom Users Status Setting");
		} else {
			System.out.println("Already disabled Custom Users Status setting");
		}
	}
	
	public String getCustomUserStatusDescription(WebDriver driver) {
		return getElementsText(driver, customstatusDescription).trim();
	}
	
	public boolean getAllowUsersOverrideTimeSettingStatus(WebDriver driver) {
		return findElement(driver, allowUsersCheckBox).isSelected();
	}
	
	public void enableAllowUsersOverrideTimeSetting(WebDriver driver){
		if(!getAllowUsersOverrideTimeSettingStatus(driver)) {
			clickElement(driver, allowUsersToggleOffBtn);
			System.out.println("Enabled Allow Users to Override Setting");
		} else {
			System.out.println("Already enabled Allow Users to Override setting");
		}
	}
	
	public void disableAllowUsersOverrideTimeSetting(WebDriver driver){
		if(getAllowUsersOverrideTimeSettingStatus(driver)) {
			clickElement(driver, allowUsersToggleOnBtn);
			System.out.println("disabled Allow Users to Override Setting");
		} else {
			System.out.println("Already disabled Allow Users to Override  setting");
		}
	}
	
	public boolean isAddNewCustomStatusBtnVisible(WebDriver driver) {
		return isElementVisible(driver, addCustomStatusBtn, 0);
	}
	
	public void clickAddNewCustomStatusBtn(WebDriver driver) {
		clickElement(driver, addCustomStatusBtn);
		waitUntilVisible(driver, modalWindowSaveButton);
	}
	
	public void enterCustomSatusName(WebDriver driver, String customStatusName) {
		enterText(driver, customStatusNameModal, customStatusName);
	}
	
	public void appendCustomSatusName(WebDriver driver, String customStatusName) {
		appendText(driver, customStatusNameModal, customStatusName);
	}
	
	public String getCustomSatusEditName(WebDriver driver) {
		return getAttribue(driver, customStatusNameModal, ElementAttributes.value);
	}
	
	public void selectCustomSatusBusyStatus(WebDriver driver, String customSatusBusyValue) {
		selectFromDropdown(driver, customStatusBusyModal, SelectTypes.visibleText, customSatusBusyValue);
	}
	
	public String getCustomSatusBusyStatus(WebDriver driver) {
		return getSelectedValueFromDropdown(driver, customStatusBusyModal);
	}
	
	public void enterCustomSatusDesc(WebDriver driver, String customStatusDesc) {
		enterText(driver, customStatusDescModal, customStatusDesc);
	}
	
	public void appendCustomSatusDesc(WebDriver driver, String customStatusDesc) {
		appendText(driver, customStatusDescModal, customStatusDesc);
	}
	
	public String getCustomSatusEditDescription(WebDriver driver) {
		return getAttribue(driver, customStatusDescModal, ElementAttributes.value);
	}
	
	public void selectCustomSatusDefaultTime(WebDriver driver, String customSatusDefaultTime) {
		selectFromDropdown(driver, customStatusTimingModal, SelectTypes.visibleText, customSatusDefaultTime);
	}
	
	public String getCustomSatusDefaultTime(WebDriver driver) {
		return getSelectedValueFromDropdown(driver, customStatusTimingModal);
	}
	
	public void selectCustomSatusTeam(WebDriver driver, String customSatusTeam) {
		enterTextAndSelectFromDropDown(driver, customStatusTeamDropdown, customStatusTeamTextBox, customStatusTeamOption, customSatusTeam);
	}
	
	public String getSelectedCustomStatusTeam(WebDriver driver) {
		return getAttribue(driver, customStatusTeamTextBox, ElementAttributes.Placeholder);
	}
	
	public void addNewCustomStatus(WebDriver driver, HashMap<CustomStatusFields, String> customStatusDetails) {
		clickAddNewCustomStatusBtn(driver);
		setCustomStatusFieldsValues(driver, customStatusDetails);
		saveComplainceHourSettings(driver);
	}
	
	public void setCustomStatusFieldsValues(WebDriver driver, HashMap<CustomStatusFields, String> customStatusDetails) {
		if(customStatusDetails.get(CustomStatusFields.StatusName) != null) 
			enterCustomSatusName(driver, customStatusDetails.get(CustomStatusFields.StatusName));
		
		if(customStatusDetails.get(CustomStatusFields.Busy) != null) 
			selectCustomSatusBusyStatus(driver, customStatusDetails.get(CustomStatusFields.Busy));
		
		if(customStatusDetails.get(CustomStatusFields.Description) != null) 
			enterCustomSatusDesc(driver, customStatusDetails.get(CustomStatusFields.Description));
		
		if(customStatusDetails.get(CustomStatusFields.Time) != null) 
			selectCustomSatusDefaultTime(driver, customStatusDetails.get(CustomStatusFields.Time));
		
		if(customStatusDetails.get(CustomStatusFields.Team) != null && !customStatusDetails.get(CustomStatusFields.Team).equals("All")) 
			selectCustomSatusTeam(driver, customStatusDetails.get(CustomStatusFields.Team));
	}
	
	public WebElement getCustomStatusRow(WebDriver driver, String customStatus) {
		List<WebElement> customStatusesRows = getInactiveElements(driver, customStatusRow);
		
		for (WebElement customStatusesRow : customStatusesRows) {
			if (customStatusesRow.findElement(customStatusNameColumn).getText().equals(customStatus))
				return customStatusesRow;
		}
		return null;
	}
	
	public void clickCustomStatusTeamName(WebDriver driver, String customStatusName) {
		WebElement customStatusRowElement = getCustomStatusRow(driver, customStatusName);
		customStatusRowElement.findElement(customStatusTeamColumn).click();
		switchToTab(driver, getTabCount(driver));
	}
	
	public boolean isCustomStausDeleteButtonVisible(WebDriver driver, String customStatus) {
		try {
			getCustomStatusRow(driver, customStatus).findElement(customStatusDeleteBtn);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public boolean isCustomStausUpdateButtonVisible(WebDriver driver, String customStatus) {
		try {
			getCustomStatusRow(driver, customStatus).findElement(customStatusUpdateBtn);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void clickCustomStausUpdateButton(WebDriver driver, String customStatus) {
		getCustomStatusRow(driver, customStatus).findElement(customStatusUpdateBtn).click();
		waitUntilVisible(driver, modalWindowSaveButton);
	}
	
	public void deleteAllCustomUserStatus(WebDriver driver) {
		List<WebElement> customStatusesRows = getInactiveElements(driver, customStatusRow);
		int numberOfRows = customStatusesRows.size();
		if(numberOfRows > 2) {
			for (int i = 2; i < numberOfRows; i++) {
				customStatusesRows.get(i).findElement(customStatusDeleteBtn).click();			
				waitUntilVisible(driver, deleteRecordConfirmBtn);
				clickElement(driver, deleteRecordConfirmBtn);
				waitUntilInvisible(driver, deleteRecordConfirmBtn);
				customStatusesRows = getInactiveElements(driver, customStatusRow);	
			}
		}
	}
	
	public void deleteCustomStatus(WebDriver driver, String customStatus) {
		WebElement customStatusRowElement = getCustomStatusRow(driver, customStatus);
		
		clickElement(driver, customStatusRowElement.findElement(customStatusDeleteBtn));
		waitUntilVisible(driver, deleteRecordConfirmBtn);
		clickElement(driver, deleteRecordConfirmBtn);
		waitUntilInvisible(driver, deleteRecordConfirmBtn);
		assertEquals(getCustomStatusRow(driver, customStatus), null);
	}
	
	public void verifyAddedCustomStatusDetails(WebDriver driver, HashMap<CustomStatusFields, String> customStatusDetails) {	
		WebElement customStatusRowElement = getCustomStatusRow(driver, customStatusDetails.get(CustomStatusFields.StatusName));
		
		if(customStatusDetails.get(CustomStatusFields.StatusName) != null) 
			assertEquals(customStatusRowElement.findElement(customStatusNameColumn).getText(), customStatusDetails.get(CustomStatusFields.StatusName));
		
		if(customStatusDetails.get(CustomStatusFields.Busy) != null) 
			assertEquals(customStatusRowElement.findElement(customStatusBusyColumn).getText(), customStatusDetails.get(CustomStatusFields.Busy));
		
		if(customStatusDetails.get(CustomStatusFields.Description) != null) 
			assertEquals(customStatusRowElement.findElement(customStatusDescrColumn).getText(), customStatusDetails.get(CustomStatusFields.Description));
		
		if(customStatusDetails.get(CustomStatusFields.Time) != null) {
			String customStatusTimeValue = null;
			switch (customStatusDetails.get(CustomStatusFields.Time)) {
			case ("5 Minutes"):
				customStatusTimeValue = "5";
				break;
			case ("15 Minutes (Default)"):
				customStatusTimeValue = "15";
				break;
			case ("30 Minutes"):
				customStatusTimeValue = "30";
				break;
			case ("1 hour"):
				customStatusTimeValue = "60";
				break;
			case ("Does Not Expire"):
				customStatusTimeValue = "No Expiration";
				break;
			default:
				break;
			}
			assertEquals(customStatusRowElement.findElement(customStatusTimeColumn).getText(), customStatusTimeValue);
		}

		if(customStatusDetails.get(CustomStatusFields.Team) != null) 
			if(isElementVisible(driver, customStatusToggleOnBtn, 0)) {
				assertEquals(customStatusRowElement.findElement(customStatusTeamColumn).getText(), customStatusDetails.get(CustomStatusFields.Team));
			}else {
				System.out.println("User is on team page");
			}
	}
	/*******Custom User Status section starts here******/

	/*******Call Disposition section starts here******/
	
	public void enableManageCallDispositions(WebDriver driver) {
		if(!findElement(driver, manageCallDispositionCheckBox).isSelected()) {
			clickElement(driver, manageCallDispositionsToggleOffBtn);
			System.out.println("Enabled manage call disposition");
			waitUntilVisible(driver, importCallDispositionDataCheckbox);
			clickElement(driver, importCallDispositionDataCheckbox);
			waitUntilVisible(driver, acceptCheckBox);
			clickElement(driver, acceptCheckBox);
			waitUntilVisible(driver, acceptBtn);
			clickElement(driver, acceptBtn);
			dashBoard.isPaceBarInvisible(driver);
		} else {
			System.out.println("Already enabled manage call disposition");
		}
	}
	
	public void disableManageCallDispositions(WebDriver driver) {
		if(findElement(driver, manageCallDispositionsToggleOnBtn).isDisplayed()) {
			clickElement(driver, manageCallDispositionsToggleOnBtn);
			System.out.println("Disabled manage call disposition");
		} else {
			System.out.println("Already disabled manage call disposition");
		}
	}
	
	public void verifyCallDispositionSectionNotVisible(WebDriver driver){
		assertFalse(isElementVisible(driver, callDispositionSection, 5));
		assertFalse(isListElementsVisible(driver, callDispositionTable, 5));
	}
	
	public void isCallDispositionSectionVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, callDispositionSection, 5));
		assertTrue(isListElementsVisible(driver, callDispositionTable, 5));
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			assertTrue(isElementVisible(driver, synctToSalesForceBtn, 5));
		}
		else if (SupportBase.drivername.toString().equals("supportDriver")) {
			assertFalse(isElementVisible(driver, synctToSalesForceBtn, 5));
		}
	}
	
	public void addCallDisposition(WebDriver driver, String callDisposition) {
		waitUntilVisible(driver, addCallDispositionIcon);
		clickElement(driver, addCallDispositionIcon);
		waitUntilVisible(driver, callDispositionTextBox);
		enterText(driver, callDispositionTextBox, callDisposition);
		clickElement(driver, addCallDispositionBtn);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public boolean isCallDispositionExists(WebDriver driver, String callDisposition) {
		dashBoard.isPaceBarInvisible(driver);
		idleWait(1);
		By callDisposistionLoc = By.xpath(callDispositionPage.replace("$callDisposition$", callDisposition));
		return isElementVisible(driver, callDisposistionLoc, 5);
	}
	
	public void deleteCallDisposition(WebDriver driver, String callDisposition) {
		dashBoard.isPaceBarInvisible(driver);
		By callDisposistionLoc = By.xpath(callDispositionDeletePage.replace("$callDisposition$", callDisposition));
		clickElement(driver, callDisposistionLoc);
		confirmDeleteAction(driver);
	}
	
	/*******Call Disposition section ends here******/

	/*******Related Records section starts here******/
	
	public void addRelatedRecordRulesIcon(WebDriver driver) {
		waitUntilVisible(driver, addRulesIcon);
		clickElement(driver, addRulesIcon);
	}
	
	public int selectMachType_Query(WebDriver driver, String matchType, String queryType) {
		int index = 0;
		if(isListElementsVisible(driver, deleteRulesIcon, 5)) {
			scrollToElement(driver, getElements(driver, matchTypeSelect).get(index));
			int size = getElements(driver, deleteRulesIcon).size();
			index = size-1;
			selectFromDropdown(driver, getElements(driver, matchTypeSelect).get(index), SelectTypes.visibleText, matchType);
			selectFromDropdown(driver, getElements(driver, querySelect).get(index), SelectTypes.visibleText, queryType);
			
			//saving account
			saveAcccountSettings(driver);
			
			//verifying details after saving account
			assertEquals(getSelectedValueFromDropdown(driver, getElements(driver, matchTypeSelect).get(size-1)), matchType);
			assertEquals(getSelectedValueFromDropdown(driver, getElements(driver, querySelect).get(size-1)).replace(" ", ""), queryType);
		}
		else {
			scrollToElement(driver, matchTypeSelect);
			selectFromDropdown(driver, matchTypeSelect, SelectTypes.visibleText, matchType);
			selectFromDropdown(driver, querySelect, SelectTypes.visibleText, queryType);
			
			//saving account
			saveAcccountSettings(driver);
			
			//verifying details after saving account
			assertEquals(getSelectedValueFromDropdown(driver, matchTypeSelect), matchType);
			assertEquals(getSelectedValueFromDropdown(driver, querySelect).replace(" ", ""), queryType);
		}
		return index;
	}
	
	public void deleteAllRelatedRecords(WebDriver driver){
		if(isListElementsVisible(driver, deleteRulesIcon, 5)){
			int i = getElements(driver, deleteRulesIcon).size() - 1;
			while (i >= 0) {
				// delete field
				clickElement(driver, getElements(driver, deleteRulesIcon).get(i));
				idleWait(1);
				i--;
			}
			saveAcccountSettings(driver);
		}
	}
	
	public void deleteRelatedRecords(WebDriver driver, int index) {
		if(isListElementsVisible(driver, deleteRulesIcon, 5)) {
			WebElement deleteRule = getInactiveElements(driver, deleteRulesIcon).get(index);
			scrollToElement(driver, deleteRule);
			clickElement(driver, deleteRule);
			assertTrue(isElementVisible(driver, restoreRuleIcon, 5));
			saveAcccountSettings(driver);
			assertFalse(isElementVisible(driver, deleteRule, 5));
		}
		else {
			scrollToElement(driver, deleteRulesIcon);
			clickElement(driver, deleteRulesIcon);
			assertTrue(isElementVisible(driver, restoreRuleIcon, 5));
			saveAcccountSettings(driver);
			assertFalse(isElementVisible(driver, deleteRulesIcon, 5));
		}
	}
	
	/*******Related Records section ends here******/
	
	public void SetDefaultIntelligentDialerSettings(WebDriver driver, String callTimeoutTimeInSeconds, String stayConnecttimoutInMinutes, String callForwardigtimoutInSeconds){
		System.out.println("Setting default settings for intelligent dialer");
		openIntelligentDialerTab(driver);
		enableWebLeadsSetting(driver);
		disableComplainceHourSetting(driver);
		enableCallQueueSetting(driver);
		enableCallRatingSetting(driver);
		enableRelatedRecordsSetting(driver);
		enableGeneralSettings(driver);
		enableChatterBoxSetting(driver);
		enableLeadStatusSetting(driver);
		enableNewTaskSetting(driver);
		enableMessagingSetting(driver);
		disableCallToolsSetting(driver);
		disableHideGroupCallHistory(driver);
		disableMultiMatchRequiredSetting(driver);
		disableCallDispositionRequiredSetting(driver);
		enableEmailButtonSetting(driver);
		disableCallTasksDueDateSetting(driver);
		disableCallTasksTypeSetting(driver);
		disableCallTasksReminderSetting(driver);
		disableCallTasksPrioritySetting(driver);		
		disableCallNotesRelatedRecordSetting(driver);
		//new settings
		scrollToGeneralSettings(driver);
		enableConferenceSetting(driver);	
		enablewebRTCSetting(driver);
		unLockUnavailableFlow(driver);
		selectUnvailableFlowSetting(driver);
		enterCallTimeoutTime(driver, callTimeoutTimeInSeconds);
		enableDirectConnectSetting(driver);
		enableAdvancedSearchSetting(driver);
		enableDualChannelRecording(driver);
		enableStayConnectedSetting(driver);
		enterStayConnectedTimeOut(driver, stayConnecttimoutInMinutes);
		enableVoiceMailEnabledSetting(driver);
		enableCallForwardingSetting(driver);
		enterCallForwardingTimeOutValue(driver, callForwardigtimoutInSeconds);
		enableClickToVoicemailSetting(driver);
		enableDynamicCallFlows(driver);
		//salesforce settings
		disableCustomActivityObjectState(driver);
		enablePhoneMatchAccountSetting(driver);
		enablePhoneMatchContactSetting(driver);
		enablePhoneMatchLeadsToggleSetting(driver);
		enablePhoneMatchOpportunitiesSetting(driver);
		enableSOSLSearchSetting(driver);
		disableDisableLeadCreationSetting(driver);
		disableCreateLeadOnMultiSearchSetting(driver);
		saveAcccountSettings(driver);
	}
	
	public void clickCentralNoAddIcon(WebDriver driver) {
		waitUntilVisible(driver, centralNoAddIcon);
		scrollIntoView(driver, centralNoAddIcon);
		clickElement(driver, centralNoAddIcon);		
	}

	public Pair<String,List<String>> addNewCentralNumber(WebDriver driver, String labelName) {
		clickCentralNoAddIcon(driver);
		return addSmartNumberPage.addNewSmartNumber(driver, AddSmartNumberPage.Country.UNITED_STATES.displayName(), null, "22", labelName, Type.Additional.toString(), SmartNumberCount.Single.toString());
	}
	
	public boolean isCentralNumberPresent(WebDriver driver, String centralNumber) {
		By locator = By.xpath(centralNumberName.replace("$NUMBER$", centralNumber));
		return isElementVisible(driver, locator, 5);
	}
	
	public void deleteCentralNumber(WebDriver driver, String centralNumber) {
		idleWait(1);
		By locator = By.xpath(centralNumberDelete.replace("$NUMBER$", centralNumber));
		waitUntilVisible(driver, locator);
		scrollIntoView(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
	}
	
	public List<String> getCentralNoList(WebDriver driver) {
		return getTextListFromElements(driver, centralNoNumberList);
	}
	
	/*******Live Queue section starts here******/
	
	public String getCurrentSizeOfLiveQueue(WebDriver driver, String queueName){
		dashBoard.isPaceBarInvisible(driver);
		By queueSizeLoc = By.xpath(liveQueueCurrentSize.replace("$queueName$", queueName));
		waitUntilVisible(driver, queueSizeLoc);
		return getElementsText(driver, queueSizeLoc);
	}
	
	public void deleteLiveQueueByName(WebDriver driver, String queueName) {
		By queueDeleteBtn = By.xpath(liveQueueDelBtn.replace("$$QueueName$$", queueName));
		if(isElementVisible(driver, queueDeleteBtn, 0)){
			clickElement(driver, queueDeleteBtn);
			waitUntilVisible(driver, confirmDeleteBtn);
			clickElement(driver, confirmDeleteBtn);
			waitUntilInvisible(driver, confirmDeleteBtn);
			System.out.println("live queue removed: " + queueName);
		}else {
			System.out.println("There is no live call for " + queueName);
		}
	}
	
	// ***sip client methods start**
	
	public void enableSipClientSetting(WebDriver driver){
		if(!findElement(driver, sipClientCheckBox).isSelected()) {
			System.out.println("Checking sip Client checkbox");
			clickElement(driver, sipClientToggleOffButton);
		}else {
			System.out.println("sip Client setting is already enabled");
		} 
	}
	
	public void disableSipClientSetting(WebDriver driver){
		if(findElement(driver, sipClientCheckBox).isSelected()) {
			System.out.println("Checking sip Client checkbox");
			clickElement(driver, sipClientToggleOnButton);
		}else {
			System.out.println("sip Client setting is already disabled");
		} 
	}
	
	// ***s3 methods starts
	
	public void enableApplyChangesS3Recording(WebDriver driver){
		if(!findElement(driver, s3RecordingCheckBox).isSelected()) {
			System.out.println("enable apply changes s3 recordings");
			clickElement(driver, s3RecordingToggleOffButton);			
		}else {
			System.out.println("Apply changes s3 recordings is already enabled");
		} 
	}
	
	public void disableApplyChangesS3Recording(WebDriver driver){
		if(findElement(driver, s3RecordingCheckBox).isSelected()) {
			System.out.println("disable apply changes s3 recordings");
			clickElement(driver, s3RecordingToggleOnButton);			
		}else {
			System.out.println("Apply changes s3 recordings is already disabled");
		}
	}
	
	public void enterS3RecordingBucketValue(WebDriver driver, String text){
		waitUntilVisible(driver, s3RecordingInput);
		enterText(driver, s3RecordingInput, text);
	}
	
	public void clearS3RecordingBucketValue(WebDriver driver){
		waitUntilVisible(driver, s3RecordingInput);
		clickElement(driver, s3RecordingInput);
		clearAll(driver, s3RecordingInput);
	}
	
	// ***web RTC methods starts
	
	public void enablewebRTCSetting(WebDriver driver){
		if(!findElement(driver, webRTCCheckbox).isSelected()) {
			System.out.println("Checking WebRTC Setting checkbox");
			clickElement(driver, webRTCToggleButton);			
		}else {
			System.out.println("WebRTC setting already enable");
		} 
	}
	
	public void disablewebRTCSetting(WebDriver driver){
		if(findElement(driver, webRTCCheckbox).isSelected()) {
			System.out.println("disable WebRTC Setting");
			clickElement(driver, webRTCToggleOnButton);			
		}else {
			System.out.println("WebRTC setting already disabled");
		} 
	}
	
	// stay connected methods
	public void enableStayConnectedSetting(WebDriver driver){
		if(!findElement(driver, stayConnectedCheckBox).isSelected()) {
			System.out.println("Checking continuous bridge checkbox");
			clickElement(driver, stayConnectedToggleOffButton);				
		}else {
			System.out.println("Continuous Bridge setting is already enabled");
		} 
	}
	
	public void disableStayConnectedSetting(WebDriver driver){
		if(findElement(driver, stayConnectedCheckBox).isSelected()) {
			System.out.println("disabling continuous bridge checkbox");
			clickElement(driver, stayConnectedTogglOnButton);				
		}else {
			System.out.println("Continuous Bridge setting is already disabled");
		} 
	}
	
	public void enterStayConnectedTimeOut(WebDriver driver, String timoutInMinutes){
		enterText(driver, stayConnectedTimeoutTextBox, timoutInMinutes);
	}
	
	// voice mail
	public void enableVoiceMailEnabledSetting(WebDriver driver){
		if(!findElement(driver, voiceMailEnabledCheckBox).isSelected()) {
			System.out.println("Checking Voicemail checkbox");
			clickElement(driver, voiceMailEnabledToggleOffButton);			
		}else {
			System.out.println("Voicemail setting is already enable");
		} 
	}
	
	public void disableVoiceMailEnabledSetting(WebDriver driver){
		if(findElement(driver, voiceMailEnabledCheckBox).isSelected()) {
			System.out.println("disabling Voicemail checkbox");
			clickElement(driver, voiceMailEnabledToggleOnButton);			
		}else {
			System.out.println("Voicemail setting is already disable");
		} 
	}
	
	//call forwarding
	public void enableCallForwardingSetting(WebDriver driver){
		if(!findElement(driver, callForwardingCheckBox).isSelected()) {
			System.out.println("Checking Call Forwarding checkbox");
			clickElement(driver, callForwardingToggleOffButton);			
		}else {
			System.out.println("Call Forwarding setting is already enable");
		} 
	}
	
	public void disableCallForwardingSetting(WebDriver driver){
		if(findElement(driver, callForwardingCheckBox).isSelected()) {
			System.out.println("disabling Call Forwarding checkbox");
			clickElement(driver, callForwardingToggleOnButton);			
		}else {
			System.out.println("Call Forwarding setting is already disabled");
		} 
	}
	
	public void enableCallForwardingPrompt(WebDriver driver) {
		System.out.println("Setting call forwarding to OFF");
		if (!findElement(driver, callForwardingPromptCheckBox).isSelected()) {
			clickElement(driver, callForwardingPromptToggleOffBtn);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding Prompt is already ON");
		}
	}
	
	public void disableCallForwardingPrompt(WebDriver driver) {
		System.out.println("Setting call forwarding prompt to OFF");
		if (findElement(driver, callForwardingPromptCheckBox).isSelected()) {
			clickElement(driver, callForwardingPromptToggleOnBtn);
		} else {
			System.out.println("Call Forwarding Prompt is already OFF");
		}
	}
	
	public String getCallForwardingLockBtnStatus(WebDriver driver) {
		waitUntilVisible(driver, callForwardingLockBtn);
		return getAttribue(driver, callForwardingLockBtn, ElementAttributes.src);
	}
	
	public boolean isCallForwardingLockActiveBtnVisible(WebDriver driver) {
		return isElementVisible(driver, callForwardingLockActive, 5);
	}
	
	public void lockCallForwarding(WebDriver driver){
		if(isElementVisible(driver, callForwardingUnlockActive, 5)){
			waitUntilVisible(driver, callForwardingUnlockActive);
			clickElement(driver, callForwardingUnlockActive);
			confirmLockBtn(driver);
		}else {
			System.out.println("Call forwarding is already locked");
		}
	}
	
	public void unlockCallForwarding(WebDriver driver){
		if(isElementVisible(driver, callForwardingLockActive, 5)){
			waitUntilVisible(driver, callForwardingLockActive);
			clickElement(driver, callForwardingLockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Unlock \"Call Forwarding\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Call Forwarding is already unlocked");
		}
	}
	public void enableCallForwardingDisabledOfflineSetting(WebDriver driver){
		if(!findElement(driver, disableOfflineForwardingCheckBox).isSelected()) {
			System.out.println("Checking disable offline Forwarding checkbox");
			scrollToElement(driver, disableOfflineForwardingToggleOffButton);
			clickElement(driver, disableOfflineForwardingToggleOffButton);			
		}else {
			System.out.println("Disable offline Forwarding setting is already enable");
		} 
	}
	
	public void disableCallForwardingDisabledOfflineSetting(WebDriver driver){
		if(findElement(driver, disableOfflineForwardingCheckBox).isSelected()) {
			System.out.println("disabling Call Forwarding checkbox");
			scrollToElement(driver, disableOfflineForwardingToggleOnButton);
			clickElement(driver, disableOfflineForwardingToggleOnButton);			
		}else {
			System.out.println("Disable offline Forwarding setting is already disabled");
		} 
	}
	
	public void lockCallForwardingPrompt(WebDriver driver){
		if(isElementVisible(driver, callForwardingPromptUnlockActive, 5)){
			waitUntilVisible(driver, callForwardingPromptUnlockActive);
			clickElement(driver, callForwardingPromptUnlockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Lock \"Call Forwarding Prompt\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Call Forwarding prompt is already locked");
		}
	}
	
	public void unlockCallForwardingPrompt(WebDriver driver){
		if(isElementVisible(driver, callForwardingPromptLockActive, 5)){
			waitUntilVisible(driver, callForwardingPromptLockActive);
			clickElement(driver, callForwardingPromptLockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Unlock \"Call Forwarding Prompt\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Call Forwarding prompt is already unlocked");
		}
	}
	
	public void lockCallForwardingDisabledOffline(WebDriver driver){
		if(isElementVisible(driver, disableOfflineForwardingUnlockActive, 5)){
			waitUntilVisible(driver, disableOfflineForwardingUnlockActive);
			clickElement(driver, disableOfflineForwardingUnlockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Lock \"Disable Offline Forwarding\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Disable offline forwarding is already locked");
		}
	}
	
	public void unlockCallForwardingDisabledOffline(WebDriver driver){
		if(isElementVisible(driver, disableOfflineForwardingLockActive, 5)){
			waitUntilVisible(driver, disableOfflineForwardingLockActive);
			clickElement(driver, disableOfflineForwardingLockActive);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Unlock \"Disable Offline Forwarding\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Disable offline forwarding is already unlocked");
		}
	}
	
	public void clearCallForwardingTimeOutValue(WebDriver driver){
		enterBackspace(driver, callForwardingTimeOutTextBox);
	}
	
	public void enterCallForwardingTimeOutValue(WebDriver driver, String timoutInSeconds){
		enterText(driver, callForwardingTimeOutTextBox, timoutInSeconds);
	}
	
	public boolean isCallForwardingLockDisabled(WebDriver driver) {
		scrollToElement(driver, callForwrdingTimeoutLockBtn);
		idleWait(2);
		return findElement(driver, callForwrdingTimeoutLockBtn).getAttribute("src").contains("disabled");
	}

	public void verifyToolTipCallFrwrdingLock(WebDriver driver){
		scrollToElement(driver, callForwrdingTimeoutLockBtn);
		hoverElement(driver, callForwrdingTimeoutLockBtn);
		assertEquals(findElement(driver, callForwrdingTimeoutLockBtn).getAttribute("data-original-title"), tooltipCallFrwrdingLock);
	}
	
	public void verifyToolTipDisableOfflineFrwrdingLock(WebDriver driver){
		scrollToElement(driver, disableOfflineForwrdingBtn);
		hoverElement(driver, disableOfflineForwrdingBtn);
		assertEquals(findElement(driver, disableOfflineForwrdingBtn).getAttribute("data-original-title"), tooltipDisableOfflineForwardingLock);
	}
	
	public void verifyToolTipDisableOfflineFrwrdingUnLock(WebDriver driver){
		scrollToElement(driver, disableOfflineForwrdingBtn);
		hoverElement(driver, disableOfflineForwrdingBtn);
		assertEquals(findElement(driver, disableOfflineForwrdingBtn).getAttribute("data-original-title"), tooltipDisableOfflineForwardingUnLock);
	}
	
	public void verifyToolTipCallForwardingPromptLock(WebDriver driver){
		scrollToElement(driver, callForwardingPromptBtn);
		hoverElement(driver, callForwardingPromptBtn);
		assertEquals(findElement(driver, callForwardingPromptBtn).getAttribute("data-original-title"), tooltipCallForwardingPromptLock);
	}
	
	public void verifyToolTipCallForwardingPromptUnLock(WebDriver driver){
		scrollToElement(driver, callForwardingPromptBtn);
		hoverElement(driver, callForwardingPromptBtn);
		assertEquals(findElement(driver, callForwardingPromptBtn).getAttribute("data-original-title"), tooltipCallForwardingPromptUnLock);
	}
	
	public void enableClickToVoicemailSetting(WebDriver driver){
		if(!findElement(driver, clickToVoicemailCheckbox).isSelected()) {
			System.out.println("Checking Click to voicemail checkbox");
			clickElement(driver, clickToVoicemailToggleButton);				
		}else {
			System.out.println("Click to voicemail setting is already disable");
		}
	}
	
	public void disableClickToVoicemailSetting(WebDriver driver){
		if(findElement(driver, clickToVoicemailCheckbox).isSelected()) {
			System.out.println("Checking Click to voicemail checkbox");
			clickElement(driver, clickToVoicemailToggleOnButton);				
		}else {
			System.out.println("Click to voicemail setting is already disable");
		}
	}
	
	//call notification
	public void disableCallNotifications(WebDriver driver){
		if(findElement(driver, callNotificationCheckBox).isSelected()) {
			System.out.println("disabling Call Notificaitons Setting");
			scrollToElement(driver, callNotificationToggleOnButton);
			clickElement(driver, callNotificationToggleOnButton);			
		}else {
			System.out.println("Call Notificaitons setting is already disabled");
		} 
	}
	
	public void enableCallNotifications(WebDriver driver){
		if(!findElement(driver, callNotificationCheckBox).isSelected()) {
			System.out.println("enabling Call Notificaitons Setting");
			scrollToElement(driver, callNotificationToggleOffButton);
			clickElement(driver, callNotificationToggleOffButton);			
		}else {
			System.out.println("Call Notificaitons setting is already enabled");
		} 
	}
	
	public void confirmLockBtn(WebDriver driver){
		waitUntilVisible(driver, confirmLockBtn);
		clickElement(driver, confirmLockBtn);
		waitUntilInvisible(driver, confirmLockBtn);
	}
	
	// lock
	public void lockUnavailableFlow(WebDriver driver){
		if(findElement(driver, unavailableFlowLockBtn).getAttribute("src").contains("/unlock-active.svg")){
			waitUntilVisible(driver, unavailableFlowLockBtn);
			clickElement(driver, unavailableFlowLockBtn);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Lock \"Unavailable Flow\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Unavailable call flow is already locked");
		}
	}
	
	public void unLockUnavailableFlow(WebDriver driver){
		if(findElement(driver, unavailableFlowLockBtn).getAttribute("src").contains("/lock-active.svg")){
			waitUntilVisible(driver, unavailableFlowLockBtn);
			clickElement(driver, unavailableFlowLockBtn);
			waitUntilVisible(driver, modalTitle);
			assertEquals(getElementsText(driver, modalTitle), "Unlock \"Unavailable Flow\" Account Setting");
			confirmLockBtn(driver);
		}else {
			System.out.println("Unavailable call flow is already unlocked");
		}
	}
	
	public void verifyToolTipUnavailableFlowLock(WebDriver driver){
		scrollToElement(driver, unavailableFlowLockBtn);
		hoverElement(driver, unavailableFlowLockBtn);
		assertEquals(findElement(driver, unavailableFlowLockBtn).getAttribute("data-original-title"), tooltipUnavailableLock);
	}
	
	public void verifyToolTipUnavailableFlowUnLock(WebDriver driver){
		scrollToElement(driver, unavailableFlowLockBtn);
		hoverElement(driver, unavailableFlowLockBtn);
		assertEquals(findElement(driver, unavailableFlowLockBtn).getAttribute("data-original-title"), tooltipUnavailableUnLock);
	}
	
	// ----------------- Unavailable call flow methods----------------------//
	public void enterUnavailableCallFlow(WebDriver driver, String callFlow) {
		clickElement(driver, unavailableFlowDropdown);
		enterText(driver, unavailableFlowTextBox, callFlow);
	}

	// Selecting unavailable call flow
	public void selectUnavailableCallFlow(WebDriver driver, String callFlow) {
		scrollToElement(driver, advancedSearchOffToggleButton);
		clickElement(driver, unavailableCallFlowDropDown);
		idleWait(1);
		enterBackspace(driver, unavailableCallFlowTextBox);
		enterText(driver, unavailableCallFlowTextBox, callFlow);
		idleWait(1);
		waitUntilVisible(driver, searchedFlowInDropDown);
		clickElement(driver, searchedFlowInDropDown);
	}

	public void selectUnvailableFlowSetting(WebDriver driver) {
		assertTrue(findElement(driver, unavailableFlowHeading).isEnabled());
		scrollToElement(driver, advancedSearchOffToggleButton);
		clickElement(driver, unavailableCallFlowDropDown);
		idleWait(1);
		enterBackspace(driver, unavailableCallFlowTextBox);
	}

	// Selecting unavailable call flow
	public void selectdefaultLPCallFlow(WebDriver driver, String callFlow) {
		scrollToElement(driver, advancedSearchOffToggleButton);
		clickElement(driver, defaultLPFlowDropdown);
		idleWait(1);
		enterBackspace(driver, defaultLPFlowTextBox);
		enterText(driver, defaultLPFlowTextBox, callFlow);
		idleWait(1);
		waitUntilVisible(driver, searchedFlowInDropDown);
		clickElement(driver, searchedFlowInDropDown);
	}

	public void selectdefaultLPFlowSetting(WebDriver driver) {
		scrollToElement(driver, advancedSearchOffToggleButton);
		clickElement(driver, defaultLPCallFlowDropDown);
		idleWait(1);
		enterBackspace(driver, defaultLPCallFlowTextBox);
	}
	
	//advance search
	public void scrollToAdvancedSearchSetting(WebDriver driver){
		scrollToElement(driver, advancedSearchOffToggleButton);
	}
	
	public void enableAdvancedSearchSetting(WebDriver driver){
		if(!findElement(driver, advancedSearchTextBox).isSelected()) {
			System.out.println("Checking advance search checkbox");
			clickElement(driver, advancedSearchOffToggleButton);
		}else {
			System.out.println("Advance search setting is already enabled");
		} 
	}
	
	public void disableAdvancedSearchSetting(WebDriver driver){
		if(findElement(driver, advancedSearchTextBox).isSelected()) {
			System.out.println("Checking advance search checkbox");
			clickElement(driver, advancedSearchOnToggleButton);
		}else {
			System.out.println("Advance search setting is already enabled");
		} 
	}
	
	public void enableDynamicCallFlows(WebDriver driver){
		if(!findElement(driver, dynamicCallFlowCheckBox).isSelected()) {
			System.out.println("enabling Dynamic Call flow Setting");
			scrollToElement(driver, dynamicCallFlowToggleOff);
			clickElement(driver, dynamicCallFlowToggleOff);			
		}else {
			System.out.println("Dynamic Call flow Setting already enabled");
		} 
	}
	
	public void disableDynamicCallFlows(WebDriver driver){
		if(findElement(driver, dynamicCallFlowCheckBox).isSelected()) {
			System.out.println("disabling Dynamic Call flow Setting");
			scrollToElement(driver, dynamicCallFlowToggleOn);
			clickElement(driver, dynamicCallFlowToggleOn);			
		}else {
			System.out.println("Dynamic Call flow Setting already disabled");
		} 
	}
	
	public void enterTextVoicemailTranscription(WebDriver driver, String transcriptionText) {
		waitUntilVisible(driver, voiceMailTranscBlacklist);
		enterText(driver, voiceMailTranscBlacklist, transcriptionText);
		saveAcccountSettings(driver);
		assertEquals(getAttribue(driver, voiceMailTranscBlacklist, ElementAttributes.value), transcriptionText);
	}
	
	public void scrollToGeneralSettings(WebDriver driver){
		scrollToElement(driver, callForwardingHeading);
	}
	
	public void enableConferenceSetting(WebDriver driver){
		if(!findElement(driver, conferenceButtonCheckBox).isSelected()) {
			System.out.println("Checking conference checkbox");
			clickElement(driver, conferenceToggleOffButton);			
		}else {
			System.out.println("Conference setting is already enabled");
		} 
	}
	
	public void disableConferenceSetting(WebDriver driver){
		if(findElement(driver, conferenceButtonCheckBox).isSelected()) {
			System.out.println("disabled conference setting");
			clickElement(driver, conferenceToggleOnButton);			
		}else {
			System.out.println("Conference setting is already disabled");
		}
	}
	
	public void enableSendEmailUsingSalesforce(WebDriver driver){
		scrollToElement(driver, ligntningEmailCheckbox);
		if(!findElement(driver, sendEmailUsingSalesForceCheckbox).isSelected()) {
			System.out.println("Checking Send Email Using Salesforce Setting checkbox");
			clickElement(driver, sendEmailUsingSalesForceToggleButton);			
		}else {
			System.out.println("Send Email Using Salesforce Setting already enable");
		} 
	}
	
	public void enableLightningEmailTemplates(WebDriver driver){
		if(!findElement(driver, ligntningEmailCheckbox).isSelected()) {
			System.out.println("enabling lightning email templates setting");
			clickElement(driver, ligntningEmailCheckbox);			
		}else {
			System.out.println("lightning email templates setting already enable");
		} 
	}
	
	public void enterCallTimeoutTime(WebDriver driver, String timeInSeconds){
		enterText(driver, callTimeoutTextBox, timeInSeconds);
	}
	
	public void enableDirectConnectSetting(WebDriver driver){
		if(!findElement(driver, directConnectCheckBox).isSelected()) {
			System.out.println("Checking Direct Connect checkbox");
			clickElement(driver, directConnectToggleButton);				
		}else {
			System.out.println("Direct Connect setting is already enabled");
		} 
	}
	
	public void enableDualChannelRecording(WebDriver driver){
		if(findElement(driver, dualChannelRecordingCheckBox).isSelected()) {
			System.out.println("Checking Dual channel recording checkbox");
			clickElement(driver, dualChannelRecordingToggleButton);			
		}else {
			System.out.println("Dual channel recording setting is already enabled");
		} 
	}
	
	// salesforce settings > ID tab
	public void clickSalesForceConnect(WebDriver driver) {
		waitUntilVisible(driver, salesForceConnectBtn);
		clickElement(driver, salesForceConnectBtn);
		assertEquals(getSaveAccountsSettingMessage(driver), "Run-as user has a valid session.");
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void clickClearBtnCache(WebDriver driver) {
		waitUntilVisible(driver, clearCacheBtn);
		clickElement(driver, clearCacheBtn);
		assertEquals(getSaveAccountsSettingMessage(driver), "Cache successfully cleared.");
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void clickPackageRun(WebDriver driver) {
		waitUntilVisible(driver, packageCheckRun);
		clickElement(driver, packageCheckRun);
		assertEquals(getSaveAccountsSettingMessage(driver), "Package check ran successfully.");
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void disableCustomActivityObjectState(WebDriver driver){
		if(findElement(driver, customActivityObjectCheckBox).isSelected()) {
			System.out.println("Checking Custom Activity object checkbox");
			clickElement(driver, customActivityObjectToggleOnButton);				
		}else {
			System.out.println("Custom Activity object setting is already disable");
		} 
	}
	
	public void enableCustomActivityObjectState(WebDriver driver){
		if(!findElement(driver, customActivityObjectCheckBox).isSelected()) {
			System.out.println("Checking Custom Activity object checkbox");
			clickElement(driver, customActivityObjectToggleOffButton);				
		}else {
			System.out.println("Custom Activity object setting is already enable");
		} 
	}
	
	public void enablePhoneMatchAccountSetting(WebDriver driver){
		if(!findElement(driver, phoneMatchAccountCheckBox).isSelected()) {
			System.out.println("Checking Match Account checkbox");
			clickElement(driver, phoneMatchAccountToggleButton);				
		}else {
			System.out.println("Match Account setting is already disable");
		}
	}
	
	public void disablePhoneMatchAccountSetting(WebDriver driver){
		if(findElement(driver, phoneMatchAccountCheckBox).isSelected()) {
			System.out.println("Checking Match Account checkbox");
			clickElement(driver, phoneMatchAccountToggleOffButton);				
		}else {
			System.out.println("Match Account setting is already disable");
		}
	}
	
	public void enablePhoneMatchContactSetting(WebDriver driver){
		if(!findElement(driver, phoneMatchContactsCheckBox).isSelected()) {
			System.out.println("Checking Match Contacts checkbox");
			clickElement(driver, phoneMatchContactsToggleButton);				
		}else {
			System.out.println("Match Contacts setting is already disable");
		}
	}
	
	public void disablePhoneMatchContactSetting(WebDriver driver){
		if(findElement(driver, phoneMatchContactsCheckBox).isSelected()) {
			System.out.println("Checking Match Contacts checkbox");
			clickElement(driver, phoneMatchContactsToggleOffButton);				
		}else {
			System.out.println("Match Contacts setting is already disable");
		}
	}
	
	public void enablePhoneMatchLeadsToggleSetting(WebDriver driver){
		if(!findElement(driver, phoneMatchLeadsCheckBox).isSelected()) {
			System.out.println("Checking Match Leads checkbox");
			clickElement(driver, phoneMatchLeadsToggleButton);				
		}else {
			System.out.println("Match Leads setting is already disable");
		}
	}
	
	public void disablePhoneMatchLeadsToggleSetting(WebDriver driver){
		if(findElement(driver, phoneMatchLeadsCheckBox).isSelected()) {
			System.out.println("Checking Match Leads checkbox");
			clickElement(driver, phoneMatchLeadsToggleOffButton);				
		}else {
			System.out.println("Match Leads setting is already disable");
		}
	}
	
	public void enablePhoneMatchOpportunitiesSetting(WebDriver driver){
		if(!findElement(driver, phoneMatchOpportunitiesCheckBox).isSelected()) {
			System.out.println("Checking Match Opportunity checkbox");
			clickElement(driver, phoneMatchOpportunitiesToggleButton);			
		}else {
			System.out.println("Match Opportunity setting is already disable");
		} 
	}
	
	public void disablePhoneMatchOpportunitiesSetting(WebDriver driver){
		if(findElement(driver, phoneMatchOpportunitiesCheckBox).isSelected()) {
			System.out.println("Checking Match Opportunity checkbox");
			clickElement(driver, phoneMatchOpportunitiesToggleOffButton);			
		}else {
			System.out.println("Match Opportunity setting is already disable");
		} 
	}
	
	public void enableSOSLSearchSetting(WebDriver driver){
		if(!findElement(driver, soslSearchCheckBox).isSelected()) {
			System.out.println("Checking Disable lead creation checkbox");
			clickElement(driver, soslSearchToggleButton);				
		}else {
			System.out.println("sosl search setting is already enable");
		} 
	}
	
	public void disableSOSLSearchSetting(WebDriver driver){
		if(findElement(driver, soslSearchCheckBox).isSelected()) {
			System.out.println("Checking Disable lead creation checkbox");
			clickElement(driver, soslSearchToggleOnButton);				
		}else {
			System.out.println("sosl search setting is already disable");
		} 
	}
	
	public void enableCreateLeadOnMultiSearchSetting(WebDriver driver){
		if(!findElement(driver, createLeadOnMultiSearchCheckBox).isSelected()) {
			System.out.println("Checking Create lead on multi-match checkbox");
			scrollToElement(driver, createLeadOnMultiSearchToggleOffBtn);
			clickElement(driver, createLeadOnMultiSearchToggleOffBtn);				
		}else {
			System.out.println("Create lead on multi-match setting is already enable");
		}
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void disableCreateLeadOnMultiSearchSetting(WebDriver driver){
		if(findElement(driver, createLeadOnMultiSearchCheckBox).isSelected()) {
			System.out.println("disabling Create lead on multi-match checkbox");
			scrollToElement(driver, createLeadOnMultiSearchToggleOnBtn);
			clickElement(driver, createLeadOnMultiSearchToggleOnBtn);				
		}else {
			System.out.println("Create lead on multi-match setting is already disabled");
		}
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public boolean isCreateLeadSFDCCampaignDisabled(WebDriver driver){
		return getAttribue(driver, createLeadSectionForSFDCCampaign, ElementAttributes.Class).contains("disabled");
	}
	
	public boolean isCreateLeadUnansweredCallDisabled(WebDriver driver){
		return getAttribue(driver, createLeadSectionForUnansweredCall, ElementAttributes.Class).contains("disabled");
	}
	
	public void disableCreateLeadSFDCCampaign(WebDriver driver){
		if(findElement(driver, createLeadSFDCCampaign).isSelected() && !isCreateLeadSFDCCampaignDisabled(driver)) {
			System.out.println("disabling Create lead on multi-match for SFDC Campaign only checkbox");
			clickElement(driver, createLeadSFDCCampaign);				
		}else {
			System.out.println("Create lead on multi-match for SFDC campaign only setting is already disabled");
		}
	}
	
	public void enableCreateLeadSFDCCampaign(WebDriver driver){
		if(!findElement(driver, createLeadSFDCCampaign).isSelected()) {
			System.out.println("enabling Create lead on multi-match for SFDC Campaign only checkbox");
			clickElement(driver, createLeadSFDCCampaign);				
		}else {
			System.out.println("Create lead on multi-match for SFDC campaign only setting is already enabled");
		}
	}
	
	public void disableCreateLeadUnansweredCall(WebDriver driver){
		if(findElement(driver, createLeadUnansweredCall).isSelected() && !isCreateLeadUnansweredCallDisabled(driver)) {
			System.out.println("disabling Create lead on multi-match for unanswered call only checkbox");
			clickElement(driver, createLeadUnansweredCall);				
		}else {
			System.out.println("Create lead on multi-match for unanswered call only setting is already disabled");
		}
	}
	
	public void enableCreateLeadUnansweredCall(WebDriver driver){
		if(!findElement(driver, createLeadUnansweredCall).isSelected()) {
			System.out.println("enabling Create lead on multi-match for unanswered call only checkbox");
			clickElement(driver, createLeadUnansweredCall);				
		}else {
			System.out.println("Create lead on multi-match for unanswered call only setting is already enabled");
		}
	}
	
	public void disableDisableLeadCreationSetting(WebDriver driver){
		if(findElement(driver, disableLeadCreationCheckBox).isSelected()) {
			System.out.println("Checking Disable lead creation checkbox");
			clickElement(driver, disableLeadCreationToggleButton);				
		}else {
			System.out.println("Disable lead creation setting is already disable");
		} 
	}
	
	public void enableDisableLeadCreationSetting(WebDriver driver){
		if(!findElement(driver, disableLeadCreationCheckBox).isSelected()) {
			System.out.println("Checking Enable lead creation checkbox");
			clickElement(driver, enableLeadCreationToggleButton);				
		}else {
			System.out.println("Disable lead creation setting is already Enabled");
		} 
	}
	
	public void enableCreateTaskEarlySetting(WebDriver driver) {
		if (!findElement(driver, createTaskEarlyCheckBox).isSelected()) {
			System.out.println("enabling Create Task Early checkbox");
			clickElement(driver, createTaskEarlyToggleOffBtn);
		} else {
			System.out.println("Create Task Early setting is already enabled");
		}
	}

	public void disableCreateTaskEarlySetting(WebDriver driver) {
		if (findElement(driver, createTaskEarlyCheckBox).isSelected()) {
			System.out.println("disabling Create Task Early checkbox");
			clickElement(driver, createTaskEarlyToggleOnBtn);
		} else {
			System.out.println("Create Task Early setting is already disabled");
		}
	}
	
	/**
	 * @param driver
	 * verify all id tab tool tips
	 */
	public void verifyAllIntelligentDialerTabToolTip(WebDriver driver){
		waitUntilVisible(driver, emailNotifyToolTip);
		scrollToElement(driver, emailNotifyToolTip);
		hoverElement(driver, emailNotifyToolTip);
		
		scrollToElement(driver, voicemailBlacklistToolTip);
		hoverElement(driver, voicemailBlacklistToolTip);
		
		scrollToElement(driver, notificationsToolTip);
		hoverElement(driver, notificationsToolTip);
	}

	public boolean isCAIAnalyticsVisible(WebDriver driver) {
		return isElementVisible(driver, UsersPage.conversationAnalyticsCheckBox, 5);
	}
	
	public void selectNewDefaultContact(WebDriver driver, String text) {
		selectFromDropdown(driver, selectDeafultContact, SelectTypes.visibleText, text);
		saveAcccountSettings(driver);
	}
	
	/**
	 * @param driver
	 * verify setting Not Visible On account Overview tab
	 */
	public void verifySettingNotVisibleOnOverview(WebDriver driver) {
		assertFalse(isElementVisible(driver, callNotification, 6));
		assertFalse(isElementVisible(driver, callForwarding, 6));
		assertFalse(isElementVisible(driver, voiceMailEnabled, 6));
		assertFalse(isElementVisible(driver, soundDisconnect, 6));
		assertFalse(isElementVisible(driver, sipRoutingHeader, 6));
		assertFalse(isElementVisible(driver, sipForwardingHeader, 6));
	}
	
	/**
	 * @param driver
	 * verify setting Visible On account Intelligent Dialer tab
	 */
	public void verifySettingVisibleOnIDTab(WebDriver driver) {
		assertTrue(isElementVisible(driver, webRTC, 6));
		assertTrue(isElementVisible(driver, callQueue, 6));
		assertTrue(isElementVisible(driver, callTimeoutTextBox, 6));
		assertTrue(isElementVisible(driver, callerId, 6));
		assertTrue(isElementVisible(driver, maxHoldTimeInput, 6));
		assertTrue(isElementVisible(driver, unavailableFlowHeading, 6));
		assertTrue(isElementVisible(driver, verifiedLocalPresence, 6));
		assertTrue(isElementVisible(driver, advancedSearchTextBox, 6));
		assertTrue(isElementVisible(driver, conferenceHeading, 6));
		assertTrue(isElementVisible(driver, clickToVoicemail, 6));
		assertTrue(isElementVisible(driver, directConnect, 6));
		assertTrue(isElementVisible(driver, dualChannelRecording, 6));
		assertTrue(isElementVisible(driver, dynamicCallFlow, 6));
		assertTrue(isElementVisible(driver, extensionNonPhoneFilter, 6));
		assertTrue(isElementVisible(driver, showSlaReport, 6));
		assertTrue(isElementVisible(driver, clickToCallDomainBlacklist, 6));
		assertTrue(isElementVisible(driver, stayConnected, 6));
		assertTrue(isElementVisible(driver, stayConnectedTimeoutTextBox, 6));
		assertTrue(isElementVisible(driver, holdMusic, 6));
		assertTrue(isElementVisible(driver, sendEmailUsingSalesforce, 6));
		assertTrue(isElementVisible(driver, queuesAnnouncement, 6));
		assertTrue(isElementVisible(driver, sipClient, 6));
		assertTrue(isElementVisible(driver, s3RecordingInput, 6));
		assertTrue(isElementVisible(driver, mp3Recordings, 6));
		
	}
	
	/**
	 * @param driver
	 * verify setting Visible On account Intelligent Dialer tab
	 */
	public void verifySettingVisibleOnIDTabForSupport(WebDriver driver) {
		assertTrue(isElementVisible(driver, basePackageCheck, 6));
		assertTrue(isElementVisible(driver, basePackageName, 6));
		assertTrue(isElementVisible(driver, basePackageVersion, 6));
		assertTrue(isElementVisible(driver, ignoreIntegrityErrors, 6));
		assertTrue(isElementVisible(driver, honorDoNotCallOrEmail, 6));
		assertTrue(isElementVisible(driver, createLeadOnMultiMatch, 6));
		assertTrue(isElementVisible(driver, phoneMatchAccounts, 6));
		assertTrue(isElementVisible(driver, phoneMatchContacts, 6));
		assertTrue(isElementVisible(driver, phoneMatchLeads, 6));
		assertTrue(isElementVisible(driver, phoneMatchOnExternalPhoneFields, 6));
		assertTrue(isElementVisible(driver, SOSLSearch, 6));
		assertTrue(isElementVisible(driver, openRecordsInSalesConsole, 6));
		assertTrue(isElementVisible(driver, ClickToCallReuseTask, 6));
		assertTrue(isElementVisible(driver, createTasksEarly, 6));
		assertTrue(isElementVisible(driver, customActivityObject, 6));
		assertTrue(isElementVisible(driver, addNewDefault, 6));
		assertTrue(isElementVisible(driver, packageCheck, 6));
		assertTrue(isElementVisible(driver, cache, 6));	
	}
	
	/**
	 * @param driver
	 * verify setting Visible On account Intelligent Dialer tab
	 */
	public void verifySettingVisibleOnIDTabForAdmin(WebDriver driver) {
		assertTrue(isElementVisible(driver, honorDoNotCallOrEmail, 6));
		assertTrue(isElementVisible(driver, createLeadOnMultiMatch, 6));
		assertTrue(isElementVisible(driver, phoneMatchAccounts, 6));
		assertTrue(isElementVisible(driver, phoneMatchContacts, 6));
		assertTrue(isElementVisible(driver, phoneMatchLeads, 6));
		assertTrue(isElementVisible(driver, phoneMatchOnExternalPhoneFields, 6));
		assertTrue(isElementVisible(driver, SOSLSearch, 6));
		assertTrue(isElementVisible(driver, openRecordsInSalesConsole, 6));
		assertTrue(isElementVisible(driver, ClickToCallReuseTask, 6));
		assertTrue(isElementVisible(driver, createTasksEarly, 6));
		assertTrue(isElementVisible(driver, customActivityObject, 6));
		assertTrue(isElementVisible(driver, addNewDefault, 6));	
	}
	
	/**
	 * @param driver
	 * verify setting Not Visible On account Intelligent Dialer tab
	 */
	public void verifySettingNotVisibleOnSalesforceTab(WebDriver driver) {
		assertFalse(isElementVisible(driver, basePackageCheck, 2));
		assertFalse(isElementVisible(driver, basePackageName, 2));
		assertFalse(isElementVisible(driver, basePackageVersion, 2));
		assertFalse(isElementVisible(driver, ignoreIntegrityErrors, 2));
		assertFalse(isElementVisible(driver, honorDoNotCallOrEmail, 2));
		assertFalse(isElementVisible(driver, createLeadOnMultiMatch, 2));
		assertFalse(isElementVisible(driver, phoneMatchAccounts, 2));
		assertFalse(isElementVisible(driver, phoneMatchContacts, 2));
		assertFalse(isElementVisible(driver, phoneMatchLeads, 2));
		assertFalse(isElementVisible(driver, phoneMatchOnExternalPhoneFields, 2));
		assertFalse(isElementVisible(driver, SOSLSearch, 2));
		assertFalse(isElementVisible(driver, openRecordsInSalesConsole, 2));
		assertFalse(isElementVisible(driver, ClickToCallReuseTask, 2));
		assertFalse(isElementVisible(driver, createTasksEarly, 2));
		assertFalse(isElementVisible(driver, customActivityObject, 2));
		assertFalse(isElementVisible(driver, addNewDefault, 2));
		assertFalse(isElementVisible(driver, packageCheck, 2));
		assertFalse(isElementVisible(driver, cache, 2));	
		}
}