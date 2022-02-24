package softphone.cases.team;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

/**
 * @author Sumit
 * This class contains test methods of team section with call forwarding
 */
public class TeamSectionCallForwarding extends SoftphoneBase{

	@BeforeClass(groups={"Regression", "Sanity"})
	public void setCallForwarding() {

		System.out.println("Running before class for team with call forwarding");

		//Initializing drivers  
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));

		//Setting call forwarding to second user
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
	}

	
	@Test(priority=201, groups={"Regression", "Sanity"}) 
	public void i_listen_manual_hold_resume_with_callforwarding() {

		System.out.println("---------------- Test Case i_listen_manual_hold_resume_with_callforwarding Started ------------------");

		String dateTime;	

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Selecting contact if associated as mutliple
		callScreenPage.selectFirstContactFromMultiple(driver3);

		//Add contact if added as unknown
		//callScreenPage.addCallerAsLead(driver3, helperFunctions.GetCurrentDateTime(), "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//putting call on hold manually 
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying listen button is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Resuming the hold call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying call is visible on team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving supervisor notes 
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//putting call on hold again
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying listen button is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Resuming the hold call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying call is visible on team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//waiting for team to refresh
		seleniumBase.idleWait(1);
		
		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Opening supervisor notes
		softPhoneTeamPage.openSupervisorNotes(driver1, agentName);

		//Verifying the data 
		assertEquals(dateTime, softPhoneTeamPage.getSupervisorNotes(driver1));
		System.out.println("Supervisor notes are coming fine");

		//Closing the supervisor notes 
		softPhoneTeamPage.closeSupervisorNotes(driver1);

		seleniumBase.idleWait(2);
		//Verifying the call duration 
		String callDuration = softPhoneTeamPage.getCallerTimer(driver1, agentName);
		System.out.println("Call duration" + callDuration);
		//assertEquals(callDuration.equals("00:00:00"), false);

		//hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver3);

		//Verifying that call is removed from team section too 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Verifying that call is removed from call forwarding device
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");
	}


	@Test(priority=202,  groups={"Regression"})
	public void i_supervior_mute_unmute_listening_with_callforwarding() {
		
		System.out.println("---------------- Test Case i_supervior_mute_unmute_listening Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Selecting contact if added as multiple
		callScreenPage.selectFirstContactFromMultiple(driver3);
		
		//Add contact if added as unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		
		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Verifying that listening is started 
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//Unmuting the call
		softPhoneTeamPage.unmuteListening(driver1, agentName);
		
		//Muting the call again
		softPhoneTeamPage.muteListening(driver1, agentName);
		
		//Navigating to contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Again coming to team section
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Verifying that listening is still continue
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//giving supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);
		
		//hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verifying that call forwarding device is free
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Opening the caller detail page
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
	
		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
	
		//getting supervisor notes
		String supervisorNotes = sfTaskDetailPage.getSuperVisorNotes(driver3);
	
		//verifying supervisor notes
		assertEquals(dateTime.trim(), supervisorNotes.trim());
	
		//closing the tab
		seleniumBase.closeTab(driver3);
	
		//switching to softphone tab
		seleniumBase.switchToTab(driver3, 1);

		//Verifying that call is removed from superviosr softphone
		softPhoneTeamPage.verifyTeamCallRemoved(driver1,agentName);
	
		//supervisor making outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//picking up call from driver 3
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Hanging up call from supervisor
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		//verifying call forwarding device also hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=203,  groups={"Regression"})
	public void o_supervisor_disconnect_listening_with_callforwarding() {
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//clicking on listen button for agent's call
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//waiting some time to check call will not hangup automatically 
		seleniumBase.idleWait(5);
		
		//supervisor disconnecting the call
		softPhoneTeamPage.disconnectListening(driver1, agentName);
		
		//verifying that call is hangup from call forwarding device 
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verifying agent still on call and listen button is visible on team section 
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//Hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	
	}
	
	
	@Test(priority=204,  groups={"Regression"})
	public void o_listen_manual_hold_resume_with_callforwarding() {

		System.out.println("---------------- Test Case o_listen_manual_hold_resume_with_callforwarding() Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		callScreenPage.selectFirstContactFromMultiple(driver3);
		
		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, helperFunctions.GetCurrentDateTime(), "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//putting call on hold manually 
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying listen button is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Resuming the hold call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying call is visible on team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving supervisor notes 
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//putting call on hold
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying listen button is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Verifying that call forwarding device is hangup
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Resuming the hold call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying call is visible on team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Opening supervisor notes
		softPhoneTeamPage.openSupervisorNotes(driver1, agentName);

		//Verifying the data 
		assertEquals(dateTime, softPhoneTeamPage.getSupervisorNotes(driver1));
		System.out.println("Supervisor notes are coming fine");

		//Closing the supervisor notes 
		softPhoneTeamPage.closeSupervisorNotes(driver1);

		seleniumBase.idleWait(2);
		//Verifying the call duration 
		String callDuration = softPhoneTeamPage.getCallerTimer(driver1, agentName);
		System.out.println("Call duration" + callDuration);
		//assertEquals(callDuration.equals("00:00:00"), false);

		//hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver3);

		//Verifying that call is removed from team section and call forwarding device too
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=205,  groups={"Regression"})
	public void o_listen_transfer_call_with_callforwarding() {

		System.out.println("---------------- Test Case o_listen_transfer_call_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making outbound call to participant1(driver5) from another agent
		System.out.println("Making call to participant1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at participant1 side 
		System.out.println("Picking up call from participant1");
		softPhoneCalling.pickupIncomingCall(driver5);

		//transferring the call to agent (driver 3)
		softPhoneCalling.transferToNumber(driver4, agentName);

		//picking up call from agent 3
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//hanging up from caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying call is removed from team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call is removed from call forwarding device too
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//making an outbound call from supervisor softphone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		//picking up call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		//picking up call from participant 
		softPhoneCalling.pickupIncomingCall(driver3);

		//hanging up with the call from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");

	}

	@Test(priority=206,  groups={"Regression"})
	public void ii_listen_agent_second_inbound_call_with_callforwarding() {

		System.out.println("---------------- Test Case ii_listen_agent_second_inbound_call_with_callforwarding() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Resuming the first call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//verifying that call is removed from call forwarding device
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Verifying caller1 details are visible 
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Again resuming the call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying details of caller 2
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Hanging up with the first call
		softPhoneCalling.hangupActiveCall(driver4);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 



	@Test(priority=207,  groups={"Regression", "Failing"})
	public void ii_agent_take_new_call_while_supervisor_listening_first_with_callforwarding(){

		System.out.println("---------------- Test Case ii_agent_take_new_call_while_supervisor_listening_first_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying the new caller details 
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that user still listening the caller
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenDisconnectButtonVisible(driver1, agentName);

		//Disconnecting with the call 
		softPhoneTeamPage.disconnectListening(driver1, agentName);

		//Verifying that call forwarding device is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Again listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call at call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//Opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//agent leaving from conference 
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//Verifying that agent is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is also removed from team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call forwarding device is also hangup
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");
	}

	//C3319, C3318
	@Test(priority=208,  groups={"Regression"})
	public void ii_listen_merge_call_with_callforwarding() {

		System.out.println("---------------- Test Case ii_agent_take_new_call_while_supervisor_listening_first_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//Ending from caller
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//verifying that listen button is removed along with listening
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenDisconnectButtonNotVisible(driver1, agentName);

		//verify that call forwarding device also hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//hanging up with other users
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=209,  groups={"Regression"})
	public void oo_agent_take_another_call_while_supervisor_listening_first_with_callforwarding() {

		System.out.println("---------------- Test Case oo_agent_take_another_call_while_supervisor_listening_first_with_callforwarding() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that user still listening the caller
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenDisconnectButtonVisible(driver1, agentName);

		//Disconneting with the call 
		softPhoneTeamPage.disconnectListening(driver1, agentName);

		//Listening the call 
		seleniumBase.idleWait(5);
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//Ending call of agent from conference 
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//Verifying that agent is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is also removed from team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}

	@Test(priority=210,  groups={"Regression"})
	public void oo_listen_agent_second_outbound_call_with_callforwarding() {


		System.out.println("---------------- Test Case oo_listen_agent_second_outbound_call_with_callforwarding() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Resuming the first call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying caller1 details are visible 
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Again resuming the call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying details of caller 2
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Hanging up with the hold call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");


	}


	@Test(priority=211,  groups={"Regression"})
	public void oo_listen_merge_call_after_second_caller_hangup_with_callforwarding() {

		System.out.println("---------------- Test Case oo_listen_merge_call_after_second_caller_hangup_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with call forwarding
		softPhoneCalling.hangupActiveCall(driver2);

		//verifying that listen button is still visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=212,  groups={"Regression", "Failing"}) 
	public void oo_listen_merge_call_after_first_caller_hangup_with_callforwarding() {


		System.out.println("---------------- Test Case oo_listen_merge_call_after_first_caller_hangup_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with call forwarding
		softPhoneCalling.hangupActiveCall(driver2);

		//verifying that listen button is still visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=213,  groups={"Regression"})
	public void oo_listen_merge_call_when_first_caller_hangup_with_callforwarding() {

		System.out.println("---------------- Test Case oo_listen_merge_call_when_first_caller_hangup_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//opening opening conference window
		softPhoneCalling.clickConferenceButton(driver3);
		softPhoneCalling.endParticipantCallFromConference(driver3, 1);
		softPhoneCalling.isConferenceButtonInvisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Verifying that call forwarding device also hang up
		softPhoneCalling.isCallBackButtonVisible(driver2);;

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}



	@Test(priority=214,  groups={"Regression"})
	public void oo_listen_merge_meanwhile_agent_leave_conference_with_callforwarding() {

		System.out.println("---------------- Test Case oo_listen_merge_meanwhile_agent_leave_conference() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//opening conference 
		softPhoneCalling.clickConferenceButton(driver3);

		//agent leaving from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying agent's call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call forwarding device also got hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=215,  groups={"Regression"})
	public void oo_agent_leave_conference_verify_call_removed_from_team_with_callforwarding() {

		System.out.println("---------------- Test Case oo_agent_leave_conference_verify_call_removed_from_team() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//opening conference 
		softPhoneCalling.clickConferenceButton(driver3);

		//agent leaving from conference
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying agent's call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call forwarding device also got hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=216,  groups={"Regression"})
	public void oo_listen_merge_call_agent_hangup_second_caller_with_callforwarding() {

		System.out.println("---------------- Test Case oo_listen_merge_call_agent_hangup_second_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making call from agent to first caller
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen button is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//listening call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//taking call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying listening is started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//opening opening conference window
		softPhoneCalling.clickConferenceButton(driver3);
		softPhoneCalling.endParticipantCallFromConference(driver3, 2);
		softPhoneCalling.isConferenceButtonInvisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Verifying that call forwarding device also hang up
		softPhoneCalling.isCallBackButtonVisible(driver2);;

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");


	}

	//@Test(priority=217,  groups={"Regression"})
	public void oo_listen_switching_agents_with_callforwarding() {
	
		System.out.println("---------------- Test Case oo_listen_switching_agents_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		String agentName1 = CONFIG.getProperty("qa_user_2_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Making another agent busy on call 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_3_number"));

		//Picking up the call from other participant  
		softPhoneCalling.pickupIncomingCall(driver6);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver4, "AutoSumit", "Metacube");

		//Verifying that listen and monitor buttons are coming for second agent
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName1);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName1);

		//listening first call
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//Monitoring another call
		softPhoneTeamPage.clickMonitor(driver1, agentName1);
		
		//Picking up call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying monitoring started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName1);
		
		//Verifying that supervisor is disconnected from first user
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//hanging up call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//stop monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName1);
		
		//verifying that agents call is still visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName1);
		
		//Hanging up with all the calls
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver6);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");
	
	}
	

	@Test(priority=218,  groups={"Regression"})
	public void io_listen_agent_second_outbound_call_with_callforwarding() {

		System.out.println("---------------- Test Case io_listen_agent_second_outbound_call_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying listening 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);

		//checking that call listen button is visible again for call
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Hanging up with the hold call 
		softPhoneCalling.hangupActiveCall(driver4);

		//hanging up with second call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 

	@Test(priority=219,  groups={"Regression"})
	public void io_agent_make_new_call_while_supervisor_listening_first_with_callforwarding() {

		System.out.println("---------------- Test Case io_agent_make_new_call_while_supervisor_listening_first_with_callforwarding() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//listening call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//Receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying listening 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Making another call to second caller from agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening the call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying that listening of agent call is started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Resuming the first call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying caller1 details are visible 
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Again resuming the call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying details of caller 2
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Hanging up with the first active call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);		
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=220,  groups={"Regression"})
	public void oi_listen_second_inbound_call_with_callforwarding() {

		System.out.println("---------------- Test Case oi_listen_second_inbound_call_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//taking another call
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Checking the caller details correctly present on team section for new caller
		contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that listening is still going on 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the second caller 
		softPhoneCalling.hangupActiveCall(driver5);

		//Verifying that conference button is removed 
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Verifying that listening is still going on 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);	
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listening is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=221,  groups={"Regression"})
	public void oi_listen_second_inbound_after_disconnect_first_with_callforwarding() {

		System.out.println("---------------- Test Case oi_listen_second_inbound_call_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//taking another call
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//verifying that again listen button is visible and call forwarding call is removed
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneCalling.isCallBackButtonVisible(driver2);		

		//Checking the caller details correctly present on team section for new caller
		contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that listening is still going on 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the first caller 
		softPhoneCalling.hangupActiveCall(driver4);

		//Verifying that conference button is removed 
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Verifying that listening is still going on 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver5);	
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listening is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=222,  groups={"Regression"})
	public void oi_listen_merge_call_after_first_caller_hangup_with_callforwarding() {

		System.out.println("---------------- Test Case oi_listen_merge_call_after_first_caller_hangup_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//taking another call
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//verifying that again listen button is visible and call forwarding call is removed
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying still listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Hanging up with the first caller 
		softPhoneCalling.hangupActiveCall(driver4);

		//Verifying that conference button is removed 
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver5);	
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listening is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call is also removed from call forwarding softphone
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=223,  groups={"Regression"})
	public void oi_listen_merge_call_with_callforwarding() {

		System.out.println("---------------- Test Case oi_listen_merge_call_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//taking another call
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//verifying that again listen button is visible and call forwarding call is removed
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Merging the call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);

		//picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that listening has started 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver5);	
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying still listening going on 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//verifying call forwarding still enable
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//hanging up from first caller too
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listening is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call is also removed from call forwarding softphone
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=224,  groups={"Regression"})
	public void oi_multi_resume_verify_caller_detail_on_team_with_callforwarding() {

		System.out.println("---------------- Test Case oi_multi_resume_verify_caller_detail_on_team_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//taking another call
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Checking the caller details correctly present on team section for new caller
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Resuming call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Verifying in team again listen button is coming
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//verifying caller details 
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Again resuming the call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Verifying in team again listen button is coming
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//verifying caller details 
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second caller 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);	

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}
	
	@Test(priority=225,  groups={"Regression"})
	public void i_monitor_agent_call_with_callforwarding() {
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Add contact if added as unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//monitoring call 
		//Monitoring another call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call at call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying monitoring
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
		
		//supervisor disconnect monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);
		
		//verifying that call forwarding got disconnected 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//verifying agent still on call and listen button is visible on team section 
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//Hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	
	}

	@Test(priority=226,  groups={"Regression"})
	public void o_monitor_hold_resume_call_with_callforwarding() {

		System.out.println("---------------- Test Case o_monitor_hold_resume_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at participant 
		System.out.println("Picking up call from participant");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible on team section 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//monitoring call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying monitoring 
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//giving supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Putting agent's call on hold 
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying that monitor stop button is still visible
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Verifying that mute and notes buttons are removed 
		softPhoneTeamPage.verifyMuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);

		//verifying that call is hangup from call forwarding device 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Resuming call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying that unmute and notes buttons are coming automatically 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Haning up from agent
		softPhoneCalling.hangupActiveCall(driver3);

		//Verifying that participant is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//verifying that call forwarding device also hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Stopping monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	//@Test(priority=227,  groups={"Regression"})
	public void o_monitor_with_callforwarding_switching_agents() {

		System.out.println("---------------- Test Case o_monitor_with_callforwarding_switching_agents() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		String agentName1 = CONFIG.getProperty("qa_user_2_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Click on monitor call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);	

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Making another agent busy on call 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_3_number"));

		//Picking up the call from other participant  
		softPhoneCalling.pickupIncomingCall(driver6);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver4, "AutoSumit", "Metacube");

		//Verifying that listen and monitor buttons are coming for second agent
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName1);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName1);

		//Monitoring another call
		seleniumBase.idleWait(2);
		softPhoneTeamPage.clickMonitor(driver1, agentName1);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying monitoring
		softPhoneTeamPage.verifyMonitoring(driver1, agentName1);

		//Verifying supervisor disconnected from previous call
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Again Listening first caller
		seleniumBase.idleWait(2);
		softPhoneTeamPage.clickListen(driver1, agentName);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying listening 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Verifying supervisor disconnected from second agent's call
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName1);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName1);

		//Hanging up with all the calls
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver6);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName1);

		//Verifying that call forwarding device is also hangup
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");
	}


	@Test(priority=228,  groups={"Regression"})
	public void o_monitor_with_callforwarding_disconnect_forwarding_line() {

		System.out.println("---------------- Test Case o_monitor_with_callforwarding_disconnect_forwarding_line() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying agent is showing on call 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying that report this call link is not visible 
		reportThisCallPage.verifyReportThisCallNotVisible(driver1);

		//Monitoring call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying monitoring is started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//giving the report
		reportThisCallPage.giveCallReport(driver1, 3, "Incorrect Caller ID", "This is from automation testing");

		//hanging up from call forwarding line 
		softPhoneCalling.hangupActiveCall(driver2);

		//verifying that unmute and notes button removed for the agent from supervisor's team section
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);

		//clicking on stop monitoring button
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//verifying listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//hanging up with the call
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying agent is removed 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=229,  groups={"Regression"})
	public void o_monitor_with_callforwarding_disconnect_agent_and_supervisor() {


		System.out.println("---------------- Test Case o_monitor_with_callforwarding_disconnect_agent_and_supervisor() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying agent is showing on call 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Monitoring call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying call monitoring
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Stopping monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//Verifying that listen button is visible
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying call forwarding line also get disconnected 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Again monitoring the call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call at call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);		

		//verifying call monitoring is started 
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Hanging up from the agent
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//verifying that unmute and notes button removed for the agent from supervisor's team section
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//clicking on stop monitoring button
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//verifying listen button is not visible
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=230,  groups={"Regression"})
	public void oo_monitor_after_conference_with_callforwarding() {

		System.out.println("---------------- Test Case oo_monitor_after_conference_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//merging the hold call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Monitoring the agent's call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//Receiving call at agent 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verifying monitoring 
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Hangup from first participant
		softPhoneCalling.hangupActiveCall(driver5);

		//verifying that agent's call is still visible and connected 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Verifying that call forwarding line is still connected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//hanging up with second participant
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removed from agent and team section side
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//verifying that call forwarding line is also disconnected 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Stopping the monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=231,  groups={"Regression"})
	public void oo_monitor_before_conference_with_callforwarding() {

		System.out.println("---------------- Test Case oo_monitor_before_conference_with_callforwarding() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Click on monitor call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//receiving call at call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Making another call
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//merging the hold call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Open conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//Leaving agent from call
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//verifying that call forwarding device is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//Stopping the monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//hanging up with the other calls 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=232,  groups={"Regression"})
	public void o_monitor_callforwarding_call_with_callforwarding() {


		System.out.println("---------------- Test Case o_monitor_callforwarding_call_with_callforwarding Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Setting call forwarding to second user
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver6, "", CONFIG.getProperty("prod_user_3_number"));

		//Making outbound call to participant 1
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call from call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver6);	

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//monitoring call
		softPhoneTeamPage.clickMonitor(driver1, agentName);

		//receiving call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying monitoring is started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Hangup from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver6);

		//Verifying that agent got free from call
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Stopping the monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//removing call forwarding
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");
	}

	@Test(priority=233,  groups={"Regression"})
	public void i_listen_monitor_agent_callforwarding_call_when_agent_logout_with_callforwarding() {

		System.out.println("---------------- Test Case i_listen_monitor_agent_callforwarding_call_when_agent_logout Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Setting call forwarding to second user
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver6, "", CONFIG.getProperty("prod_user_3_number"));

		//logging out from agent softphone
		softPhoneSettingsPage.logoutSoftphone(driver3);

		//taking inbound call to agent (driver3)
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call from call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver6);	

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening call 
		softPhoneTeamPage.clickListen(driver1, agentName);

		//Receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying listening started
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//stop listening
		softPhoneTeamPage.disconnectListening(driver1, agentName);

		//Checking that call is hangup from call forwarding device
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//monitoring call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//Receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		//verifying monitoring started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Hanging up call from participant
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver6);

		//verifying that agents call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);

		//stopping monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//verifying that agents call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Checking that call is hangup from call forwarding device
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//again logging with the agent's softphone
		SFLP.softphoneLogin(driver3, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_3_username"), CONFIG.getProperty("qa_user_3_password"));

		System.out.println("Test Case is Pass");

	}

	
	@Test(priority=234,  groups={"Regression"})
	public void o_listen_callforwarding_call_with_callforwarding() {
		
		System.out.println("---------------- Test Case o_listen_callforwarding_call_with_callforwarding Started ------------------");
		
		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));
		
		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's monitor button is visible 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Setting call forwarding to second user
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver6, "", CONFIG.getProperty("prod_user_3_number"));

		//Making outbound call to participant (driver5)
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call from call forwarding device
		System.out.println("Picking up call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver6);	

		//receiving call from participant side
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		
		//Setting user image busy 
		callScreenPage.setUserImageBusy(driver1);
		
		//verifying that listen and monitor button are disabled 
		assertEquals(softPhoneTeamPage.isListenButtonEnable(driver1, agentName), false); 
		assertEquals(softPhoneTeamPage.isMonitorButtonEnable(driver1, agentName), false);
		
		//Again setting user image to available
		seleniumBase.idleWait(2);
		callScreenPage.setUserImageAvailable(driver1);
		
		//Verifying that listen and monitor buttons are enabled now 
		assertEquals(softPhoneTeamPage.isListenButtonEnable(driver1, agentName), true); 
		assertEquals(softPhoneTeamPage.isMonitorButtonEnable(driver1, agentName), true);
		
		//listening call
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//picking up call from call forwarding 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//hanging up with the call forwarding line of agent
		softPhoneCalling.hangupActiveCall(driver6);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verifying that call is removed from team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);
		
		//removing call forwarding from driver 3
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		System.out.println("Test Case is Pass");	
	
	}


	@AfterClass(groups={"Regression", "Sanity"})
	public void setDefault() {
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//removing call forwarding from driver 1 and 3
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);	
		
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
	}

}
