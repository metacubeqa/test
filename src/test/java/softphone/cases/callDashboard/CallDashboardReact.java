/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.callDashboard;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.callQueues.CallQueuesPage;

public class CallDashboardReact extends SoftphoneBase{

	//Verify Real time call dashboard in admin navigates to call dashboard url
	//Verify display IMG if when no Active Calls listed
	//Verify name changed  'Call Dashboard' to 'ringDNA Live'
	@Test(groups={"Regression", "MediumPriority"})
	  public void verify_call_dashoard_access_from_admin_app() {
	    System.out.println("Test case --verify_call_dashoard_access_from_admin_app-- started ");
	    
		//updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    //Open call dashboard page from support app
	    //verify that the ringDNA Live heading is appearing
		loginSupport(callDashboardDriver);
		dashboard.openCallDashboardPage(callDashboardDriver);
		//callDashboardReactPage.verifyLoadingIcon(callDashboardDriver);
		callDashboardReactPage.verifyNoActiveCallsImageVisible(callDashboardDriver);
		
		//deleting supervisor from Team and verify Call Dashboard link is not there for the user
		loginSupport(callDashboardDriver);
  		dashboard.isPaceBarInvisible(callDashboardDriver);
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team_2"), "");
  		
  		//deleting supervisor from Team and verify Call Dashboard link is not there for the user
  		groupsPage.deleteSuperviosr(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team"), "");
  		groupsPage.deleteSuperviosr(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team_2"), "");
  		groupsPage.deleteSuperviosr(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  	    reloadSoftphone(callDashboardDriver);
	  	dashboard.verifyCallDashboardLinkInvisible(callDashboardDriver);
  	    
		//Add user as Supervisor again into the team
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team"), "");
  		groupsPage.addSupervisor(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team_2"), "");
  		groupsPage.addSupervisor(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  		reloadSoftphone(callDashboardDriver);
  		
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team_2"), "");
  		
  		//Add user as Supervisor again into the team
  		groupsPage.addSupervisor(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		reloadSoftphone(callDashboardDriver);
  		
  		seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver)	);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify grid view should not show active calls
	//Verify grid view to active calls
	//Verify display IMG if search different text on Active Calls
	//Verify Active Calls Table View
	@Test(groups={"MediumPriority"})
	  public void verify_active_calls_view() {
	    System.out.println("Test case --verify_active_calls_view-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    String contactNumber = CONFIG.getProperty("qa_admin_user_number");
	    
	    //verify that no icon is there for grid and row type view
	    callDashboardReactPage.verifyActiveCallHeadingBar(callDashboardDriver);
	    
	    //verify default call rows heading
	    callDashboardReactPage.verifyRowHeading(callDashboardDriver);
	    
	    //make multiple calls to the agent and pick them
	    softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	    softPhoneCalling.pickupIncomingCall(adminDriver);
	    
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(adminDriver);
	    
	    softPhoneCalling.softphoneAgentCall(driver3, contactNumber);
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(adminDriver);
	    
	    softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(adminDriver);
	   
	    //get the agent name and caller for the latest call
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    String callerName = callScreenPage.getCallerName(adminDriver);
	    
	    //verify the call entry for the last call
	    callDashboardReactPage.expandResizeHandler(callDashboardDriver);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 6, agentName, callerName, null, null, true);
	    
	    //verify Active call heading
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 6);
	    
	    //sort the call duraion anc verify it is sorted
	    callDashboardReactPage.sortAscCallDuration(callDashboardDriver);
	    callDashboardReactPage.verifyCallDurationsAreSortedAscending(callDashboardDriver);
	    
	    //Search with an invalid string so that no search image and text appears
	    callDashboardReactPage.enterSearchText(callDashboardDriver, "abcd123");
	    callDashboardReactPage.verifyNoActiveCallsImageVisible(callDashboardDriver);
	    callDashboardReactPage.clearSearchText(callDashboardDriver);
	    
	    //hangup with the calls
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify login to call dashboard
	//Verify that Left Nav is showing a successfully on Call Dashboard
	//Verify Logging out of the call dashboard
	@Test(groups={"MediumPriority"})
	public void verify_login_directly_to_call_dashoard() {
		System.out.println("Test case --verify_login_directly_to_call_dashoard-- started ");
		
		driverUsed.put("driver1", false);
		
		//updating the driver used 
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		callScreenPage.setUserImageBusy(driver3);
		
		//updating the driver used 
		if(driver1 != null) {
			driver1.quit();
			driver1 = null;
		}
		
		//login to Call Dashboard Page directly and verify logged into right page
		driver1 = getDriver();
		SFLP.supportLogin(driver1, CONFIG.getProperty("call_dashboard_react_url"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));  
		callDashboardReactPage.allCallDashboardElements(driver1);
		
		//verify that sidebar exists with atleast 15 menu items
		assertTrue(callDashboardReactPage.getSideMeduItems(driver1).size() >= 15);
		
		//Logout user
		callDashboardReactPage.logoutUser(driver1);
		SFLP.verifyLoginBtnVisible(driver1);
		
		driver1.quit();
		driver1 = null;
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify session is invalid  then Call Dashboard force logout
	//Verify Call Dashboard navigation link should not open in new tab.
    @Test(groups={"MediumPriority"})
    public void call_dashboard_logout_terminate_sessions() {
    	System.out.println("Test case --call_dashboard_logout_terminate_sessions-- started ");
    	
    	driverUsed.put("driver1", false);
    	
    	//updating the driver used 
    	initializeDriverSoftphone("callDashboardDriver");
    	driverUsed.put("callDashboardDriver", true);
    	
    	//Open call dashboard page on a tab
    	openCallDashBoardPage();
    	
    	//open web app on one of the tab
    	loginSupport(callDashboardDriver);
    	
    	//Switch to softphone and logout
    	seleniumBase.switchToTab(callDashboardDriver, 1);
    	softPhoneSettingsPage.logoutSoftphone(callDashboardDriver);
    	
    	//switch to dashboard tab and verify that the logout message is appearing and then user is logged out and login button is visible
    	seleniumBase.switchToTab(callDashboardDriver, 2);
    	callDashboardReactPage.verifyLogoutMessage(callDashboardDriver);
    	SFLP.verifyLoginBtnVisible(callDashboardDriver);
    	
    	//switch to softphone and login
    	seleniumBase.switchToTab(callDashboardDriver, 1);
    	SFLP.clickLoginPageButton(callDashboardDriver);
    	SFLP.enterUserName(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_username"));
    	SFLP.enterPassword(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_password"));
    	SFLP.clickLoginButton(callDashboardDriver);
    	
    	//switch to the dashboard and refresh, verify that user is logged into call dashboard
    	seleniumBase.switchToTab(callDashboardDriver, 2);
    	reloadSoftphone(callDashboardDriver);
    	callDashboardDriver.get(CONFIG.getProperty("call_dashboard_react_url"));
    	callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
    	
    	//switch to the web app and logout the user
    	seleniumBase.switchToTab(callDashboardDriver, 3);
    	reloadSoftphone(callDashboardDriver);
    	dashboard.logoutAdminApp(callDashboardDriver);
    	
    	//siwtch to dashboard tab and verify that the logout message is appearing and then user is logged out and login button is visible
    	seleniumBase.switchToTab(callDashboardDriver, 2);
    	callDashboardReactPage.verifyLogoutMessage(callDashboardDriver);
    	SFLP.verifyLoginBtnVisible(callDashboardDriver);
    	
    	//switch to softphone and login
    	seleniumBase.switchToTab(callDashboardDriver, 1);
    	seleniumBase.idleWait(5);
    	SFLP.verifyForcedLogout(callDashboardDriver, "");
    	SFLP.clickLoginPageButton(callDashboardDriver);
    	SFLP.enterUserName(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_username"));
    	SFLP.enterPassword(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_password"));
    	SFLP.clickLoginButton(callDashboardDriver);
    	
    	//switch to the dashboard and refresh, verify that user is logged into call dashboard
    	seleniumBase.switchToTab(callDashboardDriver, 2);
    	reloadSoftphone(callDashboardDriver);
    	callDashboardDriver.get(CONFIG.getProperty("call_dashboard_react_url"));
    	callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
    	
    	//Setting driver used to false as this test case is pass
    	driverUsed.put("callDashboardDriver", false);
    	
    	System.out.println("Test case is pass");
    }
    
    //Verify UI of new react call dashboard for unassigned Call Queue
    //Verify Send to functionality on call dashboard
    //Verify queue calls shows of QA v2 account on call dashboard
    //Verify Active List of incoming queue calls on call dashboard
    //Verify queue name when a queue call is assigned to an agent
    @Test(groups={"MediumPriority"})
	  public void verify_contact_queue_call() {	
	    System.out.println("Test case --verify_contact_queue_call-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();

	 	String queuNumber = CONFIG.getProperty("qa_dashboard_queue_number");
	 	String queueName = CONFIG.getProperty("qa_dashboard_queue");
	 	
	 	//subscribe queue for the agent	
	 	softPhoneCallQueues.unSubscribeAllQueues(adminDriver);
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queueName);
	    
	 	//make the call to the queue and do not pick it up
	    softPhoneCalling.softphoneAgentCall(driver2, queuNumber);
	    softPhoneCallQueues.isPickCallBtnVisible(adminDriver);

	    //get the caller and agent name to be verified for the queue call
	    String callerName = softPhoneCallQueues.getQeueueCallerName(adminDriver);
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    
	    //verify the call direction and the data for queue call on call dashboard
	    //verify that Active Calls heading has 1 count for active number of calls
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 1, callerName, "QA", CONFIG.getProperty("contact_account_name"), queueName);
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
	    
	    //Click on caller name on the dashboard and veify it opens in salesforce tab
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //click on send to link on the queue row
	    //click on cancel button and verify that the call is not assigned to any agent
	    callDashboardReactPage.clickSendToLink(callDashboardDriver, 1);
	    callDashboardReactPage.verifySendToAvailableHeading(callDashboardDriver, "3");
	    callDashboardReactPage.verifyAssignAgentBtnDisabled(callDashboardDriver);
	    callDashboardReactPage.verifySendToAgentGreenStatus(callDashboardDriver);
	    
	    callDashboardReactPage.searchSendToAgent(callDashboardDriver, agentName);
	    callDashboardReactPage.verifySendToAvailableHeading(callDashboardDriver, "1");
	    assertEquals(callDashboardReactPage.getSendToAgentList(callDashboardDriver).size(), 1);
	    assertEquals(callDashboardReactPage.getSendToAgentList(callDashboardDriver).get(0), agentName);
	    callDashboardReactPage.clickCancelAgentBtn(callDashboardDriver);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 1, callerName, "QA", CONFIG.getProperty("contact_account_name"), queueName);
	    
	    //click on sent to link and verify that send to link button is disabled
	    //Verify that the agent list contains all the user of which the user is supervisor and caller is logged in
	    callDashboardReactPage.clickSendToLink(callDashboardDriver, 1);
	    assertTrue(callDashboardReactPage.getSendToAgentList(callDashboardDriver).contains(CONFIG.getProperty("qa_admin_user_name")));
	    assertFalse(callDashboardReactPage.getSendToAgentList(callDashboardDriver).contains(CONFIG.getProperty("qa_agent_user_name")));
	    assertTrue(callDashboardReactPage.getSendToAgentList(callDashboardDriver).contains(CONFIG.getProperty("qa_user_3_name")));

	    //now select an agent to send the call
	    callDashboardReactPage.selectAgent(callDashboardDriver, agentName);
	    callDashboardReactPage.verifySendToAgentTeamNameOnHover(callDashboardDriver, "1", agentName, "Call Dashboard Team");
	    callDashboardReactPage.clickAssignAgentBtn(callDashboardDriver);
	    
	    //verify that the call entry is assigned to the agent
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);

	    //verify that the call is appearing for the agent and pick up the call
	    softPhoneCalling.pickupIncomingCall(adminDriver);
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }

    //Verify for unknown caller name should show on call dashboard
    //Verify Active List of incoming calls on call dashboard
	@Test(groups={"MediumPriority"}, priority = 1)
	  public void verify_contact_unknown_on_dashboard() {
	    System.out.println("Test case --verify_contact_unknown_on_dashboard-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    String contactNumber = CONFIG.getProperty("qa_admin_user_number");
	    
	    //make a call to the agent and pick the call
	    System.out.println("make a call to the agent");
	    softPhoneCalling.softphoneAgentCall(driver5, contactNumber);
	    softPhoneCalling.pickupIncomingCall(adminDriver);
	 
	    // hanging up with caller 1
 		System.out.println("hanging up with caller 1");
 		softPhoneCalling.hangupActiveCall(adminDriver);
 		
 		softPhoneContactsPage.deleteAndAddContact(adminDriver, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));

 		// Deleting contact if it not already unknown
 		System.out.println("deleting contact");
 		if (!callScreenPage.isCallerUnkonwn(adminDriver)) {
 			// add caller as Contact
 			callScreenPage.deleteCallerObject(adminDriver);
 			seleniumBase.idleWait(3);
 			softPhoneSettingsPage.closeErrorMessage(adminDriver);
 		}
	    
	    //again make a call to the unknown caller and pick it up
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    softPhoneCalling.pickupIncomingCall(adminDriver);

	    //get the value of the agent and the caller for the unknown call entry
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    String callerName = callScreenPage.getCallerName(adminDriver);
	    
	    //now make a call entry for the known caller and pick it up
	    softPhoneCalling.softphoneAgentCall(driver5, contactNumber);
	    softPhoneCalling.pickupIncomingCallFromHeaderCallNotification(adminDriver);
	    
	    //verify the call direction and data for contact caller
	    seleniumBase.idleWait(5);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //Click on the caller name and verify that data is opened in salesforce and verify contact page is open
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //Click on the ACcount name and verify that salesforce tab is opened and verify page heading is Account name
	    callDashboardReactPage.clickRowAccountName(callDashboardDriver, 1);
	    salesforceAccountPage.verifyAccountsNameHeading(callDashboardDriver, CONFIG.getProperty("contact_account_name"));
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //verify the call data for unknown caller on dashboard
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 2, CONFIG.getProperty("qa_admin_user_name"), "Unknown Caller", null, null, true);
	    
	    //hangup the active calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver5);
	    
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify for multiple caller name should show on call dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_contact_multiple_on_dashboard() {
	    System.out.println("Test case --verify_contact_multiple_on_dashboard-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver1");
 		driverUsed.put("driver1", true);
 		initializeDriverSoftphone("driver5");
 		driverUsed.put("driver5", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();

	 	String queuNumber	= CONFIG.getProperty("qa_dashboard_queue_number");
	 	String queueName	= CONFIG.getProperty("qa_dashboard_queue");
	 	
	 	addCallerAsMultiple();

	 	//Subscribe queus for the agent
	 	softPhoneCallQueues.unSubscribeAllQueues(adminDriver);
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queueName);
	    
	 	//make a call to the queue and pick this call
	    softPhoneCalling.softphoneAgentCall(driver5, queuNumber);
	    softPhoneCallQueues.pickCallFromQueue(adminDriver);
	    List<String> actualMultiUserList = callScreenPage.getMultipleContactsName(adminDriver);
	    
	    //verify the data on call dashboard for multi match caller
	    callDashboardReactPage.openViewMultiCallerMenu(callDashboardDriver, 1);
	    assertEquals(callDashboardReactPage.getMultiUsersList(callDashboardDriver), actualMultiUserList);
	    callDashboardReactPage.collapseViewMultiCallerMenu(callDashboardDriver, 1);
	    
	    //hangup the call and change the caller from multiple to single
	    softPhoneCalling.hangupActiveCall(driver5);
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Active List of Outbound calls on call dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_contact_outbound_call_on_dashboard() {
	    System.out.println("Test case --verify_contact_outbound_call_on_dashboard-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

	    //Make an outbound call and pick it up from the caller
	    softPhoneCalling.softphoneAgentCall(adminDriver, contactNumber);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //get the agent and caller name to verify for the call entry
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    String callerName = callScreenPage.getCallerName(adminDriver);
	    
	    //verify the call direction and the data for the call entry on dashboard
	    callDashboardReactPage.verifyCallImageIsOutbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //Click on the caller name and verify its opened in sfdc
	    seleniumBase.idleWait(1);
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //click on account name and verify that it is opened in salesforce tab
	    callDashboardReactPage.clickRowAccountName(callDashboardDriver, 1);
	    salesforceAccountPage.verifyAccountsNameHeading(callDashboardDriver, CONFIG.getProperty("contact_account_name"));
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //hangup the active caller
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }

	//Agent on call displayed in Busy section with count
	//Logged out Agents displayed in Offline section with count
	//Logged in Agent displayed in Available section with count
	@Test(groups={"MediumPriority"})
	  public void verify_users_status_on_call_Dashboard() {
	    System.out.println("Test case --verify_users_status_on_call_Dashboard-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
	    
		//make a call to the agent so it get's busy
	    softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //logout one of the agent
	    softPhoneSettingsPage.logoutSoftphone(driver3);
		
	    //preparing the expected list on the available,busy and offline agents
		List<String> availableAgents = new ArrayList<>();
		availableAgents.add(CONFIG.getProperty("qa_agent_user_name"));
		List<String> BusyAgents = new ArrayList<>();
		BusyAgents.add(CONFIG.getProperty("qa_admin_user_name"));
		List<String> offlineAgents = new ArrayList<>();
		offlineAgents.add(CONFIG.getProperty("qa_user_3_name"));
	
	    //login to Call Dashboard Page and select a team
		seleniumBase.openNewBlankTab(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.get(CONFIG.getProperty("call_dashboard_react_url"));
	    callDashboardReactPage.selectTeam(driver1, "Call Dashboard Team");
	    
	    //verify all the available agents data like image, green dot, time
	    for (String availableAgent : callDashboardReactPage.getAvailableAgentsList(driver1)) {
	    	callDashboardReactPage.verifyAvailableUsersData(driver1, availableAgent);
		}
	    
	    //Get the available agent list from the dashboard and verify that it is same as expected
	    List<String> allAvailableAgentsList = callDashboardReactPage.getAvailableAgentsList(driver1);
	    Collections.sort(allAvailableAgentsList);
	    Collections.sort(availableAgents);
		assertEquals(allAvailableAgentsList, availableAgents);
		
		//Verify the Available agents heading
		callDashboardReactPage.verifyAvailableAgentHeading(driver1, allAvailableAgentsList.size());
		
	    //verify all the busy agents data like image, red dot, time
		 for (String busyAgent : callDashboardReactPage.getBusyAgentsList(driver1)) {
		    	callDashboardReactPage.verifyBusyUsersData(driver1, busyAgent, false);
		}
	    
		//Get the busy agent list from the dashboard and verify that it is same as expected
	    List<String> allBusyAgentsList = callDashboardReactPage.getBusyAgentsList(driver1);
	    Collections.sort(allBusyAgentsList);
	    Collections.sort(BusyAgents);
		assertEquals(allBusyAgentsList, BusyAgents);
		
		//Verify the busy agents heading
		callDashboardReactPage.verifyBusyAgentHeading(driver1, allBusyAgentsList.size());
		
	    //verify all the offline agents data like image, grey dot, time
		 for (String offlineAgent : callDashboardReactPage.getOfflineAgentsList(driver1)) {
		    	callDashboardReactPage.verifyOfflineUsersData(driver1, offlineAgent);
		}
	    
		 //Verify the offline agent in there in the offline agents list on call dashboard
	    List<String> allOfflineAgentsList = callDashboardReactPage.getOfflineAgentsList(driver1);
		assertTrue(allOfflineAgentsList.containsAll(offlineAgents));
		
		//Verify the Offline agents heading
		callDashboardReactPage.verifyOfflineAgentHeading(driver1, allOfflineAgentsList.size());
		
		//Select the All value from team filter
		callDashboardReactPage.selectTeam(driver1, "All");
		
		//add another busy agent in expected list for the all team agent is part of
		BusyAgents.add(CONFIG.getProperty("qa_user_2_name"));
	    
	    //verify all the available agents data like image, green dot, time
	    for (String availableAgent : callDashboardReactPage.getAvailableAgentsList(driver1)) {
	    	callDashboardReactPage.verifyAvailableUsersData(driver1, availableAgent);
		}
	    
		 //Verify the expected available agents list in there in the available agents list on call dashboard
	    allAvailableAgentsList = callDashboardReactPage.getAvailableAgentsList(driver1);
		assertTrue(allAvailableAgentsList.containsAll(availableAgents));
		
		//Verify the Available agents heading
		callDashboardReactPage.verifyAvailableAgentHeading(driver1, allAvailableAgentsList.size());
		
	    //verify all the busy agents data like image, red dot, time
		 for (String busyAgent : callDashboardReactPage.getBusyAgentsList(driver1)) {
		    	callDashboardReactPage.verifyBusyUsersData(driver1, busyAgent, false);
		}
	    
		//Verify the expected busy agents list in there in the busy agents list on call dashboard
	    allBusyAgentsList = callDashboardReactPage.getBusyAgentsList(driver1);
	    assertTrue(allBusyAgentsList.containsAll(BusyAgents));
		
	    //Verify the busy agents heading
		callDashboardReactPage.verifyBusyAgentHeading(driver1, allBusyAgentsList.size());
		
	    //verify all the offline agents data like image, grey dot, time
		 for (String offlineAgent : callDashboardReactPage.getOfflineAgentsList(driver1)) {
		    	callDashboardReactPage.verifyOfflineUsersData(driver1, offlineAgent);
		}
	    
		//Verify the expected offline agents list in there in the offline agents list on call dashboard
	    allOfflineAgentsList = callDashboardReactPage.getOfflineAgentsList(driver1);
		assertTrue(allOfflineAgentsList.containsAll(offlineAgents));
		
		//Verify the Offline agents heading
		callDashboardReactPage.verifyOfflineAgentHeading(driver1, allOfflineAgentsList.size());
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("agentDriver", false);
	    
	    resetApplication();
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Active call List on call dashboard with call forwarding ON
	@Test(groups={"MediumPriority"})
	  public void verify_call_with_forwarding_on_dashboard() {
	    System.out.println("Test case --verify_call_with_forwarding_on_dashboard-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    //set call forwarding number for the agent
	    String contactNumber = CONFIG.getProperty("qa_admin_user_number");
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver4, "forwarding number", CONFIG.getProperty("qa_user_2_number"));
	    
	    //make a call to the agent and pick the call
	    softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	    softPhoneCalling.pickupIncomingCall(driver4);

	    //get the agent name and caller name for the call to verify
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    String callerName = callScreenPage.getCallerName(adminDriver);
	    
	    //verify the call data on call dashboard for call with forwarding
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 2);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //click on the contact name for the call with forwarding entry on call dashboard and verify its opening in sfdc
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //hangup the active call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //make an outbound call with forwarding
	    softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.pickupIncomingCall(driver4);

	    callerName = callScreenPage.getCallerName(adminDriver);

	    //verify the data for outbound call on call dashboard
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 2);
	    callDashboardReactPage.verifyCallImageIsOutbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //click the contact name and verify its getting opened in sfdc
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //hangup the call 
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //disable call forwarding
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify Active List of call flow calls  on call dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_call_with_call_flow() {
	    System.out.println("Test case --verify_call_with_call_flow-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    String contactNumber = CONFIG.getProperty("qa_user_3_call_flow_number");
	    
	    //make a call to the call flow number and pick up the call
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    softPhoneCalling.pickupIncomingCall(driver3);

	    //get the agent and caller name to verfiy
	    String agentName = CONFIG.getProperty("qa_user_3_name");
	    String callerName = callScreenPage.getCallerName(driver3);
	    
	    //verify data on dashboard for call through call flow
	    seleniumBase.idleWait(5);
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //click on the caller name and verify it opens in sfdc
	    callDashboardReactPage.clickRowCallerName(callDashboardDriver, 1);
	    salesforceHomePage.verifyPageHeadingType(callDashboardDriver, "Contact", callerName);
	    seleniumBase.closeTab(callDashboardDriver);
	    seleniumBase.switchToTab(callDashboardDriver, seleniumBase.getTabCount(callDashboardDriver));
	    
	    //hangup the active call
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify filter by team on call dashboard
	//Verify searches section on call dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_call_filter_by_team() {
	    System.out.println("Test case --verify_call_filter_by_team-- started ");
	    
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
		
		//Add user as callDashboardDriver again into the team
		loginSupport(callDashboardDriver);
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team"), "");
  		groupsPage.addSupervisor(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
  		
  		groupsPage.openGroupSearchPage(callDashboardDriver);
  		groupsPage.openGroupDetailPage(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_team_2"), "");
  		groupsPage.addSupervisor(callDashboardDriver, CONFIG.getProperty("qa_call_dashboard_name"));
		
		//preparing expected data to verify
		String team1 = CONFIG.getProperty("qa_call_dashboard_team");
		String team2 = CONFIG.getProperty("qa_call_dashboard_team_2");
		List<String> usersTeam = new ArrayList<>();
		usersTeam.add(team1);
		usersTeam.add(team2);
		
		//agents which are part of team 1 and team 2
		String agnetName1 = CONFIG.getProperty("qa_admin_user_name");
		String agentName2 = CONFIG.getProperty("qa_user_2_name");
		
		openCallDashBoardPage();
	    
		//make a call to the team 1 agent and pick the call
	    softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("prod_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver5);
	    String callerName1 = callScreenPage.getCallerName(adminDriver);
	    
	    //make a call to the team 2 agent and pick the call
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callerName2 = callScreenPage.getCallerName(driver4);
	    
	    //Verify that the teams of which user is a member/supervisor  is appearing in teams filter on call dashboard page
	    List<String> actualTeams = callDashboardReactPage.getAllTeamsFromFilter(callDashboardDriver);
	    actualTeams.remove("All");
	    assertEquals(actualTeams, usersTeam);
	    
	    //Verify that by default for all options all teams calls are appearing on dashboard
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 2, agentName2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //verify that on selecting team only calls of team 1 is appearing
	    callDashboardReactPage.selectTeam(callDashboardDriver, team1);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //when selecting All for team all calls are appearing on call dashboard under active calls
	    callDashboardReactPage.selectTeam(callDashboardDriver, "All");
	    
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 2, agentName2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //verify text search for agent name is working properly
	    callDashboardReactPage.enterSearchText(callDashboardDriver, agnetName1);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
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

	//Verify filter by queue on call dashboard
	//Verify Queue Filters to include all queues a user is a member
	@Test(groups={"MediumPriority"})
	  public void verify_call_filter_by_queue() {
	    System.out.println("Test case --verify_call_filter_by_queue-- started ");
	    
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
		
		List<String> usersQueues = new ArrayList<>();
		usersQueues.add(queue1);
		usersQueues.add(queue2);
		
		openCallDashBoardPage();
	    
		//subscribe queue1 for user 1 and take a call on that queue
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queue1);
	    softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_dashboard_queue_number"));
	    String callerName1 = softPhoneCallQueues.getQeueueCallerName(adminDriver);;
	    
	    //subscribe queue2 for user 2 and take a call on that queue
	 	softPhoneCallQueues.subscribeQueue(driver4, queue2);
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_dashboard_queue_number_2"));
	    String callerName2 = softPhoneCallQueues.getQeueueCallerName(driver4);;
	    
	    //Verify that the queues of which user is a member/supervisor  is appearing in queues filter on call dashboard page
	    List<String> actualQueues = callDashboardReactPage.getAllQueuesFromFilter(callDashboardDriver);
	    actualQueues.remove("All");
	    assertEquals(actualQueues, usersQueues);
	    
	    //Verify that by default for all options all queues calls are appearing on dashboard
	    callDashboardDriver.navigate().refresh();
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), queue1);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), queue2);
	    
	    //verify that on selecting queue only calls of queue 2 is appearing
	    callDashboardReactPage.selectQueue(callDashboardDriver, queue2);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 1, callerName2, "QA", CONFIG.getProperty("contact_account_name"), queue2);
	    
	    //when selecting All for queue all calls are appearing on call dashboard under active calls
	    callDashboardReactPage.selectQueue(callDashboardDriver, "All");
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), queue1);
	    callDashboardReactPage.verifyRowQueueCallsDetails(callDashboardDriver, 2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), queue2);
	    
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
	
	//Agent supervision should not able to see more link
	//Verify Call dashboard when agent not a supervisor
	//Verify user have no permission on Dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_agent_supervisor() {
	    System.out.println("Test case --verify_agent_supervisor-- started ");
	    
		
		//updating the driver used 
	    initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		 
		//Login agent into the softphone
		WebDriver agentDriver = getDriver();
		SFLP.softphoneLogin(agentDriver,CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
		  
		//Open gourp call details and add agent as supervisor
		loginSupport(driver1);
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1,
		CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addSupervisor(driver1, CONFIG.getProperty("qa_user_4_name"));
		  
		//Open web app for the agent and verify that only three items are appearing in the left nav and ringDNA live is one of the options
		loginSupport(agentDriver);
		assertEquals(dashboard.getSideMeduItems(agentDriver).size(), 3);
		assertTrue(dashboard.getSideMeduItems(agentDriver).contains("ringDNA Live"));
		assertTrue(dashboard.getSideMeduItems(agentDriver).contains("Call Recordings"));
		assertTrue(dashboard.getSideMeduItems(agentDriver).contains("Conversation AI"));
		  
		//deleting supervisor from Team 
		groupsPage.deleteSuperviosr(driver1, CONFIG.getProperty("qa_user_4_name"));
		 
		//Close the agent web driver
		reloadSoftphone(agentDriver);
  		seleniumBase.openNewBlankTab(agentDriver);
  	
  	    agentDriver.quit();
  	    agentDriver = null;
  	    
  	    //Login again as agent
  	    agentDriver = getDriver();
		SFLP.supportLogin(agentDriver, CONFIG.getProperty("call_dashboard_react_url"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
	    
		//since agent is not the supervisor then no permission image and texts should appear
		callDashboardReactPage.verifyNoPermissionPage(agentDriver);
		
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify salesforce image of user into Team section 
	//Verify Online users is showing on Offline column
	@Test(groups={"MediumPriority"})
	  public void verify_web_app_user_status() {
	    System.out.println("Test case --verify_web_app_user_status-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    //quit the browser if agent is already logged in
	    driverUsed.put("driver4", false);
		if(driver4 != null) {
			softPhoneSettingsPage.logoutSoftphone(driver4);
			driver4.quit();
			driver4 = null;
		}
		openCallDashBoardPage();
		
		String agentName = CONFIG.getProperty("qa_user_2_name");
	  		
		//Login agent directly in the web app
		driver4 = getDriver();
		SFLP.supportLogin(driver4, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));  
	
		//verify the agent is appearing in offline section and data like image, grey dot, time
		seleniumBase.idleWait(5);
		callDashboardReactPage.verifyOfflineUsersData(callDashboardDriver, agentName);
		
		//Open blank page and login to softphone
		seleniumBase.openNewBlankTab(driver4);
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		driver4.get(CONFIG.getProperty("qa_test_site_name"));
		
		//verify the agent is appearing in available section and data like image, green dot, time
		seleniumBase.idleWait(5);
	    callDashboardReactPage.verifyAvailableUsersData(callDashboardDriver, agentName);
	    
	    driver4.quit();
	    driver4 = null;
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }

	//Verify filter by team on call dashboard
	@Test(groups={"MediumPriority"})
	  public void verify_call_filter_by_call_direction() {
	    System.out.println("Test case --verify_call_filter_by_call_direction-- started ");
	    
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
		
		openCallDashBoardPage();
	    
		String agnetName1 = CONFIG.getProperty("qa_admin_user_name");
		String agentName2 = CONFIG.getProperty("qa_user_2_name");
		
		//make an outbound call to an agent and pick the call
	    softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("prod_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver5);
	    String callerName1 = callScreenPage.getCallerName(adminDriver);
	    
	    //make an inbound call to the agent and pick the call
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver4);
	    String callerName2 = callScreenPage.getCallerName(driver4);
	    
	    //Verify that by default for all options all calls are appearing on dashboard
	    System.out.println("verifying that by default all calls are appearing on ringDNA live");
	    seleniumBase.idleWait(3);
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 2);
	    callDashboardReactPage.verifyCallImageIsOutbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 2);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 2, agentName2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), true);
	    System.out.println("verication completed for all calls that are appearing on dashboard");
	    
	    //verify that on selecting inbound call direction only inbound call is appearing
	    System.out.println("verifying that on selecting inbound filter only inbound calls are appearing on ringDNA Live");
	    callDashboardReactPage.selectCallDirection(callDashboardDriver, "Inbound");
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName2, callerName2, "QA", CONFIG.getProperty("contact_account_name"), true);
	    System.out.println("Verification completed");
	    
	    //verify that on selecting outbound call direction only outbound call is appearing
	    System.out.println("verifying that on selecting outbound filter only outbound calls are appearing on ringDNA Live");
	    callDashboardReactPage.selectCallDirection(callDashboardDriver, "Outbound");
	    assertEquals(callDashboardReactPage.getTotalRowCallEntries(callDashboardDriver), 1);
	    callDashboardReactPage.verifyCallImageIsOutbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agnetName1, callerName1, "QA", CONFIG.getProperty("contact_account_name"), true);
	    System.out.println("Verification completed");
	    
	    callDashboardReactPage.selectCallDirection(callDashboardDriver, "All");

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
	
	//Verify Active List when team member on Hold
	//Verify call dashboard should not show an active call when Agent does not pick income calls
	@Test(groups={"MediumPriority"})
	  public void verify_call_put_on_hold() {
	    System.out.println("Test case --verify_call_put_on_hold-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    String contactNumber = CONFIG.getProperty("qa_admin_user_number");
	    
	    //again make a call to the unknown caller
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    
	    //verify that call is appearing on softphone
	    softPhoneCalling.isDeclineButtonVisible(adminDriver);
	    
	    //verify that no call is appearing on ringDNA Live
	    reloadSoftphone(callDashboardDriver);
	    callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
	    
	    //pick the call now
	    softPhoneCalling.pickupIncomingCall(adminDriver);

	    //get the value of the agent and the caller for the unknown call entry
	    String agentName = CONFIG.getProperty("qa_admin_user_name");
	    String callerName = callScreenPage.getCallerName(adminDriver);

	    //verify that on picking the call it is appearing on the ringDNA Live application
	    reloadSoftphone(callDashboardDriver);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //put call on hold
	    softPhoneCalling.clickHoldButton(adminDriver);
	    seleniumBase.idleWait(2);
	    
	    //verify that no call is appearing on ringDNA Live
	    reloadSoftphone(callDashboardDriver);
	    callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
	    
	    //hangup the active calls
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify call dashboard should not show an active call for offline user
	//Verify call dashboard should not show an active call for offline user with Call forwarding
	@Test(groups={"MediumPriority"})
	  public void verify_call_to_offline_user() {
	    System.out.println("Test case --verify_call_to_offline_user-- started ");
	    
	    driverUsed.put("driver1", false);
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();
	    
	    //set call forwarding number for the agent
	    String contactNumber = CONFIG.getProperty("qa_admin_user_number");
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver4, "forwarding number", CONFIG.getProperty("qa_user_2_number"));
	    
	    //logout one of the agent
	    softPhoneSettingsPage.logoutSoftphone(adminDriver);
	    
	    //make a call to the offline agent
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    softPhoneCalling.isDeclineButtonVisible(driver4);
	    
	    //verify that no call is appearing on ringDNA Live
	    callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
	    
	    //pick the call now
	    softPhoneCalling.pickupIncomingCall(driver4);

	    //get the agent name and caller name for the call to verify
	    String agentName = CONFIG.getProperty("qa_user_2_name");
	    String callerName = callScreenPage.getCallerName(driver4);
	    
	    //verify the call data on call dashboard for call with forwarding
	    callDashboardReactPage.verifyActiveCallHeading(callDashboardDriver, 1);
	    callDashboardReactPage.verifyCallImageIsInbound(callDashboardDriver, 1);
	    callDashboardReactPage.verifyRowCallsDetails(callDashboardDriver, 1, agentName, callerName, "QA", CONFIG.getProperty("contact_account_name"), true);
	    
	    //hangup the active call
	    softPhoneCalling.hangupActiveCall(driver2);
	    
	    //again login the agent
		SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));

		//disable call forwarding
	    softPhoneSettingsPage.clickSettingIcon(adminDriver);
	    softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
	    
	    //logout one of the agent
	    softPhoneSettingsPage.logoutSoftphone(adminDriver);
	    
	    //make a call to the offline agent
	    softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //verify that no call is appearing on ringDNA Live
	    callDashboardReactPage.allCallDashboardElements(callDashboardDriver);
	    
	    //hang up the call
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
	    //login agent again
	    SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }

	//Verify display Wrap Up as a custom status when user End Simulring queue call
    @Test(groups={"MediumPriority"})
	  public void verify_wrap_up_simurling() {
	    System.out.println("Test case --verify_wrap_up_simurling-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();

	 	String queuNumber	= CONFIG.getProperty("qa_dashboard_queue_number");
	 	String queueName	= CONFIG.getProperty("qa_dashboard_queue");
	 	String accountName	= CONFIG.getProperty("qa_user_account").trim();
	 	String agentName	= CONFIG.getProperty("qa_admin_user_name");
	 	
	 	//Opening the details of an simurling queue and setting wrap up time to 5 minutes
	 	loginSupport(driver3);
	 	System.out.println("Account editor is opened ");
	 	dashboard.isPaceBarInvisible(driver3);
	 	callQueuesPage.openCallQueueSearchPage(driver3);
	 	callQueuesPage.openCallQueueDetailPage(driver3, queueName, accountName);
	 	callQueuesPage.setWrapUpTime(driver3, CallQueuesPage.WrapUpTime.FiveMin);
	 	callQueuesPage.saveGroup(driver3);
	 	seleniumBase.switchToTab(driver3, 1);
	 	reloadSoftphone(adminDriver);
	 	
	 	//subscribe queue for the agent	
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queueName);
	    
	 	//make the call to the queue and do not pick it up
	    softPhoneCalling.softphoneAgentCall(driver2, queuNumber);
	    softPhoneCallQueues.isPickCallBtnVisible(adminDriver);

	    //pick call from the queue section
	    softPhoneCallQueues.pickCallFromQueue(adminDriver);
	    seleniumBase.idleWait(3);
	    softPhoneCalling.hangupActiveCall(adminDriver);
	    
	    //Verify that the agent is busy and wrapping up status is appearing
	    callDashboardReactPage.verifyBusyUsersData(callDashboardDriver, agentName, true);
	    
		// disable wrap up time
		seleniumBase.switchToTab(driver3, 2);
		callQueuesPage.disableWrapUpTime(driver3);
		callQueuesPage.saveGroup(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		reloadSoftphone(adminDriver);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
    
    //Verify display Wrap Up as a custom status when user end Longest Waiting Agent queue call
    @Test(groups={"MediumPriority"})
	  public void verify_wrap_up_acd_queue() {
	    System.out.println("Test case --verify_wrap_up_acd_queue-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();

	 	String queuNumber	= CONFIG.getProperty("qa_acd_group_1_number");
	 	String queueName	= CONFIG.getProperty("qa_acd_group_1_name");
	 	String accountName	= CONFIG.getProperty("qa_user_account").trim();
	 	String agentName	= CONFIG.getProperty("qa_user_3_name");
	 	
	 	//Opening detail of an ACD queue and setting the wrap up time as 5 minutes
	 	loginSupport(driver1);
	 	System.out.println("Account editor is opened ");
	 	dashboard.isPaceBarInvisible(driver1);
	 	callQueuesPage.openCallQueueSearchPage(driver1);
	 	callQueuesPage.openCallQueueDetailPage(driver1, queueName, accountName);
	 	callQueuesPage.setWrapUpTime(driver1, CallQueuesPage.WrapUpTime.FiveMin);
	 	callQueuesPage.saveGroup(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);	
	 	
	 	//subscribe queue for the agent	
	 	softPhoneCallQueues.unSubscribeQueue(driver1, queueName);
	 	softPhoneCallQueues.unSubscribeQueue(driver4, queueName);
	 	softPhoneCallQueues.subscribeQueue(driver3, queueName);
	    
	 	//make the call to the queue
	    softPhoneCalling.softphoneAgentCall(driver2, queuNumber);

	    //pick call from the queue section
	    softPhoneCalling.pickupIncomingCall(driver3);
	    seleniumBase.idleWait(3);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //Verify that the agent is busy and wrapping up status is appearing
	    callDashboardReactPage.verifyBusyUsersData(callDashboardDriver, agentName, true);
	    
		// disable wrap up time
		seleniumBase.switchToTab(driver1, 2);
		callQueuesPage.disableWrapUpTime(driver1);
		callQueuesPage.saveGroup(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
    
    //Verify display Wrap Up as a custom status when user end Sequential Dial queue call
    @Test(groups={"MediumPriority"})
	  public void verify_wrap_up_sequential_queue() {
	    System.out.println("Test case --verify_wrap_up_acd_queue-- started ");
	    
	    //updating the driver used 
	    initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage();

	 	String queuNumber	= CONFIG.getProperty("qa_dashboard_queue_3_number");
	 	String queueName	= CONFIG.getProperty("qa_dashboard_queue_3");
	 	String accountName	= CONFIG.getProperty("qa_user_account").trim();
	 	String agentName	= CONFIG.getProperty("qa_admin_user_name");
	 	
	 	//Opening details of Sequential queue and setting the wrap up time to 5 minutes
	 	loginSupport(adminDriver);
	 	System.out.println("Account editor is opened ");
	 	dashboard.isPaceBarInvisible(adminDriver);
	 	callQueuesPage.openCallQueueSearchPage(adminDriver);
	 	callQueuesPage.openCallQueueDetailPage(adminDriver, queueName, accountName);
	 	callQueuesPage.setWrapUpTime(adminDriver, CallQueuesPage.WrapUpTime.FiveMin);
	 	callQueuesPage.saveGroup(adminDriver);
	 	seleniumBase.switchToTab(adminDriver, 1);
	 	reloadSoftphone(adminDriver);
	 	
	 	//subscribe queue for the agent	
	 	softPhoneCallQueues.subscribeQueue(adminDriver, queueName);
	    
	 	//make the call to the queue
	    softPhoneCalling.softphoneAgentCall(driver2, queuNumber);

	    //pick call from the queue section
	    softPhoneCalling.pickupIncomingCall(adminDriver);
	    seleniumBase.idleWait(3);
	    softPhoneCalling.hangupActiveCall(adminDriver);
	    
	    //Verify that the agent is busy and wrapping up status is appearing
	    callDashboardReactPage.verifyBusyUsersData(callDashboardDriver, agentName, true);
	    
		// disable wrap up time
		seleniumBase.switchToTab(adminDriver, 2);
		callQueuesPage.disableWrapUpTime(adminDriver);
		callQueuesPage.saveGroup(adminDriver);
		seleniumBase.closeTab(adminDriver);
		seleniumBase.switchToTab(adminDriver, 1);
		reloadSoftphone(adminDriver);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("adminDriver", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("callDashboardDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
    
	public void openCallDashBoardPage() {
		
	    //updating the driver used
		initializeDriverSoftphone("callDashboardDriver");
	    driverUsed.put("callDashboardDriver", true);
	    
	    openCallDashBoardPage(callDashboardDriver);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("callDashboardDriver", false);
	}
	
	public void openCallDashBoardPage(WebDriver driver) {

		if(!driver.getTitle().equals("ringDNA Live")) {
	    	
			int tabCounts = seleniumBase.getTabCount(driver);
	    	seleniumBase.switchToTab(driver, tabCounts);
	    	if(driver.getTitle().equals("ringDNA Live"))
	    		return;
	    	
	    	if(tabCounts > 1){
	    		for (int i = 0; i < tabCounts - 1; i++) {
	    			seleniumBase.switchToTab(driver, tabCounts - i);
	    			seleniumBase.closeTab(driver);
				}
	    	}
	    		
			//Open call dashboard page from support app
	    	seleniumBase.switchToTab(driver, 1);
			loginSupport(driver);
			dashboard.openCallDashboardPage(driver);
	    }
	}
	
	public void addCallerAsMultiple() {
		
		String callerPhone = CONFIG.getProperty("prod_user_2_number");
	    
	    String existingContact = CONFIG.getProperty("prod_user_3_name").trim() + " Automation";
	    
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_2_name"));
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    // Add caller as multiple contact
	    callScreenPage.closeErrorBar(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    
	   //verify caller is multiple
	    softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("prod_user_2_number"));
	    
	   // Calling from Agent's SoftPhone
	   	softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver5);
	 	softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(3);
	 	
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.pickupIncomingCall(driver5);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);											  
	}
	
	  @AfterMethod(groups = { "Regression",  "MediumPriority", "Product Sanity"})
	  	public void afterMethod(ITestResult result) {
		if(result.getName().equals("verify_call_with_forwarding_on_dashboard") && (result.getStatus() == 2 || result.getStatus() == 3)) {
			initializeDriverSoftphone("adminDriver");
		    driverUsed.put("adminDriver", true);
		    
		    //disable call forwarding
		    softPhoneCalling.hangupIfInActiveCall(adminDriver);
		    softPhoneSettingsPage.clickSettingIcon(adminDriver);
		    softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

			// Setting driver used to false as this test case is pass
			driverUsed.put("adminDriver", false);	
		}else if(result.getName().equals("verify_web_app_user_status") && (result.getStatus() == 2 || result.getStatus() == 3)) {
			if(driver4 != null) {
				driver4.quit();
				driver4 = null;
			}
		}
	  }
}