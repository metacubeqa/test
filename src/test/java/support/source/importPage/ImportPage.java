package support.source.importPage;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import utility.HelperFunctions;

public class ImportPage extends SeleniumBase{

	By chooseFile 		  = By.cssSelector(".glyphicon-folder-open");
	By uploadBtn 		  = By.cssSelector(".upload");
	By toastMsg 		  = By.cssSelector(".toast-message");
	By startImportingBtn  = By.cssSelector(".btn-info.import");
	
	String importStatus   		 = ".//*[text()='$number$']/ancestor::div//div[contains(@class,'status')]";
	String userImportDescription = ".//*[text()='$sfUserId$']/parent::tr//td[contains(@class,'userDescription')]";

	String callImportStatus		  = ".//*[text()='$number$']/ancestor::div//div[contains(@class,'type') and text()='Call']/following-sibling::div[contains(@class,'status') and text()='Imported']";
	String recordingImportStatus  = ".//*[text()='$number$']/ancestor::div//div[contains(@class,'type') and text()='Call']/following-sibling::div[contains(@class,'status') and text()='Imported']";
	
	public void clickChooseFile(WebDriver driver) {
		waitUntilVisible(driver, chooseFile);
		clickElement(driver, chooseFile);
	}
	
	public void clickUploadBtn(WebDriver driver) {
		waitUntilVisible(driver, uploadBtn);
		clickElement(driver, uploadBtn);
	}
	
	public void verifyImportCompletedMsgVisibleForNumbers(WebDriver driver) {
		assertTrue(isElementVisible(driver, toastMsg, 2));
		assertTrue(getElementsText(driver, toastMsg).equals("Import completed"));
		waitUntilInvisible(driver, toastMsg);
	}
	
	public void numberImported(WebDriver driver, String number) {
		By numberImportStatus = By.xpath(importStatus.replace("$number$", number));
		assertTrue(getElementsText(driver, numberImportStatus).equals("Imported"));
	}
	
	public void verifyNumberCallTypeImported(WebDriver driver, String number) {
		By numberImportStatus = By.xpath(callImportStatus.replace("$number$", number));
		assertTrue(isElementVisible(driver, numberImportStatus, 5));
	}
	
	public void verifyNumberRecordingTypeImported(WebDriver driver, String number) {
		By numberImportStatus = By.xpath(recordingImportStatus.replace("$number$", number));
		assertTrue(isElementVisible(driver, numberImportStatus, 5));
	}
	
	public void uploadOutBoundNumber(WebDriver driver, String pathOFCSV, String number) {
		clickChooseFile(driver);
		idleWait(2);
		HelperFunctions.uploadCSV(pathOFCSV);
		idleWait(5);
		clickUploadBtn(driver);
		verifyImportCompletedMsgVisibleForNumbers(driver);
		numberImported(driver, number);
	}
	
	public void uploadBlockedNumber(WebDriver driver, String pathOFCSV, String number) {
		clickChooseFile(driver);
		idleWait(2);
		HelperFunctions.uploadCSV(pathOFCSV);
		idleWait(5);
		clickUploadBtn(driver);
		verifyImportCompletedMsgVisibleForNumbers(driver);
		verifyNumberCallTypeImported(driver, number);
		verifyNumberRecordingTypeImported(driver, number);
	}
	
	public void clickStartImportingBtn(WebDriver driver) {
		waitUntilVisible(driver, startImportingBtn);
		clickElement(driver, startImportingBtn);
	}
	
	public void importCompletedMsgVisibleForUsers(WebDriver driver) {
		assertTrue(isElementVisible(driver, toastMsg, 5));
		assertTrue(getElementsText(driver, toastMsg).equals("The import has completed."));
		waitUntilInvisible(driver, toastMsg);
	}
	
	public void userImported(WebDriver driver, String sfUserId) {
		By userImportDesc = By.xpath(userImportDescription.replace("$sfUserId$", sfUserId));
		assertTrue(getElementsText(driver, userImportDesc).equals("The user was created."));
	}
	
	public void uploadUser(WebDriver driver, String pathOFCSV, String sfUserId) {
		clickChooseFile(driver);
		idleWait(2);
		HelperFunctions.uploadCSV(pathOFCSV);
		idleWait(5);
		clickUploadBtn(driver);
		clickStartImportingBtn(driver);
		importCompletedMsgVisibleForUsers(driver);
		userImported(driver, sfUserId);
	}
}
