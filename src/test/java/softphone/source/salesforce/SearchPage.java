package softphone.source.salesforce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;

/**
 * @author Akshita
 *
 */

public class SearchPage extends SeleniumBase{
	
	
	By searchFieldGlobal 		= By.cssSelector("[title='Search...'][type='text'],#phSearchInput");
	By searchButtonGlobal 		= By.id("phSearchButton");
	By searchAgainField 		= By.id("secondSearchText");
	By searchAgainButton 		= By.id("secondSearchButton");
	By contactList 				= By.xpath("//table[@summary='My Contacts']");
	By leadList 				= By.xpath("//table[@summary='My Leads']");
	By accountList 				= By.xpath("//table[@summary='Accounts']");
	
	String contactNameLink		= "//div[@id='Contact_body']//th[@class=' dataCell  ']//a[text()='$$ContactName$$'] | //a[contains(@class, 'outputLookupLink') and text()='$$ContactName$$']";
	By contactPhoneList			= By.xpath("//div[@id='Contact_body']//a[@class='ringdna-phone'] | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']");
	By contactPhoneListParent	= By.xpath("//div[@id='Contact_body']//a[@class='ringdna-phone']/parent::td | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']/parent::span");
	String contactPhoneLink		= "//div[@id='Contact_body']//a[@class='ringdna-phone']/span[text()='$$ContactPhone$$']";
	By contactMessageList		= By.xpath("//div[@id='Contact_body']//a[@class='ringdna-sms']");
	
	String leadNameLink			= "//div[@id='Lead_body']//th[@class=' dataCell  ']//a[text()='$$LeadName$$'] | //a[contains(@class, 'outputLookupLink') and text()='$$LeadName$$']";
	By leadPhoneList			= By.xpath("//div[@id='Lead_body']//a[@class='ringdna-phone'] | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']");
	By leadPhoneListParent		= By.xpath("//div[@id='Lead_body']//a[@class='ringdna-phone']/parent::td | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']/parent::span");
	String leadPhoneLink		= "//div[@id='Lead_body']//a[@class='ringdna-phone']/span[text()='$$LeadPhone$$']";
	By leadMessageList			= By.xpath("//div[@id='Lead_body']//a[@class='ringdna-sms']");
	
	String accountNameLink		= "//div[@id='Account_body']//th[@class=' dataCell  ']//a[text()='$$AccountName$$'] | //a[contains(@class, 'outputLookupLink') and text()='$$AccountName$$']";
	By accountPhoneList			= By.xpath("//div[@id='Account_body']//a[@class='ringdna-phone'] | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']");
	By accountPhoneListParent	= By.xpath("//div[@id='Account_body']//a[@class='ringdna-phone']/parent::td | //td//span[contains(@class,'forceOutputPhone')]//a[@class='ringdna-phone']/parent::span");
	String accountPhoneLink		= "//div[@id='Account_body']//a[@class='ringdna-phone']/span[text()='$$AccountPhone$$']";
	By accountMessageList		= By.xpath("//div[@id='Account_body']//a[@class='ringdna-sms']");
	
	String opportunityNameNameLink		= "//div[@id='Opportunity_body']//th[@class=' dataCell  ']//a[text()='$$OpportunityName$$'] | //a[contains(@class, 'outputLookupLink') and text()='$$OpportunityName$$']";
	
	public void enterGlobalSearchText(WebDriver driver, String searchText) {
		waitUntilVisible(driver, searchFieldGlobal);
	    enterText(driver, searchFieldGlobal, searchText);
	}
	
	public void enterSearchTextandSelect(WebDriver driver, String searchText) {
		waitForPageLoaded(driver);
		waitUntilVisible(driver, searchFieldGlobal);
		scrollIntoView(driver, searchFieldGlobal );
		enterTextandSelect(driver, searchFieldGlobal, searchText);
	}
	
	public void clickGlobalSearchButton(WebDriver driver) {
		waitUntilVisible(driver, searchButtonGlobal);
	    clickElement(driver, searchButtonGlobal);
	}
	
	public void enterSearchAgainText(WebDriver driver, String searchText) {
		waitUntilVisible(driver, searchAgainField);
		enterText(driver, searchAgainField, searchText);
	}
	
	public void clickSearchAgainButton(WebDriver driver) {
		waitUntilVisible(driver, searchAgainButton);
	    clickElement(driver, searchAgainButton);
	}
	  
	public Boolean isSearchedContactVisible(WebDriver driver){
		return isElementVisible(driver, contactPhoneList, 5);
	}
	
	public Boolean isSearchedLeadVisible(WebDriver driver){
		return isElementVisible(driver, leadPhoneList, 5);
	}
	
	public Boolean isSearchedAccountVisible(WebDriver driver){
		return isElementVisible(driver, accountPhoneList, 5);
	}
	
	public void clickContactNameLink(WebDriver driver, String searchText) {
		By contactName = By.xpath(contactNameLink.replace("$$ContactName$$",searchText));
		waitUntilVisible(driver, contactName);
		clickByJs(driver, contactName);
		waitForPageLoaded(driver);
	}
	
	public void clickWhenContactNameLinkVisible(WebDriver driver, String searchText) {
		By contactName = By.xpath(contactNameLink.replace("$$ContactName$$",searchText));
		for (int i = 0; i < 5; i++) {
			if(isElementVisible(driver, contactName, 5)){
				break;
			}
			refreshCurrentDocument(driver);
		}
		clickContactNameLink(driver, searchText);
	}
	
	public String getSFDCContactPhone(WebDriver driver) {
		waitUntilVisible(driver, contactPhoneListParent);
		return getElementsText(driver, contactPhoneListParent).trim();
	}
	
	public void clickContactPhoneLink(WebDriver driver) {	    
		waitUntilVisible(driver, contactPhoneList);		
		clickByJs(driver, contactPhoneList);
	}
	
	public void clickLeadNameLink(WebDriver driver, String searchText) {
	    By leadName = By.xpath(leadNameLink.replace("$$LeadName$$",searchText));
		waitUntilVisible(driver, leadName);
		clickByJs(driver, leadName);
		waitForPageLoaded(driver);
	}
	
	public void clickWhenLeadtNameLinkVisible(WebDriver driver, String searchText) {
		 By leadName = By.xpath(leadNameLink.replace("$$LeadName$$",searchText));
		for (int i = 0; i < 5; i++) {
			if(isElementVisible(driver, leadName, 5)){
				break;
			}
			refreshCurrentDocument(driver);
		}
		clickLeadNameLink(driver, searchText);
	}
	
	public String getSFDCLeadPhone(WebDriver driver) {
		waitUntilVisible(driver, leadPhoneListParent);
		return getElementsText(driver, leadPhoneListParent).trim();
	}
	
	public void clickLeadPhoneLink(WebDriver driver) {
		waitUntilVisible(driver, leadPhoneList);		
		clickByJs(driver, leadPhoneList);
	}
	
	public void clickAccountNameLink(WebDriver driver, String searchText) {
	    By accountName = By.xpath(accountNameLink.replace("$$AccountName$$",searchText));
		waitUntilVisible(driver, accountName);
		clickByJs(driver, accountName); 
		waitUntilInvisible(driver, accountName);
	}
	
	public String getSFDCAccountPhone(WebDriver driver) {
		waitUntilVisible(driver, accountPhoneListParent);
		return getElementsText(driver, accountPhoneListParent).trim();
	}
	
	public void clickAccountPhoneLink(WebDriver driver) {	    
		waitUntilVisible(driver, accountPhoneList);		
		clickByJs(driver, accountPhoneList);
	}
	
	public void clickOpportunityNameLink(WebDriver driver, String searchText) {
	    By opportunityName = By.xpath(opportunityNameNameLink.replace("$$OpportunityName$$",searchText));
		waitUntilVisible(driver, opportunityName);
		clickByJs(driver, opportunityName);
		waitForPageLoaded(driver);
	}
}
