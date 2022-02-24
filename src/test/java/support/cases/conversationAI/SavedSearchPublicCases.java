package support.cases.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAI.InboxTabPage;
import support.source.conversationAI.LibraryTabPage;
import support.source.conversationAI.SaveSearchPage;
import utility.HelperFunctions;

public class SavedSearchPublicCases extends SupportBase {

	DashBoardConversationAI dashboardConversationAI = new DashBoardConversationAI();
	Dashboard dashBoard = new Dashboard();
	CallsTabPage callsTabPage = new CallsTabPage();
	InboxTabPage inboxPage = new InboxTabPage();
	SaveSearchPage saveSearch = new SaveSearchPage();
	LibraryTabPage libraryPage = new LibraryTabPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	Random random = new Random();
	
	private String libraryName;
	private String agentName;
	private String contactFirstName;
	private String contactAccountName;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		libraryName = CONFIG.getProperty("qa_cai_library_name");
		agentName = CONFIG.getProperty("qa_cai_caller_name");
		contactFirstName = CONFIG.getProperty("contact_first_name");
		contactAccountName = CONFIG.getProperty("contact_account_name");	
	}
	
	@Test(groups = { "Regression" })
	public void add_edit_public_search_by_caller_and_verify_driver() {
		System.out.println("Test case --add_edit_public_search_by_caller_and_verify_driver-- started ");

		//Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		//deleting all save search
		dashBoard.clickConversationAI(caiCallerDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.deleteAllSaveSearch(caiCallerDriver);
		
		//Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
	
		//Verify search btn not visible
		saveSearch.saveSearchButtonNotVisible(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		//Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);
	
		saveSearch.verifyBorderedFiltersNonClickable(caiCallerDriver);
		
		//verifying saved search in My Saved Search
		saveSearch.goToListSection(caiCallerDriver);

		// verify in my save search section only
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, saveSearchName), String.format("Save Search: %s not found", saveSearchName));
		
		//verifying editable properties and total count
		saveSearch.verifyCallerCanEditDetails(caiCallerDriver, saveSearchName);
		saveSearch.verifyTotalCalls(caiCallerDriver, saveSearchName);
		
		//Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		//Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		
		//verify in public search section
		assertTrue(saveSearch.verifyPublicSaveSearchOnPage(caiVerifyDriver, saveSearchName), String.format("Save Search: %s not found", saveSearchName));
		saveSearch.verifyNonCallerCannotEditDetails(caiVerifyDriver, saveSearchName);
		
		//Updating the save search on Caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName, SaveSearchPage.SaveSearchParams.Private.toString(), SaveSearchPage.SaveSearchParams.None.toString(), SaveSearchPage.LibraryOps.Remove.toString());
		
		//Verifying save search and share icon Visible
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		
		//verify in My Saved search section
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, updatedSaveSearchName), String.format("Save Search: %s not found", updatedSaveSearchName));
		assertTrue(saveSearch.isShareIconVisible(caiCallerDriver, updatedSaveSearchName), "Share icon is not visible");
		
		//Checking in VerifyDriver to see no save search name available
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, updatedSaveSearchName), String.format("Save Search: %s found", updatedSaveSearchName));
		
		// Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertFalse(saveSearch.verifyPublicSaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), String.format("Save Search: %s found", updatedSaveSearchName));

		//deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --add_edit_public_search_by_caller_and_verify_driver-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void delete_verify_save_search_by_trash_icon_in_library_section() {
		System.out.println("Test case --delete_verify_save_search_by_trash_icon_in_library_section-- started ");
		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Navigating to library section and deleting and verifying save search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), String.format("Save Search: %s exists after deleting", saveSearchName));

		//initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		// Navigating to library section and verifying save search for 'VerifyDriver'
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), String.format("Save Search: %s exists after deleting", saveSearchName));
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --delete_verify_save_search_by_trash_icon_in_library_section-- passed ");

	}

	@Test(groups = { "Regression" })
	public void delete_verify_save_search_by_delete_search_link() {
		System.out.println("Test case --delete_verify_save_search_by_delete_search_link-- started ");
		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Navigating to library section and verifying saved search details
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");
		assertTrue(saveSearch.isSaveSearchImageVisible(caiCallerDriver, saveSearchName), "");
		saveSearch.verifyTotalCalls(caiCallerDriver, saveSearchName);
		saveSearch.verifySaveSearchDetailsPresent(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);
		
		// Deleting by save search link and verifying save search on caller driver
		saveSearch.clickDeleteSearchLink(caiCallerDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// Navigating to library section and verifying saved search is deleted
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");
		
		//initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		// Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --delete_verify_save_search_by_delete_search_link-- passed ");
	}

	@Test(groups = { "Regression" })
	public void edit_save_search_from_library_and_verify() {
		System.out.println("Test case --edit_save_search_from_library_and_verify-- started ");
		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Navigating to library section and editing saved search 
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		
		//Verifying that click on library tag navigates to call section
		callsTabPage.clickFirstAgent(caiCallerDriver);
		assertTrue(dashboardConversationAI.navigateToCallsSection(caiCallerDriver));		
		
		// Updating and verifying save search details
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName, "", SaveSearchPage.SaveSearchParams.Weekly.toString(), "");
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");

		// deleting save search on caller driver
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --edit_save_search_from_library_and_verify-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void clone_search_by_created_and_verify_user() {
		System.out.println("Test case --clone_search_by_created_and_verify_user-- started ");
		// Updating the driver of caller used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		//Click on clone search link and verifying and adding filters
		saveSearch.clickCloneSearchLink(caiCallerDriver);
		callsTabPage.verifyFilters(caiCallerDriver, agentName);
		callsTabPage.selectTimeFrame(caiCallerDriver, CallsTabPage.TimeFrameOptions.Week.toString());
		callsTabPage.verifyFilters(caiCallerDriver, CallsTabPage.TimeFrameOptions.Week.toString());

		// Updating the driver of verifier used
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		// Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		saveSearch.goToViewResultsPageSaveSearch(caiVerifyDriver, saveSearchName);
		saveSearch.clickCloneSearchLink(caiVerifyDriver);
		
		//verifying in VerifyDriver filters
		callsTabPage.verifyFilters(caiVerifyDriver, agentName);
		callsTabPage.selectTimeFrame(caiVerifyDriver, CallsTabPage.TimeFrameOptions.Month.toString());
		callsTabPage.verifyFilters(caiVerifyDriver, CallsTabPage.TimeFrameOptions.Month.toString());

		// deleting save search on caller driver
		dashBoard.clickConversationAI(caiCallerDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
				
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --clone_search_by_created_and_verify_user-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void edit_after_view_search_result_selecting_multiple_libraries() {
		System.out.println("Test case --edit_after_view_search_result_selecting_multiple_libraries-- started ");

		//Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		//Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		//Entering save search details
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);
	
		//verifying saved search in public search
		saveSearch.goToListSection(caiCallerDriver);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, saveSearchName), "");
		
		saveSearch.goToViewResultsPageSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.clickEditBtn(caiCallerDriver);
		
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		String librarySelctd = saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Daily.toString(), SaveSearchPage.LibraryOps.Multiple.toString());
		
		//Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");
		
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, librarySelctd);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		//Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertTrue(saveSearch.verifyPublicSaveSearchOnPage(caiVerifyDriver, updatedSaveSearchName), "");
		
		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --edit_after_view_search_result_selecting_multiple_libraries-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void save_search_no_notification_no_library() {
		System.out.println("Test case --save_search_no_notification_no_library-- started ");

		//Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		//deleting existing save searches
		dashBoard.clickConversationAI(caiCallerDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.deleteAllSaveSearch(caiCallerDriver);
		
		//Navigating to conversation AI and setting agent
		int callRating = random.nextInt((5-1)+1)+1;
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		int inboxBoxCount = inboxPage.getInboxCount(caiCallerDriver);
		callsTabPage.setRatingFilter(caiCallerDriver, String.valueOf(callRating));
		
		//Entering save search details
		String saveSearchName = "NoLibNoNotificationSS".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.None.toString(), null);
	
		String callSubject = "NoLibNoNotificationSubject".concat(HelperFunctions.GetRandomString(2));
		String callNotes = "NoLibNoNotificationNote".concat(HelperFunctions.GetRandomString(2));
		
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, CONFIG.getProperty("qa_cai_disposition"));
		callToolsPanel.giveCallRatings(caiCallerDriver, callRating);
		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsLead(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		//
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);
		
		int inboxBoxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertEquals(inboxBoxCountAfter, inboxBoxCount, "Count is not same");
		
		dashboardConversationAI.navigateToInboxSection(caiCallerDriver);
		assertFalse(inboxPage.verifySaveSearchInbox(caiCallerDriver, saveSearchName), "Inbox contain notification with save search");
		
		//
		dashboardConversationAI.navigateToCallsSection(caiCallerDriver);
		callsTabPage.verifyLibraryNotSelectedCAI(caiCallerDriver);

		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --save_search_no_notification_no_library-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void save_search_immediate_notification() {
		System.out.println("Test case --save_search_immediate_notification-- started ");

		//Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		//Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.deleteAllSaveSearch(caiCallerDriver);
		int inboxBoxCount = inboxPage.getInboxCount(caiCallerDriver);
		
		int callRating = random.nextInt((5-1)+1)+1;
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setRatingFilter(caiCallerDriver, String.valueOf(callRating));
		
		//Entering save search details
		String saveSearchName = "ImmediateNotificationSS".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName, SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.Immediate.toString(), null);
	
		String callSubject = "ImmediateNotificationSubject".concat(HelperFunctions.GetRandomString(2));
		String callNotes = "ImmediateNotificationNote".concat(HelperFunctions.GetRandomString(2));
		
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, CONFIG.getProperty("qa_cai_disposition"));
		callToolsPanel.giveCallRatings(caiCallerDriver, callRating);
		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsLead(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		//
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		callsTabPage.switchToTab(caiCallerDriver, 2);
		callsTabPage.refreshCurrentDocument(caiCallerDriver);
		dashBoard.clickConversationAI(caiCallerDriver);
		
		int inboxBoxCountAfter = inboxPage.getInboxCount(caiCallerDriver);
		assertTrue(inboxBoxCountAfter > inboxBoxCount, "Count is same");
		
		dashboardConversationAI.navigateToInboxSection(caiCallerDriver);
		assertTrue(inboxPage.verifySaveSearchInbox(caiCallerDriver, saveSearchName), "Inbox do not contain notification with save search");

		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --save_search_immediate_notification-- passed ");
	}
}
