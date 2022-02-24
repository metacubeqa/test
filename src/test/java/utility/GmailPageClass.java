package utility;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class GmailPageClass extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	 
	//gmail
	By gmailLogo               = By.xpath("//a[@title='Gmail' and @href='#inbox']/img");
	By gmailAppsButton         = By.xpath("//a[@aria-label='Google apps']");
	By calendarApp             = By.xpath("//a[contains(@href,'https://www.google.com/calendar')]");
	
	//mail list
	//By mailList                = By.xpath("//table[@aria-readonly='true']/tbody/tr");
	By mailList                = By.xpath("//span[contains(text(), 'Invitation')]/ancestor::td[@role='gridcell']/div[@role='link']");
	//mail
	By mailTitle               = By.xpath("//h2[@data-thread-perm-id]");
	By mailBody         	   = By.xpath("//div[contains(@data-message-id, 'msg')]");
	By ringDnaConnText         = By.xpath("//div[contains(@data-message-id, 'msg')]//div//b");
	By mailDeleteButton        = By.xpath("//div[@data-tooltip= 'Delete' and @jslog]");
	
	//event
	By calEventGoingYes        = By.xpath("//span[contains(text(), 'Going')]/following-sibling::strong//a[text()='Yes']");
	By calEventGoingNo         = By.xpath("//span[contains(text(), 'Going')]/following-sibling::strong//a[text()='No']");
	By calEventGoingMaybe      = By.xpath("//span[contains(text(), 'Going')]/following-sibling::strong//a[text()='Maybe']");
	
	//ringdna mail locator
	By integratedAccounts      = By.xpath("//a[text()='Integrated Accounts']"); 
	By ringDnaLogoInMail       = By.xpath("//img[@alt='ringDNA logo']");
	
	By descritptionInMail      = By.xpath("//tr/td[contains(text(), 'Description')] | //span/p/text()[3]");
	By eventNameInMail         = By.xpath("//table[@role='presentation']/tbody/tr[3]/td | //span/p/text()[1]");
	By meetingTypeInMail       = By.xpath("//table[@role='presentation']/tbody/tr[4]/td | //span/p/text()[2]");
	By meetingVideoUrl         = By.xpath("//p[contains(text(), 'Video Conference')]");
	By meetingTimeInMail       = By.xpath("//td[text()='When']/following-sibling::td");
	
	By attendeeMail            = By.xpath("//td[contains(text(),'Email Address')]/a");
	
	By signOffCopyRightText    = By.xpath("//table[@role='presentation']/tbody/tr[1]/td[contains(text(), 'best')]");
	
	String sender              = "//span[text()= '%text%']/preceding-sibling::span[@email='%text%' and @name='%name%']";
	
	// connect gmail int
	By gmailEmailInput         = By.cssSelector("#identifierId");
	By gmailNextButton 		   = By.xpath("//span[text()= 'Next']/parent::button");
	By gmailPasswdInput		   = By.xpath("//input[@name='password']");
	By gmailAllowButton		   = By.xpath("//span[text()= 'Allow']/parent::button");
	By gmailTryAnotherWayButton = By.xpath("//span[contains(text(), 'Try another way')]//parent::button");
	By gmailAuthenticator 	   = By.xpath("//strong[contains(text(), 'Google Authenticator')]//ancestor::li");
	By gmailAuthOtp 		   = By.cssSelector("#totpPin");
	By gmailLoginAccount 	   = By.cssSelector("#profileIdentifier");
	
	//Search Mail in Gmail
	By input_Search_mail       = By.xpath ("//input[@placeholder='Search mail']");
	By sendername      		   = By.cssSelector("[class='gD']");
	By Search_mail			   = By.xpath("/html/body//table/tbody/tr/td[5]//div[3]/span/span"); 	
	/**
	 * open mail by index
	 * @param driver
	 * @param index
	 */
	public void openNewMailInGmail(WebDriver driver, int index) {
		List<WebElement> mails = getElements(driver, mailList);
		waitUntilVisible(driver, mails.get((1-index)));
		clickElement(driver, mails.get((1-index)));
	}
	
	/**
	 * open gmail in new tab
	 * @param driver
	 */
	public void openGmailInNewTab(WebDriver driver) {
		openNewBlankTab(driver);
		switchToTab(driver, getTabCount(driver));
		String site = "https://accounts.google.com";
		driver.get(site);
	}
	
	/**
	 * click integrated Accounts button
	 * @param driver
	 */
	public void clickIntegratedAccountsButton(WebDriver driver) {
		waitUntilVisible(driver, integratedAccounts);
		clickElement(driver, integratedAccounts);
	}
	
	/**
	 * verify gmail disconnect mail
	 * @param driver
	 */
	public void verifyGmailAccountDisconnectedMail(WebDriver driver) {
		waitUntilVisible(driver, mailTitle);
		String title = findElement(driver, mailTitle).getText();
		waitUntilVisible(driver, ringDnaConnText);
		String body = findElement(driver, ringDnaConnText).getText();
		assertEquals(title, "Your GMAIL account has been disconnected");
		assertTrue(body.contains("You've successfully disconnected your GMAIL."));
	}
	
	/**
	 * verify exchnage disconnect mail
	 * @param driver
	 */
	public void verifyExchangeAccountDisconnectedMail(WebDriver driver) {
		waitUntilVisible(driver, mailTitle);
		String title = findElement(driver, mailTitle).getText();
		waitUntilVisible(driver, ringDnaConnText);
		String body = findElement(driver, ringDnaConnText).getText();
		assertEquals(title, "Your EXCHANGE account has been disconnected");
		assertTrue(body.contains("You've successfully disconnected your EXCHANGE."));
	}
	
	/**
	 * @param driver
	 * @return title of mail
	 */
	public String getMailTitle(WebDriver driver) {
		waitUntilVisible(driver, mailTitle);
		return findElement(driver, mailTitle).getText();
	}
	
	/**
	 * @param driver
	 * @return description text from mail
	 */
	public String getDescriptionMessageFromMail(WebDriver driver) {
		waitUntilVisible(driver, descritptionInMail);
		return findElement(driver, descritptionInMail).getText();
	}
	
	/**
	 * @param driver
	 * @return Event text from mail
	 */
	public String getEventNameFromMail(WebDriver driver) {
		waitUntilVisible(driver, eventNameInMail);
		return findElement(driver, eventNameInMail).getText();
	}
	
	/**
	 * @param driver
	 * @return meeting type from mail
	 */
	public String getMeetingTypeFromMail(WebDriver driver) {
		waitUntilVisible(driver, meetingTypeInMail);
		return findElement(driver, meetingTypeInMail).getText();
	}
	
	/**
	 * delete Opened Mail
	 * @param driver
	 */
	public void deleteOpenedMail(WebDriver driver) {
		waitUntilVisible(driver, mailDeleteButton);
		clickElement(driver, mailDeleteButton);
	}
	
	/**
	 * @param driver
	 * @return visiblity of ring dna logo
	 */
	public boolean isRingDnaLogoVisibleInMail(WebDriver driver) {
		return isElementVisible(driver, ringDnaLogoInMail, 5);
	}
	
	/**
	 * @param driver
	 * @return text of ring dna logo
	 */
	public String getRingDnaLogoTextInMail(WebDriver driver) {
		waitUntilVisible(driver, ringDnaLogoInMail);
		return findElement(driver, ringDnaLogoInMail).getAttribute("alt");
	}
	
	/**
	 * @param driver
	 * @return visiblity of copy rights text and signing off text
	 */
	public boolean isCopyRightAndSignOffTextVisible(WebDriver driver) {
		return isElementVisible(driver, signOffCopyRightText, 5);
	}
	
	/**
	 * @param driver
	 * @return text of copy rights text and signing off text
	 */
	public String getCopyRightAndSignOffTextVisible(WebDriver driver) {
		waitUntilVisible(driver, signOffCopyRightText);
		return findElement(driver, signOffCopyRightText).getText();
	}
	
	/**
	 * @param driver
	 * @return attendee mail from mail
	 */
	public String getAttendeeMailFromMail(WebDriver driver) {
		waitUntilVisible(driver, attendeeMail);
		return findElement(driver, attendeeMail).getText();
	}
	
	/**
	 * @param driver
	 * @return video url mail from mail
	 */
	public String getVideoUrlFromMail(WebDriver driver) {
		waitUntilVisible(driver, meetingVideoUrl);
		return findElement(driver, meetingVideoUrl).getText();
	}
	
	public boolean isRingDnaSenderVisible(WebDriver driver, String name, String text) {
		By locator = By.xpath(sender.replace("%text%", text).replace("%name%", name));
		return isElementVisible(driver, locator, 6);
	}
	
	/**
	 * @param driver
	 * click yes for cal event
	 */
	public void clickGoingYesForCalEvent(WebDriver driver) {
		waitUntilVisible(driver, calEventGoingYes);
		clickElement(driver, calEventGoingYes);	
	}
	
	/**
	 * @param driver
	 * click no for cal event
	 */
	public void clickGoingNoForCalEvent(WebDriver driver) {
		waitUntilVisible(driver, calEventGoingNo);
		clickElement(driver, calEventGoingNo);	
	}
	
	/**
	 * @param driver
	 * click Maybe for cal event
	 */
	public void clickGoingMaybeForCalEvent(WebDriver driver) {
		waitUntilVisible(driver, calEventGoingMaybe);
		clickElement(driver, calEventGoingMaybe);	
	}
	
	public void openGoogleCalendarInNewTab(WebDriver driver) {
		openNewBlankTab(driver);
		switchToTab(driver, getTabCount(driver));
		String site = "https://calendar.google.com";
		driver.get(site);
	}
	
	/**
	 * @param driver
	 * @return meeting time from mail
	 */
	public String getMeetingTimeFromMail(WebDriver driver) {
		waitUntilVisible(driver, meetingTimeInMail);
		return getElementsText(driver, meetingTimeInMail);
	}
	
	
	public void loginGamil(WebDriver driver, String email, String password) {
		
		waitUntilVisible(driver, gmailEmailInput);
		enterText(driver, gmailEmailInput, email);

		waitUntilVisible(driver, gmailNextButton);
		clickElement(driver, gmailNextButton);

		waitUntilVisible(driver, gmailPasswdInput);
		byte[] decodedString = Base64.decodeBase64(password);
		enterText(driver, gmailPasswdInput, new String(decodedString));

		waitUntilVisible(driver, gmailNextButton);
		clickElement(driver, gmailNextButton);

		waitUntilVisible(driver, gmailTryAnotherWayButton);
		clickElement(driver, gmailTryAnotherWayButton);

		waitUntilVisible(driver, gmailAuthenticator);
		clickElement(driver, gmailAuthenticator);

		// OTP value is returned from getTwoFactor method
		waitUntilVisible(driver, gmailAuthOtp);
		enterText(driver, gmailAuthOtp, getTwoFactorCode());

		waitUntilVisible(driver, gmailNextButton);
		clickElement(driver, gmailNextButton);

	}
	
	

	
	public static String getTwoFactorCode(){
        Totp totp = new Totp("m56b mznj g2bl xuyf 7cdo 6t7u q4hm n5vn"); // 2FA secret key
        String twoFactorCode = totp.now(); //Generated 2FA code here
        return twoFactorCode;
    }

	public void searchsubject (WebDriver driver, String Subjectname) {
		waitUntilVisible(driver, input_Search_mail);
		enterText(driver, input_Search_mail, Subjectname);
		clickEnter(driver, input_Search_mail);
	}
	public void OpenSearchsubjectMail (WebDriver driver) {
		waitUntilVisible(driver, Search_mail);	
		clickElement(driver, Search_mail);
	}

	
		
	public String verifySenderName (WebDriver driver) {
		waitUntilVisible(driver, sendername);
		return getElementsText(driver, sendername);
	}
}
