/**
 * @author Abhishek Gupta
 *
 */
package support.cases.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Ordering;
import com.google.common.collect.Range;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.SearchPage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.CallsTabReactPage.CallDataFilterType;
import support.source.conversationAIReact.CallsTabReactPage.SortOptions;
import support.source.conversationAIReact.CallsTabReactPage.TimeFrame;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.InboxPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CAIReactSearch extends SupportBase{
	
	CallsTabReactPage 			callTabReactPage 			= new CallsTabReactPage();
	InboxPage 					inboxPage 					= new InboxPage();
	SearchPage 					sfSearchPage 				= new SearchPage();
	CallToolsPanel 				callToolsPanel 				= new CallToolsPanel();
	ConversationDetailPage 		conversationDetailPage 		= new ConversationDetailPage();
	CallScreenPage 				callScreenPage 				= new CallScreenPage();
	LibraryReactPage			libraryReactPage			= new LibraryReactPage();
	Dashboard 					dashboard 					= new Dashboard();
	HelperFunctions 			helperFunctions 			= new HelperFunctions();
	ReportThisCallPage			reportThisCallPage 			= new ReportThisCallPage();
	SeleniumBase 				seleniumBase 				= new SeleniumBase();
	SoftphoneBase 				softphoneBase 				= new SoftphoneBase();
	SoftphoneCallHistoryPage	softphoneCallHistoryPage	= new SoftphoneCallHistoryPage();
	SoftPhoneCalling 			softphoneCalling 			= new SoftPhoneCalling();
	UsersPage 					usersPage					= new UsersPage();
	
	String callNotes = null;
	
	@Test(groups = { "Regression", "MediumPriority" }, priority = -2)
	public void create_cai_call(){
		
		System.out.println("Test case --create_cai_calls-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		int Rating = 4;
		
		//setting the values of the call notes and rating
		String notesSubject = "CallNotesSubject" + helperFunctions.GetCurrentDateTime();
		callNotes = "CallNotes" + helperFunctions.GetCurrentDateTime();
		
		//switch to softphone
		seleniumBase.switchToTab(caiDriver1, 1);
		seleniumBase.switchToTab(caiDriver2, 1);
		
		//make an outbound call and pick it up
		softphoneCalling.softphoneAgentCall(caiDriver1, CONFIG.getProperty("qa_cai_user_2_smart_no"));
		softphoneCalling.pickupIncomingCall(caiDriver2);
		
		//select a disposition and coach
		callToolsPanel.clickCoachIcon(caiDriver1);
		callToolsPanel.selectDisposition(caiDriver1, 0);
		
		//enter call notes
		callToolsPanel.enterCallNotes(caiDriver1, notesSubject, callNotes);
		seleniumBase.idleWait(60);
		
		//give call ratings
		callToolsPanel.giveCallRatings(caiDriver1, Rating);
		
		//hang up the active call and wait for conversation AI to get generated
		softphoneCalling.hangupActiveCall(caiDriver1);
		
		//switch to softphone
		seleniumBase.switchToTab(caiDriver1, seleniumBase.getTabCount(caiDriver1));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
		
	}
	
	//Verify user filter calls as per Call Rating
	//Verify remove search text to reset the calls 
	//Conversation AI-Search-Search on Text
	//Verify agent should be able to filter calls on basis of rating of the call using rating slider filter
	//Verify boolean functionality of Call Rating filter.
	@Test(groups = { "Regression", "MediumPriority" }, dependsOnMethods = {"create_cai_call"})
	public void conversation_ai_react_search_ratings(){
		
		System.out.println("Test case --conversation_ai_react_search_ratings-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		int Rating = 4;
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Ratings
		Range<Integer> ratings = Range.closed(Rating, Rating);
		CallDataFilterType filterType = CallDataFilterType.CallRatings;
		
		//verify that the minimum range is appearing as none and maximum as 5 for Call Ratings slider filter
		callTabReactPage.expandCallDetails(caiDriver1);
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "none");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).second(), "5");
		
		//set the Rating filter for 4 stars
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, ratings);
		
		//verify that the status is now True
		Boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
	
		//open the filtered conversation AI and verify that it is the same conversation AI for which we had set rating as 4
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//click on boolean status to make it false(minus)
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//open the filtered conversation AI and verify that it is not the same conversation AI for which we had set rating as 4
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertNotEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);	
		
		//open calls page and clear all filters
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// change the ratings to 5 now
		ratings = Range.closed(5, 5);
		
		//set the Rating filter for 5 stars
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, ratings);
	
		//open the filtered conversation AI and verify that it is not the same conversation AI for which we had set rating as 4
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertNotEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//open calls page and clear all filters
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//verify the call recording for Search text
		//int pageCount = callTabReactPage.getTotalPage(caiDriver1);
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + callNotes + "\"");
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1,  "\"" + callNotes + "\""));
		
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);
	
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//now clear the search text by clicking on remove icon in search box
		callTabReactPage.navigateToCallsPage(caiDriver1);
		//callTabReactPage.clearSearchText(caiDriver1);
		
		//Verify that chip has been removed
		//assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, callNotes));
		
		//verify that the filtered result screen looks same as it was before filter applied
		//assertEquals(callTabReactPage.getTotalPage(caiDriver1), pageCount);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
		System.out.println("Test case is pass");
	}
	
	//Filter recordings - ENGAGEMENT- Flagged for Coaching
	//Filter recordings - ENGAGEMENT- Shared with Others
	//Verify boolean functionality of Engagement Filter-Flagged for Coaching
	//Verify boolean functionality of Engagement Filter-Shared with Others
	@Test(groups = { "Regression", "MediumPriority" }, dependsOnMethods = "conversation_ai_react_search_ratings")
	public void conversation_ai_react_search_coaching_shared_call(){
		
		System.out.println("Test case --conversation_ai_react_search_coaching_shared_call-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Verify that call is not appearing in the filter for which is share with others
		CallDataFilterType filterType = CallDataFilterType.SharedWithOthers;
				
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertNotEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//now share the recently created conversation AI with a user
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		conversationDetailPage.shareCall(caiDriver1, CONFIG.getProperty("qa_user_2_name"), "This call is shared for testing purpose");
		
		//navigate again to the calls page
		callTabReactPage.navigateToCallsPage(caiDriver1);
		
		//Verify that call is not appearing in the filter for which coaching is flagged
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.SharedWithOthers;
		
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		
		//verify that the status is now True
		Boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//click on boolean status to make it false(minus)
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//open the filtered conversation AI and verify that it is not the same conversation AI for which we have shared the call
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertNotEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);	
		
		//navigate to calls tab
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Verify that call is now appearing in the filter for which coaching is flagged
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.FlaggedForCoaching;
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		
		//verify that the status is now True
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//Open first call entry and verify call notes is same
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
		
		//click on boolean status to make it false(minus)
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//open the filtered conversation AI and verify that it is not the same conversation AI for which we have flagged for coaching
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertNotEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);	
		
		//click on boolean status to make it true(plus)
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now True
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify that now the record is appearing for which we have set flagged for Coaching
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		assertEquals(conversationDetailPage.getCallNotes(caiDriver1), callNotes);
				
		//navigate to calls tab
		callTabReactPage.navigateToCallsPage(caiDriver1);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
		
	//Verify upon selecting agent from agent filter, only calls created by that agent should be listed
	@Test(groups = { "MediumPriority", "Product Sanity" })
	public void conversation_ai_search_agents(){
		
		System.out.println("Test case --conversation_ai_search_agents-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
	
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1)) 
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
	
		// Set the filter type to Agent
		CallDataFilterType filterType = CallDataFilterType.Agent;
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		//filter for any agent and verify the filtered records
		List<String> tokenList = new ArrayList<String>();
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//remove agent filter from the chip
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//verify that the agent dropdown becomes blank on removing search token
		assertTrue(callTabReactPage.getSelectedAgentName(caiDriver1).isEmpty());
		
		//verify that no filter is applied and the result is same as earlier
		assertTrue(HelperFunctions.bufferedImagesEqual(callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1), defaultCallSearchPage));
	
		//Setting driver used to false as this test case is pass		
		driverUsed.put("caiDriver1", false);
	
		System.out.println("Test case is pass");
	}

	//Verify agent should be able to filter on single or multiple Disposition values
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_search_disposition(){
		
		System.out.println("Test case --conversation_ai_react_search_disposition-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to call disposition
		CallDataFilterType filterType = CallDataFilterType.Disposition;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for one call disposition
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 2, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//filter for two call dispositions
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter on single or multiple Lead Status values
	//Remove selected search Data from Drop-down will refresh CALLS tab result correctly
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_search_leadstatus(){
		
		System.out.println("Test case --conversation_ai_react_search_leadstatus-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Lead Status
		CallDataFilterType filterType = CallDataFilterType.LeadStatus;
		List<String> tokenList = new ArrayList<String>();
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		//filter for one Lead Status
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Lead Status
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//filter for two call disposition
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Lead Status
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//remove one of the lead status from the chip
		String tokenToBeRemoved = tokenList.get(0);
		callTabReactPage.removeFilter(caiDriver1, tokenToBeRemoved);
		tokenList.remove(tokenToBeRemoved);
		
		//verify that the search result is for one remaining lead status only
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//now remove the remaining lead status from the filter chip
		tokenToBeRemoved = tokenList.get(0);
		callTabReactPage.removeFilter(caiDriver1, tokenToBeRemoved);
		tokenList.remove(tokenToBeRemoved);
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter calls on single or multiple Opportunity Stage values
	//Verify that multi select filter list values auto close upon selecting a value
	//Verify agent should be able to filter on Opportunity Stage by clicking Opportunity Stage value in any call
	@Test(groups = { "Regression" })
	public void conversation_ai_react_search_opportunity_stages(){
		
		System.out.println("Test case --conversation_ai_react_search_opportunity_stages-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to opportunity stage
		CallDataFilterType filterType = CallDataFilterType.OppStage;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for one opportunity stage
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for opportunity stage
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//filter for two opportunity
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for opportunity stage
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify user filter calls by Direction- Inbound or Outbound
	@Test(groups = { "Regression" })
	public void conversation_ai_react_search_callDirection(){
		
		System.out.println("Test case --conversation_ai_react_search_callDirection-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to inbound call direction
		CallDataFilterType filterType = CallDataFilterType.callDirectionInbound;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for inbound call direction
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		
		//verify the filtered records for inbound call direction
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		// clear the filters Set the filter type to Outbound call direction
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.callDirectionOutbound;
		
		//filter for outbound call direction
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		
		//verify the filtered records for outbound call direction
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}	

	//erify user filter calls who have Supervisor Notes
	//Verify user able to filter calls by ENGAGEMENT -Has Annotations
	//Verify user filter call recordings those have Call Notes
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_search_engagement_filters(){
		
		System.out.println("Test case --conversation_ai_react_search_engagement_filters-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to has call notes
		CallDataFilterType filterType = CallDataFilterType.HasNotes;
		List<String> tokenList = new ArrayList<String>();
		
		//filter and verify for has call notes
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		// Clear the filters and Set the filter type to has supervisor notes
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.HasSupNotes;
		
		//filter and verify for has supervisor notes
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		// Clear the filters and Set the filter type to has annotations
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.HasAnnotations;
		
		//filter and verify for has annotations
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify filter calls by call duration
	//Verify agent able to filter calls on the basis of duration of the call using Duration slider
	@Test(groups = { "Regression", "MediumPriority" })
	public void conversation_ai_react_search_callduration(){
		
		System.out.println("Test case --conversation_ai_react_search_callduration-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to call duration and set its value from 6 to 21
		CallDataFilterType filterType = CallDataFilterType.CallDuration;
		Range<Integer> duration = Range.closed(6, 21);
		
		//verify that minimum and maximum range for call duration is 0 and 120
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).second(), "120");
		
		//filter for call duration and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		callTabReactPage.verifySearchResult(caiDriver1, null, duration, filterType, true);
		
		//verify taht the call duration slider filter is showing minimum value as 6 and maximum as 21
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "6");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).second(), "21");
		
		//clear all filter and set range from 23 to 35
		callTabReactPage.clearAllFilters(caiDriver1);
		duration = Range.closed(23, 35);
		
		//filter for call duration and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		callTabReactPage.verifySearchResult(caiDriver1, null, duration, filterType, true);
		
		//verify taht the call duration slider filter is showing minimum value as 23 and maximum as 35
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "23");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).second(), "35");
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter calls by single or multiple values of Library
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_libraries(){
		
		System.out.println("Test case --conversation_ai_react_search_libraries-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);

		// Set the filter type to call library
		CallDataFilterType filterType = CallDataFilterType.Library;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for one call library  and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//filter for two call library  and verify the filtered records
		searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);	
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter calls by selecting Lead Status from Show All window
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_search_show_all_lead_status(){
		
		System.out.println("Test case --conversation_ai_search_show_all_lead_status-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Lead status
		CallDataFilterType filterType = CallDataFilterType.LeadStatus;		
		List<String> tokenList = new ArrayList<String>();
		
		//filter for two lead status from show all filter and verify the filtered records
		tokenList = callTabReactPage.setFilterFromShowAllList(caiDriver1, filterType, 0, 2);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter calls by selecting multiple call dispositions from Show All window
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_search_show_all_disposition(){
		
		System.out.println("Test case --conversation_ai_search_show_all_disposition-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to call disposition	
		CallDataFilterType filterType = CallDataFilterType.Disposition;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Two call duration from show all filter and verify the filtered records
		tokenList = callTabReactPage.setFilterFromShowAllList(caiDriver1, filterType, 0, 2);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}

	//Verify agent should be able to filter calls by selecting multiple opportunity stage values from Show All window
	@Test(groups = { "MediumPriority", "Product Sanity" })
	public void conversation_ai_search_show_all_opportunity(){
		
		System.out.println("Test case --conversation_ai_search_show_all_opportunity-- started ");
		//updating the driver used 		
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to opportunity stage
		CallDataFilterType filterType = CallDataFilterType.OppStage;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Two opportunity stages from show all filter and verify the filtered records
		tokenList = callTabReactPage.setFilterFromShowAllList(caiDriver1, filterType, 0, 2);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter calls by selecting multiple libraries from Show All window
	@Test(groups = { "Regression" })
	public void conversation_ai_search_show_all_libraries(){
		
		System.out.println("Test case --conversation_ai_search_show_all_libraries-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to call library
		CallDataFilterType filterType = CallDataFilterType.Library;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Two call library from show all filter and verify the filtered records
		tokenList = callTabReactPage.setFilterFromShowAllList(caiDriver1, filterType, 0, 2);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent can filter for his calls using My Calls option of agent filter
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_search_my_calls(){
		
		System.out.println("Test case --conversation_ai_search_my_calls-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
	
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent
		CallDataFilterType filterType = CallDataFilterType.Agent;
		String searchToken = CONFIG.getProperty("qa_cai_user_1_name").trim();
		
		//take screenshot of results before applying any filter
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		//filter for My Calls agent filter and verify the filtered records for the cai agent who is logged in
		List<String> tokenList = new ArrayList<String>();
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//remove the filter
		callTabReactPage.removeFilter(caiDriver1, "My Calls");
		
		//verify that the team dropdown becomes blank on removing search token
		assertTrue(callTabReactPage.getSelectedTeamName(caiDriver1).isEmpty());
		
		//verify that no filter is applied and the result is same as earlier
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Team filter not displayed when agent is not a member/supervisor of any team
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_agent_no_team(){
		
		System.out.println("Test case --conversation_ai_agent_no_team-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		softphoneBase.initializeDriverSoftphone("chatterOnlyDriver");
		driverUsed.put("chatterOnlyDriver", true);
		softphoneBase.loginSupport(chatterOnlyDriver);
		
		//open Conversation AI page and clear all filters
		dashboard.clickConversationAI(chatterOnlyDriver);
		callTabReactPage.clearAllFilters(chatterOnlyDriver);
		
		//expand the participants section verify that no team filter is there since user is not a part of any team
		assertFalse(callTabReactPage.isTeamFilterPresent(chatterOnlyDriver));
		
		//quit the driver
		chatterOnlyDriver.quit();
		chatterOnlyDriver = null;
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify deleted team should not be visible in TEAM filter dropdown
	//Verify My Teams filter option should be displayed when logged Agent  is member of more than one teams
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_agent_deleted_team_my_team(){
		
		System.out.println("Test case --conversation_ai_agent_deleted_team_my_team-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//defining the variable for My teams and deleted team
		String myTeam = "My Teams";
		String deletedTeam = CONFIG.getProperty("qa_cai_deleted_team");
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//verify that my team is appearing in the Agent's Team filter and Deleted team is not appearing in it
		assertTrue(callTabReactPage.getTeamIndexInTeamFilter(caiDriver1, myTeam) >= 0);
		assertEquals(callTabReactPage.getTeamIndexInTeamFilter(caiDriver1, deletedTeam), -1);
		
		caiDriver1.navigate().refresh();

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify team filter display when agent is member of at least one team also no My Teams filter option when agent is member of just one team
	@Test(groups = { "Regression" })
	public void conversation_ai_agent_single_team(){
		System.out.println("Test case --conversation_ai_agent_single_team-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		//Login with he agent who is the member on only a single team
		softphoneBase.initializeDriverSoftphone("standardUserDriver");
		driverUsed.put("standardUserDriver", true);
		softphoneBase.loginSupport(standardUserDriver);
		
		//remove all teams for above logged in agent
		caiDriver1.get(CONFIG.getProperty("qa_support_tool_site"));
		dashboard.clickOnUserProfile(caiDriver1);
		dashboard.openManageUsersPage(caiDriver1);
		usersPage.OpenUsersSettings(caiDriver1, CONFIG.getProperty("qa_standard_user_name"), CONFIG.getProperty("qa_standard_user_username"));
		usersPage.deleteAllTeams(caiDriver1);
		
		//Making the above logged in user a member of only a single team
		usersPage.clickAddTeamIcon(caiDriver1);
		usersPage.selectUserFromTeam(caiDriver1, CONFIG.getProperty("qa_group_5_name"));
		usersPage.clickSaveUserTeamBtn(caiDriver1);
		
		//open Conversation AI page and clear all filters
		String myTeam = "My Teams";
		dashboard.clickConversationAI(standardUserDriver);
		callTabReactPage.clearAllFilters(standardUserDriver);
		
		//verify that since user is member of only one team so "My Team" filter option is not appearing
		assertEquals(callTabReactPage.getTeamIndexInTeamFilter(standardUserDriver, myTeam), -1);
		assertEquals(callTabReactPage.getTeamCountInTeamFilter(standardUserDriver), 1);

		//removing all users team
		usersPage.deleteAllTeams(caiDriver1);
		
		//quitting the driver
		standardUserDriver.quit();
		standardUserDriver = null;
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify CAI recordings after selecting 'My Team' from the Team filter dropdown
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_agent_filter_my_team(){
		
		System.out.println("Test case --conversation_ai_agent_filter_my_team-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent
		CallDataFilterType filterType = CallDataFilterType.Teams;
		List<String> tokenList = new ArrayList<String>();
		tokenList.add(CONFIG.getProperty("qa_cai_user_1_name").trim());
		tokenList.add(CONFIG.getProperty("qa_cai_team_1_member").trim());
		tokenList.add("Mukesh Ringdna");
		tokenList.add("Manish Ringdna");
		
		//filter for My Teams's Calls agent filter and verify the filtered records for the cai agent who is logged in
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify Filter CAI recordings after select any Team from team dropdown
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_agent_filter_team(){
		
		System.out.println("Test case --conversation_ai_agent_filter_team-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//get the index of the team name in team filter
		int teamIndex = callTabReactPage.getTeamIndexInTeamFilter(caiDriver1, CONFIG.getProperty("qa_cai_team_1").trim());
		callTabReactPage.pressEscapeKey(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Team
		CallDataFilterType filterType = CallDataFilterType.Teams;
		
		//Add the agent's of the team selected above in token list
		List<String> tokenList = new ArrayList<String>();
		tokenList.add(CONFIG.getProperty("qa_cai_user_1_name").trim());
		tokenList.add(CONFIG.getProperty("qa_cai_team_1_member").trim());
		
		//filter for Team based on index and verify the filtered records for the cai agents who are part of the team
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, teamIndex, null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Validate single select filters-Name, Company,Title under Prospect section
	//Verify user should be able to filter CAI results based on Name of one of prospect
	//Verify user should be able to filter CAI results based on Company of one of prospect
	//Verify user should be able to filter CAI results based on Title of one of prospect
	//Verify default dropdown text on Prospect should be changed on search filters of call player page.
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_agent_filter_prospects(){
		
		System.out.println("Test case --conversation_ai_agent_filter_prospects-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Setting the value of the Prospect filters to verify
		CallsTabReactPage.prospectName = CONFIG.getProperty("prod_user_1_name");
		CallsTabReactPage.prospectCompany = CONFIG.getProperty("contact_account_name");
		CallsTabReactPage.prospectTitle = "QA";
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Verify that all the prospect filters are visible
		callTabReactPage.verifyProspectFilters(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to prospect name
		CallDataFilterType filterType = CallDataFilterType.Name;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for prospect name filter and verify the filtered records for it
		tokenList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null));
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		// Set the filter type to prospect's title now
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.Title;
		tokenList.clear();
		
		//filter for prospects title filter and verify the filtered for it
		tokenList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null));
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		// Set the filter type to propspect's company
		callTabReactPage.clearAllFilters(caiDriver1);
		filterType = CallDataFilterType.Company;
		tokenList.clear();
		
		//filter for prospects company filter and verify the filtered for it
		tokenList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null));
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//filter for other prospects company filter and verify the filtered for it
		callTabReactPage.clearAllFilters(caiDriver1);
		CallsTabReactPage.prospectCompany = "Automation_Accountsupdated";
		tokenList.add(callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null));
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);

		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	

	//Verify agent able to filter calls on Agent's 'Talk Time (%)' slider under Agent Call Metrics
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_agent_talk_time(){
		
		System.out.println("Test case --conversation_ai_react_search_agent_talk_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Agent Talk Time and set its value from 6 to 45
		CallDataFilterType filterType = CallDataFilterType.AgentTalkTime;
		Range<Integer> agentTalkTimeRange = Range.closed(6, 45);
		
		//verify that minimum and maximum range for agent Talk time is 0 and 100
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).second(), "100");
		
		//filter for Agent Talk Time and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);

		//clear all filter and set range from 55 to 98
		callTabReactPage.clearAllFilters(caiDriver1);
		agentTalkTimeRange = Range.closed(55, 98);
		
		//filter for Agents Talk Time and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to filter calls on 'Average Talk Streak (min)' slider under Agent Call Metrics
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_agent_avg_talk_streak(){
		
		System.out.println("Test case --conversation_ai_react_search_agent_avg_talk_streak-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Average Talk Streak for agent and set its value from 1 to 4
		CallDataFilterType filterType = CallDataFilterType.AvgAgentTalkStreak;
		Range<Integer> agentTalkTimeRange = Range.closed(1, 4);
		
		//verify that minimum range for Average Talk Streak for agent is 0
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		
		//filter for Average Talk Streak for agent and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);

		//clear all filter and set range from 5 to 10
		callTabReactPage.clearAllFilters(caiDriver1);
		agentTalkTimeRange = Range.closed(5, 10);
		
		//filter for Average Talk Streak for agent and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to filter calls on 'Longest Talk Streak (min)' slider under Agent Call Metrics
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_agent_longest_talk_streak(){
		
		System.out.println("Test case --conversation_ai_react_search_agent_longest_talk_streak-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to longest talk streak for agent and set its value from 1 to 3
		CallDataFilterType filterType = CallDataFilterType.LongAgentTalkStreak;
		Range<Integer> agentTalkTimeRange = Range.closed(1, 3);
		
		//verify that minimum range for longest talk streak for agent is 0
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		
		//filter for longest talk streak for agent and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);

		//clear all filter and set range from 4 to 6
		callTabReactPage.clearAllFilters(caiDriver1);
		agentTalkTimeRange = Range.closed(4, 6);
		
		//filter for longest talk streak for agent and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to filter calls on 'Average Talk Streak (min)' slider under 'Overall call Metrics'
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_overall_avg_talk_streak(){
		
		System.out.println("Test case --conversation_ai_react_search_overall_avg_talk_streak-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to overall average talk streak and set its value from 1 to 5
		CallDataFilterType filterType = CallDataFilterType.OvrAvgTalkStreak;
		Range<Integer> agentTalkTimeRange = Range.closed(1, 5);
		
		//verify that minimum range for overall average talk streak is 0
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		
		//filter for overall average talk streak and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);

		//clear all filter and set range from 6 to 9
		callTabReactPage.clearAllFilters(caiDriver1);
		agentTalkTimeRange = Range.closed(6, 9);
		
		//filter for overall average talk streak and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to filter calls on 'Longest Talk Streak (min)' slider under 'Overall call Metrics'
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_overall_longest_talk_streak(){
		
		System.out.println("Test case --conversation_ai_react_search_overall_longest_talk_streak-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to overall longest talk streak and set its value from 1 to 4
		CallDataFilterType filterType = CallDataFilterType.OvrLongTalkStreak;
		Range<Integer> agentTalkTimeRange = Range.closed(1, 4);
		
		//verify that minimum range for overall longest talk streak is 0
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, filterType).first(), "0");
		
		//filter for overall longest talk streak and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);

		//clear all filter and set range from 5 to 8
		callTabReactPage.clearAllFilters(caiDriver1);
		agentTalkTimeRange = Range.closed(5, 7);
		
		//filter for overall longest talk streak and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, agentTalkTimeRange);
		callTabReactPage.verifySearchResult(caiDriver1, null, agentTalkTimeRange, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.removeFilter(caiDriver1, searchToken);
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Validate react Time Frame Filter options and default value
	//Verify upon selecting Today option of time frame all CAI recordings of current date will be listed
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_today_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_today_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//verify that minimum and maximum range for call duration is 0 and 120
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, CallDataFilterType.CallDuration).first(), "0");
		assertEquals(callTabReactPage.getSliderRanges(caiDriver1, CallDataFilterType.CallDuration).second(), "120");
		
		//verify time frame dropdown options
		callTabReactPage.verifyTimeFrameDropDownOptions(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as today
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.Today;
		tokenList.add(searchToken.toString());
		
		//filter for Today's Time frame and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify upon selecting 1 week option of time frame all CAI recordings of last 7 days will be listed
	@Test(groups = { "MediumPriority", "Product Sanity" })
	public void conversation_ai_react_search_week_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_week_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as one week
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneWeek;
		tokenList.add(searchToken.toString());
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//filter for One Week's Time frame and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify upon selecting 1 month option of time frame all CAI recordings of last 30 days will be listed
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_one_month_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_one_month_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as one month
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneMonth;
		tokenList.add(searchToken.toString());
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//filter for One Month Time frame and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify upon selecting 3 month option of time frame all CAI recordings of last 90 days will be listed
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_three_month_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_three_month_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as Three Month
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.ThreeMonth;
		tokenList.add(searchToken.toString());
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//filter for Three Month Time frame and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify upon selecting 1 year option of time frame all CAI recordings of last 365 days will be listed
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_one_year_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_one_year_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as One Year
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneYear;
		tokenList.add(searchToken.toString());
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//filter for One Year Time frame and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify upon selecting custom option of time frame all CAI recordings of given range will be listed
	//Navigate between month/year to set custom start/end date
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_search_custom_time_frame(){
		
		System.out.println("Test case --conversation_ai_react_search_custom_time_frame-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//taking the screenshot of the filtered results before any filter applied
		File defaultCallSearchPage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as today
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.Custom;
		tokenList.add(searchToken.toString());
		
		//verify the default filter selected for Time frame in All Time
		assertEquals(callTabReactPage.getTimeIntervalLabel(caiDriver1), TimeFrame.AllTime.displayValue());
		
		//filter for Custom Time frame (1 year 1 month) and verify the filtered records
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, true);
		
		//now remove the search chip by clicking cross button
		callTabReactPage.removeFilter(caiDriver1, tokenList.get(0));
		
		//Verify that chip has been removed
		assertFalse(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, tokenList.get(0)));
		
		//verify that on on selecting custom Time Frame again Start and End box are empty
		callTabReactPage.verifyDefaultStartEndCustomBox(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//verify that the filtered result screen looks same as it was before filter applied
		File actualImage = callTabReactPage.getSearchFilterContainerScreenshot(caiDriver1);
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, defaultCallSearchPage));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Arrange data as per Newest to oldest and oldest to Newest dropdown
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_sort_call_time(){
		
		System.out.println("Test case --conversation_ai_react_sort_call_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
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
		
		//Change the search Result sort to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Sort Conversation AI Calls: Call Duration - Longest to Shortest
	//Sort Conversation AI Calls: Call Duration- Shortest to Longest
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_sort_call_duration(){
		
		System.out.println("Test case --conversation_ai_react_sort_call_duration-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		
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
	
	//Sort Conversation AI Calls: Agent Name - A to Z
	//Sort Conversation AI Calls: Agent Name - Z to A
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_react_sort_agent_name(){
		
		System.out.println("Test case --conversation_ai_react_sort_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		
		//Set Sort Type as Ageny's name from A to Z and sort records
		SortOptions sortType = SortOptions.AgenNameAZ	;
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
		
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);
		
		System.out.println("Test case is pass");
	}

	//Verify Sorting options with Other CAI filters
	//Verify upon applying sorting search result reset and data displayed from first page
	@Test(groups = { "MediumPriority" })
	public void verify_sorting_options_with_other_cai_filters() {

		System.out.println("Test case --verify_sorting_options_with_other_cai_filters-- started ");

		// updating the driver used
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		// open Conversation AI page and clear all filters
		dashboard.clickConversationAI(caiDriver1);

		String filterValue = "My Calls";
		callTabReactPage.selectAgentNameFromFilter(caiDriver1, filterValue);
	
		// Set the filter type to Supervisor Notes
		CallDataFilterType filterType = CallDataFilterType.HasSupNotes;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Supervisor Notes
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);

		//verify the filtered records for Supervisor Notes
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		// Set Sort Type as Shortest to Longest and sort records
		SortOptions sortType = SortOptions.ShorttoLong;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);

		// Get the sorted list for the duration and verify that it is ordered from
		// Shortest to Longest order
		List<Date> resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		assertNotEquals(callTabReactPage.getStartingPageNumber(caiDriver1), 1);
		boolean sorted = Ordering.natural().isOrdered(resultList);
		assertTrue(sorted);

		// Set Sort Type as Longest to Shortest and sort records
		sortType = SortOptions.LongtoShort;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		assertEquals(callTabReactPage.getStartingPageNumber(caiDriver1), 1);
		
		// Get the sorted list for the duration and verify that it is ordered from
		// Longest to Shortest order
		resultList.clear();
		resultList = callTabReactPage.getSortedCaiList(caiDriver1, sortType);
		sorted = Ordering.natural().reverse().isOrdered(resultList);
		assertTrue(sorted);

		// Change the search Result sort back to "New to Old"
		callTabReactPage.selectSearchResultSort(caiDriver1, SortOptions.NewtoOld);

		// Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify click on cloud icon from prospect detail redirect user to SFDC correctly
	@Test(groups = { "MediumPriority" })
	public void conversation_ai_open_prospect_in_sfdc(){
		
		System.out.println("Test case --conversation_ai_open_prospect_in_sfdc-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//Setting the value of the Prospect Company to verify
		CallsTabReactPage.prospectCompany = CONFIG.getProperty("contact_account_name");
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to propspect's company
		CallDataFilterType filterType = CallDataFilterType.Company;
		
		//filter for prospects company filter
		callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		
		//expand the first row and verify that agent can Opwn Prospect's Name and Company in SFDC
		callTabReactPage.expandSearchRows(caiDriver1, 1);
		callTabReactPage.verifyProspectAgentInSFDC(caiDriver1);
		callTabReactPage.verifyProspectCompanyInSFDC(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//	Verify agent should be able to filter on Disposition by clicking disposition value of any call
	@Test(groups = { "Regression" })
	public void conversation_ai_react_elastic_search_disposition(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_disposition-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Sort the CAI list from Z to A so that we are able to find record for Disposition
		SortOptions sortType = SortOptions.AgenNameZA	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		// Set the filter type to call disposition
		CallDataFilterType filterType = CallDataFilterType.Disposition;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for disposition by clicking on to it
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter on lead status by clicking lead status value of any call
	@Test(groups = { "Regression" })
	public void conversation_ai_react_elastic_search_lead_status(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_lead_status-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		String searchText = "unknown";
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Enter serach text as unknown so that user can find the first record as lead
		callTabReactPage.enterSearchText(caiDriver1, searchText);
		
		//Set Sort Type as Old to new and sort records
		SortOptions sortType = SortOptions.OldtoNew	;
		callTabReactPage.selectSearchResultSort(caiDriver1, sortType);
		
		// Set the filter type to Lead status
		CallDataFilterType filterType = CallDataFilterType.LeadStatus;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for Lead Status by clicking on to it. Now there are two filters one is Lead status and other is search text
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//Clear the search text so that now only lead status filter is there
		callTabReactPage.removeFilter(caiDriver1, searchText);
		
		//verify the filtered records for lead status
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter on agent by clicking agent value of any call
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_elastic_search_agent_name(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent
		CallDataFilterType filterType = CallDataFilterType.Agent;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for Agent by clicking on to it
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for agent
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter on Company by clicking company value of any call
	@Test(groups = { "Regression" })
	public void conversation_ai_react_elastic_search_company(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_agent_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to  Company
		CallDataFilterType filterType = CallDataFilterType.Company;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for Company by clicking on to it
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be able to filter on prospect name by clicking name value in any call
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_elastic_search_prospect_name(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_prospect_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// set the elastic filter for Prospect Name by clicking on to it
		CallDataFilterType filterType = CallDataFilterType.Name;
		List<String> tokenList = new ArrayList<String>();
		
		//set the elastic filter for Prospect Name by clicking on to it
		String searchToken = callTabReactPage.setElasticFilters(caiDriver1, filterType, 0);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify agent should be redirected to Library by clicking Library Name value in any call
	@Test(groups = { "Regression" })
	public void conversation_ai_react_elastic_search_library(){
		
		System.out.println("Test case --conversation_ai_react_elastic_search_library-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
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
		callTabReactPage.navigateToCallsPage(caiDriver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify multiple search string on call search page gives same call with all match found
	@Test(groups = { "Regression" })
	public void verify_multiple_search_string_gives_same_call_with_all_match_found() {

		System.out.println("Test case --verify_multiple_search_string_gives_same_call_with_all_match_found-- started ");

		// updating the driver used
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		callTabReactPage.setConversationAIFilters(caiDriver1, CallDataFilterType.Agent, 0, null);

		// open the first conversation AI and Select a Library for it
		String superVisorNotes = "AutoSuper".concat(HelperFunctions.GetRandomString(6));
		String notes 		   = "AutoNotes".concat(HelperFunctions.GetRandomString(6));
		
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		conversationDetailPage.addOrEditSuperVisorNotes(caiDriver1, superVisorNotes);
		conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorNotes);
	
		conversationDetailPage.addOrEditCallNotes(caiDriver1, notes);
		conversationDetailPage.verifyCallNotesSaved(caiDriver1, notes);
		
		String annotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(6));
		String userName = CONFIG.getProperty("qa_cai_user_1_name");
		conversationDetailPage.enterAnnotationWithoutTag(caiDriver1, annotationName, userName);
		
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + superVisorNotes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow="Supervisor Notes";
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow, 0), textToFindInRow+": 1 match");
		
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + annotationName + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + annotationName + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow2="Annotations";
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow2, 0), textToFindInRow2+": 1 match");
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow, 0), textToFindInRow+": 1 match");
		
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + notes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + notes + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + annotationName + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow3="Call Notes";
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow3, 0), textToFindInRow3+": 1 match");
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow2, 0), textToFindInRow2+": 1 match");
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow, 0), textToFindInRow+": 1 match");

		callTabReactPage.clearAllFilters(caiDriver1);
		
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality with single search string on call search page
	//Verify multiple search string on call search page gives different calls with OR operator
	//Verify boolean functionality with multiple search string on call search page
	@Test(groups = { "Regression" })
	public void verify_multiple_search_string_gives_diff_call_with_all_match_found() {

		System.out.println("Test case --verify_multiple_search_string_gives_diff_call_with_all_match_found-- started ");

		// updating the driver used
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		callTabReactPage.setConversationAIFilters(caiDriver1, CallDataFilterType.Agent, 0, null);
		
		// open the first conversation AI and Select a Library for it
		String superVisorNotes = "AutoSuper".concat(HelperFunctions.GetRandomString(6));
		String notes 		   = "AutoNotes".concat(HelperFunctions.GetRandomString(6));
		String annotationName = "AutoAnnotation".concat(HelperFunctions.GetRandomString(6));
		
		callTabReactPage.openConversationDetails(caiDriver1, 0);
		conversationDetailPage.addOrEditSuperVisorNotes(caiDriver1, superVisorNotes);
		conversationDetailPage.verifySuperVisorNotesSaved(caiDriver1, superVisorNotes);
	
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.openConversationDetails(caiDriver1, 1);
		conversationDetailPage.addOrEditCallNotes(caiDriver1, notes);
		conversationDetailPage.verifyCallNotesSaved(caiDriver1, notes);
		
		String userName = CONFIG.getProperty("qa_cai_user_1_name");
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.openConversationDetails(caiDriver1, 2);
		conversationDetailPage.enterAnnotationWithoutTag(caiDriver1, annotationName, userName);
		
		callTabReactPage.navigateToCallsPage(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + superVisorNotes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow="Supervisor Notes";
		assertEquals(callTabReactPage.getTextToFindInCAIRow(caiDriver1, textToFindInRow, 0), textToFindInRow+": 1 match");
		
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + annotationName + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 2);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + annotationName + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow2 = "Annotations";
		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow2 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow + ": 1 match")));

		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow2 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow + ": 1 match")));
		
		callTabReactPage.enterSearchText(caiDriver1,  "\"" + notes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 3);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + notes + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + annotationName + "\""));
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, "\"" + superVisorNotes + "\""));
		
		String textToFindInRow3 = "Call Notes";
		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow3 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow2 + ": 1 match"))
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow + ": 1 match")));

		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow3 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow2 + ": 1 match"))
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow + ": 1 match")));

		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 2).equals(textToFindInRow3 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 2).equals(textToFindInRow2 + ": 1 match"))
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 2).equals(textToFindInRow + ": 1 match")));
			
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		assertTrue(callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, "\"" + superVisorNotes + "\""));
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, "\"" + superVisorNotes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 2);
		assertFalse(callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, "\"" + superVisorNotes + "\""));
		
		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow3 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow2 + ": 1 match")));
		
		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow3 + ": 1 match")
				|| (callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow2 + ": 1 match")));

		assertFalse(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow + ": 1 match")
				|| callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 1).equals(textToFindInRow + ": 1 match"));
		

		callTabReactPage.clickFilterBooleanStatus(caiDriver1, "\"" + annotationName + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);

		assertTrue(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow3 + ": 1 match"));
		
		assertFalse(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow2 + ": 1 match"));
		assertFalse(callTabReactPage.getResultMatchTextCAIRow(caiDriver1, 0).equals(textToFindInRow + ": 1 match"));

		callTabReactPage.clickFilterBooleanStatus(caiDriver1, "\"" + annotationName + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 2);
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, "\"" + notes + "\"");
		assertEquals(callTabReactPage.getSearchPageRowCount(caiDriver1), 1);

		callTabReactPage.clearAllFilters(caiDriver1);
		
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to click to Call  from number of prospect
	@Test(groups = { "Regression" })
	public void verify_click_to_call_from_cai_notification_after_expand() {
		System.out.println("Test case --verify_click_to_call_from_cai_notification_after_expand-- started ");

		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent
		callTabReactPage.setConversationAIFilters(caiDriver1, CallDataFilterType.Agent, 0, null);
	
		String parentWindow = caiDriver1.getWindowHandle();

		callTabReactPage.expandSearchRows(caiDriver1, 1);
		inboxPage.verifyDialExensionVisible(caiDriver1);
		String name = inboxPage.getProspectDetailsName(caiDriver1);
		String number = inboxPage.getProspectDetailsNumber(caiDriver1);

		inboxPage.clickProspectDetailsNumber(caiDriver1);

		// Switch to extension
		sfSearchPage.switchToExtension(caiDriver1);

		// Verify contact details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(caiDriver1), name);
		assertEquals(callScreenPage.getCallerNumberOriginalFormat(caiDriver1), number);

		//verifying calling
		softphoneCalling.softphoneAgentCall(caiDriver1, CONFIG.getProperty("qa_cai_user_2_smart_no"));
		softphoneCalling.isCallHoldButtonVisible(caiDriver1);
		softphoneCalling.switchToTab(caiDriver2, 1);
		softphoneCalling.pickupIncomingCall(caiDriver2);
		
		softphoneCalling.idleWait(2);
		softphoneCalling.hangupActiveCall(caiDriver1);
	
		// Closing salesforce task page window
		caiDriver1.close();
		sfSearchPage.switchToTabWindow(caiDriver1, parentWindow);

		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		System.out.println("Test case --verify_click_to_call_from_cai_notification_after_expand-- passed ");
	}

	//Verify functionality of autocomplete for Agent Name filter on conversation page
	//Verify functionality of autocomplete for Agent Teams filter on conversation page
	@Test(groups = { "Regression" })
	public void verify_autocomplete_feature_agent_name_and_team_after_expand() {
		System.out.println("Test case --verify_autocomplete_feature_agent_name_and_team_after_expand-- started ");

		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);

		dashboard.switchToTab(caiDriver1, 2);
		dashboard.clickConversationAI(caiDriver1);
		
		callTabReactPage.verifyAutocompleteAgentNames(caiDriver1, "Cai", " User");
		callTabReactPage.clearAllFilters(caiDriver1);
		callTabReactPage.verifyAutocompleteAgentTeams(caiDriver1, "a", "utomation_CAI_Team");
		
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --verify_autocomplete_feature_agent_name_and_team_after_expand-- passed ");

	}
}		