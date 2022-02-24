package softphone.cases.softPhoneFunctional;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.accounts.AccountIntelligentDialerTab;

public class ReportThisCall extends SoftphoneBase{
	
	@Test(groups = {"MediumPriority"})
	public void verify_report_this_call_when_call_not_picked(){
		System.out.println("Test case --verify_report_this_call_when_call_not_picked()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.isDeclineButtonVisible(driver1);
		
		//Verify that report this call option is not visible until call is connected
		reportThisCallPage.verifyReportThisCallNotVisible(driver1);
		
		//pickup incoming call
		softPhoneCalling.pickupIncomingCall(driver1);
	
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver1);
	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}

	@Test(groups = {"MediumPriority"})
	public void verify_report_this_call_with_forwrding(){
		System.out.println("Test case --verify_disposition_forwrding_device()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
		
	    //take call three times
	    
    	//making a call to a user and declining it so that call goes to unavailable call flow 
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		
		//pickup incoming call
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//report the call
		reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Call Report Test Notes");
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
	 	//verify call back button visible
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
	 	softPhoneCalling.idleWait(2);	
	 	
	 	// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver4", false);
	}
	
	@Test(groups={ "MediumPriority"})
	  public void verify_report_this_call_contact_card()
	  {
		System.out.println("Test case --verify_report_this_call_contact_card-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String contactName = CONFIG.getProperty("prod_user_1_name").trim() + " Automation";
		
		//open contact card details
		softPhoneContactsPage.openSfContactDetails(driver1, contactName);	
		callScreenPage.getCallerName(driver1);
	
		//Verify that report this call option is not visible
		reportThisCallPage.verifyReportThisCallNotVisible(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	  }  
	
	@Test(groups = {"MediumPriority"})
	public void verify_report_this_call_unauthorised_error(){
		System.out.println("Test case --verify_report_this_call_unauthorised_error()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone = CONFIG.getProperty("qa_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallToolsSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.setCallDispositionRequiredSetting(driver1, AccountIntelligentDialerTab.dispositionReqStates.All);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, callerPhone);
	 		
	 	// receiving call from receiver
	 	softPhoneCalling.pickupIncomingCall(driver1);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupActiveCall(driver1);

	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyDispositionRequiredWarning(driver1);
	 	
	 	//Logout from softphone
	 	softPhoneSettingsPage.logoutSoftphone(driver1);
	 	
	 	//login to softphone
	 	SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	
		//report the call
		reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Call Report Test Notes");
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@AfterMethod(groups = {"Regression", "MediumPriority"}, alwaysRun = true)
	public void disableDispostionSettings(ITestResult result){	
		
		if(result.getName().equals("verify_report_this_call_unauthorised_error") && result.getStatus() == 2) {
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
		 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
		 	seleniumBase.closeTab(driver1);
		 	seleniumBase.switchToTab(driver1, 1);
		 	reloadSoftphone(driver1);
		 	
		 	//Setting driver used to false as this test case is pass
		  	driverUsed.put("driver1", false);
		}
	}
}