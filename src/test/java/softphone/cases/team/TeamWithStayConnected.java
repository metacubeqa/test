package softphone.cases.team;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class TeamWithStayConnected extends SoftphoneBase{
	
	@BeforeClass(groups={"Sanity", "Regression", "QuickSanity", "Product Sanity"})
	public void beforeClass(){
	// updating the driver used
	initializeDriverSoftphone("driver1");
	driverUsed.put("driver1", true);
	initializeDriverSoftphone("driver2");
	driverUsed.put("driver2", true);
	
	// Setting call forwarding ON
	System.out.println("Setting call forwarding ON");
	softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
	
	//enable stay connected setting
	softPhoneSettingsPage.enableStayConnectedSetting(driver1);
	
	// updating the driver used
	driverUsed.put("driver1", false);
	driverUsed.put("driver2", false);
	}

	
	
	
	@Test(priority=401, groups={"Regression"}) 
	public void o_supervisor_disconnect_listening_agent_call_with_stay_connected() {
	
		System.out.println("---------------- Test Case o_supervisor_disconnect_listening_agent_call_with_stay_connected() Started ------------------");

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

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//clicking on listen button for agent's call
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//supervisor disconnecting the call
		softPhoneTeamPage.disconnectListening(driver1, agentName);
		
		//Waiting for 5 sec and checking that call is not disconnected from call forwarding device 
		seleniumBase.idleWait(5);
		
		//verifying that call is not hangup from call forwarding device 
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//verifying agent still on call and listen button is visible on team section 
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//Hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
		
	}
	
	@Test(priority=402, groups={"Regression"})
	public void o_supervisor_switching_listening_calls_with_stay_connected() {

		
		System.out.println("---------------- Test Case o_supervisor_switching_listening_calls_with_stay_connected() Started ------------------");

		String dateTime = null;
		
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
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);

		//Getting agent's name
		String agentName = CONFIG.getProperty("qa_user_3_name").trim();
		String agentName1 = CONFIG.getProperty("qa_user_2_name").trim();

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

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

		//Selecting caller if showing as multiple
		callScreenPage.selectFirstContactFromMultiple(driver4);

		//Adding if caller is showing as unknown
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
		
		//Listening another call
		softPhoneTeamPage.clickListen(driver1, agentName1);
		
		//verifying listening started
		softPhoneTeamPage.verifyListening(driver1, agentName1);
		
		//verifying call forwarding call is showing as connected on call
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//Verifying that supervisor is disconnected from first user
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);

		//giving supervisor notes 
		dateTime = helperFunctions.GetCurrentDateTime();
		softPhoneTeamPage.addSupervisorNotes(driver1, agentName1, dateTime);
		
		//Agent hanging up own call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver6);
		
		//verifying that agents call is removed from team 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName1);
	
		//waiting for 5 sec to check that call forwarding device is not getting disconnected
		seleniumBase.idleWait(5);
		
		//verifying that call forwarding device is still showing on call
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//verifying that agents call is still visible 
		softPhoneTeamPage.verifyListenButtonVisible(driver1, agentName);
		
		//Hanging up with all the calls
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//Verifying calls are removed from superviosr team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);

		//waiting for 20 more second to check call forwarding device is not disconnected  
		seleniumBase.idleWait(20);
		
		//Verifying that call forwarding device is still showing on call
		softPhoneCalling.isHangUpButtonVisible(driver2);
	
		//verifying after 45 more seconds call is getting removed from call forwarding device  
		seleniumBase.idleWait(45);
		
		//Verifying Call forwarding device is disconnecte 
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
	
	@Test(priority=403, groups={"Regression", "Sanity", "Product Sanity"})
	public void o_supervisor_auto_connect_with_monitor_enable_and_agent_come_on_Call() {

		System.out.println("---------------- Test Case o_supervisor_auto_connect_with_monitor_enable_and_agent_come_on_Call() Started ------------------");
		
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

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at participant 
		System.out.println("Picking up call from participant");
		softPhoneCalling.pickupIncomingCall(driver5);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//clicking on monitor button for agent's call
		softPhoneTeamPage.clickMonitor(driver1, agentName);
		
		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying that monitoring is started
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
		
		//hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//Waiting for 5 sec and checking that call is not disconnected from call forwarding device 
		seleniumBase.idleWait(5);
		
		//verifying that call is not hangup from call forwarding device 
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//Making outbound call from agent3
		System.out.println("Making outbound call from agent 3");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_2_number"));

		//receiving call at participant
		System.out.println("Picking up call from participant");
		softPhoneCalling.pickupIncomingCall(driver5);

		//verifying call auto connected in team
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
		
		//verifying that call forwarding device is showing as connected on call
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//Hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
	
		//Stop monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);
		
		//Hanging up from call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}
	
	@Test(priority=404, groups={"Regression"})	
	public void o_listen_agents_call_forwarding_call_with_stay_connected() {

		System.out.println("---------------- Test Case o_listen_callforwarding_call_with_callforwarding Started ------------------");
		
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

		//Verifying that call forwarding device is connected
		seleniumBase.idleWait(5);
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//Hanging up with the call forwarding device 
		softPhoneCalling.hangupActiveCall(driver2);
		
		//removing call forwarding from driver 3
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		System.out.println("Test Case is Pass");	

	}
	
	@Test(priority=405, groups={"Regression", "Failing"})
	public void i_monitor_agent_inbound_call_with_monitor_enable_and_agent_come_on_Call() {

		System.out.println("---------------- Test Case o_supervisor_auto_connect_with_monitor_enable_and_agent_come_on_Call() Started ------------------");
		
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

		//Taking inbound call from participant 
		System.out.println("Taking inbound call from participant");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Opening team section of supervisor 
		softPhoneTeamPage.openTeamSection(driver1);

		//Verifying agent added in team section 
		softPhoneTeamPage.verifyAgentPresent(driver1, agentName);

		//clicking on listen button for agent's call
		softPhoneTeamPage.clickListen(driver1, agentName);
		
		//Picking up call from call forwarding device 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verifying that listening is started
		softPhoneTeamPage.verifyListening(driver1, agentName);
		
		//hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);

		//verifying call is removed from team section 
		softPhoneTeamPage.verifyTeamCallRemoved(driver1, agentName);
		
		//verifying that call is not hangup from call forwarding device 
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//enabling monitor for agent 
		softPhoneTeamPage.clickMonitor(driver1, agentName);
		
		//Taking inbound call from participant 
		System.out.println("Taking inbound call from participant");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_3_number"));

		//receiving call at agent 
		System.out.println("Picking up call from agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//verifying call auto connected in team
		softPhoneTeamPage.verifyMonitoring(driver1, agentName);
		
		//verifying that call forwarding device is showing as connected on call
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//Hanging up from agent side
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver5);
		
		//verify call is removed from team
		softPhoneTeamPage.verifyListenButtonNotVisible(driver1, agentName);
	
		//Stop monitoring 
		softPhoneTeamPage.stopMonitoring(driver1, agentName);
		
		//Hanging up from call forwarding device 
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver5", false);
		
		System.out.println("Test Case is Pass");
	}

	
	@AfterClass(groups={"Sanity", "Regression", "QuickSanity", "Product Sanity"})
	public void afterClass(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//disable stay connected setting
		softPhoneSettingsPage.disableStayConnectedSetting(driver1);
		
		// updating the driver used
		driverUsed.put("driver1", false);
	}
	
}
