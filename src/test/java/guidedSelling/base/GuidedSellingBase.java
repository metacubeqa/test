package guidedSelling.base;

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
import softphone.source.SoftPhoneLoginPage.supportConnectionTypes;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.SalesforceTestLoginPage;

/**
 * @author AbhishekT
 
 */

public class GuidedSellingBase extends TestBase {

	SeleniumBase SB = new SeleniumBase();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	SalesforceTestLoginPage sfTestPage = new SalesforceTestLoginPage();
	SalesforceHomePage sfHomePge = new SalesforceHomePage();

	public static HashMap<String, WebDriver> webDriverMap = new HashMap<>();

	String userName;
	String userPassword;
	supportConnectionTypes connectionType;

	public static String drivername = null;

	private final static String SoftphonePageTitle = "ringDNA (qa)";

	// parameters to pass through Jenkins
	// Jenkins run time params
	public static String salesForceEnvironment    = System.getProperty("salesForceEnv");
	public static String salesForceModeRun        = System.getProperty("salesForceMode"); 

	/*
	 * (non-Javadoc)
	 * 
	 * @see base.TestBase#resetApplication()
	 * 
	 * This method resets application for failed test cases
	 */
	@Override
	public void resetApplication() {

		for (Entry<String, WebDriver> entry : webDriverMap.entrySet()) {
			String webDriverString = entry.getKey();
			WebDriver activeWebDriver = entry.getValue();

			if (((Boolean) driverUsed.get(webDriverString)).booleanValue()) {

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

						// opening salesforce test login
						SB.openNewBlankTab(activeWebDriver);
						SB.switchToTab(activeWebDriver, 2);
						activeWebDriver.get(CONFIG.getProperty("gs_salesforce_url"));
						SB.waitForPageLoaded(activeWebDriver);
					}
				} catch (Exception e) {
					activeWebDriver.quit();
					continue;
				}

				// resetting application
				System.out.println("This is reset application");
				try {
					SB.switchToWindowTitleContains(SoftphonePageTitle, activeWebDriver);
					if (SB.isElementVisible(activeWebDriver, By.cssSelector("[data-action*=login]"), 1)) {
						switch (webDriverString) {
						case "webSupportDriver":
							userName = CONFIG.getProperty("qa_support_user_username");
							userPassword = CONFIG.getProperty("qa_support_user_password");
							break;
						case "adminDriver":
							userName = CONFIG.getProperty("qa_admin_user_username");
							userPassword = CONFIG.getProperty("qa_admin_user_password");
							break;
						}

						switch (salesForceEnvironment) {
						case "Sandbox":
						case "Patch":
							connectionType = supportConnectionTypes.sandbox;
							break;
						case "Production":
							connectionType = supportConnectionTypes.production;
						}

						SB.reloadSoftphone(activeWebDriver);
						SFLP.softphoneLoginWithConnection(activeWebDriver, CONFIG.getProperty("qa_test_site_name"),
								connectionType, userName, userPassword);
						SB.switchToTab(activeWebDriver, 2);
						sfTestPage.salesForceTestLogin(activeWebDriver, CONFIG.getProperty("gs_salesforce_url"),
								userName, userPassword);
						
						// change mode
						changeModeSF(activeWebDriver);
					}
					SB.switchToTab(activeWebDriver, 2);
				} catch (Exception e) {
					activeWebDriver.quit();
					activeWebDriver = null;
				}

			}
		}
	}

	@Parameters({ "drivername" })
	@BeforeTest(groups = { "Regression", "Sanity" })
	public void setDriverName(@Optional() String driverType) {
		drivername = driverType;
	}

	/**
	 * Mapping driver value with its string value in <key,value> format
	 */
	public void setMapDriverWithString() {
		webDriverMap.put("webSupportDriver", webSupportDriver);
		webDriverMap.put("adminDriver", adminDriver);
		webDriverMap.put("qaAdminDriver", qaAdminDriver);
	}

	/**
	 * @param driver
	 */
	public void changeModeSF(WebDriver driver) {

		// switching to sf mode
		sfHomePge.closeLightningPopUp(driver);
		sfHomePge.closeLightningDialogueBoxForFirstTime(driver);
		switch (salesForceModeRun) {
		case "Classic":
			sfHomePge.switchToClassicMode(driver);
			break;
		case "Lightning":
			sfHomePge.switchToLightningMode(driver);
			break;
		default:
			break;
		}
	}

	/**
	 * This method is to initialize Salesforce Test and Web App for diff drivers,
	 * connection types
	 * 
	 * @param driver
	 */
	public void initializeSalesForceTest(String driver) {
		if ((driver.equals("webSupportDriver")) && (webSupportDriver == null)) {
			System.out.println("First browser is opening");
			webSupportDriver = getDriver();
			setMapDriverWithString();

			// login web app according to connection type
			switch (salesForceEnvironment) {
			case "Sandbox":
			case "Patch":
				SFLP.softphoneLoginWithConnection(webSupportDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.sandbox, CONFIG.getProperty("qa_support_user_username"),
						CONFIG.getProperty("qa_support_user_password"));
				break;
			case "Production":
				SFLP.softphoneLoginWithConnection(webSupportDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.production, CONFIG.getProperty("qa_support_user_username"),
						CONFIG.getProperty("qa_support_user_password"));
				break;
			default:
				break;
			}

			SB.openNewBlankTab(webSupportDriver);
			SB.switchToTab(webSupportDriver, 2);
			sfTestPage.salesForceTestLogin(webSupportDriver, CONFIG.getProperty("gs_salesforce_url"),
					CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));

			// changing the SF mode
			changeModeSF(webSupportDriver);
		}

		else if ((driver.equals("adminDriver")) && (adminDriver == null)) {
			System.out.println("First browser is opening");
			adminDriver = getDriver();
			setMapDriverWithString();

			// login web app according to connection type
			switch (salesForceEnvironment) {
			case "Sandbox":
			case "Patch":
				SFLP.softphoneLoginWithConnection(adminDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.sandbox, CONFIG.getProperty("qa_admin_user_username"),
						CONFIG.getProperty("qa_admin_user_password"));
				break;
			case "Production":
				SFLP.softphoneLoginWithConnection(adminDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.production, CONFIG.getProperty("qa_admin_user_username"),
						CONFIG.getProperty("qa_admin_user_password"));
				break;
			default:
				break;
			}

			SB.openNewBlankTab(adminDriver);
			SB.switchToTab(adminDriver, 2);
			sfTestPage.salesForceTestLogin(adminDriver, CONFIG.getProperty("gs_salesforce_url"),
			CONFIG.getProperty("qa_admin_user_username"), CONFIG.getProperty("qa_admin_user_password"));

			// changing the SF mode
			changeModeSF(adminDriver);
		}
		
		else if ((driver.equals("qaAdminDriver")) && (qaAdminDriver == null)) {
			System.out.println("First browser is opening");
			qaAdminDriver = getDriver();
			setMapDriverWithString();

			// login web app according to connection type
			switch (salesForceEnvironment) {
			case "Sandbox":
			case "Patch":
				SFLP.softphoneLoginWithConnection(qaAdminDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.sandbox, CONFIG.getProperty("qa_user_username"),
						CONFIG.getProperty("qa_user_password"));
				break;
			case "Production":
				SFLP.softphoneLoginWithConnection(qaAdminDriver, CONFIG.getProperty("qa_test_site_name"),
						supportConnectionTypes.production, CONFIG.getProperty("qa_admin_user_username"),
						CONFIG.getProperty("qa_admin_user_password"));
				break;
			default:
				break;
			}

			SB.openNewBlankTab(qaAdminDriver);
			SB.switchToTab(qaAdminDriver, 2);
			sfTestPage.salesForceTestLogin(qaAdminDriver, CONFIG.getProperty("gs_salesforce_url"),
			CONFIG.getProperty("qa_user_username"), CONFIG.getProperty("qa_user_password"));

			// changing the SF mode
			changeModeSF(qaAdminDriver);
		}

	}
}
