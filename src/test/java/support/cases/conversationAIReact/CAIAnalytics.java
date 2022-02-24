package support.cases.conversationAIReact;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import report.source.caiReportPage.CAIReportPage;
import softphone.source.SoftPhoneLoginPage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAIReact.CAIAnalyticsPage;
import support.source.conversationAIReact.InboxPage;
import support.source.conversationAIReact.LibraryReactPage;
import support.source.conversationAIReact.SavedSearchPage;
import support.source.conversationAIReact.SettingsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;

public class CAIAnalytics extends SupportBase {

	WebDriver caiAnalyticsDriver;

	CAIAnalyticsPage caiAnalyticsPage = new CAIAnalyticsPage();
	UsersPage userPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();

	InboxPage inboxPage = new InboxPage();
	LibraryReactPage libraryReactPage = new LibraryReactPage();
	SavedSearchPage savedSearchPage = new SavedSearchPage();
	SettingsPage settingsPage = new SettingsPage();
	CAIReportPage insightsPage = new CAIReportPage();

	UsersPage usersPage = new UsersPage();
	
//	Verify user logged into cai page with valid user name and password 
	@Test(groups = { "Regression"})
	public void verify_login_analytics_success() {

		System.out.println("Test case --verify_login_analytics_success-- started ");
		
		caiAnalyticsDriver = getDriver();
		
		//incorrect login
		caiAnalyticsPage.loginAnalyticsIncorrect(caiAnalyticsDriver);
		
		//correct login
		caiAnalyticsPage.refreshCurrentDocument(caiAnalyticsDriver);
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		
		System.out.println("Test case --verify_login_analytics_success-- passed ");
	}
	
//	Verify login into CAI page with deleted or non QA user 
	@Test(groups = { "Regression"})
	public void verify_login_feature_with_deleted_user() {

		System.out.println("Test case --verify_login_feature_with_deleted_user-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "ringdna.test2@ringdna.com", "ebmdna0198");
		
		caiAnalyticsPage.isElementVisible(caiAnalyticsDriver, caiAnalyticsPage.welcomeCAIHeader, 5);
		caiAnalyticsPage.isElementVisible(caiAnalyticsDriver, caiAnalyticsPage.loginBtn, 5);
		
		System.out.println("Test case --verify_login_feature_with_deleted_user-- passed ");
	}

//	Verify logged into CAI with the user who have CAI manager setting OFF 
	@Test(groups = { "Regression"})
	public void verify_insights_tab_not_visible_cai_manager_off() {
		
		System.out.println("Test case --verify_insights_tab_not_visible_cai_manager_off-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		caiAnalyticsPage.navigateToUserOverview(caiAnalyticsDriver);
		usersPage.enableConversationAnalyticsBtn(caiAnalyticsDriver);
		usersPage.disableConversationAIManagerBtn(caiAnalyticsDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiAnalyticsDriver);
		
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		caiAnalyticsPage.refreshCurrentDocument(caiAnalyticsDriver);
		assertFalse(dashBoardCAI.isInsightsSectionVisible(caiAnalyticsDriver));
		assertFalse(dashBoardCAI.isSettingsSectionVisible(caiAnalyticsDriver));
		
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 2);
		usersPage.enableConversationAnalyticsBtn(caiAnalyticsDriver);
		usersPage.enableConversationAIManagerBtn(caiAnalyticsDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiAnalyticsDriver);
		userIntelligentDialerTab.closeTab(caiAnalyticsDriver);
		
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		caiAnalyticsPage.refreshCurrentDocument(caiAnalyticsDriver);
		assertTrue(dashBoardCAI.isInsightsSectionVisible(caiAnalyticsDriver));
		assertTrue(dashBoardCAI.isSettingsSectionVisible(caiAnalyticsDriver));
	

		System.out.println("Test case --verify_insights_tab_not_visible_cai_manager_off-- passed ");
	}
	
//	Verify login with the user who not have conversation licenses or CAI setting ON
	@Test(groups = { "Regression"})
	public void verify_login_analytics_cai_settings_off() {
		
		System.out.println("Test case --verify_login_analytics_cai_settings_off-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		caiAnalyticsPage.navigateToUserOverview(caiAnalyticsDriver);
		usersPage.disableConversationAnalyticsBtn(caiAnalyticsDriver);
		usersPage.disableConversationAIManagerBtn(caiAnalyticsDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiAnalyticsDriver);
		
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		caiAnalyticsPage.refreshCurrentDocument(caiAnalyticsDriver);
		caiAnalyticsPage.waitUntilVisible(caiAnalyticsDriver, caiAnalyticsPage.userLicRequired);
		
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 2);
		usersPage.enableConversationAnalyticsBtn(caiAnalyticsDriver);
		usersPage.enableConversationAIManagerBtn(caiAnalyticsDriver);
		userIntelligentDialerTab.saveAcccountSettings(caiAnalyticsDriver);
		userIntelligentDialerTab.closeTab(caiAnalyticsDriver);

		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		caiAnalyticsPage.refreshCurrentDocument(caiAnalyticsDriver);
		assertFalse(caiAnalyticsPage.isElementVisible(caiAnalyticsDriver, caiAnalyticsPage.userLicRequired, 5));

		System.out.println("Test case --verify_login_analytics_cai_settings_off-- passed ");
	}

	//Verify access User's calednar page from the user's dropdown 3158750
	@Test(groups = { "Regression"})
	public void verify_cai_calendar_page_from_dropdown() {

		System.out.println("Test case --verify_cai_calendar_page_from_dropdown-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		caiAnalyticsPage.navigateToCalendarAvailability(caiAnalyticsDriver);
		caiAnalyticsPage.closeTab(caiAnalyticsDriver);
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		
		System.out.println("Test case --verify_cai_calendar_page_from_dropdown-- passed ");
	}

	//Veirfy conversation AI Notification page from the user's dropdown 
	@Test(groups = { "Regression"})
	public void verify_cai_notification_page_from_dropdown() {

		System.out.println("Test case --verify_cai_notification_page_from_dropdown-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		dashboard.navigateToCAINotifications(caiAnalyticsDriver);
		assertTrue(dashboard.isElementVisible(caiAnalyticsDriver, SettingsPage.notificationSettingsHeader, 5));
		caiAnalyticsPage.closeTab(caiAnalyticsDriver);
		caiAnalyticsPage.switchToTab(caiAnalyticsDriver, 1);
		
		System.out.println("Test case --verify_cai_notification_page_from_dropdown-- passed ");
	}

	//Veirfy user access all tabs from top navigation of cai application  
	@Test(groups = { "Regression"})
	public void verify_user_access_all_tabs() {

		System.out.println("Test case --verify_user_access_all_tabs-- started ");
		
		caiAnalyticsDriver = getDriver();
		caiAnalyticsPage.loginAnalytics(caiAnalyticsDriver, "vishalqav2@ringdna.com", "Rtgh331022!");
		caiAnalyticsPage.verifyLoginSuccess(caiAnalyticsDriver);
		
		inboxPage.navigateToInboxTab(caiAnalyticsDriver);
		inboxPage.verifyDefaultFilters(caiAnalyticsDriver);
		
		libraryReactPage.openLibraryPage(caiAnalyticsDriver);
		libraryReactPage.isElementVisible(caiAnalyticsDriver, libraryReactPage.createNewLibraryBtn, 5);
		
		savedSearchPage.openSavedSearchPage(caiAnalyticsDriver);
		savedSearchPage.openMySavedSearchSection(caiAnalyticsDriver);

		settingsPage.openSettingsPage(caiAnalyticsDriver);
		settingsPage.verifyCustomVocHeadersLabel(caiAnalyticsDriver);
		
		insightsPage.openInsightsTab(caiAnalyticsDriver);
		insightsPage.expandCAIEtiquetteSection(caiAnalyticsDriver);
		
		System.out.println("Test case --verify_user_access_all_tabs-- passed ");
	}
	
	@AfterMethod(groups = { "Regression" })
	public void afterMethod(ITestResult result) {
		if(caiAnalyticsDriver!=null) {
			caiAnalyticsDriver.quit();
		}
		
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "verify_login_analytics_cai_settings_off":
			case "verify_insights_tab_not_visible_cai_manager_off":
				
				caiAnalyticsDriver = getDriver();
				softPhoneLoginPage.supportLogin(caiAnalyticsDriver, "https://app-qa.ringdna.net", "vishalqav2@ringdna.com", "Rtgh331022!");
				usersPage.enableConversationAnalyticsBtn(caiAnalyticsDriver);
				usersPage.enableConversationAIManagerBtn(caiAnalyticsDriver);
				userIntelligentDialerTab.saveAcccountSettings(caiAnalyticsDriver);
				
				break;
			}
		}
		
		if(caiAnalyticsDriver!=null) {
			caiAnalyticsDriver.quit();
		}

	}
}
