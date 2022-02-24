package report.base;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.util.Strings;

import base.SeleniumBase;
import base.TestBase;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.salesforce.SalesforceHomePage;
import softphone.source.salesforce.salesforceCampaign;
import support.source.accounts.AccountsPage;
import support.source.commonpages.Dashboard;

public class ReportBase extends TestBase{
	SeleniumBase SB = new SeleniumBase();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	AccountsPage accountsPage = new AccountsPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	salesforceCampaign sfCampaign = new salesforceCampaign();
	SalesforceHomePage sfHomePge = new SalesforceHomePage();
	
	public static String drivername = null;
	public static HashMap<String, WebDriver> webDriverMap = new HashMap<>();
	
	By userDropdownPullDown = By.cssSelector(".user-profile.dropdown-toggle span.fa.fa-angle-down");
	By logoutBtn			= By.cssSelector("a[data-action='logout']");
	
	String environment = System.getProperty("environment");

	public static String agentNameValid;
	public static String teamNameValid;
	public static String locationNameValid;
	
	private static String userNameForSessionId;
	private static String passwordForSessionId;
	private static String securityKeyForSessionId;
	
	
	@BeforeClass(groups = { "Regression", "MediumPriority" })
	public void beforeClassReportBase() {
		if (environment != null && environment.equals("RingDNAProd")) {
			agentNameValid = System.getProperty("validAgentName");
			teamNameValid = System.getProperty("validTeamName");
			locationNameValid = System.getProperty("validLocationName");
			userNameForSessionId = "ringdnatester+qa@ringdna.com";
			passwordForSessionId = "Ringdna123";
			//securityKeyForSessionId = System.getProperty("securityToken");
		} else {
			agentNameValid = CONFIG.getProperty("qa_report_valid_user_name");
			teamNameValid = CONFIG.getProperty("qa_report_valid_team_name");
			locationNameValid = CONFIG.getProperty("qa_report_valid_location_name");
		}
	}
	
	/* (non-Javadoc)
	 * @see base.TestBase#resetApplication()
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
						
						// opening support driver
						SB.openNewBlankTab(activeWebDriver);
						SB.switchToTab(activeWebDriver, 2);
						activeWebDriver.get(CONFIG.getProperty("qa_support_tool_site"));
						SB.waitForPageLoaded(activeWebDriver);
					}
				}catch(Exception e) {
					activeWebDriver.quit();
					continue;
				}
				
				// resetting application in case of session ids
				
				if (environment != null && environment.equals("RingDNAProd")) {
					System.out.println("This is Support Base reset application using session id");
					//String sessionid = getSessionId();
					try {
						SB.switchToTab(activeWebDriver, 2);
						if (SB.isElementVisible(activeWebDriver, By.cssSelector("[data-action*=login]"), 1)) {
							SB.switchToTab(activeWebDriver, 1);
							SB.reloadSoftphone(activeWebDriver); // softphone login with session id
							SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), userNameForSessionId, passwordForSessionId);
							SB.openNewBlankTab(supportDriver);
							SB.switchToTab(supportDriver, 2);
							softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), userNameForSessionId, passwordForSessionId); 	
							
							//SFLP.softphoneLoginWithSessionId(activeWebDriver, CONFIG.getProperty("qa_test_site_name"), sessionid);
							//SB.switchToTab(activeWebDriver, 2); // support login with session id
							//SFLP.supportLoginWithSessionId(activeWebDriver, CONFIG.getProperty("qa_support_tool_site"), sessionid);
							//SB.waitForPageLoaded(activeWebDriver);
						} else {
							//SFLP.supportLoginWithSessionId(activeWebDriver, CONFIG.getProperty("qa_support_tool_site"), sessionid);
							softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), userNameForSessionId, passwordForSessionId); 	
							SB.waitForPageLoaded(activeWebDriver);
						}
						SB.closeRecommendationWindow(activeWebDriver);
					} catch (Exception e) {
						activeWebDriver.quit();
						activeWebDriver = null;
					}
				}
				  // resetting application in case of qa and other production 
				  else {
				 
					System.out.println("This is Support Base reset application");
					try {
						SB.switchToTab(activeWebDriver, 2);
						if (SB.isElementVisible(activeWebDriver, By.cssSelector("[data-action*=login]"), 1)) {
							SB.switchToTab(activeWebDriver, 1);
							SB.reloadSoftphone(activeWebDriver);
							loginSoftPhoneAccToDriver(webDriverString);
							SB.switchToTab(activeWebDriver, 2);
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
	}

	/**This method is to get session Id by passing username, password, security token
	 * @return
	 */
	public static String getSessionId() {
		String sessionId = null;
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://app.ringdna.com/api/v2/session/authenticateUsernamePassword" + "?username="
							+ userNameForSessionId + "&password="
							+ passwordForSessionId + securityKeyForSessionId)
					.method("POST", body).build();

			JSONObject Jobject = null;
			Response response;
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
			Jobject = new JSONObject(jsonData);
			sessionId = Jobject.get("sessionId").toString();
			assertTrue(Strings.isNotNullAndNotEmpty(sessionId), "Session ID is null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionId;
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
				username = CONFIG.getProperty("qa_report_admin_user_username");
				password = CONFIG.getProperty("qa_report_admin_user_password");
				break;
			case "supportDriver":
				username = CONFIG.getProperty("qa_report_support_user_username");
				password = CONFIG.getProperty("qa_report_support_user_password");
				break;
			}
			
			System.out.println("drivername = "+ drivername);
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), username, password);
			break;
		case "adminDriver":
			SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"),
					CONFIG.getProperty("qa_report_admin_user_username"),
					CONFIG.getProperty("qa_report_admin_user_password"));
			break;
		case "webSupportDriver":
			SFLP.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"),
					CONFIG.getProperty("qa_report_support_user_username"),
					CONFIG.getProperty("qa_report_support_user_password"));
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
			switch (drivername) {
			case "adminDriver":
				username = CONFIG.getProperty("qa_report_admin_user_username");
				password = CONFIG.getProperty("qa_report_admin_user_password");
				break;
			case "supportDriver":
				username = CONFIG.getProperty("qa_report_support_user_username");
				password = CONFIG.getProperty("qa_report_support_user_password");
				break;
			}
			
			System.out.println("drivername = "+ drivername);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"),
					username, password);
			break;

		case "adminDriver":
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(adminDriver, CONFIG.getProperty("qa_support_tool_site"),
					CONFIG.getProperty("qa_report_admin_user_username"),
					CONFIG.getProperty("qa_report_admin_user_password"));
			break;
		case "webSupportDriver":
			SFLP.supportLoginWhenSoftphoneLogin(webSupportDriver, CONFIG.getProperty("qa_support_tool_site"),
					CONFIG.getProperty("qa_report_support_user_username"),
					CONFIG.getProperty("qa_report_support_user_password"));
			break;
		}
	}
	
	public void setMapDriverWithString() {
		webDriverMap.put("supportDriver", supportDriver);
		webDriverMap.put("adminDriver", adminDriver);
		webDriverMap.put("webSupportDriver", webSupportDriver);
	}
	
	public void supportLogout(WebDriver driver) {
		SB.waitUntilVisible(driver, userDropdownPullDown);
		SB.scrollToElement(driver, userDropdownPullDown);
		SB.clickElement(driver, userDropdownPullDown);
		SB.waitUntilVisible(driver, logoutBtn);
		SB.clickElement(driver, logoutBtn);
	}

	@Parameters({"drivername"})
	@BeforeTest(groups = { "Regression", "MediumPriority"})
	public void setDriverName(@Optional() String driverType) {
		drivername = driverType;
	}
	
	/**
	 * This method is to initialize support driver setup according to different drivers reading from xml suite
	 * @throws Exception 
	 */
	public void initializeSupport() {
		if ((drivername.equals("supportDriver")) && (supportDriver == null || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_report_support_user_username"), CONFIG.getProperty("qa_report_support_user_password"));
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_report_support_user_username"), CONFIG.getProperty("qa_report_support_user_password")); 
			sfHomePge.openCampaignAndSwitchToClassicSF(supportDriver);
		} else if ((drivername.equals("adminDriver")) && (supportDriver == null || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_report_admin_user_username"), CONFIG.getProperty("qa_report_admin_user_password"));
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_report_admin_user_username"), CONFIG.getProperty("qa_report_admin_user_password")); 	
			sfHomePge.openCampaignAndSwitchToClassicSF(supportDriver);
		} else if ((drivername.equals("ringDNAProdDriver")) && (supportDriver == null || supportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			supportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(supportDriver, CONFIG.getProperty("qa_test_site_name"), userNameForSessionId, passwordForSessionId);
			SB.openNewBlankTab(supportDriver);
			SB.switchToTab(supportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(supportDriver, CONFIG.getProperty("qa_support_tool_site"), userNameForSessionId, passwordForSessionId); 	
			/*
			 * //getting session id String sessionid = getSessionId(); //softphone login
			 * with session id SFLP.softphoneLoginWithSessionId(supportDriver,
			 * CONFIG.getProperty("qa_test_site_name"), sessionid);
			 * SB.openNewBlankTab(supportDriver); SB.switchToTab(supportDriver, 2); //web
			 * app login with session id
			 * softPhoneLoginPage.supportLoginWithSessionId(supportDriver,
			 * CONFIG.getProperty("qa_support_tool_site"), sessionid);
			 */	
		}
	}
	
	/**Method to launch webdriver without passing driver from xml
	 * @param driver
	 */
	public void initializeSupport(String driver) {
		if ((driver.equals("webSupportDriver")) && (webSupportDriver == null || webSupportDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			webSupportDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_report_support_user_username"), CONFIG.getProperty("qa_report_support_user_password"));
			SB.openNewBlankTab(webSupportDriver);
			SB.switchToTab(webSupportDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(webSupportDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_report_support_user_username"), CONFIG.getProperty("qa_report_support_user_password")); 
		} else if ((driver.equals("adminDriver")) && (adminDriver == null || adminDriver.toString().contains("(null)"))) {
			System.out.println("First browser is opening");
			adminDriver = getDriver();
			setMapDriverWithString();
			SFLP.softphoneLogin(adminDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_report_admin_user_username"), CONFIG.getProperty("qa_report_admin_user_password"));
			SB.openNewBlankTab(adminDriver);
			SB.switchToTab(adminDriver, 2);
			softPhoneLoginPage.supportLoginWhenSoftphoneLogin(adminDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_report_admin_user_username"), CONFIG.getProperty("qa_report_admin_user_password")); 	
		}
	}
}