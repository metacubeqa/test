/**
 * 
 */
package softphone.source;

import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import utility.HelperFunctions;

import com.google.common.collect.Ordering;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneMessagePage extends SeleniumBase {

	By inactiveMessageIcon 			= By.cssSelector(".messages:not(.active)");
	By messageTabHeading			= By.xpath(".//h1[text()='Messages']");
	
	By selectMsgType				= By.cssSelector(".primary-match-type-filter");
	By allMessageTabLink 			= By.cssSelector(".filter.all:not(.active)");
	By allMessageActiveTabLink 		= By.cssSelector(".filter.all.active");
	By readMessageTabLink 			= By.cssSelector(".filter.read:not(.active)");
	By readMessageActiveTabLink 	= By.cssSelector(".filter.read.active");
	By unReadMessageTabLink		 	= By.cssSelector(".filter.unread:not(.active)");
	By unReadMessageActiveTabLink 	= By.cssSelector(".filter.unread.active");
	String messageAllTab			= ".//*[@id='messages']//div[contains(text(),'$message$')]";
	
	By messageDateTimeList			= By.xpath(".//div[contains(@class, 'datetime')]");
	String messageDateTime			= ".//*[contains(@class,'content') and text() = '$$Message$$']/../../..//div[contains(@class, 'datetime')]";					
	String newMsgDotImg				= ".//*[contains(@class,'content') and text() = '$$Message$$']/../../..//div[@class='new']";

	By callerName					= By.cssSelector(".name.caller");
	By newMsgs						= By.cssSelector("div.new");
	By openMessageIcon 				= By.cssSelector("[src='images/btn-open-recent-call.png']");
	By unreadMessageTabCount 		= By.id("unreadMessageCount");
	By unreadMessagCount			= By.cssSelector(".unread-message-count");

	By loadMoreBtn					= By.cssSelector("[data-action='more']");
	By spinnerWheel 				= By.className("overlay-workspace");

	public static enum MsgType {
		All,
		Leads,
		Contacts,
		Unknown
	}
	
	public boolean isMsgIconVisible(WebDriver driver){
		return isElementVisible(driver, inactiveMessageIcon, 5);
	}
	
	public void clickMessageIcon(WebDriver driver) {
		if (isMsgIconVisible(driver)) {
			clickElement(driver, inactiveMessageIcon);
			waitUntilInvisible(driver, spinnerWheel);
		}
	}
	
	public void verifyMessageHeadingPresent(WebDriver driver){
		waitUntilVisible(driver, messageTabHeading);
	}

	public void selectMsgType(WebDriver driver, String msgType) {
		waitUntilVisible(driver, selectMsgType);
		selectFromDropdown(driver, selectMsgType, SelectTypes.visibleText, msgType);
		idleWait(2);
	}
	
	public void navigateToAllMessages(WebDriver driver) {
		if (!isElementVisible(driver, allMessageActiveTabLink, 1))
			clickElement(driver, allMessageTabLink);
	}

	public void navigateToReadMessages(WebDriver driver) {
		if (!isElementVisible(driver, readMessageActiveTabLink, 1))
			clickElement(driver, readMessageTabLink);
	}

	public void navigateToUnreadMessages(WebDriver driver) {
		if (!isElementVisible(driver, unReadMessageActiveTabLink, 1))
			clickElement(driver, unReadMessageTabLink);
	}

	public String getUnreadMessageTabCount(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		String messageCount = getElementsText(driver, unreadMessageTabCount);
		return messageCount.isEmpty() ? "0" : messageCount;
	}
	
	public String getUnreadMessageCount(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		String messageCount = getElementsText(driver, unreadMessagCount);
		return messageCount.isEmpty() ? "0" : messageCount;
	}

	public void clickOpenMessageIconByIndex(WebDriver driver, int index) {
		waitUntilInvisible(driver, spinnerWheel);
		getElements(driver, openMessageIcon).get(index - 1).click();
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void clickOpenMessageIconByName(WebDriver driver, String name) {
		waitUntilInvisible(driver, spinnerWheel);
		int k = 0;
		while (k <= 2) {
			for (int i = 0; i < getElements(driver, callerName).size(); i++) {
				if (getTextListFromElements(driver, callerName).get(i).contains(name)) {
					clickElement(driver, getElements(driver, openMessageIcon).get(i));
					waitUntilInvisible(driver, spinnerWheel);
					return;
				}
			}
			clickLoadMoreBtn(driver);
			k++;
		}
	}
	
	public String getCallerName(WebDriver driver) {
		return getTextListFromElements(driver, callerName).get(0); 
	}
	
	public String getMsgDateTime(WebDriver driver, String message){
		By messageDateTimeLoc = By.xpath(messageDateTime.replace("$$Message$$", message));
		waitUntilVisible(driver, messageDateTimeLoc);
		return getElementsText(driver, messageDateTimeLoc);
	}
	
	public void isMessageListSorted(WebDriver driver){
		waitUntilVisible(driver, messageDateTimeList);
		ArrayList<Date> msgDateTimeList = new ArrayList<>();
		List<String> msgDateTimeStringList = getMsgDateTimeList(driver);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
		for (String msgDateTime : msgDateTimeStringList) {
			try {
				msgDateTimeList.add(dateTimeFormatter.parse(msgDateTime));
			} catch (ParseException e) {
				System.out.println("not able to parse date time list");
				e.printStackTrace();
			}
		}
		assertTrue(msgDateTimeList.size() > 0);
		assertTrue(Ordering.natural().reverse().isOrdered(msgDateTimeList));
	}
	
	public List<String> getMsgDateTimeList(WebDriver driver){
		return getTextListFromElements(driver, messageDateTimeList);
	}
	
	public boolean verifyMsgDateInGivenFormat(String format, String date){
		return HelperFunctions.isDateInGivenFormat(format, date);
	}
	
	public void verifyNewMsgDotVisible(WebDriver driver, String message){
		By newMsgDotTimeLoc = By.xpath(newMsgDotImg.replace("$$Message$$", message));
		waitUntilVisible(driver, newMsgDotTimeLoc);
	}
	
	public void verifyNewMsgsNotVisible(WebDriver driver){
		waitUntilInvisible(driver, newMsgs);
	}
	
	public void verifyNewMsgsVisible(WebDriver driver){
		waitUntilVisible(driver, newMsgs);
	}
	
	public boolean isMsgPresentInAllTab(WebDriver driver, String message) {
		By messageLoc = By.xpath(messageAllTab.replace("$message$", message));
		return isElementVisible(driver, messageLoc, 5);
	}

	public void clickLoadMoreBtn(WebDriver driver) {
		if(isElementVisible(driver, loadMoreBtn, 5)) {
			scrollToElement(driver, loadMoreBtn);
			clickByJs(driver, loadMoreBtn);
		}
	}
	
	public int getTotalMsgsCount(WebDriver driver) {
		return getElements(driver, openMessageIcon).size();
	}
	
}
