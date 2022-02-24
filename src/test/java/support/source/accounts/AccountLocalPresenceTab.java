package support.source.accounts;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class AccountLocalPresenceTab extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	By localPresenceTab		 		= By.cssSelector("[data-tab='local-presence']");
	By exportButton 				= By.cssSelector(".export");
	By localPresenceCheckBox 		= By.className("localPresence");
	By localPresenceToggleOffButton = By.xpath("//*[contains(@class,'localPresence')]/..//label[contains(@class,'toggle-off')]");
	By localPresenceToggleOnButton 	= By.xpath("//*[contains(@class,'localPresence')]/..//label[contains(@class,'toggle-on')]");
	By useAreasCheckBox 			= By.className("localPresenceAreas");
	By useAreasToggleButton 		= By.xpath("//*[contains(@class,'localPresenceAreas')]/..//label[contains(@class,'toggle-off')]");
	By userVerifiedNumberCheckBox 	= By.className("localPresenceVerifiedNumbers");
	By UserVerifiedNumberOffToggleButton	= By.xpath("//*[contains(@class,'localPresenceVerifiedNumbers')]/..//label[contains(@class,'toggle-off')]");
	By UserVerifiedNumberOnToggleButton	= By.xpath("//*[contains(@class,'localPresenceVerifiedNumbers')]/..//label[contains(@class,'toggle-on')]");
	By saveLocalPresenceSettings 	= By.cssSelector(".saveLocalPresenceSettings");
	By saveLocalPresenceSettingsMessage = By.className("toast-message");
	
	By manuallyAddNumber			= By.cssSelector(".addNumber");
	By deleteNumbers				= By.cssSelector(".delete-numbers");
	
	//Provision North American selectors
	By provisionLPNumberIcon		= By.cssSelector(".provisionNA");
	String areaCodeProvision		= ".//*[contains(text(),'$areaCode$')]/parent::tr//td[contains(@class,'areaCodesDesc')]";
	
	//Provision International number selectors
	By countrySelect = By.cssSelector(".countries.form-control");
	By enterPrefixInputTxtBox = By.xpath("//label[text()='Please enter prefix for number:']/..//input");
	By findNumbersBtn = By.cssSelector(".find");
	By numberList = By.cssSelector("td.number");
	By checkNumberBoxList = By.cssSelector(".available-numbers-list td input");
	By provisionNumbersBtn = By.cssSelector(".provisionIntl");
	
	
	public void openlocalPresenceTab(WebDriver driver){
		waitUntilVisible(driver, localPresenceTab);
		clickElement(driver, localPresenceTab);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickDeleteNumbersBtn(WebDriver driver){
		waitUntilVisible(driver, deleteNumbers);
		clickElement(driver, deleteNumbers);
	}
	
	public void deleteNumbers(WebDriver driver){
		clickDeleteNumbersBtn(driver);
		waitUntilVisible(driver, saveLocalPresenceSettingsMessage);
		assertEquals(getLocalPresenceSaveMessage(driver), "Deletion is done");
		IsLocalPresenceSaveMessageDisappeared(driver);
	}
	
	public void enableLocalPresenceSetting(WebDriver driver){
		if (!findElement(driver, localPresenceCheckBox).isSelected()) {
			clickElement(driver, localPresenceToggleOffButton);
			System.out.println("Enabled local presence settings");
		} else {
			System.out.println("Local presence alrady enabled");
		}
	}
	
	public void disableLocalPresenceSetting(WebDriver driver){
		if (findElement(driver, localPresenceCheckBox).isSelected()) {
			clickElement(driver, localPresenceToggleOnButton);
			System.out.println("disabled local presence settings");
		} else {
			System.out.println("Local presence alrady disabled");
		}
	}
	
	public void enableUseAreasSetting(WebDriver driver){
		if(!findElement(driver, useAreasCheckBox).isSelected()) {
			clickElement(driver, useAreasToggleButton);
			System.out.println("Enabled user area  settings");
		} else {
			System.out.println("User Area Already Enabled");
		}
	}
	
	public void enableUserVerifiedNumberSetting(WebDriver driver){
		if(!findElement(driver, userVerifiedNumberCheckBox).isSelected()) {
			clickElement(driver, UserVerifiedNumberOffToggleButton);
			System.out.println("Enabled user verified Number settings");
		} else {
			System.out.println("User verified Number setting Already Enabled");
		}
	}
	
	public void disableUserVerifiedNumberSetting(WebDriver driver){
		if(findElement(driver, userVerifiedNumberCheckBox).isSelected()) {
			clickElement(driver, UserVerifiedNumberOnToggleButton);
			System.out.println("disabled user verified Number settings");
		} else {
			System.out.println("User verified Number setting is already disabled");
		}
	}
	
	
	public String getLocalPresenceSaveMessage(WebDriver driver){
		return getElementsText(driver, saveLocalPresenceSettingsMessage);
	}
	
	public void IsLocalPresenceSaveMessageDisappeared(WebDriver driver){
		waitUntilInvisible(driver, saveLocalPresenceSettingsMessage);
	}
	
	public void saveLocalPresenceSettings(WebDriver driver){
		scrollToElement(driver, saveLocalPresenceSettings);
		clickElement(driver, saveLocalPresenceSettings);
		getLocalPresenceSaveMessage(driver);
		IsLocalPresenceSaveMessageDisappeared(driver);		
	}
	
	public void setDefaultLocalPresenceSettings(WebDriver driver){
		openlocalPresenceTab(driver);
		enableLocalPresenceSetting(driver);
		//enableUseAreasSetting(driver);
		saveLocalPresenceSettings(driver);
	}
	
	public void clickManuallyAddNumber(WebDriver driver) {
		waitUntilVisible(driver, manuallyAddNumber);
		clickElement(driver, manuallyAddNumber);
	}
	
	//Provision North American section
	
	public void clickProvisionLPNumberIcon(WebDriver driver) {
		waitUntilVisible(driver, provisionLPNumberIcon);
		clickElement(driver, provisionLPNumberIcon);
		IsLocalPresenceSaveMessageDisappeared(driver);
		waitForElementEnabled(driver, provisionLPNumberIcon);
		idleWait(5);
	}
	
	public boolean isAreaCodeProvisioned(WebDriver driver, String areaCode) {
		By areaCodeProvisionLoc = By.xpath(areaCodeProvision.replace("$areaCode$", areaCode));
		return getElementsText(driver, areaCodeProvisionLoc).contains("provided");
	}
	
	//Provision International Number section
	
	public String provisionInternationalNumber(WebDriver driver, String country, String prefix, int index) {
		selectCountry(driver, country);
		enterPrefix(driver, prefix);
		clickFindNumbersBtn(driver);
		String number = getInternationalNumber(driver, index);
		selectInternationalNoCheckBox(driver, index);
		clickProvisionNumbersBtn(driver);
		return number;
	}
	
	public void selectCountry(WebDriver driver, String country) {
		waitUntilVisible(driver, countrySelect);
		selectFromDropdown(driver, countrySelect, SelectTypes.visibleText, country);
		
	}
	
	public void enterPrefix(WebDriver driver, String prefix) {
		waitUntilVisible(driver, enterPrefixInputTxtBox);
		enterText(driver, enterPrefixInputTxtBox, prefix);
	}
		
	public void clickFindNumbersBtn(WebDriver driver) {
		waitUntilVisible(driver, findNumbersBtn);
		clickElement(driver, findNumbersBtn);
		dashboard.isPaceBarInvisible(driver);
	}

	public String getInternationalNumber(WebDriver driver, int index) {
		return getInactiveElements(driver, numberList).get(0).getText();
	}
		

	public void selectInternationalNoCheckBox(WebDriver driver, int index) {
		clickElement(driver, getInactiveElements(driver, checkNumberBoxList).get(0));
	}

	public void clickProvisionNumbersBtn(WebDriver driver) {
		waitUntilVisible(driver, provisionNumbersBtn);
		clickElement(driver, provisionNumbersBtn);
		IsLocalPresenceSaveMessageDisappeared(driver);
		dashboard.isPaceBarInvisible(driver);
	}
}
