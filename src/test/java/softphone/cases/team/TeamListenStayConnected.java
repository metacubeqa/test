/**
 * 
 */
package softphone.cases.team;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.cases.customUserStatus.CustomUserStatus;
import softphone.cases.softPhoneFunctional.MultiMatchRequired;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountIntelligentDialerTab.CustomStatusFields;
import support.source.accounts.AccountIntelligentDialerTab.CustomStatusTime;

/**
 * @author admin
 *
 */
public class TeamListenStayConnected extends SoftphoneBase{
		
	@BeforeClass(groups={"Sanity", "Regression", "QuickSanity", "Product Sanity"})
	public void beforeClass(){
	// updating the driver used
	initializeDriverSoftphone("driver3");
	driverUsed.put("driver3", true);
	initializeDriverSoftphone("driver5");
	driverUsed.put("driver5", true);
	
	// Setting call forwarding ON
	System.out.println("Setting call forwarding ON");
	softPhoneSettingsPage.setCallForwardingNumber(driver3, driver5, "", CONFIG.getProperty("prod_user_2_number"));
	
	//enable stay connected setting
	softPhoneSettingsPage.enableStayConnectedSetting(driver3);
	
	// updating the driver used
	driverUsed.put("driver3", false);
	driverUsed.put("driver5", false);
	}

	//Supervisor on Team- agent comes on outbound call with call forwarding stay connected - call shows in Team
	//Supervisor on Team- Participant end call forwarding stay connected call- call removed from Team
	@Test(priority=901, groups={"Regression"}) 
	public void verify_stay_connected_call_team_section() {
	
		System.out.println("---------------- Test Case verify_stay_connected_call_team_section() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		assertFalse(softPhoneTeamPage.getCallerTimer(driver1, agentName).equals("00:00:00"));
		
		//Hanging up from caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Supervisor listening agent's call forwarding stay connected call- Participant end call in between
	@Test(priority=902, groups={"Regression"}) 
	public void verify_stay_connected_call_disconnect() {
	
		System.out.println("---------------- Test Case verify_stay_connected_call_disconnect() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//Hanging up from caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Supervisor monitoring agent's call forwarding stay connected call- participant end call
	@Test(priority=903, groups={"Regression"}) 
	public void verify_stay_connected_listen_monitor() {
	
		System.out.println("---------------- Test Case verify_stay_connected_listen_monitor() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		
		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//click on monitor button
		softPhoneTeamPage.clickMonitor(driver1, agentName);
		
		//verifying that monitoring is started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
	
		//verify that the status of the agent is appearing On Call
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), "OnCall");

		//Hanging up from caller
		softPhoneCalling.hangupActiveCall(driver2);
		seleniumBase.idleWait(3);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//verify that the status of the agent is changed to Available
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), "Available");
		
		//verifying that monitoring is still continued
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//verify that call forwarding line not disconnected 
		softPhoneCalling.isHangUpButtonVisible(driver5);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		//stop monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Supervisor listening agent's stay connected call forwarding call- participant end- agent able to take new call
	@Test(priority=904, groups={"Regression"}) 
	public void verify_stay_connected_caller_disconnect_call_supervisor() {
	
		System.out.println("---------------- Test Case verify_stay_connected_caller_disconnect_call_supervisor() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		
		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//Hanging up from caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Make a call to supervisor
		System.out.println("Make a call to supervisor");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));

		//receiving call at supervisor
		System.out.println("Picking up call from supervisor");
		softPhoneCalling.pickupIncomingCall(driver1);
		
		//disconnect call from supervisor
		System.out.println("disconnect call from supervisor");
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Verify supervisor notes to call when team members is on call with multiple match caller
	@Test(priority=905, groups={"Regression"}) 
	public void verify_stay_connected_supervisor_notes_unknown_caller() {
	
		System.out.println("---------------- Test Case verify_stay_connected_supervisor_notes_unknown_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);
		
		//Making outbound call to caller
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.pickupIncomingCall(driver2);
	
		// delete caller to make it unknown
		if (!callScreenPage.isCallerUnkonwn(driver3)) {
 			callScreenPage.deleteCallerObject(driver3);
 			seleniumBase.idleWait(3);
 			softPhoneSettingsPage.closeErrorMessage(driver3);
 		}
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.hangupActiveCall(driver2);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//give supervisor notes to the agent
		String dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);
		
		//Hanging up from caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		//Opening the caller detail page
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.enterCallNotes(driver3, callSubject, callSubject);
		
		// open task in salesforce
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		callScreenPage.openCallerDetailPage(driver3);
		salesforceHomePage.openTaskFromTaskList(driver3, callSubject);

		//getting supervisor notes
		String supervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver3);

		//verifying supervisor notes
		assertEquals(dateTime.trim(), supervisorNotes.trim());

		//closing the tab
		seleniumBase.closeTab(driver3);

		//switching to softphone tab
		seleniumBase.switchToTab(driver3, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Verify supervisor notes to call when team members is on call with Unknown caller
	@Test(priority=906, groups={"Regression"}) 
	public void verify_stay_connected_supervisor_notes_multiple_caller() {
	
		System.out.println("---------------- Test Case verify_stay_connected_supervisor_notes_multiple_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);
		
		String callerPhone 			= CONFIG.getProperty("prod_user_1_number");
		String callerFirstName		= CONFIG.getProperty("prod_user_1_name");
		String existingContactName	= CONFIG.getProperty("prod_user_3_name");
		String existingContact 		= CONFIG.getProperty("prod_user_3_number");
	    
		//adding contact as multiple
		MultiMatchRequired multiMatchRequired = new MultiMatchRequired();
		multiMatchRequired.addCallerAsMultiple(driver3, driver2, callerPhone, callerFirstName, existingContactName, existingContact);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//give supervisor notes to the agent
		String dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);
		
		//Hanging up from caller 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//verifying that listen button is removed
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);
		
		//Opening the caller detail page
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.enterCallNotes(driver3, callSubject, callSubject);
		
		// open task in salesforce
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		callScreenPage.openCallerDetailPage(driver3);
		salesforceHomePage.openTaskFromTaskList(driver3, callSubject);

		//getting supervisor notes
		String supervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver3);

		//verifying supervisor notes
		assertEquals(dateTime.trim(), supervisorNotes.trim());

		//closing the tab
		seleniumBase.closeTab(driver3);

		//switching to softphone tab
		seleniumBase.switchToTab(driver3, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	//Verify Listen and monitor button set disable when  Custom status feature ON
	@Test(priority=907, groups={"Regression"}) 
	public void verify_monitor_listen_disable_custom_status_on() {
	
		System.out.println("---------------- Test Case verify_monitor_listen_disable_custom_status_on() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//
		CustomUserStatus customUserStatus = new CustomUserStatus();
		customUserStatus.removeAllCustomUserStatus();
		seleniumBase.switchToTab(driver1, 1);
		
		//Set the values of the default custom status busy
		String customStatusName = "Busy";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Default busy status");
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver1);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver1, customUserStatusDetail);
		
		//Select custom status which is added above
		customUserStatusPage.selectCustomStatus(driver1, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver1);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);
		
		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));

		//receiving call at agent 
		System.out.println("Picking up call from stay connected line");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//receiving call at agent 
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver2);

		
		//verify listen and monitor button are disabled
		assertFalse(softPhoneTeamPage.isListenButtonEnable(driver1, agentName));
		assertFalse(softPhoneTeamPage.isMonitorButtonEnable(driver1, agentName));
		
		//disconnect call from supervisor
		System.out.println("disconnect call from supervisor");
		softPhoneCalling.hangupActiveCall(driver2);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver5);

		customUserStatus.disableCustomUserStatus();
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	 @AfterMethod(groups = { "Regression",  "MediumPriority", "Product Sanity"})
	  	public void afterMethod(ITestResult result) {
		if(result.getName().equals("verify_stay_connected_supervisor_notes_unknown_caller") || result.getName().equals("verify_stay_connected_supervisor_notes_multiple_caller")) {
			aa_AddCallersAsContactsAndLeads();
		}
	  }
	
	@AfterClass(groups={"Sanity", "Regression", "QuickSanity", "Product Sanity"})
	public void afterClass(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		//disable stay connected setting
		softPhoneSettingsPage.disableStayConnectedSetting(driver3);
		
		// updating the driver used
		driverUsed.put("driver1", false);
	}

}
