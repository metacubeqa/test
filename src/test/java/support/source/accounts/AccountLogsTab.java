/**
 * 
 */
package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class AccountLogsTab extends SeleniumBase{
	Dashboard dashboard = new Dashboard();
	
	By accountLogsTab				= By.cssSelector("a[data-tab='logs']");
	By accountLogsHeading			= By.xpath(".//h1[text() = 'Account Logs']");
	By userDrpDwn					= By.cssSelector(".selectize-control.selector");
	By userTextBox					= By.cssSelector(".selectize-control.selector input");
	By userDrpDwnOptions			= By.cssSelector(".single.selector .selectize-dropdown-content div");
	By categoryDrpDwn				= By.cssSelector(".selectize-control.category");
	By categoryTextBox				= By.cssSelector(".selectize-control.category input");
	By categoryDrpDwnOptions		= By.cssSelector(".single.category .selectize-dropdown-content .option");
	By dateRangeDrpDwn				= By.cssSelector(".selectize-control.range");
	By dateRangeTextBox				= By.cssSelector(".selectize-control.range input");
	By dateRangeDrpDwnOptions		= By.cssSelector(".single.range .selectize-dropdown-content .option");
	By searchButton					= By.cssSelector("button.search");
	By downloadLink                 = By.cssSelector(".glyphicon.glyphicon-download-alt");

	By logRecords					= By.cssSelector("table tbody tr");
	By logRecordDateRange			= By.cssSelector(".timestamp");
	By logRecordcategory			= By.cssSelector("td:nth-child(2)");
	By logRecordsAdditional			= By.cssSelector("td:nth-child(3)");
	By logRecordCallKey				= By.cssSelector("td:nth-child(3) [href*=calls]");
	By logRecordUser				= By.cssSelector("td:nth-child(4) [href*=users]");
	By logRecordsOldValue			= By.cssSelector("td:nth-child(5)");
	By logRecordsNewValue			= By.cssSelector("td:nth-child(6)");
	By retryButton					= By.cssSelector("[data-action='retry-error']");
	By toastMessage					= By.className("toast-message");
	By logReferenceID				= By.cssSelector("td:nth-child(7) [href*=calls]");
	By noLogsError                  = By.xpath("//span[text()='No logs found.']");
	
	public enum BlockNumberValue{
		Blocked,
		Unblocked
	}
	
	public void navigateToAccountLogTab(WebDriver driver){
		dashboard.isPaceBarInvisible(driver);
		clickElement(driver, accountLogsTab);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, accountLogsHeading);
	}
	
	public void searchAccountLogs(WebDriver driver, String userName, String category, String dateRange){
		enterTextAndSelectFromDropDown(driver, userTextBox, userTextBox, userDrpDwnOptions, userName);
		clickAndSelectFromDropDown(driver, categoryTextBox, categoryDrpDwnOptions, category);
		clickAndSelectFromDropDown(driver, dateRangeTextBox, dateRangeDrpDwnOptions, dateRange);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyRecordingLogsPresent(WebDriver driver, int logIndex, String category, String dateRange, String userName, String logMessage){
		List<WebElement> accountLogsRecords	 = getElements(driver, logRecords);
		WebElement accountLogsRecord = accountLogsRecords.get(logIndex);

		assertTrue(accountLogsRecord.findElement(logRecordcategory).getText().equals(category));
		assertTrue(getElementsText(driver, getElements(driver, logRecordsAdditional).get(0)).contains(logMessage));
		assertTrue(accountLogsRecord.findElement(logRecordUser).getText().equals(userName));;
		assertTrue(accountLogsRecord.findElement(logRecordDateRange).getText().contains(dateRange));
	}
	
	public int getRecordingLogIndex(WebDriver driver, String callKey) {
		int i = 0;
		List<WebElement> accountLogsRecords	 = getElements(driver, logRecords);
		for (WebElement accountLogsRecord : accountLogsRecords) {
			if(accountLogsRecord.findElement(logReferenceID).getText().equals(callKey)){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public void verifyRecordingLogsPresent(WebDriver driver, int index, String dateRange, String category, String additionalDetails, String userName, String oldValue, String newValue, String refrence){
		List<WebElement> accountLogsRecords	 = getElements(driver, logRecords);
		WebElement accountLogsRecord = accountLogsRecords.get(index);
		if (dateRange!=null) verifylogTime(driver, accountLogsRecord.findElement(logRecordDateRange).getText(), dateRange);
		if (category!=null) assertTrue(accountLogsRecord.findElement(logRecordcategory).getText().equals(category));
		if (additionalDetails!=null) assertTrue(accountLogsRecord.findElement(logRecordsAdditional).getText().equals(additionalDetails));
		if (userName!=null) assertTrue(accountLogsRecord.findElement(logRecordUser).getText().equals(userName));
		if (oldValue!=null) assertTrue(accountLogsRecord.findElement(logRecordsOldValue).getText().contains(oldValue));
		if (newValue!=null) assertTrue(accountLogsRecord.findElement(logRecordsNewValue).getText().contains(newValue));
		if (refrence!=null) assertTrue(accountLogsRecord.findElement(logReferenceID).getText().contains(refrence));
	}
	
	public void verifylogTime(WebDriver driver, String actualDatetime, String expectedDateTime) {
		String dateFormat = "MM/dd/yyyy hh:mm a";
		Date startDate = HelperFunctions.getDateTimeInDateFormat(expectedDateTime, dateFormat);
		Date endDate = HelperFunctions.getDateTimeInDateFormat(actualDatetime, dateFormat);

		// finding diff in minutes and updating new time
		int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(startDate, endDate, dateFormat);
		assertTrue(diffInMinutes >=0 && diffInMinutes <=2);
	}
	
	public void verifyBlockedNumbersLogs(WebDriver driver, String category, String userName, String blockedNumber, Enum<?> type) {
		assertTrue(isListContainsText(driver, getElements(driver, logRecordcategory), category));
		assertTrue(isListContainsText(driver, getElements(driver, logRecordUser), userName));
		assertTrue(getElementsText(driver, getElements(driver, logRecordsAdditional).get(0)).contains(blockedNumber));
		assertEquals(getElementsText(driver, getElements(driver, logRecordsNewValue).get(0)), type.name());
	}
	
	public void verifyEmergencyCallFlowLogs(WebDriver driver, String category, String userName, String date) {
		assertTrue(isListContainsText(driver, getElements(driver, logRecordcategory), category));
		assertTrue(isListContainsText(driver, getElements(driver, logRecordUser), userName));
		assertTrue(getElementsText(driver, getElements(driver, logRecordDateRange).get(1)).contains(date));
		assertFalse(getTextListFromElements(driver, logRecordDateRange).isEmpty());
	}
	
	public void clickRetryButton(WebDriver driver, int logIndex) {
		List<WebElement> accountLogsRecords	 = getElements(driver, logRecords);
		String callID = accountLogsRecords.get(logIndex).findElement(logRecordsNewValue).getText();
		getElements(driver, retryButton).get(logIndex).click();
		waitUntilVisible(driver, toastMessage);
		waitUntilTextPresent(driver, getElements(driver, toastMessage).get(0), "Salesforce Resync for " + callID + " enqueued.");
		waitUntilTextNotPresent(driver, toastMessage, "Salesforce Resync for " + callID + " enqueued.");
		waitUntilTextPresent(driver, getElements(driver, toastMessage).get(0), "Salesforce Resync successful for " + callID);
		waitUntilInvisible(driver, toastMessage);
	}
	
	public boolean isRetryBtnEnabled(WebDriver driver, int logIndex) {
		return isAttributePresent(driver, getElements(driver, retryButton).get(logIndex).findElement(By.xpath("..")), ElementAttributes.disabled.displayName());
	}
	
	/**
	 * @param driver
	 * @return no logs error visible
	 */
	public boolean isNoLogsFoundVisible(WebDriver driver) {
		return isElementVisible(driver, noLogsError, 6);
	}
	
	/**
	 * @param driver
	 * @return log record visible
	 */
	public boolean isLogRecordsVisible(WebDriver driver) {
		return isListElementsVisible(driver, logRecords, 6);
	}
	
	/**
	 * @param driver
	 * @param downloadPath
	 * @param reportFileNameDownload
	 * @param endFileName
	 */
	public void downloadAndVerifyCSV(WebDriver driver, String downloadPath, String reportFileNameDownload, String endFileName) {
		//deleting existing files
		HelperFunctions.deletingExistingFiles(downloadPath, reportFileNameDownload);
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
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
	
	/**
	 * @param driver
	 * @param category
	 * @param userName
	 */
	public void verifySalesforceErrorLogs(WebDriver driver, String category, String userName) {
		assertTrue(isListContainsText(driver, getElements(driver, logRecordcategory), category));
		assertTrue(isListContainsText(driver, getElements(driver, logRecordUser), userName));
		assertFalse(getTextListFromElements(driver, logRecordDateRange).isEmpty());
	}
}