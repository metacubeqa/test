package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.CallScreenPage;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.SalesforceAccountPage;
import softphone.source.salesforce.SearchPage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.InboxPage;
import support.source.conversationAIReact.InboxPage.FilterType;
import support.source.conversationAIReact.InboxPage.MenuTriggerOptions;
import support.source.conversationAIReact.InboxPage.SelectNotifications;
import support.source.conversationAIReact.InboxPage.SortInput;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.conversationAIReact.SettingsPage;
import support.source.conversationAIReact.SettingsPage.CAINotificationsHeaders;
import support.source.conversationAIReact.SettingsPage.NotificationsFrequency;
import utility.HelperFunctions;

public class InboxCases extends SupportBase {
	Dashboard dashboard = new Dashboard();
	InboxPage inboxPage = new InboxPage();
	CallsTabReactPage callsTabReactPage = new CallsTabReactPage();
	ConversationDetailPage conversationDetailPage = new ConversationDetailPage();
	SearchPage sfSearchPage = new SearchPage();
	CallScreenPage callScreenPage = new CallScreenPage();
	ContactDetailPage contactDetailPage = new ContactDetailPage();
	SalesforceAccountPage sfAccountPage = new SalesforceAccountPage();
	SettingsPage settingsPage = new SettingsPage();
	LibraryReactPage libraryPage = new LibraryReactPage();
	
	private String userName;
	private String userNameAgent;
	private String userNameSupport;
	
	private static final String olddateFormat = "MM/dd/yyyy h:mm a";
	private static final String newdateFormat = "M/d/yyyy h:mma";

	@BeforeClass(groups = { "Regression", "Product Sanity" })
	public void beforeClass() {
		userName = CONFIG.getProperty("qa_cai_caller_name");
		userNameAgent = CONFIG.getProperty("qa_cai_verify_name");
		userNameSupport = CONFIG.getProperty("qa_cai_support_name");
	}
	
	@Test(groups = { "Regression" })
	public void cai_sort_inbox_notification() {
		System.out.println("Test case --cai_sort_inbox_notification-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SharedCalls.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SupervisorNotes.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue());
		
		inboxPage.selectSortFilter(caiCallerDriver, SortInput.AgentNameAtoZ.getValue());
		inboxPage.verifySortInputOnPages(caiCallerDriver, SortInput.AgentNameAtoZ);
		
		inboxPage.selectSortFilter(caiCallerDriver, SortInput.AgentNameZtoA.getValue());
		inboxPage.verifySortInputOnPages(caiCallerDriver, SortInput.AgentNameZtoA);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --cai_sort_inbox_notification-- passed ");
	}
	
	//Verify sort inbox notifications by Oldest to newest/Newest to oldest
	@Test(groups = { "Regression" })
	public void verify_sort_inbox_notifications_oldest_to_newest_vice_versa() {
		System.out.println("Test case --verify_sort_inbox_notifications_oldest_to_newest_vice_versa-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SharedCalls.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SupervisorNotes.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue());
		
		inboxPage.selectSortFilter(caiCallerDriver, SortInput.OldestToNewest.getValue());
		inboxPage.verifySortInputOnPages(caiCallerDriver, SortInput.OldestToNewest);
		
		inboxPage.selectSortFilter(caiCallerDriver, SortInput.NewestToOldest.getValue());
		inboxPage.verifySortInputOnPages(caiCallerDriver, SortInput.NewestToOldest);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_sort_inbox_notifications_oldest_to_newest_vice_versa-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_filter_notification_by_flagged_call_type() {
		System.out.println("Test case --verify_filter_notification_by_flagged_call_type-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue());
		assertTrue(inboxPage.verifyFlaggedCallsImageDropDown(caiCallerDriver));
		inboxPage.verifyFilterImages(caiCallerDriver, FilterType.FlaggedCalls, true);
		
		inboxPage.clearTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue());
		
		assertEquals(callsTabReactPage.getStartingPageNumber(caiCallerDriver), 1);
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);
		
		inboxPage.verifyDefaultFilters(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_filter_notification_by_annonation_type-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_filter_notification_by_annonation_type() {
		System.out.println("Test case --verify_filter_notification_by_annonation_type-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		assertTrue(inboxPage.verifyAnnotationImageDropDown(caiCallerDriver));
		inboxPage.verifyFilterImages(caiCallerDriver, FilterType.Annotation, true);
		
		inboxPage.clearTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		
		assertEquals(callsTabReactPage.getStartingPageNumber(caiCallerDriver), 1);
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);

		inboxPage.verifyDefaultFilters(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_filter_notification_by_annonation_type-- passed ");
	}

	//Verify user able to filter inbox notification by Saved Search type
	@Test(groups = { "Regression" })
	public void verify_user_filter_inbox_notifications_by_saved_search_type() {
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_saved_search_type-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SavedSearch.getValue());
		assertTrue(inboxPage.verifySaveSearchImageDropDown(caiCallerDriver));
		
		inboxPage.verifyFilterImages(caiCallerDriver, FilterType.SavedSearch, true);
		
		inboxPage.clearTypeFilter(caiCallerDriver, FilterType.SavedSearch.getValue());
		
		assertEquals(callsTabReactPage.getStartingPageNumber(caiCallerDriver), 1);
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);
		
		inboxPage.verifyDefaultFilters(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_saved_search_type-- passed ");
	}

	//Verify user able to filter inbox notification by Shared calls type
	@Test(groups = { "Regression" })
	public void verify_user_filter_inbox_notifications_by_shared_calls() {
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_shared_calls-- started ");

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.switchToTab(caiVerifyDriver, 2);
		dashboard.clickConversationAI(caiVerifyDriver);
		inboxPage.navigateToInboxTab(caiVerifyDriver);
		String totalPageText = inboxPage.getTotalPageText(caiVerifyDriver);

		inboxPage.selectTypeFilter(caiVerifyDriver, FilterType.SharedCalls.getValue());
		assertTrue(inboxPage.verifySharedCallImageDropDown(caiVerifyDriver));
		
		inboxPage.verifyFilterImages(caiVerifyDriver, FilterType.SharedCalls, true);
		
		inboxPage.clearTypeFilter(caiVerifyDriver, FilterType.SharedCalls.getValue());
	
		assertEquals(callsTabReactPage.getStartingPageNumber(caiVerifyDriver), 1);
		assertEquals(inboxPage.getTotalPageText(caiVerifyDriver), totalPageText);

		inboxPage.verifyDefaultFilters(caiVerifyDriver);
		
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_shared_calls-- passed ");
	}

	//Filter inbox notifications by Supervisor notes type 
	@Test(groups = { "Regression" })
	public void verify_user_filter_inbox_notifications_by_supervisor_notes() {
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_supervisor_notes-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SupervisorNotes.getValue());
		assertTrue(inboxPage.verifySuperVisorNotesImageDropDown(caiCallerDriver));
		
		inboxPage.verifyFilterImages(caiCallerDriver, FilterType.SupervisorNotes, true);
		
		inboxPage.clearTypeFilter(caiCallerDriver, FilterType.SupervisorNotes.getValue());
	
		assertEquals(callsTabReactPage.getStartingPageNumber(caiCallerDriver), 1);
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);

		inboxPage.verifyDefaultFilters(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_filter_inbox_notifications_by_supervisor_notes-- passed ");
	}
	
	//Verify remove selected User and Type filter by Clear button independently 
	@Test(groups = { "Regression" })
	public void verify_remove_selected_user_and_type_filter_by_clear_button() {
		System.out.println("Test case --verify_remove_selected_user_and_type_filter_by_clear_button-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectFilterByUser(caiCallerDriver, userName);
		inboxPage.verifyUserDisplayedInNotifications(caiCallerDriver, userName);
		
		inboxPage.clearInboxSection(caiCallerDriver);
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);
		assertFalse(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userName));
		assertFalse(inboxPage.verifyUserDisplayedWithoutEnteringText(caiCallerDriver, userName));
		
		//selecting filter by types
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SavedSearch.getValue());
		
		inboxPage.clearInboxSection(caiCallerDriver);
		assertFalse(inboxPage.isFilterTypesSelectedVisible(caiCallerDriver, FilterType.Annotation.getValue()));
		assertFalse(inboxPage.isFilterTypesSelectedVisible(caiCallerDriver, FilterType.SavedSearch.getValue()));

		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_remove_selected_user_and_type_filter_by_clear_button-- passed ");
	}

	//Verify filter inbox notifications by selected users 
	@Test(groups = { "Regression" })
	public void verify_filter_inbox_notifications_by_selected_user() {
		System.out.println("Test case --verify_filter_inbox_notifications_by_selected_user-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		inboxPage.selectFilterByUser(caiCallerDriver, userName);
		inboxPage.verifyUserDisplayedInNotifications(caiCallerDriver, userName);
		
		assertFalse(inboxPage.verifyUserExistsInFilterUserDropDown(caiCallerDriver, userName));
		inboxPage.selectFilterByUser(caiCallerDriver, userNameAgent);
		assertTrue(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userName));
		
		//removing first user
		inboxPage.deleteSelectedFilterUser(caiCallerDriver, userName);
		assertFalse(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userName));
		assertTrue(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userNameAgent));

		//removing second user
		inboxPage.deleteSelectedFilterUser(caiCallerDriver, userNameAgent);
		assertFalse(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userName));
		assertFalse(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userNameAgent));
		
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_filter_inbox_notifications_by_selected_user-- passed ");
	}

	//Verify user remove Type filter after deselect from dropdown 
	@Test(groups = { "Regression" })
	public void verify_user_remove_type_filter_after_deselect_from_dropdown() {
		System.out.println("Test case --verify_user_remove_type_filter_after_deselect_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		String totalPageText = inboxPage.getTotalPageText(caiCallerDriver);
		
		assertTrue(inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue()));
		assertTrue(inboxPage.verifyAnnotationImageDropDown(caiCallerDriver));
		assertTrue(inboxPage.verifyFilterTypeImageListVisible(caiCallerDriver, FilterType.Annotation));
		
		assertTrue(inboxPage.selectTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue()));
		assertTrue(inboxPage.verifyFilterByTypeSelectedInDropDown(caiCallerDriver, FilterType.Annotation.getValue()));
		assertTrue(inboxPage.verifyFilterByTypeSelectedInDropDown(caiCallerDriver, FilterType.FlaggedCalls.getValue()));
		
		assertFalse(inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue()));
		assertFalse(inboxPage.selectTypeFilter(caiCallerDriver, FilterType.FlaggedCalls.getValue()));
		
		assertEquals(inboxPage.getTotalPageText(caiCallerDriver), totalPageText);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_remove_type_filter_after_deselect_from_dropdown-- passed ");
	}	
	
	//Verify click to call and SMS from Inbox notification after expand 
	@Test(groups = { "Regression" })
	public void verify_click_to_call_from_inbox_notification_after_expand() {
		System.out.println("Test case --verify_click_to_call_from_inbox_notification_after_expand-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		String parentWindow = caiCallerDriver.getWindowHandle();

		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		inboxPage.verifyDialExensionVisible(caiCallerDriver);
		
		String name = inboxPage.getProspectDetailsName(caiCallerDriver);
		String number = inboxPage.getProspectDetailsNumber(caiCallerDriver);
		
		inboxPage.clickProspectDetailsNumber(caiCallerDriver);
		
		// Switch to extension
		sfSearchPage.switchToExtension(caiCallerDriver);

		// Verify contact details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(caiCallerDriver), name);
		assertEquals(callScreenPage.getCallerNumberOriginalFormat(caiCallerDriver), number);

		// Closing salesforce task page window
		caiCallerDriver.close();
		sfSearchPage.switchToTabWindow(caiCallerDriver, parentWindow);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_click_to_call_from_inbox_notification_after_expand-- passed ");
	}	
	
	// Verify Tags and library upon expand inbox notifications
//	@Test(groups = { "Regression" })
	public void verify_tags_and_library_upon_expand() {
		System.out.println("Test case --verify_tags_and_library_upon_expand-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);

		inboxPage.verifyTagsVisible(caiCallerDriver);
		inboxPage.verifyLibrariesVisible(caiCallerDriver);
		
		String library = inboxPage.clickLibraryInbox(caiCallerDriver);
		LibraryReactPage libraryPage = new LibraryReactPage();
		assertTrue(libraryPage.isLibrarySelectedAndHighlighted(caiCallerDriver, library));
		
		callsTabReactPage.navigateToCallsPage(caiCallerDriver);
		libraryPage.openLibraryPage(caiCallerDriver);
		libraryPage.verifyFirstLibrarySelectedDefaultHighlighted(caiCallerDriver, library);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_tags_and_library_upon_expand-- passed ");
	}	
		
	//Verify prospect / account cloud icon gets moved user to related sfdc page 
	@Test(groups = { "Regression" })
	public void verify_prospect_name_account_cloud_details() {
		System.out.println("Test case --verify_prospect_name_account_cloud_details-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
	
		inboxPage.verifyContactCloudIcon(caiCallerDriver);
		inboxPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		inboxPage.waitForPageLoaded(caiCallerDriver);
		contactDetailPage.verifyContactPageOpen(caiCallerDriver);
		inboxPage.closeTab(caiCallerDriver);
		inboxPage.switchToTab(caiCallerDriver, 2);
		
		inboxPage.verifyAccountCloudIcon(caiCallerDriver);
		inboxPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		inboxPage.waitForPageLoaded(caiCallerDriver);
		sfAccountPage.verifyAccountPageOpen(caiCallerDriver);
		inboxPage.closeTab(caiCallerDriver);
		inboxPage.switchToTab(caiCallerDriver, 2);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_prospect_name_account_cloud_details-- passed ");
	}	

	//Verify filter Inbox notifications by Type then sorted by Agent name 
	@Test(groups = { "Regression" })
	public void verify_filter_notifications_by_type_then_sort_by_agent() {
		System.out.println("Test case --verify_filter_notifications_by_type_then_sort_by_agent-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		assertTrue(inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue()));
		assertTrue(inboxPage.verifyAnnotationImageDropDown(caiCallerDriver));
		assertTrue(inboxPage.verifyFilterTypeImageListVisible(caiCallerDriver, FilterType.Annotation));
	
		inboxPage.selectSortFilter(caiCallerDriver, SortInput.AgentNameAtoZ.getValue());
		inboxPage.verifyNotificationTextListAscending(caiCallerDriver);
		assertTrue(inboxPage.verifyAllNotificationsContainsText(caiCallerDriver, "annotated a call"));
		assertTrue(inboxPage.verifyFilterTypeImageListVisible(caiCallerDriver, FilterType.Annotation));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_filter_notifications_by_type_then_sort_by_agent-- passed ");
	}

	//Verify selected participants users should not be removed when we removed search string on Inbox screen
	@Test(groups = { "Regression" })
	public void verify_selected_users_not_removed_when_removing_search_string() {
		System.out.println("Test case --verify_selected_users_not_removed_when_removing_search_string-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		inboxPage.selectFilterByUser(caiCallerDriver, userName);
		inboxPage.selectFilterByUser(caiCallerDriver, userNameAgent);

		assertTrue(inboxPage.verifyPartialUserNameContainsInFilterUserDropDown(caiCallerDriver, "Vish"));
		inboxPage.clearUserNameFilter(caiCallerDriver);
		
		assertTrue(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userName));
		assertTrue(inboxPage.isFilterByUserSelectedVisible(caiCallerDriver, userNameAgent));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_selected_users_not_removed_when_removing_search_string-- passed ");
	}
	
	// Selected user should not be searchable again on inbox screen 
	@Test(groups = { "Regression" })
	public void verify_selected_users_not_searchable_again() {
		System.out.println("Test case --verify_selected_users_not_searchable_again-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		inboxPage.selectFilterByUser(caiCallerDriver, userName);

		assertFalse(inboxPage.verifyUserExistsInFilterUserDropDown(caiCallerDriver, userName));
		inboxPage.clearUserNameFilter(caiCallerDriver);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_selected_users_not_searchable_again-- passed ");
	}
	
	//Inbox tab -Notification displayed when shared-> View conversation
	//Verify share video call recordings single user
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_inbox_tab_notification_displayed_when_shared() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_shared-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.clickConversationAI(caiVerifyDriver);
		inboxPage.navigateToInboxTab(caiVerifyDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiVerifyDriver);
		
		dashboard.clickConversationAI(caiCallerDriver);
		
		// filter for my calls agent
		String filterValue = "My Calls";
		String agentToShare = userNameAgent;
		String agentCaller = userName;
		callsTabReactPage.selectAgentNameFromFilter(caiCallerDriver, filterValue);
		callsTabReactPage.setConversationAIFilters(caiCallerDriver, CallDataFilterType.VideoCalls, 0, null);

		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiCallerDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiCallerDriver);
		String accountName = callsTabReactPage.getAccountNameFromDurationSubjectList(caiCallerDriver);
		String duration = callsTabReactPage.getCallDurationFromSearchList(caiCallerDriver);
		
		// now share the recently created conversation AI with a user
		callsTabReactPage.openConversationDetails(caiCallerDriver, 0);
		String createdTime = HelperFunctions.GetCurrentDateTime("h:mm a");
		createdTime = createdTime.replace("am", "AM").replace("pm", "PM");
		String createdTime1 = HelperFunctions.GetCurrentDateTime("h:mm a", true);
		createdTime1= createdTime1.replace("am", "AM").replace("pm", "PM");
		conversationDetailPage.shareCall(caiCallerDriver, agentToShare, "This call is shared for testing purpose");			
		
		int inboxCountAfter = inboxPage.getInboxCount(caiVerifyDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		
		String actualTime = inboxPage.getDateInboxList(caiVerifyDriver, 0);
		assertTrue(actualTime.equals(createdTime)|| actualTime.equals(createdTime1));
		
		assertEquals(inboxPage.getNotificationTextInbox(caiVerifyDriver, 0), agentCaller+" shared a call between "+receiverName+" and "+callerName);
		inboxPage.verifySharedCallUnReadImage(caiVerifyDriver, 0);
		
		inboxPage.clickFirstNotificationTextList(caiVerifyDriver);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiVerifyDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");

		assertEquals(inboxPage.getProspectDetailsName(caiVerifyDriver), receiverName);
		assertEquals(inboxPage.getProspectDetailsAccount(caiVerifyDriver), accountName);
		assertEquals(inboxPage.getAgentDetailsName(caiVerifyDriver), callerName);
		assertTrue(inboxPage.getAgentDetailsDuration(caiVerifyDriver).contains(duration));
		
		callsTabReactPage.verifyToolTipViewConversation(caiVerifyDriver, 0);
		callsTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		
		assertEquals(inboxPage.getInboxCount(caiVerifyDriver), inboxCountBefore);
		assertEquals(conversationDetailPage.getCallsTabPageHeading(caiVerifyDriver), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);

		inboxPage.navigateToInboxTab(caiVerifyDriver);
		inboxPage.verifySharedCallImageRead(caiVerifyDriver, 0);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_shared-- passed ");
	}	

	//Inbox Tab-> Notification displayed when new CAI call match to my Immediate saved search->view Notification 
//	@Test(groups = { "Regression" })
	public void verify_inbox_tab_notification_displayed_when_new_cai_call_match() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_new_cai_call_match-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		String parentWindow = caiCallerDriver.getWindowHandle();

		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		inboxPage.verifyDialExensionVisible(caiCallerDriver);
		
		String name = inboxPage.getProspectDetailsName(caiCallerDriver);
		String number = inboxPage.getProspectDetailsNumber(caiCallerDriver);
		
		inboxPage.clickProspectDetailsNumber(caiCallerDriver);
		
		// Switch to extension
		sfSearchPage.switchToExtension(caiCallerDriver);

		// Verify contact details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(caiCallerDriver), name);
		assertEquals(callScreenPage.getCallerNumberOriginalFormat(caiCallerDriver), number);
		
		// Closing salesforce task page window
		caiCallerDriver.close();
		sfSearchPage.switchToTabWindow(caiCallerDriver, parentWindow);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_new_cai_call_match-- passed ");
	}
	
	//Inbox tab -Notification displayed when Supervisor notes on my Calls->View conversation
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_inbox_tab_notification_displayed_when_supervisor_notes_on_my_calls() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_supervisor_notes_on_my_calls-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
	
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		
		dashboard.clickConversationAI(caiVerifyDriver);
		
		// filter for my calls agent
		String agentToFilter = userName;
		String shareCaller = userNameAgent;
		callsTabReactPage.selectAgentNameFromFilter(caiVerifyDriver, agentToFilter);
		
		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiVerifyDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiVerifyDriver);
		String accountName = callsTabReactPage.getAccountNameFromDurationSubjectList(caiVerifyDriver);
		String duration = callsTabReactPage.getCallDurationFromSearchList(caiVerifyDriver);
		
		// now share the recently created conversation AI with a user
		String notes = "AutoSupervisorNotes".concat(HelperFunctions.GetRandomString(4));
		callsTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		conversationDetailPage.addOrEditSuperVisorNotes(caiVerifyDriver, notes);
		conversationDetailPage.verifySuperVisorNotesSaved(caiVerifyDriver, notes);
		
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		assertEquals(inboxPage.getNotificationTextInbox(caiCallerDriver, 0), shareCaller+" added a supervisor note to a call between "+receiverName+" and "+callerName);
		inboxPage.verifySupervisorNotesCallUnReadImage(caiCallerDriver, 0);
		
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiCallerDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;

		assertEquals(inboxPage.getProspectDetailsName(caiCallerDriver), receiverName);
		assertEquals(inboxPage.getProspectDetailsAccount(caiCallerDriver), accountName);
		assertEquals(inboxPage.getAgentDetailsName(caiCallerDriver), callerName);
		assertTrue(inboxPage.getAgentDetailsDuration(caiCallerDriver).contains(duration));
		inboxPage.verifySuperVisorNotesBtnHoverText_Enabled(caiCallerDriver);
		
		callsTabReactPage.verifyToolTipViewConversation(caiCallerDriver, 0);
		
		callsTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		assertEquals(inboxPage.getInboxCount(caiCallerDriver), inboxCountBefore);
		assertEquals(conversationDetailPage.getCallsTabPageHeading(caiCallerDriver), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);
		conversationDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, notes);
		
		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore);
		inboxPage.verifySuperVisorNotesCallImageRead(caiCallerDriver, 0);

		//deleting individually
		// unreading notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		inboxPage.selectUnreadCheckBoxAccToIndex(caiCallerDriver, 0);
		inboxPage.cancelDeleteInboxNotification(caiCallerDriver);

		assertTrue(inboxPage.isUnreadCheckBoxSelected(caiCallerDriver, 0));
		inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore);
		
		//getting date inbox before
		String dateInboxBefore = inboxPage.getDateInboxList(caiCallerDriver, 0);

		inboxPage.deleteInboxNotification(caiCallerDriver, SelectNotifications.Single);

		inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - 1);
		assertFalse(inboxPage.getDateInboxList(caiCallerDriver, 0).equals(dateInboxBefore));
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_supervisor_notes_on_my_calls-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_expand_notifications_call_notes_supervisor_notes_icon() {
		System.out.println("Test case --verify_expand_notifications_call_notes_supervisor_notes_icon-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		
		// filter for my calls agent
		callsTabReactPage.setConversationAIFilters(caiCallerDriver, CallDataFilterType.Agent, 0, null);
		
		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiCallerDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiCallerDriver);
		
		// add call notes
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(4));
		callsTabReactPage.openConversationDetails(caiCallerDriver, 0);
		conversationDetailPage.addOrEditCallNotes(caiCallerDriver, callNotes);
		conversationDetailPage.verifyCallNotesSaved(caiCallerDriver, callNotes);
	
		// add supervisor notes
		String superVisorNotes = "AutoSupervisorNotes".concat(HelperFunctions.GetRandomString(4));
		conversationDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, superVisorNotes);
		conversationDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, superVisorNotes);

		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxPage.idleWait(1);
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		assertEquals(inboxPage.getNotificationTextInbox(caiCallerDriver, 0), callerName+" added a supervisor note to a call between "+receiverName+" and "+callerName);
		inboxPage.verifySupervisorNotesCallUnReadImage(caiCallerDriver, 0);
		
		//verify call notes btn features
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		inboxPage.verifyCallNotesBtnHoverText_Enabled(caiCallerDriver);
		inboxPage.clickCallNotesBtn(caiCallerDriver);

		conversationDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		assertFalse(conversationDetailPage.isScrollPresentAtTopOfPage(caiCallerDriver));
		int currentScrollPosition =  conversationDetailPage.getScrollPositionOfCurrentPage(caiCallerDriver);
		assertTrue(HelperFunctions.verifyIntegerInGivenRange(1100, 1600, currentScrollPosition));

		conversationDetailPage.verifyRecordingPlayButtonVisible(caiCallerDriver);
		conversationDetailPage.verifyCallNotesSaved(caiCallerDriver, callNotes);

		//verify supervisor notes btn features
		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		inboxPage.verifySuperVisorNotesBtnHoverText_Enabled(caiCallerDriver);
		inboxPage.clickSuperVisorNotesBtn(caiCallerDriver);
		
		conversationDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		assertFalse(conversationDetailPage.isScrollPresentAtTopOfPage(caiCallerDriver));
		currentScrollPosition =  conversationDetailPage.getScrollPositionOfCurrentPage(caiCallerDriver);
		assertTrue(HelperFunctions.verifyIntegerInGivenRange(1300, 1800, currentScrollPosition));
		
		conversationDetailPage.verifyRecordingPlayButtonVisible(caiCallerDriver);
		conversationDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, superVisorNotes);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_expand_notifications_call_notes_supervisor_notes_icon-- passed ");
	}
	
	//Inbox tab -Notification displayed when shared-> View conversation
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_inbox_tab_notification_displayed_when_annoated_on_my_calls() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_annoated_on_my_calls-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		
		dashboard.clickConversationAI(caiVerifyDriver);
		
		// filter for my calls agent
		String agentToFilter = userName;
		String agentCaller = userNameAgent;
		callsTabReactPage.selectAgentNameFromFilter(caiVerifyDriver, agentToFilter);
		
		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiVerifyDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiVerifyDriver);
		String accountName = callsTabReactPage.getAccountNameFromDurationSubjectList(caiVerifyDriver);
		String duration = callsTabReactPage.getCallDurationFromSearchList(caiVerifyDriver);
		
		// now share the recently created conversation AI with a user
		String annotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(4));
		String topic = "This is a test";
		callsTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		String createdTime = HelperFunctions.GetCurrentDateTime("h:mm a");
		String createdTime1 = HelperFunctions.GetCurrentDateTime("h:mm a", true);
		conversationDetailPage.enterAnnotation(caiVerifyDriver, annotationName);			
		conversationDetailPage.verifyAnnotationTopicWithThumbsUp(caiVerifyDriver, annotationName, topic);
		String annotationCountOnTab = conversationDetailPage.getAnnotationCountOnTab(caiVerifyDriver);
		
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		
		String actualTime = inboxPage.getDateInboxList(caiCallerDriver, 0);
		assertTrue(actualTime.equals(createdTime)|| actualTime.equals(createdTime1));
		
		assertEquals(inboxPage.getNotificationTextInbox(caiCallerDriver, 0), agentCaller+" annotated a call between "+receiverName+" and "+callerName);
		inboxPage.verifyAnnotationUnReadImage(caiCallerDriver, 0);
		
		inboxPage.clickFirstNotificationTextList(caiCallerDriver);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiCallerDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;

		assertEquals(inboxPage.getProspectDetailsName(caiCallerDriver), receiverName);
		assertEquals(inboxPage.getProspectDetailsAccount(caiCallerDriver), accountName);
		assertEquals(inboxPage.getAgentDetailsName(caiCallerDriver), callerName);
		assertEquals(inboxPage.getAgentDetailsDuration(caiCallerDriver), duration);
		
		//verify annotation btn features
		inboxPage.verifyAnnotationBtnHoverText_Enabled(caiCallerDriver);
		assertEquals(inboxPage.getAnnotationSize(caiCallerDriver, annotationName), annotationCountOnTab);
		inboxPage.clickAnnotationsBtn(caiCallerDriver);

		conversationDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		assertFalse(conversationDetailPage.isScrollPresentAtTopOfPage(caiCallerDriver));
		int currentScrollPosition =  conversationDetailPage.getScrollPositionOfCurrentPage(caiCallerDriver);
		assertTrue(HelperFunctions.verifyIntegerInGivenRange(500, 700, currentScrollPosition));

		inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - 1);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_annoated_on_my_calls-- passed ");
	}
	
	//Inbox tab -Flag Notification displayed when someone send for Review->View conversation
	@Test(groups = { "Regression" })
	public void verify_inbox_tab_notification_displayed_when_send_for_review() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_send_for_review-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashboard.clickConversationAI(caiSupportDriver);
		inboxPage.navigateToInboxTab(caiSupportDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiSupportDriver);
		
		dashboard.clickConversationAI(caiCallerDriver);
		
		// filter for my calls agent
		String filterValue = "My Calls";
		String agentToReview = userNameSupport;
		callsTabReactPage.selectAgentNameFromFilter(caiCallerDriver, filterValue);
		
		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiCallerDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiCallerDriver);
		String accountName = callsTabReactPage.getAccountNameFromDurationSubjectList(caiCallerDriver);
		String duration = callsTabReactPage.getCallDurationFromSearchList(caiCallerDriver);
	
		// now share the recently created conversation AI with a user
		callsTabReactPage.openConversationDetails(caiCallerDriver, 0);
		String createdTime = HelperFunctions.GetCurrentDateTime("h:mm a");
		String createdTime1 = HelperFunctions.GetCurrentDateTime("h:mm a", true);
		conversationDetailPage.addReviewers(caiCallerDriver, agentToReview);			
		
		inboxPage.navigateToInboxTab(caiSupportDriver);
		int inboxCountAfter = inboxPage.getInboxCount(caiSupportDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		
		String actualTime = inboxPage.getDateInboxList(caiSupportDriver, 0);
		assertTrue(actualTime.equals(createdTime)|| actualTime.equals(createdTime1));
		
		System.out.println(inboxPage.getNotificationTextInbox(caiSupportDriver, 0));
		assertEquals(inboxPage.getNotificationTextInbox(caiSupportDriver, 0), callerName+" flagged a call between "+receiverName+" and "+callerName);
		inboxPage.verifyFlaggedCallUnReadImage(caiSupportDriver, 0);
		
		inboxPage.clickFirstNotificationTextList(caiSupportDriver);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiSupportDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;

		assertEquals(inboxPage.getProspectDetailsName(caiSupportDriver), receiverName);
		assertEquals(inboxPage.getProspectDetailsAccount(caiSupportDriver), accountName);
		assertEquals(inboxPage.getAgentDetailsName(caiSupportDriver), callerName);
		assertTrue(inboxPage.getAgentDetailsDuration(caiSupportDriver).contains(duration));
		
		callsTabReactPage.verifyToolTipViewConversation(caiSupportDriver, 0);
		callsTabReactPage.openConversationDetails(caiSupportDriver, 0);
		assertTrue(conversationDetailPage.isMarkAsReviewedIconVisible(caiSupportDriver));
		
		assertEquals(inboxPage.getInboxCount(caiSupportDriver), inboxCountBefore);
		assertEquals(conversationDetailPage.getCallsTabPageHeading(caiCallerDriver), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);

		inboxPage.navigateToInboxTab(caiSupportDriver);
		inboxPage.verifyFlaggedCallImageRead(caiSupportDriver, 0);
		inboxCountAfter = inboxPage.getInboxCount(caiSupportDriver);
		assertEquals(inboxCountAfter, inboxCountBefore);

		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_send_for_review-- passed ");
	}
	
	//Verify delete all inbox notifications of a page by hitting the checkbox 
	@Test(groups = { "Regression" })
	public void verify_delete_all_notifications_selecting_checkbox() {
		System.out.println("Test case --verify_delete_all_notifications_selecting_checkbox-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		//unreading notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);
		int totalPageBefore = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.deleteInboxNotification(caiCallerDriver, SelectNotifications.Multiple);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		int totalPageAfter = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);
		assertEquals(totalPageAfter, totalPageBefore - 1);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_delete_all_notifications_selecting_checkbox-- passed ");
	}

	//Verify delete inbox notifications after selecting “all” from the dropdown 
	@Test(groups = { "Regression" })
	public void verify_delete_all_notifications_selecting_all_option_from_dropdown() {
		System.out.println("Test case --verify_delete_all_notifications_selecting_all_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		//unreading notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);
		int totalPageBefore = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.All);
		inboxPage.deleteInboxNotification(caiCallerDriver, SelectNotifications.Multiple);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		int totalPageAfter = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);
		assertEquals(totalPageAfter, totalPageBefore - 1);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_delete_all_notifications_selecting_all_option_from_dropdown-- passed ");
	}
	
	//Verify delete inbox notifications after selecting “unread” from the dropdown 
	@Test(groups = { "Regression" })
	public void verify_delete_notifications_selecting_unread_option_from_dropdown() {
		System.out.println("Test case --verify_delete_notifications_selecting_unread_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		//unreading notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);
		
		inboxPage.clickNotificationCheckBoxAccToIndex(caiCallerDriver, 0);
		inboxPage.markAsReadNotification(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);
		int totalPageBefore = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.Unread);
		assertTrue(inboxPage.isMinusSignMenuTriggerVisible(caiCallerDriver));
		assertTrue(inboxPage.isOnlyUnreadNotificationSelected(caiCallerDriver));
		assertFalse(inboxPage.isOnlyReadNotificationSelected(caiCallerDriver));
		
		inboxPage.deleteInboxNotification(caiCallerDriver, SelectNotifications.Multiple);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		int totalPageAfter = callsTabReactPage.getTotalPage(caiCallerDriver);
		
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);
		assertEquals(totalPageAfter, totalPageBefore - 1);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_delete_notifications_selecting_unread_option_from_dropdown-- passed ");
	}

	//Verify delete inbox notifications after selecting “read” from the dropdown 
	@Test(groups = { "Regression" })
	public void verify_delete_notifications_selecting_read_option_from_dropdown() {
		System.out.println("Test case --verify_delete_notifications_selecting_read_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		//unreading notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		
		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.Read);
		assertTrue(inboxPage.isOnlyReadNotificationSelected(caiCallerDriver));
		assertFalse(inboxPage.isOnlyUnreadNotificationSelected(caiCallerDriver));
		
		inboxPage.deleteInboxNotification(caiCallerDriver, SelectNotifications.Multiple);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		
		assertEquals(inboxCountAfter, inboxCountBefore);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_delete_notifications_selecting_read_option_from_dropdown-- passed ");
	}

	//Verify 'Mark as unread' inbox notification after select individually  
	@Test(groups = { "Regression" })
	public void verify_mark_as_unread_notifications_select_individually() {
		System.out.println("Test case --verify_mark_as_unread_notifications_select_individually-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		int index = 0;
		callsTabReactPage.openConversationDetails(caiCallerDriver, index);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxPage.verifyNotificationMarkedReadAccToIndex(caiCallerDriver, index);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		inboxPage.selectReadCheckBoxAccToIndex(caiCallerDriver, index);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		
		inboxPage.verifyNotificationMarkedUnReadAccToIndex(caiCallerDriver, index);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_notifications_select_individually-- passed ");
	}
	
	// Verify 'Mark as unread' all inbox notifications of a page by hitting the checkbox
	@Test(groups = { "Regression" })
	public void verify_mark_as_unread_all_notifications_selecting_checkbox() {
		System.out.println("Test case --verify_mark_as_unread_all_notifications_selecting_checkbox-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as read notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int readImageCount = inboxPage.getReadImageListCount(caiCallerDriver);

		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		assertTrue(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertTrue(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + readImageCount);

		inboxPage.verifyAllNotificationsMarkedUnRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_all_notifications_selecting_checkbox-- passed ");
	}

	// Verify 'Mark as unread' inbox notifications after selecting “all” from the dropdown
	@Test(groups = { "Regression" })
	public void verify_mark_as_unread_all_notifications_selecting_all_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_unread_all_notifications_selecting_all_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as read notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int readImageCount = inboxPage.getReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.All);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + readImageCount);
		
		inboxPage.verifyAllNotificationsMarkedUnRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_all_notifications_selecting_all_option_from_dropdown-- passed ");
	}
	
	// Verify 'Mark as unread' inbox notifications after selecting “all” from the dropdown
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_mark_as_unread_notifications_selecting_read_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_unread_notifications_selecting_read_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as read notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int readImageCount = inboxPage.getReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.Read);
		assertTrue(inboxPage.isOnlyReadNotificationSelected(caiCallerDriver));
		assertFalse(inboxPage.isOnlyUnreadNotificationSelected(caiCallerDriver));
		
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + readImageCount);
		
		inboxPage.verifyAllNotificationsMarkedUnRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_notifications_selecting_read_option_from_dropdown-- passed ");
	}
	
	// Verify 'Mark as unread' inbox notifications after selecting “all” from the dropdown
	@Test(groups = { "Regression" })
	public void verify_mark_as_unread_after_selecting_multiple_notifications() {
		System.out.println("Test case --verify_mark_as_unread_after_selecting_multiple_notifications-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as read notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);

		inboxPage.clickNotificationCheckBoxAccToIndex(caiCallerDriver, 0);
		inboxPage.clickNotificationCheckBoxAccToIndex(caiCallerDriver, 1);
		inboxPage.verifyBulkManagementBtnsVisible(caiCallerDriver);

		inboxPage.markAsUnreadNotification(caiCallerDriver);
		
		inboxPage.verifyNotificationMarkedUnReadAccToIndex(caiCallerDriver, 0);
		inboxPage.verifyNotificationMarkedUnReadAccToIndex(caiCallerDriver, 1);
		
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 2);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_after_selecting_multiple_notifications-- passed ");
	}

	// Verify 'Mark as Unread' notifications after moving on next pages
	@Test(groups = { "Regression" })
	public void verify_mark_as_unread_all_on_second_page_selecting_all_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_unread_all_on_second_page_selecting_all_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		callsTabReactPage.navigateToNextCAIPage(caiCallerDriver, 2, 0);

		//mark as read notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		
		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int readImageCount = inboxPage.getReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.All);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + readImageCount);

		inboxPage.verifyAllNotificationsMarkedUnRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_unread_all_on_second_page_selecting_all_option_from_dropdown-- passed ");
	}

	//Verify 'Mark as read' inbox notification after select individually  
	@Test(groups = { "Regression" })
	public void verify_mark_as_read_notifications_select_individually() {
		System.out.println("Test case --verify_mark_as_read_notifications_select_individually-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		
		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		inboxPage.selectUnreadCheckBoxAccToIndex(caiCallerDriver, 0);
		inboxPage.markAsReadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - 1);
		
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_read_notifications_select_individually-- passed ");
	}
	
	//Verify 'Mark as read' all inbox notifications of a page by hitting the checkbox. 
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_mark_as_read_all_notifications_selecting_checkbox() {
		System.out.println("Test case --verify_mark_as_read_all_notifications_selecting_checkbox-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);

		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);
		
		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_read_all_notifications_selecting_checkbox-- passed ");
	}

	// Verify 'Mark as read' inbox notifications after selecting “all” from the dropdown 
	@Test(groups = { "Regression" })
	public void verify_mark_as_read_all_notifications_selecting_all_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_read_all_notifications_selecting_all_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.All);
		inboxPage.markAsReadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);

		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_read_all_notifications_selecting_all_option_from_dropdown-- passed ");
	}
	
	// Verify 'Mark as read' inbox notifications after selecting “all” from the
	// dropdown
	@Test(groups = { "Regression" })
	public void verify_mark_as_read_notifications_selecting_unread_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_read_notifications_selecting_unread_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		// mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.Unread);
		assertTrue(inboxPage.isOnlyUnreadNotificationSelected(caiCallerDriver));
		assertFalse(inboxPage.isOnlyReadNotificationSelected(caiCallerDriver));
	
		inboxPage.markAsReadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);

		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println(
				"Test case --verify_mark_as_read_notifications_selecting_unread_option_from_dropdown-- passed ");
	}

	// Verify 'Mark as read' inbox notifications after selecting “all” from the dropdown 
	@Test(groups = { "Regression" })
	public void verify_mark_as_read_notifications_after_applying_filters() {
		System.out.println("Test case --verify_mark_as_read_notifications_after_applying_filters-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.Annotation.getValue());
		assertTrue(inboxPage.verifyAnnotationImageDropDown(caiCallerDriver));

		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.Unread);
		assertTrue(inboxPage.isOnlyUnreadNotificationSelected(caiCallerDriver));
		assertFalse(inboxPage.isOnlyReadNotificationSelected(caiCallerDriver));

		inboxPage.markAsReadNotification(caiCallerDriver);
		assertTrue(inboxPage.isFilterTypesSelectedVisible(caiCallerDriver, FilterType.Annotation.getValue()));
		
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);

		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));
		
		inboxPage.clearInboxSection(caiCallerDriver);
		assertFalse(inboxPage.isFilterTypesSelectedVisible(caiCallerDriver, FilterType.Annotation.getValue()));
		
		//selecting supervisor notes
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SupervisorNotes.getValue());
		assertTrue(inboxPage.verifyFilterTypeImageListVisible(caiCallerDriver, FilterType.SupervisorNotes));
		
		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsReadNotification(caiCallerDriver);
		
		inboxPage.verifyAllSuperVisorNotesCallImageRead(caiCallerDriver);
		
		// selecting shared calls
		inboxPage.clearInboxSection(caiCallerDriver);
		inboxPage.selectTypeFilter(caiCallerDriver, FilterType.SavedSearch.getValue());
		assertTrue(inboxPage.verifyFilterTypeImageListVisible(caiCallerDriver, FilterType.SavedSearch));

		// mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		inboxPage.clickNotificationCheckBoxAccToIndex(caiCallerDriver, 0);
		inboxPage.clickNotificationCheckBoxAccToIndex(caiCallerDriver, 1);
		inboxPage.verifyBulkManagementBtnsVisible(caiCallerDriver);

		inboxPage.markAsReadNotification(caiCallerDriver);
		
		inboxPage.verifyNotificationMarkedReadAccToIndex(caiCallerDriver, 0);
		inboxPage.verifyNotificationMarkedReadAccToIndex(caiCallerDriver, 1);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_read_notifications_after_applying_filters-- passed ");
	}
	
	// Verify 'Mark as read' notifications after moving on next pages
	@Test(groups = { "Regression" })
	public void verify_mark_as_read_all_on_second_page_selecting_all_option_from_dropdown() {
		System.out.println("Test case --verify_mark_as_read_all_on_second_page_selecting_all_option_from_dropdown-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		inboxPage.navigateToInboxTab(caiCallerDriver);

		callsTabReactPage.navigateToNextCAIPage(caiCallerDriver, 2, 0);
		
		//mark as unread notifications before starting
		inboxPage.clickMenuTriggerCheckBox(caiCallerDriver);
		inboxPage.markAsUnreadNotification(caiCallerDriver);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		int unReadImageCount = inboxPage.getUnReadImageListCount(caiCallerDriver);

		inboxPage.selectMenuTriggerOption(caiCallerDriver, MenuTriggerOptions.All);
		inboxPage.markAsReadNotification(caiCallerDriver);

		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxCountAfter, inboxCountBefore - unReadImageCount);

		inboxPage.verifyAllNotificationsMarkedRead(caiCallerDriver);
		assertFalse(inboxPage.isMenuTriggerCheckBoxSelected(caiCallerDriver));
		assertFalse(inboxPage.isNotificationCheckBoxesSelected(caiCallerDriver));

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_mark_as_read_all_on_second_page_selecting_all_option_from_dropdown-- passed ");
	}
	
	//Admin User: Access Notification from User dropdown- Can edit both inbox and Email Notifications
	@Test(groups = { "MediumPriority" })
	public void verify_admin_user_can_edit_email_inapp_notifications() {
		
		System.out.println("Test case  --verify_admin_user_can_edit_email_inapp_notifications-- started ");
		
		String notificationType = "Missed Meeting";

		// admin driver
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAINotifications(caiCallerDriver);
		
		assertFalse(settingsPage.isInboxNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertFalse(settingsPage.isInboxNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.UserNotifications));
		
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.UserNotifications));
		
		//default being selected
		settingsPage.selectInAppCheckBox(caiCallerDriver, notificationType);
		settingsPage.selectEmailCheckBox(caiCallerDriver, notificationType);
		
		//unselecting in app
		settingsPage.unSelectInAppCheckBox(caiCallerDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
		assertFalse(settingsPage.isInAppNotificationChecked(caiCallerDriver, notificationType));
		
		//unselecting email
		settingsPage.unSelectEmailCheckBox(caiCallerDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
		assertFalse(settingsPage.isEmailNotificationChecked(caiCallerDriver, notificationType));
		
		//selecting in app
		settingsPage.selectInAppCheckBox(caiCallerDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
		assertTrue(settingsPage.isInAppNotificationChecked(caiCallerDriver, notificationType));
		
		//selecting email
		settingsPage.selectEmailCheckBox(caiCallerDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
		assertTrue(settingsPage.isEmailNotificationChecked(caiCallerDriver, notificationType));
		
		//check PublicSavedSearches notifications
		if(settingsPage.isFrequencyVisibleAccToTableHeader(caiCallerDriver, CAINotificationsHeaders.PublicSavedSearches, 0)) {
			String notificationName = settingsPage.getNotificationNameAccToTableHeader(caiCallerDriver, CAINotificationsHeaders.PublicSavedSearches, 0);
			settingsPage.selectFrequencyAccToTableHeader(caiCallerDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.None);
			
			//check inapp and email disabled
			assertTrue(settingsPage.isInAppNotificationDisabledAccToNotification(caiCallerDriver, notificationName));
			assertTrue(settingsPage.isEmailNotificationDisabledAccToNotification(caiCallerDriver, notificationName));
			
			//setting freq to Immediate
			settingsPage.selectFrequencyAccToTableHeader(caiCallerDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.Immediate);
			settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
			
			//check inapp and email not disabled
			assertFalse(settingsPage.isInAppNotificationDisabledAccToNotification(caiCallerDriver, notificationName));
			assertFalse(settingsPage.isEmailNotificationDisabledAccToNotification(caiCallerDriver, notificationName));
			
			//default being selected
			settingsPage.selectInAppCheckBox(caiCallerDriver, notificationName);
			settingsPage.selectEmailCheckBox(caiCallerDriver, notificationName);
			
			//unselecting in app
			settingsPage.unSelectInAppCheckBox(caiCallerDriver, notificationName);
			settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
			assertFalse(settingsPage.isInAppNotificationChecked(caiCallerDriver, notificationName));
			
			//unselecting email
			settingsPage.unSelectEmailCheckBox(caiCallerDriver, notificationName);
			settingsPage.verifyNotificationMsgSetting(caiCallerDriver);
			assertFalse(settingsPage.isEmailNotificationChecked(caiCallerDriver, notificationName));
		}
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case  --verify_admin_user_can_edit_email_inapp_notifications-- passed ");
	}

	//Support User- Both Inbox and Email notification should be update by Support role
	@Test(groups = { "MediumPriority" })
	public void verify_support_user_can_edit_email_inapp_notifications() {
		System.out.println("Test case  --verify_support_user_can_edit_email_inapp_notifications-- started ");
		
		String notificationType = "Missed Meeting";

		// support driver
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		dashboard.switchToTab(caiSupportDriver, 2);
		dashboard.navigateToCAINotifications(caiSupportDriver);
		
		assertFalse(settingsPage.isInboxNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertFalse(settingsPage.isInboxNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.UserNotifications));
		
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiCallerDriver, CAINotificationsHeaders.UserNotifications));

		//keeping default being checked
		settingsPage.selectInAppCheckBox(caiSupportDriver, notificationType);
		settingsPage.selectEmailCheckBox(caiSupportDriver, notificationType);
		
		//unselecting in app
		settingsPage.unSelectInAppCheckBox(caiSupportDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
		assertFalse(settingsPage.isInAppNotificationChecked(caiSupportDriver, notificationType));
		
		//unselecting email
		settingsPage.unSelectEmailCheckBox(caiSupportDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
		assertFalse(settingsPage.isEmailNotificationChecked(caiSupportDriver, notificationType));
		
		//selecting in app
		settingsPage.selectInAppCheckBox(caiSupportDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
		assertTrue(settingsPage.isInAppNotificationChecked(caiSupportDriver, notificationType));
		
		//selecting email
		settingsPage.selectEmailCheckBox(caiSupportDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
		assertTrue(settingsPage.isEmailNotificationChecked(caiSupportDriver, notificationType));
		
		//check PublicSavedSearches notifications
		if(settingsPage.isFrequencyVisibleAccToTableHeader(caiSupportDriver, CAINotificationsHeaders.PublicSavedSearches, 0)) {
			String notificationName = settingsPage.getNotificationNameAccToTableHeader(caiSupportDriver, CAINotificationsHeaders.PublicSavedSearches, 0);
			
			//setting freq to None
			settingsPage.selectFrequencyAccToTableHeader(caiSupportDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.None);
			
			//check inapp and email disabled
			assertTrue(settingsPage.isInAppNotificationDisabledAccToNotification(caiSupportDriver, notificationName));
			assertTrue(settingsPage.isEmailNotificationDisabledAccToNotification(caiSupportDriver, notificationName));
			
			//setting freq to Immediate
			settingsPage.selectFrequencyAccToTableHeader(caiSupportDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.Immediate);
			settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
			
			//check inapp and email not disabled
			assertFalse(settingsPage.isInAppNotificationDisabledAccToNotification(caiSupportDriver, notificationName));
			assertFalse(settingsPage.isEmailNotificationDisabledAccToNotification(caiSupportDriver, notificationName));
			
			//default being selected
			settingsPage.selectInAppCheckBox(caiSupportDriver, notificationName);
			dashboard.isPaceBarInvisible(caiSupportDriver);
			settingsPage.selectEmailCheckBox(caiSupportDriver, notificationName);
			dashboard.isPaceBarInvisible(caiSupportDriver);
			
			//unselecting inapp
			settingsPage.unSelectInAppCheckBox(caiSupportDriver, notificationName);
			settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
			assertFalse(settingsPage.isInAppNotificationChecked(caiSupportDriver, notificationName));
			
			//unselecting email
			settingsPage.unSelectEmailCheckBox(caiSupportDriver, notificationName);
			settingsPage.verifyNotificationMsgSetting(caiSupportDriver);
			assertFalse(settingsPage.isEmailNotificationChecked(caiSupportDriver, notificationName));
		}

		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case  --verify_support_user_can_edit_email_inapp_notifications-- passed ");
	}

	//Agent User: Inbox Notification disable but can update Email Notifications
	@Test(groups = { "MediumPriority" })
	public void verify_agent_user_can_edit_email_not_inapp_notifications() {
		
		System.out.println("Test case  --verify_agent_user_can_edit_email_not_inapp_notifications-- started ");
		
		String notificationType = "Missed Meeting";

		// agent driver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashboard.switchToTab(caiVerifyDriver, 2);
		dashboard.clickConversationAI(caiVerifyDriver);
		dashboard.navigateToCAINotifications(caiVerifyDriver);
		
		assertTrue(settingsPage.isInboxNotificationListDisabledAccToHeader(caiVerifyDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertTrue(settingsPage.isInboxNotificationListDisabledAccToHeader(caiVerifyDriver, CAINotificationsHeaders.UserNotifications));
		
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiVerifyDriver, CAINotificationsHeaders.AdvancedInsightsNotifications));
		assertFalse(settingsPage.isEmailNotificationListDisabledAccToHeader(caiVerifyDriver, CAINotificationsHeaders.UserNotifications));

		//default being selected
		settingsPage.selectEmailCheckBox(caiVerifyDriver, notificationType);
		
		//unselecting email
		settingsPage.unSelectEmailCheckBox(caiVerifyDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiVerifyDriver);
		assertFalse(settingsPage.isEmailNotificationChecked(caiVerifyDriver, notificationType));
		
		//selecting email
		settingsPage.selectEmailCheckBox(caiVerifyDriver, notificationType);
		settingsPage.verifyNotificationMsgSetting(caiVerifyDriver);
		assertTrue(settingsPage.isEmailNotificationChecked(caiVerifyDriver, notificationType));
		
		//check PublicSavedSearches notifications
		if(settingsPage.isFrequencyVisibleAccToTableHeader(caiVerifyDriver, CAINotificationsHeaders.PublicSavedSearches, 0)) {
			String notificationName = settingsPage.getNotificationNameAccToTableHeader(caiVerifyDriver, CAINotificationsHeaders.PublicSavedSearches, 0);
			
			//setting freq to None
			settingsPage.selectFrequencyAccToTableHeader(caiVerifyDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.None);
			
			//check inapp and email disabled
			assertTrue(settingsPage.isInAppNotificationDisabledAccToNotification(caiVerifyDriver, notificationName));
			assertTrue(settingsPage.isEmailNotificationDisabledAccToNotification(caiVerifyDriver, notificationName));
			
			//setting freq to Immediate
			settingsPage.selectFrequencyAccToTableHeader(caiVerifyDriver, CAINotificationsHeaders.PublicSavedSearches, 0, NotificationsFrequency.Immediate);
			settingsPage.verifyNotificationMsgSetting(caiVerifyDriver);
			
			//check inapp disabled not email
			assertTrue(settingsPage.isInAppNotificationDisabledAccToNotification(caiVerifyDriver, notificationName));
			assertFalse(settingsPage.isEmailNotificationDisabledAccToNotification(caiVerifyDriver, notificationName));
			
			//default being selected
			settingsPage.selectEmailCheckBox(caiVerifyDriver, notificationName);
			dashboard.isPaceBarInvisible(caiVerifyDriver);
			
			//unselecting email
			settingsPage.unSelectEmailCheckBox(caiVerifyDriver, notificationName);
			settingsPage.verifyNotificationMsgSetting(caiVerifyDriver);
			assertFalse(settingsPage.isEmailNotificationChecked(caiVerifyDriver, notificationName));
		}

		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case  --verify_agent_user_can_edit_email_not_inapp_notifications-- passed ");
	}

	//Verify User Image should visible according to the created by user
	@Test(groups = { "MediumPriority" })
	public void verify_user_image_visible_notifications_save_search() {
		
		System.out.println("Test case  --verify_user_image_visible_notifications_save_search-- started ");
		
		// agent driver
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.navigateToCAINotifications(caiCallerDriver);
		
		settingsPage.verifyUserImagesVisibleSaveSearch(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case  --verify_user_image_visible_notifications_save_search-- passed ");
	}
	
	//Verify sharing call recording widow to multiple users.
	@Test(groups = { "Regression", "Product Sanity" }) 
	public void verify_inbox_tab_notification_displayed_when_shared_with_multiple() {
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_shared_with_multiple-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.clickConversationAI(caiVerifyDriver);
		inboxPage.navigateToInboxTab(caiVerifyDriver);
		int inboxCountBefore = inboxPage.getInboxCount(caiVerifyDriver);
		
		//support driver
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashboard.clickConversationAI(caiSupportDriver);
		inboxPage.navigateToInboxTab(caiSupportDriver);
		int supportInboxCountBefore = inboxPage.getInboxCount(caiSupportDriver);
		
		dashboard.clickConversationAI(caiCallerDriver);
		
		// filter for my calls agent
		String filterValue = "My Calls";
		String agentToShare = userNameAgent;
		String agentCaller = userName;
		callsTabReactPage.selectAgentNameFromFilter(caiCallerDriver, filterValue);

		String callerName = callsTabReactPage.getCallerNameFromDurationSubjectList(caiCallerDriver);
		String receiverName = callsTabReactPage.getReceiverNameFromDurationSubjectList(caiCallerDriver);
		String accountName = callsTabReactPage.getAccountNameFromDurationSubjectList(caiCallerDriver);
		String duration = callsTabReactPage.getCallDurationFromSearchList(caiCallerDriver);
		
		// now share the recently created conversation AI with a user
		callsTabReactPage.openConversationDetails(caiCallerDriver, 0);
		String createdTime = HelperFunctions.GetCurrentDateTime("h:mm a");
		createdTime = createdTime.replace("am", "AM").replace("pm", "PM");
		String createdTime1 = HelperFunctions.GetCurrentDateTime("h:mm a", true);
		createdTime1= createdTime1.replace("am", "AM").replace("pm", "PM");
		conversationDetailPage.shareCall(caiCallerDriver, agentToShare, "This call is shared for testing purpose");			
		//2 user
		conversationDetailPage.shareCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_username"), "This call is shared for testing purpose");
		
		int inboxCountAfter = inboxPage.getInboxCount(caiVerifyDriver);
		assertEquals(inboxCountAfter, inboxCountBefore + 1);
		
		//check count on support user
		int supportInboxCountAfter = inboxPage.getInboxCount(caiSupportDriver);
		assertEquals(supportInboxCountAfter, supportInboxCountBefore + 1);
		
		String actualTime = inboxPage.getDateInboxList(caiVerifyDriver, 0);
		assertTrue(actualTime.equals(createdTime)|| actualTime.equals(createdTime1));
		
		assertEquals(inboxPage.getNotificationTextInbox(caiVerifyDriver, 0), agentCaller+" shared a call between "+receiverName+" and "+callerName);
		inboxPage.verifySharedCallUnReadImage(caiVerifyDriver, 0);
		
		inboxPage.clickFirstNotificationTextList(caiVerifyDriver);
		String dateTime = inboxPage.getAgentDetailsDateTime(caiVerifyDriver);
		dateTime = dateTime.replace("p.m.", "pm").replace("a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;

		assertEquals(inboxPage.getProspectDetailsName(caiVerifyDriver), receiverName);
		assertEquals(inboxPage.getProspectDetailsAccount(caiVerifyDriver), accountName);
		assertEquals(inboxPage.getAgentDetailsName(caiVerifyDriver), callerName);
		assertTrue(inboxPage.getAgentDetailsDuration(caiVerifyDriver).contains(duration));
		
		callsTabReactPage.verifyToolTipViewConversation(caiVerifyDriver, 0);
		callsTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		
		assertEquals(inboxPage.getInboxCount(caiVerifyDriver), inboxCountBefore);
		assertEquals(conversationDetailPage.getCallsTabPageHeading(caiVerifyDriver), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);

		inboxPage.navigateToInboxTab(caiVerifyDriver);
		inboxPage.verifySharedCallImageRead(caiVerifyDriver, 0);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_inbox_tab_notification_displayed_when_shared_with_multiple-- passed ");
	}
	
	//Verify Retain page number of search results when user clicks into call then hits back button
	//Verify CAI arrow right icon shows bigger
	@Test(groups = { "Regression" })
	public void verify_cai_arrow_icon_and_search_page_number_result() {
		System.out.println("Test case --verify_cai_arrow_icon_and_search_page_number_result-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		
		//verify cai arrow right
		callsTabReactPage.verifyToolTipViewConversation(caiCallerDriver, 0);

		int inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		//navigate to next page
		callsTabReactPage.navigateToNextCAIPage(caiCallerDriver, 3, 0);
		
		//get count
		int inboxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		
		//assert not equal
		assertNotEquals(inboxCountBefore, inboxCountAfter);
		
		//open inbox tab
		inboxPage.navigateToInboxTab(caiCallerDriver);
		//verify cai arrow right
		callsTabReactPage.verifyToolTipViewConversation(caiCallerDriver, 0);
		
		caiCallerDriver.navigate().back();
		
		inboxCountBefore = inboxPage.getInboxCount(caiCallerDriver);
		//assert equal
		assertEquals(inboxCountBefore, inboxCountAfter);
		
		//open lib page
		libraryPage.openLibraryPage(caiCallerDriver);
		
		//verify cai arrow right
		callsTabReactPage.verifyToolTipViewConversation(caiCallerDriver, 0);
		

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_cai_arrow_icon_and_search_page_number_result-- passed ");
	}
	
	//Ability to skip 10 seconds forward or backward on a call recording displayed on call player UI
	//Ability to jump back 10 seconds on a call recording within the updated player UI
	//Ability to view time marked annotations in the call recording player
	@Test(groups = { "Regression" })
	public void verify_skip_10_seconds_forward_and_backward_on_call_recording() {
		System.out.println("Test case --verify_skip_10_seconds_forward_and_backward_on_call_recording-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.openNewBlankTab(caiCallerDriver);
		dashboard.switchToTab(caiCallerDriver, dashboard.getTabCount(caiCallerDriver));
		caiCallerDriver.get("https://analytics-qa.ringdna.net/rc1344373");
		dashboard.isPaceBarInvisible(caiCallerDriver);
		
		//wait until play button visible
		conversationDetailPage.verifyRecordingPlayButtonVisible(caiCallerDriver);
		
		//get start time
		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), "0:00");
		
		//click 20 sec forward
		dashboard.isPaceBarInvisible(caiCallerDriver);
		conversationDetailPage.idleWait(5);
		conversationDetailPage.clickTenSecForwardPlayButton(caiCallerDriver);
		conversationDetailPage.clickTenSecForwardPlayButton(caiCallerDriver);
		
		//get start time
		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), "0:20");
		
		//click 10 sec rewind
		conversationDetailPage.clickTenSecRewindPlayButton(caiCallerDriver);
		//get start time
		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), "0:10");
		
		//get click in mid of player
		conversationDetailPage.clickInMidOfCallPlayer(caiCallerDriver);
		String initial = conversationDetailPage.getProgressBarStartTime(caiCallerDriver);
		assertNotEquals(initial, "0:00");
		
		//click 10 sec rewind
		conversationDetailPage.clickTenSecRewindPlayButton(caiCallerDriver);
		
		try {
			Date date1 = new SimpleDateFormat("m:ss").parse(initial);
			date1.setTime(date1.getTime() - 10000);
			initial = String.valueOf(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), initial.substring(15, 19));
		
		
//		//transcript
//		String transcriptTime = conversationDetailPage.getTranscriptSpeakerDetail(caiCallerDriver).get(0);
//		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), transcriptTime);
//		
//		//switch annotation tab
//		conversationDetailPage.switchToAnnotationTab(caiCallerDriver);
//		String annotateTime = conversationDetailPage.getAnnotatioSpeakerDetail(caiCallerDriver).get(0);
//		assertEquals(conversationDetailPage.getProgressBarStartTime(caiCallerDriver), annotateTime);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_skip_10_seconds_forward_and_backward_on_call_recording-- passed ");
	}

}
