/**
 * 
 */
package softphone.cases.callFlow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class ACDCallFlow extends SoftphoneBase{
	
	WebDriver forwardingDeviceDriver = null;
	
	@Test(groups={"Regression"})
	public void acd_call_flow_default_values()
	{
		System.out.println("Test case --acd_call_flow_default_values-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Adding a new group with ACD
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openAddNewCallQueue(driver1);
		String groupDetailName = "AcdAutomationGroup".concat(HelperFunctions.GetRandomString(3));
		String groupDetailDesc = "AcdAutomationGroupDescription";
		callQueuesPage.addNewCallQueueDetails(driver1, groupDetailName, groupDetailDesc);
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));

		//Verify the default values for distribution
		callQueuesPage.selectLongestDistributationType(driver1);
		assertEquals(callQueuesPage.getMaxDials(driver1), "10");
		assertEquals(callQueuesPage.getDialTimeout(driver1), "10");
		
		//Add smart number to the group
		String smartNumber = callQueuesPage.addNewSmartNumberForGroups(driver1, "209", "ACDGroupCallNumber");
		callQueuesPage.saveGroup(driver1);
		
		//Change the default distribution type to default
		callQueuesPage.selectDefaultDistributationType(driver1);
		callQueuesPage.saveGroup(driver1);
		
		reloadSoftphone(driver3);
		
		//Open the agents softphone and subscribe the queue
		softPhoneCallQueues.openCallQueuesSection(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, groupDetailName);
		
		//pickup the call from the queue section
		softPhoneCalling.softphoneAgentCall(driver2, smartNumber);
		softPhoneCallQueues.pickCallFromQueue(driver3);
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//change the group disribution type to longest waiting time
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.saveGroup(driver1);
		
		reloadSoftphone(driver3);
		
		//Pick the call directly
		softPhoneCalling.softphoneAgentCall(driver2, smartNumber);
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//delete the group
		callQueuesPage.deleteCallQueue(driver1);
		
		//delete the smart number
		dashboard.openSmartNumbersTab(driver1);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, groupDetailName, smartNumber);
		smartNumbersPage.clickSmartNoByIndex(driver1, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(driver1);

		//Verifying that related campaign is corrected and lead count for that campaign
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);	    

		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	  @Test(groups={"Regression", "Product Sanity"})
	  public void acd_call_last_caller_received()
	  {
	    System.out.println("Test case --acd_call_last_caller_received()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_2_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_2_number").trim();
	    List<String> acdDriversUsed = new ArrayList<String>();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    WebDriver driverCallReceived3 = null;
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueName);
	    
		// making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    String callerName =  null;
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    	callerName =  CONFIG.getProperty("qa_user_1_name").trim();
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    	callerName =  CONFIG.getProperty("qa_user_2_name").trim();
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    	callerName =  CONFIG.getProperty("qa_user_3_name").trim();
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.pickupIncomingCall(driverCallReceived3);
	    softPhoneCalling.hangupActiveCall(driverCallReceived3);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	   //verify data for first agent
	   softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
	   softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
	   String callerPhone = softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived3, 0);
	   String callerTime = softphoneCallHistoryPage.getHistoryCallerTime(driverCallReceived3, 0);
	   String callSubject = "Inbound Call: " + CONFIG.getProperty("prod_user_1_number").trim() + ", to Queue: " + acdQueueName;
	   softphoneCallHistoryPage.openCallEntryByIndex(driverCallReceived3, 0);
	   callScreenPage.openCallerDetailPage(driverCallReceived3);
	   contactDetailPage.openRecentCallEntry(driverCallReceived3, callSubject);
	   sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived3);
	   sfTaskDetailPage.verifyCallStatus(driverCallReceived3, "Connected"); 
	   assertEquals(sfTaskDetailPage.getCallDirection(driverCallReceived3), "Inbound");
	   assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived3).contains("recordings"));
	   assertTrue(sfTaskDetailPage.getCallQueue(driverCallReceived3).contains(acdQueueName));
	   assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driverCallReceived3)) > 0);
	   assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived3).contains(callerName));
	   assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived3), "Completed");
	   assertEquals(sfTaskDetailPage.getSubject(driverCallReceived3), callSubject);
	   callQueuesPage.closeTab(driverCallReceived3);
	   callQueuesPage.switchToTab(driverCallReceived3, 1);
	  
	   //verify call entry is not in history for second agent
	   softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	   softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	   assertFalse(softphoneCallHistoryPage.getHistoryCallerTime(driverCallReceived2, 0).equals(callerTime) && softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived2, 0).equals(callerPhone));	 
	   
	   //verify call entry in history for third agent
	   softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	   softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	   assertFalse(softphoneCallHistoryPage.getHistoryCallerTime(driverCallReceived1, 0).equals(callerTime) && softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived1, 0).equals(callerPhone));
	   
	   //Logout softphone
	   softPhoneSettingsPage.logoutSoftphone(driverCallReceived1);
	   
		// making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    softPhoneCalling.declineCall(driverCallReceived3);
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	    
	    callQueuesPage.idleWait(20);
	    softPhoneCalling.hangupActiveCall(driver2);
	   
	    resetApplication();
	    
		// verify call entry in history for third agent
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived3, 0), callerPhone);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived3, 1));
	    
		// verify call entry in history for second agent
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived2, 0), callerPhone);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));
		
		// verify call entry in history for first agent
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		assertEquals(softphoneCallHistoryPage.getHistoryCallerPhone(driverCallReceived1, 0), callerPhone);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived1, 1));
	  
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression", "Product Sanity"})
	  public void acd_call_agent_received_then_last()
	  {
	    System.out.println("Test case --acd_call_agent_received_then_last()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_2_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_2_number").trim();
	    List<String> acdDriversUsed = new ArrayList<String>();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    WebDriver driverCallReceived3 = null;
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueName);
	    
		// making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
		// making second call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived3);

	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	  
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void call_acd_ignore_all_users()
	  {
	    System.out.println("Test case --call_acd_decline_all_users()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
		
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
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived1 = driver4;
	    }else{
	    	driverCallReceived1 = driver1;
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    String callerName =  null;
	    //declining call from second agent
	    System.out.println("declining call from first agent");
	    callQueuesPage.idleWait(5);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived2 = driver4;
	    	callerName =  CONFIG.getProperty("qa_user_2_name").trim();
	    }else{
	    	driverCallReceived2 = driver1;
	    	callerName =  CONFIG.getProperty("qa_user_1_name").trim();
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);
	    callQueuesPage.idleWait(10);
	    
		// wait that call has been removed caller
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//verify call data
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived1, 1));
	    softphoneCallHistoryPage.openCallEntryByIndex(driverCallReceived1, 0);
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driverCallReceived1);
		callScreenPage.openCallerDetailPage(driverCallReceived1);
		contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject1);
	    sfTaskDetailPage.verifyCallStatus(driverCallReceived1, "Missed"); 
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driverCallReceived1))> 0); 
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driverCallReceived1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived1).contains("recordings"));
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).contains(callerName));
	    callQueuesPage.closeTab(driverCallReceived1);
	    callQueuesPage.switchToTab(driverCallReceived1, 1);	    
	    
		//verify call data
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));
	    softphoneCallHistoryPage.openCallEntryByIndex(driverCallReceived2, 0);
	    String callSubject2 = callToolsPanel.changeAndGetCallSubject(driverCallReceived2);
		callScreenPage.openCallerDetailPage(driverCallReceived2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject2);
	    sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Missed"); 
	    assertTrue(Integer.parseInt(sfTaskDetailPage.getcallQueueHoldTime(driverCallReceived2))> 0); 
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driverCallReceived2);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		//assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).contains(callerName));
	    callQueuesPage.closeTab(driverCallReceived2);
	    callQueuesPage.switchToTab(driverCallReceived2, 1);	 
	    
	    //Make first caller busy
	    softPhoneCalling.softphoneAgentCall(driverCallReceived1, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
		// receiving call on acd queue
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_group_1_number"));
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //verify that call is received from other caller
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	    
	    //Hangup the call from caller if still active
	    callQueuesPage.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //Disconnect the call from caller 1
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));
	    softphoneCallHistoryPage.idleWait(2);
	    softphoneCallHistoryPage.openCallEntryByIndex(driverCallReceived2, 0);
	    String callSubject3 = callToolsPanel.changeAndGetCallSubject(driverCallReceived2);
		callScreenPage.openCallerDetailPage(driverCallReceived2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject3);
	    sfTaskDetailPage.verifyCallStatus(driverCallReceived2, "Missed");  
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driverCallReceived2);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived2).contains("recordings"));
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).contains(callerName));
		callQueuesPage.closeTab(driverCallReceived2);
		callQueuesPage.switchToTab(driverCallReceived2, 1);
	    
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived1, 1));
	    softphoneCallHistoryPage.openCallEntryByIndex(driverCallReceived1, 0);
		callScreenPage.openCallerDetailPage(driverCallReceived1);
		contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject3);
	    sfTaskDetailPage.verifyCallStatus(driverCallReceived1, "Missed"); 
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driverCallReceived1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driverCallReceived1).contains("recordings"));
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).contains(callerName));
		callQueuesPage.closeTab(driverCallReceived1);
		callQueuesPage.switchToTab(driverCallReceived1, 1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void call_acd_call_flow_drop_vm()
	  {
	    System.out.println("Test case --call_acd_call_flow_drop_vm()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    
	    //delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    if(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1)) {
	    	softphoneCallHistoryPage.deleteVMByIndex(driver1, 1);
	    }
	    
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver4);
	    if(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1)) {
	    	softphoneCallHistoryPage.deleteVMByIndex(driver4, 1);
	    }
		
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
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_call_flow_number"));
	    
		//ignoring call from first agent
	    System.out.println("declining call from first agent");
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived1 = driver4;
	    }else{
	    	driverCallReceived1 = driver1;
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    //ignoring call from second agent
	    System.out.println("declining call from first agent");
	    callQueuesPage.idleWait(5);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived2 = driver4;
	    }else{
	    	driverCallReceived2 = driver1;
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);
	    
	    callQueuesPage.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    softphoneCallHistoryPage.clickMyCallsLink(driver3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    assertFalse(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver4);
	    assertFalse(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void acd_group_abandoned_call()
	  {
	    System.out.println("Test case --acd_group_abandoned_call()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String acdQueueName 			= CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String salesforceConnectUser 	= CONFIG.getProperty("salesforce_connect_user").trim();
	    WebDriver driverCallReceived1 	= null;
		
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
	    
		//ignoring call from first agent
	    System.out.println("declining call from first agent");
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived1 = driver4;
	    }else{
	    	driverCallReceived1 = driver1;
	    }
	    
	    //Hang up call before user picks it up
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.declineCall(driverCallReceived1);	    
	   
	    //verify data for first agent
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driverCallReceived1);
	    callScreenPage.openCallerDetailPage(driverCallReceived1);
	    contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject);
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).contains(salesforceConnectUser));
	    callQueuesPage.closeTab(driverCallReceived1);
	    callQueuesPage.switchToTab(driverCallReceived1, 1);
		
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	  @Test(groups={"Regression"})
	  public void acd_call_flow_abandoned_call()
	  {
	    System.out.println("Test case --acd_call_flow_abandoned_call()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String acdQueueName 			= CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String abandonedConnectUser 	= CONFIG.getProperty("call_flow_abandoned_user").trim();
	    WebDriver driverCallReceived1 	= null;
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
		// receiving call on acd call flow
	    System.out.println("receiving call on acd queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_abandoned_acd_call_flow_number"));
	    
		//ignoring call from first agent
	    System.out.println("declining call from first agent");
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	driverCallReceived1 = driver4;
	    }else{
	    	driverCallReceived1 = driver1;
	    }
	    
	    //Hang up call before user picks it up
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.declineCall(driverCallReceived1);	    
	   
	    //verify data for first agent
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driverCallReceived1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived1);
	    contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject);
	    assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).contains(abandonedConnectUser));
	    callQueuesPage.closeTab(driverCallReceived1);
	    callQueuesPage.switchToTab(driverCallReceived1, 1);
		
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"Regression"})
	  public void acd_group_add_member()
	  {
	    System.out.println("Test case --acd_group_add_member()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();
	    List<String> acdDriversUsed = new ArrayList<String>();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    WebDriver driverCallReceived3 = null;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_user_3_name"));
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
	    callQueuesPage.switchToTab(driver1, 1);
		reloadSoftphone(driver3);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueName);
	    
	    // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    callQueuesPage.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver4);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1));
	    
	    //remove the member from the groudp	
		callQueuesPage.switchToTab(driver1,callQueuesPage.getTabCount(driver1));
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_user_3_name"));
		callQueuesPage.setMaxDials(driver1, "2");
		callQueuesPage.saveGroup(driver1);
	    callQueuesPage.closeTab(driver1);
	    callQueuesPage.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups = { "MediumPriority" })
	public void verify_dial_timeout_range_acd_call_flow() {
		System.out.println("Test case --verify_dial_timeout_range_acd_call_flow-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		// setting dial timeout greater than 99
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.setDialTimeout(driver1, "1200");
		callQueuesPage.clickSaveGroupButton(driver1);
		System.out.println(callQueuesPage.getHeaderNotification(driver1));
		assertEquals(callQueuesPage.getHeaderNotification(driver1), "'' cannot be greater than 99");
		
		//setting valid timeout
		callQueuesPage.setDialTimeout(driver1, "10");
		callQueuesPage.saveGroup(driver1);
		assertEquals(callQueuesPage.getDialTimeout(driver1), "10");
		
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);	
		
		// updating the driver used
		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_dial_timeout_range_acd_call_flow-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_dial_to_acd_queue_when_longest_waiting_agent_already_on_call() {
		System.out.println("Test case --verify_dial_to_acd_queue_when_longest_waiting_agent_already_on_call-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		dashboard.clickOnUserProfile(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver1);
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		loginSupport(driver3);
		dashboard.clickOnUserProfile(driver3);
		userIntelligentDialerTab.openIntelligentDialerTab(driver3);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver3);
		userIntelligentDialerTab.saveAcccountSettings(driver3);
		callQueuesPage.closeTab(driver3);
		callQueuesPage.switchToTab(driver3, 1);
		
		loginSupport(driver4);
		dashboard.clickOnUserProfile(driver4);
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.selectNoUnvailableFlow(driver4);
		userIntelligentDialerTab.saveAcccountSettings(driver4);
		callQueuesPage.closeTab(driver4);
		callQueuesPage.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
	    
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);

		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	   
	    String lwaCallerNumber = null;
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    	lwaCallerNumber = CONFIG.getProperty("qa_user_1_number");
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    	lwaCallerNumber = CONFIG.getProperty("qa_user_2_number");
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    	lwaCallerNumber = CONFIG.getProperty("qa_user_3_number");
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //making longest waiting agent busy on call
	    softPhoneCalling.softphoneAgentCall(driver5, lwaCallerNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver5);
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    
	    //verifying call goes to second longest waiting agent & waiting till full ring
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	    
	    softPhoneCalling.switchToTab(driverCallReceived2, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	   
	    //verifying call goes to third longest waiting agent & waiting till full ring
	    softPhoneCalling.switchToTab(driverCallReceived3, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    softPhoneCalling.declineCall(driverCallReceived3);

	    //verifying call goes to second longest waiting agent & waiting till full ring
	    softPhoneCalling.switchToTab(driverCallReceived2, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	   
	    //verifying call goes to third longest waiting agent & waiting till full ring
	    softPhoneCalling.switchToTab(driverCallReceived3, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    softPhoneCalling.declineCall(driverCallReceived3);
		
	    softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneCalling.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// verify call entry in history for third agent
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived3, acdQueueLPName);
		
		// verify call entry in history for second agent
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);

		// verify call entry in history for first agent
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case --verify_dial_to_acd_queue_when_longest_waiting_agent_already_on_call-- passed ");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void take_call_on_acd_queue_when_on_hold_call_with_call_forwarding_stay_connected() {
		System.out.println("Test case --take_call_on_acd_queue_when_on_hold_call_with_call_forwarding_stay_connected-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneCalling.hangupIfHoldCall(driver1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.hangupIfHoldCall(driver3);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneCalling.hangupIfHoldCall(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
		
		// delete first voice mail
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);
		
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //making call forwarding enable on lWA
	    softPhoneCalling.switchToTab(driverCallReceived1, 1);
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
	    softPhoneSettingsPage.setCallForwardingNumber(driverCallReceived1, driver5, "", CONFIG.getProperty("prod_user_2_number"));
		softPhoneSettingsPage.enableCallForwardingSettings(driverCallReceived1);
	    softPhoneSettingsPage.enableStayConnectedSetting(driverCallReceived1);
		
	    //making outbound call and placing on hold
	    softPhoneCalling.softphoneAgentCall(driverCallReceived1, CONFIG.getProperty("prod_user_3_number"));
	    softPhoneCalling.isCallHoldButtonVisible(driverCallReceived1);
	    
	    //picking call on call forwarding device
	    softPhoneCalling.pickupIncomingCall(driver5);
	    
	    softPhoneCalling.clickHoldButton(driverCallReceived1);
	    softPhoneCalling.isCallOnHold(driverCallReceived1);
	    
		callScreenPage.verifyUserImageBusy(driverCallReceived1);
		assertTrue(softPhoneSettingsPage.isSettingsIconVisible(driverCallReceived1));
		softPhoneCalling.clickDialPadIcon(driverCallReceived1);
	    
	    //take a call on acd queue
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //verifying call comes on free agents
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived3);

	    softPhoneCalling.hangupIfHoldCall(driverCallReceived1);
	    
	    softPhoneCalling.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);

	    // verify call entry in history for agent
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived3, acdQueueLPName);

	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case --take_call_on_acd_queue_when_on_hold_call_with_call_forwarding_stay_connected-- passed ");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void verify_caller_routes_to_vm_after_dial_to_max_available_agents() {
		System.out.println("Test case --verify_caller_routes_to_vm_after_dial_to_max_available_agents-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
	    
		// delete first voice mail
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);
														
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "2");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	assertTrue(driverCallReceived3 == null);
	    }
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verifying call goes to first longest waiting agent & waiting till full ring
	    softPhoneCalling.softphoneAgentCall(driver5, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver5);
	    
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	    softPhoneCalling.declineCall(driverCallReceived1);
	   
	    //verifying call goes to second longest waiting agent & waiting till full ring
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.declineCall(driverCallReceived2);
	    
	    //verifying call does not goes to third longest waiting agent since max dials is 2
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));

	    softPhoneCalling.idleWait(20);
	    softPhoneCalling.hangupIfInActiveCall(driver5);

	    // verify call entry in history for second agent
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);

		callQueuesPage.switchToTab(driver1, 2);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case --verify_caller_routes_to_vm_after_dial_to_max_available_agents-- passed ");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_not_affected_on_queue_when_changed_from_default_to_longest_waiting_agent() {
		System.out.println("Test case --verify_call_not_affected_on_queue_when_changed_from_default_to_longest_waiting_agent-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.switchToTab(driver1, 1);
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
	    // Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

	    // Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectDefaultDistributationType(driver1);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //navigating to call queues of any member
	    softPhoneCallQueues.switchToTab(driver4, 1);
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.toggleToCalls(driver4);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    
	    //making distribution type longest type
	    callQueuesPage.switchToTab(driver1, 2);
	    callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
	    //verifying call still visible on queue and not affected
	    softPhoneCallQueues.switchToTab(driver4, 1);
	    softPhoneCallQueues.openCallQueuesSection(driver4);
	    softPhoneCallQueues.toggleToCalls(driver4);
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    assertTrue(softPhoneCalling.isCallHoldButtonVisible(driver4));
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
		System.out.println("Test case --verify_call_not_affected_on_queue_when_changed_from_default_to_longest_waiting_agent-- passed ");
	}	

	@Test(groups = { "MediumPriority" })
	public void verify_call_not_routed_when_changed_from_longest_waiting_agent_to_default_queue_type() {
		System.out.println("Test case --verify_call_not_routed_when_changed_from_longest_waiting_agent_to_default_queue_type-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		// delete first voice mail	
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(driver1, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(driver1, 1);
		}

		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver4);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(driver4, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(driver4, 1);
		}

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver3);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(driver3, 1);
		}

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verifying call goes to first longest waiting agent
	    softPhoneCalling.softphoneAgentCall(driver5, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver5);

	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	 
	    //making distribution type default
	    callQueuesPage.switchToTab(driver1, 2);
	    callQueuesPage.selectDefaultDistributationType(driver1);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);	
		
		//wait for full ring and verify call not routed to next member
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.declineCall(driverCallReceived1);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		// verify call entry in history for second agent
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.switchToVoiceMailTab(driverCallReceived2);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case --verify_call_not_routed_when_changed_from_longest_waiting_agent_to_default_queue_type-- passed ");
	}	
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_routed_when_member_added_in_ACD_queue_after_dial() {
		System.out.println("Test case --verify_call_routed_when_member_added_in_ACD_queue_after_dial-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		String agentName = CONFIG.getProperty("qa_agent_user_name");
		 
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, agentName);
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);

		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verifying call goes to first longest waiting agent
	    softPhoneCalling.softphoneAgentCall(driver5, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver5);
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	 
	    //adding member in acd queue
	    callQueuesPage.switchToTab(driver1, 2);
	    callQueuesPage.openAddMemberWindow(driver1);
	    callQueuesPage.searchUserForAdd(driver1, agentName);
	    callQueuesPage.selectUsersToAdd(driver1, agentName);
	    callQueuesPage.clickAddForUsers(driver1);
		callQueuesPage.clickSaveGroupButton(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		 
		// verifying call not affected by adding member and goes to third longest waiting agent
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
		
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
	
		softPhoneCalling.switchToTab(driverCallReceived3, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		callQueuesPage.switchToTab(driver1, 2);
		callQueuesPage.scrollTillEndOfPage(driver1);
	    callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);	
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case --verify_call_routed_when_member_added_in_ACD_queue_after_dial-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_call_routed_when_member_removed_from_ACD_queue_after_dial() {
		System.out.println("Test case --verify_call_routed_when_member_removed_from_ACD_queue_after_dial-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);							  
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.addMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //verifying call goes to first longest waiting agent
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	 
	    //removing member in acd queue after dial
	    callQueuesPage.switchToTab(driver1, 2);
	    callQueuesPage.scrollTillEndOfPage(driver1);
	    callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
	    callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);	
		
		// verifying call not affected by removing member and goes to third longest waiting agent
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
		
		softPhoneCalling.switchToTab(driverCallReceived2, 1);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
	
		softPhoneCalling.switchToTab(driverCallReceived3, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
		System.out.println("Test case --verify_call_routed_when_member_removed_from_ACD_queue_after_dial-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_routed_to_next_agent_when_longest_waiting_agent_closes_softphone() {
		System.out.println("Test case --verify_call_routed_to_next_agent_when_longest_waiting_agent_closes_softphone-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver4);
		callQueuesPage.openCallQueueSearchPage(driver4);
		callQueuesPage.openCallQueueDetailPage(driver4, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver4, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver4);
		callQueuesPage.setMaxDials(driver4, "4");
		callQueuesPage.saveGroup(driver4);
		callQueuesPage.closeTab(driver4);
		callQueuesPage.switchToTab(driver4, 1);
		
		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    String frwrdingNumberSkype = CONFIG.getProperty("skype_number");
	    
		// selecting call forwarding skype
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driverCallReceived1);
		softPhoneSettingsPage.selectForwardingNumberFromDropDown(driverCallReceived1, frwrdingNumberSkype);

	    //verifying call goes to first longest waiting agent
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.idleWait(3);
	    callScreenPage.verifyUserImageBusy(driverCallReceived1);
	 
	    //closing tab of lwa
	    dashboard.closeTab(driverCallReceived1);
		 
		// verifying call goes to second longest waiting agent
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
		System.out.println("Test case --verify_call_routed_to_next_agent_when_longest_waiting_agent_closes_softphone-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_ACD_caller_redirected_to_agent_if_agent_free_but_having_hold_calls() {
		System.out.println("Test case --verify_ACD_caller_redirected_to_agent_if_agent_free_but_having_hold_calls-- started ");

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

		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		String callReceivedNumber1 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneCalling.hangupIfHoldCall(driver1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.hangupIfHoldCall(driver3);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneCalling.hangupIfHoldCall(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		// delete first voice mail
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);
	
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
			callReceivedNumber1 = CONFIG.getProperty("qa_user_1_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
			callReceivedNumber1 = CONFIG.getProperty("qa_user_2_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
			callReceivedNumber1 = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// taking inbound call and placing on hold
		softPhoneCalling.softphoneAgentCall(driver5, callReceivedNumber1);
		softPhoneCalling.isCallHoldButtonVisible(driver5);
		
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		softPhoneCalling.clickHoldButton(driverCallReceived1);
	    softPhoneCalling.isCallOnHold(driverCallReceived1);
	    
		callScreenPage.verifyUserImageBusy(driverCallReceived1);
		assertTrue(softPhoneSettingsPage.isSettingsIconVisible(driverCallReceived1));
		softPhoneCalling.clickDialPadIcon(driverCallReceived1);
		
	    //take a call on acd queue
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //verifying call comes on free agents
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
	    
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived3);

	    softPhoneCalling.hangupIfHoldCall(driverCallReceived1);
	    
	    softPhoneCalling.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);

	    // verify call entry in history for agent
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived3, acdQueueLPName);

	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_ACD_caller_redirected_to_agent_if_agent_free_but_having_hold_calls-- passed ");
	}
		
	@Test(groups = { "MediumPriority" })
	public void verify_voicemail_delivers_on_longest_waiting_queue_when_its_defined_under_voicemail_widget_of_call_flow() {
		System.out.println("Test case --verify_voicemail_delivers_on_longest_waiting_queue_when_its_defined_under_voicemail_widget_of_call_flow()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

//		String callFlow = "AutomationDialVoicemailACDFLow";
		String acdNoAnswerACDCallQueue = CONFIG.getProperty("qa_acd_group_no_answer_lp");
		String acdNoAnswerCallFlowNumber = "+17706277691";
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdNoAnswerACDCallQueue);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdNoAnswerACDCallQueue);
	    
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdNoAnswerACDCallQueue);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdNoAnswerACDCallQueue);

		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdNoAnswerACDCallQueue);

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdNoAnswerACDCallQueue);

		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdNoAnswerACDCallQueue);
		
		//dialing call flow number, routes to dial step user
	    softPhoneCalling.softphoneAgentCall(driver4, acdNoAnswerCallFlowNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver4);
	    
	    softPhoneCalling.switchToTab(webSupportDriver, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(webSupportDriver);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(webSupportDriver));
	    softPhoneCalling.declineCall(webSupportDriver);

	    softPhoneCalling.idleWait(30);
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//call routes to voicemail
		// Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.playVMByCallQueue(driver3, acdNoAnswerACDCallQueue);

		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.playVMByCallQueue(driver1, acdNoAnswerACDCallQueue);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_voicemail_delivers_on_longest_waiting_queue_when_its_defined_under_voicemail_widget_of_call_flow-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_priority_agent_should_be_dialed_upon_next_acd_call_if_he_gets_free_before_calling() {
		System.out.println("Test case --verify_priority_agent_should_be_dialed_upon_next_acd_call_if_he_gets_free_before_calling()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callFlowNumber = "+12058283267";
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);
		
		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);
		
		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//making lwa busy
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		callScreenPage.setUserImageBusy(driverCallReceived1);
		
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//making lwa available
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		callScreenPage.setUserImageAvailable(driverCallReceived1);
				
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_priority_agent_should_be_dialed_upon_next_acd_call_if_he_gets_free_before_calling-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_lwa_agent_unsubscribed_and_subscribed_dial_priority_not_changed() {
		System.out.println("Test case --verify_lwa_agent_unsubscribed_and_subscribed_dial_priority_not_changed-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		// Unsubscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeQueue(driverCallReceived1, acdQueueLPName);
		softPhoneCallQueues.isQueueUnsubscribed(driverCallReceived1, acdQueueLPName);

		// making acd call again and verifying call goes to second user
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		// subscribe acd queue from user 2
		softPhoneCallQueues.subscribeQueue(driverCallReceived1, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driverCallReceived1, acdQueueLPName);
		
		// making acd call again and verifying dial priority not changed
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
				
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		
		//hanging up active call
		softPhoneCalling.hangupActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_lwa_agent_unsubscribed_and_subscribed_dial_priority_not_changed-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_acd_call_not_arrive_to_agent_if_agent_set_his_profile_image_to_busy() {
		System.out.println("Test case --verify_acd_call_not_arrive_to_agent_if_agent_set_his_profile_image_to_busy-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		softPhoneCalling.idleWait(2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.declineCall(driverCallReceived2);
		
		softPhoneCalling.idleWait(2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		softPhoneCalling.declineCall(driverCallReceived3);
		
		softPhoneCalling.idleWait(10);
		softPhoneCalling.hangupActiveCall(driver2);
		
	    //Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived1, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived3, 1));
	 
	    //making image busy for driverCallRecieved1 and driverCallRecieved2
	    callScreenPage.setUserImageBusy(driverCallReceived1);
	    callScreenPage.setUserImageBusy(driverCallReceived2);
	    
		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		softPhoneCalling.idleWait(2);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    
		softPhoneCalling.idleWait(2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
	    
		//hanging up active call
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_acd_call_not_arrive_to_agent_if_agent_set_his_profile_image_to_busy-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_call_should_not_route_to_no_answer_when_agents_are_not_available() {
		System.out.println("Test case --verify_call_should_not_route_to_no_answer_when_agents_are_not_available-- started ");

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

		String noAnswerNumberToSet = CONFIG.getProperty("prod_user_2_number");
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		String numberCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "3");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
			numberCallReceived3 = CONFIG.getProperty("qa_user_1_number");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
			numberCallReceived3 = CONFIG.getProperty("qa_user_2_number");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
			numberCallReceived3 = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//making no answer enable on driverCallReceved1 with driver5 number
		softPhoneSettingsPage.setNoAnswerNumber(driverCallReceived1, noAnswerNumberToSet);
		
		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(driver2));
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver5));
		
		//hanging up active call
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_call_should_not_route_to_no_answer_when_agents_are_not_available-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_caller_again_call_after_make_user_free() {
		System.out.println("Test case --verify_caller_again_call_after_make_user_free-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		String numberCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
			numberCallReceived3 = CONFIG.getProperty("qa_user_1_number");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
			numberCallReceived3 = CONFIG.getProperty("qa_user_2_number");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
			numberCallReceived3 = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//making user image busy for driverCallReceived1
		callScreenPage.setUserImageBusy(driverCallReceived1);
		
		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.declineCall(driverCallReceived2);
		
		softPhoneCalling.idleWait(1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		//hanging up active call
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// making user image available for driverCallReceived1
		callScreenPage.setUserImageAvailable(driverCallReceived1);

		// making acd call
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		softPhoneCalling.idleWait(1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.declineCall(driverCallReceived2);

		softPhoneCalling.idleWait(1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));

		// hanging up active call
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_caller_again_call_after_make_user_free-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_moves_to_next_agent_when_do_Not_forward_setting_on_and_lwa_logout() {
		System.out.println("Test case --verify_call_moves_to_next_agent_when_do_Not_forward_setting_on_and_lwa_logout-- started ");

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
		
		String frwardingNumberToSet = CONFIG.getProperty("prod_user_2_number");
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//setting call forwarding on lwa with do not forward setting on
		System.out.println("Setting call forwarding ON with dnd setting on");
		softPhoneSettingsPage.deleteCallForwardingNumber(driverCallReceived1, frwardingNumberToSet);
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softPhoneSettingsPage.openCallForwardingDrpDwn(driverCallReceived1);
		softPhoneSettingsPage.addNewForwardingNumber(driverCallReceived1, driver5, "", frwardingNumberToSet, true);

		//log out driverCallReceived1
		softPhoneSettingsPage.logoutSoftphone(driverCallReceived1);
		
		// making acd call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call do not goes to frwrding device
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver5));

		//verifying call goes to receiver2 and 3
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.declineCall(driverCallReceived2);
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		//hanging up active call
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_call_moves_to_next_agent_when_do_Not_forward_setting_on_and_lwa_logout-- passed ");
	}
	
//	@Test(groups = { "MediumPriority" })
	public void verify_agent_able_to_merge_resume_acd_call_with_another_call_which_is_hold() {
		System.out.println("Test case --verify_agent_able_to_merge_resume_acd_call_with_another_call_which_is_hold-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		String lwaAgentNumber = null;
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneCalling.hangupIfHoldCall(driver1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneCalling.hangupIfHoldCall(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneCalling.hangupIfHoldCall(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
			lwaAgentNumber = CONFIG.getProperty("qa_user_1_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
			lwaAgentNumber = CONFIG.getProperty("qa_user_2_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
			lwaAgentNumber = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//making call on hold for lwa
		softPhoneCalling.softphoneAgentCall(driver5, lwaAgentNumber);
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		softPhoneCalling.clickHoldButton(driverCallReceived1);
		
		//making a call with acd number
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.clickOnHoldButton(driverCallReceived1);
	    softPhoneCalling.clickResumeButton(driverCallReceived1);
		softPhoneCalling.clickOnHoldButton(driverCallReceived1);
	    softPhoneCalling.clickMergeButton(driverCallReceived1);
	    softPhoneCalling.idleWait(2);
	    
		//hanging up active call
	    softPhoneCalling.hangupIfHoldCall(driverCallReceived1);
	    softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_agent_able_to_merge_resume_acd_call_with_another_call_which_is_hold-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_caller_end_call_when_acd_call_gng_to_call_frwrding_set_number() {
		System.out.println("Test case --verify_caller_end_call_when_acd_call_gng_to_call_frwrding_set_number-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		String frwardingNumberToSet = CONFIG.getProperty("prod_user_2_number");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//setting call forwarding on lwa with do not forward setting off
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driverCallReceived1, driver5, "", frwardingNumberToSet);

		// making acd call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa and frwrding device
		softPhoneCalling.switchToTab(driverCallReceived1, 1);
		callScreenPage.verifyUserImageBusy(driverCallReceived1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		
		softPhoneCalling.isAcceptCallButtonVisible(driver5);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver5));

		//hanging up call from caller side
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		callScreenPage.idleWait(5);
		callScreenPage.verifyUserImageAvailable(driverCallReceived1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver5));
		
		// Open call entry in task
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driverCallReceived1);
		softPhoneActivityPage.clickFirstSubject(driverCallReceived1);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived1);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived1);
		System.out.println(sfTaskDetailPage.getAssignedToUser(driverCallReceived1));
//		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).contains(CONFIG.getProperty("salesforce_connect_user")));
		sfTaskDetailPage.verifyCallAbandoned(driverCallReceived1);
		callQueuesPage.closeTab(driverCallReceived1);
		callQueuesPage.switchToTab(driverCallReceived1, 1);
			 
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_caller_end_call_when_acd_call_gng_to_call_frwrding_set_number-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_call_flow_ends_prematurely_when_queue_contains_one_available_user() {
		System.out.println("Test case --verify_call_flow_ends_prematurely_when_queue_contains_one_available_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String acdQueueLPName = "AutomationACDNoAnswerLP";
		String acdQueueLPNumber = "+19702832764";
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		// delete first voice mail	
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);
		
		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		 // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver4, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver4);
	    softPhoneCalling.idleWait(1);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);
	    softPhoneCalling.hangupIfInActiveCall(driver4);
	    
	    //making lwa busy
	    callScreenPage.switchToTab(driverCallReceived1, 1);
	    callScreenPage.setUserImageBusy(driverCallReceived1);
	    
	    //verifying call goes to second longest waiting agent & waiting till full ring
	    softPhoneCalling.softphoneAgentCall(driver4, acdQueueLPNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver4);
	    assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);

	    softPhoneCalling.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		 //Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
	    softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);
	    callScreenPage.setUserImageAvailable(driverCallReceived1);
	    
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
		System.out.println("Test case --verify_call_flow_ends_prematurely_when_queue_contains_one_available_user-- passed ");
	}	

	@Test(groups = { "MediumPriority" })
	public void verify_caller_continues_dials_through_list_of_members() {
		System.out.println("Test case --verify_caller_continues_dials_through_list_of_members-- started ");

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
		
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueLPNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);

		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		callQueuesPage.switchToTab(driver1, 2);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "6");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making acd call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueLPNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		//waiting till full ring and call goes to lwa2
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		
		// waiting till full ring and call goes to lwa3
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		// waiting till full ring and call goes to lwa1
		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		// waiting till full ring and call goes to lwa2
		softPhoneCalling.declineCall(driverCallReceived1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		// waiting till full ring and call goes to lwa3
		softPhoneCalling.declineCall(driverCallReceived2);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		//hanging up call from caller side
		softPhoneCalling.declineCall(driverCallReceived3);	
		softPhoneCalling.idleWait(30);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.switchToTab(driverCallReceived2, 1);
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived2, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
	    softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived3, 1));
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driverCallReceived1, 1));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_caller_continues_dials_through_list_of_members-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_caller_itself_end_acd_call_flow_new_lead_assigned_to_salesforce_user() {
		System.out.println("Test case --verify_caller_itself_end_acd_call_flow_new_lead_assigned_to_salesforce_user-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callFlowNumber = "+12542310955";
		String acdQueueLPName = "AutomationLeadQueueGreeting";
		String campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
		
		// enable both lead creation on unanswered and through campaign calls only setting
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
		
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.enableCreateLeadUnansweredCall(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.disableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, callFlowNumber);
		smartNumbersPage.clickSmartNumber(driver1, callFlowNumber);
		if(!smartNumbersPage.isSalesForceCampaignVisible(driver1, campaignName)) {
			smartNumbersPage.addSalesForceCampaign(driver1, campaignName);
		}
		assertTrue(smartNumbersPage.isSalesForceCampaignVisible(driver1, campaignName));
		accountSalesforceTab.closeTab(driver1);
		accountSalesforceTab.switchToTab(driver1, 1);
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver5, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver5);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		
		//making caller multiple
		addCallerMultiple();
		
		// making call flow call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));

		//declining call and call goes to lwa2
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		//entering call notes
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
		
		// declining call
		softPhoneCalling.hangupActiveCall(driver2);
		
		//driverCallReceived2
		callScreenPage.idleWait(10);
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driverCallReceived2);
	    assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
	    assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		
		// verify the data for lead is same as contact in sfdc
		String salesForceConnectUser = CONFIG.getProperty("salesforce_connect_user");
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived2, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived2);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived2);
		assertEquals(sfTaskDetailPage.getAssignedToUser(driverCallReceived2), salesForceConnectUser);
		assertTrue(sfTaskDetailPage.getCallQueue(driverCallReceived2).contains(acdQueueLPName));
		sfTaskDetailPage.verifyCallAbandoned(driverCallReceived2);
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);
		
		//driverCallReceived1
		callScreenPage.idleWait(2);
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driverCallReceived1);
	    assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived1));
	    assertEquals(callScreenPage.getCallerName(driverCallReceived1), "Unknown Unknown");
		
		// verify the data for lead is same as contact in sfdc
	    softPhoneActivityPage.openTaskInSalesforce(driverCallReceived1, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived1);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived1);
		assertEquals(sfTaskDetailPage.getAssignedToUser(driverCallReceived1), salesForceConnectUser);
		assertTrue(sfTaskDetailPage.getCallQueue(driverCallReceived1).contains(acdQueueLPName));
		sfTaskDetailPage.verifyCallAbandoned(driverCallReceived1);
		seleniumBase.closeTab(driverCallReceived1);
		seleniumBase.switchToTab(driverCallReceived1, 1);

		//driverCallReceived3
		callScreenPage.idleWait(2);
	    softphoneCallHistoryPage.openRecentGroupCallEntry(driverCallReceived3);
	    assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived3));
	    assertEquals(callScreenPage.getCallerName(driverCallReceived3), "Unknown Unknown");
	    
		// verify the data for lead is same as contact in sfdc
	    softPhoneActivityPage.openTaskInSalesforce(driverCallReceived3, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived3);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived3);
		assertEquals(sfTaskDetailPage.getAssignedToUser(driverCallReceived3), salesForceConnectUser);
		assertTrue(sfTaskDetailPage.getCallQueue(driverCallReceived3).contains(acdQueueLPName));
		sfTaskDetailPage.verifyCallAbandoned(driverCallReceived3);
		seleniumBase.closeTab(driverCallReceived3);
		seleniumBase.switchToTab(driverCallReceived3, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_caller_itself_end_acd_call_flow_new_lead_assigned_to_salesforce_user-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_lead_should_create_for_agent_call_flow_time_widget() {
		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_time_widget-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// enable both lead creation on unanswered and through campaign calls only setting
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		
		
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		String acdQueueNumber = "+16467621253";
		String callFlowNumber = "+12134555669";
		String acdQueueLPName = "AutomationLeadQueueTime";
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		String driverCallReceivedUser2 = null;
		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		deleteContact();
		
		// making call flow call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived1).contains("Unknown"));
		softPhoneCalling.declineCall(driverCallReceived1);
		
		// verifying call goes to lwa2
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived2).contains("Unknown"));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
		assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived2), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived2), driverCallReceivedUser2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).equals(driverCallReceivedUser2));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived2).equals(driverCallReceivedUser2));
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_time_widget-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_lead_should_create_for_agent_call_flow_sms_widget() {
		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_sms_widget-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// enable both lead creation on unanswered and through campaign calls only setting
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		String acdQueueNumber = "+14124533261";
		String callFlowNumber = "+12034635275";
		String acdQueueLPName = "AutomationLeadQueueSMS";
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		String driverCallReceivedUser2 = null;
		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//deleting contact
		deleteContact();

		// making call flow call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived1).contains("Unknown"));
		softPhoneCalling.declineCall(driverCallReceived1);

		// verifying call goes to lwa2
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived2).contains("Unknown"));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
		assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived2), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived2), driverCallReceivedUser2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).equals(driverCallReceivedUser2));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived2).equals(driverCallReceivedUser2));
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_sms_widget-- passed ");
	}

//	@Test(groups = { "MediumPriority" })
	public void verify_lead_should_create_for_agent_call_flow_prompt_widget() {
		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_prompt_widget-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// enable both lead creation on unanswered and through campaign calls only setting
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	
		String acdQueueNumber = "+19199071779";
		String callFlowNumber = "+18607172721";
		String acdQueueLPName = "AutomationLeadQueuePrompt";
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		String driverCallReceivedUser2 = null;
		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.hangupIfInActiveCall(driver2);
	
		// Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver4)) {
			callScreenPage.deleteCallerObject(driver4);
			reloadSoftphone(driver4);
		}
		
		// making call flow call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		softPhoneCalling.idleWait(1);
		softPhoneCalling.openDialPad(driver2);
		softPhoneCalling.enterNumberInDialpad(driver2, "1");

		// verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		// verifying call goes to lwa2
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
		assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived2), "Unknown Unknown");

		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived2), driverCallReceivedUser2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).equals(driverCallReceivedUser2));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived2).equals(driverCallReceivedUser2));
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_prompt_widget-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_lead_should_create_for_agent_call_flow_menu_widget() {
		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_menu_widget-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// enable both lead creation on unanswered and through campaign calls only setting
		if(!accountSalesforceTab.isUserOnSalesforcePage(driver1)){
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountSalesforceTab.openSalesforceTab(driver1);
		}
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		String acdQueueNumber = "+12086470865";
		String callFlowNumber = "+19096394040";
		String acdQueueLPName = "AutomationLeadQueueMenu";
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		String driverCallReceivedUser2 = null;
		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}
		
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//delete contact
		deleteContact();
		
		// making call flow call
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		softPhoneCalling.idleWait(1);
		softPhoneCalling.openDialPad(driver2);
		softPhoneCalling.enterNumberInDialpad(driver2, "1");

		// verifying call goes to lwa1
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);

		// verifying call goes to lwa2
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
		assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived2), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived2), driverCallReceivedUser2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).equals(driverCallReceivedUser2));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived2).equals(driverCallReceivedUser2));
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case --verify_lead_should_create_for_agent_call_flow_menu_widget-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_call_routes_to_no_answer_step() {
		System.out.println("Test case --verify_call_routes_to_no_answer_step-- started ");

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
		
		String callFlowNumber = "+13232714385";
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;
		String lwaCallerName3 = null;
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);
		
		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);		
				
		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
			lwaCallerName3 = CONFIG.getProperty("qa_user_1_number");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
			lwaCallerName3 = CONFIG.getProperty("qa_user_2_number");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
			lwaCallerName3 = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//making last lwa agent busy on call
		softPhoneCalling.softphoneAgentCall(driver5, lwaCallerName3);
		softPhoneCalling.isCallHoldButtonVisible(driver5);		
		softPhoneCalling.pickupIncomingCall(driverCallReceived3);
		
		//making user image busy for next 2 users
		callScreenPage.setUserImageBusy(driverCallReceived2);
		callScreenPage.setUserImageBusy(driverCallReceived1);
		
		// dialing call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to neither lwa agents
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		
		softPhoneCalling.idleWait(15);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived3);
		
		callScreenPage.setUserImageAvailable(driverCallReceived2);
		callScreenPage.setUserImageAvailable(driverCallReceived1);
		
		//Open call entry for the users to whom voicemail is dropped
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);

		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
		softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
		softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived3, acdQueueLPName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_call_routes_to_no_answer_step-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_first_agent_answer_call_new_lead_created() {
		System.out.println("Test case --verify_first_agent_answer_call_new_lead_created-- started ");

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
		
		String callFlowNumber = "+12054193494";
		String acdQueueLPName = "AutomationLeadQueueDial";
		String accountName = CONFIG.getProperty("qa_user_account");
		String driverCallReceivedUser1 = null;
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.saveGroup(driver1);
		
		// Enable both lead creation on unanswered and through campaign calls only
		// setting
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver1);
		accountIntelligentDialerTab.disableCreateTaskEarlySetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		accountSalesforceTab.closeTab(driver1);
		accountSalesforceTab.switchToTab(driver1, 1);
		
		deleteContact();
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_1_name");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_2_name");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// dialing call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived1).contains("Unknown"));
		
		//picking up call
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived1));
		assertEquals(callScreenPage.getCallerName(driverCallReceived1), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived1), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived1, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived1);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived1), driverCallReceivedUser1);
		contactDetailPage.openRecentCallEntry(driverCallReceived1, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived1).equals(driverCallReceivedUser1));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived1).equals(driverCallReceivedUser1));
		seleniumBase.closeTab(driverCallReceived1);
		seleniumBase.switchToTab(driverCallReceived1, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_first_agent_answer_call_new_lead_created-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_second_agent_answer_call_new_lead_created() {
		System.out.println("Test case --verify_second_agent_answer_call_new_lead_created-- started ");

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
		
		String callFlowNumber = "+13155054267";
		String acdQueueLPName = "AutomationLeadQueueGreeting";
		String accountName = CONFIG.getProperty("qa_user_account");
		
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		String driverCallReceivedUser2 = null;
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		// enable both lead creation on unanswered and through campaign calls only
		// setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableDisableLeadCreationSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		accountSalesforceTab.closeTab(driver1);
		accountSalesforceTab.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
			driverCallReceivedUser2 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		softPhoneContactsPage.addContactIfNotExist(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// dialing call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.declineCall(driverCallReceived1);
		
		// verifying call goes to lwa2
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));

		//picking up call
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived2));
		assertEquals(callScreenPage.getCallerName(driverCallReceived2), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived2), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived2, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived2);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived2);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived2), driverCallReceivedUser2);
		contactDetailPage.openRecentCallEntry(driverCallReceived2, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived2).equals(driverCallReceivedUser2));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived2).equals(driverCallReceivedUser2));
		seleniumBase.closeTab(driverCallReceived2);
		seleniumBase.switchToTab(driverCallReceived2, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_second_agent_answer_call_new_lead_created-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_last_agent_answer_call_new_lead_created() {
		System.out.println("Test case --verify_last_agent_answer_call_new_lead_created-- started ");

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
		
		String callFlowNumber = "+13155054267";
		String acdQueueLPName = "AutomationLeadQueueGreeting";
		String accountName = CONFIG.getProperty("qa_user_account");
		String campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
		String driverCallReceivedUser3 = null;

		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.saveGroup(driver1);
		
		//enable settings
		dashboard.clickAccountsLink(driver1);
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.disableCreateLeadEveryInboundCallSetting(driver1);
		accountSalesforceTab.saveAcccountSettings(driver1);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCreateLeadOnMultiSearchSetting(driver1);
		accountIntelligentDialerTab.enableCreateLeadSFDCCampaign(driver1);
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, callFlowNumber);
		smartNumbersPage.clickSmartNumber(driver1, callFlowNumber);
		if(!smartNumbersPage.isSalesForceCampaignVisible(driver1, campaignName)) {
			smartNumbersPage.addSalesForceCampaign(driver1, campaignName);
		}
		assertTrue(smartNumbersPage.isSalesForceCampaignVisible(driver1, campaignName));
		
		accountSalesforceTab.closeTab(driver1);
		accountSalesforceTab.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
			driverCallReceivedUser3 = CONFIG.getProperty("qa_user_1_name");
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
			driverCallReceivedUser3 = CONFIG.getProperty("qa_user_2_name");
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
			driverCallReceivedUser3 = CONFIG.getProperty("qa_user_3_name");
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		addCallerMultiple();
		
		// dialing call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		//verifying call goes to lwa1
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived1).contains("Multiple"));
		assertTrue(callScreenPage.isCallerMultiple(driverCallReceived1));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
		
		// verifying call goes to lwa2
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived2).contains("Multiple"));
		assertTrue(callScreenPage.isCallerMultiple(driverCallReceived2));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
		
		// verifying call goes to lwa3
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		assertTrue(callScreenPage.getForeGroundCallerName(driverCallReceived3).contains("Multiple"));
		assertTrue(callScreenPage.isCallerMultiple(driverCallReceived3));
		
		//picking up call
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.pickupIncomingCall(driverCallReceived3);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driverCallReceived3));
		assertEquals(callScreenPage.getCallerName(driverCallReceived3), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driverCallReceived3), "Unknown Unknown");
		
		callToolsPanel.enterCallNotes(driverCallReceived3, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived3);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driverCallReceived3);
		System.out.println(contactDetailPage.getLeadOwner(driverCallReceived3));
		System.out.println(driverCallReceivedUser3);
		assertEquals(contactDetailPage.getLeadOwner(driverCallReceived3), driverCallReceivedUser3);
		contactDetailPage.openRecentCallEntry(driverCallReceived3, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driverCallReceived3).equals(driverCallReceivedUser3));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driverCallReceived3).equals(driverCallReceivedUser3));
		seleniumBase.closeTab(driverCallReceived3);
		seleniumBase.switchToTab(driverCallReceived3, 1);
		
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		softPhoneContactsPage.addContactIfNotExist(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_last_agent_answer_call_new_lead_created-- passed ");
	}
	
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_flow_delivers_calls_in_correct_order_as_per_user_presence() {
		System.out.println("Test case --verify_call_flow_delivers_calls_in_correct_order_as_per_user_presence-- started ");

		// updating the driver used
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String callFlowNumber = "+12018014027";
		String acdQueueMaxDials1 = "AutomationQueueMaxDials1";
		String acdQueueMaxDials2 = "AutomationQueueMaxDials2";
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver3);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(driver3, 1);
		}
		
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);
		
		//Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(adminDriver);
	    softPhoneCallQueues.subscribeQueue(adminDriver, acdQueueMaxDials1);
	    softPhoneCallQueues.isQueueSubscribed(adminDriver, acdQueueMaxDials1);
	   
	    softPhoneCallQueues.unSubscribeAllQueues(webSupportDriver);
	    softPhoneCallQueues.subscribeQueue(webSupportDriver, acdQueueMaxDials2);
	    softPhoneCallQueues.isQueueSubscribed(webSupportDriver, acdQueueMaxDials2);
	    
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    
		// dialing call flow number
		softPhoneActivityPage.switchToTab(driver1, 1);
		softPhoneCalling.softphoneAgentCall(driver1, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);
		
		//verifying call goes to m1 user
		softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		assertTrue(callScreenPage.getForeGroundCallerName(adminDriver).contains(acdQueueMaxDials1));
		softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);
		
		//verifying call goes to m2 user
		softPhoneCalling.isAcceptCallButtonVisible(webSupportDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(webSupportDriver));
		assertTrue(callScreenPage.getForeGroundCallerName(webSupportDriver).contains(acdQueueMaxDials2));
		softPhoneCalling.verifyDeclineButtonIsInvisible(webSupportDriver);
		
		// verify call entry in history for second agent
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.switchToVoiceMailTab(driver3);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(driver3, 1));

		// Setting driver used to false as this test case is pass
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_call_flow_delivers_calls_in_correct_order_as_per_user_presence-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_sequential_dial_timeout_default_values() {
		System.out.println("Test case --verify_sequential_dial_timeout_default_values-- started ");
		
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//creating call queue
		loginSupport(driver4);
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("HH:mm:ss.SSS"));
		
		//adding new group details
		callQueuesPage.openAddNewCallQueue(driver4);
		callQueuesPage.addNewCallQueueDetails(driver4, callQueueName, callQueueDescName);
		
		callQueuesPage.selectSequentialDialDistributationType(driver4);

		//verifying more than 99 dial timeout
		callQueuesPage.setDialTimeout(driver4, "1200");
		callQueuesPage.clickSaveGroupButton(driver4);
		assertEquals(callQueuesPage.getHeaderNotification(driver4), "'' cannot be greater than 99");
		callQueuesPage.isHeaderNotificationInvisible(driver4);
		
		//making default values
		callQueuesPage.setDialTimeout(driver4, "12");
		callQueuesPage.saveGroup(driver4);
		
		//verifying more than 99 max dials
		callQueuesPage.setMaxDials(driver4, "1200");
		callQueuesPage.clickSaveGroupButton(driver4);
		assertEquals(callQueuesPage.getHeaderNotification(driver4), "'' cannot be greater than 99");
		callQueuesPage.isHeaderNotificationInvisible(driver4);

		callQueuesPage.deleteCallQueue(driver4);
		callQueuesPage.switchToTab(driver4, 1);

		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_sequential_dial_timeout_default_values-- passed ");
	}
	/*//@Test(groups={"Regression"})
	  public void acd_group_add_supervisor()
	  {
	    System.out.println("Test case --acd_group_add_supervisor()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String userName = CONFIG.getProperty("qa_user_3_name");
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();
	    List<String> acdDriversUsed = new ArrayList<String>();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    WebDriver driverCallReceived3 = null;
	    
		//adding agent as supervisor
		loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueues.openGroupSearchPage(driver1);
		callQueues.openGroupDetailPage(driver1, acdQueueName, accountName);
		callQueues.addSupervisor(driver1, userName);
		callQueues.isAgentAddedAsSupervisor(driver1, userName);
		callQueues.saveGroup(driver1);
	    callQueuesPage.switchToTab(driver1, 1);
		reloadSoftphone(driver3);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueName);
	    
	    // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    callQueuesPage.idleWait(3);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    softPhoneCalling.declineCall(driverCallReceived2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    callQueuesPage.idleWait(10);
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //remove the member from the groudp	
		callQueuesPage.switchToTab(driver1,callQueuesPage.getTabCount(driver1));
		callQueues.deleteSuperviosr(driver1, userName);
		callQueues.saveGroup(driver1);
	    callQueuesPage.closeTab(driver1);
	    callQueuesPage.switchToTab(driver1, 1);
		reloadSoftphone(driver3);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }*/
	
	
	public void addCallerMultiple() {
		String contactFirstName = "Contact_Existing";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.hangupActiveCall(driver1);

		// Deleting contact if exist
		if (!callScreenPage.isCallerMultiple(driver1) && callScreenPage.isCallerUnkonwn(driver1)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(driver1, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}

		if (!callScreenPage.isCallerMultiple(driver1)) {
			// Update caller to be contact
			callScreenPage.clickOnUpdateDetailLink(driver1);
			callScreenPage.addContactForExistingCaller(driver1, contactFirstName,
					CONFIG.getProperty("contact_account_name"));
			reloadSoftphone(driver1);

			// sync time from sfdc to ringdna
			seleniumBase.idleWait(30);

			softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("prod_user_1_number"));
		}
	}
	
	public void deleteContact() {
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		softPhoneContactsPage.addContactIfNotExist(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
			softPhoneCalling.pickupIncomingCall(driver2);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(driver2);
		}
	}
	
	/**delete contact parameters
	 * @param caller
	 * @param agentToDelete
	 * @param agentSmartNumber
	 * @param agentName
	 */
	public void deleteContact(WebDriver caller, WebDriver agentToDelete, String agentSmartNumber, String agentName) {
		softPhoneContactsPage.addContactIfNotExist(caller, agentSmartNumber, agentName);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
		softPhoneCalling.isCallHoldButtonVisible(caller);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(agentToDelete);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(agentToDelete);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(caller)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(caller);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(caller);
			reloadSoftphone(caller);
			softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
			softPhoneCalling.isCallHoldButtonVisible(caller);
			softPhoneCalling.pickupIncomingCall(agentToDelete);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(agentToDelete);
		}
		
		softPhoneCalling.hangupIfInActiveCall(caller);
		softPhoneCalling.hangupIfInActiveCall(agentToDelete);
	}
	
	/**add caller parameters
	 * @param caller
	 * @param agentToMultiple
	 * @param agentSmartNumber
	 */
	public void addCallerMultiple(WebDriver caller, WebDriver agentToMultiple, String agentSmartNumber) {
		String contactFirstName = "Contact_Existing";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
		softPhoneCalling.isCallHoldButtonVisible(caller);
		
		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(agentToMultiple);
		softPhoneCalling.hangupActiveCall(caller);

		// Deleting contact if exist
		if (!callScreenPage.isCallerMultiple(caller) && callScreenPage.isCallerUnkonwn(caller)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(caller, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}

		if (!callScreenPage.isCallerMultiple(caller)) {
			// Update caller to be contact
			callScreenPage.clickOnUpdateDetailLink(caller);
			callScreenPage.addContactForExistingCaller(caller, contactFirstName,
					CONFIG.getProperty("contact_account_name"));
			reloadSoftphone(caller);

			// sync time from sfdc to ringdna
			seleniumBase.idleWait(30);

			softPhoneContactsPage.searchUntilContacIsMultiple(caller, agentSmartNumber);
		}
	}
	
	
	//Verify No change in ACD queue order for next call if none of the agent answer and No answer Agent toggle setting is ON
	//Verify Logout agent sent to end of list when Toggle setting ON
	//Verify Decline agent sent to end of list when Toggle setting ON
	//Verify No answer agent sent to end of list when Toggle setting ON
	//Verify Busy agent sent to end of list when Toggle setting ON
	@Test(groups={"Regression", "Product Sanity"})
	  public void acd_call_agent_when_agent_no_answer_toggle_setting_on()
	  {
	    System.out.println("Test case --acd_call_agent_when_agent_no_answer_toggle_setting_on()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_2_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_2_number").trim();
	    List<String> acdDriversUsed = new ArrayList<String>();
	    WebDriver driverCallReceived1 = null;
	    WebDriver driverCallReceived2 = null;
	    WebDriver driverCallReceived3 = null;
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
	    //Unsubscribe and subscribe acd queue from user 3
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueName);
	    
	    //make call processing setting on
	    loginSupport(driver2);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver2);
		callQueuesPage.openCallQueueSearchPage(driver2);
		callQueuesPage.openCallQueueDetailPage(driver2, acdQueueName, CONFIG.getProperty("qa_user_account").trim());
		callQueuesPage.switchToTab(driver2, callQueuesPage.getTabCount(driver1));
		
		// set toggle on
		callQueuesPage.enableSendAgentAtLast(driver2);
		
		callQueuesPage.saveGroup(driver2);
	    callQueuesPage.switchToTab(driver2, 1);
	    
	    // making first call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
		//picking up with the first device
	    System.out.println("picking up with the first device");
	    if(softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived1 = driver1;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived1 = driver4;
	    }else if(softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived1 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived2 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived2 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived2 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.pickupIncomingCall(driverCallReceived2);
	    softPhoneCalling.hangupActiveCall(driverCallReceived2);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    // making second call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);

	    System.out.println("picking up with the first device");
	    if(!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)){
	    	acdDriversUsed.add("driver1");
	    	driverCallReceived3 = driver1;
	    }else if(!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)){
	    	acdDriversUsed.add("driver4");
	    	driverCallReceived3 = driver4;
	    }else if(!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)){
	    	acdDriversUsed.add("driver3");
	    	driverCallReceived3 = driver3;
	    }else{
	    	Assert.fail("No call received");
	    }
	    
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.declineCall(driverCallReceived1);

	    softPhoneCalling.pickupIncomingCall(driverCallReceived2);
	    softPhoneCalling.hangupActiveCall(driverCallReceived2);   //order - 3,1,2
	    
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    // making third call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
	    dashboard.idleWait(20);
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);   //order - 2,3,1
	    
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    // making fourth call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    dashboard.idleWait(20);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
	    dashboard.idleWait(20);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
	    dashboard.idleWait(20);
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    // making fifth call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
	    dashboard.idleWait(20);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
	    dashboard.idleWait(20);
	    softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
	    dashboard.idleWait(20);
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    // making sixth call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //set status busy
	    callScreenPage.setUserImageBusy(driverCallReceived3);
	    callScreenPage.verifyUserImageBusy(driverCallReceived3);
	    
	    softPhoneCalling.declineCall(driverCallReceived2);
	    softPhoneCalling.pickupIncomingCall(driverCallReceived1);
	    softPhoneCalling.hangupActiveCall(driverCallReceived1);     //order  -  2,3,1
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    //set status active
	    callScreenPage.setUserImageAvailable(driverCallReceived3);
	    callScreenPage.verifyUserImageAvailable(driverCallReceived3);
	    
	    // making seventh call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.declineCall(driverCallReceived2);
	    softPhoneCalling.declineCall(driverCallReceived3);
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    //Logout softphone
		softPhoneSettingsPage.logoutSoftphone(driverCallReceived3);
	    // making 8th call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.declineCall(driverCallReceived2);
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    //Logout softphone
		softPhoneSettingsPage.logoutSoftphone(driverCallReceived3);
	    // making 8th call to ACD queue
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    softPhoneCalling.declineCall(driverCallReceived2);
	    softPhoneCalling.declineCall(driverCallReceived1);
	    
		// set toggle off
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    callQueuesPage.switchToTab(driver2, callQueuesPage.getTabCount(driver1));
		callQueuesPage.disableSendAgentAtLast(driver2);
		
		callQueuesPage.saveGroup(driver2);
	    callQueuesPage.closeTab(driver2);
	    callQueuesPage.switchToTab(driver2, 1);
	    resetApplication();
	  
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	
	 @AfterMethod(groups = {"Regression", "MediumPriority", "Product Sanity"})
	  public void _afterMethod(ITestResult result){
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "acd_call_flow_default_values":
				if(callQueuesPage.isDeleteCallQueueBtnVisible(driver1)){
					callQueuesPage.deleteCallQueue(driver1);	
				}
				break;
			/*case "acd_group_add_supervisor":
				if(driver1.getTitle().contains("RingDNA Web")){
					callQueues.deleteSuperviosr(driver1, CONFIG.getProperty("qa_user_3_name"));
					callQueues.saveGroup(driver1);
				    callQueuesPage.closeTab(driver1);
				    callQueuesPage.switchToTab(driver1, 1);
					reloadSoftphone(driver3);	
				}
				break;*/
			case "acd_group_add_member":
				loginSupport(driver1);
				System.out.println("Account editor is opened ");
				dashboard.isPaceBarInvisible(driver1);
				callQueuesPage.openCallQueueSearchPage(driver1);
				callQueuesPage.openCallQueueDetailPage(driver1, CONFIG.getProperty("qa_acd_group_1_name"), CONFIG.getProperty("qa_user_account").trim());
				callQueuesPage.switchToTab(driver1, callQueuesPage.getTabCount(driver1));
				callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_user_3_name"));
				callQueuesPage.setMaxDials(driver1, "2");
				callQueuesPage.saveGroup(driver1);
			    callQueuesPage.closeTab(driver1);
			    callQueuesPage.switchToTab(driver1, 1);
				reloadSoftphone(driver3);
			break;
			case "verify_caller_routes_to_vm_after_dial_to_max_available_agents":
			case "verify_call_routed_when_changed_from_default_to_longest_waiting_agent_queue_type":	
				loginSupport(driver1);
				callQueuesPage.openCallQueueSearchPage(driver1);
				String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
				String accountName = CONFIG.getProperty("qa_user_account");
				callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
				callQueuesPage.selectLongestDistributationType(driver1);
				callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
				callQueuesPage.setMaxDials(driver1, "4");
				callQueuesPage.saveGroup(driver1);
				callQueuesPage.switchToTab(driver1, 1);
			default:
			}
		}
	 }
}
