/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.salesforce.ContactDetailPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class OtherFunctionalCases extends SoftphoneBase {
	
	@Test(groups = { "Regression" })
	public void enable_disable_call_forwarding() {
		System.out.println("Test case --enable_disable_notifications()-- started");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallForwardingSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallForwardingIsInvisible(driver1);
		softPhoneSettingsPage.verifyStayConnectedIsInvisible(driver1);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallForwardingSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(driver1);
		softPhoneSettingsPage.verifyStayConnectedIsVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}

	@Test(groups = { "Regression" })
	public void enable_disable_stay_connected() {
		System.out.println("Test case --enable_disable_stay_connected()-- started");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableStayConnectedSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(driver1);
		softPhoneSettingsPage.verifyStayConnectedIsInvisible(driver1);

		// open Support Page and enter unavailable call flow setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableStayConnectedSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(driver1);
		softPhoneSettingsPage.verifyStayConnectedIsVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}

	@Test(groups = { "Regression" })
	public void voicemail_dropped_after_call_timeout() {
		System.out.println("Test case --voicemail_dropped_after_call_timeout-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callTimeoutSec = "30";

		// open Support Page and set call timeout
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enterCallTimeoutTime(driver1, callTimeoutSec);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		// Taking incoming call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));

		// verify that call is appearing on agent
		System.out.println("verify that call is appearing on agent");
		softPhoneCalling.isAcceptCallButtonVisible(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// wait for call timeout
		seleniumBase.idleWait(40);

		// Call is removing from called agent
		System.out.println("Call is removing from called agent");
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));

		// Call is removing from caller
		System.out.println("Call is removing from caller");
		seleniumBase.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
		sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
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

	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_stay_connected_outbound_call_timeout() {
		System.out.println("Test case --verify_stay_connected_outbound_call_timeout-- started ");

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

		// Setting call forwarding ON and enable stay connected setting
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		softPhoneSettingsPage.enableStayConnectedSetting(driver1);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// tranfer call to user
		softPhoneCalling.transferToNumber(driver1,CONFIG.getProperty("qa_user_2_name"));

		// receiving call from app softphone
		softPhoneCalling.pickupIncomingCall(driver4);

		// wait for 20 seconds
		seleniumBase.idleWait(20);

		// verify that call forwarding is still connected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verify connected number of forwarding device
		assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))),"call is not resumed");

		// Making second outbound call
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("prod_user_2_number"));

		// picking up second call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver5);

		// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

		// verify connected number of forwarding device
		assertTrue(CONFIG.getProperty("qa_user_1_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver2))), "call is not resumed");

		// hangup from second caller
		softPhoneCalling.hangupActiveCall(driver5);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		for (int i = 0; i < 12; i++) {
			softPhoneCalling.isHangUpButtonVisible(driver2);
			seleniumBase.idleWait(5);
			System.out.println("Call is still connected after " + ((i + 1) * 5) + " seconds");
		}

		// Call is removed from call forwarding device
		System.out.println("verify that call is removed from forwarding device");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		// hanging up with caller 2
		System.out.println("hanging up with caller 2");
		softPhoneCalling.hangupActiveCall(driver4);

		// Setting call forwarding Off
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		// disable stay connected setting
		softPhoneSettingsPage.disableStayConnectedSetting(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_stay_connected_zero_timeout() {
		System.out.println("Test case --verify_stay_connected_zero_timeout-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableStayConnectedSetting(driver1);
		accountIntelligentDialerTab.enterStayConnectedTimeOut(driver1, "0");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// Setting call forwarding ON and enable stay connected setting
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		softPhoneSettingsPage.enableStayConnectedSetting(driver1);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));

		// receiving call from call forwarding device
		softPhoneCalling.pickupIncomingCall(driver2);

		// receiving call from the participant 1
		softPhoneCalling.pickupIncomingCall(driver3);

		// hangup from second caller
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.isCallBackButtonVisible(driver1);

		for (int i = 0; i < 4; i++) {
			seleniumBase.idleWait(5);
			
			if(softPhoneCalling.isCallBackButtonVisible(driver2, 0)) {
				System.out.println("Call is still connected after " + ((i + 1) * 5) + " seconds");
			}else {
				break;
			}
			
			if(i==3) {
				Assert.fail();
			}
		}

		// Call is removed from call forwarding device
		System.out.println("verify that call is removed from forwarding device");
		softPhoneCalling.isCallBackButtonVisible(driver2);

		//setting stay connected timeout
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableStayConnectedSetting(driver1);
		accountIntelligentDialerTab.enterStayConnectedTimeOut(driver1, "1");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// Setting call forwarding Off
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		// disable stay connected setting
		softPhoneSettingsPage.disableStayConnectedSetting(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	@AfterMethod(groups={"Regression"})
	  public void disableStayConnected(ITestResult result){
		if(result.getName().equals("verify_stay_connected_zero_timeout") && result.getStatus() == 2) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableStayConnectedSetting(driver1);
			accountIntelligentDialerTab.enterStayConnectedTimeOut(driver1, "0");
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
			
			// Setting call forwarding Off
			System.out.println("Setting call forwarding OFF");
			softPhoneSettingsPage.clickSettingIcon(driver1);
			softPhoneSettingsPage.disableCallForwardingSettings(driver1);

			// disable stay connected setting
			softPhoneSettingsPage.disableStayConnectedSetting(driver1);
		}
		 if(result.getName().equals("verify_stay_connected_outbound_call_timeout") && result.getStatus() == 2){
			 
			// Setting call forwarding Off
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(driver1);
				softPhoneSettingsPage.disableCallForwardingSettings(driver1);

				// disable stay connected setting
				softPhoneSettingsPage.disableStayConnectedSetting(driver1);
		  }
	  }
	
	@Test(groups = { "Regression" })
	public void navigate_with_chatter_user() {
		System.out.println("Test case --navigate_with_chatter_user()-- started ");

		// updating the driver used
		initializeDriverSoftphone("chatterOnlyDriver");
		driverUsed.put("chatterOnlyDriver", true);

		// navigate To Messaging Tabs
		softPhoneMessagePage.clickMessageIcon(chatterOnlyDriver);
		softPhoneMessagePage.navigateToAllMessages(chatterOnlyDriver);
		softPhoneMessagePage.navigateToReadMessages(chatterOnlyDriver);
		softPhoneMessagePage.navigateToUnreadMessages(chatterOnlyDriver);

		// Navigate to WebLeads Page
		softPhoneWebLeadsPage.navigateToWebLeadsPage(chatterOnlyDriver);

		// Navigate to Setting Page tabs
		softPhoneSettingsPage.clickSettingIcon(chatterOnlyDriver);
		softPhoneSettingsPage.navigateToVoicemailDropTab(chatterOnlyDriver);
		softPhoneSettingsPage.navigateToCustomGreetingTab(chatterOnlyDriver);
		softPhoneSettingsPage.clickAddNewCustomGreetingButton(chatterOnlyDriver);
		softPhoneSettingsPage.closeCustomGreetingDialogueBox(chatterOnlyDriver);
		seleniumBase.idleWait(2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("chatterOnlyDriver", false);
		System.out.println("Test case is pass");
		chatterOnlyDriver.quit();
	}

	@Test(groups = { "Regression" })
	public void sip_enabled_outbound_call() {
		System.out.println("Test case --sip_enabled_outbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// enable SIP setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableSipSettings(driver1);

		// Taking incoming call
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hangup call
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// verify data for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
		sfTaskDetailPage.isCreatedByRingDNA(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Taking SIP call with additional number
		// Taking incoming call
		
		//select an additional number
		String additionalNumber = softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 1);
		
		System.out.println("Making call to caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// Receiving call from called agent device
		System.out.println("Receiving call from called agent");
	    assertTrue(additionalNumber.contains(callScreenPage.getOutboundNumber(driver2)));
		softPhoneCalling.pickupIncomingCall(driver2);

		// hangup call
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// disable SIP setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableSipSettings(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void custom_calls_oubound_inbound_call() {
		System.out.println("Test case --custom_calls_oubound_inbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number"));

		//add contact	  
		//softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Select local presence number as outbound Number
		System.out.println("enabling local presence setting");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);

		// open Support Page and set custom object
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCustomActivityObjectState(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// verify that call is appearing on agent
		System.out.println("verify that call is appearing on agent");
		softPhoneCalling.pickupIncomingCall(driver2);

		// Verify that area code appearing on local presence tab is correct
		String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);

		// Call is removing from caller
		System.out.println("Call is removing from caller");
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for outbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver1).contains(" "));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver1), " ");
		assertEquals(sfTaskDetailPage.getLPRouting(driver1), " ");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Taking incoming call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, localPresenceNumber);

		// verify that call is appearing on agent
		System.out.println("verify that call is appearing on agent");
		softPhoneCalling.pickupIncomingCall(driver1);

		// Call is removing from caller
		System.out.println("Call is removing from caller");
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for inbound call
		seleniumBase.idleWait(5);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver1).contains(localPresenceNumber));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver1), "Local Presence");
		assertEquals(sfTaskDetailPage.getLPRouting(driver1), "Direct");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// reporting call
		String callCategory = "Audio Latency";
		String callNote = "Test Notes for call reporting";
		reportThisCallPage.giveCallReport(driver1, 3, callCategory, callNote);
		String callReportTime	= HelperFunctions.getDateTimeInTimeZone(HelperFunctions.GetCurrentDateTime("M/d/yyyy h:mm a"), CONFIG.getProperty("qa_user_1_timezone"), "M/d/yyyy h:mm a");
		seleniumBase.idleWait(5);
		
		// verify data for inbound call
		String dateFormat = "M/d/yyyy h:mm a";
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		assertEquals(sfTaskDetailPage.getCallReportCategory(driver1), callCategory);
		assertEquals(sfTaskDetailPage.getCallReportNote(driver1), callNote);
		Date ExpectedCallTime = HelperFunctions.getDateTimeInDateFormat(callReportTime, "M/d/yyyy h:mm a");
		Date actualCallTime = HelperFunctions.getDateTimeInDateFormat(sfTaskDetailPage.getCallReportTime(driver1), "M/d/yyyy h:mm a");
		int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(actualCallTime, ExpectedCallTime, dateFormat);
		assertTrue(diffInMinutes >= -2 || diffInMinutes <= 2);
		sfTaskDetailPage.isCallReportChecked(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Disable Local Presence Setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

		// open Support Page and set custom object
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCustomActivityObjectState(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
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
	
 @AfterMethod(groups={"Regression"})
  public void disableCustomCallObject(ITestResult result){
	  if(result.getName().equals("custom_calls_oubound_inbound_call") && result.getStatus() == 2){
		 
		// open Support Page and disable custom object
		  loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCustomActivityObjectState(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	  }
  }
	 
	  @Test(groups={"Regression"})
	  public void opt_out_email_option()
	  {
		System.out.println("Test case --opt_out_email_option-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	   
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify lead status on softphone
	    softPhoneCalling.hangupActiveCall(driver1);
	    String leadName = callScreenPage.getCallerName(driver1);
	    
		//Opening the caller detail page
	    HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.checkEmailOptOut, "true");
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.email, "abc@gmail.com");
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify lead status on softphone
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify email button is invisible
	    callScreenPage.verifyEmailButtonInvisible(driver1);
	    
	    //mark contact as favorite
	    callScreenPage.setAsFavoriteContact(driver1);
	    
	    //Search lead to verify email button not appearing
	    softPhoneContactsPage.searchSalesForce(driver1, leadName);
	    assertFalse(softPhoneContactsPage.isContactEmailVisible(driver1, leadName, SoftPhoneContactsPage.searchTypes.Contacts));
	    
	    //Verifu that email button is not appearing on favorite contact page
	    softPhoneContactsPage.navigateToFavoritePage(driver1);
	    assertFalse(softPhoneContactsPage.isFavConEmailVisible(driver1, leadName, SoftPhoneContactsPage.searchTypes.Contacts));
	    
	    //remove contact from favorite contact list
	    softPhoneContactsPage.removeContactFavorite(driver1, leadName);
	    
	    //Opening the caller detail page
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    leadDetails.clear();
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.uncheckEmailOptOut, "true");
	    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
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
	  public void do_not_call_option()
	  {
		System.out.println("Test case --do_not_call_option-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	   
		String errorMessage1 = "This person is currently on a \"Do Not Contact\" list and cannot be contacted.";
		String errorMessage2 = "If you feel that this is in error, please check with your Salesforce Administrator.";
	 	
	    softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
	    String existingContact = callScreenPage.getCallerName(driver1);
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    String favContact = callScreenPage.getCallerName(driver1);
	    
		//Opening the caller detail page
	    HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.checkDoNotCall, "true");
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage1));
	 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage2));
	 	
		//verify call not appeared on called agent
	 	softPhoneCalling.verifyDeclineButtonIsInvisible(driver3);
	    
	    //verify call back button is invisible
	    callScreenPage.verifyCallBackButtonInvisible(driver1);
	    
	    //verify call button is invisible at favourite page
	    seleniumBase.idleWait(2);
	    callScreenPage.setAsFavoriteContact(driver1);
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.navigateToFavoritePage(driver1);
	    seleniumBase.idleWait(2);
	    if(softPhoneContactsPage.clickFavConCallBtn(driver1, favContact)){
	    	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage1));
		 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage2));
	    }
	 	softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.removeContactFavorite(driver1, favContact);
	    
	    
	    //find caller when searching from contacts page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchUntilContactPresent(driver1, favContact);
	    if(softPhoneContactsPage.clickConCallBtn(driver1, favContact, SoftPhoneContactsPage.searchTypes.Contacts)){
	    	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage1));
		 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage2));
	    }
	    
	    // Update caller to be multiple
	    callScreenPage.closeErrorBar(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    seleniumBase.idleWait(25);
	    reloadSoftphone(driver1);
	    
		//calling again
	    softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("qa_user_3_number").substring(2, 12));
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		assertTrue(callScreenPage.isCallerMultiple(driver1));
	 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage1));
	 	assertTrue(callScreenPage.getErrorText(driver1).contains(errorMessage2));
	    
		softPhoneContactsPage.deleteContactUsingIndex(driver1, CONFIG.getProperty("qa_user_3_number").substring(2, 12), 0);
	    
	    //Opening the caller detail page
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    leadDetails.clear();
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.uncheckDoNotCall, "true");
	    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
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
	  public void call_history_counts()
	  {
		System.out.println("Test case --call_history_counts -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	 
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queue1Number	= CONFIG.getProperty("qa_group_1_number");
	    
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    
		softPhoneCalling.softphoneAgentCall(driver3, queue1Number);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
		seleniumBase.idleWait(5);
		
	    int missedCalls = softphoneCallHistoryPage.getMissedCallCount(driver1);
	    
	    int missedVMCall = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
	    
	    softphoneCallHistoryPage.clickMyCallsLink(driver1);
	    
	    int missedGroupCallCount = softphoneCallHistoryPage.getMissedGroupCallCount(driver1);
	    
	    assertEquals(missedCalls + missedVMCall, missedGroupCallCount);
	    
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
	    missedCalls = softphoneCallHistoryPage.getMissedCallCount(driver1);
	    
	    missedVMCall = softphoneCallHistoryPage.getMissedVoicemailCount(driver1);
	    
	    int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    
	    assertEquals(missedGroupCallCount + missedCalls + missedVMCall, callHistoryCounts);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"MediumPriority"})
	  public void missed_call_count_update()
	  {
		System.out.println("Test case --missed_call_count_update -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	 
	    String queueName = CONFIG.getProperty("qa_group_3_name").trim();
	    String queue1Number	= CONFIG.getProperty("qa_group_3_number");
	    
	    //unsubscribe from the queue
	    softPhoneCallQueues.unSubscribeQueue(driver1, queueName);
	    
	    //calling to the queue which user has unsubscribed so that call goes to vm
		softPhoneCalling.softphoneAgentCall(driver3, queue1Number);
	    seleniumBase.idleWait(15);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
		seleniumBase.idleWait(5);
	    
		//get the counts of group calls
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    int missedGroupCallsCount = softphoneCallHistoryPage.getMissedGroupCallCount(driver1);

	    //get the counts of my calls
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    int missedMyCallsCount = softphoneCallHistoryPage.getMissedMyCallCount(driver1);
	    
	    //get the count of total missed calls
	    int totalMissedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    
	    //play the group call vm
	    softphoneCallHistoryPage.playVMByIndex(driver1, 1);
	    
	    //verify that the total missed call has been reduced by 1
	    assertEquals(softphoneCallHistoryPage.getHistoryMissedCallCount(driver1), totalMissedCallCount == 0 ? 0 : totalMissedCallCount-1);
	    
	    //verify that My Calls count is same
	    assertEquals(softphoneCallHistoryPage.getMissedMyCallCount(driver1), missedMyCallsCount );
	    
	    //verify that count for group calls is decreased
	    softphoneCallHistoryPage.clickMyCallsLink(driver1);
	    assertEquals(softphoneCallHistoryPage.getMissedGroupCallCount(driver1), missedGroupCallsCount == 0 ? 0 : missedGroupCallsCount - 2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_contacts_custom_fields()
	  {
	  System.out.println("Test case --custom_calls_oubound_inbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number"));

		//add contact	  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// verify that call is appearing on agent
		System.out.println("verify that call is appearing on agent");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Verify custom fields during the call
		callScreenPage.verifyCustomFields(driver1, "Account City (Account)", "Jaipur");
		//callScreenPage.verifyCustomFields(driver1, "Address (Owner)", "Delhi");

		// Call is removing from caller
		System.out.println("Call is removing from caller");
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for outbound call
		callScreenPage.openCallerDetailPage(driver1);
		String createdDate = contactDetailPage.getCreatedTime(driver1).trim();
		createdDate = HelperFunctions.changeDateTimeFormat(createdDate, "MM/dd/yy h:mm aa", "MM/dd/yy hh:mm aa");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Verify custom fields
		callScreenPage.verifyCustomFields(driver1, "Account City (Account)", "Jaipur");
		//callScreenPage.verifyCustomFields(driver1, "Address (Owner)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Created Date", createdDate);
		
		//Verify custom fields opeing from call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.verifyCustomFields(driver1, "Account City (Account)", "Jaipur");
		//callScreenPage.verifyCustomFields(driver1, "Address (Owner)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Created Date", createdDate);
		
		//verify that user is able to call account phone
		callScreenPage.callCustomFields(driver1, "Account Phone (Account)", "(987) 654-3210");
		seleniumBase.idleWait(3);
		softPhoneCalling.hangupActiveCall(driver1);		
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_leads_custom_fields()
	  {
	  System.out.println("Test case --verify_leads_custom_fields-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_2_number"));

		//add lead	  
		softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("prod_user_2_name"));

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// verify that call is appearing on agent
		System.out.println("verify that call is appearing on agent");
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Verify custom fields during the call
		callScreenPage.verifyCustomFields(driver1, "Country (CreatedBy)", "India");
		callScreenPage.verifyCustomFields(driver1, "City (CreatedBy)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Company Name (CreatedBy)", "Metacube Software Pvt Ltd");

		// Call is removing from caller
		System.out.println("Call is removing from caller");
		softPhoneCalling.hangupIfInActiveCall(driver1);

		//Verify custom fields
		callScreenPage.verifyCustomFields(driver1, "Country (CreatedBy)", "India");
		callScreenPage.verifyCustomFields(driver1, "City (CreatedBy)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Company Name (CreatedBy)", "Metacube Software Pvt Ltd");
		
		//open the entry from call history and verify custom fields
		callScreenPage.verifyCustomFields(driver1, "Country (CreatedBy)", "India");
		callScreenPage.verifyCustomFields(driver1, "City (CreatedBy)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Company Name (CreatedBy)", "Metacube Software Pvt Ltd");
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	  }
	  
	  @Test(groups = { "Regression" })
		public void navigate_with_standard_user() {
			System.out.println("Test case --navigate_with_standard_user()-- started ");

			// updating the driver used
			initializeDriverSoftphone("standardUserDriver");
			driverUsed.put("standardUserDriver", true);
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);

			// Making an outbound call
			System.out.println("Taking an incoming call");
			softPhoneCalling.softphoneAgentCall(standardUserDriver, CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver1);
			softPhoneCalling.hangupIfInActiveCall(standardUserDriver);
			
			// navigate To Messaging Tabs
			softPhoneMessagePage.clickMessageIcon(standardUserDriver);
			softPhoneMessagePage.navigateToAllMessages(standardUserDriver);
			softPhoneMessagePage.navigateToReadMessages(standardUserDriver);
			softPhoneMessagePage.navigateToUnreadMessages(standardUserDriver);

			// Navigate to WebLeads Page
			softPhoneWebLeadsPage.navigateToWebLeadsPage(standardUserDriver);

			// Navigate to Setting Page tabs
			softPhoneSettingsPage.clickSettingIcon(standardUserDriver);
			softPhoneSettingsPage.navigateToVoicemailDropTab(standardUserDriver);
			softPhoneSettingsPage.navigateToCustomGreetingTab(standardUserDriver);
			softPhoneSettingsPage.clickAddNewCustomGreetingButton(standardUserDriver);
			softPhoneSettingsPage.closeCustomGreetingDialogueBox(standardUserDriver);
			seleniumBase.idleWait(2);

			// Setting driver used to false as this test case is pass
			driverUsed.put("standardUserDriver", false);
			System.out.println("Test case is pass");
			standardUserDriver.quit();
			standardUserDriver = null;
		}
	  
	  @Test(groups={"Regression"})
	  public void missed_count_when_marked_as_read()
	  {
		System.out.println("Test case --missed_count_when_marked_as_read -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
		
	    //Open call History page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickMyCallsLink(driver1);
	    
	    //take a miss call
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.declineCall(driver1);
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
	    //make user image busy
	    callScreenPage.setUserImageBusy(driver1);
	    
	    //take another call that will be missed again
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
	    //select all calls and mark them as read
	    softphoneCallHistoryPage.verifyUnreadMissedCallRow(driver1, 0);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    softphoneCallHistoryPage.markSelectedCallsRead(driver1);
	    softphoneCallHistoryPage.verifyReadMissedCallRow(driver1, 0);
	    
	    //take missed call count
	    int missedCalls = softphoneCallHistoryPage.getMissedCallCount(driver1);
	    
	    //verify that missed call count is 0
	    assertEquals(missedCalls, 0);
	    
	    //set user image to available again
	    callScreenPage.setUserImageAvailable(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"MediumPriority"})
	  public void max_hold_call_time_one_minute()
	  {
	    System.out.println("Test case --max_hold_call_time_one_minute-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
		//navigating to support page 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountOverviewtab.setMaxHoldTime(driver1, "1");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);

		//making call to first caller
	    System.out.println("making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //putting call on hold
	    softPhoneCalling.putActiveCallOnHold(driver1);
	    
	    //wait for one minute
	    seleniumBase.idleWait(60);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    assertTrue(softPhoneCalling.isCallBackButtonVisible(driver2, 5));
	    softPhoneCalling.onHoldButtonIsInvisible(driver1);
	    
		//navigating to support page 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountOverviewtab.setMaxHoldTime(driver1, "");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	  
	  @Test(groups={"MediumPriority"})
	  public void vm_transcription_setting_disabled()
	  {
	    System.out.println("Test case --vm_transcription_setting_disabled-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);

		//navigating to support page 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableVmTranscriptionSetting(driver1);
		assertFalse(accountIntelligentDialerTab.isVmTrascriptionByCountryEnabled(driver1));
		accountIntelligentDialerTab.enableVmTranscriptionSetting(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	     
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	  
	  //Verify retry functionality of a Salesforce error 
		@Test(groups = {"MediumPriority"})
		public void retry_functionality_sfdc_error() {
			System.out.println("Test case --retry_functionality_sfdc_error-- started ");
	
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver2");
			driverUsed.put("driver2", true);
			
			String contactName 		= CONFIG.getProperty("qa_user_1_name");
			String date 			= HelperFunctions.GetCurrentDateTime("MM/dd/yyyy"); 
		    String category 		= "Salesforce Error";
		    String dateRange		= "Today";
			
			// Calling from Agent's SoftPhone
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
			
			//pickup and hangup the call
			softPhoneCalling.pickupIncomingCall(driver2);
			
			String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
			callToolsPanel.giveCallRatings(driver1, 5);
	
			// Deleting contact if exist
			System.out.println("deleting contact");
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			seleniumBase.idleWait(3);
			assertEquals(callScreenPage.getErrorText(driver1), "A Salesforce operation failed because the entity has been deleted.");
			softPhoneSettingsPage.closeErrorMessage(driver1);
			
			assertEquals(callScreenPage.getCallerRating(driver1), "5");
			assertEquals(callScreenPage.getCallDisposition(driver1), callDisposition);
			
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountLogsTab.navigateToAccountLogTab(driver1);
			accountLogsTab.searchAccountLogs(driver1, contactName, category, dateRange);
			accountLogsTab.verifyRecordingLogsPresent(driver1, 0, category, date, contactName, "entity is deleted (ENTITY_IS_DELETED)");
			seleniumBase.switchToTab(driver1, 1);
		
			String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
			salesforceHomePage.clickRecyCleBinLink(driver1);
			salesforceHomePage.undeleteRecycleBinItem(driver1, 0);
			seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		    accountLogsTab.clickRetryButton(driver1, 0);
		    assertFalse(accountLogsTab.isRetryBtnEnabled(driver1, 0));
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
			
			//Opening the caller detail page
		    callScreenPage.openCallerDetailPage(driver1);
		
			//clicking on recent call entry
			contactDetailPage.openRecentCallEntry(driver1, callSubject);
			
			//Verifying Recent Calls Detail
		    assertEquals(sfTaskDetailPage.getRating(driver1), "5");
		    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
		    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
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
		
		@AfterMethod(groups={"Regression", "MediumPriority"}, dependsOnMethods= {"resetSetupDefault"})
		  public void disableDoNotCall(ITestResult result){
			  if(result.getName().equals("do_not_call_option") && (result.getStatus() == 2 || result.getStatus() == 3)){
				  HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
				  
				    //Opening the caller detail page
				    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
				    leadDetails.clear();
				    leadDetails.put(ContactDetailPage.LeadDetailsFields.uncheckDoNotCall, "true");
				    callScreenPage.openCallerDetailPage(driver1);
				    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
				    seleniumBase.closeTab(driver1);
				    seleniumBase.switchToTab(driver1, 1);  
				    
				    aa_AddCallersAsContactsAndLeads();
			  }else if(result.getName().equals("retry_functionality_sfdc_error") && (result.getStatus() == 2 || result.getStatus() == 3)){
				    aa_AddCallersAsContactsAndLeads();
			  }
		  }

}