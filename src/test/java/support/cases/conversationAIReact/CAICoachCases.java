package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CAICoachCases extends SupportBase{
	
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftphoneBase softphoneBase = new SoftphoneBase();
	CallsTabReactPage callsTabPage = new CallsTabReactPage();
	ConversationDetailPage caiDetailPage = new ConversationDetailPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	UsersPage usersPage = new UsersPage();
	Dashboard dashBoard = new Dashboard();
	Random random  = new Random();
	
	private String contactFirstName;
	private String contactAccountName;
	private String callerName;
	
	@BeforeClass(groups = { "Regression", "Product Sanity" })
	public void beforeClass() {

		contactFirstName 	= CONFIG.getProperty("contact_first_name");
		contactAccountName  = CONFIG.getProperty("contact_account_name");
		callerName 			= CONFIG.getProperty("qa_cai_caller_name");
	}
	
	@Test(groups = { "Regression" })
	public void verify_coach_icon_when_connected_end_before_one_min() {
		
		System.out.println("Test case --verify_coach_icon_when_connected_end_before_one_min-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
		
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiVerifyDriver));
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiVerifyDriver));
		
		callsTabPage.idleWait(2);
		softPhoneCalling.hangupActiveCall(caiCallerDriver);
		callsTabPage.idleWait(2);
		assertFalse(callToolsPanel.isCoachIconVisible(caiVerifyDriver));
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsLead(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}
		softphoneCallHistoryPage.openCallsHistoryPage(caiVerifyDriver);
		softphoneCallHistoryPage.openRecentUnknownCallerEntry(caiVerifyDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiVerifyDriver));
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_coach_icon_when_connected_end_before_one_min-- passed ");
	}

	@Test(groups = { "Regression" })
	public void continue_call_more_than_one_min_and_access_cai_links_from_diff_sections() {
		
		System.out.println("Test case --continue_call_more_than_one_min_and_access_cai_links_from_diff_sections-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
	
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		String receiverName = callScreenPage.getCallerName(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		callToolsPanel.verifyCoachIconColorNotSelected(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		System.out.println(caiDetailPage.getCallsTabPageHeading(caiCallerDriver));
		System.out.println("Call Between "+callerName+ " and " +receiverName);
		assertTrue(caiDetailPage.getCallsTabPageHeading(caiCallerDriver).contains("Call Between "+callerName+ " and " +receiverName));
		assertFalse(caiDetailPage.isOpportunityDetailTabVisible(caiCallerDriver));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);

		// Navigating to Call notes tab and verify conversation ai link
		softPhoneActivityPage.navigateToCallNotesTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		System.out.println(caiDetailPage.getCallsTabPageHeading(caiCallerDriver));
		System.out.println("Call Between "+callerName+ " and " +receiverName);
		assertTrue(caiDetailPage.getCallsTabPageHeading(caiCallerDriver).contains("Call Between "+callerName+ " and " +receiverName));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);

		// Navigating to Calls tab and verify conversation ai link
		softPhoneActivityPage.navigateToCallsTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		System.out.println(caiDetailPage.getCallsTabPageHeading(caiCallerDriver));
		System.out.println("Call Between "+callerName+ " and " +receiverName);
		assertTrue(caiDetailPage.getCallsTabPageHeading(caiCallerDriver).contains("Call Between "+callerName+ " and " +receiverName));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);

		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --continue_call_more_than_one_min_and_access_cai_links_from_diff_sections-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void access_cai_link_with_call_history_page() {
		
		System.out.println("Test case --access_cai_link_with_call_history_page-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		String receiverName = callScreenPage.getCallerName(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//opening call history page and clicking link
		softphoneCallHistoryPage.openCallsHistoryPage(caiCallerDriver);
		softphoneCallHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		System.out.println(caiDetailPage.getCallsTabPageHeading(caiCallerDriver));
		System.out.println("Call Between "+callerName+ " and " +receiverName);
		assertTrue(caiDetailPage.getCallsTabPageHeading(caiCallerDriver).contains("Call Between "+callerName+ " and " +receiverName));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --access_cai_link_with_call_history_page-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_cai_link_removed_from_call_activity_when_cai_setting_disabled() {
		
		System.out.println("Test case --verify_cai_link_removed_from_call_activity_when_cai_setting_disabled-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
	
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		// Opening Intelligent Dialer Tab
		dashBoard.switchToTab(caiCallerDriver, 2);
		dashBoard.clickOnUserProfile(caiCallerDriver);
		usersPage.disableConversationAIManagerBtn(caiCallerDriver);
		usersPage.disableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);

		//verify cai link not visible
		dashBoard.switchToTab(caiCallerDriver, 1);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		assertFalse(softPhoneActivityPage.isCAILinkVisible(caiCallerDriver, callSubject));
		
		softPhoneActivityPage.navigateToCallNotesTab(caiCallerDriver);
		assertFalse(softPhoneActivityPage.isCAILinkVisible(caiCallerDriver, callSubject));
		
		//opening call history page
		softphoneCallHistoryPage.openCallsHistoryPage(caiCallerDriver);
		assertFalse(softphoneCallHistoryPage.isHistoryCAILinkVisible(caiCallerDriver, 0));
		
		// Opening Intelligent Dialer Tab
		dashBoard.switchToTab(caiCallerDriver, 2);
		dashBoard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);

		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_cai_link_removed_from_call_activity_when_cai_setting_disabled-- passed ");
	}
	
	//Related record object tab visible when related with conversation AI call
	@Test(groups = { "Regression" })
	public void verify_user_add_supervisor_notes_cai_call_verify_in_sfdc() {
		
		System.out.println("Test case --verify_user_add_supervisor_notes_cai_call_verify_in_sfdc-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
		
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		String relatedOpportunity = "Automation Queue Opportunity";
		
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickRelatedRecordsIcon(caiCallerDriver);
		callToolsPanel.selectOpportunityFromRelatedRecordSearchList(caiCallerDriver, relatedOpportunity);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		String superVisorNotes = "AutoSuperVisor".concat(HelperFunctions.GetRandomString(3));
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, superVisorNotes);
		caiDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, superVisorNotes);
		assertTrue(caiDetailPage.isOpportunityDetailTabVisible(caiCallerDriver));
		String caiDetailPageWindow = caiCallerDriver.getWindowHandle();
		
		caiDetailPage.clickSubjectLink(caiCallerDriver);
		assertEquals(sfTaskDetailPage.getSuperVisorNotes(caiCallerDriver), superVisorNotes);
		callsTabPage.closeTab(caiCallerDriver);

		caiCallerDriver.switchTo().window(caiDetailPageWindow);
		callsTabPage.closeTab(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, 1);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_user_add_supervisor_notes_cai_call_verify_in_sfdc-- passed ");
	}
	
	//Add Call notes to own CAI call and verify in sfdc task and softphone activity
	@Test(groups = { "Regression" })
	public void verify_user_add_call_notes_on_own_cai_call_verify_in_sfdc() {
		
		System.out.println("Test case --verify_user_add_call_notes_on_own_cai_call_verify_in_sfdc-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		callsTabPage.switchToTab(caiVerifyDriver, 1);
		
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		//creating conversation AI recording
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		String caiCallNotes = "AutoCAICAllNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.addOrEditCallNotes(caiCallerDriver, caiCallNotes);
		caiDetailPage.verifyCallNotesSaved(caiCallerDriver, caiCallNotes);
		String caiDetailPageWindow = caiCallerDriver.getWindowHandle();
		
		caiDetailPage.clickSubjectLink(caiCallerDriver);
		assertEquals(sfTaskDetailPage.getComments(caiCallerDriver), caiCallNotes);
		callsTabPage.closeTab(caiCallerDriver);

		caiCallerDriver.switchTo().window(caiDetailPageWindow);
		callsTabPage.closeTab(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, 1);
		
		//verifying on softphone
		softphoneCallHistoryPage.reloadSoftphone(caiCallerDriver);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		assertEquals(softPhoneActivityPage.getCommentFromTaskList(caiCallerDriver, callSubject), caiCallNotes);

		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_user_add_call_notes_on_own_cai_call_verify_in_sfdc-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_given_call_notes_on_other_agents_cai_call() {
		
		System.out.println("Test case --verify_given_call_notes_on_other_agents_cai_call-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		//Open dial pad of driver and dial to support driver
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
		
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		String notes = "AutoNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.addOrEditCallNotes(caiCallerDriver, notes);
		caiDetailPage.verifyCallNotesSaved(caiCallerDriver, notes);
		String url = caiCallerDriver.getCurrentUrl();
		
		dashBoard.switchToTab(caiVerifyDriver, 2);
		caiVerifyDriver.get(url);
		caiDetailPage.waitForPageLoaded(caiVerifyDriver);
		caiDetailPage.verifyCallNotesSaved(caiVerifyDriver, notes);
		caiDetailPage.verifyAddEditCallNotesIconNotVisible(caiVerifyDriver);
		
		callsTabPage.closeTab(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, 1);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_given_call_notes_on_other_agents_cai_call-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void coach_icon_gets_removed_when_user_makes_conference() {
		
		System.out.println("Test case --coach_icon_gets_removed_when_user_makes_conference-- started ");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiVerifyDriver);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		callsTabPage.switchToTab(caiSupportDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiSupportDriver);
		softPhoneCalling.hangupIfInActiveCall(caiSupportDriver);
		
		//Hanging up call if present and checking coach icon
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));

		//making a call
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiSupportDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiSupportDriver));
		
		// checking hold call actions button resume and merge
		callsTabPage.idleWait(1);
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(caiCallerDriver);
		callsTabPage.idleWait(1);
		
		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);

		// switching tab
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiVerifyDriver);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
				
		//switching tab
		callsTabPage.switchToTab(caiSupportDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiSupportDriver);
		softPhoneCalling.hangupIfInActiveCall(caiSupportDriver);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --coach_icon_gets_removed_when_user_makes_conference-- passed ");
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "verify_cai_link_removed_from_call_activity_when_cai_setting_disabled":

				// Opening Intelligent Dialer Tab
				initializeSupport("caiCallerDriver");
				dashBoard.switchToTab(caiCallerDriver, 2);
				dashBoard.clickConversationAI(caiCallerDriver);
				dashBoard.clickOnUserProfile(caiCallerDriver);
				usersPage.enableConversationAIManagerBtn(caiCallerDriver);
				usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
				userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
				userIntelligentDialerTab.closeTab(caiCallerDriver);
				userIntelligentDialerTab.switchToTab(caiCallerDriver, 2);
				break;
			default:
				break;
			}
		}
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority" , "Product Sanity"}, alwaysRun = true)
	public void afterClass() {
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		dashBoard.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);
		dashBoard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		dashBoard.closeTab(caiCallerDriver);
		dashBoard.switchToTab(caiCallerDriver, 2);
		driverUsed.put("caiCallerDriver", false);
	}
}
