package softphone.source;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import base.SeleniumBase;

public class ReportThisCallPage extends SeleniumBase{
	
	By reportThisCallLink 			= By.xpath(".//*[text()='Report this call']");
	By callReportCategorFilterList  = By.xpath(".//select[@id= 'categories']/option[not(@disabled)]");
	By callReportHeader 			= By.xpath("//h4[text()='Call Report']");
	By callReportRatingHeader		= By.xpath("//h4[text()='Rating']");
	By callReportCategoryHeader 	= By.xpath("//h5[contains(text(),'Category')]");
	By callReportCategoryRequired 	= By.xpath("//h5[contains(text(),'Category')]//span[@class='required']");
	By callReportInactiveRating		= By.cssSelector(".reportContent .inactive[style='display: inline;']");
	By callReportActiveRating		= By.cssSelector(".reportContent .active[style='display: inline;']");
	By callReportCategorySelect 	= By.id("categories");
	By callReportNoteHeader	 		= By.xpath("//h5[contains(text(),'Note')]");
	By callReportNoteTextbox  		= By.id("callReportNotes");
	By callReportRequiredText 		= By.className("required-legend");
	By callReportSendButton			= By.cssSelector("[data-testid='call-report-modal.send']");
	By callReportCloseButton		= By.xpath("//button[text()='Close']");
	By callReportStarImages			= By.xpath("//div[@class='reportContent']//img[contains(@src,'images/btn-star')]");
	By callReportSuccessMsg     	= By.xpath(".//*[text()='Call report sent successfully.']");
	
	public void verifyReportThisCallNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, reportThisCallLink);
	}
	
	public void clickReportThisCallLink(WebDriver driver) {
		waitUntilVisible(driver, reportThisCallLink);
		clickElement(driver, reportThisCallLink);
	}
	
	public List<String> getCategoryFilterList(WebDriver driver){
		clickReportThisCallLink(driver);
		waitUntilVisible(driver, callReportCategorFilterList);
		return getTextListFromElements(driver, callReportCategorFilterList);
	}
	
	public String getSelectedCategory(WebDriver driver) {
		Select select = new Select(findElement(driver, callReportCategorySelect));
		return getElementsText(driver, select.getFirstSelectedOption());
	}
	
	public void selectRating(WebDriver driver, int rating) {
		clickElement(driver, getElements(driver, callReportStarImages).get(rating-1));
	}

	public int getSelectedRating(WebDriver driver) {
		return getElements(driver, callReportActiveRating).size();
	}

	public void selectCategory(WebDriver driver, String categoryName) {
		Select select = new Select(findElement(driver, callReportCategorySelect));
		select.selectByVisibleText(categoryName);
	}
	
	public void enterCallReportNote(WebDriver driver, String note) {
		enterText(driver, callReportNoteTextbox, note);
	}
	
	public void giveCallReport(WebDriver driver, int rating, String categoryName, String note) {
		clickReportThisCallLink(driver);
		selectRating(driver, rating);
		selectCategory(driver, categoryName);
		enterCallReportNote(driver, note);
		clickElement(driver, callReportSendButton);
		waitUntilVisible(driver, callReportSuccessMsg);
		String successMsg = getElementsText(driver, callReportSuccessMsg);
		assertEquals(successMsg.contains("Call report sent successfully"), true);
		clickElement(driver, callReportCloseButton);
		waitUntilInvisible(driver, callReportSuccessMsg);
	}
	
}
