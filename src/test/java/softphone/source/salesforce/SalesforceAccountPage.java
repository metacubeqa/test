package softphone.source.salesforce;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;

public class SalesforceAccountPage extends SeleniumBase{

	By accountNamePageHeading		= By.cssSelector("h2.topName, h2.pageDescription");
	By accountNamePageHeadingLight		=  By.xpath("//lightning-formatted-text[@class='custom-truncate']");
	By accountNameMainTitle			= By.xpath(".//*[text()='Account Detail']");
	By deleteAccountBtn				= By.cssSelector("[value='Delete']");
	By accountPageHearding			= By.xpath(".//h1[contains(text(),'Accounts')]");
	
	public void verifyAccountsNameHeading(WebDriver driver, String accountsName) {
		waitUntilVisible(driver, accountNamePageHeading);
		assertEquals(getElementsText(driver, accountNamePageHeading).trim(), accountsName);
	}
	
	public void verifyAccountsNameHeadingLight(WebDriver driver, String accountsName) {
		waitUntilVisible(driver, accountNamePageHeadingLight);
		assertEquals(getElementsText(driver, accountNamePageHeadingLight).trim(), accountsName);
	}
	
	public void verifyAccountPageOpen(WebDriver driver) {
		waitUntilVisible(driver, accountNameMainTitle);
	}
	
	public void deleteAccount(WebDriver driver) {
		clickElement(driver, deleteAccountBtn);
		acceptAlert(driver);
		waitUntilVisible(driver, accountPageHearding);
	}
}
