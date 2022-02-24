package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import base.TestBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.accounts.AccountOverviewTab;
import support.source.callQueues.CallQueuesPage;
import support.source.commonpages.CallRecordingPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.CallsTabReactPage.TimeFrame;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.ConversationDetailPage.actionsDropdownItems;
import support.source.conversationAIReact.InboxPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.conversationAIReact.NotificationsPage;
import support.source.conversationAIReact.SavedSearchPage;
import support.source.conversationAIReact.SettingsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class FeatureAccessibility extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	AccountOverviewTab accOverView = new AccountOverviewTab();
	SettingsPage settingsPage = new SettingsPage();
	NotificationsPage notificationsPage = new NotificationsPage();
	DashBoardConversationAI dashBoardConversationAI = new DashBoardConversationAI();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallsTabReactPage callsTabPage = new CallsTabReactPage();
	LibraryReactPage libraryPage = new LibraryReactPage();
	CallRecordingPage callRecordingPage = new CallRecordingPage();
	SavedSearchPage savedSearchPage = new SavedSearchPage();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	ConversationDetailPage caiDetailPage = new ConversationDetailPage();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	InboxPage inboxPage = new InboxPage();
	CallScreenPage callScreenPage = new CallScreenPage();
	
	private String userName;
	private static final String olddateFormat = "MM/dd/yyyy h:mm a";
	private static final String newdateFormat = "M/d/yyyy h:mma";

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		userName = CONFIG.getProperty("qa_cai_caller_name");
  }
	
	//Verify access user dropdown from conversation AI tab
	@Test(groups = { "MediumPriority" })
	public void verify_access_user_drop_down_from_cai_tab() {
		System.out.println("Test case --verify_access_user_drop_down_from_cai_tab-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		usersPage.closeTab(caiCallerDriver);
		usersPage.switchToTab(caiCallerDriver, 2);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAINotifications(caiCallerDriver);
		assertTrue(notificationsPage.isCAINotificationHeaderVisible(caiCallerDriver));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_access_user_drop_down_from_cai_tab-- passed ");
	}

	//Verify user able to access other tabs of Admin from Conversation AI Page
//	@Test(groups = { "MediumPriority" })
	public void verify_user_able_to_access_other_tabs_from_cai_tab() {
		System.out.println("Test case --verify_user_able_to_access_other_tabs_from_cai_tab-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		dashboard.clickConversationAI(caiCallerDriver);
		
		dashboard.clickHomeBars(caiCallerDriver);
		
		//click first queue
		callQueuePage.openCallQueueSearchPage(caiCallerDriver);
		callQueuePage.clickFirstCallQueue(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_able_to_access_other_tabs_from_cai_tab-- passed ");
	}
	
	//Verify clicking on ringDNA image from Conversation AI page should redirect user to Account page
//	@Test(groups = { "MediumPriority" })
	public void verify_user_redirected_to_account_page_after_clicking_ringdna_logo() {
		System.out.println("Test case --verify_user_redirected_to_account_page_after_clicking_ringdna_logo-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		dashboard.clickConversationAI(caiCallerDriver);
		
		dashBoardConversationAI.clickRDNALogo(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_redirected_to_account_page_after_clicking_ringdna_logo-- passed ");
	}
	
	//Verify user try to access other user's conversation AI recording which is not shared when Manager setting OFF
	@Test(groups = { "MediumPriority" })
	public void verify_user_try_to_access_other_users_cai_when_manager_off() {
		System.out.println("Test case --verify_user_try_to_access_other_users_cai_when_manager_off-- started ");

		initializeSupport("caiVerifyDriver"); 
		driverUsed.put("caiVerifyDriver", true);
		
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		usersPage.disableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		dashboard.closeTab(caiCallerDriver);
		dashboard.switchToTab(caiCallerDriver, 2);
		
		// filter for my calls agent and not shared with others 
		String filter = "My Calls";
		
		dashboard.switchToTab(caiVerifyDriver, 2);
		dashboard.clickConversationAI(caiVerifyDriver);
		callsTabPage.selectAgentNameFromFilter(caiVerifyDriver, filter);
		
		String searchToken = callsTabPage.setConversationAIFilters(caiVerifyDriver,
				CallDataFilterType.SharedWithOthers, 0, null);

		// change the status of the filter to false
		callsTabPage.clickFilterBooleanStatus(caiVerifyDriver, searchToken);
		callsTabPage.openConversationDetails(caiVerifyDriver, 0);
		String url = caiVerifyDriver.getCurrentUrl();
		
		//opening first user with that url
		callsTabPage.switchToTab(caiCallerDriver, 2);
		callsTabPage.openNewBlankTab(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, callsTabPage.getTabCount(caiCallerDriver));
		caiCallerDriver.get(url);
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		dashboard.isPaceBarInvisible(caiCallerDriver);
		String appUrl = TestBase.CONFIG.getProperty("qa_support_tool_site");
		
		//verify recording player should be displayed Literal(Lite Player)
		assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(caiCallerDriver));
		assertTrue(caiCallerDriver.getCurrentUrl().contains(appUrl.concat("/#call-player")));
		callRecordingPage.closeTab(caiCallerDriver);
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		dashboard.closeTab(caiCallerDriver);
		dashboard.switchToTab(caiCallerDriver, 2);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_user_try_to_access_other_users_cai_when_manager_off-- passed ");
	}
	
	//Verify user should not able to access conversation AI feature through the url  if cai feature is off
	//@Test(groups = { "MediumPriority" })
	public void verify_user_not_able_to_access_cai_if_cai_setting_of_for_user() {
		System.out.println("Test case --verify_user_not_able_to_access_cai_if_cai_setting_of_for_user-- started ");

		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		// disable cai support
		dashboard.clickOnUserProfile(caiSupportDriver);
		usersPage.disableConversationAnalyticsBtn(caiSupportDriver);
		usersPage.disableConversationAIManagerBtn(caiSupportDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiSupportDriver);

		//verifying cai not visible
		dashboard.refreshCurrentDocument(caiSupportDriver);
		assertFalse(dashboard.isConversationAIVisible(caiSupportDriver));
		
		String url = "https://app-qa.ringdna.net/#smart-recordings/calls";
		caiSupportDriver.get(url);
		dashboard.waitForPageLoaded(caiSupportDriver);
		dashboard.isPaceBarInvisible(caiSupportDriver);
		assertFalse(dashboard.isConversationAIVisible(caiSupportDriver));
		callsTabPage.verifyUserOnCallsPage(caiSupportDriver);
		
		System.out.println(callsTabPage.getTotalPageText(caiSupportDriver));
		assertEquals(callsTabPage.getStartingPageNumber(caiSupportDriver), 0);
		assertEquals(callsTabPage.getTotalPage(caiSupportDriver), 0);
		assertFalse(libraryPage.isLibraryTabVisible(caiSupportDriver));
		assertFalse(savedSearchPage.isSaveSearchTabVisible(caiSupportDriver));
		
		dashboard.clickHomeBars(caiSupportDriver);
		assertFalse(dashboard.isConversationAIVisible(caiSupportDriver));
		
		// Opening Intelligent Dialer Tab
		dashboard.clickOnUserProfile(caiSupportDriver);
		usersPage.enableConversationAnalyticsBtn(caiSupportDriver);
		usersPage.enableConversationAIManagerBtn(caiSupportDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiSupportDriver);
		
		//verifying cai visible
		dashboard.refreshCurrentDocument(caiSupportDriver);
		assertTrue(dashboard.isConversationAIVisible(caiSupportDriver));
		
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_user_try_to_access_other_users_cai_when_manager_off-- passed ");
	}
	
	//Verify Calls tab includes self and shared calls only when Manager setting OFF
	//Verify Calls Tab includes all calls of account for when Manager setting is ON
	@Test(groups = { "MediumPriority" })
	public void verify_calls_on_callstab_when_cai_manager_on_off() {
		System.out.println("Test case --verify_calls_on_callstab_when_cai_manager_on_off-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		// disable cai manager
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		usersPage.disableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);

		dashboard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		
		int totalPageBefore = callsTabPage.getTotalPage(caiCallerDriver);
		
		//verify in dropdown as well for agent user
		List<String> agentList = callsTabPage.getAgentListInDropDown(caiCallerDriver);
		assertTrue(agentList.size() == 2);
		assertTrue(agentList.contains("My Calls"));
		assertTrue(agentList.contains(CONFIG.getProperty("qa_cai_caller_name")));
		
		// verify result entry
		CallDataFilterType filterType = CallDataFilterType.Agent;
		List<String> tokenList = new ArrayList<String>();
		
		//click on Agent Name and verify that it takes user to Calls page and filter is applied
		String searchToken = CONFIG.getProperty("qa_cai_caller_name");
		tokenList.add(searchToken);
		
		//verify the filtered records for agent
		callsTabPage.verifySearchResult(caiCallerDriver, tokenList, null, filterType, true);
		agentList = callsTabPage.getAgentListInDropDown(caiCallerDriver);
		assertFalse(callsTabPage.isTextPresentInStringList(caiCallerDriver, agentList, "Cai User1"));
		
		// enable cai manager
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);

		dashboard.refreshCurrentDocument(caiCallerDriver);
		dashboard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		
		int totalPageAfter = callsTabPage.getTotalPage(caiCallerDriver);
		assertTrue(totalPageAfter > totalPageBefore);
		
		//verify the filtered records for agent
		agentList.clear();
		agentList = callsTabPage.getAgentListInDropDown(caiCallerDriver);
		assertTrue(agentList.size() > 2);
		assertTrue(callsTabPage.isTextPresentInStringList(caiCallerDriver, agentList, "Cai User1"));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_calls_on_callstab_when_cai_manager_on_off-- passed ");
	}
	
	//"Reprocess" option should see under one-year ConversationAI
	@Test(groups = { "MediumPriority" })
	public void verify_reprocess_option_visible_for_calls_more_than_one_year() {
		System.out.println("Test case --verify_reprocess_option_visible_for_calls_more_than_one_year-- started ");

		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashboard.clickConversationAI(caiSupportDriver);
		callsTabPage.clearAllFilters(caiSupportDriver);
		
		// Set the filter type to Time Frame and the search token as today
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneYear;
		tokenList.add(searchToken.toString());
		
		//filter for Today's Time frame and verify the filtered records
		String filterToken = callsTabPage.setConversationAIFilters(caiSupportDriver, filterType, searchToken.ordinal(), null);
		callsTabPage.verifySearchResult(caiSupportDriver, tokenList, null, filterType, true);
	
		callsTabPage.openConversationDetails(caiSupportDriver, 0);
		assertTrue(caiDetailPage.isActionDropDownItemVisible(caiSupportDriver, actionsDropdownItems.Reprocess));
		caiDetailPage.selectActionDropdownOption(caiSupportDriver, actionsDropdownItems.Reprocess);
		caiDetailPage.getToastMsg(caiSupportDriver).contains("is being uploaded for reprocessing");
		
		callsTabPage.navigateToCallsPage(caiSupportDriver);
		callsTabPage.clickFilterBooleanStatus(caiSupportDriver, filterToken);
		
		callsTabPage.openConversationDetails(caiSupportDriver, 0);
		assertFalse(caiDetailPage.isActionDropDownItemVisible(caiSupportDriver, actionsDropdownItems.Reprocess));
		
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_reprocess_option_visible_for_calls_more_than_one_year-- passed ");
	}
	
	//Verify conversationAI tab to unlicensed users and link to landing page
	@Test(groups = { "MediumPriority" })
	public void verify_cai_tab_to_unlicensed_users_and_link_to_landing_page() {
		System.out.println("Test case --verify_cai_tab_to_unlicensed_users_and_link_to_landing_page-- started ");

		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		WebDriver userRoleDriver = getDriver();
		SFLP.supportLoginWhenSoftphoneLogin(userRoleDriver, CONFIG.getProperty("qa_support_tool_site"),
				"utest@metacube.com", "ebmdna01981");
		dashboard.isPaceBarInvisible(userRoleDriver);

		// Verifying for admin assertions
		dashboard.waitForPageLoaded(userRoleDriver);
		dashboard.isPaceBarInvisible(userRoleDriver);
		dashboard.idleWait(3);
		
		//disabling cai settings
		dashboard.clickOnUserProfile(userRoleDriver);
		usersPage.disableConversationAnalyticsBtn(userRoleDriver);
		usersPage.disableConversationAIManagerBtn(userRoleDriver);
		userIntelligentDialerTab.saveAcccountSettings(userRoleDriver);

		dashboard.refreshCurrentDocument(userRoleDriver);
		dashboard.clickCAILearnMoreLink(userRoleDriver);

		userRoleDriver.quit();
		
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_cai_tab_to_unlicensed_users_and_link_to_landing_page-- passed ");
	}
	
	//Verify new call processed into conversation call recording when CAI setting enable for both account and user
	@Test(groups = { "Regression" })
	public void verify_cai_process_details_by_expanding_cai_in_calls_tab() {
		
		System.out.println("Test case --verify_cai_process_details_by_expanding_cai_in_calls_tab-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		// Verifying coach icon not visible
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
	
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(caiCallerDriver);
		contactDetails.put("Email", callScreenPage.getCallerEmail(caiCallerDriver));
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		//Verifying notes are correct
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashBoardConversationAI.navigateToCallsSection(caiCallerDriver);
		
		String textToFindInRow = "Call Notes";

		callsTabPage.enterSearchText(caiCallerDriver,  callNotes);	
		assertEquals(callsTabPage.getSearchPageRowCount(caiCallerDriver), 1);
		assertEquals(callsTabPage.getTextToFindInCAIRow(caiCallerDriver, textToFindInRow, 0), textToFindInRow+": 1 match");
		
		callsTabPage.expandSearchRows(caiCallerDriver, 1);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiCallerDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;

		assertEquals(inboxPage.getProspectDetailsName(caiCallerDriver), contactDetails.get("Name"));
		assertEquals(inboxPage.getProspectDetailsAccount(caiCallerDriver), contactDetails.get("Company"));
		assertEquals(inboxPage.getAgentDetailsName(caiCallerDriver), userName);
		assertEquals(callsTabPage.getAgentCallDispositionSearchRow(caiCallerDriver), disposition);
		
		inboxPage.verifyAnnotationBtnDisabled(caiCallerDriver);
		inboxPage.verifySuperVisorNotesBtnDisabled(caiCallerDriver);
		
		callsTabPage.openConversationDetails(caiCallerDriver, 0);
		assertEquals(caiDetailPage.getCallsTabPageHeading(caiCallerDriver), "Call Between "+userName+ " and " +contactDetails.get("Name")+ " - "+dateTime);
		caiDetailPage.verifyCallNotesSaved(caiCallerDriver, callNotes);
	
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_cai_process_details_by_expanding_cai_in_calls_tab-- passed ");
	}
	
  //enable cai user
	@AfterClass(groups = { "Regression", "MediumPriority" }, alwaysRun = true)
	public void afterClass() {
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		userIntelligentDialerTab.closeTab(caiCallerDriver);
		userIntelligentDialerTab.switchToTab(caiCallerDriver, 2);
		driverUsed.put("caiCallerDriver", false);
	}
}
