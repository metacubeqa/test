package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import softphone.source.SoftPhoneLoginPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;

public class CAIAnalyticsPage extends SeleniumBase{
	
	SoftPhoneLoginPage loginPage = new SoftPhoneLoginPage();
	DashBoardConversationAI caiDashBoard = new DashBoardConversationAI();
	Dashboard dashBoard = new Dashboard();
	
	public By welcomeCAIHeader   =  By.xpath("//*[@aria-label='[title]']/following-sibling::p[text()='Welcome to conversationAI']");
	public By loginBtn			  = By.xpath("//*[@aria-label='[title]']/following-sibling::a[text()='Login']");
	By userNameTextBox 	  = By.id("username");
	By passwordTextBox	  = By.id("password");
	By loginButton 		  = By.id("Login");
	public By userLicRequired	  = By.xpath("//h3[text()='User License Required']");
	
	By errorMsg 			= By.cssSelector(".loginError#error");
	
	By caiLogo				= By.xpath("//*[name()='svg' and @role='img']/*[name()='title' and text()='Conversation AI Logo']/..");
	By userDropdownPullDown = By.cssSelector(".user-profile.dropdown-toggle span.fa.fa-angle-down,[aria-label = 'rdna-menu-trigger']");
	By logoutBtn			= By.cssSelector("a[data-action='logout'], ul li[data-analyticsid='menu-item-Logout']");
	
	By adminSettingsLink	= By.xpath("//span[text()='Admin Settings']");
	By myCalendarLink		= By.xpath("//span[text()='My Calendar']");
	
	By overViewTabLink		= By.cssSelector(".user-tabs [data-tab='overview']");
	By calendarAvailability = By.xpath("//*[contains(@href, 'calendar/availability')]");

	/**
	 * @param driver
	 * @param userName
	 * @param password
	 */
	public void loginAnalytics(WebDriver driver, String userName, String password) {
		driver.get("https://analytics-qa.ringdna.net/");
		waitForPageLoaded(driver);
		waitUntilVisible(driver, welcomeCAIHeader);
		waitUntilVisible(driver, loginBtn);
		clickElement(driver, loginBtn);
		loginPage.enterUserName(driver, userName);
		loginPage.enterPassword(driver, password);
		loginPage.clickLoginButton(driver);
		waitForPageLoaded(driver);
	}
	
	/**
	 * @param driver
	 */
	public void verifyLoginSuccess(WebDriver driver) {
		waitUntilVisible(driver, caiLogo);
		assertTrue(caiDashBoard.navigateToCallsSection(driver));
	}

	/**
	 * @param driver
	 */
	public void logoutAnalytics(WebDriver driver) {
		waitUntilVisible(driver, userDropdownPullDown);
		clickElement(driver, userDropdownPullDown);
		waitUntilVisible(driver, logoutBtn);
		clickElement(driver, logoutBtn);
		waitUntilVisible(driver, welcomeCAIHeader);
		waitUntilVisible(driver, loginBtn);
	}
	
	/**
	 * @param driver
	 */
	public void loginAnalyticsIncorrect(WebDriver driver) {
		driver.get("https://analytics-qa.ringdna.net/");
		waitForPageLoaded(driver);
		waitUntilVisible(driver, welcomeCAIHeader);
		waitUntilVisible(driver, loginBtn);
		clickElement(driver, loginBtn);
		loginPage.enterUserName(driver, "abc");
		loginPage.enterPassword(driver, "abc");
		loginPage.clickLoginButton(driver);
		waitUntilVisible(driver, errorMsg);
	}
	
	/**
	 * @param driver
	 */
	public void navigateToUserOverview(WebDriver driver) {
		dashBoard.clickOnUserProfileDropDown(driver);
		waitUntilVisible(driver, adminSettingsLink);
		clickElement(driver, adminSettingsLink);
		switchToTab(driver, getTabCount(driver));
		waitUntilVisible(driver, overViewTabLink);
	}
	
	/**
	 * @param driver
	 */
	public void navigateToCalendarAvailability(WebDriver driver) {
		dashBoard.clickOnUserProfileDropDown(driver);
		waitUntilVisible(driver, myCalendarLink);
		clickElement(driver, myCalendarLink);
		switchToTab(driver, getTabCount(driver));
		waitUntilVisible(driver, calendarAvailability);
		assertEquals(getAttribue(driver, calendarAvailability, ElementAttributes.Class), "active");
	}
}
