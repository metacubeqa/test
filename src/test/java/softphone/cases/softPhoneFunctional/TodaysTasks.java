/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

/**
 * @author admin
 *
 */
public class TodaysTasks extends SoftphoneBase{
	
	HashMap<SoftPhoneNewTask.taskFields, String> emailTaskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
	HashMap<SoftPhoneNewTask.taskFields, String> callTaskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
	
	String contactFirstName	= "";
	String contactName		= "";
	String leadFirstName	= "";
	String leadName			= "";
	String accountName		= "";
	String emailTaskSubject = "Email Type Task 20201123_120928";
	String callTaskSubject 	= "Call Type Task 20201123_120928"; 
	String smsTaskSubject 	= "Message Type Task 20201123_122146";
	String genericTaskSubject = "Generic Type Task 20201123_122146";
	
	@BeforeClass(groups={"QuickSanity", "Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void BeforeClass() {
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		contactFirstName	= CONFIG.getProperty("prod_user_1_name").trim();
		contactName			= contactFirstName + " Automation";
		leadFirstName		= CONFIG.getProperty("qa_user_2_name").trim();
		leadName			= leadFirstName + " Automation";
		accountName			= CONFIG.getProperty("contact_account_name").trim();
		
		aa_AddCallersAsContactsAndLeads();
		
		//add Lead
		String contactNumber = CONFIG.getProperty("qa_user_2_number");
		softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, leadFirstName);
		
		//add contact
	    contactNumber = CONFIG.getProperty("prod_user_1_number");		  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, contactFirstName);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	}
	
	@BeforeMethod(groups={"QuickSanity", "Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void BeforeMethod() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		if(!(seleniumBase.getTabCount(driver1) > 1)) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
		}
	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	}
	
	//Verify Email Type tasks
	//Verify Message Type tasks
	//Verify call type tasks
	//Verify Generic type tasks
	@Test(groups = { "Regression" })
	public void today_tasks_create_new_tasks() {
		
		System.out.println("Test case --today_tasks_create_new_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task for receiver 
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		
		//Defining Tasks subject name
		emailTaskSubject 	= "Email Type Task " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		callTaskSubject 	= "Call Type Task " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		smsTaskSubject 		= "Message Type Task " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		genericTaskSubject	= "Generic Type Task " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		//Puttng email tasks detail in a hasmap so that it can be verified in other test scripts
		emailTaskDetails.put(SoftPhoneNewTask.taskFields.Subject, emailTaskSubject);
		emailTaskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		emailTaskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.toString());
		emailTaskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		
		//Add a task with email subject and other details
		softPhoneNewTask.addNewTask(driver1, emailTaskSubject, "", dueDate, SoftPhoneNewTask.TaskTypes.Email.toString());
		
		callTaskDetails.put(SoftPhoneNewTask.taskFields.Subject, callTaskSubject);
		callTaskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		callTaskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.toString());
		callTaskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Call.toString());
		
		//Add a an call type and sms task with other details
		softPhoneNewTask.addNewTask(driver1, callTaskSubject, "", dueDate, SoftPhoneNewTask.TaskTypes.Call.toString());
		softPhoneNewTask.addNewTask(driver1, smsTaskSubject, "", dueDate, SoftPhoneNewTask.TaskTypes.SMS.toString());
		
		//open call to the lead and create generic task for it
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.addNewTask(driver1, genericTaskSubject, "", dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);;
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to the Today's task page and verify the icons for all the tasks added above
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyTaskIcon(driver1, emailTaskSubject, SoftPhoneNewTask.TaskTypes.Email);
		todaysTasksPage.verifyTaskIcon(driver1, callTaskSubject, SoftPhoneNewTask.TaskTypes.Call);
		todaysTasksPage.verifyTaskIcon(driver1, smsTaskSubject, SoftPhoneNewTask.TaskTypes.SMS);
		todaysTasksPage.verifyTaskIcon(driver1, genericTaskSubject, SoftPhoneNewTask.TaskTypes.Other);
				
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}

	//Verify clicking task open the corresponding SFDC task page in a web browser
	//Verify Not Completed task on Today view
	@Test(groups = { "Regression"}, dependsOnMethods = "today_tasks_create_new_tasks")
	public void today_tasks_incomplete_task() {
		System.out.println("Test case --today_tasks_complete_incomplete_task()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Navigate to the Today's task page and mark a task not completed if it is already marked completed
		//verify that the task is not completed
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.markTaskNotComplete(driver1, emailTaskSubject);
		todaysTasksPage.verifyTaskIsNotCompleted(driver1, emailTaskSubject);
		
		//open the email task (which is not completed) in the salesforce and verify it's detail
		todaysTasksPage.openTaskInSFDC(driver1, emailTaskSubject);
		sfTaskDetailPage.verifyTaskData(driver1, emailTaskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Update subject, type and change its status to completed
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.displayName());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		softPhoneActivityPage.editTask(driver1, emailTaskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
				
		//navigate to today's tasks page and verify the icon changed to generic task type and task is marked completed
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyTaskIcon(driver1, newTaskSubject, SoftPhoneNewTask.TaskTypes.Other);
		todaysTasksPage.verifyTaskIsCompleted(driver1, newTaskSubject);
		
		//Open the updated task is salesforce and verify it's details
		todaysTasksPage.openTaskInSFDC(driver1, newTaskSubject);
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify Completed task on Today view
	//Verify task created /updated from activity list reflected in task view
	@Test(groups = { "Regression", "Product Sanity"}, dependsOnMethods = "today_tasks_create_new_tasks")
	public void today_tasks_completed_task() {
		System.out.println("Test case --today_tasks_completed_task()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Navigate to the Today's task page and mark a task completed if it is not marked completed
		//verify that the task is completed
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.markTaskComplete(driver1, callTaskSubject);
		todaysTasksPage.verifyTaskIsCompleted(driver1, callTaskSubject);
		
		//Update the calls type task's details where status of the task is completed
		callTaskDetails.remove(SoftPhoneNewTask.taskFields.Status);
		callTaskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.Completed.toString());
		
		//open the calls type task and verify it's details in salesforce
		todaysTasksPage.openTaskInSFDC(driver1, callTaskSubject);
		sfTaskDetailPage.verifyTaskData(driver1, callTaskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Update subject, type and change task's status to not completed
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.NotStarted.displayName());
	    taskUpdates.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		softPhoneActivityPage.editTask(driver1, callTaskSubject);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//Switch to web app and clear the salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);

		//Navigate to the Today's tasks page and verify that the updated task icon is of generic type and it is not completed
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyTaskIcon(driver1, newTaskSubject, SoftPhoneNewTask.TaskTypes.Other);
		todaysTasksPage.verifyTaskIsNotCompleted(driver1, newTaskSubject);
		
		//Open tasks details in salesforce and verify the details
		todaysTasksPage.openTaskInSFDC(driver1, newTaskSubject);
		sfTaskDetailPage.verifyTaskData(driver1, taskUpdates);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify contact detail with task view
	//Verify account detail on task view
	@Test(groups = { "Regression"}, dependsOnMethods = "today_tasks_create_new_tasks")
	public void today_tasks_for_contact() {
		System.out.println("Test case --today_tasks_for_contact()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Navigate to today's task page
		todaysTasksPage.navigateToTasksPage(driver1);

		//verify that the sms type task is associated with a contact and it's details
		//click on the contact link so it opens in salesforce and verify that contact page is opened
		todaysTasksPage.verifyContactDetail(driver1, smsTaskSubject, contactName, accountName);
		todaysTasksPage.openTaskContactInSFDC(driver1, smsTaskSubject);
		contactDetailPage.verifyContactPageOpen(driver1);
		assertTrue(contactDetailPage.getCallerName(driver1).contains(contactName));
		contactDetailPage.closeTab(driver1);
		contactDetailPage.switchToTab(driver1, 1);
		
		//Click on the Account link so it opens in salesforce and verify that Account page is opened
		todaysTasksPage.openTaskAccountInSFDC(driver1, smsTaskSubject);
		salesforceAccountPage.verifyAccountPageOpen(driver1);
		salesforceAccountPage.verifyAccountsNameHeading(driver1, accountName);
		contactDetailPage.closeTab(driver1);
		contactDetailPage.switchToTab(driver1, 1);
		
		//Open the contact details by clicking on arrow icon and verify that call detail page is opened
		todaysTasksPage.openTaskContactInfo(driver1, smsTaskSubject);
		assertEquals(callScreenPage.getCallerName(driver1), contactName);
		assertEquals(callScreenPage.getCallerCompany(driver1), accountName);
		
		//Mark contact as favorite and verify is on contact search favorites list
		callScreenPage.setAsFavoriteContact(driver1);
		softPhoneContactsPage.isContactFavorite(driver1, contactName);
		
		//Navigate to Today's task page and verify that the associated contact is marked favorite
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyContactLeadIsFav(driver1, smsTaskSubject, contactName);
		
		//open the contacts detaila dn remove contact as fav
		todaysTasksPage.openTaskContactInfo(driver1, smsTaskSubject);
		callScreenPage.removeFromFavoriteContact(driver1);
		
		//verify contact is not there in contacts fav list and on tasks page
		softPhoneContactsPage.isContactNotFavorite(driver1, contactName);
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyContactLeadIsNotFav(driver1, smsTaskSubject, contactName);
			
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}

	//Verify Lead detail on task view
	@Test(groups = { "Regression"}, dependsOnMethods = "today_tasks_create_new_tasks")
	public void today_tasks_for_lead() {
		System.out.println("Test case --today_tasks_for_lead()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open today's tasks page
		todaysTasksPage.navigateToTasksPage(driver1);

		//verify the leads detail for generic type of task create in first script
		//click on the Lead's link so it opens in salesforce and verify that Lead page is opened
		todaysTasksPage.verifyLeadDetail(driver1, genericTaskSubject, leadName, accountName);
		todaysTasksPage.openTaskContactInSFDC(driver1, genericTaskSubject);
		contactDetailPage.verifyLeadPageOpen(driver1);
		assertTrue(contactDetailPage.getCallerName(driver1).contains(leadName));
		contactDetailPage.closeTab(driver1);
		contactDetailPage.switchToTab(driver1, 1);
		
		//Open the Lead detail for the associated Task's lead and verify that lead page is open on salesforce
		todaysTasksPage.openTaskContactInfo(driver1, genericTaskSubject);
		assertEquals(callScreenPage.getCallerName(driver1), leadName);
		assertTrue(callScreenPage.isLeadImageVisible(driver1));
		
		//Set lead as favorite and verify it's there on the favorite contact's list
		callScreenPage.setAsFavoriteContact(driver1);
		softPhoneContactsPage.isContactFavorite(driver1, leadName);
		
		//navigate to today's task page and verify that lead is marked favorite
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyContactLeadIsFav(driver1, genericTaskSubject, leadName);
		
		//open the lead detail page by clicking arrow icon and remove the contact as favorite
		todaysTasksPage.openTaskContactInfo(driver1, genericTaskSubject);
		callScreenPage.removeFromFavoriteContact(driver1);

		//Verify that contact is not there in the favorite contact list and it is not marked favorite in today's task list
		softPhoneContactsPage.isContactNotFavorite(driver1, leadName);
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyContactLeadIsNotFav(driver1, genericTaskSubject, leadName);
			
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify no of overdue tasks (after completing task count should update)
	@Test(groups = { "Regression" })
	public void today_tasks_overdue_count() {
		
		System.out.println("Test case --today_tasks_create_new_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding two tasks. For one due date is of today's for other it is yesterday 
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		String dueDate = new SimpleDateFormat(dateFormat).format(c.getTime());
		
		//Set the subject of the overdue tasks
		String overdueTask1	= "overdue Type Task 1" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String overdueTask2	= "overdue Type Task 2" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		//add the overdue tasks
		softPhoneNewTask.addNewTask(driver1, overdueTask1, "", dueDate, SoftPhoneNewTask.TaskTypes.Email.toString());
		softPhoneNewTask.addNewTask(driver1, overdueTask2, "", new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()), SoftPhoneNewTask.TaskTypes.Email.toString());
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to tasks page and get the overdue tasks count
		todaysTasksPage.navigateToTasksPage(driver1);
		int taskOverdueCount = todaysTasksPage.getTaskOverdueCount(driver1);
		
		//open recent call history and mark the task complete whose due date has passed
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.markTaskAsComplete(driver1, overdueTask1);
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to today's task page and verify that the overdue tasks count has decreased by 1
		todaysTasksPage.navigateToTasksPage(driver1);
		assertEquals(todaysTasksPage.getTaskOverdueCount(driver1), taskOverdueCount - 1);
		
		//Open the reent contact details and change the due date of a task to yesterday
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
	    taskUpdates.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		softPhoneActivityPage.editTask(driver1, overdueTask2);
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Navigate to Today's task page and verify that over due tasks count has increased by 1
		todaysTasksPage.navigateToTasksPage(driver1);
		assertEquals(todaysTasksPage.getTaskOverdueCount(driver1), taskOverdueCount);
	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}

	//Verify related record detail in task view
	//Verify task after delete should be removed from task view
	@Test(groups = { "Regression" })
	public void today_tasks_related_record_task() {
		
		System.out.println("Test case --today_tasks_create_new_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Adding a new task with related record 
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String realtedRecord = CONFIG.getProperty("softphone_task_related_campaign");
		
		String taskSubject	= "reltatedRecordTask" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, realtedRecord);

		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to today's task page and verify the task's related record detail
		todaysTasksPage.navigateToTasksPage(driver1);
		todaysTasksPage.verifyRelatedRecordTaskDetail(driver1, taskSubject, contactName, realtedRecord);
		
		//click on related record link so that it opens in salesforce and verify that campaign page is opened
		todaysTasksPage.openTaskAccountInSFDC(driver1, taskSubject);
		sfCampaign.verifyCampaignPageOpened(driver1, realtedRecord);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Delete the above created task
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.deleteTask(driver1, taskSubject);
	    
	    //switch to web app and clear salesforce cache
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to today's task page and verify that the task is not appearing in the list
		todaysTasksPage.navigateToTasksPage(driver1);
		assertEquals(todaysTasksPage.getTaskRowIndex(driver1, taskSubject), -1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify Empty screen message when user has no tasks due for today
	@Test(groups = {"Regression"})
	public void today_tasks_no_tasks() {
		System.out.println("Test case --today_tasks_no_tasks()-- started ");

		// updating the driver used
		initializeDriverSoftphone("chatterOnlyDriver");
		driverUsed.put("chatterOnlyDriver", true);

		// navigate To Tasks Tabs of a contact for which there is no task added for today
		todaysTasksPage.navigateToTasksPage(chatterOnlyDriver);
		
		//Verify no tasks details page is there
		todaysTasksPage.verifyNoTasksPage(chatterOnlyDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("chatterOnlyDriver", false);
		System.out.println("Test case is pass");
		
		chatterOnlyDriver.quit();
	}
	
	//Verify tasks displayed in set of 20 on Todays Task view that are due for current date
	//Verify Load More icon not displayed if not more tasks are present to be loaded
	@Test(groups = { "Regression", "Product Sanity" }, priority = -1)
	public void today_tasks_add_more_than_20_tasks() {
		
		System.out.println("Test case --today_tasks_add_more_than_20_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		List<String> taskList = new ArrayList<String>();
		
		//navigate to today's task page and get total number of tasks
		//verify that load more tasks button is not visible
		todaysTasksPage.navigateToTasksPage(driver1);
		int numberOfTasks = todaysTasksPage.getTotalTasks(driver1);
		todaysTasksPage.verifyLoadMorebtnInvisible(driver1);
		
		//open recent call history
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		
		//Now add the tasks so that total number of tasks for today's date gets to 25
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject	= "reltatedRecordTask" + new SimpleDateFormat("MMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		for (int i = numberOfTasks; i < 25; i++) {
			String taskName = taskSubject + HelperFunctions.GetRandomString(4);
			softPhoneNewTask.addNewTask(driver1, taskName, "", dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
			taskList.add(taskName);
		}
		
		//switch to web app and clear salesforce cache
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.clickClearBtnCache(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//navigate to today's task page and verify that only 20 tasks are visible
		todaysTasksPage.navigateToTasksPage(driver1);
		assertEquals(todaysTasksPage.getTotalTasks(driver1), 20);
		
		//Click on load more tasks button and verify that all 25 tasks are visible
		todaysTasksPage.clickLoadMorebtn(driver1);
		seleniumBase.idleWait(5);
		assertEquals(todaysTasksPage.getTotalTasks(driver1), 25);
		
		//Delete and add the lead again so above created tasks are removed from today's tasks page
		String contactNumber = CONFIG.getProperty("qa_user_2_number");
		softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, leadFirstName);
		
		//make a call to contact so that this entry is the top entry in the list. It is required for other scripts
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
}