/**
 * 
 */
package softphone.cases.callDashboard;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author admin
 *
 */
public class LiveMetrices extends SoftphoneBase{
	CallDashboardReact callDashboardReact = new CallDashboardReact();

	//Verify calls waiting in queue Live Metrics when 2 caller call same queue number
	@Test(groups={"MediumPriority"})
	  public void verify_same_queue_count_matric() {
	    System.out.println("Test case --verify_same_queue_count_matric-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("callDashboardDriver");
		driverUsed.put("callDashboardDriver", true);
		
		//preparing expected queue data to verify
		String queue1 = CONFIG.getProperty("qa_dashboard_queue");
		
		callDashboardReact.openCallDashBoardPage();
	    
		//subscribe queue1 for user 1 and take a call on that queue
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queue1);
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_dashboard_queue_number"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 1);
	    
	    //Verify that only 1 calls is appearing on ringDNA live app
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 1);
	    
	    //subscribe same queue for user 2 and take a call on that queue
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_dashboard_queue_number"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 2);
	    
	    //Verify that only 2 calls are appearing on ringDNA live app
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 2);
	    
	    //hangup active calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }

	//Verify calls waiting in queue Live Metrics when 2 caller call different queue number
	//Verify Real-time Average Queue Wait Time Metrics
	//Verify Real-time Average Queue Wait Time Metrics should change according the search bar and the filters
	@Test(groups={"MediumPriority"})
	  public void verify_different_queue_metric() {
	    System.out.println("Test case --verify_different_queue_metric-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("callDashboardDriver");
		driverUsed.put("callDashboardDriver", true);
		
		//preparing expected queue data to verify
		String queue1 = CONFIG.getProperty("qa_dashboard_queue");
		String queue2 = CONFIG.getProperty("qa_dashboard_queue_2");
		
		callDashboardReact.openCallDashBoardPage();
	    
		//subscribe queue1 for user 1 and take a call on that queue
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queue1);
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_dashboard_queue_number"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 1);
	    
	    //Verify that one queue count is appearing on ringDNA live app
	    callDashboardDriver.navigate().refresh();
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 1);
	    
	    //verify average call queue metric
	    assertEquals(callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue1), callDashboardReactPage.getQueueAverageWaitTime(callDashboardDriver));
	    
	    //subscribe queue2 for user 2 and take a call on that queue
	    softPhoneCallQueues.subscribeQueue(driver4, queue2);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_dashboard_queue_number_2"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 1);
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(driver4, 1);
	    
	    //Verify that two queue count is appearing on ringDNA live app
	    String callerName = softPhoneCallQueues.getQeueueCallerName(driver4);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 2);
	    
	    //verify average call queue metric
	    //Take two queues time
	    Date queue1Time = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue1), "hh:mm:ss");
	    Date queue2Time = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue1), "hh:mm:ss");
	     
	    //convert them in seconds and take average
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(queue1Time);
	    Calendar calendar1 = Calendar.getInstance();
	    calendar1.setTime(queue2Time);
	    
	    int avgTimeInSeconds = (calendar.get(Calendar.SECOND) + calendar1.get(Calendar.SECOND))/2;
	    int avgTimeMin	= avgTimeInSeconds / 60;
	    int avgTimesec	= avgTimeInSeconds % 60;

	    //Verify that the difference between calculated average time and actual average time appearing on ringDNA live is only 2 seconds
	    Date expectedAvgQueueTime = HelperFunctions.parseStringToDateFormat( "00:" + String.valueOf(avgTimeMin) + ":" + String.valueOf(avgTimesec), "hh:mm:ss");
	    Date avtualAvgQueueTime = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getQueueAverageWaitTime(callDashboardDriver), "hh:mm:ss");
	    int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(expectedAvgQueueTime, avtualAvgQueueTime, "hh:mm:ss");
		assertTrue(diffInMinutes >=0 && diffInMinutes <=2);
		
		//verify average time is changed when searching a queue
		callDashboardDriver.navigate().refresh();
		callDashboardReactPage.enterSearchText(callDashboardDriver, callerName);
	    		
		//Only 1 searched queue should appear on ringDNA live app
		seleniumBase.idleWait(1);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    
	    //verify only 1 searched queue should appear in waiting count on ringDNA live app
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 1);
	    
	    //verify average call queue metric
	    assertEquals(callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue2), callDashboardReactPage.getQueueAverageWaitTime(callDashboardDriver));
	    callDashboardReactPage.clearSearchText(callDashboardDriver);
	    
	    //hangup active calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Real-time Agent: Caller Ratio
	//Verify Longest Queue Wait Time Metrics
	//Verify Longest Queue Wait Time Metrics  should change according the search bar and the filters
	@Test(groups={"MediumPriority"})
	  public void verify_different_longest_queue() {
	    System.out.println("Test case --verify_different_longest_queue-- started ");
	    
	    quitDrivers();
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("callDashboardDriver");
		driverUsed.put("callDashboardDriver", true);
		
		//preparing expected queue data to verify
		String queue1 = CONFIG.getProperty("qa_dashboard_queue");
		String queue2 = CONFIG.getProperty("qa_dashboard_queue_2");
		
		callDashboardReact.openCallDashBoardPage();
		
		//Set user 1 as busy
		callScreenPage.setUserImageBusy(driver1);
	    
		//subscribe queue1 for user 1 and take a call on that queue
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queue1);
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_dashboard_queue_number"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 1);
	    
	    //Verify that only one queue entry is appearing on ringdna live app
	    callDashboardDriver.navigate().refresh();
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 1);
	    
	    //Available agents counts
	    int availableAgents =  callDashboardReactPage.getAvailableAgentsList(callDashboardDriver).size();
	    
	    //verify agent:caller ratio
	    callDashboardReactPage.verifyAgentCallerRatio(callDashboardDriver, availableAgents, 1);
	    
	    //verify average call queue metric
	    Date queueCallDuration = HelperFunctions.parseStringToDateFormat( callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue1), "hh:mm:ss");
	    Date longestQueueDuration = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getLongestQueueWaitTime(callDashboardDriver), "hh:mm:ss");
	    int diffInMinutes = HelperFunctions.getDateTimeDiffInSeconds(queueCallDuration, longestQueueDuration, "hh:mm:ss");
		assertTrue(diffInMinutes >=0 && diffInMinutes <=1);
	    
	    //subscribe queue2 for user 2 and take a call on that queue
	    softPhoneCallQueues.subscribeQueue(driver4, queue2);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_dashboard_queue_number_2"));
	    
	    //verify call is appearing on the queue
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(adminDriver, 1);
	    softPhoneCallQueues.verifyAllPickQueueCallBtnVisible(driver4, 1);
	    
	    //Verify that only two queue entry is appearing on ringdna live app
	    callDashboardDriver.navigate().refresh();
	    String callerName = softPhoneCallQueues.getQeueueCallerName(driver4);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 2);
	    
	    //verify agent:caller ratio
	    callDashboardReactPage.verifyAgentCallerRatio(callDashboardDriver, availableAgents, 2);
	  
	    //verify average call queue metric
	    queueCallDuration = HelperFunctions.parseStringToDateFormat( callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue1), "hh:mm:ss");
	    longestQueueDuration = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getLongestQueueWaitTime(callDashboardDriver), "hh:mm:ss");
	    diffInMinutes = HelperFunctions.getDateTimeDiffInSeconds(queueCallDuration, longestQueueDuration, "hh:mm:ss");
		assertTrue(diffInMinutes >=0 && diffInMinutes <=1);
		
		//verify average time is changed when searching a queue
		callDashboardReactPage.enterSearchText(callDashboardDriver, callerName);
	    		
		//Verify that by default for all options all queues calls are appearing on dashboard
		seleniumBase.idleWait(1);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    
	    //verify call waiting in queue metric
	    assertEquals(callDashboardReactPage.getQueueWaitingCallCount(callDashboardDriver), 1);
	    
	 	//verify average call queue metric
	    queueCallDuration = HelperFunctions.parseStringToDateFormat( callDashboardReactPage.getCallDurationByQueueName(callDashboardDriver, queue2), "hh:mm:ss");
	    longestQueueDuration = HelperFunctions.parseStringToDateFormat(callDashboardReactPage.getLongestQueueWaitTime(callDashboardDriver), "hh:mm:ss");
	    diffInMinutes = HelperFunctions.getDateTimeDiffInSeconds(queueCallDuration, longestQueueDuration, "hh:mm:ss");
		assertTrue(diffInMinutes >=0 && diffInMinutes <=1);
		callDashboardReactPage.clearSearchText(callDashboardDriver);
	    
	    //hangup active calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver5);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
}