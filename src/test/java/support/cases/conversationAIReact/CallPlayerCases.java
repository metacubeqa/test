package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.SoftphoneCallHistoryPage.MatchTypeFilters;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewTask;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLogsTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSalesforceTab.SalesForceFields;
import support.source.admin.AccountCallRecordingTab;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.ConversationDetailPage.actionsDropdownItems;
import support.source.conversationAIReact.ConversationDetailPage.talkMetricsItems;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.profilePage.ProfilePage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CallPlayerCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	CallsTabReactPage callTabReactPage = new CallsTabReactPage();
	ConversationDetailPage caiDetailPage = new ConversationDetailPage();
	AccountCallRecordingTab adminCallRecordingTab = new AccountCallRecordingTab();
	LibraryReactPage libraryReactPage = new LibraryReactPage();
	SoftphoneCallHistoryPage sfHistoryPage = new SoftphoneCallHistoryPage();
	
	AccountSalesforceTab accountSalesforceTab = new AccountSalesforceTab();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	ProfilePage profilePage = new ProfilePage();
	AccountIntelligentDialerTab accountIntelligentDialerTab = new AccountIntelligentDialerTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	UsersPage 					usersPage					= new UsersPage();
	AccountLogsTab logsTab = new AccountLogsTab();
	
	static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	static final String extension = ".wav";
	static final String longAnnotationString = 
			"Based on your input, get a random alpha numeric string. The random string generator creates a series of numbers and letters that have no pattern. These can be helpful for creating security codes.\r\n" + 
			"\r\n" + 
			"With this utility you generate a 16 character output based on your input of numbers and upper and lower case letters.  Random strings can be unique. Used in computing, a random string generator can also be called a random character string generator. ";
	
	String profileName = "System Administrator";
	
	String relatedAccountName 	  = "Automation_Accounts";
	String relatedOpportunityName = "Abhishek_Thomas Opportunity";
	String relatedCaseName		  = "Abhishek Thomas Automation Case";
	
//	No annotation image should appears if no annotation added on Call detail page
	@Test(groups = { "Regression" })
	public void verify_no_annotation_functions_on_call_player_page() {
		System.out.println("Test case --verify_no_annotation_functions_on_call_player_page-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		String searchToken = callTabReactPage.setConversationAIFilters(caiCallerDriver,
				CallDataFilterType.HasAnnotations, 0, null);

		// change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiCallerDriver, searchToken);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		String oldAnnotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(3));
		caiDetailPage.switchToAnnotationTab(caiCallerDriver);
		caiDetailPage.verifyNoAnnotationImagePresent(caiCallerDriver);
		caiDetailPage.enterAnnotationWithoutTag(caiCallerDriver, oldAnnotationName, "Vishal Ringdna");
		
		String newAnnotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(3));
		caiDetailPage.editAnnotationCreated(caiCallerDriver, oldAnnotationName, newAnnotationName);
		caiDetailPage.verifyAnnotationCreated(caiCallerDriver, newAnnotationName, "Vishal Ringdna");
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_no_annotation_functions_on_call_player_page-- passed ");
	}
	
	//Save annotation with long data and verify show more fuctioanlity
	@Test(groups = { "Regression" })
	public void verify_see_more_on_annotation_long_data() {
		System.out.println("Test case --verify_see_more_on_annotation_long_data-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		String annotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(3));
		caiDetailPage.removeExistingSeeMoreLink(caiCallerDriver);
		assertFalse(caiDetailPage.isSeeMoreLinkVisible(caiCallerDriver));
		
		caiDetailPage.enterAnnotation(caiCallerDriver, longAnnotationString);
		caiDetailPage.switchToAnnotationTab(caiCallerDriver);
		caiDetailPage.clickSeeMoreAnnotationLink(caiCallerDriver);
		
		caiDetailPage.clickFirstPencilAnnotationIcon(caiCallerDriver);
		caiDetailPage.editAndEnterAnnotationBody(caiCallerDriver, annotationName);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_see_more_on_annotation_long_data-- passed ");
	}
	
	//Verify support and agent role can't download recording when 'Restrict Recording Download' setting ON
	//Verify admin role can download recording when 'Restrict Recording Download' ON
	@Test(groups = { "Regression" })
	public void verify_support_agent_cant_download_when_restrict_recording_download_on() {
		System.out.println("Test case --verify_support_agent_cant_download_when_restrict_recording_download_on-- started ");
		
		// admin driver
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		callTabReactPage.switchToTab(caiCallerDriver, 2);
		adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
		adminCallRecordingTab.swtichToAdvancedTab(caiCallerDriver);
		adminCallRecordingTab.enableRestrictCallRecordingSetting(caiCallerDriver);
		adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
		
		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.clearAllFilters(caiCallerDriver);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		// verify download action visible and downloadable
		HelperFunctions.deletingFilesWithExtension(downloadPath, extension);
		boolean result = caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.DownloadFile);
		assertTrue(result);
		caiDetailPage.waitForFileToDownloadWithExtension(downloadPath, extension);
		assertTrue(caiDetailPage.isExtensionFileDownloaded(downloadPath, extension));

		// agent driver
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashboard.switchToTab(caiVerifyDriver, 2);
		dashboard.clickConversationAI(caiVerifyDriver);
		callTabReactPage.clearAllFilters(caiVerifyDriver);
		callTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		
		//verify download action not visible
		result = caiDetailPage.selectActionDropdownOption(caiVerifyDriver, actionsDropdownItems.DownloadFile);
		assertFalse(result);
		
		// support driver
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashboard.switchToTab(caiSupportDriver, 2);
		dashboard.clickConversationAI(caiSupportDriver);
		callTabReactPage.clearAllFilters(caiSupportDriver);
		callTabReactPage.openConversationDetails(caiSupportDriver, 0);

		//verify download action not visible
		result = caiDetailPage.selectActionDropdownOption(caiSupportDriver, actionsDropdownItems.DownloadFile);
		assertFalse(result);
		
		callTabReactPage.switchToTab(caiCallerDriver, 2);
		dashboard.navigateToMySettings(caiCallerDriver);
		adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
		adminCallRecordingTab.swtichToAdvancedTab(caiCallerDriver);
		adminCallRecordingTab.disableRestrictCallRecordingSetting(caiCallerDriver);
		adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
		
		//verify download action visible for agent
		caiVerifyDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(caiVerifyDriver);
		result = caiDetailPage.selectActionDropdownOption(caiVerifyDriver, actionsDropdownItems.DownloadFile);
		assertTrue(result);
		
		//verify download action visible for support
		caiSupportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(caiSupportDriver);
		result = caiDetailPage.selectActionDropdownOption(caiSupportDriver, actionsDropdownItems.DownloadFile);
		assertTrue(result);
		
		callTabReactPage.switchToTab(caiCallerDriver, dashboard.getTabCount(caiCallerDriver));
		caiCallerDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(caiCallerDriver);
		result = caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.DownloadFile);
		assertTrue(result);
		
		adminCallRecordingTab.closeTab(caiCallerDriver);
		adminCallRecordingTab.switchToTab(caiCallerDriver, 2);
		
		// Setting caiCallerDriver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_support_agent_cant_download_when_restrict_recording_download_on-- passed ");
	}

	//Conversation AI player- Action- Add Reviewer- Verify user list and search user
	@Test(groups = { "Regression" })
	public void verify_user_list_search_user_action_add_reviewer() {
		System.out.println("Test case --verify_user_list_search_user_action_add_reviewer-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.AddReviewers);
		String userName = caiDetailPage.getUserNameFromReviewerList(caiCallerDriver, 2);
		caiDetailPage.clickCancelButton(caiCallerDriver);

		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.AddReviewers);
		caiDetailPage.selectReviewerToAgentFromDropDown(caiCallerDriver, userName);
		caiDetailPage.clickCancelButton(caiCallerDriver);
		
		//add reviewer
		caiDetailPage.addReviewers(caiCallerDriver, userName);

		//verifying that agent does not exits
		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.AddReviewers);
		assertFalse(caiDetailPage.isAgentExistsInDropDown(caiCallerDriver, userName, actionsDropdownItems.AddReviewers));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_list_search_user_action_add_reviewer-- passed ");
	}
	
	//Verify remove selected user from the Sharing call pop up window
	//Selected user should not be searchable again on Share call screen
	//Validate  react UI of 'Share Call ' pop up window
	//Verify selected participants users should not be removed when we removed search string on Share Cell screen
	@Test(groups = { "Regression" })
	public void verify_remove_selected_user_after_sharing() {
		System.out.println("Test case --verify_remove_selected_user_after_sharing-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		
		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.ShareCall);
		caiDetailPage.verifyErrorMsgWhenSharedEmpty(caiCallerDriver);
		
		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.ShareCall);
		caiDetailPage.selectShareToAgentFromDropDown(caiCallerDriver, "vikram test");
		assertFalse(caiDetailPage.isAgentExistsInDropDown(caiCallerDriver, "vikram test", actionsDropdownItems.ShareCall));
		
		caiDetailPage.selectShareToAgentFromDropDown(caiCallerDriver, "Yamini Ringdna");
		assertTrue(caiDetailPage.isAgentVisibleAfterShared(caiCallerDriver, "vikram test"));
		assertTrue(caiDetailPage.isAgentVisibleAfterShared(caiCallerDriver, "Yamini Ringdna"));
		
		caiDetailPage.enterRecipientInputTab(caiCallerDriver, "vik");
		assertTrue(caiDetailPage.isAgentExistsInDropDown(caiCallerDriver, "vik", actionsDropdownItems.ShareCall));
		
		caiDetailPage.clearAllReceipientInputTab(caiCallerDriver);
		assertTrue(caiDetailPage.isAgentVisibleAfterShared(caiCallerDriver, "vikram test"));
		assertTrue(caiDetailPage.isAgentVisibleAfterShared(caiCallerDriver, "Yamini Ringdna"));
		
		caiDetailPage.deleteSelectedSharedAgent(caiCallerDriver, "vikram test");
		caiDetailPage.deleteSelectedSharedAgent(caiCallerDriver, "Yamini Ringdna");
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_remove_selected_user_after_sharing-- passed ");
	}
	
	//User profile image should be displayed while sharing call on Share Call screen
	@Test(groups = { "Regression" })
	public void verify_user_image_visible_when_share_agent() {
		System.out.println("Test case --verify_user_image_visible_when_share_agent-- started ");

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashboard.clickConversationAI(caiVerifyDriver);
		callTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		
		String userName = "Vishal Ringdna";
		caiDetailPage.selectActionDropdownOption(caiVerifyDriver, actionsDropdownItems.ShareCall);
		caiDetailPage.verifyShareAgentImageVisible(caiVerifyDriver, userName);
		
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_user_image_visible_when_share_agent-- passed ");
	}
	
	//Update already existing supervisor notes
	//Expand and Collapse supervisor notes
	//Expand and Collapse call notes
	//Verify Error message should not showing on Softphone after adding more then 255 characters on Supervisor Notes field.
	//Verify user can't add Call Notes to other agent's calls
	@Test(groups = { "Regression" })
	public void expand_and_close_call_notes_superviosr_notes() {
		System.out.println("Test case --expand_and_close_call_notes_superviosr_notes-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		sfHistoryPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.reloadSoftphone(caiCallerDriver);
		assertFalse(sfHistoryPage.isSalesForceSuperVisorErrorMsgVisible(caiCallerDriver));
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.selectAgentNameFromFilter(caiCallerDriver, "Vishal Ringdna");
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
	
		//making notes empty default condition
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, "");
		assertFalse(caiDetailPage.isExpandIconSuperVisNotesVisible(caiCallerDriver));
		caiDetailPage.addOrEditCallNotes(caiCallerDriver, "");
		assertFalse(caiDetailPage.isExpandIconCallNotesVisible(caiCallerDriver));
		
		//adding long superVisorNotes
		String superVisorNotes = longAnnotationString;
		
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, superVisorNotes);
		caiDetailPage.clickExpandIconSuperVisNotes(caiCallerDriver);
		
		//comparing first and last words of string entered
		String [] notesSplit = superVisorNotes.split(" ");
		String expectedFirstWord = notesSplit[0];
		String expectedLastWord = notesSplit[notesSplit.length -1];
		
		String actualNotes = caiDetailPage.getSuperVisorNotesText(caiCallerDriver);
		String [] actualNotesSplit = actualNotes.split(" ");
		String actualFirstWord = actualNotesSplit[0];
		String actualLastWord = actualNotesSplit[actualNotesSplit.length -1];
	
		assertEquals(actualFirstWord, expectedFirstWord);
		assertEquals(actualLastWord, expectedLastWord);
		
		caiDetailPage.clickCollapseIconSuperVisNotes(caiCallerDriver);
		
		//adding long call notes
		String notes = longAnnotationString;
		
		caiDetailPage.addOrEditCallNotes(caiCallerDriver, notes);
		caiDetailPage.clickExpandIconCallNotes(caiCallerDriver);
	
		actualNotes = caiDetailPage.getCallNotesText(caiCallerDriver);
		actualNotesSplit = actualNotes.split(" ");
		actualFirstWord = actualNotesSplit[0];
		actualLastWord = actualNotesSplit[actualNotesSplit.length -1];
	
		assertEquals(actualFirstWord, expectedFirstWord);
		assertEquals(actualLastWord, expectedLastWord);

		caiDetailPage.clickCollapseIconCallNotes(caiCallerDriver);
		
//		caiDetailPage.switchToTab(caiCallerDriver, 1);
//		assertFalse(sfHistoryPage.isSalesForceSuperVisorErrorMsgVisible(caiCallerDriver));

		//making notes empty default condition
//		caiDetailPage.switchToTab(caiCallerDriver, 2);
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, "");
		assertFalse(caiDetailPage.isExpandIconSuperVisNotesVisible(caiCallerDriver));
		caiDetailPage.addOrEditCallNotes(caiCallerDriver, "");
		assertFalse(caiDetailPage.isExpandIconCallNotesVisible(caiCallerDriver));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --expand_and_close_call_notes_superviosr_notes-- passed ");
	}
	
	// Update already existing supervisor notes
	@Test(groups = { "Regression" })
	public void update_existing_supervisor_notes() {
		System.out.println("Test case --update_existing_supervisor_notes-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.selectAgentNameFromFilter(caiCallerDriver, "Vishal Ringdna");

		callTabReactPage.openConversationDetails(caiCallerDriver, 0);

		//adding supervisor notes
		String superVisorNotes = "AutoSuperNotes".concat(HelperFunctions.GetRandomString(3));
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, superVisorNotes);
		caiDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, superVisorNotes);
		
		//updating supervisor notes
		superVisorNotes = "AutoSuperNotes".concat(HelperFunctions.GetRandomString(3));
		caiDetailPage.addOrEditSuperVisorNotes(caiCallerDriver, superVisorNotes);
		caiDetailPage.verifySuperVisorNotesSaved(caiCallerDriver, superVisorNotes);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --update_existing_supervisor_notes-- passed ");
	}
	
	//Verify user can't add Call Notes to other agent's calls
	@Test(groups = { "Regression" })
	public void verify_user_cant_edit_other_users_call_notes() {
		System.out.println("Test case --verify_user_cant_edit_other_users_call_notes-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.selectAgentNameFromFilter(caiCallerDriver, "Cai User1");
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		caiDetailPage.verifyAddEditCallNotesIconNotVisible(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_cant_edit_other_users_call_notes-- passed ");
	}
	
	@Test(groups = {  "Regression", "Product Sanity" })
	public void user_relate_new_library_with_cai_call() {

		System.out.println("Test case --user_relate_new_library_with_cai_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// open Conversation AI page and move to library page
		dashboard.clickConversationAI(caiCallerDriver);
		libraryReactPage.openLibraryPage(caiCallerDriver);
		
		libraryReactPage.verifyErrorEmptyLibrary(caiCallerDriver);
		
		// Create a library
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		List<String> libraryList = new ArrayList<String>();
		libraryList.add(libraryName);
		
		libraryReactPage.createLibrary(caiCallerDriver, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(caiCallerDriver, libraryName);

		// verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiCallerDriver);
		assertEquals(callTabReactPage.getTotalPage(caiCallerDriver), 0);

		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertFalse(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		caiDetailPage.selectandAddNewLibrary(caiCallerDriver, libraryList);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertTrue(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
		
		//update the library
		String newLibraryName = "UpdatedLibrary" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryList.clear();
		libraryList.add(newLibraryName);
		
		libraryReactPage.openLibraryPage(caiCallerDriver);
		libraryReactPage.selectLibrary(caiCallerDriver, libraryName);
		libraryReactPage.editLibrary(caiCallerDriver, newLibraryName);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertTrue(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		assertTrue(caiDetailPage.isLibraryAdded(caiCallerDriver, libraryList));
		
		// delete library
		libraryReactPage.openLibraryPage(caiCallerDriver);
		libraryReactPage.selectLibrary(caiCallerDriver, newLibraryName);
		libraryReactPage.deletLibrary(caiCallerDriver, newLibraryName);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertFalse(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		assertFalse(caiDetailPage.isLibraryAdded(caiCallerDriver, libraryList));
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);

		System.out.println("Test case --user_relate_new_library_with_cai_call-- passed ");
	}

	@Test(groups = {  "Regression" })
	public void user_remove_selected_library_from_cai_call() {

		System.out.println("Test case --user_remove_selected_library_from_cai_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// open Conversation AI page and move to library page
		dashboard.clickConversationAI(caiCallerDriver);
		libraryReactPage.openLibraryPage(caiCallerDriver);
		
		// Create a library
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		List<String> libraryList = new ArrayList<String>();
		libraryList.add(libraryName);
		
		libraryReactPage.createLibrary(caiCallerDriver, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(caiCallerDriver, libraryName);

		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertFalse(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		caiDetailPage.selectandAddNewLibrary(caiCallerDriver, libraryList);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertTrue(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));
		
		//unselect added library
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		caiDetailPage.unSelectAddedLibrary(caiCallerDriver, libraryName);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertFalse(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));

		// delete library
		libraryReactPage.openLibraryPage(caiCallerDriver);
		libraryReactPage.selectLibrary(caiCallerDriver, libraryName);
		libraryReactPage.deletLibrary(caiCallerDriver, libraryName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);

		System.out.println("Test case --user_remove_selected_library_from_cai_call-- passed ");
	}
	
	@Test(groups = {  "Regression" })
	public void user_add_multiple_libraries() {

		System.out.println("Test case --user_add_multiple_libraries-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// open Conversation AI page and move to library page
		dashboard.clickConversationAI(caiCallerDriver);
		libraryReactPage.openLibraryPage(caiCallerDriver);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		caiDetailPage.unSelectAllLibraries(caiCallerDriver);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		assertTrue(callTabReactPage.isNoLibraryPresntTextVisible(caiCallerDriver));
		
		//adding multiple libraries
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		List<String> libraryList = caiDetailPage.selectMultipleLibraries(caiCallerDriver, 4);
		
		callTabReactPage.navigateToCallsPage(caiCallerDriver);
		callTabReactPage.expandSearchRows(caiCallerDriver, 1);
		
		//make method to return +2 more library text and verify
		
		assertTrue(callTabReactPage.isLibraryPresentInResults(caiCallerDriver, 0, libraryList));

		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);

		System.out.println("Test case --user_add_multiple_libraries-- passed ");
	}
	
	//Verify search entire phrases for open text search in CAI to match with Transcript
	@Test(groups = {  "Regression" })
	public void verify_search_entire_phrase_to_match_with_transcript() {

		System.out.println("Test case --verify_search_entire_phrase_to_match_with_transcript-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		// open Conversation AI page and move to library page
		String transcriptSearchText = "This call will be recorded. RINGBACK At the.";
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.enterSearchText(caiCallerDriver,  "\"" + transcriptSearchText + "\"");
		assertTrue(callTabReactPage.getSearchPageRowCount(caiCallerDriver)>= 1);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiCallerDriver, "\"" + transcriptSearchText + "\""));
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiCallerDriver, "\"" + transcriptSearchText + "\""));
		
		String textToFindInRow="Transcription";
		String actualTranscriptionText = callTabReactPage.getTextToFindInCAIRow(caiCallerDriver, textToFindInRow, 0);
	
		String matchCount = HelperFunctions.getValueAccToRegex(actualTranscriptionText, "[0-9]+");
		
		assertEquals(actualTranscriptionText, textToFindInRow+": " +matchCount+ " match");
		callTabReactPage.clearAllFilters(caiCallerDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);

		System.out.println("Test case --verify_search_entire_phrase_to_match_with_transcript-- passed ");
	}
	
	//Hide caller details block when no lead or contact and  opp is associated with a recording
	@Test(groups = {  "Regression" })
	public void verify_caller_details_hidden_for_unknown_caller() {

		System.out.println("Test case --verify_caller_details_hidden_for_unknown_caller-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.enterSearchText(caiCallerDriver,  "vikram test");
		
		assertTrue(callTabReactPage.getNotificationTextAccToRow(caiCallerDriver, 0).contains("Unknown Caller")
				|| callTabReactPage.getNotificationTextAccToRow(caiCallerDriver, 0).contains("Multiple"));
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		assertFalse(caiDetailPage.isCallerDetailTabVisible(caiCallerDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);

		System.out.println("Test case --verify_caller_details_hidden_for_unknown_caller-- passed ");
	}
	
//	Verify fields with all possible data types and values
//	Verify fields visible within CAI page when Always Visible True but fields not have values
	@Test(groups = {  "Regression" }, priority=1)
	public void verify_fields_visible_if_always_visible_true_but_fields_no_values_contact() {

		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_contact-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		String emptyValueField1 = "Mailing Address";
		String emptyValueField2 = "Billing Longitude (Account)";
		String emptyValueField3 = "Data.com Key";
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		// Creating account city field
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);

		//Creating account field
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.AccountCity.displayName())) {
			String accountFieldName = "Account City (Account)";
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.AccountCity, accountFieldName, true, true, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
		
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField2));
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField3));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField1)));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField2)));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField3)));
		
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField2));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField3));
		assertEquals(customFieldsCAIMapValues.get(emptyValueField1), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField2), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField3), "n/a");
		
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);

		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_contact-- passed ");
	}
	
	//Verify Fields not visible when 'Always visible' False and fields not have values
	@Test(groups = {  "Regression" }, priority=2)
	public void verify_fields_not_visible_if_always_visible_false_and_fields_no_values_contact() {

		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_contact-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		String emptyValueField1 = "Mailing Address";
		String emptyValueField2 = "Billing Longitude (Account)";
		String emptyValueField3 = "Data.com Key";
		
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		//navigating to profile
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);
		
		// Creating account city field
		String accountFieldName = "Account City (Account)";
		if (!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.AccountCity.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.AccountCity, accountFieldName,
					true, false, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
	
		//opening call history page and clicking link
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		assertFalse(callerDetailsCAIList.contains(notVisibleField1));
		assertFalse(callerDetailsCAIList.contains(notVisibleField2));
		assertFalse(callerDetailsCAIList.contains(notVisibleField3));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, contactSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField1);
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField2);
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField3);
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField1));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField2));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField3));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values-- passed ");
	}
	
	//Removed existing showing Contact's fields from Admin and verify within CAI page
	@Test(groups = {  "Regression" }, priority=3)
	public void remove_existing_fields_from_admin_and_verify_CAI_page_contacts() {

		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_contacts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		String emptyValueField1 = "Mailing Address";
		String emptyValueField2 = "Billing Longitude (Account)";
		String emptyValueField3 = "Data.com Key";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);
		
		//Deleting account city field
		String deletedFieldName = "Account City (Account)";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.AccountCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, deletedFieldName);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
		
		//opening call history page and clicking link
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		assertFalse(callerDetailsCAIList.contains(deletedFieldName));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, contactSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, deletedFieldName);

		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(deletedFieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_contacts-- passed ");
	}
	
	//New added Contact field should start to visible within existing CAI call page
	@Test(groups = {  "Regression" }, priority=4)
	public void verify_new_added_contact_visible_within_existing_CAI_call_page_contacts() {

		System.out.println("Test case --verify_new_added_contact_visible_within_existing_CAI_call_page_contacts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		String emptyValueField1 = "Mailing Address";
		String emptyValueField2 = "Billing Longitude (Account)";
		String emptyValueField3 = "Data.com Key";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);
		
		//Deleting account city field
		String fieldName = "Account City (Account)";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.AccountCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.AccountCity, fieldName, true, false, false);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
	
		//opening call history page and clicking link
		sfHistoryPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.reloadSoftphone(caiCallerDriver);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		assertTrue(callerDetailsCAIList.contains(fieldName));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, contactSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		callScreenPage.verifyCustomFieldsVisible(caiCallerDriver, fieldName);

		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		
		System.out.println("Test case --verify_new_added_contact_visible_within_existing_CAI_call_page_contacts-- passed ");
	}
	
	//Verify Newly added Contacts fields within new CAI Calls
	@Test(groups = {  "Regression" }, priority=5)
	public void verify_new_added_contact_visible_with_new_CAI_call_contacts() {

		System.out.println("Test case --verify_new_added_contact_visible_with_new_CAI_call_contacts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		String emptyValueField1 = "Mailing Address";
		String emptyValueField2 = "Billing Longitude (Account)";
		String emptyValueField3 = "Data.com Key";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);
		
		//Deleting account city field
		String fieldName = "Account City (Account)";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.AccountCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new account city field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.AccountCity, fieldName, true, false, false);

		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.verifyCustomFieldsVisible(caiCallerDriver, fieldName);;
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
	
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_new_added_contact_visible_with_new_CAI_call_contacts-- passed ");
	}
	
	//Verify fields showing as per existing or updated ordered
	@Test(groups = {  "Regression" }, priority=6)
	public void verify_fields_showing_as_per_after_updating_from_profiles_tab_contacts() {

		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_contacts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Master Record ID (Account)";
		String notVisibleField3 = "Invisible Languages";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.navigateToContactFieldsSection(caiCallerDriver);

		profilePage.clickArrowDownIcons(caiCallerDriver, 4);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> profileCustomList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
		
		String firstNameProfileCustom = profileCustomList.get(0);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		List<String> softPhoneCustomList = callScreenPage.getAllCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			softPhoneCustomList.remove(notVisibleCustomField);
		}

		assertEquals(softPhoneCustomList, profileCustomList);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Contacts);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom list after a particular string
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		List<String> callerDetailsNameOrdered = new ArrayList<String>();
		int startIndex = callerDetailsCAIList.indexOf(firstNameProfileCustom);
		for (int i = startIndex; i < callerDetailsCAIList.size(); i++) {
			callerDetailsNameOrdered.add(callerDetailsCAIList.get(i));
		}
		
		assertEquals(profileCustomList, callerDetailsNameOrdered);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_contacts-- passed ");
	}
	
	/////////////////////////////////////////////////Leads//////////////////////////////////////////////////
	
	@Test(groups = {  "Regression" }, priority=7)
	public void verify_fields_visible_if_always_visible_true_but_fields_no_values_lead_section() {

		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_lead_section-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		String emptyValueField1 = "Zip/Postal Code";
		String emptyValueField2 = "Rating";
		String emptyValueField3 = "Fax";
		String emptyValueField4 = "Company Name (CreatedBy)";
		
		//verify cai analytics not visible on support account intelligent dialer tab
		dashboard.switchToTab(caiSupportDriver, 2);
		dashboard.clickAccountsLink(caiSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(caiSupportDriver);
		assertFalse(accountIntelligentDialerTab.isCAIAnalyticsVisible(caiSupportDriver));
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);

		//Creating physical city field
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.PhysicalCity.displayName())) {
			String accountFieldName = "Physical City";
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.PhysicalCity, accountFieldName, true, false, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> leadSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			leadSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiSupportDriver);

		// adding lead if not
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		if (!callScreenPage.isCallerMultiple(caiCallerDriver) && callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(caiCallerDriver, "CAI Support Lead",
					CONFIG.getProperty("contact_account_name"));
		}

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiSupportDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField2));
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField3));
		assertTrue(customFieldsSoftphoneMapValues.containsKey(emptyValueField4));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField1)));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField2)));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField3)));
		assertTrue(Strings.isNullOrEmpty(customFieldsSoftphoneMapValues.get(emptyValueField4)));
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		customFieldsSoftphoneMapValues.remove(emptyValueField4);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField2));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField3));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField4));

		assertEquals(customFieldsCAIMapValues.get(emptyValueField1), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField2), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField3), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField4), "n/a");

		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		customFieldsCAIMapValues.remove(emptyValueField4);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		
		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_lead_section-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=8)
	public void verify_fields_not_visible_if_always_visible_false_and_fields_no_values_lead() {

		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_lead-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		String emptyValueField1 = "Zip/Postal Code";
		String emptyValueField2 = "Rating";
		String emptyValueField3 = "Fax";
		String emptyValueField4 = "Company Name (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);
		
		//Creating physical city field
		String accountFieldName = "Physical City";
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.PhysicalCity.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.PhysicalCity, accountFieldName, true, false, false);
		}
	
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> leadSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			leadSFFieldsList.remove(notVisibleCustomField);
		}
	
		//opening call history page and clicking link
		sfHistoryPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField1), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField2), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField3), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField4), "n/a");
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		System.out.println(callerDetailsCAIList);
		assertFalse(callerDetailsCAIList.contains(notVisibleField1));
		assertFalse(callerDetailsCAIList.contains(notVisibleField2));
		assertFalse(callerDetailsCAIList.contains(notVisibleField3));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, leadSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiSupportDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiSupportDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField1);
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField2);
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, notVisibleField3);
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		customFieldsSoftphoneMapValues.remove(emptyValueField4);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField1));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField2));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField3));

		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		customFieldsCAIMapValues.remove(emptyValueField4);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		
		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_leads-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=9)
	public void remove_existing_fields_from_admin_and_verify_CAI_page_leads() { 

		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_leads-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		String emptyValueField1 = "Zip/Postal Code";
		String emptyValueField2 = "Rating";
		String emptyValueField3 = "Fax";
		String emptyValueField4 = "Company Name (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);
		

		//Deleting account city field
		String deletedFieldName = "Physical City";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.PhysicalCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, deletedFieldName);
		}
	
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> contactSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			contactSFFieldsList.remove(notVisibleCustomField);
		}
		
		//opening call history page and clicking link
		sfHistoryPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		assertFalse(callerDetailsCAIList.contains(deletedFieldName));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, contactSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		callScreenPage.verifyCustomFieldsInvisible(caiCallerDriver, deletedFieldName);

		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		customFieldsSoftphoneMapValues.remove(emptyValueField4);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, contactSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(deletedFieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		customFieldsCAIMapValues.remove(emptyValueField4);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		
		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_leads-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=10)
	public void verify_new_added_lead_visible_within_existing_CAI_call_page() {

		System.out.println("Test case --verify_new_added_lead_visible_within_existing_CAI_call_page-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		String emptyValueField1 = "Zip/Postal Code";
		String emptyValueField2 = "Rating";
		String emptyValueField3 = "Fax";
		String emptyValueField4 = "Company Name (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);
		
		//Deleting account city field
		String fieldName = "Physical City";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.PhysicalCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.PhysicalCity, fieldName, true, true, false);

		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> leadSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			leadSFFieldsList.remove(notVisibleCustomField);
		}
	
		//opening call history page and clicking link
		sfHistoryPage.switchToTab(caiCallerDriver, 1);
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		assertTrue(callerDetailsCAIList.contains(fieldName));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(callerDetailsCAIList, leadSFFieldsList));
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 1);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		callScreenPage.verifyCustomFieldsVisible(caiCallerDriver, fieldName);

		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		customFieldsSoftphoneMapValues.remove(emptyValueField4);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		customFieldsCAIMapValues.remove(emptyValueField4);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		
		System.out.println("Test case --verify_new_added_lead_visible_within_existing_CAI_call_page-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=11)
	public void verify_new_added_lead_visible_with_new_CAI_call() {

		System.out.println("Test case --verify_new_added_lead_visible_with_new_CAI_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		String emptyValueField1 = "Zip/Postal Code";
		String emptyValueField2 = "Rating";
		String emptyValueField3 = "Fax";
		String emptyValueField4 = "Company Name (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);
		
		//Deleting account city field
		String fieldName = "Physical City";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.PhysicalCity.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new account city field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.PhysicalCity, fieldName, true, true, false);

		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> leadSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			leadSFFieldsList.remove(notVisibleCustomField);
		}

		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiSupportDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiSupportDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.verifyCustomFieldsVisible(caiCallerDriver, fieldName);;
		
		//getting softphone custom caller details in map
		Map<String, String> customFieldsSoftphoneMapValues = callScreenPage.getAllCustomFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		customFieldsSoftphoneMapValues.remove(emptyValueField1);
		customFieldsSoftphoneMapValues.remove(emptyValueField2);
		customFieldsSoftphoneMapValues.remove(emptyValueField3);
		customFieldsSoftphoneMapValues.remove(emptyValueField4);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, leadSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		customFieldsCAIMapValues.remove(emptyValueField3);
		customFieldsCAIMapValues.remove(emptyValueField4);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(customFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(customFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		
		System.out.println("Test case --verify_new_added_lead_visible_with_new_CAI_call-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=12)
	public void verify_fields_showing_as_per_after_updating_from_profiles_tab_leads() {

		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_leads-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Automation Invisible Field";
		String notVisibleField2 = "Invisible Industry";
		String notVisibleField3 = "Invisible Website";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickLeadFields(caiCallerDriver);
		
		profilePage.clickArrowDownIcons(caiCallerDriver, 4);

		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField3));
		
		//get custom field name on support salesforce tab
		List<String> profileCustomList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
		
		String firstNameProfileCustom = profileCustomList.get(0);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_support_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		List<String> softPhoneCustomList = callScreenPage.getAllCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			softPhoneCustomList.remove(notVisibleCustomField);
		}
		
		assertEquals(softPhoneCustomList, profileCustomList);
		
		//opening call history page and clicking link
		sfHistoryPage.openCallsHistoryPage(caiCallerDriver);
		sfHistoryPage.selectMatchTypeFilter(caiCallerDriver, MatchTypeFilters.Leads);
		sfHistoryPage.openHistoryCAICallEntryByIndex(caiCallerDriver, 0);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom list after a particular string
		caiDetailPage.clickCallerDetailTab(caiCallerDriver);
		List<String> callerDetailsCAIList = caiDetailPage.getCallerDetailsFieldsList(caiCallerDriver, CONFIG.getProperty("softphone_task_related_Account_update"));
		List<String> callerDetailsNameOrdered = new ArrayList<String>();
		int startIndex = callerDetailsCAIList.indexOf(firstNameProfileCustom);
		for (int i = startIndex; i < callerDetailsCAIList.size(); i++) {
			callerDetailsNameOrdered.add(callerDetailsCAIList.get(i));
		}
		
		assertEquals(profileCustomList, callerDetailsNameOrdered);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		
		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_leads-- passed ");
	}

	///////////////////////////////////////////////////Accounts/////////////////////////////////////////////////////////
	
	@Test(groups = {  "Regression" }, priority=13)
	public void verify_fields_visible_if_always_visible_true_but_fields_no_values_accounts() {

		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_accounts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		String emptyValueField1 = "Billing City";
		String emptyValueField2 = "Fax (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);

		//Creating Industry field
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			String accountFieldName = "Industry";
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Industry, accountFieldName,
					true, false, false);
		}

		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> accountSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			accountSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), relatedAccountName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedAccountName, accountSFFieldsList);
		System.out.println(relatedFieldsSoftphoneMapValues);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField1));
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField2));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField1)));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField2)));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> accountDetailsCAIList = caiDetailPage.getAccountDetailsNameList(caiCallerDriver, relatedAccountName);
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(accountDetailsCAIList, accountSFFieldsList));
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField1), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField2), "n/a");
		
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, accountSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField2));
		assertEquals(customFieldsCAIMapValues.get(emptyValueField1), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField2), "n/a");
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_account-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=14)
	public void verify_fields_not_visible_if_always_visible_false_and_fields_no_values_accounts() {

		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_accounts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		String emptyValueField1 = "Billing City";
		String emptyValueField2 = "Fax (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);
		
		//Creating Industry field
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			String accountFieldName = "Industry";
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Industry, accountFieldName,
					true, false, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> accountSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			accountSFFieldsList.remove(notVisibleCustomField);
		}
		
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), relatedAccountName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedAccountName, accountSFFieldsList);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickAccountDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, accountSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField1));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField2));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_account-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=15)
	public void remove_existing_fields_from_admin_and_verify_CAI_page_account() {

		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		String emptyValueField1 = "Billing City";
		String emptyValueField2 = "Fax (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);

		//Deleting account city field
		String deletedFieldName = "Industry";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, deletedFieldName);
		}
	
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> accountSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			accountSFFieldsList.remove(notVisibleCustomField);
		}
		
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), relatedAccountName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedAccountName, accountSFFieldsList);
		assertFalse(relatedFieldsSoftphoneMapValues.containsKey(deletedFieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickAccountDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, accountSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField1));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField2));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_account-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=16)
	public void verify_new_added_account_visible_within_existing_CAI_call_page() {

		System.out.println("Test case --verify_new_added_account_visible_within_existing_CAI_call_page-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		String emptyValueField1 = "Billing City";
		String emptyValueField2 = "Fax (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);

		//Deleting account city field
		String accountFieldName = "Industry";
		String accountFieldNameValue = "Engineering";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, accountFieldName);
		}
		
		//creating the new field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Industry, accountFieldName,
				true, false, false);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> accountSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			accountSFFieldsList.remove(notVisibleCustomField);
		}
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
	    callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedAccountName);
	    callScreenPage.verifyRelatedRecordFields(caiCallerDriver, accountFieldName, accountFieldNameValue);

		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedAccountName, accountSFFieldsList);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		//opening call activity page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedAccountName);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickAccountDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, accountSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(accountFieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);

		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_new_added_account_visible_within_existing_CAI_call_page-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=17)
	public void verify_new_added_account_visible_with_new_CAI_call() {

		System.out.println("Test case --verify_new_added_account_visible_with_new_CAI_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		String emptyValueField1 = "Billing City";
		String emptyValueField2 = "Fax (CreatedBy)";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);

		//Creating Industry field
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			String accountFieldName = "Industry";
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Industry, accountFieldName,
					true, false, false);
		}
		
		//Deleting account city field
		String fieldName = "Industry";
		String fieldNameValue = "Engineering";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Industry.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new account city field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Industry, fieldName,
				true, false, false);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> accountSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			accountSFFieldsList.remove(notVisibleCustomField);
		}
			
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), relatedAccountName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
	    callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedAccountName);
	    callScreenPage.verifyRelatedRecordFields(caiCallerDriver, fieldName, fieldNameValue);
	
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedAccountName, accountSFFieldsList);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
	
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickAccountDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, accountSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_new_added_account_visible_with_new_CAI_call-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=18)
	public void verify_fields_showing_as_per_after_updating_from_profiles_tab_account() {

		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_account-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Invisible Website";
		String notVisibleField2 = "Invisible Annual Revenue";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickAccountFields(caiCallerDriver);
		profilePage.clickArrowDownIcons(caiCallerDriver, 4);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> profileCustomList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
	
		String firstNameProfileCustom = profileCustomList.get(0);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		List<String> softPhoneRelatedList = callScreenPage.getRelatedRecordsFieldsList(caiCallerDriver, relatedAccountName);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
	
		assertEquals(softPhoneRelatedList, profileCustomList);
		
		//opening call history page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedAccountName);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom list after a particular string
		caiDetailPage.clickAccountDetailTab(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> accountDetailsCAIList = caiDetailPage.getAccountDetailsNameList(caiCallerDriver, relatedAccountName);
		List<String> accountDetailsNameOrdered = new ArrayList<String>();
		int startIndex = accountDetailsCAIList.indexOf(firstNameProfileCustom);
		for (int i = startIndex; i < accountDetailsCAIList.size(); i++) {
			accountDetailsNameOrdered.add(accountDetailsCAIList.get(i));
		}
		
		assertEquals(profileCustomList, accountDetailsNameOrdered);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_account-- passed ");
	}

	///////////////////////////////////////////////////////Opportunity////////////////////////////////////////////////////
	
	
	@Test(groups = {  "Regression" }, priority=19)
	public void verify_fields_visible_if_always_visible_true_but_fields_no_values_opportunity() {

		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_opportunity-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		String emptyValueField1 = "Fax (CreatedBy)";
		String emptyValueField2 = "Amount";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);

		//Creating Stage field
		String accountFieldName = "Stage";
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Stage.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Stage, accountFieldName,
					true, false, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> opportunitySFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			opportunitySFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedOpportunityName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedOpportunityName);
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, notVisibleField1));
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, notVisibleField2));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedOpportunityName, opportunitySFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField1));
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField2));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField1)));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField2)));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> opportunityDetailsCAIList = caiDetailPage.getOpportunityDetailsNameList(caiCallerDriver);
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField1));
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField2));
		
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(opportunityDetailsCAIList, opportunitySFFieldsList));
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField1), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField2), "n/a");

		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, opportunitySFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField2));
		assertEquals(customFieldsCAIMapValues.get(emptyValueField1), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField2), "n/a");
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_opportunity-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=20)
	public void verify_fields_not_visible_if_always_visible_false_and_fields_no_values_opportunity() {

		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_accounts-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		String emptyValueField1 = "Fax (CreatedBy)";
		String emptyValueField2 = "Amount";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);

		//Creating Stage field
		String accountFieldName = "Stage";
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Stage.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Stage, accountFieldName,
					true, false, false);
		}
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> opportunitySFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			opportunitySFFieldsList.remove(notVisibleCustomField);
		}
			
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedOpportunityName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedOpportunityName, opportunitySFFieldsList);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickOpportunityDetailTab(caiCallerDriver);
		List<String> opportunityDetailsCAIList = caiDetailPage.getOpportunityDetailsNameList(caiCallerDriver);
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField1));
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField2));
		
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, opportunitySFFieldsList);
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_opportunity-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=21)
	public void remove_existing_fields_from_admin_and_verify_CAI_page_opportunity() {

		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_opportunity-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		String emptyValueField1 = "Fax (CreatedBy)";
		String emptyValueField2 = "Amount";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);

		//Deleting account city field
		String deletedFieldName = "Stage";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Stage.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, deletedFieldName);
		}
	
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> opportunitySFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			opportunitySFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedOpportunityName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedOpportunityName, opportunitySFFieldsList);
		assertFalse(relatedFieldsSoftphoneMapValues.containsKey(deletedFieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> opportunityDetailsCAIList = caiDetailPage.getOpportunityDetailsNameList(caiCallerDriver);
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField1));
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField2));
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickOpportunityDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, opportunitySFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField1));
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField2));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_opportunity-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=22)
	public void verify_new_added_opportunity_visible_within_existing_CAI_call_page() {

		System.out.println("Test case --verify_new_added_opportunity_visible_within_existing_CAI_call_page-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);


		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		String emptyValueField1 = "Fax (CreatedBy)";
		String emptyValueField2 = "Amount";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);

		//Deleting account city field
		String fieldName = "Stage";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Stage.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Stage, fieldName,
				true, false, false);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> opportunitySFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			opportunitySFFieldsList.remove(notVisibleCustomField);
		}
			
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedOpportunityName, opportunitySFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(fieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		//opening call history page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedOpportunityName);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> opportunityDetailsCAIList = caiDetailPage.getOpportunityDetailsNameList(caiCallerDriver);
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField1));
		assertFalse(opportunityDetailsCAIList.contains(notVisibleField2));
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickOpportunityDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, opportunitySFFieldsList);
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);

		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		
		System.out.println("Test case --verify_new_added_opportunity_visible_within_existing_CAI_call_page-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=23)
	public void verify_new_added_opportunity_visible_with_new_CAI_call() {

		System.out.println("Test case --verify_new_added_opportunity_visible_with_new_CAI_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		String emptyValueField1 = "Fax (CreatedBy)";
		String emptyValueField2 = "Amount";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);

		//Deleting account city field
		String fieldName = "Stage";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Stage.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new account city field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Stage, fieldName,
				true, false, false);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> opportunitySFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			opportunitySFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), relatedOpportunityName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedOpportunityName, opportunitySFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(fieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
	
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickOpportunityDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, opportunitySFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_new_added_opportunity_visible_with_new_CAI_call-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=24)
	public void verify_fields_showing_as_per_after_updating_from_profiles_tab_opportunity() {

		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_opportunity-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField1 = "Invisible Next Step";
		String notVisibleField2 = "Invisible Description";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickOpportunityFields(caiCallerDriver);
		profilePage.clickArrowDownIcons(caiCallerDriver, 3);
		
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField1));
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField2));
		
		//get custom field name on support salesforce tab
		List<String> profileCustomList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
			
		String firstNameProfileCustom = profileCustomList.get(0);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		List<String> softPhoneRelatedList = callScreenPage.getRelatedRecordsFieldsList(caiCallerDriver, relatedOpportunityName);
		if(softPhoneRelatedList.contains("Account Name")) {
			softPhoneRelatedList.remove("Account Name");
		}
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}

		assertEquals(softPhoneRelatedList, profileCustomList);
		
		//opening call history page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedOpportunityName);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> opportunityDetailsCAIList = caiDetailPage.getOpportunityDetailsNameList(caiCallerDriver);
		List<String> opportunityDetailsNameOrdered = new ArrayList<String>();
		int startIndex = opportunityDetailsCAIList.indexOf(firstNameProfileCustom);
		for (int i = startIndex; i < opportunityDetailsCAIList.size(); i++) {
			opportunityDetailsNameOrdered.add(opportunityDetailsCAIList.get(i));
		}
		
		assertEquals(profileCustomList, opportunityDetailsNameOrdered);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_opportunity-- passed ");
	}
	
	///////////////////////////////////////////////////////Case/////////////////////////////////////////////////////////////////////
	
	
	@Test(groups = {  "Regression" }, priority=25)
	public void verify_fields_visible_if_always_visible_true_but_fields_no_values_case() {

		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_opportunity-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		String emptyValueField1 = "Empty Case Reason";
		String emptyValueField2 = "Empty Description";
			
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);

		//Creating Status field
		String accountFieldName = "Status";
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Status.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Status, accountFieldName,
					true, true, false);
		}
		
		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> caseSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			caseSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), relatedCaseName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedCaseName);
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, notVisibleField));
		assertTrue(callScreenPage.isRelatedRecordVisible(caiCallerDriver, emptyValueField1));
		assertTrue(callScreenPage.isRelatedRecordVisible(caiCallerDriver, emptyValueField2));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedCaseName, caseSFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField1));
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(emptyValueField2));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField1)));
		assertTrue(Strings.isNullOrEmpty(relatedFieldsSoftphoneMapValues.get(emptyValueField2)));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		assertFalse(caseDetailsCAIList.contains(notVisibleField));
		assertTrue(HelperFunctions.isList1ContainsValuesOfList2(caseDetailsCAIList, caseSFFieldsList));
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField1), "n/a");
		assertEquals(caiDetailPage.getValueOfDetailField(caiCallerDriver, emptyValueField2), "n/a");

		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, caseSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField1));
		assertTrue(customFieldsCAIMapValues.containsKey(emptyValueField2));
		assertEquals(customFieldsCAIMapValues.get(emptyValueField1), "n/a");
		assertEquals(customFieldsCAIMapValues.get(emptyValueField2), "n/a");
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_visible_if_always_visible_true_but_fields_no_values_opportunity-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=26)
	public void verify_fields_not_visible_if_always_visible_false_and_fields_no_values_case() {

		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_case-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		String emptyValueField1 = "Empty Case Reason";
		String emptyValueField2 = "Empty Description";
			
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);

		//Creating Status field
		String accountFieldName = "Status";
		if(!profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Status.displayName())) {
			profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Status, accountFieldName,
					true, true, false);
		}
		
		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> caseSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			caseSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), relatedCaseName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedCaseName);
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, notVisibleField));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedCaseName, caseSFFieldsList);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		assertFalse(caseDetailsCAIList.contains(notVisibleField));
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCaseDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, caseSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(notVisibleField));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_not_visible_if_always_visible_false_and_fields_no_values_case-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=27)
	public void remove_existing_fields_from_admin_and_verify_CAI_page_case() {

		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_case-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		String emptyValueField1 = "Empty Case Reason";
		String emptyValueField2 = "Empty Description";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);
		
		//Deleting status field
		String deletedFieldName = "Status";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Status.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, deletedFieldName);
		}
	
		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> caseSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			caseSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), relatedCaseName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedCaseName);
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, deletedFieldName));
		assertFalse(callScreenPage.isRelatedRecordVisible(caiCallerDriver, notVisibleField));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedCaseName, caseSFFieldsList);
		assertFalse(relatedFieldsSoftphoneMapValues.containsKey(deletedFieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		assertFalse(caseDetailsCAIList.contains(deletedFieldName));
		
		//getting custom fields values on Call player page according to web app salesforce fields
		caiDetailPage.clickCaseDetailTab(caiCallerDriver);
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, caseSFFieldsList);
		assertFalse(customFieldsCAIMapValues.containsKey(deletedFieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --remove_existing_fields_from_admin_and_verify_CAI_page_case-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=28)
	public void verify_new_added_case_visible_within_existing_CAI_call_page() {

		System.out.println("Test case --verify_new_added_case_visible_within_existing_CAI_call_page-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		String emptyValueField1 = "Empty Case Reason";
		String emptyValueField2 = "Empty Description";
			
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);


		//Deleting account city field
		String fieldName = "Status";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Status.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating the new field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Status, fieldName,
				true, true, false);
		
		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> caseSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			caseSFFieldsList.remove(notVisibleCustomField);
		}
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedCaseName);
		assertTrue(callScreenPage.isRelatedRecordVisible(caiCallerDriver, fieldName));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedCaseName, caseSFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(fieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		//opening call history page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedCaseName);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		assertTrue(caseDetailsCAIList.contains(fieldName));
		
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, caseSFFieldsList);
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);

		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		
		System.out.println("Test case --verify_new_added_case_visible_within_existing_CAI_call_page-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=29)
	public void verify_new_added_case_visible_with_new_CAI_call() {

		System.out.println("Test case --verify_new_added_case_visible_with_new_CAI_call-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		String emptyValueField1 = "Empty Case Reason";
		String emptyValueField2 = "Empty Description";
			
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);

		//Deleting status field
		String fieldName = "Status";
		if(profilePage.isDeleteIconVisible(caiCallerDriver, SalesForceFields.Status.displayName())) {
			accountSalesforceTab.deleteAdditionalField(caiCallerDriver, fieldName);
		}
		
		//creating Status field
		profilePage.createAccountSalesForceField(caiCallerDriver, SalesForceFields.Status, fieldName,
				true, false, false);

		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> caseSFFieldsList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			caseSFFieldsList.remove(notVisibleCustomField);
		}
	
		//making a new cai recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		callTabReactPage.reloadSoftphone(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callTabReactPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callTabReactPage.idleWait(70);
		callToolsPanel.linkRelatedRecords(caiCallerDriver, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), relatedCaseName);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callScreenPage.expandRelatedRecordDetails(caiCallerDriver, relatedCaseName);
		assertTrue(callScreenPage.isRelatedRecordVisible(caiCallerDriver, fieldName));
		
		//getting softphone custom caller details in map
		Map<String, String> relatedFieldsSoftphoneMapValues = callScreenPage.getAllRelatedFieldsMapValue(caiCallerDriver, relatedCaseName, caseSFFieldsList);
		assertTrue(relatedFieldsSoftphoneMapValues.containsKey(fieldName));
		relatedFieldsSoftphoneMapValues.remove(emptyValueField1);
		relatedFieldsSoftphoneMapValues.remove(emptyValueField2);
		
		softPhoneActivityPage.openCAILinkNewTab(caiCallerDriver, callSubject);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
	
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		assertTrue(caseDetailsCAIList.contains(fieldName));
		Map<String, String> customFieldsCAIMapValues = caiDetailPage.getCustomCAIDetailsFieldsMapValue(caiCallerDriver, caseSFFieldsList);
		assertTrue(customFieldsCAIMapValues.containsKey(fieldName));
		customFieldsCAIMapValues.remove(emptyValueField1);
		customFieldsCAIMapValues.remove(emptyValueField2);
		
		//verify custom fields keyset and values of softphone with caller details on cai
		assertEquals(relatedFieldsSoftphoneMapValues.keySet(), customFieldsCAIMapValues.keySet());
		assertEquals(relatedFieldsSoftphoneMapValues, customFieldsCAIMapValues);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_new_added_case_visible_with_new_CAI_call-- passed ");
	}
	
	@Test(groups = {  "Regression" }, priority=30)
	public void verify_fields_showing_as_per_after_updating_from_profiles_tab_case() {

		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_case-- started ");

		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		String notVisibleField = "Invisible Case Type";
		
		//navigating to profile page
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickOnUserProfile(caiCallerDriver);
		
		dashboard.navigateToProfilesSection(caiCallerDriver);
		if(profilePage.isWelcomeProfilesSectionVisible(caiCallerDriver)) {
			profilePage.clickGetStartedButton(caiCallerDriver);
		}
		
		profilePage.clickPencilIconProfile(caiCallerDriver, profileName);
		profilePage.clickCaseFields(caiCallerDriver);
		profilePage.clickArrowDownIcons(caiCallerDriver, 3);
	
		// get custom field name on support salesforce tab
		List<String> notVisibleCustomFieldsList = profilePage.getNotVisibleCustomFields(caiCallerDriver);
		assertTrue(notVisibleCustomFieldsList.contains(notVisibleField));
		
		//get custom field name on support salesforce tab
		List<String> profileCustomList = accountSalesforceTab.getCustomFieldsNameList(caiCallerDriver);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			profileCustomList.remove(notVisibleCustomField);
		}
	
		String firstNameProfileCustom = profileCustomList.get(0);
		
		//Open dial pad of driver and dial to support driver
		callTabReactPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.reloadSoftphone(caiCallerDriver);

		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.isCallHoldButtonVisible(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		
		List<String> softPhoneRelatedList = callScreenPage.getRelatedRecordsFieldsList(caiCallerDriver, relatedCaseName);
		System.out.println(softPhoneRelatedList);
		for(String notVisibleCustomField : notVisibleCustomFieldsList) {
			softPhoneRelatedList.remove(notVisibleCustomField);
		}
	
		assertEquals(softPhoneRelatedList, profileCustomList);
		
		//opening call history page and clicking link
		softPhoneActivityPage.clickRelatedRecordCAILink(caiCallerDriver, relatedCaseName);
		caiDetailPage.verifyRecordingPlayerVisible(caiCallerDriver);
		
		//getting custom fields values on Call player page according to web app salesforce fields
		List<String> caseDetailsCAIList = caiDetailPage.getCaseDetailsNameList(caiCallerDriver, relatedCaseName);
		List<String> caseDetailsNameOrdered = new ArrayList<String>();
		int startIndex = caseDetailsCAIList.indexOf(firstNameProfileCustom);
		for (int i = startIndex; i < caseDetailsCAIList.size(); i++) {
			caseDetailsNameOrdered.add(caseDetailsCAIList.get(i));
		}
		
		assertEquals(profileCustomList, caseDetailsNameOrdered);
		
		caiDetailPage.closeTab(caiCallerDriver);
		caiDetailPage.switchToTab(caiCallerDriver, 2);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		
		System.out.println("Test case --verify_fields_showing_as_per_after_updating_from_profiles_tab_case-- passed ");
	}
	
	//Verify Support user can not view "Delete" option for a Video and Audio conversation
	@Test(groups = { "Regression" })
	public void verify_support_user_is_not_able_to_view_delete_option() {
		System.out.println("Test case --verify_support_user_is_not_able_to_view_delete_option-- started ");

		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);

		dashboard.clickConversationAI(caiSupportDriver);
		callTabReactPage.selectAgentNameFromFilter(caiSupportDriver, "Cai User1");
	
		callTabReactPage.openConversationDetails(caiSupportDriver, 0);
		assertFalse(caiDetailPage.isActionDropDownItemVisible(caiSupportDriver, actionsDropdownItems.Delete));
		
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_support_user_is_not_able_to_view_delete_option-- passed ");
	}
	
	
	//Verify Agent user can not view "Delete" option for a Video and Audio conversation
	@Test(groups = { "Regression" })
	public void verify_agent_user_is_not_able_to_view_delete_option() {
		System.out.println("Test case --verify_agent_user_is_not_able_to_view_delete_option-- started ");

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashboard.clickConversationAI(caiVerifyDriver);
		callTabReactPage.selectAgentNameFromFilter(caiVerifyDriver, "Cai User1");
	
		callTabReactPage.openConversationDetails(caiVerifyDriver, 0);
		assertFalse(caiDetailPage.isActionDropDownItemVisible(caiVerifyDriver, actionsDropdownItems.Delete));
		
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_agent_user_is_not_able_to_view_delete_option-- passed ");
	}
	
	//Verify Admin can view and "Delete" option for an Audio conversation
	@Test(groups = { "Regression" })
	public void verify_admin_user_is_not_able_to_view_delete_option() {
		System.out.println("Test case --verify_admin_user_is_not_able_to_view_delete_option-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		callTabReactPage.selectAgentNameFromFilter(caiCallerDriver, "Cai User1");
	
		callTabReactPage.openConversationDetails(caiCallerDriver, 0);
		assertTrue(caiDetailPage.isActionDropDownItemVisible(caiCallerDriver, actionsDropdownItems.Delete));
		
		caiDetailPage.selectActionDropdownOption(caiCallerDriver, actionsDropdownItems.Delete);
		caiDetailPage.clickConfirmDeleteButton(caiCallerDriver);
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashboard.switchToTab(caiSupportDriver, 2);
		dashboard.clickOnUserProfile(caiSupportDriver);
		dashboard.clickAccountsLink(caiSupportDriver);		
	
		// navigating to accounts and searching your account
		logsTab.navigateToAccountLogTab(caiSupportDriver);
		logsTab.verifySalesforceErrorLogs(caiSupportDriver, "Recording", CONFIG.getProperty("qa_cai_caller_name"));
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_admin_user_is_not_able_to_view_delete_option-- passed ");
	}
	
	//Interruptions: Metric block should visible on the Call Detail page
	//Talk Rate: Metric block should visible on the Call Detail page
	//Average Talk Streak: Block should visible on Call Detail page
	//Voice Energy: Metric block should visible on the Call Detail page
	//Vocabulary : Block should visible on Call Detail page
	@Test(groups = { "Regression" })
	public void verify_different_metric_block_on_call_detail_page() {
		System.out.println("Test case --verify_different_metric_block_on_call_detail_page-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.openNewBlankTab(caiCallerDriver);
		dashboard.switchToTab(caiCallerDriver, dashboard.getTabCount(caiCallerDriver));
		caiCallerDriver.get("https://analytics-qa.ringdna.net/rc1344373");
		dashboard.isPaceBarInvisible(caiCallerDriver);
		
		//wait until play button visible
		caiDetailPage.verifyRecordingPlayButtonVisible(caiCallerDriver);
		
		//interruption talk metric is visible
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiCallerDriver, talkMetricsItems.Interruptions));
		//talk rates talk metric is visible 
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiCallerDriver, talkMetricsItems.TalkRates));
		//average talk streak talk metric is visible 
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiCallerDriver, talkMetricsItems.AverageTalkStreak));
		//Vocabulary metric is visible
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiCallerDriver, talkMetricsItems.Vocabulary));
		//Voice Energy metric is visible
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiCallerDriver, talkMetricsItems.VoiceEnergy));
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_different_metric_block_on_call_detail_page-- passed ");
	}
	
	//Talk Rate: one side recording (Agent Only) Metric block should visible on the Call Detail page
	@Test(groups = { "Regression" })
	public void verify_talk_rates_metric_block_on_call_detail_page_on_agent() {
		System.out.println("Test case --verify_talk_rates_metric_block_on_call_detail_page_on_agent-- started ");

		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashboard.openNewBlankTab(caiVerifyDriver);
		dashboard.switchToTab(caiVerifyDriver, dashboard.getTabCount(caiVerifyDriver));
		caiVerifyDriver.get("https://analytics-qa.ringdna.net/rc1344373");
		dashboard.isPaceBarInvisible(caiVerifyDriver);
		
		//wait until play button visible
		caiDetailPage.verifyRecordingPlayButtonVisible(caiVerifyDriver);
		
		//talk rates talk metric is visible 
		assertTrue(caiDetailPage.isTalkMetricsItemsVisible(caiVerifyDriver, talkMetricsItems.TalkRates));
		
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_talk_rates_metric_block_on_call_detail_page_on_agent-- passed ");
	}
	
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Product Sanity" }, alwaysRun = true)
	public void afterClass() {
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		dashboard.switchToTab(caiCallerDriver, 2);
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.clickOnUserProfile(caiCallerDriver);
		usersPage.enableConversationAIManagerBtn(caiCallerDriver);
		usersPage.enableConversationAnalyticsBtn(caiCallerDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiCallerDriver);
		adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
		adminCallRecordingTab.disableRestrictCallRecordingSetting(caiCallerDriver);
		adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
		adminCallRecordingTab.closeTab(caiCallerDriver);
		adminCallRecordingTab.switchToTab(caiCallerDriver, 2);
		driverUsed.put("caiCallerDriver", false);
	}
	
}
