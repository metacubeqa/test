package softphone.cases.team;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import utility.HelperFunctions;

/**
 * @author Sumit
 * This class contains test methods for team section
 *
 */
public class TeamSection extends SoftphoneBase{
	
	@Test(priority=101, groups={"Regression"}) 
	public void i_inbound_manual_hold_resume() {

		System.out.println("---------------- Test Case i_inbound_manual_hold_resume Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Opening supervisor notes
		softPhoneTeamPage.openSupervisorNotes(driver1, agentName);

		//Verifying the data 
		assertEquals(dateTime, softPhoneTeamPage.getSupervisorNotes(driver1));
		System.out.println("Supervisor notes are coming fine");

		//Closing the supervisor notes 
		softPhoneTeamPage.closeSupervisorNotes(driver1);

		//Verifying the call duration 
		seleniumBase.idleWait(2);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		seleniumBase.idleWait(2);
		softPhoneTeamPage.openTeamSection(driver1);
		String callDuration = softPhoneTeamPage.getCallerTimer(driver1, agentName);
		System.out.println("Call duration" + callDuration);
		//assertEquals(callDuration.equals("00:00:00"), false);

		//hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying that call is removed from team section too 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");
	}


	@Test( priority=102, groups={"Regression", "Sanity", "Product Sanity"})
	public void i_supervior_mute_unmute_listening() {

		System.out.println("---------------- Test Case i_supervior_mute_unmute_listening Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//picking up call from driver 3
		softPhoneCalling.pickupIncomingCall(driver3);

		//Hanging up call from supervisor
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=103, groups={"Regression", "Failing"}) 
	public void o_outbound_manual_hold_resume() {

		System.out.println("---------------- Test Case o_outbound_manual_hold_resume() Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making outbound call to participant1(driver5)
		System.out.println("Making call to participant1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at participant1 side 
		System.out.println("Picking up call from participant1");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Opening supervisor notes
		softPhoneTeamPage.openSupervisorNotes(driver1, agentName);

		//Verifying the data 
		assertEquals(dateTime, softPhoneTeamPage.getSupervisorNotes(driver1));
		System.out.println("Supervisor notes are coming fine");

		//Closing the supervisor notes 
		softPhoneTeamPage.closeSupervisorNotes(driver1);

		//Verifying the call duration 
		seleniumBase.idleWait(2);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		seleniumBase.idleWait(2);
		softPhoneTeamPage.openTeamSection(driver1);
		String callDuration = softPhoneTeamPage.getCallerTimer(driver1, agentName);
		System.out.println("Call duration" + callDuration);
		//assertEquals(callDuration.equals("00:00:00"), false);

		//hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying that call is removed from team section too 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");
	}


	@Test(priority=104, groups={"Regression"})
	public void o_listen_transfer_call() {

		System.out.println("---------------- Test Case o_listen_transfer_call() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening the call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//hanging up from caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying call is removed from team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=105, groups={"Regression"})
	public void ii_listen_agent_second_inbound_call() {

		System.out.println("---------------- Test Case ii_listen_agent_second_inbound_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//selecting contact if associated as multiple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		/*		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		 */		
		//selecting contact if associated as multiple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 


	@Test(priority=106, groups={"Regression", "Sanity", "Product Sanity"})
	public void ii_agent_take_another_call_while_supervisor_listening_first()
	{
		System.out.println("---------------- Test Case ii_agent_take_another_call_while_supervisor_listening_first() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		/*		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		 */		
		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Again listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}


	@Test(priority=107, groups={"Regression"})
	public void ii_listen_merge_call_first_caller_hangup() {

		System.out.println("---------------- Test Case ii_listen_merge_call_first_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//merging calls
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the first call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor still able to listen call
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the second call
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=108,  groups={"Regression"})
	public void ii_listen_merge_call_agent_hangup_second_caller() {
		System.out.println("---------------- Test Case ii_listen_merge_call_agent_hangup_second_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName).trim());
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName).trim());
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		/*		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		 */
		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails1.get("Name"));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//merging calls
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that still second caller is showing 
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//hangup with second caller from agent's call screen 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor still able to listen call
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Verifying that first caller info reflected to team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the first call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=109,  groups={"Regression"})
	public void oo_listen_merge_call_agent_leave() {

		System.out.println("---------------- Test Case oo_listen_merge_call_agent_leave() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));
		System.out.println("Second participant's contact details are coming fine");

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Navigating on contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);

		//Again opening team section 
		softPhoneTeamPage.openTeamSection(driver1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Hanging up with the conference call from agent side
		softPhoneCalling.clickConferenceButton(driver3);
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=110,  groups={"Regression"})
	public void oo_listen_agent_second_outbound_call() {

		System.out.println("---------------- Test Case oo_listen_agent_second_outbound_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=111, groups={"Regression", "Sanity"})
	public void oo_agent_take_another_call_while_supervisor_listening_first()
	{
		System.out.println("---------------- Test Case oo_agent_take_another_call_while_supervisor_listening_first() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Again listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}


	@Test(priority=112,  groups={"Regression"})
	public void oo_listen_merge_call_first_caller_hangup() {

		System.out.println("---------------- Test Case oo_listen_merge_call_first_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up from first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor is still connected with call 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Verifying still same call details are visible 
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with other  caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=113,  groups={"Regression"})
	public void oo_listen_merge_call_second_caller_hangup() {

		System.out.println("---------------- Test Case oo_listen_merge_call_second_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//listening the agent's call
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying listening
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up from second caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor is still connected with call 
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with other  caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=114, groups={"Regression","QuickSanity"}) 
	public void oo_resume_caller_details_verify() {

		System.out.println("---------------- Test Case oo_resume_caller_details_verify() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));
		System.out.println("Second participant's contact details are coming fine");

		//resuming the call
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Verifying first call info is showing
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Merging call
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver3);
		softPhoneCalling.clickMergeButton(driver3);

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the conference call from agent side
		softPhoneCalling.clickConferenceButton(driver3);
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=115,  groups={"Regression"})
	public void oo_listen_merge_call_after_first_caller_hangup() {

		System.out.println("---------------- Test Case oo_listen_merge_call_after_first_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//hanging up with first caller
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test( priority=116, groups={"Regression"})
	public void oo_listen_merge_call_after_second_caller_hangup() {

		System.out.println("---------------- Test Case oo_listen_merge_call_after_second_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//waiting as idle for some time 
		seleniumBase.idleWait(2);

		//Making call to participant 1(driver4)
		System.out.println("Making call to participant 1(Driver4)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//Receiving call from participant 1 (driver4)  
		System.out.println("Receiving call from participant 1 (driver4)");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//hanging up with first caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=117,  groups={"Regression"})
	public void oo_caller_detail_visibility_on_team_while_select_caller_from_conference() {


		System.out.println("---------------- Test Case oo_agent_take_another_call_while_supervisor_listening_first() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Making another call to participant 2(driver5)
		System.out.println("Making call to participant 2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call
		System.out.println("Call received at participant 2(driver5)");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying still second caller is visible on call
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//selecting another caller 
		softPhoneCalling.availableSelectButton(driver3).get(0).click();

		//verifying that caller details are changed into team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);
		seleniumBase.idleWait(3);
		
		//disconnecting call
		softPhoneTeamPage.disconnectListening(driver1, agentName);
		
		//opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//selecting another caller 
		softPhoneCalling.availableSelectButton(driver3).get(0).click();

		//verifying that caller details are changed into team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails1.get("Name").trim());
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//opening conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//agent hangup second call
		softPhoneCalling.endParticipantCallFromConference(driver3, 2);
		softPhoneCalling.isConferenceButtonInvisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//verifying that caller details are changed into team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the first call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is also removed from team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}


	@Test(priority=118,  groups={"Regression"})
	public void io_listen_agent_second_outbound_call() {

		System.out.println("---------------- Test Case io_listen_agent_second_outbound_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 

	@Test(priority=119,  groups={"Regression", "Product Sanity"})
	public void io_agent_make_outobund_call_while_supervisor_listening_first() {


		System.out.println("---------------- Test Case io_agent_make_outobund_call_while_supervisor_listening_first() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Making outbound call to second participant from agent
		System.out.println("Making outbound call to second participant from agent");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call from participant side
		System.out.println("Receiving the call from participant side");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//hanging up with the participants 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from team
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}

	@Test( priority=120, groups={"Regression", "Product Sanity"})
	public void io_agent_call_remove_when_leave_conference_without_listen() {


		System.out.println("---------------- Test Case io_agent_call_remove_when_leave_conference_without_listen() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to second participant from agent
		System.out.println("Making outbound call to second participant from agent");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call from participant side
		System.out.println("Receiving the call from participant side");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

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

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Passed");

	}


	@Test(priority=121,  groups={"Regression"})
	public void io_listen_merge_call_hangup_second_caller() {

		System.out.println("---------------- Test Case io_listen_merge_call_hangup_second_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Making outbound call to second participant from agent
		System.out.println("Making outbound call to second participant from agent");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call from participant side
		System.out.println("Receiving the call from participant side");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening the merge call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the second caller 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor still able to listen call
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the hold call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=122,  groups={"Regression"})
	public void io_listen_merge_call_after_second_caller_hangup() {

		System.out.println("---------------- Test Case io_listen_merge_call_after_second_caller_hangup() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Making outbound call to second participant from agent
		System.out.println("Making outbound call to second participant from agent");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//Receiving the call from participant side
		System.out.println("Receiving the call from participant side");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Merging the calls 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying after also caller detail is visible fine to team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails1.get("Name"));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second caller 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//Verifying after also caller detail is visible fine to team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//listening the merge call 
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Hanging up with the hold call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=123,  groups={"Regression"})
	public void oi_listen_merge_call_agent_leave() {

		System.out.println("---------------- Test Case oi_listen_merge_call_agent_leave() ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetailsCaller1 = new HashMap<String, String>();
		contactDetailsCaller1 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Taking another call to agent from caller2(driver5)
		System.out.println("Taking call from caller2(driver5)");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call
		System.out.println("Call received at agent(driver3)");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's second call");

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));
		System.out.println("Second participant's contact details are coming fine");

		//Merging the call 
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Navigating on contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);

		//Again opening team section 
		softPhoneTeamPage.openTeamSection(driver1);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		System.out.println("Listen button is coming visible for agent's merge call");

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Giving the supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Hanging up with the conference call from agent side
		softPhoneCalling.clickConferenceButton(driver3);
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//verifying that listen button is removed from softphone team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=124,  groups={"Regression"})
	public void oi_listen_agent_second_inbound_call() {

		System.out.println("---------------- Test Case oi_listen_agent_second_inbound_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making fisrt outbound call to participant 1
		System.out.println("Making first call to participant1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 

	@Test(priority=125,  groups={"Regression"})
	public void oi_agent_take_inbound_call_while_supervisor_listening_first_call() {

		System.out.println("---------------- Test Case oi_agent_take_inbound_call_while_supervisor_listening_first_call() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making fisrt outbound call to participant 1
		System.out.println("Making first call to participant1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//merging the calls
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//verifying listening
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying listening
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Hanging up with the hold call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	} 

	@Test(priority=126,  groups={"Regression"})
	public void oi_resume_caller_details_verify() {

		System.out.println("---------------- Test Case oi_resume_caller_details_verify() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making fisrt outbound call to participant 1
		System.out.println("Making first call to participant1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

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

		/*		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		 */

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Verifying the new caller details 
		Map<String, String> contactDetailsCaller2 = new HashMap<String, String>();
		contactDetailsCaller2 = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetailsCaller2.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Resuming the first call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//Checking that listen button is showing in team section
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Verifying caller1 details are visible 
		assertEquals(contactDetailsCaller1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetailsCaller1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the second call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		//Hanging up with the hold call
		softPhoneCalling.hangupActiveCall(driver5);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=127,  groups={"Regression", "Sanity"})
	public void o_monitor_call() {

		System.out.println("---------------- Test Case o_monitor_call() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

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
		softPhoneTeamPage.monitorAgent(driver1, agentName1);

		//Verifying supervisor disconnected from previous call
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//Listening first caller
		softPhoneTeamPage.listenAgent(driver1, agentName);

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

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");
	}


	@Test(priority=128,  groups={"Regression"})
	public void o_monitor_verify_supervisor_notes() {

		System.out.println("---------------- Test Case o_monitor_verify_supervisor_notes() Started ------------------");

		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//making outbound call to agent 3
		System.out.println("Making call to caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at caller
		System.out.println("Picking up call from caller");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Selecting caller is added as multiple 
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Monitoring call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//giving supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Hanging up with the agent call
		softPhoneCalling.hangupActiveCall(driver3);

		//Verifying that call is removed from team section
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Stopping the monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

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

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=129,  groups={"Regression", "Product Sanity"})
	public void o_monitor_hold_resume_call() {

		System.out.println("---------------- Test Case o_monitor_hold_resume_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying agent's call is visible on team section 
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//monitoring call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//giving supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//giving report this call 
		reportThisCallPage.giveCallReport(driver1, 3, "Incorrect Caller ID", "This is from Automation Testing");

		//Putting agent's call on hold 
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying that monitor stop button is still visible
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Verifying that mute and notes buttons are removed 
		softPhoneTeamPage.verifyMuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);

		//Resuming call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying that unmute and notes buttons are coming automatically 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Haning up from agent
		softPhoneCalling.hangupActiveCall(driver3);

		//Verifying that participant is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//Stopping monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=130,  groups={"Regression"})
	public void oo_monitor_after_conference() {

		System.out.println("---------------- Test Case oo_monitor_after_conference() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//Hangup from first participant
		softPhoneCalling.hangupActiveCall(driver5);

		//verifying that agent's call is still visible and connected 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//hanging up with second participant
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removed from agent and team section side
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Stopping the monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=131,  groups={"Regression"})
	public void oo_monitor_before_conference() {

		System.out.println("---------------- Test Case oo_monitor_before_conference Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Making another call
		System.out.println("Making call to participant 1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//merging the hold call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that agent automatically connected with the supervisor
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Open conference window 
		softPhoneCalling.clickConferenceButton(driver3);

		//Leaving agent from call
		softPhoneCalling.clickAgentConferenceLeaveButton(driver3);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Stopping the monitoring 
		seleniumBase.idleWait(2);
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//hanging up with the other calls 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=132,  groups={"Regression"})
	public void oo_monitor_callforwarding_conference() {

		System.out.println("---------------- Test Case oo_monitor_callforwarding_conference Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		// opening contact search
		System.out.println("Öpening contact search page");
		softPhoneContactsPage.clickActiveContactsIcon(driver3);

		//waiting for 2 sec for update contact search result
		seleniumBase.idleWait(2);

		// Selecting RingDNA search
		System.out.println("Selecting RingDNA radio button ");
		softPhoneContactsPage.selectRingDNAOption(driver3);

		// searching a user
		System.out.println("Searching an user");
		softPhoneContactsPage.enterSearchContactText(driver3, CONFIG.getProperty("qa_user_2_name"));
		softPhoneContactsPage.clickSearchContactButton(driver3);

		// verifying the results
		System.out.println("Verifying the results");
		int contactIndex = softPhoneContactsPage.getRdnaResultsNameIndex(driver3, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());
		assertNotEquals(contactIndex, -1);

		// Making second call to ringdna user
		softPhoneContactsPage.clickRdnaResultsCallButton(driver3, contactIndex, SoftPhoneContactsPage.searchTypes.Users.toString());

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver4);

		//Adding caller if unknown
		//callScreenPage.addCallerAsLead(driver3, "AutoSumit", "Metacube");

		//Verifying that call is connected at call forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver6);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//merging the hold call
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Monitoring the agent's call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//Hangup from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver6);

		//Verifying that agent got free from call
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Stopping the monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//hanging up with the other calls 
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Removing call forwarding from driver6
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=133,  groups={"Regression"})
	public void ii_caller_detail_verify_second_caller_hangup_from_conference() {

		System.out.println("---------------- Test Case ii_caller_detail_verify_second_caller_hangup_from_conference() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//If caller is showing as unknown then again calling 
		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}

		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails1.get("Name"));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//merging calls
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that still second caller is showing 
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//hangup with second caller 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor still able to listen call
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);

		//Verifying that first caller info reflected to team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the first call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);

		//Stopping monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=134,  groups={"Regression"})
	public void i_take_or_make_call_while_supervisor_listening_call() {

		System.out.println("---------------- Test Case ii_listen_merge_call_agent_hangup_second_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//listening call
		softPhoneTeamPage.listenAgent(driver1, agentName);
		
		//closing error if present
		callScreenPage.closeErrorBar(driver1);
		
		//dialing number from dialer
		softPhoneCalling.openDialPad(driver1);
		softPhoneCalling.enterNumberInDialpad(driver1, CONFIG.getProperty("prod_user_2_number"));
		seleniumBase.idleWait(2);
		softPhoneCalling.clickDialButton(driver1);
		
		//verifying that error to be visible
		callScreenPage.verifyErrorPresent(driver1);
		
		//verifying errors text
		assertEquals(callScreenPage.getErrorText(driver1).contains("You must disconnect from the current call before placing a new call"), true);
	
		//closing the error 
		callScreenPage.closeErrorBar(driver1);
		
		//getting voicemail count 
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		
		//Taking incoming call to supervisor
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		
		//Disconnect Call connected
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupActiveCall(driver5);
		
		//verifying that missed call count has increased
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
	
		//hanging up from the agent
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//Verifying agent is removed from supervisor team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
		
	} 


	@Test(priority=135,  groups={"Regression"})
	public void iii_caller_detail_verify_on_team() {

		System.out.println("---------------- Test Case ii_listen_merge_call_agent_hangup_second_caller() Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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

		//taking incoming call to agent 3
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

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

		//Taking another call from second caller to agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//Receiving the call 
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		/*		//Taking call again if it's showing as unknown
		if(callScreenPage.isCallerUnkonwn(driver3)) {
			//Hanging up with the call 
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.isCallBackButtonVisible(driver5);
			//Taking another call from second caller to agent
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//Receiving the call 
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		 */		
		//verifying that listen and monitor buttons are coming for new caller in team sectin 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails1 = new HashMap<String, String>();
		contactDetails1 = callScreenPage.getCallerDetails(driver3);
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails1.get("Name"));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//merging calls
		softPhoneCalling.mergeOnHoldCall(driver3, 1);

		//Verifying that still second caller is showing 
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//taking another incoming call 
		System.out.println("Taking call from caller3");
		softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_3_number"));

		//picking up the call at agent end
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver3);

		//verifying that user details are not changing at team section
		assertEquals(contactDetails1.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails1.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails1.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Listen the agent from supervisor
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//Selecting third incoming call on conference call
		softPhoneCalling.clickConferenceButton(driver3);

		//selecting another call
		softPhoneCalling.availableSelectButton(driver3).get(1).click();

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails2 = new HashMap<String, String>();
		contactDetails2 = callScreenPage.getCallerDetails(driver3);
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails2.get("Name"));
		assertEquals(contactDetails2.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails2.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//hangup with two caller from agent's call screen 
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.hangupActiveCall(driver6);
		softPhoneCalling.isConferenceButtonInvisible(driver3);

		//verifying that supervisor still able to listen call
		softPhoneTeamPage.verifyListening(driver1, agentName);

		//Verifying that first caller info reflected to team section
		softPhoneTeamPage.verifyCallerName(driver1, agentName, contactDetails.get("Name"));
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//Hanging up with the first call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=136,  groups={"Regression"})
	public void i_monitor_call() {

		System.out.println("---------------- Test Case i_monitor_call() Started ------------------");
		String dateTime;

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();

		//Making outgoing call to driver5
		System.out.println("Taking call from caller1");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		while(callScreenPage.isCallerMultiple(driver3)) {

		}

		//Selecting contact if associated as mutliple
		//callScreenPage.selectFirstContactFromMultiple(driver3);

		//Deleting the caller if added
		if(!callScreenPage.isAddNewButtonVisible(driver3)) {
			callScreenPage.deleteCallerObject(driver3);
			softPhoneCalling.hangupActiveCall(driver3);
			//taking incoming call to agent 3
			System.out.println("Taking call from caller1");
			softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));
			//receiving call at agent 
			System.out.println("Picking up call from agent");
			softPhoneCalling.pickupIncomingCall(driver3);
		}

		//Closing the salesforce error if showing 
		callScreenPage.closeErrorBar(driver3);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		//softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//Verifying agent's call is visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyMonitorButtonVisible(driver1, agentName);

		//Monitoring call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//clicking on unmute button 
		softPhoneTeamPage.unmuteListening(driver1, agentName);

		//Adding caller if it's unknown
		dateTime = helperFunctions.GetCurrentDateTime();
		callScreenPage.addCallerAsLead(driver3, dateTime, "RingDNA");

		//waiting for name to be updated 
		softPhoneTeamPage.verifyCallerIsKnown(driver1, agentName);

		//Verifying that mute button is still visible 
		softPhoneTeamPage.verifyMuteButtonVisible(driver1, agentName);

		//Checking the caller details correctly present on team section 
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(driver3);
		assertEquals(contactDetails.get("Name"), softPhoneTeamPage.getCallerName(driver1, agentName));
		assertEquals(contactDetails.get("Company"), softPhoneTeamPage.getCallerCompanyName(driver1, agentName));
		assertEquals(contactDetails.get("Title"), softPhoneTeamPage.getCallerTitle(driver1, agentName));

		//verifying that notes button is coming visible now 
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//giving supervisor notes
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName, dateTime);

		//Hanging up from caller side
		softPhoneCalling.hangupActiveCall(driver5);

		//Verifying that agent is free 
		softPhoneCalling.isCallBackButtonVisible(driver3);

		//Verifying that monitor stop button is still visible
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Verifying that mute and notes buttons are removed 
		softPhoneTeamPage.verifyMuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);

		//verifying that newly added caller is in synch with salesforce
		softPhoneContactsPage.searchUntilContactPresent(driver3, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_2_number")));

		//Again taking incoming call
		System.out.println("Taking call from participant 1");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//verifying that unmute and notes buttons are coming automatically 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Putting call on hold 
		softPhoneCalling.putActiveCallOnHold(driver3);

		//Verifying that monitor stop button is still visible
		softPhoneTeamPage.verifyMonitorStopButtonVisible(driver1, agentName);

		//Verifying that mute and notes buttons are removed 
		softPhoneTeamPage.verifyMuteButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonNotVisible(driver1, agentName);

		//Resuming call 
		softPhoneCalling.resumeHoldCall(driver3, 1);

		//verifying that unmute and notes buttons are coming automatically 
		softPhoneTeamPage.verifyUnmuteButtonVisible(driver1, agentName);
		softPhoneTeamPage.verifyNotesButtonVisible(driver1, agentName);

		//Stoping the monitor
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//Hanging up witht  the call
		softPhoneCalling.hangupActiveCall(driver3);

		//verifying that participant is hangup 
		softPhoneCalling.isCallBackButtonVisible(driver5);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}


	@Test(priority=137,  groups={"Regression"})
	public void i_listen_monitor_agent_callforwarding_call_when_agent_logout() {

		System.out.println("---------------- Test Case i_listen_monitor_agent_callforwarding_call_when_agent_logout Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//monitoring call
		softPhoneTeamPage.monitorAgent(driver1, agentName);

		//Hanging up call from participant
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver6);

		//verifying that agents call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		softPhoneTeamPage.verifyUnmuteButtonNotVisible(driver1, agentName);

		//Waiting for 2 sec to make stop 
		seleniumBase.idleWait(2);

		//stopping monitoring
		softPhoneTeamPage.stopMonitoring(driver1, agentName);

		//verifying that agents call is removed from supervisor team section
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);

		//again logging with the agent's softphone
		SFLP.softphoneLogin(driver3, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_3_username"), CONFIG.getProperty("qa_user_3_password"));

		//removing call forwarding from driver 3
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver6", false);
		driverUsed.put("driver5", false);

		System.out.println("Test Case is Pass");

	}

	@Test(priority=138,  groups={"Regression", "Sanity", "helo"})
	public void o_listen_callforwarding_call() {

		System.out.println("---------------- Test Case o_listen_callforwarding_call Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
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
		softPhoneTeamPage.listenAgent(driver1, agentName);

		//hanging up with the call forwarding line of agent
		softPhoneCalling.hangupActiveCall(driver6);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//verifying that call is removed from team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);

		//removing call forwarding from driver 3
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);

		System.out.println("Test Case is Pass");	

	}
	
	@Test(priority=139,  groups={"Regression"})
	public void select_caller_from_conference_with_multiple() {
		
		System.out.println("---------------- Test Case select_caller_from_conference_with_multiple Started ------------------");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", Boolean.valueOf(true));
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", Boolean.valueOf(true));
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", Boolean.valueOf(true));
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", Boolean.valueOf(true));

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		
		//Opening setting page
		softPhoneSettingsPage.clickSettingIcon(driver6);
		
		//Setting local presence enable
		softPhoneSettingsPage.enableLocalPresenceSetting(driver6);
		
		//Making a call to qa agent 3 (Driver 3)
		softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_3_number"));
		
		//Picking up call from qa user
		softPhoneCalling.pickupIncomingCall(driver3);

		//Getting local presence number
		String number = callScreenPage.getCallerNumber(driver3);

		//Hanging up the call
		softPhoneCalling.hangupActiveCall(driver3);

		if(!callScreenPage.isCallerMultiple(driver3)) {
			
			//Adding caller as lead
			String date = helperFunctions.GetCurrentDateTime();
			callScreenPage.addCallerAsLead(driver3, date, "Metacube");

			//adding caller as multiple by updating 
			date = helperFunctions.GetCurrentDateTime();
			callScreenPage.updateContact(driver3, date, "Metacube");
			
			softPhoneContactsPage.searchUntilContactPresent(driver3, number);
			seleniumBase.idleWait(5);
			
		}

		//Making outbound call
		softPhoneCalling.softphoneAgentCall(driver3, number);
		
		//Picking up call from prod user
		softPhoneCalling.pickupIncomingCall(driver6);
		
		//Opeening team section 
		softPhoneTeamPage.openTeamSection(driver1);
		
		//Checking that call is visible to supervison
		softPhoneTeamPage.listenAgent(driver1, agentName);
		
		//monitoring call 
		softPhoneTeamPage.monitorAgent(driver1, agentName);
		
		//Stopping listening
		softPhoneTeamPage.stopMonitoring(driver1, agentName);
		
		//Hanging up with the call 
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver6);
		
		//Setting the driver 6 number is not coming as multiple
		softPhoneContactsPage.convertMultipleToSingle(driver3, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_3_number")));

		System.out.println("Test Case is Pass");
	}
	

	@AfterClass(groups={"Regression", "Sanity", "Product Sanity"})
	public void setDefault() {
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//removing call forwarding from driver 3
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		driverUsed.put("driver3", false);
	}

}
