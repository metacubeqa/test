package softphone.cases.softPhoneFunctional;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class BlockedNumbers extends SoftphoneBase{

	@Test(groups = {"MediumPriority"})
	public void verify_blocked_number_spam_call_received(){
		System.out.println("Test case --verify_blocked_number_spam_call_received()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		String callerPhone = CONFIG.getProperty("prod_user_3_number");
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
	 		
	 	// call is appearing on agents phone
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.isDeclineButtonVisible(driver1);
	    
	    //setting up the disposition for calls
	  	callToolsPanel.selectDispositionUsingText(driver1, "spam");
	  	
	  	// call is not received so it is missed
	 	softPhoneCalling.verifyDeclineButtonIsInvisible(driver1);
	 	softPhoneCalling.idleWait(10);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
 	    
	 	//verify on call history page number is blocked on all, missed and voicemail tabs
 	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
 	    softphoneCallHistoryPage.isBlockBtnPresentByIndex(driver1, 1);
 	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
 	    softphoneCallHistoryPage.isBlockBtnPresentByIndex(driver1, 1);
 	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
 	    softphoneCallHistoryPage.isBlockBtnPresentByIndex(driver1, 1);
 	    
	 	//verify that caller is blocked
 	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
 	    
 	    //open web app and verify that entry is there for the blocked number
 	    loginSupport(driver1);
 	    dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
 		accountBlockedNumbersTab.isNumberBlocked(driver1, callerPhone);
		accountBlockedNumbersTab.closeTab(driver1);
		accountBlockedNumbersTab.switchToTab(driver1, 1);
		
		//unblock the number
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
	 	callScreenPage.clickUnblockButton(driver1);
 	    callScreenPage.verifyUnblockBtnInvisible(driver1);
	 	
	 	//verify agent is able to call back on that number
	 	softPhoneCalling.clickCallBackButton(driver1);
	    softPhoneCalling.pickupIncomingCall(driver6);
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
	 	
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_blocked_number_call_history_spam(){
		System.out.println("Test case --verify_blocked_number_call_history_spam()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
	 		
	 	// accept the call
	    softPhoneCalling.pickupIncomingCall(driver1);
	 	
	 	//hangup active call
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
 	    
		 //setting up the disposition for calls
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	  	callToolsPanel.selectDispositionUsingText(driver1, "spam");
	  	
	 	//verify that caller is blocked
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
	 	
		 //setting up the disposition for calls
	 	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
 	    
 	    //unblock the number
	 	callScreenPage.clickUnblockButton(driver1);
 	    callScreenPage.verifyUnblockBtnInvisible(driver1);
 	    
	 	//verify agent is able to call back on that number
	 	softPhoneCalling.clickCallBackButton(driver1);
	    softPhoneCalling.pickupIncomingCall(driver6);
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver6", false);
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_blocked_number_group_call(){
		System.out.println("Test case --verify_blocked_number_group_call()-- started");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber	= CONFIG.getProperty("qa_group_1_number");
	    
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    // Calling from Agent's SoftPhone
	    softPhoneCalling.softphoneAgentCall(driver6, queueNumber);
	 		
	 	// accept the call
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	 	
	 	//hangup active call
	    softPhoneCalling.idleWait(5);
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
	 	softPhoneCalling.isCallBackButtonVisible(driver1);
 	    
		 //setting up the disposition for calls
	 	softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
	  	callToolsPanel.selectDispositionUsingText(driver1, "spam");
	  	
	 	//verify that caller is blocked
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
	  	
	  	//try to call number from dialpad which is blocked
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
	    
	    //verify that call is connected
	    softPhoneCalling.pickupIncomingCall(driver6);
	    
	 	//hangup active call
	 	softPhoneCalling.hangupIfInActiveCall(driver6);
	 	callScreenPage.verifyUnblockBtnVisible(driver1);
	 	
	 	//unblick the number
	 	callScreenPage.clickUnblockButton(driver1);
	 	
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver6", false);
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority" })
	public void afterClass() {
		// navigating to support page
		loginSupport(driver1);
		
		// deleting all blocked numbers
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
		accountBlockedNumbersTab.closeTab(driver1);
		accountBlockedNumbersTab.switchToTab(driver1, 1);
	}
}
