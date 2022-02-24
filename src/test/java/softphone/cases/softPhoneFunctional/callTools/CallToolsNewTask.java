package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import softphone.source.callTools.SoftPhoneNewTask.TaskStatuses;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallToolsNewTask extends SoftphoneBase{


	@BeforeClass(groups = {"Regression", "Sanity", "QuickSanity", "MediumPriority"})
	public void beforeClass() {

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// add number as contact
		aa_AddCallersAsContactsAndLeads();
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");	  
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	
	@Test(groups = { "Regression", "Product Sanity" }, priority = 30)
	public void call_tools_new_task_email_type() {
		System.out.println("Test case --call_tools_new_task_email_type()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		String caseOnCallerScreen = CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case");

		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver 
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
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
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
	
		//getting reminder date and time in qa user 1 time zone in salesforce
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));
		
		//verify that task is appearing under Open and Email task tabs
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskInEmailTaskTab(driver1, taskSubject);
		
		//verify related record on caller detail
		callScreenPage.verifyRelatedRecordPresent(driver1, caseOnCallerScreen);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task data on salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
	    //Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
	    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DeleteReminder, "true");
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//verify that task is appearing under Open and Email task tabs
		softPhoneActivityPage.verifyTaskInvisibleOpenTaskTab(driver1, newTaskSubject);
		
		//verify related record on caller detail
		callScreenPage.verifyRelatedRecordNotPresent(driver1, caseOnCallerScreen);
		callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);
			    
		//Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority", "Product Sanity"}, priority = 31)
	public void call_tools_new_task_other_type() {
		System.out.println("Test case --call_tools_new_task_other_type()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
		String reminderDate = SoftPhoneNewTask.taskDropdownDates.Today.toString();
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//taking two created time one is exact and one in which one minute is added
		String createdTime	= HelperFunctions.getDateTimeInTimeZone(HelperFunctions.GetCurrentDateTime("MM/dd/yy hh:mm a", true), userTimeZone, "MM/dd/yy hh:mm a");
		String createdTime1	= HelperFunctions.getDateTimeInTimeZone(HelperFunctions.GetCurrentDateTime("MM/dd/yy hh:mm a", false), userTimeZone, "MM/dd/yy hh:mm a");
		String dueDateOnActivity = HelperFunctions.getDateTimeInTimeZone(dueDate, userTimeZone, "MM/dd/yy");
		
		//getting reminder date and time in qa user 1 time zone in salesforce
		reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
		
		//verify that task is appearing under Open task tabs
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		String actualCreatedTime = softPhoneActivityPage.getEventCreationDate(driver1, taskSubject);
		assertTrue(actualCreatedTime.equals(createdTime) || actualCreatedTime.equals(createdTime1));
		assertEquals(softPhoneActivityPage.getTaskDueDate(driver1, taskSubject), dueDateOnActivity);
		
	    //verify related record on caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
		
		//verify that open task has red borded
		softPhoneActivityPage.verifyOpenTasksRedBorder(driver1, taskSubject);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task Reminder Time
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Update the due date of the task
	    String newDueDate = SoftPhoneNewTask.taskDropdownDates.Tomorrow.toString();
	    softPhoneActivityPage.changeTaskDueDateFromList(driver1, taskSubject, newDueDate);
	 
	    //updating the value of due date in hash map
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String dueDateToVerify = new SimpleDateFormat(dateFormat).format(c.getTime());
	    taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(new SimpleDateFormat(dateFormat).format(c.getTime()), userTimeZone, dateFormat));
	    taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDateToVerify);
	    seleniumBase.idleWait(2);
	    //verify task data in Activity feeds tab
	    softPhoneActivityPage.editTask(driver1, taskSubject);
	    softPhoneActivityPage.verifyTaskAllFields(driver1, taskDetails);
	    
	    //verify tasks created date and new due date
	    actualCreatedTime = softPhoneActivityPage.getEventCreationDate(driver1, taskSubject);
	    assertTrue(actualCreatedTime.equals(createdTime) || actualCreatedTime.equals(createdTime1));
	    assertTrue(softPhoneActivityPage.getTaskDueDate(driver1, taskSubject).equals(HelperFunctions.getDateTimeInTimeZone(dueDateToVerify, userTimeZone, "MM/dd/yy")));
	    
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
		
	    //Check task data in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 32)
	public void call_tools_new_task_update_none_type() {
		System.out.println("Test case --call_tools_new_task_update_none_type()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		String caseOnCallerScreen = CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case");
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
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
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.None.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//getting reminder date and time in qa user 1 time zone in salesforce
		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));
		
		//verify that task is appearing under Open task tabs
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		
	    //verify related record on caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, caseOnCallerScreen);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task Reminder Time
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	   
	    //Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = SoftPhoneNewTask.taskDropdownDates.Tomorrow.toString();
	    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDateFromDropDown, newDueDate);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DeleteReminder, "true");
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//verify that task is appearing under Open task tabs
		softPhoneActivityPage.verifyTaskInvisibleOpenTaskTab(driver1, newTaskSubject);
		
	    //verify related record on caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));
		
		//getting reminder date and time in qa user 1 time zone in salesforce
		taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, new SimpleDateFormat(dateFormat).format(c.getTime()));
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);
			    
		//Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" }, priority = 33)
	public void call_tools_update_call_type_task() {
		System.out.println("Test case --call_tools_new_task_delete_without_navigation()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//Setting call forwarding Off
	    System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

	    //Select related record for the call
	    String relatedRecord = CONFIG.getProperty("softphone_task_related_campaign");
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), relatedRecord);
		HashMap<SoftPhoneNewTask.taskFields, String> relatedRecordData = new HashMap<SoftPhoneNewTask.taskFields, String>();
		relatedRecordData.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedRecord);
	    
		//change the subject of call so that we can identify the task
	    String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    callToolsPanel.changeCallSubject(driver1, taskSubject);
	    
	    //preparing details of the tasks to be updated
	    String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
	    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
	    String taskComment = "Comment " + new SimpleDateFormat("yyyyMM/dd_HHmmss").format(Calendar.getInstance().getTime());
	    relatedRecord = CONFIG.getProperty("softphone_task_related_opportunity");
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedRecord);
	    
	    //Update task details and verify that related record is selected
	    softPhoneActivityPage.idleWait(20);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, relatedRecordData);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
	
		//verify related record is changed
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, relatedRecord);
		
		//Open task in salesforce and verify the updated data
		softPhoneActivityPage.idleWait(5);
		softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression", "Product Sanity" }, priority = 34)
	public void call_tools_new_task_delete_without_navigation() {
		System.out.println("Test case --call_tools_new_task_delete_without_navigation()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String taskComment = "Comment " + new SimpleDateFormat("yyyyMM/dd_HHmmss").format(Calendar.getInstance().getTime());
	    String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
	    softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	    
	    //Deleting task from receiver
	    softPhoneActivityPage.deleteTask(driver1, taskSubject);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 35)
	public void call_tools_new_task_verify_tasks_state_while_recieving_call_forwarding_device() {
		System.out.println("Test case --call_tools_new_task_verify_tasks_state_while_recieving_call_forwarding_device()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//add number as contact
		callScreenPage.addCallerAsContact(driver1, "Automation", "Metacube");
		reloadSoftphone(driver1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//add number as contact
		callScreenPage.addContactWithDefaultValues(driver1, driver3, CONFIG.getProperty("qa_user_3_number"));
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		seleniumBase.idleWait(5);
	    softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
	    
		//Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Edit task before accepting call
	    seleniumBase.idleWait(10);
	    softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
	    seleniumBase.idleWait(2);
	    
	    //Update task
		HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskUpdates.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskUpdates.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		softPhoneNewTask.enterTaskDetails(driver1, taskUpdates);
		
		// receiving call from forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Save task if still in edit more
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);
		seleniumBase.idleWait(2);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task data on salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that call has been removed from Agents softphone
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that call has been removed from receiver
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
		//Setting call forwarding Off
	    System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 36)
	public void call_tools_new_task_search_type_priority_status() {
		System.out.println("Test case --call_tools_new_task_search_type_priority_status()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Setting call forwarding Off
	    System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		softPhoneNewTask.clickTaskIcon(driver1);
	    softPhoneNewTask.selectSearchedTaskType(driver1, "Meet", SoftPhoneNewTask.TaskTypes.Meeting.toString());
	    softPhoneNewTask.selectSearchedTaskPriority(driver1, "Nor", SoftPhoneNewTask.TaskPriorities.Normal.toString());
	    softPhoneNewTask.selectSearchedTaskStatuses(driver1, "In Prog", SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" }, priority = 37)
	public void call_tools_new_task_verify_mandatory_field() {
		System.out.println("Test case --call_tools_new_task_verify_mandatory_field()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String defaultTaskType = SoftPhoneNewTask.TaskTypes.None.toString();
		String defaultTaskPriority = SoftPhoneNewTask.TaskPriorities.Normal.toString();
		String defaultTaskStatus = SoftPhoneNewTask.TaskStatuses.NotStarted.displayName();
		
	    //Opening task from the Call History
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		softPhoneNewTask.clickTaskIcon(driver1);
	    softPhoneNewTask.isMandatoryFieldsErrorVisible(driver1);
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		softPhoneNewTask.clickTaskIcon(driver1);
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
	    
	    //Editing task and Verifying fields
	    softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
	    assertEquals(softPhoneActivityPage.getTaskType(driver1), defaultTaskType);
	    assertEquals(softPhoneActivityPage.getTaskPriority(driver1), defaultTaskPriority);
	    assertEquals(softPhoneActivityPage.getTaskStatus(driver1), defaultTaskStatus);
	    softPhoneActivityPage.clickTaskEditSaveButton(driver1);
	    
	    //Open task in salesforce and verify details
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), taskSubject);
	    assertEquals(sfTaskDetailPage.getTaskStatus(driver1), defaultTaskStatus);
	    assertEquals(sfTaskDetailPage.getTaskType(driver1), " ");
	    assertEquals(sfTaskDetailPage.getTaskPriority(driver1), defaultTaskPriority);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Sanity" }, priority = 38)
	public void call_tools_new_task_meeting_type() {
		System.out.println("Test case --call_tools_new_task_meeting_type()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
		// Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Meeting.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);

		// verify that task is appearing under Open and All task tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		
		//verify related record on caller detail
		callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_campaign"));

		// Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);

		// Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//verify data on softphone
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskUpdates);
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
			    
		//Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority" }, priority = 40)
	public void call_tools_complete_meeting_task() {
		System.out.println("Test case --call_tools_complete_meeting_task()-- started ");
		
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
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Meeting.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//verify that open task has red borded
		softPhoneActivityPage.verifyOpenTasksRedBorder(driver1, taskSubject);

		//Completing task from activity feeds section
		softPhoneActivityPage.markTaskAsComplete(driver1, taskSubject);
		HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
		taskUpdates.put(SoftPhoneNewTask.taskFields.DeleteReminder, "true");
		
		//verify that completed task has no red borded
		softPhoneActivityPage.verifyOpenTasksNoBorder(driver1, taskSubject);
		assertTrue(softPhoneActivityPage.isOpenTasksIcon(driver1, taskSubject));
		
		//verify that task is appearing under Open task tabs
		softPhoneActivityPage.verifyTaskInvisibleOpenTaskTab(driver1, taskSubject);
		
		//edit task and verify that details are updated in the activity
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskUpdates);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task Reminder Time
	    sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//verify task is appearing under all section of activity feeds
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 41)
	public void call_tools_account_type_related_record_update_due_date() {
		System.out.println("Test case --call_tools_account_type_related_record_update_due_date()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Call.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_Account"));
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//verify that task is appearing under Open tabs
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		
	    //Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
	    
	    //Check task Reminder Time
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//verify data on softphone
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskUpdates);
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
			    
		//Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "QuickSanity", "MediumPriority" }, priority = 42)
	public void new_task_create_and_update_other_type() {
		System.out.println("Test case --new_task_create_and_update_other_type()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String relatedCase = CONFIG.getProperty("softphone_task_related_case");

		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Adding a new task for receiver
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
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);

		// getting reminder date and time in qa user 1 time zone in salesforce
		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));

		// verify that task is appearing under Open task tab
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);

		// Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);

		// Check task data on salesforce
		sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Update details of the tasks from activity feeds section
		HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String newDueDate = SoftPhoneNewTask.taskDropdownDates.Tomorrow.toString();
		String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());
		String newReminderDate = new SimpleDateFormat(dateFormat).format(c.getTime());
		String newReminderTime = new SimpleDateFormat(timeFormat).format(c.getTime());
		taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
		taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskUpdates.put(SoftPhoneNewTask.taskFields.DueDateFromDropDown, newDueDate);
		taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString());
		taskUpdates.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_opportunity"));
		taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDate, newReminderDate);
		taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, newReminderTime);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		newReminderTime = softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//updating reminder date
		taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(newReminderDate, userTimeZone, dateFormat));
		taskUpdates.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(newReminderTime, userTimeZone, timeFormat));

		// verify that task is appearing under Open task tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, newTaskSubject);
		
		//verify related record on caller detail
		callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_opportunity"));

		// Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, newTaskSubject);

		// Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority" }, priority = 43)
	public void call_tools_new_task_subject_disable() {
		System.out.println("Test case --call_tools_new_task_subject_disable()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// navigating to support page
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");

		// disabling Task due date option
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToTaskFieldsSection(driver1);
		accountSalesforceTab.disableTaskSubjectRequiredSetting(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task without subject
	    String taskComment = "Comment " + new SimpleDateFormat("yyyyMM/dd_HHmmss").format(Calendar.getInstance().getTime());
	    String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
	    String dueDateToVerify	= new SimpleDateFormat("M/dd/yy").format(Calendar.getInstance().getTime());
	    softPhoneNewTask.addNewTask(driver1, "", taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	    seleniumBase.idleWait(10);
	    
	    //Verifying task due date
	    int index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment);
	    assertTrue(softPhoneActivityPage.getNoSubjectActivityDueDateByIndex(driver1, index).contains(dueDateToVerify));
	    
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.High.toString());
		softPhoneActivityPage.editNoSubjectTask(driver1, index);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//MarktTaskAsComplete
		softPhoneActivityPage.markTaskAsCompleteNoSubject(driver1, index);
		taskUpdates.put(SoftPhoneNewTask.taskFields.Status, TaskStatuses.Completed.toString());
		
		//verify that task is appearing under Open and Email task tabs
		softPhoneActivityPage.navigateToOpenTasksTab(driver1);		
		assertEquals(softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment), -1);
	    
		// Open task in salesforce from All Activity feeds section
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment);
		assertTrue(softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment)>= 0);
		
	    //Deleting task from receiver
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment);
	    softPhoneActivityPage.editNoSubjectTask(driver1, index);
	    softPhoneActivityPage.clickTaskDeleletButton(driver1);
	    softPhoneActivityPage.clickTaskWarningDeleteButton(driver1);
	    
	    //verify that task has been deleted
	    assertEquals(softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, taskComment), -1);
	    
		// navigating to support page
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountSalesforceTab.enableTaskSubjectRequiredSetting(driver1);	
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify agent able to add a task with script in subject and later open it in edit mode from activity section
	@Test(groups = { "Regression" }, priority = 44)
	public void call_tools_new_task_with_script() {
		System.out.println("Test case --call_tools_new_task_with_script()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver 
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		
		String taskSubject = "<script>alert('Test!!!!" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "')</script>";
		String taskComment = "<script>alert('Test!!!!')</script>";
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//verify that task is appearing under All Tasks tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
	   
	    //Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    String newTaskSubject = "<script>alert('Test Updated!!!!')</script>";
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
		
	    //verify that updated task name is appearing under All Tasks tab
	    softPhoneActivityPage.isTaskInvisibleInActivityList(driver1, taskSubject);
	  	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, newTaskSubject);
	    
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
	
	//Verify agent able to create Meeting type task for an existing caller
	@Test(groups = { "Regression"}, priority = 45)
	public void call_tools_task_meeting_type() {
		System.out.println("Test case --call_tools_task_meeting_type()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//verify mandatory fields
		softPhoneNewTask.clickTaskIcon(driver1);
	    softPhoneNewTask.isMandatoryFieldsErrorVisible(driver1);
	    
		// Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Meeting.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);

		// verify that task is appearing under Open and All task tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		
		//verify related record on caller detail
		callScreenPage.verifyRelatedRecordPresent(driver1, CONFIG.getProperty("softphone_task_related_campaign"));

		// Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);

		// Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    String newDueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, newDueDate);
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//verify data on softphone
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskUpdates);
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
			    
		//Check task Reminder Time
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify ReadyOnly fields should not be displayed on New task when user clicks on "Complete & New Task"  already created New task.
	@Test(groups = { "Regression" }, priority = 46)
	public void call_tools_complete_and_new_task() {
		System.out.println("Test case --call_tools_complete_and_new_task()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver 
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		
		String taskSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Task Comment" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
		
		//verify that task is appearing under All Tasks tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);
			    
		//Check task data in salesforce
		sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	   
		//verify that task is appearing under Open Tasks tab
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		
	    //Update details of the tasks from activity feeds section
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		softPhoneActivityPage.editTask(driver1, taskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
	    
		//verify task status is in progress
	    softPhoneActivityPage.editTask(driver1, taskSubject);
		assertEquals(softPhoneActivityPage.getTaskStatus(driver1), SoftPhoneNewTask.TaskStatuses.InProgress.displayName()); 
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
			    
		//Verify option Mark Complete and New
	    softPhoneActivityPage.markTaskCompleteAndOpenNewTask(driver1, taskSubject);
	    assertEquals(softPhoneActivityPage.getTaskSubject(driver1), taskSubject);
	    
	    //New tasks details
	    HashMap<SoftPhoneNewTask.taskFields, String> taskDetailsNew = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    String relatedCase = CONFIG.getProperty("softphone_task_related_case");
		dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String newTaskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.Comments, newTaskComment);
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.None.toString());
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetailsNew.put(SoftPhoneNewTask.taskFields.RelatedRecord, relatedCase);
	    
		softPhoneActivityPage.updateTaskAllFields(driver1, taskDetailsNew);

		//verify that task is appearing under Open Tasks tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, newTaskSubject);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify agent able to create new task for existing lead/contact using new task icon
	@Test(groups = { "Regression" }, priority = 47)
	public void call_tools_task_already_existing_contact() {
		System.out.println("Test case --call_tools_old_task_already_existing_contact()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
		// Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Meeting.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);

		// verify that task is appearing under Open and All task tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		assertTrue(softPhoneActivityPage.isOpenTasksIcon(driver1, taskSubject));

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify agent able to show/hide New Task/Event
	//Check availability of New Task / Event Icon
	@Test(groups = { "Regression" }, priority = 48)
	public void call_tools_new_task_icon_disabled_enabled() {
		System.out.println("Test case --call_tools_new_task_icon_disabled_enabled()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Login to web application and disable setting to show New task and Event icons
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableNewTaskSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	   
		//verify that task and event icon are not visible
		assertFalse(softPhoneNewTask.isTaskIconVisible(driver1));
		assertFalse(softPhoneNewEvent.isEventIconVisible(driver1));
		
		//Login to web application and enable setting to show New task and Event icons
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableNewTaskSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	   
		//verify that task and event icon are visible
		assertTrue(softPhoneNewTask.isTaskIconVisible(driver1));
		assertTrue(softPhoneNewEvent.isEventIconVisible(driver1));
		
		//open recent group call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	   
		//verify that task and event icon are visible
		assertTrue(softPhoneNewTask.isTaskIconVisible(driver1));
		assertTrue(softPhoneNewEvent.isEventIconVisible(driver1));
		
		//open recent Message from message page
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.clickOpenMessageIconByName(driver1, CONFIG.getProperty("prod_user_1_name"));
	   
		//verify that task and event icon are visible
		assertTrue(softPhoneNewTask.isTaskIconVisible(driver1));
		assertTrue(softPhoneNewEvent.isEventIconVisible(driver1));
		
		//open contact detail from contact search
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.openSfContactDetails(driver1, CONFIG.getProperty("prod_user_1_name"));
	   
		//verify that task and event icon are visible
		assertTrue(softPhoneNewTask.isTaskIconVisible(driver1));
		assertTrue(softPhoneNewEvent.isEventIconVisible(driver1));
		
		//open hot lead details
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.openWebLeadsContactDetail(driver1, softPhoneWebLeadsPage.getWebLeadsNameList(driver1).get(0));
	   
		//verify that task and event icon are visible
		assertTrue(softPhoneNewTask.isTaskIconVisible(driver1));
		assertTrue(softPhoneNewEvent.isEventIconVisible(driver1));

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups={"Regression", "MediumPriority", "Sanity", "Product Sanity"}, dependsOnMethods = {"resetSetupDefault"})
	public void  _afterMethod(ITestResult result){
		if(result.getName().equals("call_tools_new_task_icon_disabled_enabled()") && (result.getStatus() == 2 || result.getStatus() == 3)){
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			//Login to web application and disable setting to show New task and Event icons
			loginSupport(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableNewTaskSetting(driver1);
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			
			//switch to softphone and reload it
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
			
			driverUsed.put("driver1", false);
		}
	}
}