/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallFowarding extends SoftphoneBase {

	@BeforeClass(groups = { "Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity" })
	public void beforeClass() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	
	@BeforeClass(groups = {"Regression", "ExludeForProd", "MediumPriority", "Product Sanity"})
	 public void disableConferenceSetting(){
	
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//navigating to support page 
		loginSupport(driver1);
		
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		
		//disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableConferenceSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	  }
	
	@Test(groups = { "Sanity", "Regression"})
	public void inbound_calling_with_forwarding() {
		System.out.println("Test case --inbound_calling_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
	    //verify data for inbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "Sanity", "Regression"})
	public void inbound_calling_forwarding_device_ends() {
		System.out.println("Test case --inbound_calling_forwarding_device_ends-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with the forwarding device
		System.out.println("hanging up with the forwarding device");
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// call is removed from caller agent
		System.out.println("call is removed from caller agent");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	

	@Test(groups = { "Sanity", "Regression"})
	public void outbound_calling_with_forwarding() {
		System.out.println("Test case --outbound_calling_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//get caller Name
		String callerName = callScreenPage.getCallerName(driver1);
		String callerPhone = callScreenPage.getCallerNumber(driver1);
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Reload softphone and verify that user is on call screen page
		reloadSoftphone(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), callerName);
		assertEquals(callScreenPage.getCallerNumber(driver1), callerPhone);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
	    //verify data for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}	
	
	@Test(groups = { "Sanity", "Regression"})
	public void inbound_calling_with_forwarding_logout() {
		System.out.println("Test case --inbound_calling_with_forwarding_logout-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//Logging out from softphone
		softPhoneSettingsPage.logoutSoftphone(driver1);

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
		
	    //verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity", "Regression", "Product Sanity"})
	public void calling_with_forwarding_and_local_presence() {
		System.out.println("Test case --calling_with_forwarding_and_local_presence-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);

	    //Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(CONFIG.getProperty("qa_user_2_number").substring(2, 5) ,localPresenceNumber.substring(0,3));

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify data for inbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verifying call back on local presence number functionality
	    //taking incoming call
	    System.out.println("Taking call from twilio");
	    softPhoneCalling.softphoneAgentCall(driver4, localPresenceNumber);

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//verify that source is ringDNA
		assertEquals(callScreenPage.getCallerSource(driver1), "RingDNA");
	  	seleniumBase.idleWait(3);
	  	  	 
	  	//hanging up with caller 1
	  	System.out.println("hanging up with caller 1");
	  	softPhoneCalling.hangupActiveCall(driver4);
	  	    
	  	//Call is removing from softphone
	  	System.out.println("Call is removing from softphone");
	  	softPhoneCalling.isCallBackButtonVisible(driver1);
	  	
		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
	  	    
	  	//verify data for inbound call
	  	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	  	callScreenPage.openCallerDetailPage(driver1);
	  	contactDetailPage.openRecentCallEntry(driver1, callSubject);
	  	sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	  	sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	  	assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	  	assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	  	assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1)," ");
	  	seleniumBase.closeTab(driver1);
	  	seleniumBase.switchToTab(driver1, 1);
	    
	  	//Disable Local Presence Setting
	  	softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity"})
	public void call_transfer_with_forwarding() {
		System.out.println("Test case --call_transfer_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	    
	    //Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
	    
		//receiving call from app softphone
	    System.out.println("first called agent picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

	    //Transferring the call to other agent
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_3_name"));
	    
		//receiving call from app softphone
	    System.out.println("tranfered called agent picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// Call is removing from softphone
		System.out.println("Call is removing from transfered number");
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		 //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    //enter call Notes
		callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity" })
	public void inbound_outbound_merge_wtih_forwarding() {
		System.out.println("Test case --inbound_outbound_merge_wtih_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));

		// picking up second call
		System.out.println("picking up call from participant 2");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);
		String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeWithoutLoop(driver1);

		// verifying that conference button is visible
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		// Hanging up call from called agent
		softPhoneCalling.hangupActiveCall(driver3);

		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver1);

		// verifying that participant 2 is removed
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		 //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity", "Regression"})
	public void drop_voicemail_with_forwarding() {
		System.out.println("Test case --drop_voicemail_with_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//dropping Voice Mail
	    String usedVoiceMail = callToolsPanel.dropFirstVoiceMail(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Call is removing from called agent
		System.out.println("Call is removing from called agent");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
	    //verify data for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.verifyVoiceMalDroppped(driver1);
	    assertTrue(sfTaskDetailPage.getVoicemailUsed(driver1).contains(usedVoiceMail));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Sanity", "Regression"})
	public void call_forwarding_to_extension() {
		System.out.println("Test case --call_forwarding_to_extension-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver4, "", CONFIG.getProperty("qa_user_2_extensiont_number"));

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver3);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver3));
		softPhoneCalling.isDialPadButtonInvisible(driver3);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// verify data for inbound call
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver3);
		sfTaskDetailPage.verifyCallStatus(driver3, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
		sfTaskDetailPage.isCreatedByRingDNA(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups = { "Regression", "Product Sanity"})
	public void call_forwarding_with_hold_resume() {
		System.out.println("Test case --call_forwarding_with_hold_resume-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		softPhoneCalling.clickHoldButton(driver1);
			
	    //Putting call on hold and verify that call forwarding line is still connected
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
	    //getting misse call count
	  	int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    
	    // Taking another incoming call
 		System.out.println("Taking another call to agent");
 		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
 		
		//verifying that call is not received at agents phone
		System.out.println("verifying that call is not received at agents phone");
		softPhoneCalling.idleWait(5);
		softPhoneCalling.verifyAdditionalCallIsInvisible(driver1);
		
		//disconnect additional call from the caller
		System.out.println("disconnect additional call from the caller");
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver5);

		//verify that missed call count increases for agent
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount + 1);
		
	    //resuming call and verifying that call is still connected
		softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
	  	//verify that no new call is appearing on forwarding device
	  	softPhoneCalling.isHangUpButtonVisible(driver2);
	  	softPhoneCalling.verifyAdditionalCallIsInvisible(driver2);
	  	
		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void call_forwarding_end_call_while_on_hold() {
		System.out.println("Test case --call_forwarding_with_hold_resume-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		softPhoneCalling.clickHoldButton(driver1);
		
	    //Putting call on hold and verify that call forwarding line is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.onHoldButtonIsInvisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
	    //verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void call_forwarding_call_and_merge_outbound_call() {
		System.out.println("Test case --call_forwarding_call_and_merge_outbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		
	    //Putting call on hold and verify that call forwarding line is still connected
		softPhoneCalling.clickHoldButton(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    callScreenPage.verifyUserImageBusy(driver1);
	    
		// Make outgoing call to another user
		System.out.println("Make outgoing call to another user");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver4);

	  	//verify that no new call is appearing on forwarding device
	  	softPhoneCalling.isHangUpButtonVisible(driver2);
	  	softPhoneCalling.verifyAdditionalCallIsInvisible(driver2);
		
		//checking that first call go on hold
	    softPhoneCalling.isCallOnHold(driver1);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//verify that hold, mute and transfer buttons are disabled
		assertFalse(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		assertFalse(softPhoneCalling.isTransferButtonEnable(driver1));
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		
		//Number of additional caller should be +1
		assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
		
		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void inbound_inbound_merge_call_forwarding_agent_leave() {
		System.out.println("Test case --inbound_inbound_merge_call_forwarding_agent_leave-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
	    
		// taking another incoming call to agent user
		System.out.println("taking another incoming call to agent user");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void inbound_inbound_resume_merge_call_forwarding() {
		System.out.println("Test case --inbound_inbound_resume_merge_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
	    
		// taking another incoming call to agent user
		System.out.println("taking another incoming call to agent user");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
	    //resuming call and verifying that call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
		
	  	//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity"})
	public void outbound_outbound_resume_merge_call_forwarding() {
		System.out.println("Test case --outbound_outbound_resume_merge_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Making first outgoing call
		System.out.println("Making first outgoing call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from caller 1
		System.out.println("Receiving call from caller 1");
		softPhoneCalling.pickupIncomingCall(driver3);

		// Making second outgoing call
		System.out.println("Making second outgoing call");
		softPhoneCalling.clickHoldButton(driver1);
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// Receiving call from 2nd caller
		System.out.println("Receiving call from 2nd caller");
		softPhoneCalling.pickupIncomingCall(driver4);
		
	    //resuming call and verifying that call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
		
	  	//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_inbound_second_caller_leave_call_forwarding() {
		System.out.println("Test case --outbound_inbound_second_caller_leave_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Making first outgoing call
		System.out.println("Making first outgoing call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from caller 1
		System.out.println("Receiving call from caller 1");
		softPhoneCalling.pickupIncomingCall(driver3);

		// taking second call as inbound
		System.out.println("taking second call as inbound");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from 2nd caller
		System.out.println("Receiving call from 2nd caller");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.hangupActiveCall(driver4);
		
		//Verify that forwarding device is disconnected
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	   
	  	//resuming call and verifying that call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_outbound_merge_call_forwarding() {
		System.out.println("Test case --outbound_outbound_merge_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//enable setting to display user's number on forwarding device
		loginSupport(driver1);
		dashboard.clickOnUserProfile(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.enableCallForwardingUsingSmartNumber(driver1);
		userIntelligentDialerTab.saveAcccountSettingsDialer(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);															
		
		//Making first outgoing call
		System.out.println("Making first outgoing call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from caller 1
		System.out.println("Receiving call from caller 1");
		softPhoneCalling.pickupIncomingCall(driver3);
  
		//verify that users number is appearing on forwarding device
		assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))));															  
																																				  
		// Making second outgoing call
		System.out.println("Making second outgoing call");
		softPhoneCalling.clickHoldButton(driver1);
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// Receiving call from 2nd caller
		System.out.println("Receiving call from 2nd caller");
		softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))),"call is not resumed");
		
	  	//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		//Verifying that tranfer and hold buttons are disabled during merge
	    assertFalse(softPhoneCalling.isTransferButtonEnable(driver1));
		assertFalse(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
  
		//enable setting to display user's number on forwarding device
		loginSupport(driver1);
  		dashboard.clickOnUserProfile(driver1);
  		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		userIntelligentDialerTab.disableCallForwardingUsingSmartNumber(driver1);
  		userIntelligentDialerTab.saveAcccountSettingsDialer(driver1);
  		seleniumBase.closeTab(driver1);
  		seleniumBase.switchToTab(driver1, 1);
  		reloadSoftphone(driver1);															 
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_inbound_hold_resume_after_caller_leave_call_forwarding() {
		System.out.println("Test case --outbound_inbound_hold_resume_after_caller_leave_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Making first outgoing call
		System.out.println("Making first outgoing call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from caller 1
		System.out.println("Receiving call from caller 1");
		softPhoneCalling.pickupIncomingCall(driver3);

		// taking second call as inbound
		System.out.println("taking second call as inbound");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		callScreenPage.verifyUserImageBusy(driver1);
		
		//checking that 2nd call appear on softphone
		softPhoneCalling.verifyAdditionalCall(driver1);

		// Receiving call from 2nd caller
		System.out.println("Receiving call from 2nd caller");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//Verify that call is on hold
		softPhoneCalling.isCallOnHold(driver1);
		
		//forwarding line should be open
	  	softPhoneCalling.isHangUpButtonVisible(driver2);
	  	
	  	//checking that 2nd caller number is visible
  	  	assertTrue(CONFIG.getProperty("qa_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
		
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
	    //verify that on hold button is removed
	    softPhoneCalling.onHoldButtonIsInvisible(driver1);
	    
		//verify that hold, mute and transfer buttons are disabled
		assertFalse(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		assertFalse(softPhoneCalling.isTransferButtonEnable(driver1));
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		
		//Number of additional caller should be +1
		assertEquals(callScreenPage.getNumberOfCallers(driver1), "1");
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		
		//Verifying that transfer and hold buttons are disabled during merge
		seleniumBase.idleWait(5);
	    assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
	   
	  	//putting call on hold, verify forwarding line is connected and user image is busy
	    softPhoneCalling.clickHoldButton(driver1);
	  	softPhoneCalling.isHangUpButtonVisible(driver2);
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //resuming call and verify call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
		
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))),"call is not resumed");
	  	
		//verify forwarding line is connected
	  	softPhoneCalling.isHangUpButtonVisible(driver2);
	  	
		// hanging up from agent
	  	seleniumBase.idleWait(4);
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void inbound_inbound_connect_call_after_disconnect() {
		System.out.println("Test case --inbound_inbound_connect_call_after_disconnect-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Making first incoming call
		System.out.println("Making first incoming call");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// taking second call as inbound
		System.out.println("taking second call as inbound");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
	    //resuming call and verify call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
	    //checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButton(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		// disconnect call from second caller
		System.out.println("disconnect call from second caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	  	
		// taking second call as inbound
		System.out.println("taking second call as inbound");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
	  	
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// hanging up from first caller
		System.out.println("hanging up from first caller");
		softPhoneCalling.hangupActiveCall(driver3);
		
		//hangup fpm forwarding device
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "Product Sanity"})
	  public void call_queue_call_forwarding()
	  {
	    System.out.println("Test case --call_queue_call_forwarding()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_call_flow_call_queue_smart_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Opening the caller detail page
		callScreenPage.openCallerDetailPage(driver1);
	
		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);

		//verify call data
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallQueue(driver1), queueName + " - " + CONFIG.getProperty("qa_call_flow_call_queue")); 
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void call_forwarding_with_outbound_number()
	  {
	    System.out.println("Test case --call_forwarding_with_outbound_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String outboundNumber = CONFIG.getProperty("prod_user_1_number");
	    
	    //Configuring driver 2's number as outbound number
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver2, outboundNumber, "Prod outbound Number");
	    
	    //verifying that number has been configured as outbound number
	    int addedOutboundNumberIndex = softPhoneSettingsPage.isNumberExistInOutboundNumber(driver1, outboundNumber);
	    assertTrue(addedOutboundNumberIndex >=0);
	    
	    //Selecting the addedOutboundNumber
	    softPhoneSettingsPage.selectOutboundNumberUsingIndex(driver1, addedOutboundNumberIndex);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Verify that outbound number appearing on Outbound Number tab is correct
	    System.out.println("verifying that call is going from outbound number");
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver1)));
	    
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //Verify that outbound number appearing on receiver end
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver3)));
	    
	    //hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//verify data for first Outbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallIsNotLocalPresence(driver1);
	    assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1)," ");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    softPhoneSettingsPage.deleteOutboundNumberIfExist(driver1, outboundNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }

	
	@Test(groups = { "Regression"})
	public void call_forwarding_inbound_transfer_to_user() {
		System.out.println("Test case --call_forwarding_inbound_transfer_to_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
		
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		assertFalse(contactDetailPage.isActivityInvisible(driver1, callSubject2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void call_forwarding_inbound_transfer_to_user_group() {
		System.out.println("Test case --call_forwarding_inbound_transfer_to_user_group()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver4, CONFIG.getProperty("qa_group_1_name"));
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
		assertTrue(softPhoneCalling.isTransferButtonEnable(driver1));
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(driver1));
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);

		//tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_group_1_name"));
	    
		//receiving call from app softphone
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    softPhoneCalling.isHangUpButtonVisible(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupIfInActiveCall(driver4);
		
	    //verify data for inbound call
	    seleniumBase.idleWait(1);
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void call_outbound_forwarding_transfer_to_user() {
		System.out.println("Test case --call_outbound_forwarding_transfer_to_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		softPhoneCalling.pickupIncomingCall(driver3);
		String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//reload softphone
		reloadSoftphone(driver1);
		
		//verify call is still connected
		softPhoneCalling.isHangUpButtonVisible(driver3);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
		
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		assertTrue(contactDetailPage.isActivityInvisible(driver1, callSubject2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void call_forwarding_outbound_transfer_to_user_group() {
		System.out.println("Test case --call_forwarding_outbound_transfer_to_user_group-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver4, CONFIG.getProperty("qa_group_1_name"));		

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		softPhoneCalling.pickupIncomingCall(driver3);

		  //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_group_1_name"));
	    
		//receiving call from app softphone
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.pickCallFromQueue(driver4);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		//hanging up with caller 2
	    System.out.println("hanging up with caller 2");
	    softPhoneCalling.hangupActiveCall(driver4);
		
	    //verify data for inbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject2);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //hangup calls
	    softPhoneCalling.hangupIfInActiveCall(driver4);
	    softPhoneCalling.hangupIfInActiveCall(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void call_forwarding_to_new_number() {
		System.out.println("Test case --call_forwarding_to_new_number-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Setting call forwarding ON
		System.out.println("Deleting and Setting call forwarding ON");
		softPhoneSettingsPage.deleteAllCallForwardingNumbers(driver3);
		softPhoneSettingsPage.validateBlankCallForwardingEnable(driver3);
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver4, "", CONFIG.getProperty("qa_user_2_number"));

		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver4);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver3);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver3));
		softPhoneCalling.isDialPadButtonInvisible(driver3);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// verify data for inbound call
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver3);
		sfTaskDetailPage.verifyCallStatus(driver3, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
		sfTaskDetailPage.isCreatedByRingDNA(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		
		//Delete all forwarding numbers
		softPhoneSettingsPage.deleteAllCallForwardingNumbers(driver3);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"Regression"})
	public void call_forwarding_additional_number() {
		System.out.println("Test case --call_forwarding_additional_number-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String additionalNumber = softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 1);
		
		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		//Verify that additional number appearing on receiver end
	    assertTrue(additionalNumber.contains(callScreenPage.getOutboundNumber(driver3)));
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);

		//Verify that additional number appearing on receiver end
	    assertTrue(additionalNumber.contains(callScreenPage.getOutboundNumber(driver3)));
	    
		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//@Test(groups = { "Regression"})
	public void call_forwarding_agent_logout() {
		System.out.println("Test case --call_forwarding_agent_logout-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumberDisableOnLogout(driver3, driver4, "", CONFIG.getProperty("qa_user_2_number"));

		//Logging out from softphone
		seleniumBase.idleWait(1);
		softPhoneSettingsPage.logoutSoftphone(driver3);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		seleniumBase.idleWait(10);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		//loggin again on the softphone
		SFLP.softphoneLogin(driver3, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
				
		//verify data for call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
		softPhoneActivityPage.playTaskRecording(driver3, callSubject);
		contactDetailPage.openRecentCallEntry(driver3, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver3);
		sfTaskDetailPage.verifyCallStatus(driver3, "Missed");
		assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Inbound");
		sfTaskDetailPage.verifyVoicemailCreatedActivity(driver3);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
		sfTaskDetailPage.isCreatedByRingDNA(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}

	@Test(groups = { "Regression"})
	public void verify_call_forwarding_number_label() {
		System.out.println("Test case --verify_call_forwarding_number_label-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		String fwdNumberLabel	= "NewForwardingNumberLabel";
		String fwdNumber		= CONFIG.getProperty("prod_user_3_number");

		// add a new forwarding number with a label
		softPhoneSettingsPage.deleteAllCallForwardingNumbers(driver3);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driver3);
		softPhoneSettingsPage.addNewForwardingNumber(driver3, driver6, fwdNumberLabel, fwdNumber, false);

		// edit call flow number and verify that label is correct
		System.out.println("Setting call forwarding OFF");
		seleniumBase.idleWait(1);
		softPhoneSettingsPage.editCallForwardingNumbers(driver3, fwdNumber);
		assertEquals(softPhoneSettingsPage.getForwardingNumberLabel(driver3), fwdNumberLabel);
		softPhoneSettingsPage.cancelCallForwardingModal(driver3);
		
		//delete all call forwarding number
		softPhoneSettingsPage.deleteAllCallForwardingNumbers(driver3);

		// updating the driver used
		driverUsed.put("driver3", false);
		driverUsed.put("driver6", false);
	}
	
	@Test(groups = { "MediumPriority"})
	public void inbound_merge_call_forwarding_addition_call_vm() {
		System.out.println("Test case --inbound_merge_call_forwarding_addition_call_vm-- started ");

		// updating the driver used
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
		
		//getting misse call count
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
	    
		// taking another incoming call to agent user
		System.out.println("taking another incoming call to agent user");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		// Taking another call ib agents phone
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		
		//verifying that call is not received at agents phone
		System.out.println("verifying that call is not received at agents phone");
		softPhoneCalling.idleWait(5);
		softPhoneCalling.verifyAdditionalCallIsInvisible(driver1);
		
		//disconnect additional call from the caller
		System.out.println("disconnect additional call from the caller");
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		//verify that missed call count increases for agent
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount + 1);
		
		//Verify that call is still connected on forwarding device
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		 //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void inbound_merge_call_forwarding_try_another_call() {
		System.out.println("Test case --inbound_merge_call_forwarding_try_another_call-- started ");

		// updating the driver used
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
		
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));
		
		//enable setting to display user's number on forwarding device
		loginSupport(driver1);
		dashboard.clickOnUserProfile(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.enableCallForwardingUsingSmartNumber(driver1);
		userIntelligentDialerTab.saveAcccountSettingsDialer(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//make a call to agent from an agent to use its entry to make additional call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver5);
		
		//verify that users number is appearing on forwarding device
		assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))));
		
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);

		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		softPhoneCalling.isHangUpButtonVisible(driver1);
		assertFalse(softPhoneCalling.isMuteButtonEnables(driver1));
		softPhoneCalling.isDialPadButtonInvisible(driver1);
	    
		// taking another incoming call to agent user
		System.out.println("taking another incoming call to agent user");
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		// Receiving call from agent
		System.out.println("Receiving call from agent");
		softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(driver1);
		
		//checking that first call go on hold and forwarding device is still connected
	    softPhoneCalling.isCallOnHold(driver1);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
	    
		//checking hold call actions button resume and merge
	    softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);
	    
		//merging the second call with the first call
	    softPhoneCalling.clickMergeWithoutLoop(driver1);
	    
		// try making call from the call history
		System.out.println("Open caller detail from history");
		softphoneCallHistoryPage.openRecentContactCallHistoryEntryByIndex(driver1, 2);
		softPhoneCalling.clickCallBackButton(driver1);
		assertEquals(callScreenPage.getErrorText(driver1), "Cannot dial. You have reached the maximum number of conference participants");
		callScreenPage.closeErrorBar(driver1);
		
		// try making call from contact search page
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("prod_user_2_name"));
		softPhoneContactsPage.clicksfdcResultsCallButton(driver1, 0, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		assertEquals(callScreenPage.getErrorText(driver1), "Cannot dial. You have reached the maximum number of conference participants");
		callScreenPage.closeErrorBar(driver1);
		
		//Verify that call is still connected on forwarding device
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneCalling.isHangUpButtonVisible(driver2);
	       
		// hanging up from agent
		System.out.println("hanging up from agent");
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller 1
		System.out.println("Call is removed from caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Call is removed from caller 2
		System.out.println("Call is removed from caller 2");
		softPhoneCalling.isCallBackButtonVisible(driver3);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		 //verify data for vm dropped call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver5);
	    if(callScreenPage.isCallerUnkonwn(driver5)) {
	    	callScreenPage.addCallerAsContact(driver5, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("softphone_task_related_Account_update"));
	    }
	    callScreenPage.openCallerDetailPage(driver5);
	    salesforceHomePage.closeLightningDialogueBoxForFirstTime(driver5);
	    salesforceHomePage.switchToClassicMode(driver5);
		contactDetailPage.openRecentCallEntry(driver5, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver5);
	    sfTaskDetailPage.verifyCallStatus(driver5, "Connected");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver5).contains("recordings"));
	    seleniumBase.closeTab(driver5);
	    seleniumBase.switchToTab(driver5, 1);
	    
	    //enable setting to display user's number on forwarding device
	    loginSupport(driver1);
  		dashboard.clickOnUserProfile(driver1);
  		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
  		userIntelligentDialerTab.disableCallForwardingUsingSmartNumber(driver1);
  		userIntelligentDialerTab.saveAcccountSettingsDialer(driver1);
  		seleniumBase.closeTab(driver1);
  		seleniumBase.switchToTab(driver1, 1);
  		reloadSoftphone(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void validate_duplicate_forwarding_number() {
		
		System.out.println("Test case --validate_duplicate_forwarding_number-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driver1);
		softPhoneSettingsPage.enterForwardingNumberDetails(driver1,  "", CONFIG.getProperty("prod_user_1_number"));
	    softPhoneSettingsPage.verifyForwardingMessagPresent(driver1, "Number already verified");

		// updating the driver used
		driverUsed.put("driver1", false);
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_call_forwarding_number_cancel() {
		System.out.println("Test case --verify_call_forwarading_number_cancel-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("chatterOnlyDriver");
		driverUsed.put("chatterOnlyDriver", true);
		
		String fwdNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_chatter_only_user_number"));

		// add a new forwarding number with a label
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driver3);
		softPhoneSettingsPage.enterCallForardingNumberSearch(driver3, fwdNumber.substring(0, 2));
		softPhoneCalling.idleWait(2);
		softPhoneSettingsPage.verifyAddFrwdNumBtnInvisible(driver3);

		// cancel call forwarding number verification
		softPhoneSettingsPage.enterCallForardingNumberSearch(driver3, "");
		softPhoneSettingsPage.enterForwardingNumberDetails(driver3,  "", fwdNumber);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(chatterOnlyDriver));
	    softPhoneSettingsPage.cancelCallForwardingModal(driver3);
	
	    //verify no call appearing for user
	    softPhoneCalling.idleWait(2);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(chatterOnlyDriver));
	    
		// updating the driver used
		driverUsed.put("driver3", false);
		driverUsed.put("chatterOnlyDriver", false);
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_call_forwarding_number_delete() {
		System.out.println("Test case --verify_call_forwarading_number_cancel-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String fwdNumber = CONFIG.getProperty("prod_user_1_number");

		// add a new forwarding number label
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", fwdNumber);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driver1);

		//verify that tool tip is present and edit and delete buttons are not present for selected number
		softPhoneSettingsPage.verifyToolTipAndBtnsSelectedFrwdnNum(driver1, fwdNumber);
		
		//disable call forwarding number
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		//verify that after disabling call forwarding number, edit and delete button are appearing
		softPhoneSettingsPage.openCallForwardingDrpDwn(driver1);
		softPhoneSettingsPage.verifyToolTipAndBtnsFwdNum(driver1, fwdNumber);
		
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", fwdNumber);
	    
		// updating the driver used
		driverUsed.put("driver3", false);
		driverUsed.put("driver6", false);
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_when_call_forwarding_disabled() {
		System.out.println("Test case --verify_when_call_forwarding_disabled()-- started");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//set call forwarding
		softPhoneSettingsPage.setCallForwardingNumber(driver3, driver4, "", CONFIG.getProperty("qa_user_2_number"));
		
		// open Support Page and enter unavailable call flow setting
		loginSupport(driver3);
		dashboard.clickAccountsLink(driver3);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver3);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver3);
		accountIntelligentDialerTab.disableCallForwardingSetting(driver3);
		accountIntelligentDialerTab.lockCallForwarding(driver3);
		accountIntelligentDialerTab.saveAcccountSettings(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		reloadSoftphone(driver3);

		//verify that call forwarding setting is not visible on setting page
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.verifyCallForwardingIsInvisible(driver3);
		softPhoneSettingsPage.verifyStayConnectedIsInvisible(driver3);
		
		// Taking outgoing call
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);

		// Verifying that call is not appearing on call forwarding device
		System.out.println("Verifying that call is not appearing on call forwarding device");
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver2);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(driver2));

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		// Taking incoming call
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);

		// Verifying that call is not appearing on call forwarding device
		System.out.println("Verifying that call is not appearing on call forwarding device");
		softPhoneCalling.verifyDeclineButtonIsInvisible(driver2);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(driver2));

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver3);
		dashboard.clickAccountsLink(driver3);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver3);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver3);
		accountIntelligentDialerTab.enableCallForwardingSetting(driver3);
		accountIntelligentDialerTab.saveAcccountSettings(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		reloadSoftphone(driver3);

		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(driver3);
		softPhoneSettingsPage.verifyStayConnectedIsVisible(driver3);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups={"MediumPriority"})
	  public void call_queue_call_forwarding_multiple_match()
	  {
	    System.out.println("Test case --call_queue_call_forwarding_multiple_match()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    addCallerAsMultiple(driver1, driver3, CONFIG.getProperty("qa_user_3_number"), CONFIG.getProperty("qa_user_3_name"));
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//select a caller from multiple match dropdown
		callScreenPage.selectFirstContactFromMultiple(driver1);
		String callerName = callScreenPage.getCallerName(driver1);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//calling back to the number
		softPhoneCalling.clickCallBackButton(driver1);
		
		//accept call from the caller
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify that caller is single
		assertFalse(callScreenPage.isCallerMultiple(driver1));
		assertEquals(callScreenPage.getCallerName(driver1), callerName);
		
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Call entry should appear under Recent calls on 'Call History' page
	@Test(groups = { "MediumPriority"})
	public void call_forwarding_ends_before_pickup() {
		System.out.println("Test case --call_forwarding_ends_before_pickup-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// Taking incoming call
		System.out.println("Taking call from twilio");
		String callerPhone = CONFIG.getProperty("qa_user_3_number");
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		String callerName = callScreenPage.getCallerName(driver1);

		//get the call time
		Date callPickTime		= HelperFunctions.GetCurrentDateTimeObj();
		
		// verifying call is visible on forwarding device
		System.out.println("Verifing that call is appearing at forwarding device");
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver2));
	
		// hanging up the call before pickup
		System.out.println("hanging up the call before pickup");
		softPhoneCalling.hangupActiveCall(driver1);

	    //verify data for outbound call
		seleniumBase.idleWait(5);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerPhone(driver1, 0), HelperFunctions.getNumberInSimpleFormat(callerPhone));
		assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver1, 0), callerName);
		Date actualPickTime = HelperFunctions.parseStringToDateFormat(softphoneCallHistoryPage.getHistoryCallerTime(driver1, 0), "M/dd/yyyy, hh:mm:ss a");

		int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(callPickTime, actualPickTime, "d/MM/yyyy, HH:mm:ss");
		assertTrue(diffInMinutes >=0 && diffInMinutes <=1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_forwarding_device_reconnect_on_resume() {
		System.out.println("Test case --outbound_forwarding_device_reconnect_on_resume-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Reload softphone and verify that user is on call screen page
		softPhoneCalling.putActiveCallOnHold(driver1);

		// verifying call is visible on agent's softphone
		softPhoneCalling.hangupActiveCall(driver2);
		
		//Verify call is still connected on agent and caller
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isCallOnHold(driver1);
		
		//resume the call
		//resuming call and verifying that call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
	    //verify that the forwarding device gets call again
	    softPhoneCalling.pickupIncomingCall(driver2);

		//Verify call is still connected on agent and caller
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver1);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_forwarding_busy_on_hold() {
		System.out.println("Test case --outbound_forwarding_busy_on_hold-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Reload softphone and verify that user is on call screen page
		softPhoneCalling.putActiveCallOnHold(driver1);
		
		//verify that dialpad icon visible, user busy
		assertTrue(softPhoneCalling.isDialPadButtonvisible(driver1));
		callScreenPage.verifyUserImageBusy(driver1);
		
		// Taking second outbound call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// Receiving call from call second caller
		System.out.println("Receiving call from call second caller");
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//Agent ends the second call
		softPhoneCalling.hangupActiveCall(driver1);
		
		// Call is removed from caller
		System.out.println("Call is removing from second caller");
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//verify that dialpad icon visible, user busy
		assertTrue(softPhoneCalling.isDialPadButtonvisible(driver1));
		callScreenPage.verifyUserImageBusy(driver1);
		
		//resuming call and verifying that call is still connected
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");

	    //Verify call is still connected on agent and caller
	    softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.isHangUpButtonVisible(driver1);
		softPhoneCalling.isHangUpButtonVisible(driver3);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);


		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void outbound_forwarding_transfer_call() {
		System.out.println("Test case --outbound_forwarding_transfer_call-- started ");

		// updating the driver used
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

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Reload softphone and verify that user is on call screen page
		softPhoneCalling.putActiveCallOnHold(driver1);
		
		//verify that dialpad icon visible, user busy
		assertTrue(softPhoneCalling.isDialPadButtonvisible(driver1));
		callScreenPage.verifyUserImageBusy(driver1);
		
		//open recent call history entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//verify that the on hold button is visible
		softPhoneCalling.isCallOnHold(driver1);
		
		// Taking second outbound call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));

		// Receiving call from call second caller
		System.out.println("Receiving call from call second caller");
		softPhoneCalling.pickupIncomingCall(driver5);

	    //Transferring the call to other agent
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    System.out.println("tranfered called agent picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify that the on hold button is visible
		softPhoneCalling.isCallOnHold(driver1);
		
		//resuming call and verifying that call is still connected
		softPhoneCalling.clickOnHoldButton(driver1);
		softPhoneCalling.clickResumeButton(driver1);
		assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
	    //Verify call is still connected on agent and caller
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.isHangUpButtonVisible(driver1);
		softPhoneCalling.isHangUpButtonVisible(driver3);
		softPhoneCalling.isHangUpButtonVisible(driver5);
		softPhoneCalling.isHangUpButtonVisible(driver4);

		// hanging up with the caller 1
		System.out.println("hanging up with the caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Checking that call forwarding device is getting disconnected
		System.out.println("Checking that call forwarding device is getting disconnected");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// hanging up with the transferec caller
		System.out.println("hanging up with the transferec caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);


		System.out.println("Test case is pass");
	}
	
	public void addCallerAsMultiple(WebDriver callerDriver, WebDriver mulitpleNumberDriver, String multiplContactNumber, String multipleContactName) {
		
		String callerPhone = multiplContactNumber;
		String callerName = multipleContactName;
	    String existingContact = CONFIG.getProperty("prod_user_3_name") + " Automation";
	    
		softPhoneCalling.softphoneAgentCall(callerDriver, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.isMuteButtonEnables(callerDriver);
		softPhoneCalling.hangupActiveCall(callerDriver);
	    callScreenPage.addCallerAsContact(callerDriver, CONFIG.getProperty("prod_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    softPhoneContactsPage.deleteAndAddContact(callerDriver, callerPhone, callerName);
		//calling again
		softPhoneCalling.softphoneAgentCall(callerDriver, callerPhone);
		softPhoneCalling.pickupIncomingCall(mulitpleNumberDriver);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(callerDriver);
	    seleniumBase.idleWait(2);
	    
	    // Add caller as multiple contact
	    callScreenPage.clickOnUpdateDetailLink(callerDriver);
	    callScreenPage.addCallerToExistingContact(callerDriver, existingContact);
	    
	   //verify caller is multiple
	   softPhoneContactsPage.searchUntilContacIsMultiple(callerDriver, callerPhone);
	    
	   // Calling from Agent's SoftPhone
	   softPhoneCalling.softphoneAgentCall(callerDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(mulitpleNumberDriver);
	 	softPhoneCalling.hangupActiveCall(callerDriver);
		softPhoneCalling.idleWait(3);
	 	
	 	softPhoneCalling.clickCallBackButton(callerDriver);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.pickupIncomingCall(mulitpleNumberDriver);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(callerDriver);											  
	}
	
	@AfterMethod(groups={"Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity"}, dependsOnMethods = {"resetSetupDefault"})
	public void  afterMethod(ITestResult result){
		if(result.getName().equals("call_forwarding_to_extension") || result.getName().equals("verify_when_call_forwarding_disabled")){
			if(callScreenPage.isCallerMultiple(driver3)){
				aa_AddCallersAsContactsAndLeads();
			}
			
			// Setting call forwarding ON
			System.out.println("Setting call forwarding OFF");
			softPhoneSettingsPage.clickSettingIcon(driver3);
			softPhoneSettingsPage.disableCallForwardingSettings(driver3);		
		}else if(result.getName().equals("call_forwarding_additional_number")  || result.getName().equals("call_forwarding_with_outbound_number")){
			softPhoneSettingsPage.clickSettingIcon(driver1);
			softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
			softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
		}else if(result.getName().equals("call_queue_call_forwarding_multiple_match")){
			aa_AddCallersAsContactsAndLeads();
		}
	}
	
	@AfterClass(groups = { "Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity" })
	public void afterClass() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
	}
	
	@AfterClass(groups = { "Regression", "ExludeForProd", "MediumPriority", "Product Sanity" })
	public void enableConferenceSetting() {
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// navigating to support page
		loginSupport(driver1);

		// opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");

		// disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableConferenceSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		// close the tab
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}
}
