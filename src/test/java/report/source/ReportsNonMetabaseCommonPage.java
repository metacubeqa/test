package report.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import base.SeleniumBase;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftPhoneSettingsPage;
import support.source.commonpages.Dashboard;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class ReportsNonMetabaseCommonPage extends SeleniumBase{

	ReportThisCallPage reportCallPage = new ReportThisCallPage();
	Dashboard dashboard = new Dashboard();
	SoftPhoneSettingsPage softphoneSettingPage =  new SoftPhoneSettingsPage();
	UsersPage userPage = new UsersPage();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	
	By locationBox                = By.xpath("//span[@class='location-picker']//input/ancestor::div[1]");
	By locationInputTab 	   = By.cssSelector(".location-picker input");
	By locationDropDownItems   = By.cssSelector(".location-picker .selectize-dropdown-content div");
	By locationSelectedItemTab = By.cssSelector(".location-picker div.item");

	By toastTitle			   = By.cssSelector(".toast-title");
	By toastMessage			   = By.cssSelector(".toast-message");
	By toastCloseButton		   = By.cssSelector(".toast-close-button");
	
	By accountTextBoxSelectize  = By.cssSelector(".account-picker div.selectize-input.items");
	By accountTextBoxInput      = By.cssSelector(".account-picker div.selectize-input.items input");
	By accountTextBoxOptions    = By.cssSelector(".account-picker .selectize-dropdown-content div[data-selectable] span:not(.picker-value)");
	By accountSelectedItemTab   = By.cssSelector(".account-picker div.item");
	
	By downloadLink			  	= By.cssSelector(".pager-container .download span, button.btn-info.export, button.btn-info.start, .rdna-button.contained");
	By emailInputTab			= By.cssSelector(".form-control.email");
	
	By categorySelectInputTab  		= By.cssSelector("select.category");
	By categoryListElementsDropDown = By.xpath(".//select[contains(@class,'category')]/option[not(@selected)]");
	
	By directionSelectInputTab  = By.cssSelector("select.direction");
	
	By userInputTab 	   	   = By.cssSelector(".user-picker input");
	By userNameDropDownItems   = By.cssSelector(".user-picker .selectize-dropdown-content div");
	By userNameIdDropDownItems   = By.cssSelector(".user-picker .selectize-dropdown-content div .picker-value");
	
	By searchBtn				 = By.cssSelector("button.btn-success.search");
	
	//result table
	By locationResultTable	   = By.xpath("//table//td[contains(@class,'location.name')]");
	By userNameResultTable	   = By.xpath("//table//td/a[contains(@href,'#users')]");
	By accountNameResultTable  = By.xpath("//table//td/a[contains(@href,'#accounts')]");
	By categoryResultTable	   = By.xpath("//table//td[contains(@class,'category')]");
	By directionResultTable	   = By.xpath("//table//td[contains(@class,'type')]");
	By dateCreatedResultTable  = By.xpath("//table//td[contains(@class,'createdAt')]");  
	By callKeyResultTable	   = By.xpath("//table//td/a[contains(@href,'#calls')] | //table//td[contains(@class,'callKey')]");
	By ratingResultTable	   = By.xpath("//table//td[contains(@class,'rating')]");
	By notesResultTable        = By.xpath("//table//td[contains(@class,'notes')]");
	
	//headers
	By accountNameHeader		= By.xpath("//table//th[contains(@class,'account.name')]/a");
	By userNameHeader			= By.xpath("//table//th[contains(@class,'user.displayName')]/a");
	By categoryHeader			= By.xpath("//table//th[contains(@class,'category')]/a");
	By directionHeader			= By.xpath("//table//th[contains(@class,'type')]/a");
	By locationHeader			= By.xpath("//table//th[contains(@class,'location.name')]/a");
	By dateCreatedHeader		= By.xpath("//table//th[contains(@class,'createdAt')]/a");
	By callKeyHeader            = By.xpath("//table//th[contains(@class,'callKey')]/a");
	By ratingHeader			    = By.xpath("//table//th[contains(@class,'rating')]/a");
	
	//user counts
	By datePickerInputTab		   = By.cssSelector("input.datePicker");
	By startDateInputTab		   = By.cssSelector("input#startDate, input.start-date, input.form-control.start-period");
	By endDateInputTab			   = By.cssSelector("input#endDate, input.end-date, input.form-control.datePicker,input.form-control.end-period");
	
	//billing international
	By dateInput                   = By.xpath("//h2[contains(text(),'Billing Export')]/following::div[9]");
	By  inValidEmailMsg            = By.xpath("//p[contains(text(),'This field is required.')]");
	String yearInput               = "//div[contains(text(),'%year%')]";
	String monthtInput             = "//div[contains(text(),'%month%')]";   
	
	//sla reports
	By selectPeriodSLA			   = By.cssSelector(".MuiSelect-selectMenu.MuiOutlinedInput-input");
	By selectPeriodSlaDropDown     = By.cssSelector(".MuiList-root.MuiList-padding");
	By availablityCheckBox         = By.xpath("//input[@type='checkbox']/parent::span");
	By availabilityOnlyCheckBox	   = By.xpath("//span[@class = 'MuiIconButton-label']//input[@type='checkbox']");
	By daily                       = By.cssSelector(".MuiList-root.MuiList-padding li:nth-of-type(1)");
	By weekly                      = By.cssSelector(".MuiList-root.MuiList-padding li:nth-of-type(2)");
	By monthly                     = By.cssSelector(".MuiList-root.MuiList-padding li:nth-of-type(3)");
		
	By startDateSLAInputTab		   = By.xpath("//div[contains(@class, 'MuiGrid-align-items-xs-center')]/div[2]//input");
	By endDateSLAInputTab		   = By.xpath("//div[contains(@class, 'MuiGrid-align-items-xs-center')]/div[3]//input");
	
	By datePickerHighlightedDays   = By.xpath(".//*[@class='datepicker-days']//tbody//td[@class='highlighted day']");
	By datePickerOldHighlightedDay = By.xpath(".//*[@class='datepicker-days']//tbody//td[@class='old highlighted day']");
	By datePickerNewHighlightedDay = By.xpath(".//*[@class='datepicker-days']//tbody//td[@class='new highlighted day']");
	By dayList			           = By.cssSelector(".datepicker-days .day:not(.old):not(.new)");
	
	By datePickerMonths			   = By.xpath(".//*[@class='datepicker-months']//tbody//td//span[@class='month']");
	By billingDateInput            = By.xpath("//input[contains(@class, 'MuiInputBase-inputMarginDense') and @type='text']");
	String disableDay              = "//tr/td[text()='%date%' and @class='disabled day']";
	String billingCalYear          = "//div[contains(@class, 'MuiTypography-root MuiPickersYear') and text() = '%year%']";
	String billingCalMonth         = "//div[contains(@class, 'MuiPickersMonth-root') and text() = '%month%']";
	String calendarPickerday       = "//tr/td[text()='%date%' and contains(@class,'day') and not(contains(@class, 'disabled'))]";
	
	public void enterAndSelectLocation(WebDriver driver, String location) {
		waitUntilVisible(driver, locationInputTab);
		enterTextAndSelectFromDropDown(driver, locationInputTab, locationInputTab, locationDropDownItems, location);
	}
	
	public boolean verifyItemFromDropDown(WebDriver driver, By dropDownBox,  By dropDownItems, String itemToSearch){
		waitUntilVisible(driver, dropDownBox);
		clickElement(driver, dropDownBox);
		enterBackspace(driver, dropDownBox);
		enterText(driver, dropDownBox, itemToSearch);
		idleWait(2);
		List<WebElement> searchedDropDownItems = null;
		if(isElementVisible(driver, dropDownItems, 6)) {
			searchedDropDownItems = getInactiveElements(driver, dropDownItems);
		}
		if (searchedDropDownItems == null) {
			return false;
		} else {
			return true;
		}
	}
//		for (WebElement searchedItem : searchedDropDownItems) {
//			if(searchedItem.getText().trim().contains(itemToSearch.trim())){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean verifyLocationInDropDown(WebDriver driver, String location) {
		return verifyItemFromDropDown(driver, locationInputTab, locationDropDownItems, location);
	}
	
	public boolean verifyAgenNameInDropDown(WebDriver driver, String userName) {
		return verifyItemFromDropDown(driver, userInputTab, userNameDropDownItems, userName);
	}
	
	public void selectCategory(WebDriver driver, String category) {
		waitUntilVisible(driver, categorySelectInputTab);
		selectFromDropdown(driver, categorySelectInputTab, SelectTypes.value, category);
	}
	
	public void selectDirection(WebDriver driver, String direction) {
		waitUntilVisible(driver, directionSelectInputTab);
		selectFromDropdown(driver, directionSelectInputTab, SelectTypes.value, direction);
	}
	
	public List<String> getSelectCategoryListDropdown(WebDriver driver) {
		return getTextListFromElements(driver, categoryListElementsDropDown);
	}

	public void enterAndSelectUserName(WebDriver driver, String userName, String id) {
		waitUntilVisible(driver, locationInputTab);
		waitUntilVisible(driver, userInputTab);
		clickElement(driver, userInputTab);
		enterBackspace(driver, userInputTab);
		enterText(driver, userInputTab, userName);
		idleWait(2);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, userNameIdDropDownItems);
		List<WebElement> searcheduUserName = getInactiveElements(driver, userNameDropDownItems);
		for (WebElement searchedItem : searchedDropDownItems) {
			for (WebElement searchedUserName : searcheduUserName) {
				if (searchedItem.getText().equals(id)
						&& searchedUserName.getText().trim().contains(userName.trim())) {
					searchedItem.click();
					break;
				}
			}
		}
	}
	
	public void locationFilter(WebDriver driver , String location) {
		waitUntilVisible(driver, locationInputTab);
		clickElement(driver, locationInputTab);
		enterBackspace(driver, locationInputTab);
		enterText(driver, locationInputTab, location);
		idleWait(2);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, locationDropDownItems);
		for(WebElement searchItem : searchedDropDownItems) {
			if(searchItem.getText().equals(location)) {
				searchItem.click();
				break;
			}
		}
	}
	
	public void locationF(WebDriver driver , String location) {
		if(isElementVisible(driver, locationInputTab, timeOutInSecs)) {
			clickElement(driver, locationInputTab);
			clearAll(driver, locationInputTab);
			enterText(driver, locationBox, location);
			clickElement(driver, getElements(driver, locationDropDownItems).get(0));
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	public void enterAndSelectAccountName(WebDriver driver, String accountName) {
		if (isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			clearAll(driver, accountTextBoxInput);
			enterText(driver, accountTextBoxInput, accountName);
			clickElement(driver, getElements(driver, accountTextBoxOptions).get(0));
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	public boolean verifyAccountNameInDropDown(WebDriver driver, String accountName) {
		boolean value = false;
		if (isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			value = isElementVisible(driver, accountTextBoxOptions, 6);
		}
		return value;
	}
	
	public void verifyLocationSelected(WebDriver driver, String location) {
		assertTrue(getElementsText(driver, locationSelectedItemTab).contains(location));
	}
	
	public boolean isUserNamePresentInTable(WebDriver driver, String userName) {
		return isTextPresentInStringList(driver, getTextListFromElements(driver, userNameResultTable), userName);
	}
	
	public void verifyLocationPresentInTable(WebDriver driver, String location) {
		assertTrue(isTextPresentInStringList(driver, getTextListFromElements(driver, locationResultTable), location));
	}
	
	public void downloadAndVerifyCSV(WebDriver driver, String downloadPath, String reportFileNameDownload, String endFileName) {
		//deleting existing files
		HelperFunctions.deletingExistingFiles(downloadPath, reportFileNameDownload);
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
		// downloading and verifying csv
		waitForFileToDownloadWithPartialName(downloadPath, reportFileNameDownload, endFileName);
		boolean fileDownloaded = isFileDownloaded(downloadPath, reportFileNameDownload, endFileName);
		assertTrue(fileDownloaded, "file is not downloaded");
	}
	
	public void clickSearchBtn(WebDriver driver) {
		waitUntilVisible(driver, searchBtn);
		clickElement(driver, searchBtn);
	}
	
	public void clickAccountToSort(WebDriver driver) {
		waitUntilVisible(driver, accountNameHeader);
		clickElement(driver, accountNameHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickCallKeyToSort(WebDriver driver) {
		waitUntilVisible(driver, callKeyHeader);
		clickElement(driver, callKeyHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickUserNameToSort(WebDriver driver) {
		waitUntilVisible(driver, userNameHeader);
		clickElement(driver, userNameHeader);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickCategoryToSort(WebDriver driver) {
		waitUntilVisible(driver, categoryHeader);
		clickElement(driver, categoryHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickRatingToSort(WebDriver driver) {
		waitUntilVisible(driver, ratingHeader);
		clickElement(driver, ratingHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickDirectionToSort(WebDriver driver) {
		waitUntilVisible(driver, directionHeader);
		clickElement(driver, directionHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickLocationToSort(WebDriver driver) {
		waitUntilVisible(driver, locationHeader);
		clickElement(driver, locationHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickDateCreatedToSort(WebDriver driver) {
		waitUntilVisible(driver, dateCreatedHeader);
		clickElement(driver, dateCreatedHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public List<String> getCallKeyList(WebDriver driver) {
		isListElementsVisible(driver, callKeyResultTable, 6);
		return getTextListFromElements(driver, callKeyResultTable);
	}
	
	public String getUserName(WebDriver driver) {
		isListElementsVisible(driver, userNameResultTable, 6);
		return getElements(driver, userNameResultTable).get(0).getText();
	}
	
	public List<String> getUserNameList(WebDriver driver) {
		isListElementsVisible(driver, userNameResultTable, 6);
		return getTextListFromElements(driver, userNameResultTable);
	}
	
	public String getAccountName(WebDriver driver) {
		isListElementsVisible(driver, accountNameResultTable, 6);
		return getElements(driver, accountNameResultTable).get(0).getText();
	}
	
	public List<String> getAccountNameList(WebDriver driver) {
		isListElementsVisible(driver, accountNameResultTable, 6);
		return getTextListFromElements(driver, accountNameResultTable);
	}

	public String getCategory(WebDriver driver) {
		isListElementsVisible(driver, categoryResultTable, 6);
		return getElements(driver, categoryResultTable).get(0).getText();
	}
	
	public String getLocation(WebDriver driver) {
		isListElementsVisible(driver, locationResultTable, 6);
		return getElements(driver, locationResultTable).get(0).getText();
	}
	
	public List<String> getCategoryList(WebDriver driver) {
		isListElementsVisible(driver, categoryResultTable, 6);
		return getTextListFromElements(driver, categoryResultTable);
	}

	public List<String> getRatingList(WebDriver driver) {
		isListElementsVisible(driver, ratingResultTable, 6);
		return getTextListFromElements(driver, ratingResultTable);
	}
	
	public List<String> getLocationList(WebDriver driver) {
		isListElementsVisible(driver, locationResultTable, 6);
		return getTextListFromElements(driver, locationResultTable);
	}
	
	public List<String> getDateCreatedList(WebDriver driver) {
		isListElementsVisible(driver, dateCreatedResultTable, 6);
		return getTextListFromElements(driver, dateCreatedResultTable);
	}
	
	public String getDirection(WebDriver driver) {
		isListElementsVisible(driver, directionResultTable, 6);
		return getElements(driver, directionResultTable).get(0).getText().toLowerCase();
	}
	
	public String getRating(WebDriver driver) {
		isListElementsVisible(driver, ratingResultTable, 6);
		return getElements(driver, ratingResultTable).get(0).getText();
	}
	
	public String getNotes(WebDriver driver) {
		isListElementsVisible(driver, notesResultTable, 6);
		return getElements(driver, notesResultTable).get(0).getText();
	}
	
	public List<String> getDirectionList(WebDriver driver) {
		isListElementsVisible(driver, directionResultTable, 6);
		return getTextListFromElements(driver, directionResultTable);
	}

	public String getDateCreated(WebDriver driver) {
		isListElementsVisible(driver, directionResultTable, 6);
		return getElements(driver, dateCreatedResultTable).get(0).getText();
	}
	
	public void enterStartDateText(WebDriver driver, String date) {
		waitUntilVisible(driver, startDateInputTab);
		enterText(driver, startDateInputTab, date);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enterEndDateText(WebDriver driver, String date) {
		waitUntilVisible(driver, endDateInputTab);
		enterText(driver, endDateInputTab, date);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyCreatedDateSorted(WebDriver driver, String format, Enum<?> time) {
		Calendar previousCal = Calendar.getInstance();

		// getting todays date with 1 day ahead
		Calendar currentCal = Calendar.getInstance();
		currentCal.add(Calendar.DAY_OF_YEAR, 1);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(format);
		String startDate = new SimpleDateFormat(format).format(currentCal.getTime());

		Date previousDate = null;
		Date currentDate = null;

		try {
			currentDate = dateTimeFormatter.parse(startDate);
			if (time.name().equals("Week")) {
				previousCal.add(Calendar.DAY_OF_YEAR, -8);
				String lastDate = new SimpleDateFormat(format).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.name().equals("Month")) {
				previousCal.add(Calendar.MONTH, -1);
				previousCal.add(Calendar.DAY_OF_YEAR, -1);
				String lastDate = new SimpleDateFormat(format).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.name().equals("Last90days")) {
				previousCal.add(Calendar.DAY_OF_YEAR, -92);
				String lastDate = new SimpleDateFormat(format).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.name().equals("Annual")) {
				previousCal.add(Calendar.YEAR, -1);
				previousCal.add(Calendar.DAY_OF_YEAR, -1);
				String lastDate = new SimpleDateFormat(format).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			}

			// getting results
			ArrayList<Date> callTimeList = new ArrayList<>();

			// sorting and verifying
			clickDateCreatedToSort(driver);
			callTimeList.add(dateTimeFormatter.parse(getDateCreated(driver)));
			assertTrue(callTimeList.get(0).after(previousDate) && callTimeList.get(0).before(currentDate));

			// sorting and verifying
			callTimeList.clear();
			clickDateCreatedToSort(driver);
			callTimeList.add(dateTimeFormatter.parse(getDateCreated(driver)));
			assertTrue(callTimeList.get(0).after(previousDate) && callTimeList.get(0).before(currentDate));

		} catch (Exception e) {
			System.out.println("Not able to parse time" + e.getMessage());
		}
	}
	
	//user counts section
	public void clickDatePickerInputTab(WebDriver driver) {
		waitUntilVisible(driver, datePickerInputTab);
		clickElement(driver, datePickerInputTab);
	}
	
	public String getDatePickerInputValue(WebDriver driver) {
		waitUntilVisible(driver, datePickerInputTab);
		return getAttribue(driver, datePickerInputTab, ElementAttributes.value);
	}
	
	//sla reports section
	
	public void selectSLAPeriod(WebDriver driver, String text) {
		By selectDropdowPeriod = null;
		waitUntilVisible(driver, selectPeriodSLA);
		clickElement(driver, selectPeriodSLA);
		
		if(text.equals("Daily")) {
			selectDropdowPeriod = daily;
		}
		if(text.equals("Weekly")) {
			selectDropdowPeriod = weekly;
		}
		if(text.equals("Monthly")) {
			selectDropdowPeriod = monthly;
		}
		
		waitUntilVisible(driver, selectDropdowPeriod);
		clickElement(driver, selectDropdowPeriod);
		assertEquals(getElementsText(driver, selectDropdowPeriod), text);
		
	//	waitUntilVisible(driver, selectPeriodSlaDropDown);
	//	selectFromDropdown(driver, selectPeriodSlaDropDown, SelectTypes.visibleText, text);
	}
	
	public void clickEndDateSLA(WebDriver driver) {
		waitUntilVisible(driver, endDateSLAInputTab);
		clickElement(driver, endDateSLAInputTab);
	}
	
	public void clickStartDateSLA(WebDriver driver) {
		waitUntilVisible(driver, startDateSLAInputTab);
		clickElement(driver, startDateSLAInputTab);
	}
	
	public void clickDownLoadBtn(WebDriver driver) {
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
	}
	
	public void unCheckAvailability(WebDriver driver) {
		waitUntilVisible(driver, availablityCheckBox);
		if(isAttributePresent(driver, availabilityOnlyCheckBox, "checked")) {
			clickElement(driver, availablityCheckBox);
		}
	}
	
	public void verifyDefaultFiltersSLA(WebDriver driver) {
		waitUntilVisible(driver, availablityCheckBox);
	//	assertTrue(isAttributePresent(driver, availabilityOnlyCheckBox, "checked"));
		
		selectSLAPeriod(driver,"Daily");
		
//		waitUntilVisible(driver, selectPeriodSLA);
//		clickElement(driver, selectPeriodSLA);
//		assertEquals(getSelectedValueFromDropdown(driver, selectPeriodSlaDropDown), "Daily");
		
		assertEquals(HelperFunctions.GetCurrentDateTime("MM/dd/yyyy"), getAttribue(driver, startDateSLAInputTab, ElementAttributes.value));
		assertEquals(HelperFunctions.GetCurrentDateTime("MM/dd/yyyy"), getAttribue(driver, endDateSLAInputTab, ElementAttributes.value));
	}
	
	public void verifyDateToastMessage(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		assertTrue(getElementsText(driver, toastMessage).contains("End date cannot be before start date"));
		waitUntilInvisible(driver, toastMessage);
	}
	
	public void verifyEmailToastMessage(WebDriver driver) {
		By  inValidEmailMsg = By.xpath("//p[contains(text(),'This field is required.')]");
		waitUntilVisible(driver, inValidEmailMsg);
		assertTrue(getElementsText(driver, inValidEmailMsg).contains("This field is required."));
	}
	
	public void verifyEndDateNotBeforeStartDateWeekly(WebDriver driver) {
		selectSLAPeriod(driver, "Weekly");
		// select start date bigger than end date
		reportCommonPage.selectStartDateBiggerEndDate(driver);
		clickDownLoadBtn(driver);

		// verify date validation error occur
		reportCommonPage.verifySelectStartEndDateError(driver);
	}
	
	public void selectStartEndDateSLAWeekly(WebDriver driver) {
		selectSLAPeriod(driver, "Weekly");
		reportCommonPage.selectTodayStartDateEndDate(driver);
	}
	
	public void verifyEndDateNotBeforeStartDateMonthly(WebDriver driver) {
		selectSLAPeriod(driver, "Monthly");
		// select start date bigger than end date
		reportCommonPage.selectStartDateBiggerEndDate(driver);
		clickDownLoadBtn(driver);

		// verify date validation error occur
		reportCommonPage.verifySelectStartEndDateError(driver);
	}
	
	public void selectStartEndDateSLAMonthly(WebDriver driver) {
		selectSLAPeriod(driver, "Monthly");
		reportCommonPage.selectTodayStartDateEndDate(driver);
	}
	
	public void selectTodayDateAsEndDate(WebDriver driver) {
		String endDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
	    selectDateFromDatePicker(driver, endDate);
	}
	
	public static enum SelectDateType {
		StartDate, EndDate;
	}
	
	public void checkFutureDateIsDisabled(WebDriver driver, SelectDateType option) {
		By locator = null;
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		int i = Integer.parseInt(date);
		
		switch (option) {
		case StartDate:
			locator = startDateInputTab;
			break;
		case EndDate:
			locator = endDateInputTab;
			break;
		default:
			break;
		}
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		
		for(int loop = i+1; loop<= monthMaxDays; loop++) {
			By disableDate = By.xpath(disableDay.replace("%date%", String.valueOf(loop)));
			assertTrue(isElementVisible(driver, disableDate, 6));
		}
		By todayDate = By.xpath(calendarPickerday.replace("%date%", String.valueOf(i)));
		waitUntilVisible(driver, todayDate);
		clickElement(driver, todayDate);
	}
	
	public void checkBillingFutureDateIsDisabled(WebDriver driver) {
		LocalDateTime now = LocalDateTime.now();
		int i = now.getMonthValue();
		
		waitUntilVisible(driver, billingDateInput);
		clickElement(driver, billingDateInput);
		
		By currentYear = By.xpath(billingCalYear.replace("%year%", HelperFunctions.GetCurrentDateTime("yyyy")));
		waitUntilVisible(driver, currentYear);
		clickElement(driver, currentYear);
		
		for(int loop = i+1; loop<= 12; loop++) {
			String monthString = new DateFormatSymbols().getMonths()[loop-1];
			By disableDate = By.xpath(billingCalMonth.replace("%month%", monthString.substring(0, 3)));
			assertTrue(isElementVisible(driver, disableDate, 6));
		}
		waitUntilVisible(driver, billingDateInput);
		By currentMonth = By.xpath(billingCalMonth.replace("%month%", HelperFunctions.GetCurrentDateTime("MMM")));
		clickElement(driver, currentMonth);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectStartDateBiggerEndDate(WebDriver driver, String format) {
		String currentDate = HelperFunctions.GetCurrentDateTime(format);
		String startDate = HelperFunctions.addMonthYearDateToExisting(format, currentDate, 0, 0, 0);
		String endDate = HelperFunctions.addMonthYearDateToExisting(format, currentDate, 0, -1, 0);
		clickElement(driver, startDateInputTab);
		clearAll(driver, startDateInputTab);
		enterStartDateText(driver, startDate);
		clickElement(driver, endDateInputTab);
		clearAll(driver, endDateInputTab);
		enterEndDateText(driver, endDate);
	}
	
	public void selectBillingMonth(WebDriver driver, String month , String year) {   
				
				waitUntilVisible(driver, dateInput);
				clickElement(driver, dateInput);
				By currentYear = By.xpath(yearInput.replace("%year%", year));
				waitUntilVisible(driver, currentYear);
				clickElement(driver, currentYear);
				By currentMonth = By.xpath(monthtInput.replace("%month%", month));
				waitUntilVisible(driver, currentMonth);
				clickElement(driver, currentMonth);
				dashboard.isPaceBarInvisible(driver);
	}
	
	public String clickUrlAndGetUserId(WebDriver driver) {
		Actions actions = new Actions(driver);
		isListElementsVisible(driver, userNameResultTable, 6);
		List<WebElement> userName = getElements(driver, userNameResultTable);
		actions.keyDown(Keys.CONTROL).click(userName.get(0)).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		dashboard.isPaceBarInvisible(driver);
		String id = userPage.getUserID(driver);
		closeTab(driver);
		switchToTab(driver, 2);
		return id;
	}
}
