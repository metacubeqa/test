/**
 * 
 */
package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallNotes extends SoftphoneBase{
	
	@BeforeGroups(groups={"Call Tools Cases"})
	public void enableCallTools() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallToolsSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}

	@Test(groups = { "Regression", "MediumPriority", "Product Sanity"}, priority = 401)
	public void post_to_chatter() {
		System.out.println("Test case --post_to_chatter-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		String callerName	 = CONFIG.getProperty("qa_user_1_name");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a multiline Comment,\n" + "This is Second Line" + helperFunctions.GetCurrentDateTime();
		callToolsPanel.postToChatter(driver1, callSubject, callNotes);
		softPhoneCalling.hangupActiveCall(driver1);
		
		//opening again Callers detail on softphone
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		int index = softPhoneActivityPage.getChatterIndexByComment(driver1, callNotes);
		assertTrue(index >= 0);
		softPhoneActivityPage.isChatterIconVisibleForIndex(driver1, index);
		
		//verify that open has no red borded and no due date
		softPhoneActivityPage.verifyOpenTasksNoBorder(driver1, "Chatter Post");
		softPhoneActivityPage.getTaskDueDateNotPresent(driver1, "Chatter Post");

		// open task in salesforce
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.validateChatterData(driver1, callerName, callNotes);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Verifying post chatter checkbox for other call history entries
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		assertTrue(callToolsPanel.isCheckPostToChatterSelected(driver1));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" }, priority = 402)
	public void verify_call_notes_subject_limit() {
		System.out.println("Test case --verify_call_notes_subject_limit-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//Enter the call Notes
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		
		//generate 255 character long string and enter into the call notes subject
		String notesSubject = "";
		for (int i = 0; i < 7; i++) {
			notesSubject = notesSubject + HelperFunctions.GetRandomString(35);
		}		 
		notesSubject = notesSubject + HelperFunctions.GetRandomString(10);
		callToolsPanel.enterCallNotesSubject(driver1, notesSubject);
		callToolsPanel.idleWait(1);
		
		//try to add extra string after the 255 characters in call notes subject
		callToolsPanel.appendCallNotesSubject(driver1, "Append Subject");
		callToolsPanel.idleWait(1);
		
		//Verify that only 255 characters are there in calls subject
		assertEquals(callToolsPanel.getCallNotesSubject(driver1), notesSubject);
		
		//close call tools window
		callToolsPanel.clickCallNotesIcon(driver1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//enter call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
		
		//Entering call notes to first entry
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.enterCallNotesSubject(driver1, callSubject);
		callToolsPanel.enterCallNotesText(driver1, callNotes);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		// open task in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject);
		assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//change the call notes description to blank
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.clearCallNotesText(driver1);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		callToolsPanel.idleWait(15);
		
		// open task in salesforce adn verify comments are blank
		softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject);
		assertEquals(sfTaskDetailPage.getComments(driver1), " ");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	
	@Test(groups = { "Regression" }, priority = 403)
	public void create_call_notes() {
		System.out.println("Test case --create_call_notes-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a multiline Comment,\n,\n,\n,\n,\n" + "This is Second Line of Multiline Comment" + helperFunctions.GetCurrentDateTime();
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.clickCallNotesIcon(driver1);
		assertTrue(callToolsPanel.getCallNotesText(driver1).isEmpty());
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		
		//Verify that save button gets enables once user start typing in Call Notes
		callToolsPanel.enterCallNotesText(driver1, "A");
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		
		//Verify that notes are getting cleared clicking on Cancel Button
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		assertTrue(callToolsPanel.getCallNotesText(driver1).isEmpty());
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		
		//Save Call Notes adn verify data in Activity feeds
		callToolsPanel.enterCallNotes(driver1, callSubject, callNotes);
		softPhoneActivityPage.navigateToCallNotesTab(driver1);
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		assertEquals(softPhoneActivityPage.getCommentFromTaskList(driver1, callSubject), callNotes);
		assertTrue(softPhoneActivityPage.getIfTaskCommentIsMultiLine(driver1, callSubject));
		
		//Verify that Save button is disabled
		callToolsPanel.clickCallNotesIcon(driver1);
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.clickCallNotesIcon(driver1);

		// open task in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject);
		assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify Unsaved changes message appear on Call Notes window
	@Test(groups = { "Regression" }, priority = 404)
	public void preserve_call_notes() {
		System.out.println("Test case --preserve_call_notes-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("qa_user_1_number");
		
		//open First Call Entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
		
		//Entering call notes to first entry
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.enterCallNotesText(driver1, callNotes);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver5, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(5);
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		seleniumBase.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driver1);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.enterCallNotesSubject(driver1, callSubject);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		//Append the call
		String appendText = " This is appended Call Comments";
		callToolsPanel.clickCallNotesIcon(driver1);
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.appendCallNotesText(driver1, appendText);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		
		//verify unsaved changes message
		callToolsPanel.verifyUnsavedChangesText(driver1);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver5, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(5);
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openRecentContactCallHistoryEntryByIndex(driver1, 2);
		callToolsPanel.clickCallNotesIcon(driver1);
		seleniumBase.idleWait(2);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes + appendText);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		//verifying that save button gets enabled after clicking post to chatter checkbox
		callToolsPanel.clickCallNotesIcon(driver1);
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.unCheckPostToChatter(driver1);
		callToolsPanel.checkPostToChatter(driver1);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		
		// open task in salesforce
		softPhoneActivityPage.idleWait(30);
		softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject);
		assertEquals(sfTaskDetailPage.getComments(driver1), callNotes+ appendText);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 405)
	public void clear_all_notes_cache() {
		System.out.println("Test case --clear_all_notes_cache-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open First Call Entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
		
		//Entering call notes to first entry
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.enterCallNotesSubject(driver1, callSubject);
		callToolsPanel.enterCallNotesText(driver1, callNotes);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String appendText = " This is appended Call Comments";
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.appendCallNotesText(driver1, appendText);
		
		//Reload Softphone
		reloadSoftphone(driver1);
		
		//Logout Softphone
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		seleniumBase.idleWait(2);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);

		//append call notes
		callToolsPanel.appendCallNotesText(driver1, appendText);
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		
		// open task in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, callSubject);
		assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 406)
	public void call_notes_templates() {
		System.out.println("Test case --call_notes_templates-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotesTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName, callNoteTemplate, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 407)
	public void call_notes_preserve_template() {
		System.out.println("Test case --call_notes_preserve_template-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);

		// Opening recent call screen
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);	
		
		// Opening recent call screen
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.clickCallNotesIcon(driver1);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		assertEquals(callToolsPanel.getSelectedTemplate(driver1), callNoteName);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 408)
	public void call_notes_though_flow_queue() {
		System.out.println("Test case --call_notes_though_flow_queue-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_call_flow_call_queue_smart_number"));

	    //verify that voicemail play icon appearing for first caller
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    
		// entering call notes
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify that while updating call notes, unsaved changes to Call notes should persists
	@Test(groups = { "Regression", "Call Tools Cases" }, priority = 409)
	public void call_notes_unsaved_changes() {
		System.out.println("Test case --call_notes_unsaved_changes-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		//open First Call Entry
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Entering call notes to first entry
		seleniumBase.idleWait(1);
		callToolsPanel.clickCallToolsIcon(driver1);
		callToolsPanel.enterCallNotesSubject(driver1, callSubject);
		callToolsPanel.enterCallNotesText(driver1, callNotes);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		//hangup the call now
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(10);
		
		//Verifying that call notes are preserved
		callToolsPanel.clickCallToolsIcon(driver1);
		assertEquals(callToolsPanel.getCallNotesSubject(driver1), callSubject);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
		assertFalse(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		
		//Append the call
		String appendText = " This is appended Call Comments";
		callToolsPanel.appendCallNotesText(driver1, appendText);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.verifyUnsavedChangesText(driver1);
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);;
		callToolsPanel.clickCallToolsIcon(driver1);
		seleniumBase.idleWait(2);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes + appendText);
		assertTrue(callToolsPanel.getCallNotesSaveBtnStatus(driver1));
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		System.out.println("Test case is pass");
	}
	
	//Unsaved call notes should gone if agent close softphone without saving call notes
	@Test(groups = { "Regression", "Call Tools Cases" }, priority = 409)
	public void call_notes_unsaved_changes_closed() {
		System.out.println("Test case --call_notes_unsaved_changes_closed-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		//open First Call Entry
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Entering call notes to first entry
		seleniumBase.idleWait(1);
		callToolsPanel.clickCallToolsIcon(driver1);
		callToolsPanel.enterCallNotesSubject(driver1, callSubject);
		callToolsPanel.enterCallNotesText(driver1, callNotes);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		//hangup the call now
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Append the call notes
		String appendText = " This is appended Call Comments";
		callToolsPanel.clickCallToolsIcon(driver1);
		callToolsPanel.appendCallNotesText(driver1, appendText);
		callToolsPanel.verifyUnsavedChangesText(driver1);
		
		//Reload Softphone
		reloadSoftphone(driver1);
		
		//Logout Softphone
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
		
		//Verifying that call notes are preserved
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickCallToolsIcon(driver1);
		seleniumBase.idleWait(2);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNotes);
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Unsaved changes message should not appear on providing rating and call disposition from call tool window
	@Test(groups = { "Regression", "Call Tools Cases" }, priority = 410)
	public void call_tools_add_rating() {
		System.out.println("Test case --call_tools_add_rating-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//open First Call Entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//Entering call notes to first entry
		seleniumBase.idleWait(1);
		callToolsPanel.clickCallToolsIcon(driver1);
		callToolsPanel.clickRatings(driver1, 3);
		callToolsPanel.selectDispositionFromCallTools(driver1, "Contacted");
		callToolsPanel.idleWait(1);
		
		//Verify unsaved changes text is appearing
		callToolsPanel.verifyUnsavedChangesText(driver1);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		System.out.println("Test case is pass");
	}
	
	@AfterGroups(groups = {"Call Tools Cases"})
	public void disableCallTools() {

		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}
}