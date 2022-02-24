/**
 * 
 */
package softphone.cases.softPhoneFunctional.callTools;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallToolsNewTaskDueDateDisable extends SoftphoneBase{
	  
	  public void disableDueDate(){
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//add contact
		aa_AddCallersAsContactsAndLeads();
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");		  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));
		
		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//disabling Task due date option
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		accountSalesforceTab.disableTaskDueDateRequiredSetting(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	  }
	  
	  @Test(groups = { "Regression", "MediumPriority" , "Product Sanity"}, priority = 51)
		public void call_tools_new_task_other_type_due_date_disable_due_date() {
			System.out.println("Test case --call_tools_new_task_other_type_due_date_disable_due_date()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//dsabling due date and adding contact again
			disableDueDate();
			
			//open recent call history
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
			
			//Adding a new task for receiver
			HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
			String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
			String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
			String reminderDate = SoftPhoneNewTask.taskDropdownDates.Today.toString();
			String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
			String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
			taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown, reminderDate);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
			taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
			reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
			
			//verifying task created time 
			String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
			String createdTime	= HelperFunctions.getDateTimeInTimeZone(HelperFunctions.GetCurrentDateTime("MM/dd/yy hh:mm a", true), userTimeZone, "MM/dd/yy hh:mm a");
			String createdTime1	= HelperFunctions.getDateTimeInTimeZone(HelperFunctions.GetCurrentDateTime("MM/dd/yy hh:mm a", false), userTimeZone, "MM/dd/yy hh:mm a");
			String actualCreatedTime = softPhoneActivityPage.getEventCreationDate(driver1, taskSubject);
			assertTrue(actualCreatedTime.equals(createdTime) || actualCreatedTime.equals(createdTime1));
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
			
			//verify that task is appearing under Open task tabs
			softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
			
			//verify related record on caller detail
			callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
			
		    //Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task data in salesforce
			sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    //Update details of the tasks from activity feeds section
		    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		    Calendar c = Calendar.getInstance();
		    c.add(Calendar.DATE, 1);
			String newReminderDate = SoftPhoneNewTask.taskDropdownDates.Today.toString();
			String newReminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
		    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown, newReminderDate);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, newReminderTime);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
			softPhoneActivityPage.editTask(driver1, taskSubject);
			newReminderTime = softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			newReminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(newReminderDate, userTimeZone, dateFormat));
			taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(newReminderTime, userTimeZone, timeFormat));
			
			//verify related record on caller detail
			callScreenPage.verifyRelatedRecordNotPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
			callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_campaign"));
			
			//Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);
			
		    //Check task data in salesforce
			sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    	    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
	  
	  @Test(groups = { "Regression" }, priority = 52)
		public void call_tools_update_new_task_call_type_due_date_disable_due_date() {
			System.out.println("Test case --call_tools_update_new_task_call_type_due_date_disable_due_date()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			
			//open recent call history
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
			
			//Adding a new task for receiver
			HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
			String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
			String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
			String reminderDate = SoftPhoneNewTask.taskDropdownDates.Today.toString();
			String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
			String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
			taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown, reminderDate);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
			taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Call.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
			reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
			reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
			
			//verify that task is appearing under Open task tabs
			softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
			
			//verify related record on caller detail
			callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
			
		    //Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task data in salesforce
			sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    
		    //Update details of the tasks from activity feeds section
		    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		    Calendar c = Calendar.getInstance();
		    c.add(Calendar.DATE, 1);
			String newReminderDate = SoftPhoneNewTask.taskDropdownDates.Today.toString();
			String newReminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
		    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());		    
		    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown, newReminderDate);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, newReminderTime);
		    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_Account"));
			softPhoneActivityPage.editTask(driver1, taskSubject);
			newReminderTime = softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			newReminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(newReminderDate, userTimeZone, dateFormat));
			taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(newReminderTime, userTimeZone, timeFormat));
			
			//verify related record on caller detail
			callScreenPage.verifyRelatedRecordNotPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
			callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_Account"));
			
			//Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);
			
		    //Check task data in salesforce
			sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    	    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
	  
		@Test(groups = { "Regression", "Product Sanity" }, priority = 53)
		public void call_tools_new_task_call_type_due_date_disable_due_date() {
			System.out.println("Test case --call_tools_new_task_call_type_due_date_disable_due_date()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//open recent call history
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
			
			//Adding a new task for receiver
			HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
			String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
			taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
			taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Call.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_Account"));
			seleniumBase.idleWait(2);
			softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
			
			//verify that task is appearing under Open tabs
			softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
			
			//verify related record on caller detail
			callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_Account"));
			
		    //Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task Reminder Time
		    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);

		    //Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
		
		@Test(groups = { "Regression" }, priority = 54)
		public void call_tools_new_task_none_type_due_date_disable_due_date() {
			System.out.println("Test case --call_tools_new_task_none_type_due_date_disable_due_date()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//open recent call history
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
			
			//Adding a new task for receiver
			HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
			String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
			String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
			String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
			String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
			taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
			taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.None.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
			reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
			
			//verify that task is appearing under Open task tabs
			softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
			
		    //Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task Reminder Time
		    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    //Verify option Mark Complete and New
		    softPhoneActivityPage.markTaskCompleteAndOpenNewTask(driver1, taskSubject);
		    assertEquals(softPhoneActivityPage.getTaskSubject(driver1), taskSubject);
		    softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		    softPhoneActivityPage.isTaskUndoBarVisible(driver1, taskSubject);
		    
		    //Open task in salesforce from All Activity feeds section
		    taskDetails.remove(SoftPhoneNewTask.taskFields.ReminderDate);
		    taskDetails.remove(SoftPhoneNewTask.taskFields.ReminderTime);
		    taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task Reminder Time
		    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
	  
		@Test(groups = { "Regression" }, priority = 55)
		public void call_tools_new_task_edit_save_and_new_task() {
			System.out.println("Test case --call_tools_new_task_edit_save_and_new_task()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//open recent call history
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		    
			//Adding a new task for receiver
			HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
			String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
			String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
			String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
			String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
			taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
			taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.None.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
			taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
			reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
			
			//getting reminder date and time in qa user 1 time zone in salesforce
			String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
			taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
			
			//verify that task is appearing under Open task tabs
			softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
			
		    //Open task in salesforce from All Activity feeds section
			seleniumBase.idleWait(2);
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task Reminder Time
		    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    //Verify option Mark Complete and New
		    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Meeting.toString());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
		    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
		    seleniumBase.idleWait(2);
		    softPhoneActivityPage.editTask(driver1, taskSubject);
		    softPhoneNewTask.enterTaskDetails(driver1, taskUpdates);
		    softPhoneActivityPage.saveAndOpenNewTaskWindow(driver1);
		    
		    assertEquals(softPhoneActivityPage.getTaskSubject(driver1), taskSubject);
		    softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		    
		    seleniumBase.idleWait(3);
		    
		    //Open task in salesforce from All Activity feeds section
			softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		    
		    //Check task data
		    sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
		
		@Test(groups = { "Regression", "MediumPriority", "Product Sanity"}, priority = 56)
		public void call_tools_new_task_icon_unknown_caller() {
			
			System.out.println("Test case --call_tools_new_task_icon_unknown_caller()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver3");
			driverUsed.put("driver3", true);
			
			//Calling from Agent's SoftPhone
			softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
			
			// receiving call from receiver
			softPhoneCalling.pickupIncomingCall(driver1);
			
			//Deleting contact and calling again
			if(!callScreenPage.isCallerUnkonwn(driver1)){
				callScreenPage.deleteCallerObject(driver1);
				softPhoneCalling.hangupActiveCall(driver1);
				softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
				softPhoneCalling.pickupIncomingCall(driver1);
			}
			
			//hangup Call
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver1);
			
			//verify task icon is not visible
			softPhoneNewTask.verifyTaskIconInvisible(driver1);
			
			//Calling from Agent's SoftPhone
			softPhoneCalling.enterNumberAndDial(driver1, CONFIG.getProperty("skype_number"));
			seleniumBase.idleWait(5);
			
			//hangup Call
			softPhoneCalling.hangupIfInActiveCall(driver1);
			
			//is caller multiple
			assertTrue(callScreenPage.isCallerMultiple(driver1));
			
			//verify task icon is not visible
			softPhoneNewTask.verifyTaskIconInvisible(driver1);
			
			//enabling due date
			enableDueDate();
			
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver3", false);
			
		    System.out.println("Test case is pass");
	  }
		
	  public void enableDueDate(){
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//disabling Task due date option
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		accountSalesforceTab.enableTaskDueDateRequiredSetting(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	  }
}
