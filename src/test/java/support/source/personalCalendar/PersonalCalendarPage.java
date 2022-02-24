package support.source.personalCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class PersonalCalendarPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	//***Integration Tab***//
	By integrationHeading        = By.xpath("//a[@data-tab='integrations']");
	By integrationRemoveButton   = By.xpath("//button[@name='remove-integration']");
	By GmailIntRemoveButton      = By.xpath("//strong[text()='Gmail']/parent::div/following-sibling::div/button");
	By ExchangeIntRemoveButton   = By.xpath("//strong[text()='Microsoft']/parent::div/following-sibling::div/button");
	By ZoomIntRemoveButton       = By.xpath("//strong[text()='Zoom']/parent::div/following-sibling::div/button");
	
	By confirmRemoveButton       = By.xpath("//button/following-sibling::button[contains(@class, 'rdna-button contained')]");
	By confirmCancelButton       = By.xpath("//button[contains(@class, 'rdna-button text')]");
	
	By integrationDefaultCal     = By.cssSelector("#ringdna-select");
	By integrationTextWhenNoCal  = By.xpath("//label[@for='primaryIntegration-select']");
	
	By integrationSaveButton     = By.xpath("//button[@type='submit']");
	
	By selectExchangeAccount     = By.xpath("//li[@data-value='EXCHANGE']");
	By selectGmailAccount        = By.xpath("//li[@data-value='GMAIL']");
	
	By multipleAccountError      = By.xpath("//div[contains(@class ,'MuiAlert-filledError')]//span[contains(text(), 'We have detected that these login credentials are already in use')]");
	
	By invalidExchnageError      = By.xpath("//p[contains(text(),'Please check your email address and password')]");
	
	//connection
	By exchange                  = By.xpath("//strong[text()='Exchange']/parent::div");
	By gmail                     = By.xpath("//strong[text()='Gmail']/parent::div");
	
	//status
	By gmailStatus               = By.xpath("//strong[text()='Gmail']/parent::div//following-sibling::div/following-sibling::div/span");
	By ExchangeStatus            = By.xpath("//strong[text()='Microsoft']/parent::div//following-sibling::div/following-sibling::div/span");
	
	By addIntegrationButton      = By.xpath("//button[@name='add-integration']");
	By closeAddIntegrationWindow = By.xpath("//button[@data-testid='modal.close']");        
	
	By gmailRequestButton        = By.xpath("//span[contains(text(), 'Gmail')]/following-sibling::button[@name='request-connection']");
	By exchnageRequestButton     = By.xpath("//span[contains(text(), 'Exchange')]/following-sibling::button[@name='request-connection']");
	By gmailConnectButton        = By.xpath("//span[contains(text(), 'Gmail')]/following-sibling::button[@name='connect-integration']");
	By exchnageConnectButton     = By.xpath("//span[contains(text(), 'Exchange')]/following-sibling::button[@name='connect-integration']");
	By zoomConnectButton         = By.xpath("//span[contains(text(), 'anyone, anywhere')]/following-sibling::button[@name='connect-integration']");
	
	//connect gmail int
	By gmailEmailInput           = By.cssSelector("#identifierId");
	By gmailNextButton           = By.xpath("//span[text()= 'Next']/parent::button");
	By gmailPasswdInput          = By.xpath("//input[@name='password']");
	By gmailAllowButton          = By.xpath("//span[text()= 'Allow']/parent::button");
	By gmailTryAnotherWayButton  = By.xpath("//span[contains(text(), 'Try another way')]//parent::button");
	By gmailAuthenticator        = By.xpath("//strong[contains(text(), 'Google Authenticator')]//ancestor::li");
	By gmailAuthOtp              = By.cssSelector("#totpPin");
	String gmailLoginAccount     = "[data-identifier='%text%']";
	
	//connect exchnage int
	By exchangeLogo              = By.xpath("//input[@id='eas']/following-sibling::label/img");
	By exchangeEmailInput        = By.cssSelector("#login_hint");
	By exchangePasswdInput       = By.cssSelector("#password");
	By exchangeSigninButton      = By.xpath("//input[@value='Sign In']");
	
	//new login ui
	By exchangeName                 = By.cssSelector("#nameExchange");
	By exchangeMail                 = By.cssSelector("#email");
	By exchangePassword             = By.cssSelector("#password");
	By exchangeServer               = By.cssSelector("#easServerHost");
	By connectButton                = By.xpath("//button[text() = 'Connect Your Account']");
	
	//***Calendar Locators***//
	
	//calendar tab
	By calendarTab 			= By.cssSelector("[data-tab='calendar']");
	By calendarTabParent 	= By.xpath(".//*[@data-tab='calendar']/ancestor::li");
	
	//default calendar heading
	//Availablity Locators
	
	By defaultCalendar      = By.xpath("//span[contains(text(), 'default')]");
	By conflictCalendars    = By.xpath("//span[contains(@class, 'MuiTypography-body')]");
	By conflictCalCheckBox  = By.xpath("//input[contains(@class , 'jss')]/ancestor::span[contains(@class, 'MuiCheckbox-root')]");
	By checkedConflictCal   = By.xpath("//span[contains(@class, 'MuiCheckbox-root' ) and contains(@class, 'checked')]");
	By uncheckConflictCal   = By.xpath("//span[not(contains(@class, 'checked')) and contains(@class, 'MuiCheckbox')]");
	
	By conflictCalArrow     = By.cssSelector(".rdna-accordion-toggle-caret");
	By conflictCalInExpand  = By.cssSelector(".rdna-accordion-row.open.overflow");
	
	By availablityMessage   = By.xpath("//h6[contains(text(), 'Select the calendars you want ringDNA')]");
	
	String selectConflictCalByName = "//input[@value= '%name%']/ancestor::span[contains(@class, 'MuiCheckbox-root')]";
	String CalendarInExpand        = "//span[contains(text(), '%email%')]/ancestor::div[@class ='rdna-accordion-row open overflow']";
	String calInExpandButton       = "//span[contains(text(), '%email%')]/ancestor::div[contains(@data-testid,'accordion-row')]//div[@class='rdna-accordion-toggle-caret']";
	
	// Calendar Sub-headings
	By meetingSettings      = By.xpath("//a[contains(text(), 'Meeting Settings')]");
	By calendarHours        = By.xpath("//a[contains(text(), 'Hours')]");
	By meetingTypes         = By.xpath("//a[contains(text(), 'Meeting Types')]");
	By calendarPersonalize  = By.xpath("//a[contains(text(), 'Personalize')]");
	By calAvailablity       = By.xpath("//a[contains(text(), 'Availability')]");
	
	//***Calendar Locators***//
	
	//***Meetings Settings Locators***//
	
	// meeting settings
	By leadTimeOptions      = By.xpath("//input[@name= 'bookingLeadTimeInHours']");
	By futureBookingOptions = By.xpath("//input[@name= 'maxFutureBookingInDays']");
	
	// message Setting Question (Lead time and max future booking)
	By question1            = By.xpath("//legend[contains(text() ,'lead time')]");
	By question2            = By.xpath("//legend[contains(text() ,'future') and contains(text() ,'booking') ]");
	By maxMeetingsQues      = By.xpath("//legend[contains(text() ,'meetings') and contains(text() ,'max') ]");
	By calBufferQues        = By.xpath("//legend[contains(text() ,'calendar') and contains(text() ,'buffer') ]");
	
	//toast Message
	By toastMessage			= By.cssSelector(".toast-message");
	
	//custom lead time and max future input
	By leadTimeInput        = By.xpath("//input[@placeholder = 'hours']");
	By futureBookingInput   = By.xpath("//input[@placeholder = 'days']");
	
	//Save Button
	By disabledSaveButton   = By.xpath("//button[@Disabled and text()= 'Save']");
	By SaveButton           = By.xpath("//button[contains(@class,'rdna-button') and contains(text(),'Save')]");
	
	//custom
	By maxFutureCustom      = By.xpath("//input[@placeholder = 'days']");
	By leadTimecustom       = By.xpath("//input[@placeholder = 'hours']");     
	
	//String for get option by name
	By leadTime              = By.xpath("//div[@aria-label='bookingLeadTimeInHours']/label//span[contains(@class, 'MuiFormControlLabel-label')]");
	String leadTimeCheckBox  = "//span[text() = '%text%']/preceding-sibling::span//input";
	
	By maxFutureTime         = By.xpath("//div[@aria-label='maxFutureBookingInDays']/label//span[contains(@class, 'MuiFormControlLabel-label')]");
	String maxFutureCheckBox = "//span[text() = 'custom']/preceding-sibling::span//input[@name= 'maxFutureBookingInDays']";
	String maxFutureSelected = "//input[@value ='%text%' and @name='maxFutureBookingInDays' and @checked]/ancestor::span[2]";
	
	//***Meetings Settings Locators***//
	
	//profile locators
	By userProfileDropDown  = By.cssSelector(".user-profile.dropdown-toggle");
	By userProfileCalendar  = By.cssSelector(".user-calendar-url");
	
	//***Calendar Hours Locators***//
	
	By holidayCalendar      = By.xpath("//input[@name='holidayCalendar']/following-sibling::*[local-name() ='svg']");
	By holidayCalendarList  = By.xpath("//ul[@role='listbox']/li//span//span[contains(@class , 'MuiIconButton-label')]");
	By holidayCalTextList   = By.xpath("//ul[@role='listbox']/li");
	By holidayCalDelete     = By.xpath("//ul[@role='listbox']/li//span//input[@checked]/parent::span[contains(@class , 'MuiIconButton-label')]");
	By getHolidayNameSel    = By.xpath("//div[@data-testid='holidayCalendar-input']/div");
	By hoursHeadinglineText = By.xpath("//span[contains(text(), 'Designate')]");
	By holidayCalHeading    = By.xpath("//span[contains(text(), 'holiday calendars')]");
	By unavilableSlot       = By.cssSelector(".flex.unavailable-slots");
	
	String time             = "//ul[@role='listbox']/li[@data-value= '%text%']";
	String timeText         = "//li[@role='option' and (text() = '%text%')]";
	String daysCheckbox     = "//input[@value= '%Day%']/ancestor::span[2]";
	String dayChecked       = "//input[@value= '%Day%']/ancestor::span[contains(@class, 'Mui-checked')]";
	
	By endTimeList          = By.xpath("//div[contains(@data-testid, 'endTime')]/div");
	By startTimeList        = By.xpath("//div[contains(@data-testid, 'startTime')]/div");
	
	String setEndTimeForAnyDay   = "//div[contains(@data-testid, '%day%-endTime')]";
	String setStartTimeForAnyDay = "//div[contains(@data-testid, '%day%-startTime')]";
	String pickDayByDate    = "//div[@cursor='pointer']//div[text()='%month%' and text()='%day%']";
	
	String getDayEndTime    = "//div[contains(@data-testid, '%day%-endTime')]/div";
	String getDayStartTime    = "//div[contains(@data-testid, '%day%-startTime')]/div";
	
	//***Calendar Hours Locators***//
	
	//***Meeting Types***//
	
	By meetTypeDefaultCal   = By.cssSelector("#ringdna-select");
	By meetTypeDefultCalList = By.xpath("//ul[@role='listbox']/li"); 
	String meetTypeDefaultCalByName = "//ul[@role='listbox']/li[contains(text(), '%defaultCal%')]";
	
	//add meeting type
	By addMeetingType       = By.xpath("//*[@stroke-linecap= 'round']/preceding-sibling::*");
	
	By meetingdurationInput = By.xpath("//div[@data-testid='duration-input']/div[@id='ringdna-select']");
	By meetingNameInput     = By.cssSelector("#name");
	By meetingDescription   = By.cssSelector("#description");
	By meetingEventLink     = By.cssSelector("#eventLinkName");
	By meetingVideoLink     = By.xpath("//div[@id='ringdna-select']/parent::div[@data-testid='videoConferenceType-input']");
	By meetingCustomVideo   = By.xpath("//li[@data-testid='option-Custom']");
	By meetingEnterVideoUrl = By.cssSelector("#videoConferenceUrl");
	By submitButton         = By.xpath("//button[text() = 'Submit']");
	
	By locationInput        = By.xpath("//span[text() = 'Location']/parent::legend/parent::fieldset");
	
	By meetingCardHeading   = By.xpath("//p[contains(@class, 'meeting-card-heading')]");
	By toggleIsOn           = By.xpath("//span[contains(@class , 'Mui-checked')]");
	By toggleButton         = By.xpath("//span[@class='MuiSwitch-root']/span[contains(@class, 'MuiButtonBase-root MuiIconButton')]");
	By calendarLink         = By.xpath("//a[contains(text() ,'/calendar/')]");
	By meetingType3Dots     = By.xpath("//span[@class ='MuiIconButton-label']/parent::button");
	By deleteInDropDown     = By.xpath("//span[text()= 'Delete']");
	By copyLinkInDropDown   = By.xpath("//span[text()= 'Copy Link']");
	By editInDropDown       = By.xpath("//span[text()= 'Edit']");
	By meetingTypesDuration = By.xpath("//p[contains(@class, 'meeting-card-heading')]/following-sibling::p");
	
	
	//***Meeting Types***//
	
	//***Personalize***//
	
	By yourCalLink          = By.xpath("//*[@data-testid= 'settings-link']");
	
	By personalizeName      = By.xpath("//p[text() = 'Name']/following-sibling::p");
	By personalizeCountry   = By.xpath("//p[text() = 'Country']/following-sibling::p");
	By personalizeTimezone  = By.xpath("//p[text() = 'Timezone']/following-sibling::p");
	By personalizeCalLink   = By.xpath("//p[contains(text() ,'Calendar Link')]/following-sibling::a");
	By wlcmMessageEditButton = By.xpath("//button[@name='edit-personal-message']");
	
	By welcomeMessageInput  = By.cssSelector("#welcomeMessage");
	By welcomeMsgSaveButton = By.xpath("//button[@name='buttons-container-submit']");
	
	//***Personalize***//
	
	//***Meeting Pages Locators***//
	By invalidUrlMessage    = By.xpath("//div[@id='root']//div/following-sibling::div/following-sibling::div");
	By oopsWarning          = By.xpath("//h2[contains(text(), 'no public calendar')]");
	By pleaseWarning        = By.xpath("//p[contains(text(), 'Please reach out')]");
	
	// Book Meeting Slot Page
	By meetingTitle2        = By.xpath("//div[@class='slot-title']");
	
	By blockedDayMessage    = By.xpath("//div[text()= 'Sorry, No Options Available']");
	String dayPicker        = "//div[@class= 'weekly-style' and text()= '%Day%']";
	String datePicker       = "//div[@class= 'day-style' and text()= '%Date%']";
	
	By bookMeetingPrev      = By.xpath("//div[@data-testid='pagination-label-< Prev']");
	By bookMeetingNext      = By.xpath("//div[@data-testid='pagination-label-Next >']");
	By pickWeekButton       = By.xpath("//div[@data-testid='toggle-week']");
	By pickMonthButton      = By.xpath("//div[@data-testid='toggle-month']");
	By getCurrentDate       = By.xpath("//div//div[contains(text(),'Today')]/following-sibling::div//div/following-sibling::div");
	
	By unavailableBlockDays = By.xpath("//div[@cursor='default']/parent::li");      
	By pickAvailableDay     = By.xpath("//div[@class='day-style']");
	By pickWorkingAvaiableDay = By.xpath("//div[@cursor='pointer']");
	By pickTimeText         = By.xpath("//a[contains(@href, 'calendar')]/div/div");
	By pickTimeSlots        = By.xpath("//a[contains(@href, 'calendar')]/parent::li");
	
	By circleOfWorkingDay   = By.xpath("//div[@cursor='pointer']//div[contains(@class, 'gLiaon part')]//*[name()='svg']/*[name()='circle']");
	By circleOfBlockedDay   = By.xpath("//div[@cursor='default']//div[contains(@class, 'gLiaon part')]//*[name()='svg']/*[name()='circle']");
	By amPmTextOnSlotPage   = By.cssSelector(".label-style");
	By ringDnaLogo          = By.xpath("//header//a//div");
	By welcomeMsgOnsoftphone = By.xpath("//div[@id='root']//h2");
	By personalCalUserName  = By.xpath("//a[contains(@href, 'calendar')]/following-sibling::h1");
	By backButton           = By.xpath("//div[@id='root'] //button");
	
	String slotAvailForDay  = "//div[text()='%day%']/ancestor::div[2]//div[contains(@class, 'gLiaon part')]//*[name()='svg']/*[name()='circle']";
	
	//meeting card details
	By meetingSlot          = By.xpath("//div[@class='slot-duration']");
	By nextAvailableTime    = By.xpath("//div[@class='slot__nexttime']");
	
	//Schedule Event Summary
	
	By fullName             = By.cssSelector("#name");
	By emailId              = By.cssSelector("#email");
	By contactNumber        = By.cssSelector("#phone");
	By companyName          = By.cssSelector("#company");
	By description          = By.cssSelector("#comments");
	
	By emailIdError         = By.cssSelector("#email-helper-text");
	By contactNoError       = By.cssSelector("#phone-helper-text");
	By descriptionError         = By.cssSelector("#comments-helper-text");
	
	By scheduleButton       = By.xpath("//button[text()= 'Schedule Event']");
	By scheduleFormEventTime = By.xpath("//span[contains(@class, 'time')]");
	
	By confirmationHeading  = By.xpath("//h1[text()= 'Confirmed!']");
	By salesAcademyButton   = By.xpath("//a[text()= 'Sales Academy Blog']");
	By salesAcademyPageLink = By.xpath("//link[@rel='canonical']");
	By salesAcademyLogo     = By.xpath("//h1[text()= 'Sales Academy']");
	
	By scheduleMeetingType  = By.xpath("//span[contains(@class, 'duration')]");
	By scheduleEventName    = By.xpath("//span[contains(@class, 'bookee-name')]");
	By scheduleEventTime    = By.xpath("//span[contains(@class, 'time')]");
	
	By fieldPlaceHolderValues  = By.xpath("//fieldset[contains(@class, 'notchedOutline')]/ancestor::div/preceding-sibling::label");
	
	//Schedule Event Summary
	
	By whenNoMeetingAvailable = By.xpath("//div[contains(text(), 'This user has not activated')]");
	
	//Account account
	By licensingTab			= By.cssSelector("[data-tab='integrations']");
	By licensingTabParent   = By.xpath(".//*[@data-tab='integrations']/ancestor::li");
	By licensingTabHeading  = By.xpath("//h2[text()='Video Conferencing']");
	
	//over view
	By username             = By.cssSelector(".user-name");
	By timeZone             = By.xpath("//label[text()='Time Zone']/following-sibling::span");
	
	// zoom page
	By accessErrorOnZoom    = By.cssSelector(".error-message");

	
	/**
	 * @author Pranshu
	 *
	 */
	// Enum for Lead time
	public static enum leadTimeEnum {
		Immediate("0"), One("1"), Two("2"), Three("3"), Four("4"), Custom("custom"), Five("5");

		private String value;

		leadTimeEnum(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * @author Pranshu
	 *
	 */
	// Enum for max Future Booking
	public static enum maxFutureBooking {
		Thirty("30"), FortyFive("45"), Sixty("60"), Custom("custom"), Seventy("70"), One("1");

		private String value;

		maxFutureBooking(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * @author Pranshu
	 *
	 */
	// Enum for Week Days
	public static enum WeekDaysEnum {
		Monday("Monday"), Tuesday("Tuesday"), Wednesday("Wednesday"), Thursday("Thursday"), Four("Friday"),
		Saturday("Saturday"), Sunday("Sunday");

		private String value;

		WeekDaysEnum(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * @author Pranshu
	 *
	 */
	// Enum for meeting type
	public static enum meetingTypeEnum {
		Fiveteen("15"), FortyFive("45"), Sixty("60"), Thirty("30");

		private String value;

		meetingTypeEnum(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * @Desc: open Integration tab
	 * @param driver
	 */
	public void openIntegrationTab(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		idleWait(4);
		waitUntilVisible(driver, integrationHeading);
		clickElement(driver, integrationHeading);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc: open Calendar tab
	 */
	public void openCalendarTab(WebDriver driver) {
		if (!findElement(driver, calendarTabParent).getAttribute("class").contains("active")) {
			dashboard.isPaceBarInvisible(driver);
			waitUntilVisible(driver, calendarTab);
			clickByJs(driver, calendarTab);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @Desc: is Calendar tab visible
	 */
	public boolean isCalendarTabVisible(WebDriver driver) {
		return isElementVisible(driver, calendarTab, 5);
	}
	
	/**
	 * @Desc : check the default calendar is visible or not
	 * @return boolean
	 */
	public boolean isDefaultCalendarVisible(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return isElementVisible(driver, defaultCalendar, 5);
	}
	
	/**
	 * @Desc : check the default calendar is visible or not on integration tab
	 * @return boolean
	 */
	public boolean isDefaultCalOnIntegrationTab(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return isElementVisible(driver, integrationDefaultCal, 6);
	}
	
	/**
	 * @param driver
	 * @return cal
	 */
	public String getDefaultCalOnIntegrationTab(WebDriver driver) {
		String cal = findElement(driver, integrationDefaultCal).getText();
		return cal;
	}
	
	/**
	 * @param driver
	 * @return cal
	 */
	public String getNoCalTextOnIntegrationTab(WebDriver driver) {
		waitUntilVisible(driver, integrationTextWhenNoCal);
		return findElement(driver, integrationTextWhenNoCal).getText();
	}

	/**
	 * @param driver
	 * @param name
	 */
	public void checkConflictCalByName(WebDriver driver, String name) {
		By locator = By.xpath(selectConflictCalByName.replace("%name%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	/**
	 * @param driver
	 * availablity tab heading text
	 */
	public void verifyAvailablityTabHeadingText(WebDriver driver) {
		waitUntilVisible(driver, availablityMessage);
		String text = findElement(driver, availablityMessage).getText();
		assertEquals("Select the calendars you want ringDNA to check for availability in real time before offering invitees open slots.", text);
	}
		
	/**
	 * @param driver
	 * @param count
	 */
	public void checkConflictCalendar(WebDriver driver, int count) {
		isDefaultCalendarVisible(driver);
		if(getElements(driver, conflictCalCheckBox).size() >= 6) {
			int alreadyChecked = getElements(driver, checkedConflictCal).size();
			for(int i=alreadyChecked; i< count; i++) {
				waitUntilVisible(driver, uncheckConflictCal);
				clickElement(driver, uncheckConflictCal);
			}
		}
	}
	
	/**
	 * uncheck all conflict cal
	 * @param driver
	 */
	public void unCheckConflictCal(WebDriver driver) {
		List<WebElement> alreadyChecked = getElements(driver, checkedConflictCal);
		for(WebElement cal : alreadyChecked) {
			waitUntilVisible(driver, cal);
			clickElement(driver, cal);
		}
	}
	
	/**
	 * @param driver
	 * @param name
	 * @return check conflict cal is selected on not 
	 */
	public boolean checkConflictCalIsSelectedOrNot(WebDriver driver, String name){
		By locator = By.xpath(selectConflictCalByName.replace("%name%", name));
		return isElementPresent(driver, locator, 5);
	}
	
	/**
	 * @param driver
	 * @return no of conflict cal selected
	 */
	public int getNumberOfCheckedConflictCalSelected(WebDriver driver){
		int answer = 0;
		dashboard.isPaceBarInvisible(driver);
		if(isElementVisible(driver, checkedConflictCal, 10)) {
			List<WebElement> alreadyChecked = getElements(driver, checkedConflictCal);
			answer = alreadyChecked.size();
		}
		return answer;
	}
	
	/**
	 * @Desc : is toast message visible
	 */
	public boolean isToastMessageVisible(WebDriver driver) {
		return isElementVisible(driver, toastMessage, 6);
	}
	
	/**
	 * @Desc : verify conflict cal error by toast message
	 */
	public void verifyConflictCalToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		assertTrue(getElementsText(driver, toastMessage).contains("You have reached the maximum number of selected calendars."));
		waitUntilInvisible(driver, toastMessage);
	}
	
	/**
	 * @Desc : verify at least one conflict cal error by toast message
	 */
	public void verifyAtLeastOneConflictCalToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		assertTrue(getElementsText(driver, toastMessage).contains("at least one conflict calendar on your Default Calendar must be checked at all times."));
		waitUntilInvisible(driver, toastMessage);
	}

	/**
	 * @Desc : click Meeting Settings tab
	 */
	public void clickMeetingSettings(WebDriver driver) {
		waitUntilVisible(driver, meetingSettings);
		clickElement(driver, meetingSettings);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc : click Meeting Types tab
	 */
	public void clickMeetingTypesTab(WebDriver driver) {
		waitUntilVisible(driver, meetingTypes);
		clickElement(driver, meetingTypes);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc : check the Lead Time Options is visible or not
	 */
	public void isLeadTimeOptionsVisible(WebDriver driver) {
		waitUntilVisible(driver, leadTime);
		List<String> options = getTextListFromElements(driver, leadTime);
		//assertTrue(options.get(0).equals(leadTimeEnum.Immediate.getValue()));
		assertTrue(options.get(1).contains(leadTimeEnum.One.getValue()));
		assertTrue(options.get(2).contains(leadTimeEnum.Two.getValue()));
		assertTrue(options.get(3).contains(leadTimeEnum.Three.getValue()));
		assertTrue(options.get(4).contains(leadTimeEnum.Four.getValue()));
		assertTrue(options.get(5).toLowerCase().contains(leadTimeEnum.Custom.getValue()));
	}

	/**
	 * @Desc : check the Future Booking Options is visible or not
	 */
	public void isFutureBookingOptionsVisible(WebDriver driver) {
		waitUntilVisible(driver, maxFutureTime);
		List<String> options = getTextListFromElements(driver, maxFutureTime);
		assertTrue(options.get(0).contains(maxFutureBooking.Thirty.getValue()));
		assertTrue(options.get(1).contains(maxFutureBooking.FortyFive.getValue()));
		assertTrue(options.get(2).contains(maxFutureBooking.Sixty.getValue()));
		assertTrue(options.get(3).toLowerCase().contains(maxFutureBooking.Custom.getValue()));
	}
	
	/**
	 * @param driver
	 */
	public void leadAndMaxFuturePlaceHolderValues(WebDriver driver) {
		String leadCustom = getAttribue(driver, leadTimecustom, ElementAttributes.Placeholder);
		assertEquals("hours", leadCustom);
		String FutureCustom = getAttribue(driver, maxFutureCustom, ElementAttributes.Placeholder);
		assertEquals("days", FutureCustom);
	}

	/**
	 * @Desc : select Lead Time
	 * @param text
	 */
	public void selectLeadTime(WebDriver driver, leadTimeEnum text) {
		clickElement(driver, leadTimecustom);
		clearAll(driver, leadTimecustom);
		enterText(driver, leadTimecustom, text.getValue());
	}

	/**
	 * @Desc : select Max Future Booking
	 * @param text
	 */
	public void selectMaxFutureBooking(WebDriver driver, maxFutureBooking text) {
		clickElement(driver, maxFutureCustom);
		clearAll(driver, maxFutureCustom);
		enterText(driver, maxFutureCustom, text.getValue());
	}

	/**
	 * @Desc : check the Message Settings questions is visible or not
	 */
	public void isMessageSettingsQuestionsVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, question1, 5));
		assertEquals(findElement(driver, question1).getText(),
				"How much lead time is needed to schedule an event on your calendar?");
		assertTrue(isElementVisible(driver, question2, 5));
		assertEquals(findElement(driver, question2).getText(),
				"How many days in the future will events be available for booking?");
		assertFalse(isElementVisible(driver, maxMeetingsQues, 5));
		assertFalse(isElementVisible(driver, calBufferQues, 5));
	}
	
	/**
	 * @Desc : check custom Lead And Future Booking Text
	 */
	public void customLeadAndFutureBookingText(WebDriver driver) {
		String hours = getAttribue(driver, leadTimecustom, ElementAttributes.Placeholder);
		String days = getAttribue(driver, maxFutureCustom, ElementAttributes.Placeholder);
		assertEquals(hours, "hours");
		assertEquals(days, "days");
	}

	
	/**
	 * @param check the default message settings options is selected
	 */
	public void isDefaultLeadTimeAndMaxFutureBookingIsSelected(WebDriver driver) {
		By lead = By.xpath(leadTimeCheckBox.replace("%text%", leadTimeEnum.Two.getValue()));
		assertTrue(isAttributePresent(driver, lead, "checked"));
		By booking = By.xpath(leadTimeCheckBox.replace("%text%", maxFutureBooking.Sixty.getValue()));
		assertTrue(isAttributePresent(driver, booking, "checked"));
	}

	/**
	 * @Desc : verify error by toast message
	 */
	public void verifyCustomToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		assertTrue(getElementsText(driver, toastMessage).contains("Please enter a valid number"));
		waitUntilInvisible(driver, toastMessage);
	}

	/**
	 * @Desc : verify success save by toast message
	 */
	public void verifySucessSaveToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		String text = getElementsText(driver, toastMessage);
		assertTrue(text.contains("Your personal calendar settings were saved successfully"));
		waitUntilInvisible(driver, toastMessage);
	}

	/**
	 * @Desc : verify custom out of limit by toast message
	 */
	public void verifyCustomOutOfLimitToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		String text = getElementsText(driver, toastMessage);
		assertTrue(text.contains("The maximum days available to book in the future is 365, please enter another value."));
		waitUntilInvisible(driver, toastMessage);
	}

	/**
	 * @Desc : enter custom lead time
	 * @param text
	 */
	public void enterLeadTimeCustom(WebDriver driver, String text) {
		waitUntilVisible(driver, leadTimeInput);
		clickElement(driver, leadTimeInput);
		clearAll(driver, leadTimeInput);
		enterText(driver, leadTimeInput, text);
	}

	/**
	 * @Desc : enter future booking day
	 * @param text
	 */
	public void enterMaxFutureBookingCustom(WebDriver driver, String text) {
		waitUntilVisible(driver, futureBookingInput);
		clickElement(driver, futureBookingInput);
		clearAll(driver, futureBookingInput);
		enterText(driver, futureBookingInput, text);
	}

	/**
	 * @Desc : click Save Button
	 */
	public void clickMeetingSettingsSaveButton(WebDriver driver) {
		if (!isElementVisible(driver, disabledSaveButton, 6)) {
			waitUntilVisible(driver, SaveButton);
			clickElement(driver, SaveButton);
			isToastMessageVisible(driver);
			waitUntilInvisible(driver, toastMessage);
			dashboard.isPaceBarInvisible(driver);
		}
	}

	/**
	 * @Desc : check is save button disabled
	 * @param driver
	 * @return boolean
	 */
	public boolean isSaveButtonDisabled(WebDriver driver) {
		return isElementVisible(driver, disabledSaveButton, 6);
	}

	/**
	 * @Desc : which radio button is selected
	 * @param text
	 */
	public void assertWhichFutureBookingRadioButtonIsSelected(WebDriver driver, maxFutureBooking text) {
		By booking = By.xpath(maxFutureSelected.replace("%text%", text.getValue()));
		assertTrue(isElementVisible(driver, booking, 6));
	}

	/**
	 * @Desc : which radio button is selected
	 * @param text
	 */
	public void assertWhichLeadTimeRadioButtonIsSelected(WebDriver driver, leadTimeEnum text) {
		By booking = By.xpath(leadTimeCheckBox.replace("%text%", text.getValue()));
		assertTrue(isAttributePresent(driver, booking, "checked"));
	}

	// *** Calendar Hours Methods ***//

	/**
	 * @Desc : click Hours tab
	 */
	public void clickCalendarHours(WebDriver driver) {
		waitUntilVisible(driver, calendarHours);
		clickElement(driver, calendarHours);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc : click Holiday Calendar Input
	 */
	public void clickHolidayCalendarInput(WebDriver driver) {
		waitUntilVisible(driver, holidayCalendar);
		scrollToElement(driver, holidayCalendar);
		Actions builder = new Actions(driver);
		builder.click(findElement(driver, holidayCalendar)).build().perform();
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc : is Holiday Calendar list visible
	 */
	public void isHolidayCalendarListVisible(WebDriver driver) {
		isListElementsVisible(driver, holidayCalendarList, 6);
	}

	/**
	 * @Desc : select holiday calendar by index number
	 * @param driver
	 * @param index
	 */
	public void selectNumberOfHolidayCalendar(WebDriver driver, int index) {
		List<WebElement> holidayCal = getElements(driver, holidayCalendarList);
		for (int loop = 1; loop <= index; loop++) {
				waitUntilVisible(driver, holidayCal.get((loop - 1)));
				clickElement(driver, holidayCal.get((loop - 1)));
		}
	}

	/**
	 * @Desc : remove selected holiday calendar
	 * @param driver
	 */
	public void removeHolidayCalendar(WebDriver driver) {
		int index = 0;
		List<WebElement> holidayCal = getElements(driver, holidayCalDelete);
		for (int loop = 1; loop <= holidayCal.size(); loop++) {
			waitUntilVisible(driver, holidayCal.get((index)));
			clickElement(driver, holidayCal.get((index)));
		}
	}

	/**
	 * @Desc : check conflict calendar should not present in holiday calendar drop
	 *       down
	 * @param driver
	 */
	public void checkConflictCalendarIsNotPresentInHolidayCalendar(WebDriver driver) {
		List<String> conflictCal = getTextListFromElements(driver, conflictCalendars);
		List<String> holidayCal = getTextListFromElements(driver, holidayCalTextList);
		assertFalse(holidayCal.containsAll(conflictCal));

	}

	/**
	 * @param driver
	 * @return get the holiday drop down list
	 */
	public List<String> getHolidayCalendarListText(WebDriver driver) {
		waitUntilVisible(driver, holidayCalTextList);
		return getTextListFromElements(driver, holidayCalTextList);
	}

	/**
	 * @param driver
	 * @return get all name of conflict calendar
	 */
	public List<String> getConflictCalendarListText(WebDriver driver) {
		waitUntilVisible(driver, conflictCalendars);
		return getTextListFromElements(driver, conflictCalendars);
	}
	
	/**
	 * @param driver
	 * @return hours head line text
	 */
	public String getHoursHeadLineText(WebDriver driver) {
		waitUntilVisible(driver, hoursHeadinglineText);
		return findElement(driver, hoursHeadinglineText).getText();
	}
	
	/**
	 * @param driver
	 * @return hours head line text
	 */
	public String getHolidayHeadLineText(WebDriver driver) {
		waitUntilVisible(driver, holidayCalHeading);
		return findElement(driver, holidayCalHeading).getText();
	}
	
	/**
	 * @param driver
	 * @return hours head line text
	 */
	public String getHolidayNameSelected(WebDriver driver) {
		waitUntilVisible(driver, getHolidayNameSel);
		return findElement(driver, getHolidayNameSel).getText();
	}
	
	/**
	 * @Desc : check Day check boxes
	 * @param driver
	 * @param day
	 */
	public void selectDayCheckBox(WebDriver driver, String day) {
		By locator = By.xpath(daysCheckbox.replace("%Day%", day));
		By checkLocator = By.xpath(dayChecked.replace("%Day%", day));
		if (!isElementVisible(driver, checkLocator, 10)) {
			clickElement(driver, locator);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @Desc : un check Day check boxes
	 * @param driver
	 * @param day
	 */
	public void unSelectDayCheckBox(WebDriver driver, String day) {
		By locator = By.xpath(daysCheckbox.replace("%Day%", day));
		By checkLocator = By.xpath(dayChecked.replace("%Day%", day));
		if (isElementVisible(driver, checkLocator, 10)) {
			waitUntilVisible(driver, locator);
			clickElement(driver, locator);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @Desc : click Upward For StartTime
	 * @param driver
	 * @param day
	 */
	public void clickUpwardForStartTime(WebDriver driver, String day, String StartTime) {
		// enter start time
		By locator = By.xpath(setStartTimeForAnyDay.replace("%day%", day));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		By selectTime = By.xpath(timeText.replace("%text%", StartTime));
		waitUntilVisible(driver, selectTime);
		findElement(driver, selectTime).sendKeys(Keys.DOWN);
		clickEnter(driver, selectTime);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @Desc : click Downward for EndTime
	 * @param driver
	 * @param day
	 */
	public void clickDownwardforEndTime(WebDriver driver, String day, String EndTime) {
		By locator = By.xpath(setEndTimeForAnyDay.replace("%day%", day));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		By selectTime = By.xpath(timeText.replace("%text%", EndTime));
		waitUntilVisible(driver, selectTime);
		findElement(driver, selectTime).sendKeys(Keys.DOWN);
		clickEnter(driver, selectTime);
		dashboard.isPaceBarInvisible(driver);
	}
	
	
	/**
	 * @Desc : enter time of day in hours tab 
	 * @param driver
	 */
	public void enterTimeOfDayInHoursTab(WebDriver driver, String day, String StartTime, String EndTime) {
		By locator = By.xpath(setEndTimeForAnyDay.replace("%day%", day));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		idleWait(1);
		By selectTime = By.xpath(timeText.replace("%text%", EndTime));
		waitUntilVisible(driver, selectTime);
		clickElement(driver, selectTime);
		dashboard.isPaceBarInvisible(driver);
		// enter start time
		locator = By.xpath(setStartTimeForAnyDay.replace("%day%", day));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		idleWait(1);
		selectTime = By.xpath(timeText.replace("%text%", StartTime));
		waitUntilVisible(driver, selectTime);
		clickElement(driver, selectTime);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @Desc : get End time of day in hours tab 
	 * @param driver
	 */
	public String getDayEndTime(WebDriver driver, String day) {
		By locator = By.xpath(getDayEndTime.replace("%day%", day));
		waitUntilVisible(driver, locator);
		return findElement(driver, locator).getText();
	
	}
	
	/**
	 * @Desc : get Start time of day in hours tab 
	 * @param driver
	 */
	public String getDayStartTime(WebDriver driver, String day) {
		By locator = By.xpath(getDayStartTime.replace("%day%", day));
		waitUntilVisible(driver, locator);
		return findElement(driver, locator).getText();
	}
	
	/**
	 * @param driver
	 * @return number of unavailable days
	 */
	public int getUnavilableDaysCount(WebDriver driver) {
		if(isElementVisible(driver, unavilableSlot, 6)){
			return getElements(driver, unavilableSlot).size();
		}else {
			return 0;
		}
	}
	
	/**
	 * @param driver
	 * @return get of unavailable days text
	 */
	public String getUnavilableDaysText(WebDriver driver) {
		List <WebElement> unavailable = getElements(driver, unavilableSlot);
		return getElementsText(driver, unavailable.get(0));
	}
	
	/**
	 * @param driver
	 * click next button on personal cal page
	 */
	public void clickNextButtonOnCalendarPage(WebDriver driver) {
		waitUntilVisible(driver, bookMeetingNext);
		clickElement(driver, bookMeetingNext);
		idleWait(3);
	}
	
	/**
	 * @param driver
	 * @return number of available days on meeting type
	 */
	public int getAvilableDaysCountOnCalendarPage(WebDriver driver) {
		waitUntilVisible(driver, pickWorkingAvaiableDay);
		return getElements(driver, pickWorkingAvaiableDay).size();
	}
	
	/**
	 * @param driver
	 * @param day
	 * @return boolean is day blocked or not
	 */
	public boolean verifyDayIsBlocked(WebDriver driver, String day) {
		By locator = By.xpath(dayPicker.replace("%Day%", day));
		waitUntilVisible(driver, locator);
		hoverElement(driver, locator);
		return isElementVisible(driver, blockedDayMessage, 6);
	}

	// meeting type

	/**
	 * @param driver
	 * @return cal
	 */
	public String getAvailablityDefaultCalendar(WebDriver driver) {
		String cal = findElement(driver, defaultCalendar).getText();
		cal = cal.replace(" (default)", "");
		return cal;
	}

	/**
	 * @param driver
	 * @return cal
	 */
	public String getMeetingTypeDefaultCalendar(WebDriver driver) {
		String cal = findElement(driver, meetTypeDefaultCal).getText();
		cal = cal.replace(" (default)", "");
		cal = cal.substring((cal.indexOf(" ")+1));
		return cal;
	}
	
	/**
	 * @param driver
	 * @return visibility of meeting type default cal
	 */
	public boolean isMeetingTypeDefaultCalendarVisible(WebDriver driver) {
		return isElementVisible(driver, meetTypeDefaultCal, 5);
	}
	
	/**
	 * select default cal
	 * @param driver
	 */
	public void selectAnotherDefaultCal(WebDriver driver) {
		waitUntilVisible(driver, meetTypeDefaultCal);
		clickElement(driver, meetTypeDefaultCal);
		List<WebElement> defaultCal = getElements(driver, meetTypeDefultCalList);
		waitUntilVisible(driver, defaultCal.get(1));
		clickElement(driver, defaultCal.get(1));
	}
	
	/**
	 * get meeting type default cal list
	 * @param driver
	 */
	public List<String> getMeetingTypeDefaultCalList(WebDriver driver) {
		List<String> returnList = new ArrayList<String>();
		waitUntilVisible(driver, meetTypeDefaultCal);
		clickElement(driver, meetTypeDefaultCal);
		List<String> calList = getTextListFromElements(driver, meetTypeDefultCalList);
		for(String cal : calList) {
			if(cal.contains("(default)"))
			cal = cal.replace(" (default)", "");
			
			cal = cal.substring(cal.indexOf(" ")+1);
			returnList.add(cal);
		}
		List<WebElement> defaultCal = getElements(driver, meetTypeDefultCalList);
		waitUntilVisible(driver, defaultCal.get(0));
		clickElement(driver, defaultCal.get(0));
		return returnList;
	}
	
	/**
	 * select default cal
	 * @param driver
	 */
	public void selectDefaultCalByName(WebDriver driver, String cal) {
		waitUntilVisible(driver, meetTypeDefaultCal);
		clickElement(driver, meetTypeDefaultCal);
		
		By locator = By.xpath(meetTypeDefaultCalByName.replace("%defaultCal%", cal));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	/**
	 * @param driver
	 * @param text
	 * @return is cal in expand mode
	 */
	public boolean isCalendarInExpandMode(WebDriver driver, String text) {
		By locator = By.xpath(CalendarInExpand.replace("%email%", text));
		return isElementVisible(driver, locator, 5);
	}
	
	/**
	 * @param driver
	 * @param text
	 * @return click cal in expand mode
	 */
	public void clickCalendarforExpandMode(WebDriver driver, String text) {
		By locator = By.xpath(calInExpandButton.replace("%email%", text));
		waitUntilVisible(driver, locator);
		System.out.println();
		clickElement(driver, locator);
	}

	/**
	 * @Desc : click add meeting type button
	 * @param driver
	 */
	public void clickAddMeetingTypeButton(WebDriver driver) {
		waitUntilVisible(driver, addMeetingType);
		clickElement(driver, addMeetingType);
	}

	/**
	 * @param driver
	 * @param text
	 */
	public void enterMeetingName(WebDriver driver, String text) {
		waitUntilVisible(driver, meetingNameInput);
		clearAll(driver, meetingNameInput);
		enterText(driver, meetingNameInput, text);
	}

	/**
	 * @Desc : select meeting type
	 * @param text
	 */
	public void selectMeetingDuration(WebDriver driver, meetingTypeEnum text) {
		By locator = By.xpath(time.replace("%text%", text.getValue()));
		waitUntilVisible(driver, meetingdurationInput);
		clickElement(driver, meetingdurationInput);
		idleWait(1);
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @param driver
	 * @param meetingName
	 * @param text
	 */
	public void createMeeting(WebDriver driver, String meetingName, meetingTypeEnum text) {
		clickAddMeetingTypeButton(driver);
		
		//check meeting time is 30 by default
		waitUntilVisible(driver, meetingdurationInput);
		String defaultTime = findElement(driver, meetingdurationInput).getText();
		assertEquals("30 Minutes", defaultTime);
		
		enterMeetingName(driver, meetingName);
		selectMeetingDuration(driver, text);

		// enter event link
		waitUntilVisible(driver, meetingEventLink);
		clickElement(driver, meetingEventLink);

		// Description
		waitUntilVisible(driver, meetingDescription);
		enterText(driver, meetingDescription, "Testing");
		
		String name = getAttribue(driver, meetingEventLink, ElementAttributes.value);
		meetingName = meetingName.replaceAll(" ", "").toLowerCase();
		assertTrue(meetingName.contains(name));

		// submitButton
		waitUntilVisible(driver, submitButton);
		clickElement(driver, submitButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param meetingName
	 * @param text
	 */
	public void createMeetingWithCustomDescription(WebDriver driver, String meetingName, meetingTypeEnum text, String description) {
		clickAddMeetingTypeButton(driver);
		
		//check meeting time is 30 by default
		waitUntilVisible(driver, meetingdurationInput);
		String defaultTime = findElement(driver, meetingdurationInput).getText();
		assertEquals("30 Minutes", defaultTime);
		
		enterMeetingName(driver, meetingName);
		selectMeetingDuration(driver, text);

		// enter event link
		waitUntilVisible(driver, meetingEventLink);
		clickElement(driver, meetingEventLink);

		// Description
		waitUntilVisible(driver, meetingDescription);
		enterText(driver, meetingDescription, description);
		
		String name = getAttribue(driver, meetingEventLink, ElementAttributes.value);
		meetingName = meetingName.replaceAll(" ", "").toLowerCase();
		assertTrue(meetingName.contains(name));

		// submitButton
		waitUntilVisible(driver, submitButton);
		clickElement(driver, submitButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param meetingName
	 * @param text
	 */
	public void createMeetingWithVideoURL(WebDriver driver, String meetingName, meetingTypeEnum text, String url) {
		clickAddMeetingTypeButton(driver);
		
		//check meeting time is 30 by default
		waitUntilVisible(driver, meetingdurationInput);
		String defaultTime = findElement(driver, meetingdurationInput).getText();
		assertEquals("30 Minutes", defaultTime);
		
		enterMeetingName(driver, meetingName);
		selectMeetingDuration(driver, text);

		// enter event link
		waitUntilVisible(driver, meetingEventLink);
		clickElement(driver, meetingEventLink);
		
		// enter video link
		waitUntilVisible(driver, meetingVideoLink);
		clickElement(driver, meetingVideoLink);
		//select custom
		waitUntilVisible(driver, meetingCustomVideo);
		clickElement(driver, meetingCustomVideo);
		
		//enter video link
		waitUntilVisible(driver, meetingEnterVideoUrl);
		enterText(driver, meetingEnterVideoUrl, url);
	
		// Description
		waitUntilVisible(driver, meetingDescription);
		enterText(driver, meetingDescription, "Testing");
		
		String name = getAttribue(driver, meetingEventLink, ElementAttributes.value);
		meetingName = meetingName.replaceAll(" ", "").toLowerCase();
		assertEquals(name, meetingName);

		// submitButton
		waitUntilVisible(driver, submitButton);
		clickElement(driver, submitButton);
	}
	
	/**
	 * @param driver
	 * @param meetingName
	 * @param text
	 */
	public void editMeetingType(WebDriver driver, String meetingName, meetingTypeEnum text) {
		enterMeetingName(driver, meetingName);
		selectMeetingDuration(driver, text);

		// enter event link
		waitUntilVisible(driver, meetingEventLink);
		enterText(driver, meetingEventLink, meetingName);

		// Description
		waitUntilVisible(driver, meetingDescription);
		enterText(driver, meetingDescription, "Testing");

		// submitButton
		waitUntilVisible(driver, submitButton);
		clickElement(driver, submitButton);

		// assertion
		assertTrue(isElementVisible(driver, toggleIsOn, 5));
	}

	/**
	 * @Desc : it will delete all meeting types
	 * @param driver
	 */
	public void deleteMeetingType(WebDriver driver) {
		if (isListElementsVisible(driver, meetingType3Dots, 6)) {
			List<WebElement> meetingTypes = getElements(driver, meetingType3Dots);
			for (int i = 0; i < meetingTypes.size(); i++) {
				List<WebElement> meetingTypes2 = getElements(driver, meetingType3Dots);
				waitUntilVisible(driver, meetingTypes2.get(0));
				clickElement(driver, meetingTypes2.get(0));
				waitUntilVisible(driver, deleteInDropDown);
				clickElement(driver, deleteInDropDown);
				dashboard.isPaceBarInvisible(driver);
			}
		}
	}
	
	/**
	 * @param driver
	 * @return get meeting title on meeting type page
	 */
	public String getMeetingTypeTitleOnMeetingPage(WebDriver driver) {
		waitUntilVisible(driver, meetingCardHeading);
		return findElement(driver, meetingCardHeading).getText();
	}

	/**
	 * @Desc: get meeting type link
	 * @param driver
	 * @return meeting link
	 */
	public String getMeetingTypeLink(WebDriver driver) {
		waitUntilVisible(driver, calendarLink);
		return findElement(driver, calendarLink).getAttribute("href");
	}

	/**
	 * @param driver
	 */
	public void clickMeetingTypeLink(WebDriver driver) {
		waitUntilVisible(driver, calendarLink);
		clickElement(driver, calendarLink);
	}
	
	/**
	 * @param driver
	 * @return boolean visiblity of location
	 */
	public boolean isLocationFieldAvailable(WebDriver driver) {
		return isElementVisible(driver, locationInput, 6);
	}

	// ***Personalize***//

	/**
	 * @Desc : click Calendar Personalize tab
	 */
	public void clickCalendarPersonalize(WebDriver driver) {
		waitUntilVisible(driver, calendarPersonalize);
		clickElement(driver, calendarPersonalize);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @Desc : click Calendar Availablity tab
	 */
	public void clickCalendarAvailablity(WebDriver driver) {
		waitUntilVisible(driver, calAvailablity);
		clickElement(driver, calAvailablity);
		dashboard.isPaceBarInvisible(driver);
	}
		
	/**
	 * @param driver
	 */
	public void verifyPersonalizePage(WebDriver driver, String sname, String link, String time) {
		String name = findElement(driver, personalizeName).getText();
		String country = findElement(driver, personalizeCountry).getText();
		String timeZone = findElement(driver, personalizeTimezone).getText();
		String calLink = findElement(driver, personalizeCalLink).getText();
		assertEquals(sname, name);
		assertNotNull(country);
		assertEquals(timeZone, time);
		assertEquals(link, calLink);
	}
	
	/**
	 * @param driver
	 */
	public void verifyScheduleEventPage(WebDriver driver, String sname, String time) {
		String name = findElement(driver, scheduleEventName).getText();
		String meetingType = findElement(driver, scheduleMeetingType).getText();
		String meetingTime = findElement(driver, scheduleEventTime).getText();
		assertNotNull(meetingTime);
		assertEquals(sname, name);
		assertNotNull(meetingType.contains(time));
		assertTrue(isElementPresent(driver, confirmationHeading, 6));
	}
	
	/**
	 * edit welcome message
	 * @param driver
	 */
	public void editPersonalizeWelcomeMessage(WebDriver driver, String message) {
		waitUntilVisible(driver, wlcmMessageEditButton);
		clickElement(driver, wlcmMessageEditButton);
		
		waitUntilVisible(driver, welcomeMessageInput);
		clearAll(driver, welcomeMessageInput);
		findElement(driver, welcomeMessageInput).sendKeys(message);
		
		waitUntilVisible(driver, welcomeMsgSaveButton);
		clickElement(driver, welcomeMsgSaveButton);
	}

	/**
	 * @Desc : get your calendar link
	 * @param driver
	 * @return string cal link
	 */
	public String getYourCalendarLink(WebDriver driver) {
		return findElement(driver, yourCalLink).getText();
	}
	
	/**
	 * @Desc : click your personal calendar link
	 */
	public void clickYourCalendarLink(WebDriver driver) {
		waitUntilVisible(driver, yourCalLink);
		clickElement(driver, yourCalLink);
	}

	/**
	 * @Desc : assert error message
	 * @param driver
	 */
	public void verifyInvalidPersonalLinkUrlError(WebDriver driver) {
		String expected = "There was an error that occurred trying to reach this page.";
		waitUntilVisible(driver, invalidUrlMessage);
		assertTrue(findElement(driver, invalidUrlMessage).getText().contains(expected));
	}
	
	public void verifyPersonalLinkDisabled(WebDriver driver) { 
		waitUntilVisible(driver, oopsWarning);
		waitUntilVisible(driver, pleaseWarning);
		assertEquals(findElement(driver, oopsWarning).getText(), "Oops! It looks like there is no public calendar currently available.");
		assertEquals(findElement(driver, pleaseWarning).getText(), "Please reach out to this user directly to schedule a meeting.");
		closeTab(driver);
	}

	/**
	 * @Desc : it will copy the link of the meeting type
	 * @param driver
	 */
	public void copyMeetingTypeLink(WebDriver driver) {
		if (isListElementsVisible(driver, meetingType3Dots, 6)) {
			clickElement(driver, getElements(driver, meetingType3Dots).get(0));
			idleWait(1);
			waitUntilVisible(driver, copyLinkInDropDown);
			clickElement(driver, copyLinkInDropDown);
		}
	}
	
	/**
	 * @Desc: click edit button of meeting type
	 * @param driver
	 */
	public void clickEditMeetingTypeCreated(WebDriver driver) {
		if (isListElementsVisible(driver, meetingType3Dots, 6)) {
			List<WebElement> meetingTypes = getElements(driver, meetingType3Dots);
			waitUntilVisible(driver, meetingTypes.get(0));
			clickElement(driver, meetingTypes.get(0));
			waitUntilVisible(driver, editInDropDown);
			clickElement(driver, editInDropDown);
		}
	}

	/**
	 * @Desc : verify the order of meeting types created
	 * @param driver
	 */
	public void orderOfAllMeetingTypes(WebDriver driver) {
		waitUntilVisible(driver, meetingTypes);
		clickElement(driver, meetingTypes);
		waitUntilVisible(driver, meetingTypesDuration);
		List<WebElement> meetings = getElements(driver, meetingTypesDuration);
		assertTrue((meetings.get(0).getText()).contains("15 min"));
		assertTrue((meetings.get(1).getText()).contains("30 min"));
		assertTrue((meetings.get(2).getText()).contains("45 min"));
		assertTrue((meetings.get(3).getText()).contains("60 min"));
	}

	/**
	 * @param driver
	 */
	public void verifyPickWeekMonth(WebDriver driver) {
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		waitUntilVisible(driver, pickWeekButton);
		clickElement(driver, pickWeekButton);
		List<WebElement> Week = getElements(driver, pickAvailableDay);
		assertEquals(Week.size(), 7);
		waitUntilVisible(driver, pickMonthButton);
		clickElement(driver, pickMonthButton);
		waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
		waitUntilVisible(driver, pickAvailableDay);
		List<WebElement> Month = getElements(driver, pickAvailableDay);
		System.out.println(Month.size());
		assertEquals(Month.size(), monthMaxDays);
	}

	/**
	 * @Desc : verify ringdna logo redirect on default page
	 * @param driver
	 */
	public void verifyRingDnaLogo(WebDriver driver, String meeting) {
		idleWait(2);
		waitUntilVisible(driver, ringDnaLogo);
		clickElement(driver, ringDnaLogo);
		assertEquals(findElement(driver, meetingTitle2).getText(), meeting);
	}
	
	/**
	 * @param driver
	 * @return welcome message text
	 */
	public String getWelcomeMessageFromSoftphone(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if(isElementVisible(driver, welcomeMsgOnsoftphone, 6)) {
			return findElement(driver, welcomeMsgOnsoftphone).getText();
			
		}else {
			return null;
		}
	}

	/**
	 * @param driver
	 * @return String : title of meeting
	 */
	public String getMeetingTypeTitleOnCalPage(WebDriver driver) {
		waitUntilVisible(driver, meetingTitle2);
		return findElement(driver, meetingTitle2).getText();
	}

	/**
	 * @Desc: click Back button on personal calendar page
	 * @param driver
	 */
	public void clickPersonalCalBackButton(WebDriver driver) {
		waitUntilVisible(driver, backButton);
		clickElement(driver, backButton);
	}

	/**
	 * @param driver
	 * @param meetingName
	 * @param meetingDuration
	 */
	public void verifyPersonalCalendarLandingPage(WebDriver driver, String meetingName, String meetingDuration) {
		assertEquals(findElement(driver, meetingTitle2).getText(), meetingName);
		assertTrue(findElement(driver, meetingSlot).getText().contains(meetingDuration));
		assertNotNull(findElement(driver, nextAvailableTime).getText());
	}
	
	/**
	 * @param driver
	 * @return next slot time
	 */
	public String getNextSlotTimeOnPersonalCalPage(WebDriver driver) {
		waitUntilVisible(driver, nextAvailableTime);
		return findElement(driver, nextAvailableTime).getText();
	}
	
	/**
	 * @param driver
	 * @return user name on personal cal page
	 */
	public String getPersonalCalPageUserName(WebDriver driver) {
		return findElement(driver, personalCalUserName).getText();
	}

	/**
	 * @Desc: toggle off button of meeting type
	 * @param driver
	 */
	public void clickToggleButtonMeetingType(WebDriver driver, int index) {
		waitUntilVisible(driver, toggleButton);
		List<WebElement> toggle = getElements(driver, toggleButton);
		waitUntilVisible(driver, toggle.get((index-1)));
		clickElement(driver, toggle.get((index-1)));
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * @author Pranshu
	 * @Desc: check there is meeting type available on personal cal page
	 * @param driver
	 * @return boolean
	 */
	public boolean checkNoMeetingTypeAvailableOnPersonalCalendar(WebDriver driver) {
		assertEquals("This user has not activated their public calendar. Please reach out to them directly to schedule a meeting.", findElement(driver, whenNoMeetingAvailable).getText());
		return isElementVisible(driver, whenNoMeetingAvailable, 6);
	}
	
	//Account accounts tab
	
	public void openAccountAccountsTab(WebDriver driver) {
		if (!findElement(driver, licensingTabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, licensingTab);
			clickElement(driver, licensingTab);
			dashboard.isPaceBarInvisible(driver);
			findElement(driver, licensingTabHeading);
		}
	}
	
	/**
	 * @Desc : open meeting on personal Calendar 
	 * @param driver
	 */
	public void openMeetingTypeOnPersonalCalendarPage(WebDriver driver) {
		waitUntilVisible(driver, meetingTitle2);
		List<WebElement> Meetings = getElements(driver, meetingTitle2);
		clickElement(driver, Meetings.get(0));
		idleWait(2);
	}
	
	/**
	 * @Desc : open meeting on personal Calendar 
	 * @param driver
	 */
	public void openMeetingTypeOnCalendarPageByIndex(WebDriver driver, int index) {
		waitUntilVisible(driver, meetingTitle2);
		List<WebElement> Meetings = getElements(driver, meetingTitle2);
		clickElement(driver, Meetings.get((1-index)));
	}
	
	/**
	 * open slot by index
	 * @param driver
	 * @param index
	 */
	public void openSlotTimePageOnPersonalCalPage(WebDriver driver, int index) {
		waitUntilVisible(driver, pickWorkingAvaiableDay);
		List<WebElement> week = getElements(driver, pickWorkingAvaiableDay);
		waitUntilVisible(driver, week.get((index-1)));
		clickElement(driver, week.get((index-1)));
	}
	
	public void verifyMaxFutureBooking(WebDriver driver, int maxFuture) {
		int days = 0;
		waitUntilVisible(driver, meetingTitle2);
		List<WebElement> Meetings = getElements(driver, meetingTitle2);
		waitUntilVisible(driver, Meetings.get(0));
		clickElement(driver, Meetings.get(0));
		waitUntilVisible(driver, pickMonthButton);
		clickElement(driver, pickMonthButton);
		waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
		waitUntilVisible(driver, pickWorkingAvaiableDay);
		List<WebElement> week = getElements(driver, pickWorkingAvaiableDay);
		days = (days + week.size())-1;
		
		while(isElementVisible(driver, bookMeetingNext, 5)){
			waitUntilVisible(driver, bookMeetingNext);
			clickElement(driver, bookMeetingNext);
			idleWait(3);
			waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
			waitUntilVisible(driver, pickWorkingAvaiableDay);
			week = getElements(driver, pickWorkingAvaiableDay);
			days = days + week.size();
		}
		
		assertTrue(HelperFunctions.verifyIntegerInGivenRange(maxFuture-1, maxFuture+1, days));
		assertFalse(isElementVisible(driver, bookMeetingNext, 5));
		closeTab(driver);
	}
	
	public boolean verifyHolidayCalendarBlocksTheDay(WebDriver driver, String start, String end) {
		boolean flag = false;
		String month = HelperFunctions.GetCurrentDateTime("MMM");
		By locator = By.xpath(pickDayByDate.replace("%month%", month).replace("%day%", start));
		By locator2 = By.xpath(pickDayByDate.replace("%month%", month).replace("%day%", end));
		waitUntilVisible(driver, meetingTitle2);
		List<WebElement> Meetings = getElements(driver, meetingTitle2);
		clickElement(driver, Meetings.get(0));
		waitUntilVisible(driver, pickMonthButton);
		clickElement(driver, pickMonthButton);
		waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
		
		if(!isElementVisible(driver, locator, 6) && !isElementVisible(driver, locator2, 6)) {
			flag = true;
		}
		
		By locator3 = By.xpath(datePicker.replace("%Day%", start));
		waitUntilVisible(driver, locator3);
		hoverElement(driver, locator3);
		isElementVisible(driver, blockedDayMessage, 6);
		
		closeTab(driver);
		return flag;
	}
	
	/**
	 * @param driver
	 * verify future days are available
	 */
	public void verifyFutureDaysAreAvailable(WebDriver driver) {
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		waitUntilVisible(driver, pickMonthButton);
		clickElement(driver, pickMonthButton);
		waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
		List<WebElement> available = getElements(driver, pickWorkingAvaiableDay);
		
		int today = Integer.valueOf(HelperFunctions.GetCurrentDateTime("d"));
		monthMaxDays = ((monthMaxDays+1)-today);
		System.out.println(monthMaxDays);
		System.out.println(available.size());
		boolean answer = monthMaxDays == available.size() || (monthMaxDays+1) == available.size() || (monthMaxDays-1) == available.size();
		assertTrue(answer);
	}
	
	/**
	 * @Desc: remove integartions
	 * @param driver
	 */
	public void removeIntegration(WebDriver driver) {
		if (isElementVisible(driver, integrationRemoveButton, 6)) {
			List<WebElement> remove = getElements(driver, integrationRemoveButton);
			for (int i = 0; i < remove.size(); i++) {
				waitUntilVisible(driver, remove.get(i));
				clickElement(driver, remove.get(i));
				waitUntilVisible(driver, confirmRemoveButton);
				clickElement(driver, confirmRemoveButton);
				dashboard.isPaceBarInvisible(driver);
			}
		}
	}
	
	/**
	 * @Desc: remove Gmail integartions
	 * @param driver
	 */
	public void removeGmailIntegration(WebDriver driver) {
		if (isElementVisible(driver, GmailIntRemoveButton, 6)) {
			clickElement(driver, GmailIntRemoveButton);
			waitUntilVisible(driver, confirmRemoveButton);
			clickElement(driver, confirmRemoveButton);
			waitUntilInvisible(driver, GmailIntRemoveButton);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @Desc: remove Exchange integartions
	 * @param driver
	 */
	public void removeExchangeIntegration(WebDriver driver) {
		if (isElementVisible(driver, ExchangeIntRemoveButton, 6)) {
			clickElement(driver, ExchangeIntRemoveButton);
			waitUntilVisible(driver, confirmRemoveButton);
			clickElement(driver, confirmRemoveButton);
			waitUntilInvisible(driver, ZoomIntRemoveButton);
			driver.navigate().refresh();
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 * @return visiblity of error
	 */
	public boolean verifyInvalidExchnageError(WebDriver driver) {
		waitUntilVisible(driver, confirmRemoveButton);
		return isElementVisible(driver, invalidExchnageError, 6);
	}
	
	/**
	 * @Desc: remove Zoom integartions
	 * @param driver
	 */
	public void removeZoomIntegration(WebDriver driver) {
		if (isElementVisible(driver, ZoomIntRemoveButton, 6)) {
			clickElement(driver, ZoomIntRemoveButton);
			waitUntilVisible(driver, confirmRemoveButton);
			clickElement(driver, confirmRemoveButton);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @Desc : click Integration Save button
	 * @param driver
	 */
	public void clickIntegrationSaveButton(WebDriver driver) {
		waitUntilVisible(driver, integrationSaveButton);
		clickElement(driver, integrationSaveButton);
	}
	
	public void selectExchangeAsDefaultAccount(WebDriver driver) {
		waitUntilVisible(driver, integrationDefaultCal);
		clickElement(driver, integrationDefaultCal);
		waitUntilVisible(driver, selectExchangeAccount);
		clickElement(driver, selectExchangeAccount);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @Desc : verify Multiple Account Error Message
	 */
	public void verifyMultipleAccountErrorMessage(WebDriver driver) {
		waitUntilVisible(driver, multipleAccountError);
		String text = getElementsText(driver, multipleAccountError);
		assertTrue(text.contains("Unable to save user. We have detected that these login credentials are already in use. Please select alternate login credentials and try again. Please reach out to your ringDNA administrator."));
	}
	
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isGmailIntegrationConnected(WebDriver driver) {
			waitUntilVisible(driver, gmailStatus);
			return findElement(driver, gmailStatus).getText().equals("RUNNING");
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isGmailIntegrationVisible(WebDriver driver) {
			return isElementVisible(driver, gmailStatus, 6);
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isExchangeIntegrationVisible(WebDriver driver) {
			return isElementVisible(driver, ExchangeStatus, 6);
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isExchangeIntegrationConnected(WebDriver driver) {
		waitUntilVisible(driver, ExchangeStatus);
		return findElement(driver, ExchangeStatus).getText().equals("RUNNING");
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isGamilRequestConnectionVisible(WebDriver driver) {
		boolean flag = false;
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);
		flag = isElementVisible(driver, gmailRequestButton, 6);
		waitUntilVisible(driver, closeAddIntegrationWindow);
		clickElement(driver, closeAddIntegrationWindow);
		return flag;
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isExchangeRequestConnectionVisible(WebDriver driver) {
		boolean flag = false;
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);
		flag = isElementVisible(driver, exchnageRequestButton, 6);
		waitUntilVisible(driver, closeAddIntegrationWindow);
		clickElement(driver, closeAddIntegrationWindow);
		return flag;
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public void clickGamilRequestConnection(WebDriver driver) {
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);
		waitUntilVisible(driver, gmailRequestButton);
		clickElement(driver, gmailRequestButton);
		waitUntilVisible(driver, closeAddIntegrationWindow);
		clickElement(driver, closeAddIntegrationWindow);
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public void clickExchangeRequestConnection(WebDriver driver) {
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);
		waitUntilVisible(driver, exchnageRequestButton);
		clickElement(driver, exchnageRequestButton);
		clickElement(driver, closeAddIntegrationWindow);
	}
	
	/**
	 * @Desc: click Exchange integartion remove button
	 * @param driver
	 */
	public void clickExchangeIntRemoveButton(WebDriver driver) {
		if (isElementVisible(driver, ExchangeIntRemoveButton, 6)) {
			clickElement(driver, ExchangeIntRemoveButton);
		}
	}
	
	/**
	 * @Desc: click Confirm Integration Cancel Button
	 * @param driver
	 */
	public void clickConfirmIntegrationCancelButton(WebDriver driver) {
		waitUntilVisible(driver, confirmCancelButton);
		clickElement(driver, confirmCancelButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * add exchnage int
	 * @param driver
	 * @param email
	 * @param password
	 */
	public void addExchangeIntegration(WebDriver driver, String email, String password) {
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);

		if (isElementVisible(driver, exchnageConnectButton, 5)) {
			clickElement(driver, exchnageConnectButton);
			dashboard.isPaceBarInvisible(driver);

			waitUntilVisible(driver, exchangeEmailInput);
			enterText(driver, exchangeEmailInput, email);

			waitUntilVisible(driver, exchangeSigninButton);
			clickElement(driver, exchangeSigninButton);
			
			waitUntilVisible(driver, exchangeLogo);
			clickElement(driver, exchangeLogo);
			
			waitUntilVisible(driver, exchangePasswdInput);
			enterText(driver, exchangePasswdInput, password);
			
			waitUntilVisible(driver, exchangeSigninButton);
			clickElement(driver, exchangeSigninButton);

			// assert
			dashboard.isPaceBarInvisible(driver);
			assertTrue(isExchangeIntegrationConnected(driver));
		} else {
			waitUntilVisible(driver, closeAddIntegrationWindow);
			waitUntilClickable(driver, closeAddIntegrationWindow);
			clickElement(driver, closeAddIntegrationWindow);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * add gmail int
	 * @param driver
	 * @param email
	 * @param password
	 */
	public void addGmailIntegration(WebDriver driver, String email, String password) {
		By locator = By.cssSelector(gmailLoginAccount.replace("%text%", email));
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);

		if (isElementVisible(driver, gmailConnectButton, 6)) {
			clickElement(driver, gmailConnectButton);
			
			if(isElementVisible(driver, locator, 6)) {
				waitUntilVisible(driver, locator);
				clickElement(driver, locator);

				waitUntilVisible(driver, gmailAllowButton);
				clickElement(driver, gmailAllowButton);
				return;
			}

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

			waitUntilVisible(driver, gmailAllowButton);
			clickElement(driver, gmailAllowButton);
			dashboard.isPaceBarInvisible(driver);
		} else {
			waitUntilVisible(driver, closeAddIntegrationWindow);
			waitUntilClickable(driver, closeAddIntegrationWindow);
			clickElement(driver, closeAddIntegrationWindow);
			dashboard.isPaceBarInvisible(driver);
		}

		waitUntilVisible(driver, addIntegrationButton);
		if(isElementVisible(driver, multipleAccountError, 6)) {
			verifyMultipleAccountErrorMessage(driver);
			return;
		}
		
		while (!isGmailIntegrationConnected(driver)) {
			
			removeGmailIntegration(driver);

			waitUntilVisible(driver, addIntegrationButton);
			clickElement(driver, addIntegrationButton);

			waitUntilVisible(driver, gmailConnectButton);
			clickElement(driver, gmailConnectButton);

			waitUntilVisible(driver, locator);
			clickElement(driver, locator);

			waitUntilVisible(driver, gmailAllowButton);
			clickElement(driver, gmailAllowButton);
		}

	}

	/**
	 * add zoom int
	 * @param driver
	 * @param email
	 * @param password
	 */
	public void addZoomIntegration(WebDriver driver, String email, String password) {
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);

		if (isElementVisible(driver, zoomConnectButton, 5)) {
			clickElement(driver, zoomConnectButton);

			waitUntilVisible(driver, exchangeLogo);
			clickElement(driver, exchangeLogo);

			waitUntilVisible(driver, exchangeEmailInput);
			enterText(driver, exchangeEmailInput, email);

			waitUntilVisible(driver, exchangePasswdInput);
			enterText(driver, exchangePasswdInput, password);

			waitUntilVisible(driver, exchangeSigninButton);
			clickElement(driver, exchangeSigninButton);
			// assert
			assertTrue(isExchangeIntegrationConnected(driver));
		}
	}
	
	public void openMeetingEventPage(WebDriver driver) {
		waitUntilVisible(driver, meetingTitle2);
		List<WebElement> Meetings = getElements(driver, meetingTitle2);
		clickElement(driver, Meetings.get(0));
		waitUntilVisible(driver, pickMonthButton);
		clickElement(driver, pickMonthButton);
		waitUntillNumberOfElementsAreMore(driver, pickAvailableDay, 27);
		waitUntilVisible(driver, pickWorkingAvaiableDay);
		List<WebElement> week = getElements(driver, pickWorkingAvaiableDay);
		clickElement(driver, week.get(0));
		waitUntilVisible(driver, pickTimeSlots);
		List<WebElement> eventPickTime = getElements(driver, pickTimeSlots);
		waitUntilVisible(driver, eventPickTime.get(0));
		clickElement(driver, eventPickTime.get(0));
	}
	
	/**
	 * @param driver
	 * @param index
	 * select pick time on creating event by index
	 */
	public void clickPickTimeByIndexOnCalPage(WebDriver driver, int index) {
		waitUntilVisible(driver, pickTimeSlots);
		List<WebElement> eventPickTime = getElements(driver, pickTimeSlots);
		waitUntilVisible(driver, eventPickTime.get((index-1)));
		clickElement(driver, eventPickTime.get((index-1)));
	}
	
	/**
	 * @param driver
	 * @param index
	 * get slots time by index number on cal page
	 */
	public String getSlotTimeOnCalPageByIndex(WebDriver driver, int index) {
		idleWait(2);
		waitUntilVisible(driver, pickTimeText);
		List<WebElement> eventPickTime = getElements(driver, pickTimeText);
		return eventPickTime.get((index-1)).getText();
	}
	
	/**
	 * @param driver
	 * @return slots number
	 */
	public float getSlotAvailbleForDayInNumber(WebDriver driver, String text) {
		idleWait(1);
		By locator = By.xpath(slotAvailForDay.replace("%day%", text));
		if(isElementVisible(driver, locator, 6)) {
			String slotsNumber = findElement(driver, locator).getAttribute("style");
			slotsNumber = slotsNumber.replace("stroke-dashoffset: ", "").replace(";", "");
			Float number = Float.valueOf(slotsNumber);
			return number;		
		}else {
			return 0;
		}
	}
	
	/**
	 * @param driver
	 * @param name
	 * @param email
	 * @param number
	 * @param company
	 * @param desciption
	 * schedule Event Summary
	 */
	public void scheduleEventSummary(WebDriver driver, String name, String email, String number, String company, String desciption) {
		waitUntilVisible(driver, fullName);
		enterText(driver, fullName, name);
		
		waitUntilVisible(driver, emailId);
		enterText(driver, emailId, email);
		
		waitUntilVisible(driver, contactNumber);
		enterText(driver, contactNumber, number);
		
		waitUntilVisible(driver, companyName);
		enterText(driver, companyName, company);
		
		waitUntilVisible(driver, description);
		enterText(driver, description, desciption);	
	}
	
	/**
	 * click Event Schedule Button
	 * @param driver
	 */
	public void clickEventScheduleButton(WebDriver driver) {
		waitUntilVisible(driver, scheduleButton);
		clickElement(driver, scheduleButton);
		if(!isElementVisible(driver, emailIdError, 5))
		waitUntilInvisible(driver, scheduleButton);
	}
	
	/**
	 * @param driver
	 * @return event time on form
	 */
	public String getEventTimeFromScheduleFormPage(WebDriver driver) {
		waitUntilVisible(driver, scheduleFormEventTime);
		return getElementsText(driver, scheduleFormEventTime);
	}
	
	/**
	 * verify start time and end time
	 * @param driver
	 */
	public void verifyStartAndEndTimeOnPersonalCalPage(WebDriver driver, String start, String end) {
		waitUntilVisible(driver, pickTimeText);
		List<String> eventPickTime = getTextListFromElements(driver, pickTimeText);
		assertEquals(start, eventPickTime.get(0));
		assertEquals(end, eventPickTime.get((eventPickTime.size()-1)));
	}
	
	/**
	 * @param driver
	 * @return get time available for a day 
	 */
	public List<WebElement> getAllSlotsAvialableForDay(WebDriver driver) {
		idleWait(3);
		return getElements(driver, pickTimeText);
	}
	
	/**
	 * @param driver
	 * is error message visible
	 */
	public void isEventSummaryFieldErrorsVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, emailIdError, 5));
		assertTrue(isElementVisible(driver, contactNoError, 5));
		assertTrue(isElementVisible(driver, descriptionError, 5));
	}
	
	/**
	 * @param driver
	 * @return length of description box
	 */
	public String getMaxLengthOfEventSummaryDescriptionBox(WebDriver driver){
		waitUntilVisible(driver, description);
		String length = getAttribue(driver, description, ElementAttributes.maxlength);
		return length;
	}
	
	/**
	 * @param driver
	 * verify place holder values of event summary page
	 */
	public void verifyPlaceHolderValueOfEventSummaryFields(WebDriver driver) {
		List<String> placeholderEdit = new ArrayList<String>();
		waitUntilVisible(driver, fieldPlaceHolderValues);
		List<String> placeholder = getTextListFromElements(driver, fieldPlaceHolderValues);
		for(String value : placeholder) {
			value = value.substring(0, (value.length()-2));
			placeholderEdit.add(value);
		}
		assertTrue(placeholderEdit.contains("Full Name"));
		assertTrue(placeholderEdit.contains("Email Address"));
		assertTrue(placeholderEdit.contains("Company"));
		assertTrue(placeholderEdit.contains("Mobile Phone Number"));
		assertTrue(placeholderEdit.contains("Please share any considerations or key topics to discuss in our meeting."));
	}
	
	/**
	 * @param driver
	 * @return visiblity of schedule button 
	 */
	public boolean isScheduleButtonVisible(WebDriver driver) {
		return isElementVisible(driver, scheduleButton, 5);
	}
	
	public boolean isSalesAcademyBlogButtonVisible(WebDriver driver) {
		return isElementVisible(driver, salesAcademyButton, 6);
	}
	
	public void clickSalesAcademyBlogButton(WebDriver driver) {
		waitUntilVisible(driver, salesAcademyButton);
		clickElement(driver, salesAcademyButton);
	}
	
	/**
	 * @param driver
	 * wait for logo
	 */
	public void waitUntilSalesLogoVisibleOnPage(WebDriver driver) {
		waitUntilVisible(driver, salesAcademyLogo);
	}
	
	public String getSalesAcademyPageLink(WebDriver driver) {
		waitUntilVisible(driver, salesAcademyPageLink);
		return getAttribue(driver, salesAcademyPageLink, ElementAttributes.href);
	}
	
	/**
	 * open user profile drop down
	 * @param driver
	 */
	public void clickUserProfileDropDown(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, userProfileDropDown);
		clickElement(driver, userProfileDropDown);
		dashboard.isPaceBarInvisible(driver);
	}

	/**
	 * open calendar from user profile drop down
	 * @param driver
	 */
	public void openCalendarThroughUserProfileDropDown(WebDriver driver) {
		waitUntilVisible(driver, userProfileCalendar);
		clickElement(driver, userProfileCalendar);
	}
	
	/**
	 * @param driver
	 * @return is cal visible in user profile drop down
	 */
	public boolean isUserProfileCalendarVisibleInDropDown(WebDriver driver) {
		return isElementVisible(driver, userProfileCalendar, 5);
	}
	
	/**
	 * @param driver
	 * @return user name
	 */
	public String getUserName(WebDriver driver) {
		waitUntilVisible(driver, username);
		return findElement(driver, username).getText();
	}
	
	/**
	 * @param driver
	 * @return time zone
	 */
	public String getTimeZone(WebDriver driver) {
		waitUntilVisible(driver, timeZone);
		return findElement(driver, timeZone).getText();
	}
	
	/**
     * Method is used to get the TOTP based on the security token
     * @return code 
     */
    public static String getTwoFactorCode(){
        Totp totp = new Totp("ottq 2rjc pz2b 7z2t ietl 662c qk3q 3vox"); // 2FA secret key
        String twoFactorCode = totp.now(); //Generated 2FA code here
        return twoFactorCode;
    }
    
    /**
     * @param driver
     * @return zoom access error
     */
    public String getAcessErrorOnZoomPage(WebDriver driver) {
    	waitUntilVisible(driver, userProfileCalendar);
    	return getElementsText(driver, accessErrorOnZoom);
    }
    
    /**
     * @param driver
     * @return number of unavailable days
     */
    public int getUnavailableBlockedDays(WebDriver driver){
    	if(isElementVisible(driver, unavailableBlockDays, 5)) {
    		return getElements(driver, unavailableBlockDays).size();
    	}else {
    		return 0;
    	}
    }
    
    /**
     * @param driver
     * @color of unavailable days
     */
    public void verifyColorOfUnavailableBlockedDays(WebDriver driver){
    	if(isElementVisible(driver, circleOfBlockedDay, 5)) {
    		List<WebElement> color = getElements(driver, circleOfBlockedDay);
    		String unavailableDayColor = getAttribue(driver, color.get(0), ElementAttributes.stroke);
    		assertTrue(unavailableDayColor.equals("#CCCCCC"));   //dark grey
    	}
    }
    
    /**
     * @param driver
     * @color of unavailable days
     */
    public void verifyColorOfAvailableDays(WebDriver driver){
    	if(isElementVisible(driver, circleOfWorkingDay, 5)) {
    		List<WebElement> color = getElements(driver, circleOfWorkingDay);
    		String availableDayColor = getAttribue(driver, color.get(0), ElementAttributes.stroke);
    		assertTrue(availableDayColor.equals("#0066FF")); //blue
    	}
    }
    
    /**
     * @param driver
     * am pm text on slot page
     */
    public void verifyAmPmTextOnSlotsPage(WebDriver driver) {
    	waitUntilVisible(driver, amPmTextOnSlotPage);
    	List<String> text = getTextListFromElements(driver, amPmTextOnSlotPage);
    	assertTrue(text.get(0).equals("AM"));
    	assertTrue(text.get(1).equals("PM"));
    }
    
    /**
	 * add gmail int
	 * @param driver
	 * @param email
	 * @param password
	 */
	public void addMultipleGmailIntegration(WebDriver driver, String email, String password) {
		By locator = By.cssSelector(gmailLoginAccount.replace("%text%", email));
		waitUntilVisible(driver, addIntegrationButton);
		clickElement(driver, addIntegrationButton);

		if (isElementVisible(driver, gmailConnectButton, 6)) {
			clickElement(driver, gmailConnectButton);
			
			if(isElementVisible(driver, locator, 6)) {
				waitUntilVisible(driver, locator);
				clickElement(driver, locator);

				waitUntilVisible(driver, gmailAllowButton);
				clickElement(driver, gmailAllowButton);
				return;
			}

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

			waitUntilVisible(driver, gmailAllowButton);
			clickElement(driver, gmailAllowButton);
			dashboard.isPaceBarInvisible(driver);
			//verify
			verifyMultipleAccountErrorMessage(driver);
		}
	}
	
	/**
	 * @param driver
	 * close add integration window
	 */
	public void closeAddIntegrationWindow(WebDriver driver) {
		waitUntilVisible(driver, closeAddIntegrationWindow);
		clickElement(driver, closeAddIntegrationWindow);
	}
 
}