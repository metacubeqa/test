package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import base.SeleniumBase;

public class AccountsPage extends SeleniumBase{	
	
	By paceBar			 		= By.cssSelector(".pace.pace-active");
	By accountsHeading 			= By.xpath("//*[@id='main']//h1[contains(text(),'Accounts')]");
	By accountsTextBox 			= By.cssSelector(".form-control.account-name");
	By serachAccountsButton 	= By.cssSelector(".btn.btn-success.search");
	By salesforceIdTextBox 		= By.cssSelector(".form-control.salesforce-id");
	By searchRestulList 		= By.cssSelector("[href*='#accounts/']");
	
	String accountNamePage 		= ".//*[contains(@href, '#accounts/') and text()='$accountName$']";
	String accountDateDeleted	= ".//*[contains(@href, '#accounts/') and text()='$accountName$']/ancestor::tr//td[contains(@class,'dateDeleted')]";
	
	public boolean isAccountsHeadingPresent(WebDriver driver){
		return findElement(driver, accountsHeading).isDisplayed();
	}
	
	public boolean isAccountsTextBoxPresent(WebDriver driver){
		return findElement(driver, accountsTextBox).isDisplayed();
	}
	
	public boolean isSearchAccountsButtonPresent(WebDriver driver){
		return findElement(driver, serachAccountsButton).isDisplayed();
	}

	public void enterAccountsName(WebDriver driver, String accountName){
		enterText(driver, accountsTextBox, accountName);
	}
	
	public void clickSearchButton(WebDriver driver){
		clickElement(driver, serachAccountsButton);
		idleWait(3);
	}
	
	public void enterSalesforceId(WebDriver driver, String salesforceID){
		enterText(driver, salesforceIdTextBox, salesforceID);
	}
	
	public List<WebElement> getAccoutsList(WebDriver driver){
		return getElements(driver, searchRestulList);
	}
	
	public void clickFirstAccount(WebDriver driver){
		getAccoutsList(driver).get(0).click();
	}
	
	public void searchAndNavigateToAccount(WebDriver driver, String accountName, String salesforceID){
		if(isAccountsTextBoxPresent(driver)) {
			assertTrue(isAccountsHeadingPresent(driver));
			assertTrue(isAccountsTextBoxPresent(driver));
			assertTrue(isSearchAccountsButtonPresent(driver));
			enterAccountsName(driver, accountName);
			enterSalesforceId(driver, salesforceID);
			clickSearchButton(driver);
			isElementVisible(driver, paceBar, 1);
			waitUntilInvisible(driver, paceBar);
			clickFirstAccount(driver);
		}
	}
	
	public void verifyAccountDeleted(WebDriver driver, String accountName, String deletedDate) {
		
		//verifying color is greyed
		By accountNameLoc = By.xpath(accountNamePage.replace("$accountName$", accountName));
		String bckgroundColor = getCssValue(driver, findElement(driver, accountNameLoc), CssValues.Color);
		String bckgroundHexColor = Color.fromString(bckgroundColor).asHex();
		assertEquals(bckgroundHexColor, "#000000", "color is not greyed");
		
		//verifying parent class contains deleted
		accountNameLoc = By.xpath(accountNamePage.replace("$accountName$", accountName));
		WebElement parent = findElement(driver, accountNameLoc).findElement(By.xpath("ancestor::tr"));
		assertEquals(parent.getAttribute("class"), "deleted");
		
		//verifying deleted date
		By accountDeletedDateLoc = By.xpath(accountDateDeleted.replace("$accountName$", accountName));
		assertTrue(getElementsText(driver, accountDeletedDateLoc).contains(deletedDate));
	}
}
