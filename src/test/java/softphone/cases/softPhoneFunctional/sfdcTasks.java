/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class sfdcTasks extends SoftphoneBase {

	@Test(groups = { "Regression" })
	public void track_unknown_caller() {
		System.out.println("Test case --track_unknown_caller-- started ");

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

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			callScreenPage.deleteCallerObject(driver1);
		}
		softPhoneCalling.hangupActiveCall(driver1);

		// Taking incoming call from a unknown user
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);

		// entering call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.enterCallNotes(driver1, callSubject, callSubject);
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.isCheckPostToChatterInVisible(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		softPhoneCalling.hangupActiveCall(driver1);

		// open task in salesforce
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);

		// Verifying Recent Calls Detail
		assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
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

	@Test(groups = { "Regression" })
	public void track_multiple_caller() {
		System.out.println("Test case --track_multiple_caller-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String contactFirstName = "Contact_Existing";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver1);

		// Deleting contact if exist
		if (!callScreenPage.isCallerMultiple(driver1) && callScreenPage.isCallerUnkonwn(driver1)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(driver1, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}

		if (!callScreenPage.isCallerMultiple(driver1)) {
			// Update caller to be contact
			callScreenPage.clickOnUpdateDetailLink(driver1);
			callScreenPage.addContactForExistingCaller(driver1, contactFirstName, CONFIG.getProperty("contact_account_name"));
			reloadSoftphone(driver1);

			// sync time from sfdc to ringdna
			seleniumBase.idleWait(30);
		}

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver1);

		// entering call notes
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.enterCallNotes(driver1, callSubject, callSubject);
		assertTrue(callScreenPage.isCallerMultiple(driver1));
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.isCheckPostToChatterInVisible(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		softPhoneCalling.hangupActiveCall(driver1);

		// open task in salesforce
		callScreenPage.selectFirstContactFromMultiple(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);

		// Verifying Recent Calls Detail
		assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
		assertTrue(sfTaskDetailPage.getCallHourDayLocal(driver1)!=null && !sfTaskDetailPage.getCallHourDayLocal(driver1).isEmpty());
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		softPhoneContactsPage.convertMultipleToSingle(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void verify_sfdc_tasks_call_decline() {
		System.out.println("Test case --verify_sfdc_tasks_call_decline-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//disabling voicemail enable option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableVoiceMailEnabledSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);

		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
				
		// declining call
		System.out.println("declining call");
		softPhoneCalling.isDeclineButtonVisible(driver1);
		softPhoneCalling.declineCall(driver1);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);;
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		enableVoicemail();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	
	@Test(groups = {"Regression"})
	public void verify_sfdc_tasks_for_call_connected() {
		System.out.println("Test case --verify_sfdc_tasks_for_call_connected-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making an outbound call from softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    seleniumBase.idleWait(20);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);;
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotConnected(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		callToolsPanel.selectDispositionUsingText(driver1, "Contacted");
		
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallConnected(driver1);
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
	
	@Test(groups = {"Regression"})
	public void verify_sfdc_tasks_for_call_connected_after_90_secs() {
		System.out.println("Test case --verify_sfdc_tasks_for_call_connected_after_90_secs-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making an outbound call from softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    seleniumBase.idleWait(92);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verify missed call and vm count before
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);;
	    
	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallConnected(driver1);
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
	
	@Test(groups = {"Regression"})
	public void verify_call_duration_for_second_call() {
		System.out.println("Test case --verify_call_duration_for_second_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		
		seleniumBase.idleWait(5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		
		// decline call from agent's phone
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		// hanging up with call 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		 //resuming first call	    
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    seleniumBase.idleWait(5);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("prod_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
		
		// hanging up with call 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from called agent
		System.out.println("Call is removing from called agent");
		softPhoneCalling.isCallBackButtonVisible(driver2);

	    //verify data for vm dropped call
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    String callKeyID =sfTaskDetailPage.getCallObjectId(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		//navigating to support page 
	    loginSupport(driver1);
		
		//opening call inspector page
		dashboard.openCallInspectorPage(driver1);
		callInspector.getCallData(driver1, callKeyID);
		assertTrue(callInspector.getCallDuration(driver1) > 0);
		
		//close the tab
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
	
	//Verify for a known caller activity data correctly updated in SFDC as given at Softphone
	@Test(groups = {"Regression"})
	public void verify_sfdc_tasks_for_updated_data() {
		System.out.println("Test case --verify_sfdc_tasks_for_updated_data-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making an outbound call from softphone
	    System.out.println("first caller making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);

		//hangup call from caller if active
		System.out.println("hangup call from caller if active");
		softPhoneCalling.hangupActiveCall(driver2);
		
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
	    //verify the data before updating it
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callSubject);
	    assertEquals(sfTaskDetailPage.getRating(driver1), "0");
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), " ");
	    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), " ");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Update the task with the data
		String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String relatedTask = CONFIG.getProperty("softphone_task_related_opportunity");
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),relatedTask);
	    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
	    callToolsPanel.giveCallRatings(driver1, 4);
	    
	    //Verify the data in sfdc task after it has been updated
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callNotes);
	    assertEquals(sfTaskDetailPage.getSubject(driver1), callNotes);
	    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
	    assertEquals(sfTaskDetailPage.getRating(driver1), "4");
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), relatedTask);
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
	
	@AfterClass(groups = {"Regression"})
	public void enableVoicemail(){
		
		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//enabling voicemail enabled option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableVoiceMailEnabledSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	}
}
