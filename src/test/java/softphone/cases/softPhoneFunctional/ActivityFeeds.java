/**
 * @author Abhishek 
 *
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

public class ActivityFeeds extends SoftphoneBase{

	@BeforeClass(groups = {"Regression", "MediumPriority"})
	public void beforeClass() {

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		//add contact
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");		  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}

	@Test(groups = { "Regression" })
	public void aactivity_feeds_verify_all_tasks() {
		System.out.println("Test case --activity_feeds_verify_all_tasks()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Adding a new task for receiver
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Email.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);
		
		//send a text message to caller 2
		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
		
		//verify that task is appearing under Open and Email task tabs
	    softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
	    seleniumBase.idleWait(2);
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		seleniumBase.idleWait(2);
		softPhoneActivityPage.verifyTaskInEmailTaskTab(driver1, taskSubject);
		seleniumBase.idleWait(2);
		
	    //Verify message task in All Activities section
		softPhoneActivityPage.verifyMessageTask(driver1, CONFIG.getProperty("prod_user_1_number"), message);
		
		//Verify Outbound Call task
		softPhoneActivityPage.verifyOutboundCallTask(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		//Verify inbound Call task
		softPhoneActivityPage.verifyInboundCallTask(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		//update call task
		HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String newTaskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskUpdates.put(SoftPhoneNewTask.taskFields.Subject, newTaskSubject);
		softPhoneActivityPage.editTask(driver1, "Outbound Call: " + CONFIG.getProperty("prod_user_1_number").trim());
		softPhoneActivityPage.updateTaskAllFields(driver1, taskUpdates);
		
		//Verify task is updated
		softPhoneActivityPage.editTask(driver1, newTaskSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskUpdates);
		
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
	
	@Test(groups = { "Regression" })
	public void activity_feeds_verify_no_record_messages() {
		System.out.println("Test case --activity_feeds_verify_no_record_messages()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2,CONFIG.getProperty("qa_user_1_number"));

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver2,CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver1);
		}

		// add caller as Contact
	    String contactName = CONFIG.getProperty("qa_user_2_name");
	    String accountName	= CONFIG.getProperty("contact_account_name");
		callScreenPage.addCallerAsContact(driver1, contactName, accountName);

		//
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no Salesforce activities associated with this record. Future RingDNA calls, emails, tasks, and other activities will appear in this section.");

		softPhoneActivityPage.navigateToCallsTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no calls associated with this record. Future calls will appear in this section.");

		softPhoneActivityPage.navigateToOpenTasksTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no Open Tasks associated with this record. Open Tasks will appear in this section.");

		softPhoneActivityPage.navigateToEmailTasksTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no Emails associated with this record. Future Emails will appear in this section.");

		softPhoneActivityPage.navigateToCallNotesTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no calls with Notes associated with this record. Future calls with Notes will appear in this section.");

		// hangup Call
		softPhoneCalling.hangupActiveCall(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void activity_feeds_play_call_access() {
		System.out.println("Test case --activity_feeds_play_call_access()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		seleniumBase.idleWait(5);

		// hangup Call
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
		String notesSubject1 = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.changeCallSubject(driver1, notesSubject1);
		
		// Calling from other caller
		softPhoneCalling.softphoneAgentCall(driver3,CONFIG.getProperty("prod_user_1_number"));

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		seleniumBase.idleWait(5);

		// hangup Call
		String notesSubject2 = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.changeCallSubject(driver3, notesSubject2);
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver2);
		seleniumBase.idleWait(30);
		
		//verifying play recording buttons
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, notesSubject1));
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, notesSubject2));
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver3, notesSubject1));
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver3, notesSubject2));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void activity_feeds_play_group_call_access() {
		System.out.println("Test case --activity_feeds_play_group_call_access()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		//subscribe a queue for caller 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //subscribe a queue for caller 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, queueName);
	    
	    // making call to queue
	    System.out.println("making call to queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //call is visible to other caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);

		// receiving call from receiver
	    softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		
		//call is not visible to other caller now
		softPhoneCallQueues.isPickCallBtnInvisible(driver3);

		// hangup Call
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
		String notesSubject1 = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.changeCallSubject(driver1, notesSubject1);
		
		// Calling from other caller
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver2);
		seleniumBase.idleWait(30);
		
		//verifying play recording buttons
		assertTrue(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, notesSubject1));
		
		assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver3, notesSubject1));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void play_recording_button_on_activity_feeds() {
		System.out.println("Test case --play_recording_button_on_activity_feeds-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// Take an incoming call to number and hangup the call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//play recording from all activity tab
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject = "Call Notes Incoming " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver4, callSubject);
		softPhoneActivityPage.navigateToOpenTasksTab(driver4);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject);
		
		//play recording from all calls tab
		softPhoneActivityPage.navigateToCallsTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		
		// Make an outcoming call to number and hangup the call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver2);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//play recording from all activity tab
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
	    String callSubject1 = "Call Notes Outgoing " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver4, callSubject1);
		softPhoneActivityPage.navigateToOpenTasksTab(driver4);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject1);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject1);
		
		//play recording from all calls tab
		softPhoneActivityPage.navigateToCallsTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject1);
		seleniumBase.idleWait(1);
		softPhoneActivityPage.getRecordingPlayerCurrentTime(driver4, callSubject1);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	 @Test(groups={"MediumPriority"})
	  public void activity_feeds_recording_merged()
	  {
	    System.out.println("Test case --activity_feeds_recording_merged-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   	//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	       
	   	//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	       
	   	//Making second outbound call from softphone
	    System.out.println("agent making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	       
	   	//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
		//checking that first call go on hold 
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//resuming first call
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))));
	    
		//checking that  second call should go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickOnHoldButton(driver1);
	    
		//verifying that conference button is visible
	    softPhoneCalling.clickMergeButton(driver1);
	    
		//verifying that all the conference buttons are visible correctly
	    softPhoneCalling.isConferenceWindowButtonsEnabled(driver1);
	    
		//Verifying that two end buttons are visible 
	    assertEquals(softPhoneCalling.getParticipantConferenceEndButtons(driver1).size(), 2);
	    
		//verifying that one select button is visible
	    assertEquals(softPhoneCalling.availableSelectButton(driver1).size(), 1);
	    
		//closing the conference window
	    System.out.println("closing the conference window");
	    softPhoneCalling.closeConferenceWindow(driver1);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//checking that  conference button is disabled
	    seleniumBase.idleWait(5);
	    softPhoneCalling.isConferenceButtonInvisible(driver1);
	    
		//hanging up with the second caller
	    System.out.println("hanging up with the second caller");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//play recording from all activity tab
	    String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject);
		softPhoneActivityPage.playTaskRecording(driver1, callSubject);
		assertEquals(softPhoneActivityPage.getNumberOfRecordings(driver1, callSubject), 3);
		
		//play recording from all activity tab
		softphoneCallHistoryPage.idleWait(60);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.playTaskRecording(driver1, callSubject);
		assertEquals(softPhoneActivityPage.getNumberOfRecordings(driver1, callSubject), 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void activity_feeds_call_recording_access()
	  {
	    System.out.println("Test case --activity_feeds_call_recording_access-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	   	//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	       
	   	//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	       
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	   	//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
	       
	   	//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	       
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	        
		//Change task subjects for both the users
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject1 = "Call Notes first Caller " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    String callSubject2 = "Call Notes Second Caller " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver3, callSubject2);
		
	    //calling again from user 1 due to a bug.
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
		//Verify that each user is not able to see other users call recording
	    String caller1 = CONFIG.getProperty("qa_user_1_name");
	    String caller2 = CONFIG.getProperty("qa_user_3_name");
	    softPhoneCalling.idleWait(20);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    assertEquals(softPhoneActivityPage.getTaskOwnerName(driver3, callSubject1), caller1);
	    assertEquals(softPhoneActivityPage.getTaskOwnerName(driver3, callSubject2), caller2);
	    assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver3, callSubject2));
	    assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver3, callSubject1));
	    
	    //Verify that each user is not able to see other users call recording
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskOwnerName(driver1, callSubject1), caller1);
	    assertEquals(softPhoneActivityPage.getTaskOwnerName(driver1, callSubject2), caller2);
	    assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject1));
	    assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject2));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void activity_feeds_group_call_recording_access()
	  {
	    System.out.println("Test case --activity_feeds_group_call_recording_access-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    //Subscribe to a queue for user 1
	    softPhoneCallQueues.unSubscribeQueue(driver1, CONFIG.getProperty("qa_group_5_name"));
	   	
	    //Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_5_number"));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Change task subjects for both the users
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    String timeStamp = softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0);
	    callToolsPanel.idleWait(25);
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	    String callSubject1 = "Call Notes first Caller " + helperFunctions.GetCurrentDateTime();
	    String callerName = callScreenPage.getCallerName(driver1);
	    callToolsPanel.changeCallSubject(driver1, callSubject1);
	    assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver1, callSubject1));
	    
		//Verify that each user is not able to see other users call recording
	    softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver3);
	    assertFalse(timeStamp.equals(softphoneCallHistoryPage.getHistoryCallerTime(driver3, 0)));
	    
	    //search from contact
	    softPhoneContactsPage.openSfContactDetails(driver3, callerName);
	    assertFalse(softPhoneActivityPage.isPlayRecordingBtnVisible(driver3, callSubject1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void call_acitivity_subject_change()
	  {
	    System.out.println("Test case --call_acitivity_subject_change-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	   	//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	       
	   	//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject1 = "Call Notes first " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject1);
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	       
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify changed subject during call is present
	    softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject1);
	    
	   	//Making an outbound call from softphone
	    String callSubject2 = "Call Notes Second " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject2);
	    callToolsPanel.idleWait(20);
	    softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject2);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void call_acitivity_task_content_change()
	  {
	    System.out.println("Test case --call_acitivity_task_content_change-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	   	//Take an incoming call from softphone
	    System.out.println("Taking incoming call to caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	       
	   	//receiving call from app softphone and update the call notes
	    System.out.println("caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    String callSubject1 = "Call Subject first " + helperFunctions.GetCurrentDateTime();
	    String callNotes1 = "Call Notes first " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.enterCallNotes(driver1, callSubject1, callNotes1);
	    
		//hanging up with caller
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify changed subject during call is present
	    softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject1);
	    
	   	//Change the subject of the call
	    String callSubject2 = "Call Notes Second " + helperFunctions.GetCurrentDateTime();
		softPhoneActivityPage.editTask(driver1, callSubject1);
		softPhoneNewTask.enterTaskSubject(driver1, callSubject2);
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);
	    
	    //verify changed subject during call is present
	    softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject2);
	    
	    //verify that old call notes are there and update the call notes
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes1);
	    callToolsPanel.clickCallNotesCancelBtn(driver1);
	    
	    //verify that old call notes are there and update call notes in task
	    String callNotes2 = "Call Notes Second " + helperFunctions.GetCurrentDateTime();
	    softPhoneActivityPage.editTask(driver1, callSubject2);
	    assertEquals(softPhoneActivityPage.getTaskComments(driver1), callNotes1);
	    softPhoneNewTask.enterTaskComments(driver1, callNotes2);
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		
	    //select disposition
	    callToolsPanel.selectDisposition(driver1, 0);
	    
	    //verify that updated call notes are present after call end
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes2);
	    callToolsPanel.clickCallNotesCancelBtn(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	    
	 @Test(groups = { "MediumPriority" })
		public void verify_relate_records_during_call() {
			System.out.println("Test case --verify_relate_records_during_call()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver2");
			driverUsed.put("driver2", true);
			
			String relatedRecord = CONFIG.getProperty("softphone_task_related_opportunity");
			
			//Making outbound Call
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver2);
			
			//Adding a related record to the call
			callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedRecord );
		    
		    //hanging up with the call.
		    softPhoneCalling.hangupActiveCall(driver2);
		    softPhoneCalling.isCallBackButtonVisible(driver1);
		    seleniumBase.idleWait(2);
		    
			//Opening the caller detail page
		    String callSubject = "Call Subject " + helperFunctions.GetCurrentDateTime();
		    callToolsPanel.changeCallSubject(driver1, callSubject);
		    
		    //verify related record in call task
		    softPhoneActivityPage.editTask(driver1, callSubject);
		    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1), relatedRecord);
			softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
	 
	 @Test(groups = {"MediumPriority"})
		public void verify_relate_records_after_call() {
			System.out.println("Test case --verify_relate_records_after_call()-- started ");
			
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver2");
			driverUsed.put("driver2", true);
			
			String relatedRecord = CONFIG.getProperty("softphone_task_related_opportunity");
			
			//Making outbound Call
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver2);
		    
		    //hanging up with the call.
		    softPhoneCalling.hangupActiveCall(driver2);
		    softPhoneCalling.isCallBackButtonVisible(driver1);
		    seleniumBase.idleWait(2);
		    	
			//Adding a related record to the call
			callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedRecord );
		    
			//Opening the caller detail page
		    String callSubject = "Call Subject " + helperFunctions.GetCurrentDateTime();
		    callToolsPanel.changeCallSubject(driver1, callSubject);
		    
		    //verify related record in call task
		    softPhoneActivityPage.editTask(driver1, callSubject);
		    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1), relatedRecord);
			softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		    
			//Setting driver used to false as this test case is pass
		    driverUsed.put("driver1", false);
		    driverUsed.put("driver2", false);
		    
		    System.out.println("Test case is pass");
		}
}