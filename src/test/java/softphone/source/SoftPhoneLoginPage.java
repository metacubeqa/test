package softphone.source;

import static org.testng.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import report.base.ReportBase;

public class SoftPhoneLoginPage extends SeleniumBase {

	int counter = 0;
	int sessionCounter = 0;
	
	//*******Login Page locators starts here*******//
	By salesforceLoginButton	= By.cssSelector("[data-action='salesforce-login']");
	By selectConnection 		= By.cssSelector(".connection");
	By ringDnaLogo              = By.cssSelector("#login .rdna-logo");
	By ringdnaWelcomeMessage    = By.cssSelector(".welcome");
	By loginFooter              = By.cssSelector("#footer");
	By userNameTextBox 			= By.id("username");
	By passwordTextBox			= By.id("password");
	By loginButton 				= By.id("Login");
	By logoutButton 			= By.cssSelector("button[data-action = 'logout']");
	By dialPadImage 			= By.cssSelector("img.keypad");
	//*******Login Page locators ends here*******//
	
	//*******Login error locators starts here*******//
	By logoutErrorMessage 		= By.cssSelector("a[data-action = 'logout']");
	By forcedLogoutParagraph 	= By.cssSelector(".forced-logout p");
	By forcedLogoutBtn 			= By.cssSelector(".forced-logout button");
	By loginError 				= By.xpath(".//*[@class='loginError' and text() = 'To access this page, you have to log in to Salesforce.']");
	String forcedLogoutMsg 		= "You have been logged out by $userName$.";
	//*******Login error locators ends here*******//
	
	/**	
	 * web app connection types	
	 *	
	 */	
	public static enum supportConnectionTypes{	
		production,	
		sandbox;	
	}
	
	//This method click the login button appears on default page
	public void clickLoginPageButton(WebDriver driver) {
		clickElement(driver, salesforceLoginButton);
	}

	//This method enter's User Name into the User Name text box
	public void enterUserName(WebDriver driver, String userName) {
		enterText(driver, userNameTextBox, userName);
	}

	//This method enter's password into the the password box
	public void enterPassword(WebDriver driver, String password) {
		enterText(driver, passwordTextBox, password);
	}

	//This method clicks on login button
	public void clickLoginButton(WebDriver driver) {
		clickElement(driver, loginButton);
	}

	//This method login into the softphone application 
	public void softphoneLogin(WebDriver driver, String siteName, String userName, String password) {
		driver.get(siteName);
		waitUntilVisible(driver, ringDnaLogo);
		waitUntilVisible(driver, ringdnaWelcomeMessage);
		assertNotNull(getElementsText(driver, ringdnaWelcomeMessage));
		clickLoginPageButton(driver);
		waitUntilVisible(driver, loginFooter);
		assertNotNull(getElementsText(driver, loginFooter));
		enterUserName(driver, userName);
		enterPassword(driver, password);
		clickLoginButton(driver);
		checkSessionExpireError(driver, siteName, userName, password);
		//checkLoginErrors(driver, siteName, userName, password);
	}
	
	/**	
	 * method to login web app with connection mentioned	
	 * @param driver	
	 * @param siteName	
	 * @param userName	
	 * @param password	
	 */	
	public void supportLoginWithConnection(WebDriver driver, supportConnectionTypes connection, String siteName, String userName, String password) {	
		driver.get(siteName);	
		selectSupportConnection(driver, connection);	
		clickLoginPageButton(driver);	
		enterUserName(driver, userName);	
		enterPassword(driver, password);	
		clickLoginButton(driver);	
		checkSessionExpireErrorSupport(driver, siteName, userName, password);	
		closeRecommendationWindow(driver);	
	}
	
	//This method login into the softphone application with connection 
	public void softphoneLoginWithConnection(WebDriver driver, String siteName, supportConnectionTypes connection, String userName, String password) {
		driver.get(siteName);	
		selectSupportConnection(driver, connection);	
		clickLoginPageButton(driver);	
		enterUserName(driver, userName);	
		enterPassword(driver, password);	
		clickLoginButton(driver);	
		checkSessionExpireError(driver, siteName, userName, password);
		checkLoginErrors(driver, siteName, userName, password);
	}
	
	/**	
	 * method to select the web app connection type	
	 * @param driver	
	 */	
	public void selectSupportConnection(WebDriver driver, supportConnectionTypes connection) {	
		waitUntilVisible(driver, selectConnection);	
		selectFromDropdown(driver, selectConnection, SelectTypes.value, connection.name());	
		assertEquals(getSelectClassDefaultValue(driver, selectConnection), connection.name());	
	}	
	
	/**
	 *This method login into the softphone application using session id 
	 * @param driver
	 * @param siteName
	 * @param sessionId
	 */
	public void softphoneLoginWithSessionId(WebDriver driver, String siteName, String sessionId) {
		driver.get(siteName+"/#sessionId="+sessionId+"&newUser=false");
		waitForPageLoaded(driver);
		isElementVisible(driver, spinnerWheel, 1);
		waitUntilInvisible(driver, spinnerWheel);
		checkSessionExpireErrorSessionId(driver, siteName, sessionId);
//		checkLoginErrors_SessionId(driver, siteName, sessionId);
	}

	//This method logs out user from softphone
	public void softphoneLogout(WebDriver driver) {
		waitUntilVisible(driver, logoutButton);
		clickElement(driver, logoutButton);
		waitUntilVisible(driver, loginButton);
	}

	public void checkSessionExpireError(WebDriver driver, String siteName, String userName, String password) {
		if (isElementVisible(driver, loginError, 2)
				|| isElementVisible(driver, By.xpath(".//*[text()='OAuth Error']"), 0)) {
			softphoneLogin(driver, siteName, userName, password);
		}
	}
	
	/**This method checks session expire error and re logins using session id
	 * @param driver
	 * @param siteName
	 * @param sessionId
	 */
	public void checkSessionExpireErrorSessionId(WebDriver driver, String siteName, String sessionId) {
		while ((isElementVisible(driver, loginError, 2)
				|| isElementVisible(driver, By.xpath(".//*[text()='OAuth Error']"), 0)
				|| isElementVisible(driver, salesforceLoginButton, 2)) && counter < 2) {

			counter++;
			softphoneLoginWithSessionId(driver, siteName, sessionId);
		}

		if (counter >= 2 && isElementVisible(driver, salesforceLoginButton, 2 ) && sessionCounter < 2) {
			sessionId = ReportBase.getSessionId();
			counter = 0;
			sessionCounter++;
			checkSessionExpireErrorSessionId(driver, siteName, sessionId);
		}
	}
	
	public void supportLoginWhenSoftphoneLogin(WebDriver driver, String siteName, String userName, String password) {
		driver.get(siteName);
		if (getWebelementIfExist(driver, salesforceLoginButton) != null
				&& getWebelementIfExist(driver, salesforceLoginButton).isDisplayed()) {
			clickLoginPageButton(driver);
			enterUserName(driver, userName);
			enterPassword(driver, password);
			clickLoginButton(driver);
		}
		closeRecommendationWindow(driver);
	}
	
	/**This method login into the web app application using session id
	 * @param driver
	 * @param siteName
	 * @param sessionId
	 */
	public void supportLoginWithSessionId(WebDriver driver, String siteName, String sessionId) {
		driver.get(siteName);
		if (getWebelementIfExist(driver, salesforceLoginButton) != null
				&& getWebelementIfExist(driver, salesforceLoginButton).isDisplayed()) {
			driver.get(siteName+"/#sessionId="+sessionId+"&newUser=false");
		}
		closeRecommendationWindow(driver);
	}

	public void supportLogin(WebDriver driver, String siteName, String userName, String password) {
		driver.get(siteName);
		clickLoginPageButton(driver);
		enterUserName(driver, userName);
		enterPassword(driver, password);
		clickLoginButton(driver);
		checkSessionExpireErrorSupport(driver, siteName, userName, password);
		closeRecommendationWindow(driver);
	}

	public void checkSessionExpireErrorSupport(WebDriver driver, String siteName, String userName, String password) {
		if (isElementVisible(driver, loginError, 2)) {
			supportLogin(driver, siteName, userName, password);
		}
	}

	public void checkLoginErrors(WebDriver driver, String siteName, String userName, String password) {
		try {

			int refreshCount = 0;
			while (((getWebelementIfExist(driver, dialPadImage) == null)
					|| (getWebelementIfExist(driver, logoutErrorMessage) != null))
					&& (getWebelementIfExist(driver, salesforceLoginButton) == null) && (counter < 6)) {
				if ((getWebelementIfExist(driver, logoutErrorMessage) != null)) {
					if (refreshCount == 1)
						break;
					System.out.println("Login again error has occured, refreshing the page");
					reloadSoftphone(driver);
					refreshCount = 1;
				}
				counter++;
				idleWait(1);
			}

			if (getWebelementIfExist(driver, logoutErrorMessage) != null) {
				findElement(driver, logoutErrorMessage).click();
				System.out.println("Login again error has occured again, login into the application again");
				if ((getWebelementIfExist(driver, dialPadImage) == null)
						&& (getWebelementIfExist(driver, salesforceLoginButton) != null) && (counter < 6)) {
					counter++;
					softphoneLogin(driver, siteName, userName, password);
				}
			}

			findElement(driver, dialPadImage);
			counter = 0;
		} catch (Exception e) {
			org.testng.Assert.fail("Test Fail while handling Login error for Exception:" + e);
		}
	}

	/**Method to check login errors and re login using session id
	 * @param driver
	 * @param siteName
	 * @param userName
	 * @param password
	 */
	public void checkLoginErrors_SessionId(WebDriver driver, String siteName, String sessionId) {
		try {

			int refreshCount = 0;
			while (((getWebelementIfExist(driver, dialPadImage) == null)
					|| (getWebelementIfExist(driver, logoutErrorMessage) != null))
					&& (getWebelementIfExist(driver, salesforceLoginButton) == null) && (counter < 6)) {
				if ((getWebelementIfExist(driver, logoutErrorMessage) != null)) {
					if (refreshCount == 1)
						break;
					System.out.println("Login again error has occured, refreshing the page");
					reloadSoftphone(driver);
					refreshCount = 1;
				}
				counter++;
				idleWait(1);
			}

			if (getWebelementIfExist(driver, logoutErrorMessage) != null) {
				findElement(driver, logoutErrorMessage).click();
				System.out.println("Login again error has occured again, login into the application again");
				if ((getWebelementIfExist(driver, dialPadImage) == null)
						&& (getWebelementIfExist(driver, salesforceLoginButton) != null) && (counter < 6)) {
					counter++;
					softphoneLoginWithSessionId(driver, siteName, sessionId);
				}
			}

			findElement(driver, dialPadImage);
			counter = 0;
		} catch (Exception e) {
			org.testng.Assert.fail("Test Fail while handling Login error for Exception:" + e);
		}
	}
	public void verifyForcedLogout(WebDriver driver, String userName) {
		assertTrue(isElementVisible(driver, forcedLogoutParagraph, 5));
		assertTrue(isElementVisible(driver, forcedLogoutBtn, 5));
		assertEquals(getElementsText(driver, forcedLogoutParagraph), forcedLogoutMsg.replace("$userName$", userName));
		clickElement(driver, forcedLogoutBtn);
		assertTrue(isElementVisible(driver, salesforceLoginButton, 5));
		assertFalse(isElementVisible(driver, forcedLogoutParagraph, 5));
		assertFalse(isElementVisible(driver, forcedLogoutBtn, 5));
	}
	
	public void verifyLoginBtnVisible(WebDriver driver) {
		waitUntilVisible(driver, salesforceLoginButton);
	}
}
