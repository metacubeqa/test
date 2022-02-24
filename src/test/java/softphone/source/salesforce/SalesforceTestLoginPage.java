package softphone.source.salesforce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;

public class SalesforceTestLoginPage extends SeleniumBase {

	//*******Login Page locators starts here*******//
	By userNameTextBox 			= By.id("username");
	By passwordTextBox			= By.id("password");
	By loginButton 				= By.id("Login");
	By logoutButton 			= By.cssSelector("button[data-action = 'logout']");
	//*******Login Page locators ends here*******//
	
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

	public void salesForceTestLogin(WebDriver driver, String siteName, String userName, String password) {
		driver.get(siteName);
		if (getWebelementIfExist(driver, loginButton) != null
				&& getWebelementIfExist(driver, loginButton).isDisplayed()) {
			enterUserName(driver, userName);
			enterPassword(driver, password);
			clickLoginButton(driver);
			waitForPageLoaded(driver);
		}
		isSpinnerWheelInvisible(driver);
	}
}