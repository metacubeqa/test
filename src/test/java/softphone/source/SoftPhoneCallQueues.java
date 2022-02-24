/**
 * 
 */
package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.SeleniumBase;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneCallQueues extends SeleniumBase{

	By callQueueImg 				= By.cssSelector(".call-queue-nav");
	
	By queueToggleLink				= By.cssSelector(".callQueueViewToggle .showQueues");
	By callsToggleLink				= By.cssSelector(".callQueueViewToggle .showCalls");
	By callQueuesUnsubscribeBtnList = By.cssSelector(".call-queues .unsubscribe:not([style='display: none;'])");
	By callQueuesSubscribeBtnList = By.cssSelector(".call-queues .subscribe");
	By callQueueNameList 			= By.cssSelector(".call-queues .queueName");
	
	By subscriptionMessage 			= By.id("subscriptionMessage");
	By noQueueMessage 				= By.id("noQueueMessage");
	By disabledCallQueuePickButton 	= By.cssSelector(".disablePickupCall img");
	
	By callQueuePickButton 			= By.cssSelector(".pickupCall");
	By moreCallActionsBtnLoc		= By.cssSelector("[src='images/icon-more.svg']");
	By additionalActionsBlock		= By.xpath(".//*[@class='additional-actions']//div[@class='additional-actions__actions' and @style='display: block;']"); 
	By hangupBtnLoc					= By.cssSelector(".additional-actions__actions:not([style='display: none;']) .additional-actions__button.hangupCall");
	By sendToVoiceMailBtn			= By.cssSelector(".additional-actions__actions:not([style='display: none;']) .additional-actions__button.sendToVoicemail ");
	By queueCountLoc 				= By.id("callQueueCount");
	By fadeEffect 					= By.cssSelector(".modal-backdrop.fade");
	By queueCallerName				= By.cssSelector(".queue-container span.callerName");
	By queueTimerIcon				= By.cssSelector(".queue-container span.icon-time");
	By queueCallerTitle				= By.cssSelector(".queue-container span.callerTitle");
	By queueCallerCompany			= By.cssSelector(".queue-container span.companyName");
	By queueName					= By.cssSelector(".queue-container .queue-info span.queueName");
	By queueLeadStatus				= By.cssSelector(".queue-container span.leadStatus");
	By queueLeadSource				= By.cssSelector(".queue-container span.leadSource");
	String queueHoldTime			= ".//*[contains(@class, 'hold-info') and @style='display: block;']//*[contains(@class, 'queueHoldTime')]//*[contains(@class, 'countdown-amount')][$$index$$]";
	
	By callQueueExpandButton		= By.cssSelector(".additional-info__expand");
	By callQueueCollapseButton		= By.cssSelector(".additional-info__collapse");
	By callQueueOpportunityLabel	= By.cssSelector(".queue-container .opportunity__label");
	By QueueCallerName				= By.cssSelector(".callerName");
	String queueRow					= ".//*[contains(@class, 'callerName') and contains(text(), '$$CallerNumber$$')]/../..";
	String queueSubscribeBtn 		= ".//*[@class='queueName' and text() = '$$Queue$$']/following-sibling::button[contains(@class, 'subscribe') and not(contains(@class, 'un'))]";
	String queueUnsubscribeBtn 		= ".//*[@class='queueName' and text() = '$$Queue$$']/following-sibling::button[contains(@class, 'unsubscribe')]";
	String queueNameLocator 		= ".//*[@id='queue-metadata']//*[@class='queueName' and text()='$$Queue$$']";
	String joinQueueName 			= ".//*[@id='queuesContainer']//*[@class='queueName' and text()='$$Group$$']";
	String subscribedQueueCallsCount = ".//span[@class='queueName' and text()='$$Queue$$']/ancestor::div[@class='queue-container']//span[@class='numCalls']";																								  
	
	public static enum QueueUserDetails{
		QueueCallerName,
		QueueTimerIcon,
		QueueCallerTitle,
		QueueCallerCompany,
		QueueName,
		QueueLeadStatus,
		QueueLeadSource;
	}
	
	public void openCallQueuesSection(WebDriver driver){
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callQueueImg);
		clickElement(driver, callQueueImg);
	}
	
	public void toggleToQueues(WebDriver driver){
		waitUntilInvisible(driver, fadeEffect);
		waitUntilVisible(driver, queueToggleLink);
		if(findElement(driver, queueToggleLink).getAttribute("class").contains("active")) {
			System.out.println("Already on queue tab");	
		}else {
			clickElement(driver, queueToggleLink);
		} 
	}
	
	public void toggleToCalls(WebDriver driver){
		waitUntilVisible(driver, callsToggleLink);
		if(findElement(driver, callsToggleLink).getAttribute("class").contains("active")) {
			System.out.println("Already on calls tab");	
		}else {
			clickElement(driver, callsToggleLink);
		}
	}
	
	public void unSubscribeAllQueues(WebDriver driver){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		if(isElementVisible(driver, callQueuesUnsubscribeBtnList, 0)){
			List<WebElement> queuesCheckBox = getInactiveElements(driver, callQueuesUnsubscribeBtnList);
			for(int i = 0; i < queuesCheckBox.size(); i++){
				waitUntilInvisible(driver, getInactiveElements(driver, callQueuesSubscribeBtnList).get(0));
				WebElement firstUnsubscribeButton = getInactiveElements(driver, callQueuesUnsubscribeBtnList).get(0);
				firstUnsubscribeButton.click();
				waitUntilInvisible(driver, firstUnsubscribeButton);
			}
		}
		toggleToCalls(driver);
	}
	
	public List<String> returnCallQueueList(WebDriver driver) {
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		idleWait(2);
		List<String> queuNames = getTextListFromElements(driver, callQueueNameList);
		toggleToCalls(driver);
		return queuNames;
	}
	
	public void subscribeQueue(WebDriver driver, String queueName){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By queueSubscribeButton = By.xpath(queueSubscribeBtn.replace("$$Queue$$", queueName));
			if(!findElement(driver, queueSubscribeButton).getCssValue("display").contains("none")) {
				waitUntilVisible(driver, queueSubscribeButton);
				clickByJs(driver, queueSubscribeButton);
			}
		toggleToCalls(driver);
	}
	
	public void unSubscribeQueue(WebDriver driver, String queueName){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By queueSubscribeButton = By.xpath(queueUnsubscribeBtn.replace("$$Queue$$", queueName));
			if(!findElement(driver, queueSubscribeButton).getCssValue("display").contains("none")) {
				waitUntilVisible(driver, queueSubscribeButton);
				clickByJs(driver, queueSubscribeButton);
			}
   
		toggleToCalls(driver);
	}
	
	public String getQeueueCallerName(WebDriver driver) {
		waitUntilVisible(driver, QueueCallerName);
		return getElementsText(driver, QueueCallerName).split("\\(")[0].trim();
	}

	public void isPickCallBtnVisible(WebDriver driver){
		waitUntilVisible(driver, callQueuePickButton);
	}
	
	public boolean isPickCallBtnPresent(WebDriver driver){
		return isElementVisible(driver, callQueuePickButton, 0);
	}
	
	public void isPickCallBtnInvisible(WebDriver driver){
		waitUntilInvisible(driver, callQueuePickButton);
	}
	
	public void pickCallFromQueue(WebDriver driver){
		clickElement(driver, callQueuePickButton);
	}
	
	public void verifyAllPickQueueCallBtnVisible(WebDriver driver, int queueCallCount){
		idleWait(5);
		getElements(driver, callQueuePickButton);
		assertEquals(getElements(driver, callQueuePickButton).size(), queueCallCount);
	}
	
	public void DeclineCallFromQueue(WebDriver driver){
		clickMoreCallActionsToggle(driver);
		clickElement(driver, hangupBtnLoc);
	}
	
	public void clickMoreCallActionsToggle(WebDriver driver) {
		if(!isElementVisible(driver, additionalActionsBlock, 5)) {
			waitUntilVisible(driver, moreCallActionsBtnLoc);
			clickByJs(driver, moreCallActionsBtnLoc);
		}
	}
	
	public boolean isdeclineCallVisibleFromQueue(WebDriver driver){
		clickMoreCallActionsToggle(driver);
		return isElementVisible(driver, hangupBtnLoc, 5);
	}
	
	public void sendToVoiceMailFromQueue(WebDriver driver){
		clickMoreCallActionsToggle(driver);
		clickElement(driver, sendToVoiceMailBtn);
	}
	
	public boolean isSendToVoiceMailDisabledFromQueue(WebDriver driver){
		clickMoreCallActionsToggle(driver);
		return getAttribue(driver, sendToVoiceMailBtn, ElementAttributes.Class).contains("disabled");
	}
	
	public void DeclineCallFromQueue(WebDriver driver, int index){
		getElements(driver, moreCallActionsBtnLoc).get(index).click();
		getInactiveElements(driver, hangupBtnLoc).get(index).click();
	}
	
	public void verifyAllDeclineQueueCallBtnVisible(WebDriver driver){
		idleWait(5);
		List<WebElement> moreCallActionsBtnList = getElements(driver, moreCallActionsBtnLoc);
		for (WebElement moreCallActionsBtn : moreCallActionsBtnList) {
			clickElement(driver, moreCallActionsBtn);
			assertFalse(isAttributePresent(driver, hangupBtnLoc, "disabled"));
			clickElement(driver, moreCallActionsBtn);
		}
	}
	
	public void verifyAllDeclineQueueCallBtnInVisible(WebDriver driver) {
		waitUntilInvisible(driver, moreCallActionsBtnLoc);
	}		
	
	public void verifyQueueData(WebDriver driver, HashMap<QueueUserDetails, String> queueUserData, String callerNumber) {
		WebElement queueRowLoc = findElement(driver, By.xpath(queueRow.replace("$$CallerNumber$$", callerNumber)));
		if(isElementVisible(driver, queueRowLoc.findElement(callQueueExpandButton), 2)) {
			clickElement(driver, queueRowLoc.findElement(callQueueExpandButton));
		}
		
		if(queueUserData.get(QueueUserDetails.QueueCallerName)!=null){
			assertEquals(getElementsText(driver, queueCallerName), queueUserData.get(QueueUserDetails.QueueCallerName));
		}	
		
		if(queueUserData.get(QueueUserDetails.QueueTimerIcon)!=null){
			waitUntilVisible(driver, queueTimerIcon);
		}	
		
		if(queueUserData.get(QueueUserDetails.QueueCallerTitle)!=null){
			assertEquals(getElementsText(driver, queueCallerTitle), queueUserData.get(QueueUserDetails.QueueCallerTitle));
		}	
		
		if(queueUserData.get(QueueUserDetails.QueueCallerCompany)!=null){
			assertEquals(getElementsText(driver, queueCallerCompany), queueUserData.get(QueueUserDetails.QueueCallerCompany));
		}
		
		if(queueUserData.get(QueueUserDetails.QueueName)!=null){
			assertEquals(getElementsText(driver, queueName), queueUserData.get(QueueUserDetails.QueueName));
		}
		
		if(queueUserData.get(QueueUserDetails.QueueLeadStatus)!=null){
			assertEquals(getElementsText(driver, queueLeadStatus), "Status: " + queueUserData.get(QueueUserDetails.QueueLeadStatus));
		}
		
		if(queueUserData.get(QueueUserDetails.QueueLeadSource)!=null){
			assertEquals(getElementsText(driver, queueLeadSource), "Source: " + queueUserData.get(QueueUserDetails.QueueLeadSource));
		}
	}
	
	public String getContactOpportunityName(WebDriver driver, String callerNumber) {
		idleWait(2);
		WebElement queueRowLoc = findElement(driver, By.xpath(queueRow.replace("$$CallerNumber$$", callerNumber)));
		waitUntilVisible(driver, queueRowLoc.findElement(callQueueExpandButton));
		clickElement(driver, queueRowLoc.findElement(callQueueExpandButton));
		String opportunityName = getElementsText(driver, queueRowLoc.findElement(callQueueOpportunityLabel));
		clickElement(driver, queueRowLoc.findElement(callQueueCollapseButton));
		waitUntilInvisible(driver, queueRowLoc.findElement(callQueueCollapseButton));
		return opportunityName;
	}
	
	public void verifyQueueCallIsRed(WebDriver driver, String callerNumber) {
		WebElement queueRowLoc = findElement(driver, By.xpath(queueRow.replace("$$CallerNumber$$", callerNumber)));
		waitUntilVisible(driver, queueRowLoc.findElement(QueueCallerName));
		assertEquals(queueRowLoc.findElement(QueueCallerName).getCssValue("color"), "rgba(255, 69, 69, 1)");
	}

	public boolean isQueueSubscribed_Disabled(WebDriver driver, String queueName) {
		boolean value = false;
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By queueSubscribeButton = By.xpath(queueSubscribeBtn.replace("$$Queue$$", queueName));
		if (findElement(driver, queueSubscribeButton).getCssValue("display").contains("none")
				&& getAttribue(driver, queueSubscribeButton, ElementAttributes.Class).contains("disabled")) {
			value = true;
		} else {
			value = false;
		}
		toggleToCalls(driver);
		return value;
	}

	public void isQueueSubscribed(WebDriver driver, String queueName){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By queueSubscribeButton = By.xpath(queueSubscribeBtn.replace("$$Queue$$", queueName));
			if(findElement(driver, queueSubscribeButton).getCssValue("display").contains("none")) {
				System.out.println("queue " + queueName + " is subscribed");
			}else {
				Assert.fail();
			}
		toggleToCalls(driver);
	}
	
	public void isQueueUnsubscribed(WebDriver driver, String queueName){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By queueSubscribeButton = By.xpath(queueUnsubscribeBtn.replace("$$Queue$$", queueName));
			if(findElement(driver, queueSubscribeButton).getCssValue("display").contains("none")) {
				System.out.println("queue " + queueName + " is unsubscribed");
			}else {
				Assert.fail();									  
			}
		toggleToCalls(driver);
	}
	
	public boolean isGroupPresentInJoinQueue(WebDriver driver, String groupName) {
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By group = By.xpath(joinQueueName.replace("$$Group$$", groupName));
		boolean queuePresent = isElementVisible(driver, group, 5);
		toggleToCalls(driver);
		return queuePresent;
	}															   
 
	public void verifySubscribedQueueCount(WebDriver driver, String queueName, String queueCount){
		openCallQueuesSection(driver);
		toggleToQueues(driver);
		By subscribedQueueCountLoc = By.xpath(subscribedQueueCallsCount.replace("$$Queue$$", queueName));
		waitUntilTextPresent(driver, subscribedQueueCountLoc, queueCount);
		toggleToCalls(driver);
	}
	
	public void verifyQueueCount(WebDriver driver, String queueCount){
		waitUntilTextPresent(driver, queueCountLoc, queueCount);
	}
	
	public void queueCountInvisible(WebDriver driver)
	{
		waitUntilInvisible(driver, queueCountLoc);
	}
 
	public String getAvgQueueHoldTime(WebDriver driver, int queueIndex){
		toggleToQueues(driver);
		By holdTimeLoc = By.xpath(queueHoldTime.replace("$$index$$", String.valueOf(queueIndex + 1)));
		idleWait(1);
		String holdTime =  getElementsText(driver, holdTimeLoc);
		toggleToCalls(driver);
		return holdTime;
	}															 
}