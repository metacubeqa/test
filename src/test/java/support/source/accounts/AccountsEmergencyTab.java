package support.source.accounts;

import static org.testng.AssertJUnit.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class AccountsEmergencyTab extends SeleniumBase {

	Dashboard dashBoard = new Dashboard();

	By emergencyTab 		  = By.cssSelector("[data-tab='emergency']");
	By addIcon				  = By.cssSelector(".add-emergency .glyphicon-plus-sign");
	By callFlowPickerInputTab = By.cssSelector(".callflow-picker input");
	By updateCallFlowPickerInput = By.cssSelector(".callflow-picker .has-items div");
	By callFlowDropDown		  = By.cssSelector(".callflow-picker .selectize-dropdown.single.selector .option");
	By locationPickerInputTab = By.cssSelector("#emergency-editor-modal .location-picker input");
	By locationDropDown       = By.cssSelector(".location-picker .selectize-dropdown.multi.selector .option");
	By downloadLink			  = By.cssSelector(".glyphicon-download-alt");
	By saveBtn				  = By.cssSelector(".btn-success.persist");
	By confirmBtn			  = By.xpath("//*[@data-bb-handler='proceed'] | //*[@data-bb-handler='confirm'] | //*[@data-bb-handler='ok']");
	By toastMsg				  = By.cssSelector(".toast-message");
	By deleteRouting 		  = By.cssSelector(".glyphicon-remove-sign");
	By searchBtn			  = By.cssSelector(".btn-success.search");
	
	//search locators
	By locationSearchInputTextBox			= By.cssSelector(".location-picker .selectize-input input");
	By locationSearchDropDownItems			= By.cssSelector(".location-picker .selectize-dropdown .selectize-dropdown-content span");
	By locationSearchInputTextBoxFull		= By.cssSelector(".location-picker .selectize-input.has-items div");
	
	String routingWithCallFlow_Date_location = ".//*[text()='$location$']/ancestor::tr//td[contains(@class,'createdAt') and contains(text(),'$createdDate$')]/..//td/a[contains(@href,'call-flows') and (text()='$callFlow$')]";
	String updateRouting					 = ".//*[text()='$location$']/ancestor::tr//td[contains(@class,'createdAt') and contains(text(),'$createdDate$')]/..//td/a[contains(@href,'call-flows') and (text()='$callFlow$')]/../..//td/button[@data-action='update']";
	String deleteRoutingPage				 = ".//*[text()='$location$']/ancestor::tr//td[contains(@class,'createdAt') and contains(text(),'$createdDate$')]/..//td/a[contains(@href,'call-flows') and (text()='$callFlow$')]/../..//td/a[@class='delete']/span";
	
	By toggleBtn							 = By.xpath(".//*[@class='form-group status-cell']/div[not(contains(@class,'btn-default off'))]");
	By toggleBtnCheckBox					 = By.cssSelector(".toggle.btn.btn-sm.btn-info");
	String toggleOnRouting					 = ".//*[text()='$location$']/ancestor::tr//td[contains(@class,'createdAt') and contains(text(),'$createdDate$')]/..//td/a[contains(@href,'call-flows') and (text()='$callFlow$')]/../..//td//div/label[contains(@class,'toggle-on')]";
	String toggleOffRouting					 = ".//*[text()='$location$']/ancestor::tr//td[contains(@class,'createdAt') and contains(text(),'$createdDate$')]/..//td/a[contains(@href,'call-flows') and (text()='$callFlow$')]/../..//td//div/label[contains(@class,'toggle-off')]";
	
	public void navigateToEmergencyTab(WebDriver driver) {
		waitUntilVisible(driver, emergencyTab);
		clickElement(driver, emergencyTab);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void selectCallFlow(WebDriver driver, String itemSearch) {
		idleWait(1);
		enterTextAndSelectFromDropDown(driver, callFlowPickerInputTab, callFlowPickerInputTab, callFlowDropDown, itemSearch);
	}
	
	public void updateCallFlow(WebDriver driver, String itemSearch) {
		WebElement element = driver
				.findElement(By.cssSelector(".call-flow-picker .selectize-input input:nth-of-type(1)"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
		enterBackspace(driver, callFlowPickerInputTab);
		selectCallFlow(driver, itemSearch);
	}
	
	public void selectLocation(WebDriver driver, String itemSearch) {
		idleWait(1);
		findElement(driver, locationPickerInputTab).clear();
		enterTextAndSelectFromDropDown(driver, locationPickerInputTab, locationPickerInputTab, locationDropDown, itemSearch);
	}

	public void saveEmergencyRouting(WebDriver driver) {
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void clickDownload(WebDriver driver) {
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void clickAddIcon(WebDriver driver) {
		waitUntilVisible(driver, addIcon);
		clickElement(driver, addIcon);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void clickConfirmBtn(WebDriver driver){
		waitUntilVisible(driver, confirmBtn);
		clickElement(driver, confirmBtn);
		waitUntilInvisible(driver, confirmBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void searchLocation(WebDriver driver, String location){
		enterTextAndSelectFromDropDown(driver, locationSearchInputTextBox, locationSearchInputTextBox, locationSearchDropDownItems, location);	
		waitUntilVisible(driver, searchBtn);
		clickElement(driver, searchBtn);
		dashBoard.isPaceBarInvisible(driver);
		idleWait(2);
	}
	
	public void searchUpdatedLocation(WebDriver driver, String location){
		waitUntilVisible(driver, locationSearchInputTextBoxFull);
		clickElement(driver, locationSearchInputTextBoxFull);
		idleWait(1);
		clickElement(driver, locationSearchInputTextBox);
		enterBackspace(driver, locationSearchInputTextBox);
		idleWait(1);
		enterTextAndSelectFromDropDown(driver, locationSearchInputTextBox, locationSearchInputTextBox, locationSearchDropDownItems, location);	
	}
	
	public boolean isRoutingCreated(WebDriver driver, String location, String callFlow, String date) {
		if(Strings.isNullOrEmpty(location))
			location="All";
		By routingLoc = By.xpath(routingWithCallFlow_Date_location.replace("$location$", location).replace("$createdDate$", date).replace("$callFlow$", callFlow));
		return isElementVisible(driver, routingLoc, 5);
	}
	
	public void updateRouting(WebDriver driver, String location, String callFlow, String date) {
		if(Strings.isNullOrEmpty(location))
			location="All";
		By routingLoc = By.xpath(updateRouting.replace("$location$", location).replace("$createdDate$", date).replace("$callFlow$", callFlow));
		waitUntilVisible(driver, routingLoc);
		clickElement(driver, routingLoc);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void deleteRouting(WebDriver driver, String location, String callFlow, String date) {
		if(Strings.isNullOrEmpty(location))
			location="All";
		By routingLoc = By.xpath(deleteRoutingPage.replace("$location$", location).replace("$createdDate$", date).replace("$callFlow$", callFlow));
		waitUntilVisible(driver, routingLoc);
		clickElement(driver, routingLoc);
		clickConfirmBtn(driver);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void downloadTotalRecords(WebDriver driver) {
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void toggleStatusOnRouting(WebDriver driver, String location, String callFlow, String date) {
		if (Strings.isNullOrEmpty(location))
			location = "All";
		By routingLoc = By.xpath(toggleOffRouting.replace("$location$", location).replace("$createdDate$", date)
				.replace("$callFlow$", callFlow));

		if (isElementVisible(driver, toggleBtnCheckBox, 5)) {
			System.out.println("Already enabled status emergency routing");
		} else {
			clickElement(driver, routingLoc);
			clickConfirmBtn(driver);
			waitUntilInvisible(driver, confirmBtn);
			System.out.println("Enabled status emergency routing");
		}
		By deleteRoutingLoc = By.xpath(deleteRoutingPage.replace("$location$", location).replace("$createdDate$", date).replace("$callFlow$", callFlow));
		assertFalse(isElementVisible(driver, deleteRoutingLoc, 5));
	}	
		
	public void toggleStatusRoutingToCheckMsg(WebDriver driver, String location, String callFlow, String date) {
		if (Strings.isNullOrEmpty(location))
			location = "All";
		By routingLoc = By.xpath(toggleOffRouting.replace("$location$", location).replace("$createdDate$", date)
				.replace("$callFlow$", callFlow));
		clickElement(driver, routingLoc);
		clickConfirmBtn(driver);
		waitUntilInvisible(driver, confirmBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
		
	public void toggleStatusOffRouting(WebDriver driver, String location, String callFlow, String date) {
		if (Strings.isNullOrEmpty(location))
			location = "All";
		By routingLoc = By.xpath(toggleOnRouting.replace("$location$", location).replace("$createdDate$", date)
				.replace("$callFlow$", callFlow));

		if (isElementVisible(driver, toggleBtnCheckBox, 5)) {
			clickElement(driver, routingLoc);
			clickConfirmBtn(driver);
			waitUntilInvisible(driver, confirmBtn);
			System.out.println("Disabled status emergency routing");
		} else {
			System.out.println("Already disabled status emergency routing");
		}
	}
	
	public String getToastMsg(WebDriver driver) {
		return getElementsText(driver, toastMsg);
	}
	
	public void deleteAllRouting(WebDriver driver) {
		idleWait(1);
		scrollTillEndOfPage(driver);
		if (isListElementsVisible(driver, deleteRouting, 5)) {
			int i = getElements(driver, deleteRouting).size() - 1;
			while (i >= 0) {
				// delete field
				scrollToElement(driver, getElements(driver, deleteRouting).get(i));
				clickElement(driver, getElements(driver, deleteRouting).get(i));
				clickConfirmBtn(driver);
				waitUntilInvisible(driver, confirmBtn);
				dashBoard.isPaceBarInvisible(driver);
				i--;
			}
		}
	}
	
	public void cleanUpToggleRoutings(WebDriver driver) {
		if (isElementVisible(driver, toggleBtn, 5)) {
			waitUntilVisible(driver, toggleBtn);
			clickElement(driver, toggleBtn);
			clickConfirmBtn(driver);
			dashBoard.isPaceBarInvisible(driver);
		}
	}
}
