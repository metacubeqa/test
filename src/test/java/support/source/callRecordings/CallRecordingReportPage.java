package support.source.callRecordings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.io.File;
import java.text.SimpleDateFormat;
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
import base.TestBase;
import org.testng.Assert;
import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import softphone.source.salesforce.TaskDetailPage;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;
import utility.HelperFunctions;

public class CallRecordingReportPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CallFlowPage callFlowPage = new CallFlowPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	ReportMetabaseCommonPage reportPage = new ReportMetabaseCommonPage();
	ReportBase reportBase = new ReportBase();

	//Call Recording Report
	By recordingHeading 	= By.cssSelector(".cai-reports-filters h2");
	By clearAccountBtn	 	= By.xpath(".//*[@id='accountId']/..//button[@title='Clear']");
	By clearAgentFilter		= By.xpath(".//*[@id='agentName']/..//button[@title='Clear']");
	
	By iframeTable			= By.xpath(".//iframe[contains(@src, 'https://reporting')]");
	By agentNameValue		= By.cssSelector(".user-picker .has-items div");
	By callerNameInput		= By.id("caller");
	By downloadLink			= By.cssSelector("[title='Download this data']");
	By csvIcon				= By.cssSelector(".Icon-csv");
	By chartGraph           = By.cssSelector(".dc-chart");
	By userCountdownload    = By.xpath("//button[text()='Download']");
	
	By accountInput				= By.cssSelector("#accountId");
	By accountAutoComplete		= By.cssSelector("div[data-testid='autocomplete-accountId']");
	By accountAutoCompleteInput	= By.cssSelector("div[data-testid='autocomplete-accountId'] input");
	By accountListPopup			= By.id("accountId-popup");
	By accountNameOption		= By.xpath(".//*[contains(@id, 'accountId-option')]");
	By accountNameFirstOption	= By.id("accountId-option-0");
	
	By agentNameInput			= By.cssSelector("#agentName");
	By agentAutoComplete		= By.cssSelector("div[data-testid='autocomplete-agentName']");
	By agentAutoCompleteInput	= By.cssSelector("div[data-testid='autocomplete-agentName'] input");
	By agentListPopup			= By.id("agentName-popup");
	By agentNameOption			= By.xpath(".//*[contains(@id, 'agentName-option')]");
	By agentNameFirstOption		= By.id("agentName-option-0");
	
	By accountHelperText	= By.xpath(".//*[@id='accountId-helper-text'][text()='This field is required.']");
	By tabularDates             = By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[1]/span");
			
	//input tabs
	By createdFilter		= By.cssSelector("[data-testid = 'created-input'] #ringdna-select");
	By createdDateInputTab	= By.cssSelector("[data-testid='created-input'] #ringdna-select");
	By voiceMailFilter		= By.cssSelector("[data-testid = 'voicemail-input'] #ringdna-select");
	By directionFilter		= By.cssSelector("[data-testid = 'direction-input'] #ringdna-select");
	By listBoxItems			= By.cssSelector("ul[role='listbox'] li");
	
	
	By refreshButton 		= By.cssSelector("button[name= 'buttons-container-submit']");
	By loadingSpinner		= By.cssSelector(".LoadingSpinner");
	By stillWaitingError         = By.xpath("//div[@class='text-slate']/div[contains(text(),'Still Waiting')]");
	By reportPageFooter 	= By.xpath(".//*[@class='text-bold']");
	By qbrReport            = By.xpath("//div[contains(text(),'Calls with supervisor')]");
	By startdateInputTab    = By.cssSelector("input#startDate, input.start-date");
	By enddateInputTab	    = By.cssSelector("input#endDate, input.end-date");
	
	//result columns list
	By accountResult		= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[1]/span");
	By callKeyResult		= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[2]/span");
	By agentResult			= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[3]/span");
	By callerResult			= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[4]/span");
	By sfTaskUrlResult		= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[5]/span");
	By dateCreatedResult	= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[6]/span");
	By actionsUrlResult		= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[7]/span");
	By directionResult		= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[8]/span");
	By isAvailableResult	= By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[9]/span");
	By isNewResult		    = By.xpath(".//*[contains(@class,'CtorL ipoXK')]//tbody//td[10]/span");
	
	//header columns
	By sfTaskUrlResultHeader   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='salesforcetaskidorurl']");
	By actionsUrlResultHeader  = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='actions']");
	By callKeyResultHeader	   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='callkey']");
	By agentResultHeader	   = By.xpath(".//*[contains(@class,'Icon')]/..//div[contains(text(),'agent')] | .//*[contains(@class,'Icon')]/..//div[text()='from user name' or text()='name']");
	By dateCreatedResultHeader = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='date created' or text()='day']");
	By callDistDayHeader       = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='day']");
	By directionResultHeader   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='direction']");
	By isAvailableResultHeader = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='isavailable']");
	By isNewResultHeader   	   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='isnew']");
	By accountHeader		   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='account']");
	By callerHeader			   = By.xpath(".//*[contains(@class,'Icon')]/..//div[text()='caller']");

	
	// Enum for Time Frame-Call Recording Report
	public static enum reportDuration {
		Week("Last 7 days"), Month("Last 30 days"), Last90days("Last 90 days"), Annual("Last Year");

		private String value;

		reportDuration(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	public static enum CallType 
	{
	    Voicemail("Voicemail"), 
	    NonVoicemail("Non Voicemail"); 
	 
	    private String value;
	 
	    CallType(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	public static enum Direction 
	{
	    AllDirections("All Directions"), 
	    Inbound("Inbound"), 
	    Outbound("Outbound");
	 
	    private String value;
	 
		Direction(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	public String getCreatedDaysInputTabText(WebDriver driver) {
		waitUntilVisible(driver, createdFilter);
		return getElementsText(driver, createdFilter);
	}
	
	public void verifyDefaultFields(WebDriver driver) {
		assertEquals(getCreatedDaysInputTabText(driver), reportDuration.Week.getValue());
		assertEquals(getElementsText(driver, voiceMailFilter), CallType.NonVoicemail.getValue());
		assertEquals(getElementsText(driver, directionFilter), Direction.AllDirections.getValue());
		
		if(ReportBase.drivername.toString().equals("supportDriver")) {
			assertTrue(isElementVisible(driver, accountInput, 5));
		} else {
			assertFalse(isElementVisible(driver, accountInput, 5));
		}
	}
	
	public void verifyFieldsCallType(WebDriver driver) {
		waitUntilVisible(driver, voiceMailFilter);
		clickElement(driver, voiceMailFilter);
		idleWait(2);
		assertTrue(isTextPresentInList(driver, getInactiveElements(driver, listBoxItems), CallType.Voicemail.value));
		assertTrue(isTextPresentInList(driver, getInactiveElements(driver, listBoxItems), CallType.NonVoicemail.value));
		pressEscapeKey(driver);
	}
	
	public void verifyFieldsDirection(WebDriver driver) {
		waitUntilVisible(driver, directionFilter);
		clickElement(driver, directionFilter);
		idleWait(2);
		assertTrue(isTextPresentInList(driver, getInactiveElements(driver, listBoxItems), Direction.AllDirections.value));
		assertTrue(isTextPresentInList(driver, getInactiveElements(driver, listBoxItems), Direction.Inbound.value));
		assertTrue(isTextPresentInList(driver, getInactiveElements(driver, listBoxItems), Direction.Outbound.value));
		pressEscapeKey(driver);
	}
	
	public void switchToReportFrame(WebDriver driver) {
		waitUntilVisible(driver, refreshButton);
		waitUntilVisible(driver, iframeTable);
		switchToIframe(driver, iframeTable);
		isLoadingSpinnerInVisible(driver);
		
		// check column or graph is present
		assertTrue(isElementVisible(driver, accountResult, 6) || isElementVisible(driver, chartGraph, 6));
	}
	
	public void clickRefreshButton(WebDriver driver) {
		waitUntilVisible(driver, refreshButton);
		clickByJs(driver, refreshButton);
		switchToReportFrame(driver);
	}
	
	public void clickRefreshWithoutSwitchingToFrame(WebDriver driver) {
		waitUntilVisible(driver, refreshButton);
		clickByJs(driver, refreshButton);
	}

	public void isLoadingSpinnerInVisible(WebDriver driver) {
		isElementVisible(driver, loadingSpinner, 2);
		waitUntilInvisible(driver, loadingSpinner, Integer.valueOf(ReportBase.CONFIG.getProperty("qa_loading_spinner_wait_time")));
	}
	
	public void isStillWaitingErrorInVisible(WebDriver driver) {
		isListElementsVisible(driver, stillWaitingError, 5);
		waitUntilInvisible(driver, stillWaitingError, Integer.valueOf(ReportBase.CONFIG.getProperty("qa_loading_spinner_wait_time")));
	}
	
	public void downloadAndVerifyCSV(WebDriver driver, String downloadPath, String reportFileNameDownload, String endFileName) {
		//deleting existing files
		HelperFunctions.deletingExistingFiles(downloadPath, reportFileNameDownload);
		if(isElementVisible(driver, downloadLink, 10)) {
			clickElement(driver, downloadLink);
		}
		if(isElementVisible(driver, userCountdownload, 10)) {
			clickElement(driver, userCountdownload);
		}
		
		
		// downloading and verifying csv
		waitUntilVisible(driver, csvIcon);
		clickElement(driver, csvIcon);
		waitForFileToDownloadWithPartialName(downloadPath, reportFileNameDownload, ".csv");
		
		// verify file download rows should be greater than 1
		File file = getFileDownloadedWithPartialName(downloadPath, reportFileNameDownload, ".csv");
		int rowCount = HelperFunctions.bufferedReadRowsInFile(file);
		boolean flag = false;
		if(rowCount>1) {
			flag = true;
		}
		assertTrue(flag);
		
		boolean fileDownloaded = isFileDownloaded(downloadPath, reportFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");
		
	}
	
	public void multipleDownloadAndVerifyCSV(WebDriver driver, String downloadPath, String reportFileNameDownload,
			String endFileName) {
		// deleting existing files
		HelperFunctions.deletingExistingFiles(downloadPath, reportFileNameDownload);
		List<WebElement> downloadListLink = getInactiveElements(driver, downloadLink);
		for (int i = 0; i < downloadListLink.size(); i++) {
			hoverElement(driver, downloadListLink.get(i));
			waitUntilVisible(driver, downloadListLink.get(i));
			clickElement(driver, downloadListLink.get(i));
			waitUntilVisible(driver, csvIcon);
			clickElement(driver, csvIcon);
			// downloading and verifying csv
			waitForFileToDownloadWithPartialName(downloadPath, reportFileNameDownload, endFileName);
			
			// verify file download rows should be greater than 1
			File file = getFileDownloadedWithPartialName(downloadPath, reportFileNameDownload, ".csv");
			int count = HelperFunctions.bufferedReadRowsInFile(file);
			boolean flag = false;
			if(count>1) {
				flag = true;
			}
			assertTrue(flag);
			
			boolean fileDownloaded = isFileDownloaded(downloadPath, reportFileNameDownload, endFileName);
			assertTrue(fileDownloaded, "file is not downloaded");
		}
	}

	public void enterAccountName(WebDriver driver, String accountName) {
		waitUntilVisible(driver, accountInput);
		enterText(driver, accountInput, accountName);
		clickEnter(driver, accountInput);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyEmptyAccountError(WebDriver driver) {
		assertTrue(isElementVisible(driver, accountHelperText, 5));
	}

	public boolean verifyAgentExistsInDropDown(WebDriver driver, String agentName) {
		waitUntilVisible(driver, agentNameInput);
		clearAgentFilter(driver);
		enterText(driver, agentNameInput, agentName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownAgents(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, agentName);
		}
	}
	
	public List<WebElement> getListDropDownAgents(WebDriver driver) {
		waitUntilVisible(driver, agentAutoComplete);
		clickElement(driver, agentAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, agentAutoCompleteInput);
		clickElement(driver, agentAutoCompleteInput);
		if (isElementVisible(driver, agentListPopup, 5)) {
			return getElements(driver, agentNameOption);
		} else {
			return null;
		}
	}
	
	public boolean verifyAccountExistsInDropDown(WebDriver driver, String accountName) {
		waitUntilVisible(driver, accountInput);
		clearAccountFilter(driver);
		enterText(driver, accountInput, accountName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownAccount(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, accountName);
		}
	}
	
	public List<WebElement> getListDropDownAccount(WebDriver driver) {
		waitUntilVisible(driver, accountAutoComplete);
		clickElement(driver, accountAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, accountAutoCompleteInput);
		clickElement(driver, accountAutoCompleteInput);
		if (isElementVisible(driver, accountListPopup, 5)) {
			return getElements(driver, accountNameOption);
		} else {
			return null;
		}
	}

	public String getAgentNameInputValue(WebDriver driver) {
		waitUntilVisible(driver, agentNameInput);
		return getAttribue(driver, agentNameInput, ElementAttributes.value);
	}

	public void clearAccountFilter(WebDriver driver) {
		clickElement(driver, accountInput);
		clearAll(driver, accountInput);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clearAgentFilter(WebDriver driver) {
		clickElement(driver, agentNameInput);
		clearAll(driver, agentNameInput);
		dashboard.isPaceBarInvisible(driver);
	}

	
//////////////////////////////////////////////////////////////////////////////////////////////
	
	public void selectAccount(WebDriver driver, String accountName) {
		if (isElementVisible(driver, accountInput, 5)) {
			waitUntilVisible(driver, accountInput);
			clearAccountFilter(driver);
			assertEquals(reportPage.getStartTypingDropDownText(driver), "Start typing...");
			enterText(driver, accountInput, accountName);
			idleWait(2);
			waitUntilVisible(driver, accountAutoComplete);
			clickByJs(driver, accountAutoComplete);
			waitUntilVisible(driver, accountAutoCompleteInput);
			clickElement(driver, accountAutoCompleteInput);
			waitUntilVisible(driver, accountListPopup);
			clickElement(driver, accountNameFirstOption);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	public void selectUser(WebDriver driver, String agentName) {
		waitUntilVisible(driver, agentNameInput);
		clearAgentFilter(driver);
		assertEquals(reportPage.getStartTypingDropDownText(driver), "Start typing...");
		enterText(driver, agentNameInput, agentName);
		idleWait(2);
		waitUntilVisible(driver, agentAutoComplete);
		clickElement(driver, agentAutoComplete);
		waitUntilVisible(driver, agentAutoCompleteInput);
		clickElement(driver, agentAutoCompleteInput);
		waitUntilVisible(driver, agentListPopup);
		clickElement(driver, agentNameFirstOption);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectCreatedDateFilter(WebDriver driver, reportDuration duration) {
		clickAndSelectFromDropDown(driver, createdDateInputTab, listBoxItems, duration.getValue());
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectDirectionFilter(WebDriver driver, String direction) {
		clickAndSelectFromDropDown(driver, directionFilter, listBoxItems, direction);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectCallTypeFilter(WebDriver driver, CallType callType) {
		clickAndSelectFromDropDown(driver, voiceMailFilter, listBoxItems, callType.getValue());
		dashboard.isPaceBarInvisible(driver);
	}

	public void setCallerName(WebDriver driver, String callerName) {
		waitUntilVisible(driver, callerNameInput);
		clickElement(driver, callerNameInput);
		enterText(driver, callerNameInput, callerName);
	}

	
////////////////////////////////////////////////////////////////////////////////////////////////
	
/////////////////////////Click to sort///////////////////////////////////////////

	public void clickActionsUrlToSort(WebDriver driver) {
		scrollToElement(driver, actionsUrlResultHeader);
		waitUntilVisible(driver, actionsUrlResultHeader);
		clickElement(driver, actionsUrlResultHeader);
	}

	public void clickCallKeyToSort(WebDriver driver) {
		scrollToElement(driver, callKeyResultHeader);
		waitUntilVisible(driver, callKeyResultHeader);
		clickElement(driver, callKeyResultHeader);
	}
	
	public void clickDirectionToSort(WebDriver driver) {
		scrollToElement(driver, directionResultHeader);
		waitUntilVisible(driver, directionResultHeader);
		clickElement(driver, directionResultHeader);
	}
	
	public void clickIsAvailableToSort(WebDriver driver) {
		scrollToElement(driver, isAvailableResultHeader);
		waitUntilVisible(driver, isAvailableResultHeader);
		clickElement(driver, isAvailableResultHeader);
	}
	
	public void clickIsNewToSort(WebDriver driver) {
		scrollToElement(driver, isNewResultHeader);
		waitUntilVisible(driver, isNewResultHeader);
		clickElement(driver, isNewResultHeader);
	}

	public void clickSFTaskUrlToSort(WebDriver driver) {
		scrollToElement(driver, sfTaskUrlResultHeader);
		waitUntilVisible(driver, sfTaskUrlResultHeader);
		clickElement(driver, sfTaskUrlResultHeader);
	}

	public void clickAgentToSort(WebDriver driver) {
		scrollToElement(driver, agentResultHeader);
		waitUntilVisible(driver, agentResultHeader);
		clickElement(driver, agentResultHeader);
	}

	public void sortAndVerifyDateCreated(WebDriver driver, reportDuration time, String format, int index) {
		
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
			
			//verifying ascending order
			List<String> dateStringList = reportPage.getTableResultListAccToColumn(driver, index);
			List<String> dateWithYear = new ArrayList<String>();
			for(String y : dateStringList) {
				dateWithYear.add(reportPage.addYear(driver, y));
			}
			//assertTrue(HelperFunctions.verifyAscendingOrderedList(HelperFunctions.getStringListInDateFormat(format, dateWithYear)));
			
			//verifying range
			callTimeList.add(dateTimeFormatter.parse(reportPage.addYear(driver, reportPage.getFirstResultTableAccToColumn(driver, index))));
			assertTrue(callTimeList.get(0).after(previousDate) && callTimeList.get(0).before(currentDate));

			// sorting and verifying
			callTimeList.clear();
			clickDateCreatedToSort(driver);
			
			//verifying descending order
			dateStringList = reportPage.getTableResultListAccToColumn(driver, index);
			List<String> dateWithYear2 = new ArrayList<String>();
			for(String y : dateStringList) {
				dateWithYear2.add(reportPage.addYear(driver, y));
			}
			//assertTrue(HelperFunctions.verifyDescendingOrderedList(HelperFunctions.getStringListInDateFormat(format, dateWithYear2)));
			
			//verifying range
			callTimeList.add(dateTimeFormatter.parse(reportPage.addYear(driver, reportPage.getFirstResultTableAccToColumn(driver, index))));
			assertTrue(callTimeList.get(0).after(previousDate) && callTimeList.get(0).before(currentDate));

		} catch (Exception e) {
			System.out.println("Not able to parse time" + e.getMessage());
			Assert.fail("Case failed due to unparseable date format");
		}
	}

	public void clickDateCreatedToSort(WebDriver driver) {
		scrollToElement(driver, dateCreatedResultHeader);
		waitUntilVisible(driver, dateCreatedResultHeader);
		clickElement(driver, dateCreatedResultHeader);
	}
	
	public void clickCallerToSort(WebDriver driver) {
		scrollToElement(driver, callerHeader);
		waitUntilVisible(driver, callerHeader);
		clickElement(driver, callerHeader);
	}
	
	public void clickAccountToSort(WebDriver driver) {
		scrollToElement(driver, accountHeader);
		waitUntilVisible(driver, accountHeader);
		clickElement(driver, accountHeader);
	}
	
//////////////////////////Click to sort///////////////////////////////////////////
	
//////////////////////////Get list Elements///////////////////////////////////////////
	
	public List<String> getDirectionList(WebDriver driver) {
		isListElementsVisible(driver, directionResult, 5);
		return getTextListFromElements(driver, directionResult);
	}
	
	public List<String> getCallKeyList(WebDriver driver) {
		isListElementsVisible(driver, callKeyResult, 5);
		return getTextListFromElements(driver, callKeyResult);
	}
	
	public List<String> getSFTaskUrlList(WebDriver driver) {
		isListElementsVisible(driver, sfTaskUrlResult, 5);
		return getTextListFromElements(driver, sfTaskUrlResult);
	}
	
	public List<String> getDateCreatedList(WebDriver driver) {
		isListElementsVisible(driver, dateCreatedResult, 5);
		return getTextListFromElements(driver, dateCreatedResult);
	}
	
	public List<String> getActionsUrlList(WebDriver driver) {
		isListElementsVisible(driver, actionsUrlResult, 5);
		return getTextListFromElements(driver, actionsUrlResult);
	}
	
	public List<String> getIsAvailableList(WebDriver driver) {
		isListElementsVisible(driver, isAvailableResult, 5);
		return getTextListFromElements(driver, isAvailableResult);
	}
	
	public List<String> getIsNewList(WebDriver driver) {
		isListElementsVisible(driver, isNewResult, 5);
		return getTextListFromElements(driver, isNewResult);
	}

	public List<String> getCallerNameList(WebDriver driver) {
		isListElementsVisible(driver, callerResult, 5);
		return getTextListFromElements(driver, callerResult);
	}

	public List<String> getAgentNameList(WebDriver driver) {
		isListElementsVisible(driver, agentResult, 5);
		return getTextListFromElements(driver, agentResult);
	}
//////////////////////////Get list Elements///////////////////////////////////////////
	
//////////////////////////////////////////////////////////////////////////////////////
	
	public String getAccountName(WebDriver driver) {
		isListElementsVisible(driver, accountResult, 5);
		List<String> resultList = getTextListFromElements(driver, accountResult);
		return resultList.get(0);
	}
	
	public String getDirection(WebDriver driver) {
		isListElementsVisible(driver, directionResult, 5);
		List<WebElement> resultList = getInactiveElements(driver, directionResult);
		return resultList.get(0).getText();
	}

	public String getSFTaskURL(WebDriver driver) {
		isListElementsVisible(driver, sfTaskUrlResult, 5);
		List<WebElement> resultList = getInactiveElements(driver, sfTaskUrlResult);
		return resultList.get(0).getText();
	}
	
	public String getURL(WebDriver driver) {
		isListElementsVisible(driver, actionsUrlResult, 5);
		List<WebElement> resultList = getInactiveElements(driver, actionsUrlResult);
		return resultList.get(0).getText();
	}

	public String getCallerName(WebDriver driver) {
		isListElementsVisible(driver, callerResult, 5);
		List<String> resultList = getTextListFromElements(driver, callerResult);
		return resultList.get(0);
	}

	public String getAgentName(WebDriver driver) {
		isListElementsVisible(driver, agentResult, 5);
		List<String> resultList = getTextListFromElements(driver, agentResult);
		return resultList.get(0);
	}
	
	public String getDateCreated(WebDriver driver) {
		isListElementsVisible(driver, dateCreatedResult, 5);
		List<String> resultList = getTextListFromElements(driver, dateCreatedResult);
		return resultList.get(0);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void clickAndVerifyActionUrl(WebDriver driver) {
		isListElementsVisible(driver, actionsUrlResult, 5);
		Actions actions = new Actions(driver);
		WebElement openUrl = getInactiveElements(driver, actionsUrlResult).get(0);

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
	}
	
	public void clickSFTaskUrl(WebDriver driver, int index){
		isListElementsVisible(driver, sfTaskUrlResult, 5);
		clickElement(driver, getInactiveElements(driver, sfTaskUrlResult).get(index));
		switchToTab(driver, getTabCount(driver));
		dashboard.isPaceBarInvisible(driver);
		sfTaskDetailPage.closeLightningDialogueBox(driver);
	}
	
	public void clickAndVerifySFTaskUrl(WebDriver driver, String user, String direction, String identifierId){
		int i = 0;
		isListElementsVisible(driver, sfTaskUrlResult, 5);
		int length = getSFTaskUrlList(driver).size();
		while (i < length) {
			Actions actions = new Actions(driver);
			WebElement openUrl = getInactiveElements(driver, sfTaskUrlResult).get(i);
			
			//opening url in new tab and switching
			actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
			switchToTab(driver, getTabCount(driver));
			waitForPageLoaded(driver);
			dashboard.isPaceBarInvisible(driver);
			sfTaskDetailPage.closeLightningDialogueBox(driver);
			if (!sfTaskDetailPage.isAssignedToUserVisible(driver)) {
				closeTab(driver);
				switchToTab(driver, 2);
				switchToReportFrame(driver);
				i++;
			} else
				break;
		}

		assertTrue(sfTaskDetailPage.getCallRecordingHrefAttribute(driver).contains("rc".concat(identifierId)));
		assertTrue(sfTaskDetailPage.getAssignedToUser(driver).equalsIgnoreCase(user));
		assertEquals(sfTaskDetailPage.getCallDirection(driver), direction);
		closeTab(driver);
		switchToTab(driver, 2);
	}
	
	public void verifySFDCVoiceMailFilter(WebDriver driver, Enum<?> headerType){
		int i = 0;
		isListElementsVisible(driver, sfTaskUrlResult, 5);
		int length = getSFTaskUrlList(driver).size();
		while (i < length) {
			Actions actions = new Actions(driver);
			WebElement openUrl = getInactiveElements(driver, sfTaskUrlResult).get(i);
			
			//opening url in new tab and switching
			actions.keyDown(Keys.CONTROL).click(openUrl).keyUp(Keys.CONTROL).build().perform();
			switchToTab(driver, getTabCount(driver));
			waitForPageLoaded(driver);
			dashboard.isPaceBarInvisible(driver);
			sfTaskDetailPage.closeLightningDialogueBox(driver);
			if (!sfTaskDetailPage.isAssignedToUserVisible(driver)) {
				closeTab(driver);
				switchToTab(driver, 2);
				switchToReportFrame(driver);
				i++;
			} else
				break;
		}
		
		CallType callTypeHeader = (CallType) headerType;
		
		switch (callTypeHeader) {
		case Voicemail:
			sfTaskDetailPage.verifyVoicemailCreatedActivity(driver);
			break;
		case NonVoicemail:
			sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driver);
			break;
		default:
			break;
		}
		closeTab(driver);
		switchToTab(driver, 2);
	}
	
	/**
	 * @Desc : refresh button for negative cases
	 * @param driver
	 */
	public void clickRefreshButtonWithoutAssertion(WebDriver driver) {
		waitUntilVisible(driver, refreshButton);
		clickByJs(driver, refreshButton);
		waitUntilVisible(driver, refreshButton);
		waitUntilVisible(driver, iframeTable);
		switchToIframe(driver, iframeTable);
		isLoadingSpinnerInVisible(driver);
	}
}
