package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class AccountOverviewTab extends SeleniumBase{

	Dashboard dashboard = new Dashboard();
	
	By overViewTab 			= By.cssSelector(".account-tabs [data-tab='overview']");
	By overViewTabParent 	= By.xpath(".//*[@data-tab='overview']/ancestor::li");
	By overviewTabHeading 	= By.xpath("//*[@id='main']//h2[text()='Account Details']");
	By accountNameInputTab 	= By.xpath("//label[text()='Account Name']/..//input");
	By accountIDTab 		= By.xpath("//label[text()='Account ID']/..//input");
	By accountAliasTab		= By.xpath("//label[text()='Account Alias']/..//input");
	By typeSelectList 		= By.xpath("//label[text()='Type']/..//select/option");
	By deleteAccount		= By.cssSelector(".account-overview .delete");
	By unDeleteAccount		= By.cssSelector(".account-overview .undelete");
	By maxHoldTimeTextBox	= By.cssSelector("input.maxHoldTime");
	
	//SID locator section
	By addSmartNumberBtn 	= By.cssSelector(".import-number");
	By sidInputTab 			= By.cssSelector(".form-control.sid");
	By importSIDBtn 		= By.cssSelector(".btn-success.import");
	By openSmartNumberBtn 	= By.cssSelector(".open");
	By modalMessage 		= By.cssSelector(".modal-message");
	By modalTitle			= By.cssSelector(".modal-title");
	
	By showCallTrackingToolTip			= By.xpath(".//label[text()[normalize-space()='Show Call Tracking']]//parent::label//following-sibling::span");
	By showCallTrackingTextBox          = By.cssSelector("input.callTrackingEnabled");
	By showCallTrackingToggleOffButton  = By.xpath("//input[contains(@class,'callTrackingEnabled')]/..//label[contains(@class,'toggle-off')]");
	By showCallTrackingToggleOnButton   = By.xpath("//input[contains(@class,'callTrackingEnabled')]/..//label[contains(@class,'toggle-on')]");

	By conversationAnalyticsCheckbox	      = By.cssSelector("input.conversationAnalytics");
	By conversationAnalyticsToggleOnButton    = By.xpath("//input[contains(@class,'conversationAnalytics')]/..//label[contains(@class,'toggle-on')]");
	By conversationAnalyticsToggleOffButton   = By.xpath("//input[contains(@class,'conversationAnalytics')]/..//label[contains(@class,'toggle-off')]");
	
	By locationRequiredNewUsersCheckBox 	  = By.cssSelector("input.locationRequired");
	By locationRequiredNewUsersToggleOnButton = By.xpath("//input[contains(@class,'locationRequired')]/..//label[contains(@class,'toggle-on')]");
	
	By saveAccountsSettingButton  = By.cssSelector("button.save");
	By saveAccountsSettingMessage = By.className("toast-message");
	
	//tool tip
	By manageCustomerDataToolTip        = By.xpath(".//label[text()[normalize-space()='Manage Customer Data']]//parent::label//following-sibling::span");
	By disableEmailTrackingToolTip      = By.xpath(".//label[text()[normalize-space()='Disable Email Tracking']]//parent::label//following-sibling::span");
	By locationRequiredToolTip           = By.xpath(".//label[text()[normalize-space()='Location Required for New Users']]//parent::label//following-sibling::span");
	
	// -------- Location ----------- //
	By locationAddIcon 				= By.cssSelector(".locations .glyphicon-plus-sign");
	By locationNameInput 			= By.cssSelector(".location-editor .location");
	By locationDescriptionInput 	= By.cssSelector(".location-editor .description");
	By locationAddressInput 		= By.cssSelector(".location-editor .address");
	By saveLocationBtn 				= By.cssSelector(".location-editor .btn-success");
	By allLocationList              = By.cssSelector(".locations .string-cell.sortable.renderable.name");
	
	// -------- Holiday Schedule ----------- //
	By holidayScheduleAddIcon 		= By.cssSelector(".holiday-schedule .glyphicon-plus-sign");
	By holidayScheduleNameTab 		= By.cssSelector(".holiday-schedule-editor .name");
	By holidayScheduleDescriptionTab = By.cssSelector(".holiday-schedule-editor .description");
	By nextBtn 						= By.cssSelector(".btn.btn-success.next");
	By addHolidayBtn 				= By.cssSelector(".holiday-schedule-editor .add-holiday");
	By holidayEventTab 				= By.cssSelector(".holiday-schedule-editor .event-name");
	By saveHolidayBtn 				= By.cssSelector(".btn-success.save-holiday");
	By closeBtnWindow 				= By.cssSelector("button.close");
	By eventStartDate 				= By.cssSelector(".holiday input.from-date");
	By eventEndDate 				= By.cssSelector(".holiday input.to-date");
	By deleteHolidayButtonList		= By.cssSelector(".holiday-schedule .glyphicon-remove-sign");
	String holidayEventName 		= ".//*[text()=\"$$EventName$$\"]/ancestor::tr/td[contains(@class,'name')]";
	String holidayEventStartDate	= ".//*[text()=\"$$EventName$$\"]/ancestor::tr/td[contains(@class,'fromDate')]";
	
	String updateRecordBtn 			= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[@class='btn btn-primary update-btn']";
	String deleteRecordBtn 			= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//a[@class='delete']";
	By confirmDeleteBtn 			= By.cssSelector("[data-bb-handler='confirm']");
	By holidayList                  = By.xpath("//div[@class ='holiday-schedule']//td[@class='string-cell sortable renderable name']");
	
	// holiday banner
	By holidayBanner                = By.xpath("//div[contains(@class, 'alert alert-warning')]");
	By holidayNameOnBanner     		= By.xpath("//div[contains(@class, 'alert alert-warning')]/strong");
	
	static final String	tooltipShowCallTracking    	      	  = "When toggled ON, \"Call Tracking\" functionality will be available in the navigation bar.";
	
	public void verifyUserOnAccountOverViewPage(WebDriver driver) {
		waitUntilVisible(driver, overViewTab);
		waitUntilVisible(driver, overviewTabHeading);
	}
	
	public void openOverViewTab(WebDriver driver) {
		if (!findElement(driver, overViewTabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, overViewTab);
			clickElement(driver, overViewTab);
			waitUntilInvisible(driver, dashboard.paceBar);
			waitUntilVisible(driver, overviewTabHeading);
		}
	}
	
	public void enterAccountAlias(WebDriver driver, String aliasName){
		waitUntilVisible(driver, accountAliasTab);
		enterText(driver, accountAliasTab, aliasName);
	}
	
	public String getAccountAliasName(WebDriver driver) {
		waitUntilVisible(driver, accountAliasTab);
		return getAttribue(driver, accountAliasTab, ElementAttributes.value);
	}
	
	public void verifyOverViewTabDetails(WebDriver driver, String accountName, String accountID) {
		assertEquals(getAttribue(driver, accountNameInputTab, ElementAttributes.value), accountName);
		assertEquals(getAttribue(driver, accountIDTab, ElementAttributes.value), accountID);
		
		List<WebElement> typeList = getElements(driver, typeSelectList);
		assertTrue(isListContainsText(driver, typeList, "Trialist"));
		assertTrue(isListContainsText(driver, typeList, "Internal"));
		assertTrue(isListContainsText(driver, typeList, "Customer Attrited"));
		assertTrue(isListContainsText(driver, typeList, "Customer"));
		assertTrue(isListContainsText(driver, typeList, "Pilot"));
	}
	
	public boolean isUndeleteAccountVisible(WebDriver driver){
		return isElementVisible(driver, unDeleteAccount, 5);
	}
	
	public void deleteAccount(WebDriver driver){
		waitUntilVisible(driver, deleteAccount);
		clickElement(driver, deleteAccount);
		dashboard.isPaceBarInvisible(driver);
		waitUntilInvisible(driver, deleteAccount);
		waitUntilVisible(driver, unDeleteAccount);
	}
	
	public void unDeleteAccount(WebDriver driver){
		waitUntilVisible(driver, unDeleteAccount);
		clickElement(driver, unDeleteAccount);
		dashboard.isPaceBarInvisible(driver);
		waitUntilInvisible(driver, unDeleteAccount);
		waitUntilVisible(driver, deleteAccount);
	}
	
	public void setMaxHoldTime(WebDriver driver, String holdTime) {
		waitUntilVisible(driver, maxHoldTimeTextBox);
		enterText(driver, maxHoldTimeTextBox, holdTime);
	}
	
	public boolean isOverviewTabHeadingPresent(WebDriver driver){
		waitUntilInvisible(driver, dashboard.paceBar);
		return findElement(driver, overviewTabHeading).isDisplayed();
	}
	
	
	
	public void enableShowCallTrackingSetting(WebDriver driver){
		if(!findElement(driver, showCallTrackingTextBox).isSelected()) {
			System.out.println("Checking show call tracking checkbox");
			clickElement(driver, showCallTrackingToggleOffButton);
		}else {
			System.out.println("Show Call Tracking is already enabled");
		} 
	}
	
	public void verifyToolTipShowCallTracking(WebDriver driver){
		scrollToElement(driver, showCallTrackingToolTip);
		hoverElement(driver, showCallTrackingToolTip);
		assertEquals(getAttribue(driver, showCallTrackingToolTip, ElementAttributes.dataOriginalTitle).replace("“", "\"").replace("”", "\""), tooltipShowCallTracking);
	}
	
	public void disableShowCallTrackingSetting(WebDriver driver){
		if(findElement(driver, showCallTrackingTextBox).isSelected()) {
			System.out.println("Checking show call tracking checkbox");
			clickElement(driver, showCallTrackingToggleOnButton);
		}else {
			System.out.println("Show Call Tracking is already disabled");
		} 
	}
	
	public void disableConversationAnalysticsSetting(WebDriver driver){
		if(findElement(driver, conversationAnalyticsCheckbox).isSelected()) {
			System.out.println("disabling Conversation Analytics checkbox");
			clickElement(driver, conversationAnalyticsToggleOnButton);			
		}else {
			System.out.println("Conversation Analytics setting is already disable");
		}
	}
	
	public void enableConversationAnalysticsSetting(WebDriver driver){
		if(!findElement(driver, conversationAnalyticsCheckbox).isSelected()) {
			System.out.println("enabling Conversation Analytics checkbox");
			clickElement(driver, conversationAnalyticsToggleOffButton);			
		}else {
			System.out.println("Conversation Analytics setting is already enabled");
		}
	}
	
	public void disableLocationRequiredNewUserSetting(WebDriver driver){
		if(findElement(driver, locationRequiredNewUsersCheckBox).isSelected()) {
			System.out.println("UnChecking location required for new user checkbox");
			clickElement(driver, locationRequiredNewUsersToggleOnButton);			
		}else {
			System.out.println("location required for new user setting is already disable");
		} 
	}
	
	

	
	/*******Location section starts here******/
	public void updateLocationRecords(WebDriver driver, String oldLocationName, String newLocationName, String newLocationDescr, String newLocationAddress) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldLocationName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterlocationName(driver, newLocationName);
		enterlocationDescription(driver, newLocationDescr);
		enterlocationAddress(driver, newLocationAddress);
		clickSaveLocationBtn(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettings(driver);
	}

	public void deleteRecords(WebDriver driver, String recordName){
		By deleteRecordLoc = By.xpath(deleteRecordBtn.replace("$$RecordName$$", recordName));
		if(isElementVisible(driver, deleteRecordLoc, 5)){;
			clickElement(driver, deleteRecordLoc);
			waitUntilVisible(driver, confirmDeleteBtn);
			clickElement(driver, confirmDeleteBtn);
			waitUntilInvisible(driver, confirmDeleteBtn);	
		}
	}

	public void clickSaveLocationBtn(WebDriver driver) {
		waitUntilVisible(driver, saveLocationBtn);
		clickElement(driver, saveLocationBtn);
	}
	
	public void createLocation(WebDriver driver, String locationName, String locationDescription, String locationAddress) {
		scrollToElement(driver, locationAddIcon);
		clickAddLocationLinkButton(driver);
		enterlocationName(driver, locationName);
		enterlocationDescription(driver, locationDescription);
		enterlocationAddress(driver, locationAddress);
		clickSaveLocationBtn(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}	
	
	public void enterlocationName(WebDriver driver, String locationName) {
		waitUntilVisible(driver, locationNameInput);
		enterText(driver, locationNameInput, locationName);
	}

	public void enterlocationDescription(WebDriver driver, String locationDescription) {
		waitUntilVisible(driver, locationDescriptionInput);
		enterText(driver, locationDescriptionInput, locationDescription);
	}

	public boolean checkLocationSaved(WebDriver driver, String locationName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", locationName));
		return isElementVisible(driver, updateRecordLoc, 10);
	}
	
	public List<String> getAllLocationList(WebDriver driver) {
		return getTextListFromElements(driver, allLocationList);
	}

	public void enterlocationAddress(WebDriver driver, String locationAddress) {
		waitUntilVisible(driver, locationAddressInput);
		enterText(driver, locationAddressInput, locationAddress);
	}
	
	public void clickAddLocationLinkButton(WebDriver driver) {
		waitUntilVisible(driver, locationAddIcon);
		clickElement(driver, locationAddIcon);
	}

	/*******Location section ends here******/
	
	/*******Holiday Schedule section starts here******/
	
	public void createHolidaySchedule(WebDriver driver, String holidayName, String holidayDescription, String holidayEvent, String holidayEvent2) {
		scrollToElement(driver, holidayScheduleAddIcon);
		clickAddHolidayScheduleButton(driver);
		enterHolidayScheduleName(driver, holidayName);
		enterHolidayScheduleDescription(driver, holidayDescription);
		clickNextBtn(driver);
		clickAddHolidayBtn(driver);
		enterHolidayEventName(driver, holidayEvent);
		clickSaveHolidayBtn(driver);
		clickAddHolidayBtn(driver);
		enterHolidayEventName(driver, holidayEvent2);
		clickSaveHolidayBtn(driver);
		By holidayEventNameLoc = By.xpath(holidayEventName.replace("$$EventName$$", holidayEvent2));
		assertTrue(isElementVisible(driver, holidayEventNameLoc, 5));
		deleteRecords(driver, holidayEvent2);
		assertFalse(isElementVisible(driver, holidayEventNameLoc, 5));
		closeWindow(driver);
	}
	
	
	/**
	 * @param startDate (keep format as MM/dd/yyyy)
	 * @param endDate (keep format as MM/dd/yyyy)
	 */
	public void createHolidaySchedule(WebDriver driver, String name, String description, String event, String startDate, String endDate) {
		scrollToElement(driver, holidayScheduleAddIcon);
		clickAddHolidayScheduleButton(driver);
		enterHolidayScheduleName(driver, name);
		enterHolidayScheduleDescription(driver, description);
		clickNextBtn(driver);
		clickAddHolidayBtn(driver);
		enterHolidayEventName(driver, event);
		clickElement(driver, eventStartDate);
		selectDateFromDatePicker(driver, startDate);
		clickElement(driver, eventEndDate);
		selectDateFromDatePicker(driver, endDate);
		clickSaveHolidayBtn(driver);
		By holidayEventNameLoc = By.xpath(holidayEventName.replace("$$EventName$$", event));
		assertTrue(isElementVisible(driver, holidayEventNameLoc, 5));
		closeWindow(driver);
	}
	
	public void clickAddHolidayBtn(WebDriver driver) {
		waitUntilVisible(driver, addHolidayBtn);
		clickElement(driver, addHolidayBtn);
	}

	public void enterHolidayEventName(WebDriver driver, String holidayEvent) {
		waitUntilVisible(driver, holidayEventTab);
		enterText(driver, holidayEventTab, holidayEvent);
	}
	
	public void clickSaveHolidayBtn(WebDriver driver) {
		waitUntilVisible(driver, saveHolidayBtn);
		clickElement(driver, saveHolidayBtn);
	}
		
	public void clickNextBtn(WebDriver driver) {
		waitUntilVisible(driver, nextBtn);
		clickElement(driver, nextBtn);
	}

	public void enterHolidayScheduleName(WebDriver driver, String holidayName) {
		waitUntilVisible(driver, holidayScheduleNameTab);
		enterText(driver, holidayScheduleNameTab, holidayName);
	}
	
	public void enterHolidayScheduleDescription(WebDriver driver, String holidayDescription) {
		waitUntilVisible(driver, holidayScheduleDescriptionTab);
		enterText(driver, holidayScheduleDescriptionTab, holidayDescription);
	}

	public void clickAddHolidayScheduleButton(WebDriver driver) {
		waitUntilVisible(driver, holidayScheduleAddIcon);
		clickElement(driver, holidayScheduleAddIcon);
	}

	public void closeWindow(WebDriver driver){
		waitUntilVisible(driver, closeBtnWindow);
		idleWait(2);
		clickElement(driver, closeBtnWindow);
		waitUntilInvisible(driver, closeBtnWindow);
	}
	
	public boolean checkHolidayScheduleSaved(WebDriver driver, String holidayName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", holidayName));
		return isElementVisible(driver, updateRecordLoc, 5);
	}

	public void updateHolidayScheduleRecords(WebDriver driver, String oldHolidayName, String newHolidayName, String newHolidayDescr, String newHolidayEvent) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldHolidayName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterHolidayScheduleName(driver, newHolidayName);
		enterHolidayScheduleDescription(driver, newHolidayDescr);
		clickNextBtn(driver);
		clickAddHolidayBtn(driver);
		enterHolidayEventName(driver, newHolidayEvent);
		clickSaveHolidayBtn(driver);
		By holidayEventNameLoc = By.xpath(holidayEventName.replace("$$EventName$$", newHolidayEvent));
		assertTrue(isElementVisible(driver, holidayEventNameLoc, 5));
		closeWindow(driver);
	}

	public void verifyValidationMsgEventHoliday(WebDriver driver, String oldHolidayName, String oldEventName, String newStartDate, String newEndDate, String newHolidayEvent) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldHolidayName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		clickNextBtn(driver);
		
		By updateEventRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldEventName));
		waitUntilVisible(driver, updateEventRecordLoc);
		clickElement(driver, updateEventRecordLoc);
		
		//entering new events and dates
		enterHolidayEventName(driver, newHolidayEvent);
		clickElement(driver, eventStartDate);
		selectDateFromDatePicker(driver, newStartDate);
		String currentTime = HelperFunctions.GetCurrentDateTime("hh:mm a");
		if (currentTime.contains("PM")) {
			currentTime = currentTime.replace("PM", "pm");
		} else {
			currentTime = currentTime.replace("AM", "am");
		}
		clickElement(driver, eventEndDate);
		selectDateFromDatePicker(driver, newEndDate);
		clickSaveHolidayBtn(driver);

		//verifying validation msg 
		assertEquals(getSaveAccountsSettingMessage(driver), "To date cannot be before From date!");
		closeWindow(driver);
	}

	public void updateEventsIntoHolidayScheduleRecords(WebDriver driver, String oldHolidayName, String oldEventName, String oldStartDate, String oldEndDate, String newStartDate, String newEndDate, String newHolidayEvent) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldHolidayName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		clickNextBtn(driver);
		
		By updateEventRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldEventName));
		waitUntilVisible(driver, updateEventRecordLoc);
		clickElement(driver, updateEventRecordLoc);
		
		//verifying old event name and start and end dates
		assertEquals(getAttribue(driver, holidayEventTab, ElementAttributes.value), oldEventName);
		assertEquals(getAttribue(driver, eventStartDate, ElementAttributes.value), oldStartDate);
		assertEquals(getAttribue(driver, eventEndDate, ElementAttributes.value), oldEndDate);
		
		//entering new events and dates
		enterHolidayEventName(driver, newHolidayEvent);
		clickElement(driver, eventStartDate);
		selectDateFromDatePicker(driver, newStartDate);
		clickElement(driver, eventEndDate);
		selectDateFromDatePicker(driver, newEndDate);
		clickSaveHolidayBtn(driver);

		//verifying new event name and date 
		By holidayEventNameLoc = By.xpath(holidayEventName.replace("$$EventName$$", newHolidayEvent));
		assertTrue(isElementVisible(driver, holidayEventNameLoc, 5));
		
		By holidayEventStartDateLoc = By.xpath(holidayEventStartDate.replace("$$EventName$$", newStartDate));
		assertTrue(isElementVisible(driver, holidayEventStartDateLoc, 5));
		closeWindow(driver);
	}
	
	public void AddEventsIntoHolidayScheduleRecords(WebDriver driver, String newHolidayEvent, String oldHolidayName, String newStartDate, String newEndDate) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldHolidayName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		clickNextBtn(driver);
		//entering new events and dates
		clickAddHolidayBtn(driver);
		enterHolidayEventName(driver, newHolidayEvent);
		clickElement(driver, eventStartDate);
		selectDateFromDatePicker(driver, newStartDate);
		clickElement(driver, eventEndDate);
		selectDateFromDatePicker(driver, newEndDate);
		clickSaveHolidayBtn(driver);

		//verifying new event name and date 
		By holidayEventNameLoc = By.xpath(holidayEventName.replace("$$EventName$$", newHolidayEvent));
		assertTrue(isElementVisible(driver, holidayEventNameLoc, 5));
		
		By holidayEventStartDateLoc = By.xpath(holidayEventStartDate.replace("$$EventName$$", newStartDate));
		assertTrue(isElementVisible(driver, holidayEventStartDateLoc, 5));
		closeWindow(driver);
	}

	
	public void deleteEventsIntoHolidayScheduleRecords(WebDriver driver, String oldHolidayName, String oldEventName) {
		By updateRecordLoc = By.xpath(updateRecordBtn.replace("$$RecordName$$", oldHolidayName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		clickNextBtn(driver);
		
		deleteRecords(driver, oldEventName);

		closeWindow(driver);
	}

	public void deleteAllHolidaysIfExists(WebDriver driver) {
		while(isElementVisible(driver, deleteHolidayButtonList, 6)){
			scrollIntoView(driver, deleteHolidayButtonList);
			clickElement(driver, deleteHolidayButtonList);
			waitUntilVisible(driver, confirmDeleteBtn);
			clickElement(driver, confirmDeleteBtn);
			waitUntilInvisible(driver, confirmDeleteBtn);	
		};
	}
	
	
	public List<String> getAllHolidayListAvailable(WebDriver driver) {
		waitUntilVisible(driver, holidayList);
		return getTextListFromElements(driver, holidayList);
	}
	
	/**
	 * @param driver
	 * @param holiday
	 */
	public void verifyHolidayBannerOnHeader(WebDriver driver, String holiday) {
		assertTrue(isElementVisible(driver, holidayBanner, 6));
		assertEquals(getElementsText(driver, holidayNameOnBanner), holiday);
	}
	
	/**
	 * @param driver
	 * @param holiday
	 */
	public void verifyNoHolidayBannerOnHeader(WebDriver driver) {
		assertFalse(isElementVisible(driver, holidayBanner, 6));
		assertFalse(isElementVisible(driver, holidayNameOnBanner, 6));
	}
	
	/*******Holiday Schedule section ends here******/
	
	public String getSaveAccountsSettingMessage(WebDriver driver){
		return getElementsText(driver, saveAccountsSettingMessage);
	}
	
	public void isSaveAccountsSettingMessageDisappeared(WebDriver driver){
		isElementVisible(driver, saveAccountsSettingMessage, 2);
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void saveAcccountSettings(WebDriver driver){
		waitUntilVisible(driver, saveAccountsSettingButton);
		scrollToElement(driver, saveAccountsSettingButton);
		clickElement(driver, saveAccountsSettingButton);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		assertEquals(getSaveAccountsSettingMessage(driver), "Account saved.");
		isSaveAccountsSettingMessageDisappeared(driver);		
	}
	
	public void setDefaultAccountOverviewSettings(WebDriver driver){
		assertTrue(isOverviewTabHeadingPresent(driver));
		disableLocationRequiredNewUserSetting(driver);
		saveAcccountSettings(driver);
	}
	
	/*******SID section starts here******/
	
	public void clickAddSmartNumberBtn(WebDriver driver) {
		waitUntilVisible(driver, addSmartNumberBtn);
		clickElement(driver, addSmartNumberBtn);
	}
	
	public void enterSID(WebDriver driver, String sid) {
		waitUntilVisible(driver, sidInputTab);
		enterText(driver, sidInputTab, sid);
		waitUntilVisible(driver, importSIDBtn);
		clickElement(driver, importSIDBtn);
		waitUntilVisible(driver, openSmartNumberBtn);
		assertEquals(getElementsText(driver, modalMessage), "Smart number created.");
	}
	
	public void clickOpenSmartNumber(WebDriver driver) {
		waitUntilVisible(driver, openSmartNumberBtn);
		clickElement(driver, openSmartNumberBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/*******SID section ends here******/
	
	/**
	 * @param driver
	 * verify all overview tab tool tips
	 */
	public void verifyAllOverviewTabToolTip(WebDriver driver){
		waitUntilVisible(driver, showCallTrackingToolTip);
		scrollToElement(driver, showCallTrackingToolTip);
		hoverElement(driver, showCallTrackingToolTip);
		scrollToElement(driver, manageCustomerDataToolTip);
		hoverElement(driver, manageCustomerDataToolTip);
		scrollToElement(driver, disableEmailTrackingToolTip);
		hoverElement(driver, disableEmailTrackingToolTip);
		scrollToElement(driver, locationRequiredToolTip);
		hoverElement(driver, locationRequiredToolTip);
	}
	
}
