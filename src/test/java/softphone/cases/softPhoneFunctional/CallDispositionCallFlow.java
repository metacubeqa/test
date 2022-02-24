/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.cases.softPhoneFunctional.callTools.CallTemplate;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.callFlows.CallFlowPage;

/**
 * @author Abhishek
 *
 */
public class CallDispositionCallFlow extends SoftphoneBase{
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_unavailable_call_flow(){
		System.out.println("Test case --verify_disposition_unavailable_call_flow()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectUnavailableCallFlow(driver1, CONFIG.getProperty("qa_call_flow_call_conference"));
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver4);
	    reloadSoftphone(driver1);
	   
	    //take call three times
	    for(int i= 0; i<3; i++) {
	    	//making a call to a user and declining it so that call goes to unavailable call flow
			softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
			softPhoneCalling.declineCall(driver1);
			softPhoneCalling.pickupIncomingCall(driver4);

			//hanging up with caller 1
			System.out.println("hanging up with caller");
			softPhoneCalling.hangupActiveCall(driver4);

		 	//verify that disposition required warning message is appearing
		 	callScreenPage.verifyDispositionRequiredWarning(driver4);
		 	
		 	//verify call back button visible
		 	softPhoneCalling.isCallBackButtonVisible(driver2);
		 	softPhoneCalling.idleWait(2);
	    }	 	
	    
		//verify that alert count is zero now
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver4); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver4);
	 	
	 	//now disable unavailable call flow setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.selectUnvailableFlowSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver4);
	    reloadSoftphone(driver1);
	 	
		//verify that alert count is still zero	
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver4); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver4);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_disposition_required(){
		System.out.println("Test case --verify_call_flow_disposition_required()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver4);
	      
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		String callerName = callScreenPage.getCallerName(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.idleWait(5);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver4);
	 	assertFalse(callScreenPage.isCloseWarningbuttonVisible(driver1));
	    
		//verify that entry is there in the alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver4); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, 0);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver4, 0), callerName);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority", "addSteps"})
	public void verify_call_flow_disposition_not_required(){
		System.out.println("Test case --verify_call_flow_disposition_not_required()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_name");
		
		CallTemplate multiMatchRequired  =  new CallTemplate();
		
		multiMatchRequired.addCallerAsMultiple();

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user1, "15");
		callFlowPage.checkCallDispositionCheckBox(driver1);
		callFlowPage.saveCallFlowSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver4);
	      
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver5, callFlowNumber);
		softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.idleWait(5);
	    
	 	//verify that alert count is zero now
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver4); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver4);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	aa_AddCallersAsContactsAndLeads();
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority", "addSteps"})
	public void verify_transfer_call_flow_disposition_not_required(){
		System.out.println("Test case --verify_call_flow_disposition_not_required()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String user1			= CONFIG.getProperty("qa_user_2_name");

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.User, user1, "15");
		callFlowPage.checkCallDispositionCheckBox(driver1);
		callFlowPage.saveCallFlowSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver4);
	      
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //tranfer call to Conference Call Flow
	    softPhoneCalling.transferToNumber(driver1, callFlowNumber);
	    
	    //accept call from the user mentioned in call flow
	    softPhoneCalling.pickupIncomingCall(driver4);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.idleWait(5);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver4);
	    
	 	//verify that alert count is zero now
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver4); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver4);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority", "addSteps"})
	public void verify_call_flow_forwarding_disposition_not_required(){
		System.out.println("Test case --verify_call_flow_forwarding_disposition_required()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String userGroup		= CONFIG.getProperty("qa_group_1_name").trim();
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.CallQueue, userGroup, "15");
		callFlowPage.checkCallDispositionCheckBox(driver1);
		callFlowPage.saveCallFlowSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	      
    	//making a call to a user and declining it so that call goes to unavailable call flow 
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
		softPhoneCalling.softphoneAgentCall(driver4, callFlowNumber);
	 	softPhoneCallQueues.pickCallFromQueue(driver1);
		softPhoneCalling.pickupIncomingCall(driver2);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.idleWait(5);
	    
		//verify that no entry is there in the alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver4, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver4);
	 	
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"MediumPriority", "addSteps"})
	public void verify_call_flow_forwarding_disposition_required(){
		System.out.println("Test case --verify_call_flow_forwarding_disposition_not_required()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// open Support Page and enable call disposition prompt setting
		String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
		String callFlowNumber 	= CONFIG.getProperty("qa_call_flow_to_add_steps_number");
		String userAccout		= CONFIG.getProperty("qa_user_account");
		String userGroup		= CONFIG.getProperty("qa_group_1_name").trim();
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));

		//Opening manage call flow and searching call flow
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.Inbound);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.navigateToManageCallFlow(driver1);
		callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
		
	    callFlowPage.removeAllCallFlowSteps(driver1);
		callFlowPage.selectSFConnectUser(driver1, "");
		callFlowPage.addDialStepToRDNAUsers(driver1, CallFlowPage.DialCallRDNATOCat.CallQueue, userGroup, "15");
		callFlowPage.saveCallFlowSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	      
    	//making a call to a user and declining it so that call goes to unavailable call flow 
	    softPhoneCallQueues.subscribeQueue(driver1, CONFIG.getProperty("qa_group_1_name"));
		softPhoneCalling.softphoneAgentCall(driver4, callFlowNumber);
	 	softPhoneCallQueues.pickCallFromQueue(driver1);
		String callerName = callScreenPage.getCallerName(driver1);
		softPhoneCalling.pickupIncomingCall(driver2);

		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.idleWait(5);
	    
		//verify that alert count is 1
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	assertEquals(softphoneCallHistoryPage.getHistoryCallerName(driver1, 0), callerName);
	 	
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	dashboard.clickAccountsLink(driver1);
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	//@AfterClass(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
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
			disableDispostionSettings();
			aa_AddCallersAsContactsAndLeads();
			
			if(result.getName().equals("verify_disposition_unavailable_call_flow()")) {
				// open Support Page and disable call disposition prompt setting
				loginSupport(driver1);
				dashboard.clickAccountsLink(driver1);
				accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
				accountIntelligentDialerTab.selectUnvailableFlowSetting(driver1);
				accountIntelligentDialerTab.saveAcccountSettings(driver1);
				seleniumBase.closeTab(driver1);
				seleniumBase.switchToTab(driver1, 1);
				reloadSoftphone(driver1);
			}
		}
	}
	
	@AfterMethod(groups = {"addSteps"})
	public void _afterMethod(ITestResult result) {
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			// remove all call flow steps
			String callFlowName 	= CONFIG.getProperty("qa_call_flow_to_add_steps");
			String userAccout		= CONFIG.getProperty("qa_user_account");
			loginSupport(driver1);
			dashboard.navigateToManageCallFlow(driver1);
			callFlowPage.searchCallFlow(driver1, callFlowName, userAccout);
			callFlowPage.clickSelectedCallFlow(driver1, callFlowName);
			callFlowPage.removeAllCallFlowSteps(driver1);
		}
	}
}