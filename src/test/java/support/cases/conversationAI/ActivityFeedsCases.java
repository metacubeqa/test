package support.cases.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.Test;

import softphone.source.SoftPhoneCalling;
import support.base.SupportBase;
import support.source.accounts.AccountLicenseTab;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.CallsTabPage.CallData;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAI.InboxTabPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class ActivityFeedsCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AccountLicenseTab accountLicenseTab = new AccountLicenseTab();
	CallsTabPage callsPage = new CallsTabPage();
	InboxTabPage inboxTabPage = new InboxTabPage();
	DashBoardConversationAI dashBoardConversationAI = new DashBoardConversationAI();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	
	@Test (groups = {"Regression"})
	public void verify_notes_and_supervisor_notes_in_activity_feed() throws Exception {
		System.out.println("Test case --verify_notes_and_supervisor_notes_in_activity_feed-- started ");
		
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		callsPage.clearAllFilters(caiCallerDriver);
		callsPage.setAgentFilter(caiCallerDriver, CONFIG.getProperty("qa_cai_caller_name"));
		
		callsPage.viewCAI(caiCallerDriver);
		callsPage.clickShareIcon(caiCallerDriver);
		String note = callsPage.enterShareId(caiCallerDriver,CONFIG.getProperty("qa_cai_verify_name"));
		callsPage.clickSendIcon(caiCallerDriver);

		// Navigating to activity feeds section of caller and verifying message
		dashBoardConversationAI.navigateToActivityFeedSection(caiCallerDriver);
		
		String inboxMessage  = inboxTabPage.getInboxMsgWithSpecificNote(caiCallerDriver, note);
		assertEquals(inboxMessage, String.format("%s shared a call with %s.", CONFIG.getProperty("qa_cai_caller_name"), CONFIG.getProperty("qa_cai_verify_name")));

		// Navigating to activity feeds section of called and verifying message
		
		dashboard.clickConversationAI(caiVerifyDriver);
		dashBoardConversationAI.navigateToActivityFeedSection(caiVerifyDriver);
		inboxMessage = inboxTabPage.getInboxMsgWithSpecificNote(caiVerifyDriver, note);
		assertEquals(inboxMessage, String.format("%s shared a call with %s.", CONFIG.getProperty("qa_cai_caller_name"), CONFIG.getProperty("qa_cai_verify_name")));

		//updating supervisor note of called driver
		inboxTabPage.goToMenuRightOfInboxMsg(caiVerifyDriver, note);
		String superVisorNote = "SuperVisorNote".concat(HelperFunctions.GetRandomString(2));
		callsPage.enterSupervisorNotes(caiVerifyDriver, superVisorNote);
		caiVerifyDriver.navigate().back();
		dashboard.isPaceBarInvisible(caiVerifyDriver);
		
		//Verifying in caller driver after updating supervisor notes
		dashBoardConversationAI.navigateToInboxSection(caiCallerDriver);
		dashBoardConversationAI.navigateToActivityFeedSection(caiCallerDriver);
		inboxMessage  = inboxTabPage.getInboxMsgWithSuperVisorNote(caiCallerDriver, superVisorNote);
		assertEquals(inboxMessage, String.format("%s has added a supervisor note.", CONFIG.getProperty("qa_cai_verify_name")));
	
		inboxTabPage.viewInboxMessage(caiCallerDriver);
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		assertNotNull(inboxTabPage.getAgentInboxResult(caiCallerDriver));
		assertNotNull(inboxTabPage.getCallTimeInboxResult(caiCallerDriver));
		assertNotNull(inboxTabPage.getCallDurationInboxResult(caiCallerDriver));
		CallDetails.put(CallData.Agent, inboxTabPage.getAgentInboxResult(caiCallerDriver));
		CallDetails.put(CallData.CallDate, inboxTabPage.getCallTimeInboxResult(caiCallerDriver));
		CallDetails.put(CallData.CallDuration, inboxTabPage.getCallDurationInboxResult(caiCallerDriver));

		inboxTabPage.goToMenuRightOfInboxMsg(caiCallerDriver, superVisorNote);
		assertEquals(CallDetails.get(CallData.Agent), callsPage.getCallAgent(caiCallerDriver));
		assertEquals(CallDetails.get(CallData.CallDate), callsPage.getCallTime(caiCallerDriver));
		assertEquals(CallDetails.get(CallData.CallDuration), callsPage.getCallDuration(caiCallerDriver));

		superVisorNote = "SuperVisorNote".concat(HelperFunctions.GetRandomString(2));
		callsPage.enterSupervisorNotes(caiCallerDriver, superVisorNote);
		
		// Verify inbox msg acc to supervisor note
		caiCallerDriver.navigate().back();
		inboxMessage  = inboxTabPage.getInboxMsgWithSuperVisorNote(caiCallerDriver, superVisorNote);
		assertEquals(inboxMessage, String.format("%s has added a supervisor note.", CONFIG.getProperty("qa_cai_caller_name")));
	
		// Default search order is newest to oldest
		int page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxOrder(caiCallerDriver, page, CallsTabPage.SearchOrderOptions.Descending.toString());

		// Reset Order to Ascending when set from Search Filter
		callsPage.selectSearchOrder(caiCallerDriver, CallsTabPage.SearchOrderOptions.Ascending.toString());
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxOrder(caiCallerDriver, page, CallsTabPage.SearchOrderOptions.Ascending.toString());

		// Reset Order to Ascending when set from Search Filter
		callsPage.selectSearchOrder(caiCallerDriver, CallsTabPage.SearchOrderOptions.Descending.toString());
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxOrder(caiCallerDriver, page, CallsTabPage.SearchOrderOptions.Descending.toString());

		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_filter_activity_feeds() {
		System.out.println("Test case --verify_filter_activity_feeds-- started ");
		
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashBoardConversationAI.navigateToActivityFeedSection(caiCallerDriver);

		//Take all page count
		int initialCount = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));

		// Set Notification filter to Supervisor notes
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.Supnotes.toString());
		
		int page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxNotificationFilter(caiCallerDriver, page, InboxTabPage.InboxNotificationTypes.Supnotes.toString());

		//Reset Filter to default
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.All.toString());
		
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		assertEquals(initialCount, page);

		// Set Notification filter to Annotation
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.Annotations.toString());
		
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxNotificationFilter(caiCallerDriver, page, InboxTabPage.InboxNotificationTypes.Annotations.toString());

		//Reset Filter to default
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.All.toString());
		
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		assertEquals(initialCount, page);

		// Set Notification filter to Flagged for Review
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.Flagged.toString());
		
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		inboxTabPage.verifyInboxNotificationFilter(caiCallerDriver, page, InboxTabPage.InboxNotificationTypes.Flagged.toString());

		//Reset Filter to default
		inboxTabPage.selectInboxNotification(caiCallerDriver, InboxTabPage.InboxNotificationTypes.All.toString());
		
		page = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		assertEquals(initialCount, page);

		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case is pass");
	}
	
//	@Test(groups = { "Regression" })
	public void verify_cai_manager_settings_on_off_functionalities() {
		System.out.println("Test case --verify_cai_manager_settings_on_off_functionalities-- started ");
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.openManageUsersPage(caiSupportDriver);
		usersPage.OpenUsersSettings(caiSupportDriver, CONFIG.getProperty("qa_cai_caller_name"), CONFIG.getProperty("qa_cai_caller_email"));		
				
		usersPage.disableConversationAIManagerBtn(caiSupportDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiSupportDriver);

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		assertFalse(dashBoardConversationAI.isActivityFeedSectionVisible(caiCallerDriver));

		//Take all page count
		int pageCount = Integer.valueOf(callsPage.getTotalPage(caiCallerDriver));
		callsPage.verifySearchResult(caiCallerDriver, CONFIG.getProperty("qa_cai_caller_name"), pageCount, CallData.Agent.name(), true);
		
		usersPage.enableConversationAIManagerBtn(caiSupportDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiSupportDriver);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.refreshCurrentDocument(caiCallerDriver);
		assertTrue(dashBoardConversationAI.isActivityFeedSectionVisible(caiCallerDriver));

		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_cai_manager_settings_on_off_functionalities-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_admin_make_cai_manager_settings_on_off_for_user() {
		System.out.println("Test case --verify_admin_make_cai_manager_settings_on_off_for_user-- started ");
		
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		//Getting remaining license count
		dashboard.clickAccountsLink(caiCallerDriver);
		String type = "Conversation AI Users";
		accountLicenseTab.openLicensingTab(caiCallerDriver);
		int remainingLicenseCAI = accountLicenseTab.getRemainingLicenseCount(caiCallerDriver, type);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.disableConversationAnalyticsBtn(caiCallerDriver);
		usersPage.disableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);

		//verifying cai not visible
		dashboard.refreshCurrentDocument(caiCallerDriver);
		assertFalse(dashboard.isConversationAIVisible(caiCallerDriver));

		//verifying license count increased by 1
		dashboard.clickAccountsLink(caiCallerDriver);
		accountLicenseTab.openLicensingTab(caiCallerDriver);
		assertEquals(accountLicenseTab.getRemainingLicenseCount(caiCallerDriver, type), remainingLicenseCAI+1);

		remainingLicenseCAI = accountLicenseTab.getRemainingLicenseCount(caiCallerDriver, type);
		
		// Opening Intelligent Dialer Tab
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		
		//verifying cai visible
		dashboard.refreshCurrentDocument(caiCallerDriver);
		assertTrue(dashboard.isConversationAIVisible(caiCallerDriver));

		//verifying license count decreased by 1
		dashboard.clickAccountsLink(caiCallerDriver);
		accountLicenseTab.openLicensingTab(caiCallerDriver);
		assertEquals(accountLicenseTab.getRemainingLicenseCount(caiCallerDriver, type), remainingLicenseCAI - 1);

		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_admin_make_cai_manager_settings_on_off_for_user-- passed ");
	}
}
