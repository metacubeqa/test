/**
 * 
 */
package support.source.calls;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.DashBoardConversationAI;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallInspector extends SeleniumBase{
	
	CallsTabPage callsTabPage = new CallsTabPage();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	
	By callKey			= By.name("callId");
	By findBtn			= By.cssSelector("button.btn-success.go");
	By paperTrailBtn	= By.cssSelector(".papertrail button");
	By callDataHeading	= By.xpath(".//h2[text()='Call Data']");
	By callDuration		= By.xpath(".//h3[text()='Summary']/../following-sibling::div[1]//*[contains(text(), 'Duration')]/following-sibling::span");
	
	//Call Data locators
	By callKeyText			= By.xpath("//label[normalize-space(text()) = 'CallKey']/../span");
	By accountNameText		= By.xpath("//label[normalize-space(text()) = 'Account.Name']/../span");
	By directionText		= By.xpath(".//h3[text()='Summary']/../following-sibling::div[1]//*[contains(text(), 'Direction')]/following-sibling::span");
	By toNumberText		 	= By.xpath(".//h3[text()='Summary']/../following-sibling::div[1]//*[contains(text(), 'To Number')]/following-sibling::span");
	By fromNumberText		= By.xpath(".//h3[text()='Summary']/../following-sibling::div[1]//*[contains(text(), 'From Number')]/following-sibling::span");
	By callStatusText		= By.xpath(".//h3[text()='Summary']/../following-sibling::div[1]//*[contains(text(), 'Call Status')]/following-sibling::span");
	By ownerText			= By.xpath("//label[normalize-space(text()) = 'Owner']/../span");
	By ratingText			= By.xpath("//label[normalize-space(text()) = 'Rating']/../span");
	By notesText			= By.xpath("//label[normalize-space(text()) = 'Notes']/../span");
	By emailText			= By.xpath("//label[normalize-space(text()) = 'Email']/../span");
	By superVisorNotesText	= By.xpath("//label[normalize-space(text()) = 'Supervisor Notes']/../span");
	
	//Call details section
	By automatedVoiceMail	= By.xpath("//label[normalize-space(text()) = 'Automated VoicemailId']/../span");
	By deliveryStatus		= By.xpath("//label[normalize-space(text()) = 'Delivery Status']/../span");
	By legacyCallId			= By.xpath("//label[normalize-space(text()) = 'Legacy CallId']/../span");
	By mergedFromCallKey	= By.xpath("//label[normalize-space(text()) = 'Merged From CallKey']/../span");
	By noAnswerNumber		= By.xpath("//label[normalize-space(text()) = 'No Answer Number']/../span");
	By enteredHold			= By.xpath("//label[normalize-space(text()) = 'Entered Hold']/../span");
	By toSmartNoId			= By.xpath("//label[normalize-space(text()) = 'To SmartNumberId']/../span");
	By callFlow				= By.xpath("//label[normalize-space(text()) = 'Call Flow']/../span");
	By callFlowStepId		= By.xpath("//label[normalize-space(text()) = 'Call Flow Step Id']/../span");
	By directConnect		= By.xpath("//label[normalize-space(text()) = 'Direct Connect']/../span");
	By localPresence		= By.xpath("//label[normalize-space(text()) = 'Local Presence']/../span");
	By mergedToCallKey		= By.xpath("//label[normalize-space(text()) = 'Merged To CallKey']/../span");
	By onHold				= By.xpath("//label[normalize-space(text()) = 'On Hold']/../span");
	By storageBackend		= By.xpath("//label[normalize-space(text()) = 'Storage Backend']/../span");
	By transferredToNumber	= By.xpath("//label[normalize-space(text()) = 'Transferred To Number']/../span");
	
	//SalesForce section
	By salesForceHeading	= By.xpath(".//h3[text()='Salesforce']");
	By matchType			= By.xpath("//label[normalize-space(text()) = 'Match Type']/../span");
	By campaignId			= By.xpath("//label[normalize-space(text()) = 'CampaignMemberId']/../span");
	By disposition			= By.xpath("//label[normalize-space(text()) = 'Disposition']/../span");
	By taskId				= By.xpath("//label[normalize-space(text()) = 'TaskId']/../span");
	By recordId				= By.xpath("//label[normalize-space(text()) = 'Initiating RecordId']/../span");
	By subject				= By.xpath("//label[normalize-space(text()) = 'Subject']/../span");
	
	//Call Recording section
	By recordingUrl			= By.xpath("//label[normalize-space(text()) = 'Recording URL']/../span");
	By playerUrl			= By.xpath("//label[normalize-space(text()) = 'Player URL']/../span");
	By mediaSource			= By.xpath(".//*[@name='media']/source");
	By notificationSent		= By.xpath("//label[normalize-space(text()) = 'Notification Sent']/../span");
	By sid					= By.xpath("//label[normalize-space(text()) = 'Sid']/../span");
	By transcription		= By.xpath("//label[normalize-space(text()) = 'Transcription']/../span");
	By status				= By.xpath("//label[normalize-space(text()) = 'Status']/../span");
	By callRecordingDuration = By.xpath(".//*[text()='Call Recording']/parent::div//following-sibling::div//label[normalize-space(text()) = 'Duration']/../span");
	
	//Joined recording section
	By joinedRecordingUrl   = By.xpath(".//label[normalize-space(text())='URL']/following-sibling::span//a");
	
	//Conversation AI section
	By caiID			    = By.xpath("//label[normalize-space(text()) = 'ID']/../span");
	By mediaID				= By.xpath("//label[normalize-space(text()) = 'Media ID']/../span");
	By caistatus			= By.xpath(".//*[h2[text()='Conversation AI']]/following-sibling::div//label[normalize-space(text()) = 'Status']/../span");
	By rawData				= By.xpath(".//*[h2[text()='Conversation AI']]/following-sibling::div//label[normalize-space(text()) = 'Raw Data']/..//button");
	By caiRawDataView		= By.cssSelector("pre#voicebase-callback");
	By createdDate			= By.xpath(".//*[h2[text()='Conversation AI']]/following-sibling::div//label[normalize-space(text()) = 'Created Date']/../span");
	By updatedDate			= By.xpath(".//*[h2[text()='Conversation AI']]/following-sibling::div//label[normalize-space(text()) = 'Updated Date']/../span");
	By caiPlayer			= By.xpath(".//*[h2[text()='Conversation AI']]/following-sibling::div//label[normalize-space(text()) = 'Player']/..//a");			
	
	By mediaPlayer			= By.cssSelector("video[name='media']");
	
	public void enterCallkey(WebDriver driver, String callKeyText){
		waitUntilVisible(driver, callKey);
		enterText(driver, callKey, callKeyText);
	}

	public void clickFindButton(WebDriver driver){
		waitUntilVisible(driver, findBtn);
		clickElement(driver, findBtn);
	}
	
	public void clickPaperTrailBtn(WebDriver driver){
		waitUntilVisible(driver, paperTrailBtn);
		clickElement(driver, paperTrailBtn);
	}
	
	public int getCallDuration(WebDriver driver){
		waitUntilVisible(driver, callDuration);
		return Integer.parseInt(getElementsText(driver, callDuration).trim());
	}
	
	public void getCallData(WebDriver driver, String callKeyText){
		enterCallkey(driver, callKeyText);
		clickFindButton(driver);
		waitUntilVisible(driver, callDataHeading);
	}
	
	//Call Data Section
	
	public String getCallKey(WebDriver driver){
		return getElementsText(driver, callKeyText);
	}
	public String getAccountName(WebDriver driver){
		return getElementsText(driver, accountNameText);
	}
	public String getDirection(WebDriver driver){
		return getElementsText(driver, directionText);
	}
	
	public String getToNumber(WebDriver driver){
		return getElementsText(driver, toNumberText);
	}
	
	public String getFromNumber(WebDriver driver){
		return getElementsText(driver, fromNumberText);
	}
	
	public String getRating(WebDriver driver){
		return getElementsText(driver, ratingText);
	}
	
	public String getNotes(WebDriver driver){
		return getElementsText(driver, notesText);
	}
	
	public String getSuperVisorNotes(WebDriver driver){
		return getElementsText(driver, superVisorNotesText);
	}
	
	public String getOwner(WebDriver driver){
		return getElementsText(driver, ownerText);
	}
	
	public String getEmail(WebDriver driver){
		return getElementsText(driver, emailText);
	}
	
	public String getStatus(WebDriver driver){
		return getElementsText(driver, callStatusText);
	}

	public void verifySalesForceDetailsVisible(WebDriver driver){
		assertTrue(isElementVisible(driver, salesForceHeading, 5));
		assertTrue(isElementVisible(driver, matchType, 5));
		assertTrue(isElementVisible(driver, campaignId, 5));
		assertTrue(isElementVisible(driver, disposition, 5));
		assertTrue(isElementVisible(driver, notesText, 5));
		assertTrue(isElementVisible(driver, superVisorNotesText, 5));
		assertTrue(isElementVisible(driver, taskId, 5));
		assertTrue(isElementVisible(driver, recordId, 5));
		assertTrue(isElementVisible(driver, ratingText, 5));
		assertTrue(isElementVisible(driver, subject, 5));
	}
	
	public void verifyCallDetailsSection(WebDriver driver){
		assertTrue(isElementVisible(driver, automatedVoiceMail, 5));
		assertTrue(isElementVisible(driver, deliveryStatus, 5));
		assertTrue(isElementVisible(driver, legacyCallId, 5));
		assertTrue(isElementVisible(driver, mergedFromCallKey, 5));
		assertTrue(isElementVisible(driver, noAnswerNumber, 5));
		assertTrue(isElementVisible(driver, enteredHold, 5));
		assertTrue(isElementVisible(driver, toSmartNoId, 5));
		assertTrue(isElementVisible(driver, callFlow, 5));
		assertTrue(isElementVisible(driver, callFlowStepId, 5));
		assertTrue(isElementVisible(driver, directConnect, 5));
		assertTrue(isElementVisible(driver, localPresence, 5));
		assertTrue(isElementVisible(driver, directConnect, 5));
		assertTrue(isElementVisible(driver, mergedToCallKey, 5));
		assertTrue(isElementVisible(driver, onHold, 5));
		assertTrue(isElementVisible(driver, storageBackend, 5));
		assertTrue(isElementVisible(driver, transferredToNumber, 5));
	}
	
	//call recording section
	
	public void verifyRecordingUrlRestricted(WebDriver driver){
		scrollToElement(driver, recordingUrl);
		assertEquals(getElementsText(driver, recordingUrl), "Restricted");
		assertNotEquals(findElement(driver, recordingUrl).getTagName(), "button", "Element is clickable");
		assertNotEquals(findElement(driver, recordingUrl).getTagName(), "a", "Element is clickable");
	}
	
	public void verifyPlayerUrlRestricted(WebDriver driver){
		scrollToElement(driver, recordingUrl);
		assertEquals(getElementsText(driver, recordingUrl), "Restricted");
		assertNotEquals(findElement(driver, recordingUrl).getTagName(), "button", "Element is clickable");
		assertNotEquals(findElement(driver, recordingUrl).getTagName(), "a", "Element is clickable");
	}
	
	public void verifyRecordingUrlOpen(WebDriver driver){
		WebElement openRecordingUrl = findElement(driver, recordingUrl).findElement(By.xpath("a"));
		scrollToElement(driver, openRecordingUrl);
		assertEquals(getElementsText(driver, openRecordingUrl), "Open");
	}
	
	public void verifyPlayerUrlOpen(WebDriver driver){
		WebElement openPlayerUrl = findElement(driver, playerUrl).findElement(By.xpath("a"));
		scrollToElement(driver, openPlayerUrl);
		assertEquals(getElementsText(driver, openPlayerUrl), "Open");
	}
	
	public void clickAndVerifyRecordingUrl(WebDriver driver, String s3BucketValue){
		Actions actions = new Actions(driver);
		WebElement openUrl = findElement(driver, recordingUrl).findElement(By.xpath("a"));
		
		//opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		assertTrue(driver.getCurrentUrl().contains("https://".concat(s3BucketValue).concat(".s3.amazonaws.com")));
		
		//closing tab
		closeTab(driver);
	}
	
	public void clickAndVerifyJoinedRecordingUrl(WebDriver driver){
		Actions actions = new Actions(driver);
		WebElement openUrl = findElement(driver, joinedRecordingUrl);
		
		//opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		assertTrue(driver.getCurrentUrl().contains("https://ringdna-jackson-test.s3.amazonaws.com"));
		assertTrue(isElementVisible(driver, mediaPlayer, 5));
		
		//closing tab
		closeTab(driver);
	}
	
	public void clickAndVerifyCAIPlayerUrl(WebDriver driver, String callKey){
		Actions actions = new Actions(driver);
		WebElement openUrl = findElement(driver, caiPlayer);
		
		//opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		assertTrue(driver.getCurrentUrl().contains("#smart-recordings/".concat(callKey)));

		//closing tab
		closeTab(driver);
	}
	
	public void verifyCallRecordingSection(WebDriver driver){
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, sid)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, notificationSent)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, callRecordingDuration)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, playerUrl)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, recordingUrl)));
		assertTrue(isElementVisible(driver, transcription, 5));
		assertTrue(isElementVisible(driver, status, 5));
	}
	
	public void verifyConversationAISection(WebDriver driver, String callKey){
		waitUntilVisible(driver, caiID);
		scrollToElement(driver, caiID);
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, caiID)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, mediaID)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, caistatus)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, rawData)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, caiPlayer)));
		scrollToElement(driver, rawData);
		clickElement(driver, rawData);
		assertTrue(isElementVisible(driver, caiRawDataView, 5));
		assertTrue(HelperFunctions.isJSONValid(getElementsText(driver, caiRawDataView)));
		clickAndVerifyCAIPlayerUrl(driver, callKey);
	}
}