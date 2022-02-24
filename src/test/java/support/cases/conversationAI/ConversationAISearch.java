package support.cases.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import freemarker.core.ParseException;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.commonpages.CallRecordingPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.CallsTabPage.CallData;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAI.InboxTabPage;
import support.source.conversationAI.LibraryTabPage;
import support.source.conversationAI.SetUpTabPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

/**
 * @author Vishal Chaudhary
 *
 */
public class ConversationAISearch extends SupportBase {

	CallsTabPage callsTabPage = new CallsTabPage();
	SetUpTabPage setUpTabPage = new SetUpTabPage();
	CallRecordingPage callRecordingPage = new CallRecordingPage();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	InboxTabPage inboxTabPage = new InboxTabPage();
	LibraryTabPage libraryTabPage = new LibraryTabPage();
	UsersPage usersPage = new UsersPage();
	DashBoardConversationAI dashBoardConversationAI = new DashBoardConversationAI();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
 
	@BeforeClass(groups = { "Regression" })
	public void initialize_admin() {
		// updating the driver used
		initializeSupport("caiDriver1");
		initializeSupport("caiDriver2");
		callsTabPage.switchToTab(caiDriver1, 1);
		callsTabPage.switchToTab(caiDriver2, 1);
//		 Open dial pad of admin driver and dial to support driver
		softPhoneCalling.softphoneAgentCall(caiDriver2, CONFIG.getProperty("qa_cai_user_2_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiDriver1);
		callToolsPanel.selectDispositionUsingText(caiDriver1, CONFIG.getProperty("qa_cai_disposition"));
		callsTabPage.idleWait(70);
		softPhoneCalling.hangupActiveCall(caiDriver1);
		callsTabPage.switchToTab(caiDriver1, 2);
		callsTabPage.switchToTab(caiDriver2, 2);
	}

	@Test(groups = { "Regression" }, priority = 1)
	public void conversation_ai_search_ui_duration() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test case --conversation ai search -Call Duration-- started ");
		callsTabPage.setDuration(caiDriver1);
		// callsTabPage.clickCallTab(caiDriver1);
		Pair<String, String> durationRange = callsTabPage.getCallDurationRange(caiDriver1);
		float min = Float.parseFloat(durationRange.first().trim());
		float max = Float.parseFloat(durationRange.second().trim());
		// String maxDuration=callsTabPage.getMaxDuration(caiDriver1);
		System.out.println(min);
		System.out.println(max);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyCallDurationRange(caiDriver1, min, max, page);
		System.out.println("Test case --conversation ai search -Call Duration-- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 2)
	public void conversation_ai_search_ui_agent() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Set Agent Filter and check results
		System.out.println("Test case --conversation ai search -Agent drop down-- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		// Set Agent
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Agent.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent drop down-- Passed ");

		// Apply Boolean Filter(-) on Agent
		System.out.println("Test case --conversation ai search -Agent drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter back to (+) on Agent
		System.out.println("Test case --conversation ai search -Agent drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 3)
	public void conversation_ai_search_ui_mycalls() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Set My Calls Filter and check results
		System.out.println("Test case --conversation ai search -Agent-MyCall drop down-- Started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.idleWait(2);
		callsTabPage.setAgentFilter(caiDriver1, "My Calls");
		String searchToken = callsTabPage.getLoggedInAgentName(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent-MyCall drop down-- Passed ");

		// Apply Boolean Filter(-) on Agent-My Calls
		System.out.println("Test case --conversation ai search -MyCall drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter back to (+) on Agent-My Calls
		System.out.println("Test case --conversation ai search -Agent-MyCall drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		System.out.println("Test case --conversation ai search -Agent-MyCall drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Company Filter and check results

	@Test(groups = { "Regression" }, priority = 4)
	public void conversation_ai_search_ui_company() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Company- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Company.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Company.name(), status);
		System.out.println("Test case --conversation ai search -Company drop down-Passed ");

		// Apply Boolean Filter(-) on Company
		System.out.println("Test case --conversation ai search -Company drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Company.name(), status);
		System.out.println("Test case --conversation ai search -Company drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter back to (+) on Company
		System.out.println("Test case --conversation ai search -Company drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Company.name(), status);
		System.out.println("Test case --conversation ai search -Company drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 5)
	public void conversation_ai_search_ui_sfdcname() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Set SFDC Name Filter and check results
		System.out.println("Test case --conversation ai search -Caller Name Drop down- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Name.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Name.name(), status);
		System.out.println("Test case --conversation ai search -Caller Name Drop down- Passed ");

		// Apply Boolean Filter(-) on Caller Name
		System.out.println("Test case --conversation ai search -Caller Name drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Name.name(), status);
		System.out.println("Test case --conversation ai search -Caller Name (-)Boolean Filter- Passed ");

		// Apply Boolean Filter back to (+) on Caller Name
		System.out.println("Test case --conversation ai search -Caller Name drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Name.name(), status);
		System.out.println("Test case --conversation ai search -Caller Name drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Title Filter and check results
	@Test(groups = { "Regression" }, priority = 6)
	public void conversation_ai_search_ui_title() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Title drop down- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Title.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Title.name(), status);
		System.out.println("Test case --conversation ai search -Title drop down--Passed ");

		// Apply Boolean Filter(-) on Title
		System.out.println("Test case --conversation ai search -Title drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Title.name(), status);
		System.out.println("Test case --conversation ai search -Title drop down-(+)Boolean Filter- Passed ");

		// Apply Boolean Filter back to (+) on Title
		System.out.println("Test case --conversation ai search -Title drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Title.name(), status);
		System.out.println("Test case --conversation ai search -Title drop down-(+)Boolean Filter- started ");
		driverUsed.put("caiDriver1", false);
	}
	// Set Lead Status from Show All

	@Test(groups = { "Regression" }, priority = 7)
	public void conversation_ai_search_ui_lead_status() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Lead Status Show All Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setShowAllFilter(caiDriver1, CallData.ShowAllLeadStatus.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.LeadStatus.name(), status);
		System.out.println("Test case --conversation ai search -Lead Status Show All- Passed ");

		// Set Lead Status Filter and check results
		System.out.println("Test case --conversation ai search -Lead Status drop down- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		searchToken = callsTabPage.setFilter(caiDriver1, CallData.LeadStatus.name());
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.LeadStatus.name(), status);
		System.out.println("Test case --conversation ai search -Lead Status drop down- Passed");

		// Apply Boolean Filter (-) on Lead Status
		System.out.println("Test case --conversation ai search -Lead Status drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.LeadStatus.name(), status);
		System.out.println("Test case --conversation ai search -Lead Status drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter (+)back to on Lead Status
		System.out.println("Test case --conversation ai search -Lead Status drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.LeadStatus.name(), status);
		System.out.println("Test case --conversation ai search -Lead Status drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 8)
	public void conversation_ai_search_ui_disposition() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Set Call Disposition Status from Show All
		System.out.println("Test case --conversation ai search -Disposition Show All Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setShowAllFilter(caiDriver1, CallData.ShowAllCallDisposition.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Disposition.name(), status);
		System.out.println("Test case --conversation ai search -Disposition Show All Filter- Passed ");

		// Set Disposition Filter and check results
		System.out.println("Test case --conversation ai search -Disposition drop down-- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		searchToken = callsTabPage.setFilter(caiDriver1, CallData.Disposition.name());
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Disposition.name(), status);
		System.out.println("Test case --conversation ai search -Disposition drop down-Passed ");

		// Apply Boolean Filter (-) on Disposition
		System.out.println("Test case --conversation ai search -Disposition drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Disposition.name(), status);
		System.out.println("Test case --conversation ai search -Disposition drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter (+)back to on Disposition
		System.out.println("Test case --conversation ai search -Disposition drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Disposition.name(), status);
		System.out.println("Test case --conversation ai search -Disposition drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Opp Stage Status from Show All
	@Test(groups = { "Regression" }, priority = 9)
	public void conversation_ai_search_ui_oppstage() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Opp Stage Show All Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setShowAllFilter(caiDriver1, CallData.ShowAllOppStage.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.OppStage.name(), status);
		System.out.println("Test case --conversation ai search -Opp Stage Show All Filter- Passed ");

		// Set OppStage Filter and check results
		System.out.println("Test case --conversation ai search -Opp Stage drop down Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		searchToken = callsTabPage.setFilter(caiDriver1, CallData.OppStage.name());
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.OppStage.name(), status);
		System.out.println("Test case --conversation ai search -Opp Stage drop down Filter- Passed ");

		// Apply Boolean Filter (-) on OppStage
		System.out.println("Test case --conversation ai search -Opp Stage drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.OppStage.name(), status);
		System.out.println("Test case --conversation ai search -Opp Stage drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean Filter (+)back to on OppStage
		System.out.println("Test case --conversation ai search -Opp Stage drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.OppStage.name(), status);
		System.out.println("Test case --conversation ai search -Opp Stage drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Tag Stage Filter and check results
	@Test(groups = { "Regression" }, priority = 10)
	public void conversation_ai_search_ui_tag() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Tag drop down Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Tag.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Tag.name(), status);
		System.out.println("Test case --conversation ai search -Tag drop down Filter- Passed ");

		// Apply Boolean filter (-) on Tag
		System.out.println("Test case --conversation ai search -Tag drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Tag.name(), status);
		System.out.println("Test case --conversation ai search -Tag drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean filter (+) on Tag
		System.out.println("Test case --conversation ai search -Tag drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Tag.name(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test case --conversation ai search -Tag drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Library Filter and check results
	@Test(groups = { "Regression" }, priority = 11)
	public void conversation_ai_search_ui_library() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Library drop down Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.Library.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Library.name(), status);
		System.out.println("Test case --conversation ai search -Library drop down Filter- Passed ");

		// Apply Boolean filter (-) on Library
		System.out.println("Test case --conversation ai search -Library drop down-(-)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Library.name(), status);
		System.out.println("Test case --conversation ai search -Library drop down-(-)Boolean Filter- Passed ");

		// Apply Boolean filter (+) on Library
		System.out.println("Test case --conversation ai search -Library drop down-(+)Boolean Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Library.name(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test case --conversation ai search -Library drop down-(+)Boolean Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Has Notes
	@Test(groups = { "Regression" }, priority = 12)
	public void conversation_ai_search_ui_has_notes() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Has Notes Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.HasNotes.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Notes Filter- Passed ");

		// Apply Boolean Filter on Has Notes
		System.out.println("Test case --conversation ai search -Has Notes Boolean (-) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Notes Boolean (-) Filter- Passed ");

		// Apply Boolean Filter again on Has Notes
		System.out.println("Test case --conversation ai search -Has Notes Boolean (+) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Notes Boolean (+) Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Has Supervisor Notes
	@Test(groups = { "Regression" }, priority = 13)
	public void conversation_ai_search_ui_has_supnotes() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Supervisor Notes - started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.HasSupNotes.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasSupNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Supervisor Notes Filter- Passed ");

		// Apply Boolean Filter on Has Sup Notes
		System.out.println("Test case --conversation ai search -Supervisor Notes Boolean (-) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasSupNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Supervisor Notes Boolean (-) Filter- Passed ");

		// Apply Boolean Filter again on Has Sup Notes
		System.out.println("Test case --conversation ai search -Supervisor Notes Boolean (+) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasSupNotes.name(), page, status);
		System.out.println("Test case --conversation ai search -Supervisor Notes Boolean (+) Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Has Annotation Notes
	@Test(groups = { "Regression" }, priority = 14)
	public void conversation_ai_search_ui_has_annotation() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Has Annotation Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.HasAnnotations.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasAnnotations.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Annotation Filter- Passed ");

		// Apply Boolean Filter on Has Annotation
		System.out.println("Test case --conversation ai search -Has Annotation Boolean (-) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasAnnotations.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Annotation Boolean (-) Filter- Passed ");

		// Apply Boolean Filter again on Has Annotation
		System.out.println("Test case --conversation ai search -Has Annotation Boolean (+) Filter- started ");
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasAnnotations.name(), page, status);
		System.out.println("Test case --conversation ai search -Has Annotation Boolean (+) Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Call Direction Inbound
	//@Test(groups = { "Regression" }, priority = 15)
	public void conversation_ai_search_ui_call_direction() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Call Direction-Inbound Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.callDirectionInbound.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyCallDirection(caiDriver1, CallData.callDirectionInbound.name(), page);
		System.out.println("Test case --conversation ai search -Call Direction-Inbound Filter- Passed ");

		// Set Call Direction Outbound
		System.out.println("Test case --conversation ai search -Call Direction-Outbound Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.callDirectionOutbound.name());
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyCallDirection(caiDriver1, CallData.callDirectionOutbound.name(), page);
		System.out.println("Test case --conversation ai search -Call Direction-Outbound Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	// Set Tag from Show All
	@Test(groups = { "Regression" }, priority = 16)
	public void conversation_ai_search_ui_tag_lib_show_all() throws ParseException {

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		System.out.println("Test case --conversation ai search -Tag Show All Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		String searchToken = callsTabPage.setShowAllFilter(caiDriver1, CallData.ShowAllTags.name());
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Tag.name(), status);
		System.out.println("Test case --conversation ai search -Tag Show All Filter- Passed ");

		// Set Library from Show All
		System.out.println("Test case --conversation ai search -Library Show All Filter- started ");
		callsTabPage.clearAllFilters(caiDriver1);
		searchToken = callsTabPage.setShowAllFilter(caiDriver1, CallData.ShowAllLibraries.name());
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, searchToken, page, CallData.Library.name(), status);
		System.out.println("Test case --conversation ai search -Library Show All Filter- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 17)
	public void conversation_ai_elastic_search_agent() {
		System.out.println("Test case --conversation ai elastic search-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Elastic Search on Agent Name
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String agentToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.Agent.name(), page);
		System.out.println("agentToken Clicked is->" + agentToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, agentToken, page, CallData.Agent.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 18)
	public void conversation_ai_elastic_search_owner() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Owner
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String sfdcNameToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.Name.name(), page);
		System.out.println("sfdcNameToken Clicked is->" + sfdcNameToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, sfdcNameToken, page, CallData.Name.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 19)
	public void conversation_ai_elastic_search_company() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Company
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String sfdcCompanyToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.Company.name(), page);
		System.out.println("sfdcCompanyToken Clicked is->" + sfdcCompanyToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, sfdcCompanyToken, page, CallData.Company.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 20)
	public void conversation_ai_elastic_search_title() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Title
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String sfdcTitleToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.Title.name(), page);
		System.out.println("sfdcTitleToken Clicked is->" + sfdcTitleToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, sfdcTitleToken, page, CallData.Title.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 21)
	public void conversation_ai_elastic_search_disposition() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Disposition
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String dispositionToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.Disposition.name(), page);
		System.out.println("Disposition Token Clicked is->" + dispositionToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, dispositionToken, page, CallData.Disposition.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 22)
	public void conversation_ai_elastic_search_lead_status() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Lead Status
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String leadStatusnToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.LeadStatus.name(), page);
		System.out.println("Lead Status Token Clicked is->" + leadStatusnToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, leadStatusnToken, page, CallData.LeadStatus.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 23)
	public void conversation_ai_elastic_search_opp_stage() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Opportunity Stage
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String oppStageToken = callsTabPage.clickElasticSearchFilter(caiDriver1, CallData.OppStage.name(), page);
		System.out.println("Opp Stage Token Clicked is->" + oppStageToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifySearchResult(caiDriver1, oppStageToken, page, CallData.OppStage.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 24)
	public void conversation_ai_elastic_search_tag() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Tag Result
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String tagToken = callsTabPage.clickTabLibrary(caiDriver1, CallData.Tag.name(), page);
		System.out.println("Tag Token Clicked is->" + tagToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, tagToken, page, CallData.Tag.name(), status);
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 25)
	public void conversation_ai_elastic_search_library() {
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		// Elastic Search on Library Result
		callsTabPage.clearAllFilters(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		String libraryToken = callsTabPage.clickTabLibrary(caiDriver1, CallData.Library.name(), page);
		System.out.println("Library Token Clicked is->" + libraryToken);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTabLibSearchResult(caiDriver1, libraryToken, page, CallData.Library.name(), status);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --conversation ai elastic search-- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 26)
	public void conversation_ai_call_search_order_ui() throws Exception {
		System.out.println("Test Case-Arrange data as per Newest to oldest and oldest to Newest dropdown--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Default search order is newest to oldest
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString(),"callTime");
		System.out.println("Default search order is descending-Passed");

		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Ascending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Ascending.toString(),"callTime");
		System.out.println("Ascending search order -Passed");
		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Descending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString(),"callTime");
		System.out.println("Descending search order -Passed");
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Arrange data as per Newest to oldest and oldest to Newest dropdown--Passed");
	}

	// Conversation AI search based on time frame
	@Test(groups = { "Regression" }, priority = 27)
	public void conversation_ai_search_time_frame() throws Exception {
		System.out.println("Test case --conversation ai search-TimeFrame-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);

		// Set Today Time Frame and Verify Results
		System.out.println("Check for Today Time Frame");
		callsTabPage.selectTimeFrame(caiDriver1, CallsTabPage.TimeFrameOptions.Today.toString());
		callsTabPage.idleWait(2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Today.toString(), status);

		// Apply Boolean Filter(-) on Today Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Today.toString(), status);

		// ReApply Boolean Filter(+) on Today Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Today.toString(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Check for Today Time Frame-Passed");

		// Set Week Time Frame and Verify Results
		System.out.println("Check for Week Time Frame");
		callsTabPage.selectTimeFrame(caiDriver1, CallsTabPage.TimeFrameOptions.Week.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Week.toString(), status);
  

		// Apply Boolean Filter(-) on Weekly Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Week.toString(), status);

		// ReApply Boolean Filter(+) on Weekly Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Week.toString(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Check for Week Time Frame-Passed");

		// Set Month Time Frame and Verify Results
		System.out.println("Check for Month Time Frame");
		callsTabPage.selectTimeFrame(caiDriver1, CallsTabPage.TimeFrameOptions.Month.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Month.toString(), status);
  

		// Apply Boolean Filter(-) on Monthly Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Month.toString(), status);

		// ReApply Boolean Filter(+) on Monthly Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Month.toString(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Check for Month Time Frame-Passed");

		// Set Quarter Time Frame and Verify Results
		System.out.println("Check for Quarter Time Frame");
		callsTabPage.selectTimeFrame(caiDriver1, CallsTabPage.TimeFrameOptions.Quarter.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Quarter.toString(), status);
  

		// Apply Boolean Filter(-) on Quarter Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Quarter.toString(), status);

		// ReApply Boolean Filter(+) on Quarter Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Quarter.toString(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Check for Quarter Time Frame-Passed");

		// Set Custom Time Frame and Verify results
		System.out.println("Check for Custom Time Frame");
		callsTabPage.selectTimeFrame(caiDriver1, CallsTabPage.TimeFrameOptions.Set.toString());
		callsTabPage.idleWait(2);

		// Enter Last month's last date as Start Date and Current Date as End
		// Date
		callsTabPage.clickStartDate(caiDriver1);
		callsTabPage.setStartDate(caiDriver1);
		callsTabPage.clickEndDate(caiDriver1);
		callsTabPage.setEndDate(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Set.toString(), status);
		// Apply Boolean Filter(-) on Custom Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Set.toString(), status);

		// ReApply Boolean Filter(+) on Custom Time Frame
		callsTabPage.clickBooleanFilter(caiDriver1);
		status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTimeFrameSearch1(caiDriver1, page, CallsTabPage.TimeFrameOptions.Set.toString(), status);
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println("Check for Custom Time Frame-Passed");
		System.out.println("Test case --conversation ai search-TimeFrame-- Passed ");
	}

	@Test(groups = { "Regression" }, priority = 28)
	public void conversation_ai_add_library() throws Exception {
		System.out.println("Test case --Add/Edit/Delete Library and verify on CAI page-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		dashBoardConversationAI.navigateToLibrarySection(caiDriver1);
		String libName = libraryTabPage.addLibrary(caiDriver1);
		System.out.println("Added a new Library");

		// open callsTabPage and Verify that Added library displayed , Also
		// associate Library
		boolean verifyLibraryAdded = callsTabPage.verifyLibraryAddedOnCallTab(caiDriver1, libName);

		System.out.println("Added Library displayed on Calls and Library Tab");
		assertTrue(verifyLibraryAdded, "Added Library not displayed on Conversation AI");

		// Open first Call on list and Associate newly added Library with it
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.viewCAI(caiDriver1);
		// Get call data to later verify this call when filter with Library
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		CallDetails.put(CallsTabPage.CallData.Agent, callsTabPage.getCallAgent(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDate, callsTabPage.getCallTime(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDuration, callsTabPage.getCallDuration(caiDriver1));

		// Set newly added Library on this call
		callsTabPage.setLibraryOnCall(caiDriver1, libName);
		callsTabPage.idleWait(2);
		callsTabPage.clickCallTab(caiDriver1);

		System.out.println("Able to associate Call to newly added library");
		callsTabPage.setLibraryFilter(caiDriver1, libName);

		System.out.println("Able to set Newly created libary as Library Filter");
		boolean verifyData = callsTabPage.verifyAssociatedLibraryCall(caiDriver1, CallDetails);
		assertTrue(verifyData);
		System.out.println("Able to see Associated call upon set Newly created libary as Library Filter");
		// Open and check on Library Tab
		dashBoardConversationAI.navigateToLibrarySection(caiDriver1);
		assertTrue(libraryTabPage.isLibraryExistsOnLibraryTab(caiDriver1, libName));
		callsTabPage.clickLibraryOnLibraryTab(caiDriver1, libName);
		verifyData = callsTabPage.verifyAssociatedLibraryCall(caiDriver1, CallDetails);
		assertTrue(verifyData);
		System.out.println(
				"Able to see Associated call upon set Newly created libary as Library Filter on Library Tab as well");

		// Open Account-Library tab to update Library
		callsTabPage.idleWait(2);
		String updatedLibName = libraryTabPage.updateLibrary(caiDriver1, libName);
		System.out.println("Update Library Name of newly created libaray");
		// open callsTabPage and Verify updated library added to Library filter
		// and upon set filter same call dispalyed
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setLibraryFilter(caiDriver1, updatedLibName);
		verifyData = callsTabPage.verifyAssociatedLibraryCall(caiDriver1, CallDetails);
		assertTrue(verifyData);

		// open conversation AI-Library tab and check updated Libary name there
		// and its associated call

		libraryTabPage.clickLibraryTab(caiDriver1);
		assertTrue(libraryTabPage.isLibraryExistsOnLibraryTab(caiDriver1, updatedLibName));
		callsTabPage.clickLibraryOnLibraryTab(caiDriver1, updatedLibName);
		verifyData = callsTabPage.verifyAssociatedLibraryCall(caiDriver1, CallDetails);
		assertTrue(verifyData);
		callsTabPage.idleWait(2);
		System.out.println("Updated libaray name reflected on associated call,Library filter and Library Tab");
		// Delete Library and Verify that Libary removed from conversation AI as
		// well

		System.out.println("Now delete this libaray");
		// Open Account-Library tab to Delete Library

		callsTabPage.idleWait(2);
		libraryTabPage.deleteLibrary(caiDriver1, updatedLibName);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.viewCAI(caiDriver1);
		callsTabPage.clickLibraryPicker(caiDriver1);

		// LibraryList on Player
		assertFalse(callsTabPage.checkLibInList(caiDriver1, libName), "Added Library Not removed from List");

		callsTabPage.closeLibraryBox(caiDriver1);
		// LibraryList on Library Tab
		libraryTabPage.clickLibraryTab(caiDriver1);
		assertFalse(libraryTabPage.isLibraryExistsOnLibraryTab(caiDriver1, libName),
				"Added Library Not removed from List");

		System.out.println(
				"Associated libaray removed from associated call, libaray filter and from Library tab as well");
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --Add/Edit/Delete Library and verify on CAI page-- Passed ");

	}

	@Test(groups = { "Regression" }, priority = 29)
	public void conversation_ai_shared_with_others() throws Exception {
		System.out.println(
				"Test Case-Share call recording to single or multiple users,Inbox notification generated--Started ");
		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		dashboard.clickConversationAI(caiDriver2);
		callsTabPage.clearAllFilters(caiDriver2);
		// Before sharing , take inbox count of agent to whom we are going to
		// share call
		int inboxAgent2 = inboxTabPage.getInboxCount(caiDriver2);
		System.out.println(inboxAgent2);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));

		callsTabPage.viewCAI(caiDriver1);

		// Get Call Data
		// Get call data to later verify this call when filter with Library
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		CallDetails.put(CallsTabPage.CallData.Agent, callsTabPage.getCallAgent(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDate, callsTabPage.getCallTime(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDuration, callsTabPage.getCallDuration(caiDriver1));

		callsTabPage.enterCallNotes(caiDriver1, "");

		// Shared with others filter test, the call which yet not shared should
		// not be listed here.

		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.SharedWithOthers.name());
		assertFalse(CallDetails.get(CallData.CallDate).trim()
				.equals(inboxTabPage.getCallTimeInboxResult(caiDriver1).trim()));
  
		System.out.println("Till call not shared, call not listed with shared with others filter");

		// remove all filters
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.viewCAI(caiDriver1);

		System.out.println("Try to access call from another user without sharing, it will be opened in lite player");

		caiDriver2.get(caiDriver1.getCurrentUrl());
		callsTabPage.waitForPageLoaded(caiDriver2);
		callRecordingPage.isDownloadRecordingButtonVisible(caiDriver2);
		assertTrue(caiDriver2.getCurrentUrl().contains("#call-player"));

		// Share call to another agent
		callsTabPage.clickShareIcon(caiDriver1);
		callsTabPage.enterShareId(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		callsTabPage.clickSendIcon(caiDriver1);
		callsTabPage.idleWait(2);

		// After sharing call try to access it from another user driver now its
		// opened in smart recording
		caiDriver2.get(caiDriver1.getCurrentUrl());
		callsTabPage.waitForPageLoaded(caiDriver2);
		callsTabPage.idleWait(2);
		assertFalse(caiDriver2.getCurrentUrl().contains("#call-player"));
		assertTrue(caiDriver2.getCurrentUrl().contains("#smart-recordings"));

		// As call now shared it should be listed with shared with others filter
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.SharedWithOthers.name());
		assertTrue(CallDetails.get(CallData.CallDate).trim()
				.equals(inboxTabPage.getCallTimeInboxResult(caiDriver1).trim()));
		assertTrue(CallDetails.get(CallData.CallDuration).trim()
				.equals(inboxTabPage.getCallDurationInboxResult(caiDriver1).trim()));
		callsTabPage.viewCAI(caiDriver1);

		System.out.println("Upon share call, call listed with shared with others filter");

		// Inbox count of agent to whom we share call should be increased by 1
		assertEquals(inboxTabPage.getInboxCount(caiDriver2), inboxAgent2 + 1);

		// Verify shared call exists on Call Tab Agent to whom we share call
		callsTabPage.clickCallTab(caiDriver2);
		callsTabPage.setAgentFilter(caiDriver2, CONFIG.getProperty("qa_cai_user_2_name"));
		boolean verifyData = callsTabPage.verifyAssociatedLibraryCall(caiDriver2, CallDetails);
		assertTrue(verifyData);

		// Verify shared call exists on Inbox Tab Agent to whom we share call
		inboxTabPage.clickInboxTab(caiDriver2);

		System.out.println("As shared call notification yet to read its should be marked as unread");

		assertTrue(inboxTabPage.inboxMessageUnread(caiDriver2));

		inboxTabPage.viewInboxMessage(caiDriver2);
		assertEquals(CallDetails.get(CallData.Agent), inboxTabPage.getAgentInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDate), inboxTabPage.getCallTimeInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDuration), inboxTabPage.getCallDurationInboxResult(caiDriver2));

		System.out.println(
				"As no data given for call notes,supervisor notes and annotation given to the call so all icons are disabled");
		assertFalse(inboxTabPage.getSupNotesIconStatus(caiDriver2));
		assertFalse(inboxTabPage.getAnnotationIconStatus(caiDriver2));
		assertFalse(inboxTabPage.getCallNotesIconStatus(caiDriver2));

		// Enter call notes for call
		callsTabPage.enterCallNotes(caiDriver1, "notes123");
		inboxTabPage.clickInboxTab(caiDriver2);
		inboxTabPage.viewInboxMessage(caiDriver2);
		assertEquals(CallDetails.get(CallData.Agent), inboxTabPage.getAgentInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDate), inboxTabPage.getCallTimeInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDuration), inboxTabPage.getCallDurationInboxResult(caiDriver2));

		System.out.println("Call notes given so its icon become Active");
		assertFalse(inboxTabPage.getSupNotesIconStatus(caiDriver2));
		assertFalse(inboxTabPage.getAnnotationIconStatus(caiDriver2));
		assertTrue(inboxTabPage.getCallNotesIconStatus(caiDriver2));
  
		inboxTabPage.viewCAIInbox(caiDriver2);
		assertEquals(CallDetails.get(CallData.Agent), callsTabPage.getCallAgent(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDate), callsTabPage.getCallTime(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDuration), callsTabPage.getCallDuration(caiDriver2));

		// Inbox count of agent to whom we share call should be decreased by 1
		// when we open call from inbox tab
		assertEquals(inboxTabPage.getInboxCount(caiDriver2), inboxAgent2);
		System.out.println(
				"Test Case-Share call recording to single or multiple users,,Inbox notification generated--Passed ");
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
	}

	@Test(groups = { "Regression" }, priority = 30)
	public void conversation_ai_sup_notes() throws Exception {

		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		dashboard.clickConversationAI(caiDriver2);
		callsTabPage.clearAllFilters(caiDriver2);

		System.out.println("Test Case-Provide Supervisor notes in shared calls,Inbox notification generated--Started ");
		// Now provide Supervisor notes from another user
		// Before providing sup notes , take inbox count of agent whose call is
		// provided with sup notes
		int inboxAgent2 = inboxTabPage.getInboxCount(caiDriver1);
		System.out.println(inboxAgent2);

		System.out.println(inboxAgent2);
		callsTabPage.setAgentFilter(caiDriver2, CONFIG.getProperty("qa_cai_user_2_name"));

		callsTabPage.viewCAI(caiDriver2);

		// Get Call Data
		// Get call data to later verify this call when filter with Library
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		CallDetails.put(CallsTabPage.CallData.Agent, callsTabPage.getCallAgent(caiDriver2));
		CallDetails.put(CallsTabPage.CallData.CallDate, callsTabPage.getCallTime(caiDriver2));
		CallDetails.put(CallsTabPage.CallData.CallDuration, callsTabPage.getCallDuration(caiDriver2));

		// Enter Notes
		String notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(3));
		callsTabPage.enterSupervisorNotes(caiDriver2, notes);

		// Inbox count of agent, whose call provided with supervisor
		// notes,increased by one
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), inboxAgent2 + 1);

		// Verify Supervisor notes notification displayed for Agent whose call
		// provided with supervisor notes,increased by one
		inboxTabPage.clickInboxTab(caiDriver1);

		System.out.println("As supervisor notification yet to read its should be displayed as unread");
		assertTrue(inboxTabPage.inboxMessageUnread(caiDriver1));

		inboxTabPage.viewInboxMessage(caiDriver1);
		assertEquals(CallDetails.get(CallData.Agent), inboxTabPage.getAgentInboxResult(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDate), inboxTabPage.getCallTimeInboxResult(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDuration), inboxTabPage.getCallDurationInboxResult(caiDriver1));

		System.out.println("As supervisor notes given its icon also set to Active. Annotation yet disabled");
		assertTrue(inboxTabPage.getSupNotesIconStatus(caiDriver1));
		// assertFalse(inboxTabPage.getAnnotationIconStatus(caiDriver1));
		// assertTrue(inboxTabPage.getCallNotesIconStatus(caiDriver1));

		// Verify that given Sup notes displayed in Inbox section
		assertTrue(inboxTabPage.supNoteEntryInbox(caiDriver1, notes));
		inboxTabPage.viewCAIInbox(caiDriver1);
		assertEquals(CallDetails.get(CallData.Agent), callsTabPage.getCallAgent(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDate), callsTabPage.getCallTime(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDuration), callsTabPage.getCallDuration(caiDriver1));

		// After open the call from notification, Inbox count should decreased
		// by one
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), inboxAgent2);
		inboxTabPage.clickInboxTab(caiDriver1);

		System.out.println("As supervisor notification already read its should be displayed as read now");

		assertFalse(inboxTabPage.inboxMessageUnread(caiDriver1));
		System.out.println("Test Case-Provide Supervisor notes in shared calls,Inbox notification generated--Passed ");
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
	}

	@Test(groups = { "Regression" }, priority = 31)
	public void conversation_ai_annotation() throws Exception {

		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		// Provide Annotation

		// Before providing annotation , take inbox count of agent whose call is
		// provided with sup notes
		int inboxAgent2 = inboxTabPage.getInboxCount(caiDriver1);
		System.out.println(inboxAgent2);

		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		dashboard.clickConversationAI(caiDriver2);
		callsTabPage.clearAllFilters(caiDriver2);

		System.out.println("Test Case-Provide Supervisor notes in shared calls,Inbox notification generated--Started ");
		// Now provide Supervisor notes from another user
		// Before providing sup notes , take inbox count of agent whose call is
		// provided with sup notes
		inboxAgent2 = inboxTabPage.getInboxCount(caiDriver1);
		System.out.println(inboxAgent2);

		System.out.println(inboxAgent2);
		callsTabPage.setAgentFilter(caiDriver2, CONFIG.getProperty("qa_cai_user_2_name"));

		callsTabPage.viewCAI(caiDriver2);

		// Get Call Data
		// Get call data to later verify this call when filter with Library
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		CallDetails.put(CallsTabPage.CallData.Agent, callsTabPage.getCallAgent(caiDriver2));
		CallDetails.put(CallsTabPage.CallData.CallDate, callsTabPage.getCallTime(caiDriver2));
		CallDetails.put(CallsTabPage.CallData.CallDuration, callsTabPage.getCallDuration(caiDriver2));

		callsTabPage.clickAnnotateBtn(caiDriver2);
		callsTabPage.idleWait(10);
		callsTabPage.clickStopBtn(caiDriver2);
		callsTabPage.idleWait(2);
		assertTrue(callsTabPage.annotationExistForAgent(caiDriver2, CONFIG.getProperty("qa_cai_user_1_name")));

		System.out.println(
				"Test Case-Annotation Creation-Clicking the cancel button will close the annotation and destroy the unsaved marker--Started");
		callsTabPage.clickCancelAnnotation(caiDriver2);
		assertFalse(callsTabPage.annotationExistForAgent(caiDriver2, CONFIG.getProperty("qa_cai_user_1_name")));

		System.out.println(
  
				"Test Case-Annotation Creation-Clicking the Cancel button will close the annotation and destroy the unsaved marker--Passed");

		System.out.println(
				"Test Case-Annotation creation cancelled If the user click on the playback timeline prior to the initial marker--Started");

		callsTabPage.clickAnnotateBtn(caiDriver2);
		callsTabPage.idleWait(5);
		callsTabPage.clickToCancelAnnotation(caiDriver2);
		// callsTabPage.verifyAnnotationCancel(caiDriver2);
		System.out.println(
				"Test Case-Annotation creation cancelled If the user click on the playback timeline prior to the initial marker--Passed");

		System.out.println("Test Case-Provide Annotation on shared calls,Inbox notification generated--Started ");
		callsTabPage.refreshCurrentDocument(caiDriver2);
		callsTabPage.idleWait(5);
		callsTabPage.clickPlayIcon(caiDriver2);
		callsTabPage.clickAnnotateBtn(caiDriver2);
		callsTabPage.idleWait(10);
		callsTabPage.clickStopBtn(caiDriver2);

		// Enter annotation as random string
		String notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(3));
		callsTabPage.enterAnnotation(caiDriver2, notes);
		callsTabPage.idleWait(2);
		callsTabPage.verifySavedAnnotation(caiDriver2, CONFIG.getProperty("qa_cai_user_1_name"), notes);
		// Verify that player start and verify data displayed in editable mode
		// Inbox count of agent to whom we share call should be increased by 1
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), inboxAgent2 + 1);
		inboxTabPage.clickInboxTab(caiDriver1);
		// Verify that given Sup notes displayed in Inbox section
		assertTrue(inboxTabPage.annotationEntryInbox(caiDriver1, notes));

		System.out.println("As annotation notification yet to read its should be displayed as unread");
		assertTrue(inboxTabPage.inboxMessageUnread(caiDriver1));

		inboxTabPage.viewInboxMessage(caiDriver1);
		assertEquals(CallDetails.get(CallData.Agent), inboxTabPage.getAgentInboxResult(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDate), inboxTabPage.getCallTimeInboxResult(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDuration), inboxTabPage.getCallDurationInboxResult(caiDriver1));

		System.out.println(
				"As Annotation also given its icon also set to Active. So all 3 icons for call notes,annotation,supervisor notes are active now for this call");
		// assertTrue(inboxTabPage.getSupNotesIconStatus(caiDriver1));
		assertTrue(inboxTabPage.getAnnotationIconStatus(caiDriver1));
		// assertTrue(inboxTabPage.getCallNotesIconStatus(caiDriver1));
		inboxTabPage.viewCAIInbox(caiDriver1);
		assertEquals(CallDetails.get(CallData.Agent), callsTabPage.getCallAgent(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDate), callsTabPage.getCallTime(caiDriver1));
		assertEquals(CallDetails.get(CallData.CallDuration), callsTabPage.getCallDuration(caiDriver1));
		// Inbox count of agent should be decreased by 1 when we open call from
		// inbox tab
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), inboxAgent2);
		// inboxTabPage.clickInboxTab(caiDriver1);
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test Case-Provide Annotation on shared calls,Inbox notification generated--Passed ");
		System.out.println(
				"Test case-Agent can't provide another annotation for same call length where annotation already available from agent--Started");

		// refresh driver where we provide annotation.
		callsTabPage.refreshCurrentDocument(caiDriver2);
		callsTabPage.idleWait(5);
		callsTabPage.clickPlayIcon(caiDriver2);
		callsTabPage.idleWait(2);
		// as annotation already provided by same user for this length,
		// annotation button remains disabled
		assertTrue(callsTabPage.isAnnotationBtnDisabled(caiDriver2));
		callsTabPage.idleWait(12);
		assertFalse(callsTabPage.isAnnotationBtnDisabled(caiDriver2));
		assertTrue(callsTabPage.isAnnotationBtnEnabled(caiDriver2));
		System.out.println(
				"Test case-Agent can't provide another annotation for same call length where annotation already available from agent--Passed");

		System.out.println(
				"Test Case-Another agent can provide annotation on call length where annotation exists from another agent-Started");

		caiDriver1.get(caiDriver2.getCurrentUrl());
		callsTabPage.idleWait(2);
		callsTabPage.clickPlayIcon(caiDriver1);
		assertTrue(callsTabPage.isAnnotationBtnEnabled(caiDriver1));
		callsTabPage.clickAnnotateBtn(caiDriver1);
		notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(3));
		callsTabPage.idleWait(10);
		callsTabPage.clickStopBtn(caiDriver1);
		callsTabPage.enterAnnotation(caiDriver1, notes);
		callsTabPage.idleWait(2);
		assertTrue(callsTabPage.verifySavedAnnotation(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"), notes));
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clickCallTab(caiDriver2);
		callsTabPage.clearAllFilters(caiDriver2);
		System.out.println(
				"Test Case-Another agent can provide annotation on call length where annotation exists from another agent-Passed");
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);

	}

	@Test(groups = { "Regression" }, priority = 32)
	public void call_notes() throws Exception {
		System.out.println("Test-Case-User can only update Call Notes for own Conversation Recordings--Started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.viewCAI(caiDriver1);
		assertTrue(callsTabPage.callNotesEnabled(caiDriver1));
		// Enter Notes
		String notes = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
		callsTabPage.enterCallNotes(caiDriver1, notes);
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.viewCAI(caiDriver1);
		// Check entered notes persists after Save
		assertEquals(callsTabPage.getCallNotes(caiDriver1).trim(), notes.trim());
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		callsTabPage.viewCAI(caiDriver1);
		assertFalse(callsTabPage.callNotesEnabled(caiDriver1));
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test-Case-User can only update Call Notes for own Conversation Recordings--Passed ");
	}

	@Test(groups = { "Regression" }, priority = 33)
	public void conversation_ai_library_tab_search_order_ui() throws Exception {
		System.out.println(
				"Test Case--Arrange data as per Newest to oldest and oldest to Newest dropdown on Library Tab--Started");
		// Open Library Tab
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		libraryTabPage.clickLibraryTab(caiDriver1);
		libraryTabPage.selectLibraryFromLeftMenu(caiDriver1, "QA Test Library");
		// Default search order is newest to oldest
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString(),"callTime");
		System.out.println("Default Search order-is newest to oldest-Passed");
		// Reset Order to Ascending when set from Search Filter
		System.out.println("Change order to-Oldest to newest");
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Ascending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Ascending.toString(),"callTime");
		System.out.println("Change order to-Oldest to newest-Passed");
		// Reset Order to Descending when set from Search Filter
		System.out.println("Change order back to-Newest to Oldest");
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Descending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString(),"callTime");
		System.out.println("Change order back to-Newest to Oldest-Passed");
		callsTabPage.clickCallTab(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println(
				"Test Case--Arrange data as per Newest to oldest and oldest to Newest dropdown on Library Tab--Passed");
	}

	@Test(groups = { "Regression" }, priority = 34)
	public void inbox_order() throws Exception {
		System.out.println("Test Case-Arrange inbox notification from 'Oldest to Newest' --Started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		inboxTabPage.clickInboxTab(caiDriver1);
		// Default search order is newest to oldest
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString());

		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Ascending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Ascending.toString());

		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.Descending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.Descending.toString());
		callsTabPage.clickCallTab(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Arrange inbox notification from 'Oldest to Newest'--Passed");
	}

	@Test(groups = { "Regression" }, priority = 35)
	public void inbox_notification_filter() throws Exception {
		System.out.println("Test Case-Filter Inbox notification by notificaiton type' --Started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		inboxTabPage.clickInboxTab(caiDriver1);

		// Take all page count
		int initialCount = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));

		// Set Notification filter to Supervisor notes
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Supnotes.toString());
		callsTabPage.idleWait(2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxNotificationFilter(caiDriver1, page,
				InboxTabPage.InboxNotificationTypes.Supnotes.toString());

		// Reset Filter to default
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.All.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(initialCount, page);

		// Set Notification filter to Annotation
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Annotations.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxNotificationFilter(caiDriver1, page,
				InboxTabPage.InboxNotificationTypes.Annotations.toString());

		// Reset Filter to default
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.All.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(initialCount, page);

		// Set Notification filter to Flagged for Review
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Flagged.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxNotificationFilter(caiDriver1, page,
				InboxTabPage.InboxNotificationTypes.Flagged.toString());

		// Reset Filter to default
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.All.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(initialCount, page);

		System.out.println("Test Case-Filter Inbox notification by notificaiton type'--Passed");

		System.out.println("Test Case-Filter notification by User name and action type--Started");
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Flagged.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.selectAgentInbox(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxNotificationAgentFilter(caiDriver1, page,
		InboxTabPage.InboxNotificationTypes.Flagged.toString(), CONFIG.getProperty("qa_cai_user_2_name"));
		// Reset Filter to default
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.All.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.clearAgentInbox(caiDriver1);
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(initialCount, page);
		System.out.println("Test Case-Filter notification by User name and action type--Passed");

		System.out.println("Test Case-Filter notification by User name --Started");

		inboxTabPage.selectAgentInbox(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxAgentFilter(caiDriver1, page, CONFIG.getProperty("qa_cai_user_2_name"));

		// Reset Filter to default
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.All.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.clearAgentInbox(caiDriver1);
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(initialCount, page);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Filter notification by User name --Passed");

	}

	@Test(groups = { "Regression" }, priority = 36)
	public void annotation_verification() {

		System.out.println("Test Case-Verify Saved Annotation on Player--Started");
		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		dashboard.clickConversationAI(caiDriver1);
		dashboard.clickConversationAI(caiDriver2);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver2);
		callsTabPage.setAgentFilter(caiDriver2, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.viewCAI(caiDriver2);

		String notes = callsTabPage.getAnnotationMessage(caiDriver2);
		// Now Click Annotation from Annotation section and Verify
		callsTabPage.openToEditAnnotation(caiDriver2, notes);
		callsTabPage.annotationClickandVerifyDetails(caiDriver2, notes);
		System.out.println("Test Case-Verify Saved Annotation on Player--Passed");

		System.out.println("Test Case-User update and Save created Annotation, verify after Save--Started");

		callsTabPage.openToEditAnnotation(caiDriver2, notes);
		notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(5));
		callsTabPage.enterAnnotation(caiDriver2, notes);
		callsTabPage.openToEditAnnotation(caiDriver2, notes);
		callsTabPage.annotationClickandVerifyDetails(caiDriver2, notes);

		// Verify details from segment
		callsTabPage.clickSegment(caiDriver2, CONFIG.getProperty("qa_cai_user_1_name"));
		callsTabPage.annotationClickandVerifyDetails(caiDriver2, notes);

		// Open record for which annotation updated
		System.out.println("Test Case-User update and Save created Annotation, verify after Save--Passed");
		inboxTabPage.clickInboxTab(caiDriver1);
		inboxTabPage.openCallFromInbox(caiDriver1, notes);
		// callsTabPage.annotationClickandVerifyDetails(caiDriver1, notes);
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
	}

	@Test(groups = { "Regression" }, priority = 37)
	public void add_call_for_review() {

		System.out.println(
				"Test Case-Conversation AI player- Action- Review call- verify notification by reviewer--Started");

		driverUsed.put("caiDriver1", true);
		driverUsed.put("caiDriver2", true);
		dashboard.clickConversationAI(caiDriver2);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver2);

		// Before sending for review , take inbox count of agent to whom we are
		// going to share call for review
		int inboxAgent2 = inboxTabPage.getInboxCount(caiDriver2);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.viewCAI(caiDriver1);
		// Get Call Data
		// Get call data to later verify this call when filter with Library
		HashMap<CallsTabPage.CallData, String> CallDetails = new HashMap<CallData, String>();
		CallDetails.put(CallsTabPage.CallData.Agent, callsTabPage.getCallAgent(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDate, callsTabPage.getCallTime(caiDriver1));
		CallDetails.put(CallsTabPage.CallData.CallDuration, callsTabPage.getCallDuration(caiDriver1));

		// Share call for Review to another agent
		callsTabPage.clickReviewIcon(caiDriver1);
		callsTabPage.enterReviewUser(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		callsTabPage.idleWait(2);

		// Inbox count of agent to whom we share call should be increased by 1
		assertEquals(inboxTabPage.getInboxCount(caiDriver2), inboxAgent2 + 1);
		System.out.println("Call shared for Review");
		// Verify shared call exists on Inbox Tab Agent to whom we share call
		inboxTabPage.clickInboxTab(caiDriver2);

		// Check for Review Icon should be Wait for Review icon
		Pair<Boolean, Boolean> iconStatus = inboxTabPage.getWaitReviewIconStatus(caiDriver2);
		assertTrue(iconStatus.first(), "Wait for Review icon not displayed");
		assertFalse(iconStatus.second(), "Already Reviewed icon displayed");
		System.out.println("Wait for Review Red Icon displayed");
		// Check for availability wait for Review icon
		inboxTabPage.viewInboxMessage(caiDriver2);
		assertEquals(CallDetails.get(CallData.Agent), inboxTabPage.getAgentInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDate), inboxTabPage.getCallTimeInboxResult(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDuration), inboxTabPage.getCallDurationInboxResult(caiDriver2));

		inboxTabPage.viewCAIInbox(caiDriver2);
		assertEquals(CallDetails.get(CallData.Agent), callsTabPage.getCallAgent(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDate), callsTabPage.getCallTime(caiDriver2));
		assertEquals(CallDetails.get(CallData.CallDuration), callsTabPage.getCallDuration(caiDriver2));

		// Inbox count of agent to whom we share call should be decreased by 1
		// when we open call from inbox tab
		assertEquals(inboxTabPage.getInboxCount(caiDriver2), inboxAgent2);

		// Check for avalibility of Mark As Reviewed button
		assertTrue(callsTabPage.markAsReviewAvailable(caiDriver2));

		// Click For Review
		callsTabPage.clickForReview(caiDriver2);

		// Check for reviewed icon
		inboxTabPage.clickInboxTab(caiDriver2);

		// Check for Review Icon should be Wait for Review icon
		iconStatus = inboxTabPage.getWaitReviewIconStatus(caiDriver2);
		assertFalse(iconStatus.first(), "Wait for review icon displayed");
		assertTrue(iconStatus.second(), "Reviewed Icon not displayed");
		System.out.println(
				"Test Case-Conversation AI player- Action- Review call- verify notification by reviewer--Passed");

		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiDriver2", false);
	}

	@Test(groups = { "Regression" }, priority = 38)
	public void delete_inbox_notification() {

		System.out.println("Test Case--Delete Notification from Inbox section--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		inboxTabPage.clickInboxTab(caiDriver1);

		// Take count of unread messages before delete a notification
		int initialcount = inboxTabPage.getInboxCount(caiDriver1);

		// View Unread notification, upon view inbox count should redeuced by 1
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.viewUnreadNotification(caiDriver1, page);
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), initialcount - 1);

		inboxTabPage.clickInboxTab(caiDriver1);
		initialcount = inboxTabPage.getInboxCount(caiDriver1);

		// View Read notification, upon view inbox count should redeuced by 1
		inboxTabPage.viewReadNotification(caiDriver1, page);
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), initialcount);

		inboxTabPage.clickInboxTab(caiDriver1);

		// Upon delete read notifications , inbox count not updated
		inboxTabPage.deleteReadNotification(caiDriver1, page);
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), initialcount);

		inboxTabPage.clickInboxTab(caiDriver1);

		// Delete one unread notification,upon view inbox count should redeuced
		// by 1
		inboxTabPage.deleteUnreadNotification(caiDriver1, page);
		assertEquals(inboxTabPage.getInboxCount(caiDriver1), initialcount - 1);
		initialcount = inboxTabPage.getInboxCount(caiDriver1);

		System.out.println("Test Case--Delete Notification from Inbox section--Passed");

		driverUsed.put("caiDriver1", false);

	}

	@Test(groups = { "Regression" }, priority = 39)
	public void agent_talktime_search() {

		System.out.println("Test case --Filter by Agent Talk Time (%) selection-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentTalkTime(caiDriver1);
		Pair<String, String> durationRange = callsTabPage.getCallDurationRange(caiDriver1);
		float min = Float.parseFloat(durationRange.first().trim());
		float max = Float.parseFloat(durationRange.second().trim());
		// String maxDuration=callsTabPage.getMaxDuration(caiDriver1);
		System.out.println(min);
		System.out.println(max);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifyTalkTimePercentage(caiDriver1, min, max, page);
		System.out.println("Test case --Filter by Agent Talk Time (%) selection-- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 40)
	public void cai_pagination() {
		System.out.println("Test case --Pagination on Conversation AI search page-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.paginationCAI(caiDriver1);
		System.out.println("Test case --Pagination on Conversation AI search page-- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 41)
	public void cai_remove_search_filter() {
		System.out.println("Test case --Remove selected search Data from Drop-down will refresh CALLS tab-- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);

		int beforeFilter = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.setEngagementFilter(caiDriver1, CallData.callDirectionOutbound.name());

		int afterFilter = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertTrue(afterFilter < beforeFilter);
		// Set few filters and later remove them, after removing filter count
		// should be same
		callsTabPage.removeFilter(caiDriver1);
		afterFilter = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		assertEquals(afterFilter, beforeFilter);
		System.out.println("Test case --Remove selected search Data from Drop-down will refresh CALLS tab-- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 42)
	public void add_remove_tag() {
		System.out.println(
				"Test case --Verify added and Removed tags in dropdown after search from dropdown -- started ");
		System.out.println("Test case --Tagging functionality works correctly, type ahead and drop down work--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);

		// Open CAI and provide the tag from existing list
		callsTabPage.viewCAI(caiDriver1);
		String token = callsTabPage.enterCallDetailsTag(caiDriver1, "test");

		// Click thumbsUp
		callsTabPage.clickThumbsUpTag(caiDriver1);
		// Check that upon search existing tag , tag list correctly populated

		// Verify Now given tag available as tag filter option
		callsTabPage.clickCallTab(caiDriver1);
		assertTrue(callsTabPage.checkTagInList(caiDriver1, token));

		// Now give non existing tag. It should not displayed as search token in
		// Tag filter
		String notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(5));
		assertFalse(callsTabPage.checkTagInList(caiDriver1, notes));

		// Open CAI and provide this new tag
		callsTabPage.viewCAI(caiDriver1);
		callsTabPage.enterCallDetailsTag(caiDriver1, notes);

		// Click thumbsDown
		callsTabPage.clickThumbsDownTag(caiDriver1);

		// Verify Now given tag available as tag filter option
		callsTabPage.clickCallTab(caiDriver1);
		assertTrue(callsTabPage.checkTagInList(caiDriver1, notes));

		// Now remove the tag
		callsTabPage.viewCAI(caiDriver1);
		callsTabPage.clearTag(caiDriver1);

		// check that cleared tag removed from tag filter option values
		callsTabPage.clickCallTab(caiDriver1);
		assertFalse(callsTabPage.checkTagInList(caiDriver1, notes));

		// Open CAI and provide this new tag
		callsTabPage.viewCAI(caiDriver1);
		callsTabPage.enterCallDetailsTag(caiDriver1, notes);

		// Click removeTag
		callsTabPage.clearTag(caiDriver1);

		// Verify Now given tag available as tag filter option
		callsTabPage.clickCallTab(caiDriver1);
		assertFalse(callsTabPage.checkTagInList(caiDriver1, notes));

		System.out
				.println("Test case --Verify added and Removed tags in dropdown after search from dropdown -- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 43)
	public void call_duration_consistent() {
		System.out.println("Test case --Verify Call Duration on cai player is consistent -- started ");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Get call duration listed on first call
		String callDuration = callsTabPage.getCallDurationCallTabResult(caiDriver1);
		callsTabPage.viewCAI(caiDriver1);
		assertEquals(callsTabPage.getCallDuration(caiDriver1), callDuration);											  
																		 
		System.out.println("Test case --Verify Call Duration on cai player is consistent -- Passed ");
		driverUsed.put("caiDriver1", false);
	}

	@Test(groups = { "Regression" }, priority = 44)
	public void keyword_marker_play() {

		System.out.println("Test case --Play Voicebase recording after select or Search keyword--Started");

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentTalkTime(caiDriver1);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.clickKeyword(caiDriver1, page);
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test case --Play Voicebase recording after select or Search keyword--Passed");
		driverUsed.put("caiDriver1", false);
	}
 
												
									 
 
																				

	@Test(groups = { "Regression" }, priority = 45)
	public void lead_status_on_player() {

		System.out.println("Test case --Lead status display on player page--Started");

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Set Lead Status Filter and check results
		String searchToken = callsTabPage.setFilter(caiDriver1, CallData.LeadStatus.name());

		callsTabPage.viewCAI(caiDriver1);
		assertTrue(callsTabPage.getLeadStatus(caiDriver1).trim().equals(searchToken.trim()));
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println("Test case --Lead status display on player page--Passed");
		System.out.println("Test case--Opportunity stage display on player--Started");
		// Set Lead Status Filter and check results
		searchToken = callsTabPage.setFilter(caiDriver1, CallData.OppStage.name());
		callsTabPage.viewCAI(caiDriver1);
		assertTrue(callsTabPage.getOppStage(caiDriver1).trim().equals(searchToken.trim()));
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case--Opportunity stage display on player--Passed");
  }													   

	//@Test(groups = { "Regression" }, priority =414)
	public void activity_feed_search() throws Exception {

		System.out.println("Test case --Search Activity Feeds by User and action type--Started");

		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Open activity feed tab
		callsTabPage.clickActivityTab(caiDriver1);

		// Check search by User Name and Verify
		inboxTabPage.selectAgentInbox(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxAgentFilter(caiDriver1, page, CONFIG.getProperty("qa_cai_user_1_name"));
		System.out.println("Search by agent name working fine on Activity Tab");

		// Check by user name and notification type
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Supnotes.toString());
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		inboxTabPage.verifyInboxNotificationAgentFilter(caiDriver1, page,
				InboxTabPage.InboxNotificationTypes.Supnotes.toString(), CONFIG.getProperty("qa_cai_user_1_name"));
		System.out.println("Search by agent name and notification type working fine on Activity Tab");
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case--Search Activity Feeds by User and action type--Passed");
	}

	@Test(groups = { "Regression" }, priority = 46)
	public void access_SetUpTab() {
		
		
		System.out.println("Test case -Access Setup tab from within Conversation AI--Started");
		driverUsed.put("caiDriver1", true);
		
		//Enable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		setUpTabPage.clickSetUpTab(caiDriver1);

		// Try to add voc of more then 64 chars
		setUpTabPage.addKeywordMoreThenLimit(caiDriver1);
		System.out.println("Not able to add vocabulary of more then 64 chars-Passed");
		String selectedKeyword = setUpTabPage.getSelectedSetting(caiDriver1);
		assertTrue(selectedKeyword.equals("Custom Vocabulary"));
		dashboard.redirectHomePage(caiDriver1);
		setUpTabPage.clickSetUpTabViaProfile(caiDriver1);
		selectedKeyword = setUpTabPage.getSelectedSetting(caiDriver1);
		assertTrue(selectedKeyword.equals("Custom Vocabulary"));
		System.out.println("Test case -Access Setup tab from within Conversation AI--Passed");

		System.out.println("Test case -Add Custom Vocabulary,the UI to allow for 2 columns--Started");
		String notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(3));
		setUpTabPage.addVocabluary(caiDriver1, notes);
		assertTrue(setUpTabPage.vocabluaryExistInList(caiDriver1, notes));
		System.out.println("Test case -Add Custom Vocabulary,the UI to allow for 2 columns--Passed");
		System.out.println("Test case -Add Custom Vocabulary,Via Enter button--Started");
		setUpTabPage.addVocabluaryVaiEnter(caiDriver1, notes + "enter");
		assertTrue(setUpTabPage.vocabluaryExistInList(caiDriver1, notes + "enter"));
		System.out.println("Test case -Add Custom Vocabulary,Via Enter button--Passed");
		System.out.println("Test case -Delete Custom Vocabulary --Started");
		setUpTabPage.deleteVocabluaryItem(caiDriver1, notes);
		assertFalse(setUpTabPage.vocabluaryExistInList(caiDriver1, notes));
		System.out.println("Test case -Delete Custom Vocabulary --Passed");

 
		driverUsed.put("caiDriver1", false);

	}

	@Test(groups = { "Regression" }, priority = 47)
	public void keyword_SetUpTab() {

		System.out.println("Test case -Add keyword Group-Started");
		driverUsed.put("caiDriver1", true);
		String keyPhrase1 = "city";
		String keyPhrase2 = "would";
		String newKeyPhrase = HelperFunctions.GetRandomString(5);

		//Enable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		setUpTabPage.clickSetUpTab(caiDriver1);
		setUpTabPage.clickKeywordGroupLeftNav(caiDriver1);
		
		//verifying keyword group name cannot be more than 60 characters
		String group60Char = "cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzu";
		String groupMoreThan60Char = "cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzuqwqeef";
		String group54Char	= "zanchigasjamqrxavxeamzfwoffsdhcksrnqszkbbsnusqrtftpcthe";
		String group55Char = "zanchigasjamqrxavxeamzfwoffsdhcksrnqszkbbsnusqrtftpcthea";
		
		//entering more than 60 and verifying it does not accept
		setUpTabPage.verifyKeywordGroupNameMoreThan60NotAccept(caiDriver1, groupMoreThan60Char, group60Char);
		
		//msg when characters reach 55 or more
		setUpTabPage.verifyAlertMsgGroupName55OrMore(caiDriver1, group54Char, group55Char);
		
		String keywordGroup = "keywordGroup".concat(" ").concat(HelperFunctions.GetRandomString(3));
		setUpTabPage.addKeywordGroup(caiDriver1, keywordGroup,keyPhrase1,keyPhrase2,newKeyPhrase);
		assertTrue(setUpTabPage.keywordGroupExistInList(caiDriver1, keywordGroup));
		assertTrue(setUpTabPage.keywordPhraseCount(caiDriver1, keywordGroup).equals("3"));
		System.out.println("Test case -Add keyword Group-Passed");
		System.out.println(
				"Test case -Only matching keyword phrases listed as CAI search keyword phrase option-C9113-Started");

		callsTabPage.clickCallTab(caiDriver1);
		assertTrue(callsTabPage.keywordsExistInList(caiDriver1, keywordGroup));

		// Verify that only phrase for which calls exist listed under that
		// phrase list for given keyword group
		assertTrue(callsTabPage.keyPhraseExistInList(caiDriver1, keywordGroup, keyPhrase1));
		assertTrue(callsTabPage.keyPhraseExistInList(caiDriver1, keywordGroup, keyPhrase2));
		assertFalse(callsTabPage.keyPhraseExistInList(caiDriver1, keywordGroup, newKeyPhrase));

		// Select KeyPhrase from given keyword group
		callsTabPage.setKeyPhraseFilter(caiDriver1, keywordGroup, keyPhrase1);
		callsTabPage.setKeyPhraseFilter(caiDriver1, keywordGroup, keyPhrase2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));

		callsTabPage.verifyKeyPhraseFilter1(caiDriver1, page, callsTabPage.getFiltersList(caiDriver1));
		callsTabPage.clearAllFilters(caiDriver1);
		System.out.println(
				"Test case -Only matching keyword phrases listed as CAI search keyword phrase option-C9113-Passed");

		System.out.println("Test case -Delete Keyword Separately--Started");
		setUpTabPage.clickSetUpTab(caiDriver1);
		setUpTabPage.clickKeywordGroupLeftNav(caiDriver1);
		setUpTabPage.clickKeywordGroup(caiDriver1, keywordGroup);

		// Delete KeyPhrase and ensure its removed from search
		setUpTabPage.deleteKeyPhraseItem(caiDriver1, keyPhrase1);
		callsTabPage.clickCallTab(caiDriver1);
		assertFalse(callsTabPage.keyPhraseExistInList(caiDriver1, keywordGroup, keyPhrase1));

		System.out.println("Test case -Delete Keyword Separately--Passed");

		System.out.println("Test case -Delete Keyword Group who have Keywords--Started");

		setUpTabPage.clickSetUpTab(caiDriver1);
		setUpTabPage.clickKeywordGroupLeftNav(caiDriver1);

		// delete keyword group and ensure its removed from search
		setUpTabPage.deleteKeywordGroup(caiDriver1, keywordGroup);
		callsTabPage.clickCallTab(caiDriver1);
		assertFalse(callsTabPage.keywordsExistInList(caiDriver1, keywordGroup));

		System.out.println("Test case -Delete Keyword Group who have Keywords--Passed");
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);
	}								

//	@Test(groups = { "Regression" }, priority = 48)
	public void oppStatus_SetUpTab() {
		System.out.println("Test case Opportunity Status Change - Add after select values from dropdown-Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);											  
		setUpTabPage.clickSetUpTab(caiDriver1);
		setUpTabPage.clickOppStatusLeftNav(caiDriver1);

		// Add lead Status change
		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.statusChangeOptions.Positive.toString());
		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());														 

		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		// setUpTabPage.addDupeStatusChange(caiDriver1,SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString()
		// , SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
		// SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test case -Opportunity Status Change - Add after select values from dropdown-Passed");

		System.out.println("Test case -Opportunity Status Change - update duplicate Opportunity status data-Started");
		setUpTabPage.updateStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.statusChangeOptions.Positive.toString(),
				SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test case -Opportunity Status Change - update duplicate Opportunity status data-Passed");

		System.out.println("Test case -Opportunity Status Change - Update added Opportunity Status Change-Started");

		// Update added lead Status change
		setUpTabPage.updateStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.statusChangeOptions.Positive.toString(),
				SetUpTabPage.oppStatusOptions.PerceptionAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test case -Opportunity Status Change - Update added Opportunity Status Change-Passed");
		// Delete added lead Status change

		System.out.println("Test case-Opportunity Statuus Change Old Status Sorting-Started");

		String sortingStatus = setUpTabPage.clickToSortOldStatus(caiDriver1);
		List<String> oldStatus = setUpTabPage.getOldStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, oldStatus, sortingStatus));

		sortingStatus = setUpTabPage.clickToSortOldStatus(caiDriver1);
		oldStatus = setUpTabPage.getOldStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, oldStatus, sortingStatus));

		System.out.println("Test case-Opportunity Statuus Change Old Status Sorting-Passed");

		System.out.println("Test case-Opportunity Statuus Change New Status Sorting-Started");

		sortingStatus = setUpTabPage.clickToSortNewStatus(caiDriver1);
		List<String> newStatus = setUpTabPage.getNewStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, newStatus, sortingStatus));

		sortingStatus = setUpTabPage.clickToSortNewStatus(caiDriver1);
		newStatus = setUpTabPage.getNewStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, newStatus, sortingStatus));

		System.out.println("Test case-Opportunity Statuus Change New Status Sorting-Passed");
		System.out.println("Test case-Opportunity Status Change - Delete added Opportunity Status Change-Started");

		setUpTabPage.deleteStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
				SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString());
		callsTabPage.idleWait(1);
		setUpTabPage.deleteStatusChange(caiDriver1, SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString(),
				SetUpTabPage.oppStatusOptions.ValueProposition.toString());
		System.out.println("Test case-Opportunity Status Change - Delete added Opportunity Status Change-Passed");
		driverUsed.put("caiDriver1", false);

	}																				

//	@Test(groups = { "Regression" }, priority = 49)
	public void leadStatus_SetUpTab() {
		System.out.println("Test case -Lead Status Change- Add after select values from dropdown-Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		setUpTabPage.clickSetUpTab(caiDriver1);
		setUpTabPage.clickleadStatusLeftNav(caiDriver1);
		// Add lead Status change
		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.leadStatusOptions.Contacted.toString(),
				SetUpTabPage.statusChangeOptions.Positive.toString());

		System.out.println("Test Case-Try to add duplicate lead status change--Started");
		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.leadStatusOptions.Contacted.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());

		System.out.println("Test Case-Try to add duplicate lead status change--Passed");

		System.out.println("Test Case-Add lead status change--Started");
		setUpTabPage.addStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.UnQualified.toString(),
				SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		// setUpTabPage.addDupeStatusChange(caiDriver1,SetUpTabPage.oppStatusOptions.NeedsAnalysis.toString()
		// , SetUpTabPage.oppStatusOptions.ValueProposition.toString(),
		// SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test Case-Add lead status change--Passed");

		System.out.println("Test case -Lead Status Change - update duplicate Lead status data-Started");
		setUpTabPage.updateStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.UnQualified.toString(),
				SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString(),
				SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.leadStatusOptions.Contacted.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test case -Lead Status Change - update duplicate Lead status data-Passed");

		System.out.println("Test case -Opportunity Status Change - Update added lead Status Change-Started");

		// Update added lead Status change
		setUpTabPage.updateStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.UnQualified.toString(),
				SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString(),
				SetUpTabPage.leadStatusOptions.Contacted.toString(),
				SetUpTabPage.leadStatusOptions.UnQualified.toString(),
				SetUpTabPage.statusChangeOptions.Negative.toString());
		System.out.println("Test case -Lead Status Change - Update added lead Status Change-Passed");
		// Delete added lead Status change

		System.out.println("Test case-Lead Statuus Change Old Status Sorting-Started");

		String sortingStatus = setUpTabPage.clickToSortOldStatus(caiDriver1);
		List<String> oldStatus = setUpTabPage.getOldStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, oldStatus, sortingStatus));

		sortingStatus = setUpTabPage.clickToSortOldStatus(caiDriver1);
		oldStatus = setUpTabPage.getOldStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, oldStatus, sortingStatus));

		System.out.println("Test case-Lead Statuus Change Old Status Sorting-Passed");

		System.out.println("Test case-Lead Status Change New Status Sorting-Started");

		sortingStatus = setUpTabPage.clickToSortNewStatus(caiDriver1);
		List<String> newStatus = setUpTabPage.getNewStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, newStatus, sortingStatus));

		sortingStatus = setUpTabPage.clickToSortNewStatus(caiDriver1);
		newStatus = setUpTabPage.getNewStatusList(caiDriver1);
		assertTrue(setUpTabPage.getSortOrder(caiDriver1, newStatus, sortingStatus));

		System.out.println("Test case-Lead Status Change New Status Sorting-Passed");

		System.out.println("Test case-Lead Status Change - Delete added Opportunity Status Change-Started");

		setUpTabPage.deleteStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.Open.toString(),
				SetUpTabPage.leadStatusOptions.Contacted.toString());
		callsTabPage.idleWait(1);
		setUpTabPage.deleteStatusChange(caiDriver1, SetUpTabPage.leadStatusOptions.Contacted.toString(),
				SetUpTabPage.leadStatusOptions.UnQualified.toString());
		System.out.println("Test case-Lead Status Change - Delete added Opportunity Status Change-Passed");
		driverUsed.put("caiDriver1", false);

	}

	@Test(groups = { "Regression" }, priority = 50)
	public void cai_player_back_15sec() {

		System.out.println("Test Case-15 second skip back--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		callsTabPage.viewCAI(caiDriver1);
		callsTabPage.clickPlayIcon(caiDriver1);
		callsTabPage.idleWait(17);
		float progress = callsTabPage.getProgressBarStatus(caiDriver1);
		System.out.println(progress);
		// int timerProgress = callsTabPage.getPlayerTimerProgress(caiDriver1);
		callsTabPage.clickBackIcon(caiDriver1);

		callsTabPage.idleWait(1);
		float backProgress = callsTabPage.getProgressBarStatus(caiDriver1);
		System.out.println(backProgress);
		// int bakcTimerProgress =
		// callsTabPage.getPlayerTimerProgress(caiDriver1);
		// callsTabPage.getPlayerTimerProgress(caiDriver1);
		assertTrue(progress - backProgress > 8);
		// assertTrue(timerProgress - bakcTimerProgress>12);
		driverUsed.put("caiDriver1", false);

		System.out.println("Test Case-15 second skip back--Passed");
	}

	@Test(groups = { "Regression" }, priority = 51)
	public void team_filter_cai() {

		System.out.println(
				"Test Case-Team filter should NOT visible when logged in user is not a member/supervisor of team	--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.deleteAllTeams(caiDriver1);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// callsTabPage.clickCallTab(caiDriver1);
		System.out.println(callsTabPage.isTeamFilterDisplayed(caiDriver1));
		assertFalse(callsTabPage.isTeamFilterDisplayed(caiDriver1));
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.clickAddTeamIcon(caiDriver1);
		usersPage.addUserToTeams(caiDriver1, CONFIG.getProperty("qa_cai_team_1"));
		dashboard.clickConversationAI(caiDriver1);
		System.out.println(callsTabPage.isTeamFilterDisplayed(caiDriver1));
		assertTrue(callsTabPage.isTeamFilterDisplayed(caiDriver1));
		System.out.println(
				"Test Case-Team filter should NOT visible when logged in user is not a member/supervisor of team	--Passed");

		System.out.println(
				"Test Case-My Teams filter option will only come when Agent is member of 2 or more teams	--Started");

		List<String> teamValues = callsTabPage.getTeamFilterValues(caiDriver1);
		assertFalse(callsTabPage.isTextPresentInStringList(caiDriver1, teamValues, "My Teams"));
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.clickAddTeamIcon(caiDriver1);
		usersPage.addUserToTeams(caiDriver1, CONFIG.getProperty("qa_cai_team_2"));
		dashboard.clickConversationAI(caiDriver1);
		teamValues = callsTabPage.getTeamFilterValues(caiDriver1);
		System.out.println(teamValues);
		assertTrue(callsTabPage.isTextPresentInStringList(caiDriver1, teamValues, "My Teams"));

		System.out.println(
				"Test Case-My Teams filter option will only come when Agent is member of 2 or more teams	--Passed");

		System.out.println("Test Case-Filter Team recordings after select 'My Team' from the Team dropdown	--Started");

		callsTabPage.setTeamFilter(caiDriver1, "My Teams");
		assertFalse(callsTabPage.isCaiResultNOTAvailable(caiDriver1));

		// Set Agent Filter to Vishal C
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		assertFalse(callsTabPage.isCaiResultNOTAvailable(caiDriver1));
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setTeamFilter(caiDriver1, "My Teams");
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		assertFalse(callsTabPage.isCaiResultNOTAvailable(caiDriver1));
		callsTabPage.clearAllFilters(caiDriver1);

		System.out.println("Test Case-Filter Team recordings after select 'My Team' from the Team dropdown	--Passed");

		System.out.println("Test Case-Filter recordings after select any Team from dropdown--Started");
		callsTabPage.setTeamFilter(caiDriver1, CONFIG.getProperty("qa_cai_team_1"));
		assertFalse(callsTabPage.isCaiResultNOTAvailable(caiDriver1));
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_2_name"));
		assertFalse(callsTabPage.isCaiResultNOTAvailable(caiDriver1));
		callsTabPage.clearAllFilters(caiDriver1);
		callsTabPage.setTeamFilter(caiDriver1, CONFIG.getProperty("qa_cai_team_1"));
		callsTabPage.setAgentFilter(caiDriver1, CONFIG.getProperty("qa_cai_user_1_name"));
		assertTrue(callsTabPage.isCaiResultNOTAvailable(caiDriver1));
		System.out.println("Test Case-Filter recordings after select any Team from dropdown--Passed");
		callsTabPage.clearAllFilters(caiDriver1);
		driverUsed.put("caiDriver1", false);

	}

	@Test(groups = { "Regression" }, priority = 52)
	public void access_other_account_cai() {

		System.out.println(
				"Test Case-Access Conversation AI Recording by other account's admin- Should not Access	--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		caiDriver1.navigate().to("https://app-qa.ringdna.net/#smart-recordings/rc864811");
		dashboard.isPaceBarInvisible(caiDriver1);
		assertTrue(callsTabPage.isCaiNotAccessible(caiDriver1));
		System.out.println(
				"Test Case-Access Conversation AI Recording by other account's admin- Should not Access	--Passed");
		driverUsed.put("caiDriver1", false);

	}

	@Test(groups = { "Regression" }, priority = 53)
	public void open_cai_via_inbox() {

		System.out.println(
				"Test Case--Open call by clicking supervisor notes icon, check that page scrolled down fully--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		inboxTabPage.clickInboxTab(caiDriver1);
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Supnotes.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.viewInboxMessage(caiDriver1);
		inboxTabPage.clickSuperVisorNotesIcon(caiDriver1);
		callsTabPage.idleWait(2);
		JavascriptExecutor executor = (JavascriptExecutor) caiDriver1;
		Long value = (Long) executor.executeScript("return window.pageYOffset;");
		System.out.println(value);
		assertTrue(value > 500, String.format("Expected value: %s not matching with given value", value));
		System.out.println(
				"Test Case--Open call by clicking supervisor notes icon, check that page scrolled down fully--Passed");

		System.out.println(
				"Test Case--Open call by clicking annotation icon, check that page scrolled down fully--Started");
		inboxTabPage.clickInboxTab(caiDriver1);
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Annotations.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.viewInboxMessage(caiDriver1);
		inboxTabPage.clickAnnotationIcon(caiDriver1);
		callsTabPage.idleWait(2);
		executor = (JavascriptExecutor) caiDriver1;
		value = (Long) executor.executeScript("return window.pageYOffset;");
		System.out.println(value);
		assertTrue(value > 500, String.format("Expected value: %s not matching with given value", value));
		callsTabPage.enterCallNotes(caiDriver1, "Testing Scrolling");
		System.out.println(
				"Test Case--Open call by clicking annotation icon, check that page scrolled down fully--Passed");

		System.out.println(
				"Test Case--Open call by clicking call notes icon, check that page scrolled down fully--Started");
		inboxTabPage.clickInboxTab(caiDriver1);
		inboxTabPage.selectInboxNotification(caiDriver1, InboxTabPage.InboxNotificationTypes.Annotations.toString());
		callsTabPage.idleWait(2);
		inboxTabPage.viewInboxMessage(caiDriver1);
		inboxTabPage.clickCallNotesIcon(caiDriver1);
		callsTabPage.idleWait(2);
		executor = (JavascriptExecutor) caiDriver1;
		value = (Long) executor.executeScript("return window.pageYOffset;");
		System.out.println(value);
		assertTrue(value > 500, String.format("Expected value: %s not matching with given value", value));
		System.out.println(
				"Test Case--Open call by clicking call notes icon, check that page scrolled down fully--Passed");
	}
	
	@Test(groups = { "Regression" }, priority = 54)
	public void conversation_ai_call_duration_search_order_ui() throws Exception {
		System.out.println("Test Case-Arrange data on call duration in ascending order--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationAscending.toString());
		callsTabPage.idleWait(2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.DurationAscending.toString(),"callDuration");
		System.out.println("Test Case-Arrange data on call duration in ascending order--Passed");
				
		System.out.println("Test Case-Arrange data on call duration in descending order--Started");	
				// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationDescending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.DurationDescending.toString(),"callDuration");
		System.out.println("Call Duration Ascending search order -Passed");
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Arrange data on call duration in descending order--Passed");
	}
	
	@Test(groups = { "Regression" }, priority = 55)
	public void conversation_ai_agent_name_search_order_ui() throws Exception {
		System.out.println("Test Case-Arrange data on Agent name in ascending order--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.AgentAscending.toString());
		callsTabPage.idleWait(2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.AgentAscending.toString(),"agentName");
		System.out.println("Test Case-Arrange data on Agent name in ascending order--Passed");
				
		System.out.println("Test Case-Arrange data on Agent name in descending order--Started");	
				// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.AgentDescending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.AgentDescending.toString(),"agentName");
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Arrange data on Agent name in descending order--Passed");
	}
	
	@Test(groups = { "Regression" }, priority = 56)
	public void conversation_ai_multiple_filters() throws Exception {
		System.out.println("Test Case-Arrange data on Agent name in ascending order--Started");
		driverUsed.put("caiDriver1", true);
		dashboard.clickConversationAI(caiDriver1);
		callsTabPage.clearAllFilters(caiDriver1);
		// Reset Order to Ascending when set from Search Filter
		callsTabPage.setAgentFilter(caiDriver1, "My Calls");
		String searchToken = callsTabPage.getLoggedInAgentName(caiDriver1);
		boolean status = callsTabPage.getBooleanFilterStatus(caiDriver1);
		callsTabPage.setEngagementFilter(caiDriver1, CallData.HasSupNotes.name());
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationAscending.toString());
		callsTabPage.idleWait(2);
		int page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchResult(caiDriver1, searchToken, page, CallData.Agent.name(), status);
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationAscending.toString());
		callsTabPage.idleWait(2);
		callsTabPage.verifyEngagementFilter(caiDriver1, CallData.HasSupNotes.name(), page, status);
		callsTabPage.clickCallTab(caiDriver1);
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationAscending.toString());
		callsTabPage.idleWait(2);
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.DurationAscending.toString(),"callDuration");
		System.out.println("Test Case-Arrange data on call duration in ascending order--Passed");
				
		System.out.println("Test Case-Arrange data on call duration in descending order--Started");	
		callsTabPage.clickCallTab(caiDriver1);
		// Reset Order to Ascending when set from Search Filter
		callsTabPage.selectSearchOrder(caiDriver1, CallsTabPage.SearchOrderOptions.DurationDescending.toString());
		callsTabPage.idleWait(2);
		page = Integer.valueOf(callsTabPage.getTotalPage(caiDriver1));
		callsTabPage.verifySearchOrder(caiDriver1, page, CallsTabPage.SearchOrderOptions.DurationDescending.toString(),"callDuration");
	
		driverUsed.put("caiDriver1", false);
		System.out.println("Test Case-Arrange data on Agent name in descending order--Passed");
	}
	
	@Test(groups = { "MediumPriority" }, priority = 57)
	public void verify_setting_tab_accessible_functionality_with_admin() {
		
		System.out.println("Test case --verify_setting_tab_accessible_functionality_with_admin--Started");
		driverUsed.put("caiDriver1", true);
		
		//Enable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	
		dashboard.clickConversationAI(caiDriver1);
		assertTrue(setUpTabPage.isSetUpTabVisible(caiDriver1));
		setUpTabPage.clickSetUpTab(caiDriver1);
		assertTrue(dashboard.isCAISetupDropDownVisible(caiDriver1));
	
		//Disable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.disableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	
		dashboard.clickConversationAI(caiDriver1);
		assertFalse(setUpTabPage.isSetUpTabVisible(caiDriver1));
		assertFalse(dashboard.isCAISetupDropDownVisible(caiDriver1));
	
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --verify_setting_tab_accessible_functionality_with_admin--Started");
	}
	
	@Test(groups = { "MediumPriority" }, priority = 58)
	public void verify_setting_tab_accessible_functionality_with_agent() {
		
		System.out.println("Test case --verify_setting_tab_accessible_functionality_with_agent--Started");
		driverUsed.put("caiDriver1", true);
		
		//Enable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver2);
		usersPage.enableConversationAIManagerBtn(caiDriver2);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver2);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver2);
	
		dashboard.clickConversationAI(caiDriver2);
		assertTrue(setUpTabPage.isSetUpTabVisible(caiDriver2));
		setUpTabPage.clickSetUpTab(caiDriver2);
		assertTrue(dashboard.isCAISetupDropDownVisible(caiDriver2));
	
		//Disable CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver2);
		usersPage.disableConversationAIManagerBtn(caiDriver2);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver2);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver2);
	
		dashboard.clickConversationAI(caiDriver2);
		assertFalse(setUpTabPage.isSetUpTabVisible(caiDriver2));
		assertFalse(dashboard.isCAISetupDropDownVisible(caiDriver2));
	
		driverUsed.put("caiDriver2", false);
		System.out.println("Test case --verify_setting_tab_accessible_functionality_with_agent--Started");
	}
	
	@Test(groups = { "MediumPriority" }, priority = 59)
	public void verify_no_setup_no_notification_visible_in_user_dropdown_when_cai_off() {
		
		System.out.println("Test case --verify_no_setup_no_notification_visible_in_user_dropdown_when_cai_off--Started");
		driverUsed.put("caiDriver1", true);
		
		//Disable CAI Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.disableConversationAnalyticsBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	
		assertFalse(dashboard.isCAISetupDropDownVisible(caiDriver1));
		assertFalse(dashboard.isCAINotificationsDropDownVisible(caiDriver1));
	
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --verify_no_setup_no_notification_visible_in_user_dropdown_when_cai_off--Started");
	}
	
	@Test(groups = { "MediumPriority" }, priority = 60)
	public void verify_reports_accessibility_when_manager_settings_on_off() {
		
		System.out.println("Test case --verify_reports_accessibility_when_manager_settings_on_off--Started");
		driverUsed.put("caiDriver1", true);
		
		//Config CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.enableConversationAnalyticsBtn(caiDriver1);
		usersPage.disableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
		
		dashboard.clickConversationAI(caiDriver1);
		assertFalse(dashBoardConversationAI.isInsightsSectionVisible(caiDriver1));
		
		String reportsUrl = "https://app-qa.ringdna.net/#smart-recordings/reporting";
		dashboard.openNewBlankTab(caiDriver1);
		dashboard.switchToTab(caiDriver1, dashboard.getTabCount(caiDriver1));
		caiDriver1.get(reportsUrl);
		assertFalse(caiDriver1.getCurrentUrl().contains("reporting"));
		
		dashboard.closeTab(caiDriver1);
		dashboard.switchToTab(caiDriver1, 2);
		
		// Config CAI Manager Setting
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.enableConversationAnalyticsBtn(caiDriver1);
		usersPage.enableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);

		dashboard.clickConversationAI(caiDriver1);
		assertTrue(dashBoardConversationAI.isInsightsSectionVisible(caiDriver1));
		
		driverUsed.put("caiDriver1", false);
		System.out.println("Test case --verify_reports_accessibility_when_manager_settings_on_off--Started");
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority" })
	public void afterClass() {
		// disable CAI Manager Setting
		dashboard.switchToTab(caiDriver1, 2);
		dashboard.clickOnUserProfile(caiDriver1);
		usersPage.disableConversationAIManagerBtn(caiDriver1);
		userIntelligentDialerTab.saveAcccountSettings(caiDriver1);
		userIntelligentDialerTab.refreshCurrentDocument(caiDriver1);
	}
}