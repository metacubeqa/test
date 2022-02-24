/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftphoneCallHistoryPage.CallHistoryFiedls;
import support.source.accounts.AccountIntelligentDialerTab;

/**
 * @author Abhishek
 *
 */
public class CallDisposition extends SoftphoneBase{
	
	@BeforeMethod(groups = { "Regression", "MediumPriority" })
	public void beforeMethod() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and disable call disposition prompt setting
		if (seleniumBase.getTabCount(driver1) == 1 || seleniumBase.getWindowHandleByTitle(driver1, "ringDNA Web") == null) {
			loginSupport(driver1);
		}
		
		if(!accountIntelligentDialerTab.isUserOnIntelligentDialerTab(driver1)) {
			dashboard.clickAccountsLink(driver1);
			System.out.println("Account editor is opened ");
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_required_prompt_setting(){
		System.out.println("Test case --verify_disposition_required_prompt_setting()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	callScreenPage.clickDispositionWarning(driver1);
	 	callToolsPanel.verifyDispositionSerchBoxVisible(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression", "MediumPriority"})
	public void verify_disposition_required_setting(){
		System.out.println("Test case --verify_disposition_required_setting()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	String callerName = callScreenPage.getCallerName(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//verify call history counts
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//verifying that no problem occurs while tacking incoming call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//Verifying user not able to make call until disposition is set
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	
	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//Verifying user not able to make call until disposition is set
	 	softPhoneContactsPage.salesforceSearchAndCall(driver1, callerName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//Verifying user not able to make call until disposition is set
	    softPhoneCalling.enterNumberAndDial(driver1, callerPhone);
	    
	    //verifying that dispostion required window is openend
	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callScreenPage.clickDispositionWarning(driver1);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callScreenPage.clickDispositionWarning(driver1);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
		//verify that alert count is zero now
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver1);
	 	
	 	//Verifying that user is able to take the call once dispositions are set
	 	 // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_required_incoming_call(){
		System.out.println("Test case --verify_disposition_required_incoming_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    //getting call history counts before the call
	    int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	 	
	 	//verifying that no problem occurs while making outbound call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 		 	
		//verify that alert count is zero now
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver1);
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, callHistoryCounts-1);
	 	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_required_all_calls(){
		System.out.println("Test case --verify_disposition_required_all_calls()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	String callerName = callScreenPage.getCallerName(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//verifying that no problem occurs while tacking incoming call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 1);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_List(){
		System.out.println("Test case --verify_disposition_List()-- started");

		 // updating the driver used
 		initializeDriverSoftphone("driver1");
 		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("driver2");
 		driverUsed.put("driver2", true);
 		initializeDriverSoftphone("driver3");
 		driverUsed.put("driver3", true);
 		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		seleniumBase.switchToTab(driver1, 1);
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		List<String> supportDispostionList = accountIntelligentDialerTab.getCallDispositionList(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 		
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
 		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(driver1);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(driver1);
	 	Collections.sort(supportDispostionList);
	 	Collections.sort(SoftphoneDispositionList);
	 	assertTrue(SoftphoneDispositionList.equals(supportDispostionList));

 		// hanging up with the caller 1
 		System.out.println("hanging up with the caller 1");
 		softPhoneCalling.hangupActiveCall(driver3);

 		// Call is removing from softphone
 		System.out.println("Call is removing from softphone");
 		softPhoneCalling.isCallBackButtonVisible(driver1);

 		// Checking that call forwarding device is getting disconnected
 		System.out.println("Checking that call forwarding device is getting disconnected");
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
	
	@Test(groups = {"Regression"})
	public void verify_spam_blocked_number(){
		System.out.println("Test case --verify_spam_blocked_number()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_3_number");
	    
	    // Calling from Agent's SoftPhone
		seleniumBase.switchToTab(driver1, 1);
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver6);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//setting up the disposition for calls
	 	callToolsPanel.selectDispositionUsingText(driver1, "spam");
	 	
	 	//verify that caller is blocked
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
	 	
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
	 		
	 	// verify that call is not appearing for agent
	    softPhoneCalling.isCallBackButtonVisible(driver6);
	 	softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);
	 	
	 	//unblock the caller
	 	callScreenPage.clickUnblockButton(driver1);
		
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
	 			
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify that caller is unblocked
	 	callScreenPage.verifyUnblockBtnInvisible(driver1);
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_description_box(){
		System.out.println("Test case --verify_call_description_box()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("qa_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallToolsSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    //verify alert tab is there and it is blank
	    softphoneCallHistoryPage.switchToAlertsTab(driver1);
	    softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	    softphoneCallHistoryPage.idleWait(2);
	    assertTrue(softphoneCallHistoryPage.isCallerHistoryBlank(driver1));
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
	 	//Verifying that Call Notes desciption box can be opened and comments can be entered.
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		String callNotes = "This is a Call Task Comments" + helperFunctions.GetCurrentDateTime();
		callToolsPanel.clickCallToolsIcon(driver1);
	 	callToolsPanel.enterCallNotesSubject(driver1, callSubject);
	 	callToolsPanel.enterCallNotesText(driver1, callNotes);
	 	callToolsPanel.clickCallNotesSaveBtn(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_alert_for_old_calls(){
		System.out.println("Test case --verify_disposition_required_alert_for_old_calls()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		seleniumBase.switchToTab(driver1, 1);
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		String oldChangeTime = null;
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		oldChangeTime = accountIntelligentDialerTab.getCallDispositionSettingChangedTime(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	String callerName = callScreenPage.getCallerName(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//Now verify that there is no issue while taking call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	//verify alert counts
	 	assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver1));
	 	
	 	//verifying that no problem occurs when setting is off
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	softPhoneCalling.isCallBackButtonVisible(driver5);
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	
	 	assertFalse(oldChangeTime.contentEquals(accountIntelligentDialerTab.getCallDispositionSettingChangedTime(driver1)));
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
		//verify that alert count is zero now and history is blank
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver5", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_managed_by_team(){
		System.out.println("Test case --verify_disposition_managed_by_team()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyTeamDispositionSettingPresents(driver1);
		
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), "");
		groupsPage.verifyTeamDispositionSettingPresents(driver1);
			
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    //verify alert counts
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver1));
	    
	 	// open Support Page and disable call disposition prompt setting
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver5", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_directions(){
		System.out.println("Test case --verify_disposition_directions()-- started");

		// updating the driver used
 		initializeDriverSoftphone("agentDriver");
 		driverUsed.put("agentDriver", true);
 		initializeDriverSoftphone("driver2");
 		driverUsed.put("driver2", true);
 		
 		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 		
 		// Taking incoming call
 		System.out.println("Taking call from twilio");
 		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_agent_user_number"));

 		// Receiving call from call forwarding device
 		System.out.println("Receiving call from call forwarding device");
 		softPhoneCalling.pickupIncomingCall(agentDriver);
 		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(agentDriver);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(agentDriver);
	 	assertTrue(SoftphoneDispositionList.contains("Inbound Disposition 1"));
	 	assertTrue(SoftphoneDispositionList.contains("Inbound Disposition 2"));
	 	assertFalse(SoftphoneDispositionList.contains("Outbound Disposition 1"));
	 	assertFalse(SoftphoneDispositionList.contains("Outbound Disposition 2"));
	 	callToolsPanel.clickDispositionIcon(agentDriver);
	 	
 		// hanging up with the caller 1
 		System.out.println("hanging up with the caller 1");
 		softPhoneCalling.hangupActiveCall(agentDriver);

 		// Call is removing from softphone
 		System.out.println("Call is removing from softphone");
 		softPhoneCalling.isCallBackButtonVisible(driver2);
 		
 		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
 		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("agentDriver", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_disposition_managed_by_multiple_teams(){
		System.out.println("Test case --verify_disposition_managed_by_multiple_teams()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("agentDriver");
 		driverUsed.put("agentDriver", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
		//verify call recording is Unlocked on groups page
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		groupsPage.saveGroup(driver1);
		
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.None);
		groupsPage.saveGroup(driver1);
			
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(agentDriver);
	    
	    //verify alert tab is disabled
	    softphoneCallHistoryPage.openCallsHistoryPage(agentDriver);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(agentDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(agentDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(agentDriver));
	    
	 	// open Support Page and disable call disposition prompt setting
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("agentDriver", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_for_group_call(){
		System.out.println("Test case --verify_disposition_required_for_group_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	 		
	 	// receiving call from receiver
	 	softPhoneCallQueues.pickCallFromQueue(driver1);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	softPhoneCalling.hangupIfInActiveCall(driver2);
	 	softPhoneCalling.idleWait(15);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//try to make another call without giving any disposition
	 	//Verifying user not able to make call until disposition is set
	    softPhoneCalling.enterNumberAndDial(driver1, callerPhone);
	 	
	    //verify that review call window appears. click on it and verify user is navigated to alert tab
	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callScreenPage.clickDispositionWarning(driver1);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//Verifying that user is able to take the call once dispositions are set
	 	// Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_transfer_call(){
		System.out.println("Test case --verify_disposition_required_group_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callerName = callScreenPage.getCallerName(driver1);
	    
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from the transfered device
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //hangup the calls
	 	softPhoneCalling.hangupActiveCall(driver4);
	 	softPhoneCalling.hangupIfInActiveCall(driver2);
	 	
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	 	softPhoneCalling.idleWait(15);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//open call history page and verify that the acller details appearing are of first call only
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts, only one call shoud be there
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_group_history_hidden(){
		System.out.println("Test case --verify_disposition_required_group_history_hidden()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
	 	accountIntelligentDialerTab.enableHideGroupCallHistory(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	 		
	 	// receiving call from receiver
	 	softPhoneCallQueues.pickCallFromQueue(driver1);
	 	softPhoneCalling.idleWait(5);
	 	String callerName = callScreenPage.getCallerName(driver1);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	softPhoneCalling.hangupIfInActiveCall(driver2);
	 	softPhoneCalling.idleWait(10);
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, callHistoryCounts);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//open call history page and verify that the acller details appearing are of first call only
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts, only one call shoud be there
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_required_none(){
		System.out.println("Test case --verify_disposition_required_none()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver4, AccountIntelligentDialerTab.dispositionReqStates.None);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		accountIntelligentDialerTab.verifyCallDispositionchangedTimeInvisible(driver4);
	    seleniumBase.switchToTab(driver4, 1);

	    //verify that alert tab is disabled
	    reloadSoftphone(driver4);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver4));
	 	
	 	// open Support Page and disable call disposition prompt setting
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver4);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver4);
	 	seleniumBase.closeTab(driver4);
	 	seleniumBase.switchToTab(driver4, 1);
	 	reloadSoftphone(driver4);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_forwrding_device(){
		System.out.println("Test case --verify_disposition_forwrding_device()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		seleniumBase.switchToTab(driver1, 1);
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		// open Support Page and enable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	   
	    //take call three times
	    
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callerName = callScreenPage.getCallerName(driver1);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
	 	//verify call back button visible
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
	 	softPhoneCalling.idleWait(2);	

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	    
		//verify that alert count is 1
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver1, 0), callerName);
	 	
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_dispositions_call_flow(){
		System.out.println("Test case --verify_dispositions_call_flow()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		List<String> supportDispostionList = accountIntelligentDialerTab.getCallDispositionList(driver1);
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		// verifying call is visible on agent's softphone
 		System.out.println("Verifing that call is appearing at agent's softphone");
 		callToolsPanel.isRelatedRecordsIconVisible(driver4);
 		softPhoneCalling.isHangUpButtonVisible(driver4);
 		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(driver4);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(driver4);
	 	Collections.sort(supportDispostionList);
	 	Collections.sort(SoftphoneDispositionList);
	 	assertTrue(SoftphoneDispositionList.equals(supportDispostionList));

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(5);
		
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver5", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_list_for_single_teams(){
		System.out.println("Test case --verify_disposition_list_for_single_teams()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("agentDriver");
 		driverUsed.put("agentDriver", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		groupsPage.openGroupSearchPage(driver1);
  		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
			
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(agentDriver);
	    
	    //making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_agent_user_number"));
		softPhoneCalling.pickupIncomingCall(agentDriver);
		
		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		callToolsPanel.isRelatedRecordsIconVisible(agentDriver);
		softPhoneCalling.isHangUpButtonVisible(agentDriver);
		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(agentDriver);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(agentDriver);
	 	assertTrue(SoftphoneDispositionList.contains("Inbound Disposition 1"));
	 	assertFalse(SoftphoneDispositionList.contains("Inbound Disposition 2"));
	 	assertFalse(SoftphoneDispositionList.contains("Outbound Disposition 1"));
	 	assertFalse(SoftphoneDispositionList.contains("Outbound Disposition 2"));
	
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(5);
	    
	 	// open Support Page and disable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		groupsPage.openGroupSearchPage(driver1);
  		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.addMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("agentDriver", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_list_multiple_teams(){
		System.out.println("Test case --verify_disposition_list_for_multiple_teams()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("agentDriver");
 		driverUsed.put("agentDriver", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		List<String> supportDispostionList = accountIntelligentDialerTab.getCallDispositionList(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		groupsPage.openGroupSearchPage(driver1);
  		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.addMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
  		
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(agentDriver);
	    
	    //making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_agent_user_number"));
		softPhoneCalling.pickupIncomingCall(agentDriver);
		
		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		callToolsPanel.isRelatedRecordsIconVisible(agentDriver);
		softPhoneCalling.isHangUpButtonVisible(agentDriver);
		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(agentDriver);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(agentDriver);
	 	supportDispostionList.remove("Unselected Disposition");
	 	supportDispostionList.remove("Outbound Disposition 1");
	 	supportDispostionList.remove("Outbound Disposition 2");
	 	Collections.sort(supportDispostionList);
	 	Collections.sort(SoftphoneDispositionList);
	 	assertTrue(SoftphoneDispositionList.equals(supportDispostionList));
	
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(5);
	    
	 	// open Support Page and disable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("agentDriver", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_same_disposition_list_multiple_teams(){
		System.out.println("Test case --verify_same_disposition_list_multiple_teams()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("agentDriver");
 		driverUsed.put("agentDriver", true);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		List<String> supportDispostionList = accountIntelligentDialerTab.getCallDispositionList(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		groupsPage.openGroupSearchPage(driver1);
  		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.addMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
  		if(!groupsPage.isInboundCallDispositionChecked(driver1, "Inbound Disposition 1")){
  			groupsPage.unCheckInboundCallDisposition(driver1, "Inbound Disposition 1");
  		}
  		if(!groupsPage.isOutboundCallDispositionChecked(driver1, "Inbound Disposition 1")){
  			groupsPage.unCheckOutboundCallDisposition(driver1, "Outbound Disposition 1");
  		}
  		groupsPage.saveGroup(driver1);
  		
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(agentDriver);
	    
	    //making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// verifying call is visible on agent's softphone
		System.out.println("Verifing that call is appearing at agent's softphone");
		callToolsPanel.isRelatedRecordsIconVisible(agentDriver);
		softPhoneCalling.isHangUpButtonVisible(agentDriver);
		
	 	//setting up the disposition for calls
	 	callToolsPanel.clickDispositionIcon(agentDriver);
	 	List<String> SoftphoneDispositionList =  callToolsPanel.getSoftphoneDispostionList(agentDriver);
	 	supportDispostionList.remove("Unselected Disposition");
	 	supportDispostionList.remove("Inbound Disposition 1");
	 	supportDispostionList.remove("Inbound Disposition 2");
	 	Collections.sort(supportDispostionList);
	 	Collections.sort(SoftphoneDispositionList);
	 	assertTrue(SoftphoneDispositionList.equals(supportDispostionList));
	
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.idleWait(5);
	    
	 	// open Support Page and disable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
		groupsPage.openGroupSearchPage(driver1);
  		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_5_name"), "");
  		if(groupsPage.isInboundCallDispositionChecked(driver1, "Inbound Disposition 1")){
  			groupsPage.unCheckInboundCallDisposition(driver1, "Inbound Disposition 1");
  		}
  		if(groupsPage.isOutboundCallDispositionChecked(driver1, "Inbound Disposition 1")){
  			groupsPage.unCheckOutboundCallDisposition(driver1, "Outbound Disposition 1");
  		}
  		groupsPage.saveGroup(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("agentDriver", false);
	}

	@Test(groups = {"MediumPriority"})
	public void verify_disposition_flag_no_Team(){
		System.out.println("Test case --verify_disposition_flag_no_Team()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//updating the driver used
		WebDriver qaDriver = getDriver();
		SFLP.softphoneLogin(qaDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
		softPhoneSettingsPage.setDefaultSoftPhoneSettings(qaDriver);
		
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    reloadSoftphone(qaDriver);
	    
	    //make a call
		softPhoneCalling.softphoneAgentCall(qaDriver, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(qaDriver);
	    
		//verify that user is able to select disposition for recent call entry
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(qaDriver);
	    callToolsPanel.selectDisposition(qaDriver, 0);
	    
	    qaDriver.quit();
	    qaDriver = null;
	    
	 	// open Support Page and disable call disposition prompt setting
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver5", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_team_inbound_all(){
		System.out.println("Test case --verify_disposition_team_inbound_all()-- started");

		//updating the driver used
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// open Support Page and enable call disposition managed by team setting
 		loginSupport(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(webSupportDriver);
		accountIntelligentDialerTab.enableManageDispositionByTeam(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	    
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.All);
		groupsPage.saveGroup(webSupportDriver);
		
		//set call disposition required setting for Inbound Calls for team 2
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		groupsPage.saveGroup(webSupportDriver);

	    seleniumBase.switchToTab(webSupportDriver, 1);

	    //reload softphone
	    reloadSoftphone(webSupportDriver);
	    
	    //verify alert tab is enabled
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(webSupportDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    
	    String callerPhone = CONFIG.getProperty("prod_user_1_number");
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(webSupportDriver);
	    
	    // Make an outbound call
	    softPhoneCalling.softphoneAgentCall(webSupportDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(webSupportDriver, historyCounts);
	 	
	 	//verify alert counts has increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, 0);
	 	
	 	//try to make another call without giving any disposition
	 	//Verifying user not able to make call until disposition is set
	    softPhoneCalling.enterNumberAndDial(webSupportDriver, callerPhone);
	 	
	    //verify that review call window appears. click on it and verify user is navigated to alert tab
	 	callScreenPage.isMissingDispositionWindowVisible(webSupportDriver);
	 	callScreenPage.clickReviewCallsButton(webSupportDriver);
	 	softphoneCallHistoryPage.veifyAlertTabActive(webSupportDriver);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(webSupportDriver, 0);
	 	callScreenPage.clickDispositionWarning(webSupportDriver);
	 	callToolsPanel.selectDisposition(webSupportDriver, 0);
	    
	    historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(webSupportDriver);
	    
	    // Taking an incoming call to the agent
	    softPhoneCalling.softphoneAgentCall(driver2,  CONFIG.getProperty("qa_support_user_number"));
	 	softPhoneCalling.pickupIncomingCall(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	
	 	//verify call history counts increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(webSupportDriver, historyCounts);
	 	
	 	//verify alert counts increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, 0);
	 	
	 	//try to make another call without giving any disposition
	 	//Verifying user not able to make call until disposition is set
	    softPhoneCalling.enterNumberAndDial(webSupportDriver, callerPhone);
	 	
	    //verify that review call window appears. click on it and verify user is navigated to alert tab
	 	callScreenPage.isMissingDispositionWindowVisible(webSupportDriver);
	 	callScreenPage.clickReviewCallsButton(webSupportDriver);
	 	softphoneCallHistoryPage.veifyAlertTabActive(webSupportDriver);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(webSupportDriver, 0);
	 	callScreenPage.clickDispositionWarning(webSupportDriver);
	 	callToolsPanel.selectDisposition(webSupportDriver, 0);
	 	
	 	// Verify that user is able to make the call once the disposition is set
	    softPhoneCalling.softphoneAgentCall(webSupportDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	    
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(webSupportDriver, seleniumBase.getTabCount(webSupportDriver));
	 	dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(webSupportDriver);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(webSupportDriver);
	 	accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	 	seleniumBase.closeTab(webSupportDriver);
	 	seleniumBase.switchToTab(webSupportDriver, 1);
	 	reloadSoftphone(webSupportDriver);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("webSupportDriver", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_team_oubtound_inbound(){
		System.out.println("Test case --verify_disposition_team_inbound_all()-- started");

		//updating the driver used
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		// open Support Page and enable call disposition managed by team setting
 		loginSupport(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(webSupportDriver);
		accountIntelligentDialerTab.enableManageDispositionByTeam(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	    
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		groupsPage.saveGroup(webSupportDriver);
		
		//set call disposition required setting for Inbound Calls for team 2
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		groupsPage.saveGroup(webSupportDriver);
			
	    seleniumBase.switchToTab(webSupportDriver, 1);

	    //reload softphone
	    reloadSoftphone(webSupportDriver);
	    
	    //verify alert tab is enabled
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(webSupportDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    
	    String callerPhone = CONFIG.getProperty("prod_user_1_number");
	    
	    // Make an outbound call
	    softPhoneCalling.softphoneAgentCall(webSupportDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	    String callerName = callScreenPage.getCallerName(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	
	 	//set outbound call data to verify on alerts tab
	 	HashMap<CallHistoryFiedls, String> outboundCallHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	outboundCallHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	outboundCallHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	outboundCallHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	outboundCallHistoryData.put(CallHistoryFiedls.OutgoingCall, "true");
	    
	    // Taking an incoming call to the agent
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_support_user_number"));
	 	softPhoneCalling.pickupIncomingCall(webSupportDriver);
	    callerName = callScreenPage.getCallerName(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	
	 	//set inbound call data to verify on alerts tab
	 	HashMap<CallHistoryFiedls, String> inboundCallHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	inboundCallHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	inboundCallHistoryData.put(CallHistoryFiedls.callerPhone, CONFIG.getProperty("prod_user_2_number"));
	 	inboundCallHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	inboundCallHistoryData.put(CallHistoryFiedls.IncomingCall, "true");
	 	
	 	//verify alert counts increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, 1);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(webSupportDriver, 1, outboundCallHistoryData);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(webSupportDriver, 0, inboundCallHistoryData);
	    
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(webSupportDriver, seleniumBase.getTabCount(webSupportDriver));
		dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(webSupportDriver);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(webSupportDriver);
	 	accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	 	seleniumBase.closeTab(webSupportDriver);
	 	seleniumBase.switchToTab(webSupportDriver, 1);
	 	reloadSoftphone(webSupportDriver);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("webSupportDriver", false);
	  	driverUsed.put("driver2", false);
	 	driverUsed.put("driver5", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_team_inbound(){
		System.out.println("Test case --verify_disposition_team_inbound()-- started");

		//updating the driver used
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// open Support Page and enable call disposition managed by team setting
 		loginSupport(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(webSupportDriver);
		accountIntelligentDialerTab.enableManageDispositionByTeam(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	    
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		groupsPage.saveGroup(webSupportDriver);
		
		//set call disposition required setting for Inbound Calls for team 2
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.deleteMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.saveGroup(webSupportDriver);
		
	    seleniumBase.switchToTab(webSupportDriver, 1);

	    //reload softphone
	    reloadSoftphone(webSupportDriver);
	    
	    //verify alert tab is enabled
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(webSupportDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    
	    String callerPhone = CONFIG.getProperty("prod_user_1_number");
	    
	    // Make an outbound call
	    softPhoneCalling.softphoneAgentCall(webSupportDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);

	 	//verify alert counts increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, -1);
	 	    
	    // Taking an incoming call to the agent
	    softPhoneCalling.softphoneAgentCall(driver2,  CONFIG.getProperty("qa_support_user_number"));
	 	softPhoneCalling.pickupIncomingCall(webSupportDriver);
	 	String callerName = callScreenPage.getCallerName(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//set inbound call data to verify on alerts tab
	 	HashMap<CallHistoryFiedls, String> inboundCallHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	inboundCallHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	inboundCallHistoryData.put(CallHistoryFiedls.callerPhone, CONFIG.getProperty("prod_user_1_number"));
	 	inboundCallHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	inboundCallHistoryData.put(CallHistoryFiedls.IncomingCall, "true");
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	
	 	//verify alert counts increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(webSupportDriver, 0, inboundCallHistoryData);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, 0);
	    
	 	//set call disposition required setting for Inbound Calls for team 2
	 	seleniumBase.switchToTab(webSupportDriver, seleniumBase.getTabCount(webSupportDriver));
  		groupsPage.openGroupSearchPage(webSupportDriver);
  		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
  		groupsPage.saveGroup(webSupportDriver);
	  		
	 	// open Support Page and disable call disposition prompt setting
  		dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(webSupportDriver);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(webSupportDriver);
	 	accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	 	seleniumBase.closeTab(webSupportDriver);
	 	seleniumBase.switchToTab(webSupportDriver, 1);
	 	reloadSoftphone(webSupportDriver);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("webSupportDriver", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_team_outbound(){
		System.out.println("Test case --verify_disposition_team_inbound()-- started");

		//updating the driver used
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// open Support Page and enable call disposition managed by team setting
 		loginSupport(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(webSupportDriver);
		accountIntelligentDialerTab.enableManageDispositionByTeam(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	    
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.setCallDispositionRequiredSetting(webSupportDriver, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		groupsPage.saveGroup(webSupportDriver);
		
		//set call disposition required setting for Inbound Calls for team 2
		groupsPage.openGroupSearchPage(webSupportDriver);
		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.deleteMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
		groupsPage.saveGroup(webSupportDriver);
			
	    seleniumBase.switchToTab(webSupportDriver, 1);

	    //reload softphone
	    reloadSoftphone(webSupportDriver);
	    
	    //verify alert tab is enabled	
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(webSupportDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(webSupportDriver));
	    
	    String callerPhone = CONFIG.getProperty("prod_user_1_number");
	    
	    // Make an outbound call
	    softPhoneCalling.softphoneAgentCall(webSupportDriver, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	String callerName = callScreenPage.getCallerName(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	//set inbound call data to verify on alerts tab
	 	HashMap<CallHistoryFiedls, String> outboundCallHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	outboundCallHistoryData.put(CallHistoryFiedls.callerName, callerName);
	 	outboundCallHistoryData.put(CallHistoryFiedls.callerPhone, CONFIG.getProperty("prod_user_1_number"));
	 	outboundCallHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	outboundCallHistoryData.put(CallHistoryFiedls.OutgoingCall, "true");
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(webSupportDriver);
	 	    
	    // Taking an incoming call to the agent
	    softPhoneCalling.softphoneAgentCall(driver2,  CONFIG.getProperty("qa_support_user_number"));
	 	softPhoneCalling.pickupIncomingCall(webSupportDriver);
	 	softPhoneCalling.hangupActiveCall(webSupportDriver);
	 	
	 	//verify alert counts increased
	 	softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(webSupportDriver, 0, outboundCallHistoryData);
	 	softphoneCallHistoryPage.isAlertCountIncreased(webSupportDriver, 0);
	    
	 	//set call disposition required setting for Inbound Calls for team 2
	 	seleniumBase.switchToTab(webSupportDriver, seleniumBase.getTabCount(webSupportDriver));
  		groupsPage.openGroupSearchPage(webSupportDriver);
  		groupsPage.openGroupDetailPage(webSupportDriver, CONFIG.getProperty("qa_group_5_name"), "");
  		groupsPage.addMember(webSupportDriver, CONFIG.getProperty("qa_support_user_name"));
  		groupsPage.saveGroup(webSupportDriver);
	  		
	 	// open Support Page and disable call disposition prompt setting
  		dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(webSupportDriver);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(webSupportDriver);
	 	accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
	 	seleniumBase.closeTab(webSupportDriver);
	 	seleniumBase.switchToTab(webSupportDriver, 1);
	 	reloadSoftphone(webSupportDriver);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("webSupportDriver", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_member_no_team(){
		System.out.println("Test case --verify_disposition_team_inbound()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and enable call disposition managed by team setting
		accountIntelligentDialerTab.enableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.enableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.None);
		groupsPage.saveGroup(driver1);
			
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);

		//updating the driver used
		WebDriver qaDriver = getDriver();
		SFLP.softphoneLogin(qaDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);	
	    
	    //verify alert tab is enabled	
		softphoneCallHistoryPage.openCallsHistoryPage(qaDriver);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(qaDriver));
	    softphoneCallHistoryPage.clickGroupCallsLink(qaDriver);
	    softphoneCallHistoryPage.idleWait(5);
	    assertFalse(softphoneCallHistoryPage.isAlertTabDisabled(qaDriver));
	    
	    //verify alert tab is disabled	
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver1));
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    softphoneCallHistoryPage.idleWait(5);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver1));
	  		
	 	// open Support Page and disable call disposition prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	
		//set call disposition required setting for All Calls for team 1
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.verifyTeamDispositionSettingInvisible(driver1);;
		
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	qaDriver.quit();
	 	qaDriver = null;
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("standardUserDriver", false);
	}

	//Verify user should not allow to make calls by call forwarding when have missed disposition call
	@Test(groups = {"Regression"})
	public void verify_disposition_forwrding_device_all(){
		System.out.println("Test case --verify_disposition_forwrding_device_all()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		seleniumBase.switchToTab(driver1, 1);
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		// open Support Page and enable call disposition and prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	   
    	//making a call to an agent which has forwarding enable and accept the call from forwarding device
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
	 	//verify call back button visible
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
	 	seleniumBase.idleWait(2);

	 	//click call back button immediately and verify that the call disposition missing alert dialogue box should come
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//open the recent call entry and provide the disposition
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callScreenPage.clickDispositionWarning(driver1);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//Verifying that user is able to take the call once dispositions are set
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
	 	//verify call back button visible
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
	 	
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	 	
	 	// open Support Page and disable call disposition and prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	//Verify user should not allow to make calls by call forwarding stay connected when have missed disposition call
	@Test(groups = {"Regression"})
	public void verify_disposition_with_stay_connected(){
		System.out.println("Test case --verify_disposition_with_stay_connected()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		seleniumBase.switchToTab(driver1, 1);
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		//enable stay connected setting
		softPhoneSettingsPage.enableStayConnectedSetting(driver1);
		
		// open Support Page and enable call disposition and prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	   
    	//making a call from agent which has forwarding enable and accept the call from forwarding device
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		
	 	//verify call back button visible
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
	 	seleniumBase.idleWait(2);
	 	
	 	// verifying that call forwarding device is not disconnected
		softPhoneCalling.isHangUpButtonVisible(driver2);

	 	//click call back button immediately and verify that the call disposition missing alert dialogue box should come
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	callScreenPage.isMissingDispositionWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
		// Setting call forwarding OFF
	 	//disable stay connected setting
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableStayConnectedSetting(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	 	
	 	// open Support Page and disable call disposition and prompt setting
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}

	//Verify Call Disposition window dismissed on selecting any value when Call Tools Off
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_selected(){
		System.out.println("Test case --verify_disposition_selected()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Open recent call history entry and select a disposition
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callDisposition = callToolsPanel.selectDisposition(driver4, 0);
		
		//verify that selected disposition is appearing on caller details
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
		
		//open the caller detail in salesforce
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
	
		//Open recent call entry for which the disposition is selected
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that task has call disposition selected
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //again select the same disposition and verify under call screen
	    callToolsPanel.selectDisposition(driver4, 0);
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
	
		//Open caller detail page in salesforce and open the recent call entry
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that task has call disposition selected
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    // select different disposition and verify under call screen
	    callDisposition = callToolsPanel.selectDisposition(driver4, 1);
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
		
		//Open caller detail page in salesforce and open the recent call entry
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that task has call disposition selected
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver4", false);
	}

	//Verify Call Disposition window dismissed on selecting any value when Call Tools ON
	@Test(groups = {"MediumPriority"})
	public void verify_selected_disposition_call_tools_enabled(){
		System.out.println("Test case --verify_selected_disposition_call_tools_enabled()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call tools setting
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableCallToolsSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    reloadSoftphone(driver4);
		
		//open the second recent call history entry
		softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver4);
		
		// select a disposition and change the subject of the activity
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.clickCallToolsIcon(driver4);
		callToolsPanel.enterCallNotesSubject(driver4, callSubject);
		String callDisposition = "Left Voicemail";
 		callToolsPanel.selectDispositionFromCallTools(driver4, callDisposition);
		callToolsPanel.clickCallNotesSaveBtn(driver4);
		
		//verify that the selected disposition is appearing under caller detail
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
		
		//Opening the caller detail page in salesforce
		callScreenPage.openCallerDetailPage(driver4);
	
		//open recent call entry for the caller in the salesforce
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that the selected disposition is appearing associated with the call entry in sfdc
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //again select the same disposition and verify under call screen
 		callToolsPanel.clickCallToolsIcon(driver4);
 		callToolsPanel.enterCallNotesText(driver4, callSubject);
 		callToolsPanel.selectDispositionFromCallTools(driver4, callDisposition);
 		callToolsPanel.clickCallNotesSaveBtn(driver4);
		
		//verify that the selected disposition is appearing under caller detail
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
	
		//open recent call entry for the caller in the salesforce
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that the selected disposition is appearing associated with the call entry in sfdc
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //select a different disposition and verify under call screen
 		callToolsPanel.clickCallToolsIcon(driver4);
 		callDisposition = "Contacted";
 		callToolsPanel.selectDispositionFromCallTools(driver4, callDisposition);
 		callToolsPanel.clickCallNotesSaveBtn(driver4);
		
		//verify that the selected disposition is appearing under caller detail
		assertEquals(callScreenPage.getCallDisposition(driver4), callDisposition);
		
		//open recent call entry for the caller in the salesforce
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		
		//Verifying that the selected disposition is appearing associated with the call entry in sfdc
	    assertEquals(sfTaskDetailPage.getDisposition(driver4), callDisposition);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
		// open Support Page and disable call tools setting
 	 	loginSupport(driver4);
 	 	dashboard.clickAccountsLink(driver4);
 	 	System.out.println("Account editor is opened ");
 	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
 		accountIntelligentDialerTab.disableCallToolsSetting(driver4);
 	 	accountIntelligentDialerTab.saveAcccountSettings(driver4);
 	 	seleniumBase.closeTab(driver4);
 	 	seleniumBase.switchToTab(driver4, 1);
 	 	reloadSoftphone(driver4);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver4", false);
	}
	
	@AfterClass(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void disableDispostionSettings(){
		// open Support Page and disable call disposition prompt setting
		beforeMethod();
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	}
	
	@AfterMethod(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void afterMethod(ITestResult result){
		if(result.getStatus() == 2 || result.getStatus() == 3) {
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			disableDispostionSettings();
			aa_AddCallersAsContactsAndLeads();
			
			if(result.getName().equals("verify_spam_blocked_number()")) {
				softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
				callScreenPage.clickUnblockButton(driver1);
			}else if(result.getName().equals("verify_spam_blocked_number()")) {
				// Setting call forwarding OFFverify_disposition_forwrding_device
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(driver3);
				softPhoneSettingsPage.disableCallForwardingSettings(driver3);
			}else if(result.getName().equals("verify_disposition_forwrding_device_all") || result.getName().equals("verify_disposition_with_stay_connected")){
				// Setting call forwarding OFF
				System.out.println("Setting call forwarding OFF");
				softPhoneSettingsPage.clickSettingIcon(driver1);
				softPhoneSettingsPage.disableCallForwardingSettings(driver1);
			}else if(result.getName().equals("verify_selected_disposition_call_tools_enabled") || result.getName().equals("verify_call_description_box()")){
				initializeDriverSoftphone("driver1");
				driverUsed.put("driver4", true);
				
				// open Support Page and disable call tools setting
		 	 	loginSupport(driver4);
		 	 	dashboard.clickAccountsLink(driver4);
		 	 	System.out.println("Account editor is opened ");
		 	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		 		accountIntelligentDialerTab.disableCallToolsSetting(driver4);
		 	 	accountIntelligentDialerTab.saveAcccountSettings(driver4);
		 	 	seleniumBase.closeTab(driver4);
		 	 	seleniumBase.switchToTab(driver4, 1);
		 	 	reloadSoftphone(driver4);
		 	 	
		 	 	//Setting driver used to false as this test case is pass
				driverUsed.put("driver4", false);
			}
			
			//Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);
		}
	}
	
	public void addCallerAsMultiple() {
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
	    
	    String existingContact = CONFIG.getProperty("prod_user_3_name") + " Automation";
	    
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    // Add caller as multiple contact
	    callScreenPage.closeErrorBar(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    
	   //verify caller is multiple
	   softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
	   // Calling from Agent's SoftPhone
	   softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(3);
	 	
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);											  
	}
}