/**
 * 
 */
package softphone.cases.callDashboard;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

/**
 * @author Abhishek
 *
 */
public class CallDashboard extends SoftphoneBase{
	
	@Test(groups={"Sanity", "Product Sanity"})
	  public void call_dashboard_call_directions() {
	    System.out.println("Test case --call_dashboard_call_directions-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
		callDashboardPage.openCallDashboardPage(driver1);
	    
	    String agentName = CONFIG.getProperty("qa_user_3_name").trim();
	    
	    //Verify outbound Call on Call Dashboard
		//taking incoming call
	    System.out.println("Taking call from first caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call
	    System.out.println("Picking up call from called agent");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //verify that agent entry is present on call Dashboard
	    int index = callDashboardPage.getActiveCallUsersIndex(driver1, agentName);
	    callDashboardPage.verifyOutgoingCallUsingIndex(driver1, index);
	    
		//hanging up with the second caller
	    System.out.println("hanging up with the called agent");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from agent");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //Verify inbound Call on Call Dashboard
		//taking incoming call
	    System.out.println("Taking call from first caller");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call
	    System.out.println("Picking up call from called agent");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //verify that agent entry is present on call Dashboard
	    index = callDashboardPage.getActiveCallUsersIndex(driver1, agentName);
	    callDashboardPage.verifyIncomingCallUsingIndex(driver1, index);
	    
		//hanging up with the second caller
	    System.out.println("hanging up with the called agent");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from agent");
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
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
	
	@Test(groups={"Sanity", "Product Sanity"})
	  public void call_dashboard_send_queue_call_to_agent() {
	    System.out.println("Test case --call_dashboard_send_queue_call_to_agent-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String agentName = CONFIG.getProperty("qa_user_3_name").trim();
	    String queueName = CONFIG.getProperty("qa_group_1_name").trim();
	    
		callDashboardPage.openCallDashboardPage(driver1);
		
	    //Unsubscribe all queues
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    
	    //subscribe queues
	    softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    
	    //Verify outbound Call on Call Dashboard
		//taking incoming call
	    System.out.println("making call to queue");
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));
	    
	    //verify that agent entry is present on call Dashboard
	    int index = callDashboardPage.getActiveQueueIndex(driver1, queueName);
	    callDashboardPage.clickSendToLinkUsingIndex(driver1, index);
	    callDashboardPage.selectSentToAgent(driver1, agentName, index);
	    
		//receiving call
	    System.out.println("Picking up call from called agent");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
		//hanging up with the second caller
	    System.out.println("hanging up with the called agent");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from agent");
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
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
}
