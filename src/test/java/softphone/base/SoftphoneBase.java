package softphone.base;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.SeleniumBase;
import base.TestBase;
import softphone.source.CallScreenPage;
import softphone.source.CustomUserStatusPage;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCallQueues;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneMessagePage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftPhoneTeamPage;
import softphone.source.SoftPhoneWebLeadsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.SoftphoneTodaysTasksPage;
import softphone.source.callDashboard.CallDashboardPage;
import softphone.source.callDashboard.CallDashboardReactPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewEvent;
import softphone.source.callTools.SoftPhoneNewTask;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.LightningEmailTemplate;
import softphone.source.salesforce.SalesforceAccountPage;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.SearchPage;
import softphone.source.salesforce.TaskDetailPage;
import softphone.source.salesforce.salesforceCampaign;
import softphone.source.sip.SipCallingPage;
import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLocalPresenceTab;
import support.source.accounts.AccountLogsTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSkillsTab;
import support.source.accounts.AccountsPage;
import support.source.admin.AccountCallRecordingTab;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.calls.CallInspector;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.CallRecordingPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.ConversationDetailPage;
import support.source.messages.MessagesPage;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.ExcelDataManager;
import utility.HelperFunctions;
import utility.listeners.AlterSuite;

public class SoftphoneBase extends TestBase{
	
	protected AccountBlockedNumbersTab 		accountBlockedNumbersTab 	= new AccountBlockedNumbersTab();
	protected AccountCallRecordingTab 		accountCallRecordingTab 	= new AccountCallRecordingTab();	
	protected AccountIntelligentDialerTab	accountIntelligentDialerTab = new AccountIntelligentDialerTab();
	protected AccountLocalPresenceTab 		accountLocalPresenceTab 	= new AccountLocalPresenceTab();
	protected AccountLogsTab 				accountLogsTab 				= new AccountLogsTab();
	protected AccountOverviewTab 			accountOverviewtab 			= new AccountOverviewTab();
	protected AccountSalesforceTab 			accountSalesforceTab 		= new AccountSalesforceTab();
	protected AccountsPage 					accountsPage 				= new AccountsPage();
	protected AccountSkillsTab				accountSkillsTab			= new AccountSkillsTab();
	protected AddSmartNumberPage 			addSmartNoPage 				= new AddSmartNumberPage();
	protected AdminCallTracking				adminCallTracking			= new AdminCallTracking();
	protected CallDashboardPage 			callDashboardPage 			= new CallDashboardPage();
	protected CallDashboardReactPage 		callDashboardReactPage 		= new CallDashboardReactPage();
	protected CallFlowPage 					callFlowPage 				= new CallFlowPage();
	protected CallInspector 				callInspector				= new CallInspector();
	protected CallQueuesPage 				callQueuesPage 				= new CallQueuesPage();
	protected CallRecordingPage 			callRecordingPage 			= new CallRecordingPage();
	protected CallScreenPage 				callScreenPage 				= new CallScreenPage();
	protected CallsTabReactPage 			callTabReactPage 			= new CallsTabReactPage();
	protected CallToolsPanel 				callToolsPanel 				= new CallToolsPanel();
	protected ContactDetailPage 			contactDetailPage 			= new ContactDetailPage();
	protected ConversationDetailPage 		conversationDetailPage 		= new ConversationDetailPage();
	protected CustomUserStatusPage			customUserStatusPage		= new CustomUserStatusPage();
	protected Dashboard				 		dashboard 					= new Dashboard();
	protected ExcelDataManager 				excelDataManager 			= new ExcelDataManager();
	protected GroupsPage 					groupsPage 					= new GroupsPage();
	protected HelperFunctions 				helperFunctions 			= new HelperFunctions();
	protected LightningEmailTemplate 		sfLightningEmailTemplate 	= new LightningEmailTemplate();
	protected MessagesPage					messagesPage				= new MessagesPage();
	protected ReportThisCallPage 			reportThisCallPage 			= new ReportThisCallPage();
	protected SalesforceAccountPage 		salesforceAccountPage 		= new SalesforceAccountPage();
	protected salesforceCampaign 			sfCampaign 					= new salesforceCampaign();
	protected SalesforceHomePage 			salesforceHomePage 			= new SalesforceHomePage();
	protected SearchPage 					sfSearchPage 				= new SearchPage();
	protected SeleniumBase 					seleniumBase 				= new SeleniumBase();
	protected SipCallingPage 				sipCallingPage 				= new SipCallingPage();
	protected SmartNumbersPage 				smartNumbersPage 			= new SmartNumbersPage();
	protected SoftPhoneActivityPage 		softPhoneActivityPage 		= new SoftPhoneActivityPage();
	protected SoftphoneCallHistoryPage 		softphoneCallHistoryPage 	= new SoftphoneCallHistoryPage();
	protected SoftPhoneCalling 				softPhoneCalling 			= new SoftPhoneCalling();
	protected SoftPhoneCallQueues 			softPhoneCallQueues 		= new SoftPhoneCallQueues();
	protected SoftPhoneContactsPage 		softPhoneContactsPage 		= new SoftPhoneContactsPage();
	protected SoftPhoneLoginPage 			SFLP 						= new SoftPhoneLoginPage();
	protected SoftPhoneMessagePage 			softPhoneMessagePage 		= new SoftPhoneMessagePage();
	protected SoftPhoneNewEvent 			softPhoneNewEvent 			= new SoftPhoneNewEvent();
	protected SoftPhoneNewTask 				softPhoneNewTask 			= new SoftPhoneNewTask();
	protected SoftPhoneSettingsPage 		softPhoneSettingsPage 		= new SoftPhoneSettingsPage();
	protected SoftPhoneTeamPage 			softPhoneTeamPage 			= new SoftPhoneTeamPage();
	protected SoftPhoneWebLeadsPage 		softPhoneWebLeadsPage 		= new SoftPhoneWebLeadsPage();
	protected SoftphoneTodaysTasksPage		todaysTasksPage				= new SoftphoneTodaysTasksPage();
	protected SupportBase					supportBase					= new SupportBase();
	protected TaskDetailPage 				sfTaskDetailPage 			= new TaskDetailPage();
	protected UserIntelligentDialerTab 		userIntelligentDialerTab 	= new UserIntelligentDialerTab();
	protected UsersPage 					usersPage 					= new UsersPage();
	
	public void initializeDriverSoftphone(String driver)
	{
		if ((driver.equals("driver1")) && (driver1 == null  || driver1.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			driver1 = getDriver();
			SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(driver1);
		} else if ((driver.equals("driver2")) && (driver2 == null || driver2.toString().contains("(null)"))) {
			System.out.println("Second browser is opening");
			driver2 = getDriver();
			System.out.println("Login with prod softphone 1");
			SFLP.softphoneLogin(driver2, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_1_username"), CONFIG.getProperty("prod_user_1_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver2);
		} else if ((driver.equals("driver3")) && (driver3 == null || driver3.toString().contains("(null)"))) {
			System.out.println("Third browser is opening");
			driver3 = getDriver();
			System.out.println("Login with qa softphone 3");
			SFLP.softphoneLogin(driver3, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_3_username"), CONFIG.getProperty("qa_user_3_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver3);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(driver3);
		} else if ((driver.equals("driver4")) && (driver4 == null || driver4.toString().contains("(null)"))) {
			System.out.println("Fourth browser is opening");
			driver4 = getDriver();
			System.out.println("Login with qa softphone 2");
			SFLP.softphoneLogin(driver4, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver4);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(driver4);
		} else if ((driver.equals("driver5")) && (driver5 == null || driver5.toString().contains("(null)"))) {
			System.out.println("Fifth browser is opening");
			driver5 = getDriver();
			System.out.println("Login with prod softphone 2");
			SFLP.softphoneLogin(driver5, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_2_username"), CONFIG.getProperty("prod_user_2_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver5);
		} else if ((driver.equals("driver6")) && (driver6 == null || driver6.toString().contains("(null)"))) {
			System.out.println("Sixth browser is opening");
			driver6 = getDriver();
			System.out.println("Login with prod softphone 3");
			SFLP.softphoneLogin(driver6, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_3_username"), CONFIG.getProperty("prod_user_3_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver6);
		} else if ((driver.equals("chatterOnlyDriver")) && (chatterOnlyDriver == null)) {
			System.out.println("chatter only browser is opening");
			chatterOnlyDriver = getDriver();
			System.out.println("Login with chatter only user");
			SFLP.softphoneLogin(chatterOnlyDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_chatter_only_user_username"), CONFIG.getProperty("qa_chatter_only_user_password"));
		} else if ((driver.equals("standardUserDriver")) && (standardUserDriver == null)) {
			System.out.println("chatter only browser is opening");
			standardUserDriver = getDriver();
			System.out.println("Login with chatter only user");
			SFLP.softphoneLogin(standardUserDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_standard_user_username"), CONFIG.getProperty("qa_standard_user_password"));
			salesforceHomePage.openCampaignAndSwitchToClassicSF(standardUserDriver);
		} else if ((driver.equals("webSupportDriver")) && (webSupportDriver == null)) {
			System.out.println("Support browser is opening");
			webSupportDriver = getDriver();
			System.out.println("Login with support driver");
			SFLP.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(webSupportDriver);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(webSupportDriver);
		} else if ((driver.equals("adminDriver")) && (adminDriver == null || adminDriver.toString().contains("(null)"))) {
			System.out.println("admin browser is opening");
			adminDriver = getDriver();
			System.out.println("Login with admin driver");
			SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(adminDriver);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(adminDriver);
		} else if ((driver.equals("qaV2Driver")) && (qaV2Driver == null)) {
			System.out.println("qa v2 browser is opening");
			qaV2Driver = getDriver();
			System.out.println("Login with qa v2 driver");
			SFLP.softphoneLogin(qaV2Driver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_v2_user_username"), CONFIG.getProperty("qa_v2_user_password"));
			//softPhoneSettingsPage.setDefaultSoftPhoneSettings(qaV2Driver);
			//salesforceHomePage.openCampaignAndSwitchToClassicSF(qaV2Driver);
		} else if ((driver.equals("agentDriver")) && (agentDriver == null || agentDriver.toString().contains("(null)"))) {
			System.out.println("agent browser is opening");
			agentDriver = getDriver();
			System.out.println("Login with agent driver");
			SFLP.softphoneLogin(agentDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(agentDriver);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(agentDriver);
		}else if ((driver.equals("callDashboardDriver")) && (callDashboardDriver == null || callDashboardDriver.toString().contains("(null)"))) {
			System.out.println("call Dashboard browser is opening");
			callDashboardDriver = getDriver();
			System.out.println("Login with call Dashboard driver");
			SFLP.softphoneLogin(callDashboardDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_call_dashboard_username"), CONFIG.getProperty("qa_call_dashboard_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(callDashboardDriver);
			salesforceHomePage.openCampaignAndSwitchToClassicSF(callDashboardDriver);
		}
	}
		
	public void closeLightningBox(WebDriver driver) {
		sfCampaign.openSalesforceCampaignPage(driver);
		reloadSoftphone(driver);
		salesforceHomePage.closeLightningDialogueBoxForFirstTime(driver);
		seleniumBase.closeTab(driver);
		seleniumBase.switchToTab(driver, 1);
	}
	
	public void loginSupport(WebDriver driver) {
		seleniumBase.openNewBlankTab(driver);
		seleniumBase.switchToTab(driver, seleniumBase.getTabCount(driver));
		driver.get(CONFIG.getProperty("qa_support_tool_site"));
		seleniumBase.closeRecommendationWindow(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void loginSupportProd(WebDriver driver) {
		seleniumBase.openNewBlankTab(driver);
		seleniumBase.switchToTab(driver, seleniumBase.getTabCount(driver));
		driver.get(CONFIG.getProperty("app_support_tool_site"));
		seleniumBase.closeRecommendationWindow(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void resetSoftphone(WebDriver driver, String siteName, String userName, String password)
	{
		
		// closing all other opened tabs
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		if (tabs.size() > 1) {
			for (int count = tabs.size(); count > 1; count--) {
				seleniumBase.switchToTab(driver, count);
				driver.close();
			}
			seleniumBase.switchToTab(driver, 1);
		}

		//hanging up if active calls are available
		try {
			if(seleniumBase.isElementVisible(driver, softPhoneCalling.hangUpButton, 1)){
				seleniumBase.getWebelementIfExist(driver, softPhoneCalling.hangUpButton).click();
			}
			if (seleniumBase.isElementVisible(driver, softPhoneCalling.onHoldButton, 2)) {
				driver.findElement(softPhoneCalling.onHoldButton).click();
				driver.findElement(softPhoneCalling.resumeHoldCallButton).click();
				seleniumBase.isElementVisible(driver, softPhoneCalling.hangUpButton, 30);
				driver.findElement(softPhoneCalling.hangUpButton).click();
			}
		} catch (Exception e) {
			System.out.println("not able to hangup call due to error " + e.getMessage());
		}

		//login to softphone if user is logged out
		if (seleniumBase.isElementVisible(driver, By.cssSelector("[data-action*=login]"), 1) || seleniumBase.isElementVisible(driver, By.xpath(".//*[text()='OAuth Error']"), 0)) {
			driver.get(siteName);
			SFLP.softphoneLogin(driver, siteName, userName, password);
		}else {
			reloadSoftphone(driver);
		}
	}

	public void reloadSoftphone(WebDriver driver) {
		try {
			By spinnerWheel = By.xpath("//*[contains(@id, 'spinner')]");
			driver.navigate().refresh();
			try {
				driver.switchTo().alert().accept();
			} catch (Exception e) {
				System.out.println("No alert took place");
			}
			seleniumBase.isElementVisible(driver, spinnerWheel, 3);
			seleniumBase.waitUntilInvisible(driver, spinnerWheel);

			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("scroll(0, 0)", new Object[0]);
			if (seleniumBase.getWebelementIfExist(driver, By.cssSelector("img.keypad")) == null || (seleniumBase.getWebelementIfExist(driver, By.cssSelector("img.keypad")) != null && !(seleniumBase.getWebelementIfExist(driver, By.cssSelector("img.keypad")).isDisplayed()))) {
				System.out.println("Browser is not loaded properly");
			}
			softphoneCallHistoryPage.isCallerHistoryBlank(driver);
		} catch (Exception e) {
			e.getMessage();
			quitDrivers();
			setDriverUsedToFalse();
		}
	}

	@Override
	public void resetApplication() {
		System.out.println("This is in reset application of softphone base");
		if ((driverUsed.get("driver2") != null && (driverUsed.get("driver2")).booleanValue())) {
			try {
				System.out.println("Reset the prod softphone 1");
				resetSoftphone(driver2, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_1_username"), CONFIG.getProperty("prod_user_1_password"));	
			}catch (Exception e) {
				driver2.quit();
				driver2 = null;
			}
		}
		if ((driverUsed.get("driver3") != null && (driverUsed.get("driver3")).booleanValue())) {
			try {
				System.out.println("Reset the qa softphone 3");
				resetSoftphone(driver3, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_3_username"), CONFIG.getProperty("qa_user_3_password"));	
			}catch (Exception e) {
				driver3.quit();
				driver3 = null;
			}
		}
		if ((driverUsed.get("driver4") != null && (driverUsed.get("driver4")).booleanValue())) {
			try {
				System.out.println("Reset the qa softphone 2");
				resetSoftphone(driver4, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));	
			}catch (Exception e) {
				driver4.quit();
				driver4 = null;
			}
		}
		if ((driverUsed.get("driver5") != null && (driverUsed.get("driver5")).booleanValue())) {
			try {
				System.out.println("Reset the prod softphone 2");
				resetSoftphone(driver5, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_2_username"), CONFIG.getProperty("prod_user_2_password"));	
			}catch (Exception e) {
				driver5.quit();
				driver5 = null;
			}
		}
		if ((driverUsed.get("driver6") != null && (driverUsed.get("driver6")).booleanValue())) {
			try {
				System.out.println("Reset the prod softphone 3");
				resetSoftphone(driver6, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("prod_user_3_username"), CONFIG.getProperty("prod_user_3_password"));	
			}catch (Exception e) {
				driver6.quit();
				driver6 = null;
			}
		}
		if ((driverUsed.get("driver1") != null && (driverUsed.get("driver1")).booleanValue())) {
			try {
				System.out.println("Reset the qa softphone 1");
				resetSoftphone(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));	
			}catch (Exception e) {
				driver1.quit();
				driver1 = null;
			}
			seleniumBase.idleWait(3);
		}
		if ((driverUsed.get("chatterOnlyDriver") != null && (driverUsed.get("chatterOnlyDriver")).booleanValue())) {
			System.out.println("Reset cahtter only user");
			resetSoftphone(chatterOnlyDriver, CONFIG.getProperty("app_test_site_name"), CONFIG.getProperty("qa_chatter_only_user_username"), CONFIG.getProperty("qa_chatter_only_user_password"));
			seleniumBase.idleWait(3);
		}
		if ((driverUsed.get("webSupportDriver") != null && ( driverUsed.get("webSupportDriver")).booleanValue())) {
			try {
				System.out.println("Reset the web support driver");
				resetSoftphone(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));	
			}catch (Exception e) {
				webSupportDriver.quit();
				webSupportDriver = null;
			}
		}
		if ((driverUsed.get("agentDriver") != null && ( driverUsed.get("agentDriver")).booleanValue())) {
			try {
				System.out.println("Reset the agent driver");
				resetSoftphone(agentDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));	
			}catch (Exception e) {
				agentDriver.quit();
				agentDriver = null;
			}
		}
		if ((driverUsed.get("adminDriver") != null && ( driverUsed.get("adminDriver")).booleanValue())) {
			try {
				System.out.println("Reset the admin driver");
				resetSoftphone(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));	
			}catch (Exception e) {
				adminDriver.quit();
				adminDriver = null;
			}
		}
		if ((driverUsed.get("qaV2Driver") != null && ( driverUsed.get("qaV2Driver")).booleanValue())) {
			try {
				System.out.println("Reset the qa v2 driver");
				resetSoftphone(qaV2Driver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_v2_user_username"), CONFIG.getProperty("qa_v2_user_password"));	
			}catch (Exception e) {
				qaV2Driver.quit();
				qaV2Driver = null;
			}
		}
		if ((driverUsed.get("standardUserDriver") != null && ( driverUsed.get("standardUserDriver")).booleanValue())) {
			try {
				System.out.println("Reset the standard User driver");
				resetSoftphone(standardUserDriver, CONFIG.getProperty("qa_test_site_name"),  CONFIG.getProperty("qa_standard_user_username"), CONFIG.getProperty("qa_standard_user_password"));	
			}catch (Exception e) {
				standardUserDriver.quit();
				standardUserDriver = null;
			}
		}
		if ((driverUsed.get("callDashboardDriver") != null && ( driverUsed.get("callDashboardDriver")).booleanValue())) {
			try {
				System.out.println("Reset the standard User driver");
				resetSoftphone(callDashboardDriver, CONFIG.getProperty("qa_test_site_name"),  CONFIG.getProperty("qa_call_dashboard_username"), CONFIG.getProperty("qa_call_dashboard_password"));	
			}catch (Exception e) {
				callDashboardDriver.quit();
				callDashboardDriver = null;
			}
		}
	}
	
	@Test(groups={"Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void aa_AddCallersAsContactsAndLeads() {
		
		System.out.println("running aa_AddCallersAsContactsAndLeads");

		//updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		softPhoneContactsPage.closeTab(driver1);
		softPhoneContactsPage.switchToTab(driver1, 1);
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("qa_user_1_number"), CONFIG.getProperty("qa_user_1_name"));
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("qa_user_3_number"), CONFIG.getProperty("qa_user_3_name"));
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));
		softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
		driverUsed.put("driver1", false);		
	}
	
	@BeforeClass
	public void smokeBeforeClass() {
		if (AlterSuite.type == AlterSuite.Types.Smoke) {
			aa_AddCallersAsContactsAndLeads();
		}
	}
}