/**
 * 
 */
package softphone.source.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import  support.source.accounts.AccountSalesforceTab.salesforceFieldsType;

import base.SeleniumBase;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneNewTask extends SeleniumBase{
	By taskIcon 							= By.cssSelector(".new-task-container");
	By relatedRecordsIcon 					= By.cssSelector(".related-records-container");
	By taskSubjectErrorControl 				= By.cssSelector(".form-group.subject-control.has-error");
	By eventSubjectErrorClose 				= By.cssSelector(".alert button.close-error");	
	By eventSubjectErrorLabel 				= By.cssSelector("#errors .message-text");	
	By taskSubject 							= By.cssSelector("input.subject-select");	
	By taskSubjectDropDownItems 			= By.cssSelector("div[class*='subject-select-content'] li");
	By taskType 							= By.cssSelector(".selectize-control.task-types.single");
	By taskTypeTextBox 						= By.cssSelector(".selectize-control.task-types.single input");
	By taskTypeSelectedOption 				= By.cssSelector(".selectize-control.task-types.single .item");
	By taskTypesSearchResultOption 			= By.cssSelector(".selectize-control.task-types.single .option.active");
	By taskPrioritiesDropDown 				= By.cssSelector(".task-priorities.single.selectize-control");
	By taskPrioritiesTextBox 				= By.cssSelector(".task-priorities.single.selectize-control input");
	By taskPrioritiesSelectedOption 		= By.cssSelector(".task-priorities.single.selectize-control .item");
	By taskPrioritySearchResultOption 		= By.cssSelector(".task-priorities.single.selectize-control .option.active");
	By taskPrioritiesDropdownOptions 		= By.cssSelector(".task-priorities.single .selectize-dropdown-content .option");
	By taskdueDateTextBox 					= By.cssSelector("input.dueDate");
	By taskdueDateErrorControl 				= By.cssSelector("div.due-date-tool.has-error");
	By taskDueDateCalenderButton 			= By.cssSelector(".due-date-tool .icon-calendar");
	By taskDueDateDropdownDates 			= By.cssSelector(".due-date-tool .dropdown-menu .date");	
	By taskCommentsTextBox 					= By.cssSelector("textarea.edit-comments");
	By taskStatuses 						= By.cssSelector(".task-statuses.single.selectize-control");
	By taskStatusesTextBox 					= By.cssSelector(".task-statuses.single.selectize-control input");
	By taskStatusDropDown 					= By.cssSelector(".task-statuses.single.selectize-control");	
	By taskStatusesSelectedOption 			= By.cssSelector(".task-statuses.single.selectize-control .item");
	By taskStatusesSearchResultOption 		= By.cssSelector(".task-statuses.single.selectize-control .option.active");
	By taskRelatedRecordBox 				= By.cssSelector("input.edit-related-record");
	By taskRelatedRecordSearchButton 		= By.cssSelector("[data-action='searchRelatedRecords']");
	By taskSaveButton 						= By.cssSelector(".create-activity .save");
	By taskReminderDate 					= By.cssSelector(".reminderDate");
	By taskReminderTime 					= By.cssSelector(".reminder-time");
	By taskReminderDeleteButton 			= By.cssSelector(".delete-img.toggle-reminder-btn");
	By addRemindercheckBox 					= By.cssSelector("input.reminder-set");
	By reminderCalendarButton 				= By.cssSelector(".reminder-tool .icon-calendar");
	By reminderCalenderDropdownDatesList 	= By.cssSelector(".reminder-tool .dropdown-menu.quick-date .date");
	
	By taskFieldRequired								= By.xpath("//ancestor::*[contains(@class,'has-error')]");
	By taskCustomFieldLists								= By.xpath(".//div[not(contains(@class, 'custom-checkbox-field'))]/div[contains(@class, 'custom-field-control')]/*[1]|.//div[contains(@class, 'custom-checkbox-field')]//div[contains(@class, 'custom-field-control')]/*[2]");
	String createTaskFieldLocator						= ".//*[@class='create-activity']//span[text() = '$$FieldName$$']";
	String createTaskFieldTextLocator					= ".//*[@class='create-activity']//input[contains(@placeholder , '$$FieldName$$')]";
	String eidtTaskFieldLabelLocator					= ".//*[@class='edit-container']//span[text() = '$$FieldName$$']";
	String editTaskFieldLocator							= ".//*[@class='edit-container']//*[contains(@placeholder , '$$FieldName$$')]";
	String editTaskCheckBoxField						= ".//*[text() = '$$FieldName$$']//../input";
	String editTaskDopdownField							= ".//*[@class='edit-container']//*[contains(@placeholder , '$$FieldName$$')]/following-sibling::div//div[@class='item']";
	String editTaskPicklistField						= ".//*[@class='edit-container']//*[contains(@data-original-title , '$$FieldName$$')]";
	String editAddressField								= ".//*[@class='edit-container']//*[contains(text(),'$$FieldName$$')]/../following-sibling::div";
	String editTimeField								= ".//*[@class='edit-container']//*[contains(@class, 'bootstrap-timepicker')]/*[contains(@data-sf-name,'$$FieldName$$')]";
	String taskStatusesDropdownOptionsLoc 				= ".//*[contains(@class, 'task-statuses')]//*[contains(@class, 'option') and text() = '$$taskStatus$$']";
	String taskRelatedCaseRecordToSelectFromList 		= ".//*[contains(@class,'tt-dataset-relatedRecords')]//b[text()='Case']/parent::div/following-sibling::div[text()='$$Case$$']";
	String taskPrioritiesDropDownOptionsLoc 			= ".//*[contains(@class, 'task-priorities')]//*[contains(@class, 'option') and text() = '$$taskPriority$$']";
	String taskRelatedCampaignRecordToSelectFromList 	= ".//*[contains(@class,'tt-dataset-relatedRecords')]//b[text()='Campaign']/parent::div/following-sibling::div[text()='$$Campaign$$']";
	String taskTypesDropdownOptionsLocator 				= ".//*[contains(@class, 'task-type')]//*[contains(@class, 'option') and text() = '$$taskType$$']";
	String taskRelatedOpportunityRecordToSelectFromList = ".//*[contains(@class,'tt-dataset-relatedRecords')]//b[text()='Opportunity']/parent::div/following-sibling::div[text()='$$Opportunity$$']";
	String taskRelatedAccountRecordToSelectFromList 	= ".//*[contains(@class,'tt-dataset-relatedRecords')]//b[text()='Account']/parent::div/following-sibling::div[text()='$$Account$$']";
	
	public static enum taskFields{
		Subject,
		Type,
		Priority,
		Assignee,
		DueDate,
		DueDateFromDropDown,
		DueDateDisable,
		ReminderDate,
		ReminderDateFromDropDown,
		ReminderTime,
		DeleteReminder,
		RelatedRecordType,
		RelatedRecord,
		Comments,
		Status
	}
	
	public static enum TaskTypes{
		None,
		Call,
		Meeting,
		Other,
		Email,
		SMS;
	}
	
	public static enum TaskPriorities{
		High,
		Normal,
		Low
	}
	
	public enum TaskStatuses{
		NotStarted("Not Started"),
		InProgress("In Progress"),
		Completed("Completed"),
		WaitingOn("Waiting on someone else"),
		Deffered("deferred");
		
	    private String displayName;

	    TaskStatuses(String displayName) {
	        this.displayName = displayName;
	    }
	    
	    public String displayName() { return displayName; }

	    @Override 
	    public String toString() { return displayName; }
	}
	
	public static enum taskRelatedRecordsType{
		Campaign,
		Opportunity,
		Account,
		Case,
		Property;
	}
	
	public static enum taskDropdownDates{
		Today,
		Tomorrow,
		Onedays,
		Twodays,
		Oneweek,
		Twoweeks
	}
	
	public boolean isTaskIconVisible(WebDriver driver) {
		return isElementVisible(driver, taskIcon, 0);
	}
	
	public void clickTaskIcon(WebDriver driver){
		if(!isElementVisible(driver, taskSubject, 2) ){
		isElementVisible(driver, relatedRecordsIcon, 10);
		waitUntilVisible(driver, taskIcon);
		clickElement(driver, taskIcon);
		waitUntilVisible(driver, taskSubject);
		//waitUntilVisible(driver, taskPrioritiesDropDown);
		}
	}
	
	public String getTaskSalesforceFieldValues(WebDriver driver, String fieldName, salesforceFieldsType fieldType) {
		if(fieldType.equals(salesforceFieldsType.picklist)) {
			By fieldLocator = By.xpath(editTaskDopdownField.replace("$$FieldName$$", fieldName));
			return getElementsText(driver, fieldLocator);
		}else if(fieldType.equals(salesforceFieldsType.address)){
			By fieldLocator = By.xpath(editAddressField.replace("$$FieldName$$", fieldName));
			return getElementsText(driver, fieldLocator);
		}
		
		else if(fieldType.equals(salesforceFieldsType.Boolean)) {
			By fieldLocator = By.xpath(editTaskCheckBoxField.replace("$$FieldName$$", fieldName));
			if(findElement(driver, fieldLocator).isSelected()) {
				return "true";
			}else {
				return "false";
			}
		}else {
			By fieldLocator = By.xpath(editTaskFieldLocator.replace("$$FieldName$$", fieldName));
			if(getElementsText(driver, fieldLocator).equals("")) {
				return getAttribue(driver, fieldLocator, ElementAttributes.value);
			}else {
				return getElementsText(driver, fieldLocator);
			}
		}
	}
	
	public boolean isSalesforceFieldRequired(WebDriver driver, String fieldName, salesforceFieldsType fieldType) {
		if(fieldType.equals(salesforceFieldsType.Boolean)) {
			System.out.println("Boolean fields are not checked as required fields");
			return true;
		}else if(fieldType.equals(salesforceFieldsType.time)) {
			By fieldLocator = By.xpath(editTimeField.replace("$$FieldName$$", fieldName));
			return isElementVisible(driver, findElement(driver, fieldLocator).findElement(taskFieldRequired), 0);
		}
		else {
			By fieldLocator = By.xpath(createTaskFieldTextLocator.replace("$$FieldName$$", fieldName));
			return isElementVisible(driver, findElement(driver, fieldLocator).findElement(taskFieldRequired), 0);
		}
	}
	
	public boolean isDefaultFieldRequired(WebDriver driver, String fieldName, salesforceFieldsType fieldType) {
		if(fieldType.equals(salesforceFieldsType.Boolean)) {
			System.out.println("Boolean fields are not checked as required fields");
			return true;
		}else {
			By fieldLocator = By.xpath(editTaskPicklistField.replace("$$FieldName$$", fieldName));
			return isElementVisible(driver, findElement(driver, fieldLocator).findElement(taskFieldRequired), 0);
		}
	}
	
	public void enterSalesforceFieldValues(WebDriver driver, String fieldName, salesforceFieldsType fieldType, String fieldValue) {
		if(fieldType.equals(salesforceFieldsType.Boolean)) {
			By fieldLocator = By.xpath(editTaskCheckBoxField.replace("$$FieldName$$", fieldName));
			if(fieldValue.equals("true")) {
				if(!findElement(driver, fieldLocator).isSelected()) {
					clickElement(driver, fieldLocator);
				}
			}else {
				if(findElement(driver, fieldLocator).isSelected()) {
					clickElement(driver, fieldLocator);
				}
			}
		}else {
			By fieldLocator = By.xpath(createTaskFieldTextLocator.replace("$$FieldName$$", fieldName));
			enterText(driver, fieldLocator, fieldValue);
			clickEnter(driver, fieldLocator);
		}
	}
	
	public boolean isTaskSfFieldVisibleOnTaskCreate(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(createTaskFieldLocator.replace("$$FieldName$$", sfTaskFieldName));
		return isElementVisible(driver, sfTaskField, 0);
	}
	
	public boolean isTaskSfFieldVisibleOnTaskEdit(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(eidtTaskFieldLabelLocator.replace("$$FieldName$$", sfTaskFieldName));
		return isElementVisible(driver, sfTaskField, 0);
	}
	
	public boolean isTaskSfFieldEnabled(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(editTaskFieldLocator.replace("$$FieldName$$", sfTaskFieldName));
		return findElement(driver, sfTaskField).isEnabled();
	}
	
	public boolean isTaskSfPicklistFieldEnabled(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(editTaskPicklistField.replace("$$FieldName$$", sfTaskFieldName));
		return findElement(driver, sfTaskField).isEnabled();
	}
	
	public boolean isTaskSfCheckBoxFieldEnabled(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(editTaskCheckBoxField.replace("$$FieldName$$", sfTaskFieldName));
		return findElement(driver, sfTaskField).isEnabled();
	}
	
	public boolean isTaskSfTimeFieldEnabled(WebDriver driver, String sfTaskFieldName) {
		By sfTaskField = By.xpath(editTimeField.replace("$$FieldName$$", sfTaskFieldName));
		return findElement(driver, sfTaskField).isEnabled();
	}
	
	public List<String> getCustomFieldLists(WebDriver driver){
		List<String> customFieldsNameList = new ArrayList<String>();
		List<WebElement> customFiledsList = getInactiveElements(driver, taskCustomFieldLists);
		for (WebElement webElement : customFiledsList) {
			if(webElement.getText().equals("")) {
				customFieldsNameList.add(getAttribue(driver, webElement, ElementAttributes.Placeholder).replace(" (required)", ""));
			}else {
				customFieldsNameList.add(getElementsText(driver, webElement).replace(" (required)", ""));
			}
		}
		return customFieldsNameList;
	}
	
	public void verifyTaskIconInvisible(WebDriver driver){
		waitUntilInvisible(driver, taskIcon);
	}
	public void enterTaskSubject(WebDriver driver, String subject){
		enterText(driver, taskSubject, subject);
	}
	
	public void enterTaskComments(WebDriver driver, String comments){
		enterText(driver, taskCommentsTextBox, comments);
	}
	
	public boolean checkTask_NoteSubjectExistsInTasksTab(WebDriver driver, String eventSubject) {
		clickTaskIcon(driver);
		waitUntilVisible(driver, taskSubject);
		clickElement(driver, taskSubject);
		return isTextPresentInList(driver, getInactiveElements(driver, taskSubjectDropDownItems), eventSubject);
	}
	
	public void selectTaskType(WebDriver driver, String selectTaskType){
		clickElement(driver, taskTypeTextBox);
		By taskTypeOption = By.xpath(taskTypesDropdownOptionsLocator.replace("$$taskType$$", selectTaskType));
		clickElement(driver, taskTypeOption);
	}
	
	public void enterDueDate(WebDriver driver, String dueDate) {
		enterText(driver, taskdueDateTextBox, dueDate);
		clickEnter(driver, taskdueDateTextBox);
	}
	
	public void selectSearchedTaskType(WebDriver driver, String searchKeyword, String expectedTaskType){
		enterText(driver, taskTypeTextBox, searchKeyword);
		getElements(driver, taskTypesSearchResultOption).get(0).click();
		assertEquals(getElementsText(driver, taskTypeSelectedOption), expectedTaskType);
	}
	
	public void selectTaskPriority(WebDriver driver, String selectTaskPriority){
		clickElement(driver, taskCommentsTextBox);
		clickElement(driver, taskPrioritiesDropDown);
		By taskPriorityOption = By.xpath(taskPrioritiesDropDownOptionsLoc.replace("$$taskPriority$$", selectTaskPriority));
		for (int i = 0; i < 10; i++) {
			clickElement(driver, taskPriorityOption);
			if(isElementInvisible(driver, taskPriorityOption, 0)) {
				break;
			}
		}
	}
	
	public void selectSearchedTaskPriority(WebDriver driver, String searchKeyword, String expectedTaskPriority){
		enterText(driver, taskPrioritiesTextBox, searchKeyword);
		getElements(driver, taskPrioritySearchResultOption).get(0).click();
		assertEquals(getElementsText(driver, taskPrioritiesSelectedOption), expectedTaskPriority);
	}
	
	public void selectTaskStatus(WebDriver driver, String selectTaskStatus){
		clickElement(driver, taskCommentsTextBox);
		scrollToElement(driver, taskStatusDropDown);
		clickElement(driver, taskStatusDropDown);
		By taskStatusOption = By.xpath(taskStatusesDropdownOptionsLoc.replace("$$taskStatus$$", selectTaskStatus));
		for (int i = 0; i < 10; i++) {
			clickElement(driver, taskStatusOption);
			if(isElementInvisible(driver, taskStatusOption, 0)) {
				break;
			}
		}
	}
	
	public void selectSearchedTaskStatuses(WebDriver driver, String searchKeyword, String expectedTaskStatuses){
		clickElement(driver, taskStatusesTextBox);
		enterBackspace(driver, taskStatusesTextBox);
		enterText(driver, taskStatusesTextBox, searchKeyword);
		getElements(driver, taskStatusesSearchResultOption).get(0).click();
		assertEquals(getElementsText(driver, taskStatusesSelectedOption), expectedTaskStatuses);
	}
	
	public void checkTaskAddRemindercheckbox(WebDriver driver){
		if(!findElement(driver, addRemindercheckBox).isSelected()) {
			clickElement(driver, addRemindercheckBox);
			waitUntilVisible(driver, taskReminderDate);
		}
	}
	
	public void enterTaskReminderTime(WebDriver driver, String reminderDate, String reminderTime){
		enterText(driver, taskReminderDate, reminderDate);
		enterText(driver, taskReminderDate, reminderDate);
		clickEnter(driver, taskReminderDate);
		enterText(driver, taskReminderTime, reminderTime);
		clickEnter(driver, taskReminderTime);
		clickElement(driver, taskCommentsTextBox);
	}
	
	public void selectTaskReminderDateFromDropdown(WebDriver driver, String reminderDate, String reminderTime){
		enterText(driver, taskReminderTime, reminderTime);
		clickEnter(driver, taskReminderTime);
		clickElement(driver, taskCommentsTextBox);
		idleWait(1);
		clickElement(driver, reminderCalendarButton);
		List<WebElement> reminderDropdownDates = getElements(driver, reminderCalenderDropdownDatesList);
		if(reminderDate.equals(taskDropdownDates.Today.toString())){
			for (int i = 0; i < 10; i++) {
				reminderDropdownDates.get(0).click();
				if(!isElementVisible(driver, reminderDropdownDates.get(0), 0)) {
					break;
				}
			}
		}
	}
	
	public void deleteTaskReminderTime(WebDriver driver){
		if(findElement(driver, addRemindercheckBox).isSelected()) {
			clickElement(driver, addRemindercheckBox);
		}
	}
	
	public void selectTaskDueDateFromDropdown(WebDriver driver, String dueDate){
		clickElement(driver, taskDueDateCalenderButton);
		List<WebElement> reminderDropdownDates = getElements(driver, taskDueDateDropdownDates);
		if(dueDate.equals(taskDropdownDates.Today.toString())){
			reminderDropdownDates.get(0).click();
		} else if(dueDate.equals(taskDropdownDates.Tomorrow.toString())){
			reminderDropdownDates.get(1).click();
		}
	}
	
	public String getTaskReminderTime(WebDriver driver){
		return getAttribue(driver, taskReminderTime, ElementAttributes.value);
	}
	
	public void clickSaveTaskButton(WebDriver driver){
		clickElement(driver, taskSaveButton);
	}
	
	public void isMandatoryFieldsErrorVisible(WebDriver driver){
		clickSaveTaskButton(driver);
		waitUntilVisible(driver, taskSubjectErrorControl);
		waitUntilVisible(driver, taskdueDateErrorControl);
		waitUntilVisible(driver, eventSubjectErrorClose);
		waitUntilVisible(driver, eventSubjectErrorLabel);
		clickElement(driver, eventSubjectErrorClose);
	}
	
	public void searchtaskRelatedRecord(WebDriver driver, String searchText){
		idleWait(2);
		waitUntilVisible(driver, taskRelatedRecordBox);
		clickElement(driver, taskRelatedRecordBox);
		enterBackspace(driver, taskRelatedRecordBox);
		enterText(driver, taskRelatedRecordBox, searchText);
		clickElement(driver, taskRelatedRecordSearchButton);
	}
	
	public void selectCaseFromRelatedRecordSearchList(WebDriver driver, String relatedCase){
		By caseLocator = By.xpath(taskRelatedCaseRecordToSelectFromList.replace("$$Case$$", relatedCase));
		waitUntilVisible(driver, caseLocator);
		clickElement(driver, caseLocator);
	}
	
	public void selectCampaignFromRelatedRecordSearchList(WebDriver driver, String relatedCampaign){
		By campaignLocator = By.xpath(taskRelatedCampaignRecordToSelectFromList.replace("$$Campaign$$", relatedCampaign));
		waitUntilVisible(driver, campaignLocator);
		clickElement(driver, campaignLocator);
	}
	
	public void selectOpportunityFromRelatedRecordSearchList(WebDriver driver, String relatedOpportunity){
		By opportunityLocator = By.xpath(taskRelatedOpportunityRecordToSelectFromList.replace("$$Opportunity$$", relatedOpportunity));
		waitUntilVisible(driver, opportunityLocator);
		clickElement(driver, opportunityLocator);
	}
	
	public void selectAccountFromRelatedRecordSearchList(WebDriver driver, String relatedOpportunity){
		By accountLocator = By.xpath(taskRelatedAccountRecordToSelectFromList.replace("$$Account$$", relatedOpportunity));
		waitUntilVisible(driver, accountLocator);
		clickElement(driver, accountLocator);
	}
	
	public void selectTaskRelatedRecords(WebDriver driver, String recordType, String recordName){
		searchtaskRelatedRecord(driver, recordName);
		if (recordType.equals(taskRelatedRecordsType.Case.toString())) {
			selectCaseFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Campaign.toString())) {
			selectCampaignFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Opportunity.toString())) {
			selectOpportunityFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Account.toString())) {
			selectAccountFromRelatedRecordSearchList(driver, recordName);
		}
	}
	
	public void addNewTask(WebDriver driver, String subject, String comments, String dueDate, String selectTaskType){
		System.out.println("adding task " + subject);
		clickTaskIcon(driver);
		enterTaskSubject(driver, subject);
		clickEnter(driver, taskSubject);
		enterText(driver, taskCommentsTextBox, comments);
		selectTaskType(driver, selectTaskType);
		enterText(driver, taskdueDateTextBox, dueDate);
		clickEnter(driver, taskdueDateTextBox);
		clickElement(driver, taskPrioritiesDropDown);
		idleWait(1);
		List<WebElement> taskPriorities = getElements(driver, taskPrioritiesDropdownOptions);
		taskPriorities.get(1).click();
		clickSaveTaskButton(driver);
	}
	
	public String addNewTaskWithAllFields(WebDriver driver, HashMap<taskFields, String> taskDetails){
		clickTaskIcon(driver);
		String actualTaskReminderTime = enterTaskDetails(driver, taskDetails);
		clickElement(driver, taskSaveButton);
		System.out.println("added new task with subject " + taskDetails.get(taskFields.Subject));
		return actualTaskReminderTime;
	}
	
	public String enterTaskDetails(WebDriver driver, HashMap<taskFields, String> taskDetails) {
		String actualTaskReminderTime = "";
		
		// Enter Subject
		if(taskDetails.get(taskFields.Subject)!=null){
			System.out.println("Updating Task Subject: " + taskDetails.get(taskFields.Subject));
			enterTaskSubject(driver, taskDetails.get(taskFields.Subject));
		}	
		
		// select task type
		if(taskDetails.get(taskFields.Type)!=null){
			System.out.println("Selecting Task Type: " + taskDetails.get(taskFields.Type));
			selectTaskType(driver, taskDetails.get(taskFields.Type));
		}
		
		// Enter Task Due Date
		idleWait(1);
		if(taskDetails.get(taskFields.DueDate)!= null){
			System.out.println("Entering Due Date: " + taskDetails.get(taskFields.DueDate));
			enterDueDate(driver, taskDetails.get(taskFields.DueDate));
		}else if(taskDetails.get(taskFields.DueDateFromDropDown)!=null){
			System.out.println("Selecting Due Date: " + taskDetails.get(taskFields.DueDateFromDropDown));
			selectTaskDueDateFromDropdown(driver, taskDetails.get(taskFields.DueDateFromDropDown));
		}else if(taskDetails.get(taskFields.DueDateDisable)!=null && Boolean.parseBoolean(taskDetails.get(taskFields.DueDateDisable))){
			System.out.println("verifying that Due Date disable is: " + taskDetails.get(taskFields.DueDateDisable));
			assertFalse(isElementVisible(driver, taskdueDateTextBox, 0));
			assertFalse(isElementVisible(driver, taskDueDateCalenderButton, 0));
		}
		
		// Add reminder date and time and return it
		if(taskDetails.get(taskFields.ReminderDate)!=null){
			System.out.println("Selecting Reminder Time: " + taskDetails.get(taskFields.ReminderDate) + " " + taskDetails.get(taskFields.ReminderTime));
			if(isElementVisible(driver, addRemindercheckBox, 0))
				checkTaskAddRemindercheckbox(driver);
			enterTaskReminderTime(driver, taskDetails.get(taskFields.ReminderDate), taskDetails.get(taskFields.ReminderTime));
			actualTaskReminderTime = getTaskReminderTime(driver);
		}
		else if(taskDetails.get(taskFields.ReminderDateFromDropDown)!=null){
			System.out.println("Selecting Reminder Time: " + taskDetails.get(taskFields.ReminderDateFromDropDown) + " " + taskDetails.get(taskFields.ReminderTime));
			if(isElementVisible(driver, addRemindercheckBox, 0))
				checkTaskAddRemindercheckbox(driver);
			selectTaskReminderDateFromDropdown(driver, taskDetails.get(taskFields.ReminderDateFromDropDown), taskDetails.get(taskFields.ReminderTime));
			actualTaskReminderTime = getTaskReminderTime(driver);
		}else if(taskDetails.get(taskFields.DeleteReminder)!=null && Boolean.parseBoolean(taskDetails.get(taskFields.DeleteReminder))){
			System.out.println("deleting Reminder Time");
			deleteTaskReminderTime(driver);
		}

		// select task Priorities
		if(taskDetails.get(taskFields.Priority)!=null){
			System.out.println("Selecting Task priority: " + taskDetails.get(taskFields.Priority));
			selectTaskPriority(driver, taskDetails.get(taskFields.Priority));
		}
		
		// select task Status
		if (taskDetails.get(taskFields.Status) != null) {
			System.out.println("Selecting Task Status: " + taskDetails.get(taskFields.Status));
			selectTaskStatus(driver, taskDetails.get(taskFields.Status));
		}

		// select related records
		if(taskDetails.get(taskFields.RelatedRecordType)!=null) {
			System.out.println("Selecting Related Record: " + taskDetails.get(taskFields.RelatedRecord));
			selectTaskRelatedRecords(driver, taskDetails.get(taskFields.RelatedRecordType), taskDetails.get(taskFields.RelatedRecord));
		}

		// enter task comments
		if(taskDetails.get(taskFields.Comments)!=null){
			System.out.println("Entering Comments: " + taskDetails.get(taskFields.Comments));
			enterTaskComments(driver, taskDetails.get(taskFields.Comments));
		}
		
		// return's actual task reminder time
		return actualTaskReminderTime;
	}
	
	public void verifyCallTasksFeaturesDisabledInSoftphone(WebDriver driver) {
		assertFalse(isElementVisible(driver, taskType, 5));
		assertFalse(isElementVisible(driver, taskPrioritiesTextBox, 5));
		assertFalse(isElementVisible(driver, taskdueDateTextBox, 5));
		assertFalse(isElementVisible(driver, taskRelatedRecordBox, 5));
		assertFalse(isElementVisible(driver, addRemindercheckBox, 5));
	}
}
