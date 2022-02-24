/**
 *
 */
package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import base.SeleniumBase;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewTask;
import softphone.source.callTools.SoftPhoneNewTask.taskDropdownDates;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneActivityPage extends SeleniumBase {

	SoftPhoneNewTask softPhoneNewTask = new SoftPhoneNewTask();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	
	By spinnerWheel 					= By.className("spinner");
	
	By msgNumberSelector				= By.cssSelector(".message-sender-container img[title=\"SMS Phone Number Selector\"]");
	By msgNumberList					= By.cssSelector(".message-sender-container .sms-phone-selector .number-phone");
	By defaultMessagePlaceholder		= By.cssSelector(".sms-textarea[placeholder = 'Please enter your SMS message']");
	By messageTextBox 					= By.className("sms-textarea");
	By sendMessageBtn 					= By.cssSelector("[data-action='send-message']");
	By msgTemplateBtn					= By.cssSelector("[data-action='show-sms-template-selector'] [title='SMS Templates']");
	By inboundMessageList 				= By.cssSelector(".inbound-message.pull-left:not([style='display: none;']) .content");
	By outboundMessageList 				= By.cssSelector(".outbound-message:not([style='display: none;']) .content");
	By messageOwnerList					= By.cssSelector(".outbound-message:not([style='display: none;']) .owner, .inbound-message:not([style='display: none;']) .owner");
	By inboundMsgOwnerList				= By.cssSelector(".inbound-message:not([style='display: none;']) .owner");
	By outboundMsgOwnerList				= By.cssSelector(".outbound-message:not([style='display: none;']) .owner");
	By inboundMsgDateList				= By.cssSelector(".inbound-message.pull-left div span.datetime");
	By messageRestrictAlert				= By.cssSelector(".alert-danger .message-text");
	By closeMessageAlert 				= By.cssSelector(".close-error");
	By noRecordTextMessage 				= By.className("activity-no-tab");
	By messageOwner 					= By.cssSelector("#messageOwner");
	By messageOwnerDropdown 			= By.cssSelector("#ownerMessageSelector");
	By messageOwnerDropdownOptions 		= By.cssSelector("#ownerMessageSelector option");
	By spamLink 						= By.cssSelector(".report-spam:not([style='display: none;']) .report-link:not([style='display: none;'])");
	By spamLinkTextNotAssociated		= By.cssSelector(".message-item .row.report-spam:not([style='display: none;']) span:not(.ladda-label)");
	By blockAndDeleteBtn 				= By.cssSelector(".report-spam:not([style='display: none;']) button[data-action= 'block-delete']");
	By smsCount							= By.cssSelector(".sms-count");
	By characterCount					= By.cssSelector(".char-count");
	By tooltipWarning					= By.cssSelector(".tooltip-warning .warning-sms-sending-title");
	By tooltipDismiss					= By.cssSelector(".tooltip-warning .warning-sms-sending-dismiss");
	String outboundMessageTime			= ".//*[contains(@class, 'outbound-message')]/div[contains(@class,'content') and text() = '$$Message$$']/..//span[contains(@class, 'datetime')]";
	String smsTemplate 					= ".//*[@class='sms-template-name' and text() ='$$TemplateName$$']";
	
	By inactiveActivityTab				= By.cssSelector(".activity-tabs.disabled");
	By messageTabImg 					= By.cssSelector("[href='#messages'] img.inactive");
	By callActivities 					= By.cssSelector(".crm-activities .media-heading.subject");
	By openTaskTabActiveLink 			= By.cssSelector(".inactive[title='Open Tasks']");
	By emailTaskTabActiveLink 			= By.cssSelector("[title='Emails'].inactive");
	By inActiveCallsTabLink 			= By.cssSelector(".inactive[title='Calls']");
	By inActiveCallNotesTabLink 		= By.cssSelector("[href='#callsWithNotes']  img.inactive[title='Call Notes']");
	By inActiveAllActivityTabLink		= By.cssSelector("img[title='All'].inactive");
	
	By taskSubjectList					= By.cssSelector(".subject");
	By taskRelatedRecordList			= By.cssSelector(".crm-activities .related-object:not([style='display: none;']) .related-object-text");																															   
	By noSubjectTaskList				= By.xpath(".//*[contains(@class,'subject') and not(text())]");
	By noSubjectEventList				= By.xpath(".//*[contains(@class,'subject') and not(text())]/preceding-sibling::a[contains(@class, 'isFutureEvent')]");
	By noSubjectActivitycomments		= By.xpath(".//*[contains(@class,'subject') and not(text())]/../../..//*[contains(@class, 'comments')]"); 
	By noSubjectActivityEditBtn			= By.xpath(".//*[contains(@class,'subject') and not(text())]/.././/*[contains(@class, 'edit-task-icon')]");
	By noSubjectDueDate					= By.xpath(".//*[contains(@class,'subject') and not(text())]/../..//*[contains(@class, 'date-activity')]//small");
	By noSubjectTaskCompleteBarList		= By.xpath(".//*[contains(@class,'subject') and not(text())]/../../..//*[contains(@class, 'complete') and contains(@class, 'full-width')]");
	By noSubjectTaskUndoBarList		 	= By.xpath(".//*[contains(@class,'subject') and not(text())]/../../..//*[@class='completed-task']");
	
	String taskInList 					= ".//*[contains(@class,'subject') and text()=\"$$Subject$$\"]";
	String taskCommentExpanDownArrow	= ".//*[text()='$$Subject$$']/..//span[contains(@class,'glyphicon-chevron-down')]";
	String taskCommentInList 			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/../../..//*[contains(@class,'comments')]";
	String taskEditButtonLoc 			= ".//*[contains(@class,'subject') and text()=\"$$Subject$$\"]/parent::*[@class='media']//*[contains(@class,'edit-task-icon')]";
	String taskCompleteBarLoc			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[contains(@class, 'complete') and contains(@class, 'full-width')]";
	String taskUndoBarLoc 				= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[@class='completed-task']";
	String taskCompleteArrowButton 		= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[contains(@class,'complete-dropdown')]";
	String taskCompleteAndNewButton 	= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[@class='complete-and-new']";
	String taskListCalenderButton 		= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[contains(@class, 'glyphicon-calendar')]";
	String taskContainer				= ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::div[contains(@class, 'activity-item')]";
	String taskdueDateContainer			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/../..//*[contains(@class, 'date-activity')]";
	String taskDueDate					= ".//*[text()='$$Subject$$']/following-sibling::div/*[contains(@class, 'date-activity')]//small";
	String taskRecordingPlayBtn			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/..//button[contains(@class,'play-recording')]";
	String taskRecPlayCurerntTime		= ".//*[contains(@class,'subject') and text()='$$Subject$$']/..//span[contains(@class,'mejs-currenttime')]";
	String taskownername				= ".//*[contains(@class,'subject') and text()='$$Subject$$']/..//*[contains(@class,'owner')]";
	String taskRelatedRecord			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/..//*[contains(@class,'related-object-text')]";
	String openTaskIcon					= "//*[contains(@class,'subject') and text()='$$Subject$$']/..//img[contains(@class, 'isOpenTask')]";	
	String taskDispositionTask			= ".//*[contains(@class,'subject') and text()='$$Subject$$']/..//div[@class='disposition' and not(@style='display: none;')]//span[@class='disposition-text']";
	
	String taskListDueDateDropdownDates = ".//*[contains(@class,'subject') and text()='$$Subject$$']/ancestor::*[@class='container-task']//*[@class='date-menu']";
	By taskSubject 						= By.cssSelector("input.subject-select");
	By taskTypeSelectedOption			= By.cssSelector(".selectize-control.task-types.single .item");
	By taskPrioritiesSelectedOption		= By.cssSelector(".task-priorities.single.selectize-control .item");
	By taskStatusesSelectedOption		= By.cssSelector(".task-statuses.single.selectize-control .item");
	By selectedRelatedRecord 			= By.cssSelector("input.edit-related-record");
	By taskDeleteButton					= By.cssSelector("#edit-form .delete");
	By addReminderButton 				= By.cssSelector("button.toggle-reminder-btn");
	By taskReminderDate 				= By.cssSelector(".reminderDate");
	By taskReminderTime 				= By.cssSelector(".reminder-time");
	By reminderCalendarButton 			= By.cssSelector(".reminder-tool .glyphicon-calendar");
	By taskComments 						= By.cssSelector("textarea.edit-comments");
	By reminderClndarDropdownDatesList 	= By.cssSelector(".reminder-tool .dropdown-menu.quick-date .date");
	
	By chatterIcons						= By.xpath(".//*[text()='Chatter Post']/preceding-sibling::div/img[@src='images/icon-chatter.svg']");
	By chatterCommentsLoc				= By.xpath(".//*[text()='Chatter Post']/following-sibling::div/small/div[contains(@class,'comments')]");
	
	By taskEditSaveButton 				= By.cssSelector(".container-task button[data-action='save']");
	By taskEditCancelButton 			= By.cssSelector(".container-task button[data-action='cancel']");
	By taskWarningDeleteButton			= By.cssSelector("#edit-form .delete-warning .delete");
	By taskEditSaveAndNewTaskArrow 		= By.xpath(".//*[@data-action='save']/following-sibling::button[contains(@class,'dropdown-button')]");
	By taskEditSaveAndNewTaskButton 	= By.className("save-and-new");
	
	// event edit
	By eventEditSubject 				= By.cssSelector("input.subject-select");
	By eventEditAllDayCheckBox 			= By.cssSelector("input.allDay");
	By eventEditStartDate 				= By.cssSelector("input.start-date");
	By eventEditStartTime 				= By.cssSelector("input.start-time");
	By eventEditEndDate 				= By.cssSelector("input.end-date");
	By eventEditEndTime 				= By.cssSelector("input.end-time");
	By eventEditReminderCheckBox 		= By.cssSelector("input.reminder-set");

	By activeEventActivityList 			= By.cssSelector(".activity-item.event");

	By eventIconFuture 					= By.xpath(".//*[text()='$$Subject$$']/../../preceding-sibling::div//*[@src='images/icon-event-upcoming.svg']");
	By eventIconPast 					= By.xpath(".//*[text()='$$Subject$$']/../../preceding-sibling::div//*[@src='images/icon-event.svg']");
	By eventContainerDate 				= By.xpath(".//*[text()='$$Subject$$']/following-sibling::div[@class='date-activity event']/b");
	By eventContainerOwner 				= By.xpath(".//*[text()='$$Subject$$']/following-sibling::div/*[contains(@class, 'owner')]");
	By eventContainerCreatedDate 		= By.xpath(".//*[text()='$$Subject$$']/following-sibling::div/*[contains(@class, 'createdDate')]");
	By eventContainerDescription 		= By.xpath(".//*[contains(@class,'subject') and text()='$$Subject$$']//ancestor::div[contains(@class,'event')]//*[contains(@class,'comments')]");
	String eventContainerDateLocator 	= ".//*[text()='$$Subject$$']/following-sibling::div/*[@class='date-activity event']/b";
	String eventContainerOwnerLocator 	= ".//*[text()='$$Subject$$']/following-sibling::div//*[contains(@class, 'owner')]";
	String eventContainerCreatedDateLocator = ".//*[text()='$$Subject$$']/following-sibling::div//*[contains(@class, 'createdDate')]";
	String eventIconFutureLocator 		= ".//*[text()='$$Subject$$']/preceding-sibling::div//*[@src='images/icon-event-upcoming.svg']";
	String eventIconPastLocator 		= ".//*[text()='$$Subject$$']/preceding-sibling::div//*[@src='images/icon-event.svg']";
	String eventContainerDescriptionLocator = ".//*[contains(@class,'subject') and text()='$$Subject$$']//ancestor::div[contains(@class,'event')]//*[contains(@class,'comments')]";
	
	//Conversation AI selectors
	String conversationAILink   		= ".//*[text()='$callNoteSubject$']/parent::div//small[@class='pull-right']/a";
	
	String conversationAIRelatedRecordLink	= ".//*[text()='$relatedRecord$']/../.././..//small/a";
	
	public boolean isActivityTabActive(WebDriver driver, By locator) {
		WebElement parent = findElement(driver, locator).findElement(By.xpath("ancestor::li"));
		if (parent.getAttribute("class").contains("active")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void verifyAcitivityTabEnabled(WebDriver driver){
		isElementVisible(driver, inactiveActivityTab, 2);										   
		waitUntilInvisible(driver, inactiveActivityTab);
	}
	
	public boolean isMsgTabActive(WebDriver driver) {
		return isActivityTabActive(driver, messageTabImg);
	}
	
	public void openMessageTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(findElement(driver, messageTabImg).isDisplayed()){
			clickElement(driver, messageTabImg);
			waitUntilInvisible(driver, spinnerWheel);
			waitUntilVisible(driver, messageTextBox);
		}
	}
	
	public String selectMessagingNumber(WebDriver driver, int index){
		String text = "";
		if(isElementVisible(driver, msgNumberSelector, 0)) {
			clickElement(driver, msgNumberSelector);
			text = getElementsText(driver, getElements(driver, msgNumberList).get(index));
			getElements(driver, msgNumberList).get(index).click();
			waitUntilInvisible(driver, msgNumberList);
		}
		return text;
	}

	public void sendMessage(WebDriver driver, String message, int useNumberIndex) {
		waitUntilVisible(driver, messageTextBox);
		waitUntilVisible(driver, sendMessageBtn);
		selectMessagingNumber(driver, useNumberIndex);
		waitUntilInvisible(driver, overlayWorkSpace);
		enterMessageText(driver, message);
		clickElement(driver, sendMessageBtn);
		isListElementsVisible(driver, outboundMessageList, 2);
		List<String> outboundMessageList = getOuboundMessageList(driver);
		outboundMessageList.get(outboundMessageList.size() - 1).equals(message);
	}

	public void enterMessageText(WebDriver driver, String message){
		waitUntilVisible(driver, messageTextBox);
		enterText(driver, messageTextBox, message);
	}
	
	public void clearAllMsgTextBox(WebDriver driver) {
		waitUntilVisible(driver, messageTextBox);
		clickElement(driver, messageTextBox);
		clearAll(driver, messageTextBox);
	}
	
	public String getToolTipWarningOnSendbtn(WebDriver driver) {
		waitUntilVisible(driver, tooltipWarning);
		return getElementsText(driver, tooltipWarning);
	}
	
	public void clickToolTipDismiss(WebDriver driver) {
		waitUntilVisible(driver, tooltipDismiss);
		clickElement(driver, tooltipDismiss);
	}
	
	public void clickSendButton(WebDriver driver){
		selectMessagingNumber(driver, 0);
		waitUntilVisible(driver, sendMessageBtn);
		clickElement(driver, sendMessageBtn);
	}
	
	public boolean isSendBtnVisible(WebDriver driver) {
		return isElementVisible(driver, sendMessageBtn, 5);
	}
	
	public boolean isSendBtnDisabled(WebDriver driver) {
		return isElementDisabled(driver, sendMessageBtn, 5);
	}
	
	public String getMessageText(WebDriver driver){
		waitUntilVisible(driver, messageTextBox);
		return getAttribue(driver, messageTextBox, ElementAttributes.value);
	}
	
	public String getMessagePlaceholderText(WebDriver driver){
		waitUntilVisible(driver, messageTextBox);
		waitUntilInvisible(driver, defaultMessagePlaceholder);
		idleWait(2);
		return getAttribue(driver, messageTextBox, ElementAttributes.Placeholder);
	}
	
	public List<String> getOuboundMessageList(WebDriver driver) {
		List<String> messagList = getTextListFromElements(driver, outboundMessageList);
		messagList.replaceAll(String::trim);
		return messagList;
	}

	public List<WebElement> getInboundMessageList(WebDriver driver) {
		return getElements(driver, inboundMessageList);
	}
	
	public void verifyAllMessagesOwner(WebDriver driver, String ownerName) {
		List<String> messagesOwnersList = getTextListFromElements(driver, messageOwnerList);
		for (String owner : messagesOwnersList) {
	        assertEquals(owner, ownerName);
	    }
	}
	
	public void verifyInboundMessagesOwner(WebDriver driver, String ownerName) {
		List<String> messagesOwnersList = getTextListFromElements(driver, inboundMsgOwnerList);
		for (String owner : messagesOwnersList) {
	        assertEquals(owner, ownerName);
	    }
	}
	
	public void verifyOutboundMessagesOwner(WebDriver driver, String ownerName) {
		List<String> messagesOwnersList = getTextListFromElements(driver, outboundMsgOwnerList);
		for (String owner : messagesOwnersList) {
	        assertEquals(owner, ownerName);
	    }
	}
	
	public void verifyInboundMessageListNotPresent(WebDriver driver){
		waitUntilInvisible(driver, inboundMessageList);
	}

	public List<String> getInboundMsgDateTimeList(WebDriver driver){
		return getTextListFromElements(driver, inboundMsgDateList);
	}
	
	public boolean verifyInboundMsgDateInGivenFormat(String format, String date){
		return HelperFunctions.isDateInGivenFormat(format, date);
	}
	
	public boolean verifyInboundMessage(WebDriver driver, String message) {
		List<WebElement> inboundMessageList = getInboundMessageList(driver);
		if (inboundMessageList.get(inboundMessageList.size()-1).getText().trim().equals(message)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyOutboundMessage(WebDriver driver, String message) {
		List<String> outboundMessagesList = getOuboundMessageList(driver);
		if (outboundMessagesList.get(outboundMessagesList.size() - 1).equals(message)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void verifyRestrictSmsMsg(WebDriver driver) {
		waitUntilVisible(driver, messageRestrictAlert);
		assertEquals(getElementsText(driver, messageRestrictAlert),
				"The party you are trying to text has blocked texts from this number.");
		clickElement(driver, closeMessageAlert);
	}
	
	public String getOutboundMsgDateTime(WebDriver driver, String message){
		By messageDateTime = By.xpath(outboundMessageTime.replace("$$Message$$", message));
		waitUntilVisible(driver, messageDateTime);
		return getElementsText(driver, messageDateTime);
	}
	
	public String getMessageOwnerName(WebDriver driver) {
		waitUntilVisible(driver, messageOwner);
		return getElementsText(driver, messageOwner);
	}
	
	public List<String> getMessageOwnerNamesList(WebDriver driver) {
		waitUntilVisible(driver, messageOwnerDropdown);
		clickElement(driver, messageOwnerDropdown);
		waitUntilVisible(driver, messageOwnerDropdownOptions);
		return getTextListFromElements(driver, messageOwnerDropdownOptions);
	}
	
	public String getSelectedMessageOwner(WebDriver driver) {
		waitUntilVisible(driver, messageOwnerDropdown);
		waitUntilClickable(driver, messageOwnerDropdown);
		return getSelectedValueFromDropdown(driver, messageOwnerDropdown);
	}
	
	public boolean isMsgHasMultipleOwner(WebDriver driver) {
		return isElementVisible(driver, messageOwnerDropdown, 0);
	}
	
	public void selectAllFromMessageOwnerDropDown(WebDriver driver) {
		selectFromDropdown(driver, messageOwnerDropdown, SelectTypes.visibleText, "All");
	}
	
	public void selectMessageOwnerFromDropDown(WebDriver driver, String owner) {
		selectFromDropdown(driver, messageOwnerDropdown, SelectTypes.visibleText, owner);
	}
	
	public void isSpamLinkPresent(WebDriver driver){
		waitUntilVisible(driver, spamLink);
	}
	
	public void verifySpamNotAssociatedText(WebDriver driver) {
		waitUntilVisible(driver, spamLinkTextNotAssociated);
		assertEquals(getElementsText(driver, spamLinkTextNotAssociated), "This number is not associated with an existing lead or contact.");
	}
	
	public void isSpamLinkNotPresent(WebDriver driver){
		waitUntilInvisible(driver, spamLink);
	}
	
	public void reportNumberAsSpam(WebDriver driver){
		isSpamLinkPresent(driver);
		clickByJs(driver, spamLink);
		waitUntilVisible(driver, blockAndDeleteBtn);
		clickElement(driver, blockAndDeleteBtn);
		isElementVisible(driver, spinnerWheel, 3);
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void clickSmsTemplateBtn(WebDriver driver){
		waitUntilVisible(driver, msgTemplateBtn);
		clickElement(driver, msgTemplateBtn);
	}
	
	public boolean isSMSTemplateBtnVisible(WebDriver driver) {
		return isElementVisible(driver, msgTemplateBtn, 5);
	}
	
	public void SelectTemplate(WebDriver driver, String templateName){
		clickSmsTemplateBtn(driver);
		By smsTemplateOption = By.xpath(smsTemplate.replace("$$TemplateName$$", templateName));
		waitUntilVisible(driver, smsTemplateOption);
		clickElement(driver, smsTemplateOption);
	}
	
	public boolean isTemplateVisible(WebDriver driver, String templateName) {
		clickSmsTemplateBtn(driver);
		By smsTemplateOption = By.xpath(smsTemplate.replace("$$TemplateName$$", templateName));
		return isElementVisible(driver, smsTemplateOption, 2);
	}
	
	public int getSMSCount(WebDriver driver) {
		waitUntilVisible(driver, smsCount);
		return Integer.parseInt(getElementsText(driver, smsCount));
	}
	
	public int getCharacterCount(WebDriver driver) {
		waitUntilVisible(driver, characterCount);
		return Integer.parseInt(getElementsText(driver, characterCount));
	}
	
/*******Messaging section ends here*******/
	
	public void navigateToAllActivityTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(findElement(driver, inActiveAllActivityTabLink).isDisplayed()){
			clickElement(driver, inActiveAllActivityTabLink);
		}
		verifyAcitivityTabEnabled(driver);
	}
	
	public void navigateToCallNotesTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(findElement(driver, inActiveCallNotesTabLink).isDisplayed()){
			clickElement(driver, inActiveCallNotesTabLink);
		}
		verifyAcitivityTabEnabled(driver);
	}
	
	public void navigateToCallsTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(isElementVisible(driver, inActiveCallsTabLink,3)){
			clickElement(driver, inActiveCallsTabLink);
		}
		verifyAcitivityTabEnabled(driver);
	}

	public void openActivityInNewWindow(WebDriver driver, int index) {
		getElements(driver, callActivities).get(index).click();
	}
	
	public void navigateToOpenTasksTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(isElementVisible(driver, openTaskTabActiveLink, 3)){
			clickElement(driver, openTaskTabActiveLink);
		}
		verifyAcitivityTabEnabled(driver);
	}
	
	public void navigateToEmailTasksTab(WebDriver driver){
		verifyAcitivityTabEnabled(driver);
		if(isElementVisible(driver, emailTaskTabActiveLink, 3)){
			clickElement(driver, emailTaskTabActiveLink);
		}
		verifyAcitivityTabEnabled(driver);
	}

	public void clickTaskEditButton(WebDriver driver, String taskName) {
		By taskEditButton = By.xpath(taskEditButtonLoc.replace("$$Subject$$", taskName));
		clickElement(driver, taskEditButton);
	}

	public void clickTaskCompleteBar(WebDriver driver, String taskName) {
		By taskCompletebar = By.xpath(taskCompleteBarLoc.replace("$$Subject$$", taskName));
		clickElement(driver, taskCompletebar);
	}

	public void isTaskUndoBarVisible(WebDriver driver, String taskName) {
		By taskUndoBar = By.xpath(taskUndoBarLoc.replace("$$Subject$$", taskName));
		waitUntilVisible(driver, taskUndoBar);
	}

	public void clickTaskDeleletButton(WebDriver driver) {
		waitUntilVisible(driver, taskDeleteButton);
		clickElement(driver, taskDeleteButton);
	}

	public void clickTaskWarningDeleteButton(WebDriver driver) {
		waitUntilVisible(driver, taskWarningDeleteButton);
		clickElement(driver, taskWarningDeleteButton);
		waitUntilInvisible(driver, taskWarningDeleteButton);
	}

	public void isTaskVisibleInActivityList(WebDriver driver, String taskName) {
		By taskInActivityList = By.xpath(taskInList.replace("$$Subject$$", taskName));
		waitUntilVisible(driver, taskInActivityList);
	}

	public void isTaskInvisibleInActivityList(WebDriver driver, String taskName) {
		By taskInActivityList = By.xpath(taskInList.replace("$$Subject$$", taskName));
		waitUntilInvisible(driver, taskInActivityList);
	}
	
	public String getCommentFromTaskList(WebDriver driver, String taskSubject){
		By commentLocator = By.xpath(taskCommentInList.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, commentLocator);
		return getElementsText(driver, commentLocator);
	}
	
	public Boolean isRecordingPlayBtnVisible(WebDriver driver, String taskSubject){
		By playRecordingLocator = By.xpath(taskRecordingPlayBtn.replace("$$Subject$$", taskSubject));
		return isElementVisible(driver, playRecordingLocator, 2);
	}
	
	public void playTaskRecording(WebDriver driver, String taskSubject){
		By taskRecordingPlayBtnLoc = By.xpath(taskRecordingPlayBtn.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, taskRecordingPlayBtnLoc);
		clickElement(driver, taskRecordingPlayBtnLoc);
	}
	
	public boolean isPlayRecordingBtnVisible(WebDriver driver, String taskSubject){
		By taskRecordingPlayBtnLoc = By.xpath(taskRecordingPlayBtn.replace("$$Subject$$", taskSubject));
		return isElementVisible(driver, taskRecordingPlayBtnLoc, 1);
	}
	
	public int getRecordingPlayerCurrentTime(WebDriver driver, String taskSubject){
		By taskRecPlayCurerntTimeLoc = By.xpath(taskRecPlayCurerntTime.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, taskRecPlayCurerntTimeLoc);
		return Integer.parseInt(getElementsText(driver, taskRecPlayCurerntTimeLoc).substring(3,5));
	}
	
	public int getNumberOfRecordings(WebDriver driver, String taskSubject){
		By taskRecPlayCurerntTimeLoc = By.xpath(taskRecPlayCurerntTime.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, taskRecPlayCurerntTimeLoc);
		return getElements(driver, taskRecPlayCurerntTimeLoc).size();
	}
	
	public void verifyRecordingIsDeleted(WebDriver driver, String taskSubject){
		playTaskRecording(driver, taskSubject);
		idleWait(1);
		assertEquals(driver.switchTo().alert().getText(), "Sorry, the voicemail was deleted.");
		acceptAlert(driver);
		assertFalse(isRecordingPlayBtnVisible(driver, taskSubject));
	}
	
	public void playRecordingBtnIsInvisible(WebDriver driver, String taskSubject) {
		assertFalse(isRecordingPlayBtnVisible(driver, taskSubject));
	}																		
															  
	public String getTaskOwnerName(WebDriver driver, String taskSubject){
		By taskOwnerNameLocator = By.xpath(taskownername.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, taskOwnerNameLocator);
		return getElementsText(driver, taskOwnerNameLocator);
	}
	
	public String getTaskRelatedRecord(WebDriver driver, String taskSubject){
		By taskRelatedRecordLocator = By.xpath(taskRelatedRecord.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, taskRelatedRecordLocator);
		return getElementsText(driver, taskRelatedRecordLocator);
	}
 
	public Boolean getIfTaskCommentIsMultiLine(WebDriver driver, String taskSubject){
		By commentExpandLocator = By.xpath(taskCommentExpanDownArrow.replace("$$Subject$$", taskSubject));
		By commentLocator = By.xpath(taskCommentInList.replace("$$Subject$$", taskSubject));
		waitUntilVisible(driver, commentExpandLocator);
		clickElement(driver, commentExpandLocator);
		idleWait(1);
		return Boolean.parseBoolean(getAttribue(driver, commentLocator, ElementAttributes.AriaExpanded));
	}
	
	public void verifyOpenTasksRedBorder(WebDriver driver, String taskName) {
		By taskContainerLoc = By.xpath(taskContainer.replace("$$Subject$$", taskName));
		assertEquals(findElement(driver, taskContainerLoc).getCssValue("border-bottom-color"), "rgba(255, 69, 69, 1)");
		By taskDueDateLoc = By.xpath(taskdueDateContainer.replace("$$Subject$$", taskName));
		assertEquals(findElement(driver, taskDueDateLoc).getCssValue("border-bottom-color"), "rgba(255, 69, 69, 1)");
	}
	
	
	public void verifyTaskDisposition(WebDriver driver, String taskName, String disposition) {
		if(disposition == null) {
			waitUntilInvisible(driver, By.xpath(taskDispositionTask.replace("$$Subject$$", taskName)));
		}else {
			waitUntilTextPresent(driver, By.xpath(taskDispositionTask.replace("$$Subject$$", taskName)), disposition);
		}
	}
	
	public void verifyOpenTasksGreenBorder(WebDriver driver, String taskName) {
		By taskContainerLoc = By.xpath(taskContainer.replace("$$Subject$$", taskName));
		assertEquals(findElement(driver, taskContainerLoc).getCssValue("border-bottom-color"), "rgba(51, 204, 153, 1)");
		By taskDueDateLoc = By.xpath(taskdueDateContainer.replace("$$Subject$$", taskName));
		assertEquals(findElement(driver, taskDueDateLoc).getCssValue("border-bottom-color"), "rgba(51, 204, 153, 1)");
	}
	
	public String getTaskDueDate(WebDriver driver, String taskName) {
		isTaskVisibleInActivityList(driver, taskName);
		By taskdueDateLoc = By.xpath(taskDueDate.replace("$$Subject$$", taskName));
		return getElementsText(driver, taskdueDateLoc).replace("DUE: ", "");
	}
	
	public void getTaskDueDateNotPresent(WebDriver driver, String taskName) {
		isTaskVisibleInActivityList(driver, taskName);
		By taskdueDateLoc = By.xpath(taskDueDate.replace("$$Subject$$", taskName));
		waitUntilInvisible(driver, taskdueDateLoc);
	}
	
	public void verifyOpenTasksNoBorder(WebDriver driver, String taskName) {
		By taskContainerLoc = By.xpath(taskContainer.replace("$$Subject$$", taskName));
		assertEquals(findElement(driver, taskContainerLoc).getCssValue("border-bottom-color"), "rgba(204, 204, 204, 1)");
	}
	
	public Boolean isOpenTasksIcon(WebDriver driver, String taskName) {
		By openTaskIconLoc = By.xpath(openTaskIcon.replace("$$Subject$$", taskName));
		return isElementVisible(driver, openTaskIconLoc, 0);
	}

	public void clickTaskEditSaveButton(WebDriver driver) {
		clickElement(driver, taskEditSaveButton);
	}

	public void clickTaskEditDeleteButton(WebDriver driver) {
		clickElement(driver, taskDeleteButton);
	}

	public void clickTaskEditCancelButton(WebDriver driver) {
		clickElement(driver, taskEditCancelButton);
	}

	public void verifyNoRecordTextMessage(WebDriver driver, String message) {
		waitUntilTextPresent(driver, noRecordTextMessage, message);
	}
	
	public String getTaskSubject(WebDriver driver){
		return getAttribue(driver, taskSubject, ElementAttributes.value);
	}
	
	public String getTaskComments(WebDriver driver){
		return getAttribue(driver, taskComments, ElementAttributes.value);
	}

	public String getTaskRelatedRecord(WebDriver driver){
		return 	getAttribue(driver, selectedRelatedRecord, ElementAttributes.value);
	}
	
	public String getTaskType(WebDriver driver) {
		return getElementsText(driver, taskTypeSelectedOption);
	}

	public String getTaskPriority(WebDriver driver) {
		return getElementsText(driver, taskPrioritiesSelectedOption);
	}

	public String getTaskStatus(WebDriver driver) {
		return getElementsText(driver, taskStatusesSelectedOption);
	}

	public void verifyTaskInAllTaskTab(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
	}

	public void verifyTaskInOpenTaskTab(WebDriver driver, String taskName) {
		navigateToOpenTasksTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
	}

	public void verifyTaskInEmailTaskTab(WebDriver driver, String taskName) {
		navigateToEmailTasksTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
	}
	
	public void verifyTaskInCallsTab(WebDriver driver, String taskName) {
		navigateToCallsTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
	}

	public void verifyTaskInvisibleOpenTaskTab(WebDriver driver, String taskName) {
		navigateToOpenTasksTab(driver);
		isTaskInvisibleInActivityList(driver, taskName);
	}

	public void changeTaskDueDateFromList(WebDriver driver, String taskName, String dueDate) {
		clickElement(driver, By.xpath(taskListCalenderButton.replace("$$Subject$$", taskName)));
		List<WebElement> reminderDropdownDates = getElements(driver,
				By.xpath(taskListDueDateDropdownDates.replace("$$Subject$$", taskName)));
		if (dueDate.equals(taskDropdownDates.Today.toString())) {
			reminderDropdownDates.get(0).click();
		} else if (dueDate.equals(taskDropdownDates.Tomorrow.toString())) {
			reminderDropdownDates.get(1).click();
		}
	}

	public void verifyTaskInvisibleEmailTaskTab(WebDriver driver, String taskName) {
		navigateToEmailTasksTab(driver);
		isTaskInvisibleInActivityList(driver, taskName);
	}

	public void verifyMessageTask(WebDriver driver, String userNumber, String messageText) {
		String taskName = "Outbound Message: " + userNumber.trim();
		verifyTaskInAllTaskTab(driver, taskName);
		verifyOpenTasksNoBorder(driver, taskName);
		String actualMessage = getCommentFromTaskList(driver, taskName);
		assertEquals(messageText, actualMessage);
	}

	public void verifyOutboundCallTask(WebDriver driver, String calledNumber) {
		String taskName = "Outbound Call: " + calledNumber.trim();
		verifyTaskInAllTaskTab(driver, taskName);
	}

	public void verifyInboundCallTask(WebDriver driver, String callerNumber) {
		String taskName = "Inbound Call: " + callerNumber.trim();
		verifyTaskInAllTaskTab(driver, taskName);
		verifyTaskInAllTaskTab(driver, taskName);
	}

	public void markTaskAsComplete(WebDriver driver, String taskName) {
		clickTaskCompleteBar(driver, taskName);
		isTaskUndoBarVisible(driver, taskName);
		idleWait(1);
	}

	public void markTaskCompleteAndOpenNewTask(WebDriver driver, String taskName) {
		By taskMarkCompleteArrowButton = By.xpath(taskCompleteArrowButton.replace("$$Subject$$", taskName));
		clickElement(driver, taskMarkCompleteArrowButton);
		By taskMarkCompleteAndNewButton = By.xpath(taskCompleteAndNewButton.replace("$$Subject$$", taskName));
		clickElement(driver, taskMarkCompleteAndNewButton);
	}
	
	public void markTaskAsCompleteNoSubject(WebDriver driver, int index) {
		getInactiveElements(driver, noSubjectTaskCompleteBarList).get(index).click();
		waitUntilVisible(driver, getInactiveElements(driver, noSubjectTaskUndoBarList).get(index));
		idleWait(1);
	}

	public void editTask(WebDriver driver, String taskName) {
		callToolsPanel.isRelatedRecordsIconVisible(driver);
		By taskInActivityList = By.xpath(taskInList.replace("$$Subject$$", taskName));
		navigateToAllActivityTab(driver);
		scrollToLastTask(driver);
		scrollToElement(driver, taskInActivityList);
		clickTaskEditButton(driver, taskName);
		scrollToElement(driver, taskEditSaveButton);
	}
	
	public void scrollToLastTask(WebDriver driver) {
		List<WebElement> taskList = getInactiveElements(driver, taskSubjectList);
		scrollToElement(driver, taskList.get(taskList.size()-1));
	}
	
	public void clickFirstSubject(WebDriver driver) {
		List<WebElement> taskList = getInactiveElements(driver, taskSubjectList);
		waitUntilVisible(driver, taskList.get(0));
		clickElement(driver, taskList.get(0));
		switchToSFTab(driver, getTabCount(driver));
	}
	
	public void verifyRelatedRecordFilters(WebDriver driver, List<String> selectedRelatedRecords){
		List<String> activityRelatedRecords = getTextListFromElements(driver, taskRelatedRecordList);
		for (String activityRelatedRecord : activityRelatedRecords) {
			System.out.println(activityRelatedRecord);
			assertTrue(selectedRelatedRecords.contains(activityRelatedRecord));
		}
	}
 
	public int getNoSubjectActivityIndexUsingComments(WebDriver driver, String comments){
		if(getWebelementIfExist(driver, noSubjectActivitycomments) != null){
			List<WebElement> noSubjectActivitycommentsLoc = getInactiveElements(driver, noSubjectActivitycomments);
			int i = 0;
			for (WebElement webElement : noSubjectActivitycommentsLoc) {
				if(webElement.getText().equals(comments)){
					return i;	
				}
				i++;
			}
		}
		return -1;
	}
	
	public String getNoSubjectActivityDueDateByIndex(WebDriver driver, int index){
		return getInactiveElements(driver, noSubjectDueDate).get(index).getText();
	}
	
	public void editNoSubjectTask(WebDriver driver, int index){
		scrollToLastTask(driver);
		getElements(driver, noSubjectActivityEditBtn).get(index).click();
	}
	
	public void deleteAllNoSubjectTask(WebDriver driver){
		while(isElementVisible(driver, noSubjectActivityEditBtn,0)) {
			clickElement(driver, noSubjectActivityEditBtn);
		    clickTaskDeleletButton(driver);
		    clickTaskWarningDeleteButton(driver);
		}
	}

	public void deleteTask(WebDriver driver, String taskName) {
		editTask(driver, taskName);
		clickTaskDeleletButton(driver);
		clickTaskWarningDeleteButton(driver);
		isTaskInvisibleInActivityList(driver, taskName);
	}

	public void saveAndOpenNewTaskWindow(WebDriver driver) {
		clickElement(driver, taskEditSaveAndNewTaskArrow);
		idleWait(1);
		clickElement(driver, taskEditSaveAndNewTaskButton);
	}

	public String setTaskReminderTime(WebDriver driver, String taskName, String reminderDate, String reminderTime) {
		By taskInActivityList = By.xpath(taskInList.replace("$$Subject$$", taskName));
		editTask(driver, taskName);
		softPhoneNewTask.checkTaskAddRemindercheckbox(driver);
		softPhoneNewTask.enterTaskReminderTime(driver, reminderDate, reminderTime);
		String actualTaskReminderTime = softPhoneNewTask.getTaskReminderTime(driver);
		clickTaskEditSaveButton(driver);
		waitUntilVisible(driver, taskInActivityList);
		return actualTaskReminderTime;
	}

	public void openTaskInSalesforce(WebDriver driver, String taskName) {	
		CallScreenPage callScreenPage = new CallScreenPage();
		callScreenPage.selectFirstContactIfMultiple(driver);
		By taskInActivityList = By.xpath(taskInList.replace("$$Subject$$", taskName));
		navigateToAllActivityTab(driver);
		scrollToElement(driver, taskInActivityList);
		clickElement(driver, taskInActivityList);
		switchToSFTab(driver, getTabCount(driver));
	}
	
	public void openNoSubjectTaskInSalesforce(WebDriver driver, int index) {								
		getInactiveElements(driver, noSubjectTaskList).get(index).click();
		//closeTabUsingTitle(driver, "Reminder");
		switchToSFTab(driver, getTabCount(driver));
	}

	public void openNoSubjectEventInSalesforce(WebDriver driver, int index) {
		getInactiveElements(driver, noSubjectEventList).get(index).click();
		switchToSFTab(driver, getTabCount(driver));
	}
	
	public String updateTaskAllFields(WebDriver driver, HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates) {
		String actualTaskReminderTime = softPhoneNewTask.enterTaskDetails(driver, taskUpdates);
		clickTaskEditSaveButton(driver);
		idleWait(5);
		return actualTaskReminderTime;
	}

	public void verifyTaskAllFields(WebDriver driver, HashMap<SoftPhoneNewTask.taskFields, String> taskUpdates) {
		// verify Subject
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.Subject) != null) {
			System.out.println("verifying Task Subject: " + taskUpdates.get(SoftPhoneNewTask.taskFields.Subject));
			assertEquals(getTaskSubject(driver), taskUpdates.get(SoftPhoneNewTask.taskFields.Subject));
		}

		// verify task type
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.Type) != null) {
			// to do
		}

		// verify Task Due Date
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.DueDate) != null
				|| taskUpdates.get(SoftPhoneNewTask.taskFields.DueDateFromDropDown) != null) {
			// To Do
		}

		// Add reminder date and time and return it
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.ReminderDate) != null || taskUpdates.get(SoftPhoneNewTask.taskFields.ReminderDateFromDropDown) != null) {
			// to do
		}

		// select task Priorities
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.Priority) != null) {
			// To Do
		}

		// select task Status
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.Status) != null) {
			System.out.println("verify Task Status: " + taskUpdates.get(SoftPhoneNewTask.taskFields.Status));
			assertEquals(getTaskStatus(driver), taskUpdates.get(SoftPhoneNewTask.taskFields.Status));
		}

		//select related records
		if(taskUpdates.get(SoftPhoneNewTask.taskFields.RelatedRecord)!=null){
			String actualRelatedRecord = getTaskRelatedRecord(driver);
			assertEquals(actualRelatedRecord, taskUpdates.get(SoftPhoneNewTask.taskFields.RelatedRecord));
		}

		// enter task comments
		if (taskUpdates.get(SoftPhoneNewTask.taskFields.Comments) != null) {
			// To Do
		}

		clickTaskEditCancelButton(driver);
	}

	public String getEventStartDate(WebDriver driver) {
		return getAttribue(driver, eventEditStartDate, ElementAttributes.value);
	}

	public String getEventEndDate(WebDriver driver) {
		return getAttribue(driver, eventEditEndDate, ElementAttributes.value);
	}

	public String getEventStartTime(WebDriver driver) {
		return getAttribue(driver, eventEditStartTime, ElementAttributes.value);
	}

	public String getEventEndTime(WebDriver driver) {
		return getAttribue(driver, eventEditEndTime, ElementAttributes.value);
	}

	public String getReminder(WebDriver driver) {
		return getAttribue(driver, eventEditEndTime, ElementAttributes.value);
	}

	public void isEventFutureIconVisible(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
		By eventIconFuture = By.xpath(eventIconFutureLocator.replace("$$Subject$$", taskName));
		waitUntilVisible(driver, eventIconFuture);
		System.out.println("Future Event Icon found on Activity");
	}

	public String getEventCreationDate(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		System.out.println(taskName);
		isTaskVisibleInActivityList(driver, taskName);
		By eventContainerCreatedDate = By.xpath(eventContainerCreatedDateLocator.replace("$$Subject$$", taskName));
		System.out.println(findElement(driver, eventContainerCreatedDate).getText());
		return getElementsText(driver, eventContainerCreatedDate);
	}

	public String getEventOwner(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		System.out.println(taskName);
		isTaskVisibleInActivityList(driver, taskName);
		By eventContainerOwner = By.xpath(eventContainerOwnerLocator.replace("$$Subject$$", taskName));
		System.out.println(findElement(driver, eventContainerOwner).getText());
		return getElementsText(driver, eventContainerOwner);
	}

	public String getEventDescription(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		System.out.println(taskName);
		isTaskVisibleInActivityList(driver, taskName);
		By eventContainerDescription = By.xpath(eventContainerDescriptionLocator.replace("$$Subject$$", taskName));
		System.out.println(findElement(driver, eventContainerDescription).getText());
		return getElementsText(driver, eventContainerDescription);
	}

	public String getEventDate(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		System.out.println(taskName);
		isTaskVisibleInActivityList(driver, taskName);
		By eventContainerDate = By.xpath(eventContainerDateLocator.replace("$$Subject$$", taskName));
		System.out.println(findElement(driver, eventContainerDate).getText());
		return getElementsText(driver, eventContainerDate);
	}

	public void isEventPastIconVisible(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
		By eventIconPast = By.xpath(eventIconPastLocator.replace("$$Subject$$", taskName));
		waitUntilVisible(driver, eventIconPast);
		System.out.println("Past Event Icon found on Activity");
	}

	public void eventOwnerContainer(WebDriver driver, String taskName) {
		navigateToAllActivityTab(driver);
		isTaskVisibleInActivityList(driver, taskName);
	}
	
	/*******Chatter section starts here*******/
	public int getChatterIndexByComment(WebDriver driver, String chatComment){
		List<WebElement> chatterComments = getElements(driver, chatterCommentsLoc);
		int i = 0;
		for (WebElement comment : chatterComments) {
			if(chatComment.equals(comment.getText())){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public void isChatterIconVisibleForIndex(WebDriver driver, int index){
		assertTrue(getElements(driver, chatterIcons).get(index).isDisplayed() && getElements(driver, chatterIcons).get(index).isEnabled());
	}
	/*********Chatter section ends here*****************/
	
	/*******Conversation AI section starts here*********/
	
	/**this method loops for 5 times every 60 seconds till cai link becomes visible 
	 * and breaks loop if found before
	 * @param driver
	 * @param callNoteSubject
	 */
	public void waitAndVerifyCAILinkVisible(WebDriver driver, String callNoteSubject) {
		By conversationAILoc = By.xpath(conversationAILink.replace("$callNoteSubject$", callNoteSubject));
		int counter = 1;
		int totalTime = 0;;
		while (!isElementVisible(driver, conversationAILoc, timeOutInSecs) && counter <= 4) {
			navigateToCallNotesTab(driver);
			navigateToAllActivityTab(driver);
			totalTime = totalTime+ timeOutInSecs;
			counter++;
		}

		System.out.println("total time in seconds:" +totalTime );
		if (isElementVisible(driver, conversationAILoc, 2)) {
			assertTrue(isElementVisible(driver, conversationAILoc, 1));
		} else {
			Assert.fail("Conversation AI link not available");
		}
	}
	
	/**this method loops for 2 times every 60 seconds till cai link becomes not visible
	 * @param driver
	 * @param callNoteSubject
	 */
	public void waitAndVerifyCAILinkNotVisible(WebDriver driver, String callNoteSubject) {
		By conversationAILoc = By.xpath(conversationAILink.replace("$callNoteSubject$", callNoteSubject));
		int counter = 1;
		int totalTime = 0;;
		while (!isElementVisible(driver, conversationAILoc, timeOutInSecs) && counter <= 2) {
			navigateToCallNotesTab(driver);
			navigateToAllActivityTab(driver);
			totalTime = totalTime+ timeOutInSecs;
			counter++;
		}

		System.out.println("total time in seconds:" +totalTime );
		if (!isElementVisible(driver, conversationAILoc, 2)) {
			assertFalse(isElementVisible(driver, conversationAILoc, 1));
		} else {
			Assert.fail("Conversation AI link available");
		}
	}
	
	/**verify whether cai link is visible
	 * @param driver
	 * @param callNoteSubject
	 * @return
	 */
	public boolean isCAILinkVisible(WebDriver driver, String callNoteSubject) {
		By conversationAILoc = By.xpath(conversationAILink.replace("$callNoteSubject$", callNoteSubject));
		return isElementVisible(driver, conversationAILoc, 2);
	}
	
	/**open cai link in new tab
	 * @param driver
	 * @param callNoteSubject
	 */
	public void openCAILinkNewTab(WebDriver driver, String callNoteSubject) {
		Actions actions = new Actions(driver);
		By conversationAILoc = By.xpath(conversationAILink.replace("$callNoteSubject$", callNoteSubject));

		// opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(findElement(driver, conversationAILoc)).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
	}
	
	/**open cai link in new tab on basis of related record
	 * @param driver
	 * @param relatedRecord
	 */
	public void clickRelatedRecordCAILink(WebDriver driver, String relatedRecord) {
		Actions actions = new Actions(driver);
		By caiAccountLoc = By.xpath(conversationAIRelatedRecordLink.replace("$relatedRecord$", relatedRecord));
		
		// opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(getInactiveElements(driver, caiAccountLoc).get(0)).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
	}
	
	/*********Conversation AI section ends here**********/
}