/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCallQueues;
import softphone.source.SoftPhoneCallQueues.QueueUserDetails;
import softphone.source.salesforce.ContactDetailPage;
import support.source.callQueues.CallQueuesPage;
import utility.HelperFunctions;

/**
 * @author Abhishek Gupta
 *
 */
public class QueueCalls extends SoftphoneBase{
	  
	  String teamName = null;
	
	 @Test(groups={"QuickSanity", "Sanity", "Regression", "Product Sanity"})
	  public void calling_add_remove_groups()
	  {
	    System.out.println("Test case --calling_add_remove_groups()-- started ");
	    
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
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_queue_smart_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCallQueues.idleWait(2);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    //pickup call from the queue
	    softPhoneCallQueues.idleWait(2);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//hanging up with the second device
	    System.out.println("hanging up with the second device");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
	    //verify Queue Count
	    softPhoneCallQueues.queueCountInvisible(driver1);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Opening the caller detail page
		contactDetailPage.idleWait(5);
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
	    
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"Sanity", "Regression", "Product Sanity"})
	  public void add_remove_groups_verify_call_counts()
	  {
	    System.out.println("Test case --add_remove_groups_verify_call_counts()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName1 = CONFIG.getProperty("qa_group_5_name").trim();
	    String queueName2 = CONFIG.getProperty("qa_group_2_name").trim();
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver1, queueName1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName2);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName1);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName2);
	    
		// making call to queue 2
	    System.out.println("making call to queue 2");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_2_number"));
	    
		//making call to queue 1
	    System.out.println("making call to queue 1");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_5_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //unsubscribe queue 2
	    softPhoneCallQueues.unSubscribeQueue(driver1, queueName2);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    //Subscribe again queue 1
	    softPhoneCallQueues.subscribeQueue(driver1, queueName2);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
		//hanging up the call with the first device
	    System.out.println("hanging up with the call");
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		//hanging up if any calls is still active
	    System.out.println("hanging up with the second device");
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
		// wait that call has been removed caller
		softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify Queue is invisible
	    softPhoneCallQueues.queueCountInvisible(driver1);

		//verify call data
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);

	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertTrue(sfTaskDetailPage.getCallQueue(driver1).contains(queueName1)|sfTaskDetailPage.getCallQueue(driver1).contains(queueName2));
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
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
	  
	 @Test(groups={"Sanity"})
	  public void call_acd_decline_all_users()
	  {
	    System.out.println("Test case --call_acd_decline_all_users()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	  //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_group_1_number"));
	    
		//declining call from first agent
	    System.out.println("declining call from first agent");
	    seleniumBase.idleWait(5);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	softPhoneCalling.declineCall(driver4);
	    }else{
	    	softPhoneCalling.declineCall(driver1);
	    }
	    
	    //declining call from second agent
	    System.out.println("declining call from second agent");
	    seleniumBase.idleWait(5);
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	softPhoneCalling.declineCall(driver1);
	    }else{
	    	softPhoneCalling.declineCall(driver4);
	    }
	    seleniumBase.idleWait(10);
	    
		// wait that call has been removed caller
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//verify call data
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Missed"); 
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//verify call data for second caller
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed"); 
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver4))> 0); 
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Sanity"})
	  public void acd_call_receiving_order()
	  {
	    System.out.println("Test case --acd_call_receiving_order()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String driverCallReceived;
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
		// making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_group_1_number"));
	    seleniumBase.idleWait(10);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	softPhoneCalling.pickupIncomingCall(driver4);
	    	driverCallReceived = "driver4";
	    }else{
	    	softPhoneCalling.pickupIncomingCall(driver1);
	    	driverCallReceived = "driver1";
	    }
	    
	    //Hanging up with the call
	    seleniumBase.idleWait(10);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		// making second call to ACD queue
	    System.out.println(" making second call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_group_1_number"));
	    seleniumBase.idleWait(5);
	    
	    //picking up with the other device
	    System.out.println("picking up with the other device");
	    if(driverCallReceived.equals("driver4")){
	    	assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
	    	softPhoneCalling.pickupIncomingCall(driver1);
	    }else{
	    	assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));
	    	softPhoneCalling.pickupIncomingCall(driver4);
	    }
	    
	    //Hanging up with the call
	    seleniumBase.idleWait(10);
	    softPhoneCalling.hangupActiveCall(driver2);
	    
 	   //verify data for first agent
	   callScreenPage.openCallerDetailPage(driver1);
	   contactDetailPage.openRecentCallEntry(driver1, callSubject);
	   sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	   sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	   assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	   assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	   assertTrue(sfTaskDetailPage.getCallQueue(driver1).contains(acdQueueName));
	   seleniumBase.closeTab(driver1);
	   seleniumBase.switchToTab(driver1, 1);
	  
	   //verifying data for the second agent
	   callScreenPage.openCallerDetailPage(driver4);
	   contactDetailPage.openRecentCallEntry(driver4, callSubject);
	   sfTaskDetailPage.verifyCallNotAbandoned(driver4);
	   sfTaskDetailPage.verifyCallStatus(driver4, "Connected"); 
	   assertEquals(sfTaskDetailPage.getCallDirection(driver4), "Inbound");
	   assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
	   assertTrue(sfTaskDetailPage.getCallQueue(driver4).contains(acdQueueName));
	   seleniumBase.closeTab(driver4);
	   seleniumBase.switchToTab(driver4, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "MediumPriority", "Product Sanity"})
	  public void end_group_call_using_end_icon()
	  {
	    System.out.println("Test case --end_group_call_using_end_icon()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);	    
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
	    // enable allow agent to reject calls to queue 
	    loginSupport(driver1);
  		callQueuesPage.openCallQueueSearchPage(driver1);
  		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
  		callQueuesPage.disableAllowRejectQueueCalls(driver1);
  		callQueuesPage.saveGroup(driver1);
  		seleniumBase.switchToTab(driver1, 1);
  		
  		 //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	   
	    // making call to call queue
	    System.out.println("making call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnInVisible(driver1);
	    
	    //end the queue call
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.idleWait(3);
	    
	    int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		
	    // enable allow agent to reject calls to queue 
 		seleniumBase.switchToTab(driver1, 2);
 		callQueuesPage.enableAllowRejectQueueCalls(driver1);
 		callQueuesPage.saveGroup(driver1);
 		seleniumBase.closeTab(driver1);
 		seleniumBase.switchToTab(driver1, 1);
	    
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnVisible(driver1);
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnVisible(driver1);
	    
	    //unsubscribe and re subscribe queuess and verify decline call buttons
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnVisible(driver1);
	    
	    //decline call from the queue
	    softPhoneCallQueues.DeclineCallFromQueue(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnVisible(driver1);
	    
	    //verify history counts has been increased
	    softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, callHistoryCounts);
	    
	    //hangup remaining calls
	    softPhoneCallQueues.DeclineCallFromQueue(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity"})
	  public void calling_multiple_calls_on_group()
	  {
	    System.out.println("Test case --calling_multiple_calls_on_group()-- started ");
	    
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
	    
	    String queue1Name = CONFIG.getProperty("qa_group_1_name").trim();
	    String queue1Number	= CONFIG.getProperty("qa_group_1_number");

	    String queue2Name = CONFIG.getProperty("qa_group_2_name").trim();
	    String queue2Number	= CONFIG.getProperty("qa_group_2_number");
		
	    //subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queue1Name);
	    softPhoneCallQueues.subscribeQueue(driver1, queue2Name);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queue1Number);   
   
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver3, queue2Number);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
		// receiving call from call second caller
	    System.out.println("making 3rd call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver4, queue1Number);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "3");
	    
		// receiving call from call second caller
	    System.out.println("making 4th call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver5, queue2Number);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "4");
	    
		// receiving call from call second caller
	    System.out.println("making 5th call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver6, queue1Number);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "5");
	    softPhoneCallQueues.verifySubscribedQueueCount(driver1, queue1Name, "3");
	    softPhoneCallQueues.verifySubscribedQueueCount(driver1, queue2Name, "2");
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(driver1, 5);
	    
	    //reload softphone
	    reloadSoftphone(driver1);
	    
	    //verify Queue Count
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "5");
	    softPhoneCallQueues.verifySubscribedQueueCount(driver1, queue1Name, "3");
	    softPhoneCallQueues.verifySubscribedQueueCount(driver1, queue2Name, "2");
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(driver1, 5);
	    
	    //disconnect all calls
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.hangupActiveCall(driver5);
	    softPhoneCalling.hangupActiveCall(driver6);
	    
	    //subscribe queues for caller 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queue1Name);
	    
	    //verify that call is visible
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    
	    //disconnect the call from the caller
	    softPhoneCalling.hangupActiveCall(driver2);
	    
	    //pick call button should not be visible for any caller
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    softPhoneCallQueues.isPickCallBtnInvisible(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_call_history_for_other_agent()
	  {
	    System.out.println("Test case --verify_call_history_for_other_agent()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String queue1Name = CONFIG.getProperty("qa_group_1_name").trim();
	    String queue1Number	= CONFIG.getProperty("qa_group_1_number");
		
	    //subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queue1Name);

	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queue1Name);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queue1Number);   
   
	    //pick call button should not be visible for any caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    softPhoneCallQueues.isPickCallBtnInvisible(driver4);
	    seleniumBase.idleWait(5);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject);
   
	    //verify call data
	  	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	  	softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	  	softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	  	callToolsPanel.clickCallNotesIcon(driver1);
	  	assertEquals(callToolsPanel.getCallNotesSubject(driver1), (callSubject));
	    
	    //verify call data
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	  	callToolsPanel.clickCallNotesIcon(driver4);
	  	assertNotEquals(callToolsPanel.getCallNotesSubject(driver4), (callSubject));
	  	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void hide_group_call_history()
	  {
	    System.out.println("Test case --hide_group_call_history()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queue1Name = CONFIG.getProperty("qa_group_1_name").trim();
	    String queue1Number	= CONFIG.getProperty("qa_group_1_number");
	    
	    // open Support Page and enable hide group call history setting
	    loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.enableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
		
	    //subscribe queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queue1Name);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queue1Number);   
   
	    //pick call button should not be visible for any caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
	    callToolsPanel.changeCallSubject(driver1, callSubject);
   
	    //verify call data
	  	softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	  	softphoneCallHistoryPage.verifyGroupCallsLinkInvisible(driver1);
	  	
	    //verify call data
	  	softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	  	callToolsPanel.clickCallNotesIcon(driver1);
	  	assertEquals(callToolsPanel.getCallNotesSubject(driver1), (callSubject));
	    
	  	//open Support Page and disable hide group call history setting
	  	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	  	
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }

	@Test(groups={"MediumPriority"})
	  public void group_call_end_icon_already_in_call()
	  {
	    System.out.println("Test case --group_call_end_icon_already_in_call()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);	    
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    // enable allow agent to reject calls to queue 
	    loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.enableAllowRejectQueueCalls(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnVisible(driver1);
	    
		//Making a call to another user
	    System.out.println("making a call to another user");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //pick up the call
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //verify that user can decline queue call
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.DeclineCallFromQueue(driver1);
	    
	    //Disconnect call from the user
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//@Test(groups={"MediumPriority"})
	  public void queue_hold_time()
	  {
	    System.out.println("Test case --queue_hold_time()-- started ");
	    
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
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to call flow");
	    softPhoneCallQueues.idleWait(5);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //get avg queue hold time
	    Date queueHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //verify new queue hold time is less than previous one
	    Date newQueueHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    System.out.println("queue hold time " + queueHoldTime + "  new queue hold time " + newQueueHoldTime);
	    assertTrue(queueHoldTime.after(newQueueHoldTime));
	    
	    //decline call from the queue
	    softPhoneCallQueues.DeclineCallFromQueue(driver1, 1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    Date queueHoldTimeAfterCallDeclined = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    System.out.println(queueHoldTimeAfterCallDeclined + " " + newQueueHoldTime);
	    assertTrue(queueHoldTimeAfterCallDeclined.after(newQueueHoldTime));
	    
		//hanging up with the remaining device
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//@Test(groups={"MediumPriority"})
	  public void queue_hold_time_when_caller_disconnets()
	  {
	    System.out.println("Test case --queue_hold_time_when_caller_disconnets()-- started ");
	    
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
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to call flow");
	    softPhoneCallQueues.idleWait(5);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //verify new queue hold time is less than previous one
	    Date newQueueHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    
	    //decline call from the queue
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
	    Date queueHoldTimeAfterCallDeclined = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    assertTrue(newQueueHoldTime.before(queueHoldTimeAfterCallDeclined));
	    
		//hanging up with the remaining device
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//@Test(groups={"MediumPriority"})
	  public void queue_hold_time_when_subscribed_unsubscribed()
	  {
	    System.out.println("Test case --queue_hold_time_when_subscribed_unsubscribed()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// receiving call from call second caller
	    System.out.println("making 2nd call to queue");
	    softPhoneCallQueues.idleWait(5);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //Unsubscribe all queues for agent 1
	    Date firstAgentHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.queueCountInvisible(driver1);
	    softPhoneCallQueues.isQueueUnsubscribed(driver1, queueName);
	    
	    //verify that queue hold time is not changes for second agent
	    Date secondAgentHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver4, 0), "mm:ss");
	    assertTrue(secondAgentHoldTime.after(firstAgentHoldTime));
	    assertTrue(secondAgentHoldTime.before(HelperFunctions.addSecondsToDate(firstAgentHoldTime, 25)));

	    //subscribe queue again
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.verifyQueueCount(driver1, "2");
	    
	    //verify that the agent can see hold time and queue can be seen
	    firstAgentHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    assertTrue(firstAgentHoldTime.after(secondAgentHoldTime));
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //accept a call and verify that hold time is changed
	    firstAgentHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver1, 0), "mm:ss");
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    secondAgentHoldTime = HelperFunctions.parseStringToDateFormat(softPhoneCallQueues.getAvgQueueHoldTime(driver4, 0), "mm:ss");
	    assertTrue(secondAgentHoldTime.before(firstAgentHoldTime));	    
	    
	    //Hang up the calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void new_queue_decline_btn_invisible()
	  {
	    System.out.println("Test case --new_queue_decline_btn_invisible()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);	    
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    // enable allow agent to reject calls to queue 
	    loginSupport(driver1);
		callQueuesPage.openAddNewCallQueue(driver1);
		String groupName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(3));
		String teamDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(3));
		callQueuesPage.addNewCallQueueDetails(driver1, groupName, teamDesc);
		teamName = groupName;
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_user_1_name"));
		callQueuesPage.disableAllowRejectQueueCalls(driver1);
		
		//Add smart number to the group
		String smartNumber = callQueuesPage.addNewSmartNumberForGroups(driver1, "315", "queueDeclineBtnDisabled");
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);
		seleniumBase.reloadSoftphone(driver1);
		
		 //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, groupName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, groupName);
	   
	    // making call to call queue
	    System.out.println("making call to call queue");
	    softPhoneCalling.softphoneAgentCall(driver2, smartNumber);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnInVisible(driver1);
	    
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, groupName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, groupName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyAllDeclineQueueCallBtnInVisible(driver1);
	    
	    //end the queue call
	    softPhoneCalling.hangupActiveCall(driver2);
	    
	    // delete team which has call override setting on
 		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
 		callQueuesPage.deleteCallQueue(driver1); 		
 		dashboard.openSmartNumbersTab(driver1);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, groupName, smartNumber);
		smartNumbersPage.clickSmartNoByIndex(driver1, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(driver1);

 		seleniumBase.closeTab(driver1);
 		seleniumBase.switchToTab(driver1, 1);
 		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void new_acd_queue_member_auto_subscribed()
	  {
	    System.out.println("Test case --new_acd_queue_member_auto_subscribed()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);	    
	    
	    // enable allow agent to reject calls to queue 
	    loginSupport(driver1);
		callQueuesPage.openAddNewCallQueue(driver1);
		String groupName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(4));
		String teamDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(4));
		callQueuesPage.addNewCallQueueDetails(driver1, groupName, teamDesc);
		teamName = groupName;
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_user_1_name"));
		
		//add acd attributes
		callQueuesPage.selectLongestDistributationType(driver1);
		assertEquals(callQueuesPage.getMaxDials(driver1), "10");
		assertEquals(callQueuesPage.getDialTimeout(driver1), "10");
		
		//verify member subscribed on queue page
		assertTrue(callQueuesPage.isMemberSubscribed(driver1, CONFIG.getProperty("qa_user_1_name")));
		
		// verify member subscribed on users page
		dashboard.clickOnUserProfile(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		assertTrue(userIntelligentDialerTab.isCallQueueSubscribed(driver1, groupName));
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		 //verify member subscribed on softphon queue page
		softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.isQueueSubscribed(driver1, groupName);
	    
	    // delete queue
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, groupName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.deleteCallQueue(driver1); 		

		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void call_exists_queue_call_unable_to_pick()
	  {
	    System.out.println("Test case --call_exists_queue_call_unable_to_pick()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
	    //Making user busy on a call
	    System.out.println("Making user busy on a call");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));;
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
		//Making an call to the queue
	    System.out.println("Making an call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //verify Queue Count
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    		
		//hanging up with the remaining device
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void queue_contact_verify_opportunity()
	  {
	    System.out.println("Test case --queue_contact_verify_opportunity()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);	
	    
	    String callerNumber = CONFIG.getProperty("qa_user_3_uk_number").trim();
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String leadCompany	= CONFIG.getProperty("contact_account_name").trim();
		String callerName 	= "AS UK Local Presence";
	    HashMap<SoftPhoneCallQueues.QueueUserDetails, String> queueContactDetails = new HashMap<SoftPhoneCallQueues.QueueUserDetails, String>();
	    
	    queueContactDetails.put(QueueUserDetails.QueueCallerName, callerName + " " + HelperFunctions.getNumberInRDNAFormat(callerNumber));
	    queueContactDetails.put(QueueUserDetails.QueueTimerIcon, "true");
	    queueContactDetails.put(QueueUserDetails.QueueCallerTitle, "QA,");
	    queueContactDetails.put(QueueUserDetails.QueueCallerCompany, leadCompany);
	    queueContactDetails.put(QueueUserDetails.QueueName, queueName);
	    
	    //select additional number for the contact which has
	    int index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver3, callerNumber);
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver3, index);
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.idleWait(2);
	    
	    //verify opportunity
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    assertTrue(softPhoneCallQueues.getContactOpportunityName(driver1, callerNumber).contains("Automation Queue Opportunity"));
	    
/*	    //Verify call entry is turned to red
	    softPhoneCallQueues.idleWait(60);
	    softPhoneCallQueues.verifyQueueCallIsRed(driver1, callerNumber);*/
	    
	    //hangup active call
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    softPhoneCallQueues.idleWait(2);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	   //verify in salesforce
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    assertTrue(sfTaskDetailPage.getCallQueue(driver1).contains(queueName));
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driver1))> 0); 
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void existing_queue_member_auto_subscribed()
	  {
	    System.out.println("Test case --existing_queue_member_auto_subscribed()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);	    
	    
	    // enable allow agent to reject calls to queue 
	    loginSupport(driver1);
		callQueuesPage.openAddNewCallQueue(driver1);
		String groupName = "AutoGrpDetailName".concat(HelperFunctions.GetRandomString(3));
		String teamDesc = "AutoGrpDetailDesc".concat(HelperFunctions.GetRandomString(3));
		callQueuesPage.addNewCallQueueDetails(driver1, groupName, teamDesc);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, groupName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_user_1_name"));
		
		//verify member subscribed on queue page
		assertTrue(callQueuesPage.isMemberSubscribed(driver1, CONFIG.getProperty("qa_user_1_name")));
		
		// verify member subscribed on users page
		dashboard.clickOnUserProfile(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		assertTrue(userIntelligentDialerTab.isCallQueueSubscribed(driver1, groupName));
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		 //verify member subscribed on softphon queue page
		softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.isQueueSubscribed(driver1, groupName);
	    
	    // delete queue
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, groupName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.deleteCallQueue(driver1); 		

		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void verify_lead_queue_details()
	  {
	    System.out.println("Test case --verify_lead_queue_details()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
	    String leadCompany	= CONFIG.getProperty("contact_account_name").trim();
		String leadStatus	= "Open";
	    String leadSource	= "Other";
		String callerName 	= CONFIG.getProperty("qa_user_3_name");
		String callerNumber	= CONFIG.getProperty("qa_user_3_number");
		HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
	    HashMap<SoftPhoneCallQueues.QueueUserDetails, String> queueLeadDetails = new HashMap<SoftPhoneCallQueues.QueueUserDetails, String>();
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, callerNumber, callerName);	
	    
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		leadDetails.put(ContactDetailPage.LeadDetailsFields.leadSource, leadSource);
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    queueLeadDetails.put(QueueUserDetails.QueueCallerName, callerName + " Automation" + " " + HelperFunctions.getNumberInRDNAFormat(callerNumber));
	    queueLeadDetails.put(QueueUserDetails.QueueTimerIcon, "true");
	    queueLeadDetails.put(QueueUserDetails.QueueCallerTitle, "QA,");
	    queueLeadDetails.put(QueueUserDetails.QueueCallerCompany, leadCompany);
	    queueLeadDetails.put(QueueUserDetails.QueueName, queueName);
	    queueLeadDetails.put(QueueUserDetails.QueueLeadStatus, leadStatus);
	    queueLeadDetails.put(QueueUserDetails.QueueLeadSource, leadSource);
		
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
		//Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// verifying caller queue details
	    softPhoneCallQueues.verifyQueueData(driver1, queueLeadDetails, HelperFunctions.getNumberInRDNAFormat(callerNumber));
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCallQueues.idleWait(2);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, callerNumber, callerName);	
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void verify_unknown_queue_details()
	  {
	    System.out.println("Test case --verify_lead_queue_details()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String queueName 	= CONFIG.getProperty("qa_group_1_name").trim();
		String callerNumber	= CONFIG.getProperty("qa_user_3_number");
	    HashMap<SoftPhoneCallQueues.QueueUserDetails, String> queueLeadDetails = new HashMap<SoftPhoneCallQueues.QueueUserDetails, String>();
	    
	    softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver3);
	    
	    //Verify queue details for unknown caller
		// Deleting contact and calling again
	    softPhoneCalling.softphoneAgentCall(driver1, callerNumber);
		softPhoneCalling.pickupIncomingCall(driver3);
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
			softPhoneCalling.pickupIncomingCall(driver3);
		}
		softPhoneCalling.hangupActiveCall(driver1);
		
		//adding queue details to be verified into a hash map
		queueLeadDetails.put(QueueUserDetails.QueueCallerName, "Unknown " + HelperFunctions.getNumberInRDNAFormat(callerNumber));
	    queueLeadDetails.put(QueueUserDetails.QueueTimerIcon, "true");
	    queueLeadDetails.put(QueueUserDetails.QueueName, queueName);

	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
		
	    //Making an outbound call from softphone
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify Queue Count
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    softPhoneCallQueues.verifyQueueCount(driver1, "1");
	    
		// verifying caller queue details
	    softPhoneCallQueues.verifyQueueData(driver1, queueLeadDetails, HelperFunctions.getNumberInRDNAFormat(callerNumber));
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
	    //hanging up with the first device
	    System.out.println("hanging up with the first device");
	    softPhoneCallQueues.idleWait(2);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		// add caller as Lead
		aa_AddCallersAsContactsAndLeads();
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups = { "Regression" })
	public void verify_wrap_up_time() {
		System.out.println("Test case --verify_wrap_up_time-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String queueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_acd_group_1_number");

		// Create a web lead rule on supports tab
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.TwoMins);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// subscribe queues
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// get wrap up time and verify
		int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 120 && wrapUpTime >= 117);
		callScreenPage.verifyUserImageBusy(driver1);

		// verify user image is set to available
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
		
		/*******verify with sip enable setting*******//*
		// enable SIP setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableSipSettings(driver1);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// get wrap up time and verify
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 120 && wrapUpTime >= 117);
		callScreenPage.verifyUserImageBusy(driver1);
		
		// disable SIP setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableSipSettings(driver1);*/

		// disable web lead rule on supports tab
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_wrap_up_time_extend() {
		System.out.println("Test case --verify_wrap_up_time_extend-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_group_1_number");

		// Set wrap up time to 30 seconds
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// subscribe queues
		softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// get wrap up time and verify
		int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);

		// Making another to verify that wrap up time counter is removed
		System.out.println("agent making call between wrap up time");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// verify user image is set to available
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		
		// hang up with the call
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		/*******verify wrap up time extension******/
		// Taking a call through Queue
		System.out.println("Taking a call through Queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.openCallQueuesSection(driver1);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// get wrap up time and verify
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//extend time
		callScreenPage.extendWrapUpTime(driver1, CallScreenPage.WrapUpTimeExtensions.Sixty);
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime > 87 && wrapUpTime <= 90);
		
		/*******verify unlimited wrap up time extension******/
		// Taking a call through Queue
		System.out.println("Taking a call through Queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.openCallQueuesSection(driver1);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// get wrap up time and verify
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//extend time
		callScreenPage.extendWrapUpTime(driver1, CallScreenPage.WrapUpTimeExtensions.Busy);
		callScreenPage.verifyUserImageBusy(driver1);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		
		// Create a web lead rule on supports tab
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_wrap_up_time_acd() {
		System.out.println("Test case --verify_wrap_up_time_acd-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String queueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_acd_group_1_number");

		// Set wrap up time to 30 seconds
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.Unlimited);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// subscribe queues
		softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//Verify that user image is busy for unlimited time and no countdonw is presend
		seleniumBase.idleWait(3);
		callScreenPage.verifyUserImageBusy(driver1);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.setUserImageAvailable(driver1);
		
		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		seleniumBase.idleWait(5);
		softPhoneCalling.clickHoldButton(driver1);
		softPhoneCalling.isCallOnHold(driver1);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		// Create a web lead rule on supports tab
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case is pass");
	}

	//Verify wrap up timer continue after queue call transfer
	@Test(groups = { "MediumPriority" })
	public void verify_wrap_up_time_transfer() {
		System.out.println("Test case --verify_wrap_up_time_transfer-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_group_1_number");

		// Create a web lead rule on supports tab
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.OneMin);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// subscribe queues
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		
	    //tranfer call to user
	    softPhoneCalling.transferToNumber(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		//receiving call from app softphone
	    softPhoneCalling.pickupIncomingCall(driver4);
		
		// get wrap up time and verify
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 60 && wrapUpTime >= 50);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Hang up the call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// verify user image is not set to available
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 55 && wrapUpTime >= 45);
		
		//wait for 10 more seconds
		seleniumBase.idleWait(10);
		int newWrapUpTime = callScreenPage.getWrapUpTime(driver1);
		System.out.println(newWrapUpTime);
		assertTrue(newWrapUpTime <= (wrapUpTime - 10) && newWrapUpTime >= (wrapUpTime - 15));

		// disable web lead rule on supports tab
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	//Verify user should be continue with wrapup time when someone leave voicemail in between
	@Test(groups = { "MediumPriority" })
	public void verify_wrap_up_time_drop_vm() {
		System.out.println("Test case --verify_wrap_up_time_drop_vm-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_group_1_number");

		// Create a web lead rule on supports tab
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, CONFIG.getProperty("qa_user_account"));
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.FiveMin);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// subscribe queues
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, queueName);

		// Taking a call through Queue
		System.out.println("taking a call through queue");
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		seleniumBase.idleWait(5);
		
		//Hang up the call
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// get wrap up time and verify
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 300 && wrapUpTime >= 295);
		callScreenPage.verifyUserImageBusy(driver1);
		
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		
		//Make a call to the agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));

		//drop vm
		callToolsPanel.dropFirstVoiceMail(driver4);

		//
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, callHistoryCounts);
		
		//wait for 10 more seconds
		int newWrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(newWrapUpTime <= wrapUpTime);

		// disable web lead rule on supports tab
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	  @AfterMethod(groups = { "Regression",  "MediumPriority", "Product Sanity"})
	  	public void deleteGroup(ITestResult result) {
		if(result.getName().equals("new_queue_decline_btn_invisible") && (result.getStatus() == 2 || result.getStatus() == 3) && teamName != null) {
			initializeDriverSoftphone("driver1");
		    driverUsed.put("driver1", true);
		    
		    // delete team which has call override setting on
		    loginSupport(driver1);
			callQueuesPage.openCallQueueSearchPage(driver1);
			callQueuesPage.openCallQueueDetailPage(driver1, teamName, CONFIG.getProperty("qa_user_account"));
			callQueuesPage.deleteCallQueue(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			
			teamName = null;

			// Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);	
		}
	  }
}