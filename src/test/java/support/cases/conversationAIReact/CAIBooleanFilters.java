/**
 * @author Abhishek Gupta
 *
 */
package support.cases.conversationAIReact;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Range;

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
import support.source.conversationAIReact.CallsTabReactPage.TimeFrame;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CAIBooleanFilters extends SupportBase{
	
	CallsTabReactPage 			callTabReactPage 			= new CallsTabReactPage();
	CallToolsPanel 				callToolsPanel 				= new CallToolsPanel();
	ConversationDetailPage 		conversationDetailPage 		= new ConversationDetailPage();
	LibraryReactPage			libraryReactPage			= new LibraryReactPage();
	Dashboard 					dashboard 					= new Dashboard();
	HelperFunctions 			helperFunctions 			= new HelperFunctions();
	ReportThisCallPage			reportThisCallPage 			= new ReportThisCallPage();
	SeleniumBase 				seleniumBase 				= new SeleniumBase();
	SoftphoneBase 				softphoneBase 				= new SoftphoneBase();
	SoftphoneCallHistoryPage	softphoneCallHistoryPage	= new SoftphoneCallHistoryPage();
	SoftPhoneCalling 			softphoneCalling 			= new SoftPhoneCalling();
	UsersPage 					usersPage					= new UsersPage();
	
	@Test(groups = { "ProdOnly" }, priority = -1)
	public void make_cai_calls(){
		
		System.out.println("Test case --make_cai_calls-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		initializeSupport("caiDriver2");
		driverUsed.put("caiDriver2", true);
		
		//switch to softphone
		seleniumBase.switchToTab(caiDriver1, 1);
		seleniumBase.switchToTab(caiDriver2, 1);
		
		// make an outbound call and pick it up
		softphoneCalling.softphoneAgentCall(caiDriver1, CONFIG.getProperty("qa_cai_user_2_smart_no"));
		softphoneCalling.pickupIncomingCall(caiDriver2);

		// select a disposition and coach
		callToolsPanel.clickCallToolsIcon(caiDriver1);
		callToolsPanel.selectDispositionFromCallTools(caiDriver1, "Contacted");
		callToolsPanel.clickCallNotesSaveBtn(caiDriver1);

		// enter call notes
		seleniumBase.idleWait(60);

		// give call ratings

		// hang up the active call and wait for conversation AI to get generated
		softphoneCalling.hangupActiveCall(caiDriver1);
		seleniumBase.idleWait(5);

		//switch to web app
		seleniumBase.switchToTab(caiDriver1, 2);
		seleniumBase.switchToTab(caiDriver2, 2);
		
		//put driver used to false
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
		
	}
	
	//Verify Boolean functionality of Call Disposition filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_disposition(){
		
		System.out.println("Test case --conversation_ai_react_boolean_disposition-- started ");
		
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
		
		//filter for call disposition
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the disposition selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for call disposition
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Lead Status filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_lead_status(){
		
		System.out.println("Test case --conversation_ai_react_boolean_lead_status-- started ");
		
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
		
		//filter for Lead Status
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Lead Status
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Lead Status selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Lead Status
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Agent filter
	@Test(groups = { "Regression", "Product Sanity" })
	public void conversation_ai_react_boolean_agent(){
		
		System.out.println("Test case --conversation_ai_react_boolean_agent-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent Name
		CallDataFilterType filterType = CallDataFilterType.Agent;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Agent Name
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Agent name
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Agent Name selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Agent Name
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Timeframe-  weekly.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_week_time(){
		
		System.out.println("Test case --conversation_ai_react_boolean_week_time-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as one week
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneWeek;
		tokenList.add(searchToken.toString());
		
		//filter for one week time frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for Agent name
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain the data for last week time frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for week time frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Timeframe- Today.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_today(){
		
		System.out.println("Test case --conversation_ai_react_boolean_today-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as today
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.Today;
		tokenList.add(searchToken.toString());
		
		//filter for today time frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for Today's time frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain today's data
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for Today's time frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Agent filter - My Calls
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_my_calls(){
		
		System.out.println("Test case --conversation_ai_react_boolean_my_calls-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent Name
		CallDataFilterType filterType = CallDataFilterType.Agent;
		List<String> tokenList = new ArrayList<String>();
		String searchToken = CONFIG.getProperty("qa_cai_user_1_name").trim();
		tokenList.add(searchToken);
		
		//filter for My calls data
		String acutalToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, acutalToken);
		
		//verify the filtered records for Agent's call(My Calls)
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, acutalToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, acutalToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the  data for Agent's call(My Calls)
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, acutalToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, acutalToken);
		assertTrue(status);
		
		//verify the filtered records for Agent's call(My Calls)
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Prospect-Company filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_prospect_company(){
		
		System.out.println("Test case --conversation_ai_react_boolean_prospect_company-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Prospect Company
		CallDataFilterType filterType = CallDataFilterType.Company;
		List<String> tokenList = new ArrayList<String>();
		CallsTabReactPage.prospectCompany = CONFIG.getProperty("contact_account_name");
		
		//filter for Prospect Company
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Prospect Company
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Prospect Company selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Prospect Company
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//	Verify boolean functionality of Opportunity Stage filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_opportunity_stage(){
		
		System.out.println("Test case --conversation_ai_react_boolean_opportunity_stage-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Opportunity Stage
		CallDataFilterType filterType = CallDataFilterType.OppStage;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Opportunity Stage
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Opportunity Stage
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Opportunity Stage selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Opportunity Stage
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}

	//Verify boolean functionality of Libraries filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_libraries(){
		
		System.out.println("Test case --conversation_ai_react_boolean_libraries-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Library
		CallDataFilterType filterType = CallDataFilterType.Library;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Library
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records Library
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Library selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Library
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//	Verify boolean functionality of Engagement Filter-Has Notes (Call notes)
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_has_notes(){
		
		System.out.println("Test case --conversation_ai_react_boolean_has_notes-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Has Notes
		CallDataFilterType filterType = CallDataFilterType.HasNotes;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Has Notes
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Has Notes
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Has Notes selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Has Notes
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Engagement Filter-Has Supervisor Notes
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_has_supervisor_notes(){
		
		System.out.println("Test case --conversation_ai_react_boolean_has_supervisor_notes-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Supervisor Notes
		CallDataFilterType filterType = CallDataFilterType.HasSupNotes;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Supervisor Notes
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Supervisor Notes
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Supervisor Notes selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Supervisor Notes
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Engagement Filter-Has Annotation
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_has_annotation(){
		
		System.out.println("Test case --conversation_ai_react_boolean_has_annotation-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Has Annotation
		CallDataFilterType filterType = CallDataFilterType.HasAnnotations;
		List<String> tokenList = new ArrayList<String>();
		
		//filter for Has Annotation
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 1, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Has Annotation
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Has Annotation selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Has Annotation
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}

	//Verify boolean functionality of Timeframe-Monthly.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_monthly(){
		
		System.out.println("Test case --conversation_ai_react_boolean_monthly-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as a Month
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneMonth;
		tokenList.add(searchToken.toString());
		
		//filter for a Month Time Frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for a Month Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain the a Month Time Frame selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for a Month Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Timeframe-Quarterly
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_quaterly(){
		
		System.out.println("Test case --conversation_ai_react_boolean_quaterly-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as Three Month
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.ThreeMonth;
		tokenList.add(searchToken.toString());
		
		//filter for Three Month Time Frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for Three Month Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain the Three Month Time Frame selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for Three Month Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}

	
	//Verify boolean functionality of Timeframe-Yearly
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_yearly(){
		
		System.out.println("Test case --conversation_ai_react_boolean_today-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as an year
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.OneYear;
		tokenList.add(searchToken.toString());
		
		//filter for an Year of Time Frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for an Year of Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken.displayValue());
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain an Year of Time Frame selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for an Year of Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality of Timeframe-Custom
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_custom(){
		
		System.out.println("Test case --conversation_ai_react_boolean_custom-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Time Frame and the search token as custom
		List<String> tokenList = new ArrayList<String>();
		CallDataFilterType filterType = CallDataFilterType.TimeFrame;
		TimeFrame searchToken = TimeFrame.Custom;
		tokenList.add(searchToken.toString());
		
		//filter for Custom Time Frame
		String token = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, searchToken.ordinal(), null);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		tokenList.add(searchToken.toString());
		
		//verify the filtered records for Custom Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertFalse(status);
		
		//verify the filtered records does not contain the Custom Time Frame selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, token);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, token);
		assertTrue(status);
		
		//verify the filtered records for Custom Time Frame
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}

	//Verify boolean functionality of Prospect-Caller Name filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_prospect_name(){
		
		System.out.println("Test case --conversation_ai_react_boolean_prospect_name-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Prospect Name
		CallDataFilterType filterType = CallDataFilterType.Name;
		List<String> tokenList = new ArrayList<String>();
		CallsTabReactPage.prospectName = CONFIG.getProperty("prod_user_1_name");;
		
		//filter for Prospect Name
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Prospect Name
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Prospect Name selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Agent Name
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//	Verify boolean functionality of Prospect-Title filter.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_prospect_title(){
		
		System.out.println("Test case --conversation_ai_react_boolean_prospect_title-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Prospect Title
		CallDataFilterType filterType = CallDataFilterType.Title;
		List<String> tokenList = new ArrayList<String>();
		CallsTabReactPage.prospectTitle = "QA";
		
		//filter for Prospect Title
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, null);
		tokenList.add(searchToken);
		boolean status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify the filtered records for Prospect Title
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to false
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
		
		//verify that the status is now false
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertFalse(status);
		
		//verify the filtered records does not contain the Prospect Title selected above
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//change the status of the filter to true
		callTabReactPage.clickFilterBooleanStatus(caiDriver1, searchToken);
				
		//verify that the status is now true
		status = callTabReactPage.getSelectFilterBooleanStatus(caiDriver1, searchToken);
		assertTrue(status);
		
		//verify the filtered records for Prospect Title
		callTabReactPage.verifySearchResult(caiDriver1, tokenList, null, filterType, status);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	//Verify boolean functionality should not be tagged with Call Duration filter
	//Verify boolean functionality should not be tagged with Open Text Search filter
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_not_present(){
		
		System.out.println("Test case --conversation_ai_react_boolean_not_present-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Call Duration
		CallDataFilterType filterType = CallDataFilterType.CallDuration;
		Range<Integer> duration = Range.closed(6, 21);
		
		//filter for call duration and verify the filtered records
		String searchToken = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the case
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken));
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		//Now enter a Text to search in search box
		searchToken = "Test";
		callTabReactPage.enterSearchText(caiDriver1, searchToken);
		assertTrue(callTabReactPage.isSelectedFilterChipPresent(caiDriver1, searchToken));
		
		//verify that boolean icon is not present for the case
		assertTrue(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality should not be tagged the Agent Call Metrics filters.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_agent_matrics(){
		
		System.out.println("Test case --conversation_ai_react_boolean_agent_matrics-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent Talk Time
		CallDataFilterType filterType = CallDataFilterType.AgentTalkTime;
		Range<Integer> duration = Range.closed(30, 40);
		
		//filter for Agent Talk Time and verify the filtered records
		String searchToken1 = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the cases
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken1));
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Average Agent talk Streak
		filterType = CallDataFilterType.AvgAgentTalkStreak;
		duration = Range.closed(1, 5);
		
		//filter for Average Agent talk Streak and verify the filtered records
		String searchToken2 = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the cases
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken2));
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Agent's longest Talk Streak
		filterType = CallDataFilterType.LongAgentTalkStreak;
		duration = Range.closed(1, 4);
		
		//filter for Agent's longest Talk Streak and verify the filtered records
		String searchToken3 = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the cases
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken3));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
	
	//Verify boolean functionality should not be tagged the Overall Call Metrics filters.
	@Test(groups = { "Regression" })
	public void conversation_ai_react_boolean_overall_matrics(){
		
		System.out.println("Test case --conversation_ai_react_boolean_overall_matrics-- started ");
		
		//updating the driver used 
		initializeSupport("caiDriver1");
		driverUsed.put("caiDriver1", true);
		
		//open Conversation AI page and clear all filters
		if(!callTabReactPage.isUserOnCallsTab(caiDriver1))
				dashboard.clickConversationAI(caiDriver1);
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Overall Average Talk Streak
		CallDataFilterType filterType = CallDataFilterType.OvrAvgTalkStreak;
		Range<Integer> duration = Range.closed(2, 4);
		
		//filter for Overall Average Talk Streak and verify the filtered records
		String searchToken1 = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the case
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken1));
		callTabReactPage.clearAllFilters(caiDriver1);
		
		// Set the filter type to Overall Longest Talk Streak
		filterType = CallDataFilterType.OvrLongTalkStreak;
		duration = Range.closed(1, 5);
		
		//filter for Overall Longest Talk Streak and verify the filtered records
		String searchToken2 = callTabReactPage.setConversationAIFilters(caiDriver1, filterType, 0, duration);
		
		//verify that boolean icon is not present for the cases
		assertFalse(callTabReactPage.isFilterBooleanStatusPresent(caiDriver1, searchToken2));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("caiDriver1", false);
		
		System.out.println("Test case is pass");
	}
}