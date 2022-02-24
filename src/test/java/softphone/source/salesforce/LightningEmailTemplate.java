/**
 * 
 */
package softphone.source.salesforce;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;

/**
 * @author Abhishek
 *
 */
public class LightningEmailTemplate extends SeleniumBase{
	
	By pageHeader = By.className("pageDescription");
	
	By emailIframe 				= By.xpath(".//*[@class='oneAlohaPage']//iframe");
	By emailPage				= By.className("oneAlohaPage");
	
	By emailSubjectTextBox		= By.xpath(".//td[*[text()='Subject']]/following-sibling::td//input");
	By emailBodyTextBox			= By.xpath(".//td[*[text()='Body']]/following-sibling::td//textarea");
	By emailSendBtn				= By.xpath(".//input[@value=' Send ']");
	
	public void verifyPageHeading(WebDriver driver){
		waitUntilVisible(driver, emailPage);
		switchToIframe(driver, emailIframe);
		waitUntilVisible(driver, pageHeader);
		assertEquals(getElementsText(driver, pageHeader), "Send an Email");
	}
	
	public void sendEmail(WebDriver driver, String emailSubject, String emailBody){
		verifyPageHeading(driver);
		enterText(driver, emailSubjectTextBox, emailSubject);
		enterText(driver, emailBodyTextBox, emailBody);
		clickElement(driver, emailSendBtn);
		acceptAlert(driver);
	}
}
