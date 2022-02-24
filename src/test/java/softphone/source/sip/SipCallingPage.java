/**
 * @author Abhishe Gupta
 *
 */
package softphone.source.sip;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;

public class SipCallingPage extends SeleniumBase{

	//******** SIP Login Page locator starts here********//
	By userNameTextBox			= By.id("onsip-login-username");
	By passwordTextBox			= By.id("onsip-login-password");
	By submitButton				= By.cssSelector("button[type='submit']");
	//******** SIP Login Page locator ends here********//
	
	//******** SIP Calling Page locator starts here********//
	By avatarImage				= By.cssSelector("onsip-status-menu onsip-avatar");
	By callAcceptBtn			= By.id("onsip--incoming-call--answer-audio");
	By callDeclineBtn			= By.id("onsip--incoming-call--end-call");
	By openDialPadIcon			= By.xpath("//*[contains(@class, 'mat-button-base')]//mat-icon[text() = 'dialpad']");
	By dialPadNum1				= By.xpath(".//onsip-dialpad//*[@class = 'number' and text() = '1']");
	By callEndBtn				= By.id("onsip--call2--end-call");
	By callMuteBtn				= By.id("onsip--call2--toggle-mute-volume");
	//******** SIP Calling Page locator ends here********//
	
	/**
	 * this method is user to login to SIP application
	 * @param driver
	 * @param config Properties file object which contains username password for SIP 
	 */
	public void loginToSipApp(WebDriver driver, Properties config) {
		driver.get("https://app.onsip.com/");
		waitUntilVisible(driver, submitButton);
		enterText(driver, userNameTextBox, config.getProperty("sip_account_username"));
		enterText(driver, passwordTextBox, config.getProperty("sip_account_password"));
		clickElement(driver, submitButton);
		waitUntilVisible(driver, avatarImage);
	}
	
	/**
	 * Use this method to verify that call is appearing on SIP
	 * @param driver
	 */
	public void verifyCallAppearing(WebDriver driver) {
		waitUntilVisible(driver, callAcceptBtn);
		waitUntilVisible(driver, callDeclineBtn);
	}
	
	/**
	 * Use this method to accept incoming call to SIP
	 * @param driver
	 */
	public void acceptCall(WebDriver driver) {
		waitUntilVisible(driver, callAcceptBtn);
		clickElement(driver, callAcceptBtn);
		waitUntilVisible(driver, openDialPadIcon);
	}
	
	/**
	 * Use this method to decline incoming call to SIP
	 * @param driver
	 */
	public void declineCall(WebDriver driver) {
		waitUntilVisible(driver, callDeclineBtn);
		clickElement(driver, callDeclineBtn);
	}
	
	/**
	 * Use this method to accept forwarding verifying call and click on dial pad number 1
	 * @param driver
	 */
	public void clickDialPadNum1(WebDriver driver) {
		acceptCall(driver);
		clickElement(driver, openDialPadIcon);
		clickElement(driver, dialPadNum1);
		waitUntilInvisible(driver, openDialPadIcon);
	}
	
	/**
	 * Use this method to verify that call is connected
	 * @param driver
	 */
	public void verifyCallIsConnected(WebDriver driver) {
		waitUntilVisible(driver, callEndBtn);
		waitUntilVisible(driver, callMuteBtn);
	}
	
	
	/**
	 * Use this method to verify that call has been ended
	 * @param driver
	 */
	public void verifyCallHasEnded(WebDriver driver) {
		waitUntilInvisible(driver, callEndBtn);
		waitUntilInvisible(driver, callMuteBtn);
	}
}