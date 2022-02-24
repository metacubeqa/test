package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;

public class CallRecordingsVisibility extends SoftphoneBase{
	
	@BeforeClass(groups = {"Regression", "Sanity", "QuickSanity", "MediumPriority"})
	public void beforeClass() {

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		  
		//adding agent again as supervisor
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectRecordingVisibility(driver4, AccountCallRecordingTab.RecordingVisibilityTypeOptions.AgentAndTeamListenIn);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
	}
	
	//Admin role user should able to access and playback recording of all users of same account.
	//Admin role user should not able to access and playback recording of other acocunt.
	//Support role user should able to access and playback recording of all users of same acocunt.
	//Support role user should able to access and playback recording of all users of other acocunts.
	@Test(groups={"MediumPriority"})
	public void verify_recording_url_access_other_admin_user() 
	{		
		System.out.println("---------------- Test Case verify_recording_url_access_other_user Started ------------------");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	  
	    WebDriver qaAutoAdminDriver = getDriver();
		SFLP.softphoneLogin(qaAutoAdminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qaAuto_admin_user_username"), CONFIG.getProperty("qaAuto_admin_user_password"));
	    
  		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver3);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver3);
  		contactDetailPage.openRecentCallEntry(driver3, callSubject);
  	    String recordingURl = sfTaskDetailPage.getCallRecordingUrl(driver3);
  	    seleniumBase.closeTab(driver3);
  	    seleniumBase.switchToTab(driver3, 1);
		
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(driver4);		
		seleniumBase.switchToTab(driver4,seleniumBase.getTabCount(driver4));
		driver4.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(driver4);
		callRecordingPage.clickPlayButton(driver4, 0);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
  	    
  	    //verify that user is not able to access the url
  		seleniumBase.openNewBlankTab(qaAutoAdminDriver);		
		seleniumBase.switchToTab(qaAutoAdminDriver,seleniumBase.getTabCount(qaAutoAdminDriver));
		qaAutoAdminDriver.get(recordingURl);
  		callRecordingPage.verifyAccessDeniedMessageOtherOrg(qaAutoAdminDriver);
	    seleniumBase.closeTab(qaAutoAdminDriver);
	    seleniumBase.switchToTab(qaAutoAdminDriver, 1);
	    
	    //verify that user is not able to access the url
	    WebDriver qaAutoSupportDriver = getDriver();
		SFLP.softphoneLogin(qaAutoSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qaAuto_support_user_username"), CONFIG.getProperty("qaAuto_support_user_password"));
	    
  		seleniumBase.openNewBlankTab(qaAutoSupportDriver);		
		seleniumBase.switchToTab(qaAutoSupportDriver ,seleniumBase.getTabCount(qaAutoSupportDriver));
		qaAutoSupportDriver.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(qaAutoSupportDriver);
		callRecordingPage.clickPlayButton(qaAutoSupportDriver, 0);
	    seleniumBase.closeTab(qaAutoSupportDriver);
	    seleniumBase.switchToTab(qaAutoSupportDriver, 1);
	    
	    qaAutoAdminDriver.quit();
	    qaAutoAdminDriver = null; 
	    qaAutoSupportDriver.quit();
	    qaAutoSupportDriver = null;
	    
	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(driver1);		
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(driver1);
		callRecordingPage.clickPlayButton(driver1, 0);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("standardUserDriver", false);
		driverUsed.put("driver4", false);

		System.out.println("Test Case is Pass");
	}
	
	//Supervisor (agent role) of team should be access and playback recording of team's members
	@Test(groups = { "MediumPriority"})
	public void verify_recording_visibility_agent_supervisor() 
	{		
		System.out.println("Test case --verify_recording_visibility_agent_supervisor-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.addSupervisor(driver4, CONFIG.getProperty("qa_user_4_name"));
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
	
  		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver4);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
  		contactDetailPage.openRecentCallEntry(driver4, callSubject);
  	    String recordingURl = sfTaskDetailPage.getCallRecordingUrl(driver4);
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
	    
	    //verify that user is not able to access the url
	    WebDriver qaAgentDriver = getDriver();
		SFLP.softphoneLogin(qaAgentDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
	    
  		seleniumBase.openNewBlankTab(qaAgentDriver);		
		seleniumBase.switchToTab(qaAgentDriver ,seleniumBase.getTabCount(qaAgentDriver));
		qaAgentDriver.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(qaAgentDriver);
		callRecordingPage.clickPlayButton(qaAgentDriver, 0);
	    seleniumBase.closeTab(qaAgentDriver);
	    seleniumBase.switchToTab(qaAgentDriver, 1);
	    
	    qaAgentDriver.quit();
	    qaAgentDriver = null; 
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		
		//Deleting member from the group
  		dashboard.isPaceBarInvisible(driver4);
  		groupsPage.openGroupSearchPage(driver4);
  		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
  		
  		//deleting newly added member from group
  		groupsPage.deleteSuperviosr(driver4, CONFIG.getProperty("qa_user_4_name"));
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}

	//If A team member belongs to more than teams, he should be able to access recording of team members in which he has Listen cabability
	@Test(groups = { "MediumPriority"})
	public void verify_agent_listen_in_multiple_teams() 
	{		
		System.out.println("Test case --verify_agent_listen_in_multiple_teams-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String agentName = CONFIG.getProperty("qa_agent_user_name");
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.enableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	
  		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver4);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
  		contactDetailPage.openRecentCallEntry(driver4, callSubject);
  	    String recordingURl1 = sfTaskDetailPage.getCallRecordingUrl(driver4);
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
  	    
  	    // Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver1);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
  		contactDetailPage.openRecentCallEntry(driver1, callSubject);
  	    String recordingURl2 = sfTaskDetailPage.getCallRecordingUrl(driver1);
  	    seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that user is not able to access the url
  	    seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver, seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl1);
		callRecordingPage.verifyAccessDeniedMessageOtherOrg(agentDriver);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
	    
	    
	    seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver ,seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl2);
		callRecordingPage.getNumberOfCallRecordings(agentDriver);
		callRecordingPage.clickPlayButton(agentDriver, 0);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
	    
	    //adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	    
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case is pass");
	}
	
	//An agent should able to access and playback own recording when he is team member without "listenIn" capability
	//Admin role user  should be access all recording of same account when he is team member without listen In capability
	@Test(groups = { "MediumPriority"})
	public void verify_agent_listen_own_recordings() 
	{		
		System.out.println("Test case --verify_agent_listen_own_recordings-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String agentName = CONFIG.getProperty("qa_agent_user_name");
	    String adminName = CONFIG.getProperty("qa_user_2_name");
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(driver4, adminName);
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.disableListenInMember(driver4, adminName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.disableListenInMember(driver4, adminName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	
  		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("qa_user_2_number"));
  		softPhoneCalling.pickupIncomingCall(driver4);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver4);
  		softPhoneCalling.isCallBackButtonVisible(agentDriver);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(agentDriver);
  		contactDetailPage.openRecentCallEntry(agentDriver, callSubject);
  		String recordingURl = sfTaskDetailPage.getCallRecordingUrl(agentDriver);
  	    sfTaskDetailPage.clickRecordingURL(agentDriver);
  	    callRecordingPage.getNumberOfCallRecordings(agentDriver);
		callRecordingPage.clickPlayButton(agentDriver, 0);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
  	    seleniumBase.closeTab(agentDriver);
  	    seleniumBase.switchToTab(agentDriver, 1);
  	    
  	    //verifying that user is able to access the automated vm recording url
		seleniumBase.openNewBlankTab(driver4);		
		seleniumBase.switchToTab(driver4,seleniumBase.getTabCount(driver4));
		driver4.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(driver4);
		callRecordingPage.clickPlayButton(driver4, 0);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
  	    
  	    //adding agent again as supervisor
	    loginSupport(driver4);
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.deleteMember(driver4, adminName);
		groupsPage.saveGroup(driver4);
		
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case is pass");
	}
	
	//Team member who listen-in capability should able to access and playback recording of his team supervisor.
	@Test(groups = { "MediumPriority"})
	public void verify_agent_listen_supervisor_recording() 
	{		
		System.out.println("Test case --verify_agent_listen_supervisor_recording-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String agentName = CONFIG.getProperty("qa_agent_user_name");
	    String supervisorName = CONFIG.getProperty("qa_user_2_name");
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addSupervisor(driver4, supervisorName);
		groupsPage.enableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	
		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver4);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
  		contactDetailPage.openRecentCallEntry(driver4, callSubject);
  	    String recordingURl = sfTaskDetailPage.getCallRecordingUrl(driver4);
  	    seleniumBase.closeTab(driver4);
  	    seleniumBase.switchToTab(driver4, 1);
  	    
  	    seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver ,seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl);
		callRecordingPage.getNumberOfCallRecordings(agentDriver);
		callRecordingPage.clickPlayButton(agentDriver, 0);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
  	    
  	    //adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.deleteSuperviosr(driver4, supervisorName);
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case is pass");
	}
	
	//Verify 'Call Recording Visibility Policy' on Account's call recording tab
	//Member (agent role) is deleted from the team (listen-in capability was checked) then he/she should not able to access and playback recording of his/she team members.
	@Test(groups = { "MediumPriority"})
	public void verify_agent_listen_after_deletion() 
	{		
		System.out.println("Test case --verify_agent_listen_after_deletion-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String agentName = CONFIG.getProperty("qa_agent_user_name");
	    
		//adding agent again as supervisor
	    loginSupport(driver4);
	    
	    //Verify that selected option is appearing as Agents and team memebers
	    accountCallRecordingTab.openCallRecordingTab(driver4);
		assertEquals(accountCallRecordingTab.getSelectedRecordVisibilityOption(driver4), "Agent and team members with listen-in capabilities");
		accountCallRecordingTab.verifyBothRecordVisibilityOption(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.enableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	
		// Calling and creating a voicemail
  		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver2);
  		seleniumBase.idleWait(5);
  		softPhoneCalling.hangupIfInActiveCall(driver2);
  		softPhoneCalling.isCallBackButtonVisible(driver1);
  		
  		//Verifying Recent Calls Details and getting automated VM url
  	    String  callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
  		contactDetailPage.openRecentCallEntry(driver1, callSubject);
  	    String recordingURl = sfTaskDetailPage.getCallRecordingUrl(driver1);
  	    seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);
  	    
  	    //adding agent again as supervisor
	    loginSupport(driver4);
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.deleteMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
  	    
  	    seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver ,seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl);
		callRecordingPage.verifyAccessDeniedMessageOtherOrg(agentDriver);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
  	    
  	    //adding agent again as supervisor
	    loginSupport(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
		groupsPage.addMember(driver4, agentName);
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		groupsPage.disableListenInMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case is pass");
	}
	
	//Verify access queue voicemail player which is dropped by call flow when call recording visibility set to Team members
	@Test(groups={"Regression"})
	public void verify_access_call_flow_dial_queue_vm(){
		System.out.println("Test case --verify_access_call_flow_dial_queue_vm-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		//subscribe queue
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, queueName);
		softPhoneCallQueues.isQueueSubscribed(driver4, queueName);
		
		//Making an call to call flow which has queue
		System.out.println("caller making call to call flow");
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_call_flow_call_queue_drop_vm_smart_number"));
		
		//verify Queue Count
		softPhoneCallQueues.verifyQueueCount(driver4, "1");
		
		//pickup call from the queue
		softPhoneCallQueues.openCallQueuesSection(driver4);
		seleniumBase.idleWait(25);
		
		//hanging up with the called device
		System.out.println("hanging up with the device");
		softPhoneCalling.hangupActiveCall(driver3);
		seleniumBase.idleWait(5);
		
		//verify sfdc data
		//verify data for vm dropped call
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver4);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Missed");
	    sfTaskDetailPage.verifyVoicemailCreatedActivity(driver4);
	    sfTaskDetailPage.clickRecordingURL(driver4);
		assertEquals(1, callRecordingPage.getNumberOfCallRecordings(driver4), "number of call recordings are not same");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
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
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" })
	public void afterMethod(ITestResult result) {
	    if((result.getStatus() == 2 || result.getStatus() == 3)) {
	    	
	    	//initialise drivers
		    initializeDriverSoftphone("driver4");
		    driverUsed.put("driver4", true);
		    
		    loginSupport(driver4);
		    
		    String agentName = CONFIG.getProperty("qa_agent_user_name");
		    String supervisorName = CONFIG.getProperty("qa_user_2_name");
		    
	    	if(result.getName().equals("verify_recording_visibility_agent_supervisor") ) {
				
				//Deleting member from the group
		  		dashboard.isPaceBarInvisible(driver4);
		  		groupsPage.openGroupSearchPage(driver4);
		  		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
		  		groupsPage.deleteSuperviosr(driver4, CONFIG.getProperty("qa_user_4_name"));
				groupsPage.saveGroup(driver4);
		  		
			}else if(result.getName().equals("verify_agent_listen_own_recordings")) {
				
			    groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
				groupsPage.deleteMember(driver4, supervisorName);
				groupsPage.saveGroup(driver4);
			    
			}else if(result.getName().equals("verify_agent_listen_supervisor_recording")) {
			    
				 //adding agent again as supervisor
			    loginSupport(driver4);
				
				groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
				groupsPage.deleteSuperviosr(driver4, supervisorName);
				groupsPage.saveGroup(driver4);
			    
			}else if(result.getName().equals("verify_agent_listen_after_deletion")) {
	
			  //adding agent again as supervisor
			    loginSupport(driver4);
				
				groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
				groupsPage.addMember(driver4, agentName);
				groupsPage.saveGroup(driver4);    
			}
	    	
	    	groupsPage.openGroupSearchPage(driver4);
			groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
			groupsPage.disableListenInMember(driver4, agentName);
			groupsPage.saveGroup(driver4);
			
			groupsPage.openGroupSearchPage(driver4);
			groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
			groupsPage.disableListenInMember(driver4, agentName);
			groupsPage.saveGroup(driver4);
		    seleniumBase.closeTab(driver4);
		    seleniumBase.switchToTab(driver4, 1);
			reloadSoftphone(agentDriver);
	    }
	}
	
	@AfterClass(groups = {"Regression", "Sanity", "QuickSanity", "MediumPriority"})
	public void setRecordingVisibilityToAll() {

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//adding agent again as supervisor
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		assertEquals(accountCallRecordingTab.getSelectedRecordVisibilityOption(driver4), "Agent and team members with listen-in capabilities");
		accountCallRecordingTab.selectRecordingVisibility(driver4, AccountCallRecordingTab.RecordingVisibilityTypeOptions.All);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
	}
}