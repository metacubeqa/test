package softphone.cases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class prodHealthCheck extends SoftphoneBase{
		
	  //This is a prod health check case
	  @Test(groups = { "Regression" }, priority = 1)
	  public void prod_incoming_call_through_queue() {
	    System.out.println("Test case --prod_incoming_call_through_queue-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    //subscribe a queue
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	    
	    if(softPhoneCallQueues.isPickCallBtnPresent(driver1)) {
	    	loginSupport(driver1);
	    	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	    	accountIntelligentDialerTab.deleteLiveQueueByName(driver1, queueName);
	    	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    	accountOverviewtab.closeTab(driver1);
	    	accountOverviewtab.switchToTab(driver1, 1);
	    }
	    
	    //Taking a call on the queue
	    System.out.println("caller making call to call flow");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_group_1_number"));
	    
		//receiving call from the queue
	    System.out.println("pick up a call from queue");
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.hangupIfInActiveCall(driver3);
	    System.out.println("Incoming call through queue case passed");
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	//This is a prod health check case
	  @Test(groups = { "Regression" }, priority = 2)
	  public void prod_outgoing_call() {
	    System.out.println("Test case --prod_outgoing_call-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

		//taking incoming call
	    System.out.println("making an outbound call");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    String callSubject1 = callToolsPanel.changeAndGetCallSubject(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject1);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    sfTaskDetailPage.isCreatedByRingDNA(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    System.out.println("outgoing call case passed");
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //This is a prod health check case
	  @Test(groups = { "Regression" }, priority = 3)
	  public void prod_outbound_message() {
	    System.out.println("Test case --prod_outbound_message-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
  		//Making an outbound call from softphone
  	    System.out.println("agent making call to first caller");
  	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
  	    
  	    //send a text message to caller 2
  	    softPhoneActivityPage.openMessageTab(driver1);
  	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);  	    
  	    
  	    //wait for message to get received
  	    softPhoneMessagePage.idleWait(2);
  	    
  	    //verify that message is received by caller 2
  	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
  	    softPhoneActivityPage.openMessageTab(driver3);
  	    softPhoneActivityPage.verifyInboundMessage(driver3, outboundMessage);
  	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Outbound Messaging Test case is pass");
	  }
	  
	//This is a prod health check case
	  @Test(groups = { "Regression" }, priority = 4)
	  public void prod_inbound_mesage() {
	    System.out.println("Test case --prod_inbound_mesage-- started ");
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    String InboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  	    
	    //Making an outbound call from softphone
  	    System.out.println("agent making call to first caller");
  	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
	    
  	    //send a text message from caller 2
  	    softPhoneActivityPage.openMessageTab(driver3);
  	    softPhoneActivityPage.sendMessage(driver3, InboundMessage, 0);
  	    
  	    //wait for message to get received
  	    softPhoneMessagePage.idleWait(2);
  	    
  	    //verify that message is received by agent
  	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
  	    softPhoneActivityPage.openMessageTab(driver1);
  	    softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage);	    
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Inbound Messaging Test case is pass");
	  }
}
