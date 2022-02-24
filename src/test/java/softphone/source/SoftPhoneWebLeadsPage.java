/**
 * 
 */
package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import softphone.source.salesforce.ContactDetailPage;
import utility.HelperFunctions;
import base.SeleniumBase;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneWebLeadsPage extends SeleniumBase{
	
	ContactDetailPage contactDetailPage = new ContactDetailPage();
	
	By webLeadsIcon 				= By.cssSelector(".webLeads");
	By webLeadsHeading 				= By.xpath(".//h1[text()='Hot Leads']");
	By webLeadsName					= By.cssSelector("#web-leads .name.canOpen");
	By webLeadPhone					= By.cssSelector(".phone-number");
	By webLeadTitle					= By.cssSelector(".title");
	By webLeadCompany				= By.cssSelector(".company");
	By webLeadEmail					= By.cssSelector(".emailAddress");
	By webLeadDescription			= By.cssSelector(".description");
	By fullDescriptionIcon			= By.cssSelector(".description-icon");
	By desciptionToolTip			= By.cssSelector(".tooltip-inner");
	By creationDate					= By.cssSelector(".date");
	By webLeadPhoneImage			= By.cssSelector("[src='images/contact-btn-call-v2.svg']");
	By webLeadMessageImage			= By.cssSelector("[src='images/contact-btn-sms-v2.svg']");
	By webLeadEmailImage			= By.cssSelector("[src='images/contact-btn-email-v2.svg']");
	By webLeadsNotificationCount	= By.id("webLeadsCount");
	By webLeadsSearchBox			= By.cssSelector("input.wed-lead-name");
	By webLeadsSearchBtn			= By.cssSelector("button[data-action='search-web-leads']");
	By webLeadsSourceDrpDwn			= By.className("filter-source");
	By webLeadsSortDrpDwn			= By.className("filter-updated");
	By noWebLeadsLabel				= By.xpath(".//*[@id='no-search-results' and text()='No Hot Leads']");
	By webLeadCommentLabel			= By.xpath(".//div[@class='crm-activities']//*[@class='activity-item task'][1]//strong[text()='Hot Lead Comment:']");
	By webLeadsComment				= By.cssSelector(".crm-activities .activity-item.task:nth-child(1) .description");
	String openWebLeadDetailsLink	= ".//*[@data-action='view-contact' and text() ='$$WebLeadName$$']/preceding-sibling::button[@data-action=\"view-crm\"]";
	String webLeadRow				= ".//*[@id='web-leads']//*[@data-action='view-contact' and text() ='$$WebLeadName$$']//ancestor::div[contains(@class,'contact-item')]";
	
	public void navigateToWebLeadsPage(WebDriver driver){
		if(!isElementVisible(driver, webLeadsHeading, 0)){
			clickElement(driver, webLeadsIcon);
			waitUntilVisible(driver, webLeadsHeading);
		}
	}
	
	public boolean isWebLeadsSectionVisible(WebDriver driver){
		return isElementVisible(driver, webLeadsIcon, 5);
	}
	
	public List<WebElement> getWebLeadList(WebDriver driver){
		return getElements(driver, webLeadsName);
	}
	
	public int getWebLeadIndex(WebDriver driver, String webLeadsName){
		List<WebElement> webLeadsList = getWebLeadList(driver);
		int i = 0;
		for (WebElement webLead : webLeadsList) {
			if(webLead.getText().equals(webLeadsName)){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public List<String> getWebLeadsNameList(WebDriver driver){
		return getTextListFromElements(driver, webLeadsName);
	}
	
	public void verifyWebLeadExists(WebDriver driver, String webLeadName){
		isTextPresentInList(driver, getWebLeadList(driver), webLeadName);
	}
	
	public void verifyWebLeadSearch(WebDriver driver, String webLeadName) {
		List<WebElement> webLeadsList = getWebLeadList(driver);
		for (WebElement webLead : webLeadsList) {
			assertTrue(webLead.getText().contains(webLeadName));
		}
	}
	
	public void verifyWebLeadsSourceSearch(WebDriver driver, String leadSource){
		List<WebElement> webLeadsList = getWebLeadList(driver);
		for (WebElement webLead : webLeadsList) {
			webLead.click();
			switchToTab(driver, getTabCount(driver));
			assertTrue(contactDetailPage.getLeadSource(driver).equals(leadSource));
			closeTab(driver);
			switchToTab(driver, 1);
		}
	}

	public int getWebLeadsNotificationCounts(WebDriver driver){
		try{
			return Integer.parseInt(getElementsText(driver, webLeadsNotificationCount));
		}catch(Exception e){
			return 0;
		}
	}
	
	public void verifyWebLeadsNotificationCounts(WebDriver driver, int count){
		waitUntilTextPresent(driver, webLeadsNotificationCount, String.valueOf(count));
	}
	
	public void VerifyWebLeadsNotificationCoutntsIncreased(WebDriver driver, int oldWebLeadsCounts){
		waitUntilTextPresent(driver, webLeadsNotificationCount, Integer.toString(oldWebLeadsCounts + 1));
	}
	
	public void searchWebLeads(WebDriver driver, String searchString){
		enterText(driver, webLeadsSearchBox, searchString);
		clickElement(driver, webLeadsSearchBtn);
	}
	
	public void selectLeadSource(WebDriver driver, String option){
		selectFromDropdown(driver, webLeadsSourceDrpDwn, SelectTypes.visibleText, option);
	}
	
	public void selectCreationDate(WebDriver driver, String creationDateType){
		selectFromDropdown(driver, webLeadsSortDrpDwn, SelectTypes.visibleText, creationDateType);
	}
	
	public void noWebLeadMessage(WebDriver driver) {
		waitUntilVisible(driver, noWebLeadsLabel);
	}
	
	public void openWebLeadsContactDetail(WebDriver driver, String webLeadName) {
		By webLeadOpenIcon = By.xpath(openWebLeadDetailsLink.replace("$$WebLeadName$$", webLeadName));
		waitUntilVisible(driver, webLeadOpenIcon);
		clickElement(driver, webLeadOpenIcon);
	}
	
	public void verifyWebLeadDescription(WebDriver driver, String desciption) {
		waitUntilVisible(driver, webLeadCommentLabel);
		assertEquals(getElementsText(driver, webLeadsComment), desciption);
	}
	
	public WebElement getWebLeadRow(WebDriver driver, String webLeadName) {
		By webLeadEntry = By.xpath(webLeadRow.replace("$$WebLeadName$$", webLeadName));
		waitUntilVisible(driver, webLeadEntry);
		return findElement(driver, webLeadEntry);
	}
	
	public String getWebLeadTitle(WebDriver driver, String webLeadName) {
		return getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadTitle)).trim();
	}
	
	public String getWebLeadCompany(WebDriver driver, String webLeadName) {
		return getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadCompany)).trim();
	}
	
	public String getWebLeadPhone(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhoneImage));
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadMessageImage));
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhone))).trim();
	}
	
	public String getWebLeadEmail(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadEmailImage));
		return getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadEmail)).trim();
	}
	
	public String getWebLeadDescription(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadDescription));
		return getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadDescription)).trim();
	}
	
	public File getWebLeadsDescriptionImage(WebDriver driver, String webLeadName) {
		return HelperFunctions.captureElementScreenShot(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadDescription));
	}
	
	public String getWebLeadsDescriptionToolTip(WebDriver driver, String webLeadName) {
		hoverElement(driver, getWebLeadRow(driver, webLeadName).findElement(fullDescriptionIcon));
		return getElementsText(driver, desciptionToolTip).trim();
	}
	
	public String getWebLeadCreateionDate(WebDriver driver, String webLeadName) {
		return getElementsText(driver, getWebLeadRow(driver, webLeadName).findElement(creationDate)).trim();
	}
	
	public void clickWebLeadCallBackBtn(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhoneImage));
		clickElement(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhoneImage));
	}
	
	public void clickWebLeadPhoneNumber(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhone));
		clickElement(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadPhone));
	}
	
	public void clickWebLeadMessageBtn(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadMessageImage));
		clickElement(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadMessageImage));
	}
	
	public void clickWebLeadEmailBtn(WebDriver driver, String webLeadName) {
		waitUntilVisible(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadEmailImage));
		clickElement(driver, getWebLeadRow(driver, webLeadName).findElement(webLeadEmailImage));
	}
}
