package guidedSelling.source.pageClasses;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import base.SeleniumBase;
import utility.HelperFunctions;
import utility.GmailPageClass;

public class SnippetPage extends SeleniumBase  {
	 By snippetiframe 			 = By.xpath("//*[@class='iframe-parent slds-template_iframe slds-card']/iframe");
	 By createSnippet			 = By.xpath("//*[contains(@class,'RdnaButton__StyledButton-cqEIsw')]");
	 By snippetName				 = By.xpath("//*[contains(@placeholder, 'Name your Snippet')]");
	 By snippetBody				 = By.xpath("//*[contains(@class, 'se-wrapper-wysiwyg sun-editor-editable')]");
	 By saveBtn 			     = By.xpath("//button[text()='Save and Finish']");
	 String snipptName			 = "//span[text()='$Snipptname$']";
	 By searchSnippt			 = By.xpath("//input[contains(@id,'global-search-filter')]");

	public void create_Snippets (WebDriver driver, String subjectText, String emailBodytext){
		if (isElementVisible(driver, snippetiframe, 5)) {
			
			switchToIframe(driver, snippetiframe);
			clickElement(driver, createSnippet);
			waitUntilVisible(driver, snippetName);
			enterText(driver, snippetName, subjectText);
			waitUntilVisible(driver, snippetBody);
			
			//scrollTillEndOfPage(driver);
			clickElement(driver, snippetBody);
			driver.findElement(snippetBody).sendKeys(Keys.ENTER);
			driver.findElement(snippetBody).sendKeys(emailBodytext);
			waitUntilTextPresent(driver, snippetBody, emailBodytext.trim());
			
			waitForElementEnabled(driver, saveBtn);
			idleWait(1);
			waitUntilClickable(driver, saveBtn);
			clickElement(driver, saveBtn);
			
		
		}else {
			waitUntilVisible(driver, snippetName);
			enterText(driver, snippetName, subjectText);
			waitUntilVisible(driver, snippetBody);
			
			//scrollTillEndOfPage(driver);
			clickElement(driver, snippetBody);
			driver.findElement(snippetBody).sendKeys(Keys.ENTER);
			driver.findElement(snippetBody).sendKeys(emailBodytext);
			waitUntilTextPresent(driver, snippetBody, emailBodytext.trim());
			
			waitForElementEnabled(driver, saveBtn);
			idleWait(1);
			waitUntilClickable(driver, saveBtn);
			clickElement(driver, saveBtn);
		}
	}
		public void verifySnippetsCreated(WebDriver driver, String subjectText) {
			isSpinnerWheelInvisible(driver);
			By snipptHeaderLoc = By.xpath(snipptName.replace("$Snipptname$", subjectText));
			if (isElementVisible(driver, snippetiframe, 5)) {
				switchToIframe(driver, snippetiframe);
			waitUntilVisible(driver, snipptHeaderLoc);
			}else {
				waitUntilVisible(driver, snipptHeaderLoc);
			}
		}


}

	

