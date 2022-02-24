package support.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import base.SeleniumBase;
import base.TestBase;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.salesforceCampaign;
import support.source.accounts.AccountsPage;
import support.source.commonpages.Dashboard;

public class SupportBase extends TestBase{

	SeleniumBase SB = new SeleniumBase();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	AccountsPage accountsPage = new AccountsPage();
	protected SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	protected SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	salesforceCampaign sfCampaign = new salesforceCampaign();
	SalesforceHomePage sfHomePge = new SalesforceHomePage();
	
	public static String drivername = null;
	public static HashMap<String, WebDriver> webDriverMap = new HashMap<>();
	private final static String SoftphonePageTitle   = "ringDNA (qa)";
	private final static String SupportAppTitle	 = "ringDNA Web";

	static int i = 0;
	By userDropdownPullDown = By.cssSelector(".user-profile.dropdown-toggle span.fa.fa-angle-down,[aria-label = 'rdna-menu-trigger']");
	By logoutBtn			= By.cssSelector("a[data-action='logout'], ul li[data-analyticsid='menu-item-Logout']");
	
	/* 
	 * This method is used in case of test failure exception
	 */
	@Override
	public void resetApplication() {
		
		for (Entry<String, WebDriver> entry : webDriverMap.entrySet()) {
			String webDriverString = entry.getKey();
			WebDriver activeWebDriver = entry.getValue();

			if (((Boolean) driverUsed.get(webDriverString)).booleanValue()) {
				System.out.println(webDriverString);
				
				// closing all other opened tabs
				try {
					ArrayList<String> tabs = new ArrayList<String>(activeWebDriver.getWindowHandles());
					if (tabs.size() > 1) {
						for (int count = tabs.size(); count > 1; count--) {
							SB.switchToTab(activeWebDriver, count);
							activeWebDriver.close();
						}
						SB.switchToTab(activeWebDriver, 1);
						SB.reloadSoftphone(activeWebDriver);
						SB.waitForPageLoaded(activeWebDriver);
						
						//opening support driver
						SB.openNewBlankTab(activeWebDriver);
						SB.switchToTab(activeWebDriver, 2);
						activeWebDriver.get(CONFIG.getProperty("qa_support_tool_site"));
						SB.waitForPageLoaded(activeWebDriver);
					}
				}catch(Exception e) {
						activeWebDriver.quit();
						continue;
				}
				
				//resetting application
				System.out.println("This is Support Base reset application");
				try {
					SB.switchToWindowTitleContains(SupportAppTitle, activeWebDriver);
					if (SB.isElementVisible(activeWebDriver, By.cssSelector("[data-action*=login]"), 1)) {
						SB.switchToWindowTitleContains(SoftphonePageTitle, activeWebDriver);
						SB.reloadSoftphone(activeWebDriver);
						loginSoftPhoneAccToDriver(webDriverString);
						SB.switchToWindowTitleContains(SupportAppTitle, activeWebDriver);
						activeWebDriver.get(CONFIG.getProperty("qa_support_tool_site"));
						SB.waitForPageLoaded(activeWebDriver);
					} else {
						activeWebDriver.get(CONFIG.getProperty("qa_support_tool_site"));
						SB.waitForPageLoaded(activeWebDriver);
					}
					SB.closeRecommendationWindow(activeWebDriver);
				} catch (Exception e) {
					activeWebDriver.quit();
					activeWebDriver = null;
				}
			}
		}
	}
	
	/**reset method to re login according to driver passed on softphone 
	 * @param driverString
	 */
	public void loginSoftPhoneAccToDriver(String driverString) {

		String username = "";
		String password = "";

		switch (driverString) {
		case "supportDriver":
			switch (drivername) {
			case "adminDriver":
				username = CONFIG.getProperty("qa_admin_user_username");
				password = CONFIG.getProperty("qa_admin_user_password");
				break;
			case "supportDriver":
				username = CONFIG.getProperty("qa_support_user_username");
				password = CONFIG.getProperty("qa_support_user_password");
				break;
			}
			
			System.out.println("drivername = "+ drivername);
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), username, password);
			break;
		case "adminDriver":
			SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
			break;
		case "agentDriver":
			SFLP.softphoneLogin(agentDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
			break;
		case "webSupportDriver":
			SFLP.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
			break;
		case "caiCallerDriver":
			SFLP.softphoneLogin(caiCallerDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_caller_username"), CONFIG.getProperty("qa_cai_caller_password"));
			break;
		case "caiVerifyDriver":
			SFLP.softphoneLogin(caiVerifyDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_verify_username"), CONFIG.getProperty("qa_cai_verify_password"));
			break;
		case "caiDriver1":
			SFLP.softphoneLogin(caiDriver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_user_1_username"), CONFIG.getProperty("qa_cai_user_1_password"));
			break;
		case "caiDriver2":
			SFLP.softphoneLogin(caiDriver2, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_user_2_username"), CONFIG.getProperty("qa_cai_user_2_password"));
			break;
		}
	}
	
	/**reset method to re login according to driver passed on web app 
	 * @param driverString
	 */
	public void loginSupportLoginAccToDriver(String driverString) {
		
		String username = "";
		String password = "";
		
		switch (driverString) {
		case "supportDriver":
			switch(drivername) {
			case "adminDriver":
				username = CONFIG.getProperty("qa_admin_user_username");
				password = CONFIG.getProperty("qa_admin_user_password");
				break;
			case "supportDriver":
				username = CONFIG.getProperty("qa_support_user_username");
				password = CONFIG.getProperty("qa_support_user_password");
				break;
			case "agentDriver":
				username = CONFIG.getProperty("qa_agent_user_username");
				password = CONFIG.getProperty("qa_agent_user_password");
				break;
			}
			
			System.out.println("drivername = "+ drivername);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"),
					username, password);
			break;
		case "adminDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(adminDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
			break;
		case "agentDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(agentDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
			break;
		case "webSupportDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(webSupportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
			break;
		case "caiCallerDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiCallerDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_caller_username"), CONFIG.getProperty("qa_cai_caller_password"));
			break;
		case "caiVerifyDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiVerifyDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_verify_username"), CONFIG.getProperty("qa_cai_verify_password")); 	
			break;
		case "caiDriver1":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiDriver1, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_user_1_username"), CONFIG.getProperty("qa_cai_user_1_password")); 	
			break;
		case "caiDriver2":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiDriver2, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_user_2_username"), CONFIG.getProperty("qa_cai_user_2_password"));
			break;
		}
	}
	
	public void setMapDriverWithString() {
		webDriverMap.put("supportDriver", supportDriver);
		webDriverMap.put("webSupportDriver", webSupportDriver);
		webDriverMap.put("adminDriver", adminDriver);
		webDriverMap.put("agentDriver", agentDriver);
		webDriverMap.put("caiCallerDriver", caiCallerDriver);
		webDriverMap.put("caiVerifyDriver", caiVerifyDriver);
		webDriverMap.put("caiDriver2", caiDriver2);
		webDriverMap.put("caiDriver1", caiDriver1);
	}
	
	public void supportLogout(WebDriver driver) {
		SB.waitUntilVisible(driver, userDropdownPullDown);
		SB.scrollToElement(driver, userDropdownPullDown);
		SB.clickElement(driver, userDropdownPullDown);
		SB.waitUntilVisible(driver, logoutBtn);
		SB.clickElement(driver, logoutBtn);
	}

	@Parameters({"drivername"})
	@BeforeTest(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void setDriverName(@Optional() String driverType) {
		drivername = driverType;
	}
	
	/**
	 * This method is to initialize support driver setup according to different drivers reading from xml suite
	 */
	public void initializeSupport() {
		if ((drivername.equals("supportDriver")) && (supportDriver == null  || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(supportDriver);
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password")); 
			dashboard.assertDashBoardForSupportUser(supportDriver);
			accountsPage.searchAndNavigateToAccount(supportDriver, CONFIG.getProperty("qa_user_account"), CONFIG.getProperty("qa_user_account_salesforrce_id"));
			sfHomePge.openCampaignAndSwitchToClassicSF(supportDriver);
		} else if ((drivername.equals("adminDriver")) && (supportDriver == null   || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(supportDriver);
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password")); 	
			dashboard.assertDashBoardForAdminUser(supportDriver);
			sfHomePge.openCampaignAndSwitchToClassicSF(supportDriver);
		} else if ((drivername.equals("agentDriver")) && (supportDriver == null   || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(supportDriver);
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		}
	}
	
	/**
	 * This method is to initialize support driver setup according to different drivers reading from xml suite
	 */
	public void initializeSupport(String driver) {
		if ((driver.equals("webSupportDriver")) && (webSupportDriver == null  || webSupportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			webSupportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(webSupportDriver);
			SB.openNewBlankTab(webSupportDriver);
			SB.switchToTab(webSupportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(webSupportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password")); 
			dashboard.assertDashBoardForSupportUser(webSupportDriver);
			accountsPage.searchAndNavigateToAccount(webSupportDriver, CONFIG.getProperty("qa_user_account"), CONFIG.getProperty("qa_user_account_salesforrce_id"));
			sfHomePge.openCampaignAndSwitchToClassicSF(webSupportDriver);
		} else if ((driver.equals("adminDriver")) && (adminDriver == null || adminDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			adminDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(adminDriver);
			SB.openNewBlankTab(adminDriver);
			SB.switchToTab(adminDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(adminDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password")); 	
			dashboard.assertDashBoardForAdminUser(adminDriver);
			sfHomePge.openCampaignAndSwitchToClassicSF(adminDriver);
		} else if ((driver.equals("agentDriver")) && (agentDriver == null || agentDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			agentDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(agentDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(agentDriver);
			SB.openNewBlankTab(agentDriver);
			SB.switchToTab(agentDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(agentDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_agent_user_username"), CONFIG.getProperty("qa_agent_user_password"));
		} else if ((driver.equals("caiCallerDriver")) && (caiCallerDriver == null || caiCallerDriver.toString().contains("(null)"))) {
			String prodEnv = System.getProperty("environment");
			if(prodEnv != null && System.getProperty("environment").equals("Prod")) {
				if(caiDriver1 == null)
					initializeSupport("caiDriver1");
				caiCallerDriver = caiDriver1;
				setMapDriverWithString();
				return;
			}
			
			System.out.println("First browser is opening"); caiCallerDriver =
			getDriver(); 
			setMapDriverWithString(); 
			SFLP.softphoneLogin(caiCallerDriver,
			CONFIG.getProperty("qa_test_site_name"),
			CONFIG.getProperty("qa_cai_caller_username"),
			CONFIG.getProperty("qa_cai_caller_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(caiCallerDriver);
			SB.openNewBlankTab(caiCallerDriver); 
			SB.switchToTab(caiCallerDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiCallerDriver,
			CONFIG.getProperty("qa_support_tool_site"),
			CONFIG.getProperty("qa_cai_caller_username"),
			CONFIG.getProperty("qa_cai_caller_password"));
			sfHomePge.openCampaignAndSwitchToClassicSF(caiCallerDriver);
			 
		} else if ((driver.equals("caiVerifyDriver")) && (caiVerifyDriver == null || caiVerifyDriver.toString().contains("(null)"))) {
			String prodEnv = System.getProperty("environment");
			if(prodEnv != null && System.getProperty("environment").equals("Prod")) {
				if (caiDriver2 == null)
					initializeSupport("caiDriver2");
				caiVerifyDriver = caiDriver2;
				setMapDriverWithString();
				return;
			}

			caiVerifyDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(caiVerifyDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_verify_username"), CONFIG.getProperty("qa_cai_verify_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(caiVerifyDriver);
			SB.openNewBlankTab(caiVerifyDriver);
			SB.switchToTab(caiVerifyDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiVerifyDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_verify_username"), CONFIG.getProperty("qa_cai_verify_password"));
			sfHomePge.openCampaignAndSwitchToClassicSF(caiVerifyDriver);
			 
		} else if ((driver.equals("caiDriver2")) && (caiDriver2 == null || caiDriver2.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			caiDriver2 = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(caiDriver2, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_user_2_username"), CONFIG.getProperty("qa_cai_user_2_password"));
			//softPhoneSettingsPage.setDefaultSoftPhoneSettings(caiDriver2);
			SB.openNewBlankTab(caiDriver2);
			SB.switchToTab(caiDriver2, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiDriver2, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_user_2_username"), CONFIG.getProperty("qa_cai_user_2_password"));
			sfHomePge.openCampaignAndSwitchToClassicSF(caiDriver2);
		} else if ((driver.equals("caiDriver1")) && (caiDriver1 == null || caiDriver1.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			caiDriver1 = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(caiDriver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_user_1_username"), CONFIG.getProperty("qa_cai_user_1_password"));
			//softPhoneSettingsPage.setDefaultSoftPhoneSettings(caiDriver1);
			SB.openNewBlankTab(caiDriver1);
			SB.switchToTab(caiDriver1, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiDriver1, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_user_1_username"), CONFIG.getProperty("qa_cai_user_1_password"));
			sfHomePge.openCampaignAndSwitchToClassicSF(caiDriver1);
		} else if ((driver.equals("caiSupportDriver")) && (caiSupportDriver == null || caiSupportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			caiSupportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(caiSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_cai_support_username"), CONFIG.getProperty("qa_cai_support_password"));
			softPhoneSettingsPage.setDefaultSoftPhoneSettings(caiSupportDriver);
			SB.openNewBlankTab(caiSupportDriver);
			SB.switchToTab(caiSupportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(caiSupportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_cai_support_username"), CONFIG.getProperty("qa_cai_support_password"));
			sfHomePge.openCampaignAndSwitchToClassicSF(caiSupportDriver);
		}
	}
	
	//@AfterMethod(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void afterMethod() {

		i++;
		if (i % 10 == 0) {
			for (Entry<String, WebDriver> entry : webDriverMap.entrySet()) {
				String webDriverString = entry.getKey();
				WebDriver activeWebDriver = entry.getValue();
				if (activeWebDriver!=null) {
					System.out.println("Reset application by closing browser after " + i + " iterations");
					driverUsed.put(webDriverString, true);
					resetApplication();
				}
			}
		}
	}
}
