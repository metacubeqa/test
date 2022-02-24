package support.source.accounts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class AccountAccountsTab extends SeleniumBase{

	Dashboard dashBoard = new Dashboard();
	
	//Account account
	By licensingTab			= By.cssSelector("[data-tab='integrations']");
	By licensingTabParent   = By.xpath(".//*[@data-tab='integrations']/ancestor::li");
	By licensingTabHeading  = By.xpath("//h2[text()='Email / Calendars']");
	By accountsTab          = By.cssSelector("[data-tab='accounts']");
	
	By emailCalText         = By.xpath("//div[contains(text(), 'Manage allowed third party email and calendar services')]");
	
	By zoomViewUser         = By.cssSelector(".view-users");
	By userCount            = By.cssSelector(".user-count");
	By manageButton         = By.cssSelector(".btn.btn-primary.manage");
	By manageConnUserButton = By.cssSelector(".btn.btn-primary.pull-right.manage-nylas-users");
	By backToInterations    = By.cssSelector(".back-to-integrations");
	
	By licenceAvilable      = By.cssSelector(".show-licenses");
	
	By userTypeDropDown     = By.cssSelector("#ringdna-select");
	By ZoomuserTypeDropDown = By.cssSelector(".user-connected.form-control");
	
	//users list
	By userNameList         = By.xpath("//td[@class= 'string-cell sortable renderable']");
	By exchnageUserList     = By.xpath("//div[text()='EXCHANGE']");
	By confirmDelete        = By.xpath("//button[@data-testid='ui-dialog.primary-btn']");
	
	String accountRemoveButton  = "//a[text()='%name%']/parent::div/following-sibling::div/button[@name='remove-user']";
	
	
	// accounts toggle
	By enableGmailAccount      = By.xpath("//span[contains(text(), 'Gmail')]/parent::div/following-sibling::div//label[contains(@class, 'toggle-on')]");
	By disableGmailAccount     = By.xpath("//span[contains(text(), 'Gmail')]/parent::div/following-sibling::div//label[contains(@class, 'toggle-off')]");
	By disableContinueButton   = By.xpath("//button[text()='Continue']");
	By disableCancelButton 	   = By.xpath("//button[text()='Cancel']");
	By enableExchangeAccount   = By.xpath("//span[contains(text(), 'Exchange')]/parent::div/following-sibling::div//label[contains(@class, 'toggle-on')]");
	By disableExchangeAccount  = By.xpath("//span[contains(text(), 'Exchange')]/parent::div/following-sibling::div//label[contains(@class, 'toggle-off')]");
	
	// Account accounts tab
	
	/**
	 * @author Pranshu
	 *
	 */
	// Enum for User Type
	public static enum UserTypeEnum {
		AllUsers("All Users"), Connected("Connected"), NotConnected("Not Connected"), Gmail("Gmail"), Exchange("Exchange");

		private String value;

		UserTypeEnum(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * @Desc: open account accounts tab
	 * @param driver
	 */
	public void openAccountIntegrationsTab(WebDriver driver) {
		if (!findElement(driver, licensingTabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, licensingTab);
			clickElement(driver, licensingTab);
			dashBoard.isPaceBarInvisible(driver);
			findElement(driver, licensingTabHeading);
		}
	}
	
	public int getUserCount(WebDriver driver) {
		List<WebElement> users = getElements(driver, userCount);
		String count = users.get(0).getText().replace(" Users", "");
		return Integer.parseInt(count);
	}
	
	public void clickOnUserCount(WebDriver driver) {
		List<WebElement> users = getElements(driver, userCount);
		waitUntilVisible(driver, users.get(1));
		clickElement(driver, users.get(1));
	}
	
	public void clickOnZoomViewUser(WebDriver driver) {
		waitUntilVisible(driver, zoomViewUser);
		clickElement(driver, zoomViewUser);
	}
	
	/**
	 * @param driver
	 */
	public void clickOnManageButton(WebDriver driver) {
		waitUntilVisible(driver, manageButton);
		clickElement(driver, manageButton);
	}
	
	public void clickOnManageConnUserButton(WebDriver driver) {
		waitUntilVisible(driver, manageConnUserButton);
		clickElement(driver, manageConnUserButton);
	}
	
	public void clickOnBackToIntegration(WebDriver driver) {
		waitUntilVisible(driver, backToInterations);
		clickElement(driver, backToInterations);
	}
	
	public int getLicenseAvailableCount(WebDriver driver) {
		String count = findElement(driver, licenceAvilable).getText();
		count= count.substring(0, count.indexOf(" ")); 
		return Integer.parseInt(count);
	}
	
	public void clickOnLicenseAvailable(WebDriver driver) {
		waitUntilVisible(driver, licenceAvilable);
		clickElement(driver, licenceAvilable);
	}
	
	public void selectUserFromDropDown(WebDriver driver, UserTypeEnum text) {
		waitUntilVisible(driver, userTypeDropDown);
		clickElement(driver, userTypeDropDown);
		String option = "//li[text()= '%text%']";
		By locator = By.xpath(option.replace("%text%", text.getValue()));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param text
	 */
	public void selectZoomUserFromDropDown(WebDriver driver, UserTypeEnum text) {
		waitUntilVisible(driver, ZoomuserTypeDropDown);
		selectFromDropdown(driver, ZoomuserTypeDropDown, SelectTypes.visibleText, text.getValue());
		dashBoard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @return account list size
	 */
	public int getAccountNameListSize(WebDriver driver) {
		isListElementsVisible(driver, userNameList, 6);
		List<String> users = getTextListFromElements(driver, userNameList);
		return users.size();
	}
	
	/**
	 * @param driver
	 * @return size of exchnage user
	 */
	public int getExchnageUserListSize(WebDriver driver) {
		List<String> users = getTextListFromElements(driver, exchnageUserList);
		return users.size();
	}
	
	/**
	 * @param driver
	 */
	public void enableGmailAccount(WebDriver driver) {
		waitUntilVisible(driver, disableGmailAccount);
		clickElement(driver, disableGmailAccount);
		if(isElementVisible(driver, disableCancelButton, 6)) {
			clickElement(driver, disableCancelButton);
			dashBoard.isPaceBarInvisible(driver);
		}
		
	}
	
	/**
	 * @param driver
	 */
	public void disableGmailAccount(WebDriver driver) {
		waitUntilVisible(driver, enableGmailAccount);
		clickElement(driver, enableGmailAccount);
		if(isElementVisible(driver, disableContinueButton, 6)) {
			clickElement(driver, disableContinueButton);
			dashBoard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 */
	public void enableExchangeAccount(WebDriver driver) {
		waitUntilVisible(driver, disableExchangeAccount);
		clickElement(driver, disableExchangeAccount);
		if(isElementVisible(driver, disableCancelButton, 6)) {
			clickElement(driver, disableCancelButton);
			dashBoard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 */
	public void disableExchangeAccount(WebDriver driver) {
		waitUntilVisible(driver, enableExchangeAccount);
		clickElement(driver, enableExchangeAccount);
		if(isElementVisible(driver, disableContinueButton, 6)) {
			clickElement(driver, disableContinueButton);
			dashBoard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * remove all account
	 * @param driver
	 */
	public void removeAllAccountFromAccountsTab(WebDriver driver, String name) {
		By locator = By.xpath(accountRemoveButton.replace("%name%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, confirmDelete);
		clickElement(driver, confirmDelete);
		waitUntilInvisible(driver, locator);
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isAccountAccountsTabVisible(WebDriver driver) {
		return isElementVisible(driver, accountsTab, 6);
	}
	
	/**
	 * @param driver
	 * @return text visiblity
	 */
	public boolean isAccountIntegrationTextVisible(WebDriver driver) {
		return isElementVisible(driver, emailCalText, 10);
	}

}
