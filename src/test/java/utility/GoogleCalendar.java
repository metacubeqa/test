package utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class GoogleCalendar extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	//add new calendar
	By addNewCalButton           = By.xpath("//div[@data-tooltip='Add other calendars']");
	By createNewCal              = By.xpath("//div[text()='Create new calendar']");
	By backButton                = By.xpath("//div[@aria-label='Go back']");
	By confirmRemoveCalButton    = By.xpath("//span[text()='Remove calendar']/ancestor::div[@role='button']");
	
	//new cal form
	By newCalName                = By.xpath("//input[@aria-label='Name']");
	By createCalButton           = By.xpath("//span[text()= 'Create calendar']/ancestor::div[@role='button']");
	By sucessfullCreatedMsg      = By.xpath("//div[contains(text(), 'successfully created')]");
	
	String deleteMyCal           = "//span[text()='%name%']/parent::div/following-sibling::div//div[@role='button'][1]";
	String myCalList             = "//div[@data-text='%name%']";
	
	//add new event
	By addEventButton            = By.xpath("//div[text()='Create']/ancestor::button");
	By moreOptions               = By.xpath("//span[text()='More options']/ancestor::div[@role='button']");
	By editEventButton           = By.xpath("//div[@data-tooltip='Edit event']");
	By addTitle                  = By.xpath("//input[@aria-label='Title']");
	By startDate                 = By.xpath("//input[@aria-label='Start date']");
	By endDate                   = By.xpath("//input[@aria-label='End date']");
	By startTimeInput            = By.xpath("//input[@aria-label='Start time']");
	By endTimeInput              = By.xpath("//input[@aria-label='End time']");
	By saveButton                = By.xpath("//div[@aria-label='Save']");
	
	//delete event
	String selectEvent           = "//span[text()='%name%']/ancestor::div[@role='button']";
	By allEvents                 = By.xpath("//div[@role='button' and @data-eventid]");
	By deleteEventButton         = By.xpath("//div[@data-tooltip='Delete event' or @data-tooltip='Remove from this calendar']");
	By dontSendMail              = By.xpath("//span[contains(text(),' send')]");
	By nextWeekButton            = By.xpath("//div[@role='button' and @data-tooltip='Next week']");
	
	
	/**
	 * @param driver
	 * @param name
	 * create new my calendar on cal page
	 */
	public void addNewMyCalendar(WebDriver driver, String name) {
		waitUntilVisible(driver, addNewCalButton);
		scrollToElement(driver, addNewCalButton);
		clickElement(driver, addNewCalButton);
		//select create new cal
		waitUntilVisible(driver, createNewCal);
		clickElement(driver, createNewCal);
		//add details of new cal
		waitUntilVisible(driver, newCalName);
		clickElement(driver, newCalName);
		enterText(driver, newCalName, name);
		//click create button
		waitUntilVisible(driver, createCalButton);
		clickElement(driver, createCalButton);
		waitUntilVisible(driver, sucessfullCreatedMsg);
		waitUntilInvisible(driver, sucessfullCreatedMsg);
		//click back button
		waitUntilVisible(driver, backButton);
		clickElement(driver, backButton);
	}
	
	/**
	 * @param driver
	 * @param name
	 * delete my cal by name
	 */
	public void deleteMyCalendar(WebDriver driver, String name) {
		By calName = By.xpath(myCalList.replace("%name%", name));
		waitUntilVisible(driver, calName);
		hoverElement(driver, calName);
		By delCal = By.xpath(deleteMyCal.replace("%name%", name));
		waitUntilVisible(driver, delCal);
		clickElement(driver, delCal);
		waitUntilVisible(driver, confirmRemoveCalButton);
		clickElement(driver, confirmRemoveCalButton);
		waitUntilInvisible(driver, calName);
	}
	
	/**
	 * @param driver
	 * @param name
	 * @param date
	 * @param startTime
	 * @param endTime
	 */
	public void createEventOnCalendar(WebDriver driver, String name, String StartDate, String EndDate, String startTime, String endTime) {
		waitUntilVisible(driver, addEventButton);
		clickElement(driver, addEventButton);
		waitUntilVisible(driver, moreOptions);
		clickElement(driver, moreOptions);
		
		waitUntilVisible(driver, addTitle);
		clickElement(driver, addTitle);
		enterText(driver, addTitle, name);
		
		waitUntilVisible(driver, startDate);
		clickElement(driver, startDate);
		findElement(driver, startDate).sendKeys(Keys.BACK_SPACE);
		findElement(driver, startDate).sendKeys(StartDate);
		//enterText(driver, startDate, StartDate);
		
		waitUntilVisible(driver, endDate);
		clickElement(driver, endDate);
		findElement(driver, startDate).sendKeys(Keys.BACK_SPACE);
		findElement(driver, startDate).sendKeys(EndDate);
		clickEnter(driver, startDate);
		//enterText(driver, endDate, EndDate);
		
		waitUntilVisible(driver, startTimeInput);
		clickElement(driver, startTimeInput);
		findElement(driver, startTimeInput).sendKeys(Keys.BACK_SPACE);
		findElement(driver, startTimeInput).sendKeys(startTime);
		clickEnter(driver, startTimeInput);
		//enterText(driver, startTimeInput, startTime);
		
		waitUntilVisible(driver, endTimeInput);
		clickElement(driver, endTimeInput);
		findElement(driver, endTimeInput).sendKeys(Keys.BACK_SPACE);
		findElement(driver, endTimeInput).sendKeys(endTime);
		clickEnter(driver, endTimeInput);
		//enterText(driver, endTimeInput, endTime);
		
		waitUntilVisible(driver, saveButton);
		clickElement(driver, saveButton);
	}
	
	/**
	 * @param driver
	 * @param name
	 * @param StartDate
	 * @param EndDate
	 * @param startTime
	 * @param endTime
	 * edit the event
	 */
	public void editEventOnCalendar(WebDriver driver, String name, String StartDate, String EndDate, String startTime, String endTime) {
		By locator = By.xpath(selectEvent.replace("%name%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		
		waitUntilVisible(driver, editEventButton);
		clickElement(driver, editEventButton);
		
		waitUntilVisible(driver, addTitle);
		clickElement(driver, addTitle);
		enterText(driver, addTitle, name);
		
		waitUntilVisible(driver, startDate);
		StartDate = getAttribue(driver, locator, ElementAttributes.value);
		clickElement(driver, startDate);
		enterText(driver, startDate, StartDate);
		
		waitUntilVisible(driver, endDate);
		clickElement(driver, endDate);
		enterText(driver, endDate, EndDate);
		
		waitUntilVisible(driver, startTimeInput);
		clickElement(driver, startTimeInput);
		enterText(driver, startTimeInput, startTime);
		
		waitUntilVisible(driver, endTimeInput);
		clickElement(driver, endTimeInput);
		enterText(driver, endTimeInput, endTime);
		
		waitUntilVisible(driver, saveButton);
		clickElement(driver, saveButton);
	}
	
	/**
	 * @param driver
	 * @param name
	 * delete event by name
	 */
	public void deleteEventByNameOnCalendar(WebDriver driver, String name) {
		By locator = By.xpath(selectEvent.replace("%name%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, deleteEventButton);
		clickElement(driver, deleteEventButton);
	}
	
	/**
	 * @param driver
	 * @param name
	 * delete event by name
	 */
	public void deleteAllEventsOnCalendar(WebDriver driver) {
		for (int i = 0; i <= 2; i++) {
			if (isElementVisible(driver, allEvents, 5)) {
				List<WebElement> events = getElements(driver, allEvents);
				for (WebElement event : events) {
					waitUntilVisible(driver, event);
					clickElement(driver, event);
					waitUntilVisible(driver, deleteEventButton);
					clickElement(driver, deleteEventButton);
					idleWait(1);
					if(isElementVisible(driver, dontSendMail, 5)) {
						clickElement(driver, dontSendMail);	
					}
					idleWait(3);
				}
			}
			waitUntilVisible(driver, nextWeekButton);
			clickElement(driver, nextWeekButton);
		}
	}
	
	/**
	 * @param driver
	 * @param name
	 * @return is event present or not
	 */
	public boolean isEventVisibleOnCalendar(WebDriver driver, String name) {
		By locator = By.xpath(selectEvent.replace("%name%", name));
		return isElementVisible(driver, locator, 5);
	}

}
