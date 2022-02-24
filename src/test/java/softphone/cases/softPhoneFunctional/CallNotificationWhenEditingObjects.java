package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;


/**
 * @author Lokesh Gupta
 *
 */

public class CallNotificationWhenEditingObjects extends SoftphoneBase {
	
	
	@Test(groups = {"Regression"})
	public void accept_call_during_task_create_edit() {
		System.out.println("Test case --accept_call_during_task_create_edit()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//add number as contact
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		
		//Open recent call from call history 
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//verifying notification from call queue section
		softPhoneCallQueues.openCallQueuesSection(driver1);
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//verifying notification from contact search screen
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Accepting call form Message screen
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneCalling.acceptCallFromTop(driver1);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//again opening latest caller entry for edit created Task 
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
				
		//editing the task
		softPhoneActivityPage.editTask(driver1, taskSubject);
				
		// Caller Calls to agent during Task edit
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		//Verifying call notification on top for agent
		softPhoneCalling.verifyAdditionalCall(driver1);
		softPhoneActivityPage.clickTaskDeleletButton(driver1);
		softPhoneActivityPage.clickTaskWarningDeleteButton(driver1);
		softPhoneCalling.acceptCallFromTop(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
				
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression", "Product Sanity"})
	public void notification_during_editing_notes(){
		System.out.println("Test case --notification_during_editing_notes()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		
		//agent receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //Agent hang up with call
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver1);
	    seleniumBase.idleWait(3);
	    
	    //Clicking on Call Notes icon
	    String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.clickCallNotesIcon(driver1);
	    
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.verifyAdditionalCall(driver1);
	    
	    //Entering notes in between notifiction on top
	    callToolsPanel.enterCallNotesText(driver1, callNotes);
	    softPhoneCalling.acceptCallFromTop(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(5);
	    
	    //verifying call notes preserved
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
	    callToolsPanel.clickCallNotesSaveBtn(driver1);
	    
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test (groups= {"Regression"})
	public void call_notification_upon_related_record_search(){
		System.out.println("Test case --call_notification_upon_related_record_search()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String campaignName = CONFIG.getProperty("softphone_task_related_campaign");
		
		//Opening recent call from hisotry 
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickRelatedRecordsIcon(driver1);
		
		//Caller calls to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		
		//Verify notification on top of agent's softphone
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//search and select related record for caller in between
		callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), CONFIG.getProperty("softphone_task_related_campaign"));
		callToolsPanel.selectCampaignFromRelatedRecordSearchList(driver1,CONFIG.getProperty("softphone_task_related_campaign"));
		
		//verify related record selected 
		seleniumBase.idleWait(3);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, campaignName);
	
		//Agent accept call from top notification 
		softPhoneCalling.acceptCallFromTop(driver1);
	    
	    //hangup with the agent
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test (groups= {"Regression"})
	public void notification_when_updating_rating(){
		System.out.println("Test case --notification_when_updating_rating()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickRatingIcon(driver1);
		
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		callToolsPanel.clickRatings(driver1, 5);
		
		softPhoneCalling.acceptCallFromTop(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups= {"Regression"})
	public void decline_call_during_link_access(){
		System.out.println("Test case --decline_call_during_link_access()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	
		//getting missed call and voicemail count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		seleniumBase.idleWait(3);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
		
		//Open recent call entry from history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//clicking Custom link icon
		callToolsPanel.clickCustomLinkIcon(driver1);
		
		//New call coming to user in between to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Select Link when notification on top
		callToolsPanel.selectCustomLinkByText(driver1, CONFIG.getProperty("softphone_custom_link"));
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    assertTrue(driver1.getTitle().contains("Google"));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Decline Call from Top
		softPhoneCalling.declineAdditionalCall(driver1);
		
		// hanging up with caller
		System.out.println("Call end by caller");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		seleniumBase.idleWait(3);
				
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.isMissedVMCountIncreased(driver1, missedVoicemailCount);
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups={"Regression"})
	public void call_notification_during_caller_update(){
		System.out.println("Test case --call_notification_during_caller_update()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Open recent call entry from hisotry 
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//updating the caller for call
		callScreenPage.clickOnUpdateDetailLink(driver1);
		
		//Taking call in between to agent and accept this from top 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test (groups={"Regression"})
	public void notification_during_task_edit_from_email_and_open_activity_tab(){
		System.out.println("Test case --notification_during_task_edit_from_email_and_open_activity_tab()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Open recent call entry from history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Adding a new task for agent
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Email.toString());
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//verify that task is appearing under Open task activity
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
		seleniumBase.idleWait(2); 
		
		//Editing the task from Open Tasks section
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		
		//Caller calls to agent during task editing from Open Task section
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		//Verifying call notification on top for agent
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Closing edit task window
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Call end by caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verify that task is appearing under Email activity section
		softPhoneActivityPage.verifyTaskInEmailTaskTab(driver1, taskSubject);
		
		//Editing the task from Email section
		seleniumBase.idleWait(5);
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
		
		//Caller calls to agent during task editing from Email Task section
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
						
		//Verifying call notification on top for agent
		softPhoneCalling.verifyAdditionalCall(driver1);
				
		//Closing edit task window
		softPhoneActivityPage.clickTaskEditCancelButton(driver1);
		
		//Agent accepting call from top
		softPhoneCalling.acceptCallFromTop(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups={"Regression"})
	public void call_notification_during_caller_add(){
		System.out.println("Test case --call_notification_during_caller_add()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);	
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
			    
		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver1);
		}

		// Open add Caller window
		softPhoneCalling.hangupActiveCall(driver1);
		callScreenPage.clickAddNewButton(driver1);
		
		//Take an incoming call
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//complete adding the contact
	    String contactName = CONFIG.getProperty("qa_user_2_name");
	    String accountName	= CONFIG.getProperty("contact_account_name");
		callScreenPage.enterContactDetailsAndSave(driver1, contactName, accountName);
		softPhoneSettingsPage.closeErrorMessage(driver1);
		
		//Taking call in between to agent and accept this from top 
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Open recent call entry from hisotry 
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), "Abhishek Gupta Automation");
				
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"Regression"})
	public void notification_with_forwarding_during_task_creation() {
		System.out.println("Test case --notification_with_forwarding_during_task_creation()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Open recent call from call history 
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
				
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"Regression"})
	public void notification_with_forwarding_during_editing_notes(){
		System.out.println("Test case --notification_with_forwarding_during_editing_notes()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//agent receiving call
	    System.out.println("Picking up call from forwarding device");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //Agent hang up with call
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(3);
	    
	    //Clicking on Call Notes icon
	    String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.clickCallNotesIcon(driver1);
	    
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.verifyAdditionalCall(driver1);
	    
	    //Entering notes in between notifiction on top
	    callToolsPanel.enterCallNotesText(driver1, callNotes);
	    
	    //Accepting call form Message screen
	  	softPhoneCalling.pickupIncomingCall(driver2);
	  		
	  	//hanging up with agent
	  	System.out.println("hanging up with agent");
	  	softPhoneCalling.hangupIfInActiveCall(driver2);
	  	softPhoneCalling.hangupIfInActiveCall(driver1);
	    
	    //verifying call notes preserved
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
	    callToolsPanel.clickCallNotesSaveBtn(driver1);
	    
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void enable_disable_notifications(){
		System.out.println("Test case --enable_disable_notifications()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallNotifications(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallNotificationIsInvisible(driver1);
		
		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotifications(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallNotificationIsVisible(driver1);
		
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void notification_during_setting_disposition(){
		System.out.println("Test case --notification_during_setting_disposition()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callerName 	= CONFIG.getProperty("prod_user_1_name").trim() + " " + "Automation";
		String companyName	= CONFIG.getProperty("contact_account_name");
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		
		//agent receiving call
	    System.out.println("Picking up call from agent");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //Agent hang up with call
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver1);
	    seleniumBase.idleWait(3);
	    
	    //Clicking on disposition icon
	    callToolsPanel.clickDispositionIcon(driver1);
	    
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.verifyAdditionalCall(driver1);
	    
	    //verify hearder caller details
	    softPhoneCalling.verifyAdditionCallUserDetail(driver1, callerName, companyName);
	    
	    //verify that disposition window is still open
	    callToolsPanel.verifyDispositionSerchBoxVisible(driver1);
	    softPhoneCalling.acceptCallFromTop(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups={"MediumPriority"})
	public void call_notification_during_lead_creation(){
		System.out.println("Test case --call_notification_during_lead_creation()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
			    
		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver1);
		}

		// Open add Caller window
		softPhoneCalling.hangupActiveCall(driver1);
		callScreenPage.clickAddNewButton(driver1);
		callScreenPage.selectLeadOption(driver1);
		
		//Take an incoming call
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//complete adding the contact
	    String leadName = CONFIG.getProperty("prod_user_1_name").trim();
	    String companyName	= CONFIG.getProperty("contact_account_name");
		callScreenPage.addLeadDetailsAndSave(driver1, leadName, companyName);
		softPhoneSettingsPage.closeErrorMessage(driver1);
		
		//Taking call in between to agent and accept this from top 
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		//Open recent call entry from hisotry 
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), leadName + " Automation");
				
		
		//delete and add caller as contact
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), leadName);	
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver3", false);
	}
	
	@Test(groups={"MediumPriority"})
	public void call_notification_during_add_to_existing(){
		System.out.println("Test case --call_notification_during_add_to_existing()-- started");
		
		//Updting the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String existingContact = CONFIG.getProperty("qa_user_2_name").trim() + " Automation";
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
			    
		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver1);
		}

		// Hang up call and search an existing contact to add the caller
		softPhoneCalling.hangupActiveCall(driver1);
		callScreenPage.clickAddToExistingButton(driver1);
		callScreenPage.searchExistingContact(driver1, existingContact);
		
		//Take an incoming call
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//complete adding the contact
		callScreenPage.selectFirstExistingContact(driver1);
		
		//Taking call in between to agent and accept this from top 
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Open recent call entry from hisotry 
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void accept_call_during_conact_card_task_create_edit() {
		System.out.println("Test case --accept_call_during_conact_card_task_create_edit()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String searchContact = CONFIG.getProperty("prod_user_1_name").trim() + " Automation";
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		
		
		//opening caller detail from contact search for edit created Task 
		softPhoneContactsPage.openSfContactDetails(driver1, searchContact);

		//Click on task icon
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//hanging up with agent
		softPhoneCalling.acceptCallFromTop(driver1);
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//mark contact as favourtite
		callScreenPage.setAsFavoriteContact(driver1);
		
		//open contact from favorite tab
		softPhoneContactsPage.openFavContactDetails(driver1, searchContact);
		
		//editing the task
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
				
		//Verifying call notification on top for agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.verifyAdditionalCall(driver1);
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		softPhoneCalling.acceptCallFromTop(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//remove contact from favourite
		callScreenPage.removeFromFavoriteContact(driver1);
		
		//again opening caller detail from contact search for edit created Task 
		softPhoneContactsPage.openSfContactDetails(driver1, searchContact);		
		
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
				
		//editing the task
		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
				
		// Caller Calls to agent during Task edit
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		//Verifying call notification on top for agent
		softPhoneCalling.verifyAdditionalCall(driver1);
		softPhoneActivityPage.clickTaskDeleletButton(driver1);
		softPhoneActivityPage.clickTaskWarningDeleteButton(driver1);
		softPhoneCalling.acceptCallFromTop(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver1);
				
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void notification_accept_multiple_call_from_forwarding() {
		System.out.println("Test case --notification_accept_call_from_forwarding()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Open recent call from call history 
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
	
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
				
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void notification_with_forwarding_without_view_during_editing_notes(){
		System.out.println("Test case --notification_with_forwarding_during_editing_notes()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//agent receiving call
	    System.out.println("Picking up call from forwarding device");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //Agent hang up with call
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(3);
	    
	    //Clicking on Call Notes icon
	    String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.clickCallNotesIcon(driver1);
	    
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
	    
	    //wait until call is removed from forwarding device
	    softPhoneCalling.isAcceptCallButtonVisible(driver2);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver2);
  		
	    //hanging up with the caller
	  	System.out.println("hanging up with agent");
	  	softPhoneCalling.hangupIfInActiveCall(driver3);
		    
	    //verify that view button is removed from agent
	    softPhoneCalling.viewHeaderButtonInvisible(driver1);
	    
	    //Completed adding notes once call is removed
	    callToolsPanel.enterCallNotesText(driver1, callNotes);
	    
	    //verifying call notes preserved
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
	    callToolsPanel.clickCallNotesSaveBtn(driver1);
	    
	    //Clicking on Call Notes icon
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.clickCallNotesIcon(driver1);
	    
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
	    
	    //Accepting call form Message screen
	  	softPhoneCalling.pickupIncomingCall(driver2);
	    
	  	//additional calls visible
	    softPhoneCalling.verifyAdditionalCall(driver1);
	  	
	  	//verify that agent is busy
	  	callScreenPage.verifyUserImageBusy(driver1);
	    
	    //Entering notes in between notifiction on top
	    callToolsPanel.enterCallNotesText(driver1, callNotes);
	  		
	  	//hanging up with agent
	  	System.out.println("hanging up with agent");
	  	softPhoneCalling.hangupIfInActiveCall(driver2);
	  	softPhoneCalling.hangupIfInActiveCall(driver1);
	    
	    //verifying call notes preserved
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callToolsPanel.clickCallNotesIcon(driver1);
	    assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
	    callToolsPanel.clickCallNotesSaveBtn(driver1);
	    
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver3", false);
	}

	@Test(groups = {"MediumPriority"})
	public void notification_with_forwarding_dial_button_not_visible(){
		System.out.println("Test case --notification_with_forwarding_dial_button_not_visible()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//agent receiving call
	    System.out.println("Picking up call from forwarding device");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //Agent hang up with call
	    System.out.println("Call end by agent");
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(3);
	    
	    //Adding a new task for agent
  		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
  		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Email.toString());
  		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
  		
  		//verify that task is appearing under Open task activity
  		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
  		seleniumBase.idleWait(2); 
  		
  		//Editing the task from Open Tasks section
  		softPhoneActivityPage.clickTaskEditButton(driver1, taskSubject);
  		
	    //Verifying new incoming notification on top
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
	    
	    //wait until call is removed from forwarding device
	    softPhoneCalling.pickupIncomingCall(driver2);

	    //verify dial pad is not visible
	    softPhoneCalling.isDialPadButtonInvisible(driver1);
  		
	    //verify task is still in edit state
	    softPhoneActivityPage.clickTaskEditCancelButton(driver1);
	    
	    //verify that task is appearing under Open task activity
  		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);
  		seleniumBase.idleWait(2); 
  		
	    //hanging up with the caller
	  	System.out.println("hanging up with agent");
	  	softPhoneCalling.hangupActiveCall(driver3);
	  	softPhoneCalling.isCallBackButtonVisible(driver2);
		    
	    //verify that view button is removed from agent
	    softPhoneCalling.viewHeaderButtonInvisible(driver1);
	    
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
	    //Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver3", false);
	}
	@Test(groups = {"MediumPriority"})
	public void notification_accept_multiple_call_related_record() {
		System.out.println("Test case --notification_accept_multiple_call_related_record()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Open recent call from call history 
		callToolsPanel.clickRelatedRecordsIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
	
		//Verify notification on top of agent's softphone
		softPhoneCalling.verifyAdditionalCall(driver1);
		
		//search and select related record for caller in between
		callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), CONFIG.getProperty("softphone_task_related_campaign"));
		callToolsPanel.selectCampaignFromRelatedRecordSearchList(driver1,CONFIG.getProperty("softphone_task_related_campaign"));

		//verify related record selected 
		seleniumBase.idleWait(3);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, CONFIG.getProperty("softphone_task_related_campaign"));
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
				
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void notification_end_call_after_received_by_forwarding() {
		System.out.println("Test case --notification_end_call_after_received_by_forwarding()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller is busy editing the task
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Open recent call from call history 
		softPhoneNewTask.clickTaskIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify that end call header button is visible
		softPhoneCalling.isAdditionalCallDeclineBtnEnable(driver1);
		
		//decline call
		softPhoneCalling.declineAdditionalCall(driver1);
		
		//Adding a new task for receiver
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		softPhoneNewTask.addNewTask(driver1, taskSubject, taskComment, dueDate, SoftPhoneNewTask.TaskTypes.Other.toString());
		
		//verify that task is appearing under all tabs
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, taskSubject);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
				
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void notification_navigate_after_received_by_forwarding() {
		System.out.println("Test case --notification_end_call_after_received_by_forwarding()-- started");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//Caller is busy editing the task
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Open recent call from call history 
		callToolsPanel.clickCustomLinkIcon(driver1);
		
		//Caller calls to agent 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
		
		//Accepting call form Message screen
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// navigate to some other tab
		softPhoneMessagePage.clickMessageIcon(driver1);
		
	    //verify view button is visible on header 
	    softPhoneCalling.viewHeaderButtonVisible(driver1);
		
		//hanging up with agent
		System.out.println("hanging up with agent");
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver2);
				
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Product Sanity" }, dependsOnMethods = { "resetSetupDefault" })
	public void afterMethod(ITestResult result) {
		aa_AddCallersAsContactsAndLeads();
	}
}