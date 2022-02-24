package support.cases.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAI.InboxTabPage;
import support.source.conversationAI.LibraryTabPage;
import support.source.conversationAI.SaveSearchPage;
import utility.HelperFunctions;

public class SavedSearchPrivateCases extends SupportBase {

	DashBoardConversationAI dashboardConversationAI = new DashBoardConversationAI();
	Dashboard dashBoard = new Dashboard();
	CallsTabPage callsTabPage = new CallsTabPage();
	SaveSearchPage saveSearch = new SaveSearchPage();
	LibraryTabPage libraryPage = new LibraryTabPage();
	InboxTabPage inboxTabPage = new InboxTabPage();

	private String libraryName;
	private String agentName;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		libraryName = CONFIG.getProperty("qa_cai_library_name");
		agentName = CONFIG.getProperty("qa_cai_caller_name");
	}

	@Test(groups = { "Regression" })
	public void add_edit_private_search_by_caller_and_verify_driver() {
		System.out.println("Test case --add_edit_private_search_by_caller_and_verify_driver-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		saveSearch.saveSearchButtonNotVisible(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// verifying saved search in public search
		saveSearch.goToListSection(caiCallerDriver);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, saveSearchName));

		// verifying editable properties and total count
		saveSearch.verifyCallerCanEditDetails(caiCallerDriver, saveSearchName);
		assertTrue(saveSearch.isShareIconVisible(caiCallerDriver, saveSearchName));
		saveSearch.verifyTotalCalls(caiCallerDriver, saveSearchName);

		// Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName));

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName));

		// Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName));
		assertFalse(saveSearch.isViewResultsPageVisible(caiVerifyDriver, saveSearchName));

		// Updating the save search on Caller driver to 'Public' visibility
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName,
				SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.None.toString(), "");

		// Verifying save search and share icon not Visible
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, updatedSaveSearchName));
		assertFalse(saveSearch.isShareIconVisible(caiCallerDriver, updatedSaveSearchName), "Share icon is visible");

		// Checking in VerifyDriver to see save search name available
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertTrue(saveSearch.verifyPublicSaveSearchOnPage(caiVerifyDriver, updatedSaveSearchName));

		// Navigating to library section and verifying saved search
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, updatedSaveSearchName));
		assertTrue(saveSearch.isViewResultsPageVisible(caiVerifyDriver, updatedSaveSearchName));

		// deleting save search on caller driver
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void delete_verify_private_save_search_by_trash_icon() {
		System.out.println("Test case --delete_verify_private_save_search_by_trash_icon-- started ");
		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details with 'Private' search
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// verifying saved search in my search and deleting save search
		saveSearch.goToListSection(caiCallerDriver);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// Navigating to library section of 'Caller' driver and verifying saved search is deleted
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void delete_verify_private_save_search_by_delete_search_link() {
		System.out.println("Test case --delete_verify_private_save_search_by_delete_search_link-- started ");
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
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Deleting by save search link and verifying save search on caller driver
		saveSearch.clickDeleteSearchLink(caiCallerDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// Navigating to library section of 'Caller' driver and verifying saved search
		// is deleted
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Going to 'VerifyDriver' and checking in public search
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void clone_private_search_by_created_user() {
		System.out.println("Test case --clone_private_search_by_created_user-- started ");
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
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Click on clone search link and verifying and adding filters
		saveSearch.clickCloneSearchLink(caiCallerDriver);
		callsTabPage.verifyFilters(caiCallerDriver, agentName);
		callsTabPage.selectTimeFrame(caiCallerDriver, CallsTabPage.TimeFrameOptions.Week.toString());
		callsTabPage.verifyFilters(caiCallerDriver, CallsTabPage.TimeFrameOptions.Week.toString());

		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void edit_delete_private_save_search_from_library_and_verify() {
		System.out.println("Test case --edit_delete_private_save_search_from_library_and_verify-- started ");
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
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Navigating to library section and editing saved search
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);

		// Updating and verifying save search details
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName, "",
				SaveSearchPage.SaveSearchParams.Weekly.toString(), "");
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");

		// deleting save search on caller driver
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Navigating to library section and verifying save search for 'VerifyDriver'
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertFalse(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, updatedSaveSearchName), "");

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void edit_private_search_after_view_saved_search() {
		System.out.println("Test case --edit_private_search_after_view_saved_search-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// deleting all save search
		dashBoard.clickConversationAI(caiCallerDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.deleteAllSaveSearch(caiCallerDriver);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// Updating and verifying save search details
		saveSearch.goToListSection(caiCallerDriver);
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName,
				SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.None.toString(),
				SaveSearchPage.LibraryOps.Multiple.toString());
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");
		assertFalse(saveSearch.isShareIconVisible(caiCallerDriver, updatedSaveSearchName));

		// deleting save search on caller driver
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void share_private_search_from_saved_search_list_view() {
		System.out.println("Test case --share_private_search_from_saved_search_list_view-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility and verifying
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// sharing save search
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickShareBtn(caiCallerDriver);
		saveSearch.enterShareDetails(caiCallerDriver, shareMsg, CONFIG.getProperty("qa_cai_verify_name"));

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiVerifyDriver, saveSearchName), "");
		saveSearch.verifyNonCallerCannotEditDetails(caiVerifyDriver, saveSearchName);
		assertFalse(saveSearch.isShareIconVisible(caiVerifyDriver, saveSearchName), "");
		saveSearch.goToViewResultsPageSaveSearch(caiVerifyDriver, saveSearchName);
		saveSearch.verifyAfterSharingSaveSearchOnPage(caiVerifyDriver);

		// verifying inbox count increase after sharing
		dashboardConversationAI.navigateToInboxSection(caiVerifyDriver);
		String msg = inboxTabPage.getInboxFirstMsg(caiVerifyDriver);
		assertEquals(msg, String.format("%s has shared \"%s\" with you.", agentName, saveSearchName));

		// Navigating to library section and verifying save search
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");

		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.goToListSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void share_private_search_after_view_saved_search() {
		System.out.println("Test case --share_private_search_after_view_saved_search-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility and verifying
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);
		saveSearch.goToListSection(caiCallerDriver);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, saveSearchName), "");

		// sharing save search
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickShareIconSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.enterShareDetails(caiCallerDriver, shareMsg, CONFIG.getProperty("qa_cai_verify_name"));

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Navigating to Save search section and verifying save search for
		// 'VerifyDriver'
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToSaveSearchSection(caiVerifyDriver);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiVerifyDriver, saveSearchName));
		saveSearch.verifyNonCallerCannotEditDetails(caiVerifyDriver, saveSearchName);
		assertFalse(saveSearch.isShareIconVisible(caiVerifyDriver, saveSearchName));
		saveSearch.goToViewResultsPageSaveSearch(caiVerifyDriver, saveSearchName);
		saveSearch.verifySaveSearchDetailsForSharedViewer(caiVerifyDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName, agentName);
		int beforeViewCount = saveSearch.getViewCount(caiVerifyDriver);
		
		//verifying share search details
		saveSearch.goToListSection(caiVerifyDriver);
		saveSearch.verifyAfterSharingSaveSearchOnPage(caiVerifyDriver);

		//verifying view counts
		saveSearch.goToViewResultsPageSaveSearch(caiVerifyDriver, saveSearchName);
		int afterViewCount = saveSearch.getViewCount(caiVerifyDriver);
		saveSearch.verifyViewCount(caiVerifyDriver, beforeViewCount, afterViewCount);

		// cloning in 'VerifyDriver'and checking filters
		saveSearch.clickCloneSearchLink(caiVerifyDriver);
		callsTabPage.verifyFilters(caiVerifyDriver, agentName);
		callsTabPage.clearAllFilters(caiVerifyDriver);
		callsTabPage.selectTimeFrame(caiVerifyDriver, CallsTabPage.TimeFrameOptions.Month.toString());
		callsTabPage.verifyFilters(caiVerifyDriver, CallsTabPage.TimeFrameOptions.Month.toString());

		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);

		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void share_private_search_from_library_tab() {
		System.out.println("Test case --share_private_search_from_library_tab-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility and verifying
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);
		saveSearch.goToListSection(caiCallerDriver);
		assertTrue(saveSearch.verifyMySavedSearchOnPage(caiCallerDriver, saveSearchName), "");

		// initiating the VerifyDriver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		// Navigating to Library section and verifying edit properties of 'VerifyDriver'
		dashBoard.clickConversationAI(caiVerifyDriver);
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");
		saveSearch.verifyNonCallerCannotEditDetails(caiVerifyDriver, saveSearchName);
		assertFalse(saveSearch.isShareIconVisible(caiVerifyDriver, saveSearchName), "");

		// sharing save search from library section of 'CallerDriver'
		dashboardConversationAI.navigateToLibrarySection(caiCallerDriver);
		libraryPage.selectLibraryFromLeftMenu(caiCallerDriver, libraryName);
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickShareIconSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.enterShareDetails(caiCallerDriver, shareMsg, CONFIG.getProperty("qa_cai_verify_name"));

		// navigating to library section and checking in 'VerifyDriver' after sharing from 'callerDriver'
		dashboardConversationAI.navigateToLibrarySection(caiVerifyDriver);
		libraryPage.selectLibraryFromLeftMenu(caiVerifyDriver, libraryName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiVerifyDriver, saveSearchName), "");
		saveSearch.goToViewResultsPageSaveSearch(caiVerifyDriver, saveSearchName);
		saveSearch.verifyAfterSharingSaveSearchOnPage(caiVerifyDriver);
		
		// deleting save search on caller driver
		dashboardConversationAI.navigateToSaveSearchSection(caiCallerDriver);
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void edit_private_search_after_share_search() {
		System.out.println("Test case --edit_private_search_after_share_search-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility and verifying
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// sharing save search
		saveSearch.goToListSection(caiCallerDriver);
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickShareIconSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.enterShareDetails(caiCallerDriver, shareMsg, CONFIG.getProperty("qa_cai_verify_name"));

		// Updating save search after sharing save search
		String updatedSaveSearchName = saveSearchName.concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickPencilSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.editSaveSearchDetails(caiCallerDriver, saveSearchName, updatedSaveSearchName,
				SaveSearchPage.SaveSearchParams.Public.toString(), SaveSearchPage.SaveSearchParams.None.toString(),
				SaveSearchPage.LibraryOps.Multiple.toString());
		saveSearch.clickPencilSaveSearch(caiCallerDriver, updatedSaveSearchName);
		saveSearch.verifyEditDetailsSaved(caiCallerDriver, updatedSaveSearchName);
		assertTrue(saveSearch.verifySaveSearchOnPage(caiCallerDriver, updatedSaveSearchName), "");

		//deleting save search
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, updatedSaveSearchName);
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void user_can_share_private_search_to_participant_once() {
		System.out.println("Test case --user_can_share_private_search_to_participant_once-- started ");

		// Updating the driver of caller and verifier used
		System.out.println("in before method");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// Navigating to conversation AI and setting agent
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setAgentFilter(caiCallerDriver, agentName);

		// Entering save search details of 'Private' Visibility and verifying
		String saveSearchName = "AutoSaveSearch".concat(HelperFunctions.GetRandomString(3));
		saveSearch.clickSaveSearchOnPageBtn(caiCallerDriver);
		saveSearch.enterSaveSearchDetails(caiCallerDriver, saveSearchName,
				SaveSearchPage.SaveSearchParams.Private.toString(),
				SaveSearchPage.SaveSearchParams.Immediate.toString(), libraryName);

		// sharing save search
		saveSearch.goToListSection(caiCallerDriver);
		String participant_user = CONFIG.getProperty("qa_cai_verify_name");
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		saveSearch.clickShareIconSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.enterShareDetails(caiCallerDriver, shareMsg, participant_user);

		//verifying shared participant cannot be shared again
		saveSearch.clickShareIconSaveSearch(caiCallerDriver, saveSearchName);
		saveSearch.verifyShareParticipantNotVisible(caiCallerDriver, participant_user);
		
		//deleting save search
		saveSearch.clickTrashIconSaveSearch(caiCallerDriver, saveSearchName);
		
		// setting driver to false
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case is pass");
	}
}
