/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewEvent;
import softphone.source.callTools.SoftPhoneNewTask;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSalesforceTab.SalesForceFields;
/**
 * @author Abhishek
 *
 */
import  support.source.accounts.AccountSalesforceTab.salesforceFieldsType;

public class TaskEventCustomFields extends SoftphoneBase{
	
	HashMap<String, String> taskEventFields = new HashMap<>();
	HashMap<String, AccountSalesforceTab.salesforceFieldsType> taskEventFieldsType = new HashMap<>();
	HashMap<String, String> taskEventFieldsValue = new HashMap<>();
	String userName 	= "yaminitest@ringdna.com";
	String email		= "abhishek.gupta@metacube.com";
	String firstName	= "Yamini";
	String lastName		= "Ringdna";
	String aboutMe		= "This is a load test user.";
	String isActive		= "true";

	@Test(groups = { "MediumPriority" })
	public void default_fields_tasks() {
		System.out.println("Test case --additional_fields_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		String taskEventField = null;
		String taskEventFieldName = null;
		AccountSalesforceTab.salesforceFieldsType taskEventFieldType = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("Description", "Description");
		taskEventFields.put("ActivityDate", "Due Date");
		taskEventFields.put("Priority", "Priority");
		taskEventFields.put("WhatId", "Related Record");
		taskEventFields.put("ReminderDateTime", "Reminder Date/Time");
		taskEventFields.put("IsReminderSet", "Reminder Set");
		taskEventFields.put("Status", "Status");
		taskEventFields.put("Subject", "Subject");
		taskEventFields.put("Type","Type");
		
		//hash map for the task fields and their type
		taskEventFieldsType.put("Description", salesforceFieldsType.address);
		taskEventFieldsType.put("Due Date", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Priority", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Related Record", salesforceFieldsType.string);
		taskEventFieldsType.put("Reminder Date/Time", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Reminder Set", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Status", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Subject", salesforceFieldsType.string);
		taskEventFieldsType.put("Type", salesforceFieldsType.picklist);
		
		//open task field tab of salesforce settings page on app-qa 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		System.out.println("focus is on task tab of salesforce settings page");
		
		// make the editable setting enabled for default fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!(taskEventField.equals("CreatedDate"))) {
				System.out.println("makine field " + taskEventFieldName + " required");
				if(!accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName)) {
					accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
					accountSalesforceTab.idleWait(1);
					accountSalesforceTab.checkEditableCheckBox(driver1);
					accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				}

				assertTrue(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
			}
		}
		
		//switch to softphone tab and reload it to reflect task fields changes
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that task field are enabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			System.out.println(taskEventFieldEntry.getValue());
			if(taskEventFieldEntry.getValue().equals("Due Date") || taskEventFieldEntry.getValue().equals("Related Record") || taskEventFieldEntry.getValue().equals("Subject")) {
				assertTrue(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else if(!(taskEventFieldEntry.getValue().equals("Reminder Set")||taskEventFieldEntry.getValue().equals("Reminder Date/Time"))){
				assertTrue(softPhoneNewTask.isTaskSfPicklistFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		

		//data creation for adding a new task
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		
		//adding new task with the data mentioned above
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//Edit the task created above and verify that default fields are editable
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(taskEventFieldEntry.getValue().equals("Due Date") || taskEventFieldEntry.getValue().equals("Related Record") || taskEventFieldEntry.getValue().equals("Subject")) {
				assertTrue(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else if(!(taskEventFieldEntry.getValue().equals("Reminder Set")||taskEventFieldEntry.getValue().equals("Reminder Date/Time"))){
				assertTrue(softPhoneNewTask.isTaskSfPicklistFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Make a call and drop voicemail
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
	
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.giveCallRatings(driver1, 5);
		callToolsPanel.selectDisposition(driver1, 0);
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Change the subject of the call task and edit it then verify the data in the required task fields
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, callSubject);
		HashMap<SoftPhoneNewTask.taskFields, String> phoneTask = new HashMap<SoftPhoneNewTask.taskFields, String>();
		phoneTask.put(SoftPhoneNewTask.taskFields.Subject, callSubject);
		phoneTask.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		phoneTask.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Call.toString());
		phoneTask.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		phoneTask.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
		softPhoneActivityPage.verifyTaskAllFields(driver1, phoneTask);
		
		//Switch to app-qa and make the required setting enabled for default task fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName)) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			}
			assertTrue(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
		}
		
		//switch to softphone and open new task windows and verify that the default fields are required
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		for (Map.Entry<String, AccountSalesforceTab.salesforceFieldsType> taskEventFieldEntry: taskEventFieldsType.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldType = taskEventFieldEntry.getValue();
			if(taskEventFieldName.equals("Due Date") || taskEventFieldName.equals("Related Record") || taskEventFieldName.equals("Subject")) {
				assertTrue(softPhoneNewTask.isSalesforceFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}else if(!(taskEventFieldName.equals("Reminder Set")||taskEventFieldName.equals("Reminder Date/Time"))){
				assertTrue(softPhoneNewTask.isDefaultFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}
			
		}
		
		//click on save button and verify that error message is appearing
		softPhoneNewTask.clickSaveTaskButton(driver1);
		assertEquals(callScreenPage.getErrorText(driver1), "Please fill in the required fields.");
		
		//close the error message and enter values in the default required fields, save the task and verify that it is appearing in activity feeds
		callScreenPage.closeErrorBar(driver1);
		taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		//Switch to app-qa and disable the required setting for default fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if((!taskEventFieldName.equals("Subject") || taskEventFieldName.equals("Due Date")) && accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName)) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				//assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
			}
		}
	
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_tasks() {
		System.out.println("Test case --additional_fields_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		String taskEventField = null;
		String taskEventFieldName = null;
		AccountSalesforceTab.salesforceFieldsType taskEventFieldType = null;
		String taskEventFieldValue = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("ringdna__Call_Disposition__c", "Disposition");
		taskEventFields.put("ringdna__Abandoned_Call__c", "Abandoned Call");
		taskEventFields.put("ringdna__Call_Duration__c", "Duration");
		taskEventFields.put("ringdna__Call_Rating__c", "Rating");
		taskEventFields.put("CreatedDate", "Created Date");
		taskEventFields.put("ringdna__Call_Status__c", "Call Status");
		taskEventFields.put("ringdna__Call_Recording_URL__c", "Recording URL");
		taskEventFields.put("ringdna__Automated_Voicemail__c", "Automated Voicemail?");
		taskEventFields.put("ringdna__Automated_Voicemail_Link__c", "Automated Voicemail Link");
		taskEventFields.put("ringdna__Automated_Voicemail_Used__c","Automated Voicemail Used");
		
		//hash map for the task fields and their type
		taskEventFieldsType.put("Disposition", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Abandoned Call", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Duration", salesforceFieldsType.Double);
		taskEventFieldsType.put("Rating", salesforceFieldsType.Double);
		taskEventFieldsType.put("Created Date", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Call Status", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Recording URL", salesforceFieldsType.url);
		taskEventFieldsType.put("Automated Voicemail?", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Automated Voicemail Link", salesforceFieldsType.url);
		taskEventFieldsType.put("Automated Voicemail Used", salesforceFieldsType.string);
		
		//hash map for the task fields name and their value that will be used
		taskEventFieldsValue.put("Disposition", "Wrong Number");
		taskEventFieldsValue.put("Abandoned Call", "false");
		taskEventFieldsValue.put("Duration", "30");
		taskEventFieldsValue.put("Rating", "3");
		taskEventFieldsValue.put("Created Date", "");
		taskEventFieldsValue.put("Call Status", "Connected");
		taskEventFieldsValue.put("Recording URL", "test URL");
		taskEventFieldsValue.put("Automated Voicemail?", "true");
		taskEventFieldsValue.put("Automated Voicemail Link", "automated voice mail link");
		taskEventFieldsValue.put("Automated Voicemail Used", "automated voicemail");
		
		//open task field tab of salesforce settings page on app-qa 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		System.out.println("focus is on task tab of salesforce settings page");
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				if(!(taskEventFieldName.equals("Automated Voicemail?") || taskEventFieldName.equals("Abandoned Call"))) {
					assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
					System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				}else {
					assertTrue(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
					System.out.println("Salesforce task field " + taskEventFieldName + " is editable");
				}
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that no task field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Automated Voicemail?") || taskEventFieldEntry.getValue().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			}else {
				assertTrue(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			}
		}
		
		//data creation for adding a new task
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		
		//adding new task with the data mentioned above but no data is added for additional fields
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//Edit the task created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getKey().equals("Automated Voicemail?") || taskEventFieldEntry.getKey().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
			}else {
				assertTrue(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on task tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			if(!taskEventFieldName.equals("Created Date")) {
				assertTrue(accountSalesforceTab.isEditableCheckBoxEnable(driver1));
			}else {
				assertFalse(accountSalesforceTab.isEditableCheckBoxEnable(driver1));
			}
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		
		//switch to softphone, reload it and verify in the tasks that the additional fields are now visible but appearing disabled
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Automated Voicemail?") || taskEventFieldEntry.getValue().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else {
				System.out.println("boolean fields will be visible");
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//enable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//Make a call and drop voicemail
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
	
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.giveCallRatings(driver1, 5);
	    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Change the subject of the call task and edit it then verify the data in the additional fields
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, callSubject);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Disposition", taskEventFieldsType.get("Disposition")), callDisposition);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Rating", taskEventFieldsType.get("Rating")), "5");
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Automated Voicemail?", taskEventFieldsType.get("Automated Voicemail?")), "true");
		
		//Switch to app-qa and make the required setting enabled for additional fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!(taskEventField.equals("CreatedDate"))) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				assertTrue(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
			}
		}
		
		//switch to softphone and open new task windows and verify that the additional fields are required
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		for (Map.Entry<String, AccountSalesforceTab.salesforceFieldsType> taskEventFieldEntry: taskEventFieldsType.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldType = taskEventFieldEntry.getValue();
			if(!(taskEventFieldName.equals("Created Date"))) {
				assertTrue(softPhoneNewTask.isSalesforceFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}else {
				System.out.println("");
			}
			
		}
		
		//click on save button and verify that error message is appearing
		softPhoneNewTask.clickSaveTaskButton(driver1);
		assertEquals(callScreenPage.getErrorText(driver1), "Please fill in the required fields.");
		
		//close the error message and enter values in the required fields, save the task and verify that it is appearing in activity feeds
		callScreenPage.closeErrorBar(driver1);
		taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		softPhoneNewTask.enterTaskSubject(driver1, taskSubject);
		softPhoneNewTask.enterDueDate(driver1, dueDate);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFieldsValue.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldValue = taskEventFieldEntry.getValue();
			taskEventFieldType = taskEventFieldsType.get(taskEventFieldName);
			if(!(taskEventFieldName.equals("Created Date"))) {
				softPhoneNewTask.enterSalesforceFieldValues(driver1, taskEventFieldName, taskEventFieldType, taskEventFieldValue);
			}else {
				System.out.println("");
			}
			
		}
		softPhoneNewTask.clickSaveTaskButton(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		

		//verify the order of the task fields list
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		List<String> accountsCustomFieldList = accountSalesforceTab.getSfdcFieldsList(driver1);
		
		//move task fields two steps down
		accountSalesforceTab.moveSfdcFieldDown(driver1, "Automated Voicemail Used");
		accountSalesforceTab.moveSfdcFieldDown(driver1, "Automated Voicemail Used");
		
		//change the list accordingly
		accountsCustomFieldList.remove(0);
		accountsCustomFieldList.add(2, "Automated Voicemail Used");
		
		//verify that the new list as per the changed order
		assertEquals(accountSalesforceTab.getSfdcFieldsList(driver1), accountsCustomFieldList);
	
		//remove created date from the expected list
		accountsCustomFieldList.remove("Created Date");
		
		//verify the order is same on softphone tasks
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		assertEquals(softPhoneNewTask.getCustomFieldLists(driver1), accountsCustomFieldList);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		// add total 15 fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.CreatedByEmail, "Email", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.CreatedByUserName, "UserName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.OwnerLastName, "LastName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.OwnerFirstName, "FirstName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.AboutMe, "About Me", true, false, false);
		
		//update the task fields hash map with the new data
		taskEventFields.put("CreatedBy.Email", "Email");
		taskEventFields.put("CreatedBy.Username", "UserName");
		taskEventFields.put("Owner.LastName", "LastName");
		taskEventFields.put("Owner.FirstName", "FirstName");
		taskEventFields.put("CreatedBy.AboutMe", "About Me");
		
		//verify that add salesforce field become disabled after 15 fields has been added
		assertTrue(accountSalesforceTab.isFieldsAddBtnDisabled(driver1));
		
		//delete all the additional fields and close app-qa
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//disable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_tasks_created_by() {
		System.out.println("Test case --additional_fields_tasks_created_by()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("CreatedBy.CreatedDate", "Created Date (CreatedBy)");
		taskEventFields.put("CreatedBy.Username", "Username (CreatedBy)");
		taskEventFields.put("CreatedBy.Email", "Email (CreatedBy)");
		taskEventFields.put("CreatedBy.FirstName", "First Name (CreatedBy)");
		taskEventFields.put("CreatedBy.AboutMe", "About Me (CreatedBy)");
		taskEventFields.put("CreatedBy.IsActive", "Active (CreatedBy)");
		taskEventFields.put("CreatedBy.LastName", "Last Name (CreatedBy)");
		
		//hash map for the task fields and their type
		taskEventFieldsType.put("Created Date (CreatedBy)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (CreatedBy)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("About Me (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Active (CreatedBy)", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Last Name (CreatedBy)", salesforceFieldsType.string);
		
		//open task field tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that no task field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			
		}
		
		//data creation for adding a new task
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		
		//adding new task with the data mentioned above but no data is added for additional fields
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//Edit the task created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on task tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		
		//switch to softphone, reload it and create a taskthen verify in the tasks that the additional fields are now visible but appearing disabled
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		softPhoneNewTask.enterTaskSubject(driver1, taskSubject);
		softPhoneNewTask.enterDueDate(driver1, dueDate);
		softPhoneNewTask.clickSaveTaskButton(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Active (CreatedBy)"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//enable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//Make a call and drop voicemail
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.giveCallRatings(driver1, 5);
	    callToolsPanel.selectDisposition(driver1, 0);
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Change the subject of the call task and edit it then verify the data in the additional fields
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, callSubject);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (CreatedBy)", taskEventFieldsType.get("Username (CreatedBy)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (CreatedBy)", taskEventFieldsType.get("Email (CreatedBy)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (CreatedBy)", taskEventFieldsType.get("First Name (CreatedBy)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "About Me (CreatedBy)", taskEventFieldsType.get("About Me (CreatedBy)")), aboutMe);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Active (CreatedBy)", taskEventFieldsType.get("Active (CreatedBy)")), isActive);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (CreatedBy)", taskEventFieldsType.get("Last Name (CreatedBy)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//disable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_tasks_last_modified_by() {
		System.out.println("Test case --additional_fields_tasks_last_modified_by()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("LastModifiedBy.CreatedDate", "Created Date (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.Username", "Username (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.Email", "Email (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.FirstName", "First Name (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.AboutMe", "About Me (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.IsActive", "Active (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.LastName", "Last Name (LastModifiedBy)");
		
		//hash map for the task fields and their type
		taskEventFieldsType.put("Created Date (LastModifiedBy)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (LastModifiedBy)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("About Me (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Active (LastModifiedBy)", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Last Name (LastModifiedBy)", salesforceFieldsType.string);
		
		//open task field tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that no task field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			
		}
		
		//data creation for adding a new task
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		
		//adding new task with the data mentioned above but no data is added for additional fields
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//Edit the task created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//enable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//Make a call and drop voicemail
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.giveCallRatings(driver1, 5);
	    callToolsPanel.selectDisposition(driver1, 0);
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Change the subject of the call task and edit it then verify the data in the additional fields
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, callSubject);

		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (LastModifiedBy)", taskEventFieldsType.get("Username (LastModifiedBy)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (LastModifiedBy)", taskEventFieldsType.get("Email (LastModifiedBy)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (LastModifiedBy)", taskEventFieldsType.get("First Name (LastModifiedBy)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "About Me (LastModifiedBy)", taskEventFieldsType.get("About Me (LastModifiedBy)")), aboutMe);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Active (LastModifiedBy)", taskEventFieldsType.get("Active (LastModifiedBy)")), isActive);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (LastModifiedBy)", taskEventFieldsType.get("Last Name (LastModifiedBy)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//disable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_tasks_owner() {
		System.out.println("Test case --additional_fields_tasks_owner()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("CreatedBy.CreatedDate", "Created Date (CreatedBy)");
		taskEventFields.put("Owner.Username", "Username (Owner)");
		taskEventFields.put("Owner.Email", "Email (Owner)");
		taskEventFields.put("Owner.FirstName", "First Name (Owner)");
		taskEventFields.put("Owner.LastName", "Last Name (Owner)");
		taskEventFields.put("Owner.UserRoleId", "Role ID (Owner)");
		
		//hash map for the task fields and their type
		taskEventFieldsType.put("Created Date (CreatedBy)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (Owner)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Role ID (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Last Name (Owner)", salesforceFieldsType.string);
		
		//open task field tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that no task field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
		}
		
		//data creation for adding a new task
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		
		//adding new task with the data mentioned above but no data is added for additional fields
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//Edit the task created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
		assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		assertTrue(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on task t
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		
		//switch to softphone, reload it, create a new task and verify in the tasks that the additional fields are now visible but appearing disabled
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		softPhoneNewTask.enterTaskSubject(driver1, taskSubject);
		softPhoneNewTask.enterDueDate(driver1, dueDate);
		softPhoneNewTask.clickSaveTaskButton(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Role ID (Owner)"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//enable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//Make a call and drop voicemail
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//dropping Voice Mail
	    callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.giveCallRatings(driver1, 5);
	    callToolsPanel.selectDisposition(driver1, 0);
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Change the subject of the call task and edit it then verify the data in the additional fields
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, callSubject);

		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (Owner)", taskEventFieldsType.get("Username (Owner)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (Owner)", taskEventFieldsType.get("Email (Owner)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (Owner)", taskEventFieldsType.get("First Name (Owner)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (Owner)", taskEventFieldsType.get("Last Name (Owner)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//disable local presence setting on softphone
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_events() {
		System.out.println("Test case --additional_fields_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		String taskEventField = null;
		String taskEventFieldName = null;
		AccountSalesforceTab.salesforceFieldsType taskEventFieldType = null;
		String taskEventFieldValue = null;
		
		//Hash map for the event fields Name that will be used
		taskEventFields.put("ringdna__Call_Disposition__c", "Disposition");
		taskEventFields.put("ringdna__Abandoned_Call__c", "Abandoned Call");
		taskEventFields.put("ringdna__Call_Duration__c", "Duration");
		taskEventFields.put("ringdna__Call_Rating__c", "Rating");
		taskEventFields.put("CreatedDate", "Created Date");
		taskEventFields.put("ringdna__Call_Status__c", "Call Status");
		taskEventFields.put("ringdna__Call_Recording_URL__c", "Recording URL");
		taskEventFields.put("ringdna__Automated_Voicemail__c", "Automated Voicemail?");
		taskEventFields.put("ringdna__Automated_Voicemail_Link__c", "Automated Voicemail Link");
		taskEventFields.put("ringdna__Automated_Voicemail_Used__c","Automated Voicemail Used");
		
		//hash map for the event fields and their type
		taskEventFieldsType.put("Disposition", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Abandoned Call", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Duration", salesforceFieldsType.Double);
		taskEventFieldsType.put("Rating", salesforceFieldsType.Double);
		taskEventFieldsType.put("Created Date", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Call Status", salesforceFieldsType.picklist);
		taskEventFieldsType.put("Recording URL", salesforceFieldsType.url);
		taskEventFieldsType.put("Automated Voicemail?", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Automated Voicemail Link", salesforceFieldsType.url);
		taskEventFieldsType.put("Automated Voicemail Used", salesforceFieldsType.string);
		
		//hash map for the event fields name and their value that will be used
		taskEventFieldsValue.put("Disposition", "Wrong Number");
		taskEventFieldsValue.put("Abandoned Call", "false");
		taskEventFieldsValue.put("Duration", "30");
		taskEventFieldsValue.put("Rating", "3");
		taskEventFieldsValue.put("Created Date", "");
		taskEventFieldsValue.put("Call Status", "Connected");
		taskEventFieldsValue.put("Recording URL", "test URL");
		taskEventFieldsValue.put("Automated Voicemail?", "true");
		taskEventFieldsValue.put("Automated Voicemail Link", "automated voice mail link");
		taskEventFieldsValue.put("Automated Voicemail Used", "automated voicemail");
		
		//open event field tab of salesforce settings page on app-qa 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		System.out.println("focus is on task tab of salesforce settings page");
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				if(!(taskEventFieldName.equals("Automated Voicemail?") || taskEventFieldName.equals("Abandoned Call"))) {
					assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
					System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				}else {
					assertTrue(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
					System.out.println("Salesforce task field " + taskEventFieldName + " is editable");
				}
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and open event window then verify that no additional event field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Automated Voicemail?") || taskEventFieldEntry.getValue().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			}else {
				assertTrue(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
			}
		}
		
		//adding new event
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		
		//Edit the event created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getKey().equals("Automated Voicemail?") || taskEventFieldEntry.getKey().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
			}else {
				assertTrue(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on task tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			if(!taskEventFieldName.equals("Created Date")) {
				assertTrue(accountSalesforceTab.isEditableCheckBoxEnable(driver1));
			}else {
				assertFalse(accountSalesforceTab.isEditableCheckBoxEnable(driver1));
			}
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		
		//switch to softphone, reload it and verify in the tasks that the additional fields are now visible but appearing disabled
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Automated Voicemail?") || taskEventFieldEntry.getValue().equals("Abandoned Call"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else {
				System.out.println("boolean fields will be visible");
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
				
		//Switch to app-qa and make the editable setting enabled for additional fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!(taskEventField.equals("CreatedDate"))) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkEditableCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				assertTrue(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
			}
		}
		
		//switch to softphone, reload it and verify in the tasks that the additional fields are now editable
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!((taskEventFieldEntry.getValue().equals("Created Date")||taskEventFieldEntry.getValue().equals("Automated Voicemail?") || taskEventFieldEntry.getValue().equals("Abandoned Call")))) {
				System.out.println("verifying that field " + taskEventFieldEntry.getValue() + " is editable");
				assertTrue(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));	
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Switch to app-qa and make the required setting enabled for additional fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!(taskEventField.equals("CreatedDate"))) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				assertTrue(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
			}
		}
		
		//switch to softphone and open new event windows and verify that the additional fields are required
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, AccountSalesforceTab.salesforceFieldsType> taskEventFieldEntry: taskEventFieldsType.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldType = taskEventFieldEntry.getValue();
			if(!(taskEventFieldName.equals("Created Date"))) {
				assertTrue(softPhoneNewTask.isSalesforceFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}else {
				System.out.println("");
			}
		}
		
		//click on save button and verify that error message is appearing
		softPhoneNewEvent.saveEvent(driver1);
		assertEquals(callScreenPage.getErrorText(driver1), "Please fill in the required fields.");
		
		//close the error message and enter values in the required fields, save the event and verify that it is appearing in activity feeds
		callScreenPage.closeErrorBar(driver1);
		eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFieldsValue.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldValue = taskEventFieldEntry.getValue();
			taskEventFieldType = taskEventFieldsType.get(taskEventFieldName);
			if(!(taskEventFieldName.equals("Created Date"))) {
				softPhoneNewTask.enterSalesforceFieldValues(driver1, taskEventFieldName, taskEventFieldType, taskEventFieldValue);
			}else {
				System.out.println("");
			}
		}
		softPhoneNewEvent.saveEvent(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, eventSubject);

		//verify the order of the event fields list
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		List<String> accountsCustomFieldList = accountSalesforceTab.getSfdcFieldsList(driver1);
		
		//move task fields two steps down
		accountSalesforceTab.moveSfdcFieldDown(driver1, "Automated Voicemail Used");
		accountSalesforceTab.moveSfdcFieldDown(driver1, "Automated Voicemail Used");
		
		//change the list accordingly
		accountsCustomFieldList.remove(0);
		accountsCustomFieldList.add(2, "Automated Voicemail Used");
		
		//verify that the new list as per the changed order
		assertEquals(accountSalesforceTab.getSfdcFieldsList(driver1), accountsCustomFieldList);
	
		//remove created date from the expected list
		accountsCustomFieldList.remove("Created Date");
		
		//verify the order is same on softphone events
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		assertEquals(softPhoneNewTask.getCustomFieldLists(driver1), accountsCustomFieldList);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		// add total 15 fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.CreatedByEmail, "Email", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.CreatedByUserName, "UserName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.OwnerLastName, "LastName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.OwnerFirstName, "FirstName", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1, SalesForceFields.AboutMe, "About Me", true, false, false);
		
		//update the task fields hash map with the new data
		taskEventFields.put("CreatedBy.Email", "Email");
		taskEventFields.put("CreatedBy.Username", "UserName");
		taskEventFields.put("Owner.LastName", "LastName");
		taskEventFields.put("Owner.FirstName", "FirstName");
		taskEventFields.put("CreatedBy.AboutMe", "About Me");
		
		//verify that add salesforce field become disabled after 15 fields has been added
		assertTrue(accountSalesforceTab.isFieldsAddBtnDisabled(driver1));
		
		//delete all the additional fields and close app-qa
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_event_created_by() {
		System.out.println("Test case --additional_fields_event_created_by()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the event fields Name that will be used
		taskEventFields.put("CreatedBy.CreatedDate", "Created Date (CreatedBy)");	
		taskEventFields.put("CreatedBy.Username", "Username (CreatedBy)");
		taskEventFields.put("CreatedBy.Email", "Email (CreatedBy)");
		taskEventFields.put("CreatedBy.FirstName", "First Name (CreatedBy)");
		taskEventFields.put("CreatedBy.AboutMe", "About Me (CreatedBy)");
		taskEventFields.put("CreatedBy.IsActive", "Active (CreatedBy)");
		taskEventFields.put("CreatedBy.LastName", "Last Name (CreatedBy)");
		
		//Hash map for the event fields Name that will be used
		taskEventFieldsType.put("Created Date (CreatedBy)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (CreatedBy)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("About Me (CreatedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Active (CreatedBy)", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Last Name (CreatedBy)", salesforceFieldsType.string);
		
		//open task event tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new event fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on evnt icon then verify that no event field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
		}
		
		//adding new event but no data is added for additional fields 
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		
		//Edit the event created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on event tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		
		//switch to softphone, reload it, add an event
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		eventSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, eventSubject);
		
		//edit the created event and verify in the events that the additional fields are now visible but appearing disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Active (CreatedBy)"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}

		//verify the data in the additional fields
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (CreatedBy)", taskEventFieldsType.get("Username (CreatedBy)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (CreatedBy)", taskEventFieldsType.get("Email (CreatedBy)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (CreatedBy)", taskEventFieldsType.get("First Name (CreatedBy)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "About Me (CreatedBy)", taskEventFieldsType.get("About Me (CreatedBy)")), aboutMe);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Active (CreatedBy)", taskEventFieldsType.get("Active (CreatedBy)")), isActive);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (CreatedBy)", taskEventFieldsType.get("Last Name (CreatedBy)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups = { "MediumPriority" })
	public void additional_fields_event_last_modified_by() {
		System.out.println("Test case --additional_fields_event_last_modified_by()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the event fields Name that will be used
		taskEventFields.put("LastModifiedBy.CreatedDate", "Created Date (LastModifiedBy)");	
		taskEventFields.put("LastModifiedBy.Username", "Username (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.Email", "Email (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.FirstName", "First Name (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.AboutMe", "About Me (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.IsActive", "Active (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.LastName", "Last Name (LastModifiedBy)");
		
		//hash map for the event fields and their type
		taskEventFieldsType.put("Created Date (LastModifiedBy)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (LastModifiedBy)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("About Me (LastModifiedBy)", salesforceFieldsType.string);
		taskEventFieldsType.put("Active (LastModifiedBy)", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Last Name (LastModifiedBy)", salesforceFieldsType.string);
		
		//open event field tab of salesforce settings page on app-qa 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new event fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on event icon then verify that no event field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
		}
		
		//adding new event
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		
		//Edit the event created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on event tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//edit the created event and verify in the events that the additional fields are now visible but appearing disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Active (LastModifiedBy)"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else {
				System.out.println("");
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//add another event and edit it to verify data in additional fields
		
		softPhoneNewEvent.clickEventTab(driver1);
		eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);

		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (LastModifiedBy)", taskEventFieldsType.get("Username (LastModifiedBy)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (LastModifiedBy)", taskEventFieldsType.get("Email (LastModifiedBy)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (LastModifiedBy)", taskEventFieldsType.get("First Name (LastModifiedBy)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "About Me (LastModifiedBy)", taskEventFieldsType.get("About Me (LastModifiedBy)")), aboutMe);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Active (LastModifiedBy)", taskEventFieldsType.get("Active (LastModifiedBy)")), isActive);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (LastModifiedBy)", taskEventFieldsType.get("Last Name (LastModifiedBy)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_event_owner() {
		System.out.println("Test case --additional_fields_event_owner()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the event fields Name that will be used
		taskEventFields.put("CreatedBy.CreatedDate", "Created Date (CreatedBy)");	
		taskEventFields.put("Owner.Username", "Username (Owner)");
		taskEventFields.put("Owner.Email", "Email (Owner)");
		taskEventFields.put("Owner.FirstName", "First Name (Owner)");
		taskEventFields.put("Owner.UserRoleId", "Role ID (Owner)");
		taskEventFields.put("Owner.LastName", "Last Name (Owner)");
		
		//hash map for the event fields and their type
		taskEventFieldsType.put("Created Date (Owner)", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Username (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Email (Owner)", salesforceFieldsType.email);
		taskEventFieldsType.put("First Name (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Role ID (Owner)", salesforceFieldsType.string);
		taskEventFieldsType.put("Last Name (Owner)", salesforceFieldsType.string);
		
		//open event field tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
				assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				
				//verify that editable and required checkebox are not checked by default
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
				System.out.println("added field " + taskEventFieldName);
				assertFalse(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not always visible");
				assertFalse(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not editable");
				assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
				System.out.println("Salesforce task field " + taskEventFieldName + " is not requied");
		}
		
		//switch to softphone tab and reload it to reflect new event fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on event icon then verify that no event field is appearing since they have always visible setting disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskCreate(driver1, taskEventFieldEntry.getValue()));
		}
		
		//adding new event 
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		
		//Edit the event created above and verify that none of the additional fields are visible other than boolean fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			assertFalse(softPhoneNewTask.isTaskSfFieldVisibleOnTaskEdit(driver1, taskEventFieldEntry.getKey()));
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Move to app-qa and enable always visible setting and verify that setting is appearing in the fields list on event tab
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
			assertTrue(accountSalesforceTab.isAlwaysVisibleCheckBoxEnable(driver1));
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.getSelectedSfFieldName(driver1).contains(taskEventFieldName));
			assertTrue(accountSalesforceTab.getSelectedSfField(driver1).contains(taskEventField));
			accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
			accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			accountSalesforceTab.idleWait(1);
			assertTrue(accountSalesforceTab.isAddedSfFiedlVisible(driver1, taskEventFieldName));
		}
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//edit the created event and verify in the events that the additional fields are now visible but appearing disabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			if(!(taskEventFieldEntry.getValue().equals("Role ID (Owner)"))) {
				assertFalse(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}else {
				System.out.println("");
			}
		}
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		// add a new event and edit it to verify the data in the additional fields
		
		softPhoneNewEvent.clickEventTab(driver1);
		eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.saveEvent(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);

		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Username (Owner)", taskEventFieldsType.get("Username (Owner)")), userName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (Owner)", taskEventFieldsType.get("Email (Owner)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "First Name (Owner)", taskEventFieldsType.get("First Name (Owner)")), firstName);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Last Name (Owner)", taskEventFieldsType.get("Last Name (Owner)")), lastName);
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all the additional fields and close app-qa
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void additional_fields_tasks_address_type_fields() {
		System.out.println("Test case --additional_fields_tasks_address_type_fields()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Variables declaration
		taskEventFields = new HashMap<>();
		String taskEventField = null;
		String taskEventFieldName = null;
		
		//Hash map for the task fields Name that will be used
		taskEventFields.put("LastModifiedBy.Email", "Email (LastModifiedBy)");
		taskEventFields.put("LastModifiedBy.Address", "Address (LastModifiedBy)");
		taskEventFields.put("CreatedBy.Address", "Address (CreatedBy)");
	
		//hash map for the task fields and their type
		taskEventFieldsType.put("Email (LastModifiedBy)", salesforceFieldsType.email);
		taskEventFieldsType.put("Address (LastModifiedBy)", salesforceFieldsType.address);
		taskEventFieldsType.put("Address (CreatedBy)", salesforceFieldsType.address);
		
		//open task field tab of salesforce settings page on app-qa
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		
		//Create additional fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
				taskEventField = taskEventFieldEntry.getKey();
				taskEventFieldName = taskEventFieldEntry.getValue();
				accountSalesforceTab.clickAdditionalLeadFieldsBtn(driver1);
				accountSalesforceTab.selectSalesForceField(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkAlwaysVisibleCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				assertTrue(accountSalesforceTab.isAddditionalFieldCreated(driver1, taskEventFieldName));
		}
		
		//switch to softphone tab and reload it to reflect new task fields added
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//add a new task
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		softPhoneNewTask.enterTaskSubject(driver1, taskSubject);
		softPhoneNewTask.enterDueDate(driver1, dueDate);
		softPhoneNewTask.clickSaveTaskButton(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		// edit the task and verify the data in the additional fields
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, "Automation Subject 1");
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Email (LastModifiedBy)", taskEventFieldsType.get("Email (LastModifiedBy)")), email);
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Address (LastModifiedBy)", taskEventFieldsType.get("Address (LastModifiedBy)")), "Delhi\n India\n itv5 Metacube");
		assertEquals(softPhoneNewTask.getTaskSalesforceFieldValues(driver1, "Address (CreatedBy)", taskEventFieldsType.get("Address (CreatedBy)")), "Delhi\n India\n itv5 Metacube");
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//delete all fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getValue();
			accountSalesforceTab.deleteAdditionalField(driver1, taskEventFieldName);
		}
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void default_fields_events() {
		System.out.println("Test case --default_fields_events()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Variables declaration
		String taskEventField = null;
		String taskEventFieldName = null;
		AccountSalesforceTab.salesforceFieldsType taskEventFieldType = null;
		
		//Hash map for the event fields Name that will be used
		taskEventFields.put("Description", "Description");
		taskEventFields.put("IsAllDayEvent", "All Day Event");
		taskEventFields.put("EndDateTime", "End");
		taskEventFields.put("ReminderDateTime", "Reminder Date/Time");
		taskEventFields.put("IsReminderSet", "Reminder Set");
		taskEventFields.put("StartDateTime", "Start");
		taskEventFields.put("Subject", "Subject");
		
		//hash map for the event fields and their type
		taskEventFieldsType.put("Description", salesforceFieldsType.address);
		taskEventFieldsType.put("All Day Event", salesforceFieldsType.datetime);
		taskEventFieldsType.put("End", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Reminder Date/Time", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Reminder Set", salesforceFieldsType.Boolean);
		taskEventFieldsType.put("Start", salesforceFieldsType.datetime);
		taskEventFieldsType.put("Subject", salesforceFieldsType.string);
		
		//open task field tab of salesforce settings page on app-qa 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		System.out.println("focus is on task tab of salesforce settings page");
		
		// make the editable setting enabled for default fields
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!(taskEventField.equals("CreatedDate"))) {
				System.out.println("makine field " + taskEventFieldName + " required");
				if(!accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName)) {
					accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
					accountSalesforceTab.idleWait(1);
					accountSalesforceTab.checkEditableCheckBox(driver1);
					accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				}

				assertTrue(accountSalesforceTab.isAddedSfFiedlEditable(driver1, taskEventFieldName));
			}
		}
		
		//switch to softphone tab and reload it to reflect event fields changes
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Open recent entry from call history page and click on task icon then verify that event field are enabled
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			System.out.println(taskEventFieldEntry.getValue());
			if(taskEventFieldEntry.getValue().equals("Start") || taskEventFieldEntry.getValue().equals("End") || taskEventFieldEntry.getValue().equals("Subject")) {
				assertTrue(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		assertTrue(softPhoneNewTask.isTaskSfCheckBoxFieldEnabled(driver1, "All Day"));
		assertTrue(softPhoneNewTask.isTaskSfCheckBoxFieldEnabled(driver1, "Reminder"));
		assertTrue(softPhoneNewTask.isTaskSfTimeFieldEnabled(driver1, "StartDateTime"));
		assertTrue(softPhoneNewTask.isTaskSfTimeFieldEnabled(driver1, "EndDateTime"));
		

		//data creation for adding a new task
		// Start Time is current hour + 2 hrs
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		Calendar c = Calendar.getInstance();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		c.add(Calendar.DATE, 2);
		String endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartTime, startTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndTime, endTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Reminder, SoftPhoneNewEvent.reminderTime.twodays.toString());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);

		
		//adding new task with the data mentioned above
		softPhoneNewEvent.addNewEventWithAllFields(driver1, eventDetails);
		
		//Edit the task created above and verify that default fields are editable
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			System.out.println(taskEventFieldEntry.getValue());
			if(taskEventFieldEntry.getValue().equals("Start") || taskEventFieldEntry.getValue().equals("End") || taskEventFieldEntry.getValue().equals("Subject")) {
				assertTrue(softPhoneNewTask.isTaskSfFieldEnabled(driver1, taskEventFieldEntry.getValue()));
			}
		}
		assertTrue(softPhoneNewTask.isTaskSfCheckBoxFieldEnabled(driver1, "All Day"));
		assertTrue(softPhoneNewTask.isTaskSfCheckBoxFieldEnabled(driver1, "Reminder"));
		assertTrue(softPhoneNewTask.isTaskSfTimeFieldEnabled(driver1, "StartDateTime"));
		assertTrue(softPhoneNewTask.isTaskSfTimeFieldEnabled(driver1, "EndDateTime"));
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Switch to app-qa and make the required setting enabled for default event fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if(!accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName)) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
			}
			assertTrue(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
		}
		
		//switch to softphone and open new task windows and verify that the default fields are required
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		softPhoneNewEvent.clickEventTab(driver1);
		for (Map.Entry<String, AccountSalesforceTab.salesforceFieldsType> taskEventFieldEntry: taskEventFieldsType.entrySet()) {
			taskEventFieldName = taskEventFieldEntry.getKey();
			taskEventFieldType = taskEventFieldEntry.getValue();
			if(taskEventFieldName.equals("Start")) {
				assertTrue(softPhoneNewTask.isSalesforceFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}else if(taskEventFieldName.equals("Description") ) {
				assertTrue(softPhoneNewTask.isDefaultFieldRequired(driver1, taskEventFieldName, taskEventFieldType));
			}
		}
		
		//click on save button and verify that error message is appearing
		softPhoneNewTask.clickSaveTaskButton(driver1);
		assertEquals(callScreenPage.getErrorText(driver1), "Please fill in the required fields.");
		
		//close the error message and enter values in the default required fields, save the task and verify that it is appearing in activity feeds
		callScreenPage.closeErrorBar(driver1);
		eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.enterDescription(driver1, "This is a test description");
		softPhoneNewEvent.saveEvent(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, eventSubject);
		
		//Switch to app-qa and disable the required setting for default fields
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		for (Map.Entry<String, String> taskEventFieldEntry: taskEventFields.entrySet()) {
			taskEventField = taskEventFieldEntry.getKey();
			taskEventFieldName = taskEventFieldEntry.getValue();
			if((!taskEventFieldName.equals("Subject")) && accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName)) {
				System.out.println("makine field " + taskEventFieldName + " required");
				accountSalesforceTab.clickUpddateBtn(driver1, taskEventField);
				accountSalesforceTab.idleWait(1);
				accountSalesforceTab.checkRequiredCheckBox(driver1);
				accountSalesforceTab.clickSalesForceSaveBtn(driver1);
				//assertFalse(accountSalesforceTab.isAddedSfFiedlRequired(driver1, taskEventFieldName));
			}
		}
	
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void afterMethod(ITestResult result){
		if(result.getStatus() == 2 || result.getStatus() == 3) {
			
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			System.out.println("Account editor is opened ");
			accountSalesforceTab.openSalesforceTab(driver1);
			accountSalesforceTab.navigateToTaskFieldsSection(driver1);
			accountSalesforceTab.deleteAllFieldsIfExists(driver1);
			accountSalesforceTab.navigateToEventFieldsSection(driver1);
			accountSalesforceTab.deleteAllFieldsIfExists(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		}
	}
}
