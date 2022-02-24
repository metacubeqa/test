package report.cases.caiReports;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.caiReportPage.CAIReportPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import utility.HelperFunctions;

public class KeywordImpact extends ReportBase {

	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CAIReportPage caiReportPage = new CAIReportPage();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	
	static String dateFormat = "MMMMM dd, yyyy";

	private String accountName;
//	private String accountNameOther;
//	private String userNameValid;
	private String userNameInValid;
//	private String locationNameValid;
	private String locationNameInValid;
//	private String teamNameValid;
	private String teamNameInValid;

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
//		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
		
//		userNameValid = CONFIG.getProperty("qa_cai_report_valid_user_name");
		userNameInValid = CONFIG.getProperty("qa_report_invalid_user_name");

//		locationNameValid = CONFIG.getProperty("qa_cai_report_valid_location_name");
		locationNameInValid = CONFIG.getProperty("qa_report_invalid_location_name");
		
//		teamNameValid = CONFIG.getProperty("qa_cai_report_valid_team_name");
		teamNameInValid = CONFIG.getProperty("qa_report_invalid_team_name");
		
	}
	
	//Verify keywordImpact Report with Account Name which is not exists
	@Test(groups = { "Regression"})
	public void verify_invalid_account_name_not_exists_for_keywordImpact() {
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_keywordImpact-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToKeywordImpact(webSupportDriver);

		// verifying invalid Account Name not exits
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(webSupportDriver,HelperFunctions.GetRandomString(5)));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_invalid_account_name_not_exists_for_keywordImpact-- passed ");
	}
	
	//Verify keywordImpact Report with User Name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_username_not_exists_for_selected_account_keywordImpact() {
		System.out.println("Test case --verify_username_not_exists_for_selected_account_keywordImpact-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToKeywordImpact(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Agent Name not exits
		assertFalse(callRecordingReportPage.verifyAgentExistsInDropDown(webSupportDriver, userNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_username_not_exists_for_selected_account_keywordImpact-- passed ");
	}
	
	//Verify keywordImpact Report with Location name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_location_not_exists_for_selected_account_keywordImpact() {
		System.out.println("Test case --verify_location_not_exists_for_selected_account_keywordImpact-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToKeywordImpact(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying Location Name not exits
		assertFalse(reportCommonPage.verifyLocationExistsInDropDown(webSupportDriver, locationNameInValid));

		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_location_not_exists_for_selected_account_keywordImpact-- passed ");
	}
	
	//Verify keywordImpact Report with Team name which does not exist in selected/logged in account
	@Test(groups = { "Regression" })
	public void verify_teamname_not_exists_for_selected_account_keywordImpact() {
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_keywordImpact-- started ");

		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickConversationAI(webSupportDriver);
		dashBoardCAI.navigateToInsightsSection(webSupportDriver);
		caiReportPage.navigateToKeywordImpact(webSupportDriver);

		callRecordingReportPage.selectAccount(webSupportDriver, accountName);

		// verifying team name exist or not
		assertFalse(reportCommonPage.verifyTeamNameExistsInDropDown(webSupportDriver, teamNameInValid));
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_teamname_not_exists_for_selected_account_keywordImpact-- passed ");
	}
}