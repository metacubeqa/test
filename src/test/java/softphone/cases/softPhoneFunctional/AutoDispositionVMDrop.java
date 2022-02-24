/**
 * @author Abhishek Gupta
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.accounts.AccountIntelligentDialerTab;

public class AutoDispositionVMDrop extends SoftphoneBase{

	@BeforeMethod(groups = { "Regression", "MediumPriority" })
	public void beforeMethod(ITestResult result) {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and disable call disposition prompt setting
		if (seleniumBase.getTabCount(driver1) == 1) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			System.out.println("Account editor is opened ");
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	}
	
	//Verify new section Auto Disposition Voicemail Drops on Intelligent dialer tab
	//Verify Auto Disposition Voicemail Drops with None , missing disposition alert displayed
	@Test(groups = {"Regression"})
	public void verify_auto_disp_none_with_disposition_alert(){
		System.out.println("Test case --verify_auto_disp_none_with_disposition_alert()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and verify Auto disposition on VM Drops default settings
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.verifyDefaultAutoDispositionVMSection(driver1);
		
		//enable diposition prompt setting and required disposition setting for outbound calls
		//Select none as disposition to be selected when VM dropped
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 		 	
	 	//verify no disposition on call history page, task in all activities
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, null);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	softphoneCallHistoryPage.verifyHistoCallDispNotVisible(driver1, 0);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry for which the disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying that task has no disposition selected on sfdc task as well
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), " ");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	    //try to make another call without giving any disposition
	    //Verifying user not able to make call until disposition is set
	    softPhoneCalling.enterNumberAndDial(driver1, callerPhone);
 	
	    //verify that review call window appears
	    callScreenPage.isMissingDispositionWindowVisible(driver1);
	    callScreenPage.clickReviewCallsButton(driver1);
	    
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}

	//Verify Auto Disposition Voicemail Drops value should be set when agent drop automated vm when caller not answered
	@Test(groups = {"Regression"})
	public void verify_auto_diposition_set(){
		System.out.println("Test case --verify_auto_diposition_set()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		String disposition = "Contacted";
		
		// open Support Page and disable call disposition prompt setting
		// Select a disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, disposition);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is not appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 		 	
	 	//verify disposition on call history page, task in all activities
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, disposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), disposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry for which the disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying that sfdc task has call disposition selected
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), disposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}

	//Verify when None selected as Auto Disposition Voicemail Drop setting with Call Disposition Prompt OFF
	@Test(groups = {"Regression"})
	public void verify_auto_disp_none_without_disposition_alert(){
		System.out.println("Test case --verify_auto_disp_none_without_disposition_alert()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		// Select none as disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 		 	
	 	//verify no disposition on call history page, task in all activities
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, null);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	softphoneCallHistoryPage.verifyHistoCallDispNotVisible(driver1, 0);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
	 	//select disposition
	 	String disposition = callToolsPanel.selectDisposition(driver1, 2);
	 	
	 	//verify manually selected disposition on call history page, task in all activities
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, disposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), disposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), disposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry for which the disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying that sfdc task has call disposition selected
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), disposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	//Verify selected disposition value assigned automatically for All outbound calls with Call Disposition Prompt ON
	//Verify user can select disposition manually after giving VM drop  if disposition already selected in Auto Disposition Voicemail Drop
	@Test(groups = {"Regression"})
	public void verify_auto_disp_selected_outgoing_call(){
		System.out.println("Test case --verify_auto_disp_selected_outgoing_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		String disposition = "Contacted";
		
		//enable diposition prompt setting and required disposition setting for outbound calls
		// Select a disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, disposition);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is not appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 		 	
	 	//verify disposition is selected in call task, call details and history page
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, disposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), disposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), disposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry and verify disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), disposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 	
	 	//Select disposition for already selected disposition call
	 	softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	 	
	 	//select disposition
	 	String newDisposition = callToolsPanel.selectDisposition(driver1, 2);
	 	
	 	//verify alert counts
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, newDisposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), newDisposition);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	//Verify user can select disposition manually before giving VM drop  if disposition already selected in Auto Disposition Voicemail Drop
	@Test(groups = {"Regression"})
	public void verify_manual_disp_selected_outgoing_call(){
		System.out.println("Test case --verify_manual_disp_selected_outgoing_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		String disposition = "Contacted";
		
		// open Support Page and enable call disposition prompt setting
		// Select a disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, disposition);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	    
	    //select disposition manually
	 	String manualDisposition = callToolsPanel.selectDisposition(driver1, 2);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is not appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 		 	
	 	//verify disposition is selected in call task, call details and history page
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, manualDisposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), manualDisposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), manualDisposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry and verify manually selected disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), manualDisposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}

	//Verify selected disposition added with call forwarding ON
	@Test(groups = {"Regression"})
	public void verify_auto_disp_selected_call_forwarding(){
		System.out.println("Test case --verify_auto_disp_selected_call_forwarding()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
		String callerPhone = CONFIG.getProperty("qa_user_3_number");
		String disposition = "Contacted";
		
		// enable diposition prompt setting and required disposition setting for outbound calls
		// Select a disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Outbound);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, disposition);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	 	//verifying that no problem occurs while making outbound call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	    
	    // Accepting call from forwarding device
	    softPhoneCalling.pickupIncomingCall(driver2);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupIfInActiveCall(driver1);
	 	
	 	//verify that disposition required warning message is not appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 		 	
	 	//verify disposition is selected in call task, call details and history page
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, disposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), disposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), disposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry and verify disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), disposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 	
		// Setting call forwarding Off
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}

	//Verify selected disposition added with call forwarding ON stay connected
	@Test(groups = {"Regression"})
	public void verify_auto_disp_selected_stay_connected(){
		System.out.println("Test case --verify_auto_disp_selected_stay_connected()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		softPhoneSettingsPage.enableStayConnectedSetting(driver1);
		
		String callerPhone = CONFIG.getProperty("qa_user_3_number");
		String disposition = "Contacted";
		
		// open Support Page and disable call disposition prompt setting
		// Select a disposition to be selected when VM dropped
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, disposition);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	 	//verifying that no problem occurs while making outbound call
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	    
	    // Accepting call from forwarding device
	    softPhoneCalling.pickupIncomingCall(driver2);
	 		
	 	// receiving call from receiver
	 	callToolsPanel.dropFirstVoiceMail(driver1);
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	 	
	 	//hangup active call
	 	seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupIfInActiveCall(driver2);
	 	
	 	//verify that disposition required warning message is not appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 		 	
	 	//verify disposition is selected in call task, call details and history page
	 	softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
	 	softPhoneActivityPage.verifyTaskDisposition(driver1, callSubject, disposition);
	 	assertEquals(callScreenPage.getCallDisposition(driver1), disposition);
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallDisposition(driver1, 0), disposition);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//Open recent call entry and verify disposition is selected
	 	callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), disposition);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 	
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 	
		// Setting call forwarding Off
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@AfterClass(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void disableDispostionSettings(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Support Page and disable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.disableManageDispositionByTeam(driver1);
		accountIntelligentDialerTab.selectAutoDispositionVM(driver1, "(none)");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	}
}