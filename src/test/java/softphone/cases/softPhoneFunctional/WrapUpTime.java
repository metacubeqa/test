package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import support.source.callQueues.CallQueuesPage;

public class WrapUpTime extends SoftphoneBase{	
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_off_acd_group()
	  {
	    System.out.println("Test case --wrap_up_time_off_acd_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
		//picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //verify that caller is busy
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is available
	    callScreenPage.verifyUserImageAvailable(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_off_queue()
	  {
	    System.out.println("Test case --acd_group_add_member()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    
	    //verify that caller is busy
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is available
	    callScreenPage.verifyUserImageAvailable(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_1_min_acd_group()
	  {
	    System.out.println("Test case --wrap_up_time_1_min_acd_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.OneMin);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
		//Test 1: Connect with a caller and verify that after caller disconnects user is busy for 60 seconds wrap up time. After 60 secs it should be available again. 
	    //picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //verify that caller is busy
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 60 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 60 && wrapUpTime >= 57);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Wait till the wrap up time and verify that user image is available again
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
	    
		//Test 2: Verify that when first user's wrap up time is in countdown then the call trasfer to other user
		//Connect a call to user 1 and make it busy with wrap up time of 60 seconds
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.pickupIncomingCall(driver1);
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 60 seconds
	    wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 60 && wrapUpTime >= 57);
		callScreenPage.verifyUserImageBusy(driver1);
	
	    //Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueName);
	    
		//call again and verify that call is connecting with second user which is available
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    seleniumBase.idleWait(5);
	    
	    //verify that user 1 is not getting call
	    callScreenPage.verifyUserImageBusy(driver1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));
		
		//verify that call is connecting to another caller
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		reloadSoftphone(driver4);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_30_secs_queue()
	  {
	    System.out.println("Test case --wrap_up_time_30_secs_queue()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 30 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		 //Test 1: Try to make a direct call and verify that it fails
		 softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number").trim());
		 seleniumBase.idleWait(5);
		 assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));
		 callScreenPage.verifyUserImageBusy(driver1);
		 softPhoneCalling.hangupIfInActiveCall(driver2);
		 wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		 assertTrue(wrapUpTime <= 30 && wrapUpTime > 0);
		 
		//again take a call and make user busy for 30 seconds
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Test 4: verify that the warp up progress bar is there around the user image and user is able to pick the call
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver1));
		
		//Test 2: verify that user is able to pick the queue call while the wrap up time is on
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 30 seconds
	    wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Test 3: Wait till the wrap up time and verify that user image is available again after 30 seconds
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
	    
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_5_min_acd_group()
	  {
	    System.out.println("Test case --wrap_up_time_5_min_acd_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.FiveMin);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
		//picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //verify that caller is busy
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 60 seconds
	    int oldWrapUpTime = callScreenPage.getWrapUpTime(driver1);
	    System.out.println("Wrap up time is " + oldWrapUpTime);
		assertTrue(oldWrapUpTime <= 300 && oldWrapUpTime >= 297);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Wait till the wrap up time and verify that user image is available again
		seleniumBase.idleWait(11);
		int newWrapUpTime = callScreenPage.getWrapUpTime(driver1);
		System.out.println("Wrap up time after 10 seconds is " + newWrapUpTime);
		assertTrue(newWrapUpTime <= (oldWrapUpTime - 10) && newWrapUpTime >= (oldWrapUpTime - 13));
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Wait till the wrap up time and verify that user image is available again
		seleniumBase.idleWait(20);
		newWrapUpTime = callScreenPage.getWrapUpTime(driver1);
		System.out.println("Wrap up time after 30 seconds is " + newWrapUpTime);
		assertTrue(newWrapUpTime <= (oldWrapUpTime - 30) && newWrapUpTime >= (oldWrapUpTime - 33));
		callScreenPage.verifyUserImageBusy(driver1);
		
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_set_image_free()
	  {
	    System.out.println("Test case --wrap_up_time_set_image_free()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //verify that caller is busy for 30 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		seleniumBase.idleWait(2);
		
		 //Test 1: Set user image free while user is in call
		callScreenPage.setUserImageAvailable(driver1);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyUserImageAvailable(driver1);
		
		 //verify that user is able to make the call 
		 softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number").trim());
		 softPhoneCalling.pickupIncomingCall(driver1);;
		 seleniumBase.idleWait(5);
		 softPhoneCalling.hangupActiveCall(driver1);
		 softPhoneCalling.isCallBackButtonVisible(driver2);
		 
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);

	    //verify that caller is busy for 30 seconds
	    wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		seleniumBase.idleWait(2);
	    
	    //Test 2: extend time to busy status
		callScreenPage.extendWrapUpTime(driver1, CallScreenPage.WrapUpTimeExtensions.Busy);
		callScreenPage.verifyUserImageBusy(driver1);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_extend_30_secs()
	  {
	    System.out.println("Test case --wrap_up_time_extend_30_secs()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 30 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
	    
	    //Test 1: extend time to 30 seconds
		callScreenPage.extendWrapUpTime(driver1, CallScreenPage.WrapUpTimeExtensions.Thirty);
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime > 57 && wrapUpTime <= 60);
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_listen_call()
	  {
	    System.out.println("Test case --wrap_up_time_listen_call()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
	    
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver4);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver4);
	    
	    //verify that caller is busy for 30 seconds and wrap up countdown is there
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver4);
		
		//verify that the warpa up progress bar is there around the user image
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		
		//verify that listen button is not present
		softPhoneTeamPage.openTeamSection(driver1);
		assertFalse(softPhoneTeamPage.isListenButtonVisible(driver1, CONFIG.getProperty("qa_user_2_name").trim()));
		
		//verify user does still have wrap up time
		wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 30 && wrapUpTime > 0);
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
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
	
	@Test(groups={"MediumPriority"})
	public void wrap_up_time_call_directs_to_vm()
	  {
	    System.out.println("Test case --wrap_up_time_call_directs_to_vm()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.FiveMin);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Make call to the queue and verify that wrap time is 300 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 300 seconds and wrap up countdown is there
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 300 && wrapUpTime >= 297);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//verify that the warpa up progress bar is there around the user image
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver1));
		
		//get the missed call count to verify it later that it has increased
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver1);
		
		//Test 1: Try to make a direct call and verify that it fails
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number").trim());
		seleniumBase.idleWait(10);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver1));
		callScreenPage.verifyUserImageBusy(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 300 && wrapUpTime > 0);
		
		//verify that call is moved to voicemail
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver1, missedCallCount);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		softPhoneActivityPage.navigateToOpenTasksTab(driver1);
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		softPhoneActivityPage.playTaskRecording(driver1, callSubject);
		
		//verify user does still have wrap up time
		wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 300 && wrapUpTime > 0);
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver1));
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }	
	
	@Test(groups={"MediumPriority"})
	public void wrap_up_time_call_flow_directs_to_vm()
	  {
	    System.out.println("Test case --wrap_up_time_call_flow_directs_to_vm()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver4);
		callQueuesPage.openCallQueueSearchPage(driver4);
		callQueuesPage.openCallQueueDetailPage(driver4, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver4, CallQueuesPage.WrapUpTime.FiveMin);
		callQueuesPage.saveGroup(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
	    //Unsubscribe and subscribe queue from user 
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
	    
		//Make call to the queue and verify that wrap time is 300 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver4);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver4);
	    
	    //verify that caller is busy for 300 seconds and wrap up countdown is there
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 300 && wrapUpTime >= 297);
		callScreenPage.verifyUserImageBusy(driver4);
		
		//verify that the warpa up progress bar is there around the user image
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		
		//get the missed call count to verify it later that it has increased
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//Test 1: Try to make a direct call and verify that it fails
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_call_flow_number").trim());
		seleniumBase.idleWait(10);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		callScreenPage.verifyUserImageBusy(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 300 && wrapUpTime > 0);
		
		//verify that call is moved to voicemail
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, missedCallCount);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings"));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneActivityPage.navigateToOpenTasksTab(driver4);
		softPhoneActivityPage.navigateToAllActivityTab(driver4);
		softPhoneActivityPage.playTaskRecording(driver4, callSubject);
		
		//verify user does still have wrap up time
		wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 300 && wrapUpTime > 0);
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver4, 2);
		callQueuesPage.disableWrapUpTime(driver4);
		callQueuesPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_acd_group_busy()
	  {
	    System.out.println("Test case --wrap_up_time_1_min_acd_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.OneMin);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
		//picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //verify that caller is busy
	    callScreenPage.verifyUserImageBusy(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 60 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 60 && wrapUpTime >= 57);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Wait till the wrap up time and verify that user image is available again
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
		
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	public void wrap_up_time_call_flow_to_group()
	  {
	    System.out.println("Test case --wrap_up_time_call_flow_to_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String callFLowNumber = CONFIG.getProperty("qa_call_flow_call_queue_smart_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver4);
		callQueuesPage.openCallQueueSearchPage(driver4);
		callQueuesPage.openCallQueueDetailPage(driver4, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver4, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
	    //Unsubscribe and subscribe queue from user 
	    softPhoneCallQueues.subscribeQueue(driver4, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
	    
		//Make call to the call flow and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver2, callFLowNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver4);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver4);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver4);
	    
	    //verify that caller is busy for 30 seconds and wrap up countdown is there
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver4);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver4);
		
		//verify that the warpa up progress bar is there around the user image
		assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		
		// disable wrap up time
		seleniumBase.switchToTab(driver4, 2);
		callQueuesPage.disableWrapUpTime(driver4);
		callQueuesPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_leave_vm()
	  {
	    System.out.println("Test case --wrap_up_time_leave_vm()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
		//Make call to the queue and verify that wrap time is 30 seconds
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver4, queueNumber);
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    seleniumBase.idleWait(5);	    
	    callScreenPage.verifyUserImageBusy(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 30 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
	    
	    //make user available
		callScreenPage.setUserImageAvailable(driver1);
		
		//Call on the queue again and mek user available
	    System.out.println("Make call to the queue");
	    softPhoneCalling.softphoneAgentCall(driver4, queueNumber);
	    softPhoneCallQueues.openCallQueuesSection(driver1);
	    seleniumBase.idleWait(5);	
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    callToolsPanel.dropVoiceMailByName(driver4, "Automation Global Voicemail");
	    softPhoneCalling.isCallBackButtonVisible(driver4);
	    
	    //Verify that call is not there on queue page
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    seleniumBase.idleWait(10);
	    
		//Test: verify that user image is available and there is no wrap up time
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
		 
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void wrap_up_time_transfer_to_acd_group()
	  {
	    System.out.println("Test case --wrap_up_time_transfer_to_acd_group()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String acdQueueName = CONFIG.getProperty("qa_acd_group_1_name").trim();
	    String acdQueueNumber = CONFIG.getProperty("qa_acd_group_1_number").trim();
	    String accountName = CONFIG.getProperty("qa_user_account").trim();;
	    
		//adding agent again as supervisor
	    loginSupport(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueName, accountName);
		callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.ThirtySecs);
		callQueuesPage.saveGroup(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
	    //Unsubscribe and subscribe acd queue from user 1
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);
	    
	    //picking up with the first device
	    System.out.println("making first call to ACD queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //verify that caller is busy
	    softPhoneCalling.transferToNumber(driver4, acdQueueNumber);
	    
	    //pickup call from caller
	    softPhoneCalling.pickupIncomingCall(driver1);
	    
	    //hangup with the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that caller is busy for 30 seconds
	    int wrapUpTime = callScreenPage.getWrapUpTime(driver1);
		assertTrue(wrapUpTime <= 30 && wrapUpTime >= 27);
		callScreenPage.verifyUserImageBusy(driver1);
		
		//Wait till the wrap up time and verify that user image is available again
		seleniumBase.idleWait(wrapUpTime);
		callScreenPage.verifyWrapUpTimeInvisible(driver1);
		callScreenPage.verifyUserImageAvailable(driver1);
	    
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		reloadSoftphone(driver4);
	    
	    // Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
}