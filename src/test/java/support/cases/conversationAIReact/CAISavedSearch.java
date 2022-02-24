/**
 * 
 */
package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.CallsTabReactPage.SortOptions;
import support.source.conversationAIReact.CallsTabReactPage.TimeFrame;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.InboxPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.conversationAIReact.SavedSearchPage;
import support.source.conversationAIReact.SavedSearchPage.SaveSearchMenuTriggerOptions;
import support.source.conversationAIReact.SavedSearchPage.notificationOptions;
import support.source.conversationAIReact.SavedSearchPage.visibityOptions;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

/**
 * @author Abhishek Gupta
 *
 */
public class CAISavedSearch extends SupportBase{

	CallsTabReactPage 			callTabReactPage 			= new CallsTabReactPage();
	CallToolsPanel 				callToolsPanel 				= new CallToolsPanel();
	ConversationDetailPage 		conversationDetailPage 		= new ConversationDetailPage();
	LibraryReactPage			libraryReactPage			= new LibraryReactPage();
	SavedSearchPage				savedSearchPage				= new SavedSearchPage();
	Dashboard 					dashboard 					= new Dashboard();
	HelperFunctions 			helperFunctions 			= new HelperFunctions();
	ReportThisCallPage			reportThisCallPage 			= new ReportThisCallPage();
	SeleniumBase 				seleniumBase 				= new SeleniumBase();
	SoftphoneBase 				softphoneBase 				= new SoftphoneBase();
	SoftphoneCallHistoryPage	softphoneCallHistoryPage	= new SoftphoneCallHistoryPage();
	SoftPhoneCalling 			softphoneCalling 			= new SoftPhoneCalling();
	UsersPage 					usersPage					= new UsersPage();
	UserIntelligentDialerTab 	userIntelligentDialerTab	= new UserIntelligentDialerTab();
	SoftPhoneLoginPage 			SFLP 						= new SoftPhoneLoginPage();
	DashBoardConversationAI	    dashBoardCAI				= new DashBoardConversationAI();
	InboxPage 					inboxPage 					= new InboxPage();
	
	//Verify user able to add public saved search with immediate notification type
	//Verify and view added public saved search listed under 'My Saved Search'
	//Verify added public saved search showing within related Libraries
	@Test(groups = { "Regression", "Product Sanity" })
	public void cai_react_create_public_saved_search(){
		
		System.out.println("Test case --cai_react_create_public_saved_search-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());

		// Set the second filter as outbound calls
		filterType = CallDataFilterType.callDirectionOutbound;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Call Directions");
		filtersList.add(searchToken);
		
		// set the third filter to agent name
		filterType = CallDataFilterType.Agent;
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);
		
		//get the number of search rows for the above filters
		int numberOfRows = callTabReactPage.getSearchPageRowCount(caiDriver1);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//Click Action button and verify the list items are for public search only
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		
		//verify Saved Search details on My saved search list
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), savedSearchName);
		//assertEquals(savedSearchPage.getRowTotalCallCounts(caiDriver1, 0), numberOfRows);
		assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver1, 0), 1);
		
		//Expand the saved search from the list and verify search criteria
		savedSearchPage.expandListSavedSearchDetail(caiDriver1, 0);
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		savedSearchPage.collapseListSavedSearchDetail(caiDriver1, 0);
		
		//Click menu icon and verify the list items are for public search only
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open the saved search details and verify then view count has changed to 2
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, -1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//navigate back to My Saved Search page and verify that the view count is 2 for the created public search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		seleniumBase.idleWait(2);
		//assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver1, 0), 2);
		
		//Go to library tab and verify the details for the public search
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), savedSearchName);
		//assertEquals(savedSearchPage.getRowTotalCallCounts(caiDriver1, 0), numberOfRows);
		//assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver1, 0), 2);
		
		//Expand the saved search from the list and verify search criteria on library tab
		savedSearchPage.expandListSavedSearchDetail(caiDriver1, 0);
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		savedSearchPage.collapseListSavedSearchDetail(caiDriver1, 0);
		
		//Click menu icon and verify the list items are for public search only
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		
		//open saved search detail on library tab and verify the details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, -1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//navigate back to Saved search list on library tab and verify that view count is changed to 3
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		//assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver1, 0), 3);
		
		//verify that the saved search is available in the list for second library as well
		libraryReactPage.selectLibraryByIndex(caiDriver1, 1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), savedSearchName);
		
		//verify that saved search is not available for 3rd library
		libraryReactPage.selectLibraryByIndex(caiDriver1, 2);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//open saved search page again and delete the saved search
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not visible in the list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify and view public saved search created by other user
	//Verify user update public saved search to Private type from the list under 'my saved search'
	//Verify user deleted Public saved search from list view
	@Test(groups = { "Regression" })
	public void cai_react_edit_public_saved_search(){
		
		System.out.println("Test case --cai_react_edit_public_saved_search-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		//Verify that user on the saved search details page and verify all saved search details
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		
		//open My saved search page and get the number of calls from the list for the above saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		int numberOfCalls = savedSearchPage.getRowTotalCallCounts(caiDriver1, 0);
		
		//open the saved search and verify the search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, -1, numberOfCalls, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//verify public save search for other user
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openPublicSavedSearchSection(caiDriver2);
		
		//verify the saved search details for other user
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver2).indexOf(savedSearchName);
		assertTrue(saveSearchIndex >= 0);
		//assertEquals(savedSearchPage.getRowTotalCallCounts(caiDriver2, saveSearchIndex), numberOfCalls);
		//assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver2, saveSearchIndex), 2);
		
		//Verify the menu items are of public type for other user
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver2, saveSearchIndex);
		savedSearchPage.verifySharedPublicSearchMenuItems(caiDriver2);
		seleniumBase.pressEscapeKey(caiDriver2);
		
		//open saved search details and menu items are of public type for other user
		savedSearchPage.openSavedSearch(caiDriver2, 0);
		savedSearchPage.clickActionButton(caiDriver2);
		savedSearchPage.verifySharedPublicSearchMenuItems(caiDriver2);
		seleniumBase.pressEscapeKey(caiDriver2);
		
		//Open My Saved Search page for user and edit the saved filter
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		savedSearchPage.clickEditActionButton(caiDriver1);
		
		//Changed the saved search type to private and unselect the libraries
		String newSavedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions newVisibityOptionToSelect = visibityOptions.Private;
		savedSearchPage.editSavedSearchDetails(caiDriver1, newSavedSearchName, newVisibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Open the detail of the updated Saved Search and verify its details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), newSavedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, newVisibityOptionToSelect, notificationOptionToSelect, -1, numberOfCalls, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//Click Action button and verify the list items are for private search only
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPrivateSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		
		//verify that public search is not appearing for other user now
		savedSearchPage.openMySavedSearchSection(caiDriver2);
		savedSearchPage.openPublicSavedSearchSection(caiDriver2);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).indexOf(newSavedSearchName), -1);
		
		//open library page for the first user and verify the saved search is not appearing
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName), -1);
		
		//open second library page for the first user and verify the saved search is not appearing
		libraryReactPage.selectLibraryByIndex(caiDriver1, 1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName), -1);
		
		//open saved search page again and delete the saved search
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not visible in the list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//verify that the saved search is not visible in the list for other user as well
		savedSearchPage.openMySavedSearchSection(caiDriver2);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).indexOf(savedSearchName), -1);
				
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user update public saved search from detail view
	//Verify user update private saved search from detail view
	//Verify saved search by search text box
	@Test(groups = { "Regression", "Product Sanity" })
	public void cai_react_edit_public_saved_search_from_detail(){
		
		System.out.println("Test case --cai_react_edit_public_saved_search_from_detail()-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.None;
		int numberOfLibrariesToSelect = 0;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Define a filter list for the filter criteria
		CallDataFilterType filterType = CallDataFilterType.Disposition;
		List<String> filtersList = new ArrayList<>();
		
		// Set one more disposition as a filter adn the criteria should be or
		filtersList.add("Call Dispositions");
		filtersList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 2, null));
		filterType = CallDataFilterType.Disposition;
		filtersList.add("or");
		filtersList.add("Call Dispositions");
		filtersList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null));

		// Set the filter type to inbound call direction
		filterType = CallDataFilterType.callDirectionInbound;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Call Directions");
		filtersList.add(searchToken);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//Click Action button and verify the list items are for public search only
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		
		//Edit saved search and verify the old name is present
		savedSearchPage.clickEditActionButton(caiDriver1);
		assertEquals(savedSearchPage.getEditSavedSearchName(caiDriver1), savedSearchName);
		
		//Change the type to private and notification as immediate and select two libraries
		String newSavedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions newVisibityOptionToSelect = visibityOptions.Private;
		notificationOptions newNotificationOptionToSelect =  notificationOptions.Immediate;
		numberOfLibrariesToSelect = 2;
		savedSearchPage.editSavedSearchDetails(caiDriver1, newSavedSearchName, newVisibityOptionToSelect, newNotificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), newSavedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, newVisibityOptionToSelect, newNotificationOptionToSelect, 2, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//Click Action button and verify the list items are for private search
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPrivateSearchMenuItems(caiDriver1);
		
		//Again click on the edit button
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.clickEditActionButton(caiDriver1);
		assertEquals(savedSearchPage.getEditSavedSearchName(caiDriver1), newSavedSearchName);
		
		//Change the filter type to public, notification type as daily and unselect previously selected libraries and select other
		notificationOptionToSelect =  notificationOptions.Daily;
		numberOfLibrariesToSelect = 3;
		savedSearchPage.editSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 3, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//Click Action button and verify the list items are for public search
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		savedSearchPage.pressEscapeKey(caiDriver1);
		
		//Go to library tab and verify that the updated saved search is not appearing for first library
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//verify that the updated saved search is not appearing for second library as well
		libraryReactPage.selectLibraryByIndex(caiDriver1, 1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);

		//verify that the updated saved search is appearing for third library
		libraryReactPage.selectLibraryByIndex(caiDriver1, 2);
		int savedSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(savedSearchIndex >= 0);

		//verify the search type of the updated saved search as public
		assertEquals(savedSearchPage.getRowSearchType(caiDriver1, savedSearchIndex), "Public Saved Search");

		//Go to Saved search and search for the filter
		savedSearchPage.openSavedSearchPage(caiDriver1);
		callTabReactPage.enterSearchText(caiDriver1, savedSearchName);
		
		//get the saved search list and verify that only 1 row is there
		List<String> saveSearchList = savedSearchPage.getSavedSearchList(caiDriver1);
		assertEquals(saveSearchList.size(), 1);
		
		//verify that the saved search name of first entry is equal to the search text
		assertEquals(saveSearchList.get(0), savedSearchName);
		
		//search with invalid search string and verify no result found message appears
		callTabReactPage.enterSearchText(caiDriver1, "aweqwdaad");
		savedSearchPage.verifyLibNoSearchSavedSearchMsg(caiDriver1);
		
		//delete the saved search
		libraryReactPage.openLibraryPage(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user edit Public saved search from Library
	//Verify user delete Public saved search from Library
	@Test(groups = { "Regression" })
	public void cai_react_edit_public_saved_search_library_tab(){
		
		System.out.println("Test case --cai_react_edit_public_saved_search_library_tab-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details	
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
	
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Go to library tab and verify that the saved search is visible there
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		int savedSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(savedSearchIndex >= 0);
		
		//click on menu button and edit the saved search
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		savedSearchPage.clickEditActionButton(caiDriver1);
		
		//verify the saved search name is already there
		assertEquals(savedSearchPage.getEditSavedSearchName(caiDriver1), savedSearchName);
		
		//update the visibility type of the saved search to private and unselect the library
		String newSavedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions newVisibityOptionToSelect = visibityOptions.Private;
		savedSearchPage.editSavedSearchDetails(caiDriver1, newSavedSearchName, newVisibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify that saved search name is not present in the least anymore
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName), -1);
		
		//Go to my saved search verify that the saved search name is updated and type should be private
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName);
		assertTrue(savedSearchIndex >= 0);
		assertEquals(savedSearchPage.getRowSearchType(caiDriver1, savedSearchIndex), "Private Saved Search");
		
		//change the saved search to public and select the library
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.clickEditActionButton(caiDriver1);
		assertEquals(savedSearchPage.getEditSavedSearchName(caiDriver1), newSavedSearchName);
		savedSearchPage.editSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, 2);
		
		//Go to library tab and verify that the saved search is visible there
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		savedSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(savedSearchIndex >= 0);
		
		//delete the saved search from library tab
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that user is navigated to Saved search page and My saved search link is highlighted
		//vverify that saved search is not available in the list
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//go to library page again and verify that saved search is not visible there anymore
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user able to add Private saved search with immediate notification type
	//Verify user edit private saved search to public from list view
	@Test(groups = { "Regression" })
	public void cai_react_create_private_saved_search(){
		
		System.out.println("Test case --cai_react_create_private_saved_search-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);
		
		//get the number of search rows for the above filters
		int numberOfRows = callTabReactPage.getSearchPageRowCount(caiDriver1);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
			
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//Click Action button and verify the list items are for private search only
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPrivateSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		
		//Go to My saved search page and edit the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPrivateSearchMenuItems(caiDriver1);
		savedSearchPage.clickEditActionButton(caiDriver1);
		
		//Change the type to public, notification as none and un select libraries
		String newSavedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions newVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions newNotificationOptionToSelect =  notificationOptions.None;
		savedSearchPage.editSavedSearchDetails(caiDriver1, newSavedSearchName, newVisibityOptionToSelect, newNotificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and verify that save search is updated
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), newSavedSearchName);
		assertEquals(savedSearchPage.getRowSearchType(caiDriver1, 0), "Public Saved Search");
	
		//Open the menu options for the saved search and verify the menu options are of public search only
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		
		//Open the saved search detail and view count is increased
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), newSavedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, newVisibityOptionToSelect, newNotificationOptionToSelect, 2, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		
		//click option button and verify the menu items are of public type
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.verifyPublicSearchMenuItems(caiDriver1);
		seleniumBase.pressEscapeKey(caiDriver1);
		
		//Open library page and move to saved search tab. Verify the saved search is not present for the libraries
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName), -1);
		libraryReactPage.selectLibraryByIndex(caiDriver1, 1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(newSavedSearchName), -1);
		
		//verify public save search for other user
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openPublicSavedSearchSection(caiDriver2);
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver2).indexOf(newSavedSearchName);
		assertTrue(saveSearchIndex >= 0);
		
		//Delete the saved search
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Delete Private search from Saved search list view
	@Test(groups = { "Regression" })
	public void cai_react_delete_private_saved_search_list(){
		
		System.out.println("Test case --cai_react_delete_private_saved_search_list-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		
		//Move to My saved search page and delete the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not present anymore
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Delete Private saved search from Library tab 
	@Test(groups = { "Regression" })
	public void cai_react_delete_private_saved_search_from_library_tab(){
		
		System.out.println("Test case --cai_react_delete_private_saved_search_from_library_tab-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI page and move to Saved Search page
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		List<String> libraryList  = savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		String librarySelected = libraryList.get(0);
		
		//Navigate to library
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibrary(caiDriver1, librarySelected);
		
		//verify save search present
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		int index = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(index >= 0);

		//delete save search
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, index);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not present anymore
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user can update private saved search from associated library
	@Test(groups = { "Regression" })
	public void cai_react_update_private_saved_search_from_library_tab(){
		
		System.out.println("Test case --cai_react_update_private_saved_search_from_library_tab-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI page and move to Saved Search page
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		List<String> libraryList  = savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		String librarySelected = libraryList.get(0);
		
		//Navigate to library
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibrary(caiDriver1, librarySelected);
		
		//verify save search present
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		int index = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(index >= 0);

		//edit save search
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, index);
		savedSearchPage.clickEditActionButton(caiDriver1);
		
		//Change the type to public, notification as none and un select libraries
		String newSavedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions newVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions newNotificationOptionToSelect =  notificationOptions.None;
		savedSearchPage.editSavedSearchDetails(caiDriver1, newSavedSearchName, newVisibityOptionToSelect, newNotificationOptionToSelect, 0);
		
		//verify user is on Saved Search page and verify that save search is updated
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), newSavedSearchName);
		assertEquals(savedSearchPage.getRowSearchType(caiDriver1, 0), "Public Saved Search");
//		savedSearchPage.getRowTotalViewCounts(caiDriver1, 0);
		
		List<String> menuOptions = savedSearchPage.getMenuTriggerList(caiDriver1, newSavedSearchName);
		assertFalse(menuOptions.contains(SaveSearchMenuTriggerOptions.Share.name()));
		
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not present anymore
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Share private saved search from Library tab
	@Test(groups = { "Regression" })
	public void cai_react_share_private_saved_search_from_library_tab(){
		
		System.out.println("Test case --cai_react_share_private_saved_search_from_library_tab-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//updating the driver used 
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);

		dashboard.switchToTab(caiDriver2, 2);
		dashboard.clickConversationAI(caiDriver2);
		int inboxCountBefore = inboxPage.getInboxCount(caiDriver2);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI page and move to Saved Search page
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		List<String> libraryList  = savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		String librarySelected = libraryList.get(0);
		
		//Navigate to library
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.selectLibrary(caiDriver1, librarySelected);
		
		//verify save search present
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		int index = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(index >= 0);

		// sharing save search
		List<String> agentList = new ArrayList<String>();
		agentList.add(CONFIG.getProperty("qa_cai_user_2_name"));

		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		savedSearchPage.shareSaveSearch(caiDriver1, savedSearchName, agentList, shareMsg);
		
		// Navigating to Save search section of other user and verify share option not appearing
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
				
		List<String> menuOptions = savedSearchPage.getMenuTriggerList(caiDriver2, savedSearchName);
		assertFalse(menuOptions.contains(SaveSearchMenuTriggerOptions.Share.name()));
		seleniumBase.pressEscapeKey(caiDriver2);
		
		inboxPage.navigateToInboxTab(caiDriver2);
		System.out.println(inboxPage.getSavedSearchNotificationTextInbox(caiDriver2, 0));
		assertEquals( inboxPage.getSavedSearchNotificationTextInbox(caiDriver2, 0), "Cai User1 has shared \"" + savedSearchName + "\" with you.");
		
		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
				
		//verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), savedSearchName);

		//delete save search
		index = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, index);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify that the saved search is not present anymore
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user delete Private saved search from detail view
	@Test(groups = { "Regression" })
	public void cai_react_delete_private_detail_view(){
		
		System.out.println("Test case --cai_react_edit_save_search-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		
		//delete the saved search from the detal page
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//verify the user on My saved search page and the list doesn't contain deleted saved search
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//open library page and verify that saved search not present
		libraryReactPage.openLibraryPage(caiDriver1);
		libraryReactPage.clickSavedSearchTab(caiDriver1);
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName), -1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify sort saved search by "Alphabetically(A-Z )"
	//Verify sort saved search by "Alphabetically(Z-A )"
	@Test(groups = { "Regression" })
	public void cai_react_sort_public_save_search_by_name(){
		
		System.out.println("Test case --cai_react_sort_public_save_search_by_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//Set Sort Type as A to Z and sort records
		SortOptions sortType = SortOptions.AgenNameAZ	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the save search name and verify that it is ordered from A to Z
		List<String> resultList = savedSearchPage.getSavedSearchList(caiDriver1);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as Z to A and sort records
		sortType = SortOptions.AgenNameZA;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from Z to A order
		resultList.clear();
		resultList = savedSearchPage.getSavedSearchList(caiDriver1);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//verify sort calls of saved search from oldest to newest
	//Verify sort calls of saved search newest to oldest
	@Test(groups = { "MediumPriority" })
	public void cai_react_sort_saved_search_calls_by_time(){
		
		System.out.println("Test case --cai_react_sort_saved_search_calls_by_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
				
		//Set Sort Type as new to old and sort records
		SortOptions sortType = SortOptions.NewtoOld	;
		
		//Get the sorted list for the time and verify that it is ordered from new to old order
		seleniumBase.idleWait(2);
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Set Sort Type as Old to new and sort records
		sortType = SortOptions.OldtoNew	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from old to new order
		seleniumBase.idleWait(2);
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as New to Old and sort records
		sortType = SortOptions.NewtoOld	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from new to old order
		seleniumBase.idleWait(2);
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
	
	//verify sort calls of saved search from Agent name (A-Z)
	//verify sort calls of saved search from Agent name (Z-A)
	@Test(groups = { "MediumPriority" })
	public void cai_react_sort_saved_search_calls_by_agent_name(){
		
		System.out.println("Test case --cai_react_sort_saved_search_calls_by_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
				
		//Set Sort Type as Ageny's name from A to Z and sort records
		SortOptions sortType = SortOptions.AgenNameAZ;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		
		//Get the sorted list for the Agent's name and verify that it is ordered from A to Z order
		callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		List<String> resultList = CallsTabReactPage.agentNameList;
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as Ageny's name from Z to A and sort records
		sortType = SortOptions.AgenNameZA	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the Agent's name and verify that it is ordered from Z to A order
		resultList.clear();
		callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		resultList = CallsTabReactPage.agentNameList;
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Change the search Result sort back to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//verify sort calls of saved search from from longest to shortest
	//verify sort call of saved search from shortest to longest
	@Test(groups = { "MediumPriority" })
	public void cai_react_sort_saved_search_calls_by_duration(){
		
		System.out.println("Test case --cai_react_sort_saved_search_calls_by_duration-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
				
		//Set Sort Type as Shortest to Longest and sort records
		SortOptions sortType = SortOptions.ShorttoLong	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the duration and verify that it is ordered from Shortest to Longest order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as Longest to Shortest and sort records
		sortType = SortOptions.LongtoShort	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the duration and verify that it is ordered from Longest to Shortest order
		resultList.clear();
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Change the search Result sort back to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//verify user able to filter calls as per search text
	@Test(groups = { "MediumPriority" })
	public void cai_react_saved_search_call_search(){
		
	System.out.println("Test case --cai_react_saved_search_call_search-- started ");
	
	//updating the driver used 
	initializeSupport("caiDriver1");
	driverUsed.put("caiDriver1", true);
	
	String superVisorSubject = "SupervisorSubject" + helperFunctions.GetCurrentDateTime();
	
	//open Conversation AI page and move to Saved Search page
	
	dashboard.clickConversationAI(caiDriver1);
	savedSearchPage.openSavedSearchPage(caiDriver1);

	//open first saved search details
	savedSearchPage.openSavedSearch(caiDriver1, 0);
	
	//open the first call entry and change it's supervisor notes
	callTabReactPage.openConversationDetails(caiDriver1, 0);
	conversationDetailPage.addOrEditSuperVisorNotes(caiDriver1, superVisorSubject);
	conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorSubject);

	//open first saved search details
	savedSearchPage.openSavedSearchPage(caiDriver1);
	savedSearchPage.openSavedSearch(caiDriver1, 0);
	
	//Search the calls for above supervisor notes and verify that only 1 entry is there
	callTabReactPage.enterSearchText(caiDriver1,  "\"" + superVisorSubject + "\"");
	assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);

	//open first conversation ai entry and verify that supervisor notes are the same
	callTabReactPage.openConversationDetails(caiDriver1, 0);
	conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorSubject);
	
	//move back to the library page
	savedSearchPage.openSavedSearchPage(caiDriver1);
	
	//Setting driver used to false as this test case is pass
	driverUsed.put("caiDriver1", false);
	
	System.out.println("Test case is pass");
	}
	
	//View conversation AI call from saved search detail page
	@Test(groups = { "MediumPriority" })
	public void cai_react_saved_search_open_cai(){
		
	System.out.println("Test case --cai_react_saved_search_open_cai-- started ");
	
	//updating the driver used 
	initializeSupport("caiDriver1");
	driverUsed.put("caiDriver1", true);
	
	//open Conversation AI page and move to Saved Search page
	
		dashboard.clickConversationAI(caiDriver1);
	savedSearchPage.openSavedSearchPage(caiDriver1);

	//open first saved search details
	savedSearchPage.openSavedSearch(caiDriver1, 0);
	
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
	
	//open the first call entry and verify that the heading is proper
	callTabReactPage.openConversationDetails(caiDriver1, 0);
	assertEquals(conversationDetailPage.getCallsTabPageHeading(caiDriver1), "Call Between "+callerName+ " and " +receiverName+ " - "+dateTime);
		
	//move back to the library page
	savedSearchPage.openSavedSearchPage(caiDriver1);
	
	//Setting driver used to false as this test case is pass
	driverUsed.put("caiDriver1", false);
	
	System.out.println("Test case is pass");
	}
	
	//verify click on any elastic filter should redirect user to calls tab with filter apply
	@Test(groups = { "MediumPriority" })
	public void cai_react_saved_search_click_agent_name(){
		
		System.out.println("Test case --cai_react_saved_search_click_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);

		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		
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
	
	//Verify clicking on library from call within saved search should redirect user to that library	
	@Test(groups = { "MediumPriority" })
	public void cai_react_saved_search_click_library_name(){
		
		System.out.println("Test case --cai_react_saved_search_click_library_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);

		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		
		//open the first conversation AI and Select a Library for it
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		conversationDetailPage.setLibrary(caiDriver1, 0);
		callTabReactPage.navigateToCallsPage(caiDriver1);
		
		// Set the filter type to Library
		CallDataFilterType filterType = CallDataFilterType.Library;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for Library by clicking on to it
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		
		//verify the filtered records for Library
		libraryReactPage.verifyUserOnLibraryPage(caiDriver1, searchToken);

		//open Conversation AI page and move to Saved Search page
		savedSearchPage.openSavedSearchPage(caiDriver1);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify when No saved searches in My Saved Searches
	//Verify when No saved searches in Shared With Me section
	//Verify when No Public saved searches
	
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_saved_search_empty_states(){
		
		System.out.println("Test case --conversation_ai_saved_search_empty_states-- started ");
		
		//updating the driver used 
		WebDriver noCAIDriver = getDriver();
		
		//login to the agent driver
		SFLP.supportLogin(noCAIDriver, CONFIG.getProperty("qa_support_tool_site"), "agent@ringdna.com", "Auto0198");
		dashboard.isPaceBarInvisible(noCAIDriver);
		
		//open Conversation AI page and move to Saved Search page
		dashboard.clickConversationAI(noCAIDriver);
		savedSearchPage.openSavedSearchPage(noCAIDriver);
		
		//open my saved search page and verify that no saved search message
		savedSearchPage.openMySavedSearchSection(noCAIDriver);
		savedSearchPage.verifyNoMySavedSearchMsg(noCAIDriver);

		//open Shared saved search page and verify that no saved search message
		savedSearchPage.openSharedSavedSearchSection(noCAIDriver);
		savedSearchPage.verifyNoSharedSavedSearchMsg(noCAIDriver);
		
		//open public saved search page and verify that no saved search message
		savedSearchPage.openPublicSavedSearchSection(noCAIDriver);
		savedSearchPage.verifyNoPublicSavedSearchMsg(noCAIDriver);
		
		//open to my saved search page
		savedSearchPage.openMySavedSearchSection(noCAIDriver);
		
		//quit the driver
		noCAIDriver.quit();
		noCAIDriver = null;
		
		System.out.println("Test case is pass");
	}
	
	//Verify when No calls on an individual saved search page
	//Verify when No results on search
	@Test(groups = { "Regression" })
	public void conversation_ai_saved_search_empty_states_of_calls(){
		
		System.out.println("Test case --conversation_ai_saved_search_empty_states_of_calls-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open first saved search detail
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		
		//Enter a search text
		callTabReactPage.enterSearchText(caiDriver1, "azv123456");
		
		//verify that no call search message appears
		savedSearchPage.verifyNoCallsSearchMessage(caiDriver1);
		
		//navigate to my saved search page
		savedSearchPage.openSharedSavedSearchSection(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		//open the saved search which has no calls in it
		int index = savedSearchPage.getSavedSearchList(caiDriver1).indexOf("NoSaveSearchFilter");
		savedSearchPage.openSavedSearch(caiDriver1, index);
		
		//verify no calls message is appearing
		savedSearchPage.verifyNoSavedSearchCallMessage(caiDriver1);
		
		//open my saved search page
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify sort saved search by "oldest to newest"
	//Verify sort calls of saved search newest to oldest
	//@Test(groups = { "MediumPriority" })
	public void cai_react_sort_public_saved_search_calls_by_time(){
		
		System.out.println("Test case --cai_react_sort_public_saved_search_calls_by_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and move to Saved Search page
		
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		
		//open first saved search details
		savedSearchPage.openSavedSearch(caiDriver1, 0);
		savedSearchPage.openPublicSavedSearchSection(caiDriver1);
				
		//Set Sort Type as new to old and sort records
		SortOptions sortType = SortOptions.NewtoOld	;
		
		//Get the sorted list for the time and verify that it is ordered from new to old order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		boolean sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);
		
		//Set Sort Type as Old to new and sort records
		sortType = SortOptions.OldtoNew	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from old to new order
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		//Set Sort Type as New to Old and sort records
		sortType = SortOptions.NewtoOld	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		//Get the sorted list for the time and verify that it is ordered from new to old order
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
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void share_private_search_to_other_agent_from_saved_search_list_view() {
		System.out.println("Test case --share_private_search_to_other_agent_from_saved_search_list_view-- started ");

		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String timeFrameValue =callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Go to My saved search page and edit the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		// sharing save search
		List<String> agentList = new ArrayList<String>();
		agentList.add(CONFIG.getProperty("qa_cai_user_2_name").trim());
		
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		savedSearchPage.shareSaveSearch(caiDriver1, savedSearchName, agentList, shareMsg);
		
		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
				
		//verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), savedSearchName);
		List<String> menuOptions = savedSearchPage.getMenuTriggerList(caiDriver2, savedSearchName);
		assertTrue(menuOptions.size() == 1);
		assertTrue(menuOptions.contains(SaveSearchMenuTriggerOptions.Clone.name()));
		
		int sharedNumberOfRows = -1;
		savedSearchPage.pressEscapeKey(caiDriver2);
		savedSearchPage.openSavedSearch(caiDriver2, 0);
		savedSearchPage.verifySavedSearchDetails(caiDriver2, visibityOptionToSelect, notificationOptionToSelect, 2, sharedNumberOfRows, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver2, filtersList);
		savedSearchPage.verifySavedSearchCriteriaLabelsDisabled(caiDriver2);
		
		//cloning and verify on calls tab
		libraryReactPage.openLibraryPage(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
		savedSearchPage.selectSaveSearchMenuFilter(caiDriver2, savedSearchName, SaveSearchMenuTriggerOptions.Clone);
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver2, searchToken));
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver2, timeFrameValue));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptionToSelect = visibityOptions.Public;
		notificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver2);
		savedSearchPage.enterSavedSearchDetails(caiDriver2, updatedSaveSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver2);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver2);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver2), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver2, visibityOptionToSelect, notificationOptionToSelect, 1, sharedNumberOfRows, CONFIG.getProperty("qa_cai_user_2_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver2, filtersList);
	
		// setting driver to false
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		System.out.println("Test case is pass");
	}
	
	//Verify private saved search share to team
	@Test(groups = { "Regression" })
	public void share_private_search_to_team() {
		System.out.println("Test case --share_private_search_to_team-- started ");

		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Go to My saved search page and edit the saved search
		dashBoardCAI.navigateToSaveSearchSection(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		//get the number of search rows for the above filters
		savedSearchPage.getRowTotalCallCounts(caiDriver1, 0);
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(saveSearchIndex >= 0);
	
		//navigate inside save search detail
		savedSearchPage.openSavedSearch(caiDriver1, saveSearchIndex);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		
		// sharing save search
		List<String> teamList = new ArrayList<String>();
		teamList.add("AutomationCAIShareTeam");
		
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.shareSaveSearchByAction(caiDriver1, savedSearchName, teamList, shareMsg);
		
		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashboard.switchToTab(caiVerifyDriver, 2);
		dashboard.clickConversationAI(caiVerifyDriver);
		savedSearchPage.openSavedSearchPage(caiVerifyDriver);
		savedSearchPage.openSharedSavedSearchSection(caiVerifyDriver);
				
		//verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(caiVerifyDriver).get(0), savedSearchName);
	
		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashboard.switchToTab(caiSupportDriver, 2);
		dashboard.clickConversationAI(caiSupportDriver);
		savedSearchPage.openSavedSearchPage(caiSupportDriver);
		savedSearchPage.openSharedSavedSearchSection(caiSupportDriver);
				
		//verify Saved Search details on My saved search list
		if(savedSearchPage.getSavedSearchList(caiSupportDriver).size() > 0)
			assertNotEquals(savedSearchPage.getSavedSearchList(caiSupportDriver).get(0), savedSearchName);

		// setting driver to false
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiVerifyDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void share_private_search_to_multiple_users() {
		System.out.println("Test case --share_private_search_to_multiple_users-- started ");

		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI call page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Go to My saved search page and edit the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		// sharing save search to multiple users
		List<String> agentList = new ArrayList<String>();
		agentList.add(CONFIG.getProperty("qa_cai_user_2_name"));
		agentList.add("Automation Softphone");
	
		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		savedSearchPage.shareSaveSearch(caiDriver1, savedSearchName, agentList, shareMsg);
		
		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
				
		//verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), savedSearchName);
		List<String> menuOptions = savedSearchPage.getMenuTriggerList(caiDriver2, savedSearchName);
		assertTrue(menuOptions.size() == 1);
		assertTrue(menuOptions.contains(SaveSearchMenuTriggerOptions.Clone.name()));
		
		//updating the driver used
		//login as agent user
		WebDriver noCAIDriver = getDriver();
		
		//login to the agent driver
		SFLP.supportLogin(noCAIDriver, CONFIG.getProperty("qa_support_tool_site"), "softphone_automation@ringdna.com", "ebmdna0198");
		dashboard.isPaceBarInvisible(noCAIDriver);

		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		dashboard.clickConversationAI(noCAIDriver);
		savedSearchPage.openSavedSearchPage(noCAIDriver);
		savedSearchPage.openSharedSavedSearchSection(noCAIDriver);
				
		//verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(noCAIDriver).get(0), savedSearchName);
		menuOptions = savedSearchPage.getMenuTriggerList(noCAIDriver, savedSearchName);
		assertTrue(menuOptions.size() == 1);
		assertTrue(menuOptions.contains(SaveSearchMenuTriggerOptions.Clone.name()));
		noCAIDriver.quit();
	
		// setting driver to false
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_clone_private_search() {
		System.out.println("Test case --verify_clone_private_search-- started ");

		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;
		
		//open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String timeFrameValue =callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);
	
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//Go to My saved search page and edit the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		//get the number of search rows for the above filters
		int numberOfRows = savedSearchPage.getRowTotalCallCounts(caiDriver1, 0);
		
		//cloning and verify on calls tab
		savedSearchPage.selectSaveSearchMenuFilter(caiDriver1, savedSearchName, SaveSearchMenuTriggerOptions.Clone);
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken));
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, timeFrameValue));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions updatedVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions updatedNotificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, updatedSaveSearchName, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, 1, numberOfRows, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		// setting driver to false
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case is pass");
	}
	
	//Verify user clone the Public search of other user from list view (3 dots)
	@Test(groups = { "Regression" })
	public void clone_public_save_search_of_other_user_from_list_view(){
		
		System.out.println("Test case --clone_public_save_search_of_other_user_from_list_view-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//navigating to 2nd user to clone
		dashboard.switchToTab(caiDriver2, 2);
		dashboard.clickConversationAI(caiDriver2);
		
		//Go to My saved search page and edit the saved search
		dashBoardCAI.navigateToSaveSearchSection(caiDriver2);
		savedSearchPage.openPublicSavedSearchSection(caiDriver2);
		
		//get the number of search rows for the above filters
		int numberOfRows = savedSearchPage.getRowTotalCallCounts(caiDriver2, 0);
		
		//cloning from list view and clicking on 3 dots
		savedSearchPage.selectSaveSearchMenuFilter(caiDriver2, savedSearchName, SaveSearchMenuTriggerOptions.Clone);
		
		//verify on calls tab
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver2, searchToken));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions updatedVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions updatedNotificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver2);
		savedSearchPage.enterSavedSearchDetails(caiDriver2, updatedSaveSearchName, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver2);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver2);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver2), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver2, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, 1, numberOfRows, CONFIG.getProperty("qa_cai_user_2_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver2, filtersList);
		
		//delete saved search
		savedSearchPage.openMySavedSearchSection(caiDriver2);
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver2).indexOf(updatedSaveSearchName);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver2, saveSearchIndex);
		savedSearchPage.deleteSavedSearch(caiDriver2);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user clone the Public search of other user from detail action button
	@Test(groups = { "Regression" })
	public void clone_public_save_search_of_other_user_from_action_button(){
		
		System.out.println("Test case --clone_public_save_search_of_other_user_from_action_button-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//navigating to 2nd user to clone
		dashboard.switchToTab(caiDriver2, 2);
		dashboard.clickConversationAI(caiDriver2);
		
		//Go to My saved search page and edit the saved search
		dashBoardCAI.navigateToSaveSearchSection(caiDriver2);
		savedSearchPage.openPublicSavedSearchSection(caiDriver2);
		
		//get the number of search rows for the above filters
		int numberOfRows = savedSearchPage.getRowTotalCallCounts(caiDriver2, 0);
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver2).indexOf(savedSearchName);
		assertTrue(saveSearchIndex >= 0);
	
		//navigate inside save search detail
		savedSearchPage.openSavedSearch(caiDriver2, saveSearchIndex);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver2), savedSearchName);
		
		//click action button and clone
		savedSearchPage.clickActionButton(caiDriver2);
		savedSearchPage.clickCloneActionButton(caiDriver2);
	
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver2, searchToken));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions updatedVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions updatedNotificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver2);
		savedSearchPage.enterSavedSearchDetails(caiDriver2, updatedSaveSearchName, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver2);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver2);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver2), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver2, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, 1, numberOfRows, CONFIG.getProperty("qa_cai_user_2_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver2, filtersList);
		
		//delete the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver2);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver2, saveSearchIndex);
		savedSearchPage.deleteSavedSearch(caiDriver2);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user clone own created public saved search from action button
	@Test(groups = { "Regression" })
	public void clone_public_save_search_of_own_user_from_action_button(){
		
		System.out.println("Test case --clone_public_save_search_of_own_user_from_action_button-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//Go to My saved search page and edit the saved search
		dashBoardCAI.navigateToSaveSearchSection(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		//get the number of search rows for the above filters
		int numberOfRows = savedSearchPage.getRowTotalCallCounts(caiDriver1, 0);
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(savedSearchName);
		assertTrue(saveSearchIndex >= 0);
	
		//navigate inside save search detail
		savedSearchPage.openSavedSearch(caiDriver1, saveSearchIndex);
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		
		//click action button and clone
		savedSearchPage.clickActionButton(caiDriver1);
		savedSearchPage.clickCloneActionButton(caiDriver1);
	
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions updatedVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions updatedNotificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, updatedSaveSearchName, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, 1, numberOfRows, CONFIG.getProperty("qa_cai_user_2_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		// open saved search page again and delete the saved search
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);

		// verify the saved search details for other user
		saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(updatedSaveSearchName);
		assertTrue(saveSearchIndex >= 0);
		
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, saveSearchIndex);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user clone own created public saved search from list view
	@Test(groups = { "Regression" })
	public void clone_public_save_search_of_own_user_from_list_view(){
		
		System.out.println("Test case --clone_public_save_search_of_own_user_from_list_view-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Define saved search details
		String savedSearchName = "Automation Public Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Public;
		notificationOptions notificationOptionToSelect =  notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 2;
		
		//open Conversation AI call page and clear all filters
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);
		
		//Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());
		
		//save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect, notificationOptionToSelect, numberOfLibrariesToSelect);
		
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), savedSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, visibityOptionToSelect, notificationOptionToSelect, 1, -1, CONFIG.getProperty("qa_cai_user_1_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		//navigating to 2nd user to clone
		dashboard.switchToTab(caiDriver1, 2);
		dashboard.clickConversationAI(caiDriver1);
		
		//Go to My saved search page and edit the saved search
		dashBoardCAI.navigateToSaveSearchSection(caiDriver1);
		savedSearchPage.openPublicSavedSearchSection(caiDriver1);
		
		//get the number of search rows for the above filters
		int numberOfRows = savedSearchPage.getRowTotalCallCounts(caiDriver1, 0);
		
		//cloning from list view and clicking on 3 dots
		savedSearchPage.selectSaveSearchMenuFilter(caiDriver1, savedSearchName, SaveSearchMenuTriggerOptions.Clone);
		
		//verify on calls tab
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken));
		
		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions updatedVisibityOptionToSelect = visibityOptions.Public;
		notificationOptions updatedNotificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, updatedSaveSearchName, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, numberOfLibrariesToSelect);
	
		//verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);
		
		//Verify that user on the saved search details page and verify all saved search details
		assertEquals(savedSearchPage.getSavedSearchNameHeading(caiDriver1), updatedSaveSearchName);
		savedSearchPage.verifySavedSearchDetails(caiDriver1, updatedVisibityOptionToSelect, updatedNotificationOptionToSelect, 1, numberOfRows, CONFIG.getProperty("qa_cai_user_2_name").trim());
		savedSearchPage.verifySavedSearchCriteria(caiDriver1, filtersList);
		
		// open saved search page again and delete the saved search
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.openPublicSavedSearchSection(caiDriver1);

		// verify the saved search details for other user
		int saveSearchIndex = savedSearchPage.getSavedSearchList(caiDriver1).indexOf(updatedSaveSearchName);
		assertTrue(saveSearchIndex >= 0);
		
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, saveSearchIndex);
		savedSearchPage.deleteSavedSearch(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_user_update_shared_private_search() {
		System.out.println("Test case --verify_user_update_shared_private_search-- started ");

		// updating the driver used
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		// Define saved search details
		String savedSearchName = "Automation Private Search" + HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptions visibityOptionToSelect = visibityOptions.Private;
		notificationOptions notificationOptionToSelect = notificationOptions.Immediate;
		int numberOfLibrariesToSelect = 1;

		// open Conversation AI call page and clear all filters
		
		dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);

		// filter the records by Time Frame and the search token as one week
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame time = TimeFrame.OneWeek;
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, time.ordinal(), null);

		// Define a filter list for the filter criteria
		List<String> filtersList = new ArrayList<>();
		filtersList.add("Timeframe");
		filtersList.add(time.displayValue());

		// Set the second filter as Agent name
		filterType = CallDataFilterType.Agent;
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		filtersList.add("and");
		filtersList.add("Agents");
		filtersList.add(searchToken);

		// get the number of search rows for the above filters
		int numberOfRows = callTabReactPage.getSearchPageRowCount(caiDriver1);

		// save the search with above mentioned details
		callTabReactPage.clickSaveSearchButton(caiDriver1);
		savedSearchPage.enterSavedSearchDetails(caiDriver1, savedSearchName, visibityOptionToSelect,
				notificationOptionToSelect, numberOfLibrariesToSelect);

		// Go to My saved search page and edit the saved search
		savedSearchPage.openMySavedSearchSection(caiDriver1);

		// sharing save search
		List<String> agentList = new ArrayList<String>();
		agentList.add(CONFIG.getProperty("qa_cai_user_2_name"));

		String shareMsg = "AutoShareMsg".concat(HelperFunctions.GetRandomString(2));
		savedSearchPage.shareSaveSearch(caiDriver1, savedSearchName, agentList, shareMsg);

		// Navigating to Save search section and verifying save search for
		// 'VerifyDriver'
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);

		// verify Saved Search details on My saved search list
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), savedSearchName);
		List<String> menuOptions = savedSearchPage.getMenuTriggerList(caiDriver2, savedSearchName);
		assertTrue(menuOptions.size() == 1);
		assertTrue(menuOptions.contains(SaveSearchMenuTriggerOptions.Clone.name()));

		// modify the saved search
		String updatedSaveSearchName = "Automation Public Search"
				+ HelperFunctions.GetCurrentDateTime("yyyyMMdd_HHmmss");
		visibityOptionToSelect = visibityOptions.Public;
		notificationOptionToSelect = notificationOptions.Daily;
		numberOfLibrariesToSelect = 1;

		// edit option by owner
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		savedSearchPage.selectSaveSearchMenuFilter(caiDriver1, savedSearchName, SaveSearchMenuTriggerOptions.Edit);
		savedSearchPage.editSavedSearchDetails(caiDriver1, updatedSaveSearchName, visibityOptionToSelect,
				notificationOptionToSelect, numberOfLibrariesToSelect);

		// verify user is on Saved Search page and My saved search link is highlighted
		savedSearchPage.verifyUserOnSavedSearchpage(caiDriver1);
		savedSearchPage.verifyMySavedSearchLinkHighlighted(caiDriver1);

		// Verify that user on the saved search details page and verify all saved search
		// details
		assertEquals(savedSearchPage.getSavedSearchList(caiDriver1).get(0), updatedSaveSearchName);
		//assertEquals(savedSearchPage.getRowTotalCallCounts(caiDriver1, 0), numberOfRows);
		assertEquals(savedSearchPage.getRowTotalViewCounts(caiDriver1, 0), 2);

		// Navigating to Save search section and verifying save search for 'VerifyDriver'
		if(!callTabReactPage.isUserOnCallsTab(caiDriver2))
			dashboard.clickConversationAI(caiDriver2);
		libraryReactPage.openLibraryPage(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openSharedSavedSearchSection(caiDriver2);
		assertNotEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), savedSearchName);
		assertNotEquals(savedSearchPage.getSavedSearchList(caiDriver2).get(0), updatedSaveSearchName);
		
		//verify Saved Search details on My saved search list
	
		// setting driver to false
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		System.out.println("Test case is pass");
	}
	
	//this test case mark cases failure due to bug cai 647
	@Test(groups={"Regression", "MediumPriority"})
	public void cai_647_failed_case() {
		Assert.fail();
	}
	
	//Delete unused saved searches
	@AfterClass(groups={"Regression", "MediumPriority"})
	public void deleteUnusedSavedSearched() {
		// updating the driver used
	  	initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//navigate to My saved search page
		dashboard.clickConversationAI(caiDriver1);
		savedSearchPage.openSavedSearchPage(caiDriver1);
		savedSearchPage.openMySavedSearchSection(caiDriver1);
		
		int pageCount = callTabReactPage.getTotalPage(caiDriver1);
		while (pageCount > 1) {
			savedSearchPage.clickListSavedSearchMenuButton(caiDriver1, 0);
			savedSearchPage.deleteSavedSearch(caiDriver1);
			pageCount = callTabReactPage.getTotalPage(caiDriver1);
		}
		
		//navigate to My saved search page
		dashboard.clickConversationAI(caiDriver2);
		savedSearchPage.openSavedSearchPage(caiDriver2);
		savedSearchPage.openMySavedSearchSection(caiDriver2);
		
		pageCount = callTabReactPage.getTotalPage(caiDriver2);
		while (pageCount > 1) {
			savedSearchPage.clickListSavedSearchMenuButton(caiDriver2, 0);
			savedSearchPage.deleteSavedSearch(caiDriver2);
			pageCount = callTabReactPage.getTotalPage(caiDriver2);
		}
		
		// updating the driver used
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
	}
}
