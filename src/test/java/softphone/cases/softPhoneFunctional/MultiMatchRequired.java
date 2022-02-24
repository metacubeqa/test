package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftphoneCallHistoryPage.CallHistoryFiedls;

public class MultiMatchRequired extends SoftphoneBase{
	
	@BeforeClass(groups={"Regression", "MediumPriority"})
	public void createMultiMatchContact(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		aa_AddCallersAsContactsAndLeads();
		
		String callerPhone 			= CONFIG.getProperty("prod_user_1_number");
		String callerFirstName		= CONFIG.getProperty("prod_user_1_name");
		String existingContactName	= CONFIG.getProperty("prod_user_3_name");
		String existingContact 		= CONFIG.getProperty("prod_user_3_number");
	    
		addCallerAsMultiple(driver1, driver2, callerPhone, callerFirstName, existingContactName, existingContact);
		
		
		existingContact			= CONFIG.getProperty("prod_user_2_number");
		existingContactName 	= CONFIG.getProperty("prod_user_2_name");
		callerPhone 			= CONFIG.getProperty("qa_user_2_number");
		callerFirstName			= CONFIG.getProperty("qa_user_2_name");
	    
		addCallerAsMultiple(driver1, driver4, callerPhone, callerFirstName, existingContactName, existingContact);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@BeforeMethod(groups = {"Regression", "MediumPriority"})
	public void beforeMethod(ITestResult result){
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			// open Support Page and disable call disposition prompt setting
			if(seleniumBase.getTabCount(driver1) == 1) {
				loginSupport(driver1);
			 	dashboard.clickAccountsLink(driver1);
			 	System.out.println("Account editor is opened ");
			 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			}
			seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_miss_Call(){
		System.out.println("Test case --verify_multi_match_required_setting()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callerPhone = CONFIG.getProperty("qa_user_1_number");	
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	//Calling from multiple matched contact
	    softPhoneCalling.softphoneAgentCall(driver2, callerPhone);
					
	    //declining that call
	 	softPhoneCalling.declineCall(driver1);	
	 	softPhoneCalling.hangupActiveCall(driver2);
								
	    //verify alert tab is there and it is blank
	 	seleniumBase.idleWait(5);
	    softphoneCallHistoryPage.switchToAlertsTab(driver1);
	    softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	    softphoneCallHistoryPage.idleWait(2);
	    assertTrue(softphoneCallHistoryPage.isCallerHistoryBlank(driver1));
	 	
	    //verify alert tab is there and it is blank
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyDispositionReqWarningNotVisible(driver1);
	 	
	 	// Verifying that user is able to make another call
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_transfer(){
		System.out.println("Test case --verify_multi_match_required_transfer()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);	
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");			
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	 	//Calling to multiple matched contact
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
								
	 	//transfer call to another caller
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_name"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
	 	
	    //Now hangup the active call
	 	softPhoneCalling.hangupActiveCall(driver4);
	 	softPhoneCalling.isCallBackButtonVisible(driver2);
								
	 	//Open call history page and verify that entry is appearing in alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.MultiMatchAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts has increased
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//Verifying user not able to make call until caller is matched
	 	softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	 	
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyMultiMatchRequiredWarning(driver1);
	 	
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	callScreenPage.isMultiMatchWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 
	 	//Select a caller from multiple caller dropdown
	 	softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	callToolsPanel.isRelatedRecordsIconVisible(driver1);
	 	
	 	//verify that alert count is not present
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1); 
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, -1);
	 	softphoneCallHistoryPage.isCallerHistoryBlank(driver1);
	 	
	 	// Verifying that user is able to make another call after selecting a caller
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"Regression"})
	public void verify_multiple_disposition_required_setting(){
		System.out.println("Test case --verify_disposition_multiple_required_setting()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");		
	    
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
								  
	 	softPhoneCalling.pickupIncomingCall(driver2);
								
	 	softPhoneCalling.hangupActiveCall(driver1);
								
	 	
	 	//verify that disposition required warning message is appearing
	 	callScreenPage.verifyMultipleRequiredWarning(driver1);
	 	assertFalse(callScreenPage.isCloseWarningbuttonVisible(driver1));
	 	
	 	//click on disposition warning message and verify that disposition menu gets open
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();	
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	callHistoryData.put(CallHistoryFiedls.MultiMatchAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	 	
	 	//verify alert counts
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	seleniumBase.idleWait(3);
	 	
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
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_group_call(){
		System.out.println("Test case --verify_multi_match_required_group_call()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
	    // Calling from Agent's SoftPhone
		String queueName 	= CONFIG.getProperty("qa_group_1_name");
		String queuePhoneNumber = CONFIG.getProperty("qa_group_1_number");		
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);	
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

	 	//Calling to multiple matched contact
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCalling.softphoneAgentCall(driver2, queuePhoneNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver2);
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
	 	seleniumBase.idleWait(2);
	 	
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyMultiMatchRequiredWarning(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//Open call history page and verify that entry is appearing in alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	//softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1);
	 	
	 	//verify alert counts has increased
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//Verifying user not able to make call until caller is matched
	 	softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	callScreenPage.isMultiMatchWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	 	
	 	//Select a caller from multiple caller dropdown
	 	softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	callToolsPanel.isRelatedRecordsIconVisible(driver1);
	 	
	 	// Verifying that user is able to make another call after selecting a caller
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);	
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_missed_Call(){
		System.out.println("Test case --verify_multi_match_required_missed_Call()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callerPhone = CONFIG.getProperty("prod_user_1_number");	
	 	String callerName = CONFIG.getProperty("prod_user_1_name") + " Automation";
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	//Calling from multiple matched contact
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	    seleniumBase.idleWait(10);
					
	    //declining that call
	 	softPhoneCalling.hangupActiveCall(driver1);	
								
	    //verify alert tab is there and it is blank
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.switchToAlertsTab(driver1);
	    softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	    softphoneCallHistoryPage.idleWait(2);
	    softphoneCallHistoryPage.verifyHistoryMultiMatchAlert(driver1, 0);
	 	
	    //verify alert tab is there and it is blank
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
	    //Verifying user not able to make call until disposition is set
	 	softPhoneContactsPage.salesforceSearchAndCall(driver1, callerName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
	  	
	 	//Select caller from the drop down
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// Verifying that user is able to make another call after selecting a caller
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_message_caller_selected(){
		System.out.println("Test case --verify_multi_match_message_caller_selected()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callerPhone = CONFIG.getProperty("prod_user_1_number");			
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	 	//Calling to multiple matched contact
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	seleniumBase.idleWait(1);
								
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyMultiMatchRequiredWarning(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//Select a caller from multiple caller dropdown
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts - 1);
	 	
	 	//verify alert tab is there and it is blank
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.switchToAlertsTab(driver1);
	    softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	    softphoneCallHistoryPage.idleWait(2);
	    assertTrue(softphoneCallHistoryPage.isCallerHistoryBlank(driver1));
	 	
	 	// open Support Page and disable call disposition prompt setting
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_direct_queue_Calls(){
		System.out.println("Test case --verify_multi_match_direct_queue_Calls()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callerPhone = CONFIG.getProperty("prod_user_1_number");	
		String queueName 	= CONFIG.getProperty("qa_group_1_name");
		String queuePhoneNumber = CONFIG.getProperty("qa_group_1_number");	
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	 	//Calling to multiple matched contact
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);				  
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//Calling to multiple matched contact
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCalling.softphoneAgentCall(driver2, queuePhoneNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver2);
	 	seleniumBase.idleWait(2);
	 	
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyMultiMatchRequiredWarning(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts + 1);
	 	
	 	//Open call history page and verify that entry is appearing in alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	 	
	 	//verify alert tab is there and it is blank
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.switchToAlertsTab(driver1);
	    softphoneCallHistoryPage.veifyAlertTabActive(driver1);
	    softphoneCallHistoryPage.idleWait(2);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 0);
	    int newHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	 	
	 	//Select a caller from multiple caller dropdown
	 	softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	//verify call history counts has decreased by 1
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, newHistoryCounts - 2);
	 	
	 	//Select a caller from multiple caller dropdown
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	//verify call history counts has decreased by 2
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts - 1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_forwarding(){
		System.out.println("Test case --verify_multi_match_required_forwarding()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);	

		String callerPhone = CONFIG.getProperty("qa_user_2_number");	
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 	
	 	// Setting call forwarding ON
	 	System.out.println("Setting call forwarding ON");
	 	softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
	 	
	 	//Calling from multiple matched contact
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    seleniumBase.idleWait(5);
					
	    //declining that call
	 	softPhoneCalling.hangupActiveCall(driver1);	
								
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.verifyMultiMatchRequiredWarning(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//click on call back button and verify user is not able to call back
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	callScreenPage.isMultiMatchWindowVisible(driver1);
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	
	 	//Select caller from the drop down
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_hide_group_history(){
		System.out.println("Test case --verify_multi_match_direct_queue_Calls()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");

		String callerPhone = CONFIG.getProperty("prod_user_1_number");	
		String queueName 	= CONFIG.getProperty("qa_group_1_name");
		String queuePhoneNumber = CONFIG.getProperty("qa_group_1_number");	
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.enableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	 	//Calling to multiple matched contact
	    int historyCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);				  
	 	softPhoneCalling.pickupIncomingCall(driver2);		
	 	seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts);
	 	
	 	//Calling to multiple matched contact
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCalling.softphoneAgentCall(driver2, queuePhoneNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver2);
	 	seleniumBase.idleWait(2);
	 	
	 	//verify call history counts has increased
	 	softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, historyCounts + 1);
	 	
	 	//Open call history page and verify that entry is appearing in alert tab
	 	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	 	softphoneCallHistoryPage.switchToAlertsTab(driver1);
	 	softphoneCallHistoryPage.isAlertCountIncreased(driver1, 1);
	 	
	 	//Select a caller from multiple caller dropdown
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_multiple_required_new_call(){
		System.out.println("Test case --verify_disposition_multiple_required_setting()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callerPhone = CONFIG.getProperty("qa_user_1_number");		
	    
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver2, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	seleniumBase.idleWait(2);
								
	    // Calling from Agent's SoftPhone without setting disposition and selecting caller
	    softPhoneCalling.enterNumberAndDial(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
	    //verify the review alert message
	    assertEquals(callScreenPage.getAlertText(driver1), "You currently have calls that need your attention. To continue making outbound calls, ensure that you select a caller for multi-match selection for every call and enter a disposition for every call. To review these calls go to Call History and click on the Alert tab.");
	    callScreenPage.clickReviewCallsButton(driver1);
	    
	 	//setting up the disposition for calls
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	seleniumBase.idleWait(3);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disposition_multiple_required_vm(){
		System.out.println("Test case --verify_disposition_multiple_required_vm()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_1_number");		
	    
		// open Support Page and enable call disposition prompt setting
		accountIntelligentDialerTab.enableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.enableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
		seleniumBase.idleWait(2);
	 	callToolsPanel.dropVoiceMailByName(driver1, "Automation Long VM (Don't Delete)");
	 	seleniumBase.idleWait(2);
	 	
	 	//Click on call back button
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	softPhoneCalling.isHangUpButtonVisible(driver2);
	    
	    //verify the review alert message
	    assertEquals(callScreenPage.getAlertText(driver1), "You currently have calls that need your attention. To continue making outbound calls, ensure that you select a caller for multi-match selection for every call and enter a disposition for every call. To review these calls go to Call History and click on the Alert tab.");
	    callScreenPage.clickReviewCallsButton(driver1);
	    
	    //Verify the entry on alerts tab
	    HashMap<CallHistoryFiedls, String> callHistoryData = new HashMap<CallHistoryFiedls, String>();	
	 	callHistoryData.put(CallHistoryFiedls.callerPhone, callerPhone);
	 	callHistoryData.put(CallHistoryFiedls.dispositionAlert, "true");
	 	callHistoryData.put(CallHistoryFiedls.MultiMatchAlert, "true");
	 	softphoneCallHistoryPage.verifyCallHistoryTabDataByIndex(driver1, 0, callHistoryData);
	    
	    // Calling from Agent's SoftPhone without setting disposition and selecting caller
	    softPhoneCalling.enterNumberAndDial(driver1, CONFIG.getProperty("prod_user_1_number"));
	    assertEquals(callScreenPage.getAlertText(driver1), "You currently have calls that need your attention. To continue making outbound calls, ensure that you select a caller for multi-match selection for every call and enter a disposition for every call. To review these calls go to Call History and click on the Alert tab.");
	    
	 	//setting up the disposition for calls
	    callScreenPage.clickReviewCallsButton(driver1);
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callToolsPanel.selectDisposition(driver1, 0);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	seleniumBase.idleWait(3);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
	 	accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	 		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_multi_match_required_click_to_call(){
		System.out.println("Test case --verify_multi_match_required_click_to_call()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callerPhone = CONFIG.getProperty("prod_user_1_number");			
  
	    // open Support Page and enable multi match required setting
	 	accountIntelligentDialerTab.enableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);	
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	 	//Calling to multiple matched contact
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);					  
	 	softPhoneCalling.pickupIncomingCall(driver2);		
	 	softPhoneCalling.hangupActiveCall(driver1);

	 	//open salesforce page click the contact phone number to call
	 	sfCampaign.openSalesforceCampaignPage(driver1);
		
		//search the contact
		sfSearchPage.enterGlobalSearchText(driver1, "CTC_2_Contact Auto");
		sfSearchPage.clickGlobalSearchButton(driver1);
		assertTrue(sfSearchPage.isSearchedContactVisible(driver1));
		
		//click the contact phone link to call
		sfSearchPage.clickContactPhoneLink(driver1);
		
		// Switch to extension
		sfSearchPage.switchToExtension(driver1);
		
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.clickReviewCallsButton(driver1);
	 	
	 	//Select caller from the drop down
	 	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// Verifying that user is able to make another call after selecting a caller
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	
	 	//Select caller from the drop down
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	
	 	// open Support Page and disable call disposition prompt setting
	 	seleniumBase.switchToTab(driver1, 2);
	 	accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	public void addCallerAsMultiple(WebDriver callerDriver, WebDriver multiContactDriver, String callerPhone, String callerFirstName, String existingContact, String existingContactNumber) {
	    
		softPhoneCalling.softphoneAgentCall(callerDriver, existingContactNumber);
		softPhoneCalling.isMuteButtonEnables(callerDriver);
		softPhoneCalling.hangupActiveCall(callerDriver);
	    callScreenPage.addCallerAsContact(callerDriver, existingContact, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    softPhoneContactsPage.deleteAndAddContact(callerDriver, callerPhone, callerFirstName);
		
	    //calling again
		softPhoneCalling.softphoneAgentCall(callerDriver, callerPhone);
		seleniumBase.idleWait(8);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(callerDriver);
	    
	    // Add caller as multiple contact
	    callScreenPage.closeErrorBar(callerDriver);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(callerDriver);
	    callScreenPage.clickOnUpdateDetailLink(callerDriver);
	    callScreenPage.addCallerToExistingContact(callerDriver, existingContact + " Automation");
	    
	   //verify caller is multiple
	   softPhoneContactsPage.searchUntilContacIsMultiple(callerDriver, callerPhone);
	    
	   // Calling from Agent's SoftPhone
	   softPhoneCalling.softphoneAgentCall(callerDriver, callerPhone);
	   softPhoneCalling.idleWait(5);
	   softPhoneCalling.hangupIfInActiveCall(callerDriver);
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
		accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
		accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		aa_AddCallersAsContactsAndLeads();
	
		//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	}

	@AfterMethod(groups = {"Regression", "MediumPriority"})
	public void afterMethod(ITestResult result){
		if(result.getStatus() == 2 || result.getStatus() == 3) {
			// updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			// open Support Page and disable call disposition prompt setting
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			System.out.println("Account editor is opened ");
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.disableMultiMatchRequiredSetting(driver1);
			accountIntelligentDialerTab.disableCallDispositionPromptSetting(driver1);
			accountIntelligentDialerTab.disableCallDispositionRequiredSetting(driver1);
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
			
			if(result.getName().equals("verify_multi_match_required_forwarding")) {
				// Setting call forwarding OFF
				System.out.println("Setting call forwarding OFF");
				softPhoneCalling.hangupIfInActiveCall(driver1);
				softPhoneSettingsPage.clickSettingIcon(driver1);
				softPhoneSettingsPage.disableCallForwardingSettings(driver1);
			}
		}
	}
}
