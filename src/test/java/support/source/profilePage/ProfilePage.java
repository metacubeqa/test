package support.source.profilePage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.accounts.AccountSalesforceTab;
import support.source.commonpages.Dashboard;

public class ProfilePage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	AccountSalesforceTab salesForceTab = new AccountSalesforceTab();
	
	By welcomeProfileHeader = By.cssSelector(".welcome-to-profiles-modal h4");
	By getStartedButton	  = By.xpath(".//*[@class='welcome-to-profiles-modal']//button[text()='Get Started']");
	By redExclamationList = By.cssSelector(".glyphicon-exclamation-sign.red.alert-tooltip:not([style*='display: none;'])");
	By profileNameList 	  = By.cssSelector("td .profile-name");
	By accountLabel 	  = By.xpath(".//*[@class='salesforce-profile-fields']//label[text()='Account']");
	By profileNameLabel	  = By.xpath(".//*[@class='salesforce-profile-fields']//label[text()='Profile Name']");
	By profileIdLabel 	  = By.xpath(".//*[@class='salesforce-profile-fields']//label[text()='Profile ID']");
	By usersLabel 	  	  = By.xpath(".//*[@class='salesforce-profile-fields']//label[text()='Users']");
	By firstLaunchDialog  = By.xpath("//p[contains(text() , 'This Profile has inherited the field permissions')]");	
	
	By editPencilProfileList    = By.cssSelector(".salesforce-profile-search .glyphicon-pencil.edit-profile");
	By additionalFieldHeader    = By.xpath(".//*[@class='modal-content']//h4[contains(text(),'Add Additional')]");
	By accountFields            = By.cssSelector(".sfdc-field-tabs .btn-default.accountFields");
	By contactFields            = By.cssSelector(".btn.btn-default.contactFields");
	By fieldsAddIcon	        = By.cssSelector(".sfdc-tabs .add .glyphicon-plus-sign");
	By disabledFieldAddIcon     = By.cssSelector(".glyphicon.glyphicon-plus-sign.muted");
	By closeSpan		        = By.cssSelector(".close span");
	By fieldSaveButton          = By.cssSelector(".ladda-button.btn.btn-success.save");
	By fieldSaveBtnAdding 		= By.cssSelector(".ladda-button.btn.btn-success.persist");
	
	By allCheckBoxesList        	= By.xpath("//th[@class='renderable']/input[@type= 'checkbox']");
	By allViewCheckbox		    	= By.cssSelector("#add-sfdc-setting-visible");
	By salesForceFieldsTextBox 		= By.cssSelector(".selectize-control.available-fields.single .has-options input");
	By salesForceFieldsTextBoxItems	= By.cssSelector(".selectize-dropdown.available-fields .selectize-dropdown-content .option");
	By salesForceNameTab			= By.cssSelector(".form-control.name");
	By alwaysVisibleChckBx_InAdding	= By.cssSelector("input#add-sfdc-setting-visible");
	By editableChckBx_InAdding		= By.cssSelector("input#add-sfdc-setting-editable");
	By requiredChckBx_InAdding		= By.cssSelector("input#add-sfdc-setting-required");
	
	By accountTextBoxSelectize  = By.cssSelector(".account-picker div.selectize-input.items");
	By accountTextBoxInput      = By.cssSelector(".account-picker div.selectize-input.items input");
	By accountTextBoxOptions    = By.cssSelector(".account-picker .selectize-dropdown-content div[data-selectable]");
	By profileSearchBtn			= By.cssSelector(".salesforce-profile-search .btn.search");
	
	By profileNameTextBox       = By.cssSelector(".profile-name.form-control");
	By profileIdTextBox         = By.cssSelector(".profile-id.form-control");
	
	By manageAccountFields      = By.xpath("//td[contains(@class, 'salesforceField.name') or contains(@class, 'salesforceField.sfdcName')] ");
	By overviewFields           = By.cssSelector(".form-group > label");
	
	String editProfileIcon		= ".//*[@class='profile-name' and text()='$profileName$']/parent::td/parent::tr//span[contains(@class,'edit-profile')]";

	//profile
	By profileDropDown          = By.cssSelector(".form-control.profile");
	
	//fields locators
	By campaignFields			= By.cssSelector(".btn.btn-default.campaignFields");
	By caseFields				= By.cssSelector(".btn.btn-default.caseFields");
	
	By sfAddedFieldsNameList 	= By.cssSelector(".salesforce-fields-backgrid-container tbody tr td:nth-child(2) ");    
	By notVisibleCheckBoxList 	= By.xpath("//td//span[contains(@class, 'glyphicon-remove-sign')]/parent::a/../parent::tr//td[contains(@class, 'updatedAt')]/following-sibling::td[2]//input");
	
	String profileNameCell		= ".//*[@class='profile-name' and text()='$profileName$']";
	String updateField			= ".//*[text()='$FieldName$']/ancestor::tr//*[@class='glyphicon glyphicon-pencil edit-profile']";
	String selectAccountById    = "//div[@data-value='%text%']";
	String selectViewCheckBoxByName = "//a[text()='%name%']/parent::td/following-sibling::td/div//input";
	
	//Manage Profiles Locators
	String deleteField			= ".//*[text()='$FieldName$']/ancestor::tr//span[@class='glyphicon glyphicon-remove-sign']";

	By profileNameTitle 		= By.cssSelector(".profile-name-title");
	By contactFieldsBtn			= By.cssSelector(".btn-default.contactFields");
	By leadFieldsBtn			= By.cssSelector(".btn-default.leadFields");
	By accountFieldsBtn			= By.cssSelector(".btn-default.accountFields");
	By opportunityFieldsBtn		= By.cssSelector(".btn-default.opportunityFields");
	By caseFieldsBtn			= By.cssSelector(".btn-default.caseFields");
	
	By arrowDownIconList		= By.cssSelector("div.order-button:not([disabled]) span[data-action='arrow-down']");
	By customNameList 			= By.xpath("//td//span[contains(@class, 'glyphicon-remove-sign')]/parent::a/../parent::tr//td[contains(@class,'salesforceField.name')]");
	
	// customer data
	By customerEmail            = By.cssSelector(".email");
	By customerSearch           = By.cssSelector(".search");
	By noRecordFound            = By.cssSelector(".empty span");
	
	By emailDataList            = By.cssSelector(".renderable.email");
	
	
	public void verifyWelcomeProfileHeaderMessage(WebDriver driver){
		waitUntilVisible(driver, welcomeProfileHeader);
		assertEquals(getElementsText(driver, welcomeProfileHeader).trim(),"Welcome to Profiles!");
	}
	
	public boolean isWelcomeProfilesSectionVisible(WebDriver driver) {
		return isElementVisible(driver, getStartedButton, 5);
	}
	
	public void clickGetStartedButton(WebDriver driver){
		waitUntilVisible(driver, getStartedButton);
		clickElement(driver, getStartedButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isRedExclamationListVisible(WebDriver driver){
		return isListElementsVisible(driver, redExclamationList, 5);
	}
	
	public boolean isProfileNameListVisible(WebDriver driver){
		return isListElementsVisible(driver, profileNameList, 5);
	}
	
	public void clickPencilEditIcon(WebDriver driver){
		isListElementsVisible(driver, editPencilProfileList, 5);
		clickElement(driver, getElements(driver, editPencilProfileList).get(1));
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**method to click edit icon according to profile
	 * @param driver
	 * @param profileName
	 */
	public void clickPencilIconProfile(WebDriver driver, String profileName){
		isListElementsVisible(driver, editPencilProfileList, 5);
		By profileLoc = By.xpath(editProfileIcon.replace("$profileName$", profileName));
		waitUntilVisible(driver, profileLoc);
		clickElement(driver, profileLoc);
		dashboard.isPaceBarInvisible(driver);
		assertTrue(getElementsText(driver, profileNameTitle).equals(profileName));
	}
	
	/**click contact fields
	 * @param driver
	 */
	public void clickContactFields(WebDriver driver) {
		waitUntilVisible(driver, contactFieldsBtn);
		clickElement(driver, contactFieldsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	

	/**click lead fields
	 * @param driver
	 */
	public void clickLeadFields(WebDriver driver) {
		waitUntilVisible(driver, leadFieldsBtn);
		clickElement(driver, leadFieldsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**click account fields
	 * @param driver
	 */
	public void clickAccountFields(WebDriver driver) {
		waitUntilVisible(driver, accountFieldsBtn);
		clickElement(driver, accountFieldsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**click Opportunity fields
	 * @param driver
	 */
	public void clickOpportunityFields(WebDriver driver) {
		waitUntilVisible(driver, opportunityFieldsBtn);
		clickElement(driver, opportunityFieldsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**click Case fields
	 * @param driver
	 */
	public void clickCaseFields(WebDriver driver) {
		waitUntilVisible(driver, caseFieldsBtn);
		clickElement(driver, caseFieldsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**click arrow down icons upto the index provided
	 * @param driver
	 * @param index
	 */
	public void clickArrowDownIcons(WebDriver driver, int index) {
		isListElementsVisible(driver, arrowDownIconList, 5);
		for (int i = 0; i < index; i++) {
			clickByJs(driver, getElements(driver, arrowDownIconList).get(i));
			dashboard.isPaceBarInvisible(driver);
		}
		clickFieldsSaveButton(driver);
	}
	
	/**
	 * @param driver
	 * @param index
	 */
	public void clickArrowDownIcon(WebDriver driver) {
	isListElementsVisible(driver, arrowDownIconList, 5);
	clickByJs(driver, getElements(driver, arrowDownIconList).get(0));
	dashboard.isPaceBarInvisible(driver);
	}
	
	/**get custom names list
	 * @param driver
	 * @return
	 */
	public List<String> getCustomNameList(WebDriver driver) {
		isListElementsVisible(driver, customNameList, 5);
		return getTextListFromElements(driver, customNameList);
	}

	/**
	 * @param driver
	 * @param fieldName
	 */
	public void clickUpddateBtn(WebDriver driver, String fieldName) {
		By locator = By.xpath(updateField.replace("$FieldName$", fieldName));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	public boolean isAccountFieldSectionDefault(WebDriver driver) {
		if (getAttribue(driver, accountFields, ElementAttributes.Class).contains("active btn-primary")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void navigateToContactFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, contactFields);
		clickElement(driver, contactFields);
		dashboard.isPaceBarInvisible(driver);
	}

	// This method is to select account for search
	public void enterAccountAndSearch(WebDriver driver, String accountName) {
		if (isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			clickElement(driver, accountTextBoxOptions);
		}
		clickElement(driver, profileSearchBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	// This method is to select account for search
	public void enterAccountWithIdAndSearch(WebDriver driver, String accountName, String id) {
		if (isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			By locator = By.xpath(selectAccountById.replace("%text%", id));
			clickElement(driver, locator);
		}
		clickElement(driver, profileSearchBtn);
		dashboard.isPaceBarInvisible(driver);
	}

	public boolean isProfileNameVisible(WebDriver driver, String profileName) {
		By profileNameLoc = By.xpath(profileNameCell.replace("$profileName$", profileName));
		return isElementVisible(driver, profileNameLoc, 5);
	}

	public void clickAddFieldIcon(WebDriver driver){
		waitUntilVisible(driver, fieldsAddIcon);
		clickElement(driver, fieldsAddIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, additionalFieldHeader);
	}
	
	public boolean isAddFieldIconDisabled(WebDriver driver){
		return isElementVisible(driver, disabledFieldAddIcon, 6);
	}
	
	public void closeField(WebDriver driver){
		waitUntilVisible(driver, closeSpan);
		clickElement(driver, closeSpan);
	}
	
	public void checkViewCheckBox(WebDriver driver){
		waitUntilVisible(driver, allViewCheckbox);
		checkCheckBox(driver, allViewCheckbox);
	}
	
	public void isAllCheckBoxesVisible(WebDriver driver){
		List<WebElement> checkBox = getElements(driver, allCheckBoxesList);
		for(WebElement element : checkBox) {
			assertTrue(isElementVisible(driver, element, 6));
		}
	}
	
	public void isAllManageAccountFieldsVisible(WebDriver driver){
		List<WebElement> checkBox = getElements(driver, manageAccountFields);
		for(WebElement element : checkBox) {
			assertTrue(isElementVisible(driver, element, 6));
		}
	}
	
	/**
	 * @param driver
	 * overview fields visible
	 */
	public void isAllOverviewAccountFieldsVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, accountLabel, 6));
		assertTrue(isElementVisible(driver, profileNameLabel, 6));
		assertTrue(isElementVisible(driver, profileIdLabel, 6));
		assertTrue(isElementVisible(driver, usersLabel, 6));
	}
	
	public void isOverviewFieldsVisible(WebDriver driver){
		List<WebElement> checkBox = getElements(driver, overviewFields);
		for(WebElement element : checkBox) {
			assertTrue(isElementVisible(driver, element, 6));
		}
	}
	
	public void clickFieldsSaveButton_InAdding(WebDriver driver) {
		waitUntilVisible(driver, fieldSaveBtnAdding);
		clickElement(driver, fieldSaveBtnAdding);
		waitUntilInvisible(driver, fieldSaveBtnAdding);
		dashboard.isPaceBarInvisible(driver);
		salesForceTab.isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void clickFieldsSaveButton(WebDriver driver) {
		scrollToElement(driver, fieldSaveButton);
		waitUntilVisible(driver, fieldSaveButton);
		clickElement(driver, fieldSaveButton);
		dashboard.isPaceBarInvisible(driver);
		salesForceTab.isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void enterAdditionalFieldName(WebDriver driver, String fieldName) {
		waitUntilVisible(driver, salesForceNameTab);
		clearAll(driver, salesForceNameTab);
		enterText(driver, salesForceNameTab, fieldName);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param fieldName
	 * @return
	 */
	public boolean isDeleteIconVisible(WebDriver driver, String fieldName){
		By locator = By.xpath(deleteField.replace("$FieldName$", fieldName));
		return isElementVisible(driver, locator, 5);
	}
	
	/**
	 * @param driver
	 * @param field
	 * @param accountFieldName
	 * @param isVisible
	 * @param isEditable
	 * @param isRequired
	 * @param name
	 */
	public void createAccountSalesForceField(WebDriver driver, Enum<?> field, String accountFieldName, boolean isVisible, boolean isEditable, boolean isRequired) {
		clickAddFieldIcon(driver);
		selectSalesForceField(driver, field);
		enterAdditionalFieldName(driver, accountFieldName);
		if (isVisible) {
			checkAlwaysVisibleCheckBox_InAdding(driver);
		}
			
		if(isEditable)
			checkEditableCheckBox_InAdding(driver);
		
		if(isRequired)
			checkRequiredCheckBox_InAdding(driver);
		
		clickFieldsSaveButton_InAdding(driver);
	}
	
	/**
	 * @param driver
	 */
	private void checkRequiredCheckBox_InAdding(WebDriver driver) {
		waitUntilVisible(driver, requiredChckBx_InAdding);
		checkCheckBox(driver, requiredChckBx_InAdding);
		
	}

	/**
	 * @param driver
	 */
	private void checkEditableCheckBox_InAdding(WebDriver driver) {
		waitUntilVisible(driver, editableChckBx_InAdding);
		checkCheckBox(driver, editableChckBx_InAdding);
	}

	/**
	 * @param driver
	 */
	public void checkAlwaysVisibleCheckBox_InAdding(WebDriver driver){
		waitUntilVisible(driver, alwaysVisibleChckBx_InAdding);
		checkCheckBox(driver, alwaysVisibleChckBx_InAdding);
	}
	
	/**
	 * @param driver
	 * @param field
	 */
	public void selectSalesForceField(WebDriver driver, Enum<?> field) {
		waitUntilVisible(driver, salesForceFieldsTextBox);
		enterText(driver, salesForceFieldsTextBox, field.toString());
		idleWait(2);
		for(WebElement element: getElements(driver, salesForceFieldsTextBoxItems)){
			if(getAttribue(driver, element, ElementAttributes.dataValue).contains(field.toString())){
				clickElement(driver, element);
				break;
			}
		}
		
	}

	public boolean isProfileNameDisabled(WebDriver driver){
		return isElementDisabled(driver, profileNameTextBox, 6);
	}
	
	public boolean isProfileIdDisabled(WebDriver driver){
		return isElementDisabled(driver, profileIdTextBox, 6);
	}
	
	public String getProfileValueFromDropDown(WebDriver driver) {
		waitUntilVisible(driver, profileDropDown);
		String value = getAttribue(driver, profileDropDown, ElementAttributes.value);
		return value;
	}
	
	/**
	 * @param driver
	 * @Desc : navigate To Campaign Fields Section
	 */
	public void navigateToCampaignFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, campaignFields);
		clickElement(driver, campaignFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @Desc : navigate To Case Fields Section
	 */
	public void navigateToCaseFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, caseFields);
		clickElement(driver, caseFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param sfFieldName
	 * @return get index of salesforce field
	 */
	public int getAddedSfFieldIndex(WebDriver driver, String sfFieldName) {
		return getTextListFromElements(driver, sfAddedFieldsNameList).indexOf(sfFieldName);
	}

	/**method to return Not visible custom fields
	 * @param driver
	 */
	public List<String> getNotVisibleCustomFields(WebDriver driver) {

		List<String> textList = new ArrayList<String>();

		for (WebElement notVisibleCheckBox : getElements(driver, notVisibleCheckBoxList)) {
			if (!notVisibleCheckBox.isSelected()) {
				String text = notVisibleCheckBox
						.findElement(By.xpath("../parent::tr//td[contains(@class,'salesforceField.name')]")).getText()
						.trim();
				textList.add(text);
			}
		}
		return textList;
	}
	
	// customer data page methods
	
	/**
	 * @param driver
	 */
	public void enterCustomerEmail(WebDriver driver, String text) {
		waitUntilVisible(driver, customerEmail);
		enterText(driver, customerEmail, text);
	}
	
	/**
	 * @param driver
	 * click search button
	 */
	public void clickCustomerSearch(WebDriver driver) {
		waitUntilVisible(driver, customerSearch);
		clickElement(driver, customerSearch);
	}
	
	/**
	 * @param driver
	 */
	public void verifyRecordsFound(WebDriver driver, String email) {
		assertTrue(isElementVisible(driver, noRecordFound, 6) || checkEmailDataFromList(driver, email));
	}
	
	/**
	 * @param driver
	 * verify first launch dialog tooltip
	 */
	public void verifyfirstLaunchDialogTooltip(WebDriver driver) {
		if (isElementPresent(driver, firstLaunchDialog, 10)) {
			assertTrue(getElementsText(driver, firstLaunchDialog).contains("This Profile has inherited the field permissions previously defined on the Salesforce Field tabs in the"));
		}
	}
	
	/**
	 * @param driver
	 * @param email
	 * @return email verify
	 */
	public boolean checkEmailDataFromList(WebDriver driver, String email) {
		waitUntilVisible(driver, emailDataList);
		return getTextListFromElements(driver, emailDataList).get(1).contains(email);
		
	}

}