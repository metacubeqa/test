package report.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.ParseException;
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
import org.openqa.selenium.support.Color;
import org.testng.Assert;

import base.SeleniumBase;
import base.TestBase;
import support.source.callFlows.CallFlowPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class ReportMetabaseCommonPage extends SeleniumBase {

	Dashboard dashboard = new Dashboard();
	CallFlowPage callFlowPage = new CallFlowPage();
	
	By noResultsTable                                 = By.xpath(".//span[@class='h4 text-bold' and text()='No results!']");
	By accountInput                                   = By.cssSelector("#accountId");
	public static By startTypingValidation            = By.xpath("//div[@role='presentation']/div/div");
	By agentNameField                                 = By.cssSelector("#agentName");
	By startdateInputTab                              = By.cssSelector("input#startDate, input.start-date");
	By enddateInputTab                                = By.cssSelector("input#endDate, input.end-date");
	By transferNumberInputTab                         = By.cssSelector("input#transferToNumber");
	By dispositionInputTab                            = By.cssSelector("input#disposition");

	By endDateError                                   = By.cssSelector("#endDate-helper-text");
	By startDateError                                 = By.cssSelector("#startDate-helper-text");
	By noResultImage                                  = By.xpath(".//*[contains(@class,'text-slate-light')]/img");
	By noResultText                                   = By.xpath(".//*[contains(@class,'text-slate-light')]/span");
	
	By callShadowDetails                              = By.xpath("//div[contains(text(),' Call Shadowing Details')]");

	By leftNavigateCalendarIcon                       = By.xpath("//*[contains(@class, 'MuiPickersCalendarHeader-switchHeader')]//button[1]/span[1]");
	By rightNavigateCalendarIcon                      = By.xpath("//*[contains(@class, 'MuiPickersCalendarHeader-switchHeader')]//button[2]/span[1]");

	String dateSelectFromCalendar                     = ".//*[contains(@class, 'MuiPickersBasePicker-pickerView')]//div[contains(@class, 'MuiPickersCalendar-week')]//button[not(contains(@class, '-hidden'))]/span/p[text()='$date$']";

	By calendarmonth                                  = By.xpath("//div[contains(@class,'MuiPickersCalendarHeader-transitionContainer')]/p");
	By calMonthButton                                 = By.xpath("//button[contains (@class , 'MuiPickersCalendarHeader-iconButton' )]");
	By calYearButton                                  = By.xpath("//h6[contains( @class, 'MuiTypography-subtitle1')]/ancestor::button");
	By calyearSelection                               = By.xpath("//div[contains(text(),'201') or contains(text(),'202')]");

	By fromNumberInputTab                             = By.cssSelector("#fromNumber");
	By toNumberInputTab                               = By.cssSelector("#toNumber");

	By graphDates                                     = By.cssSelector(".axis.x .tick text");
	By tabularDates                                   = By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[1]/span");

	// headers
	By allheaderList                                  = By.xpath(".//*[contains(@class,'Icon')]/..//div");
	String allHeaderInOne                             =  ".//*[contains(@class,'Icon')]/..//div[text()='%Header%']";
	By fromNumberResultHeader                         = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='fromnumber'or text()='from number']");
	By toNumberResultHeader                           = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='tonumber'or text()='to number']");
	
	By transferredFromCallKeyHeader                     = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='tranferred from call key']");
	By transferredToCallKeyHeader                     = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='tranferred to call key']");
	By mergedFromCallKeyHeader                        = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='merged from call key']");
	By mergedToCallKeyHeader                          = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='merged to call key']");
	
	By dateResultHeader                               = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='date']");
	By durationDateHeader                             = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='starttime']");
	By transfertoUserNameHeader                       = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='transfer to user name']");
	By transfertoNumberHeader                         = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='transfer to number' or text()='tranferred to number']");
	By callDispositionHeader                          = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='call disposition']");
	By directionHeader                                = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='direction' or text()='call direction']");
	By urlResultHeader                                = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='url']");
	By locationHeader                                 = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='location' or text()='location_name']");
	By teamHeader                                     = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='team' or text()='team_name' or text()='teams']");
	By displayNameHeader                              = By.xpath(".//*[contains(@class,'Icon')]/..//div[contains(text(),'agent')] | .//*[contains(@class,'Icon')]/..//div[text()='from user name' or text()='name' or text()='displayname']");
	By supervisorHeader                               = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='supervisor_name']");
	By callKeyHeader                                  = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='callkey']");
	By userIdHeader                                   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='user_id' or text()='user id' or text()='userid']");
	By accountIdHeader                                = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='account_id']");
	By callOwnerHeader                                = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='call owner']");

	String disabledDate                               = ".//*[contains(@class, 'MuiPickersBasePicker-pickerView')]//button/span/p[text()='%date%']/ancestor::div[@role='presentation']/button[contains(@class,'MuiPickersDay-dayDisabled-')]";

	By startDateResultHeader                          = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='start time' or text()='starttime'] | .//table//th//div[contains(text(), 'created at') or contains(text(), 'call start time') or contains(text(), 'date') or contains(text(), 'day')]");
	By endDateResultHeader                            = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='end time'] | .//table//th//div[contains(text(), 'call end time')]");
	By toNumberField                                  = By.cssSelector("#toNumber");
	
	
	By recordingHeading 	            	= By.cssSelector(".cai-reports-filters-react h2");
	By defaultDays			            	= By.cssSelector("#ringdna-select");
	By callQueueName		            	= By.cssSelector("#callQueueName");
	
	By clearLocationFilter              	= By.xpath(".//*[@id='locationName']/..//button[@title='Clear']");
	By clearCallQueueFilter             	= By.xpath(".//*[@id='callQueueName']/..//button[@title='Clear']");
	By clearTeamNameFilter              	= By.xpath(".//*[@id='teamName']/..//button[@title='Clear']");
	
	By locationAutoCompleteInput	    	= By.cssSelector("div[data-testid='autocomplete-locationName'] input");
	By locationNameInput			    	= By.cssSelector("#locationName");
	By locationAutoComplete		        	= By.cssSelector("div[data-testid='autocomplete-locationName']");
	By locationListPopup			    	= By.id("locationName-popup");
	By locationNameOption			    	= By.xpath(".//*[contains(@id, 'locationName-option')]");
	By locationNameFirstOption		    	= By.id("locationName-option-0");
	
	By callQueueAutoCompleteInput	    	= By.cssSelector("div[data-testid='autocomplete-callQueueName'] input");
	By callQueueNameInput			   	 	= By.cssSelector("#callQueueName");
	By callQueueAutoComplete		    	= By.cssSelector("div[data-testid='autocomplete-callQueueName']");
	By callQueueListPopup			    	= By.id("callQueueName-popup");
	By callQueueNameOption			        = By.xpath(".//*[contains(@id, 'callQueueName-option')]");
	By callQueueNameFirstOption		        = By.id("callQueueName-option-0");
	
	By teamNameAutoCompleteInput	        = By.cssSelector("div[data-testid='autocomplete-teamName'] input");
	By teamNameInput			            = By.cssSelector("#teamName");
	By teamNameAutoComplete		            = By.cssSelector("div[data-testid='autocomplete-teamName']");
	By teamNameListPopup			        = By.id("teamName-popup");
	By teamNameOption			            = By.xpath(".//*[contains(@id, 'teamName-option')]");
	By teamNameFirstOption		            = By.id("teamName-option-0");
	
	By supervisorNameAutoCompleteInput	    = By.cssSelector("div[data-testid='autocomplete-supervisorName'] input");
	By supervisorNameInput			        = By.cssSelector("#supervisorName");
	By supervisorNameAutoComplete		    = By.cssSelector("div[data-testid='autocomplete-supervisorName']");
	By supervisorNameListPopup			    = By.id("supervisorName-popup");
	By supervisorNameOption			        = By.xpath(".//*[contains(@id, 'supervisorName-option')]");
	By supervisorNameFirstOption		    = By.id("supervisorName-option-0");
	
	By agentNameHeader                  	= By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='agent_name']");
	By callQueueHeader                  	= By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='call_queue_name']");
	By callDistDayHeader                	= By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='day']");
	
	By dayCreatedList		            	= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[1]/span");
	By nextButton                           = By.xpath("//span[@class ='text-brand-hover pr1 cursor-pointer']");
	By disabledNextButton                   = By.xpath("//span[@class ='text-brand-hover pr1 cursor-pointer disabled']");
	
	//sla
	By startDateSLAInputTab		   = By.xpath("//div[contains(@class, 'MuiGrid-align-items-xs-center')]/div[2]//input");
	By endDateSLAInputTab		   = By.xpath("//div[contains(@class, 'MuiGrid-align-items-xs-center')]/div[3]//input");
	

	public static enum HeaderNames {
		TransferToUsername, TransferToNumber, CallDisposition, Direction, StartDate, EndDate, Url, Location, Team,
		AgentName, Supervisor, CallKey, AccountId, CallOwner, CallDurationSeconds, ListenerName, ListenEndTime,
		ListenStartTime, ListenDuration, CallQueueName, AverageCallDuration, TotalCallDuration, Leads, Contacts,
		Unknown, FromNumber, ToNumber, Inboundd, DialNext, Duration, Status, AbandonedCall, VoiceMail, NumberType, Id,
		Disposition, Source, Name, SFCampaignId, CampaignName, Notes, Outbound, All, SmartNumberId, UserPresenceDetail,
		UserId, TimeMinutes, TimeSeconds, CallSId, ActualDuration, Correct, SFTaskId, Leg1Role, Leg1SId, Leg1Duration,
		Leg2Role, Leg2SId, Leg2Duration, Leg3Role, Leg3SId, Leg3Duration, ExpectedDuration, SmartNumberCount,
		SalesforceId, SandBox, OutboundCallDuration, InboundCallDuration, LastLogin, UpdatedDate, CallState, CallNotes,
		CallDuration, Inbound, TransferredFromCallKey, TransferredToCallKey, MergedFromCallKey, MergedToCallKey;
	}

	public void selectStartEndDate(WebDriver driver, String option, String date, String month, String year) {
		driver.switchTo().defaultContent();
		if (option.equals("Start")) {
			if(isElementVisible(driver, startdateInputTab, 10)) { 
				clickElement(driver, startdateInputTab);	
			}
			if(isElementVisible(driver, startDateSLAInputTab, 6)) {
				clickElement(driver, startDateSLAInputTab);	
			}
		}
		if (option.equals("End")) {
			if(isElementVisible(driver, enddateInputTab, 10)) {
				clickElement(driver, enddateInputTab);	
			}
			if(isElementVisible(driver, endDateSLAInputTab, 6)) {
				clickElement(driver, endDateSLAInputTab);	
			}
		}

		// select year
		waitUntilVisible(driver, calYearButton);
		clickElement(driver, calYearButton);
		// waitUntilVisible(driver, calyearSelection);
		List<WebElement> years = getElements(driver, calyearSelection);
		for (int loop = 0; loop < years.size(); loop++) {
			if (years.get(loop).getText().equals(year)) {
				clickElement(driver, years.get(loop));
				break;
			}
		}
		
		By startDateLoc = By.xpath(dateSelectFromCalendar.replace("$date$", date));
		if(isElementVisible(driver, startDateLoc, 10));{
			clickByJs(driver, startDateLoc);
		}
		

		if (option.equals("Start")) {
			if(isElementVisible(driver, startdateInputTab, 6)) {
				clickElement(driver, startdateInputTab);	
			}
			if(isElementVisible(driver, startDateSLAInputTab, 6)) {
				clickElement(driver, startDateSLAInputTab);	
			}
		}
		if (option.equals("End")) {
			if(isElementVisible(driver, enddateInputTab, 6)) {
				clickElement(driver, enddateInputTab);	
			}
			if(isElementVisible(driver, endDateSLAInputTab, 6)) {
				clickElement(driver, endDateSLAInputTab);	
			}
		}
		
		idleWait(1);

		// select month
		for (int i = 0; i < 12; i++) {
			String mon = getElementsText(driver, calendarmonth);
			String Month[] = mon.split(" ");

			if (Month[0].equals(month)) {
				break;
			}

			List<WebElement> monthButton = getElements(driver, calMonthButton);
			waitUntilVisible(driver, monthButton.get(0));
			clickElement(driver, monthButton.get(0));
		}

		// select date
		By startDateLoc2 = By.xpath(dateSelectFromCalendar.replace("$date$", date));
		waitUntilVisible(driver, startDateLoc2);
		clickByJs(driver, startDateLoc2);
	}

	public void enterTransferToNumber(WebDriver driver, String number) {
		waitUntilVisible(driver, transferNumberInputTab);
		clearAll(driver, transferNumberInputTab);
		enterText(driver, transferNumberInputTab, number);
	}

	public void enterDisposition(WebDriver driver, String number) {
		waitUntilVisible(driver, dispositionInputTab);
		enterText(driver, dispositionInputTab, number);
	}

	public String getStartDateInputTab(WebDriver driver) {
		waitUntilVisible(driver, startdateInputTab);
		return getAttribue(driver, startdateInputTab, ElementAttributes.value);
	}

	public String getEndDateInputTab(WebDriver driver) {
		waitUntilVisible(driver, enddateInputTab);
		return getAttribue(driver, enddateInputTab, ElementAttributes.value);
	}

	public void clickStartTimeToSort(WebDriver driver) {
		scrollToElement(driver, startDateResultHeader);
		waitUntilVisible(driver, startDateResultHeader);
		clickElement(driver, startDateResultHeader);
	}

	public void clickFromNumberToSort(WebDriver driver) {
		scrollToElement(driver, fromNumberResultHeader);
		waitUntilVisible(driver, fromNumberResultHeader);
		clickElement(driver, fromNumberResultHeader);
	}

	public void clickToNumberToSort(WebDriver driver) {
		scrollToElement(driver, toNumberResultHeader);
		waitUntilVisible(driver, toNumberResultHeader);
		clickElement(driver, toNumberResultHeader);
	}

	public void clickTransferToUserNameSort(WebDriver driver) {
		scrollToElement(driver, transfertoUserNameHeader);
		waitUntilVisible(driver, transfertoUserNameHeader);
		clickElement(driver, transfertoUserNameHeader);
	}

	public void verifyResultsNotAvailable(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By resultLoc = By.xpath(resultTableList);
		assertFalse(isListElementsVisible(driver, resultLoc, 5));
		assertTrue(isElementVisible(driver, noResultsTable, 5));
	}

	public String getStartDateResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By startDateResultLoc = By.xpath(resultTableList);
		return getInactiveElements(driver, startDateResultLoc).get(0).getText();
	}

	public String getEndDateResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By endDateResultLoc = By.xpath(resultTableList);
		return getInactiveElements(driver, endDateResultLoc).get(0).getText();
	}

	public String getAgentNameResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By agentNameResultLoc = By.xpath(resultTableList);
		return getInactiveElements(driver, agentNameResultLoc).get(0).getText();
	}

	public String getFromNumberResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By fromNumberResultLoc = By.xpath(resultTableList);
		return getTextListFromElements(driver, fromNumberResultLoc).get(0);
	}

	public String getUrlResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By urlResultLoc = By.xpath(resultTableList);
		return getInactiveElements(driver, urlResultLoc).get(0).getText();
	}

	public void clickHeaderToSort(WebDriver driver, HeaderNames header) {

		By headerLocator = null;

		switch (header) {
		case TransferToUsername:
			headerLocator = transfertoUserNameHeader;
			break;
		case TransferToNumber:
			headerLocator = transfertoNumberHeader;
			break;
		case CallDisposition:
			headerLocator = callDispositionHeader;
			break;
		case Direction:
			headerLocator = directionHeader;
			break;
		case StartDate:
			headerLocator = startDateResultHeader;
			break;
		case EndDate:
			headerLocator = endDateResultHeader;
			break;
		case Url:
			headerLocator = urlResultHeader;
			break;
		case Location:
			headerLocator = locationHeader;
			break;
		case Team:
			headerLocator = teamHeader;
			break;
		case AgentName:
			headerLocator = displayNameHeader;
			break;
		case Supervisor:
			headerLocator = supervisorHeader;
			break;
		default:
			break;
		}
		scrollToElement(driver, headerLocator);
		waitUntilVisible(driver, headerLocator);
		clickElement(driver, headerLocator);
	}

	public String iterateListForNonEmptyResult(WebDriver driver, int index) {

		String value = "";
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By resultLoc = By.xpath(resultTableList);
		for (int i = 0; i < 3; i++) {
			resultLoc = By.xpath(resultTableList);
			List<String> list = getTextListFromElements(driver, resultLoc);
			for (String text : list) {
				if (!text.equals("-")) {
					value = text;
					return value;
				}
			}
			clickHeaderToSortByIndex(driver, index);
		}
		return value;
	}

	public void enterFromNumber(WebDriver driver, String fromNumber) {
		waitUntilVisible(driver, fromNumberInputTab);
		clickElement(driver, fromNumberInputTab);
		clearAll(driver, fromNumberInputTab);
		enterText(driver, fromNumberInputTab, fromNumber);
	}

	public String getFirstResultTableAccToColumn(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By resultLoc = By.xpath(resultTableList);
		return getInactiveElements(driver, resultLoc).get(0).getText();
	}

	public List<String> getTableResultListAccToColumn(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By resultLoc = By.xpath(resultTableList);
		return getTextListFromElements(driver, resultLoc);
	}
	
	public void clickFirstResultTableAccToColumn(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By resultLoc = By.xpath(resultTableList);
		getInactiveElements(driver, resultLoc).get(0).click();
	}

	public String getToNumberResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By toNumberResultLoc = By.xpath(resultTableList);
		return getTextListFromElements(driver, toNumberResultLoc).get(0);
	}

	public String getDirectionResult(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By directionResultLoc = By.xpath(resultTableList);
		return getTextListFromElements(driver, directionResultLoc).get(0);
	}

	public void enterToNumber(WebDriver driver, String toNumber) {
		waitUntilVisible(driver, toNumberInputTab);
		clickElement(driver, toNumberInputTab);
		clearAll(driver, toNumberInputTab);
		enterText(driver, toNumberInputTab, toNumber);
	}

	public void clickAndVerifyActionUrl(WebDriver driver, int index) {
		String resultTableList = String.format(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[%d]/span", index);
		By actionUrlLoc = By.xpath(resultTableList);
		isListElementsVisible(driver, actionUrlLoc, 5);

		List<String> list = getTextListFromElements(driver, actionUrlLoc);
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals("-")) {
				Actions actions = new Actions(driver);
				WebElement openUrl = getInactiveElements(driver, actionUrlLoc).get(i);

				// opening url in new tab and switching
				actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
				switchToTab(driver, getTabCount(driver));
				waitForPageLoaded(driver);
				dashboard.isPaceBarInvisible(driver);
				String appUrl = TestBase.CONFIG.getProperty("qa_support_tool_site");
				assertTrue(driver.getCurrentUrl().contains(appUrl.concat("/#call-player"))
						|| driver.getCurrentUrl().contains(appUrl.concat("/#smart-recordings")));
				closeTab(driver);
				switchToTab(driver, 2);
				break;
			}
		}
	}

	/**
	 * @param driver
	 * @param inputFormat
	 * @param outputFormat
	 * @param index
	 * @param startDate
	 * @param endDate
	 * 
	 * Verifying Date Range and Order based on custom date on both Start & End Date Headers
	 */
	public void verifyDateSortedCustom(WebDriver driver, String inputFormat, String outputFormat, int index, String startDate, String endDate) {
		SimpleDateFormat castFormatter = new SimpleDateFormat(inputFormat);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(outputFormat);

		startDate = HelperFunctions.addMonthYearDateToExisting(inputFormat, startDate, -1, 0, 0);

		endDate = HelperFunctions.addMonthYearDateToExisting(inputFormat, endDate, 1, 0, 0);

		try {

			Date startDateInput = castFormatter.parse(startDate);
			Date endDateInput = castFormatter.parse(endDate);

			// getting results
			ArrayList<Date> callTimeList = new ArrayList<>();

			// sorting and verifying
			clickHeaderToSortByIndex(driver, index);
			String castDate = castFormatter.format(dateTimeFormatter.parse(addYear(driver, getStartDateResult(driver, index))));
			callTimeList.add(castFormatter.parse(castDate));
			assertTrue(callTimeList.get(0).after(startDateInput) && callTimeList.get(0).before(endDateInput));
			List<String> dateStringList = getTableResultListAccToColumn(driver, index);
			List<String> dateWithYear = new ArrayList<String>();
			for(String y : dateStringList) {
				dateWithYear.add(addYear(driver, y));
			}
			//assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, dateWithYear)));

			// sorting and verifying
			callTimeList.clear();
			clickHeaderToSortByIndex(driver, index);
			castDate = castFormatter.format(dateTimeFormatter.parse(addYear(driver, getStartDateResult(driver, index))));
			callTimeList.add(castFormatter.parse(castDate));
			assertTrue(callTimeList.get(0).after(startDateInput) && callTimeList.get(0).before(endDateInput));
			dateStringList = getTableResultListAccToColumn(driver, index);
			List<String> dateWithYear2 = new ArrayList<String>();
			for(String y : dateStringList) {
				dateWithYear2.add(addYear(driver, y));
			}
			//assertTrue(HelperFunctions.verifyDescendingOrderedList(HelperFunctions.getStringListInDateFormat(outputFormat, dateWithYear2)));
			clickHeaderToSortByIndex(driver, index);

		} catch (Exception e) {
			System.out.println("Not able to parse time" + e.getMessage());
			Assert.fail("Case failed due to unparseable date format");
		}
	}

	/**
	 * @param driver
	 * @param format
	 * 
	 * Verifying Graph Date Range & Order 
	 */
	public void verifygraphDate(WebDriver driver, String format, String startDate, String endDate) {
		Date preDate = null;
		Date currDate = null;
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(format);
		try {
			preDate = dateTimeFormatter.parse(startDate);
			currDate = dateTimeFormatter.parse(endDate);
			Calendar c = Calendar.getInstance();
			c.setTime(preDate);
			c.add(Calendar.DATE, -1);
			preDate = c.getTime();
			c.setTime(currDate);
			c.add(Calendar.DATE, 1);
			currDate = c.getTime();
			List <Date> callTimeList = new ArrayList<Date>();
			List<WebElement> graphDateElements = getElements(driver, graphDates);
			for (int i = 0; i < graphDateElements.size(); i++) {
				callTimeList.add(dateTimeFormatter.parse(addYear(driver, getTextListFromElements(driver, graphDates).get(i))));
				assertTrue(callTimeList.get(i).after(preDate) && callTimeList.get(i).before(currDate));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//verifying ascending order graph dates
		List<String> dateStringList = getTextListFromElements(driver, graphDates);
		List<String> dateWithYear = new ArrayList<String>();
		for(String y : dateStringList) {
			dateWithYear.add(addYear(driver, y));
		}
		//assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(format, dateWithYear)));
	}
	
	public String addYear(WebDriver driver, String dateText) {
		String date = null;
		Calendar prevYear = Calendar.getInstance();
		prevYear.add(Calendar.YEAR, -1);
		prevYear.get(Calendar.YEAR);
		String Year = new SimpleDateFormat("yyyy").format(new Date());
		
		if (dateText.contains(Year) || dateText.contains(String.valueOf(prevYear.get(Calendar.YEAR)))) {
			return dateText;
		}
		if (!dateText.contains(Year) || !dateText.contains(String.valueOf(prevYear.get(Calendar.YEAR)))) {
			int month = Integer.valueOf(dateText.substring(0, 2));

			if (month > (Calendar.getInstance().get(Calendar.MONTH) + 1)) {
				date = dateText + "/" + (prevYear.get(Calendar.YEAR));
			}

			if ((Calendar.getInstance().get(Calendar.MONTH) + 1) >= month) {
				date = dateText + "/" + Year;
			}

		}
		return date;
	}

	public void clickDurationDateToSort(WebDriver driver) {
		waitUntilVisible(driver, durationDateHeader);
		scrollToElement(driver, durationDateHeader);
		waitUntilVisible(driver, durationDateHeader);
		clickElement(driver, durationDateHeader);
	}

	public boolean verifyAccountNamePresent(WebDriver driver) {
		return isElementVisible(driver, accountInput, 5);
	}

	public void verifyDefaultDaysInputTab(WebDriver driver, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime localDate = LocalDateTime.now();
		String EndDate = dtf.format(localDate);
		String StartDate = dtf.format(localDate.minusDays(30));
		

		assertEquals(getAttribue(driver, startdateInputTab, ElementAttributes.value), StartDate);
		assertEquals(getAttribue(driver, enddateInputTab, ElementAttributes.value), EndDate);
	}

	public boolean checkMultiplesItemsOnUI(WebDriver driver, List<String> list, List<String> listOnUI) {
		int count = 0;
		boolean answer = false;
		for (int i = 0; i < listOnUI.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (listOnUI.get(i).equals(list.get(j))) {
					count++;
					j = 0;
				}
			}
		}
		if (count == listOnUI.size()) {
			answer = true;
		}

		return answer;
	}

	public static enum SelectDate {
		StartDate, EndDate;
	}

	public void checkFutureDateIsDisabled(WebDriver driver, SelectDate option) {
		By locator = null;
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		int i = Integer.parseInt(date);

		switch (option) {
		case StartDate:
			locator = startdateInputTab;
			break;
		case EndDate:
			locator = enddateInputTab;
			break;
		default:
			break;
		}
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);

		for (int loop = i + 1; loop <= monthMaxDays; loop++) {
			By disableDate = By.xpath(disabledDate.replace("%date%", String.valueOf(loop)));
			assertTrue(isElementVisible(driver, disableDate, 6));
		}
		By todayDate = By.xpath(dateSelectFromCalendar.replace("$date$", String.valueOf(i)));
		waitUntilVisible(driver, todayDate);
		clickElement(driver, todayDate);
	}

	public String getUserNamefiledValue(WebDriver driver) {
		String answer = getAttribue(driver, agentNameField, ElementAttributes.value);
		return answer;
	}

	public void verifyNoResultTextImage(WebDriver driver) {
		waitUntilVisible(driver, noResultText);
		assertTrue(isElementVisible(driver, noResultImage, 5));
		assertEquals(getElementsText(driver, noResultText), "No results!");
	}

	public void verifySelectStartEndDateError(WebDriver driver) {
		waitUntilVisible(driver, endDateError);
		assertTrue(isElementVisible(driver, endDateError, 5));
		waitUntilVisible(driver, startDateError);
		assertTrue(isElementVisible(driver, startDateError, 5));
		assertEquals(getElementsText(driver, startDateError), "Start date must be before end date");
		assertEquals(getElementsText(driver, endDateError), "End date must be after start date");

		String startDatecolor = getCssValue(driver, findElement(driver, startDateError), CssValues.Color);
		String startDateHexColor = Color.fromString(startDatecolor).asHex();
		assertEquals(startDateHexColor, "#f44336", "color is not red");

		String EndDatecolor = getCssValue(driver, findElement(driver, endDateError), CssValues.Color);
		String EndDateHexColor = Color.fromString(EndDatecolor).asHex();
		assertEquals(EndDateHexColor, "#f44336", "color is not red");
	}

	public void selectStartDateBiggerEndDate(WebDriver driver) {
		String curentDate = HelperFunctions.GetCurrentDateTime("d");
		String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
		String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");

		selectStartEndDate(driver, "Start", curentDate, currentMonth, currentYear);
		selectStartEndDate(driver, "Start", curentDate, currentMonth, currentYear);

		String endYear = HelperFunctions.addMonthYearDateToExisting("yyyy", currentYear, 0, 0, -1);
		selectStartEndDate(driver, "End", curentDate, currentMonth, endYear);
		selectStartEndDate(driver, "End", curentDate, currentMonth, endYear);
	}

	/**
	 * @param driver
	 * select current start and end date
	 */
	public void selectTodayStartDateEndDate(WebDriver driver) {
		String curentDate = HelperFunctions.GetCurrentDateTime("d");
		String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
		String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");

		selectStartEndDate(driver, "Start", curentDate, currentMonth, currentYear);
		idleWait(1);
		selectStartEndDate(driver, "End", curentDate, currentMonth, currentYear);
		idleWait(1);
		selectStartEndDate(driver, "Start", curentDate, currentMonth, currentYear);
		idleWait(1);
		selectStartEndDate(driver, "End", curentDate, currentMonth, currentYear);
	}

	public String getUserNamefieldValue(WebDriver driver) {
		String answer = getAttribue(driver, agentNameField, ElementAttributes.value);
		return answer;
	}

	public void selectToNumber(WebDriver driver, String toNumber) {
		driver.switchTo().defaultContent();
		waitUntilVisible(driver, toNumberField);
		findElement(driver, toNumberField).sendKeys(toNumber);
	}

	public void clickHeaderToSortByIndex(WebDriver driver, int index) {
		int headerIndex = index - 1;
		List<WebElement> Headers = getElements(driver, allheaderList);
		scrollToElement(driver, Headers.get(headerIndex));
		waitUntilVisible(driver, Headers.get(headerIndex));
		clickElement(driver, Headers.get(headerIndex));
	}

	public void clickMultipleToNumberToSort(WebDriver driver) {
		List<WebElement> toNumber = getElements(driver, toNumberResultHeader);
		scrollToElement(driver, toNumber.get(0));
		waitUntilVisible(driver, toNumber.get(0));
		clickElement(driver, toNumber.get(0));
	}
	
	public String getAgentInputValue(WebDriver driver) {
		return getAttribue(driver, agentNameField, ElementAttributes.value);
	}
	
	public int getIndexNoByHeaderName(WebDriver driver, HeaderNames header) {
		String headerLocator = null;
		By locator = null;
		boolean replaceLocator = false;
		
		switch (header) {
		case TransferToUsername:
			headerLocator = "tranferred to user name";
			replaceLocator = true;
			break;
		case TransferToNumber:
			headerLocator = "tranferred to number";
			replaceLocator = true;
			break;
		case CallDisposition:
			headerLocator = "call disposition";
			replaceLocator = true;
			break;
		case Direction:
			locator  = directionHeader;
			break;
		//	headerLocator = "direction";
		//	replaceLocator = true;
			
		case StartDate:
			locator = startDateResultHeader;
			break;
		case EndDate:
			locator = endDateResultHeader;
			break;
		case Url:
			headerLocator = "url";
			replaceLocator = true;
			break;
		case Location:
			locator = locationHeader;
			break;
		case AgentName:
			locator = displayNameHeader;
			break;
		case Supervisor:
			headerLocator = "supervisor_name";
			replaceLocator = true;
			break;
		case Team:
			locator = teamHeader;
			break;
		case CallKey:
			headerLocator = "callkey";
			replaceLocator = true;
			break;
		case AccountId:
			headerLocator = "account_id";
			replaceLocator = true;
			break;
		case CallOwner:
			headerLocator = "call owner";
			replaceLocator = true;
			break;
		case CallDurationSeconds:
			headerLocator = "call duration (seconds)";
			replaceLocator = true;
			break;
		case CallDuration:
			headerLocator = "call duration";
			replaceLocator = true;
			break;
		case TotalCallDuration:
			headerLocator = "total call duration (min)";
			replaceLocator = true;
			break;
		case AverageCallDuration:
			headerLocator = "average call duration (sec)";
			replaceLocator = true;
			break;
		case ListenerName:
			headerLocator = "listener name";
			replaceLocator = true;
			break;
		case ListenStartTime:
			headerLocator = "listen start time";
			replaceLocator = true;
			break;
		case ListenEndTime:
			headerLocator = "listen end time";
			replaceLocator = true;
			break;
		case ListenDuration:
			headerLocator = "listen duration (seconds)";
			replaceLocator = true;
			break;
		case CallQueueName:
			headerLocator = "call_queue_name";
			replaceLocator = true;
			break;
		case Leads:
			headerLocator = "leads";
			replaceLocator = true;
			break;
		case Contacts:
			headerLocator = "contacts";
			replaceLocator = true;
			break;
		case Unknown:
			headerLocator = "unknown";
			replaceLocator = true;
			break;
		case FromNumber:
			locator = fromNumberResultHeader;
			break;
		case ToNumber:
			locator = toNumberResultHeader;
			break;
		case TransferredFromCallKey:
			locator = transferredFromCallKeyHeader;
			break;
		case TransferredToCallKey:
			locator = transferredToCallKeyHeader;
			break;
		case MergedFromCallKey:
			locator = mergedFromCallKeyHeader;
			break;
		case MergedToCallKey:
			locator = mergedToCallKeyHeader;
			break;
		case Inboundd:
			headerLocator = "inbound?";
			replaceLocator = true;
			break;
		case Inbound:
			headerLocator = "inbound";
			replaceLocator = true;
			break;
		case DialNext:
			headerLocator = "dialnext";
			replaceLocator = true;
			break;
		case Duration:
			headerLocator = "duration";
			replaceLocator = true;
			break;
		case Status:
			headerLocator = "status";
			replaceLocator = true;
			break;
		case VoiceMail:
			headerLocator = "voice mail";
			replaceLocator = true;
			break;
		case AbandonedCall:
			headerLocator = "abandoned call";
			replaceLocator = true;
			break;
		case Disposition:
			headerLocator = "disposition";
			replaceLocator = true;
			break;
		case Id:
			headerLocator = "id";
			replaceLocator = true;
			break;
		case NumberType:
			headerLocator = "number type";
			replaceLocator = true;
			break;
		case Source:
			headerLocator = "source";
			replaceLocator = true;
			break;
		case Name:
			headerLocator = "name";
			replaceLocator = true;
			break;
		case SFCampaignId:
			headerLocator = "sfcampaignid";
			replaceLocator = true;
			break;
		case CampaignName:
			headerLocator = "campaign name";
			replaceLocator = true;
			break;
		case Notes:
			headerLocator = "notes";
			replaceLocator = true;
			break;
		case All:
			headerLocator = "all";
			replaceLocator = true;
			break;
		case Outbound:
			headerLocator = "outbound";
			replaceLocator = true;
			break;
		case SmartNumberId:
			headerLocator = "smartnumber id";
			replaceLocator = true;
			break;
		case UserPresenceDetail:
			headerLocator = "user presence detail";
			replaceLocator = true;
			break;
		case UserId:
			locator = userIdHeader;
			break;
		case TimeSeconds:
			headerLocator = "time (seconds)";
			replaceLocator = true;
			break;
		case TimeMinutes:
			headerLocator = "time (minutes)";
			replaceLocator = true;
			break;
		case CallSId:
			headerLocator = "callsid";
			replaceLocator = true;
			break;
		case ActualDuration:
			headerLocator = "actual_duration";
			replaceLocator = true;
			break;
		case ExpectedDuration:
			headerLocator = "expected_duration";
			replaceLocator = true;
			break;
		case Correct:
			headerLocator = "correct";
			replaceLocator = true;
			break;
		case SFTaskId:
			headerLocator = "sftaskid";
			replaceLocator = true;
			break;
		case Leg1Role:
			headerLocator = "leg1_role";
			replaceLocator = true;
			break;
		case Leg1SId:
			headerLocator = "leg1_sid";
			replaceLocator = true;
			break;
		case Leg1Duration:
			headerLocator = "leg1_duration";
			replaceLocator = true;
			break;
		case Leg2Role:
			headerLocator = "leg2_role";
			replaceLocator = true;
			break;
		case Leg2SId:
			headerLocator = "leg2_sid";
			replaceLocator = true;
			break;
		case Leg2Duration:
			headerLocator = "leg2_duration";
			replaceLocator = true;
			break;
		case Leg3Role:
			headerLocator = "leg3_role";
			replaceLocator = true;
			break;
		case Leg3SId:
			headerLocator = "leg3_sid";
			replaceLocator = true;
			break;
		case Leg3Duration:
			headerLocator = "leg3_duration";
			replaceLocator = true;
			break;
		case SalesforceId:
			headerLocator = "salesforce id";
			replaceLocator = true;
			break;
		case SandBox:
			headerLocator = "sandbox";
			replaceLocator = true;
			break;
		case SmartNumberCount:
			headerLocator = "smart number count";
			replaceLocator = true;
			break;
		case UpdatedDate:
			headerLocator = "updateddate";
			replaceLocator = true;
			break;
		case LastLogin:
			headerLocator = "last login";
			replaceLocator = true;
			break;
		case InboundCallDuration:
			headerLocator = "inbound call duration (sec)";
			replaceLocator = true;
			break;
		case OutboundCallDuration:
			headerLocator = "outbound call duration (sec)";
			replaceLocator = true;
			break;
		case CallState:
			headerLocator = "call state";
			replaceLocator = true;
			break;
		case CallNotes:
			headerLocator = "call notes";
			replaceLocator = true;
			break;
		default:
			break;
		}
		
		if (replaceLocator)
			locator = By.xpath(allHeaderInOne.replace("%Header%", headerLocator));
		
		scrollToElement(driver, locator);
		waitUntilVisible(driver, locator);
		String locatorName = findElement(driver, locator).getText();
		List<String> headerText = getTextListFromElements(driver, allheaderList);
		int index = headerText.indexOf(locatorName);
		return index+1;
	}
	
	public String getStartTypingDropDownText(WebDriver driver) {
		waitUntilVisible(driver, startTypingValidation);
		return findElement(driver, startTypingValidation).getText();
	}
	
	
	// call distribution page
	public void defaultDaysSelected(WebDriver driver) {
		waitUntilVisible(driver, defaultDays);
		assertEquals(findElement(driver, defaultDays).getText(), "Last 7 days");
	}

	public void selectCallQueue(WebDriver driver, String agentName) {
		waitUntilVisible(driver, callQueueNameInput);
		enterText(driver, callQueueNameInput, agentName);
		idleWait(2);
		waitUntilVisible(driver, callQueueAutoComplete);
		clickElement(driver, callQueueAutoComplete);
		waitUntilVisible(driver, callQueueAutoCompleteInput);
		clickElement(driver, callQueueAutoCompleteInput);
		waitUntilVisible(driver, callQueueListPopup);
		clickElement(driver, callQueueNameFirstOption);
		dashboard.isPaceBarInvisible(driver);
	}

	public void selectLocation(WebDriver driver, String agentName) {
		waitUntilVisible(driver, locationNameInput);
		clickElement(driver, locationNameInput);
		clearAll(driver, locationNameInput);
		enterText(driver, locationNameInput, agentName);
		idleWait(2);
		waitUntilVisible(driver, locationAutoComplete);
		clickElement(driver, locationAutoComplete);
		waitUntilVisible(driver, locationAutoCompleteInput);
		clickElement(driver, locationAutoCompleteInput);
		waitUntilVisible(driver, locationListPopup);
		clickElement(driver, locationNameFirstOption);
		dashboard.isPaceBarInvisible(driver);
	}

	public void selectTeamName(WebDriver driver, String teamName) {
		waitUntilVisible(driver, teamNameInput);
		clearAll(driver, teamNameInput);
		enterText(driver, teamNameInput, teamName);
		idleWait(2);
		waitUntilVisible(driver, teamNameAutoComplete);
		clickElement(driver, teamNameAutoComplete);
		waitUntilVisible(driver, teamNameAutoCompleteInput);
		clickElement(driver, teamNameAutoCompleteInput);
		waitUntilVisible(driver, teamNameListPopup);
		clickElement(driver, teamNameFirstOption);
		dashboard.isPaceBarInvisible(driver);
	}

	public void selectSupervisorName(WebDriver driver, String teamName) {
		waitUntilVisible(driver, supervisorNameInput);
		clearAll(driver, supervisorNameInput);
		enterText(driver, supervisorNameInput, teamName);
		idleWait(2);
		waitUntilVisible(driver, supervisorNameAutoComplete);
		clickElement(driver, supervisorNameAutoComplete);
		waitUntilVisible(driver, supervisorNameAutoCompleteInput);
		clickElement(driver, supervisorNameAutoCompleteInput);
		waitUntilVisible(driver, supervisorNameListPopup);
		clickElement(driver, supervisorNameFirstOption);
		dashboard.isPaceBarInvisible(driver);
	}

	public boolean isLocationFilterExists(WebDriver driver) {
		return isElementVisible(driver, locationNameInput, 5);
	}
	
	public void clearLocationFilter(WebDriver driver) {
		clickElement(driver, locationNameInput);
		clearAll(driver, locationNameInput);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clearTeamNameFilter(WebDriver driver) {
		clickElement(driver, teamNameInput);
		clearAll(driver, teamNameInput);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clearSupervisorNameFilter(WebDriver driver) {
		clickElement(driver, supervisorNameInput);
		clearAll(driver, supervisorNameInput);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clearCallQueueFilter(WebDriver driver) {
		hoverElement(driver, locationNameInput);
		if (isElementVisible(driver, clearCallQueueFilter, 5)) {
			waitUntilVisible(driver, clearCallQueueFilter);
			clickElement(driver, clearCallQueueFilter);
		}
		dashboard.isPaceBarInvisible(driver);
	}

	public boolean verifyLocationExistsInDropDown(WebDriver driver, String locationName) {
		waitUntilVisible(driver, locationNameInput);
		clearLocationFilter(driver);
		enterText(driver, locationNameInput, locationName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownLocations(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, locationName);
		}
	}

	public List<WebElement> getListDropDownLocations(WebDriver driver) {
		waitUntilVisible(driver, locationAutoComplete);
		clickElement(driver, locationAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, locationAutoCompleteInput);
		clickElement(driver, locationAutoCompleteInput);
		if (isElementVisible(driver, locationListPopup, 5)) {
			return getElements(driver, locationNameOption);
		} else {
			return null;
		}
	}

	public boolean verifyCallQueueExistsInDropDown(WebDriver driver, String locationName) {
		waitUntilVisible(driver, callQueueNameInput);
		clearCallQueueFilter(driver);
		enterText(driver, callQueueNameInput, locationName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownCallQueue(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, locationName);
		}
	}

	public List<WebElement> getListDropDownCallQueue(WebDriver driver) {
		waitUntilVisible(driver, callQueueAutoComplete);
		clickElement(driver, callQueueAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, callQueueAutoCompleteInput);
		clickElement(driver, callQueueAutoCompleteInput);
		if (isElementVisible(driver, callQueueListPopup, 5)) {
			return getElements(driver, callQueueNameOption);
		} else {
			return null;
		}
	}

	public boolean verifyTeamNameExistsInDropDown(WebDriver driver, String agentName) {
		waitUntilVisible(driver, teamNameInput);
		clearTeamNameFilter(driver);
		enterText(driver, teamNameInput, agentName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownTeams(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, agentName);
		}
	}

	public List<WebElement> getListDropDownTeams(WebDriver driver) {
		waitUntilVisible(driver, teamNameAutoComplete);
		clickElement(driver, teamNameAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, teamNameAutoCompleteInput);
		clickElement(driver, teamNameAutoCompleteInput);
		if (isElementVisible(driver, teamNameListPopup, 5)) {
			return getElements(driver, teamNameOption);
		} else {
			return null;
		}
	}

	public boolean verifySupervisorNameExistsInDropDown(WebDriver driver, String supervisorName) {
		waitUntilVisible(driver, supervisorNameInput);
		clearSupervisorNameFilter(driver);
		enterText(driver, supervisorNameInput, supervisorName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownSupervisor(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, supervisorName);
		}
	}

	public List<WebElement> getListDropDownSupervisor(WebDriver driver) {
		waitUntilVisible(driver, supervisorNameAutoComplete);
		clickElement(driver, supervisorNameAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, supervisorNameAutoCompleteInput);
		clickElement(driver, supervisorNameAutoCompleteInput);
		if (isElementVisible(driver, supervisorNameListPopup, 5)) {
			return getElements(driver, supervisorNameOption);
		} else {
			return null;
		}
	}
	
	public String getSupervisorDashboardTeamName(WebDriver driver, String teamName) {
		if(teamName.contains(",")) {
			teamName = teamName.split(",")[0];
		}
		return teamName;
	}
	
	
	/**
	 * @Desc : click on next page
	 * @param driver
	 * @param page
	 * @return flag
	 */
	public boolean navigateToNextPage(WebDriver driver, int page) {
		boolean flag = false;
		for(int loop=1; loop<= page; loop++) {
			if(isElementVisible(driver, nextButton, 6) && !isElementVisible(driver, disabledNextButton, 5)) {
				waitUntilVisible(driver, nextButton);
				clickElement(driver, nextButton);
				flag = true;
			}else {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * @Desc : check the duplicate rc key
	 * @param rckeyList
	 * @param callerNameList
	 * @return boolean flag
	 */
	public boolean isDuplicateCallKeyPresent(WebDriver driver, List<Float> rckeyList, List<String> callerNameList) {
		boolean flag = false;
		for (int i = 0; i < rckeyList.size(); i++) {
			for (int j = i + 1; j < rckeyList.size(); j++) {
				if (rckeyList.get(i).equals(rckeyList.get(j))) {
					if (callerNameList.get(i).equals(callerNameList.get(j))) {
						flag = true;
						return flag;
					}
				}
			}
		}
		return flag;
	}
	
}
