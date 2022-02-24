package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountsEmergencyTab;
import support.source.callFlows.CallFlowPage;
import support.source.callFlows.CallFlowPage.CallFlowStatus;
import support.source.callQueues.CallQueuesPage;
import support.source.commonpages.Dashboard;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class EmergencyTabCases extends SupportBase{
	
	AccountsEmergencyTab emergencyPage = new AccountsEmergencyTab();
	Dashboard dashboard = new Dashboard();
	CallFlowPage callFlowPage = new CallFlowPage();
	CallQueuesPage callQueuesPage = new CallQueuesPage();
	AccountOverviewTab overviewTab  = new AccountOverviewTab();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	UsersPage usersPage = new UsersPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	
	private String accountName;
	private String accountID;
	private String callFlow;
	private String qa_support_number;
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String emergencyFileNameDownload = "emergency-routes";

	private static final String callFlowAssociatedDeleteMsg = "This call flow cannot be deleted as it's associated with Emergency Routing."
			+ " Please remove this association before deleting this call flow.";
	private static final String multipleEmergencyRoutingMsg	= "We already have enabled Emergency Route for one or more selected locations";
	
	@BeforeClass(groups = { "Regression", "MediumPriority" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account");
		accountID = CONFIG.getProperty("qa_user_account_id");
		callFlow = CONFIG.getProperty("qa_call_flow_emergency_route");
		qa_support_number = CONFIG.getProperty("qa_support_user_number");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void emergency_routing_functionality() {
		
		System.out.println("Test case --emergency_routing_functionality-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		callFlowPage.clickAccountLink(supportDriver);
		
		//verifying overview tab details
		overviewTab.openOverViewTab(supportDriver);
		overviewTab.verifyOverViewTabDetails(supportDriver, accountName, accountID);
		
		// Opening Emergency Tab
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		// clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);

		//getting current date
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		
		//finding routing saved
		assertTrue(emergencyPage.isRoutingCreated(supportDriver, null, callFlow, createdDate));

		// downloading and verifying csv
		HelperFunctions.deletingExistingFiles(downloadPath, emergencyFileNameDownload);
		emergencyPage.downloadTotalRecords(supportDriver);
		emergencyPage.waitForFileToDownload(supportDriver, downloadPath, emergencyFileNameDownload, ".csv");
		boolean fileDownloaded = emergencyPage.isFileDownloaded(downloadPath, emergencyFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file not downloaded");

		// creating location if not created
		String location = "Jaipur";
		overviewTab.openOverViewTab(supportDriver);
		if (!overviewTab.checkLocationSaved(supportDriver, location)) {
			overviewTab.createLocation(supportDriver, location, "", "");
		}
		
		//creating emergency routing
		emergencyPage.navigateToEmergencyTab(supportDriver);
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");

		//searching with location
		emergencyPage.searchLocation(supportDriver, location);
		
		//finding routing saved
		assertTrue(emergencyPage.isRoutingCreated(supportDriver, location, callFlow, createdDate));

		//adding new location if not present
		String newLocation = "AutomationLocation";
		overviewTab.openOverViewTab(supportDriver);
		if (!overviewTab.checkLocationSaved(supportDriver, newLocation)) {
			overviewTab.createLocation(supportDriver, newLocation, "", "");
		}
		
		//updating routing
		String newCallFlow = "EmergencyRouteCallFlowCopy";
		emergencyPage.navigateToEmergencyTab(supportDriver);
		emergencyPage.updateRouting(supportDriver, location, callFlow, createdDate);
		emergencyPage.selectLocation(supportDriver, newLocation);
		emergencyPage.updateCallFlow(supportDriver, newCallFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);

		String newCreatedDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		
		// searching with new location
		emergencyPage.searchLocation(supportDriver, newLocation);
		
		//finding routing saved
		assertTrue(emergencyPage.isRoutingCreated(supportDriver, newLocation, newCallFlow, newCreatedDate));
		
		//deleting routing
		emergencyPage.deleteRouting(supportDriver, newLocation, newCallFlow, newCreatedDate);
		
		// clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --emergency_routing_functionality-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void associate_call_flow_and_delete() {
		
		System.out.println("Test case --associate_call_flow_and_delete-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//creating location if not created
		String location = "AutomationLocation2";

		dashboard.clickOnUserProfile(supportDriver);
		callFlowPage.clickAccountLink(supportDriver);

		overviewTab.openOverViewTab(supportDriver);
		if(!overviewTab.checkLocationSaved(supportDriver, location)){
			overviewTab.createLocation(supportDriver, location, "", "");
		}
		
		// Opening Emergency Tab
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		//Deleting the call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, accountName);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		callFlowPage.selectStatus(supportDriver, CallFlowStatus.Paused.name());
		callFlowPage.clickSaveBtn(supportDriver);
		
		//Verifying delete call flow msg for associated call flow
		callFlowPage.clickDeleteCallFlowBtn(supportDriver);
		assertEquals(callFlowPage.getSaveCallFLowMessage(supportDriver), callFlowAssociatedDeleteMsg, "Message does not match");
		callFlowPage.clickCloseBtn(supportDriver);
		
		// Opening Emergency Tab
		callFlowPage.clickAccountLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//Creating a second Emergency routing 
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting created date
		createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusRoutingToCheckMsg(supportDriver, location, callFlow, createdDate);

		//Verifying msg when associating second routing
		assertEquals(emergencyPage.getToastMsg(supportDriver), multipleEmergencyRoutingMsg, "Multiple emergency routing msg do not match");
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --associate_call_flow_and_delete-- passed ");
	}

	// Verify call routed to emergency call routing call flow dial step user who have same location for which emergency routing ON
	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_direct_call_to_user_after_enable_emergency_routing() {
		
		System.out.println("Test case --verify_direct_call_to_user_after_enable_emergency_routing-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//getting default location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "AutomationLocation");
		usersPage.savedetails(supportDriver);
		String location = usersPage.getDefaultLocation(supportDriver);
		
		//setting up call flow for emergency routing
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, "");
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.User, CONFIG.getProperty("qa_admin_user_name"));
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		// Opening Emergency Tab
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		// initializing admin driver on which emergency routing call flow is set
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, qa_support_number);
		callToolsPanel.callNotesSectionVisible(agentDriver);

		// verifying on support that no accept button is visible
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));

		// verifying on admin that call is incoming
		softPhoneCalling.switchToTab(adminDriver, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		// Opening Emergency Tab
		softPhoneCalling.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_direct_call_to_user_after_enable_emergency_routing-- passed ");
	}

//	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_transfer_call_to_user_after_enable_emergency_routing() {
		
		System.out.println("Test case --verify_transfer_call_to_user_after_enable_emergency_routing-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//getting default location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "Jaipur");
		usersPage.savedetails(supportDriver);
		String location = usersPage.getDefaultLocation(supportDriver);
		
		// Opening Emergency Tab
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to admin/support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		//taking an inbound call to user agent
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.softphoneAgentCall(supportDriver, CONFIG.getProperty("qa_agent_user_number"));
		callToolsPanel.callNotesSectionVisible(supportDriver);

		// Transfer call to support from agent
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.pickupIncomingCall(agentDriver);
		softPhoneCalling.transferToNumber(agentDriver, CONFIG.getProperty("qa_support_user_name"));
		softPhoneCalling.isCallBackButtonVisible(agentDriver);

		// verifying on support that call not transfered
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(supportDriver));
		
		// Opening Emergency Tab
		softPhoneCalling.switchToTab(supportDriver, 2);
		callFlowPage.clickAccountLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_transfer_call_to_user_after_enable_emergency_routing-- passed ");
	}
	
	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_user_receives_call_when_routing_set_for_all_and_location_set_to_none() {
		
		System.out.println("Test case --verify_user_receives_call_when_routing_set_for_all_and_location_set_to_none-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//getting default location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "None");
		usersPage.saveUserOverviewPage(supportDriver);
		
		// setting up call flow for emergency routing
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, "");
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.User, CONFIG.getProperty("qa_admin_user_name"));
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		// Opening Emergency Tab
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, null, callFlow, createdDate);

		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to admin/support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, qa_support_number);
		callToolsPanel.callNotesSectionVisible(agentDriver);

		// verifying on support that accept button is visible
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		softPhoneCalling.clickAcceptCallButton(supportDriver);
		
		// verifying on agent that call is not disconnected
		softPhoneCalling.switchToTab(agentDriver, 1);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(agentDriver));
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		// Opening Emergency Tab
		softPhoneCalling.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_receives_call_when_routing_set_for_all_and_location_set_to_none-- passed ");
	}

	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_call_from_central_number_after_enable_emergency_routing() {
		
		System.out.println("Test case --verify_call_from_central_number_after_enable_emergency_routing-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//setting up call flow for emergency routing
		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, "");
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.User, CONFIG.getProperty("qa_admin_user_name"));
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		//getting extension number
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.addExtension(supportDriver, "98745");
		usersPage.selectLocation(supportDriver, "AutomationLocation");
		usersPage.savedetails(supportDriver);
		
		String location = usersPage.getDefaultLocation(supportDriver);
		
		//getting central number
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//creating central number if not present
		if (accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()) {
			String centralLabelName = "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
		}
		
		String centralNumber = accountIntelligentTab.getCentralNoList(supportDriver).get(0);
		
		// Opening Emergency Tab
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		//adding emergency routing for user location
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		// initializing admin driver set in emregency call flow user
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to support's central number
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, centralNumber);
		softPhoneCalling.verifyDeclineButtonIsInvisible(agentDriver);
		assertTrue(softPhoneCalling.isActiveCallHoldButtonEnable(agentDriver));

		//entering extension number in dial pad
		softPhoneCalling.idleWait(12);
		softPhoneCalling.openDialPad(agentDriver);
		softPhoneCalling.appentTextInDialpad(agentDriver, "9");
		softPhoneCalling.appentTextInDialpad(agentDriver, "8");
		softPhoneCalling.appentTextInDialpad(agentDriver, "7");
		softPhoneCalling.appentTextInDialpad(agentDriver, "4");
		softPhoneCalling.appentTextInDialpad(agentDriver, "5");
		softPhoneCalling.appentTextInDialpad(agentDriver, "#");
		
		// verifying on support that no accept button is visible
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));

		// verifying on admin that call is connected
		softPhoneCalling.switchToTab(adminDriver, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);

		//clean up code
		softPhoneCalling.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_from_central_number_after_enable_emergency_routing-- passed ");
	}
	
	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_call_to_user_queue_from_call_flow_after_enable_routing() {
		
		System.out.println("Test case --verify_call_to_user_queue_from_call_flow_after_enable_routing-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		//call queue details
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime(" HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime(" HH:mm:ss.SSS"));

		// getting missed call and voicemail count before
		softphoneCallHistoryPage.reloadSoftphone(supportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.clickGroupCallsLink(supportDriver);
		softphoneCallHistoryPage.idleWait(2);
		int missedCallCount = softphoneCallHistoryPage.getHistoryMissedCallCount(supportDriver);
		int missedVoicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(supportDriver);
		softphoneCallHistoryPage.deleteAllVMQueue(supportDriver, callQueueName);

		//getting default location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "AutomationLocation");
		usersPage.savedetails(supportDriver);
		String location = usersPage.getDefaultLocation(supportDriver);
		
		callQueuesPage.openAddNewCallQueue(supportDriver);
		callQueuesPage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuesPage.addMember(supportDriver, CONFIG.getProperty("qa_support_user_name"));
		
		//setting up call flow for emergency routing
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, "");
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.refreshCurrentDocument(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		// Opening Emergency Tab
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("emergency_route_call_flow_number"));
		callToolsPanel.callNotesSectionVisible(agentDriver);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(agentDriver));

		// verifying on support that no accept button is visible
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		
		//hanging up active call on agent
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);

		//verify missed call and vm count before
		softPhoneCalling.switchToTab(supportDriver, 1);
		softphoneCallHistoryPage.reloadSoftphone(supportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.clickGroupCallsLink(supportDriver);
		softphoneCallHistoryPage.idleWait(2);
		assertEquals(softphoneCallHistoryPage.getHistoryMissedCallCount(supportDriver), missedCallCount+1);
		assertEquals(softphoneCallHistoryPage.getMissedVoicemailCount(supportDriver), missedVoicemailCount+1);
		softphoneCallHistoryPage.playVMByCallQueue(supportDriver, callQueueName);
		
		// Opening Emergency Tab
		softPhoneCalling.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);

		// deleting second call queue
		callQueuesPage.openCallQueueSearchPage(supportDriver);
		callQueuesPage.searchCallQueues(supportDriver, callQueueName, accountName);
		callQueuesPage.selectCallQueue(supportDriver, callQueueName);
		callQueuesPage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_to_user_queue_from_call_flow_after_enable_routing-- passed ");
	}

	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_call_to_user_queue_from_acd_call_flow_after_enable_routing() {
		
		System.out.println("Test case --verify_call_to_user_queue_from_acd_call_flow_after_enable_routing-- started ");
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetRandomString(4));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetRandomString(4));

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		// delete voice mail with the queue name
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.clickGroupCallsLink(supportDriver);
		softphoneCallHistoryPage.deleteAllVMQueue(supportDriver, callQueueName);
		
		//getting default location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "AutomationLocation");
		usersPage.savedetails(supportDriver);
		String location = usersPage.getDefaultLocation(supportDriver);
		
		//adding call queue
		callQueuesPage.openAddNewCallQueue(supportDriver);
		callQueuesPage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuesPage.addMember(supportDriver, CONFIG.getProperty("qa_support_user_name"));
		callQueuesPage.selectLongestDistributationType(supportDriver);
		callQueuesPage.saveGroup(supportDriver);
		
		//setting up call flow for emergency routing
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, "");
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
		
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		callFlowPage.idleWait(1);
		callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		// Opening Emergency Tab
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		emergencyPage.clickAddIcon(supportDriver);
		emergencyPage.selectLocation(supportDriver, location);
		emergencyPage.selectCallFlow(supportDriver, callFlow);
		emergencyPage.saveEmergencyRouting(supportDriver);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriver, location, callFlow, createdDate);

		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("emergency_route_call_flow_number"));
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(agentDriver));

		// verifying on support that no accept button is visible
		softPhoneCalling.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		
		softPhoneCalling.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		//verifying voicemail drop on call queues
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.clickGroupCallsLink(supportDriver);
		softphoneCallHistoryPage.playVMByCallQueue(supportDriver, callQueueName);
		
		// Opening Emergency Tab
		softPhoneCalling.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		emergencyPage.navigateToEmergencyTab(supportDriver);

		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);

		// deleting second call queue
		callQueuesPage.openCallQueueSearchPage(supportDriver);
		callQueuesPage.searchCallQueues(supportDriver, callQueueName, accountName);
		callQueuesPage.selectCallQueue(supportDriver, callQueueName);
		callQueuesPage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_call_to_user_queue_from_acd_call_flow_after_enable_routing-- passed ");
	}
	
	@AfterClass(groups = { "SupportOnly" })
	public void afterClass(){
		
		//cleaning emergency call routings if any
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		
		// Opening Emergency Tab
		emergencyPage.navigateToEmergencyTab(supportDriver);
		
		// clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriver);
		emergencyPage.deleteAllRouting(supportDriver);
		
		driverUsed.put("supportDriver", false);
	}
}
