/**
 * 
 */
package softphone.cases.callDashboard;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author admin
 *
 */
public class ListenAndMonitor extends SoftphoneBase{
	CallDashboardReact callDashboardReact = new CallDashboardReact();
	
	public void disableWebRtcSetting() {

		//Initializing drivers  
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", Boolean.valueOf(true));
		
		// open Support Page and disable call disposition prompt setting
		if (seleniumBase.getTabCount(webSupportDriver) == 1 || seleniumBase.getWindowHandleByTitle(webSupportDriver, "ringDNA Web") == null) {
			loginSupport(webSupportDriver);
		}
		
		if(!accountIntelligentDialerTab.isUserOnIntelligentDialerTab(webSupportDriver)) {
			dashboard.clickAccountsLink(webSupportDriver);
			System.out.println("Account editor is opened ");
			accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		}
				
		accountIntelligentDialerTab.disablewebRTCSetting(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);

		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", false);

	}
	
	//Verify Kebab with Monitor Option
	//Verify Success Toast for Monitoring
	//Verify user Clicking on the Stop Monitoring Button
	//Verify Monitoring another agent is disabled when already monitoring an agent
	@Test(priority=10501, groups={"Regression", "MediumPriority"})
	  public void verify_monitor_disabled() {
	    System.out.println("Test case --verify_monitor_disabled-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Open call dashboard page from support app
		loginSupport(callDashboardDriver);
		dashboard.openCallDashboardPage(callDashboardDriver);
		
		//Verify that kebab icon is visible for all users
		callDashboardReactPage.verifyAllKebabIconsVisible(callDashboardDriver);
		
		//Click on the monitor button for the agent
		callDashboardReactPage.clickMonitorForAvailableAgent(callDashboardDriver, CONFIG.getProperty("qa_admin_user_name"));
		
		//Verify the pop up message on clicking monitor button
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019re now monitoring " + CONFIG.getProperty("qa_admin_user_name") + ". You\u0027ll automatically listen in on all their calls.");
		
		//verify that popup message is invisible after 5 minutes
		seleniumBase.idleWait(5);
		assertFalse(callDashboardReactPage.isPopupMessageVisible(callDashboardDriver));
		seleniumBase.pressEscapeKey(callDashboardDriver);
		
		//Verify that the monitor button is disabled for other agents
		callDashboardReactPage.verifyMonitorDisabledForAvailableAgent(callDashboardDriver, CONFIG.getProperty("qa_user_2_name"));

		//click on stop monitor button
		callDashboardReactPage.clickStopMonitoringBtn(callDashboardDriver);
		
		//verify the pop up message when monitoring is stopped
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019ve stopped monitoring " + CONFIG.getProperty("qa_admin_user_name") + ".");
		
		//verify that popup message is invisible after 5 minutes
		seleniumBase.idleWait(5);
		assertFalse(callDashboardReactPage.isPopupMessageVisible(callDashboardDriver));
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Monitoring an agent is disabled when already listening in on a call
	@Test(priority=10502, groups={"MediumPriority"})
	  public void verify_monitor_disabled_when_listen_call() {
	    System.out.println("Test case --verify_monitor_disabled_when_listen_call-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    //Open call dashboard page from support app
		loginSupport(callDashboardDriver);
		dashboard.openCallDashboardPage(callDashboardDriver);
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		
		//click on listen button for agent
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//Verify that the monitor button is disabled for other agents
		String toolTipText = callDashboardReactPage.verifyMonitorDisabledForAvailableAgent(callDashboardDriver, CONFIG.getProperty("qa_user_3_name"));
		
		//verify that the tool tip for monitor button is appearing
		assertEquals(toolTipText, "You can\u2019t monitor an agent while listening to a call");
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver4);

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Monitoring an agent is disabled when already listening in on a call
	@Test(priority=10503, groups={"MediumPriority"})
	  public void verify_monitor_call_toast() {
	    System.out.println("Test case --verify_monitor_call_toast-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    
	    //Open call dashboard page from support app
		loginSupport(callDashboardDriver);
		dashboard.openCallDashboardPage(callDashboardDriver);
		
		String agentName = CONFIG.getProperty("qa_admin_user_name");
		
		//Click on the monitor button for the agent
		callDashboardReactPage.clickMonitorForAvailableAgent(callDashboardDriver, agentName);
		
		//Verify the pop up message on clicking monitor button
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019re now monitoring " + agentName + ". You\u0027ll automatically listen in on all their calls.");
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		String callerName = callScreenPage.getCallerName(adminDriver);
		
		//Verify the pop up message on clicking monitor button
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019re monitoring " + agentName + ". You\u2019re now joining their call.");
		 
		//Verify the connecting listen widget controls
		callDashboardReactPage.verifyListenConnectingWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"));
		
		//Verify the connected listen widget controls
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver6);

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Listening on a call is disabled when already monitoring an agent
	//Verify Monitor Widget at the bottom of the page
	//Verify Transition from the Listen in Widget to the Monitor Widget
	@Test(priority=10504, groups={"MediumPriority"})
	  public void verify_monitor_listen_widget_transition() {
	    System.out.println("Test case --verify_monitor_listen_widget_transition-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    
	    //Open call dashboard page from support app
		loginSupport(callDashboardDriver);
		dashboard.openCallDashboardPage(callDashboardDriver);
		
		String agentName = CONFIG.getProperty("qa_admin_user_name");
		
		//Click on the monitor button for the agent
		callDashboardReactPage.clickMonitorForAvailableAgent(callDashboardDriver, agentName);
		
		//Verify the pop up message on clicking monitor button
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019re now monitoring " + agentName + ". You\u0027ll automatically listen in on all their calls.");
		
		//verify monitor widget
		callDashboardReactPage.verifyMonitorWidget(callDashboardDriver, agentName, "Available");
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		String callerName = callScreenPage.getCallerName(adminDriver);
		
		//Verify the pop up message on clicking monitor button
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "You\u2019re monitoring " + agentName + ". You\u2019re now joining their call.");
		 
		//Verify the connecting listen widget controls
		callDashboardReactPage.verifyListenConnectingWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"));
		
		//Verify the connected listen widget controls
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//verify all listen buttons are disabled
		callDashboardReactPage.verifyAllListenBtnDisabled(callDashboardDriver, -1);
		
		//leave the listening call
		callDashboardReactPage.leaveListenCall(callDashboardDriver);
		
		//verify listening widget is changed to monitor widget now
		callDashboardReactPage.verifyMonitorWidget(callDashboardDriver, agentName, "On call");
		
		//click listen button
		callDashboardReactPage.clickMonitorWIdgetListenButton(callDashboardDriver);
		
		//Verify the connected listen widget controls
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver6);
		seleniumBase.idleWait(5);
		
		//verify listening widget is changed to monitor widget now
		callDashboardReactPage.verifyMonitorWidget(callDashboardDriver, agentName, "Available");

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Connecting state for the call widget
	@Test(priority=10505, groups={"MediumPriority"})
	  public void verify_listen_call_with_forwarding() {
	    System.out.println("Test case --verify_listen_call_with_forwarding-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
	    //Set call forwarding number for call dashboard driver
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver2, "forwarding number", CONFIG.getProperty("prod_user_1_number"));
	    
	    //setting call forwarding for agent
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver6, "forwarding number", CONFIG.getProperty("prod_user_3_number"));
	    
	    disableWebRtcSetting();
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_admin_user_number"));
		String callerName = callScreenPage.getCallerName(adminDriver);
		softPhoneCalling.pickupIncomingCall(driver6);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click unmute button
		callDashboardReactPage.clickUnmuteButton(callDashboardDriver);
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver6);
		
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		enableWebRtcSetting();

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Connecting state for the call widget with ACD Queue
	@Test(priority=10506, groups={"MediumPriority"})
	  public void verify_listen_acd_call() {
	    System.out.println("Test case --verify_listen_acd_call-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    
	    String agentName 	= CONFIG.getProperty("qa_user_2_name");
	    String queuNumber	= CONFIG.getProperty("qa_acd_group_1_number");
	 	String queueName	= CONFIG.getProperty("qa_acd_group_1_name");
	 	
	 	//subscribe queue for the agent	
	 	//softPhoneCallQueues.unSubscribeQueue(driver1, queueName);
	 	softPhoneCallQueues.subscribeQueue(driver4, queueName);
	 	//softPhoneCallQueues.unSubscribeQueue(driver3, queueName);
	    
	    //Set call forwarding number for call dashboard driver
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver5, "forwarding number", CONFIG.getProperty("prod_user_2_number"));
	    
	    softPhoneSettingsPage.clickSettingIcon(driver4);
	    softPhoneSettingsPage.setCallForwardingNumber(driver4, driver6, "forwarding number", CONFIG.getProperty("prod_user_3_number"));
	    
	    disableWebRtcSetting();
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver2, queuNumber);
		softPhoneCalling.pickupIncomingCall(driver6);
		String callerName = callScreenPage.getCallerName(driver4);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click unmute button
		callDashboardReactPage.clickUnmuteButton(callDashboardDriver);
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver2);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		enableWebRtcSetting();

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Connecting state for the call widget with Conference call
	@Test(priority=10507, groups={"MediumPriority"})
	  public void verify_listen_call_with_conference() {
	    System.out.println("Test case --verify_listen_call_with_conference-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
	    disableWebRtcSetting();
	    
	    //Set call forwarding number for call dashboard driver
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver2, "forwarding number", CONFIG.getProperty("prod_user_1_number"));
	    
	    //setting call forwarding for agent
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver6, "forwarding number", CONFIG.getProperty("prod_user_3_number"));
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(driver6);
		String callerName = callScreenPage.getCallerName(adminDriver);
		
		//Take one more call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(adminDriver);
		
		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(adminDriver);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(adminDriver);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(adminDriver);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 2);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click unmute button
		callDashboardReactPage.clickUnmuteButton(callDashboardDriver);
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.hangupActiveCall(driver1);
		
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		enableWebRtcSetting();

		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }

	//Verify Supervisor send note without call forwarding
	@Test(priority=10508, groups={"MediumPriority"})
	  public void verify_send_supervisor_notes() {
	    System.out.println("Test case --verify_send_supervisor_notes-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
	    
	    //Set call forwarding number for call dashboard driver
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver2, "forwarding number", CONFIG.getProperty("prod_user_1_number"));
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_admin_user_number"));
		String callerName = callScreenPage.getCallerName(adminDriver);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click send onte button
		callDashboardReactPage.hoverSendNoteButton(callDashboardDriver);
		callDashboardReactPage.clickSendNoteButton(callDashboardDriver);
		
		//verify supervisor notes are empty
		assertEquals(callDashboardReactPage.getSupervisorText(callDashboardDriver), "");
		callDashboardReactPage.verifySendBtnDisabled(callDashboardDriver);
		assertEquals(callDashboardReactPage.getSupervisorPlaceholderText(callDashboardDriver), "Add a note for the agent. Notes are emailed to the agent immediately, and a copy is saved to the Salesforce call activity.");
		
		//enter some supervisor notes
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, "Test Message");
		
		//verify remaining supervisor count limit
		assertEquals(callDashboardReactPage.getReaminingNotesCharacLimit(callDashboardDriver), "243");
		
		//generate 255 character long string and enter into the call notes subject
		String notesSubject = "Test";
		for (int i = 0; i < 7; i++) {
			notesSubject = notesSubject + HelperFunctions.GetRandomString(35);
		}	
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, notesSubject);
		
		//verify characters limit error
		assertEquals(callDashboardReactPage.getSupervisorNotesCharacLimitError(callDashboardDriver), "You\u2019ve reached the maximum number of characters.");
		
		//send valid supervisor notes
		callDashboardReactPage.clickCancelBtn(callDashboardDriver);
		callDashboardReactPage.clickSendNoteButton(callDashboardDriver);
		String supervisortext = "This is a test supervisor text";
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, supervisortext);
		callDashboardReactPage.clickSendBtn(callDashboardDriver);
		
		//verify the popup message for successful notes send
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "Your note was sent.");
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver5);
		
		//Verify supervisor notes in sfdc task
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(adminDriver);
		contactDetailPage.openRecentCallEntry(adminDriver, callSubject);
		String taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(adminDriver);
		assertEquals(supervisortext, taskSupervisorNotes.trim());
		seleniumBase.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Supervisor send a note with call forwarding with WebRTC disabled
	@Test(priority=10509, groups={"MediumPriority"})
	  public void verify_send_supervisor_notes_call_Forwarding() {
	    System.out.println("Test case --verify_send_supervisor_notes_call_Forwarding-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
	    disableWebRtcSetting();
	    
	    //Set call forwarding number for call dashboard driver
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver2, "forwarding number", CONFIG.getProperty("prod_user_1_number"));
	    
	    //setting call forwarding for agent
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver6, "forwarding number", CONFIG.getProperty("prod_user_3_number"));
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_admin_user_number"));
		String callerName = callScreenPage.getCallerName(adminDriver);
		softPhoneCalling.pickupIncomingCall(driver6);;
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click send onte button
		callDashboardReactPage.hoverSendNoteButton(callDashboardDriver);
		callDashboardReactPage.clickSendNoteButton(callDashboardDriver);
		
		//verify supervisor notes are empty
		assertEquals(callDashboardReactPage.getSupervisorText(callDashboardDriver), "");
		callDashboardReactPage.verifySendBtnDisabled(callDashboardDriver);
		assertEquals(callDashboardReactPage.getSupervisorPlaceholderText(callDashboardDriver), "Add a note for the agent. Notes are emailed to the agent immediately, and a copy is saved to the Salesforce call activity.");
		
		//enter some supervisor notes
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, "Test Message");
		
		//verify remaining supervisor count limit
		assertEquals(callDashboardReactPage.getReaminingNotesCharacLimit(callDashboardDriver), "243");
		
		//generate 255 character long string and enter into the call notes subject
		String notesSubject = "Test";
		for (int i = 0; i < 7; i++) {
			notesSubject = notesSubject + HelperFunctions.GetRandomString(35);
		}	
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, notesSubject);
		
		//verify characters limit error
		assertEquals(callDashboardReactPage.getSupervisorNotesCharacLimitError(callDashboardDriver), "You\u2019ve reached the maximum number of characters.");
		
		//send valid supervisor notes
		callDashboardReactPage.clickCancelBtn(callDashboardDriver);
		callDashboardReactPage.clickSendNoteButton(callDashboardDriver);
		String supervisortext = "This is a test supervisor text";
		callDashboardReactPage.SetSuperVisorText(callDashboardDriver, supervisortext);
		callDashboardReactPage.clickSendBtn(callDashboardDriver);
		
		//verify the popup message for successful notes send
		assertEquals(callDashboardReactPage.getPopupMessage(callDashboardDriver), "Your note was sent.");
		
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver5);
		
		//Verify supervisor notes in sfdc task
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(adminDriver);
		contactDetailPage.openRecentCallEntry(adminDriver, callSubject);
		String taskSupervisorNotes = sfTaskDetailPage.getSuperVisorNotes(adminDriver);
		assertEquals(supervisortext, taskSupervisorNotes.trim());
		seleniumBase.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		enableWebRtcSetting();
	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Connecting state for the call widget with WebRTC enabled
	@Test(priority=10510, groups={"MediumPriority"})
	  public void verify_listen_call_with_webrtc_enabled() {
	    System.out.println("Test case --verify_listen_call_with_webrtc_enabled-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    enableWebRtcSetting();
	    
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
	    //setting call forwarding for agent
	    seleniumBase.switchToTab(callDashboardDriver, 1);
	    softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(callDashboardDriver, driver2, "forwarding number", CONFIG.getProperty("prod_user_1_number"));
	    
	    //Open call dashboard page from support app
		callDashboardReact.openCallDashBoardPage();
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_admin_user_number"));
		String callerName = callScreenPage.getCallerName(adminDriver);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
		callDashboardReactPage.clickListenButton(callDashboardDriver, 0);
		 
		//pickup call from call forwarding device of call dashboard driver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify listeing widget for the listen call
		callDashboardReactPage.verifyListeningWidget(callDashboardDriver, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), "1");
		
		//click unmute button
		callDashboardReactPage.clickUnmuteButton(callDashboardDriver);
		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver6);
		String agentName2 = CONFIG.getProperty("qa_user_2_name");
		String callerName2 = callScreenPage.getCallerName(driver4);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 2);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 2, agentName2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), true);
		
	    //verify all listen buttons are disabled
	  	callDashboardReactPage.verifyAllListenBtnDisabled(callDashboardDriver, 0);
	  	
	  	//click leave button
	  	callDashboardReactPage.clickLeaveButton(callDashboardDriver);
	  	
	  	//verify all listen button enable
	  	callDashboardReactPage.verifyAllListenBtnEnabled(callDashboardDriver);
	  	
		//hangup the call
	    softPhoneCalling.hangupActiveCall(driver6);
		softPhoneCalling.hangupActiveCall(driver5);
		
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify users without a smart number from accessing the dashboard
	@Test(priority=10511, groups={"MediumPriority"})
	  public void verify_no_smart_number_supervisor() {
	    System.out.println("Test case --verify_no_smart_number_supervisor-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

	    //login to Call Dashboard Page directly and verify logged into right page
  		WebDriver noSmartNumberDriver = getDriver();
  		SFLP.supportLogin(noSmartNumberDriver, CONFIG.getProperty("call_dashboard_react_url"), CONFIG.getProperty("no_smart_no_user_username"), CONFIG.getProperty("no_smart_no_user_password")); 
	  		
		//make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		
		//click on listen button for agent
		callDashboardReactPage.verifyActiveCallHeading(noSmartNumberDriver, 1);
		callDashboardReactPage.clickListenBtn(noSmartNumberDriver, 0);
		
		//get popup message for no smart number
		assertEquals(callDashboardReactPage.getPopupMessage(noSmartNumberDriver), "Sorry, you need a smart number to perform this action. Please contact your ringDNA administrator.");
	  	
		//hangup the call
		softPhoneCalling.hangupActiveCall(driver2);
		
		//quit the driver
		noSmartNumberDriver.quit();
		noSmartNumberDriver = null;
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver6", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@AfterMethod(groups={"Regression", "MediumPriority", "Product Sanity"}) 
	public void afterMethod(ITestResult result) {
		if(result.getStatus() == 3 ||	 result.getStatus() == 2) {
			enableWebRtcSetting();
		}
	}
	
	public void enableWebRtcSetting() {
		
		//Initializing drivers  
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", Boolean.valueOf(true));
		
		//Login to support 
		if (seleniumBase.getTabCount(webSupportDriver) == 1 || seleniumBase.getWindowHandleByTitle(webSupportDriver, "ringDNA Web") == null) {
			loginSupport(webSupportDriver);
		}
		
		if(!accountIntelligentDialerTab.isUserOnIntelligentDialerTab(webSupportDriver)) {
			dashboard.clickAccountsLink(webSupportDriver);
			System.out.println("Account editor is opened ");
			accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		}
		accountIntelligentDialerTab.enablewebRTCSetting(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);

		//Setting up driver used to false state
		driverUsed.put("webSupportDriver", false);
	}
	
	@AfterClass(groups={"Regression", "MediumPriority", "Product Sanity"}) 
	public void disableCallForwarding() {
		
		//Initializing drivers  
		initializeDriverSoftphone("callDashboardDriver");
		driverUsed.put("callDashboardDriver", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//disable call forwarding for agent
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//disable call forwarding for call dashboard
		seleniumBase.switchToTab(callDashboardDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(callDashboardDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(callDashboardDriver);
		seleniumBase.switchToTab(callDashboardDriver, 2);
		
		//Setting up driver used to false state
		driverUsed.put("adminDriver", false);
		driverUsed.put("driver4", false);
		driverUsed.put("callDashboardDriver", false);
	}
}