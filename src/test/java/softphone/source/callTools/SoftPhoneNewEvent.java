/**
 *
 */
package softphone.source.callTools;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import softphone.source.SoftPhoneActivityPage;
import utility.HelperFunctions;

/**
 * @author Vishal
 *
 */
public class SoftPhoneNewEvent extends SeleniumBase{

	HelperFunctions helperFunctions = new HelperFunctions();

	SeleniumBase seleniumBase= new SeleniumBase();

	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();

	By relatedRecordsIcon = By.cssSelector(".related-records-container");
	By taskSubject = By.cssSelector("input.subject-select, input.subject-input:not([style='display: none;'])");
	By taskPriorities = By.cssSelector(".task-priorities.single.selectize-control");
	By eventIcon=By.cssSelector(".new-event-container");
	By eventSubjectErrorClose = By.cssSelector(".alert button.close-error");
	By eventSubjectErrorLabel = By.cssSelector("#errors .message-text");
	By eventSubject = By.cssSelector("input.subject-select");
	By allDayCheckBox = By.cssSelector(".allDay");
	By eventSubjectDropDownItems = By.cssSelector("div[class*='subject-select-content'] li");

	//start date
	By eventStartDateCalenderButton = By.cssSelector(".start-date-tool .glyphicon-calendar");
	By eventStartDateTextBox = By.cssSelector("input.start-date");
	By eventStartDateDropdownDates = By.cssSelector(".start-date-tool .dropdown-menu .date");

	//end date
	By eventEndDateCalenderButton = By.cssSelector(".end-date-tool .glyphicon-calendar");
	By eventEndDateTextBox = By.cssSelector("input.end-date");
	By eventEndDateDropdownDates = By.cssSelector(".end-date-tool .dropdown-menu .date");

	//start time
	By eventStartTime = By.cssSelector("input.start-time");
	By eventStartTimeDisabled= By.cssSelector(".start-time:disabled");

	//end time
	By eventEndTime= By.cssSelector("input.end-time");
	By eventEndTimeDisabled= By.cssSelector(".end-time:disabled");

	//reminder
	By reminderCheckBox= By.cssSelector(".reminder-set:enabled");
	By reminderDisabled=By.cssSelector(".reminder-set:disabled");
	By reminderOption = By.cssSelector(".selectize-control.reminder-select.single");
	By reminderTextBox =By.cssSelector(".reminder-select input");
	By reminderSelectedOption=By.cssSelector(".has-items .item");
	String eventReminderDropdownOptionsLocator = ".//*[contains(@class, 'reminder-select')]//*[contains(@class, 'option') and text() = '$$reminderTime$$']";
	By eventReminderSearchResultOption = By.cssSelector(".selectize-control.reminder-select.single .option.active");
	By eventReminderDropdownOptions = By.cssSelector(".reminder-select.single .selectize-dropdown-content .option");
	By eventReminderDropdownOptionsDisbaled = By.cssSelector(".selectize-input.items.full.has-options.has-items.disabled.locked");
	By eventDescription = By.cssSelector("textarea.edit-comments");
	By eventSaveButton = By.cssSelector(".create-activity .save");

	//eventEdit

	By cancelButton = By.cssSelector(".actions-right .cancel, .actions .cancel");

	public static enum eventFields{
		Subject,
		AllDay,
		StartDate,
		StartTime,
		EndDate,
		EndTime,
		ReminderStatus,
		Reminder,
		Description,
	}

	public enum reminderTime{
		zerominute("0 minute"),
		fiveminute("5 minute"),
		fivteenminute("15 minute"),
		thirtyminute("30 minute"),
		onehour("1 hours"),
		twohours("2 hours"),
		threehours("3 hours"),
		fourhours("4 hours"),
		fivehours("5 hours"),
		sixhours("6 hours"),
		sevenhours("7 hours"),
		eighthours("8 hours"),
		ninehours("9 hours"),
		tenhours("10 hours"),
		elevenhours("11 hours"),
		halfday("0.5 days"),
		eighteenhours("18 hours"),
		oneday("1 day"),
		twodays("2 days"),
		threedays("3 days"),
		fourdays("4 days"),
		oneweek("1 week"),
		twoweeks("2 weeks");

		private String displayName;

		reminderTime(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() { return displayName; }

		@Override
		public String toString() { return displayName; }
	}
	
	public boolean isEventIconVisible(WebDriver driver) {
		return isElementVisible(driver, eventIcon, 0);
	}

	// click event tab
	public void clickEventTab(WebDriver driver) {
		clickElement(driver, eventIcon);
	}
	public void clickAllDayCheckBox(WebDriver driver) {
		clickElement(driver, allDayCheckBox);
	}
	public void clickCancel(WebDriver driver) {
		clickElement(driver, cancelButton);
	}
	public void clickDescription(WebDriver driver) {
		clickElement(driver, eventDescription);
	}

	// enter subject on event
	public void clickSubject(WebDriver driver) {
		clickElement(driver, eventSubject);
	}
	public void enterStartDate(WebDriver driver,String date) {
		enterText(driver, eventStartDateTextBox, date);
		clickEnter(driver, eventStartDateTextBox);
	}
	public void enterEndDate(WebDriver driver,String date) {
		enterText(driver, eventEndDateTextBox, date);
		clickEnter(driver, eventEndDateTextBox);
	}
	public void enterStartTime(WebDriver driver,String time) {
		enterText(driver, eventStartTime, time);
		clickEnter(driver, eventStartTime);
	}
	public void enterEndTime(WebDriver driver,String date) {
		enterText(driver, eventEndTime, date);
		clickEnter(driver, eventEndTime);
	}

	public void enterEventSubject(WebDriver driver, String subject) {
		enterText(driver, eventSubject, subject);
	}

	public boolean checkEventSubjectExists(WebDriver driver, String eventSubjectText) {
		idleWait(1);
		clickEventTab(driver);
		waitUntilVisible(driver, eventSubject);
		clickElement(driver, eventSubject);
		return isTextPresentInList(driver, getInactiveElements(driver, eventSubjectDropDownItems), eventSubjectText);
	}
	
	public void enterReminder(WebDriver driver, String reminder) {

		seleniumBase.enterBackspace(driver, reminderTextBox);
		enterText(driver, reminderTextBox, reminder);
		clickEnter(driver, reminderTextBox);
	}

	public void enterDescription(WebDriver driver, String description) {
		enterText(driver, eventDescription, description);
	}

	public void addNewEventWithAllFields(WebDriver driver, HashMap<eventFields, String> eventDetails){
		clickEventTab(driver);
		enterEventDetails(driver, eventDetails);
		clickElement(driver, eventSaveButton);
	}

	public void editEventWithAllFields(WebDriver driver, HashMap<eventFields, String> eventDetails){
		enterEventDetails(driver, eventDetails);
	}


	public void enterEventDetails(WebDriver driver, HashMap<eventFields, String> eventDetails) {
		if(eventDetails.get(eventFields.StartDate)!=null){
			enterStartDate(driver,eventDetails.get(eventFields.StartDate));
		}
		
		// Enter Subject
		if (eventDetails.get(eventFields.Subject) != null) {
			enterEventSubject(driver, eventDetails.get(eventFields.Subject));
		}

		if(eventDetails.get(eventFields.StartTime)!=null){
			enterStartTime(driver,eventDetails.get(eventFields.StartTime));
		}

		if(eventDetails.get(eventFields.EndTime)!=null){
			enterEndTime(driver,eventDetails.get(eventFields.EndTime));
		}

		if(eventDetails.get(eventFields.Reminder)!=null){
			enterReminder(driver,eventDetails.get(eventFields.Reminder));
		}

		if(eventDetails.get(eventFields.Description)!=null){
			enterDescription(driver,eventDetails.get(eventFields.Description));
		}
		
		if(eventDetails.get(eventFields.EndDate)!=null){
			enterEndDate(driver,eventDetails.get(eventFields.EndDate));
			enterEndDate(driver,eventDetails.get(eventFields.EndDate));
		}
	}

	public String getStartDate(WebDriver driver) {
		return getAttribue(driver, eventStartDateTextBox, ElementAttributes.value);
	}

	public String getEndDate(WebDriver driver) {
		return getAttribue(driver, eventEndDateTextBox, ElementAttributes.value);
	}

	public String getStartTime(WebDriver driver) {
		return getAttribue(driver, eventStartTime, ElementAttributes.value);
	}

	public String getEndTime(WebDriver driver) {
		return getAttribue(driver, eventEndTime, ElementAttributes.value);
	}

	public String getEventCreatedDate(WebDriver driver) {
		return getAttribue(driver, eventEndTime, ElementAttributes.value);
	}


	public String getReminderText(WebDriver driver) {
		return getElementsText(driver, reminderSelectedOption);
	}

	public boolean getReminderStatus(WebDriver driver) {
		return findElement(driver, reminderCheckBox).isSelected();
	}

	public boolean getAllDayStatus(WebDriver driver) {
		return findElement(driver, allDayCheckBox).isSelected();
	}

	public void enableAllDayStatus(WebDriver driver) {
		if (findElement(driver, allDayCheckBox).isSelected() == false) {
			clickElement(driver, allDayCheckBox);
		}
		assertEquals(findElement(driver, eventStartTimeDisabled).isDisplayed(), true);
		assertEquals(findElement(driver, eventEndTimeDisabled).isDisplayed(), true);
		assertEquals(findElement(driver, reminderDisabled).isDisplayed(), true);
		assertEquals(findElement(driver, eventReminderDropdownOptionsDisbaled).isDisplayed(), true);
	}

	public void saveEvent(WebDriver driver) {
		clickElement(driver, eventSaveButton);
	}

	public void eventSubjectRequired(WebDriver driver) {
		saveEvent(driver);
		waitUntilVisible(driver, eventSubjectErrorClose);
		waitUntilVisible(driver, eventSubjectErrorLabel);
		clickElement(driver, eventSubjectErrorClose);
	}
}
