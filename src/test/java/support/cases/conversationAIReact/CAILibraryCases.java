/**
 * @author Abhishek Gupta
 *
 */
package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.CallsTabReactPage.SortOptions;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.conversationAIReact.SavedSearchPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CAILibraryCases extends SupportBase{
	
	CallsTabReactPage 			callTabReactPage 			= new CallsTabReactPage();
	CallToolsPanel 				callToolsPanel 				= new CallToolsPanel();
	ConversationDetailPage 		conversationDetailPage 		= new ConversationDetailPage();
	LibraryReactPage			libraryReactPage			= new LibraryReactPage();
	Dashboard 					dashboard 					= new Dashboard();
	HelperFunctions 			helperFunctions 			= new HelperFunctions();
	ReportThisCallPage			reportThisCallPage 			= new ReportThisCallPage();
	SavedSearchPage				savedSearchPage				= new SavedSearchPage();
	SeleniumBase 				seleniumBase 				= new SeleniumBase();
	SoftphoneBase 				softphoneBase 				= new SoftphoneBase();
	SoftphoneCallHistoryPage	softphoneCallHistoryPage	= new SoftphoneCallHistoryPage();
	SoftPhoneCalling 			softphoneCalling 			= new SoftPhoneCalling();
	UsersPage 					usersPage					= new UsersPage();
	UserIntelligentDialerTab 	userIntelligentDialerTab	= new UserIntelligentDialerTab();
	
	//Library Tab - No data for selected Library
	//Verify CAI library empty states
	@Test(groups = { "MediumPriority","Regression" })
	public void cai_react_add_library(){
		
		System.out.println("Test case --cai_react_add_library-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		
		// Create a library
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.createLibrary(caiDriver1, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiDriver1);

		//Verify that page count is 0
		assertEquals(callTabReactPage.getTotalPage(caiDriver1), 0);
		
		//Enter a search text
		callTabReactPage.enterSearchText(caiDriver1, "Test");
		
		//verify that on search message and image is appearing
		libraryReactPage.verifyLibraryNoSearchMessage(caiDriver1);
		
		//navigate to saved search tab and verify that no library message and image is appearing
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		savedSearchPage.verifyLibNoSaveSearchMessage(caiDriver1);
		
		//now enter some text to search a saved search. Verify that no result message and image is appearing
		savedSearchPage.enterSavedSearchText(caiDriver1, "Test");
		savedSearchPage.verifyLibNoSearchSavedSearchMsg(caiDriver1);
		
		//delete library
		libraryReactPage.deletLibrary(caiDriver1, libraryName);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user sort calls of library by 'Oldest to newest'/ Newest to oldest order
	@Test(groups = { "Regression" })
	public void cai_react_lib_sort_time(){
		
		System.out.println("Test case --cai_react_lib_sort_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		//navigate to saved seach page of library tab
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);	
		
		//Set Sort Type as Old to new and sort records
		SortOptions sortType = SortOptions.OldtoNew	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from old to new order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as New to Old and sort records
		sortType = SortOptions.NewtoOld	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from new to old order
		resultList.clear();
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Setting driver used to false as this test case is pass
		callTabReactPage.pressEscapeKey(caiDriver1);
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Click on any Elastic search filter from Library tab will redirect user to CALLS tab with filter applied
	@Test(groups = { "MediumPriority" })
	public void cai_react_elastic_search_agent_name(){
		
		System.out.println("Test case --cai_react_elastic_search_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		// Set the filter type to Agent
		CallDataFilterType filterType = CallDataFilterType.Agent;
		List<String> tokenList = new ArrayList<String>();
		
		//click on Agent Name and verify that it takes user to Calls page and filter is applied
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		callTabReactPage.verifyUserOnCallsPage(caiDriver1);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for agent
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user view conversation from Library tab
	@Test(groups = { "Regression" })
	public void cai_react_library_open_converstaion(){
		
		System.out.println("Test case --cai_react_library_open_converstaion-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		List<String> libraryList = new ArrayList<>();
		libraryList.add(libraryReactPage.selectLibraryByIndex(caiDriver1, 1));
		
		//get the caller name and receiver name of the first entry
		String callerName = callTabReactPage.getCallerNameFromDurationSubjectList(caiDriver1);
		String receiverName = callTabReactPage.getReceiverNameFromDurationSubjectList(caiDriver1);
		
		//expand first call entry and get the call date and format it
		callTabReactPage.expandSearchRows(caiDriver1, 1);
		String olddateFormat = "MM/dd/yyyy hh:mma";
		String newdateFormat = "M/d/yyyy h:mma";
		String dateTime = callTabReactPage.getConverstaionCallDate(caiDriver1);
		dateTime = dateTime.replace(" p.m.", "pm").replace(" a.m.", "am");
		dateTime = HelperFunctions.changeDateTimeFormat(dateTime, olddateFormat, newdateFormat).replace("PM", "pm").replace("AM", "am");;
		
		//open the first call entry and verify that the headig is proper
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallsTabPageHeading(caiDriver1), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);
		assertTrue(conversationDetailPage.isLibraryAdded(caiDriver1, libraryList));
		
		//navigate back to library page
		libraryReactPage.openLibraryPage(caiDriver1);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify sort saved search Alphabetically ( A-Z ) within library
	//Verify sort saved search Alphabetically ( Z-A ) within library
	@Test(groups = { "Regression" })
	public void cai_react_lib_sort_save_search_alphabetically(){
		
		System.out.println("Test case --cai_react_lib_sort_alphabetically-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibrary(caiDriver1, "11");
		
		//navigate to saved search tab of library
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		
		//Set Sort Type as A to Z and sort records
		SortOptions sortType = SortOptions.libSavedSearchAZ	;
		savedSearchPage.selectSaveSearchSort(caiDriver1, sortType);
		
		//Get the sorted list for the save search name and verify that it is ordered from A to Z
		List<String> resultList = savedSearchPage.getSavedSearchList(caiDriver1);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as Z to A and sort records
		sortType = SortOptions.libSavedSearchZA	;
		savedSearchPage.selectSaveSearchSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from Z to A order
		resultList.clear();
		resultList = savedSearchPage.getSavedSearchList(caiDriver1);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Change the search Result sort from A to Z
		savedSearchPage.selectSaveSearchSort(caiDriver1, SortOptions.libSavedSearchAZ);
		
		//navigate to calls tab of library
		libraryReactPage.clickCallsTab(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user search saved searches by Name within Library tab
	@Test(groups = { "Regression" })
	public void cai_react_lib_save_search_text(){
		
		System.out.println("Test case --cai_react_lib_save_search_text-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibrary(caiDriver1, "11");
		
		//navigate to Saved Search tab of Library page
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		
		//get the saved search name list and set the search text as the second last entry of first page
		List<String> saveSearchList = savedSearchPage.getSavedSearchList(caiDriver1);
		String serachText = saveSearchList.get(saveSearchList.size() - 1);
		
		//search for the above search text
		savedSearchPage.enterSavedSearchText(caiDriver1, serachText);
		
		//get the saved search list again and verify that only 1 row is there
		saveSearchList = savedSearchPage.getSavedSearchList(caiDriver1);
		assertEquals(saveSearchList.size(), 1);
		
		//verify that the saved search name of first entry is equalt to the search text
		assertEquals(saveSearchList.get(0), serachText);
		
		//navigate to calls tab of Library page
		libraryReactPage.clickCallsTab(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user sort Library calls as per Agent Name ( A- Z) and (Z-A)
	@Test(groups = { "Regression" })
	public void cai_react_lib_sort_agent_name(){
		
		System.out.println("Test case --cai_react_lib_sort_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		//Select the search type from Newest to oldest call
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Set Sort Type as A to Z agent name and sort records
		SortOptions sortType = SortOptions.AgenNameAZ;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the agent name and verify that it is ordered from A to Z order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as agent name Z to A and sort records
		sortType = SortOptions.AgenNameZA	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the Agent Name and verify that it is ordered from Z to A order
		resultList.clear();
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Change the search Result sort to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Sort calls Longest to Shortest and Shortest and Longest within Library
	@Test(groups = { "MediumPriority" })
	public void cai_react_lib_sort_duration(){
		
		System.out.println("Test case --cai_react_lib_sort_duration-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		//select the sort type from Newest to Oldest
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Set Sort Type as Longest to Shortest and sort records
		SortOptions sortType = SortOptions.LongtoShort;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the duration and verify that it is ordered from Longest to Shortest order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as Shortest to Longest and sort records
		sortType = SortOptions.ShorttoLong	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the duration and verify that it is ordered from Shortest to Longest order
		resultList.clear();
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);
		
		//Change the search Result sort to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Search calls by search text-box within library
	@Test(groups = { "MediumPriority" })
	public void cai_react_library_search_Call(){
		
	System.out.println("Test case --cai_react_library_search_Call-- started ");
	
	//updating the driver used 
	initializeSupport("caiDriver1");
	driverUsed.put("caiDriver1", true);
	
	String superVisorSubject = "SupervisorSubject" + helperFunctions.GetCurrentDateTime();
	
	//open Conversation AI page and move to library page
	dashboard.clickConversationAI(caiDriver1);
	libraryReactPage.openLibraryPage(caiDriver1);
	
	//open the first call entry and change it's supervisor notes
	callTabReactPage.openConversationDetails(caiDriver1, 0);
	conversationDetailPage.addOrEditSuperVisorNotes(caiDriver1, superVisorSubject);
	conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorSubject);
	libraryReactPage.openLibraryPage(caiDriver1);
	
	//Search the calls for above supervisor notes and verify that only 1 entry is there
	callTabReactPage.enterSearchText(caiDriver1,  "\"" + superVisorSubject + "\"");
	assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);

	//open first conversation ai entry and verify that supervisor notes are the same
	callTabReactPage.openConversationDetails(caiDriver1, 0);
	conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorSubject);
	
	//move back to the library page
	libraryReactPage.openLibraryPage(caiDriver1);
	
	//Setting driver used to false as this test case is pass
	driverUsed.put("caiDriver1", false);
	
	System.out.println("Test case is pass");
	}
	
	//Verify user redirected to correct Library after click on Library from call within library
	//Verify user redirected to correct Library after click on Library from Saved search calls
	@Test(groups = { "MediumPriority" })
	public void cai_react_library_click_another_library(){
		
		System.out.println("Test case --cai_react_library_click_another_library-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		
		//Select first library from the library list
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		//open first conversation AI and set a library for it
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		conversationDetailPage.setLibrary(caiDriver1, 2);
		
		//navigate back to the library page
		libraryReactPage.openLibraryPage(caiDriver1);
		
		//expand first call entry and click on the library selected above
		callTabReactPage.expandSearchRows(caiDriver1, 1);
		String library = callTabReactPage.clickLibrary(caiDriver1, 1);
		
		//verify that user is navigate to the clicked library
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, library);
		
		//move back to library page and select first library from the list
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 0);
		
		//navigate to the Saved search page
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		
		//open first saved search and expand first row from the call records
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		callTabReactPage.expandSearchRows(caiDriver1, 1);
		
		//click on the library selected above
		library = callTabReactPage.clickLibrary(caiDriver1, 1);
		
		//verify that user is navigate to the clicked library
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, library);

		//move back to library page
		libraryReactPage.openLibraryPage(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Admin/Support should able to create/update/delete library if  "Conversation Analytics Manager" is ON "Intelligent Dialer Settings" tab of user setting.
	@Test(groups = { "MediumPriority" })
	public void cai_react_add_edit_delete_library_support_cam_on(){
		
		System.out.println("Test case --cai_react_add_edit_delete_library_support_cam_on-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		
		// Create a library and verify that the user is on this library page
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.createLibrary(caiDriver1, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiDriver1);

		//update the library name
		libraryName = "UpdatedLibrary" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.editLibrary(caiDriver1, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiDriver1);
		
		//delete the library
		libraryReactPage.deletLibrary(caiDriver1, libraryName);
		
		//verify that the library is not present in the list
		assertFalse(libraryReactPage.isLibraryExistInList(caiDriver1, libraryName));

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Admin/Support should able to create/update/delete library if  "Conversation Analytics Manager" is OFF "  in Intelligent Dialer Settings" tab of user setting.
	@Test(groups = { "MediumPriority" })
	public void cai_react_add_edit_delete_library_support_cam_off(){
		
		System.out.println("Test case --cai_react_add_edit_delete_library_support_cam_off-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Open user settings for the CAI support user and disable CAI manager setting
		dashboard.clickConversationAI(caiDriver1);
		dashboard.clickOnUserProfile(caiDriver1);
		dashboard.openManageUsersPage(caiDriver1);
		usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"), CONFIG.getProperty("qa_cai_user_1_username"));
		usersPage.disableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.closeTab(caiDriver1);
		userIntelligentDialerTab.switchToTab(caiDriver1, 2);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		
		// Create a library
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.createLibrary(caiDriver1, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiDriver1);

		//update the library
		libraryName = "UpdatedLibrary" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.editLibrary(caiDriver1, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(caiDriver1);
		
		//delete the library and verify that it does not exist in the library list
		libraryReactPage.deletLibrary(caiDriver1, libraryName);
		assertFalse(libraryReactPage.isLibraryExistInList(caiDriver1, libraryName));
		
		//Open user settings for the CAI support user and enable CAI manager setting
		dashboard.clickOnUserProfile(caiDriver1);
		dashboard.openManageUsersPage(caiDriver1);
		usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"), CONFIG.getProperty("qa_cai_user_1_username"));
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Create /Edit/Delete library button should not be visible to Agent User with CAI manager Setting Off
	//Verify Agent with CAI Manager Setting ON should be able to Create /Edit/Delete Library
	@Test(groups = { "MediumPriority","Regression" })
	public void cai_react_add_edit_delete_library_agent_cam_on(){
		
		System.out.println("Test case --cai_react_add_edit_delete_library_support_cam_off-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Open user settings for the agent and disable CAI manager setting
		dashboard.clickConversationAI(caiDriver1);
		dashboard.clickOnUserProfile(caiDriver1);
		dashboard.openManageUsersPage(caiDriver1);
		usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_username"));
		usersPage.disableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.closeTab(caiDriver1);
		userIntelligentDialerTab.switchToTab(caiDriver1, 2);
		
		//updating the driver used 
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//open Conversation AI page and move to library page
		dashboard.clickConversationAI(agentDriver);
		libraryReactPage.openLibraryPage(agentDriver);
		
		//verify that add, edit and delete button for Library are not visible to agent
		libraryReactPage.noAccessForAgentCamOff(agentDriver);
		
		//Open user settings for the agent and enable CAI manager setting
		dashboard.clickOnUserProfile(caiDriver1);
		dashboard.openManageUsersPage(caiDriver1);
		usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_username"));
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		seleniumBase.reloadSoftphone(agentDriver);
		
		//open Conversation AI page and move to library page
		dashboard.clickConversationAI(agentDriver);
		libraryReactPage.openLibraryPage(agentDriver);
		
		// Create a library
		String libraryName = "Library" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.createLibrary(agentDriver, libraryName);
		libraryReactPage.verifyUserOnLibraryPage(agentDriver, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(agentDriver);

		//update the library
		libraryName = "UpdatedLibrary" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.editLibrary(agentDriver, libraryName);
		
		//verify no call are related to the library
		libraryReactPage.verifyNoLibraryCallMessage(agentDriver);
		
		//delete the library and verify that it does not exist in the library list anymore
		libraryReactPage.deletLibrary(agentDriver, libraryName);
		assertFalse(libraryReactPage.isLibraryExistInList(agentDriver, libraryName));
		
		agentDriver.quit();
		agentDriver = null;

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("agentDriver", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify create library with maximum chars allowed
	@Test(groups = { "MediumPriority" })
	public void cai_react_library_name_max_limit(){
		
		System.out.println("Test case --cai_react_library_name_max_limit-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to library page
		
		dashboard.clickConversationAI(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		
		// Create a library with the maximum allowed limit for library name
		String libraryName = "Library_" +  HelperFunctions.GetRandomString(27) + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		libraryReactPage.createLibrary(caiDriver1, libraryName);
		
		//verify that library is created with the above name
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, libraryName);
		
		//delete library
		libraryReactPage.deletLibrary(caiDriver1, libraryName);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" })
	public void afterMethod(ITestResult result) {
		if(result.getName().equals("cai_react_add_edit_delete_library_support_cam_off") && (result.getStatus() == 2 || result.getStatus() == 3)) {
			
			//updating the driver used 
			initializeSupport("caiDriver1");
			driverUsed.put("caiDriver1", true);
			
			//Open user settings for the CAI support/admin user and enable CAI manager setting
			dashboard.clickConversationAI(caiDriver1);
			dashboard.clickOnUserProfile(caiDriver1);
			dashboard.openManageUsersPage(caiDriver1);
			usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"), CONFIG.getProperty("qa_cai_user_1_username"));
			usersPage.enableConversationAIManagerBtn(caiDriver1);
			userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
			userIntelligentDialerTab.closeTab(caiDriver1);
			userIntelligentDialerTab.switchToTab(caiDriver1, 2);
		}else if(result.getName().equals("cai_react_add_edit_delete_library_support_cam_off") && (result.getStatus() == 2 || result.getStatus() == 3)) {
			
			//updating the driver used 
			initializeSupport("caiDriver1");
			driverUsed.put("caiDriver1", true);
			
			//Open user settings for the CAI agent user and enable CAI manager setting
			dashboard.clickConversationAI(caiDriver1);
			dashboard.clickOnUserProfile(caiDriver1);
			dashboard.openManageUsersPage(caiDriver1);
			usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_username"));
			usersPage.enableConversationAIManagerBtn(caiDriver1);
			userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
			userIntelligentDialerTab.closeTab(caiDriver1);
			userIntelligentDialerTab.switchToTab(caiDriver1, 2);
			
			agentDriver.quit();
			agentDriver = null;
			
			driverUsed.put("caiDriver1", false);
			driverUsed.put("agentDriver", false);
		}
	}
}